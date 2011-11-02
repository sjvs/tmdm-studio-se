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
package org.talend.mdm.repository.ui.editors;


/**
 * DOC achen  class global comment. Detailled comment
 */
public interface ISvnHistory {

    public static final String CONTRUIBUTIONID_SVNHISTORY = "org.talend.mdm.repository.svn.history.propertycontributer";//$NON-NLS-1$

    boolean hasSvnHistory();

    void setName(String name);
}