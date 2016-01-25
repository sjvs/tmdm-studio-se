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
package org.talend.mdm.repository.ui.wizards.imports;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.internal.wizards.datatransfer.TarException;
import org.eclipse.ui.internal.wizards.datatransfer.TarFile;
import org.eclipse.ui.internal.wizards.datatransfer.TarLeveledStructureProvider;
import org.eclipse.ui.internal.wizards.datatransfer.ZipLeveledStructureProvider;
import org.eclipse.ui.progress.UIJob;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.JobDocumentationItem;
import org.talend.core.model.properties.JobletDocumentationItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.RepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.mdm.repository.core.IServerObjectRepositoryType;
import org.talend.mdm.repository.core.command.CommandManager;
import org.talend.mdm.repository.core.command.ICommand;
import org.talend.mdm.repository.core.impl.transformerV2.ITransformerV2NodeConsDef;
import org.talend.mdm.repository.core.impl.view.IViewNodeConstDef;
import org.talend.mdm.repository.core.service.ContainerCacheService;
import org.talend.mdm.repository.core.service.ImportService;
import org.talend.mdm.repository.i18n.Messages;
import org.talend.mdm.repository.model.mdmproperties.MDMServerObjectItem;
import org.talend.mdm.repository.model.mdmproperties.MdmpropertiesFactory;
import org.talend.mdm.repository.model.mdmproperties.WSDataModelItem;
import org.talend.mdm.repository.model.mdmproperties.WSTransformerV2Item;
import org.talend.mdm.repository.model.mdmproperties.WSViewItem;
import org.talend.mdm.repository.model.mdmserverobject.MDMServerObject;
import org.talend.mdm.repository.model.mdmserverobject.MdmserverobjectFactory;
import org.talend.mdm.repository.model.mdmserverobject.WSDataModelE;
import org.talend.mdm.repository.ui.dialogs.importexchange.ImportExchangeOptionsDialogR;
import org.talend.mdm.repository.ui.dialogs.lock.LockedObjectDialog;
import org.talend.mdm.repository.ui.navigator.MDMRepositoryView;
import org.talend.mdm.repository.ui.wizards.imports.viewer.ImportRepositoryObjectCheckTreeViewer;
import org.talend.mdm.repository.utils.IOUtil;
import org.talend.mdm.repository.utils.RepositoryResourceUtil;
import org.talend.mdm.repository.utils.RepositoryTransformUtil;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.repository.imports.ImportItemUtil;
import org.talend.repository.imports.ItemRecord;
import org.talend.repository.imports.ResourcesManager;
import org.talend.repository.imports.ResourcesManagerFactory;
import org.talend.repository.model.ERepositoryStatus;

import com.amalto.workbench.dialogs.ImportExchangeOptionsDialog;
import com.amalto.workbench.exadapter.ExAdapterManager;
import com.amalto.workbench.export.ImportItemsWizard;
import com.amalto.workbench.widgets.WidgetFactory;

/**
 * DOC hywang class global comment. Detailled comment
 */
public class MDMImportRepositoryItemsWizard extends ImportItemsWizard {

    private ImportItemUtil repositoryUtil = new ImportItemUtil();

    private final List<String> errors = new ArrayList<String>();

    private List<ItemRecord> selectedItems = new ArrayList<ItemRecord>();

    private ResourcesManager manager;

    private static Logger log = Logger.getLogger(MDMImportRepositoryItemsWizard.class);

    ImportRepositoryObjectCheckTreeViewer checkTreeViewer;

    private IMDMImportRepositoryItemsWizardExAdapter exAdapter;

    public MDMImportRepositoryItemsWizard(IStructuredSelection sel) {
        super(sel);
        this.exAdapter = ExAdapterManager.getAdapter(this, IMDMImportRepositoryItemsWizardExAdapter.class);
    }

