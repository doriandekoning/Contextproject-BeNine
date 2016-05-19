package com.benine.backend.camera.ipcameracontrol;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
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
  public MockServerRule mockServerRule = new MockServerRule(this, 9002);

  private MockServerClient mockServerClient;
  private IPCamera camera;

  private ArrayList<Parameter> parameterList;
  
  @Before
  public final void setUp() throws InvalidCameraTypeException{
	  camera = new IPCamera("127.0.0.1:9002");
	  mockServerClient.reset();
  }
  
  @Test
  public final void testGetMACAddress() throws CameraConnectionException {
    parameterList = new ArrayList<Parameter>();
    parameterList.add(new Parameter("FILE", "1"));

    final HttpRequest request = HttpRequest.request("/cgi-bin/getinfo")
                                    .withQueryStringParameters(parameterList);
    mockServerClient.when(request).respond(HttpResponse.response().withBody("MAC=8C-C1-21-F0-46-C9 "
        + "SERIAL=D5TBA0099 VERSION=V01.0600 NAME=AW-HE130K SDrec=disable SDrec2=disable"
        + " sAlarm=OFF sAUX=off ePort=31004 aEnable=off aEnc=3 aBitrate=32 aBitrate2=64 "
        + "aInInterval=40 aOutInterval=640 aOutPort=34004 aOutStatus=off aOutUID=0 aInPort_h264=33004"
        + " aInPort_h264_2=33014 aInPort_h264_3=33024 aInPort_h264_4=33034 sRtspMode_h264=0 sRtspMode_h264_2=0 "
        + "sRtspMode_h264_3=0 sRtspMode_h264_4=0 ImageCaptureMode=2m ratio=16_9 Maxfps= StreamMode=1 "
        + "iTransmit_h264=1 sDelivery_h264=uni iBitrate_h264=4096 iResolution_h264=1920 iQuality_h264=normal"
        + " iMultiAuto_h264=0 iTransmit_h264_2=1 sDelivery_h264_2=uni iBitrate_h264_2=1536 iResolution_h264_2=640"
        + " iQuality_h264_2=normal iMultiAuto_h264_2=0 iTransmit_h264_3=1 sDelivery_h264_3=uni "
        + "iBitrate_h264_3=1024 iResolution_h264_3=320 iQuality_h264_3=normal iMultiAuto_h264_3=0 "
        + "iTransmit_h264_4=1 sDelivery_h264_4=uni iBitrate_h264_4=512 iResolution_h264_4=160 "
        + "iQuality_h264_4=normal iMultiAuto_h264_4=0"));
    
    String actual = camera.getMacAddress();
    mockServerClient.verify(request, VerificationTimes.once());
    assertEquals("8C-C1-21-F0-46-C9", actual);
  }
  
  @Test(expected = IpcameraConnectionException.class)
  public final void testGetMACAddressFails() throws CameraConnectionException {
    parameterList = new ArrayList<Parameter>();
    parameterList.add(new Parameter("FILE", "1"));

    final HttpRequest request = HttpRequest.request("/cgi-bin/getinfo")
                                    .withQueryStringParameters(parameterList);
    mockServerClient.when(request).respond(HttpResponse.response().withBody(""));
    
    camera.getMacAddress();
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
    assertEquals(res, "http://127.0.0.1:9002/cgi-bin/mjpeg");
  }
  
  @Test(expected = IpcameraConnectionException.class)
  public final void testNonExcistingIpAdres() throws CameraConnectionException {
    IPCamera camera = new IPCamera("1.300.3.4");
    camera.move(180, 50);
  }

  @Test
  public final void testGetSetId() {
    IPCamera camera = new IPCamera("1.300.3.4");
    camera.setId(4);
    Assert.assertEquals(4, camera.getId());
  }

  @Test
  public final void testUninitializedId(){
    IPCamera camera = new IPCamera("1.300.3.4");
    Assert.assertEquals(-1, camera.getId());
  }
}
