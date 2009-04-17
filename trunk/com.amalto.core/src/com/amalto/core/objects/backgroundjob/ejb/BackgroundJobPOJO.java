package com.amalto.core.objects.backgroundjob.ejb;

import java.io.IOException;
import java.util.HashMap;

import com.amalto.core.ejb.ObjectPOJO;
import com.amalto.core.ejb.ObjectPOJOPK;
import com.amalto.core.ejb.XtentisWSBean;
import com.amalto.core.objects.transformers.v2.util.TypedContent;
import com.amalto.core.util.Util;
import com.amalto.core.webservice.WSPipeline;



/**
 * @author bgrieder
 * Stores information on a job run in the server as a background thread
 * 
 */
public class BackgroundJobPOJO extends ObjectPOJO{
   

	public static int _COMPLETED_=0;
	public static int _RUNNING_=1;  
	public static int _SUSPENDED_=2;
	public static int _STOPPED_=3;
	public static int _CANCEL_REQUESTED_=4;
	public static int _SCHEDULED_=5;
	
	private String id;
    private String description;
    private int status;
    private String message;
    private int percentage;
    private String timestamp;
    private WSPipeline wsPipeline;
    private byte[] serializedObject;
    
    
    /**
     * 
     */
    public BackgroundJobPOJO() {
    	this.id = Util.generateGUID(this);
    }    
	public BackgroundJobPOJO(String id) {
		super();
		this.id = id;
	}
	

	
	
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getPercentage() {
		return percentage;
	}
	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timeStamp) {
		this.timestamp = timeStamp;
	}
	public byte[] getSerializedObject() {
		return serializedObject;
	}
	public void setSerializedObject(byte[] serializedObject) {
		this.serializedObject = serializedObject;
	}
	public WSPipeline getWsPipeline() {
    	return wsPipeline;
    }
	public void setWsPipeline(WSPipeline wsPipeline) {
    	this.wsPipeline = wsPipeline;
    }
	/**
	 * Sets the Pipeline on the Background Job.
	 * The pipeline is changed to a {@link WSPipeline} to be Marshalable
	 * Note: doing this will consume the streams of the pipeline
	 * @param pipeline
	 * @throws IOException 
	 */
	public void setPipeline(HashMap<String, TypedContent> pipeline) throws Exception {
    	this.wsPipeline = XtentisWSBean.POJO2WS(pipeline);
    }
	
	
	@Override
	public ObjectPOJOPK getPK() {
		if (getId()==null) return null;
		return new ObjectPOJOPK(new String[] {id});
	}

	

	
	

}
