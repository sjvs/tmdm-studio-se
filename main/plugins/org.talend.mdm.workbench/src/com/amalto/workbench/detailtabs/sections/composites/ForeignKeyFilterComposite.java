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
package com.amalto.workbench.detailtabs.sections.composites;

import java.util.ArrayList;

import org.apache.commons.lang.StringEscapeUtils;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import com.amalto.workbench.detailtabs.sections.BasePropertySection;
import com.amalto.workbench.detailtabs.sections.model.annotationinfo.relationship.ForeignKeyFilterAnnoInfo;
import com.amalto.workbench.detailtabs.sections.model.annotationinfo.relationship.ForeignKeyFilterAnnoInfoDefUnit;
import com.amalto.workbench.detailtabs.sections.providers.ForeignKeyFilterCellModifier;
import com.amalto.workbench.i18n.Messages;
import com.amalto.workbench.image.EImage;
import com.amalto.workbench.image.ImageCache;
import com.amalto.workbench.models.infoextractor.IAllDataModelHolder;
import com.amalto.workbench.providers.ColumnTextExtractor;
import com.amalto.workbench.providers.CommonTableLabelProvider;
import com.amalto.workbench.utils.IConstants;
import com.amalto.workbench.widgets.celleditor.XPathCellEditor;
import com.amalto.workbench.widgets.composites.ComplexAnnotaionInfoComposite;

public class ForeignKeyFilterComposite extends ComplexAnnotaionInfoComposite<ForeignKeyFilterAnnoInfoDefUnit> {
    private final String CUSTOM_FILTERS_PREFIX = "$CFFP:";//$NON-NLS-1$

    private Text txtCustomFilter;

    private IAllDataModelHolder dataModelHolder;

    public ForeignKeyFilterComposite(Composite parent, int style, IAllDataModelHolder dataModelHolder,BasePropertySection section) {
    	super(parent,style,new Object[] { dataModelHolder },section);

    }
    public ForeignKeyFilterComposite(Composite parent, int style, IAllDataModelHolder dataModelHolder) {
        super(parent, style, new Object[] { dataModelHolder },null);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ITableLabelProvider getLabelProviderForViewer() {
        return new CommonTableLabelProvider<ForeignKeyFilterAnnoInfoDefUnit>(new ColumnTextExtractor[] {
                new ColumnTextExtractor<ForeignKeyFilterAnnoInfoDefUnit>("getXpath"),//$NON-NLS-1$
                new ColumnTextExtractor<ForeignKeyFilterAnnoInfoDefUnit>("getOperator"),//$NON-NLS-1$
                new ColumnTextExtractor<ForeignKeyFilterAnnoInfoDefUnit>("getValue"),//$NON-NLS-1$
                new ColumnTextExtractor<ForeignKeyFilterAnnoInfoDefUnit>("getPredicate") });//$NON-NLS-1$
    }

    @Override
    protected void createExtentArea() {
        Composite comp = new Composite(this, SWT.NONE);
        comp.setLayout(new GridLayout(2, false));
        defineCF = new Button(comp, SWT.CHECK);
        defineCF.setText(Messages.ForeignKeyFilterComposite_defineCustomeFilter);
        warnLabel = new CLabel(comp, SWT.LEFT | SWT.WRAP);

        Image warnImage = ImageCache.getCreatedImage(EImage.WARN_TSK.getPath());
        warnLabel.setImage(warnImage);
        warnLabel.setText(Messages.ForeignKeyFilterComposite_defineWarningMsg);
        warnLabel.setVisible(false);
        defineCF.addSelectionListener(getWarnSelectionListener());

        Group gpCustomFilter = new Group(this, SWT.NORMAL);
        gpCustomFilter.setText(Messages.ForeignKeyFilterComposite_CustomFilters);
        groupLayoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
        groupLayoutData.minimumHeight = 60;
        gpCustomFilter.setLayoutData(groupLayoutData);
        gpCustomFilter.setLayout(new GridLayout());

        txtCustomFilter = new Text(gpCustomFilter, SWT.MULTI | SWT.V_SCROLL);
        GridData gdTxtCustomFilter = new GridData(SWT.FILL, SWT.FILL, true, true);
        gdTxtCustomFilter.heightHint = 35;
        txtCustomFilter.setLayoutData(gdTxtCustomFilter);
        txtCustomFilter.addModifyListener(getCustomFilterModifyListener());
    }

    private SelectionListener getWarnSelectionListener() {
        SelectionListener listener = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                showCustomFilterText(defineCF.getSelection());
            }
        };

