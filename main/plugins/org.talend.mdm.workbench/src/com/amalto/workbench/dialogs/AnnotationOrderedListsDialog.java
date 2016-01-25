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
package com.amalto.workbench.dialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.xsd.XSDAnnotation;
import org.eclipse.xsd.XSDComponent;
import org.eclipse.xsd.XSDElementDeclaration;
import org.w3c.dom.Element;

import com.amalto.workbench.editors.DataModelMainPage;
import com.amalto.workbench.i18n.Messages;
import com.amalto.workbench.image.EImage;
import com.amalto.workbench.image.ImageCache;
import com.amalto.workbench.models.TreeObject;
import com.amalto.workbench.utils.Util;

public class AnnotationOrderedListsDialog extends Dialog {

    private static Log log = LogFactory.getLog(AnnotationOrderedListsDialog.class);

    protected Control textControl;

    protected Button checkBox;

    // protected Text labelText;
    // protected Combo combo;
    protected TableViewer viewer;

    private List<String> roles;

    protected ArrayList<String> xPaths = new ArrayList<String>();

    private SelectionListener caller = null;

    private String title = "";//$NON-NLS-1$

    private String columnName = "";//$NON-NLS-1$

    private boolean recursive = true;

    private boolean lock;

    private boolean retrieveFKinfos = true;

    public boolean isRetrieveFKinfos() {
        return retrieveFKinfos;
    }

    public void setRetrieveFKinfos(boolean retrieveFKinfos) {
        this.retrieveFKinfos = retrieveFKinfos;
    }

    private DataModelMainPage parentPage = null;

    private TreeObject xObject = null;

    private int actionType;

    private String dataModelName;

    public static final int AnnotationForeignKeyInfo_ActionType = 1 << 0;

    public static final int AnnotationHidden_ActionType = 1 << 1;

    public static final int AnnotationTargetSystems_ActionType = 1 << 2;

    public static final int AnnotationWrite_ActionType = 1 << 3;

    public static final int AnnotationSchematron_ActionType = 1 << 4;

    public static final int AnnotationLookupField_ActionType = 1 << 5;

    public static final int AnnotationPrimaKeyInfo_ActionType = 1 << 6;

    /**
     * @param parentShell
     */
    public AnnotationOrderedListsDialog(ArrayList<String> xPaths, SelectionListener caller, Shell parentShell, String title,
            String columnName, DataModelMainPage parentPage, int actionType, String dataModelName) {
        super(parentShell);
        setShellStyle(this.getShellStyle() | SWT.RESIZE);
        this.xPaths = xPaths;
        this.caller = caller;
        this.title = title;
        this.columnName = columnName;
        this.parentPage = parentPage;
        this.xObject = parentPage.getXObject();
        this.actionType = actionType;
        this.dataModelName = dataModelName;
    }

