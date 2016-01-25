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
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.xsd.XSDComponent;
import org.eclipse.xsd.XSDEnumerationFacet;
import org.eclipse.xsd.XSDFractionDigitsFacet;
import org.eclipse.xsd.XSDLengthFacet;
import org.eclipse.xsd.XSDMaxExclusiveFacet;
import org.eclipse.xsd.XSDMaxInclusiveFacet;
import org.eclipse.xsd.XSDMaxLengthFacet;
import org.eclipse.xsd.XSDMinExclusiveFacet;
import org.eclipse.xsd.XSDMinInclusiveFacet;
import org.eclipse.xsd.XSDMinLengthFacet;
import org.eclipse.xsd.XSDPatternFacet;
import org.eclipse.xsd.XSDSimpleTypeDefinition;
import org.eclipse.xsd.XSDTotalDigitsFacet;
import org.eclipse.xsd.XSDWhiteSpaceFacet;
import org.eclipse.xsd.util.XSDSchemaBuildingTools;

import com.amalto.workbench.dialogs.FacetsListInputDialog;
import com.amalto.workbench.dialogs.InputComboDialog;
import com.amalto.workbench.editors.DataModelMainPage;
import com.amalto.workbench.i18n.Messages;
import com.amalto.workbench.image.EImage;
import com.amalto.workbench.image.ImageCache;
import com.amalto.workbench.utils.Util;

public class XSDEditFacetAction extends UndoAction {

    private static Log log = LogFactory.getLog(XSDEditFacetAction.class);

    protected String facetName = null;

    // protected XSDSchema schema = null;
    protected XSDSimpleTypeDefinition std = null;

    protected Dialog dialog;

    protected ArrayList newValues;

    public XSDEditFacetAction(DataModelMainPage page, String facetName) {
        super(page);
        this.facetName = facetName;
        setImageDescriptor(ImageCache.getImage(EImage.FACET.getPath()));
        setText(Messages.bind(Messages.XSDEditFacetAction_Title, facetName));
        setToolTipText(Messages.bind(Messages.XSDEditFacetAction_ActionTip, facetName));
    }

    public IStatus doAction() {
        try {

            IStructuredSelection selection = (IStructuredSelection) page.getTreeViewer().getSelection();

            XSDComponent xSDCom = null;
            if (selection.getFirstElement() instanceof XSDSimpleTypeDefinition)
                std = (XSDSimpleTypeDefinition) selection.getFirstElement();
            else {
                TreePath tPath = ((TreeSelection) selection).getPaths()[0];
                for (int i = 0; i < tPath.getSegmentCount(); i++) {
                    if (tPath.getSegment(i) instanceof XSDSimpleTypeDefinition)
                        std = (XSDSimpleTypeDefinition) (tPath.getSegment(i));
                }
            }
            // std = (XSDSimpleTypeDefinition)((IStructuredSelection)selection).getFirstElement();
            /**
             * totalDigits, fractionDigits, maxInclusive, maxExclusive, minInclusive, minExclusive
             */
            if (facetName.equals("pattern")) {//$NON-NLS-1$
                editPattern();
            } else if (facetName.equals("enumeration")) {//$NON-NLS-1$
                editEnumeration();
            } else if (facetName.equals("length")) {//$NON-NLS-1$
                editLength();
            } else if (facetName.equals("minLength")) {//$NON-NLS-1$
                editMinLength();
            } else if (facetName.equals("maxLength")) {//$NON-NLS-1$
                editMaxLength();
            } else if (facetName.equals("totalDigits")) {//$NON-NLS-1$
                editTotalDigits();
            } else if (facetName.equals("fractionDigits")) {//$NON-NLS-1$
                editFractionDigits();
            } else if (facetName.equals("maxInclusive")) {//$NON-NLS-1$
                editMaxInclusive();
            } else if (facetName.equals("maxExclusive")) {//$NON-NLS-1$
                editMaxExclusive();
            } else if (facetName.equals("minInclusive")) {//$NON-NLS-1$
                editMinInclusive();
            } else if (facetName.equals("minExclusive")) {//$NON-NLS-1$
                editMinExclusive();
            } else if (facetName.equals("whiteSpace")) {//$NON-NLS-1$
                editWhiteSpace();
            }

            else {
                MessageDialog.openError(page.getSite().getShell(), Messages._Error, Messages.bind(Messages.XSDEditFacetAction_ErrorMsg1, facetName));
                return Status.CANCEL_STATUS;
            }

            std.updateElement();

            
            page.getTreeViewer().refresh(true);
            page.markDirty();
            page.refresh();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            MessageDialog.openError(page.getSite().getShell(), Messages._Error,
                    Messages.bind(Messages.XSDEditFacetAction_ErrorMsg2, e.getLocalizedMessage()));
            return Status.CANCEL_STATUS;
        }
        return Status.OK_STATUS;
    }

