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
package org.talend.mdm.repository.ui.actions;

import org.talend.core.model.properties.Item;

import com.amalto.workbench.exadapter.IExAdapter;

/**
 * created by HHB on 2014-2-28 Detailled comment
 * 
 */
public interface IImportObjectActionExAdapter extends IExAdapter<ImportObjectAction.OriginalObjectAction> {

    void updateRelation(Item item);

    boolean isDisableDependencyChecking();
}
