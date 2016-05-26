package com.benine.backend.http;

import com.benine.backend.LogEvent;
import com.benine.backend.ServerController;
import com.benine.backend.camera.Camera;
import com.benine.backend.video.MJPEGStreamReader;
import com.benine.backend.video.StreamDistributer;
import com.benine.backend.video.StreamNotAvailableException;
import com.benine.backend.video.StreamReader;
import com.benine.backend.video.StreamType;
import org.eclipse.jetty.server.Request;

import java.io.IOException;
import java.io.PipedInputStream;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class responsible for handling the /camera/ route.
 */
public class CameraStreamHandler extends CameraRequestHandler {

  @Override
  public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res)
          throws IOException, ServletException {

    int camID = getCameraId(request);

    StreamReader streamReader = null;
    try {
      streamReader = ServerController.getInstance().getStreamController().getStreamReader(camID);
    } catch (StreamNotAvailableException e) {
      getLogger().log("No stream available for this camera.", LogEvent.Type.WARNING);
    }

    // We need an MJPEG streamreader to stream MJPEG.
    if (streamReader instanceof MJPEGStreamReader) {
      MJPEGStreamReader streamReaderMJPEG = (MJPEGStreamReader) streamReader;
      StreamDistributer distributer = new StreamDistributer(streamReaderMJPEG);

      // Set the headers
      setHeaders(streamReaderMJPEG, res);

      // Get an inputstream from the distributer.

      byte[] bytes = new byte[16384];
      int bytesRead;

      try (PipedInputStream in = new PipedInputStream(distributer.getStream());
           ServletOutputStream os = res.getOutputStream()) {

        while ((bytesRead = in.read(bytes)) != -1) {
          os.write(bytes, 0, bytesRead);
          os.flush();
        }
      } catch (IOException e) {
        getLogger().log("Client "
                + request.getRemoteAddr()
                + " disconnected from MJPEG stream "
                + camID, LogEvent.Type.INFO);
      }

    } else {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    request.setHandled(true);
  }

  /**
   * Sets the HTTP Headers so the browser detects MJPEG.
   *
   * @param reader              The MJPEG stream reader containing the boundary.
   * @param httpServletResponse The response for which the headers should be set.
   */
  private void setHeaders(MJPEGStreamReader reader, HttpServletResponse httpServletResponse) {
    httpServletResponse.setContentType(MJPEGHeader.CONTENT_TYPE.getContents()
            + reader.getBoundary());
    httpServletResponse.setHeader("Cache-Control", MJPEGHeader.CACHE_CONTROL.getContents());
    httpServletResponse.setHeader("Connection", MJPEGHeader.CONNECTION.getContents());
    httpServletResponse.setHeader("Pragma", MJPEGHeader.PRAGMA.getContents());
    httpServletResponse.setHeader("Expires", MJPEGHeader.EXPIRES.getContents());
    httpServletResponse.setStatus(HttpServletResponse.SC_OK);
  }

  @Override
  boolean isAllowed(Camera cam) {
    return cam.getStreamType() == StreamType.MJPEG;
  }
}