    public void runWithEvent(Event event) {
        super.runWithEvent(event);
    }

    private void editPattern() {
        List currentValues = std.getPatternFacets();
        ArrayList stringValues = new ArrayList();
        for (Iterator iter = currentValues.iterator(); iter.hasNext();) {
            XSDPatternFacet facet = (XSDPatternFacet) iter.next();
            stringValues.add(facet.getLexicalValue());
        }
        dialog = new FacetsListInputDialog(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                newValues = ((FacetsListInputDialog) dialog).getItems();
                dialog.close();
            }
        }, page.getSite().getShell(), Messages.XSDEditFacetAction_DialogTitle1, stringValues);

        dialog.setBlockOnOpen(true);
        int ret = dialog.open();
        if (ret == Dialog.CANCEL)
            return;

        std.getFacetContents().removeAll(currentValues);
        for (Iterator iter = newValues.iterator(); iter.hasNext();) {
            String element = (String) iter.next();
            XSDPatternFacet f = XSDSchemaBuildingTools.getXSDFactory().createXSDPatternFacet();
            f.setLexicalValue(element);
            std.getFacetContents().add(f);
        }

    }// editPattern

    private void editEnumeration() {
        List currentValues = std.getEnumerationFacets();
        ArrayList stringValues = new ArrayList();
        for (Iterator iter = currentValues.iterator(); iter.hasNext();) {
            XSDEnumerationFacet facet = (XSDEnumerationFacet) iter.next();
            stringValues.add(facet.getLexicalValue());
        }
        dialog = new FacetsListInputDialog(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                newValues = ((FacetsListInputDialog) dialog).getItems();
                dialog.close();
            }
        }, page.getSite().getShell(), Messages.XSDEditFacetAction_DialogTitle2, stringValues);

        dialog.setBlockOnOpen(true);
        int ret = dialog.open();
        if (ret == Dialog.CANCEL)
            return;

        std.getFacetContents().removeAll(currentValues);
        for (Iterator iter = newValues.iterator(); iter.hasNext();) {
            String element = (String) iter.next();
            XSDEnumerationFacet f = XSDSchemaBuildingTools.getXSDFactory().createXSDEnumerationFacet();
            f.setLexicalValue(element);
            std.getFacetContents().add(f);
        }

    }// EditEnumeration

    private void editLength() {
        XSDLengthFacet currentValue = std.getLengthFacet();
        String stringValue = "0";//$NON-NLS-1$
        if (currentValue != null)
            stringValue = currentValue.getLexicalValue();
        dialog = new InputDialog(page.getSite().getShell(), Messages.XSDEditFacetAction_DialogTitle3, Messages.XSDEditFacetAction_DialogTitle3Tip,
                stringValue == null ? "" : stringValue, new IInputValidator() {//$NON-NLS-1$

                    public String isValid(String newText) {
                        int val;
                        try {
                            val = Integer.parseInt(newText);
                        } catch (Exception e) {
                            return Messages.XSDEditFacetAction_ValueMustBeXX;
                        }
                        if (val < 0)
                            return Messages.XSDEditFacetAction_ValueMustBeXX;
                        return null;
                    }
                });

        dialog.setBlockOnOpen(true);
        int ret = dialog.open();
        if (ret == Dialog.CANCEL)
            return;

        if (currentValue != null)
            std.getFacetContents().remove(currentValue);
        int intValue = Integer.parseInt(((InputDialog) dialog).getValue());
        if (intValue > 0) {
            XSDLengthFacet f = (XSDSchemaBuildingTools.getXSDFactory()).createXSDLengthFacet();
            f.setLexicalValue("" + intValue);//$NON-NLS-1$
            std.getFacetContents().add(f);
        }

    }// EditLength

    private void editMinLength() {
        XSDMinLengthFacet currentValue = std.getMinLengthFacet();
        String stringValue = "0";//$NON-NLS-1$
        if (currentValue != null)
            stringValue = currentValue.getLexicalValue();
        dialog = new InputDialog(page.getSite().getShell(), Messages.XSDEditFacetAction_DialogTitle4, Messages.XSDEditFacetAction_DialogTitle4Tip,
                stringValue == null ? "" : stringValue, new IInputValidator() {//$NON-NLS-1$

                    public String isValid(String newText) {
                        int val;
                        try {
                            val = Integer.parseInt(newText);
                        } catch (Exception e) {
                            return Messages.XSDEditFacetAction_ValueMustBeXX;
                        }
                        if (val < 0)
                            return Messages.XSDEditFacetAction_ValueMustBeXX;
                        return null;
                    }
                });

        dialog.setBlockOnOpen(true);
        int ret = dialog.open();
        if (ret == Dialog.CANCEL)
            return;

        if (currentValue != null)
            std.getFacetContents().remove(currentValue);
        int intValue = Integer.parseInt(((InputDialog) dialog).getValue());
        if (intValue > 0) {
            XSDMinLengthFacet f = (XSDSchemaBuildingTools.getXSDFactory()).createXSDMinLengthFacet();
            f.setLexicalValue("" + intValue);//$NON-NLS-1$
            std.getFacetContents().add(f);
        }

    }// EditMinLength

    private void editMaxLength() {
        XSDMaxLengthFacet currentValue = std.getMaxLengthFacet();
        String stringValue = "0";//$NON-NLS-1$
        if (currentValue != null)
            stringValue = currentValue.getLexicalValue();
        dialog = new InputDialog(page.getSite().getShell(), Messages.XSDEditFacetAction_DialogTitle5, Messages.XSDEditFacetAction_DialogTitle4Tip,
                stringValue == null ? "" : stringValue, new IInputValidator() {//$NON-NLS-1$

                    public String isValid(String newText) {
                        int val;
                        try {
                            val = Integer.parseInt(newText);
                        } catch (Exception e) {
                            return Messages.XSDEditFacetAction_ValueMustBeXX;
                        }
                        if (val < 0)
                            return Messages.XSDEditFacetAction_ValueMustBeXX;
                        return null;
                    }
                });
        dialog.setBlockOnOpen(true);
        int ret = dialog.open();
        if (ret == Dialog.CANCEL)
            return;

        if (currentValue != null)
            std.getFacetContents().remove(currentValue);
        int intValue = Integer.parseInt(((InputDialog) dialog).getValue());
        if (intValue > 0) {
            XSDMaxLengthFacet f = (XSDSchemaBuildingTools.getXSDFactory()).createXSDMaxLengthFacet();
            f.setLexicalValue("" + intValue);//$NON-NLS-1$
            std.getFacetContents().add(f);
        }

    }// EditMaxLength

    private void editTotalDigits() {
        XSDTotalDigitsFacet currentValue = std.getTotalDigitsFacet();
        String stringValue = "0";//$NON-NLS-1$
        if (currentValue != null)
            stringValue = currentValue.getLexicalValue();
        dialog = new InputDialog(page.getSite().getShell(), Messages.XSDEditFacetAction_DialogTitle6,
                Messages.XSDEditFacetAction_DialogTitle6Tip, stringValue == null ? "" : stringValue, new IInputValidator() {//$NON-NLS-1$

                    public String isValid(String newText) {
                        int val;
                        try {
                            val = Integer.parseInt(newText);
                        } catch (Exception e) {
                            return Messages.XSDEditFacetAction_ValueMustBeXX;
                        }
                        if (val < 0)
                            return Messages.XSDEditFacetAction_ValueMustBeXX;
                        return null;
                    }
                });
        dialog.setBlockOnOpen(true);
        int ret = dialog.open();
        if (ret == Dialog.CANCEL)
            return;

        if (currentValue != null)
            std.getFacetContents().remove(currentValue);
        int intValue = Integer.parseInt(((InputDialog) dialog).getValue());
        if (intValue > 0) {
            XSDTotalDigitsFacet f = (XSDSchemaBuildingTools.getXSDFactory()).createXSDTotalDigitsFacet();
            f.setLexicalValue("" + intValue);//$NON-NLS-1$
            std.getFacetContents().add(f);
        }
    }

    private void editFractionDigits() {
        XSDFractionDigitsFacet currentValue = std.getFractionDigitsFacet();
        String stringValue = "0";//$NON-NLS-1$
        if (currentValue != null)
            stringValue = currentValue.getLexicalValue();
        dialog = new InputDialog(page.getSite().getShell(), Messages.XSDEditFacetAction_DialogTitle7,
                Messages.XSDEditFacetAction_DialogTitle7Tip, stringValue == null ? "" : stringValue, new IInputValidator() {//$NON-NLS-1$

                    public String isValid(String newText) {
                        int val;
                        try {
                            val = Integer.parseInt(newText);
                        } catch (Exception e) {
                            return Messages.XSDEditFacetAction_ValueMustBeXX;
                        }
                        if (val < 0)
                            return Messages.XSDEditFacetAction_ValueMustBeXX;
                        return null;
                    }
                });
        dialog.setBlockOnOpen(true);
        int ret = dialog.open();
        if (ret == Dialog.CANCEL)
            return;

        if (currentValue != null)
            std.getFacetContents().remove(currentValue);
        int intValue = Integer.parseInt(((InputDialog) dialog).getValue());
        if (intValue > 0) {
            XSDFractionDigitsFacet f = (XSDSchemaBuildingTools.getXSDFactory()).createXSDFractionDigitsFacet();
            f.setLexicalValue("" + intValue);//$NON-NLS-1$
            std.getFacetContents().add(f);
        }
    }

    private void editMaxInclusive() {
        XSDMaxInclusiveFacet currentValue = std.getMaxInclusiveFacet();
        String stringValue = "0";//$NON-NLS-1$
        if (currentValue != null)
            stringValue = currentValue.getLexicalValue();
        dialog = new InputDialog(page.getSite().getShell(), Messages.XSDEditFacetAction_DialogTitle8,
                Messages.XSDEditFacetAction_DialogTitle8Tip, stringValue == null ? "" : stringValue, new IInputValidator() {//$NON-NLS-1$

                    public String isValid(String newText) {
                        // double val;
                        // try {
                        // val = Double.parseDouble(newText);
                        // } catch (Exception e) {
                        // return "The value must be a non negative double";
                        // }
                        // if (val < 0)
                        // return "The value must be a non negative double";
                        // return null;
                        return isValidBoundaryNumber(std, newText);
                    }
                });
        dialog.setBlockOnOpen(true);
        int ret = dialog.open();
        if (ret == Dialog.CANCEL)
            return;

        if (currentValue != null)
            std.getFacetContents().remove(currentValue);
        // int intValue = Integer.parseInt(((InputDialog) dialog).getValue());
        if (Double.parseDouble(((InputDialog) dialog).getValue()) > 0) {
            XSDMaxInclusiveFacet f = (XSDSchemaBuildingTools.getXSDFactory()).createXSDMaxInclusiveFacet();
            // f.setLexicalValue("" + intValue);
            f.setLexicalValue("" + getValidBoundaryNumber(std, ((InputDialog) dialog).getValue()));//$NON-NLS-1$
            std.getFacetContents().add(f);
        }
    }

    private void editMaxExclusive() {
        XSDMaxExclusiveFacet currentValue = std.getMaxExclusiveFacet();
        String stringValue = "0";//$NON-NLS-1$
        if (currentValue != null)
            stringValue = currentValue.getLexicalValue();
        dialog = new InputDialog(page.getSite().getShell(), Messages.XSDEditFacetAction_DialogTitle9,
                Messages.XSDEditFacetAction_DialogTitle9Tip, stringValue == null ? "" : stringValue, new IInputValidator() {//$NON-NLS-1$

                    public String isValid(String newText) {
                        // double val;
                        // try {
                        // val = Double.parseDouble(newText);
                        // } catch (Exception e) {
                        // return "The value must be a non negative double";
                        // }
                        // if (val < 0)
                        // return "The value must be a non negative double";
                        // return null;
                        return isValidBoundaryNumber(std, newText);
                    }
                });
        dialog.setBlockOnOpen(true);
        int ret = dialog.open();
        if (ret == Dialog.CANCEL)
            return;

        if (currentValue != null)
            std.getFacetContents().remove(currentValue);
        // double intValue = Double.parseDouble(((InputDialog)dialog).getValue());
        // int intValue = Integer.parseInt(((InputDialog) dialog).getValue());
        if (Double.parseDouble(((InputDialog) dialog).getValue()) > 0) {
            XSDMaxExclusiveFacet f = (XSDSchemaBuildingTools.getXSDFactory()).createXSDMaxExclusiveFacet();
            // f.setLexicalValue("" + intValue);
            f.setLexicalValue("" + getValidBoundaryNumber(std, ((InputDialog) dialog).getValue()));//$NON-NLS-1$
            std.getFacetContents().add(f);
        }
    }

    private void editMinInclusive() {
        XSDMinInclusiveFacet currentValue = std.getMinInclusiveFacet();
        String stringValue = "0";//$NON-NLS-1$
        if (currentValue != null)
            stringValue = currentValue.getLexicalValue();
        dialog = new InputDialog(page.getSite().getShell(), Messages.XSDEditFacetAction_DialogTitle10,
                Messages.XSDEditFacetAction_DialogTitle10Tip, stringValue == null ? "" : stringValue, new IInputValidator() {//$NON-NLS-1$

                    public String isValid(String newText) {
                        // double val;
                        // try {
                        // val = Double.parseDouble(newText);
                        // } catch (Exception e) {
                        // return "The value must be a non negative double";
                        // }
                        // if (val < 0)
                        // return "The value must be a non negative double";
                        // return null;
                        return isValidBoundaryNumber(std, newText);
                    }
                });
        dialog.setBlockOnOpen(true);
        int ret = dialog.open();
        if (ret == Dialog.CANCEL)
            return;

        if (currentValue != null)
            std.getFacetContents().remove(currentValue);
        // int intValue = Integer.parseInt(((InputDialog) dialog).getValue());
        // double intValue = Double.parseDouble(((InputDialog)dialog).getValue());
        if (Double.parseDouble(((InputDialog) dialog).getValue()) > 0) {
            XSDMinInclusiveFacet f = (XSDSchemaBuildingTools.getXSDFactory()).createXSDMinInclusiveFacet();
            // f.setLexicalValue("" + intValue);
            f.setLexicalValue("" + getValidBoundaryNumber(std, ((InputDialog) dialog).getValue())); //$NON-NLS-1$
            std.getFacetContents().add(f);
        }
    }

    private void editMinExclusive() {
        XSDMinExclusiveFacet currentValue = std.getMinExclusiveFacet();
        String stringValue = "0"; //$NON-NLS-1$
        if (currentValue != null)
            stringValue = currentValue.getLexicalValue();
        dialog = new InputDialog(page.getSite().getShell(), Messages.XSDEditFacetAction_DialogTitle11,
                Messages.XSDEditFacetAction_DialogTitle11Tip, stringValue == null ? "" : stringValue, new IInputValidator() {//$NON-NLS-1$

                    public String isValid(String newText) {
                        // double val;
                        // try {
                        // val = Double.parseDouble(newText);
                        // } catch (Exception e) {
                        // return "The value must be a non negative double";
                        // }
                        // if (val < 0)
                        // return "The value must be a non negative double";
                        // return null;
                        return isValidBoundaryNumber(std, newText);
                    }
                });
        dialog.setBlockOnOpen(true);
        int ret = dialog.open();
        if (ret == Dialog.CANCEL)
            return;

        if (currentValue != null)
            std.getFacetContents().remove(currentValue);
        // int intValue = Integer.parseInt(((InputDialog) dialog).getValue());
        // double intValue = Double.parseDouble(((InputDialog)dialog).getValue());
        if (Double.parseDouble(((InputDialog) dialog).getValue()) > 0) {
            XSDMinExclusiveFacet f = (XSDSchemaBuildingTools.getXSDFactory()).createXSDMinExclusiveFacet();
            // f.setLexicalValue("" + intValue);
            f.setLexicalValue("" + getValidBoundaryNumber(std, ((InputDialog) dialog).getValue())); //$NON-NLS-1$
            std.getFacetContents().add(f);
        }
    }

    private void editWhiteSpace() {
        String[] values = { "preserve", "replace", "collapse" };//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
        XSDWhiteSpaceFacet currentValue = std.getWhiteSpaceFacet();
        String stringValue = "preserve";//$NON-NLS-1$
        if (currentValue != null)
            stringValue = currentValue.getLexicalValue();
        dialog = new InputComboDialog(page.getSite().getShell(), Messages.XSDEditFacetAction_DialogTitle12, Messages.XSDEditFacetAction_DialogTitle12Tip, values,
                stringValue,null);
        dialog.setBlockOnOpen(true);
        int ret = dialog.open();
        if (ret == Dialog.CANCEL)
            return;

        if (currentValue != null)
            std.getFacetContents().remove(currentValue);
        String stirngValue = ((InputComboDialog) dialog).getValue();
        if (stirngValue != null && stirngValue.length() > 0) {
            XSDWhiteSpaceFacet f = (XSDSchemaBuildingTools.getXSDFactory()).createXSDWhiteSpaceFacet();
            f.setLexicalValue("" + stirngValue);//$NON-NLS-1$
            std.getFacetContents().add(f);
        }
    }

    private Object getValidBoundaryNumber(XSDSimpleTypeDefinition type, String text) {

        try {
            if (Util.isDouble(type) || Util.isDecimal(type))
                return Double.parseDouble(text);

            if (Util.isFloat(type))
                return Float.parseFloat(text);

            return Integer.parseInt(text);
        } catch (Exception e) {
            return text;
        }

    }

    private String isValidBoundaryNumber(XSDSimpleTypeDefinition type, String text) {

        if (Util.isDouble(type) || Util.isDecimal(type))
            return isValidBoundaryDoubleNumber(text);

        if (Util.isFloat(type))
            return isValidBoundaryFloatNumber(text);

        return isValidBoundaryIntNumber(text);
    }

    private String isValidBoundaryIntNumber(String text) {

        try {
            int tempVal = Integer.parseInt(text);

            if (tempVal < 0)
                return Messages.XSDEditFacetAction_ValueMustBeXI;

        } catch (Exception e) {
            return Messages.XSDEditFacetAction_ValueMustBeXI;
        }

        return null;

    }

    private String isValidBoundaryFloatNumber(String text) {

        try {
            Float tempVal = Float.parseFloat(text);

            if (tempVal < 0)
                return Messages.XSDEditFacetAction_ValueMustBeXF;

        } catch (Exception e) {
            return Messages.XSDEditFacetAction_ValueMustBeXF;
        }

        return null;
    }

    private String isValidBoundaryDoubleNumber(String text) {

        try {
            Double tempVal = Double.parseDouble(text);

            if (tempVal < 0)
                return Messages.XSDEditFacetAction_ValueMustBeXD;

        } catch (Exception e) {
            return Messages.XSDEditFacetAction_ValueMustBeXD;
        }

        return null;
    }
}
