package travellog;

/**
 *
 * @author plourand
 */
public class LogInfo {
    private String timestamp;
    private String gps;

    public LogInfo() {
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getGps() {
        return gps;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
