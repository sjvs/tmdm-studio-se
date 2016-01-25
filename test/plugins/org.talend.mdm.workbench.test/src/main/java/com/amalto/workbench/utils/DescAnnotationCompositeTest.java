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
package com.amalto.workbench.utils;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.amalto.workbench.widgets.DescAnnotationComposite;

/**
 * DOC Administrator  class global comment. Detailled comment
 */
public class DescAnnotationCompositeTest {

    /**
     * Test method for {@link com.amalto.workbench.widgets.DescAnnotationComposite#escapeMultiLanguageString(java.util.Map)}.
     */
    @Test
    public void testEscapeMultiLanguageString() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("en", "English view description");
        map.put("fr", "French view description");
        String ret = DescAnnotationComposite.escapeMultiLanguageString(map);
        assertTrue(ret.contains("EN"));
        assertTrue(ret.contains("FR"));

        map.clear();
        map.put("eN", "English view description");
        map.put("Fr", "French view description");
        ret = DescAnnotationComposite.escapeMultiLanguageString(map);
        assertTrue(ret.contains("EN"));
        assertTrue(ret.contains("FR"));

        map.clear();
        map.put("Ab", "English view description");
        map.put("zh", "French view description");
        ret = DescAnnotationComposite.escapeMultiLanguageString(map);
        assertTrue(ret.contains("AB"));
        assertTrue(ret.contains("ZH"));

    }

}
