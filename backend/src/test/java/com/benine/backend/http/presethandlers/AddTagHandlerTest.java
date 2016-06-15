package com.benine.backend.http.presethandlers;

import com.benine.backend.camera.CameraBusyException;

import org.eclipse.jetty.util.MultiMap;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Created by BeNine on 25-5-16.
 */
public class AddTagHandlerTest extends PresetRequestHandlerTest {

  @Override
  public PresetRequestHandler supplyHandler() {
    return new AddTagHandler(httpserver);
  }

  @Before
  public void initialize() throws IOException, JSONException, CameraBusyException {
    super.initialize();
  }

  @Test
  public void testAddTag() throws Exception{
    setPath("/presets/addtag");

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
