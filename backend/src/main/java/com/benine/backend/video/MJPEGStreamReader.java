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

public class MJPEGStreamReader implements Runnable {

    private Stream stream;
    private BufferedInputStream bufferedStream;

    /**
     * Creates a new MJPEGStreamReader.
     * @param url The url fo the stream.
     */
    public MJPEGStreamReader(URL url) throws IOException{
        this.stream = new Stream(url);
        this.bufferedStream = new BufferedInputStream(stream.getInputStream());
    }

    @Override
    public void run() {
//        try {
//            JFrame frame= new JFrame();
//
//            frame.setLayout(new FlowLayout());
//            frame.setSize(800,800);
//            JPanel panel = new JPanel(new BorderLayout());
//            frame.add(panel);
//
//            ImageIcon icon = new ImageIcon();
//            JLabel picture = new JLabel(icon);
//            panel.add(picture);
//            frame.setVisible(true);
//
//            while (true) {
//                ByteArrayInputStream bais = new ByteArrayInputStream(getImage());
//                BufferedImage bi = ImageIO.read(bais);
//                icon.setImage(bi);
//                panel.repaint();
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        while (true) {
            try {
                getImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Peeks into the next amount of bytes and returns them.
     * @param amount    The amount of bytes to check.
     * @return          An int[] array containing the checked bytes in order.
     * @throws IOException
     */
    private int[] checkNextBytes(int amount) throws IOException {
        int[] byteArray = new int[amount];

        bufferedStream.mark(amount);
        for (int i = 0; i < amount; i++) {
            byteArray[i] = bufferedStream.read();
        }
        bufferedStream.reset();

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
            header.write(bufferedStream.read());
        }

        System.out.println(header.toString());

        int contentLength = getContentLength(header.toString());
        byte[] image = new byte[contentLength];

        int offset = 0;
        int readByte;

        for (int i = 0; i < contentLength; i++) {
            readByte = bufferedStream.read(image, offset, contentLength - offset);
            offset += readByte;
        }

        return image;
    }

    /**
     * Looks for the Content-Length: tag in the header, and extracts the value.
     * @param header    A header string.
     * @return          -1 if content-length not found, else content length.
     */
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
