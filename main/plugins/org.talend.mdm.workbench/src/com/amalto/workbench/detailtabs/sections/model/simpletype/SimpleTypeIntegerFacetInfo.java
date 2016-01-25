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
package com.amalto.workbench.detailtabs.sections.model.simpletype;

public class SimpleTypeIntegerFacetInfo extends SimpleTypeFacetInfo<Integer> {

    public SimpleTypeIntegerFacetInfo(String propName, Integer propValue) {
        super(propName, propValue);
    }

    @Override
    protected String getValueLabel(Integer value) {
        return value.toString();
    }

    @Override
    protected Integer parseLabel(String label) {

        int result = 0;

        try {
            result = Integer.parseInt(label);
        } catch (Exception e) {
            return this.propValue;
        }

        return result;
    }

}
