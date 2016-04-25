package benine.backend;

import com.benine.backend.Config;

import org.junit.Test;
import org.junit.Assert;

/**
 * Test class used to test the Config class.
 * @author Dorian
 */
public class ConfigTest {

    @Test
    public final void TestAddAtributeValue() {
        Config cfg = new Config();
        cfg.addAttribute("Username", "Test123");
        Assert.assertEquals("Test123", cfg.getValue("Username"));
    }

    @Test
    public final void TestAddAtributeValueOverWrite() {
        Config cfg = new Config();
        cfg.addAttribute("Username", "Test123");
        cfg.addAttribute("Username", "Overwritten");
        Assert.assertEquals("Overwritten", cfg.getValue("Username"));
    }

    //TODO test for equals method

}
