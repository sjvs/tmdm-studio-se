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
public class PartialUpdatePluginDetail extends AbstractPluginDetail {

    /**
     * DOC hbhong ProjectPluginDetail constructor comment.
     * 
     * @param twoLetterLanguageCode
     */
    public PartialUpdatePluginDetail(String twoLetterLanguageCode) {
        super(twoLetterLanguageCode);
    }

    private static final String INPUT_XML = "xml_instance"; //$NON-NLS-1$

    private static final String OUTPUT_PK = "item_primary_key"; //$NON-NLS-1$

    private static final String INPUT_PK = "item_primary_key"; //$NON-NLS-1$

    private static final String INPUT_DATA_MODEL = "data_model"; //$NON-NLS-1$

    private static final String INPUT_CLEAR_MODEL_CACHE = "clear_cache"; //$NON-NLS-1$

    protected String[] getInputVarNames() {
        return new String[] { INPUT_XML, INPUT_PK, INPUT_DATA_MODEL, INPUT_CLEAR_MODEL_CACHE };
    }

    protected String[] getOutputVarNames() {
        return new String[] { OUTPUT_PK };
    }

    public String getDescription() {

        if ("fr".matches(twoLettersLanguageCode.toLowerCase())) //$NON-NLS-1$
            return "Mis à jour partielle d'un aricle existant"; //$NON-NLS-1$
        return "Partial update of an existing item"; //$NON-NLS-1$

    }

    public String getDocumentation() {

        return "The PartialUpdate plugin performs partial updates on an item. " //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "Parameters\n" //$NON-NLS-1$
                + "   pivot [mandatory]: the xPath of sub-elements that must be added or updated on the original item" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "   overwrite [optional]: overwrite an existing item 'true' or 'false'. Default: 'true'. keyXPath must be specified" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "   keyXPath: if supplied, the key Xpath will be applied to all sub-elements. All those for which the result " //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "       equals that of the same xPath applied to the partial update element will be replaced. " //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "       If not supplied, all elements with a local name matching that of the update element will be replaced " //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "   startingPosition [optional]: the position of the added sub-element in the original item parent childs list. " //$NON-NLS-1$
                + "\n" + "       Set to -1 if you want to diable add. Default: add at the the end of the parent's chailds" + "\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + "   dataModel [optional]: the Data Model to use if none is given at runtime" + "\n" //$NON-NLS-1$ //$NON-NLS-2$
                + "   dataCluster [optional]: the Data Cluster to use if none is given at runtime" + "\n" + "\n" + "\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                + "Example" + "\n" + "   <parameters>" + "\n" + "       <pivot>MyRecord/ListOfChildElements/ChildElement</pivot>" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                + "\n" + "       <overwrite>true</overwrite>" + "\n" + "       <keyXPath>./ChildElementKey</keyXPath>" + "\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                + "       <startingPosition>9999999999</startingPosition>" + "\n" //$NON-NLS-1$ //$NON-NLS-2$
                + "       <dataCluster>myDataCluster</dataCluster>" + "\n" + "       <dataModel>myDataModel</dataModel>" + "\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                + "   </parameters>" + "\n" + "\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    }

    public String getParametersSchema() {
        return null;
    }

    public String getJNDIName() {
        return "partialupdate"; //$NON-NLS-1$
    }
}
