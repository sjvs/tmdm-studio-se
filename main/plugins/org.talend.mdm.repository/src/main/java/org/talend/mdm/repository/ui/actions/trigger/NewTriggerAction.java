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
package org.talend.mdm.repository.ui.actions.trigger;

import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.mdm.repository.i18n.Messages;
import org.talend.mdm.repository.model.mdmproperties.MdmpropertiesFactory;
import org.talend.mdm.repository.model.mdmproperties.WSRoutingRuleItem;
import org.talend.mdm.repository.model.mdmserverobject.MdmserverobjectFactory;
import org.talend.mdm.repository.model.mdmserverobject.WSRoutingRuleE;
import org.talend.mdm.repository.ui.actions.AbstractSimpleAddAction;
import org.talend.mdm.repository.utils.RepositoryResourceUtil;

/**
 * DOC class global comment. Detailled comment <br/>
 * 
 */
public class NewTriggerAction extends AbstractSimpleAddAction {


    public NewTriggerAction() {
        super();
    }


    @Override
    protected String getDialogTitle() {
        return Messages.NewTriggerAction_newTrigger;
    }

    private WSRoutingRuleE newRoutingRule(String key) {
        WSRoutingRuleE routingRule = MdmserverobjectFactory.eINSTANCE.createWSRoutingRuleE();

        routingRule.setName(key);
        routingRule.setDescription(""); //$NON-NLS-1$
        routingRule.setSynchronous(false);
        routingRule.setConcept("*"); //$NON-NLS-1$
        routingRule.setServiceJNDI(""); //$NON-NLS-1$
        routingRule.setParameters(""); //$NON-NLS-1$

        routingRule.setCondition(null);
        routingRule.setDeactive(false);

        return routingRule;
    }

    protected Item createServerObject(String key) {

        WSRoutingRuleItem item = MdmpropertiesFactory.eINSTANCE.createWSRoutingRuleItem();

        ItemState itemState = PropertiesFactory.eINSTANCE.createItemState();
        item.setState(itemState);

        WSRoutingRuleE routingRule = newRoutingRule(key);
        item.setWsRoutingRule(routingRule);

        if (parentItem != null) {
            item.getState().setPath(parentItem.getState().getPath());
            RepositoryResourceUtil.createItem(item, key);
        }
        return item;
    }


}
