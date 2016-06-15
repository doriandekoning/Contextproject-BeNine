package com.benine.backend.http;

import com.benine.backend.preset.PresetController;
import com.benine.backend.preset.autopresetcreation.PresetPyramidCreator;
import com.benine.backend.preset.autopresetcreation.SubView;
import org.eclipse.jetty.util.MultiMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;

import java.util.Collection;

import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 *
 */
public class AutoCreationSubViewsHandlerTest extends AutoPresetHandlerTest {

  @Test
  public void testHandleWithParams() throws Exception {
    setPath("/presets/autoCreateSubViews");
    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("rows", "1");
    parameters.add("columns", "1");
    parameters.add("levels", "1");
    setParameters(parameters);
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(out).write(matches("\\{\"SubViews\":\\[\\{\"bottomRight\":\\{\"x\":\\d*.\\d*,\"y\":\\d*.\\d*\\},\"topLeft\":\\{\"x\":\\d*.\\d*,\"y\":\\d*.\\d*\\}\\}\\]\\}"));
    verify(requestMock).setHandled(true);
  }


  @Test
  public void testHandleNoParams() throws Exception {
    setPath("/presets/autocreatesubviews");
    MultiMap<String> parameters = new MultiMap<>();
    setParameters(parameters);
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    Collection<SubView> subViews = new PresetPyramidCreator(3, 3, 3, 0.0, mock(PresetController.class)).generateSubViews();
    JSONObject jsonObj = new JSONObject();
    JSONArray jsonArr = new JSONArray();
    subViews.forEach(vs -> jsonArr.add(vs.toJSON()));
    jsonObj.put("SubViews", jsonArr);
    verify(out).write(jsonObj.toJSONString());
    verify(requestMock).setHandled(true);
  }

  @Override
  public AutoPresetHandler supplyHandler() {
    return new AutoCreationSubViewsHandler(httpserver);
  }
}
