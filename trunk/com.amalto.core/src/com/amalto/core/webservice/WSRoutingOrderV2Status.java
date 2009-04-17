// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.2_01, construire R40)
// Generated source version: 1.1.2

package com.amalto.core.webservice;


import java.util.Map;
import java.util.HashMap;

public class WSRoutingOrderV2Status {
    private java.lang.String value;
    private static Map valueMap = new HashMap();
    public static final String _ACTIVEString = "ACTIVE";
    public static final String _FAILEDString = "FAILED";
    public static final String _COMPLETEDString = "COMPLETED";
    
    public static final java.lang.String _ACTIVE = new java.lang.String(_ACTIVEString);
    public static final java.lang.String _FAILED = new java.lang.String(_FAILEDString);
    public static final java.lang.String _COMPLETED = new java.lang.String(_COMPLETEDString);
    
    public static final WSRoutingOrderV2Status ACTIVE = new WSRoutingOrderV2Status(_ACTIVE);
    public static final WSRoutingOrderV2Status FAILED = new WSRoutingOrderV2Status(_FAILED);
    public static final WSRoutingOrderV2Status COMPLETED = new WSRoutingOrderV2Status(_COMPLETED);
    
    protected WSRoutingOrderV2Status(java.lang.String value) {
        this.value = value;
        valueMap.put(this.toString(), this);
    }
    
    public java.lang.String getValue() {
        return value;
    }
    
    public static WSRoutingOrderV2Status fromValue(java.lang.String value)
        throws java.lang.IllegalStateException {
        if (ACTIVE.value.equals(value)) {
            return ACTIVE;
        } else if (FAILED.value.equals(value)) {
            return FAILED;
        } else if (COMPLETED.value.equals(value)) {
            return COMPLETED;
        }
        throw new IllegalArgumentException();
    }
    
    public static WSRoutingOrderV2Status fromString(String value)
        throws java.lang.IllegalStateException {
        WSRoutingOrderV2Status ret = (WSRoutingOrderV2Status)valueMap.get(value);
        if (ret != null) {
            return ret;
        }
        if (value.equals(_ACTIVEString)) {
            return ACTIVE;
        } else if (value.equals(_FAILEDString)) {
            return FAILED;
        } else if (value.equals(_COMPLETEDString)) {
            return COMPLETED;
        }
        throw new IllegalArgumentException();
    }
    
    public String toString() {
        return value.toString();
    }
    
    private Object readResolve()
        throws java.io.ObjectStreamException {
        return fromValue(getValue());
    }
    
    public boolean equals(Object obj) {
        if (!(obj instanceof WSRoutingOrderV2Status)) {
            return false;
        }
        return ((WSRoutingOrderV2Status)obj).value.equals(value);
    }
    
    public int hashCode() {
        return value.hashCode();
    }
}
