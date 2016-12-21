package travellog;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author plourand
 */
public class LogInfo implements Comparable<LogInfo> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");

    private final LocalDateTime timestamp;
    private final double longitude;
    private final double latitude;
    private final String filename;

    public LogInfo(final String timestamp, final double longitude, final double latitude, final String filename) {
        this.timestamp = LocalDateTime.parse(timestamp, DATE_FORMATTER);
        this.longitude = longitude;
        this.latitude = latitude;
        this.filename = filename;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getFilename() {
        return filename;
    }

    @Override
    public int compareTo(LogInfo o) {
        return timestamp.compareTo(o.timestamp);
    }
}
