package travellog;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.GpsDirectory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author plourand
 */
public class TravelLogApp {

    private static final Logger LOG = LogManager.getLogger();
    
    private static final Pattern IMAGES_PATTERN = Pattern.compile("\\.(png|jpg|jpeg)", Pattern.CASE_INSENSITIVE);
    
    @Parameter(names = {"--input-directory", "-d"}, description = "Directory to scan image files for", required = true)
    private String directory;
    
    @Parameter(names = {"--output", "-o"}, description = "Output path and file name of report", required = true)
    private String reportFile;

    private List<LogInfo> logs;
    
    public TravelLogApp() {
    }

    public void run() {
        Path inputDirectory = Paths.get(directory);

        logs = new ArrayList<>();

        Predicate<Path> fileFilter = (p) -> {
            final String filename = p.getFileName().toString();
            
            Matcher match = IMAGES_PATTERN.matcher(filename);
            
            return match.find();
        };
        
        try {
            Files.list(inputDirectory).filter(fileFilter).forEach(f -> {
                try {
                    Metadata meta = ImageMetadataReader.readMetadata(f.toFile());

                    ExifIFD0Directory metaDir = meta.getFirstDirectoryOfType(ExifIFD0Directory.class);
                    final String datetime = metaDir.getString(ExifDirectoryBase.TAG_DATETIME);

                    GpsDirectory gpsMetaDir = meta.getFirstDirectoryOfType(GpsDirectory.class);
                    GeoLocation location = gpsMetaDir.getGeoLocation();
                    final double latitude = location.getLatitude();
                    final double longitude = location.getLongitude();
                    

                    logs.add(new LogInfo(datetime, longitude, latitude, f.toString()));
                } catch (IOException | ImageProcessingException jpegReadFail) {
                    LOG.error("", jpegReadFail);
                }
            });
            
            logs.stream().sorted(LogInfo::compareTo).forEach(l -> LOG.info("Pic taken: {}, Longitude: {}, Latitude: {}, file: {}", 
                    l.getTimestamp(), l.getLongitude(), l.getLatitude(), l.getFilename()));
        } catch (IOException invalidDir) {
            LOG.error("Unable to list files in directory", invalidDir);
        }
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public void setReportFile(String reportFile) {
        this.reportFile = reportFile;
    }
   
    public List<LogInfo> getLogs() {
        return logs;
    }
           
    public static void main(String[] args) {
        TravelLogApp app = new TravelLogApp();

        new JCommander(app, args);
        app.run();
    }
}
