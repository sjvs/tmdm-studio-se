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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.mdm.repository.core.IRepositoryNodeConfiguration;
import org.talend.mdm.repository.core.IRepositoryNodeResourceProvider;
import org.talend.mdm.repository.core.impl.recyclebin.RecycleBinNodeConfiguration;
import org.talend.mdm.repository.model.mdmproperties.ContainerItem;
import org.talend.mdm.repository.plugin.RepositoryPlugin;
import org.talend.mdm.repository.utils.Bean2EObjUtil;
import org.talend.mdm.repository.utils.RepositoryResourceUtil;

/**
 * DOC hbhong class global comment. Detailled comment <br/>
 * 
 */
public class RepositoryNodeConfigurationManager {

    private static Logger log = Logger.getLogger(RepositoryNodeConfigurationManager.class);

    private static final String EXTENSION_POINT_TEMPLATE = "repositoryConfiguration"; //$NON-NLS-1$

    private static final String PROP_CLASS = "class"; //$NON-NLS-1$

    private static boolean inited = false;

    private static Map<EClass, IRepositoryNodeConfiguration> itemConfMap = new HashMap<EClass, IRepositoryNodeConfiguration>();

    private static Map<ERepositoryObjectType, IRepositoryNodeConfiguration> typeConfMap = new HashMap<ERepositoryObjectType, IRepositoryNodeConfiguration>();

    private static List<IRepositoryNodeConfiguration> configurations = new LinkedList<IRepositoryNodeConfiguration>();

    private static boolean initedBean = false;

    private static IRepositoryNodeConfiguration recycleBinNodeConfiguration = new RecycleBinNodeConfiguration();

    public static IRepositoryNodeConfiguration getRecycleBinNodeConfiguration() {
        return recycleBinNodeConfiguration;
    }

    public static List<IRepositoryNodeConfiguration> getConfigurations() {
        if (!inited) {
            initTemplateDefine();
        }
        if (!initedBean) {
            initBeanMap();
        }
        return configurations;
    }

    public static IRepositoryNodeConfiguration getConfiguration(Item item) {
        EClass eClass = item.eClass();
        IRepositoryNodeConfiguration configuration = itemConfMap.get(eClass);
        if (configuration != null) {
            return configuration;
        }
        for (IRepositoryNodeConfiguration conf : getConfigurations()) {
            IRepositoryNodeResourceProvider resourceProvider = conf.getResourceProvider();
            if (resourceProvider != null) {
                if (resourceProvider.canHandleItem(item)) {
                    if (!(item instanceof ContainerItem)) {
                        itemConfMap.put(eClass, conf);
                    }
                    return conf;
                }
            }
        }
        if (recycleBinNodeConfiguration.getResourceProvider().canHandleItem(item)) {
            return recycleBinNodeConfiguration;
        }
        return null;
    }

    public static IRepositoryNodeConfiguration getConfiguration(IRepositoryViewObject viewObj) {
        Item item = viewObj.getProperty().getItem();
        if (item != null && !(item instanceof ContainerItem)) {
            item = RepositoryResourceUtil.assertItem(item);
        }
        if (item != null) {
            if (RepositoryResourceUtil.isDeletedFolder(item, viewObj.getRepositoryObjectType())) {
                return getRecycleBinNodeConfiguration();
            } else {
                return getConfiguration(item);
            }
        }
        return null;
    }

    public static IRepositoryNodeConfiguration getConfiguration(ERepositoryObjectType type) {

        IRepositoryNodeConfiguration configuration = typeConfMap.get(type);
        if (configuration != null) {
            return configuration;
        }
        for (IRepositoryNodeConfiguration conf : configurations) {
            IRepositoryNodeResourceProvider resourceProvider = conf.getResourceProvider();
            if (resourceProvider != null) {
                if (resourceProvider.canHandleRepObjType(type)) {
                    typeConfMap.put(type, conf);

                    return conf;
                }
            }
        }
        return null;
    }

    private static void initTemplateDefine() {
        if (!inited) {
            IExtensionRegistry registry = Platform.getExtensionRegistry();

            //
            IExtensionPoint extensionPoint = registry.getExtensionPoint(RepositoryPlugin.PLUGIN_ID, EXTENSION_POINT_TEMPLATE);
            if (extensionPoint != null && extensionPoint.isValid()) {
                IExtension[] extensions = extensionPoint.getExtensions();
                for (IExtension s : extensions) {
                    IConfigurationElement[] elements = s.getConfigurationElements();
                    for (IConfigurationElement element : elements) {
                        if (element.getAttribute(PROP_CLASS) != null) {
                            try {
                                IRepositoryNodeConfiguration configuration = (IRepositoryNodeConfiguration) element
                                        .createExecutableExtension(PROP_CLASS);
                                configurations.add(configuration);

                            } catch (CoreException e) {
                                log.error(e.getMessage(), e);
                            }
                        }
                    }
                }
                // Bean2EObjUtil.getInstance().dumpMap();
            }
            inited = true;
        }
    }

    private static void initBeanMap() {
        if (!initedBean) {
            for (IRepositoryNodeConfiguration conf : configurations) {
                // init class structure
                Class wsObjectClass = conf.getContentProvider().getWSObjectClass();
                if (wsObjectClass != null) {
                    Bean2EObjUtil.getInstance().registerClassMap(wsObjectClass);
                }
            }
        }
        initedBean = true;
    }

}
