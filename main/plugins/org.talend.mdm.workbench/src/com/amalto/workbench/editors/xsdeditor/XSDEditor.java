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
package com.amalto.workbench.editors.xsdeditor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xsd.ui.internal.adapters.CategoryAdapter;
import org.eclipse.wst.xsd.ui.internal.adapters.RedefineCategoryAdapter;
import org.eclipse.wst.xsd.ui.internal.adapters.XSDBaseAdapter;
import org.eclipse.wst.xsd.ui.internal.editor.InternalXSDMultiPageEditor;
import org.eclipse.wst.xsd.ui.internal.editor.XSDTabbedPropertySheetPage;
import org.eclipse.xsd.XSDAnnotation;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDComponent;
import org.eclipse.xsd.XSDConcreteComponent;
import org.eclipse.xsd.XSDDiagnostic;
import org.eclipse.xsd.XSDDiagnosticSeverity;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDIdentityConstraintDefinition;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTerm;
import org.eclipse.xsd.XSDTypeDefinition;
import org.eclipse.xsd.XSDXPathDefinition;
import org.talend.mdm.commmon.util.core.CommonUtil;
import org.w3c.dom.Node;
import org.xml.sax.SAXParseException;

import com.amalto.workbench.dialogs.ErrorExceptionDialog;
import com.amalto.workbench.editors.DataModelMainPage;
import com.amalto.workbench.editors.IServerObjectEditorState;
import com.amalto.workbench.i18n.Messages;
import com.amalto.workbench.models.TreeObject;
import com.amalto.workbench.providers.datamodel.SchemaTreeContentProvider;
import com.amalto.workbench.utils.Util;
import com.amalto.workbench.views.MDMPerspective;
import com.amalto.workbench.webservices.WSDataModel;

@SuppressWarnings("restriction")
public class XSDEditor extends InternalXSDMultiPageEditor implements IServerObjectEditorState {

    private static Log log = LogFactory.getLog(XSDEditor.class);

    public static final String CONTRUIBUTIONID_DATAMODELPAGE = "org.talend.mdm.workbench.propertyContributor.datamodel";//$NON-NLS-1$

    protected static int MODEL_PAGE_INDEX = -1;

    private TreeExpandHelper expandHelper = new TreeExpandHelper();

    protected int preActivePageIndex = -1;

    protected IEditorInput xsdInput;

    protected TreeObject xobject;

    private XSDSelectionManagerSelectionListener fXSDSelectionListener;

    private IPropertyListener dirtyNotifyListener;

    private byte[] fileContents = null;

    public void setXSDInput(IEditorInput input) {
        this.xsdInput = input;
    }

    public void setXObject(TreeObject xobject) {
        this.xobject = xobject;
        InputStreamReader reader = null;
        try {// temporarily store the file data for restore
            IFile file = getXSDFile(xobject);
            reader = new InputStreamReader(file.getContents());
            fileContents = IOUtils.toByteArray(reader, "utf-8"); //$NON-NLS-1$
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (reader != null) {
                IOUtils.closeQuietly(reader);
            }
        }
    }

    @Override
    public String getPartName() {
        String part = super.getPartName();
        if (part.endsWith(".xsd")) {//$NON-NLS-1$
            return part.substring(0, part.length() - 4);
        } else {
            int start = part.lastIndexOf('_');
            int end = part.lastIndexOf(' ');
            if (start != -1 && end != -1) {
                return part.substring(0, start) + part.substring(end);
            }
        }
        return part;
    }

    protected void superDoSave(IProgressMonitor monitor) {
        super.doSave(monitor);
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
        InputStream stream = null;
        try {
            EditorPart part = (EditorPart) getSelectedPage();
            String xsd = null;
            if (null != part) {
                if (part instanceof DataModelMainPage) {
                    DataModelMainPage dmp = (DataModelMainPage) getSelectedPage();
                    xsd = dmp.getXSDSchemaString();
                } else if (part instanceof StructuredTextEditor) {
                    xsd = ((StructuredTextEditor) part).getTextViewer().getDocument().get();
                }
            }
            if (null != xsd) {
                DataModelMainPage mainPage = getdMainPage();
                WSDataModel wsDataModel = (WSDataModel) xobject.getWsObject();
                wsDataModel.setXsdSchema(xsd);
                if (mainPage != null) {
                    mainPage.save(xsd);
                }
                fileContents = xsd.getBytes("utf-8"); //$NON-NLS-1$
            }
            getDataModelEditorPage().setDirty(false);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(stream);
            super.doSave(monitor);
            if (null != monitor) {
                monitor.done();
            }
        }
    }

