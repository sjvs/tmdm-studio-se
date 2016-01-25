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

import org.eclipse.core.resources.IFile;
import org.talend.core.IService;

/**
 * created by HHB on 2012-10-25 Detailled comment
 * 
 */
public interface ISyncWorkflowService extends IService {

    public void startSyncWorkflowTask();

    public void updateWorkflowContent(String oldName, String newName, IFile file);

    public void updateVersion(String version, IFile file);

}
