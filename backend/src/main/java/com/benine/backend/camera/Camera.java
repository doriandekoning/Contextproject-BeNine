package com.benine.backend.camera;


/**
 * Interface for communication with remote camera's.
 * @author Bryan
 */

public interface Camera {

  String toJSON() throws CameraConnectionException;

  void setId(int id);

  int getId();
}
