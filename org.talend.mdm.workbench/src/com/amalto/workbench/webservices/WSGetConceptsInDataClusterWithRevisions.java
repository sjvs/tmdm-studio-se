// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation （1.1.2_01，编译版 R40）
// Generated source version: 1.1.2

package com.amalto.workbench.webservices;


public class WSGetConceptsInDataClusterWithRevisions {
    protected com.amalto.workbench.webservices.WSDataClusterPK dataClusterPOJOPK;
    protected com.amalto.workbench.webservices.WSUniversePK universePK;
    
    public WSGetConceptsInDataClusterWithRevisions() {
    }
    
    public WSGetConceptsInDataClusterWithRevisions(com.amalto.workbench.webservices.WSDataClusterPK dataClusterPOJOPK, com.amalto.workbench.webservices.WSUniversePK universePK) {
        this.dataClusterPOJOPK = dataClusterPOJOPK;
        this.universePK = universePK;
    }
    
    public com.amalto.workbench.webservices.WSDataClusterPK getDataClusterPOJOPK() {
        return dataClusterPOJOPK;
    }
    
    public void setDataClusterPOJOPK(com.amalto.workbench.webservices.WSDataClusterPK dataClusterPOJOPK) {
        this.dataClusterPOJOPK = dataClusterPOJOPK;
    }
    
    public com.amalto.workbench.webservices.WSUniversePK getUniversePK() {
        return universePK;
    }
    
    public void setUniversePK(com.amalto.workbench.webservices.WSUniversePK universePK) {
        this.universePK = universePK;
    }
}