    private List<String> getConceptElements() {
        DataModelMainPage page = parentPage;
        IStructuredSelection selection = (IStructuredSelection) page.getTreeViewer().getSelection();
        List<String> childNames = new ArrayList<String>();
        XSDElementDeclaration decl = null;
        if (selection.getFirstElement() instanceof XSDElementDeclaration) {
            decl = (XSDElementDeclaration) selection.getFirstElement();
            // childNames = Util.getChildElementNames(decl.getElement());
        } else if (selection.getFirstElement() instanceof Element) {
            TreePath tPath = ((TreeSelection) selection).getPaths()[0];
            XSDComponent xSDCom = null;
            for (int i = 0; i < tPath.getSegmentCount(); i++) {
                if (tPath.getSegment(i) instanceof XSDAnnotation) {
                    xSDCom = (XSDAnnotation) (tPath.getSegment(i));
                    break;
                }
            }
            decl = (XSDElementDeclaration) xSDCom.getContainer();
        }
        try {
            childNames = Util.getChildElementNames(decl.getName(), decl);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return childNames;
    }

    protected List<String> getAllRolesStr() {
        return Util.getChildren(this.xObject.getServerRoot(), TreeObject.ROLE);
    }

    @Override
    protected Control createDialogArea(Composite parent) {

        // Should not really be here but well,....

        parent.getShell().setText(this.title);

        Composite composite = (Composite) super.createDialogArea(parent);

        GridLayout layout = (GridLayout) composite.getLayout();
        layout.numColumns = 3;
        layout.makeColumnsEqualWidth = false;
        // layout.verticalSpacing = 10;

        if (actionType == AnnotationWrite_ActionType || actionType == AnnotationHidden_ActionType) {
            textControl = new CCombo(composite, SWT.BORDER | SWT.READ_ONLY);

            // roles=Util.getCachedXObjectsNameSet(this.xObject, TreeObject.ROLE);
            roles = getAllRolesStr();
            ((CCombo) textControl).setItems(roles.toArray(new String[roles.size()]));

        } else if (actionType == AnnotationLookupField_ActionType || actionType == AnnotationPrimaKeyInfo_ActionType) {
            textControl = new CCombo(composite, SWT.BORDER | SWT.READ_ONLY);

            // roles=Util.getCachedXObjectsNameSet(this.xObject, TreeObject.ROLE);
            roles = getConceptElements();
            ((CCombo) textControl).setItems(roles.toArray(new String[roles.size()]));

        } else {
            if (actionType == AnnotationOrderedListsDialog.AnnotationSchematron_ActionType) {
                textControl = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
            } else {
                textControl = new Text(composite, SWT.BORDER | SWT.SINGLE);
            }
        }

        if (actionType == AnnotationOrderedListsDialog.AnnotationForeignKeyInfo_ActionType) {
            textControl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        } else {
            if (actionType == AnnotationOrderedListsDialog.AnnotationSchematron_ActionType) {
                textControl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 7));
            } else {
                textControl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
            }
        }

        ((GridData) textControl.getLayoutData()).minimumWidth = 400;

