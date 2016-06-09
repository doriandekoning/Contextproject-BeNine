package com.benine.backend.http;//TODO add Javadoc comment

import com.benine.backend.preset.PresetController;
import com.benine.backend.preset.autopresetcreation.PresetPyramidCreator;
import com.benine.backend.preset.autopresetcreation.SubView;
import com.sun.org.apache.xml.internal.utils.SuballocatedByteVector;
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

    verify(out).write("{\"SubViews\":[" +  new SubView(0, 100, 100, 0).toJSON().toJSONString()+"]}");
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
