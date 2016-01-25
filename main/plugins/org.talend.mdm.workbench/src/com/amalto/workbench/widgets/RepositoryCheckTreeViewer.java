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
package com.amalto.workbench.widgets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import com.amalto.workbench.i18n.Messages;
import com.amalto.workbench.models.TreeObject;
import com.amalto.workbench.models.TreeParent;
import com.amalto.workbench.utils.Util;
import com.amalto.workbench.webservices.WSVersioningUniverseVersions;

/**
 * @author achen DOC achen class global comment. Detailled comment
 */
public class RepositoryCheckTreeViewer {

    protected static Log log = LogFactory.getLog(RepositoryCheckTreeViewer.class);

    protected FilteredCheckboxTree filteredCheckboxTree;

    // protected CheckboxRepositoryView exportItemsTreeView;

    Collection<TreeObject> repositoryNodes = new ArrayList<TreeObject>();

    Collection<TreeObject> optimizedCheckNodes = new ArrayList<TreeObject>();

    protected IStructuredSelection selection;

    protected SashForm sash;

    protected Button moveButton;

    protected TreeParent serverRoot;

    protected List<TreeObject> checkItems = new ArrayList<TreeObject>();

    protected String defaultTagText;

    protected boolean isTagEditable;

    protected VersionTagWidget vwidget;

    protected SelectionListener tagSelectionListener;

    protected SelectionListener restoreSelectionListener;

    protected IDoubleClickListener tagsViewerDoubleClickListener;

    private List<WSVersioningUniverseVersions.TagStructure> hisEntries;

    public RepositoryCheckTreeViewer(IStructuredSelection selection, String defaultTagText, boolean isTagEditable) {
        this.selection = selection;
        Object firstElement = selection.getFirstElement();
        if (firstElement != null && firstElement instanceof TreeObject) {
            serverRoot = ((TreeObject) firstElement).getServerRoot();
        }
        checkItems = selection.toList();
        this.defaultTagText = defaultTagText;
        this.isTagEditable = isTagEditable;
    }

    public RepositoryCheckTreeViewer(IStructuredSelection selection) {
        this.selection = selection;
        Object firstElement = selection.getFirstElement();
        if (firstElement != null && firstElement instanceof TreeObject) {
            serverRoot = ((TreeObject) firstElement).getServerRoot();
        }
        checkItems = selection.toList();
    }

    public RepositoryCheckTreeViewer(TreeParent serverRoot) {
        this.serverRoot = serverRoot;
    }

    public void setCheckItems(List<TreeObject> list) {
        checkItems = list;
        refresh();
    }

    public void setServerRoot(TreeParent serverRoot) {
        this.serverRoot = serverRoot;
    }

