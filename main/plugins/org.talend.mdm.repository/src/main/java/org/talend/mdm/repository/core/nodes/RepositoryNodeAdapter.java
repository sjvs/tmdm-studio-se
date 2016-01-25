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
package org.talend.mdm.repository.core.nodes;

import org.talend.commons.ui.runtime.image.IImage;
import org.talend.core.repository.IExtendRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC hbhong class global comment. Detailled comment <br/>
 * 
 */
public class RepositoryNodeAdapter implements IExtendRepositoryNode {

    private final IImage image;

    private final int ordinal;

    protected final static RepositoryNode[] EMPTY_NODE_ARRAY = new RepositoryNode[0];

    public RepositoryNodeAdapter(IImage image, int ordinal) {
        this.image = image;
        this.ordinal = ordinal;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.repository.IExtendRepositoryNode#getNodeImage()
     */

    public IImage getNodeImage() {
        return image;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.repository.IExtendRepositoryNode#getOrdinal()
     */

    public int getOrdinal() {
        return ordinal;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.repository.IExtendRepositoryNode#getChildren()
     */

    public Object[] getChildren() {
        return EMPTY_NODE_ARRAY;
    }

}
