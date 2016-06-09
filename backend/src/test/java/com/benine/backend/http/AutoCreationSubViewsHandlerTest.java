package com.benine.backend.http;//TODO add Javadoc comment

import com.benine.backend.preset.PresetController;
import org.eclipse.jetty.util.MultiMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.model.MultipleFailureException;

import static org.mockito.Mockito.verify;

/**
 *
 */
public class AutoCreationSubViewsHandlerTest extends RequestHandlerTest {

  @Test
  public void testHandle() throws Exception {
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

  @Override
  public RequestHandler supplyHandler() {
    return new AutoCreationSubViewsHandler(httpserver);
  }
}
