package com.amalto.core.objects.transformers.v2.util;

import com.amalto.core.objects.transformers.v2.ejb.TransformerV2POJO;
import com.amalto.core.util.XtentisException;

public class TransformerPluginContext extends TransformerContext{
	
	private final static String GLOBAL_CONTEXT = "com.amalto.core.util.TransformerPluginContext.GLOBAL_CONTEXT";
	private final static String PLUGIN_NUMBER = "com.amalto.core.util.TransformerPluginContext.PLUGIN_HANDLE";
	private final static String PLUGINS_CALLBACK = "com.amalto.core.util.TransformerPluginContext.PLUGINS_CALLBACK";
	
	/**
	 * A Transformer Plugin Context should never be instantiated directly 
	 * but via the {@link TransformerGlobalContext#getTransformerPluginContext(int)}
	 * @param globalContext - The Transformer Global Context used to create this Plugin Context
	 */
	public TransformerPluginContext(TransformerGlobalContext globalContext, int pluginNumber) {
		super(globalContext.getTransformerV2POJOPK());
		this.put(GLOBAL_CONTEXT,globalContext);
		this.put(PLUGIN_NUMBER,new Integer(pluginNumber));
		this.setPipeline(globalContext.getPipeline());
		this.setProjectedPKs(globalContext.getProjectedPKs());
	}

	public TransformerGlobalContext getTransformerGlobalContext() {
		return (TransformerGlobalContext)this.get(GLOBAL_CONTEXT);
	}
	
	public int getPluginNumber() {
		return ((Integer)this.get(PLUGIN_NUMBER)).intValue();
	}

	public TransformerV2POJO getTransformerPOJO() throws XtentisException{
		return getTransformerGlobalContext().getTransformerPOJO();
	}
	
	
	public TransformerPluginCallBack getPluginCallBack() {
		return (TransformerPluginCallBack) get(PLUGINS_CALLBACK);
	}
	public void setPluginCallBack(TransformerPluginCallBack callback) {
		put(PLUGINS_CALLBACK,callback);
	}

	
}
