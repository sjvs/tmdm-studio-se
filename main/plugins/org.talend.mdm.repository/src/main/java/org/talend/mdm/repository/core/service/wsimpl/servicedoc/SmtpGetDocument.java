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
public class SmtpGetDocument extends AbstractGetDocument {

    /**
     * DOC hbhong CopyOfCallJobGetDocument constructor comment.
     * 
     * @param twoLettersLanguageCode
     */
    public SmtpGetDocument(String twoLettersLanguageCode) {
        super(twoLettersLanguageCode);
    }

    @Override
    public String getJNDIName() {
        return "smtp"; //$NON-NLS-1$
    }

    @Override
    public String getDescription() {
        return Messages.SmtpGetDocument_Desc0
                + Messages.SmtpGetDocument_Desc1
                + Messages.SmtpGetDocument_Desc2
                + Messages.SmtpGetDocument_Desc3
                + Messages.SmtpGetDocument_Desc4
                + Messages.SmtpGetDocument_Desc5
                + Messages.SmtpGetDocument_Desc6
                + Messages.SmtpGetDocument_Desc7
                + Messages.SmtpGetDocument_Desc8
                + Messages.SmtpGetDocument_Desc9
                + Messages.SmtpGetDocument_Desc10
                + Messages.SmtpGetDocument_Desc11
                + Messages.SmtpGetDocument_Desc12
                + Messages.SmtpGetDocument_Desc13;
    }

    @Override
    public String getDocument() {
        return "There are two type parameters,\n\n" + "One(Example) :\n" //$NON-NLS-1$ //$NON-NLS-2$
                + "from=***@***.com&to=###@###.com&subjectprefix=MDM Logging Event" + "\n\n\n" + "Two(Example) :\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + "<parameters>\n" + "  <from>***@***.com</from>\n" + " <to>###@###.com</to>\n" + " <cc></cc>\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                + " <bcc></bcc>\n" + "  <subjectprefix></subjectprefix>\n" + "  <logfilename></logfilename>\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + " <process></process>\n" + "</parameters>\n"; //$NON-NLS-1$ //$NON-NLS-2$
    }

}
