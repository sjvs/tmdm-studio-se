// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.2_01, construire R40)
// Generated source version: 1.1.2

package com.amalto.core.webservice;


public class WSGetBusinessConceptValue {
    protected com.amalto.core.webservice.WSDataClusterPK wsDataClusterPK;
    protected com.amalto.core.webservice.WSBusinessConceptPK wsBusinessConceptPK;
    
    public WSGetBusinessConceptValue() {
    }
    
    public WSGetBusinessConceptValue(com.amalto.core.webservice.WSDataClusterPK wsDataClusterPK, com.amalto.core.webservice.WSBusinessConceptPK wsBusinessConceptPK) {
        this.wsDataClusterPK = wsDataClusterPK;
        this.wsBusinessConceptPK = wsBusinessConceptPK;
    }
    
    public com.amalto.core.webservice.WSDataClusterPK getWsDataClusterPK() {
        return wsDataClusterPK;
    }
    
    public void setWsDataClusterPK(com.amalto.core.webservice.WSDataClusterPK wsDataClusterPK) {
        this.wsDataClusterPK = wsDataClusterPK;
    }
    
    public com.amalto.core.webservice.WSBusinessConceptPK getWsBusinessConceptPK() {
        return wsBusinessConceptPK;
    }
    
    public void setWsBusinessConceptPK(com.amalto.core.webservice.WSBusinessConceptPK wsBusinessConceptPK) {
        this.wsBusinessConceptPK = wsBusinessConceptPK;
    }
}
