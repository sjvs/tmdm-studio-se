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
package org.talend.mdm.workbench.serverexplorer.ui.dialogs;

import org.eclipse.swt.widgets.Composite;
import org.talend.mdm.repository.model.mdmmetadata.MDMServerDef;

import com.amalto.workbench.exadapter.IExAdapter;

/**
 * created by HHB on 2014-1-3 Detailled comment
 * 
 */
public interface IServerDefDialogExAdapter extends IExAdapter<ServerDefDialog> {

    void createDialogArea(Composite container);

    void initListener();

    void updateUI2Model(MDMServerDef serverDef);

    void initValue(MDMServerDef serverDef);
}
