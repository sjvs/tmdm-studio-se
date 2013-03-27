// ============================================================================
//
// Copyright (C) 2006-2013 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.mdm.workbench.serverexplorer.console;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.internal.console.IOConsolePage;
import org.eclipse.ui.part.IPageBookViewPage;
import org.talend.mdm.repository.model.mdmmetadata.MDMServerDef;
import org.talend.mdm.workbench.serverexplorer.i18n.Messages;
import org.talend.mdm.workbench.serverexplorer.plugin.MDMServerExplorerPlugin;
import org.talend.mdm.workbench.serverexplorer.ui.dialogs.DownloadLogDialog;

/**
 * created by Karelun Huang on Mar 19, 2013 Detailled comment
 *
 */
public class MDMServerMessageConsole extends MessageConsole implements IPropertyChangeListener {

    private static final int HTTP_STATUS_OK = 200;

    private static final int HTTP_STATUS_NO_ACCESS = 401;

    private static final int HTTP_STSTUS_FORBIDDEN = 403;

    private static final int HTTP_STATUS_NOT_FOUND = 404;

    public class DownloadAction extends Action {

        public DownloadAction() {
            super(Messages.MDMServerMessageConsole_DownloadAction_Text);
            setImageDescriptor(createImageDesc("icons/download.png"));//$NON-NLS-1$
        }

