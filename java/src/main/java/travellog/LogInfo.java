package travellog;

import java.time.LocalDateTime;

/**
 *
 * @author plourand
 */
public class LogInfo implements Comparable<LogInfo> {
    private final LocalDateTime timestamp;
    private final String longitude;
    private final String latitude;
    private final String filename;

    public LogInfo(final String timestamp, final String longitude, final String latitude, final String filename) {
        this.timestamp = LocalDateTime.parse(timestamp);
        this.longitude = longitude;
        this.latitude = latitude;
        this.filename = filename;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getFilename() {
        return filename;
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
