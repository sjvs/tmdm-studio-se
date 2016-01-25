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
package com.amalto.workbench.detailtabs.sections.model.annotationinfo.relationship;

import org.eclipse.xsd.XSDComponent;

import com.amalto.workbench.detailtabs.sections.handlers.CommitHandler;
import com.amalto.workbench.detailtabs.sections.handlers.ElementForeignKeyInfosCommitHandler;
import com.amalto.workbench.detailtabs.sections.model.annotationinfo.ListContentsAnnotationInfo;

public class ForeignKeyInfosAnnoInfo extends ListContentsAnnotationInfo {

    private boolean isResolveAutoInWeb;

    public ForeignKeyInfosAnnoInfo(XSDComponent sourceComponent, String[] infos, boolean isResolveAutoInWeb) {
        super(sourceComponent, infos);

        this.isResolveAutoInWeb = isResolveAutoInWeb;
    }

    public CommitHandler<ForeignKeyInfosAnnoInfo> createCommitHandler() {
        return new ElementForeignKeyInfosCommitHandler(this);
    }

    public boolean isResolveAutoInWeb() {
        return isResolveAutoInWeb;
    }
}