        return listener;
    }

    private ModifyListener modifyListener;

    private Button defineCF;

    private CLabel warnLabel;

    private GridData groupLayoutData;

    private ModifyListener getCustomFilterModifyListener() {
        if(modifyListener == null) {
            modifyListener = new ModifyListener() {
                public void modifyText(ModifyEvent e) {
                    if (section != null)
                        section.autoCommit();
                }
            };
        }

        return modifyListener;
    }

    @Override
    protected void fillColumnsInTree(Tree tree) {

        final TreeColumn colXPath = new TreeColumn(tree, SWT.NONE);
        colXPath.setWidth(200);
        colXPath.setText(Messages.ForeignKeyFilterComposite_XPath);
        colXPath.setImage(nillableColImage);

        final TreeColumn colOperator = new TreeColumn(tree, SWT.NONE);
        colOperator.setWidth(100);
        colOperator.setText(Messages.ForeignKeyFilterComposite_Operator);
        colOperator.setImage(nillableColImage);

        final TreeColumn colValue = new TreeColumn(tree, SWT.NONE);
        colValue.setWidth(200);
        colValue.setText(Messages.ForeignKeyFilterComposite_Value);
        colValue.setImage(nillableColImage);

        final TreeColumn colPredicate = new TreeColumn(tree, SWT.NONE);
        colPredicate.setWidth(100);
        colPredicate.setText(Messages.ForeignKeyFilterComposite_Predicate);

    }

    @Override
    protected ForeignKeyFilterAnnoInfoDefUnit createDefaultInfoObj() {
        return new ForeignKeyFilterAnnoInfoDefUnit("newXPath", IConstants.VIEW_CONDITION_OPERATORS[0], "",//$NON-NLS-1$//$NON-NLS-2$
                IConstants.PREDICATES[0]);
    }

    @Override
    protected boolean validateBeforePaste(ForeignKeyFilterAnnoInfoDefUnit copyedObj) {
        return true;
    }

    @Override
    protected ForeignKeyFilterAnnoInfoDefUnit cloneCopyedObj(ForeignKeyFilterAnnoInfoDefUnit copyedObj) {
        return new ForeignKeyFilterAnnoInfoDefUnit(copyedObj.getXpath(), copyedObj.getOperator(), copyedObj.getValue(),
                copyedObj.getPredicate());
    }

    @Override
    protected String[] getColumnProperties() {
        return ForeignKeyFilterCellModifier.COLPROPS;
    }

    @Override
    protected CellEditor[] getCellEditors() {
        return new CellEditor[] { new XPathCellEditor(tvInfos.getTree(), dataModelHolder),
                new ComboBoxCellEditor(tvInfos.getTree(), IConstants.VIEW_CONDITION_OPERATORS, SWT.READ_ONLY),
                new TextCellEditor(tvInfos.getTree()),
                new ComboBoxCellEditor(tvInfos.getTree(), IConstants.PREDICATES, SWT.READ_ONLY) };
    }

    @Override
    protected ICellModifier getCellModifier() {
        ForeignKeyFilterCellModifier modifier=new ForeignKeyFilterCellModifier(tvInfos);
        modifier.setSection(section);
        return modifier;
    }

    @Override
    protected void initParameters(Object[] parameters) {
        infos = new ArrayList<ForeignKeyFilterAnnoInfoDefUnit>();

        for (Object eachPara : parameters) {

            if (eachPara instanceof IAllDataModelHolder)
                dataModelHolder = (IAllDataModelHolder) eachPara;
        }
    }

    private String getCustomFilter() {
        return txtCustomFilter.getText().trim();
    }

    private boolean hasCustomFilter() {
        return !"".equals(getCustomFilter().trim());//$NON-NLS-1$
    }

    public String getFilterExpression() {

        if (hasCustomFilter())
            return ForeignKeyFilterAnnoInfo.getFKFilterByRawCustomFilterStr(getCustomFilter());

        return ForeignKeyFilterAnnoInfo.getFKFilterByFKFilterCfgInfos(infos.toArray(new ForeignKeyFilterAnnoInfoDefUnit[0]));
    }

    public void setFilter(String filterExpression) {
    	String filter=ForeignKeyFilterAnnoInfo.getCustomFilterInfo(filterExpression);

        boolean isCustomFilter = filter.startsWith(CUSTOM_FILTERS_PREFIX);
        defineCF.setSelection(isCustomFilter);

        showCustomFilterText(isCustomFilter);

        if (isCustomFilter) {
            filter = StringEscapeUtils.unescapeXml(filter).substring(6);
        }

        txtCustomFilter.removeModifyListener(getCustomFilterModifyListener());


        if (!filter.equals(txtCustomFilter.getText())) {
            txtCustomFilter.setText(filter);
        }

        txtCustomFilter.addModifyListener(getCustomFilterModifyListener());
        setInfos(ForeignKeyFilterAnnoInfo.getFKFilterCfgInfos(filterExpression));

    }

    private void showCustomFilterText(boolean show) {
        warnLabel.setVisible(show);
        txtCustomFilter.setVisible(show);
        groupLayoutData.exclude = !show;
        layout();
    }

    @Override
    protected void createTopExtentArea() {
    }
}
