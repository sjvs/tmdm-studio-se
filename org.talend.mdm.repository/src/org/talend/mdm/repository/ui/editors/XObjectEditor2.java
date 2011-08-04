// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
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

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.mdm.repository.i18n.Messages;
import org.talend.mdm.repository.model.mdmproperties.MDMServerObjectItem;
import org.talend.mdm.repository.model.mdmserverobject.MDMServerObject;
import org.talend.mdm.repository.utils.Bean2EObjUtil;
import org.talend.repository.model.IProxyRepositoryFactory;

import com.amalto.workbench.editors.AFormPage;
import com.amalto.workbench.editors.XObjectEditor;
import com.amalto.workbench.models.TreeObject;

/**
 * DOC hbhong class global comment. Detailled comment
 */
public class XObjectEditor2 extends XObjectEditor {

    static Logger log = Logger.getLogger(XObjectEditor2.class);

    public static final String EDITOR_ID = "org.talend.mdm.repository.ui.editors.XObjectEditor2"; //$NON-NLS-1$

    IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();

    @Override
    public void doSave(IProgressMonitor monitor) {

        this.saveInProgress = true;
        // For the XMLEditor(the schema editor for the data model),it should be saved and then just refresh the data
        // model page and do nothing else if there are some changes.
        if (xmlEditor != null && this.getCurrentPage() == 1) {
            xmlEditor.doSave(monitor);
            ((AFormPage) (formPages.get(0))).refreshPage();
            return;
        }
        int numPages = formPages.size();
        monitor.beginTask(Messages.bind(Messages.XObjectEditor2_saving, this.getEditorInput().getName()), numPages + 1);
        for (int i = 0; i < numPages; i++) {
            if ((formPages.get(i)) instanceof AFormPage) {
                if (!((AFormPage) (formPages.get(i))).beforeDoSave())
                    return;
            }
            (formPages.get(i)).doSave(monitor);
            monitor.worked(1);
            if (monitor.isCanceled()) {
                this.saveInProgress = false;
                return;
            }
        }
        // if(xmlEditor!=null)xmlEditor.doSave(monitor);
        // perform the actual save
        boolean saved = saveResourceToRepository();
        if (xmlEditor != null && saved) {
            xmlEditor.refresh();
        }
        monitor.done();
    }

    private boolean saveResourceToRepository() {
        XObjectEditorInput2 editorInput = (XObjectEditorInput2) this.getEditorInput();
        TreeObject xobject = (TreeObject) editorInput.getModel();
        MDMServerObjectItem serverObjectItem = (MDMServerObjectItem) editorInput.getInputItem();
        MDMServerObject serverObject = serverObjectItem.getMDMServerObject();
        EObject eObj = Bean2EObjUtil.getInstance().convertFromBean2EObj(xobject.getWsObject(), serverObject);
        if (eObj != null) {
            IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
            try {
                factory.save(serverObjectItem);
                // TODO should call the following,but the page in editor has many call to remote webService ,it will
                // search ServerRoot which cause a NPE
                // xobject.fireEvent(IXObjectModelListener.SAVE, xobject.getParent(), xobject);
                editorDirtyStateChanged();
                return true;
            } catch (PersistenceException e) {
                log.error(e.getMessage(), e);
            }
        }
        return false;
    }

    @Override
    public boolean isLocalInput() {
        return super.isLocalInput();
    }
    
}