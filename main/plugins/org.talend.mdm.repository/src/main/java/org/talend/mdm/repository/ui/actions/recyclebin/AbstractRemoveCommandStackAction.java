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
package org.talend.mdm.repository.ui.actions.recyclebin;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.dataquality.properties.TDQMatchRuleItem;
import org.talend.mdm.repository.core.AbstractRepositoryAction;
import org.talend.mdm.repository.core.IServerObjectRepositoryType;
import org.talend.mdm.repository.core.command.CommandManager;
import org.talend.mdm.repository.core.service.ContainerCacheService;
import org.talend.mdm.repository.model.mdmproperties.ContainerItem;
import org.talend.mdm.repository.model.mdmproperties.MDMServerObjectItem;
import org.talend.mdm.repository.models.FolderRepositoryObject;
import org.talend.mdm.repository.ui.dialogs.recycle.WaitToDeployDialog;
import org.talend.mdm.repository.utils.RepositoryResourceUtil;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * DOC hbhong class global comment. Detailled comment
 */
public abstract class AbstractRemoveCommandStackAction extends AbstractRepositoryAction {

    IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();

    static Logger log = Logger.getLogger(AbstractRemoveCommandStackAction.class);

    /**
     * DOC hbhong AbstractRemoveCommandStackAction constructor comment.
     * 
     * @param text
     */
    protected AbstractRemoveCommandStackAction(String text) {
        super(text);
    }

    protected boolean canRemove(List<IRepositoryViewObject> viewObjs) {
        WaitToDeployDialog dialog = new WaitToDeployDialog(getShell(), viewObjs);
        if (dialog.needShowDialog()) {
            return dialog.open() == IDialogConstants.OK_ID;
        }
        return true;
    }

    protected void removeViewObjects(List<IRepositoryViewObject> viewObjs) {
        if (canRemove(viewObjs)) {
            for (IRepositoryViewObject viewObj : viewObjs) {
                RepositoryResourceUtil.closeEditor(viewObj, false);
                if (isServerObject(viewObj)) {
                    removeServerObject(viewObj);
                } else if (viewObj instanceof FolderRepositoryObject) {
                    removeFolderObject((FolderRepositoryObject) viewObj);
                }
            }
        }

        try {
            factory.saveProject(ProjectManager.getInstance().getCurrentProject());
        } catch (PersistenceException e) {
            log.error(e.getMessage(), e);
        }

        refreshRepositoryRoot(IServerObjectRepositoryType.TYPE_RECYCLE_BIN);
    }

    private boolean isServerObject(IRepositoryViewObject viewObj) {
        Item item = viewObj.getProperty().getItem();
        return item instanceof MDMServerObjectItem || item instanceof ProcessItem || item instanceof TDQMatchRuleItem;
    }

    private void removeServerObject(IRepositoryViewObject viewObj) {
        try {
            String id = viewObj.getId();
            List<IRepositoryViewObject> viewObjs = new ArrayList<IRepositoryViewObject>();
            viewObjs.add(viewObj);

            ContainerCacheService.remove(id);
            factory.deleteObjectPhysical(viewObj);
            CommandManager.getInstance().removeCommandStack(id);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void removeFolderObject(FolderRepositoryObject viewObj) {
        Project project = ProjectManager.getInstance().getCurrentProject();
        ContainerItem containerItem = (ContainerItem) viewObj.getProperty().getItem();
        String path = containerItem.getState().getPath();
        ERepositoryObjectType repObjType = containerItem.getRepObjType();
        // ContainerCacheService.removeContainer(repObjType, path);
        for (IRepositoryViewObject childObj : viewObj.getChildren()) {
            if (childObj instanceof FolderRepositoryObject) {
                removeFolderObject((FolderRepositoryObject) childObj);
            } else {
                removeServerObject(childObj);
            }
        }
        try {
            factory.deleteFolder(project, repObjType, new Path(path), false);
        } catch (PersistenceException e) {
            log.error(e.getMessage(), e);
        }
    }

}
