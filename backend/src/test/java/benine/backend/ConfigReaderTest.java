package benine.backend;

import com.benine.backend.Config;
import com.benine.backend.ConfigReader;

import org.junit.Test;
import org.junit.Assert;

import java.io.File;

import static org.junit.Assert.fail;

/**
 * Unit test class used to test the default configReader class.
 * @author Dorian
 */
public class ConfigReaderTest {

    @Test
    public final void ConfigReaderTest() {
        Config cfg = new Config();
        cfg.addAttribute("Test", "1234");
        try {
            Config readConfig = ConfigReader.readConfig("resources"+ File.separator + "configs" + File.separator + "testconfig1.conf");
            Assert.assertEquals(cfg, readConfig);
        }catch(Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    // TODO tests for bad weather behaviour, multiple lines etc.

}
