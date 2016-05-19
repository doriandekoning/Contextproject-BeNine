package com.benine.backend.http;

import com.benine.backend.ServerController;
import com.benine.backend.video.MJPEGStreamReader;
import com.benine.backend.video.StreamDistributer;
import com.benine.backend.video.StreamNotAvailableException;
import com.benine.backend.video.StreamReader;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CameraStreamHandler extends AbstractHandler {

  private int camID;

  public CameraStreamHandler(int camID) {
    this.camID = camID;
  }

  @Override
  public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
    StreamReader streamReader = null;
    try {
      streamReader = ServerController.getInstance().getStreamController().getStreamReader(camID);
    } catch (StreamNotAvailableException e) {
      //getLogger().log(e.toString(), LogEvent.Type.WARNING);
    }

    if (streamReader instanceof MJPEGStreamReader) {
      MJPEGStreamReader streamReaderMJPEG = (MJPEGStreamReader) streamReader;
      StreamDistributer distributer = new StreamDistributer(streamReaderMJPEG);

      httpServletResponse.setContentType("multipart/x-mixed-replace;boundary=" + streamReaderMJPEG.getBoundary());
      httpServletResponse.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, pre-check=0, post-check=0, max-age=0");
      httpServletResponse.setHeader("Connection", "close");
      httpServletResponse.setHeader("Pragma", "no-cache");
      httpServletResponse.setHeader("Expires", "Thu, 01 Dec 1994 16:00:00 GMT");
      httpServletResponse.setStatus(HttpServletResponse.SC_OK);

      PipedInputStream in = new PipedInputStream(distributer.getStream());
      BufferedInputStream bs = new BufferedInputStream(in);
      OutputStream os = httpServletResponse.getOutputStream();

      boolean sending = true;

      while (bs.available() > -1 && sending) {
        try {
          os.write(bs.read());
        } catch (IOException e) {
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
}
