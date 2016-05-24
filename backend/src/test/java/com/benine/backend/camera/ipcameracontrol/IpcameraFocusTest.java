package com.benine.backend.camera.ipcameracontrol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.Parameter;
import org.mockserver.verify.VerificationTimes;

import com.benine.backend.camera.CameraConnectionException;

/**
 * Test class to test the IP Camera Focus function class.
 * The mock server is used to simulate the camera.
 */
public class IpcameraFocusTest {
  
  @Rule
  public MockServerRule mockServerRule = new MockServerRule(this, false);

  private MockServerClient mockServerClient;
  private IPCamera camera;

  private ArrayList<Parameter> parameterList;
  
  @Before
  public final void setUp() {
    camera = new IPCamera("127.0.0.1:" + mockServerRule.getPort());
    mockServerClient.reset();
  }
  
  @Test
  public final void testGetFocusPosition() throws CameraConnectionException {
    parameterList = new ArrayList<Parameter>();
    parameterList.add(new Parameter("res", "1"));
    parameterList.add(new Parameter("cmd", "#GF"));

    final HttpRequest request = HttpRequest.request("/cgi-bin/aw_ptz")
                                  .withQueryStringParameters(parameterList);
    mockServerClient.when(request).respond(HttpResponse.response().withBody("gfA42"));

    int res = camera.getFocusPosition();
    
    mockServerClient.verify(request, VerificationTimes.once());
    
    assertEquals(res, 2626, 0.000001);
  }
  
  @Test(expected = IpcameraConnectionException.class)
  public final void testGetFocusPositionException() throws CameraConnectionException {
    parameterList = new ArrayList<Parameter>();
    parameterList.add(new Parameter("res", "1"));
    parameterList.add(new Parameter("cmd", "#GF"));

    final HttpRequest request = HttpRequest.request("/cgi-bin/aw_ptz")
                                  .withQueryStringParameters(parameterList);
    mockServerClient.when(request).respond(HttpResponse.response().withBody("ggA42"));

    camera.getFocusPosition();
  }
  
  @Test
  public final void testSetFocus() throws CameraConnectionException {
    parameterList = new ArrayList<Parameter>();
    parameterList.add(new Parameter("res", "1"));
    parameterList.add(new Parameter("cmd", "#AXFFFF"));

    final HttpRequest request = HttpRequest.request("/cgi-bin/aw_ptz")
                                  .withQueryStringParameters(parameterList);
    mockServerClient.when(request).respond(HttpResponse.response().withBody("axfFFF"));

    camera.setFocusPosition(2882);
    
    mockServerClient.verify(request, VerificationTimes.once());
  }
  
  @Test
  public final void testMoveFocus() throws CameraConnectionException {
    parameterList = new ArrayList<Parameter>();
    parameterList.add(new Parameter("res", "1"));
    parameterList.add(new Parameter("cmd", "#F80"));

    final HttpRequest request = HttpRequest.request("/cgi-bin/aw_ptz")
                                  .withQueryStringParameters(parameterList);
    mockServerClient.when(request).respond(HttpResponse.response().withBody("fs80"));

    camera.moveFocus(80);
    
    mockServerClient.verify(request, VerificationTimes.once());
  }
  
  @Test
  public final void testMoveFocus2() throws CameraConnectionException {
    parameterList = new ArrayList<Parameter>();
    parameterList.add(new Parameter("res", "1"));
    parameterList.add(new Parameter("cmd", "#F02"));

    final HttpRequest request = HttpRequest.request("/cgi-bin/aw_ptz")
                                  .withQueryStringParameters(parameterList);
    mockServerClient.when(request).respond(HttpResponse.response().withBody("fs02"));

    camera.moveFocus(2);
    
    mockServerClient.verify(request, VerificationTimes.once());
  }
  
  @Test
  public final void testIsAutoFocusOn() throws CameraConnectionException {
    parameterList = new ArrayList<Parameter>();
    parameterList.add(new Parameter("res", "1"));
    parameterList.add(new Parameter("cmd", "#D1"));

    final HttpRequest request = HttpRequest.request("/cgi-bin/aw_ptz")
                                  .withQueryStringParameters(parameterList);
    mockServerClient.when(request).respond(HttpResponse.response().withBody("d11"));

    boolean res = camera.isAutoFocusOn();
    
    mockServerClient.verify(request, VerificationTimes.once());
    assertTrue(res);
  }
  
  @Test
  public final void testIsAutoFocusOff() throws CameraConnectionException {
    parameterList = new ArrayList<Parameter>();
    parameterList.add(new Parameter("res", "1"));
    parameterList.add(new Parameter("cmd", "#D1"));

    final HttpRequest request = HttpRequest.request("/cgi-bin/aw_ptz")
                                  .withQueryStringParameters(parameterList);
    mockServerClient.when(request).respond(HttpResponse.response().withBody("d10"));

    boolean res = camera.isAutoFocusOn();
    
    mockServerClient.verify(request, VerificationTimes.once());
    assertFalse(res);
  }
  
  @Test(expected = CameraConnectionException.class)
  public final void testIsAutoFocusOffException() throws CameraConnectionException {
    parameterList = new ArrayList<Parameter>();
    parameterList.add(new Parameter("res", "1"));
    parameterList.add(new Parameter("cmd", "#D1"));

    final HttpRequest request = HttpRequest.request("/cgi-bin/aw_ptz")
                                  .withQueryStringParameters(parameterList);
    mockServerClient.when(request).respond(HttpResponse.response().withBody("K10"));

    camera.isAutoFocusOn();
  }
  
  @Test
  public final void testSetAutoFocusOff() throws CameraConnectionException {
    parameterList = new ArrayList<Parameter>();
    parameterList.add(new Parameter("res", "1"));
    parameterList.add(new Parameter("cmd", "#D10"));

    final HttpRequest request = HttpRequest.request("/cgi-bin/aw_ptz")
                                  .withQueryStringParameters(parameterList);
    mockServerClient.when(request).respond(HttpResponse.response().withBody("d10"));

    camera.setAutoFocusOn(false);
    
    mockServerClient.verify(request, VerificationTimes.once());
  }
  
  @Test
  public final void testSetAutoFocusOn() throws CameraConnectionException {
    parameterList = new ArrayList<Parameter>();
    parameterList.add(new Parameter("res", "1"));
    parameterList.add(new Parameter("cmd", "#D11"));

    final HttpRequest request = HttpRequest.request("/cgi-bin/aw_ptz")
                                  .withQueryStringParameters(parameterList);
    mockServerClient.when(request).respond(HttpResponse.response().withBody("d11"));

    camera.setAutoFocusOn(true);
    
    mockServerClient.verify(request, VerificationTimes.once());
  }
}
