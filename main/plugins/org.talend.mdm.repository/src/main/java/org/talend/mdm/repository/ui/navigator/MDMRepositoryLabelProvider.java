// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.mdm.repository.ui.navigator;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonLabelProvider;
import org.eclipse.ui.navigator.IDescriptionProvider;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.mdm.repository.core.IRepositoryNodeConfiguration;
import org.talend.mdm.repository.core.IRepositoryNodeLabelProvider;
import org.talend.mdm.repository.core.service.IMDMSVNProviderService;
import org.talend.mdm.repository.extension.RepositoryNodeConfigurationManager;
import org.talend.mdm.repository.utils.RepositoryResourceUtil;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * DOC hbhong class global comment. Detailled comment <br/>
 * 
 */
public class MDMRepositoryLabelProvider extends ColumnLabelProvider implements ILabelProvider, IDescriptionProvider,
        IColorProvider, IFontProvider, ICommonLabelProvider, IStyledLabelProvider {

    private static Logger log = Logger.getLogger(MDMRepositoryLabelProvider.class);

    private IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();

    private IMDMSVNProviderService svnProviderService = null;

    private Boolean isInSvnMode = null;

    @Override
    public void addListener(ILabelProviderListener listener) {
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean isLabelProperty(Object element, String property) {
        return false;
    }

    @Override
    public void removeListener(ILabelProviderListener listener) {
    }

    public String getDescription(Object element) {
        IRepositoryNodeLabelProvider provider = getLabelProvider(element);
        if (provider != null) {
            return provider.getDescription(element);
        }
        return null;
    }

    @Override
    public Image getImage(Object element) {
        IRepositoryNodeLabelProvider provider = getLabelProvider(element);
        if (provider != null) {
            return provider.getImage(element);
        }
        return null;
    }

    @Override
    public String getText(Object element) {
        IRepositoryNodeLabelProvider provider = getLabelProvider(element);
        if (provider != null) {
            return provider.getText(element);
        }
        return element.toString();
    }

    protected IRepositoryNodeLabelProvider getLabelProvider(Object element) {
        if (element instanceof IRepositoryViewObject) {
            IRepositoryViewObject viewObject = (IRepositoryViewObject) element;
            viewObject = RepositoryResourceUtil.assertViewObject(viewObject);
            Property property = viewObject.getProperty();
            if (property != null) {
                Item item = property.getItem();
                IRepositoryNodeConfiguration conf = RepositoryNodeConfigurationManager.getConfiguration(item);
                if (conf != null) {
                    return conf.getLabelProvider();
                }
            }
        }
        return null;
    }

    @Override
    public Color getForeground(Object element) {
        IRepositoryNodeLabelProvider provider = getLabelProvider(element);
        if (provider != null) {
            return provider.getForeground(element);
        }
        return null;
    }

    @Override
    public Font getFont(Object element) {
        IRepositoryNodeLabelProvider provider = getLabelProvider(element);
        if (provider != null) {
            return provider.getFont(element);
        }
        return null;
    }

    @Override
    public Color getBackground(Object element) {
        return null;
    }

    @Override
    public String getToolTipText(Object element) {
        if (isInSvnMode()) {
            IRepositoryViewObject viewObj = (IRepositoryViewObject) element;

            if (getSvnProviderService() != null) {
                String toolTip = getSvnProviderService().getLockInfo(viewObj);
                return toolTip;
            }
        }

        return null;
    }

    private boolean isInSvnMode() {
        if (isInSvnMode == null) {
            try {
                if (!factory.isLocalConnectionProvider()) {
                    IMDMSVNProviderService service = getSvnProviderService();
                    if (service != null && service.isProjectInSvnMode()) {
                        isInSvnMode = Boolean.TRUE;
                    }
                }
            } catch (PersistenceException e) {
                log.error(e.getMessage(), e);
            }

            if (isInSvnMode == null) {
                isInSvnMode = Boolean.FALSE;
            }
        }

        return isInSvnMode.booleanValue();
    }

    private IMDMSVNProviderService getSvnProviderService() {
        if (svnProviderService == null) {
            try {
                svnProviderService = (IMDMSVNProviderService) GlobalServiceRegister.getDefault().getService(
                        IMDMSVNProviderService.class);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        return svnProviderService;
    }

    public void restoreState(IMemento aMemento) {
    }

    public void saveState(IMemento aMemento) {
    }

    public void init(ICommonContentExtensionSite aConfig) {
    }

    public StyledString getStyledText(Object element) {
        return new StyledString(getText(element));
    }

}