        @Override
        public void run() {
            DownloadLogDialog d = new DownloadLogDialog(new Shell());
            int ret = d.open();
            if (ret != IDialogConstants.OK_ID) {
                return;
            }
            final String filePath = d.getDirectoryPath();
            final boolean needOpen = d.needOpen();
            ProgressMonitorDialog pmd = new ProgressMonitorDialog(new Shell());
            try {
                pmd.run(false, true, new IRunnableWithProgress() {

                    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                        download(filePath, needOpen, monitor);
                        monitor.done();
                    }
                });
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public class MonitorAction extends Action {

        /**
         * false: Puased. true: Resumed.
         */
        private boolean isPaused = false;

        public MonitorAction() {
            update();
        }

        @Override
        public void run() {
            isPaused = !isPaused;
            pauseOrResume(isPaused);
            update();
        }

        private void update() {
            if (isPaused) {
                setText(Messages.MDMServerMessageConsole_MonitorAction_ResumeText);
                setImageDescriptor(createImageDesc("icons/resume.gif")); //$NON-NLS-1$
            } else {
                setText(Messages.MDMServerMessageConsole_MonitorAction_PauseText);
                setImageDescriptor(createImageDesc("icons/pause.gif")); //$NON-NLS-1$
            }
        }
    }

    public class ReloadAction extends Action {

        public ReloadAction() {
            super(Messages.MDMServerMessageConsole_ReloadAction_Text);
            setImageDescriptor(createImageDesc("icons/refresh.gif")); //$NON-NLS-1$
        }

        @Override
        public void run() {
            reload();
        }
    }

    private MonitorAction monitorAction = null;

    private DownloadAction downloadAction = null;

    private ReloadAction reloadAction = null;

    private MDMServerDef serverDef = null;

    private Timer timer = null;

    private int position = 0;

    public MDMServerMessageConsole(MDMServerDef serverDef) {
        this(Messages.MDMServerMessageConsole_Name, null);
        this.serverDef = serverDef;
        initMessageConsole();
        PlatformUI.getPreferenceStore().addPropertyChangeListener(this);
    }

    public MDMServerMessageConsole(String name, ImageDescriptor imageDescriptor) {
        super(name, imageDescriptor);
    }

    public void setServerDef(MDMServerDef serverDef) {
        this.serverDef = serverDef;
        initMessageConsole();
    }

    private void initMessageConsole() {
        String name = Messages.MDMServerMessageConsole_Name + " (" + serverDef.getName() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        setName(name);

        reloadAction = new ReloadAction();
        monitorAction = new MonitorAction();
        downloadAction = new DownloadAction();
    }

    @Override
    public IPageBookViewPage createPage(IConsoleView view) {
        IOConsolePage consolePage = new IOConsolePage(this, view) {

            @Override
            protected void contextMenuAboutToShow(IMenuManager menuManager) {
                super.contextMenuAboutToShow(menuManager);
                menuManager.add(new Separator());
                menuManager.add(reloadAction);
                menuManager.add(monitorAction);
                menuManager.add(downloadAction);
            }

            @Override
            public void dispose() {
                disposeTimer();
                PlatformUI.getPreferenceStore().removePropertyChangeListener(MDMServerMessageConsole.this);
                MDMServerExplorerPlugin.getDefault().getServerToConsole().remove(serverDef.getName());
                ConsolePlugin.getDefault().getConsoleManager().removeConsoles(new IConsole[] { MDMServerMessageConsole.this });
                super.dispose();
            }
        };
        consolePage.setReadOnly();
        return consolePage;
    }

    @Override
    public void activate() {
        super.activate();
        display();
    }

    private void display() {
        position = 0;
        clearConsole();

        monitor();
    }

    private synchronized void monitor() {
        if (timer == null) {
            timer = new Timer(true);
        }
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                doMonitor();
            }
        }, 0, getRefrehFrequency());
    }

    private void doMonitor() {
        DefaultHttpClient httpClient = createHttpClient();
        String monitorURL = buildMonitorURL(position);
        HttpGet httpGet = new HttpGet(monitorURL);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            int code = response.getStatusLine().getStatusCode();
            if (HTTP_STATUS_OK == code) {
                modifyChunkedPosition(response);
                if (isEndOfChunk(response)) {
                    return;
                }
                clearConsole();
                InputStream is = response.getEntity().getContent();
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                IOUtils.copy(is, os);
                String content = os.toString().trim();
                newMessageStream().println(content);
                is.close();
                os.close();
            } else if (HTTP_STATUS_NO_ACCESS == code) {
                newErrorMessageStream().println(Messages.MDMServerMessageConsole_No_Acess_Message);
                disposeTimer();
            } else if (HTTP_STSTUS_FORBIDDEN == code) {
                newErrorMessageStream().println(Messages.MDMServerMessageConsole_Forbidden_Message);
                disposeTimer();
            } else if (HTTP_STATUS_NOT_FOUND == code) {
                newErrorMessageStream().println(Messages.MDMServerMessageConsole_NotConnected_Message);
                disposeTimer();
            }
        } catch (IOException e) {
            newErrorMessageStream().println(e.getMessage());
            disposeTimer();
        }
    }

    private int getRefrehFrequency() {
        return MDMServerPreferenceService.getRefrehFrequency() * 1000;
    }

    private void modifyChunkedPosition(HttpResponse response) {
        Header[] headers = response.getHeaders("X-Log-Position"); //$NON-NLS-1$
        Assert.isTrue(headers.length > 0);
        String value = headers[0].getValue();
        position = Integer.parseInt(value);
    }

    private boolean isEndOfChunk(HttpResponse response) {
        Header[] headers = response.getHeaders("Content-Length"); //$NON-NLS-1$
        if (headers == null || headers.length == 0) {
            return false;
        }
        String value = headers[0].getValue();
        return "0".equals(value); //$NON-NLS-1$
    }

    private DefaultHttpClient createHttpClient() {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        AuthScope authScope = new AuthScope(serverDef.getHost(), Integer.parseInt(serverDef.getPort()));
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(serverDef.getUser(), serverDef.getPasswd());
        httpclient.getCredentialsProvider().setCredentials(authScope, credentials);
        return httpclient;
    }

    private String buildMonitorURL(int pos) {
        StringBuilder sb = new StringBuilder();
        sb.append("http://"); //$NON-NLS-1$
        sb.append(serverDef.getHost());
        sb.append(":"); //$NON-NLS-1$
        sb.append(serverDef.getPort());
        sb.append("/datamanager/logviewer/log?position=" + pos); //$NON-NLS-1$
        //        sb.append("&maxLines=10"); //$NON-NLS-1$
        sb.append("&maxLines=" + MDMServerPreferenceService.getMaxDisplayedLines()); //$NON-NLS-1$
        return sb.toString();
    }

    private void pauseOrResume(boolean isPaused) {
        if (isPaused) {
            disposeTimer();
        } else {
            monitor();
        }
    }

    private void reload() {
        disposeTimer();
        display();
    }

    private void download(String dirPath, boolean needOpen, IProgressMonitor monitor) {
        try {
            monitor.beginTask(Messages.MDMServerMessageConsole_DownloadTask_Name, 100);
            DefaultHttpClient httpClient = createHttpClient();
            String monitorURL = buildDownloadURL();
            HttpGet httpGet = new HttpGet(monitorURL);
            monitor.worked(20);
            HttpResponse response = httpClient.execute(httpGet);
            monitor.worked(40);
            int code = response.getStatusLine().getStatusCode();
            if (HTTP_STATUS_OK == code) {
                String fileName = getFileName(response);
                InputStream is = response.getEntity().getContent();
                monitor.worked(60);
                File file = new File(dirPath + File.separator + fileName);
                FileOutputStream os = new FileOutputStream(file);
                IOUtils.copy(is, os);
                monitor.worked(85);
                is.close();
                os.close();
                if (needOpen) {
                    Program.launch(file.getAbsolutePath());
                }
                monitor.worked(90);
            } else {
                newErrorMessageStream().println(Messages.MDMServerMessageConsole_DownloadFailed_Message);
                if (HTTP_STATUS_NO_ACCESS == code) {
                    newErrorMessageStream().println(Messages.MDMServerMessageConsole_No_Acess_Message);
                } else if (HTTP_STSTUS_FORBIDDEN == code) {
                    newErrorMessageStream().println(Messages.MDMServerMessageConsole_Forbidden_Message);
                } else if (HTTP_STATUS_NOT_FOUND == code) {
                    newErrorMessageStream().println(Messages.MDMServerMessageConsole_NotConnected_Message);
                }
                monitor.worked(90);
            }
        } catch (IOException e) {
            newErrorMessageStream().println(Messages.MDMServerMessageConsole_DownloadFailed_Message);
            newErrorMessageStream().println(e.getMessage());
            monitor.worked(90);
        }
    }

    private String buildDownloadURL() {
        StringBuilder sb = new StringBuilder();
        sb.append("http://"); //$NON-NLS-1$
        sb.append(serverDef.getHost());
        sb.append(":"); //$NON-NLS-1$
        sb.append(serverDef.getPort());
        sb.append("/datamanager/logviewer/log"); //$NON-NLS-1$
        return sb.toString();
    }

    private String getFileName(HttpResponse response) {
        Header[] headers = response.getHeaders("Content-Disposition"); //$NON-NLS-1$
        Assert.isTrue(headers.length > 0);
        String value = headers[0].getValue();
        int begin = value.indexOf("\""); //$NON-NLS-1$
        int end = value.lastIndexOf("\""); //$NON-NLS-1$
        return value.substring(begin + 1, end);
    }

    private void disposeTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private MessageConsoleStream newErrorMessageStream() {
        final MessageConsoleStream msgStream = newMessageStream();
        ConsolePlugin.getStandardDisplay().asyncExec(new Runnable() {

            public void run() {
                msgStream.setColor(Display.getDefault().getSystemColor(SWT.COLOR_RED));
            }
        });
        return msgStream;
    }

    public void propertyChange(PropertyChangeEvent event) {
        if (event.getProperty().equals(MDMServerPreferenceService.REFRESH_FREQ)) {
            disposeTimer();
            monitor();
        }
    }

    public ReloadAction getReloadAction() {
        return this.reloadAction;
    }

    public MonitorAction getMonitorAction() {
        return this.monitorAction;
    }

    public DownloadAction getDownloadAction() {
        return this.downloadAction;
    }

    private ImageDescriptor createImageDesc(String path) {
        return MDMServerExplorerPlugin.imageDescriptorFromPlugin(MDMServerExplorerPlugin.PLUGIN_ID, path);
    }
}