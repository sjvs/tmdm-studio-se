// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
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
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.runtime.Platform;

import com.amalto.workbench.dialogs.LoginDialog;

public class MDMServerHelper {

    public static final String ROOT = "MDMServer"; //$NON-NLS-1$

    public static final String PROPERTIES = "properties"; //$NON-NLS-1$

    public static final String NAME = "name"; //$NON-NLS-1$

    public static final String URL = "url"; //$NON-NLS-1$

    public static final String USER = "user"; //$NON-NLS-1$

    public static final String PASSWORD = "password"; //$NON-NLS-1$

    public static final String UNIVERSE = "universe"; //$NON-NLS-1$

    public static final String workbenchConfigFile = Platform.getInstanceLocation().getURL().getPath()
            + "/mdm_workbench_config.xml"; //$NON-NLS-1$

    private static final Log log = LogFactory.getLog(LoginDialog.class);

    public static List<MDMServerDef> getServers() {
        List<MDMServerDef> defs = new ArrayList<MDMServerDef>();
        SAXReader reader = new SAXReader();
        Element root;
        Document logininfoDocument;
        if (new File(workbenchConfigFile).exists()) {
            try {
                logininfoDocument = reader.read(new File(workbenchConfigFile));
            } catch (DocumentException e) {
                log.error(e.getMessage(), e);
                return defs;
            }
            root = logininfoDocument.getRootElement();
        } else {
            logininfoDocument = DocumentHelper.createDocument();
            root = logininfoDocument.addElement(ROOT);
        }

        List<?> properties = root.elements(PROPERTIES);
        for (Iterator<?> iterator = properties.iterator(); iterator.hasNext();) {
            Element ele = (Element) iterator.next();
            String password = ele.element(PASSWORD).getText();

            String universe = ele.element(UNIVERSE) != null ? ele.element(UNIVERSE).getText() : ""; //$NON-NLS-1$
            String name = ele.element(NAME) != null ? ele.element(NAME).getText() : ""; //$NON-NLS-1$

            password = PasswordUtil.decryptPassword(password);
            MDMServerDef def = MDMServerDef.parse(ele.element(URL).getText(), ele.element(USER).getText(), password, universe,
                    name);
            defs.add(def);
        }
        return defs;
    }

    public static MDMServerDef getServer(String name) {
        List<MDMServerDef> servers = getServers();
        for (MDMServerDef server : servers) {
            if (server.getName().equals(name)) {
                return server;
            }
        }
        return null;
    }

    public static void deleteServer(String name) {
        SAXReader reader = new SAXReader();
        File file = new File(MDMServerHelper.workbenchConfigFile);
        if (file.exists()) {
            Document logininfoDocument = null;
            try {
                logininfoDocument = reader.read(file);
            } catch (DocumentException e) {
                log.error(e.getMessage(), e);
                return;
            }
            Element root = logininfoDocument.getRootElement();
            deleteServer(root, name);
            try {
                XMLWriter writer = new XMLWriter(new FileWriter(MDMServerHelper.workbenchConfigFile));
                writer.write(logininfoDocument);
                writer.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private static void deleteServer(Element root, String name) {
        java.util.List<?> properties = root.elements(MDMServerHelper.PROPERTIES);
        for (Iterator<?> iterator = properties.iterator(); iterator.hasNext();) {
            Element ele = (Element) iterator.next();
            if (ele.element(MDMServerHelper.NAME).getText().equals(name))
                root.remove(ele);
        }
    }
}
