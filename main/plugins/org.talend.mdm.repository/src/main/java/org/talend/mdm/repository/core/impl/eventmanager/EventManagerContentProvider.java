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
package org.talend.mdm.repository.core.impl.eventmanager;

import java.util.LinkedList;
import java.util.List;

import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.mdm.repository.core.IRepositoryNodeConfiguration;
import org.talend.mdm.repository.core.IServerObjectRepositoryType;
import org.talend.mdm.repository.core.impl.AbstractContentProvider;
import org.talend.mdm.repository.extension.RepositoryNodeConfigurationManager;
import org.talend.mdm.repository.utils.RepositoryResourceUtil;

public class EventManagerContentProvider extends AbstractContentProvider {

    private List<IRepositoryViewObject> result;

    /**
     * DOC hbhong EventManagerContentProvider constructor comment.
     */
    public EventManagerContentProvider() {

    }

    @Override
    protected List<IRepositoryViewObject> getViewObjFromSystemFolder(Item parentItem) {
        if (result == null) {
            result = new LinkedList<IRepositoryViewObject>();
            IRepositoryNodeConfiguration processConf = RepositoryNodeConfigurationManager
                    .getConfiguration(IServerObjectRepositoryType.TYPE_TRANSFORMERV2);
            IRepositoryNodeConfiguration triggerConf = RepositoryNodeConfigurationManager
                    .getConfiguration(IServerObjectRepositoryType.TYPE_ROUTINGRULE);

            //
            addCategoryViewObject(result, processConf);
            addCategoryViewObject(result, triggerConf);
        }
        return result;
    }

    private void addCategoryViewObject(List<IRepositoryViewObject> result, IRepositoryNodeConfiguration conf) {
        if (conf != null) {
            IRepositoryViewObject categoryViewObject = RepositoryResourceUtil.getCategoryViewObject(conf);
            if (categoryViewObject != null) {
                result.add(categoryViewObject);
            }
        }
    }

    public Class getWSObjectClass() {
        return null;
    }

}
