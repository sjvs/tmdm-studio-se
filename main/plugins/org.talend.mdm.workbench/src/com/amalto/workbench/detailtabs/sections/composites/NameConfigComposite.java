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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.amalto.workbench.detailtabs.sections.BasePropertySection;
import com.amalto.workbench.detailtabs.sections.handlers.RefreshPropertySheetTitleHandler;
import com.amalto.workbench.detailtabs.sections.model.INameEditable;
import com.amalto.workbench.detailtabs.sections.model.entity.EntityWrapper;
import com.amalto.workbench.i18n.Messages;
import com.amalto.workbench.image.EImage;
import com.amalto.workbench.image.ImageCache;

public class NameConfigComposite extends Composite {

    private INameEditable target;

    private Text txtName;

    private ModifyListener lTxtNameListener;

    private Label lblNameErrIndicator;

    private Image errIcon = ImageCache.getImage(EImage.ERROR.getPath()).createImage();

    protected BasePropertySection section;
    public NameConfigComposite(Composite parent, int style,BasePropertySection section) {
    	this(parent,style);
    	this.section=section;
    }
    
    public NameConfigComposite(Composite parent, int style) {
        super(parent, style);

        setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));

        final GridLayout gridLayout = new GridLayout();
        gridLayout.horizontalSpacing = 0;
        gridLayout.numColumns = 3;
        setLayout(gridLayout);

        final Label lblName = new Label(this, SWT.NONE);
        lblName.setText(Messages.NameConfigComposite_Name);
        lblName.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));

        lblNameErrIndicator = new Label(this, SWT.NONE);
        lblNameErrIndicator.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        lblNameErrIndicator.setImage(errIcon);
        lblNameErrIndicator.setVisible(false);

        txtName = new Text(this, SWT.BORDER);
        final GridData gd_txtName = new GridData(SWT.FILL, SWT.CENTER, true, false);
        txtName.setLayoutData(gd_txtName);

        initUIListener();

        addUIListener();
    }

    public void setTarget(INameEditable target) {
        this.target = target;

        remvoeUIListener();

        txtName.setText("");//$NON-NLS-1$
        lblNameErrIndicator.setVisible(false);
        lblNameErrIndicator.setToolTipText("");//$NON-NLS-1$

        if (target != null) {
            String name = target.getName();
			txtName.setText(name);
    		if (name != null) {
    			int length = name.length();
				if (length >= caretOffset) {
    				txtName.setSelection(caretOffset,caretOffset);
    			} else {
    				txtName.setSelection(length,length);
    			}
    		}
        }

        addUIListener();
    }
    private int caretOffset;
    private void initUIListener() {

        lTxtNameListener = new ModifyListener() {

            public void modifyText(ModifyEvent e) {

                if (target == null)
                    return;

                String errMsg = target.validNewName(txtName.getText().trim());

                lblNameErrIndicator.setVisible(errMsg != null);
                lblNameErrIndicator.setToolTipText(errMsg == null ? "" : errMsg);//$NON-NLS-1$

                target.setName(txtName.getText().trim());
                caretOffset = txtName.getCaretPosition();
                if (section != null) {
                    section.autoCommit();

                    if (target instanceof EntityWrapper) {
                        EntityWrapper wrapper = (EntityWrapper) target;
                        RefreshPropertySheetTitleHandler.refreshPropertySheetTitle(section, wrapper.getSourceEntity());
                    }
                }
            }
        };
    }

    private void addUIListener() {
        txtName.addModifyListener(lTxtNameListener);
    }

    private void remvoeUIListener() {
        txtName.removeModifyListener(lTxtNameListener);
    }

    @Override
    public void dispose() {
        super.dispose();

        errIcon.dispose();
    }
}
