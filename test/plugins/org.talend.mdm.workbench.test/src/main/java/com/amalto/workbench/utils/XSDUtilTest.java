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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.eclipse.xsd.XSDComplexTypeContent;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDParticleContent;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTerm;
import org.eclipse.xsd.XSDTypeDefinition;
import org.junit.Test;

/**
 * DOC hbhong  class global comment. Detailled comment
 */
public class XSDUtilTest {

    final String X_FOREIGN_KEY = "X_ForeignKey"; //$NON-NLS-1$
    @Test
    public void testGetAnnotationValue() throws Exception {
        String fileName = "Product_0.1.xsd"; //$NON-NLS-1$
        String elementName = "Family"; //$NON-NLS-1$
        String fk = "Store/Id"; //$NON-NLS-1$
        String xsdString = TestUtil.readTestResource(XSDUtilTest.this.getClass(), fileName);
        assertNotNull(xsdString);
        XSDSchema xsdSchema = Util.getXSDSchema(xsdString);
        for (XSDElementDeclaration element : xsdSchema.getElementDeclarations()) {
            XSDTypeDefinition typeDefinition = element.getTypeDefinition();

            if (typeDefinition instanceof XSDComplexTypeDefinition) {
                XSDComplexTypeContent xsdComplexTypeContent = ((XSDComplexTypeDefinition) typeDefinition).getContent();
                if (xsdComplexTypeContent instanceof XSDParticle) {
                    XSDParticleContent content = ((XSDParticle) xsdComplexTypeContent).getContent();
                    if (content instanceof XSDModelGroup) {
                        for (XSDParticle particle : ((XSDModelGroup) content).getParticles()) {
                            XSDTerm term = particle.getTerm();
                            if (term instanceof XSDElementDeclaration) {
                                XSDElementDeclaration elementDeclaration = (XSDElementDeclaration) term;
                                if (elementDeclaration.getName().equals(elementName)) {
                                    String value = XSDUtil.getAnnotationValue(elementDeclaration, X_FOREIGN_KEY);
                                    assertEquals(fk, value);
                                    return;
                                }
                            }
                        }
                    }

                }
            }
        }
        fail();
    }
}
