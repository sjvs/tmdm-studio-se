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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.amalto.workbench.MDMWorbenchPlugin;
import com.amalto.workbench.i18n.Messages;
import com.amalto.workbench.preferences.JSSEConstants.KEYSTORE_TYPE;
import com.amalto.workbench.preferences.JSSEConstants.SSL_Algorithm;
import com.amalto.workbench.utils.PasswordUtil;
import com.amalto.workbench.utils.SSLContextProvider;

/**
 * created by changguopiao on 2013-7-10 Detailled comment
 * 
 */
public class SSLPreferences extends PreferencePage implements IWorkbenchPreferencePage {

    private static final String[] SSL_ALGORITHMS;

    private static final String[] KEYSTORE_TYPES;

    private static Log log = LogFactory.getLog(SSLPreferences.class);

    static {
        SSL_Algorithm[] algorithms = SSL_Algorithm.values();
        SSL_ALGORITHMS = new String[algorithms.length];
        for (int i = 0; i < algorithms.length; i++) {
            SSL_ALGORITHMS[i] = algorithms[i].name();
        }
        KEYSTORE_TYPE[] kt = KEYSTORE_TYPE.values();
        KEYSTORE_TYPES = new String[kt.length];
        for (int i = 0; i < kt.length; i++) {
            KEYSTORE_TYPES[i] = kt[i].name();
        }
    }

    public static String getDefaultKeystoreType() {
        return KEYSTORE_TYPES[0];
    }

    public static String getDefaultAlgorithmType() {
        return SSL_ALGORITHMS[0];
    }

    public synchronized void init(IWorkbench workbench) {
        IPreferenceStore store = MDMWorbenchPlugin.getDefault().getPreferenceStore();
        super.setPreferenceStore(store);
    }

    private Combo sslAlgorithmCombo;

    private Button[] verificationType;

    private Text keyPath;

    private Text keyPassword;

    private Combo keyType;

    private Text trustPath;

    private Text trustPassword;

    private Combo trustType;

    private CLabel createLabel(Composite parent, String text) {
        CLabel label = new CLabel(parent, SWT.LEFT);
        label.setText(text);
        GridDataFactory.fillDefaults().applyTo(label);
        return label;
    }

    private Text createText(Composite parent, String content, int hsize) {
        return createText(parent, content, hsize, false);
    }

    private Text createText(Composite parent, String content, int hsize, boolean pw) {
        Text text = null;
        if (pw) {
            text = new Text(parent, SWT.BORDER | SWT.PASSWORD);
        } else {
            text = new Text(parent, SWT.SINGLE | SWT.BORDER);
        }
        if (null != content) {
            text.setText(content);
        }
        if (hsize == 1) {
            GridDataFactory.swtDefaults().span(hsize, 1).hint(200, -1).applyTo(text);
        } else {
            GridDataFactory.fillDefaults().span(hsize, 1).applyTo(text);
        }
        return text;
    }

    private Combo createCombo(Composite parent, String[] items, int index, int hsize) {
        Combo combo = new Combo(parent, SWT.READ_ONLY | SWT.BORDER);
        combo.setItems(items);
        combo.select(index);
        GridDataFactory.defaultsFor(combo).span(hsize, 1).indent(hsize, 1).minSize(100, SWT.DEFAULT).applyTo(combo);
        return combo;
    }

    private Combo createCombo(Composite parent, String[] items, String dItem, int hsize) {
        int index = 0;
        if (null != dItem) {
            for (int i = 0; i < items.length; i++) {
                if (dItem.equals(items[i])) {
                    index = i;
                }
            }
        }
        return createCombo(parent, items, index, hsize);
    }

