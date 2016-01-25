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
package org.talend.mdm.repository.core.impl.recyclebin;

import org.eclipse.swt.graphics.Image;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.mdm.repository.core.IRepositoryNodeConfiguration;
import org.talend.mdm.repository.core.IRepositoryNodeLabelProvider;
import org.talend.mdm.repository.core.IServerObjectRepositoryType;
import org.talend.mdm.repository.core.impl.AbstractLabelProvider;
import org.talend.mdm.repository.extension.RepositoryNodeConfigurationManager;
import org.talend.mdm.repository.model.mdmproperties.MDMServerObjectItem;
import org.talend.mdm.repository.models.FolderRepositoryObject;
import org.talend.mdm.repository.plugin.RepositoryPlugin;
import org.talend.mdm.repository.utils.EclipseResourceManager;

/**
 * DOC hbhong class global comment. Detailled comment <br/>
 * 
 */
public class RecycleBinLabelProvider extends AbstractLabelProvider {

    private static final Image EMPTY_IMG = EclipseResourceManager.getImage(RepositoryPlugin.PLUGIN_ID,
            "icons/recycle_bin_empty.png"); //$NON-NLS-1$;

    private static final Image FULL_IMG = EclipseResourceManager.getImage(RepositoryPlugin.PLUGIN_ID,
            "icons/recycle_bin_full.png"); //$NON-NLS-1$;

    public String getCategoryLabel(ERepositoryObjectType type) {
        return "Recycle bin"; //$NON-NLS-1$
    }

    public Image getCategoryImage(Item item) {
        return null;
    }

    @Override
    public Image getImage(Object element) {
        if (element instanceof IRepositoryViewObject) {

            IRepositoryViewObject viewObj = (IRepositoryViewObject) element;
            ERepositoryObjectType type = viewObj.getRepositoryObjectType();
            if (type == IServerObjectRepositoryType.TYPE_RECYCLE_BIN) {
                if (viewObj instanceof FolderRepositoryObject) {
                    return ((FolderRepositoryObject) viewObj).getChildren().isEmpty() ? EMPTY_IMG : FULL_IMG;
                }
            }
            IRepositoryNodeConfiguration configuration = RepositoryNodeConfigurationManager.getConfiguration(type);
            if (configuration != null) {
                IRepositoryNodeLabelProvider labelProvider = configuration.getLabelProvider();
                return labelProvider.getImage(element);
            }
        }
        return null;
    }

    @Override
    protected String getServerObjectItemText(Item item) {
        MDMServerObjectItem mItem = (MDMServerObjectItem) item;
        return mItem.getMDMServerObject().getName();

    }

}
