package com.benine.backend.video;

import com.sun.media.jfxmedia.logging.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Stream {

    private URL streamURL;
    private URLConnection connection;
    private InputStream inputstream;

    public Stream(URL url) {
        this.streamURL = url;
        this.connection = openConnection();
        this.inputstream = fetchInputStream();
    }

    private URLConnection openConnection() {
        try {
            return streamURL.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private InputStream fetchInputStream() {
        try {
            return connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public InputStream getInputStream() {
        return this.inputstream;
    }

}
