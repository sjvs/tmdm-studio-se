// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
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

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Event;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDIdentityConstraintDefinition;
import org.eclipse.xsd.XSDXPathDefinition;
import org.eclipse.xsd.XSDXPathVariety;
import org.eclipse.xsd.util.XSDSchemaBuildingTools;

import com.amalto.workbench.dialogs.SelectFieldDialog;
import com.amalto.workbench.editors.DataModelMainPage;
import com.amalto.workbench.image.EImage;
import com.amalto.workbench.image.ImageCache;
import com.amalto.workbench.utils.Util;

public class XSDEditXPathAction extends UndoAction {

    private static Log log = LogFactory.getLog(XSDEditXPathAction.class);

    private XSDIdentityConstraintDefinition icd = null;

    public XSDEditXPathAction(DataModelMainPage page) {
        super(page);
        setImageDescriptor(ImageCache.getImage(EImage.EDIT_OBJ.getPath()));
        setText("Edit Selector/Field");
        setToolTipText("Edit a Selector/Field");
    }

    public IStatus doAction() {
        try {
            IStructuredSelection selection = (IStructuredSelection) page.getTreeViewer().getSelection();
            XSDXPathDefinition xpath = (XSDXPathDefinition) selection.getFirstElement();
            icd = (XSDIdentityConstraintDefinition) xpath.getContainer();

            // InputDialog id = new InputDialog(
            // page.getSite().getShell(),
            // "Edit XPath",
            // "Enter a new XPath for the "+((xpath.getVariety().equals(XSDXPathVariety.FIELD_LITERAL))?"field":"selector"),
            // xpath.getValue(),
            // new IInputValidator() {
            // public String isValid(String newText) {
            // if ((newText==null) || "".equals(newText)) return "The XPath cannot be empty";
            // return null;
            // };
            // }
            // );
            List<String> childNames = Util.getChildElementNames("", (XSDElementDeclaration) icd.getContainer());
            SelectFieldDialog id = new SelectFieldDialog(page.getSite().getShell(), "Select one field", childNames,
                    xpath.getValue());
            id.create();
            id.setBlockOnOpen(true);
            int ret = id.open();
            if (ret == Window.CANCEL) {
                return Status.CANCEL_STATUS;
            }
            String field = id.getField();
            if (field.length() == 0)
                return Status.CANCEL_STATUS;
            XSDXPathDefinition newXpath = XSDSchemaBuildingTools.getXSDFactory().createXSDXPathDefinition();
            newXpath.setValue(field);
            if (xpath.getVariety().equals(XSDXPathVariety.FIELD_LITERAL)) {
                int index = icd.getFields().indexOf(xpath);
                newXpath.setVariety(XSDXPathVariety.FIELD_LITERAL);
                icd.getFields().set(index, newXpath);
            } else {
                newXpath.setVariety(XSDXPathVariety.SELECTOR_LITERAL);
                icd.setSelector(newXpath);
            }

            icd.updateElement();
            page.refresh();
            page.getTreeViewer().setSelection(new StructuredSelection(newXpath), true);
            page.markDirty();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            MessageDialog.openError(page.getSite().getShell(), "Error",
                    "An error occured trying to edit a Field/Selector: " + e.getLocalizedMessage());

            return Status.CANCEL_STATUS;
        }
        return Status.OK_STATUS;
    }

    public void runWithEvent(Event event) {
        super.runWithEvent(event);
    }

}