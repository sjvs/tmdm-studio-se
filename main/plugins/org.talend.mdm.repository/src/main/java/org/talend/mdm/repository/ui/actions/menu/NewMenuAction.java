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
package org.talend.mdm.repository.ui.actions.menu;

import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.mdm.repository.i18n.Messages;
import org.talend.mdm.repository.model.mdmproperties.MdmpropertiesFactory;
import org.talend.mdm.repository.model.mdmproperties.WSMenuItem;
import org.talend.mdm.repository.model.mdmserverobject.MdmserverobjectFactory;
import org.talend.mdm.repository.model.mdmserverobject.WSMenuE;
import org.talend.mdm.repository.model.mdmserverobject.WSMenuEntryE;
import org.talend.mdm.repository.model.mdmserverobject.WSMenuMenuEntriesDescriptionsE;
import org.talend.mdm.repository.ui.actions.AbstractSimpleAddAction;
import org.talend.mdm.repository.utils.RepositoryResourceUtil;

/**
 * DOC hbhong class global comment. Detailled comment <br/>
 * 
 */
public class NewMenuAction extends AbstractSimpleAddAction {

    /**
     * DOC hbhong AddMenu constructor comment.
     * 
     * @param text
     */
    public NewMenuAction() {
        super();
    }


    @Override
    protected String getDialogTitle() {
        return Messages.NewMenuAction_newMenu;
    }

    private WSMenuE newBlankMenu(String key) {

        WSMenuMenuEntriesDescriptionsE descriptions = MdmserverobjectFactory.eINSTANCE.createWSMenuMenuEntriesDescriptionsE();
        descriptions.setLabel(key);
        descriptions.setLanguage("en"); //$NON-NLS-1$
        //

        WSMenuEntryE entry = MdmserverobjectFactory.eINSTANCE.createWSMenuEntryE();
        entry.getDescriptions().add(descriptions);
        entry.setId(key);
        //
        WSMenuE menu = MdmserverobjectFactory.eINSTANCE.createWSMenuE();
        menu.setName(key);
        menu.getMenuEntries().add(entry);
        //
        return menu;
    }

    protected Item createServerObject(String key) {

        WSMenuItem item = MdmpropertiesFactory.eINSTANCE.createWSMenuItem();
        ItemState itemState = PropertiesFactory.eINSTANCE.createItemState();
        item.setState(itemState);
        //
        WSMenuE menu = newBlankMenu(key);
        item.setWsMenu(menu);

        if (parentItem != null) {
            item.getState().setPath(parentItem.getState().getPath());
            RepositoryResourceUtil.createItem(item, key);
        }
        return item;
    }


}
