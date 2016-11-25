package travellog;

import java.time.LocalDateTime;

/**
 *
 * @author plourand
 */
public class LogInfo implements Comparable<LogInfo> {
    private LocalDateTime timestamp;
    private String gps;

    public LogInfo() {
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = LocalDateTime.parse(timestamp);
    }

    public String getGps() {
        return gps;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public int compareTo(LogInfo o) {
        if (LogInfo.class.isAssignableFrom(o.getClass())) {
            return timestamp.compareTo(o.timestamp);
        } else {
            return -1;
        }
    }
}
