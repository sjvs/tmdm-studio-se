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
public class FixedLengthParserPluginDetail extends AbstractPluginDetail {

    /**
     * DOC hbhong ProjectPluginDetail constructor comment.
     * 
     * @param twoLetterLanguageCode
     */
    public FixedLengthParserPluginDetail(String twoLetterLanguageCode) {
        super(twoLetterLanguageCode);
    }

    private final static String INPUT_LINE = "line"; //$NON-NLS-1$

    private final static String OUTPUT_XML = "xml"; //$NON-NLS-1$

    protected String[] getInputVarNames() {
        return new String[] { INPUT_LINE };
    }

    protected String[] getOutputVarNames() {
        return new String[] { OUTPUT_XML };
    }

    public String getDescription() {
        if ("fr".matches(twoLettersLanguageCode.toLowerCase())) //$NON-NLS-1$
            return "Découpe une ligne de texte avec des champs de longueur fixe"; //$NON-NLS-1$
        return "Parses a text line with fixed length fields"; //$NON-NLS-1$
    }

    public String getDocumentation() {
        return "The fixed length parser will parse a fixed length fields text and generate a new text with the extracted values" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "The plugin will replace the extraction sequences {offset,length) specified in the template with the correspondig character sequences in the orginal text" //$NON-NLS-1$
                + "\n" + "The first character of the text starts at position 1 (e.g. offset = 1)" + "\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + "To avoid interpretation of an accolade, backslash it: \\{" + "\n" + "\n" + "\n" + "Parameters\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                + "   template: the new text containing the extraction sequences" + "\n" + "\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + "Example - this will generate an xml starting with element MyXml\n" + "   <parameters>" + "\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + "       <template>" + "\n" + "           <MyXml>" + "\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                + "                   <Field1>{3,10}</Field1> <!-- characters 3 to 12 included of the input text -->" + "\n" //$NON-NLS-1$ //$NON-NLS-2$
                + "                   <Combo>{15,2}--{17,2}</Combo>" + "\n" //$NON-NLS-1$ //$NON-NLS-2$
                + "                   <NotInterpreted>\\{20,1}</NotInterpreted>" + "\n" + "           </MyXml>" + "\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                + "       </template>" + "\n" + "   </parameters>" + "\n" + ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

    }

    public String getParametersSchema() {
        return "<xsd:schema" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "       elementFormDefault='unqualified'" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "       xmlns:xsd='http://www.w3.org/2001/XMLSchema'" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + ">" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "<xsd:element name='parameters'>" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "           <xsd:complexType >" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "               <xsd:sequence>" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "                   <xsd:element minOccurs='1' maxOccurs='1' nillable='false' name='rootElementName' type='xsd:String'/>" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "                   <xsd:element minOccurs='1' maxOccurs='unbounded' nillable='false' name='field' >" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "                       <xsd:complexType >" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "                           <xsd:sequence>" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "                               <xsd:element minOccurs='1' maxOccurs='1' nillable='false' name='xmlName' type='xsd:string'/>" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "                               <xsd:element minOccurs='1' maxOccurs='1' nillable='false' name='length' type='xsd:int'/>" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "                               <xsd:element minOccurs='0' maxOccurs='1' nillable='false' name='trim' type='xsd:boolean'/>" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "                           </xsd:sequence>" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "                       </xsd:complexType>" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "                   </xsd:element>" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "                   <xsd:element minOccurs='0' maxOccurs='1' nillable='false' name='contentType' type='xsd:int'/>" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "                   <xsd:element minOccurs='0' maxOccurs='1' nillable='false' name='charset' type='xsd:int'/>" //$NON-NLS-1$
                + "\n" + "               </xsd:sequence>" + "\n" + "           </xsd:complexType>" + "\n" + "</xsd:element>" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
                + "\n" + "</xsd:schema>"; //$NON-NLS-1$ //$NON-NLS-2$

    }

    public String getJNDIName() {
        return "fixedlengthparser"; //$NON-NLS-1$
    }
}
