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
package com.amalto.workbench.editors;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.amalto.workbench.dialogs.DOMViewDialog;
import com.amalto.workbench.dialogs.datacontainer.DataContainerDOMViewDialog;
import com.amalto.workbench.i18n.Messages;
import com.amalto.workbench.utils.Util;
import com.amalto.workbench.webservices.WSDataClusterPK;
import com.amalto.workbench.webservices.WSDataModelPK;
import com.amalto.workbench.webservices.WSDataModelPKArray;
import com.amalto.workbench.webservices.WSPutItem;
import com.amalto.workbench.webservices.WSPutItemWithReport;
import com.amalto.workbench.webservices.WSRegexDataModelPKs;
import com.amalto.workbench.webservices.XtentisPort;

/**
 * created by liusongbo on 2014-2-19
 */
public class NewItemHandler {

    private static final Log log = LogFactory.getLog(NewItemHandler.class);

    private boolean created = false;

    public static NewItemHandler getNewInstance() {
        return new NewItemHandler();
    }

    public boolean createItemRecord(final XtentisPort port, final Shell ashell, final WSDataClusterPK dataClusterPk,
            boolean isMaster) {
        if (port == null || dataClusterPk == null) {
            throw new IllegalArgumentException();
        }

        final Shell shell = ashell != null ? ashell : Display.getCurrent().getActiveShell();

        try {
            String xml = "<NewItem><NewElement></NewElement></NewItem>"; //$NON-NLS-1$

            WSDataModelPKArray dataModelPKs = port.getDataModelPKs(new WSRegexDataModelPKs("*")); //$NON-NLS-1$
            List<WSDataModelPK> dmPKs = dataModelPKs.getWsDataModelPKs();
            List<String> dataModels = new ArrayList<String>();
            if (dmPKs != null) {
                for (WSDataModelPK pk : dmPKs) {
                    if (!"XMLSCHEMA---".equals(pk.getPk())) { //$NON-NLS-1$
                        dataModels.add(pk.getPk());
                    }
                }
            }
            final DataContainerDOMViewDialog d = new DataContainerDOMViewDialog(shell, port, Util.parse(xml), dataModels,
                    DOMViewDialog.SOURCE_VIEWER, null, isMaster);
            d.addListener(new Listener() {

                public void handleEvent(Event event) {
                    if (event.button == DOMViewDialog.BUTTON_SAVE) {
                        // attempt to save
                        try {
                            WSPutItem putItem = new WSPutItem(dataClusterPk, d.getXML(), "".equals(d.getDataModelName()) ? null //$NON-NLS-1$
                                    : new WSDataModelPK(d.getDataModelName()), false);
                            WSPutItemWithReport item = new WSPutItemWithReport(putItem, "genericUI", d.isBeforeVerification());//$NON-NLS-1$
                            if (d.isTriggerProcess()) {
                                port.putItemWithReport(item);
                            } else {
                                port.putItem(putItem);
                            }
                            created = true;

                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                            MessageDialog.openError(
                                    shell,
                                    Messages.DataClusterBrowserMainPage_100,
                                    Messages.bind(Messages.DataClusterBrowserMainPage_101,
                                            Util.formatErrorMessage(e.getLocalizedMessage())));
                            return;
                        }
                    }
                    d.close();
                }
            });

            d.setBlockOnOpen(true);
            d.open();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            if (!Util.handleConnectionException(shell, e, null)) {
                MessageDialog.openError(shell, Messages._Error,
                        Messages.bind(Messages.DataClusterBrowserMainPage_103, e.getLocalizedMessage()));
            }
        }

        return created;
    }
}
