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

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.talend.core.model.properties.FolderType;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.mdm.repository.core.IRepositoryNodeLabelProvider;
import org.talend.mdm.repository.model.mdmproperties.ContainerItem;
import org.talend.mdm.repository.model.mdmproperties.MDMServerObjectItem;
import org.talend.mdm.repository.model.mdmserverobject.MDMServerObject;
import org.talend.mdm.repository.plugin.RepositoryPlugin;
import org.talend.mdm.repository.utils.EclipseResourceManager;
import org.talend.mdm.repository.utils.RepositoryResourceUtil;

/**
 * DOC hbhong class global comment. Detailled comment <br/>
 * 
 */
public abstract class AbstractLabelProvider implements IRepositoryNodeLabelProvider {

    private static final Image FOLDER_IMG = EclipseResourceManager.getImage(RepositoryPlugin.PLUGIN_ID, "icons/folder.gif"); //$NON-NLS-1$

    private static final Color COLOR_GREY = EclipseResourceManager.getColor(SWT.COLOR_DARK_GRAY);

    private static final Color COLOR_BLACK = EclipseResourceManager.getColor(SWT.COLOR_BLACK);

    private static FontData defaultFontData = JFaceResources.getDefaultFont().getFontData()[0];

    public static final Font FONT_BOLD = EclipseResourceManager.getFont(defaultFontData.getName(), defaultFontData.getHeight(),
            SWT.BOLD);

    public String getText(Object element) {
        Item item = getItem(element);
        if (item != null) {
            return getText(item);
        }
        return ""; //$NON-NLS-1$
    }

    protected Item getItem(Object element) {
        if (element instanceof IRepositoryViewObject) {
            Item item = RepositoryResourceUtil.getItemFromRepViewObj(element);
            return item;
        }
        return null;
    }

    public Image getImage(Object element) {
        Item item = getItem(element);
        if (item != null) {

            if (item instanceof ContainerItem) {
                switch (((ContainerItem) item).getType().getValue()) {
                case FolderType.SYSTEM_FOLDER:
                    return getCategoryImage(item);
                case FolderType.FOLDER:
                case FolderType.STABLE_SYSTEM_FOLDER:
                    return FOLDER_IMG;
                }
            }
        }
        return null;
    }

    public String getDescription(Object anElement) {
        if (anElement instanceof IRepositoryViewObject) {
            Property property = ((IRepositoryViewObject) anElement).getProperty();
            Item item = property.getItem();
            if (item instanceof MDMServerObjectItem) {
                String label = getServerObjectItemText(item);
                if (label != null) {
                    return label;
                }
            }
            String label = property.getLabel();
            return label;

        }
        return null;
    }

    protected String getText(Item item) {
        if (item instanceof ContainerItem) {
            return getConainerItemText(item);
        }
        if (item instanceof MDMServerObjectItem) {
            String label = getServerObjectItemText(item);
            if (label != null) {
                return label;
            }
        }
        // default
        String label = item.getProperty().getLabel();
        if (label != null) {
            return label;
        }
        return ""; //$NON-NLS-1$
    }

    protected String getConainerItemText(Item item) {
        return ((ContainerItem) item).getLabel();
    }

    public Color getForeground(Object element) {
        if (isSystemServerObjectItem(element)) {
            return COLOR_GREY;
        }
        return COLOR_BLACK;
    }

    public Font getFont(Object element) {
        if (isSystemServerObjectItem(element)
                || RepositoryResourceUtil.hasContainerItem(element, FolderType.SYSTEM_FOLDER_LITERAL)) {
            return FONT_BOLD;
        }
        return null;
    }

    protected boolean isSystemServerObjectItem(Object element) {
        Item item = getItem(element);
        if (item != null) {
            if (item instanceof ContainerItem && ((ContainerItem) item).getType() == FolderType.STABLE_SYSTEM_FOLDER_LITERAL) {
                return true;
            }
            if (item instanceof MDMServerObjectItem) {

                MDMServerObject serverObject = ((MDMServerObjectItem) item).getMDMServerObject();
                if (serverObject != null) {
                    return serverObject.isSystem();
                }

            }
        }
        return false;
    }

    protected String getServerObjectItemText(Item item) {
        item = RepositoryResourceUtil.assertItem(item);
        MDMServerObject serverObject = ((MDMServerObjectItem) item).getMDMServerObject();
        if (serverObject != null) {
            return serverObject.getName();
        }
        return null;
    }

    public abstract Image getCategoryImage(Item item);

    public static Image getFolderImg() {
        return FOLDER_IMG;
    }

}
