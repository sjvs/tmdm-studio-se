// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.mdm.repository.core;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.actions.BaseSelectionListenerAction;
import org.eclipse.ui.navigator.CommonViewer;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.mdm.repository.core.service.ContainerCacheService;
import org.talend.mdm.repository.i18n.Messages;
import org.talend.mdm.repository.models.FolderRepositoryObject;
import org.talend.mdm.repository.ui.dialogs.message.MultiStatusDialog;
import org.talend.mdm.repository.ui.navigator.MDMRepositoryView;
import org.talend.mdm.repository.utils.RepositoryResourceUtil;
import org.talend.repository.RepositoryWorkUnit;

/**
 * DOC hbhong class global comment. Detailled comment <br/>
 * 
 */
public abstract class AbstractRepositoryAction extends BaseSelectionListenerAction {

    public static final String GROUP_EDIT = "group.edit"; //$NON-NLS-1$

    public static final String GROUP_COMMON = "group.common"; //$NON-NLS-1$

    public static final String GROUP_EXPORT = "group.export"; //$NON-NLS-1$

    public static final String GROUP_SERVER = "group.server"; //$NON-NLS-1$

    public static final String GROUP_DEPLOY = "group.deploy"; //$NON-NLS-1$

    public static final String GROUP_SVN = "group.svn"; //$NON-NLS-1$

    protected CommonViewer commonViewer;

    /**
     * DOC hbhong AbstractRepositoryAction constructor comment.
     * 
     * @param text
     */
    protected AbstractRepositoryAction(String text) {
        super(text);
    }

    public abstract String getGroupName();

    public void initCommonViewer(CommonViewer commonViewer) {
        this.commonViewer = commonViewer;

    }

    @SuppressWarnings("unchecked")
    public List<Object> getSelectedObject() {

        IStructuredSelection structuredSelection = getStructuredSelection();
        if (structuredSelection.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<Object> result = new LinkedList<Object>();
        for (Iterator<Object> il = structuredSelection.iterator(); il.hasNext();) {
            result.add(il.next());
        }
        return result;
    }

    protected Shell getShell() {
        if (commonViewer != null) {
            return commonViewer.getControl().getShell();
        } else {
            return MDMRepositoryView.show().getCommonViewer().getControl().getShell();
        }
    }

    public boolean isVisible(IRepositoryViewObject viewObj) {
        return true;
    }

    @Override
    public void run() {
        if (needValidateLockedObject()) {
            if (isLocked()) {
                MessageDialog.openError(getShell(), Messages.AbstractRepositoryAction_lockedObjTitle, getAlertLockedMsg());
            } else {
                runRWU();
            }
        } else {
            runRWU();
        }
    }

    protected boolean isLocked() {
        List<Object> selectedObject = getSelectedObject();
        if (selectedObject != null && !selectedObject.isEmpty()) {
            Object object = selectedObject.get(0);
            if (object instanceof IRepositoryViewObject) {
                return RepositoryResourceUtil.isLockedAndEdited((IRepositoryViewObject) object);
            }
        }
        return false;
    }

    protected boolean needValidateLockedObject() {
        return false;
    }

    protected String getAlertLockedMsg() {
        return Messages.AbstractRepositoryAction_lockedObjMessage;
    }

    protected void runRWU() {
        RepositoryWorkUnit<Object> repositoryWorkUnit = new RepositoryWorkUnit<Object>(Messages.OpenObjectAction_open, this) {

            @Override
            protected void run() throws LoginException, PersistenceException {
                doRun();
            }
        };
        repositoryWorkUnit.setAvoidUnloadResources(true);
        CoreRuntimePlugin.getInstance().getProxyRepositoryFactory().executeRepositoryWorkUnit(repositoryWorkUnit);
    }

    protected abstract void doRun();

    protected void refreshParent() {
        Object object = getSelectedObject().get(0);
        commonViewer.setSelection(new StructuredSelection());
        if (object instanceof IRepositoryViewObject) {
            final IRepositoryViewObject parent = ContainerCacheService.getParent((IRepositoryViewObject) object);
            if (parent != null) {
                Display.getDefault().syncExec(new Runnable() {

                    public void run() {

                        if (parent != null) {
                            commonViewer.refresh(parent);
                        }
                    }
                });
                commonViewer.refresh(parent);
            }
        }

    }

    protected void refreshRepositoryRoot(ERepositoryObjectType type) {
        Object input = getCommonViewer().getInput();
        if (input != null && input instanceof IRepositoryViewObject[]) {
            for (IRepositoryViewObject viewObject : (IRepositoryViewObject[]) input) {
                if (refreshRepositoryContainer(viewObject, type)) {
                    return;
                }
                List<IRepositoryViewObject> children = viewObject.getChildren();
                if (children != null) {
                    for (IRepositoryViewObject child : children) {
                        if (refreshRepositoryContainer(child, type)) {
                            return;
                        }
                    }
                }

            }
        }
    }

    public CommonViewer getCommonViewer() {
        if (commonViewer == null) {
            commonViewer = MDMRepositoryView.show().getCommonViewer();
        }
        return commonViewer;
    }

    private boolean refreshRepositoryContainer(IRepositoryViewObject viewObj, ERepositoryObjectType type) {
        if (viewObj instanceof FolderRepositoryObject) {
            FolderRepositoryObject containerRepositoryObject = (FolderRepositoryObject) viewObj;
            if (containerRepositoryObject.getRepositoryObjectType().equals(type)) {
                commonViewer.refresh(containerRepositoryObject);
                return true;
            }
        }
        return false;
    }

    protected void showRemoveStatus(IStatus status) {
        MultiStatusDialog dialog = new MultiStatusDialog(getShell(), status.getChildren().length
                + Messages.AbstractDeployAction_removeMessage, status);
        dialog.open();
    }

}
