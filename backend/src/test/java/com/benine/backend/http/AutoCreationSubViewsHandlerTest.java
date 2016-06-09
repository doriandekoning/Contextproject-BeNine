package com.benine.backend.http;//TODO add Javadoc comment

import com.benine.backend.preset.PresetController;
import com.benine.backend.preset.autopresetcreation.PresetPyramidCreator;
import com.benine.backend.preset.autopresetcreation.SubView;
import org.eclipse.jetty.util.MultiMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;

import java.util.Collection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 *
 */
public class AutoCreationSubViewsHandlerTest extends RequestHandlerTest {

  @Test
  public void testHandleWithParams() throws Exception {
    setPath("/presets/autoCreateSubViews");
    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("rows", "1");
    parameters.add("columns", "1");
    parameters.add("levels", "1");
    setParameters(parameters);
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(out).write("{\"SubViews\":[SubView{topLeft=(0.0,100.0), bottomRight=(100.0,0.0)}]}");
    verify(requestMock).setHandled(true);
  }


  @Test
  public void testHandleNoParams() throws Exception {
    setPath("/presets/autoCreateSubViews");
    MultiMap<String> parameters = new MultiMap<>();
    setParameters(parameters);
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    Collection<SubView> subViews = new PresetPyramidCreator(3, 3, 3, 0.0, mock(PresetController.class)).generateSubViews();
    JSONObject jsonObj = new JSONObject();
    JSONArray jsonArr = new JSONArray();
    jsonArr.addAll(subViews);
    jsonObj.put("SubViews", jsonArr);
    verify(out).write(jsonObj.toJSONString());
    verify(requestMock).setHandled(true);
  }

  @Override
  public RequestHandler supplyHandler() {
    return new AutoCreationSubViewsHandler(httpserver);
  }
}
