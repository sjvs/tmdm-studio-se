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
package com.amalto.workbench.service;

import javax.xml.ws.BindingProvider;

import org.talend.core.IService;

/**
 * created by HHB on 2013-12-23 Detailled comment
 * 
 */
public interface IWebServiceHook extends IService {

    void preRequestSendingHook(BindingProvider provider, String username);

}
