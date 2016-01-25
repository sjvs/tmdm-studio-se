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
package org.talend.mdm.repository.core.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.talend.mdm.repository.core.service.wsimpl.servicedoc.AbstractGetDocument;
import org.talend.mdm.repository.core.service.wsimpl.servicedoc.CallJobGetDocument;
import org.talend.mdm.repository.core.service.wsimpl.servicedoc.CallTransformerGetDocument;
import org.talend.mdm.repository.core.service.wsimpl.servicedoc.DumpToConsoleGetDocument;
import org.talend.mdm.repository.core.service.wsimpl.servicedoc.ItemDispatcherGetDocument;
import org.talend.mdm.repository.core.service.wsimpl.servicedoc.JdbcGetDocument;
import org.talend.mdm.repository.core.service.wsimpl.servicedoc.LoggingGetDocument;
import org.talend.mdm.repository.core.service.wsimpl.servicedoc.LoggingSmtpGetDocument;
import org.talend.mdm.repository.core.service.wsimpl.servicedoc.SVNGetDocument;
import org.talend.mdm.repository.core.service.wsimpl.servicedoc.SmtpGetDocument;
import org.talend.mdm.repository.core.service.wsimpl.servicedoc.SynchronizationServiceGetDocument;
import org.talend.mdm.repository.core.service.wsimpl.servicedoc.WorkflowContextGetDocument;
import org.talend.mdm.repository.core.service.wsimpl.servicedoc.WorkflowGetDocument;
import org.talend.mdm.repository.core.service.wsimpl.transformplugin.AbstractPluginDetail;
import org.talend.mdm.repository.core.service.wsimpl.transformplugin.BatchProjectPluginDetail;
import org.talend.mdm.repository.core.service.wsimpl.transformplugin.CSVParserPluginDetail;
import org.talend.mdm.repository.core.service.wsimpl.transformplugin.CodeProjectPluginDetail;
import org.talend.mdm.repository.core.service.wsimpl.transformplugin.CrossReferencingPluginDetail;
import org.talend.mdm.repository.core.service.wsimpl.transformplugin.DumpAndGoPluginDetail;
import org.talend.mdm.repository.core.service.wsimpl.transformplugin.FixedLengthParserPluginDetail;
import org.talend.mdm.repository.core.service.wsimpl.transformplugin.GroovyPluginDetail;
import org.talend.mdm.repository.core.service.wsimpl.transformplugin.GroupedLinesReaderPluginDetail;
import org.talend.mdm.repository.core.service.wsimpl.transformplugin.LineReaderPluginDetail;
import org.talend.mdm.repository.core.service.wsimpl.transformplugin.PartialUpdatePluginDetail;
import org.talend.mdm.repository.core.service.wsimpl.transformplugin.ProjectPluginDetail;
import org.talend.mdm.repository.core.service.wsimpl.transformplugin.RegexpPluginDetail;
import org.talend.mdm.repository.core.service.wsimpl.transformplugin.ReplacePluginDetail;
import org.talend.mdm.repository.core.service.wsimpl.transformplugin.RoutePluginDetail;
import org.talend.mdm.repository.core.service.wsimpl.transformplugin.TISCallJobPluginDetail;
import org.talend.mdm.repository.core.service.wsimpl.transformplugin.WorkflowContextTriggerPluginDetail;
import org.talend.mdm.repository.core.service.wsimpl.transformplugin.WorkflowTriggerPluginDetail;
import org.talend.mdm.repository.core.service.wsimpl.transformplugin.XPathPluginDetail;
import org.talend.mdm.repository.core.service.wsimpl.transformplugin.XSLTPluginDetail;
import org.talend.mdm.repository.i18n.Messages;
import org.talend.mdm.repository.model.mdmmetadata.MDMServerDef;
import org.talend.mdm.workbench.serverexplorer.ui.dialogs.SelectServerDefDialog;

