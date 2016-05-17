package com.benine.backend.video;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
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
            JFrame frame=new JFrame();
            frame.setVisible(true);

            frame.setLayout(new FlowLayout());
            frame.setSize(200,300);
            JPanel panel = new JPanel(new BorderLayout());
            frame.add(panel);

            ImageIcon icon = new ImageIcon();
            JLabel picture = new JLabel(icon);
            panel.add(picture);

            while (true) {
                ByteArrayInputStream bais = new ByteArrayInputStream(getImage());
                BufferedImage bi = ImageIO.read(bais);
                icon.setImage(bi);
                panel.repaint();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int[] checkNextBytes(int amount) throws IOException {
        int[] byteArray = new int[amount];

        streamdata.mark(amount);
        for (int i = 0; i < amount; i++) {
            byteArray[i] = streamdata.read();
        }
        streamdata.reset();

        return byteArray;
    }

    /**
     * Checks the next 2 bytes and checks if they are the JPEG header (FF D8).
     * @return true if header, false if not header.
     * @throws IOException
     */
    private boolean checkJPEGHeader() throws IOException {
        int[] byteArray = checkNextBytes(2);
        return (byteArray[0] == 255 && byteArray[1] == 216);
    }

    private byte[] getImage() throws IOException {
        StringWriter header = new StringWriter(128);

        while (!checkJPEGHeader()) {
            header.write(streamdata.read());
        }

        int contentLength = getContentLength(header.toString());
        byte[] image = new byte[contentLength];

        int offset = 0;
        int readByte;

        for (int i = 0; i < contentLength; i++) {
            readByte = streamdata.read(image, offset, contentLength - offset);
            offset += readByte;
        }

        return image;
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
