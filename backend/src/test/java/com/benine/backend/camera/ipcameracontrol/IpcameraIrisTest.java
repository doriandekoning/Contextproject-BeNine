package com.benine.backend.camera.ipcameracontrol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

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
 * Test class to test the IP Camera Iris functions class.
 * The mock server is used to simulate the camera.
 * @author Bryan
 */
public class IpcameraIrisTest {
  
  @Rule
  public MockServerRule mockServerRule = new MockServerRule(this, 9003);

  private MockServerClient mockServerClient;
  private IPCamera camera = new IPCamera("127.0.0.1:9003");

  private ArrayList<Parameter> parameterList;
  
  @Test
  public final void testSetAutoIrisOff() throws CameraConnectionException {
    parameterList = new ArrayList<Parameter>();
    parameterList.add(new Parameter("res", "1"));
    parameterList.add(new Parameter("cmd", "#D30"));

    final HttpRequest request = HttpRequest.request("/cgi-bin/aw_ptz")
                                  .withQueryStringParameters(parameterList);
    mockServerClient.when(request).respond(HttpResponse.response().withBody("d30"));

    camera.setAutoIrisOn(false);
    
    mockServerClient.verify(request, VerificationTimes.once());
  }
  
  @Test
  public final void testSetAutoIrisOn() throws CameraConnectionException {
    parameterList = new ArrayList<Parameter>();
    parameterList.add(new Parameter("res", "1"));
    parameterList.add(new Parameter("cmd", "#D31"));

    final HttpRequest request = HttpRequest.request("/cgi-bin/aw_ptz")
                                  .withQueryStringParameters(parameterList);
    mockServerClient.when(request).respond(HttpResponse.response().withBody("d31"));

    camera.setAutoIrisOn(true);
    
    mockServerClient.verify(request, VerificationTimes.once());
  }
  
  @Test
  public final void testIsAutoIrisOff() throws CameraConnectionException {
    parameterList = new ArrayList<Parameter>();
    parameterList.add(new Parameter("res", "1"));
    parameterList.add(new Parameter("cmd", "#D3"));

    final HttpRequest request = HttpRequest.request("/cgi-bin/aw_ptz")
                                  .withQueryStringParameters(parameterList);
    mockServerClient.when(request).respond(HttpResponse.response().withBody("d30"));

    boolean res = camera.isAutoIrisOn();
    
    mockServerClient.verify(request, VerificationTimes.once());
    assertFalse(res);
  }
  
  @Test
  public final void testIsAutoIrisOn() throws CameraConnectionException {
    parameterList = new ArrayList<Parameter>();
    parameterList.add(new Parameter("res", "1"));
    parameterList.add(new Parameter("cmd", "#D3"));

    final HttpRequest request = HttpRequest.request("/cgi-bin/aw_ptz")
                                  .withQueryStringParameters(parameterList);
    mockServerClient.when(request).respond(HttpResponse.response().withBody("d31"));

    boolean res = camera.isAutoIrisOn();
    
    mockServerClient.verify(request, VerificationTimes.once());
    assertTrue(res);
  }
  
  @Test(expected = IpcameraConnectionException.class)
  public final void testIsAutoIrisOnException() throws CameraConnectionException {
    parameterList = new ArrayList<Parameter>();
    parameterList.add(new Parameter("res", "1"));
    parameterList.add(new Parameter("cmd", "#D3"));

    final HttpRequest request = HttpRequest.request("/cgi-bin/aw_ptz")
                                  .withQueryStringParameters(parameterList);
    mockServerClient.when(request).respond(HttpResponse.response().withBody("p31"));

    camera.isAutoIrisOn();
  }
  
  @Test
  public final void testSetIrisPosition() throws CameraConnectionException {
    parameterList = new ArrayList<Parameter>();
    parameterList.add(new Parameter("res", "1"));
    parameterList.add(new Parameter("cmd", "#I80"));

    final HttpRequest request = HttpRequest.request("/cgi-bin/aw_ptz")
                                  .withQueryStringParameters(parameterList);
    mockServerClient.when(request).respond(HttpResponse.response().withBody("iC80"));

    camera.setIrisPos(80);
    
    mockServerClient.verify(request, VerificationTimes.once());
  }
  
  @Test
  public final void testGetIrisPosition() throws CameraConnectionException {
    parameterList = new ArrayList<Parameter>();
    parameterList.add(new Parameter("res", "1"));
    parameterList.add(new Parameter("cmd", "#GI"));

    final HttpRequest request = HttpRequest.request("/cgi-bin/aw_ptz")
                                  .withQueryStringParameters(parameterList);
    mockServerClient.when(request).respond(HttpResponse.response().withBody("giD421"));

    int res = camera.getIrisPos();
    
    mockServerClient.verify(request, VerificationTimes.once());
    
    assertEquals(res, 3394, 0.000001);
  }
}
