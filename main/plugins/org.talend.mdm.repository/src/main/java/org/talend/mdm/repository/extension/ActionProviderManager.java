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
package org.talend.mdm.repository.extension;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.talend.mdm.repository.core.IRepositoryNodeActionProvider;
import org.talend.mdm.repository.plugin.RepositoryPlugin;

/**
 * DOC hbhong class global comment. Detailled comment <br/>
 * 
 */
public class ActionProviderManager {

    private static Logger log = Logger.getLogger(ActionProviderManager.class);

    private static final String EXTENSION_POINT_TEMPLATE = "actionProviderConfiguration"; //$NON-NLS-1$

    private static final String PROP_CLASS = "class"; //$NON-NLS-1$

    private static final String ID = "id"; //$NON-NLS-1$

    private static boolean inited = false;

    private static Map<String, IRepositoryNodeActionProvider> providerMap = new HashMap<String, IRepositoryNodeActionProvider>();

    public static Collection<IRepositoryNodeActionProvider> getActionProviders() {
        if (!inited) {
            initProviderDefine();
        }
        return providerMap.values();
    }

    public static IRepositoryNodeActionProvider getActionProvider(String id) {
        if (id == null)
            throw new IllegalArgumentException();
        initProviderDefine();
        return providerMap.get(id);
    }

    private static void initProviderDefine() {
        if (!inited) {
            IExtensionRegistry registry = Platform.getExtensionRegistry();

            //
            IExtensionPoint extensionPoint = registry.getExtensionPoint(RepositoryPlugin.PLUGIN_ID, EXTENSION_POINT_TEMPLATE);
            if (extensionPoint != null && extensionPoint.isValid()) {
                IExtension[] extensions = extensionPoint.getExtensions();
                for (IExtension s : extensions) {
                    IConfigurationElement[] elements = s.getConfigurationElements();
                    for (IConfigurationElement element : elements) {
                        String id = element.getAttribute(ID);
                        if (id != null && element.getAttribute(PROP_CLASS) != null) {
                            try {
                                IRepositoryNodeActionProvider actionProvider = (IRepositoryNodeActionProvider) element
                                        .createExecutableExtension(PROP_CLASS);
                                providerMap.put(id, actionProvider);

                            } catch (CoreException e) {
                                log.error(e.getMessage(), e);
                            }
                        }
                    }
                }
            }
            inited = true;
        }
    }

}
