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

import java.util.List;

import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.mdm.repository.core.AbstractRepositoryAction;
import org.talend.mdm.repository.core.IRepositoryViewGlobalActionHandler;
import org.talend.mdm.repository.i18n.Messages;
import org.talend.mdm.repository.model.mdmproperties.ContainerItem;

import com.amalto.workbench.image.EImage;
import com.amalto.workbench.image.ImageCache;

/**
 * DOC hbhong class global comment. Detailled comment
 */
public class RefreshAction extends AbstractRepositoryAction {

    /**
     * DOC hbhong RefreshAction constructor comment.
     * 
     * @param text
     */
    public RefreshAction() {
        super(Messages.RefreshAction_Refresh);
        setImageDescriptor(ImageCache.getImage(EImage.REFRESH.getPath()));
        this.setId(IRepositoryViewGlobalActionHandler.REFRESH);
        this.setActionDefinitionId(IRepositoryViewGlobalActionHandler.REFRESH);
    }

    protected void doRun() {
        List<Object> selectedObject = getSelectedObject();
        if (!selectedObject.isEmpty()) {
            Object obj = selectedObject.get(0);
            commonViewer.refresh(obj);

            Item item = ((IRepositoryViewObject) obj).getProperty().getItem();
            if (item instanceof ContainerItem) {
                commonViewer.expandToLevel(obj, 1);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.mdm.repository.core.AbstractRepositoryAction#getGroupName()
     */
    @Override
    public String getGroupName() {
        return GROUP_COMMON;
    }

}
