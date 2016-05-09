package com.benine.backend.camera.ipcameracontrol;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.benine.backend.camera.Camera;
import com.benine.backend.camera.CameraConnectionException;
import com.benine.backend.camera.MovingCamera;
import com.benine.backend.camera.Position;
import com.benine.backend.camera.ZoomingCamera;

public class IPCamera2 implements Camera, MovingCamera, ZoomingCamera {
  
  String ipaddress;

  private int id = -1;

  /**
   *  Create a new IP Camera object.
   *  @param ip address of this camera.
   */
  public IPCamera2(String ip) {
    ipaddress = ip;
  }

  @Override
  public String sendCommand(String command) {
    try {
      InputStream com = new URL("http://" + ipaddress + "/cgi-bin/CGIProxy.fcgi?cmd=" + command + "&usr=user&pwd=geheim").openStream();
      DocumentBuilderFactory dbFactory  = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(com);
      doc.getDocumentElement().normalize();
      NodeList l = doc.getElementsByTagName("result");
      if (Integer.parseUnsignedInt(l.item(0).getTextContent()) != 0) {
        throw new IOException();
      }
      return doc.toString();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SAXException e){
      e.printStackTrace();
    } catch (ParserConfigurationException e){
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public int getZoomPosition() throws CameraConnectionException {
    //Not supported
    return 50;
  }

  @Override
  public void zoomTo(int zpos) throws CameraConnectionException {
    //Not Supported
  }

  @Override
  public void zoom(int dir) throws CameraConnectionException {
    int speed = Math.abs(dir - 50);
    if (speed < 17) {
      sendCommand("setZoomSpeed&speed=2");
    } else if (speed < 33) {
      sendCommand("setZoomSpeed&speed=1");
    } else {
      sendCommand("setZoomSpeed&speed=0");
    }
    if (dir > 50) {
      sendCommand("zoomIn");
    } else if (dir < 50) {
      sendCommand("zoomOut");
    } else {
      sendCommand("zoomStop");
    }
  }

  @Override
  public void moveTo(Position pos, int panSpeed, int tiltSpeed)
      throws CameraConnectionException {
    //not supported
  }

  @Override
  public void move(int pan, int tilt) throws CameraConnectionException {
    int speed = pan;
    speed = Math.abs(speed - 50);
    if (speed < 10) {
      sendCommand("setPTZSpeed&speed=4");
    } else if (speed < 20) {
      sendCommand("setPTZSpeed&speed=3");
    } else if (speed < 30) {
      sendCommand("setPTZSpeed&speed=2");
    } else if (speed < 40) {
      sendCommand("setPTZSpeed&speed=1");
    } else {
      sendCommand("setPTZSpeed&speed=0");
    }
    if (pan == 50 && tilt == 50) {
      sendCommand("ptzStopRun");
    } else if (pan > 85) {
      sendCommand("ptzMoveRight");
    } else if (pan < 15) {
      sendCommand("ptzMoveLeft");
    } else if (tilt > 85) { 
      sendCommand("ptzMoveUp");
    } else if (tilt < 15) { 
      sendCommand("ptzMoveDown");
    } 
    
    
      
  }

  @Override
  public Position getPosition() throws CameraConnectionException {
    //Not supported
    return new Position(0, 0);
  }

  /**
   * Returns a JSON representation of this camera.
   * @return A JSON representation of this camera.
   */
  @Override
  public String toJSON() throws CameraConnectionException {
    JSONObject json = new JSONObject();
    json.put("id", Integer.valueOf(this.id));
    try {
      json.put("pan", new Double(getPosition().getPan()));
      json.put("tilt", new Double(getPosition().getTilt()));
      json.put("zoom", new Double(getZoomPosition()));
      json.put("iris", 20);
      json.put("focus", 80);
      json.put("streamlink", getStreamLink());
    } catch (Exception e) {
      //TODO log not possible yet because logger acts funny when used in multiple threads (httpha
      System.out.println(e.toString());
    }
    return  json.toString();

  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public int getId() {
    return id;
  }
  
  public String getStreamLink(){
    return "http://" + ipaddress + "/cgi-bin/CGIStream.cgi?cmd=GetMJStream&usr=user&pwd=geheim";
  }

}
