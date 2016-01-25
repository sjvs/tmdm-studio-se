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
package org.talend.mdm.repository.core.service.interactive;

import java.util.List;

import javax.xml.ws.WebServiceException;

import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.mdm.repository.core.command.deploy.AbstractDeployCommand;
import org.talend.mdm.repository.core.service.IInteractiveHandler;
import org.talend.mdm.repository.core.service.RepositoryWebServiceAdapter;
import org.talend.mdm.repository.model.mdmmetadata.MDMServerDef;
import org.talend.mdm.repository.model.mdmproperties.MDMServerObjectItem;
import org.talend.mdm.repository.model.mdmserverobject.MDMServerObject;
import org.talend.mdm.repository.utils.Bean2EObjUtil;

import com.amalto.workbench.utils.XtentisException;
import com.amalto.workbench.webservices.XtentisPort;

/**
 * DOC hbhong class global comment. Detailled comment
 */
public abstract class AbstractInteractiveHandler implements IInteractiveHandler {

    protected XtentisPort getPort(MDMServerDef serverDef) throws XtentisException {
        return RepositoryWebServiceAdapter.getXtentisPort(serverDef);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.mdm.repository.core.service.IInteractiveHandler#convert(org.talend.core.model.properties.Item,
     * org.talend.mdm.repository.model.mdmserverobject.MDMServerObject)
     */
    public Object convert(Item item, MDMServerObject serverObj) {
        Object wsObj = Bean2EObjUtil.getInstance().convertFromEObj2Bean(serverObj);
        return wsObj;
    }

    public boolean doDeployWSObject(XtentisPort port, Object wsObj) {
        return false;
        // do nothing
    }

    public boolean doRemove(XtentisPort port, AbstractDeployCommand cmd) throws WebServiceException, XtentisException {
        return true;
        // do nothing
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.mdm.repository.core.service.IInteractiveHandler#deploy(com.amalto.workbench.webservices.XtentisPort,
     * org.talend.core.model.properties.Item, org.talend.mdm.repository.model.mdmserverobject.MDMServerObject)
     */
    public boolean deploy(AbstractDeployCommand cmd) throws XtentisException {
        IRepositoryViewObject viewObj = cmd.getViewObject();
        Item item = viewObj.getProperty().getItem();
        MDMServerObject serverObject = ((MDMServerObjectItem) item).getMDMServerObject();
        Object wsObj = convert(item, serverObject);
        XtentisPort port = getPort(cmd.getServerDef());
        return doDeployWSObject(port, wsObj);
    }

    public boolean remove(AbstractDeployCommand cmd) throws XtentisException {
        XtentisPort port = getPort(cmd.getServerDef());
        return doRemove(port, cmd);
    }

    public void assertPropertyIsInited(Item item) {
    }

    public List<IRepositoryViewObject> getAssociatedObjects(IRepositoryViewObject obj) {
        return null;
    }

    public boolean isShownInResultDialog(IRepositoryViewObject viewObj) {
        return true;
    }

}
