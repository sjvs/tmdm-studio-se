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


/**
 * DOC hbhong class global comment. Detailled comment
 */
public class ProjectPluginDetail extends AbstractPluginDetail {

    /**
     * DOC hbhong ProjectPluginDetail constructor comment.
     * 
     * @param twoLetterLanguageCode
     */
    public ProjectPluginDetail(String twoLetterLanguageCode) {
        super(twoLetterLanguageCode);
    }

    private static final String INPUT_XML = "xml_instance"; //$NON-NLS-1$

    private static final String OUTPUT_PK = "item_primary_key"; //$NON-NLS-1$

    private static final String INPUT_PK = "item_primary_key"; //$NON-NLS-1$

    private static final String INPUT_DATA_MODEL = "data_model"; //$NON-NLS-1$

    private static final String INPUT_CLEAR_MODEL_CACHE = "clear_cache"; //$NON-NLS-1$


    @Override
    protected String[] getInputVarNames() {
        return new String[] { INPUT_XML, INPUT_PK, INPUT_DATA_MODEL, INPUT_CLEAR_MODEL_CACHE };
    }

    @Override
    protected String[] getOutputVarNames() {
        return new String[] { OUTPUT_PK };
    }

    @Override
    public String getDescription() {
        if ("fr".matches(twoLettersLanguageCode.toLowerCase())) //$NON-NLS-1$
            return "Projette un item vers le Gestionnaire de Données"; //$NON-NLS-1$
        return "Projects and item to the Data Manager"; //$NON-NLS-1$
    }

    @Override
    public String getDocumentation() {
        return "The Project plugin projects content to the Data Manager. " //$NON-NLS-1$
                + "If no Item Primary Key is passed as a variable in the pipeline,\n" //$NON-NLS-1$
                + "the default Data Model and default Data Cluster to used must be specified in the parameters\n" + "\n" + "\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + "Parameters\n" + "   defaultDataModel [optional]: the Data Model to use if none is given at runtime" + "\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + "   defaultDataCluster [optional]: the Data Cluster to use if none is given at runtime" + "\n" //$NON-NLS-1$ //$NON-NLS-2$
                + "   overwrite [optional]: overwrite an existing item 'true' or 'false'. Default: 'true'" + "\n" + "\n" + "\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                + "Example" + "\n" + "   <parameters>" + "\n" + "       <defaultDataCluster>myDataCluster</defaultDataCluster>" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                + "\n" + "       <defaultDataModel>myDataModel</defaultDataModel>" + "\n" + "       <overwrite>true</overwrite>" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                + "\n" + "   </parameters>" + "\n" + "\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }

    @Override
    public String getParametersSchema() {
        return null;
    }

    public String getJNDIName() {
        return "project"; //$NON-NLS-1$
    }
}