    @Override
    protected void exchangeImport() {
        ImportExchangeOptionsDialog dlg = getExchangeOptionsDialog();
        dlg.setBlockOnOpen(true);
        if (dlg.open() == Window.OK) {

            File directory = new File(zipFileRepository.toString());
            File[] files = directory.listFiles(new FileFilter() {

                public boolean accept(File pathname) {
                    return pathname.getName().endsWith(".xsd"); //$NON-NLS-1$
                }
            });
            if (null != files && files.length > 0) {
                try {
                    MDMRepositoryView view = MDMRepositoryView.show();
                    for (File file : files) {
                        final String label = file.getName().substring(0, file.getName().lastIndexOf('.'));
                        final WSDataModelItem item = MdmpropertiesFactory.eINSTANCE.createWSDataModelItem();
                        ItemState itemState = PropertiesFactory.eINSTANCE.createItemState();
                        item.setState(itemState);
                        WSDataModelE dataModel = MdmserverobjectFactory.eINSTANCE.createWSDataModelE();
                        dataModel.setName(label);
                        InputStream stream = null;
                        try {
                            stream = new FileInputStream(file);
                            dataModel.setXsdSchema(IOUtils.toString(stream));
                            item.setWsDataModel(dataModel);
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        }
                        IOUtils.closeQuietly(stream);
                        item.getState().setPath(""); //$NON-NLS-1$
                        RepositoryResourceUtil.createItem(item, label);
                        view.refreshRootNode(IServerObjectRepositoryType.TYPE_DATAMODEL);
                    }
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                }
                WizardDialog dialog = (WizardDialog) MDMImportRepositoryItemsWizard.this.getContainer();
                dialog.close();
            } else {
                MessageDialog.openWarning(getShell(), null, Messages.NO_XSD_RESOURCE);
            }
        }
    }

    @Override
    public boolean performFinish() {
        if (!showLockedObjDialog(getCheckedObjects())) {
            return false;
        }
        boolean result = super.performFinish();

        return result;
    }

    List<ItemRecord> toImportItemRecords = new LinkedList<ItemRecord>();