    protected IFile getXSDFile(TreeObject object) throws Exception {
        return XSDEditorUtil.createFile(object);
    }

    boolean doUpdateSourceLocation = false;

    @Override
    protected void pageChange(int newPageIndex) {
        resetTreeSelection(newPageIndex);

        super.pageChange(newPageIndex);
        doUpdateSourceLocation = newPageIndex == SOURCE_PAGE_INDEX;
        if (doUpdateSourceLocation && fXSDSelectionListener != null) {
            fXSDSelectionListener.doSetSelection();
        }
        doPageChanged();
        refreshPropertyView();
        setFocus();
    }

    private void doPageChanged() {
        if (xobject == null) {
            return;
        }
        try {
            if (getSelectedPage() instanceof DataModelMainPage) {// save the
                                                                 // file's
                                                                 // contents
                                                                 // to
                DataModelMainPage mainPage = getDataModelEditorPage();
                if (mainPage != null) {
                    getEditorSite().setSelectionProvider(mainPage.getSelectionProvider());

                    String xsd = getTextEditor().getTextViewer().getDocument().get();
                    XSDSchema schema = Util.createXsdSchema(xsd, xobject);
                    mainPage.setXsdSchema(schema);
                    mainPage.getTypeContentProvider().setXsdSchema(schema);
                    mainPage.getSchemaContentProvider().setXsdSchema(schema);
                    mainPage.refresh();

                    //
                    expandHelper.recoverExpandState(mainPage);
                }
            } else {
                // save DataModelMainPage's contents to file
                getEditorSite().setSelectionProvider(getSelectionManager());
                DataModelMainPage mainPage = getDataModelEditorPage();
                expandHelper.recordExpandState(mainPage);
                if (mainPage != null && mainPage.isDirty()) {
                    String xsd = mainPage.getXSDSchemaString();
                    xsd = Util.formatXsdSource(xsd);
                    WSDataModel wsDataModel = (WSDataModel) xobject.getWsObject();
                    wsDataModel.setXsdSchema(xsd);
                    IFile file = getXSDFile(xobject);
                    file.setCharset("utf-8", null);//$NON-NLS-1$
                    file.setContents(new ByteArrayInputStream(xsd.getBytes("utf-8")), IFile.FORCE, null);//$NON-NLS-1$

                    initializeGraphicalViewer();
                }
            }
        } catch (SAXParseException ex) {
            // do nothing
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private DataModelMainPage getDataModelEditorPage() {
        IEditorPart[] editors = findEditors(xsdInput);
        if (editors.length == 1 && editors[0] instanceof DataModelMainPage) {
            return (DataModelMainPage) editors[0];
        }
        return null;
    }

    private class XSDSelectionManagerSelectionListener implements ISelectionChangedListener {

        private Map<XSDElementDeclaration, XSDElementDeclaration> declarMap = new HashMap<XSDElementDeclaration, XSDElementDeclaration>();

        /**
         * Determines DOM node based on object (xsd node)
         * 
         * @param object
         * @return
         */
        private Object getObjectForOtherModel(Object object) {
            Node node = null;
            if (object instanceof Node) {
                node = (Node) object;
            } else if (object instanceof XSDComponent) {
                if (object instanceof XSDElementDeclaration) {
                    XSDElementDeclaration selected = (XSDElementDeclaration) object;
                    String name = selected.getName();

                    EList<XSDElementDeclaration> elementDeclarations = getXSDSchema().getElementDeclarations();

                    for (XSDElementDeclaration elem : elementDeclarations) {
                        if (elem.getName().equals(name)) {
                            node = elem.getElement();
                            break;
                        }
                    }
                } else if (object instanceof XSDParticle) {
                    XSDTerm term = ((XSDParticle) object).getTerm();
                    if (term instanceof XSDElementDeclaration) {
                        XSDElementDeclaration declar = (XSDElementDeclaration) term;
                        XSDElementDeclaration[] xsdDeclarations = getAllMarkableDeclarationOfParticle();
                        if (xsdDeclarations != null) {
                            XSDElementDeclaration content = (XSDElementDeclaration) ((XSDParticle) object).getContent();
                            boolean isReference = content.isElementDeclarationReference();
                            String name = declar.getName();
                            for (XSDElementDeclaration elem : xsdDeclarations) {
                                if (name.equals(elem.getName())) {
                                    if (isUnderSameComplexType(declar, isReference, elem)) {
                                        if (isReference) {
                                            node = declarMap.get(elem).getElement();
                                        } else {
                                            node = elem.getElement();
                                        }

                                        break;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    node = ((XSDComponent) object).getElement();
                }
            } else if (object instanceof RedefineCategoryAdapter) {
                RedefineCategoryAdapter category = (RedefineCategoryAdapter) object;
                node = category.getXSDRedefine().getElement();
            } else if (object instanceof CategoryAdapter) {
                node = ((CategoryAdapter) object).getXSDSchema().getElement();
            } else if (object instanceof XSDBaseAdapter) {
                if (((XSDBaseAdapter) object).getTarget() instanceof XSDConcreteComponent) {
                    node = ((XSDConcreteComponent) ((XSDBaseAdapter) object).getTarget()).getElement();
                }
            } else if (object instanceof String) {
                // This case was added to make the F3/hyperlink navigation work
                // when an
                // inline schema from a WSDL document is opened in the schema
                // editor.
                // The string is expected to be a URI fragment used to identify
                // an XSD
                // component in the context of the enclosing WSDL resource.

                String uriFragment = (String) object;
                Resource resource = getXSDSchema().eResource();
                EObject modelObject = resource.getEObject(uriFragment);

                if (modelObject != null && modelObject instanceof XSDComponent) {
                    XSDComponent component = (XSDComponent) modelObject;
                    node = component.getElement();
                }
            }

            // the text editor can only accept sed nodes!
            //
            if (!(node instanceof IDOMNode)) {
                node = null;
            }
            return node;
        }

        private XSDElementDeclaration[] getAllMarkableDeclarationOfParticle() {
            declarMap.clear();
            // find all entities and record their name
            List<String> concepts = new ArrayList<String>();
            SchemaTreeContentProvider schemaContentProvider = (SchemaTreeContentProvider) getDataModelEditorPage()
                    .getSchemaContentProvider();
            Object[] elementDeclarations = schemaContentProvider.getElements(getDataModelEditorPage().getXSDSchema());
            for (Object dec : elementDeclarations) {
                if (dec instanceof XSDElementDeclaration) {
                    concepts.add(((XSDElementDeclaration) dec).getName());
                }
            }

            Set<XSDTypeDefinition> visited = new HashSet<XSDTypeDefinition>();

            // add from entity
            List<XSDElementDeclaration> particles = new ArrayList<XSDElementDeclaration>();
            EList<XSDElementDeclaration> declarations = getXSDSchema().getElementDeclarations();
            for (XSDElementDeclaration dec : declarations) {
                if (concepts.contains(dec.getName())) {
                    XSDTypeDefinition typeDefinition = dec.getTypeDefinition();
                    addChildParticles(particles, typeDefinition, visited);
                }
            }

            // add from complex type
            EList<XSDTypeDefinition> typeDefinitions = getXSDSchema().getTypeDefinitions();
            for (XSDTypeDefinition definition : typeDefinitions) {
                if ((definition instanceof XSDComplexTypeDefinition) && !visited.contains(definition)) {
                    addChildParticles(particles, definition, visited);
                }
            }

            return particles.toArray(new XSDElementDeclaration[0]);
        }

        private void addChildParticles(List<XSDElementDeclaration> particles, XSDTypeDefinition typeDefinition,
                Set<XSDTypeDefinition> visited) {
            if (typeDefinition instanceof XSDComplexTypeDefinition && !visited.contains(typeDefinition)) {
                visited.add(typeDefinition);

                XSDComplexTypeDefinition complexDefinition = (XSDComplexTypeDefinition) typeDefinition;

                XSDParticle particle = (XSDParticle) complexDefinition.getContent();
                if (particle != null) {
                    XSDModelGroup content = (XSDModelGroup) particle.getTerm();
                    EList<XSDParticle> contents = content.getContents();

                    for (XSDParticle xsdParticle : contents) {
                        if (xsdParticle.getTerm() instanceof XSDElementDeclaration) {
                            XSDElementDeclaration contt = (XSDElementDeclaration) xsdParticle.getContent();
                            boolean isDeclarRef = contt.isElementDeclarationReference();
                            if (isDeclarRef) {
                                declarMap.put((XSDElementDeclaration) xsdParticle.getTerm(), contt);
                            }

                            particles.add((XSDElementDeclaration) xsdParticle.getTerm());
                            addChildParticles(particles, ((XSDElementDeclaration) xsdParticle.getTerm()).getTypeDefinition(),
                                    visited);
                        }
                    }
                }
            }
        }

        private boolean isUnderSameComplexType(XSDElementDeclaration elemOnTree, boolean isElemOnTreeBeReferenceType,
                XSDElementDeclaration elemInSource) {
            try {
                if (isElemOnTreeBeReferenceType) {
                    boolean isDeclarationReference = declarMap.get(elemInSource).isElementDeclarationReference();
                    if (isDeclarationReference && (elemOnTree.getContainer() instanceof XSDSchema)
                            && (elemInSource.getContainer() instanceof XSDSchema)) {
                        return true;
                    }

                    return false;
                } else {
                    XSDComplexTypeDefinition selContainer = (XSDComplexTypeDefinition) elemOnTree.getContainer().getContainer()
                            .getContainer().getContainer();
                    XSDComplexTypeDefinition tarContainer = (XSDComplexTypeDefinition) elemInSource.getContainer().getContainer()
                            .getContainer().getContainer();
                    if (selContainer.getContainer().getClass().equals(tarContainer.getContainer().getClass())) {
                        if (selContainer.getContainer() instanceof XSDSchema) {
                            if (selContainer.getName().equals(tarContainer.getName())) {
                                return true;
                            }
                        } else if (selContainer.getContainer() instanceof XSDElementDeclaration) {
                            XSDElementDeclaration selDec = (XSDElementDeclaration) selContainer.getContainer();
                            XSDElementDeclaration tarDec = (XSDElementDeclaration) tarContainer.getContainer();
                            if (selDec.getName().equals(tarDec.getName())) {
                                return true;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

            return false;
        }

        public void selectionChanged(SelectionChangedEvent event) {
            // do not fire selection in source editor if the current active page
            // is the InternalXSDMultiPageEditor
            // (source)
            // We only want to make source selections if the active page is
            // either the outline or properties (a modify
            // has been done via the outline or properties and not the source
            // view). We don't want to be selecting
            // and unselecting things in the source when editing in the source!!
            boolean makeSelection = true;
            if (PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage() != null) {
                IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                if (page.getActivePart() instanceof InternalXSDMultiPageEditor) {
                    if (getActiveEditor() instanceof StructuredTextEditor) {
                        makeSelection = false;
                    }
                }
            }

            // do not fire selection in source editor if selection event came
            // from source editor
            if (event.getSource() != getTextEditor().getSelectionProvider() && makeSelection) {
                ISelection selection = event.getSelection();
                if (selection instanceof IStructuredSelection) {
                    List otherModelObjectList = new ArrayList();
                    for (Iterator i = ((IStructuredSelection) selection).iterator(); i.hasNext();) {
                        Object modelObject = i.next();
                        Object otherModelObject = getObjectForOtherModel(modelObject);
                        if (otherModelObject != null) {
                            otherModelObjectList.add(otherModelObject);
                        }
                    }
                    if (!otherModelObjectList.isEmpty()) {
                        if (getActivePage() == SOURCE_PAGE_INDEX) {
                            StructuredSelection nodeSelection = new StructuredSelection(otherModelObjectList);
                            getTextEditor().getSelectionProvider().setSelection(nodeSelection);
                        }
                    }
                }
            }
        }

        public void doSetSelection() {
            ISelection iSelection = getSelectionManager().getSelection();
            if (iSelection != null) {
                Object firstElement = ((StructuredSelection) iSelection).getFirstElement();
                Object otherModelObject = getObjectForOtherModel(firstElement);
                if (otherModelObject != null) {
                    StructuredSelection nodeSelection = new StructuredSelection(otherModelObject);
                    getTextEditor().getSelectionProvider().setSelection(nodeSelection);
                }
            }
        }
    }

    @Override
    protected void createPages() {
        super.createPages();
        fXSDSelectionListener = new XSDSelectionManagerSelectionListener();
        getSelectionManager().addSelectionChangedListener(fXSDSelectionListener);

        ((CTabFolder) getContainer()).addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (preActivePageIndex == SOURCE_PAGE_INDEX) {
                    Exception exc = validateXsdSourceEditor();
                    if (exc != null) {
                        setActivePage(preActivePageIndex);
                        ErrorExceptionDialog.openError(getSite().getShell(), Messages.XSDEditor_ChangedPageErrorTitle,
                                CommonUtil.getErrMsgFromException(exc));
                        return;
                    }
                }
                preActivePageIndex = getActivePage();
            }
        });
    }

    @Override
    protected void createSourcePage() {
        this.structuredTextEditor = new StructuredTextEditor() {

            @Override
            public boolean isEditable() {
                if (isReadOnly()) {
                    return false;
                }
                return super.isEditable();
            }

        };
        try {
            int index = addPage(this.structuredTextEditor, getEditorInput());
            setPageText(index, org.eclipse.wst.xsd.ui.internal.adt.editor.Messages._UI_LABEL_SOURCE);
            this.structuredTextEditor.update();
            this.structuredTextEditor.setEditorPart(this);
            this.structuredTextEditor.addPropertyListener(this);

            /*
             * **please don't remove this annotation util the problem is really resolve.
             * 
             * Here add a property listener to source editor to achieve a effect that when source editor is modified by
             * user, notify the model editor that it should be in dirty state.
             * 
             * this is not a perfect resolution of issue TMDM-6403, because we have not find the real reason that lead
             * to that result. the more detailed thing is the variable 'fSynchronizationStamp' in class
             * 'ResourceTextFileBuffer' is updated in some place and this variable finally affect the variable
             * 'fCanBeSaved' (see documentChanged method of the inner class DocumentListener)which indicated the source
             * editor's dirty state, so affect combined editor's dirty state,then affect the combined editor's property
             * listener's execution, then lead to the editor's save and model validation.
             */
            this.structuredTextEditor.addPropertyListener(getDirtyNotifyListener());
            firePropertyChange(1);
        } catch (PartInitException e) {
            ErrorDialog.openError(getSite().getShell(), Messages.XSDEditor_ErrorMessage, null, e.getStatus());
        }
    }

    private IPropertyListener getDirtyNotifyListener() {
        if (dirtyNotifyListener == null) {
            dirtyNotifyListener = new IPropertyListener() {

                public void propertyChanged(Object source, int propId) {
                    if (source == structuredTextEditor && propId == PROP_DIRTY) {
                        boolean dirty = structuredTextEditor.isDirty();
                        if (dirty) {
                            DataModelMainPage dMainPage = getdMainPage();
                            if (dMainPage != null) {
                                dMainPage.setDirty(dirty);
                            }
                        }
                    }
                }
            };
        }

        return dirtyNotifyListener;
    }

    private Exception validateXsdSourceEditor() {
        String xsd = getTextEditor().getTextViewer().getDocument().get();
        try {
            XSDSchema xsdSchema = Util.createXsdSchema(xsd, xobject);
            String error = validateDiagnoses(xsdSchema);
            if (!"".equals(error)) { //$NON-NLS-1$
                return new IllegalAccessException(error);
            }
            return null;
        } catch (SAXParseException e) {
            return e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return e;
        }
    }

    /**
     * DOC talend-mdm Comment method "validateDiagnoses".
     * 
     * @param xsdSchema
     * @return
     */
    private String validateDiagnoses(XSDSchema xsdSchema) {
        String msg_omit[] = { Messages.XsdOmit1, Messages.XsdOmit2, Messages.XsdOmit3, Messages.XsdOmit4, Messages.XsdOmit5 };
        xsdSchema.clearDiagnostics();
        xsdSchema.getAllDiagnostics().clear();
        xsdSchema.validate();
        EList<XSDDiagnostic> diagnoses = xsdSchema.getAllDiagnostics();
        String error = "";//$NON-NLS-1$
        Set<String> errors = new HashSet<String>();
        for (int i = 0; i < diagnoses.size(); i++) {
            XSDDiagnostic dia = diagnoses.get(i);
            XSDDiagnosticSeverity servity = dia.getSeverity();
            if (servity == XSDDiagnosticSeverity.ERROR_LITERAL || servity == XSDDiagnosticSeverity.FATAL_LITERAL) {
                boolean omit = false;
                for (String msg : msg_omit) {
                    if (dia.getMessage().indexOf(msg) != -1) {
                        omit = true;
                        break;
                    }
                }
                if (!omit && !errors.contains(dia.getMessage())) {
                    error += dia.getMessage() + "\n";//$NON-NLS-1$
                    errors.add(dia.getMessage());
                }
            }
        }
        return error;
    }

    @Override
    public String getContributorId() {
        if (getActiveEditor() instanceof DataModelMainPage) {
            return CONTRUIBUTIONID_DATAMODELPAGE;
        }
        return super.getContributorId();
    }

    @Override
    public Object getAdapter(Class type) {
        if (type == IPropertySheetPage.class) {
            int activePageIndex = getActivePage();
            if (activePageIndex == DESIGN_PAGE_INDEX || activePageIndex == SOURCE_PAGE_INDEX) {
                return new XSDTabbedPropertySheetPage(this);
            }
            return new TabbedPropertySheetPage(this) {

                @Override
                public String getTitleText(ISelection selection) {
                    String text = super.getTitleText(selection);
                    text = isReadOnly() ? NLS.bind(Messages.XSDEditor_SheetPageTitle, text) : text;
                    return text;
                }
            };
        }

        if (type == DataModelMainPage.class) {
            for (int i = 0; i < getPageCount(); i++) {
                if (getEditor(i) instanceof DataModelMainPage) {
                    return getEditor(i);
                }
            }
        }
        if (Boolean.class == type) {
            return new Boolean(isReadOnly());
        }
        // Add this for enable false the properties view while this editor is
        // readOnly.
        if (XSDSchema.class == type && isReadOnly()) {
            return null;
        }
        return super.getAdapter(type);

    }

    private void refreshPropertyView() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart propView = page.findView(MDMPerspective.VIEWID_PROPERTYVIEW);
        if (propView != null) {
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().hideView(propView);
        }
        try {
            page.showView(MDMPerspective.VIEWID_PROPERTYVIEW);
        } catch (PartInitException e) {
            e.printStackTrace();
        }
    }

    private void resetTreeSelection(int newPageIndex) {
        DataModelMainPage dataModelEditorPage = getDataModelEditorPage();
        if (dataModelEditorPage != null) {
            TreeViewer treeViewer = dataModelEditorPage.getTreeViewer();
            if (newPageIndex == MODEL_PAGE_INDEX) {
                treeViewer.setSelection(null);
            } else if (newPageIndex == SOURCE_PAGE_INDEX) {
                IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
                if (!selection.isEmpty()) {
                    Object firstElement = selection.getFirstElement();
                    if ((firstElement instanceof XSDIdentityConstraintDefinition)) {
                        XSDIdentityConstraintDefinition cdf = (XSDIdentityConstraintDefinition) firstElement;
                        XSDConcreteComponent container = cdf.getContainer();
                        treeViewer.setSelection(new StructuredSelection(container));
                    } else if ((firstElement instanceof XSDXPathDefinition)) {
                        XSDXPathDefinition pathdef = (XSDXPathDefinition) firstElement;
                        XSDConcreteComponent container = pathdef.getContainer().getContainer();
                        treeViewer.setSelection(new StructuredSelection(container));
                    } else if (firstElement instanceof XSDAnnotation) {
                        XSDAnnotation annotation = (XSDAnnotation) firstElement;
                        XSDConcreteComponent container = annotation.getContainer();
                        treeViewer.setSelection(new StructuredSelection(container));
                    }
                }
            }
        }
    }

    @Override
    public void setFocus() {
        Object selectedPage = getSelectedPage();
        if (selectedPage instanceof IEditorPart) {
            ((IEditorPart) selectedPage).setFocus();
        } else {
            super.setFocus();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.amalto.workbench.editors.IServerObjectEditorState#isLocalInput()
     */
    public boolean isLocalInput() {
        return false;
    }

    public void setName(String name) {
        setPartName(name);
    }

    @Override
    public void dispose() {
        try {
            if (isDirty()) {
                IFile file = getXSDFile(xobject);
                file.setCharset("utf-8", null);//$NON-NLS-1$
                file.setContents(new ByteArrayInputStream(fileContents), IFile.FORCE, null);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            try {

                IEditorInput input = getEditorInput();
                if (input != null && input instanceof XSDEditorInput) {
                    XSDEditorInput editorInput = (XSDEditorInput) input;
                    editorInput.dispose();
                }
                System.gc();
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
            }
        }

        super.dispose();
    }

    public DataModelMainPage getdMainPage() {
        return null;
    }
}
