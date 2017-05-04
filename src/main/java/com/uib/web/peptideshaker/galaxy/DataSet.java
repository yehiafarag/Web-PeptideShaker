
package com.uib.web.peptideshaker.galaxy;

import com.github.jmchilton.blend4j.galaxy.beans.Dataset;

/**
 *This class represents the Galaxy Dataset in the application
 * @author Yehia Farag
 */
public class DataSet{
    private String name;
    private String galaxyId;
    private String historyId;
    private String reIndexedId;
    private String reIndexedHistoryId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGalaxyId() {
        return galaxyId;
    }

    public void setGalaxyId(String galaxyId) {
        this.galaxyId = galaxyId;
    }

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    public String getReIndexedId() {
        return reIndexedId;
    }

    public void setReIndexedId(String reIndexedId) {
        this.reIndexedId = reIndexedId;
    }

    public String getReIndexedHistoryId() {
        return reIndexedHistoryId;
    }

    public void setReIndexedHistoryId(String reIndexedHistoryId) {
        this.reIndexedHistoryId = reIndexedHistoryId;
    }
    
}
