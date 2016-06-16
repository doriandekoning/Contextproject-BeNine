package com.benine.backend.camera.ipcameracontrol;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

/**
 * Test the focus value class.
 */
public class FocusValueTest {
  
  FocusValue focus;
  
  @Before
  public void setup() {
    focus = new FocusValue(1, true);
  }
  
  @Test
  public void testGetFocusPosition() {
    Assert.assertEquals(1, focus.getFocus());
  }
  
  @Test
  public void testGetAutoFocus() {
    Assert.assertEquals(true, focus.isAutofocus());
  }
  
  @Test
  public void testSetAutoFocus() {
    focus.setAutofocus(false);
    Assert.assertEquals(false, focus.isAutofocus());
  }
  
  @Test
  public void testSetFocus() {
    focus.setFocus(5);
    Assert.assertEquals(5, focus.getFocus());
  }
  
  @Test
  public void testNotEqualsNull() {
    Assert.assertNotEquals(null, focus);
  }
  
  @Test
  public void testNotEqualsAutoFocus() {
    FocusValue focus2 = new FocusValue(1, false);
    Assert.assertNotEquals(focus2, focus);
  }
  
  @Test
  public void testNotEqualsFocusPosition() {
    FocusValue focus2 = new FocusValue(2, true);
    Assert.assertNotEquals(focus2, focus);
  }
  
  @Test
  public void testNotEqualsOne() {
    Assert.assertNotEquals(1, focus);
  }
  
  @Test
  public void testEqualsSelf() {
    Assert.assertEquals(focus, focus);
  }
  
  @Test
  public void testEqualsOther() {
    FocusValue focus2 = new FocusValue(1, true);
    Assert.assertEquals(focus2, focus);
  }
  
  @Test
  public void testEqualsHashCode() {
    FocusValue focus2 = new FocusValue(1, true);
    Assert.assertEquals(focus2.hashCode(), focus.hashCode());
  }
}
