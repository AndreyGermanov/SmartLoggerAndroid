package portal.it_port.ru.smartlogger.models;

/**
 * Created by Andrey Germanov on 10/28/18.
 */
public class Cronjob extends Model {

    private String id = "";
    private boolean enabled = false;
    private String status = "";
    private String name = "";
    private String type = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
}
