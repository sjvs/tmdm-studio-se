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
package org.talend.mdm.repository.ui.actions.storedprocedure;

import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.mdm.repository.i18n.Messages;
import org.talend.mdm.repository.model.mdmproperties.MdmpropertiesFactory;
import org.talend.mdm.repository.model.mdmproperties.WSStoredProcedureItem;
import org.talend.mdm.repository.model.mdmserverobject.MdmserverobjectFactory;
import org.talend.mdm.repository.model.mdmserverobject.WSStoredProcedureE;
import org.talend.mdm.repository.ui.actions.AbstractSimpleAddAction;
import org.talend.mdm.repository.utils.RepositoryResourceUtil;

/**
 * DOC class global comment. Detailled comment <br/>
 * 
 */
public class NewStoredProcedureAction extends AbstractSimpleAddAction {


    public NewStoredProcedureAction() {
        super();
    }


    @Override
    protected String getDialogTitle() {
        return Messages.NewStoredProcedureAction_newStoredProcedure;
    }

    private WSStoredProcedureE newStoredProcedure(String key) {


        WSStoredProcedureE storedProcedure = MdmserverobjectFactory.eINSTANCE.createWSStoredProcedureE();

        storedProcedure.setName(key);
        storedProcedure.setDescription(""); //$NON-NLS-1$
        storedProcedure.setProcedure("");//$NON-NLS-1$
        storedProcedure.setRefreshCache(false);

        return storedProcedure;
    }

    protected Item createServerObject(String key) {

        WSStoredProcedureItem item = MdmpropertiesFactory.eINSTANCE.createWSStoredProcedureItem();

        ItemState itemState = PropertiesFactory.eINSTANCE.createItemState();
        item.setState(itemState);

        WSStoredProcedureE storedProcedure = newStoredProcedure(key);
        item.setWsStoredProcedure(storedProcedure);

        if (parentItem != null) {
            item.getState().setPath(parentItem.getState().getPath());
            RepositoryResourceUtil.createItem(item, key);
        }
        return item;
    }


}
