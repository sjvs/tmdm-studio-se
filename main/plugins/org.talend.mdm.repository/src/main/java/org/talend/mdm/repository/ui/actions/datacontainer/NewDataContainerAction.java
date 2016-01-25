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
package org.talend.mdm.repository.ui.actions.datacontainer;

import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.mdm.repository.i18n.Messages;
import org.talend.mdm.repository.model.mdmproperties.MdmpropertiesFactory;
import org.talend.mdm.repository.model.mdmproperties.WSDataClusterItem;
import org.talend.mdm.repository.model.mdmserverobject.MdmserverobjectFactory;
import org.talend.mdm.repository.model.mdmserverobject.WSDataClusterE;
import org.talend.mdm.repository.ui.actions.AbstractSimpleAddAction;
import org.talend.mdm.repository.utils.RepositoryResourceUtil;

/**
 * DOC class global comment. Detailled comment <br/>
 * 
 */
public class NewDataContainerAction extends AbstractSimpleAddAction {


    public NewDataContainerAction() {
        super();
    }


    @Override
    protected String getDialogTitle() {
        return Messages.NewDataContainerAction_newDataContainer;
    }

    private WSDataClusterE newDataContainer(String key) {


        WSDataClusterE datacontainer = MdmserverobjectFactory.eINSTANCE.createWSDataClusterE();

        datacontainer.setName(key);
        datacontainer.setDescription(""); //$NON-NLS-1$
        datacontainer.setVocabulary("");//$NON-NLS-1$

        return datacontainer;
    }

    protected Item createServerObject(String key) {

        WSDataClusterItem item = MdmpropertiesFactory.eINSTANCE.createWSDataClusterItem();

        ItemState itemState = PropertiesFactory.eINSTANCE.createItemState();
        item.setState(itemState);
        //
        WSDataClusterE datacontainer = newDataContainer(key);
        item.setWsDataCluster(datacontainer);

        if (parentItem != null) {
            item.getState().setPath(parentItem.getState().getPath());
            RepositoryResourceUtil.createItem(item, key);
        }
        return item;
    }


    @Override
    protected boolean runOpenActionAfterCreation(Item item) {
        return false;
    }

}
