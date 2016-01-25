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
package org.talend.mdm.repository.core.impl;

import java.util.List;

import org.talend.core.model.properties.FolderType;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.mdm.repository.core.IRepositoryNodeContentProvider;
import org.talend.mdm.repository.extension.RepositoryNodeConfigurationManager;
import org.talend.mdm.repository.model.mdmproperties.ContainerItem;
import org.talend.mdm.repository.models.FolderRepositoryObject;
import org.talend.mdm.repository.utils.RepositoryResourceUtil;

/**
 * DOC hbhong class global comment. Detailled comment <br/>
 * 
 */
public abstract class AbstractContentProvider implements IRepositoryNodeContentProvider {

    public Object[] getChildren(Object element) {
        Item item = RepositoryResourceUtil.getItemFromRepViewObj(element);
        if (item != null && item instanceof ContainerItem) {

            ContainerItem containerItem = (ContainerItem) item;
            // recycle
            if (RepositoryResourceUtil.isDeletedFolder(item, ((IRepositoryViewObject) element).getRepositoryObjectType())) {
                AbstractContentProvider recycleContentProvider = (AbstractContentProvider) RepositoryNodeConfigurationManager
                        .getRecycleBinNodeConfiguration().getContentProvider();
                return recycleContentProvider.getChildren(element);
            }
            //
            FolderType containerType = containerItem.getType();
            List<IRepositoryViewObject> children = ((FolderRepositoryObject) element).getChildren();
            if (containerType == FolderType.SYSTEM_FOLDER_LITERAL) {
                List<IRepositoryViewObject> viewObjects = getViewObjFromSystemFolder(containerItem);
                if (viewObjects != null) {
                    children.clear();
                    children.addAll(viewObjects);
                    return viewObjects.toArray();
                }
            }
            if (containerType == FolderType.FOLDER_LITERAL || containerType == FolderType.STABLE_SYSTEM_FOLDER_LITERAL) {
                List<IRepositoryViewObject> viewObjects = getViewObjFromFolder(containerItem);
                if (viewObjects != null) {
                    children.clear();
                    children.addAll(viewObjects);
                    return viewObjects.toArray();
                }
            }
            // if (containerType == FolderType.STABLE_SYSTEM_FOLDER_LITERAL) {
            // List<IRepositoryViewObject> children = ((ContainerRepositoryObject) element).getChildren();
            // if (children != null) {
            // return children.toArray();
            // }
            // }
        }
        return new Object[0];
    }

    protected abstract List<IRepositoryViewObject> getViewObjFromSystemFolder(Item parentItem);

    protected List<IRepositoryViewObject> getViewObjFromFolder(ContainerItem containerItem) {
        return RepositoryResourceUtil.findViewObjects(containerItem.getRepObjType(), containerItem);
    }

    public boolean isShownInRoot() {
        return true;
    }

}