    private boolean showLockedObjDialog(Object[] objs) {

        List<IRepositoryViewObject> viewObjs = new LinkedList<IRepositoryViewObject>();
        ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        for (Object obj : objs) {
            if (obj instanceof ItemRecord) {
                ItemRecord itemRecord = (ItemRecord) obj;
                try {
                    IRepositoryViewObject viewObj = factory.getLastVersion(itemRecord.getProperty().getId());
                    if (viewObj != null) {
                        if (RepositoryResourceUtil.isLockedViewObject(viewObj)) {
                            viewObjs.add(viewObj);
                        } else {
                            toImportItemRecords.add(itemRecord);
                        }
                    } else {
                        toImportItemRecords.add(itemRecord);
                    }
                } catch (PersistenceException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        LockedObjectDialog lockDialog = new LockedObjectDialog(getShell(), Messages.ImportServerObjectWizard_lockedObjectMessage,
                viewObjs);
        if (lockDialog.needShowDialog() && lockDialog.open() == IDialogConstants.CANCEL_ID) {
            return false;
        }
        return true;
    }

    private void filterImportedItems(List<ItemRecord> toImportItemRecords) {
        for (ItemRecord itemRecord : toImportItemRecords) {
            Item item = itemRecord.getProperty().getItem();
            String statePath = item.getState().getPath();
            if (item instanceof WSViewItem) {
                if (RepositoryTransformUtil.getInstance().getViewType(item.getProperty().getLabel()) == IViewNodeConstDef.TYPE_WEBFILTER) {
                    if (!statePath.startsWith(IPath.SEPARATOR + IViewNodeConstDef.PATH_WEBFILTER)) {
                        item.getState().setPath(IPath.SEPARATOR + IViewNodeConstDef.PATH_WEBFILTER + statePath);
                    }
                } else {
                    if (!statePath.startsWith(IPath.SEPARATOR + IViewNodeConstDef.PATH_SEARCHFILTER)) {
                        item.getState().setPath(IPath.SEPARATOR + IViewNodeConstDef.PATH_SEARCHFILTER + statePath);
                    }
                }
            }

            if (item instanceof WSTransformerV2Item) {
                String processName = item.getProperty().getLabel();
                int processType = RepositoryTransformUtil.getInstance().getProcessType(processName);
                switch (processType) {
                case ITransformerV2NodeConsDef.TYPE_BEFORESAVE:
                    if (!statePath.startsWith(IPath.SEPARATOR + ITransformerV2NodeConsDef.PATH_BEFORESAVE)) {
                        item.getState().setPath(IPath.SEPARATOR + ITransformerV2NodeConsDef.PATH_BEFORESAVE + statePath);
                    }
                    break;
                case ITransformerV2NodeConsDef.TYPE_BEFOREDEL:
                    if (!statePath.startsWith(IPath.SEPARATOR + ITransformerV2NodeConsDef.PATH_BEFOREDEL)) {
                        item.getState().setPath(IPath.SEPARATOR + ITransformerV2NodeConsDef.PATH_BEFOREDEL + statePath);
                    }
                    break;
                case ITransformerV2NodeConsDef.TYPE_ENTITYACTION:
                    if (!statePath.startsWith(IPath.SEPARATOR + ITransformerV2NodeConsDef.PATH_ENTITYACTION)) {
                        item.getState().setPath(IPath.SEPARATOR + ITransformerV2NodeConsDef.PATH_ENTITYACTION + statePath);
                    }
                    break;
                case ITransformerV2NodeConsDef.TYPE_WELCOMEACTION:
                    if (!statePath.startsWith(IPath.SEPARATOR + ITransformerV2NodeConsDef.PATH_WELCOMEACTION)) {
                        item.getState().setPath(IPath.SEPARATOR + ITransformerV2NodeConsDef.PATH_WELCOMEACTION + statePath);
                    }
                    break;
                case ITransformerV2NodeConsDef.TYPE_SMARTVIEW:
                    if (!statePath.startsWith(IPath.SEPARATOR + ITransformerV2NodeConsDef.PATH_SMARTVIEW)) {
                        item.getState().setPath(IPath.SEPARATOR + ITransformerV2NodeConsDef.PATH_SMARTVIEW + statePath);
                    }
                    break;
                case ITransformerV2NodeConsDef.TYPE_OTHER:
                    if (!statePath.startsWith(IPath.SEPARATOR + ITransformerV2NodeConsDef.PATH_OTHER)) {
                        item.getState().setPath(IPath.SEPARATOR + ITransformerV2NodeConsDef.PATH_OTHER + statePath);
                    }
                    break;
                default:
                    break;
                }
            }
        }
    }

    final List<String> importedIds = new LinkedList<String>();

    @Override
    public void doImport(final Object[] selectedObjs, final IProgressMonitor monitor) {
        ImportService.setImporting(true);
        monitor.beginTask("Import MDM object", toImportItemRecords.size() * 3); //$NON-NLS-1$

        RepositoryWorkUnit rwu = new RepositoryWorkUnit("MDM Import Items") { //$NON-NLS-1$

            @Override
            protected void run() throws LoginException, PersistenceException {
                filterImportedItems(toImportItemRecords);

                repositoryUtil.importItemRecords(manager, toImportItemRecords, monitor, isOverride, null, ""); //$NON-NLS-1$

                for (ItemRecord itemRec : toImportItemRecords) {
                    MDMServerObject serverObj = null;
                    try {
                        ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
                        List<IRepositoryViewObject> allVersion = factory.getAllVersion(itemRec.getItemId());
                        for (IRepositoryViewObject object : allVersion) {
                            if (object.getVersion() != null && object.getVersion().equals(itemRec.getItemVersion())) {
                                Property property = object.getProperty();
                                Item item = property.getItem();
                                boolean needSave = false;
                                importedIds.add(itemRec.getItemId());
                                if (item instanceof MDMServerObjectItem) {

                                    serverObj = ((MDMServerObjectItem) item).getMDMServerObject();
                                    if (serverObj.getLastServerName() != null) {
                                        serverObj.setLastServerName(null);
                                        needSave = true;
                                    }
                                    if (serverObj.getLastServerDef() != null) {
                                        serverObj.setLastServerDef(null);
                                        needSave = true;
                                    }

                                    if (item instanceof WSDataModelItem && exAdapter != null) {
                                        exAdapter.handleDataModelItem(item);
                                    }

                                } else {
                                    EMap additionalProperties = property.getAdditionalProperties();
                                    if (additionalProperties != null) {
                                        additionalProperties.remove(RepositoryResourceUtil.PROP_LAST_SERVER_DEF);
                                        needSave = true;
                                    }
                                }
                                try {
                                    if (needSave) {
                                        factory.save(item, true);
                                    }
                                    if (itemRec.isValid()) {

                                        String[] split = itemRec.getLabel().split(" "); //$NON-NLS-1$
                                        String name = split.length > 0 ? split[0] : null;

                                        // // flagged as new
                                        if (name != null) {
                                            CommandManager.getInstance().pushCommand(ICommand.CMD_ADD, itemRec.getItemId(), name);
                                        }

                                    }
                                    ContainerCacheService.put(object);
                                } catch (Exception e) {
                                    log.error(e.getMessage(), e);
                                }
                            }
                        }
                    } catch (PersistenceException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        };
        ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(rwu);
        monitor.done();

        ImportService.setImporting(false);

        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                MDMRepositoryView.show().getCommonViewer().refresh();
            }
        });
        if (exAdapter != null) {
            exAdapter.syncWorkflow();
        }
    }

    @Override
    protected void createOverwriteBtn(Composite composite) {
        btnOverwrite = new Button(composite, SWT.CHECK);
        btnOverwrite.setText(Messages.Overwrite_Exists_Items);
        btnOverwrite.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                isOverride = btnOverwrite.getSelection();
                parse();
            }

        });
    }

