// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.2_01, construire R40)
// Generated source version: 1.1.2

package com.amalto.core.webservice;


public class WSGetFullPathValues {
    protected com.amalto.core.webservice.WSDataClusterPK wsDataClusterPK;
    protected java.lang.String fullPath;
    protected com.amalto.core.webservice.WSWhereItem whereItem;
    protected int spellThreshold;
    protected java.lang.String orderBy;
    protected java.lang.String direction;
    
    public WSGetFullPathValues() {
    }
    
    public WSGetFullPathValues(com.amalto.core.webservice.WSDataClusterPK wsDataClusterPK, java.lang.String fullPath, com.amalto.core.webservice.WSWhereItem whereItem, int spellThreshold, java.lang.String orderBy, java.lang.String direction) {
        this.wsDataClusterPK = wsDataClusterPK;
        this.fullPath = fullPath;
        this.whereItem = whereItem;
        this.spellThreshold = spellThreshold;
        this.orderBy = orderBy;
        this.direction = direction;
    }
    
    public com.amalto.core.webservice.WSDataClusterPK getWsDataClusterPK() {
        return wsDataClusterPK;
    }
    
    public void setWsDataClusterPK(com.amalto.core.webservice.WSDataClusterPK wsDataClusterPK) {
        this.wsDataClusterPK = wsDataClusterPK;
    }
    
    public java.lang.String getFullPath() {
        return fullPath;
    }
    
    public void setFullPath(java.lang.String fullPath) {
        this.fullPath = fullPath;
    }
    
    public com.amalto.core.webservice.WSWhereItem getWhereItem() {
        return whereItem;
    }
    
    public void setWhereItem(com.amalto.core.webservice.WSWhereItem whereItem) {
        this.whereItem = whereItem;
    }
    
    public int getSpellThreshold() {
        return spellThreshold;
    }
    
    public void setSpellThreshold(int spellThreshold) {
        this.spellThreshold = spellThreshold;
    }
    
    public java.lang.String getOrderBy() {
        return orderBy;
    }
    
    public void setOrderBy(java.lang.String orderBy) {
        this.orderBy = orderBy;
    }
    
    public java.lang.String getDirection() {
        return direction;
    }
    
    public void setDirection(java.lang.String direction) {
        this.direction = direction;
    }
}
