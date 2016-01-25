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
package com.amalto.workbench.preferences;

/**
 * created by changguopiao on 2013-7-10 Detailled comment
 * 
 */
public interface PreferenceConstants {

    // aligorithm of create sslContext
    String SSL_Algorithm = "sslAlgorithm"; //$NON-NLS-1$

    // File of the keystore
    String KEYSTORE_FILE = "keystoreFile"; //$NON-NLS-1$

    // Password to unlock the keystore.
    String KEYSTORE_PASSWORD = "keystorePassword"; //$NON-NLS-1$

    // Type of file used for the keystore.
    String KEYSTORE_TYPE = "keystoreType"; //$NON-NLS-1$

    // File of the truststore
    String TRUSTSTORE_FILE = "truststoreFile"; //$NON-NLS-1$

    // Password to unlock the truststore.
    String TRUSTSTORE_PASSWORD = "truststorePassword"; //$NON-NLS-1$

    // Type of file used for the truststore.
    String TRUSTSTORE_TYPE = "truststoreType"; //$NON-NLS-1$

    // verifyHostname:
    String VERIFY_HOSTNAME = "verifyHostname"; //$NON-NLS-1$
}
