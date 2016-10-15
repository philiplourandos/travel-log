package travellog;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.drew.lang.ByteArrayReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifReader;
import com.drew.tools.FileUtil;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author plourand
 */
public class TravelLogApp {

    private static final Logger LOG = LogManager.getLogger();
    
    @Parameter(names = {"--input-directory", "-d"}, description = "Directory to scan image files for", required = true)
    private String directory;
    
    @Parameter(names = {"--output", "-o"}, description = "Output path and file name of report", required = true)
    private String reportFile;

    public TravelLogApp() {
    }

    public void run() {
        Path inputDirectory = Paths.get(directory);
        
        try {
            Files.list(inputDirectory).forEach(f -> {
                LOG.info("Processing file: {}", f.toString());

                try {
                    Metadata meta = new Metadata();

                    new ExifReader().extract(new ByteArrayReader(FileUtil.readBytes(f.toFile())), meta);
                    
                    ExifIFD0Directory directory = meta.getFirstDirectoryOfType(ExifIFD0Directory.class);
                    
                } catch (IOException jpegReadFail) {
                    LOG.error("", jpegReadFail);
                }
            });
        } catch (IOException invalidDir) {
            LOG.error("Unable to list files in directory", invalidDir);
        }
    }
    
    public static void main(String[] args) {
        TravelLogApp app = new TravelLogApp();

        new JCommander(app, args);
        app.run();
    }
}
