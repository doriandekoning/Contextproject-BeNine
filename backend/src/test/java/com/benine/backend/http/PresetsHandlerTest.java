package com.benine.backend.http;

import com.benine.backend.camera.CameraBusyException;
import com.benine.backend.camera.ZoomPosition;
import com.benine.backend.camera.ipcameracontrol.FocusValue;
import com.benine.backend.camera.ipcameracontrol.IrisValue;
import com.benine.backend.preset.IPCameraPreset;
import com.benine.backend.preset.Preset;
import org.eclipse.jetty.util.MultiMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

public class PresetsHandlerTest extends RequestHandlerTest {

  private CreatePresetHandler createHandler;

  private RecallPresetHandler recallHandler;
  
  private JSONObject jsonObject;

  @Override
  public RequestHandler supplyHandler() {
    return new PresetsHandler(httpserver);
  }

  @Before
  public void initialize() throws IOException, CameraBusyException {
    super.initialize();

    createHandler = mock(CreatePresetHandler.class);
    recallHandler = mock(RecallPresetHandler.class);
    ((PresetsHandler) getHandler()).addHandler("createpreset", createHandler);
    ((PresetsHandler) getHandler()).addHandler("recallpreset", recallHandler);

    Preset preset = new IPCameraPreset(new ZoomPosition(1, 1, 1), new FocusValue(1, true), new IrisValue(1, true), 0);
    preset.setName("name");
    Set<String> keywords = new HashSet<>();
    keywords.add("Violin");
    Preset presetKeywords = new IPCameraPreset(new ZoomPosition(1, 1, 1), new FocusValue(1, true), new IrisValue(1, true), 0);
    presetKeywords.setName("name");
    presetKeywords.addTags(keywords);

    ArrayList<Preset> allList = new ArrayList<>();
    allList.add(preset);
    allList.add(presetKeywords);

    ArrayList<Preset> tagList = new ArrayList<>();
    tagList.add(presetKeywords);

    when(presetController.getPresetsByTag("Violin")).thenReturn(tagList);
    when(presetController.getPresets()).thenReturn(allList);
    jsonObject = new JSONObject();
    JSONArray tagsJSON = new JSONArray();
    jsonObject.put("tags", tagsJSON);

    JSONArray presetsJSON = new JSONArray();
    presetController.getPresets().forEach(p -> presetsJSON.add(p.toJSON()));
    jsonObject.put("presets", presetsJSON);
    when(presetController.getPresetsJSON("Violin")).thenReturn(jsonObject.toString());
    when(presetController.getPresetsJSON(null)).thenReturn(jsonObject.toString());
  }

  @Test
  public void testRouteCreatePreset() throws IOException, ServletException {
    setPath("/presets/createpreset");
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(createHandler).handle(target, requestMock, httprequestMock, httpresponseMock);
  }

  @Test
  public void testRouteRecallPreset() throws IOException, ServletException {
    setPath("/presets/recallpreset");
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(recallHandler).handle(target, requestMock, httprequestMock, httpresponseMock);
  }

  @Test
  public void testRouteUnknown() throws IOException, ServletException {
    setPath("/presets/unknownroute");
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(out).write(jsonObject.toString());
    verify(requestMock).setHandled(true);
  }

  @Test
  public void testRouteDefault() throws IOException, ServletException {
    setPath("/presets");
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);
    
    verify(out).write(jsonObject.toString());
    verify(requestMock).setHandled(true);
  }

  @Test
  public void testRouteTag() throws IOException, ServletException {
    setPath("/presets?tag=Violin");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("tag", "Violin");
    setParameters(parameters);

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);
    
    verify(out).write(jsonObject.toString());
    verify(requestMock).setHandled(true);
  }
}
