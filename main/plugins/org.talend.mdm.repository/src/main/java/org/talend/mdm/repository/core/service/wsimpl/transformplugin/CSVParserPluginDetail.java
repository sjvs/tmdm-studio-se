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
public class CSVParserPluginDetail extends AbstractPluginDetail {

    /**
     * DOC hbhong ProjectPluginDetail constructor comment.
     * 
     * @param twoLetterLanguageCode
     */
    public CSVParserPluginDetail(String twoLetterLanguageCode) {
        super(twoLetterLanguageCode);
    }

    private static final String INPUT_CSV_LINE = "csv_line"; //$NON-NLS-1$

    private static final String OUTPUT_XML = "xml"; //$NON-NLS-1$

    protected String[] getInputVarNames() {
        return new String[] { INPUT_CSV_LINE };
    }

    protected String[] getOutputVarNames() {
        return new String[] { OUTPUT_XML };
    }

    public String getDescription() {
        if ("fr".matches(twoLettersLanguageCode.toLowerCase())) //$NON-NLS-1$
            return "Découpe une ligne de texte avec des champs séparés par un séparateur"; //$NON-NLS-1$
        return "Parses a text line with fields separated with a separator"; //$NON-NLS-1$
    }

    public String getDocumentation() {
        return "The CSV parser parses a line of fields values (or a set of lines) and it generates an xml with the extracted values." //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "            2/ a set of line and generate an xml with the extracted values." //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "The CSV parser is typically invoked right after a step calling the lineparser plugin." //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "The plugin will replace \n" //$NON-NLS-1$
                + "- the {number} sequences  in the template with the value of the corresponding column in the line. " //$NON-NLS-1$
                + " The first column of the line has number 1" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "- the {/'regex'} sequences  in the template with the value of the column which HEADER name matches 'regex'" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "Column header names must be specified\n" //$NON-NLS-1$
                + "- either as a comma separated list in a <headers>,header1,header2,...</headers> element \n" //$NON-NLS-1$
                + "- or must be present on the first line of the CSV in which case a section <headersOnFirstLine>true</headersOnFirstLine> " //$NON-NLS-1$
                + " should be specified in the parameters\n" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "To avoid interpretation of an accolade, backslash it: \\{" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "The CSV parser can also loop over a sequence of columns using structure [LOOP regex] template [/LOOP]" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "The loop will be triggered every time a header matching 'regex' is found and the 'template' will be executed" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "An error will be triggered if not matching header is found, unless 'failOnMissingHeaders' is set to 'false'" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "Parameters\n" //$NON-NLS-1$
                + "   separator: the separator used between fields\n" //$NON-NLS-1$
                + "   lineseparator: the line separator used at the end of each line\n" //$NON-NLS-1$
                + "   multiplelines: multiple lines (true or false)\n" //$NON-NLS-1$
                + "   template: the new text containing the extraction sequences\n" //$NON-NLS-1$
                + "   headers: [OPTIONAL defaults to nil]. A separated list of column names\n" //$NON-NLS-1$
                + "   headersOnFirstLine: [OPTIONAL defaults to false]. If set to true, will pick up column names from the CSV first line\n" //$NON-NLS-1$
                + "   failOnMissingHeaders: [OPTIONAL defaults to true]. If set to false, no error will be triggered when trying to match a header name\n" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "Example - this will generate an xml starting with element MyXml\n" //$NON-NLS-1$
                + "   <parameters>" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "       <separator>,</separator>  <!-- the fields are separated by a comma -->" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "       <headersOnFirstLine>true</headersOnFirstLine>  <!-- the header names are on the first line of the CSV -->" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "       <template>" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "       <![CDATA[" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "           <MyXml>" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "                   <Field1>{1}</Field1> <!-- first field -->" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "                   <Combo>{2}-{4}</Combo> <!-- values of fields 2 and 4 separated by a dash -->" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "                   <NotInterpreted>\\{3}</NotInterpreted>  <!-- the value will remain {3}" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "                   <HeaderReference>{/Header1}</HeaderReference>  <!-- the value in column named 'Header1'" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "                   [LOOP lineNumber.*]   <!--starts a loop triggered by headers matching the regular expression lineNumber.*" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "                   <Line>                           <!--inside the loop - multiple Line elements may be generated -->" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "                       <Quantity>{/Quantity.*}</Quantity>  <!-- the value in first column matching 'Quantity.*' " //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "                                                                       after the one matching 'lineNumber.*'-->" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "                   </Line>" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "                   [/LOOP] <!-- End Of Loop -->" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "           </MyXml>" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "       ]]>" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "       </template>" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + "   </parameters>" //$NON-NLS-1$
                + "\n" //$NON-NLS-1$
                + ""; //$NON-NLS-1$

    }

    public String getParametersSchema() {
        return null;
    }

    public String getJNDIName() {
        return "csvparser"; //$NON-NLS-1$
    }
}
