// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package com.amalto.workbench.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLPeerUnverifiedException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.amalto.workbench.i18n.Messages;

/**
 * created by HHB on 2013-6-27 This tool class wrap DefaultHttpClient to provide some methods for other class,for
 * example uploading,downloading,wrap ssl, and so on.
 * 
 */
public class HttpClientUtil {

    private static final String DEFAULT_CHARSET = "UTF-8"; //$NON-NLS-1$

    private static Log log = LogFactory.getLog(HttpClientUtil.class);

    private static final String STRING_CONTENT_TYPE = "text/plain"; //$NON-NLS-1$

    private static final int CONNECT_TIMEOUT = 6000000;

    private static final int SOCKET_TIMEOUT = 6000000;

    private static final ClientConnectionManager cm = new ThreadSafeClientConnManager();

    private static DefaultHttpClient createClient() {
        HttpParams params = new BasicHttpParams();
        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECT_TIMEOUT);
        params.setParameter(CoreConnectionPNames.SO_TIMEOUT, SOCKET_TIMEOUT);
        params.setParameter(CoreConnectionPNames.TCP_NODELAY, false);
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        return new DefaultHttpClient(cm, params);
    }

    private static DefaultHttpClient wrapAuthClient(String url, String username, String password) throws SecurityException {
        DefaultHttpClient client = createClient();
        URI uri = URI.create(url);
        if ("https".equals(uri.getScheme())) { //$NON-NLS-1$
            client = enableSSL(client, uri.getPort());
        }
        AuthScope authScope = new AuthScope(uri.getHost(), uri.getPort());
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
        client.getCredentialsProvider().setCredentials(authScope, credentials);
        return client;
    }

    public static String getStringContentByHttpget(String url) throws XtentisException {
        return commonGetRequest(url, String.class);
    }

    public static String getStringContentByHttpget(String url, String username, String password) throws XtentisException {
        DefaultHttpClient client = wrapAuthClient(url, username, password);
        return commonGetRequest(client, url, "", String.class); //$NON-NLS-1$
    }

    public static byte[] getByteArrayContentByHttpget(String url) throws XtentisException {
        return commonGetRequest(url, byte[].class);
    }

    public static InputStream getInstreamContentByHttpget(String url) throws XtentisException {
        DefaultHttpClient client = createClient();
        HttpGet get = new HttpGet(url);
        return getResponseContentStream(client, get, ""); //$NON-NLS-1$
    }

    private static <T> T commonGetRequest(String url, Class<T> t) throws XtentisException {
        DefaultHttpClient client = createClient();
        return commonGetRequest(client, url, "", t); //$NON-NLS-1$
    }

    private static <T> T commonGetRequest(DefaultHttpClient client, String url, String message, Class<T> t)
            throws XtentisException {
        HttpGet get = new HttpGet(url);
        return getResponseContent(client, get, message, t);
    }

    private static HttpUriRequest createUploadRequest(String URL, String localFilename, String filename, String imageCatalog,
            HashMap<String, String> picturePathMap) {
        HttpPost request = new HttpPost(URL);
        MultipartEntity entity = new MultipartEntity();
        if (!Messages.Util_24.equalsIgnoreCase(localFilename)) {
            File file = new File(localFilename);
            if (file.exists()) {
                entity.addPart("imageFile", new FileBody(file)); //$NON-NLS-1$
            }
        }
        if (imageCatalog != null) {
            entity.addPart("catalogName", StringBody.create(imageCatalog, STRING_CONTENT_TYPE, null)); //$NON-NLS-1$
        }
        if (filename != null) {
            int pos = filename.lastIndexOf('.');
            if (pos != -1) {
                filename = filename.substring(0, pos);
            }
            entity.addPart("fileName", StringBody.create(filename, STRING_CONTENT_TYPE, null)); //$NON-NLS-1$
        }
        request.setEntity(entity);
        return request;
    }

    public static String uploadFileToAppServer(String URL, String localFilename, String username, String password)
            throws XtentisException {
        HttpUriRequest request = createUploadFileToServerRequest(URL, localFilename);
        DefaultHttpClient client = wrapAuthClient(URL, username, password);
        String errMessage = Messages.Util_21 + "%s" + Messages.Util_22 + "%s"; //$NON-NLS-1$//$NON-NLS-2$
        String content = getTextContent(client, request, errMessage);
        if (null == content) {
            throw new XtentisException("no response content"); //$NON-NLS-1$
        }
        return content;
    }

    private static HttpUriRequest createUploadFileToServerRequest(String URL, final String fileName) {
        HttpPost request = new HttpPost(URL);
        String path = fileName;
        if (URL.indexOf("deletefile") == -1) {//$NON-NLS-1$
            if (URL.indexOf("deployjob") != -1) {//$NON-NLS-1$
                path = URL.substring(URL.indexOf("=") + 1);//$NON-NLS-1$
            }
            MultipartEntity entity = new MultipartEntity();
            entity.addPart(path, new FileBody(new File(fileName)));
            request.setEntity(entity);
        }
        return request;
    }

    public static byte[] getResourceFile(String uri) throws XtentisException {
        HttpUriRequest request = new HttpGet(uri);
        DefaultHttpClient client = wrapAuthClient(uri, "admin", "talend"); //$NON-NLS-1$ //$NON-NLS-2$
        byte[] data = getByteArrayStreamContent(client, request, "{}"); //$NON-NLS-1$
        if (null == data) {
            throw new XtentisException("no response data"); //$NON-NLS-1$
        }
        return data;
    }

    @SuppressWarnings("unused")
    private static <T> T getResponseContent(DefaultHttpClient client, HttpUriRequest request, ResponseHandler<T> handler)
            throws Exception {
        if (null == request) {
            throw new IllegalArgumentException("null request"); //$NON-NLS-1$
        }
        return client.execute(request, handler);
    }

    private static <T> T wrapResponse(HttpResponse response, String message, Class<T> clz) throws XtentisException,
            IllegalStateException, IOException {
        HttpEntity content = response.getEntity();
        switch (response.getStatusLine().getStatusCode()) {
        case HttpStatus.SC_OK:
        case HttpStatus.SC_NO_CONTENT:
            break;

        case HttpStatus.SC_NOT_FOUND:
            throw new XtentisException(Messages.httpclientError_url);
        case HttpStatus.SC_UNAUTHORIZED:
            throw new XtentisException(Messages.httpclientError_principal);
        default:
            throw new XtentisException(String.format(message, response.getStatusLine().getStatusCode(), response.getStatusLine()
                    .getReasonPhrase()));
        }
        if (null != clz && content != null && content.isStreaming()) {
            if (clz.equals(byte[].class)) {
                InputStream instream = content.getContent();
                try {
                    return (T) IOUtils.toByteArray(instream);
                } finally {
                    IOUtils.closeQuietly(instream);
                }
            }
            if (clz.equals(String.class)) {
                return (T) EntityUtils.toString(content, DEFAULT_CHARSET);
            }
        }
        return null;
    }

    private static String getTextContent(DefaultHttpClient client, HttpUriRequest request, String message)
            throws XtentisException {
        return getResponseContent(client, request, message, String.class);
    }

    private static byte[] getByteArrayStreamContent(DefaultHttpClient client, HttpUriRequest request, String message)
            throws XtentisException {
        return getResponseContent(client, request, message, byte[].class);
    }

    private static InputStream getResponseContentStream(DefaultHttpClient client, HttpUriRequest request, String message)
            throws XtentisException {
        if (null == request) {
            throw new IllegalArgumentException("null request"); //$NON-NLS-1$
        }
        HttpResponse response = null;
        try {
            response = client.execute(request);
            HttpEntity content = response.getEntity();
            if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
                if (null != message) {
                    throw new XtentisException(String.format(message, response.getStatusLine().getStatusCode(), response
                            .getStatusLine().getReasonPhrase()));
                }
            }
            return content.getContent();
        } catch (XtentisException ex) {
            closeResponse(response);
            throw ex;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            request.abort();
            closeResponse(response);
            throw new XtentisException(e.getMessage(), e);
        }
    }

    private static void closeResponse(HttpResponse response) {
        if (response != null) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                try {
                    EntityUtils.consume(entity);
                } catch (final IOException ex) {
                    log.error(ex.getMessage(), ex);
                }
            }
        }
    }

    private static <T> T getResponseContent(DefaultHttpClient client, HttpUriRequest request, String message, Class<T> clz)
            throws XtentisException {
        if (null == request) {
            throw new IllegalArgumentException("null request"); //$NON-NLS-1$
        }
        HttpResponse response = null;
        try {
            response = client.execute(request);
            return wrapResponse(response, message, clz);
        } catch (XtentisException ex) {
            throw ex;
        } catch (SSLPeerUnverifiedException e) {
            log.error(Messages.httpclientError_certification);
            request.abort();
            throw new XtentisException(Messages.httpclientError_certification, e);
        } catch (SSLException e) {
            // hostname unmatch
            log.error(e.getMessage(), e);
            request.abort();
            throw new XtentisException(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            request.abort();
            throw new XtentisException(e.getMessage(), e);
        } finally {
            closeResponse(response);
        }
    }

    public static byte[] downloadFile(String url, String userName, String password) throws IOException {
        HttpUriRequest request = new HttpGet(url);
        try {
            DefaultHttpClient client = wrapAuthClient(url, userName, password);
            byte[] data = getByteArrayStreamContent(client, request, null);
            if (null == data) {
                throw new IOException("no response content"); //$NON-NLS-1$
            }
            return data;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public static String uploadImageFile(String URL, String localFilename, String filename, String imageCatalog, String username,
            String password, HashMap<String, String> picturePathMap) throws XtentisException {
        HttpUriRequest request = createUploadRequest(URL, localFilename, filename, imageCatalog, picturePathMap);
        DefaultHttpClient client = wrapAuthClient(URL, username, password);
        String errMessage = Messages.Util_25 + "%s" + Messages.Util_26 + "%s"; //$NON-NLS-1$//$NON-NLS-2$
        String content = getTextContent(client, request, errMessage);
        if (null == content) {
            throw new XtentisException("no response content"); //$NON-NLS-1$
        }
        if (content.contains("upload")) {//$NON-NLS-1$
            String returnValue = content.substring(content.indexOf("upload"), content.indexOf("}") - 1);//$NON-NLS-1$//$NON-NLS-2$
            if (picturePathMap != null) {
                File file = new File(localFilename);
                String fileName1 = file.getName();
                picturePathMap.put(fileName1, returnValue);
            }
            return returnValue;
        } else {
            return ""; //$NON-NLS-1$
        }
    }

    public static String invokeModelService(String protocol, String host, String port, String username, String password,
            String modelName, String xsd, boolean isUpdate, Boolean force) throws XtentisException {
        try {
            String url = protocol + host + ":" + port + "/datamanager/services/system/models/" + modelName; //$NON-NLS-1$ //$NON-NLS-2$ 
            if (force != null) {
                url += "?force=" + force.toString(); //$NON-NLS-1$
            }
            HttpUriRequest request = null;
            request = createModelRequest(url, isUpdate, xsd);
            DefaultHttpClient httpClient = wrapAuthClient(url, username, password);
            String errMessage = Messages.Util_21 + "%s" + Messages.Util_22 + "%s"; //$NON-NLS-1$//$NON-NLS-2$
            String content = getTextContent(httpClient, request, errMessage);

            return content;
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        } catch (SecurityException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    private static HttpUriRequest createModelRequest(String url, boolean isUpdate, String content)
            throws UnsupportedEncodingException {

        HttpEntityEnclosingRequestBase request = null;
        if (!isUpdate) {
            request = new HttpPost(url);
        } else {
            request = new HttpPut(url);
        }

        StringEntity entity = new StringEntity(content, DEFAULT_CHARSET);
        request.setEntity(entity);
        return request;

    }

    public static String invokeMatchSimulation(String protocol, String host, int port, String userName, String password,
            String modelName, String entityName, String records) throws XtentisException {
        String url = protocol + host + ":" + port + "/datamanager/services/tasks/matching/explain/?model=" + modelName + "&type=" //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
                + entityName;
        String contentType = "application/xml;charset=UTF-8"; //$NON-NLS-1$
        try {
            HttpPost request = new HttpPost(url);
            request.setHeader(HTTP.CONTENT_TYPE, contentType);

            StringEntity entity = new StringEntity(records, HTTP.UTF_8);
            request.setEntity(entity);

            DefaultHttpClient client = wrapAuthClient(url, userName, password);
            String errMessage = Messages.Util_21 + "%s" + Messages.Util_22 + "%s"; //$NON-NLS-1$//$NON-NLS-2$
            String content = getTextContent(client, request, errMessage);
            return content;
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        } catch (SecurityException e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    public static DefaultHttpClient enableSSL(DefaultHttpClient client, int port) throws SecurityException {
        if (client == null) {
            throw new IllegalArgumentException();
        }
        SSLContext context = SSLContextProvider.getContext();
        X509HostnameVerifier verifier = (X509HostnameVerifier) SSLContextProvider.getHostnameVerifier();
        SSLSocketFactory ssf = new SSLSocketFactory(context, verifier);
        ClientConnectionManager ccm = client.getConnectionManager();
        SchemeRegistry sr = ccm.getSchemeRegistry();
        sr.register(new Scheme("https", port, ssf)); //$NON-NLS-1$
        return new DefaultHttpClient(ccm, client.getParams());

    }

    private static final String PATTERN_URL = "[http|https]+://.+:(\\d+)/.*"; //$NON-NLS-1$

    public static int getPortFromUrl(String url) {
        if (url == null) {
            throw new IllegalArgumentException();
        }
        Matcher m = Pattern.compile(PATTERN_URL).matcher(url);
        if (m.find()) {
            String portStr = m.group(1);
            return Integer.parseInt(portStr);
        } else {
            return 80;
        }
    }

    public static DefaultHttpClient enableSSL(DefaultHttpClient client, String url) {
        int port = getPortFromUrl(url);
        return enableSSL(client, port);
    }

    public static OutputStream downloadFile(String url, String downloadFolder) {
        InputStream input = null;
        OutputStream output = null;
        try {
            URL urlFile = new URL(url);
            String filename = urlFile.getFile();
            if (filename != null) {
                int pos = filename.lastIndexOf('/');
                if (pos != -1) {
                    filename = filename.substring(pos + 1);
                }
            } else {
                int pos = url.lastIndexOf('/');
                if (pos != -1) {
                    filename = url.substring(pos + 1);
                }
            }
            input = urlFile.openStream();
            byte[] bytes = IOUtils.toByteArray(input);
            output = new FileOutputStream(new File(downloadFolder + "/" + filename)); //$NON-NLS-1$
            IOUtils.write(bytes, output);
            return output;
        } catch (MalformedURLException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(output);
        }
        return null;
    }
}
