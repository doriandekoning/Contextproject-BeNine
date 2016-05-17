package com.benine.backend.video;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MJPEGStreamReader {

    private Stream stream;
    private InputStream streamdata;

    /**
     * Creates a new MJPEGStreamReader.
     * @param url The url fo the stream.
     */
    public MJPEGStreamReader(URL url) throws IOException{
        this.stream = new Stream(url);
        this.streamdata = stream.getInputStream();
    }


}
