 /*
 * Generated by XDoclet - Do not edit!
 * this class was prodiuced by xdoclet automagically...
 */
package com.amalto.core.objects.synchronization.ejb.remote;

import java.util.*;

/**
 * This class is remote adapter to SynchronizationItemCtrl. It provides convenient way to access
 * facade session bean. Inverit from this class to provide reasonable caching and event handling capabilities.
 *
 * Remote facade for SynchronizationItemCtrl.
 * @xdoclet-generated at 15-04-09
 * @copyright The XDoclet Team
 * @author XDoclet
 * @version ${version}
 */

public class SynchronizationItemCtrlRemote extends Observable
{
    static SynchronizationItemCtrlRemote _instance = null;
    public static SynchronizationItemCtrlRemote getInstance() {
        if(_instance == null) {
	   _instance = new SynchronizationItemCtrlRemote();
	}
	return _instance;
    }

  /**
   * cached remote session interface
   */
  com.amalto.core.objects.synchronization.ejb.remote.SynchronizationItemCtrl _session = null;
  /**
   * return session bean remote interface
   */
   protected com.amalto.core.objects.synchronization.ejb.remote.SynchronizationItemCtrl getSession() {
      try {
   	if(_session == null) {
	   _session = com.amalto.core.objects.synchronization.ejb.local.SynchronizationItemCtrlUtil.getHome().create();
	}
	return _session;
      } catch(Exception ex) {
        // just catch it here and return null.
        // somebody can provide better solution
	ex.printStackTrace();
	return null;
      }
   }

   public com.amalto.core.objects.synchronization.ejb.SynchronizationItemPOJOPK putSynchronizationItem ( com.amalto.core.objects.synchronization.ejb.SynchronizationItemPOJO synchronizationItem )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        com.amalto.core.objects.synchronization.ejb.SynchronizationItemPOJOPK retval;
       retval =  getSession().putSynchronizationItem( synchronizationItem );

      return retval;

   }

   public com.amalto.core.objects.synchronization.ejb.SynchronizationItemPOJO getSynchronizationItem ( com.amalto.core.objects.synchronization.ejb.SynchronizationItemPOJOPK pk )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        com.amalto.core.objects.synchronization.ejb.SynchronizationItemPOJO retval;
       retval =  getSession().getSynchronizationItem( pk );

      return retval;

   }

   public com.amalto.core.objects.synchronization.ejb.SynchronizationItemPOJO existsSynchronizationItem ( com.amalto.core.objects.synchronization.ejb.SynchronizationItemPOJOPK pk )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        com.amalto.core.objects.synchronization.ejb.SynchronizationItemPOJO retval;
       retval =  getSession().existsSynchronizationItem( pk );

      return retval;

   }

   public com.amalto.core.objects.synchronization.ejb.SynchronizationItemPOJOPK removeSynchronizationItem ( com.amalto.core.objects.synchronization.ejb.SynchronizationItemPOJOPK pk )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        com.amalto.core.objects.synchronization.ejb.SynchronizationItemPOJOPK retval;
       retval =  getSession().removeSynchronizationItem( pk );

      return retval;

   }

   public java.util.Collection getSynchronizationItemPKs ( java.lang.String regex )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        java.util.Collection retval;
       retval =  getSession().getSynchronizationItemPKs( regex );

      return retval;

   }

   public com.amalto.core.objects.synchronization.ejb.SynchronizationItemPOJO resolveSynchronization ( com.amalto.core.objects.synchronization.ejb.SynchronizationItemPOJOPK pk,java.lang.String resolvedProjection )
	  throws com.amalto.core.util.XtentisException, java.rmi.RemoteException
   {
        com.amalto.core.objects.synchronization.ejb.SynchronizationItemPOJO retval;
       retval =  getSession().resolveSynchronization( pk,resolvedProjection );

      return retval;

   }

  /**
   * override this method to provide feedback to interested objects
   * in case collections were changed.
   */
  public void invalidate() {

  	setChanged();
	notifyObservers();
  }
}
