package com.benine.backend.http.jetty;

import com.benine.backend.ServerController;
import com.benine.backend.video.MJPEGStreamReader;
import com.benine.backend.video.StreamDistributer;
import com.benine.backend.video.StreamNotAvailableException;
import com.benine.backend.video.StreamReader;
import org.eclipse.jetty.server.Request;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class responsible for handling the /camera/ route.
 */
public class CameraStreamHandler extends CameraRequestHandler {

  @Override
  public void handle(String s, Request request,
                     HttpServletRequest httpServletRequest,
                     HttpServletResponse httpServletResponse)
          throws IOException, ServletException {

    int camID = getCameraId(request);

    StreamReader streamReader = null;
    try {
      streamReader = ServerController.getInstance().getStreamController().getStreamReader(camID);
    } catch (StreamNotAvailableException e) {
      e.printStackTrace();
      //getLogger().log(e.toString(), LogEvent.Type.WARNING);
    }

    // We need an MJPEG streamreader to stream MJPEG.
    if (streamReader instanceof MJPEGStreamReader) {
      MJPEGStreamReader streamReaderMJPEG = (MJPEGStreamReader) streamReader;
      StreamDistributer distributer = new StreamDistributer(streamReaderMJPEG);

      // Set the headers
      setHeaders(streamReaderMJPEG, httpServletResponse);

      // Get an inputstream from the distributer.
      PipedInputStream in = new PipedInputStream(distributer.getStream());
      BufferedInputStream bs = new BufferedInputStream(in);
      OutputStream os = httpServletResponse.getOutputStream();

      boolean sending = true;
      while (bs.available() > -1 && sending) {
        try {
          os.write(bs.read());
        } catch (IOException e) {
          // An exception occured, this means the browser cannot be reached.
          sending = false;
        }
      }

      os.close();
      bs.close();
      in.close();

    } else {
      httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    request.setHandled(true);
  }

  /**
   * Sets the HTTP Headers so the browser detects MJPEG.
   * @param reader                The MJPEG stream reader containing the boundary.
   * @param httpServletResponse   The response for which the headers should be set.
   */
  private void setHeaders(MJPEGStreamReader reader, HttpServletResponse httpServletResponse) {
    httpServletResponse.setContentType("multipart/x-mixed-replace;boundary="
            + reader.getBoundary());
    httpServletResponse.setHeader("Cache-Control", "no-store, "
            + "no-cache, must-revalidate, pre-check=0, post-check=0, max-age=0");
    httpServletResponse.setHeader("Connection", "close");
    httpServletResponse.setHeader("Pragma", "no-cache");
    httpServletResponse.setHeader("Expires", "Thu, 01 Dec 1994 16:00:00 GMT");
    httpServletResponse.setStatus(HttpServletResponse.SC_OK);
  }
}
