// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.mdm.studio.test.datamodel.properties;

import junit.framework.Assert;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.ui.IPageLayout;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.talend.mdm.studio.test.TalendSWTBotForMDM;
import org.talend.mdm.studio.test.util.Util;

import com.amalto.workbench.editors.DataModelMainPage;
import com.amalto.workbench.editors.xsdeditor.XSDEditor;

/**
 * 
 * 
 * DOC rhou class global comment. Detailled comment
 */
@RunWith(SWTBotJunit4ClassRunner.class)
public class DataModelTypesElementPresentationTabTest extends
		TalendSWTBotForMDM {

	private SWTBotTree typesBotTree;

	private DataModelMainPage mainpage;

	private SWTBotTreeItem dataModelItem;

	private SWTBotTreeItem typeNode;

	private SWTBotTreeItem elementNode;

	@Before
	public void runBeforeEveryTest() {
		dataModelItem = serverItem.getNode("Data Model [HEAD]");
		dataModelItem.expand();

		dataModelItem.contextMenu("New").click();
		SWTBotShell newDataContainerShell = bot.shell("New Data Model");
		newDataContainerShell.activate();
		SWTBotText text = bot
				.textWithLabel("Enter a name for the New Instance");
		text.setText("TestDataModel");
		sleep();
		bot.buttonWithTooltip("Add").click();
		sleep();
		bot.button("OK").click();
		sleep();
		Assert.assertNotNull(dataModelItem.getNode("TestDataModel"));
		sleep(4);

		final SWTBotEditor editor = bot.editorByTitle("TestDataModel");
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				XSDEditor ep = (XSDEditor) editor.getReference().getPart(true);
				mainpage = (DataModelMainPage) ep.getSelectedPage();
			}
		});
		Tree typesTree = mainpage.getTypesViewer().getTree();
		typesBotTree = new SWTBotTree(typesTree);

		addComplexType();
		bot.viewById(IPageLayout.ID_PROP_SHEET).setFocus();
		Util.selecteTalendTabbedPropertyListAtIndex(bot, 1);
	}

	@After
	public void runAfterEveryTest() {
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				mainpage.doSave(new NullProgressMonitor());
				bot.activeEditor().close();
			}
		});
		dataModelItem.getNode("TestDataModel").contextMenu("Delete").click();
		bot.button("OK").click();
	}

	private void addComplexType() {
		typesBotTree.contextMenu("Create a Complex Type").click();
		sleep();
		SWTBotShell changeTypeShell = bot.shell("Complex Type Properties");
		changeTypeShell.activate();
		bot.ccomboBox(0).setText("TestComplexType");
		bot.radio("Sequence").click();
		bot.button("OK").click();
		sleep(2);

		typeNode = typesBotTree.getTreeItem("TestComplexType");
		typeNode.select();
		bot.toolbarButtonWithTooltip("Expand...", 1).click();
		elementNode = typeNode.getNode("subelement").select();
	}

	@Test
	public void setDisplayFormatTest() {
		bot.comboBox(0).setSelection(0);
		bot.text(0).setText("test error format in English");
		bot.buttonWithTooltip("Add", 0).click();
		sleep();
		bot.comboBox(0).setSelection(1);
		bot.text(0).setText("test error format in French");
		bot.buttonWithTooltip("Add", 0).click();
		sleep();
		bot.tree(0).select(1);
		bot.buttonWithTooltip("Del").click();
		bot.button("Apply").click();
	}

	@Test
	public void setLabelsTest() {
		bot.comboBox(1).setSelection(0);
		bot.text(1).setText("en");
		bot.buttonWithTooltip("Add", 1).click();
		sleep();
		bot.comboBox(1).setSelection(1);
		bot.text(1).setText("fr");
		bot.buttonWithTooltip("Add", 1).click();
		sleep();
		bot.tree(1).select(1);
		bot.buttonWithTooltip("Del").click();
		bot.button("Apply").click();
		Assert.assertNotNull(elementNode.expand().getNode("Annotations")
				.expand().getNode("English Label: en"));
	}

	@Test
	public void setDescriptionsTest() {
		bot.comboBox(2).setSelection(0);
		bot.text(2).setText("english description");
		bot.buttonWithTooltip("Add", 2).click();
		sleep();
		bot.comboBox(2).setSelection(1);
		bot.text(2).setText("french description");
		bot.buttonWithTooltip("Add", 2).click();
		sleep();
		bot.tree(2).select(1);
		bot.buttonWithTooltip("Del").click();
		bot.button("Apply").click();
		Assert.assertNotNull(elementNode.expand().getNode("Annotations")
				.expand().getNode("English Description: enlish description"));
	}

	@Test
	public void setFacetTest() {
		bot.comboBox(3).setSelection(0);
		bot.text(3).setText("test error facet in English");
		bot.buttonWithTooltip("Add", 3).click();
		sleep();
		bot.comboBox(3).setSelection(1);
		bot.text(3).setText("test error facet in French");
		bot.buttonWithTooltip("Add", 3).click();
		sleep();
		bot.tree(3).select(1);
		bot.buttonWithTooltip("Del").click();
		sleep();
		bot.button("Apply").click();
		sleep();
		// Assert.assertNotNull(elementNode.expand().getNode("Annotations")
		// .expand().getNode("Facet_Msg_EN: test error facet in English"));
	}

}