    public SashForm createContents(Composite parent) {
        // Splitter
        final GridData data = new GridData();
        data.heightHint = 400;
        data.widthHint = 700;
        sash = new SashForm(parent, SWT.HORIZONTAL | SWT.SMOOTH);
        sash.setLayoutData(data);

        GridLayout layout = new GridLayout();
        layout.marginLeft = 0;
        layout.marginRight = 0;
        sash.setLayout(layout);
        // create tree
        createItemList(sash);
        // create button
        Composite buttonComposite = new Composite(sash, SWT.ERROR);
        layout = new GridLayout();
        layout.marginLeft = 0;
        layout.marginRight = 0;
        buttonComposite.setLayout(layout);

        moveButton = new Button(buttonComposite, SWT.PUSH);
        moveButton.setText("<<"); //$NON-NLS-1$
        moveButton.setToolTipText("Show server tree"); //$NON-NLS-1$

        final GridData layoutData = new GridData();
        layoutData.verticalAlignment = GridData.CENTER;
        layoutData.horizontalAlignment = GridData.CENTER;
        layoutData.grabExcessHorizontalSpace = true;
        layoutData.grabExcessVerticalSpace = true;
        layoutData.widthHint = 30;
        moveButton.setLayoutData(layoutData);

        // create version composite
        Composite versionComposite = new Composite(sash, SWT.NONE);
        layout = new GridLayout();
        layout.marginLeft = 0;
        layout.marginRight = 0;
        versionComposite.setLayout(layout);
        vwidget = new VersionTagWidget(versionComposite, "Universe", defaultTagText, isTagEditable, tagSelectionListener,//$NON-NLS-1$
                restoreSelectionListener, tagsViewerDoubleClickListener, this.hisEntries);

        sash.setWeights(new int[] { 20, 2, 21 });
        // add listner
        moveButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (moveButton.getText().equals("<<")) { //$NON-NLS-1$
                    sash.setWeights(new int[] { 0, 2, 23 });
                    moveButton.setText(">>"); //$NON-NLS-1$

                } else if (moveButton.getText().equals(">>")) { //$NON-NLS-1$
                    sash.setWeights(new int[] { 20, 2, 21 });
                    moveButton.setText("<<"); //$NON-NLS-1$    
                    moveButton.setToolTipText("Hide server tree"); //$NON-NLS-1$
                }
            }
        });

        return sash;
    }

    Label itemLabel = null;

    protected TreeViewer viewer;

    /**
     * 
     * @param workArea
     */
    public Composite createItemList(Composite workArea) {
        Group itemComposite = new Group(workArea, 0);
        GridLayoutFactory.swtDefaults().numColumns(2).applyTo(itemComposite);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).hint(400, 300).applyTo(itemComposite);

        itemLabel = new Label(itemComposite, SWT.NONE);
        itemLabel.setText(Messages.RepositoryCheckTreeViewer_ItemLabel);
        GridDataFactory.swtDefaults().span(2, 1).applyTo(itemLabel);

        createTreeViewer(itemComposite);

        createSelectionButton(itemComposite);

        // force loading all nodes
        setCreatedViewer();

        refresh();
        return itemComposite;
    }

    protected void setCreatedViewer() {
        // viewer = exportItemsTreeView.getViewer();
    }

    public void refresh() {

        // if user has select some items in repository view, mark them as checked
        for (TreeObject obj : checkItems) {
            if (obj instanceof TreeParent) {

                repositoryNodes.addAll(Util.getChildrenObj((TreeParent) obj));

            } else {
                repositoryNodes.add(obj);

            }
        }

        ((CheckboxTreeViewer) viewer).setCheckedElements(repositoryNodes.toArray());

    }

    public void setItemText(String text) {
        itemLabel.setText(text);
    }

    public TreeViewer getViewer() {
        return viewer;
    }

    public void setViewer(TreeViewer viewer) {
        this.viewer = viewer;
    }

    private void expandParent(TreeViewer viewer, TreeObject node) {
        TreeParent parent = node.getParent();
        if (parent != null) {
            expandParent(viewer, parent);
            viewer.setExpandedState(parent, true);
        }
    }

    public Object[] getCheckNodes() {
        Object[] selected = null;
        List ret = new ArrayList();
        if (viewer != null && viewer instanceof CheckboxTreeViewer) {
            selected = ((CheckboxTreeViewer) viewer).getCheckedElements();
        }
        filterCheckedObjects(selected, ret);
        return ret.toArray();
    }

    protected void filterCheckedObjects(Object[] selected, List ret) {

        for (int i = 0; i < selected.length; i++) {
            if (selected[i] instanceof TreeObject) {
                TreeObject node = (TreeObject) selected[i];
                if (node.isXObject()) {
                    ret.add(node);
                }
            }
        }

    }

    private SelectionListener bunListener;

    public void setRoot(TreeParent root) {
    }

    protected void createTreeViewer(Composite itemComposite) {

        // filteredCheckboxTree = new FilteredCheckboxTree(itemComposite, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL |
        // SWT.MULTI) {
        //
        // @Override
        // protected CheckboxTreeViewer doCreateTreeViewer(Composite parent, int style) {
        // exportItemsTreeView = new CheckboxRepositoryView();
        // try {
        // exportItemsTreeView.init(repositoryView.getViewSite());
        // } catch (PartInitException e) {
        // log.error(e.getMessage(), e);
        // }
        // exportItemsTreeView.createPartControl(parent);
        // exportItemsTreeView.getViewer().setInput(repositoryView.getSite());
        // return (CheckboxTreeViewer) exportItemsTreeView.getViewer();
        // }
        //
        // @Override
        // protected void refreshCompleted() {
        // getViewer().expandToLevel(3);
        // restoreCheckedElements();
        // }
        //
        // @Override
        // protected boolean isNodeCollectable(TreeItem item) {
        // // Object obj = item.getData();
        // // if (obj instanceof RepositoryNode) {
        // // RepositoryNode node = (RepositoryNode) obj;
        // // if (node.getObjectType() == ERepositoryObjectType.METADATA_CONNECTIONS) {
        // // return true;
        // // }
        // // }
        // return false;
        // }
        // };
        // exportItemsTreeView.getViewer().addFilter(new ViewerFilter() {
        //
        // @Override
        // public boolean select(Viewer viewer, Object parentElement, Object element) {
        // TreeObject node = (TreeObject) element;
        // return filterRepositoryNode(node);
        // }
        // });
    }

    public void addCheckStateListener(ICheckStateListener listener) {
        // ((CheckboxTreeViewer) exportItemsTreeView.getViewer()).addCheckStateListener(listener);
    }

    public void removeCheckStateListener(ICheckStateListener listener) {
        // ((CheckboxTreeViewer) exportItemsTreeView.getViewer()).removeCheckStateListener(listener);
    }

    protected boolean filterRepositoryNode(TreeObject node) {
        if (node == null) {
            return false;
        }
        // remove the filter for resource to provide the function to import and export resources.
        if (node.getType() == TreeObject.SUBSCRIPTION_ENGINE || node.getType() == TreeObject.SERVICE_CONFIGURATION
                || !Util.IsEnterPrise() && node.getType() == TreeObject.WORKFLOW || !Util.IsEnterPrise()
                && node.getType() == TreeObject.WORKFLOW_PROCESS || node.getType() == TreeObject.JOB
                || node.getType() == TreeObject.JOB_REGISTRY) {
            return false;
        }
        if (!Util.IsEnterPrise()) {
            if (node.getType() == TreeObject.ROLE || node.getType() == TreeObject.UNIVERSE
                    || node.getType() == TreeObject.SYNCHRONIZATIONPLAN) {
                return false;
            }
        }
        return true;
    }

    /**
     * DOC hcw Comment method "createSelectionButton".
     * 
     * @param itemComposite
     */
    private void createSelectionButton(Composite itemComposite) {
        Composite buttonComposite = new Composite(itemComposite, SWT.NONE);
        GridLayoutFactory.swtDefaults().margins(0, 25).applyTo(buttonComposite);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.BEGINNING).applyTo(buttonComposite);
        buttonComposite.setLayout(new RowLayout(SWT.VERTICAL));

        Button hide = new Button(buttonComposite, SWT.PUSH);
        hide.setVisible(false);
        Button selectAll = new Button(buttonComposite, SWT.PUSH);
        selectAll.setText(Messages.RepositoryCheckTreeViewer_SelectAll);
        selectAll.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                ((CheckboxTreeViewer) viewer).setAllChecked(true);
            }
        });

        Button deselectAll = new Button(buttonComposite, SWT.PUSH);
        deselectAll.setText(Messages.RepositoryCheckTreeViewer_DeselectAll);
        deselectAll.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                ((CheckboxTreeViewer) viewer).setAllChecked(false);
            }
        });
        if (bunListener != null) {
            selectAll.addSelectionListener(bunListener);
            deselectAll.addSelectionListener(bunListener);
        }

        // setButtonLayoutData(deselectAll);

        Button expandBtn = new Button(buttonComposite, SWT.PUSH);
        expandBtn.setText("Expand All"); //$NON-NLS-1$
        expandBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                viewer.expandAll();
            }
        });
        // setButtonLayoutData(expandBtn);

        Button collapseBtn = new Button(buttonComposite, SWT.PUSH);
        collapseBtn.setText("Collapse All"); //$NON-NLS-1$
        collapseBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                viewer.collapseAll();
            }
        });
        // setButtonLayoutData(collapseBtn);
    }

    public SelectionListener getTagSelectionListener() {
        return tagSelectionListener;
    }

    public void setTagSelectionListener(SelectionListener tagSelectionListener) {
        this.tagSelectionListener = tagSelectionListener;
    }

    public SelectionListener getRestoreSelectionListener() {
        return restoreSelectionListener;
    }

    public void setRestoreSelectionListener(SelectionListener restoreSelectionListener) {
        this.restoreSelectionListener = restoreSelectionListener;
    }

    public IDoubleClickListener getTagsViewerDoubleClickListener() {
        return tagsViewerDoubleClickListener;
    }

    public void setTagsViewerDoubleClickListener(IDoubleClickListener tagsViewerDoubleClickListener) {
        this.tagsViewerDoubleClickListener = tagsViewerDoubleClickListener;
    }

    public void setHisEntries(List<WSVersioningUniverseVersions.TagStructure> hisEntries) {

        this.hisEntries = hisEntries;

    }

    public String getComment() {

        return vwidget.getCommentText().getText().trim();

    }

    public String getTagText() {

        return vwidget.getTagText().getText().trim();

    }

    public void refreshHistoryTable(List<WSVersioningUniverseVersions.TagStructure> hisEntries) {
        this.vwidget.refreshData(hisEntries);
    }

    public TableViewer getTagsViewer() {

        return this.vwidget.getTagsViewer();

    }

    // /**
    // *
    // * A repository view with checkbox on the left.
    // */
    // protected class CheckboxRepositoryView extends ServerView {
    //
    // @Override
    // protected TreeViewer createTreeViewer(Composite parent) {
    // return new CheckboxRepositoryTreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
    // }
    //
    // /*
    // * (non-Javadoc)
    // *
    // * @see org.talend.repository.ui.views.RepositoryView#createPartControl(org.eclipse.swt.widgets.Composite)
    // */
    // @Override
    // public void createPartControl(Composite parent) {
    // super.createPartControl(parent);
    //
    // }
    //
    // @Override
    // public void initView() {
    // }
    // }

    public void addButtonSelectionListener(SelectionListener listener) {
        this.bunListener = listener;

    }
}
