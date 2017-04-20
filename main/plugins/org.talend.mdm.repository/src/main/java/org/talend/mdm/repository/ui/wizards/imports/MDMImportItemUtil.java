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
package org.talend.mdm.repository.ui.wizards.imports;

import java.io.File;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;
import org.eclipse.ui.internal.wizards.datatransfer.TarFile;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.mdm.commmon.util.workbench.ZipToFile;
import org.talend.mdm.repository.utils.IOUtil;
import org.talend.mdm.repository.utils.IOUtilExt;


/**
 * created by liusognbo on Mar 10, 2015
 */
public class MDMImportItemUtil {

    private static Logger log = Logger.getLogger(MDMImportItemUtil.class);

    public static File buildUnzippedTempFile(Object fileorDirectory) {
        File tempFile = null;
        try {
            if (fileorDirectory instanceof File) {
                String targetFolder = IOUtil.getTempFolder().getAbsolutePath();
                FilesUtils.copyDirectory((File) fileorDirectory, new File(targetFolder));

                tempFile = new File(new File(targetFolder), ((File) fileorDirectory).getName());
            } else if (fileorDirectory instanceof ZipFile) {
                tempFile = upZipFile((ZipFile) fileorDirectory);
            } else if (fileorDirectory instanceof TarFile) {
                tempFile = upzipFile((TarFile) fileorDirectory);
            }

            return tempFile;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    private static File upZipFile(ZipFile zfile) {
        String targetFolder = IOUtil.getTempFolder().getAbsolutePath();
        try {
            ZipToFile.unZipFile(zfile.getName(), targetFolder);

            File file = new File(targetFolder);
            return file.listFiles()[0];
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    @SuppressWarnings("restriction")
    private static File upzipFile(TarFile tarFile) {

        String targetFolder = IOUtil.getTempFolder().getAbsolutePath();
        try {
            IOUtilExt.extractTarFile(tarFile.getName(), targetFolder);
            File file = new File(targetFolder);

            return file.listFiles()[0];
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }
}
