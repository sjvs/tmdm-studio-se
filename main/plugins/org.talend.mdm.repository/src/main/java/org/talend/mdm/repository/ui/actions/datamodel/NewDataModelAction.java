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
package org.talend.mdm.repository.ui.actions.datamodel;

import java.util.Properties;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.intro.IIntroSite;
import org.eclipse.ui.intro.config.IIntroAction;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.ui.branding.IBrandingConfiguration;
import org.talend.mdm.repository.core.IServerObjectRepositoryType;
import org.talend.mdm.repository.core.service.ContainerCacheService;
import org.talend.mdm.repository.core.service.IMatchRuleMapInfoService;
import org.talend.mdm.repository.i18n.Messages;
import org.talend.mdm.repository.model.mdmproperties.ContainerItem;
import org.talend.mdm.repository.model.mdmproperties.MdmpropertiesFactory;
import org.talend.mdm.repository.model.mdmproperties.WSDataModelItem;
import org.talend.mdm.repository.model.mdmserverobject.MdmserverobjectFactory;
import org.talend.mdm.repository.model.mdmserverobject.WSDataModelE;
import org.talend.mdm.repository.models.FolderRepositoryObject;
import org.talend.mdm.repository.ui.actions.AbstractSimpleAddAction;
import org.talend.mdm.repository.ui.actions.datacontainer.NewDataContainerAction;
import org.talend.mdm.repository.utils.RepositoryResourceUtil;
import org.talend.mdm.repository.utils.ServiceUtil;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * DOC hbhong class global comment. Detailled comment <br/>
 * 
 */
public class NewDataModelAction extends AbstractSimpleAddAction implements IIntroAction {

    /**
     * DOC hbhong NewDataModelAction constructor comment.
     * 
     * @param text
     */
    public NewDataModelAction() {
        super();
    }

    boolean needCreateDataContainer;

    @Override
    protected String getDialogTitle() {
        return Messages.NewDataModelAction_newDataModel;
    }

    private WSDataModelE newBlankDataModel(String key) {

        WSDataModelE dataModel = MdmserverobjectFactory.eINSTANCE.createWSDataModelE();
        dataModel.setName(key);
        //
        String defaultXSD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"//$NON-NLS-1$
                + "<xsd:schema xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"/>"; //$NON-NLS-1$

        dataModel.setXsdSchema(defaultXSD);
        //
        return dataModel;
    }

    @Override
    protected Item createServerObject(String key) {

        WSDataModelItem item = MdmpropertiesFactory.eINSTANCE.createWSDataModelItem();
        ItemState itemState = PropertiesFactory.eINSTANCE.createItemState();
        item.setState(itemState);
        //
        WSDataModelE dataModel = newBlankDataModel(key);
        item.setWsDataModel(dataModel);

        if (parentItem != null) {
            item.getState().setPath(parentItem.getState().getPath());
            RepositoryResourceUtil.createItem(item, key);
            createMatchRuleMapInfo(item);
        }

        if (needCreateDataContainer) {
            createDataContainerObject(key);
        }
        return item;
    }

    private void createMatchRuleMapInfo(WSDataModelItem item) {
        IMatchRuleMapInfoService mapInfoService = ServiceUtil.getService(IMatchRuleMapInfoService.class);
        if (mapInfoService != null) {
            mapInfoService.loadMatchRuleMapInfo(item);
        }
    }

    protected void createDataContainerObject(final String key) {
        if (!RepositoryResourceUtil.isExistByName(IServerObjectRepositoryType.TYPE_DATACLUSTER, key)) {
            NewDataContainerAction newDataContainerAction = new NewDataContainerAction() {

                @Override
                protected String getInputName() {
                    return key;
                }

                @Override
                protected void updateParentItem() {
                    IRepositoryViewObject folderViewObj = ContainerCacheService.get(IServerObjectRepositoryType.TYPE_DATACLUSTER,
                            ""); //$NON-NLS-1$
                    if (folderViewObj != null) {
                        Item pItem = folderViewObj.getProperty().getItem();
                        setParentItem((ContainerItem) pItem);

                    }
                }

            };
            newDataContainerAction.run();
        }
    }

    private boolean noSelection = false;

    @Override
    public void setParentItem(ContainerItem parentItem) {
        this.parentItem = parentItem;
        noSelection = true;
    }

    @Override
    protected void updateParentItem() {
        if (noSelection) {
            return;
        }

        super.updateParentItem();
    }

    public void run(IIntroSite site, Properties params) {

        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        if (factory.isUserReadOnlyOnCurrentProject()) {
            MessageDialog.openWarning(null, Messages.NewDataModelAction_UserAuthority,
                    Messages.NewDataModelAction_CanNotCreateModel);
        } else {
            PlatformUI.getWorkbench().getIntroManager().closeIntro(PlatformUI.getWorkbench().getIntroManager().getIntro());
            IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
            if (null == workbenchWindow) {
                return;
            }
            IWorkbenchPage workbenchPage = workbenchWindow.getActivePage();
            if (null == workbenchPage) {
                return;
            }

            IPerspectiveDescriptor currentPerspective = workbenchPage.getPerspective();
            if (!IBrandingConfiguration.PERSPECTIVE_MDM_ID.equals(currentPerspective.getId())) {
                // show mdm perspective
                try {
                    workbenchWindow.getWorkbench().showPerspective(IBrandingConfiguration.PERSPECTIVE_MDM_ID, workbenchWindow);
                    workbenchPage = workbenchWindow.getActivePage();
                } catch (WorkbenchException e) {
                    ExceptionHandler.process(e);
                    return;
                }
            }
            //
            IRepositoryViewObject folderViewObj = ContainerCacheService.get(IServerObjectRepositoryType.TYPE_DATAMODEL, ""); //$NON-NLS-1$
            selectObj = folderViewObj;
            if (folderViewObj != null && folderViewObj instanceof FolderRepositoryObject) {
                Item pItem = folderViewObj.getProperty().getItem();
                if (pItem instanceof ContainerItem) {
                    setParentItem((ContainerItem) pItem);
                }
            }
            run();
        }

    }

    @Override
    protected void extendDialogArea(Control area) {
        needCreateDataContainer = true;
        Composite composite = (Composite) area;
        final Button createDCBun = new Button(composite, SWT.CHECK);
        createDCBun.setText(Messages.NewDataModelAction_createDataContainer);
        createDCBun.setSelection(needCreateDataContainer);
        createDCBun.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                needCreateDataContainer = createDCBun.getSelection();
            }

        });
    }
}
