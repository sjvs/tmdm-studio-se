// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.2_01, construire R40)
// Generated source version: 1.1.2

package com.amalto.core.webservice;


public class WSTransformer {
    protected java.lang.String name;
    protected java.lang.String description;
    protected com.amalto.core.webservice.WSTransformerPluginSpec[] pluginSpecs;
    
    public WSTransformer() {
    }
    
    public WSTransformer(java.lang.String name, java.lang.String description, com.amalto.core.webservice.WSTransformerPluginSpec[] pluginSpecs) {
        this.name = name;
        this.description = description;
        this.pluginSpecs = pluginSpecs;
    }
    
    public java.lang.String getName() {
        return name;
    }
    
    public void setName(java.lang.String name) {
        this.name = name;
    }
    
    public java.lang.String getDescription() {
        return description;
    }
    
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    
    public com.amalto.core.webservice.WSTransformerPluginSpec[] getPluginSpecs() {
        return pluginSpecs;
    }
    
    public void setPluginSpecs(com.amalto.core.webservice.WSTransformerPluginSpec[] pluginSpecs) {
        this.pluginSpecs = pluginSpecs;
    }
}
