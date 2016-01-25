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
package org.talend.mdm.repository.core.service.wsimpl.servicedoc;



/**
 * created by liusongbo on 2013-9-5
 *
 */
public class WorkflowContextGetDocument extends AbstractGetDocument {

    public WorkflowContextGetDocument(String twoLettersLanguageCode) {
        super(twoLettersLanguageCode);
    }

    @Override
    public String getJNDIName() {
        return "workflowcontext"; //$NON-NLS-1$
    }

    @Override
    public String getDescription() {
        return "This service handle the basic functionality of workflow"; //$NON-NLS-1$
    }

    @Override
    public String getDocument() {
        return "Instantiate a workflow and send it an MDM Context object. \n"//$NON-NLS-1$
                + "\n"//$NON-NLS-1$
                + "\n"//$NON-NLS-1$
                + "The Context objext is a POJO, It will include the following some infos: \n"//$NON-NLS-1$
                + " - Context info (host, port etc) " //$NON-NLS-1$
                + "\n"//$NON-NLS-1$
                + " - Update Report info " //$NON-NLS-1$
                + "\n"//$NON-NLS-1$
                + " - Entity XML (it is a String object) " //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + " - XsdSchema (it is a xsd schema) " //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "Note: you can use the mdm context object in Bonita Groovy Editor "//$NON-NLS-1$
                + "\n"//$NON-NLS-1$
                + " - define a global variable in bonita data, e.g: mdm_context (It is a MDMContext POJO) "//$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + " - using the mdm_context.getHost() to get mdm context host value " //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + " - using the mdm_context.getValue(String xpath) to get element value " //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + " - using the mdm_context.setValue(String xpath, Object value) to set element value " //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "For more detail comment, you can see the MDMContext javadoc. " //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "Notes for Plugin Developers: " + "\n" + "  empty"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
    }

}
