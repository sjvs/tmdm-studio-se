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
public class BatchProjectPluginDetail extends AbstractPluginDetail {

    /**
     * DOC hbhong ProjectPluginDetail constructor comment.
     * 
     * @param twoLettersLanguageCode
     */
    public BatchProjectPluginDetail(String twoLettersLanguageCode) {
        super(twoLettersLanguageCode);
    }

    private static final String INPUT_XML = "xml_instance"; //$NON-NLS-1$

    private static final String INPUT_PATH = "file_path"; //$NON-NLS-1$

    private static final String OUTPUT_XML = "unavailable_content"; //$NON-NLS-1$

    protected String[] getInputVarNames() {
        return new String[] { INPUT_XML, INPUT_PATH };
    }

    protected String[] getOutputVarNames() {
        return new String[] { OUTPUT_XML };
    }

    public String getDescription() {
        String description = ""; //$NON-NLS-1$
        if (twoLettersLanguageCode.toLowerCase().equals("en")) { //$NON-NLS-1$
            description = Messages.BatchProjectXX_BatchToDataManager;
        } else {
            description = Messages.BatchProjectXX_UNSupportedLan;
        }
        return description;
    }

    public String getDocumentation() {
        return "Batch projecting items with the same concept to a specific Data Cluster. \n" + "\n" + "\n" + "Parameters\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                + "   dataClusterName [mandatory]: the Data Cluster to use " + "\n" //$NON-NLS-1$ //$NON-NLS-2$
                + "   dataModelName [mandatory]: the Data Model to use " + "\n" //$NON-NLS-1$ //$NON-NLS-2$
                + "   conceptName [mandatory]: the Concept Model to use " + "\n" //$NON-NLS-1$ //$NON-NLS-2$
                + "   overwrite [optional]: overwrite an existing item 'true' or 'false'. Default: 'true'" + "\n" + "\n" + "\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                + "Example" + "\n" + "   <parameters>" + "\n" + "       <dataClusterName>Order</dataClusterName>" + "\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
                + "       <dataModelName>Order</dataModelName>" + "\n" + "       <conceptName>Customer</conceptName>" + "\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                + "   </parameters>" + "\n" + "\n" + "\n" + "Notes for Plugin Developers: " + "\n" + "       empty"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
    }

    public String getParametersSchema() {
        return "<xsd:schema" //$NON-NLS-1$
                + "       elementFormDefault='unqualified'" //$NON-NLS-1$
                + "       xmlns:xsd='http://www.w3.org/2001/XMLSchema'" //$NON-NLS-1$
                + ">" //$NON-NLS-1$
                + "<xsd:element name='parameters'>" //$NON-NLS-1$
                + "           <xsd:complexType >" //$NON-NLS-1$
                + "               <xsd:sequence>" //$NON-NLS-1$
                + "                   <xsd:element minOccurs='1' maxOccurs='1' nillable='false' name='dataClusterName' type='xsd:string'/>" //$NON-NLS-1$
                + "                   <xsd:element minOccurs='1' maxOccurs='1' nillable='false' name='dataModelName' type='xsd:string'/>" //$NON-NLS-1$
                + "                   <xsd:element minOccurs='1' maxOccurs='1' nillable='false' name='conceptName' type='xsd:string'/>" //$NON-NLS-1$
                + "                   <xsd:element minOccurs='0' maxOccurs='1' nillable='false' name='overwrite' type='xsd:string'/>" //$NON-NLS-1$
                + "               </xsd:sequence>" + "           </xsd:complexType>" + "</xsd:element>" + "</xsd:schema>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }

    public String getJNDIName() {
        return "batchproject"; //$NON-NLS-1$
    }
}
