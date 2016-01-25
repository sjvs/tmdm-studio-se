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
public interface JSSEConstants {

    public enum VERIFY_TYPE {
        ALLOW_ALL,
        STRICT
    }

    // http://docs.oracle.com/javase/6/docs/technotes/guides/security/StandardNames.html#CertificateFactory
    public enum CERTIFICATE_FACTORY_TYPE {
        X509;
    }

    // http://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyStore
    public enum KEYSTORE_TYPE {
        JKS,
        JCEKS,
        PKCS12;
    }

    // http://docs.oracle.com/javase/6/docs/technotes/guides/security/StandardNames.html#SSLContext
    public enum SSL_Algorithm {
        TLS,
        SSL;
    }

}
