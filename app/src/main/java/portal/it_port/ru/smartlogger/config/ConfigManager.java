package portal.it_port.ru.smartlogger.config;

/**
 * Created by Andrey Germanov on 10/28/18.
 */
public class ConfigManager {

    private String host = "portal.it-port.ru";
    private int port = 8080;
    private String scheme = "http";
    private int pollPeriod = 5000;

    private static ConfigManager instance;

    public static ConfigManager getInstance() {
        if (instance==null) instance = new ConfigManager();
        return instance;
    }

    private ConfigManager() {
        super();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public int getPollPeriod() {
        return pollPeriod;
    }

    public void setPollPeriod(int pollPeriod) {
        this.pollPeriod = pollPeriod;
    }
}