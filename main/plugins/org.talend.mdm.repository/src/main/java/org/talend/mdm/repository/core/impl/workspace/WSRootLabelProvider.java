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
package org.talend.mdm.repository.core.impl.workspace;

import org.apache.log4j.Logger;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.mdm.repository.core.IRepositoryNodeLabelProvider;
import org.talend.mdm.repository.core.impl.AbstractLabelProvider;
import org.talend.mdm.repository.i18n.Messages;
import org.talend.mdm.repository.plugin.RepositoryPlugin;
import org.talend.mdm.repository.utils.EclipseResourceManager;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * DOC hbhong class global comment. Detailled comment
 */
public class WSRootLabelProvider implements IRepositoryNodeLabelProvider {

    static Logger log = Logger.getLogger(WSRootLabelProvider.class);

    private static final Image IMG_REPOSITORY = EclipseResourceManager.getImage(RepositoryPlugin.PLUGIN_ID,
            "icons/repository.gif"); //$NON-NLS-1$;

    private static final Image IMG_SVN_REPOSITORY = EclipseResourceManager.getImage(RepositoryPlugin.PLUGIN_ID,
            "icons/svn_repository.gif"); //$NON-NLS-1$;

    public String getCategoryLabel(ERepositoryObjectType type) {
        return getText(null);
    }

    public String getText(Object element) {
        if (isSVNRepository())
            return Messages.WSRootLabelProvider_svn;
        return Messages.WSRootLabelProvider_localRepository;
    }

    public Image getImage(Object element) {
        if (isSVNRepository())
            return IMG_SVN_REPOSITORY;
        else
            return IMG_REPOSITORY;
    }

    public String getDescription(Object anElement) {
        return null;
    }

    public Color getForeground(Object element) {
        return null;
    }

    public Font getFont(Object element) {
        return AbstractLabelProvider.FONT_BOLD;
    }

    private boolean isSVNRepository() {
        IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
        try {
            return !factory.isLocalConnectionProvider();
        } catch (PersistenceException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }
}