    @Override
    protected ImportExchangeOptionsDialog getExchangeOptionsDialog() {
        FormToolkit toolkit = WidgetFactory.getWidgetFactory();
        ImportExchangeOptionsDialog dlg = new ImportExchangeOptionsDialogR(getShell(), toolkit, true, zipFileRepository);
        dlg.setRadioEnable(true);
        dlg.create();

        return dlg;
    }

    @Override
    protected void createViewer() {
        checkTreeViewer = new ImportRepositoryObjectCheckTreeViewer(repositoryUtil);
    }

    /** need to override this method to list all the import items to the viewer **/
    @Override
    protected void parses(boolean importFromArchieve, String zipFilePath, IProgressMonitor monitor) {
        selectedItems.clear();
        String importPath = importFromArchieve ? zipFilePath : importFolder;
        final Collection<ItemRecord> items = new ArrayList<ItemRecord>();

        monitor.beginTask(Messages.MDMImportRepositoryItemsWizard_searchProject, 100);
        File directory = new File(importPath);
        monitor.worked(10);
        if (importFromArchieve && IOUtil.isTarFile(importPath)) {
            TarFile sourceTarFile = getSpecifiedTarSourceFile(importPath);
            if (sourceTarFile == null) {
                return;
            }
            TarLeveledStructureProvider provider = new TarLeveledStructureProvider(sourceTarFile);
            manager = ResourcesManagerFactory.getInstance().createResourcesManager(provider);

            if (!manager.collectPath2Object(provider.getRoot())) {
                return;
            }
        } else if (importFromArchieve && IOUtil.isZipFile(importPath)) {
            ZipFile sourceFile = getSpecifiedZipSourceFile(importPath);
            if (sourceFile == null) {
                return;
            }
            ZipLeveledStructureProvider provider = new ZipLeveledStructureProvider(sourceFile);
            manager = ResourcesManagerFactory.getInstance().createResourcesManager(provider);

            if (!manager.collectPath2Object(provider.getRoot())) {
                return;
            }
        } else if (!importFromArchieve && directory.isDirectory()) {
            manager = ResourcesManagerFactory.getInstance().createResourcesManager();

            if (!manager.collectPath2Object(directory)) {
                return;
            }
        } else {
            monitor.worked(60);
        }
        repositoryUtil.clearAllData();
        if (manager != null) {
            items.addAll(repositoryUtil.populateItems(manager, isOverride, monitor));
        }
        updateErrors(items);
        selectedItems.addAll(items);

        if (this.noVaildItems(items)) {
            Display.getDefault().syncExec(new Runnable() {

                public void run() {
                    MessageDialog.openWarning(getContainer().getShell(), Messages.AddBrowseItemsWizardR_warning,
                            Messages.RepositoryViewImportRepositoryItem_nothingImport);
                    checkTreeViewer.getViewer().setInput(Collections.EMPTY_LIST);
                }
            });
        } else {
            this.getShell().getDisplay().syncExec(new Runnable() {

                public void run() {
                    checkTreeViewer.getViewer().setInput(repositoryUtil.getTreeViewInput());
                }
            });
        }
        monitor.done();

    }

