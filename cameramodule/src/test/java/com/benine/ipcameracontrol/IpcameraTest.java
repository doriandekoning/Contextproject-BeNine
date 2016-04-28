package com.benine.ipcameracontrol;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.Parameter;
import org.mockserver.verify.VerificationTimes;

import java.util.ArrayList;

/**
 * Test class to test the IP Camera class.
 * The mock server is used to simulate the camera.
 * @author Bryan
 */
public class IpcameraTest {

  @Rule
  public MockServerRule mockServerRule = new MockServerRule(this, 9000);

  private MockServerClient mockServerClient;
  private Ipcamera camera = new Ipcamera("127.0.0.1:9000");

  private ArrayList<Parameter> parameterList;

  @Test
  public final void testMoveToHomePosition() {
    parameterList = new ArrayList<Parameter>();
    parameterList.add(new Parameter("res", "1"));
    parameterList.add(new Parameter("cmd", "#APS80008000111"));

    final HttpRequest request = HttpRequest.request("/cgi-bin/aw_ptz")
                                    .withQueryStringParameters(parameterList);
    mockServerClient.when(request).respond(HttpResponse.response().withBody("aPS80008000111"));
    
    //move with pan speed 17 and tilt speed 1
    camera.moveTo(0, 180, 17, 1);
    mockServerClient.verify(request, VerificationTimes.once());
  }
  
  @Test
  public final void testMoveWithSpecifiedSpeed() {
    parameterList = new ArrayList<Parameter>();
    parameterList.add(new Parameter("res", "1"));
    parameterList.add(new Parameter("cmd", "#PTS0199"));

    final HttpRequest request = HttpRequest.request("/cgi-bin/aw_ptz")
                                  .withQueryStringParameters(parameterList);
    mockServerClient.when(request).respond(HttpResponse.response().withBody("pTS0199"));

    //move camera with max speed to top left.
    camera.move(01, 99);

    mockServerClient.verify(request, VerificationTimes.once());
  }
  
  @Test
  public final void testGetPosition() {
    parameterList = new ArrayList<Parameter>();
    parameterList.add(new Parameter("res", "1"));
    parameterList.add(new Parameter("cmd", "#APC"));

    final HttpRequest request = HttpRequest.request("/cgi-bin/aw_ptz")
                                  .withQueryStringParameters(parameterList);
    mockServerClient.when(request).respond(HttpResponse.response().withBody("aPC80008000"));

    double[] res = camera.getPosition();
    
    mockServerClient.verify(request, VerificationTimes.once());
    assertEquals(0, res[0], 0.000001);
    assertEquals(180, res[1], 0.000001);
  }
  
  @Test
  public final void testGetZoomPosition() {
    parameterList = new ArrayList<Parameter>();
    parameterList.add(new Parameter("res", "1"));
    parameterList.add(new Parameter("cmd", "#GZ"));

    final HttpRequest request = HttpRequest.request("/cgi-bin/aw_ptz")
                                  .withQueryStringParameters(parameterList);
    mockServerClient.when(request).respond(HttpResponse.response().withBody("gz655"));

    int res = camera.getZoomPosition();
    
    mockServerClient.verify(request, VerificationTimes.once());
    assertEquals(res, 1621, 0.000001);
  }
  
  @Test
  public final void testZoomTo() {
    parameterList = new ArrayList<Parameter>();
    parameterList.add(new Parameter("res", "1"));
    parameterList.add(new Parameter("cmd", "#AXZBAB"));

    final HttpRequest request = HttpRequest.request("/cgi-bin/aw_ptz")
                                  .withQueryStringParameters(parameterList);
    mockServerClient.when(request).respond(HttpResponse.response().withBody("axzBAB"));

    camera.zoomTo(1622);
    
    mockServerClient.verify(request, VerificationTimes.once());
  }
  
  @Test
  public final void testZoom() {
    parameterList = new ArrayList<Parameter>();
    parameterList.add(new Parameter("res", "1"));
    parameterList.add(new Parameter("cmd", "#Z80"));

    final HttpRequest request = HttpRequest.request("/cgi-bin/aw_ptz")
                                  .withQueryStringParameters(parameterList);
    mockServerClient.when(request).respond(HttpResponse.response().withBody("zS80"));

    camera.zoom(80);
    
    mockServerClient.verify(request, VerificationTimes.once());
  }

}
