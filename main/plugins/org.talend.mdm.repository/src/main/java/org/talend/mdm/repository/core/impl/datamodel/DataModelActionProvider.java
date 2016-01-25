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
package org.talend.mdm.repository.core.impl.datamodel;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.navigator.CommonViewer;
import org.talend.core.model.properties.FolderType;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.mdm.repository.core.AbstractRepositoryAction;
import org.talend.mdm.repository.core.IServerObjectRepositoryType;
import org.talend.mdm.repository.core.impl.RepositoryNodeActionProviderAdapter;
import org.talend.mdm.repository.model.mdmproperties.MDMServerObjectItem;
import org.talend.mdm.repository.ui.actions.datamodel.NewDataModelAction;
import org.talend.mdm.repository.ui.editors.IRepositoryViewEditorInput;
import org.talend.mdm.repository.ui.editors.XSDEditorInput2;
import org.talend.mdm.repository.utils.RepositoryResourceUtil;

/**
 * DOC hbhong class global comment. Detailled comment <br/>
 * 
 */
public class DataModelActionProvider extends RepositoryNodeActionProviderAdapter {

    static Logger log = Logger.getLogger(DataModelActionProvider.class);

    AbstractRepositoryAction addAction;

    @Override
    public void initCommonViewer(CommonViewer commonViewer) {
        super.initCommonViewer(commonViewer);
        addAction = new NewDataModelAction();

        //
        addAction.initCommonViewer(commonViewer);
        //
        // if (Util.IsEnterPrise()) {
        // deployMatchRuleAction = new DeployMatchRuleAction();
        // deployMatchRuleAction.initCommonViewer(commonViewer);
        // undeployMatchRuleAction = new UndeployMatchRuleAction();
        // undeployMatchRuleAction.initCommonViewer(commonViewer);
        // }

    }

    @Override
    public List<AbstractRepositoryAction> getActions(IRepositoryViewObject viewObj) {
        List<AbstractRepositoryAction> actions = super.getActions(viewObj);
        if (RepositoryResourceUtil.hasContainerItem(viewObj, FolderType.SYSTEM_FOLDER_LITERAL, FolderType.FOLDER_LITERAL)) {
            addAction(actions, addAction, viewObj);
        }

        if (viewObj.getProperty().getItem() instanceof MDMServerObjectItem) {
            addAction(actions, renameAction, viewObj);
            // validate
            if (validateAction != null) {
                addAction(actions, validateAction, viewObj);
            }
            // deploy
            addAction(actions, deployToAction, viewObj);
            addAction(actions, deployToLastServerAction, viewObj);
            addAction(actions, deployAnotherToAction, viewObj);
            addAction(actions, undeployAction, viewObj);
            // if (Util.IsEnterPrise()) {
            // addAction(actions, deployMatchRuleAction, viewObj);
            // addAction(actions, undeployMatchRuleAction, viewObj);
            //
            // }
        }
        addAction(actions, deployAllAction, viewObj);
        return actions;
    }

    @Override
    public IRepositoryViewEditorInput getOpenEditorInput(IRepositoryViewObject viewObj) {
        try {
            Item item = viewObj.getProperty().getItem();
            IFile file = RepositoryResourceUtil.findReferenceFile(IServerObjectRepositoryType.TYPE_DATAMODEL, item, "xsd"); //$NON-NLS-1$
            return new XSDEditorInput2(viewObj, file);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
