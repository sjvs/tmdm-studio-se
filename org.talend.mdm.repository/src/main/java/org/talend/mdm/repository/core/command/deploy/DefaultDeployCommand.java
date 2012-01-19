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
package org.talend.mdm.repository.core.command.deploy;

import java.rmi.RemoteException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.mdm.repository.core.service.DeployService.DeployStatus;
import org.talend.mdm.repository.core.service.IInteractiveHandler;
import org.talend.mdm.repository.core.service.InteractiveService;
import org.talend.mdm.repository.i18n.Messages;

import com.amalto.workbench.utils.XtentisException;

/**
 * DOC hbhong class global comment. Detailled comment
 */
public abstract class DefaultDeployCommand extends AbstractDeployCommand {



    public IStatus execute(Object params, IProgressMonitor monitor) {
        ERepositoryObjectType type = getViewObjectType();
        String objectName = getLabel();
        IInteractiveHandler handler = InteractiveService.findHandler(type);
        if (handler != null) {
            String typeLabel = handler.getLabel();
            monitor.subTask(Messages.Deploy_text + typeLabel + "...");
            try {
                if (handler.deploy(this)) {
                    if (getCommandType() == CMD_MODIFY)
                        return DeployStatus.getOKStatus(this, typeLabel + " \"" + objectName + "\""
                                + " " + Messages.Deploy_successfully_text);
                    return DeployStatus.getOKStatus(this, typeLabel + " \"" + objectName + "\"" + " "
                            + Messages.Create_successfully_text);
                }
                else
                    return DeployStatus.getErrorStatus(this, Messages.Deploy_fail_text + " " + typeLabel + " \"" + objectName);

            } catch (RemoteException e) {
                return DeployStatus.getErrorStatus(this, Messages.Deploy_fail_text + " " + typeLabel + " \"" + objectName + "\","
                        + Messages.Causeis_text + ":" + e.getMessage(), e);
            } catch (XtentisException e) {
                return DeployStatus.getErrorStatus(this, Messages.Deploy_fail_text + " " + typeLabel + " \"" + objectName + "\","
                        + Messages.Causeis_text + ":" + e.getMessage(), e);
            }
        } else {
            return DeployStatus.getErrorStatus(this, Messages.Deploy_notSupport_text + " \"" + objectName + "\"");
        }

    }

    /**
     * DOC hbhong Comment method "getLabel".
     * 
     * @return
     */
    protected String getLabel() {
        return getObjLastName();
    }

}