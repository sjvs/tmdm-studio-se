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

import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.mdm.repository.core.AbstractRepositoryAction;
import org.talend.mdm.repository.i18n.Messages;

import com.amalto.workbench.image.EImage;
import com.amalto.workbench.image.ImageCache;

/**
 * DOC jsxie class global comment. Detailled comment
 */
public class RefreshViewAction extends AbstractRepositoryAction {

    public RefreshViewAction() {
        super(Messages.RefreshAction_Refresh_All);
        setImageDescriptor(ImageCache.getImage(EImage.REFRESH.getPath()));
        setToolTipText(Messages.RefreshAction_Refresh_All_tooltip);
    }

    protected void doRun() {
        Object input = commonViewer.getInput();
        for (IRepositoryViewObject viewObj : (IRepositoryViewObject[]) input) {
            commonViewer.refresh(viewObj);
        }

    }

    @Override
    public String getGroupName() {
        return GROUP_COMMON;
    }
}
