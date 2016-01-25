// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.mdm.repository.core.impl.jobmodel;

import java.util.List;

import org.eclipse.ui.navigator.CommonViewer;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.FolderType;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.mdm.repository.core.AbstractRepositoryAction;
import org.talend.mdm.repository.core.IRepositoryViewGlobalActionHandler;
import org.talend.mdm.repository.core.impl.RepositoryNodeActionProviderAdapter;
import org.talend.mdm.repository.model.mdmproperties.ContainerItem;
import org.talend.mdm.repository.ui.actions.bridge.DeleteAction;
import org.talend.mdm.repository.ui.actions.bridge.RenameFolderAction;
import org.talend.mdm.repository.ui.actions.job.CreateProcessAction;
import org.talend.mdm.repository.ui.actions.job.EditJobPropertiesAction;
import org.talend.mdm.repository.ui.actions.job.EditProcessAction;
import org.talend.mdm.repository.ui.actions.job.GenerateJobTransformerAction;
import org.talend.mdm.repository.ui.actions.job.GenerateJobTriggerAction;
import org.talend.mdm.repository.ui.actions.job.OpenExistVersionProcessAction;
import org.talend.mdm.repository.ui.actions.job.ReadProcessAction;
import org.talend.mdm.repository.ui.actions.job.RunProcessAction;
import org.talend.mdm.repository.ui.editors.IRepositoryViewEditorInput;

/**
 * DOC hbhong class global comment. Detailled comment <br/>
 * 
 */
public class TISProcessActionProvider extends RepositoryNodeActionProviderAdapter {

    AbstractRepositoryAction createProcessAction;

    AbstractRepositoryAction renameFolderAction;

    AbstractRepositoryAction editProcessAction;

    AbstractRepositoryAction readProcessAction;

    AbstractRepositoryAction runProcessAction;

    AbstractRepositoryAction openExistVersionProcessAction;

    AbstractRepositoryAction generateTransformerAction;

    AbstractRepositoryAction generateTriggerAction;

    AbstractRepositoryAction deleteAction;

    AbstractRepositoryAction editPropAction;

    @Override
    public void initCommonViewer(CommonViewer commonViewer) {
        super.initCommonViewer(commonViewer);
        renameFolderAction = new RenameFolderAction();
        createProcessAction = new CreateProcessAction();
        editProcessAction = new EditProcessAction();
        readProcessAction = new ReadProcessAction();
        runProcessAction = new RunProcessAction();
        openExistVersionProcessAction = new OpenExistVersionProcessAction();
        deleteAction = new DeleteAction();
        generateTransformerAction = new GenerateJobTransformerAction();
        generateTriggerAction = new GenerateJobTriggerAction();
        refreshAction = globalActionHandler.getGlobalAction(IRepositoryViewGlobalActionHandler.REFRESH);
        editPropAction = new EditJobPropertiesAction();
        //
        renameFolderAction.initCommonViewer(commonViewer);
        createProcessAction.initCommonViewer(commonViewer);
        editProcessAction.initCommonViewer(commonViewer);
        readProcessAction.initCommonViewer(commonViewer);
        runProcessAction.initCommonViewer(commonViewer);
        openExistVersionProcessAction.initCommonViewer(commonViewer);
        deleteAction.initCommonViewer(commonViewer);
        generateTransformerAction.initCommonViewer(commonViewer);
        generateTriggerAction.initCommonViewer(commonViewer);
        editPropAction.initCommonViewer(commonViewer);
        undeployAction.initCommonViewer(commonViewer);

    }

    @Override
    public List<AbstractRepositoryAction> getActions(IRepositoryViewObject viewObj) {

        List<AbstractRepositoryAction> actions = super.getActions(viewObj);
        Item item = viewObj.getProperty().getItem();
        if (item instanceof ContainerItem) {
            FolderType type = ((FolderItem) item).getType();
            switch (type.getValue()) {
            case FolderType.SYSTEM_FOLDER:
                addAction(actions, createProcessAction, viewObj);
                break;
            case FolderType.STABLE_SYSTEM_FOLDER:
                break;

            case FolderType.FOLDER:
                addAction(actions, createProcessAction, viewObj);
                addAction(actions, renameFolderAction, viewObj);
                addAction(actions, deleteAction, viewObj);
                break;
            }

        } else if (item instanceof ProcessItem) {

            addAction(actions, editProcessAction, viewObj);
            addAction(actions, readProcessAction, viewObj);
            addAction(actions, runProcessAction, viewObj);
            addAction(actions, openExistVersionProcessAction, viewObj);
            addAction(actions, editPropAction, viewObj);
            addAction(actions, deleteAction, viewObj);
            addAction(actions, generateTransformerAction, viewObj);
            addAction(actions, generateTriggerAction, viewObj);

            // deploy
            actions.add(deployToAction);
            addAction(actions, deployToLastServerAction, viewObj);
            addAction(actions, deployAnotherToAction, viewObj);
            addAction(actions, undeployAction, viewObj);
        }
        actions.add(deployAllAction);
        return actions;
    }

    @Override
    public AbstractRepositoryAction getOpenAction(IRepositoryViewObject viewObj) {
        return editProcessAction;
    }

    @Override
    public IRepositoryViewEditorInput getOpenEditorInput(IRepositoryViewObject viewObj) {
        return null;
    }

}