        textControl.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
                if ((e.stateMask == 0) && (e.character == SWT.CR)) {
                    xPaths.add(AnnotationOrderedListsDialog.getControlText(textControl));
                    viewer.refresh();
                }
            }
        });

        if (actionType == AnnotationOrderedListsDialog.AnnotationForeignKeyInfo_ActionType) {
            Button xpathButton = new Button(composite, SWT.PUSH | SWT.CENTER);
            xpathButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
            xpathButton.setText("...");//$NON-NLS-1$
            xpathButton.setToolTipText(Messages.AnnotationOrderedListsDialog_SelectXpath);
            xpathButton.addSelectionListener(new SelectionListener() {

                public void widgetDefaultSelected(SelectionEvent e) {

                }

                public void widgetSelected(SelectionEvent e) {
                    XpathSelectDialog dlg = getNewXpathSelectDialog(parentPage, dataModelName);

                    dlg.setLock(lock);

                    dlg.setBlockOnOpen(true);

                    dlg.open();

                    if (dlg.getReturnCode() == Window.OK) {
                        ((Text) textControl).setText(dlg.getXpath());
                        dlg.close();
                    }

                }

            });

        }

        Button addLabelButton = new Button(composite, SWT.PUSH);
        addLabelButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        // addLabelButton.setText("Set");
        addLabelButton.setImage(ImageCache.getCreatedImage(EImage.ADD_OBJ.getPath()));
        addLabelButton.setToolTipText(Messages.AnnotationOrderedListsDialog_Add);
        addLabelButton.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            };

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                boolean exist = false;
                for (String string : xPaths) {
                    if (string.equals(getControlText(textControl))) {
                        exist = true;
                    }
                }
                if (!exist && getControlText(textControl) != null && getControlText(textControl) != "") {
                    xPaths.add(getControlText(textControl));
                }
                viewer.refresh();
            };
        });

        final String COLUMN = columnName;

        viewer = new TableViewer(composite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
        viewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        ((GridData) viewer.getControl().getLayoutData()).heightHint = 100;
        // Set up the underlying table
        Table table = viewer.getTable();
        // table.setLayoutData(new GridData(GridData.FILL_BOTH));

        new TableColumn(table, SWT.CENTER).setText(COLUMN);
        table.getColumn(0).setWidth(500);
        for (int i = 1, n = table.getColumnCount(); i < n; i++) {
            table.getColumn(i).pack();
        }

        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        // Create the cell editors --> We actually discard those later: not natural for an user
        CellEditor[] editors = new CellEditor[1];
        if (actionType == AnnotationOrderedListsDialog.AnnotationWrite_ActionType
                || actionType == AnnotationOrderedListsDialog.AnnotationHidden_ActionType
                || actionType == AnnotationLookupField_ActionType || actionType == AnnotationPrimaKeyInfo_ActionType) {
            editors[0] = new ComboBoxCellEditor(table, roles.toArray(new String[] {}), SWT.READ_ONLY);
        } else {
            editors[0] = new TextCellEditor(table);
        }

        viewer.setCellEditors(editors);

        // set the content provider
        viewer.setContentProvider(new IStructuredContentProvider() {

            public void dispose() {
            }

            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }

            public Object[] getElements(Object inputElement) {
                @SuppressWarnings("unchecked")
                ArrayList<String> xPaths = (ArrayList<String>) inputElement;
                ArrayList<DescriptionLine> lines = new ArrayList<DescriptionLine>();
                for (String xPath : xPaths) {
                    DescriptionLine line = new DescriptionLine(xPath);
                    lines.add(line);
                }
                // we return an instance line made of a Sring and a boolean
                return lines.toArray(new DescriptionLine[lines.size()]);
            }
        });

        // set the label provider
        viewer.setLabelProvider(new ITableLabelProvider() {

            public boolean isLabelProperty(Object element, String property) {
                return false;
            }

            public void dispose() {
            }

            public void addListener(ILabelProviderListener listener) {
            }

            public void removeListener(ILabelProviderListener listener) {
            }

            public String getColumnText(Object element, int columnIndex) {
                // System.out.println("getColumnText() "+columnIndex);
                DescriptionLine line = (DescriptionLine) element;
                switch (columnIndex) {
                case 0:
                    return line.getLabel();
                }
                return "";//$NON-NLS-1$
            }

            public Image getColumnImage(Object element, int columnIndex) {
                return null;
            }
        });

        // Set the column properties
        viewer.setColumnProperties(new String[] { COLUMN });

        // set the Cell Modifier
        viewer.setCellModifier(new ICellModifier() {

            public boolean canModify(Object element, String property) {
                // if (INSTANCE_ACCESS.equals(property)) return true; Deactivated
                return true;
                // return false;
            }

            public void modify(Object element, String property, Object value) {

                TableItem item = (TableItem) element;
                DescriptionLine line = (DescriptionLine) item.getData();
                String orgValue = line.getLabel();
                if (actionType != AnnotationWrite_ActionType && actionType != AnnotationHidden_ActionType
                        && actionType != AnnotationLookupField_ActionType && actionType != AnnotationPrimaKeyInfo_ActionType) {
                    int targetPos = xPaths.indexOf(value.toString());
                    if (targetPos < 0) {
                        line.setLabel(value.toString());
                        xPaths.add(value.toString());
                        viewer.update(line, null);
                    } else if (targetPos >= 0 && !value.toString().equals(orgValue)) {
                        MessageDialog.openInformation(null, Messages.Warning,
                                Messages.AnnotationOrderedListsDialog_ValueAlreadyExists);
                    }
                    return;
                } else {

                    String[] attrs = roles.toArray(new String[] {});
                    int index = Integer.parseInt(value.toString());
                    if (index == -1) {
                        return;
                    }
                    value = attrs[index];
                    int pos = xPaths.indexOf(value.toString());
                    if (pos >= 0 && !(orgValue.equals(value))) {
                        MessageDialog.openInformation(null, Messages.Warning, Messages.AnnotationOrderedListsDialog_);
                        return;
                    } else if (pos < 0) {
                        line.setLabel(value.toString());
                        xPaths.set(xPaths.indexOf(orgValue), value.toString());
                        viewer.update(line, null);
                    }
                }
            }

            public Object getValue(Object element, String property) {
                DescriptionLine line = (DescriptionLine) element;
                String value = line.getLabel();

                if (actionType == AnnotationWrite_ActionType || actionType == AnnotationHidden_ActionType
                        || actionType == AnnotationLookupField_ActionType || actionType == AnnotationPrimaKeyInfo_ActionType) {
                    String[] attrs = roles.toArray(new String[] {});
                    return Arrays.asList(attrs).indexOf(value);
                } else {
                    return value;
                }

            }
        });

        // Listen for changes in the selection of the viewer to display additional parameters
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                DescriptionLine line = (DescriptionLine) ((IStructuredSelection) viewer.getSelection()).getFirstElement();
                if (line != null) {
                    if (textControl instanceof CCombo) {
                        ((CCombo) textControl).setText(line.getLabel());
                    }
                    if (textControl instanceof Text) {
                        ((Text) textControl).setText(line.getLabel());
                    }
                }
            }
        });

        // display for Delete Key events to delete an instance
        viewer.getTable().addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
                // System.out.println("Table keyReleased() ");
                if ((e.stateMask == 0) && (e.character == SWT.DEL) && (viewer.getSelection() != null)) {
                    DescriptionLine line = (DescriptionLine) ((IStructuredSelection) viewer.getSelection()).getFirstElement();
                    @SuppressWarnings("unchecked")
                    ArrayList<String> xPaths = (ArrayList<String>) viewer.getInput();
                    xPaths.remove(line.getLabel());
                    viewer.refresh();
                }
            }
        });

        viewer.setInput(xPaths);
        viewer.refresh();

        Composite rightButtonsComposite = new Composite(composite, SWT.NULL);
        rightButtonsComposite.setLayout(new GridLayout(1, true));
        rightButtonsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));

        Button upButton = new Button(rightButtonsComposite, SWT.PUSH | SWT.CENTER);
        upButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
        upButton.setImage(ImageCache.getCreatedImage(EImage.PREV_NAV.getPath()));
        upButton.setToolTipText(Messages.AnnotationOrderedListsDialog_MoveUpTheItem);
        upButton.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            };

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                DescriptionLine line = (DescriptionLine) ((IStructuredSelection) viewer.getSelection()).getFirstElement();
                if (line == null) {
                    return;
                }
                int i = 0;
                for (String xPath : xPaths) {
                    if (xPath.equals(line.getLabel())) {
                        if (i > 0) {
                            xPaths.remove(i);
                            xPaths.add(i - 1, xPath);
                            viewer.refresh();
                            viewer.getTable().setSelection(i - 1);
                            viewer.getTable().showSelection();
                        }
                        return;
                    }
                    i++;
                }
            };
        });
        Button downButton = new Button(rightButtonsComposite, SWT.PUSH | SWT.CENTER);
        downButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
        downButton.setImage(ImageCache.getCreatedImage(EImage.NEXT_NAV.getPath()));
        downButton.setToolTipText(Messages.AnnotationOrderedListsDialog_MoveDownTheItem);
        downButton.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            };

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                DescriptionLine line = (DescriptionLine) ((IStructuredSelection) viewer.getSelection()).getFirstElement();
                if (line == null) {
                    return;
                }
                int i = 0;
                for (String xPath : xPaths) {
                    if (xPath.equals(line.getLabel())) {
                        if (i < xPaths.size() - 1) {
                            xPaths.remove(i);
                            xPaths.add(i + 1, xPath);
                            viewer.refresh();
                            viewer.getTable().setSelection(i + 1);
                            viewer.getTable().showSelection();
                        }
                        return;
                    }
                    i++;
                }
            };
        });
        Button delButton = new Button(rightButtonsComposite, SWT.PUSH | SWT.CENTER);
        delButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
        delButton.setImage(ImageCache.getCreatedImage(EImage.DELETE_OBJ.getPath()));
        delButton.setToolTipText(Messages.AnnotationOrderedListsDialog_DelTheItem);

        delButton.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            };

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                DescriptionLine line = (DescriptionLine) ((IStructuredSelection) viewer.getSelection()).getFirstElement();
                if (line != null) {
                    @SuppressWarnings("unchecked")
                    ArrayList<String> xPaths = (ArrayList<String>) viewer.getInput();
                    xPaths.remove(line.getLabel());
                    viewer.refresh();
                }
            };
        });

        textControl.setFocus();
        if (actionType == AnnotationOrderedListsDialog.AnnotationForeignKeyInfo_ActionType) {
            checkBox = new Button(composite, SWT.CHECK);
            checkBox.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 2, 1));
            checkBox.addSelectionListener(new SelectionListener() {

                public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
                };

                public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                    recursive = checkBox.getSelection();
                    setRetrieveFKinfos(checkBox.getSelection());
                };
            });

            checkBox.setSelection(isRetrieveFKinfos());
            checkBox.setText(Messages.FKInfos_resolve);
        } else if (actionType != AnnotationOrderedListsDialog.AnnotationForeignKeyInfo_ActionType
                && actionType != AnnotationOrderedListsDialog.AnnotationTargetSystems_ActionType
                && actionType != AnnotationOrderedListsDialog.AnnotationSchematron_ActionType
                && actionType != AnnotationOrderedListsDialog.AnnotationLookupField_ActionType
                && actionType != AnnotationOrderedListsDialog.AnnotationPrimaKeyInfo_ActionType) {
            checkBox = new Button(composite, SWT.CHECK);
            checkBox.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 2, 1));
            checkBox.addSelectionListener(new SelectionListener() {

                public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
                };

                public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                    recursive = checkBox.getSelection();
                };
            });
            checkBox.setSelection(recursive);
            checkBox.setText(Messages.AnnotationOrderedListsDialog_SetRoleRecursively);
            // Label label = new Label(composite, SWT.LEFT);
            // label.setText("set role recursively");
            // label.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true,
            // 1, 1));
        }

        return composite;
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        super.createButtonsForButtonBar(parent);

        getButton(IDialogConstants.OK_ID).addSelectionListener(this.caller);
        getButton(IDialogConstants.CANCEL_ID).addSelectionListener(this.caller);
    }

    protected XpathSelectDialog getNewXpathSelectDialog(DataModelMainPage parentPage, String dataModelName) {
        return new XpathSelectDialog(parentPage.getSite().getShell(), xObject.getParent(),
                Messages.AnnotationOrderedListsDialog_SelectXPathXX, parentPage.getSite(), false, dataModelName);
    }

    @Override
    protected void okPressed() {
        setReturnCode(OK);
        getButton(IDialogConstants.OK_ID).setData("dialog", AnnotationOrderedListsDialog.this);//$NON-NLS-1$
        // no close let Action Handler handle it
    }

    @Override
    protected void cancelPressed() {
        setReturnCode(CANCEL);
        getButton(IDialogConstants.CANCEL_ID).setData("dialog", AnnotationOrderedListsDialog.this);//$NON-NLS-1$
        // no close let Action Handler handle it
    }

    /**************************************************************************************************
     * Public getters read by caller
     ***************************************************************************************************/

    /*
     * public TableViewer getDescriptionsTableViewer() { return descriptionsViewer; }
     */

    public ArrayList<String> getXPaths() {
        return xPaths;
    }

    public boolean getRecursive() {
        return recursive;
    }

    private static String getControlText(Control textControl) {
        if (textControl instanceof CCombo) {
            return ((CCombo) textControl).getText();
        } else if (textControl instanceof Text) {
            return ((Text) textControl).getText();
        } else {
            return "";//$NON-NLS-1$
        }

    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    /**************************************************************************************************
     * A table viewer line
     ***************************************************************************************************/
    class DescriptionLine {

        private String label;

        public DescriptionLine(String label) {
            super();
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }

}
