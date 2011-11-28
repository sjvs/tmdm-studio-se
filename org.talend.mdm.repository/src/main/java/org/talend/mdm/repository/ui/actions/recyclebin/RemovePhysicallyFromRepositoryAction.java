// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.mdm.repository.ui.actions.recyclebin;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.FolderType;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.mdm.repository.core.AbstractRepositoryAction;
import org.talend.mdm.repository.core.command.CommandManager;
import org.talend.mdm.repository.i18n.Messages;
import org.talend.mdm.repository.model.mdmproperties.ContainerItem;
import org.talend.mdm.repository.model.mdmproperties.MDMServerObjectItem;
import org.talend.mdm.repository.utils.RepositoryResourceUtil;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;

import com.amalto.workbench.image.EImage;
import com.amalto.workbench.image.ImageCache;

/**
 * DOC hbhong class global comment. Detailled comment
 */
public class RemovePhysicallyFromRepositoryAction extends AbstractRepositoryAction {

    static Logger log = Logger.getLogger(RemovePhysicallyFromRepositoryAction.class);

    IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();

    /**
     * DOC hbhong RemoveFromRepositoryAction constructor comment.
     * 
     * @param text
     */
    public RemovePhysicallyFromRepositoryAction() {
        super(Messages.RemovePhysicallyFromRepositoryAction_title);
        setImageDescriptor(ImageCache.getImage(EImage.DELETE_OBJ.getPath()));
    }

    @Override
    public String getGroupName() {
        return GROUP_EDIT;
    }

    @Override
    public void run() {
        int size = getSelectedObject().size();
        if (size > 0) {
            if (!MessageDialog.openConfirm(getShell(), Messages.RemoveFromRepositoryAction_Title, Messages.bind(
                    Messages.RemoveFromRepositoryAction_confirm, size, size > 1 ? Messages.RemoveFromRepositoryAction_instances
                            : Messages.RemoveFromRepositoryAction_instance))) {
                return;
            }

        }
        for (Object obj : getSelectedObject()) {
            if (obj instanceof IRepositoryViewObject) {
                IRepositoryViewObject viewObj = (IRepositoryViewObject) obj;
                RepositoryResourceUtil.closeEditor(viewObj, false);
                if (isServerObject(viewObj)) {
                    removeServerObject(viewObj);
                } else if (RepositoryResourceUtil.hasContainerItem(obj, FolderType.FOLDER_LITERAL)) {
                    removeFolderObject(viewObj);
                }

            }
        }

        try {
            factory.saveProject(ProjectManager.getInstance().getCurrentProject());
        } catch (PersistenceException e) {
            log.error(e.getMessage(), e);
        }

        commonViewer.refresh();

    }

    private boolean isServerObject(IRepositoryViewObject viewObj) {
        Item item = viewObj.getProperty().getItem();
        return item instanceof MDMServerObjectItem || item instanceof ProcessItem;
    }

    private void removeServerObject(IRepositoryViewObject viewObj) {
        try {
            String id = viewObj.getId();
            factory.deleteObjectPhysical(viewObj);
            CommandManager.getInstance().removeCommandStack(id);

        } catch (PersistenceException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void removeFolderObject(IRepositoryViewObject viewObj) {
        Project project = ProjectManager.getInstance().getCurrentProject();
        ContainerItem containerItem = (ContainerItem) viewObj.getProperty().getItem();
        String path = containerItem.getState().getPath();
        ERepositoryObjectType repObjType = containerItem.getRepObjType();
        // ContainerCacheService.removeContainer(repObjType, path);
        try {
            factory.deleteFolder(project, repObjType, new Path(path), false);
        } catch (PersistenceException e) {
            log.error(e.getMessage(), e);
        }

    }

}
