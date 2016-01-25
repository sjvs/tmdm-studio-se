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
package org.talend.mdm.repository.core;

import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;

/**
 * DOC hbhong class global comment. Detailled comment <br/>
 * 
 */
public interface IRepositoryNodeConfiguration {

    public boolean canHandleRepObjType(ERepositoryObjectType type);

    public boolean canHandleItem(Item item);

    // resource
    public void setResourceProvider(IRepositoryNodeResourceProvider resourceProvider);

    public IRepositoryNodeResourceProvider getResourceProvider();

    // label
    public IRepositoryNodeLabelProvider getLabelProvider();

    public void setLabelProvider(IRepositoryNodeLabelProvider labelProvider);

    // content
    public IRepositoryNodeContentProvider getContentProvider();

    public void setContentProvider(IRepositoryNodeContentProvider contentProvider);

    // action
    public IRepositoryNodeActionProvider getActionProvider();

    public void setActionProvider(IRepositoryNodeActionProvider actionProvider);

}
