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
package com.amalto.workbench.widgets.xmleditor.infoholder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amalto.workbench.webservices.WSMDMConfig;
import com.amalto.workbench.webservices.XtentisPort;

public class MDMServerInfoHolder extends ExternalInfoHolder<WSMDMConfig> {

    private static Log log = LogFactory.getLog(MDMServerInfoHolder.class);

    private XtentisPort port;

    public MDMServerInfoHolder(XtentisPort port) {
        this.port = port;
    }

    @Override
    public WSMDMConfig getExternalInfo() {
        return port.getMDMConfiguration();
    }

    @Override
    public String getId() {
        return INFOID_MDMSERVERINFO;
    }

}
