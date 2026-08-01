package helpers.image;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ImageLoader {

    private static final ImageLoader instance = new ImageLoader();
    public static ImageLoader getInstance(){return instance;}

    private final Map<String, ImageIcon> iconCache = new ConcurrentHashMap<>();
    private final Map<String, BufferedImage> imageCache = new ConcurrentHashMap<>();

    public BufferedImage loadImage(String path) {
        BufferedImage cached = imageCache.get(path);
        if (cached != null) return cached;

        try {
            java.net.URL imgURL = getClass().getResource(path);
            if (imgURL == null) {
                System.err.println("Couldn't find image: " + path);
                return null;
            }
            BufferedImage img = ImageIO.read(imgURL);
            imageCache.put(path, img);
            return img;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ImageIcon loadIcon(String path, int size) {
        String key = path + "@" + size;
        ImageIcon cached = iconCache.get(key);
        if (cached != null) return cached;

        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL == null) {
            System.err.println("Couldn't find icon: " + path);
            return new ImageIcon();
        }

        ImageIcon raw = new ImageIcon(imgURL);
        Image resized = raw.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(resized);
        iconCache.put(key, icon);
        return icon;
    }

    public ImageIcon loadScaledIcon(String path, int width, int height) {
        String key = path + "@" + width + "x" + height;
        ImageIcon cached = iconCache.get(key);
        if (cached != null) return cached;

        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL == null) {
            System.err.println("Couldn't find icon: " + path);
            return new ImageIcon();
        }

        ImageIcon raw = new ImageIcon(imgURL);
        Image resized = raw.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(resized);
        iconCache.put(key, icon);
        return icon;
    }
}