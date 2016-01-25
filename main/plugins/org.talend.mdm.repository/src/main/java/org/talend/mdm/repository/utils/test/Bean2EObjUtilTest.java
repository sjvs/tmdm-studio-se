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
package org.talend.mdm.repository.utils.test;

//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//
//import org.eclipse.emf.common.util.EList;
//import org.eclipse.emf.ecore.EObject;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.talend.mdm.repository.model.mdmserverobject.MdmserverobjectFactory;
//import org.talend.mdm.repository.model.mdmserverobject.WSMenuE;
//import org.talend.mdm.repository.model.mdmserverobject.WSMenuEntryE;
//import org.talend.mdm.repository.model.mdmserverobject.WSMenuMenuEntriesDescriptionsE;
//import org.talend.mdm.repository.utils.Bean2EObjUtil;
//
//import com.amalto.workbench.webservices.WSMenu;
//import com.amalto.workbench.webservices.WSMenuEntry;
//import com.amalto.workbench.webservices.WSMenuMenuEntriesDescriptions;


/**
 * DOC hbhong  class global comment. Detailled comment
 */
public class Bean2EObjUtilTest {

//    private static Bean2EObjUtil util;
//
//    /**
//     * DOC hbhong Comment method "setUpBeforeClass".
//     * 
//     * @throws java.lang.Exception
//     */
//    @BeforeClass
//    public static void setUpBeforeClass() throws Exception {
//        util = Bean2EObjUtil.getInstance();
//
//    }
//
//    /**
//     * Test method for {@link test.MapUtil#registerClassMap(java.lang.Class)}.
//     */
//    @Test
//    public void testRegisterClassMap() {
//        util.registerClassMap(WSMenu.class);
//    }
//
//    private void initBean(WSMenu menu) {
//        menu.setDescription("this is a description"); //$NON-NLS-1$
//        menu.setName("menuA"); //$NON-NLS-1$
//        //
//        WSMenuEntry[] menuEntries = new WSMenuEntry[2];
//        menuEntries[0] = new WSMenuEntry();
//        menuEntries[0].setApplication("application A"); //$NON-NLS-1$
//        menuEntries[0].setContext("contextA"); //$NON-NLS-1$
//        menuEntries[0].setIcon("icon A"); //$NON-NLS-1$
//        menuEntries[0].setId("id A"); //$NON-NLS-1$
//        //
//        menuEntries[1] = new WSMenuEntry();
//        menuEntries[1].setApplication("application B"); //$NON-NLS-1$
//        menuEntries[1].setContext("contextB"); //$NON-NLS-1$
//        menuEntries[1].setIcon("icon B"); //$NON-NLS-1$
//        menuEntries[1].setId("id B"); //$NON-NLS-1$
//        //
//        WSMenuMenuEntriesDescriptions[] ds = new WSMenuMenuEntriesDescriptions[1];
//        ds[0] = new WSMenuMenuEntriesDescriptions();
//        ds[0].setLabel("label A"); //$NON-NLS-1$
//        ds[0].setLanguage("En"); //$NON-NLS-1$
//        menuEntries[0].setDescriptions(ds);
//        menu.setMenuEntries(menuEntries);
//
//    }
//
//    private void initEObject(WSMenuE menuE) {
//        menuE.setDescription("this is a description"); //$NON-NLS-1$
//        menuE.setName("menuA"); //$NON-NLS-1$
//        menuE.setSystem(false);
//        menuE.getMenuEntries();
//        //
//
//        WSMenuEntryE wsMenuEntry = MdmserverobjectFactory.eINSTANCE.createWSMenuEntryE();
//        wsMenuEntry.setApplication("application A"); //$NON-NLS-1$
//        wsMenuEntry.setContext("contextA"); //$NON-NLS-1$
//        wsMenuEntry.setIcon("icon A"); //$NON-NLS-1$
//        wsMenuEntry.setId("id A"); //$NON-NLS-1$
//        menuE.getMenuEntries().add(wsMenuEntry);
//        //
//        wsMenuEntry = MdmserverobjectFactory.eINSTANCE.createWSMenuEntryE();
//        wsMenuEntry.setApplication("application B"); //$NON-NLS-1$
//        wsMenuEntry.setContext("contextB"); //$NON-NLS-1$
//        wsMenuEntry.setIcon("icon B"); //$NON-NLS-1$
//        // wsMenuEntry.setId("id B");
//        menuE.getMenuEntries().add(wsMenuEntry);
//        //
//        WSMenuMenuEntriesDescriptionsE ds = MdmserverobjectFactory.eINSTANCE.createWSMenuMenuEntriesDescriptionsE();
//        ds.setLabel("Label C"); //$NON-NLS-1$
//        ds.setLanguage("En"); //$NON-NLS-1$
//        wsMenuEntry.getDescriptions().add(ds);
//    }
//
//    /**
//     * Test method for {@link test.MapUtil#convertFromBean2EObj(java.lang.Object)}.
//     */
//    @Test
//    public void testConvertFromBean2EObj() {
//
//        WSMenu menu;
//        menu = new WSMenu();
//        initBean(menu);
//        EObject eObject = util.convertFromBean2EObj(menu, null);
//        assertNotNull(eObject);
//        WSMenuE menuE = (WSMenuE) eObject;
//        //
//        assertEquals(menu.getName(), menuE.getName());
//        assertEquals(menu.getDescription(), menuE.getDescription());
//        //
//        WSMenuEntry[] menuEntries = menu.getMenuEntries();
//        EList<WSMenuEntryE> menuEntriesE = menuE.getMenuEntries();
//        for (int i = 0; i < menuEntries.length; i++) {
//            checkMenuEntry(menuEntries[i], menuEntriesE.get(i));
//        }
//    }
//
//    private void checkMenuEntry(WSMenuEntry entry, WSMenuEntryE entryE) {
//        assertEquals(entry.getApplication(), entryE.getApplication());
//        assertEquals(entry.getContext(), entryE.getContext());
//        assertEquals(entry.getIcon(), entryE.getIcon());
//        assertEquals(entry.getId(), entryE.getId());
//        WSMenuMenuEntriesDescriptions[] ds = entry.getDescriptions();
//        EList<WSMenuMenuEntriesDescriptionsE> des = entryE.getDescriptions();
//        if (ds != null)
//            for (int i = 0; i < ds.length; i++) {
//                checkDesc(ds[i], des.get(i));
//            }
//    }
//
//    private void checkDesc(WSMenuMenuEntriesDescriptions ds, WSMenuMenuEntriesDescriptionsE des) {
//        assertEquals(ds.getLabel(), des.getLabel());
//        assertEquals(ds.getLanguage(), des.getLanguage());
//    }
//
//    @Test
//    public void testConvertFromEObj2Bean() {
//        WSMenuE menuE = MdmserverobjectFactory.eINSTANCE.createWSMenuE();
//        initEObject(menuE);
//        //
//        WSMenu menu = (WSMenu) util.convertFromEObj2Bean(menuE);
//        //
//        assertEquals(menu.getName(), menuE.getName());
//        assertEquals(menu.getDescription(), menuE.getDescription());
//        WSMenuEntry[] menuEntries = menu.getMenuEntries();
//        EList<WSMenuEntryE> menuEntriesE = menuE.getMenuEntries();
//        for (int i = 0; i < menuEntries.length; i++) {
//            checkMenuEntry(menuEntries[i], menuEntriesE.get(i));
//        }
//    }
}