import com.amalto.workbench.models.KeyValue;
import com.amalto.workbench.models.TreeObject;
import com.amalto.workbench.models.TreeParent;
import com.amalto.workbench.utils.EXtentisObjects;
import com.amalto.workbench.utils.UserInfo;
import com.amalto.workbench.utils.Util;
import com.amalto.workbench.utils.XtentisException;
import com.amalto.workbench.webservices.WSServiceGetDocument;
import com.amalto.workbench.webservices.WSTransformerPluginV2Details;
import com.amalto.workbench.webservices.XtentisPort;

/**
 * DOC hbhong class global comment. Detailled comment
 */
public class RepositoryWebServiceAdapter {

    private static Logger log = Logger.getLogger(RepositoryWebServiceAdapter.class);

    private static Map<String, AbstractPluginDetail> transformerPluginMap;

    private static Map<String, AbstractGetDocument> documentServiceMap;

    public static XtentisPort getXtentisPort(MDMServerDef serverDef) throws XtentisException {
        return getXtentisPort(serverDef, true);
    }

    /**
     * Warning: the param ServerDef must a decrypted serverDef
     * 
     * @param serverDef
     * @return
     * @throws XtentisException
     */
    public static XtentisPort getXtentisPort(MDMServerDef serverDef, boolean showMissingJarDialog) throws XtentisException {

        try {
            if (serverDef == null) {
                return null;
            }

            XtentisPort port = Util.getPort(new URL(serverDef.getUrl()), serverDef.getUniverse(), serverDef.getUser(),
                    serverDef.getPasswd(), showMissingJarDialog);

            return port;
        } catch (MalformedURLException e) {
            throw new XtentisException(Messages.bind(Messages.RepositoryWebServiceAdapter_InvalidEndpointAddress,
                    serverDef.getUrl()));
        }
    }

