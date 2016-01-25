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

import org.talend.core.IService;

/**
 * created by liusongbo on 2012-11-20
 * 
 */
public interface IUpdateLastServerService extends IService {

    /**
     * @param ser
     * @param repositoryViewObjName
     * @param repositoryViewObjType the valid value see constants in class TreeObject,like
     * TreeObject.WORKFLOW_PROCESS,TreeObject.DATA_MODEL
     */
    public void updateLastServerDefInfo(Object serverDef, String repositoryViewObjName, int repositoryViewObjType);
}
