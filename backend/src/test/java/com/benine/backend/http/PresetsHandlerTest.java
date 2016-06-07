package com.benine.backend.http;

<<<<<<< HEAD
import com.benine.backend.Preset;
import com.benine.backend.PresetFactory;
=======
import com.benine.backend.preset.IPCameraPreset;
import com.benine.backend.preset.Preset;
>>>>>>> develop
import com.benine.backend.camera.Position;
import org.eclipse.jetty.util.MultiMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;

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
  public void initialize() throws IOException {
    super.initialize();

    createHandler = mock(CreatePresetHandler.class);
    recallHandler = mock(RecallPresetHandler.class);
    ((PresetsHandler) getHandler()).addHandler("createpreset", createHandler);
    ((PresetsHandler) getHandler()).addHandler("recallpreset", recallHandler);

<<<<<<< HEAD
    Preset preset = new PresetFactory().createPreset(new Position(1, 1), 1, 1, 1, true, 1, 1, true, 0);
    ArrayList<String> keywords = new ArrayList<>();
    keywords.add("Violin");
    Preset presetKeywords = new PresetFactory().createPreset(new Position(1, 1), 1, 1, 1, true, 1, 1, true, 0, keywords);
=======
    Preset preset = new IPCameraPreset(new Position(1, 1), 1, 1, 1, true, 1, 1, true, 0);
    Set<String> keywords = new HashSet<>();
    keywords.add("Violin");
    Preset presetKeywords = new IPCameraPreset(new Position(1, 1), 1, 1, 1, true, 1, 1, true, 0, keywords);
>>>>>>> develop

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
    when(presetController.getPresetsJSON("Violin")).thenReturn(jsonObject.toJSONString());
    when(presetController.getPresetsJSON(null)).thenReturn(jsonObject.toJSONString());
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

    verify(out).write(jsonObject.toJSONString());
    verify(requestMock).setHandled(true);
  }

  @Test
  public void testRouteDefault() throws IOException, ServletException {
    setPath("/presets");
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);
    
    verify(out).write(jsonObject.toJSONString());
    verify(requestMock).setHandled(true);
  }

  @Test
  public void testRouteTag() throws IOException, ServletException {
    setPath("/presets?tag=Violin");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("tag", "Violin");
    setParameters(parameters);

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);
    
    verify(out).write(jsonObject.toJSONString());
    verify(requestMock).setHandled(true);
  }
}