    public static XtentisPort getXtentisPort(Shell shell) {
        SelectServerDefDialog dialog = new SelectServerDefDialog(shell);

        try {
            if (dialog.open() == IDialogConstants.OK_ID) {
                MDMServerDef serverDef = dialog.getSelectedServerDef();
                return RepositoryWebServiceAdapter.getXtentisPort(serverDef);
            }
        } catch (XtentisException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static XtentisPort getXtentisPort(Shell shell, MDMServerDef lastserverDef) {
        if (lastserverDef == null) {
            MessageDialog.openWarning(null, Messages.Warning_text, Messages.RepositoryWebServiceAdapter_DeployFirst);
            return null;
        }
        SelectServerDefDialog dialog = new SelectServerDefDialog(shell);
        dialog.create();
        dialog.setSelectServer(lastserverDef);
        try {
            if (dialog.open() == IDialogConstants.OK_ID) {
                MDMServerDef serverDef = dialog.getSelectedServerDef();
                XtentisPort port = RepositoryWebServiceAdapter.getXtentisPort(serverDef);
                return port;
            }
        } catch (XtentisException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static WSTransformerPluginV2Details findTransformerPluginV2Detail(String jndiName) {
        if (jndiName == null) {
            return null;
        }
        initTransformerPluginDetails();
        return transformerPluginMap.get(jndiName);
    }

    public static Collection<AbstractPluginDetail> findAllTransformerPluginV2Details() {
        initTransformerPluginDetails();
        return transformerPluginMap.values();
    }

    public static WSServiceGetDocument getServiceDocument(String jndiName) {
        if (jndiName == null) {
            return null;
        }
        initGetDocumentServices();
        return documentServiceMap.get(jndiName);
    }

    private static void initGetDocumentServices() {
        if (documentServiceMap == null) {
            documentServiceMap = new LinkedHashMap<String, AbstractGetDocument>();
            String twoLettersLanguageCode = "en"; //$NON-NLS-1$
            addGetDoc(documentServiceMap, new CallJobGetDocument(twoLettersLanguageCode));
            addGetDoc(documentServiceMap, new CallTransformerGetDocument(twoLettersLanguageCode));
            addGetDoc(documentServiceMap, new DumpToConsoleGetDocument(twoLettersLanguageCode));
            addGetDoc(documentServiceMap, new ItemDispatcherGetDocument(twoLettersLanguageCode));
            addGetDoc(documentServiceMap, new JdbcGetDocument(twoLettersLanguageCode));
            addGetDoc(documentServiceMap, new LoggingGetDocument(twoLettersLanguageCode));
            addGetDoc(documentServiceMap, new LoggingSmtpGetDocument(twoLettersLanguageCode));
            addGetDoc(documentServiceMap, new SmtpGetDocument(twoLettersLanguageCode));
            addGetDoc(documentServiceMap, new SVNGetDocument(twoLettersLanguageCode));
            addGetDoc(documentServiceMap, new SynchronizationServiceGetDocument(twoLettersLanguageCode));
            addGetDoc(documentServiceMap, new WorkflowGetDocument(twoLettersLanguageCode));
            addGetDoc(documentServiceMap, new WorkflowContextGetDocument(twoLettersLanguageCode));
        }
    }

    private static void initTransformerPluginDetails() {
        if (transformerPluginMap == null) {
            transformerPluginMap = new LinkedHashMap<String, AbstractPluginDetail>();
            String twoLettersLanguageCode = "en"; //$NON-NLS-1$
            addDetail(transformerPluginMap, new BatchProjectPluginDetail(twoLettersLanguageCode));
            addDetail(transformerPluginMap, new CodeProjectPluginDetail(twoLettersLanguageCode));
            addDetail(transformerPluginMap, new CrossReferencingPluginDetail(twoLettersLanguageCode));
            addDetail(transformerPluginMap, new CSVParserPluginDetail(twoLettersLanguageCode));
            addDetail(transformerPluginMap, new DumpAndGoPluginDetail(twoLettersLanguageCode));
            addDetail(transformerPluginMap, new FixedLengthParserPluginDetail(twoLettersLanguageCode));
            addDetail(transformerPluginMap, new GroovyPluginDetail(twoLettersLanguageCode));
            addDetail(transformerPluginMap, new GroupedLinesReaderPluginDetail(twoLettersLanguageCode));
            addDetail(transformerPluginMap, new LineReaderPluginDetail(twoLettersLanguageCode));
            addDetail(transformerPluginMap, new PartialUpdatePluginDetail(twoLettersLanguageCode));
            addDetail(transformerPluginMap, new ProjectPluginDetail(twoLettersLanguageCode));
            addDetail(transformerPluginMap, new RegexpPluginDetail(twoLettersLanguageCode));
            addDetail(transformerPluginMap, new ReplacePluginDetail(twoLettersLanguageCode));
            addDetail(transformerPluginMap, new RoutePluginDetail(twoLettersLanguageCode));
            addDetail(transformerPluginMap, new TISCallJobPluginDetail(twoLettersLanguageCode));
            addDetail(transformerPluginMap, new WorkflowTriggerPluginDetail(twoLettersLanguageCode));
            addDetail(transformerPluginMap, new WorkflowContextTriggerPluginDetail(twoLettersLanguageCode));
            addDetail(transformerPluginMap, new XPathPluginDetail(twoLettersLanguageCode));
            addDetail(transformerPluginMap, new XSLTPluginDetail(twoLettersLanguageCode));
        }
    }

    public static String[] getTheObjectsForUniverse() {
        String[] objects = new String[] { "Routing Engine V2", "Synchronization Plan", "Service", "Universe", "Routing Rule", //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                "Background Job", "Menu", "Transformer V2", "Stored Procedure", "View", "Routing Order V2 Active",//$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
                "Routing Order V2 Failed", "Item", "Data Model", "Routing Order V2 Completed", "Synchronization Conflict", //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                "Transformer Plugin V2", "Role", "Data Cluster", "Configuration Info" };//$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$
        return objects;

    }

    public static List<String> getListForUniverseMap() {
        List<String> list = new ArrayList<String>();

        list.add("Transformer V2"); //$NON-NLS-1$
        list.add("View");//$NON-NLS-1$
        list.add("Data Model");//$NON-NLS-1$
        list.add("Role");//$NON-NLS-1$
        list.add("Routing Rule");//$NON-NLS-1$
        list.add("Stored Procedure");//$NON-NLS-1$
        list.add("Menu");//$NON-NLS-1$
        list.add("Synchronization Plan");//$NON-NLS-1$

        return list;
    }

    public static String[] getItemsAlgorithmsStringsForSynchronization() {

        String algorithms[] = new String[] { "Local Wins", "Manual", "Remote Wins" };//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        return algorithms;
    }

    public static String[] getObjectsAlgorithmsStringsForSynchronization() {

        String algorithms[] = new String[] { "Local Wins", "Remote Wins" };//$NON-NLS-1$ //$NON-NLS-2$

        return algorithms;
    }

    public static String[] getXtentisObjectsForSynchronizationPlans() {

        String objects[] = new String[] {
                "Background Job", "Data Cluster", "Data Model", "Item", "Menu", "Role", "Routing Engine V2", "Routing Order V2 Active",//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
                "Routing Order V2 Completed", "Routing Order V2 Failed", "Routing Rule", "Service", "Stored Procedure", "Synchronization Conflict",//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
                "Transformer Plugin V2", "Transformer V2", "Universe", "View" };//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        return objects;
    }

    public static String[] getTheObjectsForRole() {
        String[] objects = new String[] { "Routing Engine V2", "Synchronization Plan", "Service", "Universe", "Routing Rule", //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                "Background Job", "Menu", "Transformer V2", "Stored Procedure", "View", "Routing Order V2 Active",//$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
                "Routing Order V2 Failed", "Item", "Data Model", "Routing Order V2 Completed", "Synchronization Conflict", //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                "Transformer Plugin V2", "Role", "Data Cluster", "Configuration Info", "Custom Layout" };//$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
        return objects;

    }

    public static String[] getComboListForServiceConfig() {
        String[] comblist = new String[] { "smtp", "svn", "workflow" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        return comblist;

    }

    public static List<KeyValue> getListForUniverseXtentisObjects() {
        List<KeyValue> list = new ArrayList<KeyValue>();
        list.add(new KeyValue("Data Model", ""));//$NON-NLS-1$ //$NON-NLS-2$
        list.add(new KeyValue("Menu", ""));//$NON-NLS-1$ //$NON-NLS-2$
        list.add(new KeyValue("Role", ""));//$NON-NLS-1$ //$NON-NLS-2$
        list.add(new KeyValue(EXtentisObjects.RoutingRule.getDisplayName(), ""));//$NON-NLS-1$
        list.add(new KeyValue("Stored Procedure", ""));//$NON-NLS-1$ //$NON-NLS-2$
        list.add(new KeyValue("Synchronization Plan", ""));//$NON-NLS-1$ //$NON-NLS-2$
        list.add(new KeyValue(EXtentisObjects.Transformer.getDisplayName(), ""));//$NON-NLS-1$
        list.add(new KeyValue("View", ""));//$NON-NLS-1$ //$NON-NLS-2$

        return list;

    }

    private static void addDetail(Map<String, AbstractPluginDetail> map, AbstractPluginDetail detail) {
        map.put(detail.getJNDIName(), detail);
    }

    private static void addGetDoc(Map<String, AbstractGetDocument> map, AbstractGetDocument doc) {
        map.put(doc.getJNDIName(), doc);
    }

    public static String[] getServiceNames() {
        initGetDocumentServices();
        return documentServiceMap.keySet().toArray(new String[0]);
    }

    public static void resetXObject(MDMServerDef serverDef, TreeObject xobject) {
        String serverName = serverDef.getHost();
        String universe = serverDef.getUniverse();
        String username = serverDef.getUser();
        String password = serverDef.getPasswd();
        String endpointaddress = serverDef.getUrl();
        TreeParent serverRoot = new TreeParent(serverName, null, TreeObject._SERVER_, endpointaddress, ("".equals(universe) ? ""//$NON-NLS-1$//$NON-NLS-2$
                : universe + "/") + username + ":" + (password == null ? "" : password));//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
        UserInfo user = new UserInfo();
        user.setUsername(username);
        user.setPassword(password);
        user.setServerUrl(endpointaddress);
        user.setUniverse(universe);

        serverRoot.setUser(user);

        xobject.setServerRoot(serverRoot);

    }
}
