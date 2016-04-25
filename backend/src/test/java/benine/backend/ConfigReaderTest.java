package benine.backend;

import com.benine.backend.Config;
import com.benine.backend.ConfigReader;
import com.benine.backend.ConfigReader.InvalidConfigFileException;

import org.junit.Test;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.fail;

/**
 * Unit test class used to test the default configReader class.
 * @author Dorian
 */
public class ConfigReaderTest {

  @Test
  public final void testConfigReader() {
    Config cfg = new Config();
    cfg.addAttribute("Test", "1234");
    try {
      Config readConfig = ConfigReader.readConfig("resources" + File.separator + "configs" + File.separator + "testconfig1.conf");
      Assert.assertEquals(cfg, readConfig);
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public final void testConfigReaderOnlyComment() {
    Config cfg = new Config();
    String fileLoc = "resources" + File.separator + "configs" + File.separator + "testconfig2.conf";
    try {
      Config readConfig = ConfigReader.readConfig(fileLoc);
      Assert.assertEquals(cfg, readConfig);
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }
  @Test(expected=Exception.class)
  public final void testConfigReaderBadFormatted()  throws Exception {
    String fileLoc =  "resources" + File.separator + "configs" + File.separator + "testconfig3.conf";
    ConfigReader.readConfig(fileLoc);
  }
  @Test(expected=IOException.class)
  public final void testThrowExceptionFileNonExistentFile() throws Exception {
    ConfigReader.readConfig("there/does/not/exist");
  }
}
