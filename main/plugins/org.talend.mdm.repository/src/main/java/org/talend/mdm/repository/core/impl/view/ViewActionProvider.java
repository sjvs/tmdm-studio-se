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
package org.talend.mdm.repository.core.impl.view;

import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.navigator.CommonViewer;
import org.talend.core.model.properties.FolderType;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.mdm.repository.core.AbstractRepositoryAction;
import org.talend.mdm.repository.core.impl.RepositoryNodeActionProviderAdapter;
import org.talend.mdm.repository.model.mdmproperties.ContainerItem;
import org.talend.mdm.repository.model.mdmproperties.MDMServerObjectItem;
import org.talend.mdm.repository.ui.actions.view.BrowseViewAction;
import org.talend.mdm.repository.ui.actions.view.MDMEditViewProcessPropertyAction;
import org.talend.mdm.repository.ui.actions.view.NewViewAction;
import org.talend.mdm.repository.ui.actions.view.RenameViewAction;
import org.talend.mdm.repository.utils.RepositoryResourceUtil;

/**
 * DOC hbhong class global comment. Detailled comment <br/>
 * 
 */
public class ViewActionProvider extends RepositoryNodeActionProviderAdapter {

    AbstractRepositoryAction addAction;

    AbstractRepositoryAction browseViewAction;

    AbstractRepositoryAction renameViewAction;

    private AbstractRepositoryAction editViewProcessPropertyAction;

    @Override
    public void initCommonViewer(CommonViewer commonViewer) {
        super.initCommonViewer(commonViewer);
        addAction = initRepositoryAction(new NewViewAction(), commonViewer);
        browseViewAction = initRepositoryAction(new BrowseViewAction(), commonViewer);
        renameViewAction = initRepositoryAction(new RenameViewAction(), commonViewer);
        editViewProcessPropertyAction = initRepositoryAction(new MDMEditViewProcessPropertyAction(), commonViewer);
        //
    }

    @Override
    public List<AbstractRepositoryAction> getActions(IRepositoryViewObject viewObj) {
        List<AbstractRepositoryAction> actions = super.getActions(viewObj);

        Item item = viewObj.getProperty().getItem();
        String path = item.getState().getPath();
        if (path.isEmpty()) {
            actions.remove(createFolderAction);
        }

        if ((item instanceof ContainerItem)
                && (path.equals(IPath.SEPARATOR + IViewNodeConstDef.PATH_SEARCHFILTER) || path.equals(IPath.SEPARATOR
                        + IViewNodeConstDef.PATH_WEBFILTER))) {
            actions.remove(removeFromRepositoryAction);
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

            addAction(actions, renameViewAction, viewObj);
            addAction(actions, browseViewAction, viewObj);
            // deploy
            addAction(actions, deployToAction, viewObj);
            addAction(actions, deployToLastServerAction, viewObj);
            addAction(actions, deployAnotherToAction, viewObj);
            addAction(actions, undeployAction, viewObj);
        }
        addAction(actions, deployAllAction, viewObj);
        return actions;
    }

}
