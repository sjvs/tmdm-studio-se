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

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.util.URI;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.IRepositoryViewObject;

/**
 * DOC hbhong class global comment. Detailled comment
 */
public class WorkflowEditorInput extends URIEditorInput implements IRepositoryViewEditorInput {

    public static String EDITOR_ID = "org.bonitasoft.studio.model.process.diagram.part.ProcessDiagramEditorID"; //$NON-NLS-1$

    private final IRepositoryViewObject viewObject;

    private String version;

    private final IFile file;

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * DOC hbhong WorkflowEditorInput constructor comment.
     * 
     * @param file
     */
    public WorkflowEditorInput(IRepositoryViewObject viewObject, IFile file) {
        super(URI.createPlatformResourceURI(file.getFullPath().toString(), true));
        this.viewObject = viewObject;
        this.file = file;
        version = viewObject.getVersion();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.mdm.repository.ui.editors.IRepositoryViewEditorInput#getEditorId()
     */
    public String getEditorId() {
        return EDITOR_ID;
    }

    public Item getInputItem() {
        return viewObject.getProperty().getItem();
    }

    public IRepositoryViewObject getViewObject() {
        return viewObject;
    }

    private boolean readOnly;

    public boolean isReadOnly() {
        return this.readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        if (file != null) {
            file.setReadOnly(readOnly);
        }
    }

    @Override
    public String getName() {
        if (getVersion() != null) {
            return viewObject.getLabel() + "_" + getVersion() + ".proc"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        return super.getName();
    }

    public IFile getFile() {
        return this.file;
    }
}
