// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.mdm.engines.client.ui.wizards;

import java.util.ArrayList;
import java.util.List;

import org.talend.repository.documentation.ExportFileResource;

/**
 * The Job deployment information <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public class JobDeploymentInfo {

    private String jobLabelName;

    private String jobVersion;

    private String jobPath;

    private String jobPurposeDescription;

    private String descValue;
    
    private List<ExportFileResource> resourceList = new ArrayList<ExportFileResource>();
    

    /**
     * Getter for jobLabelName.
     * 
     * @return the jobLabelName
     */
    public String getJobLabelName() {
        return this.jobLabelName;
    }

    /**
     * Sets the jobLabelName.
     * 
     * @param jobLabelName the jobLabelName to set
     */
    public void setJobLabelName(String jobLabelName) {
        this.jobLabelName = jobLabelName;
    }

    /**
     * Getter for jobVersion.
     * 
     * @return the jobVersion
     */
    public String getJobVersion() {
        return this.jobVersion;
    }

    /**
     * Sets the jobVersion.
     * 
     * @param jobVersion the jobVersion to set
     */
    public void setJobVersion(String jobVersion) {
        this.jobVersion = jobVersion;
    }

    /**
     * Getter for jobPath.
     * 
     * @return the jobPath
     */
    public String getJobPath() {
        return this.jobPath;
    }

    /**
     * Sets the jobPath.
     * 
     * @param jobPath the jobPath to set
     */
    public void setJobPath(String jobPath) {
        this.jobPath = jobPath;
    }

    /**
     * Getter for jobPurposeDescription.
     * 
     * @return the jobPurposeDescription
     */
    public String getJobPurposeDescription() {
        return this.jobPurposeDescription;
    }

    /**
     * Sets the jobPurposeDescription.
     * 
     * @param jobPurposeDescription the jobPurposeDescription to set
     */
    public void setJobPurposeDescription(String jobPurposeDescription) {
        this.jobPurposeDescription = jobPurposeDescription;
    }

    /**
     * Getter for descValue.
     * 
     * @return the descValue
     */
    public String getDescValue() {
        return this.descValue;
    }

    /**
     * Sets the descValue.
     * 
     * @param descValue the descValue to set
     */
    public void setDescValue(String descValue) {
        this.descValue = descValue;
    }

    
    /**
     * Getter for resourceList.
     * @return the resourceList
     */
    public List<ExportFileResource> getResourceList() {
        return this.resourceList;
    }

}
