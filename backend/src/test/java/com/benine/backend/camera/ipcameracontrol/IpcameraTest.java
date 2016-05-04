package com.benine.backend.camera.ipcameracontrol;

import static org.junit.Assert.assertEquals;

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
import com.benine.backend.camera.CameraFactory.InvalidCameraTypeException;
import com.benine.backend.camera.Position;

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
  private IPCamera camera;

  private ArrayList<Parameter> parameterList;
  
  @Before
  public final void setUp() throws InvalidCameraTypeException{
	  IPCameraFactory factory = new IPCameraFactory();
	  String[] camSpec = {"ipcamera", "127.0.0.1:9000"};
	  camera = factory.createCamera(camSpec);
  }


  @Test
  public final void testMoveToHomePosition() throws CameraConnectionException {
    parameterList = new ArrayList<Parameter>();
    parameterList.add(new Parameter("res", "1"));
    parameterList.add(new Parameter("cmd", "#APS80008000111"));

    final HttpRequest request = HttpRequest.request("/cgi-bin/aw_ptz")
                                    .withQueryStringParameters(parameterList);
    mockServerClient.when(request).respond(HttpResponse.response().withBody("aPS80008000111"));
    
    //move with pan speed 17 and tilt speed 1
    Position pos = new Position(0, 180);
    camera.moveTo(pos, 17, 1);
    mockServerClient.verify(request, VerificationTimes.once());
  }
  
  @Test
  public final void testMoveWithSpecifiedSpeed() throws CameraConnectionException {
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
  public final void testGetPosition() throws CameraConnectionException {
    parameterList = new ArrayList<Parameter>();
    parameterList.add(new Parameter("res", "1"));
    parameterList.add(new Parameter("cmd", "#APC"));

    final HttpRequest request = HttpRequest.request("/cgi-bin/aw_ptz")
                                  .withQueryStringParameters(parameterList);
    mockServerClient.when(request).respond(HttpResponse.response().withBody("aPC80008000"));

    Position res = camera.getPosition();
    
    mockServerClient.verify(request, VerificationTimes.once());
    assertEquals(0, res.getPan(), 0.000001);
    assertEquals(180, res.getTilt(), 0.000001);
  }
  
  @Test(expected = IpcameraConnectionException.class)
  public final void testGetPositionException() throws CameraConnectionException {
    parameterList = new ArrayList<Parameter>();
    parameterList.add(new Parameter("res", "1"));
    parameterList.add(new Parameter("cmd", "#APC"));

    final HttpRequest request = HttpRequest.request("/cgi-bin/aw_ptz")
                                  .withQueryStringParameters(parameterList);
    mockServerClient.when(request).respond(HttpResponse.response().withBody("aPP80008000"));

    camera.getPosition();
  }
   
  @Test
  public final void testGetStreamLink() {
    parameterList = new ArrayList<Parameter>();
    parameterList.add(new Parameter("res", "1"));
    parameterList.add(new Parameter("cmd", "#D30"));

    String res = camera.getStreamLink();
    assertEquals(res, "http://127.0.0.1:9000/cgi-bin/mjpeg");
  }
  
  @Test(expected = IpcameraConnectionException.class)
  public final void testNonExcistingIpAdres() throws CameraConnectionException {
    IPCamera camera = new IPCamera("1.300.3.4");
    camera.move(180, 50);
  }

}
