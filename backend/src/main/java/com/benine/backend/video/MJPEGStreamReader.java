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

    private boolean checkJPEGBegin() throws IOException {
        // Check next 2 bytes.
        streamdata.mark(2);
        int b1 = streamdata.read();
        int b2 = streamdata.read();
        streamdata.reset();

        // Check if JPEG Begin flag is found,
        // this is FF D8, 255 216 in integers.
        return (b1 == 255 && b2 == 216);
    }

    private boolean checkJPEGEnd() throws IOException {
        // Check next 2 bytes.
        streamdata.mark(2);
        int b1 = streamdata.read();
        int b2 = streamdata.read();
        streamdata.reset();

        // Check if JPEG Begin flag is found,
        // this is FF D8, 255 216 in integers.
        return (b1 == 255 && b2 == 217);
    }

    private Byte[] getImage() throws IOException {
        // We store the header in here.
        StringWriter header = new StringWriter(128);

        while (!checkJPEGBegin()) {
            header.write(streamdata.read());
        }

        System.out.println(getContentLength(header.toString()));
        System.out.println(header.toString());

        return null;
    }

    private int getContentLength(String header) {
        Pattern content_length = Pattern.compile("Content-Length: \\d+");
        Matcher matcher = content_length.matcher(header);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group().replaceAll("[^0-9]", ""));
        } else {
            return -1;
        }
    }

}
