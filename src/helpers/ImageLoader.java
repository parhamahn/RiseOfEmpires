package helpers;
import javax.swing.*;
import java.awt.*;

public class ImageLoader {

    private static ImageLoader instance = new ImageLoader();

    public static ImageLoader getInstance(){return instance;}

    public ImageIcon loadIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            ImageIcon raw = new ImageIcon(imgURL);
            Image resized = raw.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            return new ImageIcon(resized);
        } else {
            System.err.println("Couldn't find icon: " + path);
            return new ImageIcon();
        }
    }

}
