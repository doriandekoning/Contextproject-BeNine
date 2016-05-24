package com.benine.backend.camera.ipcameracontrol;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.benine.backend.camera.CameraConnectionException;

/**
 * Test class to test the IP Camera zoom functions class.
 * The mock server is used to simulate the camera.
 */
public class IpcameraZoomTest {
  
  private IPCamera camera;
  
  @Before
  public final void setUp(){
    camera = new IPCamera("127.0.0.1:");
  }
  
//  @Test
//  public final void testGetZoomPosition() throws CameraConnectionException {
//    parameterList = new ArrayList<Parameter>();
//    parameterList.add(new Parameter("res", "1"));
//    parameterList.add(new Parameter("cmd", "#GZ"));
//
//    final HttpRequest request = HttpRequest.request("/cgi-bin/aw_ptz")
//                                  .withQueryStringParameters(parameterList);
//    mockServerClient.when(request).respond(HttpResponse.response().withBody("gz655"));
//
//    int res = camera.getZoomPosition();
//    
//    mockServerClient.verify(request, VerificationTimes.once());
//    assertEquals(res, 1621, 0.000001);
//  }
//  
//  @Test(expected = IpcameraConnectionException.class)
//  public final void testGetZoomPositionException() throws CameraConnectionException {
//    parameterList = new ArrayList<Parameter>();
//    parameterList.add(new Parameter("res", "1"));
//    parameterList.add(new Parameter("cmd", "#GZ"));
//
//    final HttpRequest request = HttpRequest.request("/cgi-bin/aw_ptz")
//                                  .withQueryStringParameters(parameterList);
//    mockServerClient.when(request).respond(HttpResponse.response().withBody("gs655"));
//
//    camera.getZoomPosition();
//  }
//  
//  @Test
//  public final void testZoomTo() throws CameraConnectionException {
//    parameterList = new ArrayList<Parameter>();
//    parameterList.add(new Parameter("res", "1"));
//    parameterList.add(new Parameter("cmd", "#AXZB84"));
//
//    final HttpRequest request = HttpRequest.request("/cgi-bin/aw_ptz")
//                                  .withQueryStringParameters(parameterList);
//    mockServerClient.when(request).respond(HttpResponse.response().withBody("axzBAB"));
//
//    camera.zoomTo(58);
//    
//    mockServerClient.verify(request, VerificationTimes.once());
//  }
//  
//  @Test
//  public final void testZoom() throws CameraConnectionException {
//    parameterList = new ArrayList<Parameter>();
//    parameterList.add(new Parameter("res", "1"));
//    parameterList.add(new Parameter("cmd", "#Z80"));
//
//    final HttpRequest request = HttpRequest.request("/cgi-bin/aw_ptz")
//                                  .withQueryStringParameters(parameterList);
//    mockServerClient.when(request).respond(HttpResponse.response().withBody("zS80"));
//
//    camera.zoom(80);
//    
//    mockServerClient.verify(request, VerificationTimes.once());
//  }
//  
//  @Test
//  public final void testZoom2() throws CameraConnectionException {
//    parameterList = new ArrayList<Parameter>();
//    parameterList.add(new Parameter("res", "1"));
//    parameterList.add(new Parameter("cmd", "#Z02"));
//
//    final HttpRequest request = HttpRequest.request("/cgi-bin/aw_ptz")
//                                  .withQueryStringParameters(parameterList);
//    mockServerClient.when(request).respond(HttpResponse.response().withBody("zS02"));
//
//    camera.zoom(2);
//    
//    mockServerClient.verify(request, VerificationTimes.once());
//  }
//  
}
