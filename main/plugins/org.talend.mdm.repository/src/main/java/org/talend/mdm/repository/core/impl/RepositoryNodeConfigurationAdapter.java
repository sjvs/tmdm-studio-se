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

import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.mdm.repository.core.IRepositoryNodeActionProvider;
import org.talend.mdm.repository.core.IRepositoryNodeConfiguration;
import org.talend.mdm.repository.core.IRepositoryNodeContentProvider;
import org.talend.mdm.repository.core.IRepositoryNodeLabelProvider;
import org.talend.mdm.repository.core.IRepositoryNodeResourceProvider;

/**
 * DOC hbhong class global comment. Detailled comment <br/>
 * 
 */
public class RepositoryNodeConfigurationAdapter implements IRepositoryNodeConfiguration {

    IRepositoryNodeResourceProvider resourceProvider;

    IRepositoryNodeLabelProvider labelProvider;

    IRepositoryNodeContentProvider contentProvider;

    IRepositoryNodeActionProvider actionProvider;

    public IRepositoryNodeActionProvider getActionProvider() {
        return this.actionProvider;
    }

    public void setActionProvider(IRepositoryNodeActionProvider actionProvider) {
        this.actionProvider = actionProvider;
    }

    public IRepositoryNodeContentProvider getContentProvider() {
        if (contentProvider == null) {
            initContentProvider();
        }
        return this.contentProvider;
    }

    public void setContentProvider(IRepositoryNodeContentProvider contentProvider) {
        this.contentProvider = contentProvider;
    }

    public boolean canHandleRepObjType(ERepositoryObjectType type) {

        return resourceProvider.canHandleRepObjType(type);
    }

    public boolean canHandleItem(Item item) {
        return resourceProvider.canHandleItem(item);
    }

    public IRepositoryNodeLabelProvider getLabelProvider() {
        if (labelProvider == null) {
            initLabelProvider();
        }
        return this.labelProvider;
    }

    public void setLabelProvider(IRepositoryNodeLabelProvider labelProvider) {
        this.labelProvider = labelProvider;
    }

    public void setResourceProvider(IRepositoryNodeResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;
    }

    public IRepositoryNodeResourceProvider getResourceProvider() {
        return resourceProvider;
    }

    protected void initLabelProvider() {

    }

    protected void initContentProvider() {

    }
}
