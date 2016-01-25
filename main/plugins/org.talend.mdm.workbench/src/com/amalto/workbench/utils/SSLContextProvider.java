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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.axis.utils.StringUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.eclipse.jface.preference.IPreferenceStore;

import com.amalto.workbench.MDMWorbenchPlugin;
import com.amalto.workbench.i18n.Messages;
import com.amalto.workbench.preferences.PreferenceConstants;

/**
 * created by changguopiao on 2013-7-10 Detailled comment
 * 
 */
public class SSLContextProvider {

    private static SSLContext context;

    private static final IPreferenceStore store = MDMWorbenchPlugin.getDefault().getPreferenceStore();

    private static final TrustManager TRUST_ALL = new X509TrustManager() {

        public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    };

    public static synchronized SSLContext getContext() {
        if (null == context) {
            buildContext();
        }
        return context;
    }

    public static HostnameVerifier getHostnameVerifier() {
        boolean verify = store.getBoolean(PreferenceConstants.VERIFY_HOSTNAME);
        if (verify) {
            return SSLSocketFactory.STRICT_HOSTNAME_VERIFIER;
        } else {
            return SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
        }
    }

    private static KeyManager[] buildKeyManagers(String path, String storePass, String keytype) throws KeyStoreException,
            NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {
        InputStream stream = null;
        try {
            if (StringUtils.isEmpty(path)) {
                return null;
            }
            if (!new File(path).exists()) {
                throw new KeyStoreException(Messages.bind(Messages.noKeystoreFile_error, path));
            }
            stream = new FileInputStream(path);

            KeyStore tks = KeyStore.getInstance(keytype);
            tks.load(stream, storePass.toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509"); //$NON-NLS-1$
            kmf.init(tks, storePass.toCharArray());

            return kmf.getKeyManagers();
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    private static TrustManager[] buildTrustManagers(String path, String storePass, String trusttype) throws KeyStoreException,
            NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {
        InputStream stream = null;
        try {
            if (StringUtils.isEmpty(path)) {
                return new TrustManager[] { TRUST_ALL };
            }
            if (!new File(path).exists()) {
                throw new KeyStoreException(Messages.bind(Messages.noKeystoreFile_error, path));
            }
            stream = new FileInputStream(path);

            KeyStore tks = KeyStore.getInstance(trusttype);
            tks.load(stream, storePass.toCharArray());

            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509"); //$NON-NLS-1$
            tmf.init(tks);

            return tmf.getTrustManagers();
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    private synchronized static void buildContext() {
        String algorithm = store.getString(PreferenceConstants.SSL_Algorithm);
        String keypath = store.getString(PreferenceConstants.KEYSTORE_FILE);
        String keypass = store.getString(PreferenceConstants.KEYSTORE_PASSWORD);
        String keytype = store.getString(PreferenceConstants.KEYSTORE_TYPE);
        String trustpath = store.getString(PreferenceConstants.TRUSTSTORE_FILE);
        String trustpass = store.getString(PreferenceConstants.TRUSTSTORE_PASSWORD);
        String trusttype = store.getString(PreferenceConstants.TRUSTSTORE_TYPE);
        keypass = PasswordUtil.decryptPassword(keypass);
        trustpass = PasswordUtil.decryptPassword(trustpass);
        buildContext(algorithm, keypath, keypass, keytype, trustpath, trustpass, trusttype);
    }

    public synchronized static void buildContext(String algorithm, String keypath, String keypass, String keytype,
            String trustpath, String trustpass, String trusttype) {
        try {
            KeyManager[] kms = buildKeyManagers(keypath, keypass, keytype);
            TrustManager[] tms = buildTrustManagers(trustpath, trustpass, trusttype);
            SSLContext sslcontext = SSLContext.getInstance(algorithm);
            sslcontext.init(kms, tms, null);
            context = sslcontext;
        } catch (Exception e) {
            throw new SecurityException(e.getMessage(), e);
        }
    }
}
