package benine.backend;

import com.benine.backend.Config;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class used to test the Config class.
 * @author Dorian
 */
public class ConfigTest {

  @Test
  public final void testAddAtributeValue() {
    Config cfg = new Config();
    cfg.addAttribute("Username", "Test123");
    Assert.assertEquals("Test123", cfg.getValue("Username"));
  }

  @Test
  public final void testAddAtributeValueOverWrite() {
    Config cfg = new Config();
    cfg.addAttribute("Username", "Test123");
    cfg.addAttribute("Username", "Overwritten");
    Assert.assertEquals("Overwritten", cfg.getValue("Username"));
  }

  @Test
  public final void testEqualsEqual() {
    Config cfg1 = new Config();
    Config cfg2 = new Config();
    cfg1.addAttribute("Username", "Test123");
    cfg2.addAttribute("Username", "Test123");
    Assert.assertEquals(cfg1, cfg2);
  }

  @Test
  public final void testEqualsNonEqualAttribute() {
    Config cfg1 = new Config();
    Config cfg2 = new Config();
    cfg1.addAttribute("Name", "Test123");
    cfg2.addAttribute("Username", "Test123456");
    Assert.assertNotEquals(cfg1, cfg2);
  }

  @Test
  public final void testEqualsNonEqualAmountAttributes() {
    Config cfg1 = new Config();
    Config cfg2 = new Config();
    cfg1.addAttribute("Name", "Test123");
    cfg2.addAttribute("Name", "Test123");
    cfg1.addAttribute("Username", "Test123456");
    Assert.assertNotEquals(cfg1, cfg2);
  }
}
