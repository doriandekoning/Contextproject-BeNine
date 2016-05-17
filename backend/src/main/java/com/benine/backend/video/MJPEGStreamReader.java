package com.benine.backend.video;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MJPEGStreamReader {

    private Stream stream;
    private BufferedInputStream streamdata;

    /**
     * Creates a new MJPEGStreamReader.
     * @param url The url fo the stream.
     */
    public MJPEGStreamReader(URL url) throws IOException{
        this.stream = new Stream(url);
        this.streamdata = new BufferedInputStream(stream.getInputStream());

        try {
            getImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int checkNextByte() throws IOException {
        int b = 0;

        streamdata.mark(1);
        b = streamdata.read();
        streamdata.reset();

        return b;
    }

    private Byte[] getImage() throws IOException {
        // We store the header in here.
        StringWriter header = new StringWriter(128);

        while (checkNextByte() != 255) {
            header.write(streamdata.read());
        }

        System.out.println(getContentLength(header.toString()));
        System.out.println(header.toString());

        return null;
    }

    private int getContentLength(String header) {
        Pattern number = Pattern.compile("-?\\d+");
        Matcher matcher = number.matcher(header);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        } else {
            return -1;
        }
    }

}
