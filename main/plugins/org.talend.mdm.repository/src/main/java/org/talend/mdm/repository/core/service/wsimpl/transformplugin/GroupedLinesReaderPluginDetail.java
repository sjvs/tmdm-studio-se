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
public class GroupedLinesReaderPluginDetail extends AbstractPluginDetail {

    /**
     * DOC hbhong ProjectPluginDetail constructor comment.
     * 
     * @param twoLetterLanguageCode
     */
    public GroupedLinesReaderPluginDetail(String twoLetterLanguageCode) {
        super(twoLetterLanguageCode);
    }

    private static final String INPUT_CONTENT = "text_content"; //$NON-NLS-1$

    private static final String OUTPUT_LINE = "line"; //$NON-NLS-1$


    protected String[] getInputVarNames() {
        return new String[] { INPUT_CONTENT };
    }

    protected String[] getOutputVarNames() {
        return new String[] { OUTPUT_LINE };
    }

    public String getDescription() {
        if ("fr".matches(twoLettersLanguageCode.toLowerCase())) //$NON-NLS-1$
            return "Lit un texte, regroupe certaines lignes"; //$NON-NLS-1$
        return "Reads a text, grouping several lines"; //$NON-NLS-1$
    }

    public String getDocumentation() {
        return "The Document Extractor plugin reads a logical document which can be put in several lines. " //$NON-NLS-1$
                + "Each extracted document is sent after each other for further processing by subsequent plugins\n" + "\n" + "\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + "Parameters\n" //$NON-NLS-1$
                + "   ignoreFirstLines [optional]: will ignore the number of lines specified at the beginning of the text" + "\n" //$NON-NLS-1$ //$NON-NLS-2$
                + "   ignoreBlankLine [optional]: will ignore the blank lines: true or false (default is true)" + "\n" + "\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + "\n" + "Example" + "\n" + "         <parameters>" + "\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                + "                 <ignoreFirstLines>0</ignoreFirstLines>" + "\n" + "                 <type>CSV</type>" + "\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                + "                 <position>3</position>" + "\n" + "                 <separator>,</separator>" + "\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                + "         </parameters>" + "\n" + "\n" + "\n" + "Notes for Plugin Developers: " + "\n" + "   Context:" + "\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
                + "       com.amalto.core.plugin.Grouped Lines Reader.numlines: the number of lines processed"; //$NON-NLS-1$

    }

    public String getParametersSchema() {
        return "<xsd:schema" //$NON-NLS-1$
                + "       elementFormDefault='unqualified'" //$NON-NLS-1$
                + "       xmlns:xsd='http://www.w3.org/2001/XMLSchema'" //$NON-NLS-1$
                + ">" //$NON-NLS-1$
                + "<xsd:element name='parameters'>" //$NON-NLS-1$
                + "           <xsd:complexType >" //$NON-NLS-1$
                + "               <xsd:sequence>" //$NON-NLS-1$
                + "                   <xsd:element minOccurs='0' maxOccurs='1' nillable='false' name='contentType' type='xsd:int'/>" //$NON-NLS-1$
                + "                   <xsd:element minOccurs='0' maxOccurs='1' nillable='false' name='charset' type='xsd:int'/>" //$NON-NLS-1$
                + "                   <xsd:element minOccurs='0' maxOccurs='1' nillable='false' name='ignoreFirstLines' type='xsd:int'/>" //$NON-NLS-1$
                + "               </xsd:sequence>" + "           </xsd:complexType>" + "</xsd:element>" + "</xsd:schema>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }

    public String getJNDIName() {
        return "groupedlinesreader"; //$NON-NLS-1$
    }
}
