// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.2_01, construire R40)
// Generated source version: 1.1.2

package com.amalto.core.webservice;


public class WSSingleSearch {
    protected com.amalto.core.webservice.WSDataClusterPK wsDataClusterPK;
    protected com.amalto.core.webservice.WSViewPK wsViewPK;
    protected com.amalto.core.webservice.WSWhereItem whereItem;
    protected int spellTreshold;
    protected int skip;
    protected int maxItems;
    
    public WSSingleSearch() {
    }
    
    public WSSingleSearch(com.amalto.core.webservice.WSDataClusterPK wsDataClusterPK, com.amalto.core.webservice.WSViewPK wsViewPK, com.amalto.core.webservice.WSWhereItem whereItem, int spellTreshold, int skip, int maxItems) {
        this.wsDataClusterPK = wsDataClusterPK;
        this.wsViewPK = wsViewPK;
        this.whereItem = whereItem;
        this.spellTreshold = spellTreshold;
        this.skip = skip;
        this.maxItems = maxItems;
    }
    
    public com.amalto.core.webservice.WSDataClusterPK getWsDataClusterPK() {
        return wsDataClusterPK;
    }
    
    public void setWsDataClusterPK(com.amalto.core.webservice.WSDataClusterPK wsDataClusterPK) {
        this.wsDataClusterPK = wsDataClusterPK;
    }
    
    public com.amalto.core.webservice.WSViewPK getWsViewPK() {
        return wsViewPK;
    }
    
    public void setWsViewPK(com.amalto.core.webservice.WSViewPK wsViewPK) {
        this.wsViewPK = wsViewPK;
    }
    
    public com.amalto.core.webservice.WSWhereItem getWhereItem() {
        return whereItem;
    }
    
    public void setWhereItem(com.amalto.core.webservice.WSWhereItem whereItem) {
        this.whereItem = whereItem;
    }
    
    public int getSpellTreshold() {
        return spellTreshold;
    }
    
    public void setSpellTreshold(int spellTreshold) {
        this.spellTreshold = spellTreshold;
    }
    
    public int getSkip() {
        return skip;
    }
    
    public void setSkip(int skip) {
        this.skip = skip;
    }
    
    public int getMaxItems() {
        return maxItems;
    }
    
    public void setMaxItems(int maxItems) {
        this.maxItems = maxItems;
    }
}
