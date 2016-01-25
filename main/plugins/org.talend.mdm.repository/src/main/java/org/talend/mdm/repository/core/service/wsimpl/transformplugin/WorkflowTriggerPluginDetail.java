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
package org.talend.mdm.repository.core.service.wsimpl.transformplugin;

import org.talend.mdm.repository.i18n.Messages;


/**
 * DOC hbhong class global comment. Detailled comment
 */
public class WorkflowTriggerPluginDetail extends AbstractPluginDetail {

    /**
     * DOC hbhong ProjectPluginDetail constructor comment.
     * 
     * @param twoLetterLanguageCode
     */
    public WorkflowTriggerPluginDetail(String twoLetterLanguageCode) {
        super(twoLetterLanguageCode);
    }

    private static final String INPUT_UPDATE_REPORT = "update_report"; //$NON-NLS-1$

    private static final String INPUT_PK = "item_primary_key"; //$NON-NLS-1$

    private static final String INPUT_DATA_MODEL = "data_model"; //$NON-NLS-1$

    private static final String OUTPUT_TEXT = "execution_result"; //$NON-NLS-1$

    protected String[] getInputVarNames() {
        return new String[] { INPUT_UPDATE_REPORT, INPUT_PK, INPUT_DATA_MODEL };
    }

    protected String[] getOutputVarNames() {
        return new String[] { OUTPUT_TEXT };
    }

    public String getDescription() {
        String description = ""; //$NON-NLS-1$
        if (twoLettersLanguageCode.toLowerCase().equals("en")) { //$NON-NLS-1$
            description = Messages.WorkflowTriggerXX_PressItem;
        } else {
            description = Messages.BatchProjectXX_UNSupportedLan;
        }
        return description;
    }

    public String getDocumentation() {
        return "Instanciate a workflow and send it an MDM record. \n" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "Parameters\n" //$NON-NLS-1$
                +
                // "    initialContextFactory [mandatory]: the Initial Context Factory "+"\n"+
                // "    providerURL [mandatory]: the provider URL "+"\n"+
                // "    apiType [mandatory]: the API type "+"\n"+
                // "    packageId [mandatory]: the packageId of the process "+"\n"+
                "   processId [mandatory]: the processId of the workflow " //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + " processVersion [mandatory]: the processVersion of the workflow " //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + " useBuiltInVariable [optional]: default value is true (see below)" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                +
                // "    needGoThrough [optional]: default value is false "+"\n"+
                "   defaultDataModel [optional]: default value of the datamodel (if not " //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + " set, workflowtrigger will use the last data-model the record was\n" //$NON-NLS-1$
                + " last created or updated with)\n" //$NON-NLS-1$
                +
                // "    username [mandatory]: the username used to login workflow "+"\n"+
                // "    password [mandatory]: the password used to login workflow "+"\n"+
                "   variable [optional]: define a variable so send in the workflow " //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "     scope: the scope of the variable, 'process' or 'activity' " //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "     activityId [optional]: if the scope is equals to 'activity' " //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "     name: the name of the variable defined in the workflow" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "     type: the type of variable, like 'String','Boolean' etc. " //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "     fromItem: grab the value from the record, 'true' or 'false' " //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "     xpath [optional]: if the fromItem is equals to 'true' " //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "     value [optional]: if the fromItem is equals to 'false' " //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "When useBuiltInVariable is set to 'true', workflowtrigger will also\n" //$NON-NLS-1$
                + "pass the following set of variables in the workflow instance:\n" //$NON-NLS-1$
                + " MDM_host: the host MDM server is running on\n" //$NON-NLS-1$
                + " MDM_port: the port MDM server is listening to\n" //$NON-NLS-1$
                + " MDM_universe: the version to connect to (the old name 'universe' was\n" //$NON-NLS-1$
                + " retained for background compatibility with early versions of Talend MDM)\n" //$NON-NLS-1$
                + " MDM_dataCluster: the data-container to connect to ('data-cluster' = old\n" //$NON-NLS-1$
                + " name for data-container)\n" //$NON-NLS-1$
                + " MDM_dataModel: the data-model to use\n" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "Note: you can define those variables in your workflow with the\n" //$NON-NLS-1$
                + "'MDM_' prefix (uppercase) or 'mdm_' prefix (lowercase), both are supported.\n" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "Example" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + " <parameters>" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                +
                // "        <initialContextFactory>org.jnp.interfaces.NamingContextFactory</initialContextFactory>"
                // +"\n"+
                // "        <providerURL>jnp://localhost:1099</providerURL>" +"\n"+
                // "        <apiType>EJB2</apiType>" +"\n"+
                // "        <packageId>ApprovalWorkflow</packageId>" +"\n"+
                "       <processId>ApprovalWorkflow</processId>" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "     <processVersion>1.0</processVersion>" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                +
                // "        <username>admin</username>" +"\n"+
                // "        <password>talend</password>" +"\n"+
                "       <variable>" + "\n" + "          <scope>process</scope>" + "\n" + "          <name>SampleItemName</name>" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                + "\n" + "         <type>String</type>" + "\n" + "         <fromItem>true</fromItem>" + "\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                + "           <xpath>/Customer/Name</xpath>" + "\n" + "      </variable>" + "\n" + "     <variable>" + "\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
                + "          <scope>activity</scope>" + "\n" + "         <activityId>Approval</activityId>" + "\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                + "           <name>isGranted</name>" + "\n" + "          <type>Boolean</type>" + "\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                + "          <fromItem>false</fromItem>" + "\n" + "          <value>true</value>" + "\n" + "     </variable>" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                + "\n" + " </parameters>" + "\n" + "\n" + "\n" + "Notes for Plugin Developers: " + "\n" + "        empty"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
    }

    public String getParametersSchema() {
        return null;
    }

    public String getJNDIName() {
        return "workflowtrigger"; //$NON-NLS-1$
    }
}
