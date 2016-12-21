package travellog;

import com.beust.jcommander.ParameterException;
import java.io.IOException;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.springframework.core.io.ClassPathResource;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author lore
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class AppTest {
    
    
    @Test
    public void successProcessFiles() throws IOException {
        ClassPathResource imageDirectory = new ClassPathResource(".");
        
        TravelLogApp app = new TravelLogApp();
        app.setDirectory(imageDirectory.getFile().getPath());
        app.setReportFile("report.csv");
        app.run();

        List<LogInfo> logs = app.getLogs();
        assertEquals(3, logs.size());
        
    }
    
    @Test(expected = ParameterException.class)
    public void failDirectoryToProcessNotSuppliedß() {
        TravelLogApp.main(new String[]{"-o", "report.txt"});
    }
    
    @Test(expected = ParameterException.class)
    public void failNoOutputFileSupplied() {
        TravelLogApp.main(new String[]{"-d", "/var/tmp"});
    }
}
