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
package org.talend.mdm.repository.ui.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.talend.mdm.repository.core.AbstractRepositoryAction;
import org.talend.mdm.repository.i18n.Messages;
import org.talend.mdm.repository.ui.navigator.MDMRepositoryView;
import org.talend.mdm.repository.ui.wizards.exports.MDMExportRepositoryItemsWizard;

import com.amalto.workbench.image.EImage;
import com.amalto.workbench.image.ImageCache;

/**
 * DOC hbhong class global comment. Detailled comment <br/>
 * 
 */
public class ExportObjectAction extends AbstractRepositoryAction {

    private static Log log = LogFactory.getLog(ExportObjectAction.class);

    private MDMRepositoryView view = null;

    /**
     * DOC hbhong AddMenu constructor comment.
     * 
     * @param text
     */
    public ExportObjectAction() {
        super(Messages.ExportObjectAction_label);
        setImageDescriptor(ImageCache.getImage(EImage.EXPORT.getPath()));
    }

    @Override
    protected void doRun() {
        try {
            ISelection selection = null;
            // if (this.view != null) { // called from ServerView
            view = MDMRepositoryView.show();
            selection = view.getCommonViewer().getSelection();
            // }
            MDMExportRepositoryItemsWizard wizard = new MDMExportRepositoryItemsWizard((IStructuredSelection) selection);
            WizardDialog dialog = new WizardDialog(view.getSite().getShell(), wizard);
            dialog.create();
            dialog.getShell().setText(Messages.ExportObjectAction_exportRepositoryItems);
            dialog.open();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            MessageDialog.openError(view.getSite().getShell(), Messages.ExportObjectAction_error,
                    Messages.ExportObjectAction_hasError + e.getLocalizedMessage());
        }
    }

    @Override
    public String getGroupName() {
        return GROUP_EXPORT;
    }

}
