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
package org.talend.mdm.repository.ui.editors;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.xsd.XSDDiagnostic;
import org.eclipse.xsd.XSDSchema;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.mdm.repository.core.command.CommandManager;
import org.talend.mdm.repository.core.command.ICommand;
import org.talend.mdm.repository.core.service.DeployService;
import org.talend.mdm.repository.core.service.IModelValidationService;
import org.talend.mdm.repository.core.service.IModelValidationService.IModelValidateResult;
import org.talend.mdm.repository.i18n.Messages;
import org.talend.mdm.repository.model.mdmmetadata.MDMServerDef;
import org.talend.mdm.repository.utils.RepositoryResourceUtil;
import org.talend.repository.RepositoryWorkUnit;

import com.amalto.workbench.editors.DataModelMainPage;
import com.amalto.workbench.editors.xsdeditor.XSDEditor;
import com.amalto.workbench.editors.xsdeditor.XSDSelectionListener;
import com.amalto.workbench.exadapter.ExAdapterManager;
import com.amalto.workbench.models.TreeObject;
import com.amalto.workbench.utils.CompositeViewersSelectionProvider;
import com.amalto.workbench.utils.Util;

/**
 * DOC hbhong class global comment. Detailled comment
 */
public class XSDEditor2 extends XSDEditor implements ISvnHistory {

    public static final String EDITOR_ID = "org.talend.mdm.repository.ui.editors.XSDEditor2"; //$NON-NLS-1$

    static Logger log = Logger.getLogger(XObjectEditor2.class);

    private DataModelMainPage2 dMainPage;

    private CTabFolder folder;

    private IXSDEditor2ExAdapter exAdapter;

    public XSDEditor2() {
        exAdapter = ExAdapterManager.getAdapter(this, IXSDEditor2ExAdapter.class);
    }

