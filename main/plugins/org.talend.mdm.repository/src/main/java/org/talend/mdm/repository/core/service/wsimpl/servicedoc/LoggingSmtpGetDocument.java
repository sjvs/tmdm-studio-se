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

import org.talend.mdm.repository.i18n.Messages;

/**
 * DOC hbhong class global comment. Detailled comment
 */
public class LoggingSmtpGetDocument extends AbstractGetDocument {

    /**
     * DOC hbhong CopyOfCallJobGetDocument constructor comment.
     * 
     * @param twoLettersLanguageCode
     */
    public LoggingSmtpGetDocument(String twoLettersLanguageCode) {
        super(twoLettersLanguageCode);
    }

    @Override
    public String getJNDIName() {
        return "loggingsmtp"; //$NON-NLS-1$
    }

    @Override
    public String getDescription() {
        return Messages._LoggingSmtpDesc;
    }

    @Override
    public String getDocument() {
        return "This service sends Logging Event emails through the SMTP connector. \n" //$NON-NLS-1$
                + "The diference with the SMTP service is that the Logging SMTP service only logs warnings \n" //$NON-NLS-1$
                + "in case of error, to avoid infnite looping. \n\n" + //$NON-NLS-1$

                "This service creates a default 'Logging Events to Smtp HTML Transformer' transformer \n" //$NON-NLS-1$
                + "when it does not exist. This transformer provdes a default formatting. \n\n" + //$NON-NLS-1$

                "There are two type parameters,\n\n" + "One(Example) :\n" //$NON-NLS-1$ //$NON-NLS-2$
                + "from=b2box@customer.com&to=aiming_chen@hotmail.com&subjectprefix=MDM Logging Event" + "\n\n\n" //$NON-NLS-1$ //$NON-NLS-2$
                + "Two(Example) :\n" + "<parameters>\n" + " <from>b2box@customer.com</from>\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + " <to>aiming_chen@hotmail.com</to>\n" + " <cc></cc>\n" + "    <bcc></bcc>\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + "  <logFileName></logFileName>\n" + " <subjectprefix></subjectprefix>\n" + "  <logfilename></logfilename>\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + "  <transformer></transformer>\n" + "</parameters>\n"; //$NON-NLS-1$ //$NON-NLS-2$
    }

}
