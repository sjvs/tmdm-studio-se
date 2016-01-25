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
package org.talend.mdm.repository.ui.wizards.imports.viewer;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.dialogs.ContainerCheckedTreeViewer;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.mdm.repository.core.IServerObjectRepositoryType;
import org.talend.mdm.repository.core.service.ContainerCacheService;
import org.talend.mdm.repository.i18n.Messages;
import org.talend.mdm.repository.ui.widgets.AbstractNodeCheckTreeViewer;
import org.talend.mdm.repository.utils.RepositoryTransformUtil;
import org.talend.repository.imports.ImportItemUtil;
import org.talend.repository.imports.ItemRecord;
import org.talend.repository.imports.TreeBuilder.IContainerNode;

import com.amalto.workbench.widgets.FilteredCheckboxTree;

/**
 * DOC hbhong class global comment. Detailled comment
 */
public class ImportRepositoryObjectCheckTreeViewer extends AbstractNodeCheckTreeViewer {

    private ImportItemUtil repositoryUtil;

    private TableViewer errorsListViewer;

    /**
     * DOC hbhong RepositoryObjectCheckTreeViewer constructor comment.
     * 
     * @param repositoryUtil
     */
    public ImportRepositoryObjectCheckTreeViewer(ImportItemUtil repositoryUtil) {
        super(new StructuredSelection());
        this.repositoryUtil = repositoryUtil;
    }

    /**
     * DOC hbhong RepositoryObjectCheckTreeViewer constructor comment.
     * 
     * @param selection
     */
    public ImportRepositoryObjectCheckTreeViewer(IStructuredSelection selection) {
        super(selection);
    }

    @Override
    protected void createTreeViewer(Composite itemComposite) {
        filteredCheckboxTree = new FilteredCheckboxTree(itemComposite, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI) {

            ContainerCheckedTreeViewer treeViewer;

            @Override
            protected CheckboxTreeViewer doCreateTreeViewer(Composite parent, int style) {
                treeViewer = new ContainerCheckedTreeViewer(parent);
                treeViewer.setContentProvider(new ITreeContentProvider() {

                    public void dispose() {
                    }

                    public Object[] getChildren(Object parentElement) {
                        if (parentElement instanceof IContainerNode) {
                            List children = ((IContainerNode) parentElement).getChildren();
                            sortInputItems(children);
                            return children.toArray();
                        }
                        return null;
                    }

                    private void sortInputItems(List typeNodes) {
                        Collections.sort(typeNodes, new Comparator() {

                            public int compare(Object o1, Object o2) {

                                if (o1 instanceof IContainerNode && o2 instanceof IContainerNode) {
                                    String name1 = ((IContainerNode) o1).getLabel();
                                    String name2 = ((IContainerNode) o2).getLabel();
                                    return name1.compareTo(name2);
                                } else if (o1 instanceof ItemRecord && o2 instanceof ItemRecord) {
                                    String name1 = ((ItemRecord) o1).getLabel();
                                    String name2 = ((ItemRecord) o2).getLabel();
                                    return name1.compareTo(name2);
                                }
                                return 0;
                            }

                        });
                    }

                    public Object[] getElements(Object inputElement) {
                        if (inputElement instanceof Collection) {
                            return ((Collection) inputElement).toArray();
                        }
                        return repositoryUtil.getTreeViewInput().toArray();
                    }

                    public Object getParent(Object element) {
                        return null;
                    }

                    public boolean hasChildren(Object element) {
                        if (element instanceof IContainerNode) {
                            return ((IContainerNode) element).hasChildren();
                        }
                        return false;
                    }

                    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
                    }

                });

                treeViewer.setLabelProvider(new LabelProvider() {

                    @Override
                    public Image getImage(Object element) {
                        if (element instanceof IContainerNode) {
                            return ((IContainerNode) element).getImage();
                        } else if (element instanceof ItemRecord) {
                            return ((ItemRecord) element).getImage();
                        }

                        return super.getImage(element);
                    }

                    @Override
                    public String getText(Object element) {
                        if (element instanceof IContainerNode) {
                            return ((IContainerNode) element).getLabel();
                        }
                        String itemRecordLabel = ((ItemRecord) element).getLabel();

                        itemRecordLabel = filterVieProcessName(itemRecordLabel, (ItemRecord) element);

                        return itemRecordLabel;
                    }

                    private String filterVieProcessName(String itemRecordLabel, ItemRecord record) {
                        String filteredName = itemRecordLabel;
                        if (filteredName != null && !filteredName.isEmpty()) {

                            ERepositoryObjectType type = record.getRepositoryType();

                            if (type == null) {
                                IRepositoryViewObject repositoryViewObject = ContainerCacheService.get(record.getProperty());
                                type = repositoryViewObject.getRepositoryObjectType();
                            }
                            if (type != null) {
                                if (type == IServerObjectRepositoryType.TYPE_VIEW) {
                                    filteredName = RepositoryTransformUtil.getInstance().transformToSilyViewName(filteredName,
                                            true);
                                }

                                if (type == IServerObjectRepositoryType.TYPE_TRANSFORMERV2) {
                                    filteredName = RepositoryTransformUtil.getInstance().transformToSilyProcessName(filteredName,
                                            true);
                                }
                            }
                        }

                        return filteredName;
                    }

                });
                return treeViewer;
            }

            @Override
            protected boolean isNodeCollectable(TreeItem item) {
                return false;
            }

            @Override
            protected void refreshCompleted() {
                treeViewer.expandToLevel(3);
                restoreCheckedElements();
            }
        };

    }

    @Override
    protected void filterCheckedObjects(Object[] selected, List<Object> ret) {
        for (Object obj : selected) {
            ret.add(obj);
        }

    }

    @Override
    protected void createOtherControl(Composite itemComposite) {
        Composite composite = new Composite(itemComposite, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.makeColumnsEqualWidth = false;
        layout.marginWidth = 0;
        composite.setLayout(layout);
        GridData gridData = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.FILL_BOTH);
        gridData.heightHint = 100;
        composite.setLayoutData(gridData);

        Label title = new Label(composite, SWT.NONE);
        title.setText(Messages.ImportRepositoryObjectCheckTreeViewer_errorLabel);
        title.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        errorsListViewer = new TableViewer(composite, SWT.BORDER);
        errorsListViewer.getControl().setLayoutData(gridData);

        errorsListViewer.setContentProvider(new ArrayContentProvider());
        errorsListViewer.setLabelProvider(new LabelProvider());
        errorsListViewer.setSorter(new ViewerSorter());
    }

    public void updateErrors(final List<String> errors) {
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                errorsListViewer.setInput(errors);
            }
        });
    }
}