    private void createFilePicker(Composite parent, final Text text, final Shell shell) {
        Button button = new Button(parent, SWT.PUSH);
        button.setText(Messages.fileBrowse);
        button.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                FileDialog dialog = new FileDialog(shell);
                dialog.setText(Messages.keystoreFileTile);
                String str = dialog.open();
                if (null != str) {
                    text.setText(str);
                }
            }
        });
        GridDataFactory.fillDefaults().applyTo(button);
    }

    private Group createGroup(String title, Composite parent, int column) {
        Group group = new Group(parent, SWT.NONE);
        group.setText(title);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(group);
        group.setLayout(new GridLayout(column, false));
        return group;
    }

    private Button[] createRadioBox(Composite parent, int hsize) {
        Composite com = new Composite(parent, SWT.NONE);
        GridDataFactory.fillDefaults().span(hsize, 1).applyTo(com);
        com.setLayout(new FillLayout());
        Button[] buttons = new Button[2];
        buttons[0] = new Button(com, SWT.RADIO);
        buttons[0].setText(Messages.verify_ALLOW_ALL);
        buttons[1] = new Button(com, SWT.RADIO);
        buttons[1].setText(Messages.verify_STRICT);
        return buttons;
    }

    private void setRadioValue(boolean value) {
        verificationType[0].setSelection(!value);
        verificationType[1].setSelection(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createContents(Composite parent) {
        IPreferenceStore store = getPreferenceStore();
        Composite composite = new Composite(parent, SWT.NONE);
        GridDataFactory.fillDefaults().applyTo(composite);
        composite.setLayout(new GridLayout(1, false));
        {
            Group group = createGroup(Messages.securityTitle, composite, 3);
            createLabel(group, Messages.sslAlgorithm);
            sslAlgorithmCombo = createCombo(group, SSL_ALGORITHMS, store.getString(PreferenceConstants.SSL_Algorithm), 2);
            createLabel(group, Messages.hostnameVerificationMode);
            verificationType = createRadioBox(group, 2);
            setRadioValue(store.getBoolean(PreferenceConstants.VERIFY_HOSTNAME));

        }
        {
            Group group = createGroup(Messages.keystoreConfiguration, composite, 3);
            createLabel(group, Messages.keystorePath);
            keyPath = createText(group, store.getString(PreferenceConstants.KEYSTORE_FILE), 1);
            createFilePicker(group, keyPath, parent.getShell());
            createLabel(group, Messages.keystorePassword);
            String keystorePassword = store.getString(PreferenceConstants.KEYSTORE_PASSWORD);
            keystorePassword = decryptPasswd(keystorePassword);
            keyPassword = createText(group, keystorePassword, 2, true);
            createLabel(group, Messages.keyststoreType);
            keyType = createCombo(group, SSLPreferences.KEYSTORE_TYPES, store.getString(PreferenceConstants.KEYSTORE_TYPE), 2);
        }
        {
            Group group = createGroup(Messages.truststoreConfiguration, composite, 3);
            createLabel(group, Messages.keystorePath);
            trustPath = createText(group, store.getString(PreferenceConstants.TRUSTSTORE_FILE), 1);
            createFilePicker(group, trustPath, parent.getShell());
            createLabel(group, Messages.keystorePassword);
            String truststorePassword = store.getString(PreferenceConstants.TRUSTSTORE_PASSWORD);
            truststorePassword = decryptPasswd(truststorePassword);
            trustPassword = createText(group, truststorePassword, 2, true);
            createLabel(group, Messages.keyststoreType);
            trustType = createCombo(group, SSLPreferences.KEYSTORE_TYPES, store.getString(PreferenceConstants.KEYSTORE_TYPE), 2);
        }
        return composite;
    }

    private String encryptPasswd(String passwd) {
        return PasswordUtil.encryptPassword(passwd);
    }

    private String decryptPasswd(String passwd) {
        return PasswordUtil.decryptPassword(passwd);
    }

    private void comboSelect(Combo combo, String str) {
        if (null == str) {
            return;
        }
        String[] items = combo.getItems();
        for (int i = 0; i < items.length; i++) {
            if (str.equals(items[i])) {
                combo.select(i);
                return;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
     */
    @Override
    protected void performDefaults() {
        IPreferenceStore store = getPreferenceStore();

        comboSelect(sslAlgorithmCombo, store.getDefaultString(PreferenceConstants.SSL_Algorithm));
        setRadioValue(store.getDefaultBoolean(PreferenceConstants.VERIFY_HOSTNAME));

        keyPath.setText(store.getDefaultString(PreferenceConstants.KEYSTORE_FILE));
        keyPassword.setText(store.getDefaultString(PreferenceConstants.KEYSTORE_PASSWORD));
        comboSelect(keyType, store.getDefaultString(PreferenceConstants.KEYSTORE_TYPE));

        trustPath.setText(store.getDefaultString(PreferenceConstants.TRUSTSTORE_PASSWORD));
        trustPassword.setText(store.getDefaultString(PreferenceConstants.TRUSTSTORE_PASSWORD));
        comboSelect(trustType, store.getDefaultString(PreferenceConstants.TRUSTSTORE_TYPE));

        super.performDefaults();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    @Override
    public boolean performOk() {
        IPreferenceStore store = getPreferenceStore();
        String algorithm = sslAlgorithmCombo.getText();
        String keypath = keyPath.getText();
        String keypass = keyPassword.getText();
        String keytype = keyType.getText();
        String trustpath = trustPath.getText();
        String trustpass = trustPassword.getText();
        String trusttype = trustType.getText();
        try {
            SSLContextProvider.buildContext(algorithm, keypath, keypass, keytype, trustpath, trustpass, trusttype);
            store.setValue(PreferenceConstants.SSL_Algorithm, algorithm);
            store.setValue(PreferenceConstants.VERIFY_HOSTNAME, verificationType[1].getSelection());
            store.setValue(PreferenceConstants.KEYSTORE_FILE, keypath);
            store.setValue(PreferenceConstants.KEYSTORE_PASSWORD, encryptPasswd(keypass));
            store.setValue(PreferenceConstants.KEYSTORE_TYPE, keytype);
            store.setValue(PreferenceConstants.TRUSTSTORE_FILE, trustpath);
            store.setValue(PreferenceConstants.TRUSTSTORE_PASSWORD, encryptPasswd(trustpass));
            store.setValue(PreferenceConstants.TRUSTSTORE_TYPE, trusttype);
            setErrorMessage(null);
            return super.performOk();
        } catch (Exception e) {
            setErrorMessage(e.getMessage());
            log.error(e.getMessage(), e);
            return false;
        }

    }
}
