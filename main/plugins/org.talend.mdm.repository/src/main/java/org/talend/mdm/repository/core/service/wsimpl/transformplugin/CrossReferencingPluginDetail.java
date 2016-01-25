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
public class CrossReferencingPluginDetail extends AbstractPluginDetail {

    /**
     * DOC hbhong ProjectPluginDetail constructor comment.
     * 
     * @param twoLetterLanguageCode
     */
    public CrossReferencingPluginDetail(String twoLetterLanguageCode) {
        super(twoLetterLanguageCode);
    }

    private static final String INPUT_XML = "xml"; //$NON-NLS-1$

    private static final String OUTPUT_TEXT = "text"; //$NON-NLS-1$

    protected String[] getInputVarNames() {
        return new String[] { INPUT_XML };
    }

    protected String[] getOutputVarNames() {
        return new String[] { OUTPUT_TEXT };
    }

    public String getDescription() {
        if ("fr".matches(twoLettersLanguageCode.toLowerCase())) //$NON-NLS-1$
            return "Transforme un XML en utilisant des règles de transcodifications"; //$NON-NLS-1$
        return "Transform an XML using crossreferencing rules"; //$NON-NLS-1$
    }

    public String getDocumentation() {
        return "The CrossReferencing plugin transform xml content using cross referencing tables. " + "\n" + "\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + "Parameters\n" + "   An xml file describing all cross referencing parameters" + "\n" + "\n" + "\n" + "Example" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
                + "\n" + "   <parameters>" + "\n" + "       <CrossRef>" + "\n" + "           <xrefName>myCrossRefName</xrefName>" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
                + "\n" + "           <xrefCluster>b2box CROSSREFERENCING</xrefCluster>" + "\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + "           <xrefRootElement>//PurchaseOrderLineItem</xrefRootElement>" + "\n" + "           <xrefIn>\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + "               <mapping>\n" + "                   <xrefElement>Quantity/Value/@UOM</xrefElement>\n" //$NON-NLS-1$ //$NON-NLS-2$
                + "                   <xrefPath>UnitOfMeasure/cXMLUnit</xrefPath>\n" + "               </mapping>\n" //$NON-NLS-1$ //$NON-NLS-2$
                + "           </xrefIn>" + "\n" + "           <xrefOut>\n" + "               <mapping>\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                + "                   <xrefElement>Quantity/Value/@UOM</xrefElement>\n" //$NON-NLS-1$
                + "                   <xrefPath>UnitOfMeasure/XBITSUnit</xrefPath>\n" + "               </mapping>\n" //$NON-NLS-1$ //$NON-NLS-2$
                + "           </xrefOut>" + "\n" + "       </CrossRef>\n" + "   </parameters>" + "\n" + "\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
    }

    public String getParametersSchema() {
        return null;
    }

    public String getJNDIName() {
        return "crossreferencing"; //$NON-NLS-1$
    }
}
