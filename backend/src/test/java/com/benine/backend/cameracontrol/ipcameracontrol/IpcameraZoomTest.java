package com.benine.backend.cameracontrol.ipcameracontrol;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.Parameter;
import org.mockserver.verify.VerificationTimes;

import com.benine.backend.cameracontrol.CameraConnectionException;

/**
 * Test class to test the IP Camera zoom functions class.
 * The mock server is used to simulate the camera.
 * @author Bryan
 */
public class IpcameraZoomTest {
  
  @Rule
  public MockServerRule mockServerRule = new MockServerRule(this, 9000);

  private MockServerClient mockServerClient;
  private Ipcamera camera = new Ipcamera("127.0.0.1:9000");

  private ArrayList<Parameter> parameterList;
  
  @Test
  public final void testGetZoomPosition() throws CameraConnectionException {
    parameterList = new ArrayList<Parameter>();
    parameterList.add(new Parameter("res", "1"));
    parameterList.add(new Parameter("cmd", "#GZ"));

    final HttpRequest request = HttpRequest.request("/cgi-bin/aw_ptz")
                                  .withQueryStringParameters(parameterList);
    mockServerClient.when(request).respond(HttpResponse.response().withBody("gz655"));

    int res = camera.getZoom().getZoomPosition();
    
    mockServerClient.verify(request, VerificationTimes.once());
    assertEquals(res, 1621, 0.000001);
  }
  
  @Test(expected = IpcameraConnectionException.class)
  public final void testGetZoomPositionException() throws CameraConnectionException {
    parameterList = new ArrayList<Parameter>();
    parameterList.add(new Parameter("res", "1"));
    parameterList.add(new Parameter("cmd", "#GZ"));

    final HttpRequest request = HttpRequest.request("/cgi-bin/aw_ptz")
                                  .withQueryStringParameters(parameterList);
    mockServerClient.when(request).respond(HttpResponse.response().withBody("gs655"));

    camera.getZoom().getZoomPosition();
  }
  
  @Test
  public final void testZoomTo() throws CameraConnectionException {
    parameterList = new ArrayList<Parameter>();
    parameterList.add(new Parameter("res", "1"));
    parameterList.add(new Parameter("cmd", "#AXZBAB"));

    final HttpRequest request = HttpRequest.request("/cgi-bin/aw_ptz")
                                  .withQueryStringParameters(parameterList);
    mockServerClient.when(request).respond(HttpResponse.response().withBody("axzBAB"));

    camera.getZoom().zoomTo(1622);
    
    mockServerClient.verify(request, VerificationTimes.once());
  }
  
  @Test
  public final void testZoom() throws CameraConnectionException {
    parameterList = new ArrayList<Parameter>();
    parameterList.add(new Parameter("res", "1"));
    parameterList.add(new Parameter("cmd", "#Z80"));

    final HttpRequest request = HttpRequest.request("/cgi-bin/aw_ptz")
                                  .withQueryStringParameters(parameterList);
    mockServerClient.when(request).respond(HttpResponse.response().withBody("zS80"));

    camera.getZoom().zoom(80);
    
    mockServerClient.verify(request, VerificationTimes.once());
  }
  
}
