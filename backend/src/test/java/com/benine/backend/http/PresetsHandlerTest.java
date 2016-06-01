package com.benine.backend.http;

import com.benine.backend.preset.Preset;
import com.benine.backend.camera.Position;
import org.eclipse.jetty.util.MultiMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletException;

import static org.mockito.Mockito.*;

public class PresetsHandlerTest extends RequestHandlerTest {

  private CreatePresetHandler createHandler;

  private RecallPresetHandler recallHandler;

  @Override
  public RequestHandler supplyHandler() {
    return new PresetsHandler();
  }

  @Before
  public void initialize() throws IOException {
    super.initialize();

    createHandler = mock(CreatePresetHandler.class);
    recallHandler = mock(RecallPresetHandler.class);
    ((PresetsHandler) getHandler()).addHandler("createpreset", createHandler);
    ((PresetsHandler) getHandler()).addHandler("recallpreset", recallHandler);

    Preset preset = new Preset(new Position(1, 1), 1, 1, 1, true, 1, 1, true, 0);
    ArrayList<String> keywords = new ArrayList<>();
    keywords.add("Violin");
    Preset presetKeywords = new Preset(new Position(1, 1), 1, 1, 1, true, 1, 1, true, 0, keywords);

    ArrayList<Preset> allList = new ArrayList<>();
    allList.add(preset);
    allList.add(presetKeywords);

    ArrayList<Preset> tagList = new ArrayList<>();
    tagList.add(presetKeywords);

    when(presetController.getPresetsByTag("Violin")).thenReturn(tagList);
    when(presetController.getPresets()).thenReturn(allList);
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

    JSONArray tagsJSON = new JSONArray();
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("tags", tagsJSON);

    JSONArray presetsJSON = new JSONArray();
    presetController.getPresets().forEach(p -> presetsJSON.add(p.toJSON()));
    jsonObject.put("presets", presetsJSON);
    verify(out).write(jsonObject.toJSONString());
    verify(requestMock).setHandled(true);
  }

  @Test
  public void testRouteDefault() throws IOException, ServletException {
    setPath("/presets");
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);
    
    JSONArray tagsJSON = new JSONArray();
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("tags", tagsJSON);

    JSONArray presetsJSON = new JSONArray();
    presetController.getPresets().forEach(p -> presetsJSON.add(p.toJSON()));
    jsonObject.put("presets", presetsJSON);
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
    
    JSONObject jsonObject = new JSONObject();
    JSONArray presetsJSON = new JSONArray();
    presetController.getPresetsByTag("Violin").forEach(p -> presetsJSON.add(p.toJSON()));
    jsonObject.put("presets", presetsJSON);
    verify(out).write(jsonObject.toJSONString());
    verify(requestMock).setHandled(true);
  }
}
