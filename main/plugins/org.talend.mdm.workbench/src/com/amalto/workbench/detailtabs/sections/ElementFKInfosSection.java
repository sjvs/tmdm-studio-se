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
package com.amalto.workbench.detailtabs.sections;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.xsd.XSDAnnotation;
import org.eclipse.xsd.XSDComponent;
import org.eclipse.xsd.XSDConcreteComponent;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDNamedComponent;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTerm;

import com.amalto.workbench.detailtabs.sections.model.ISubmittable;
import com.amalto.workbench.detailtabs.sections.model.annotationinfo.relationship.ForeignKeyInfosAnnoInfo;
import com.amalto.workbench.detailtabs.sections.util.FixDMNameBasePropertySectionDataModelExtractor;
import com.amalto.workbench.i18n.Messages;
import com.amalto.workbench.models.infoextractor.IAllDataModelHolder;
import com.amalto.workbench.utils.Util;
import com.amalto.workbench.utils.XSDAnnotationsStructure;
import com.amalto.workbench.widgets.composites.XpathComposite;

public class ElementFKInfosSection extends XpathSection {

	private List<String> fkInfos = new ArrayList<String>();

	private boolean isResolveAutoInWeb;

	private XpathComposite composite;

	private FixDMNameBasePropertySectionDataModelExtractor holder;

	private Button chkResolveAutoInWeb;

	Object[] getXSDSchemaChildren(XSDSchema schema) {
		List<XSDElementDeclaration> declarations = new ArrayList<XSDElementDeclaration>();

		EList<XSDElementDeclaration> elementDeclarations = schema
				.getElementDeclarations();
		for (XSDElementDeclaration declaration : elementDeclarations) {
			if (declaration.eContainer().equals(schema)) {
                declarations.add(declaration);
            }
		}

		Object[] schemaChildren = Util.filterOutDuplicatedElems(declarations
				.toArray(new XSDNamedComponent[declarations.size()]));

		return schemaChildren;
	}

	XSDSchema getDatamodelXSD(XSDConcreteComponent component) {
		if (null == component.getContainer()) {
			return (XSDSchema) component;
		}
		return getDatamodelXSD(component.getContainer());
	}

	@Override
	public void refresh() {
		composite.setInfos(fkInfos.toArray(new String[0]));
		removeUIListener();
		chkResolveAutoInWeb.setSelection(isResolveAutoInWeb);
		addUIListener();
		updateSectionEnabled();
	}

	@Override
	protected void initUIContents(XSDComponent editedObj) {
		super.initUIContents(editedObj);

		fkInfos.clear();

		XSDAnnotationsStructure annoStruct = new XSDAnnotationsStructure(
				curXSDComponent);

		for (String eachFKInfo : annoStruct.getForeignKeyInfos().values()) {
            fkInfos.add(eachFKInfo);
        }

		isResolveAutoInWeb = annoStruct.getRetrieveFKinfos();
		holder.setDefaultDataModel(getDataModelName());
		holder.setDefaultEntity(getEntityName());
	}

	@Override
	protected ISubmittable getSubmittedObj() {
        return new ForeignKeyInfosAnnoInfo(curXSDComponent, composite.getInfos(), isResolveAutoInWeb);
	}

	@Override
	protected void createControlsInSection(Composite compSectionClient) {
		composite = new XpathComposite(compSectionClient, SWT.NONE, this) {
			@Override
			protected void createExtentUIArea(Composite parent) {
				chkResolveAutoInWeb = new Button(this, SWT.CHECK);
				chkResolveAutoInWeb.setText(Messages.FKInfos_resolve);
				chkResolveAutoInWeb.setSelection(true);
				chkResolveAutoInWeb.setLayoutData(new GridData());
			}
		};
		holder = new FixDMNameBasePropertySectionDataModelExtractor(this);
	}

	SelectionAdapter selectionListener = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
            isResolveAutoInWeb = chkResolveAutoInWeb.getSelection();
			autoCommit();
		}
	};

	public void addUIListener() {
		chkResolveAutoInWeb.addSelectionListener(selectionListener);
	}

	public void removeUIListener() {
		chkResolveAutoInWeb.removeSelectionListener(selectionListener);
	}

	public boolean isResolveAutoInWeb() {
		return chkResolveAutoInWeb.getSelection();
	}

	public void setIsResolveAutoInWeb(boolean isResolveAutoInWeb) {
		chkResolveAutoInWeb.setSelection(isResolveAutoInWeb);
	}

	@Override
	protected String getSectionTitle() {
		return Messages.ElementFKInfosSection_ForeignKeyInfos;
	}

	@Override
	public Set<String> getEntities() {
		if (curXSDComponent instanceof XSDParticle) {
			XSDParticle ele = ((XSDParticle) curXSDComponent);
			XSDTerm term = ele.getTerm();
			if (term instanceof XSDElementDeclaration) {
				XSDAnnotation anno = ((XSDElementDeclaration) term)
						.getAnnotation();
				if (null != anno) {
					Set<String> list = new HashSet<String>();
					Util.getForeignKeyofParcle(list, anno);
					return list;
				}
			}
		}
		return null;
	}

	@Override
	public IAllDataModelHolder getDataHolder() {
		return holder;
	}
}
