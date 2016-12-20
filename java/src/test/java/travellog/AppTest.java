package travellog;

import com.beust.jcommander.ParameterException;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author lore
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class AppTest {
    
    
    @Test
    public void successProcessFiles() throws IOException {
        ClassPathResource imageDirectory = new ClassPathResource(".");
        
        TravelLogApp.main(new String[]{"-o", "", "-d" , imageDirectory.getFile().getPath()});
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
