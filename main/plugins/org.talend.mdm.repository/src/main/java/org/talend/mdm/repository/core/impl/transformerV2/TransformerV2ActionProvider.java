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
package org.talend.mdm.repository.core.impl.transformerV2;

import java.util.Arrays;
import java.util.List;

import org.eclipse.ui.navigator.CommonViewer;
import org.talend.core.model.properties.FolderType;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.mdm.repository.core.AbstractRepositoryAction;
import org.talend.mdm.repository.core.impl.RepositoryNodeActionProviderAdapter;
import org.talend.mdm.repository.model.mdmproperties.ContainerItem;
import org.talend.mdm.repository.model.mdmproperties.MDMServerObjectItem;
import org.talend.mdm.repository.ui.actions.process.NewProcessAction;
import org.talend.mdm.repository.ui.actions.process.RenameProcessAction;
import org.talend.mdm.repository.ui.actions.view.MDMEditViewProcessPropertyAction;
import org.talend.mdm.repository.utils.RepositoryResourceUtil;

/**
 * DOC hbhong class global comment. Detailled comment <br/>
 * 
 */
public class TransformerV2ActionProvider extends RepositoryNodeActionProviderAdapter {

    AbstractRepositoryAction addAction;

    AbstractRepositoryAction renameProcessAction;

    AbstractRepositoryAction editViewProcessPropertyAction;

    @Override
    public void initCommonViewer(CommonViewer commonViewer) {
        super.initCommonViewer(commonViewer);

        addAction = new NewProcessAction();
        renameProcessAction = new RenameProcessAction();
        editViewProcessPropertyAction = new MDMEditViewProcessPropertyAction();

        //
        addAction.initCommonViewer(commonViewer);
        renameProcessAction.initCommonViewer(commonViewer);
        editViewProcessPropertyAction.initCommonViewer(commonViewer);
    }

    @Override
    public List<AbstractRepositoryAction> getActions(IRepositoryViewObject viewObj) {
        List<AbstractRepositoryAction> actions = super.getActions(viewObj);

        if (!canDelete(viewObj)) {
            actions.remove(removeFromRepositoryAction);
        }
        if (isProcessNode(viewObj)) {
            actions.remove(createFolderAction);
        }

        if (RepositoryResourceUtil.hasContainerItem(viewObj, FolderType.SYSTEM_FOLDER_LITERAL, FolderType.FOLDER_LITERAL)) {
            addAction(actions, addAction, viewObj);
        }
        
        if (viewObj.getProperty().getItem() instanceof MDMServerObjectItem) {
            int index = actions.indexOf(mdmEditPropertyAction);
            if (index != -1) {
                actions.add(index, editViewProcessPropertyAction);
                actions.remove(mdmEditPropertyAction);
            }

            addAction(actions, renameProcessAction, viewObj);
            // deploy
            addAction(actions, deployToAction, viewObj);
            addAction(actions, deployToLastServerAction, viewObj);
            addAction(actions, deployAnotherToAction, viewObj);
            addAction(actions, undeployAction, viewObj);
        }
        addAction(actions, deployAllAction, viewObj);
        return actions;
    }

    private boolean isProcessNode(IRepositoryViewObject viewObj) {
        String path = viewObj.getProperty().getItem().getState().getPath();

        return path.isEmpty();
    }

    private boolean canDelete(IRepositoryViewObject viewObj) {
        Item item = viewObj.getProperty().getItem();
        if (!(item instanceof ContainerItem)) {
            return true;
        }

        String path = item.getState().getPath();
        List<String> paths = Arrays.asList(ITransformerV2NodeConsDef.PATH_BEFOREDEL, ITransformerV2NodeConsDef.PATH_BEFORESAVE,
                ITransformerV2NodeConsDef.PATH_ENTITYACTION, ITransformerV2NodeConsDef.PATH_WELCOMEACTION,
                ITransformerV2NodeConsDef.PATH_SMARTVIEW, ITransformerV2NodeConsDef.PATH_OTHER);
        if (path.startsWith("/")) { //$NON-NLS-1$
            path = path.substring(1);
        }
        boolean canDel = !paths.contains(path);

        return canDel;
    }

}
