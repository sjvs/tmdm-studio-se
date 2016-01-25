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
package org.talend.mdm.repository.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

public class IOUtilTest {


    /**
     * Test method for {@link org.talend.mdm.repository.utils.IOUtil#getTempFolder()}
     * 
     */
    @Test
    public void testGetTempFolder() {
        File theFile = IOUtil.getTempFolder();
        assertNotNull(theFile);
        assertTrue(theFile.exists());
    }

    /**
     * Test method for {@link org.talend.mdm.repository.utils.IOUtil#cleanFolder(java.io.File folder)}
     * 
     */
    @Test
    public void testCleanFolder() {
        File file = IOUtil.getTempFolder();
        String tempFolderPath = file.getAbsolutePath();
        File newfile = new File(tempFolderPath + File.separator + "testtemp"); //$NON-NLS-1$
        if (!newfile.exists()) {
            newfile.mkdirs();
        }
        IOUtil.cleanFolder(newfile);
        assertFalse(newfile.exists());
    }

}
