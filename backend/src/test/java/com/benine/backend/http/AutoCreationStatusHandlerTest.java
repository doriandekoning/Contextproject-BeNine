package com.benine.backend.http;

import com.benine.backend.preset.PresetController;
import com.benine.backend.preset.autopresetcreation.PresetPyramidCreator;
import com.benine.backend.preset.autopresetcreation.SubView;
import org.eclipse.jetty.util.MultiMap;
import org.junit.Test;

import java.util.Collection;

import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 *
 */
public class AutoCreationStatusHandlerTest extends AutoPresetHandlerTest {

  @Test
  public void testHandleWithParams() throws Exception {
    setPath("/presets/autoCreateStatus");
    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("rows", "1");
    parameters.add("columns", "1");
    parameters.add("levels", "1");
    parameters.add("camera", "1");
    setParameters(parameters);
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(out).write(matches("\\{\"amount_total\":1,\"amount_created\":\"0\"}"));
    verify(requestMock).setHandled(true);
  }


  @Test
  public void testHandleNoParams() throws Exception {
    setPath("/presets/autocreatestatus");
    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("camera", "1");
    setParameters(parameters);
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    Collection<SubView> subViews = new PresetPyramidCreator(3, 3, 3, 0.0, mock(PresetController.class)).generateSubViews();

    verify(out).write("\\{\"amount_total\":1,\"amount_created\":\"0\"}");
    verify(requestMock).setHandled(true);
  }

  @Override
  public AutoPresetHandler supplyHandler() {
    return new AutoPresetCreationStatusHandler(httpserver);
  }
}
