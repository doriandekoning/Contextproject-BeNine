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

    @Test
    public final void TestEqualsEqual() {
        Config cfg1 = new Config();
        Config cfg2 = new Config();
        cfg1.addAttribute("Username", "Test123");
        cfg2.addAttribute("Username", "Test123");
        Assert.assertEquals(cfg1, cfg2);
    }

    @Test
    public final void TestEqualsNonEqualAttribute(){
        Config cfg1 = new Config();
        Config cfg2 = new Config();
        cfg1.addAttribute("Name", "Test123");
        cfg2.addAttribute("Username", "Test123456");
        Assert.assertNotEquals(cfg1, cfg2);
    }

    @Test
    public final void TestEqualsNonEqualAmountAttributes() {
        Config cfg1 = new Config();
        Config cfg2 = new Config();
        cfg1.addAttribute("Name", "Test123");
        cfg2.addAttribute("Name", "Test123");
        cfg1.addAttribute("Username", "Test123456");
        Assert.assertNotEquals(cfg1, cfg2);
    }
}
