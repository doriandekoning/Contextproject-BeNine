package com.benine.backend.http;

import com.benine.backend.PresetController;
import com.benine.backend.ServerController;
import com.benine.backend.camera.CameraController;
import com.sun.corba.se.spi.activation.Server;
import org.eclipse.jetty.util.MultiMap;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Created by dorian on 25-5-16.
 */
public class AddTagHandlerTest extends RequestHandlerTest {

  private PresetController presetController = mock(PresetController.class);

  @Override
  public RequestHandler supplyHandler() {
    return new AddTagHandler();
  }

  @Before
  public void initialize() throws IOException{
    super.initialize();
    ServerController.getInstance().setPresetController(presetController);
  }

  @Test
  public void testAddTag() throws Exception{
    setPath("/presets/addtag?name=tag1");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("name", "tag1");
    setParameters(parameters);

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(presetController).addTag("tag1");
    verify(requestMock).setHandled(true);

  }

  @Test
  public void testAddTagNoName() throws Exception{
    setPath("/presets/addtag");

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verifyZeroInteractions(presetController);

  }
}