    private void updateErrors(final Collection<ItemRecord> items) {
        errors.clear();
        for (ItemRecord itemRecord : items) {

            if (itemRecord.getExistingItemWithSameId() != null
                    && itemRecord.getExistingItemWithSameId() instanceof RepositoryViewObject) {
                RepositoryViewObject reObject = (RepositoryViewObject) itemRecord.getExistingItemWithSameId();
                if (itemRecord.getProperty() != null && reObject != null) {
                    if (itemRecord.getProperty().getId().equals(reObject.getId())
                            && itemRecord.getProperty().getLabel().equals(reObject.getLabel())) {
                        for (String error : itemRecord.getErrors()) {
                            errors.add(Messages.bind(Messages.MDMImportRepositoryItemsWizard_errorMsg, itemRecord.getItemName(),
                                    error));
                        }
                    } else {

                        // if item is locked, cannot overwrite
                        ERepositoryStatus status = reObject.getRepositoryStatus();
                        if (status == ERepositoryStatus.LOCK_BY_OTHER || status == ERepositoryStatus.LOCK_BY_USER) {
                            for (String error : itemRecord.getErrors()) {
                                errors.add(Messages.bind(Messages.MDMImportRepositoryItemsWizard_errorMsg,
                                        itemRecord.getItemName(), error));
                            }
                        }
                    }
                }
            } else {
                if (itemRecord.getProperty() != null) {
                    Item item = itemRecord.getProperty().getItem();
                    if (item != null && (item instanceof JobDocumentationItem || item instanceof JobletDocumentationItem)) {
                        continue;
                    }
                    for (String error : itemRecord.getErrors()) {
                        errors.add(Messages.bind(Messages.MDMImportRepositoryItemsWizard_errorMsg, itemRecord.getItemName(),
                                error));
                    }
                }
            }
        }
        //
        checkTreeViewer.updateErrors(errors);
    }

    @Override
    protected Object[] getCheckedObjects() {
        return checkTreeViewer.getCheckNodes();
    }

    private ZipFile getSpecifiedZipSourceFile(String fileName) {
        if (fileName.length() == 0) {
            return null;
        }

        try {
            return new ZipFile(fileName);
        } catch (ZipException e) {
            displayErrorDialog(Messages.MDMImportRepositoryItemsWizard_badFormat);
        } catch (IOException e) {
            displayErrorDialog(Messages.MDMImportRepositoryItemsWizard_canNotRead);
        }

        zip.getText().setFocus();
        return null;
    }

    private TarFile getSpecifiedTarSourceFile(String fileName) {
        if (fileName.length() == 0) {
            return null;
        }

        try {
            return new TarFile(fileName);
        } catch (TarException e) {
            displayErrorDialog(Messages.MDMImportRepositoryItemsWizard_badFormat);
        } catch (IOException e) {
            displayErrorDialog(Messages.MDMImportRepositoryItemsWizard_canNotRead);
        }

        zip.getText().setFocus();
        return null;
    }

    protected void displayErrorDialog(String message) {
        MessageDialog.openError(getContainer().getShell(), getErrorDialogTitle(), message);
    }

    protected String getErrorDialogTitle() {
        return Messages.MDMImportRepositoryItemsWizard_internalError;
    }

    @Override
    protected void refreshViewJob() {
        new UIJob(Messages.MDMImportRepositoryItemsWizard_refreshServer) {

            @Override
            public IStatus runInUIThread(IProgressMonitor monitor) {
                MDMRepositoryView.show().getCommonViewer().refresh();
                return Status.OK_STATUS;
            }

        }.schedule();
    }

    @Override
    protected Composite initItemTreeViewer(Composite composite) {
        Composite returnComposite = checkTreeViewer.createItemList(composite);
        checkTreeViewer.getViewer().setInput(null);
        checkTreeViewer.setItemText(Messages.MDMImportRepositoryItemsWizard_toImport);
        return returnComposite;
    }

    private boolean noVaildItems(Collection<ItemRecord> items) {

        if (items.size() == 0) {
            return true;
        }
        Iterator<ItemRecord> itemIt = items.iterator();
        while (itemIt.hasNext()) {// channged by hqzhang, if there are any valid items, then can import
            ItemRecord itemRecord = itemIt.next();
            if (!(itemRecord.getExistingItemWithSameId() == null && itemRecord.getErrors() != null && itemRecord.getErrors()
                    .size() > 0)) {
                return false;
            }
        }
        return true;
    }

    public List<String> getImportedIds() {
        return this.importedIds;
    }

}
