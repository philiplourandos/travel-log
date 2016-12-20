package travellog;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.drew.lang.ByteArrayReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifReader;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.tools.FileUtil;
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
                LOG.info("Processing file: {}", f.toString());

                try {
                    Metadata meta = new Metadata();

                    new ExifReader().extract(new ByteArrayReader(FileUtil.readBytes(f.toFile())), meta);
                    
                    ExifIFD0Directory metaDir = meta.getFirstDirectoryOfType(ExifIFD0Directory.class);
                    final String datetime = metaDir.getString(ExifDirectoryBase.TAG_DATETIME);

                    GpsDirectory gpsMetaDir = meta.getFirstDirectoryOfType(GpsDirectory.class);
                    final String latitude = gpsMetaDir.getString(GpsDirectory.TAG_DEST_LATITUDE);
                    final String longitude = gpsMetaDir.getString(GpsDirectory.TAG_DEST_LONGITUDE);

                    LOG.info("Pic taken: {}, Longitude: {}, Latitude: {}", datetime, longitude, latitude);
                    
                    logs.add(new LogInfo(datetime, longitude, latitude, f.getFileName().toString()));
                } catch (IOException jpegReadFail) {
                    LOG.error("", jpegReadFail);
                }
            });
        } catch (IOException invalidDir) {
            LOG.error("Unable to list files in directory", invalidDir);
        }
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