    @Override
    protected void createPages() {

        super.createPages();
        XSDEditorInput2 editorInput = (XSDEditorInput2) getEditorInput();
        TreeObject treeObject = editorInput.getTreeObject();
        IRepositoryViewEditorInput xobjectEditorinput = new XObjectEditorInput2(editorInput.getViewObject(), treeObject,
                treeObject.getDisplayName());
        xobjectEditorinput.setReadOnly(editorInput.isReadOnly());
        IFile xsdFile = null;
        try {
            xsdFile = getXSDFile(treeObject);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        dMainPage = new DataModelMainPage2(treeObject);
        try {
            MODEL_PAGE_INDEX = addPage(dMainPage, xobjectEditorinput);
        } catch (PartInitException e) {
            log.error(e.getMessage(), e);
        }
        // add repository view object in selectionprovider
        IRepositoryViewObject repositoryViewObj = editorInput.getViewObject();// ContainerCacheService.get(editorInput.getInputItem().getProperty());
        CompositeViewersSelectionProvider selectionProvider = (CompositeViewersSelectionProvider) dMainPage
                .getSelectionProvider();
        selectionProvider.setRepositoryViewObj(repositoryViewObj);
        //
        getSite().setSelectionProvider(dMainPage.getSelectionProvider());

        // add XSDSelectionListener
        XSDSelectionListener xsdListener = new XSDSelectionListener(this, dMainPage);
        dMainPage.getTypesViewer().addSelectionChangedListener(xsdListener);
        dMainPage.getElementsViewer().addSelectionChangedListener(xsdListener);
        //

        setXSDInput(xobjectEditorinput);
        setXObject(treeObject);
        //
        folder = (CTabFolder) dMainPage.getMainControl().getParent();
        folder.getItem(2).setText(treeObject.getDisplayName() + " " + Util.getRevision(treeObject));//$NON-NLS-1$
        folder.getItem(0).setText(Messages.XSDEditor2_schemaDesign);
        folder.getItem(1).setText(Messages.XSDEditor2_schemaSource);
        // default use
        activePage(xsdFile);
        this.addPropertyListener(new IPropertyListener() {

            private boolean needValidate = false;

            public void propertyChanged(Object source, int propId) {
                if (propId == PROP_DIRTY) {
                    if (isDirty()) {
                        needValidate = true;

                    } else if (needValidate) {
                        needValidate = false;
                        IRepositoryViewObject viewObject = getCurrentViewObject();
                        validateModel(viewObject);

                    }
                }

            }
        });
    }

    private void activePage(IFile xsdFile) {
        if (model != null) {
            Notifier target = ((Adapter) model).getTarget();
            XSDSchema xs = (XSDSchema) target;
            xs.validate();
            EList<XSDDiagnostic> diagnostics = xs.getAllDiagnostics();
            if (!diagnostics.isEmpty()) {
                setActivePage(SOURCE_PAGE_INDEX);
                preActivePageIndex = SOURCE_PAGE_INDEX;
                return;
            }
        }
        setActivePage(2);
        preActivePageIndex = 2;
    }

    @Override
    protected IFile getXSDFile(TreeObject xobject) throws Exception {
        return ((IFileEditorInput) getEditorInput()).getFile();
    }

    @Override
    public boolean isLocalInput() {
        return true;
    }

    @Override
    public boolean isReadOnly() {
        IRepositoryViewEditorInput editorInput = (IRepositoryViewEditorInput) this.getEditorInput();
        return editorInput.isReadOnly();
    }

    @Override
    public void setFocus() {
        super.setFocus();
        if (folder != null) {
            folder.setFocus();
        }
    }

    private IRepositoryViewObject getCurrentViewObject() {
        return ((IRepositoryViewEditorInput) xsdInput).getViewObject();
    }

    @Override
    public void doSave(final IProgressMonitor monitor) {
        RepositoryWorkUnit<Object> repositoryWorkUnit = new RepositoryWorkUnit<Object>("", this) { //$NON-NLS-1$

            @Override
            protected void run() throws LoginException, PersistenceException {
                innerSave(monitor);
            }
        };
        repositoryWorkUnit.setAvoidUnloadResources(true);
        CoreRuntimePlugin.getInstance().getProxyRepositoryFactory().executeRepositoryWorkUnit(repositoryWorkUnit);
    }

    private void innerSave(IProgressMonitor monitor) {
        super.doSave(monitor);
        IRepositoryViewObject viewObject = getCurrentViewObject();
        Item item = viewObject.getProperty().getItem();
        if (isEE()) {
            exAdapter.doSave(item, dMainPage, monitor);
        }

        DeployService deployService = DeployService.getInstance();
        if (deployService.isAutoDeploy()) {
            deployService.autoDeploy(getSite().getShell(), viewObject);
        } else {
            MDMServerDef lastServerDef = RepositoryResourceUtil.getLastServerDef(item);
            if (lastServerDef != null) {
                CommandManager.getInstance().pushCommand(ICommand.CMD_MODIFY, viewObject);
            }
        }
    }

    private int validateModel(IRepositoryViewObject viewObject) {
        IModelValidationService service = (IModelValidationService) GlobalServiceRegister.getDefault().getService(
                IModelValidationService.class);
        List<IRepositoryViewObject> viewObjs = new ArrayList<IRepositoryViewObject>();
        viewObjs.add(viewObject);
        IModelValidateResult validateResult = service.validate(viewObjs, IModelValidationService.VALIDATE_AFTER_SAVE);
        if (validateResult != null) {
            return validateResult.getSelectedButton();
        }
        return IModelValidationService.BUTTON_CANCEL;
    }

    @Override
    public DataModelMainPage getdMainPage() {
        return dMainPage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.amalto.workbench.editors.xsdeditor.XSDEditor#getAdapter(java.lang.Class)
     */
    @Override
    public Object getAdapter(Class type) {

        if (type == IGotoMarker.class) {

            return dMainPage;
        } else if (type == XSDEditorInput2.class) {
            return getEditorInput();
        }
        if (isEE()) {
            Object adapter = exAdapter.getAdapter(type);
            if (adapter != null) {
                return adapter;
            }
        }

        return super.getAdapter(type);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.wst.xsd.ui.internal.adt.editor.CommonMultiPageEditor#init(org.eclipse.ui.IEditorSite,
     * org.eclipse.ui.IEditorInput)
     */
    @Override
    public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {

        super.init(site, editorInput);

        if (isEE()) {
            exAdapter.init(editorInput);
        }
    }

    public void fireDirtyPropChange() {
        firePropertyChange(PROP_DIRTY);
    }

    private boolean isEE() {
        return exAdapter != null;
    }

    @Override
    public void dispose() {
        if (isEE()) {
            exAdapter.dispose();
        }
        super.dispose();
    }

    @Override
    public boolean isDirty() {
        boolean mapInfoDirty = false;
        if (isEE()) {
            mapInfoDirty = exAdapter.isDirty();
        }
        return super.isDirty() || mapInfoDirty;
    }

}
