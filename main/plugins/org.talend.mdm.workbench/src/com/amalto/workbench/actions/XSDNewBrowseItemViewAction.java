// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package com.amalto.workbench.actions;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xsd.XSDElementDeclaration;

import com.amalto.workbench.dialogs.AddBrowseItemsWizard;
import com.amalto.workbench.editors.DataModelMainPage;
import com.amalto.workbench.i18n.Messages;
import com.amalto.workbench.image.EImage;
import com.amalto.workbench.image.ImageCache;
import com.amalto.workbench.utils.Util;

public class XSDNewBrowseItemViewAction extends AbstractXSDNewAction {

    private static Log log = LogFactory.getLog(XSDNewBrowseItemViewAction.class);

    protected DataModelMainPage page;

    private List<XSDElementDeclaration> declList = new ArrayList<XSDElementDeclaration>();

    public XSDNewBrowseItemViewAction(DataModelMainPage page) {
        super();

        this.page = page;
        setImageDescriptor(ImageCache.getImage(EImage.ADD_OBJ.getPath()));
        setText(Messages.XSDNewBrowseItemViewAction_Text);
        setToolTipText(Messages.XSDNewBrowseItemViewAction_Text);
    }

    @Override
    public void doRun() {
        IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IEditorPart part = activePage.getActiveEditor();
        if (part.isDirty()) {
            boolean save = MessageDialog.openConfirm(page.getSite().getShell(), Messages.SaveResource,
                    Messages.bind(Messages.modifiedChanges, page.getXObject().getDisplayName()));
            if (save) {
                activePage.saveEditor(part, false);
            } else {
                return;
            }
        }
        IStructuredSelection selection = (IStructuredSelection) page.getTreeViewer().getSelection();
        declList.clear();
        List list = selection.toList();
        for (Object obj : list) {
            if (obj instanceof XSDElementDeclaration) {
                XSDElementDeclaration declaration = (XSDElementDeclaration) obj;
                if (Util.getParent(obj) == obj) {
                    declList.add(declaration);
                }
            }
        }

        if (!declList.isEmpty()) {
            //
            AddBrowseItemsWizard wizard = getAddBrowseItemsWizard(declList);
            WizardDialog dialog = new WizardDialog(page.getSite().getShell(), wizard);
            dialog.open();
        }
    }

    private AddBrowseItemsWizard getAddBrowseItemsWizard(List<XSDElementDeclaration> declList) {
        Object object = page.getAdapter(AddBrowseItemsWizard.class);
        if (object != null) {
            AddBrowseItemsWizard wizard = (AddBrowseItemsWizard) object;
            wizard.setDeclarations(declList);
            return wizard;
        } else {
            return new AddBrowseItemsWizard(page, declList);
        }
    }

    @Override
    public void runWithEvent(Event event) {
        super.runWithEvent(event);
    }
}
