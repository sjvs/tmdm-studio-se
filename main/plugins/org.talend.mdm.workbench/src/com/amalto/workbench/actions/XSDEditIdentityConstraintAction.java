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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Event;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDIdentityConstraintCategory;
import org.eclipse.xsd.XSDIdentityConstraintDefinition;

import com.amalto.workbench.editors.DataModelMainPage;
import com.amalto.workbench.i18n.Messages;
import com.amalto.workbench.image.EImage;
import com.amalto.workbench.image.ImageCache;
import com.amalto.workbench.utils.inputvalidator.EditXSDIdentityConstraintNameValidator;

public class XSDEditIdentityConstraintAction extends UndoAction {

    private static Log log = LogFactory.getLog(XSDEditIdentityConstraintAction.class);

    protected XSDIdentityConstraintDefinition constraint;

    public XSDEditIdentityConstraintAction(DataModelMainPage page) {
        super(page);
        setImageDescriptor(ImageCache.getImage(EImage.EDIT_OBJ.getPath()));
        setText(Messages.XSDEditIdentityXX_EditKey);
        setToolTipText(Messages.XSDEditIdentityXX_EditAKey);
    }

    public IStatus doAction() {
        try {

            ISelection selection = page.getTreeViewer().getSelection();
            constraint = (XSDIdentityConstraintDefinition) ((IStructuredSelection) selection).getFirstElement();
            String oldName = constraint.getName();

            InputDialog id = new InputDialog(page.getSite().getShell(), Messages.XSDEditIdentityXX_EditKey, Messages.XSDEditIdentityXX_EnterANameForKey, oldName,
                    new EditXSDIdentityConstraintNameValidator(constraint)
            // new IInputValidator() {
            // public String isValid(String newText) {
            // if ((newText==null) || "".equals(newText)) return "The Entity Name cannot be empty";
            // XSDSchema schema = XSDEditIdentityConstraintAction.this.constraint.getSchema();
            // EList list = schema.getIdentityConstraintDefinitions();
            // for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            // XSDIdentityConstraintDefinition icd = (XSDIdentityConstraintDefinition) iter.next();
            // if (icd.getName().equals(newText)) return "This Key already exists";
            // }
            // return null;
            // };
            // }
            );

            id.setBlockOnOpen(true);
            int ret = id.open();
            if (ret == Dialog.CANCEL) {
                return Status.CANCEL_STATUS;
            }

            if (XSDIdentityConstraintCategory.UNIQUE_LITERAL.equals(constraint.getIdentityConstraintCategory())
                    && !((XSDElementDeclaration) constraint.getContainer()).getName().equals(id.getValue())) {

                MessageDialog.openWarning(page.getSite().getShell(), Messages.Warning,
                        Messages.XSDEditIdentityXX_WarningMsg);
                return Status.CANCEL_STATUS;
            }

            constraint.setName(id.getValue());
            constraint.updateElement();

            page.refresh();
            page.markDirty();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            MessageDialog.openError(page.getSite().getShell(), Messages._Error,
                    Messages.bind(Messages.XSDEditIdentityXX_ErrorEditEntity, e.getLocalizedMessage()));
            return Status.CANCEL_STATUS;
        }
        return Status.OK_STATUS;
    }

    public void runWithEvent(Event event) {
        super.runWithEvent(event);
    }

}
