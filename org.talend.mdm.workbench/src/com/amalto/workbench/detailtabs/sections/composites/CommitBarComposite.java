package com.amalto.workbench.detailtabs.sections.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class CommitBarComposite extends Composite {

    private Button btnSubmit;

    private Button btnReset;

    private List<CommitBarListener> listeners = new ArrayList<CommitBarListener>();

    public CommitBarComposite(Composite parent, int style) {
        super(parent, style);

        final GridLayout gridLayout = new GridLayout();
        gridLayout.horizontalSpacing = 10;
        gridLayout.makeColumnsEqualWidth = true;
        gridLayout.numColumns = 2;
        setLayout(gridLayout);

        setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));

        btnSubmit = new Button(this, SWT.NONE);
        final GridData gd_btnSubmit = new GridData(SWT.FILL, SWT.FILL, false, false);
        gd_btnSubmit.heightHint = 18;
        gd_btnSubmit.widthHint = 74;
        btnSubmit.setLayoutData(gd_btnSubmit);
        btnSubmit.setText("Submit");

        btnReset = new Button(this, SWT.NONE);
        btnReset.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        btnReset.setText("Reset");

        initUIListeners();
    }

    public void addCommitListener(CommitBarListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }

    }

    public void removeCommitListener(CommitBarListener listener) {
        listeners.remove(listener);
    }

    public void removeAllCommitBarListeners() {
        listeners.clear();
    }

    public CommitBarListener[] getCommitBarListeners() {
        return listeners.toArray(new CommitBarListener[0]);
    }

    private void fireSubmit() {
        for (CommitBarListener eachListener : listeners)
            eachListener.onSubmit();
    }

    private void fireReset() {
        for (CommitBarListener eachListener : listeners)
            eachListener.onReset();
    }

    private void initUIListeners() {

        btnSubmit.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                fireSubmit();
            }
        });

        btnReset.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {

                fireReset();
            }
        });
    }

    public interface CommitBarListener {

        public void onReset();

        public void onSubmit();

    }
}