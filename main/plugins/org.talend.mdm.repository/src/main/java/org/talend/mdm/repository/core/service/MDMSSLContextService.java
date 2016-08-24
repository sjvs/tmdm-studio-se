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

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.talend.designer.core.IMDMService;

import com.amalto.workbench.utils.SSLContextProvider;

/**
 * created by HHB on 2016-8-24 Detailled comment
 *
 */
public class MDMSSLContextService implements IMDMService {

    @Override
    public SSLContext getSSLContext() {

        return SSLContextProvider.getContext();
    }

    @Override
    public HostnameVerifier getDefaultHostnameVerifier() {
        return SSLContextProvider.getHostnameVerifier();
    }

}
