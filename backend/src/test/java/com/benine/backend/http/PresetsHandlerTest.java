package com.benine.backend.http;

import com.benine.backend.Preset;
import com.benine.backend.PresetFactory;
import com.benine.backend.camera.Position;
import org.eclipse.jetty.util.MultiMap;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
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

    Preset preset = new PresetFactory().createPreset(new Position(1, 1), 1, 1, 1, true, 1, 1, true, 0);
    ArrayList<String> keywords = new ArrayList<>();
    keywords.add("Violin");
    Preset presetKeywords = new PresetFactory().createPreset(new Position(1, 1), 1, 1, 1, true, 1, 1, true, 0, keywords);

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

    verify(out).write("{\"presets\":[\"{\\\"image\\\":null,\\\"iris\\\":1,\\\"tiltspeed\\\":1,\\\"panspeed\\\":1,\\\"autoiris\\\":true,\\\"focus\\\":1,\\\"zoom\\\":1,\\\"tilt\\\":1.0,\\\"id\\\":-1,\\\"pan\\\":1.0,\\\"autofocus\\\":true,\\\"tags\\\":[]}\",\"{\\\"image\\\":null,\\\"iris\\\":1,\\\"tiltspeed\\\":1,\\\"panspeed\\\":1,\\\"autoiris\\\":true,\\\"focus\\\":1,\\\"zoom\\\":1,\\\"tilt\\\":1.0,\\\"id\\\":-1,\\\"pan\\\":1.0,\\\"autofocus\\\":true,\\\"tags\\\":[\\\"Violin\\\"]}\"],\"tags\":[]}");
    verify(requestMock).setHandled(true);
  }

  @Test
  public void testRouteDefault() throws IOException, ServletException {
    setPath("/presets");
    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(out).write("{\"presets\":[\"{\\\"image\\\":null,\\\"iris\\\":1,\\\"tiltspeed\\\":1,\\\"panspeed\\\":1,\\\"autoiris\\\":true,\\\"focus\\\":1,\\\"zoom\\\":1,\\\"tilt\\\":1.0,\\\"id\\\":-1,\\\"pan\\\":1.0,\\\"autofocus\\\":true,\\\"tags\\\":[]}\",\"{\\\"image\\\":null,\\\"iris\\\":1,\\\"tiltspeed\\\":1,\\\"panspeed\\\":1,\\\"autoiris\\\":true,\\\"focus\\\":1,\\\"zoom\\\":1,\\\"tilt\\\":1.0,\\\"id\\\":-1,\\\"pan\\\":1.0,\\\"autofocus\\\":true,\\\"tags\\\":[\\\"Violin\\\"]}\"],\"tags\":[]}");
    verify(requestMock).setHandled(true);
  }

  @Test
  public void testRouteTag() throws IOException, ServletException {
    setPath("/presets?tag=Violin");

    MultiMap<String> parameters = new MultiMap<>();
    parameters.add("tag", "Violin");
    setParameters(parameters);

    getHandler().handle(target, requestMock, httprequestMock, httpresponseMock);

    verify(out).write("{\"presets\":[\"{\\\"image\\\":null,\\\"iris\\\":1,\\\"tiltspeed\\\":1,\\\"panspeed\\\":1,\\\"autoiris\\\":true,\\\"focus\\\":1,\\\"zoom\\\":1,\\\"tilt\\\":1.0,\\\"id\\\":-1,\\\"pan\\\":1.0,\\\"autofocus\\\":true,\\\"tags\\\":[\\\"Violin\\\"]}\"]}");
    verify(requestMock).setHandled(true);
  }
}
