package com.benine.backend.http;

import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.benine.backend.camera.CameraController;

import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class PresetCreationHandlerTest {

  @Test
  public void testResponseMessageTrue() {
    CameraController camera = mock(CameraController.class);
    PresetCreationHandler handler = new PresetCreationHandler(camera);
    String response = "{\"succes\":\"true\"}"; 
    String actual= handler.responseMessage(true);
    Assert.assertTrue(actual.equals(response));
  }
  
  @Test
  public void testResponseMessageFalse() {
    CameraController camera = mock(CameraController.class);
    PresetCreationHandler handler = new PresetCreationHandler(camera);
    String response = "{\"succes\":\"false\"}"; 
    String actual= handler.responseMessage(false);
    Assert.assertTrue(actual.equals(response));
  }
    

}
