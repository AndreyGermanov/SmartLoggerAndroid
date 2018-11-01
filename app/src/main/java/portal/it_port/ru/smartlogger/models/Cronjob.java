package portal.it_port.ru.smartlogger.models;

import java.util.HashMap;

/**
 * Model of Cronjob task
 */
public class Cronjob extends Model {

    // Is cronjob enabled
    private boolean enabled = false;
    // Current status of cronjob - IDLE or RUNNING
    private String status = "";
    // Internal Name of cronjob
    private String name = "";
    // Type of collection, to which cronjob belongs: "loggers","aggregators","persisters" etc.
    private String type = "";
    // Information record about last operation, performed on server by this cronjob
    // (actual information depends on cronjob type)
    private HashMap<String,Object> lastRecord = new HashMap<>();
    // Timestamp of last run of cronjob
    private long lastRunTimestamp = 0L;

    /**
     * Getters and setters of private fields
     */

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getStatus() {
        return status;
    }

    void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HashMap<String, Object> getLastRecord() {
        return lastRecord;
    }

    public void setLastRecord(HashMap<String, Object> lastRecord) {
        this.lastRecord = lastRecord;
    }

    public long getLastRunTimestamp() {
        return lastRunTimestamp;
    }

    void setLastRunTimestamp(long lastRunTimestamp) {
        this.lastRunTimestamp = lastRunTimestamp;
    }

    /**
     * Methods returns string representation of object
     * @return String representation of object
     */
    public String toString() {
        return this.getId()+"-"+this.getStatus();
    }

}
