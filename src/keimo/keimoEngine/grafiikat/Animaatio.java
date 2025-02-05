package keimo.keimoEngine.grafiikat;

import keimo.keimoEngine.Timer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Animaatio implements Kuva {
    private int pointer;
    private ArrayList<Tekstuuri> frames = new ArrayList<>();
    private ArrayList<BufferedImage> images = new ArrayList<>();

    private double elapsedTime;
    private double currentTime;
    private double lastTime;
    private double fps;
    private int toistot;
    private int toistettu;

    public Animaatio(int fps, String tiedostonNimi, int toistot) {
        this.pointer = 0;
        this.elapsedTime = 0;
        this.currentTime = 0;
        this.toistot = toistot;
        this.toistettu = 0;
        this.lastTime = Timer.getTime();
        this.fps = 1.0/(double)fps;

        images = loadGif(tiedostonNimi);
        for (BufferedImage image : images) {
            frames.add(new Tekstuuri(image));
        }
    }

    public Animaatio(int fps, String tiedostonNimi) {
        this(fps, tiedostonNimi, 0);
    }

    public void bind() {
        bind(0);
    }

    @Override
    public void bind(int sampler) {
        this.currentTime = Timer.getTime();
        this.elapsedTime += currentTime - lastTime;

        if (elapsedTime >= fps) {
            if (elapsedTime >= 0.2) elapsedTime = 0.2;
            elapsedTime -= fps;
            pointer++;
        }
        if (pointer >= frames.size()) {
            if (toistettu < toistot-1) {
                pointer = 0;
                toistettu++;
            }
            else if (toistot == 0) {
                pointer = 0;
            }
            else {
                pointer = frames.size()-1;
            }
        }

        this.lastTime = currentTime;
        frames.get(pointer).bind(sampler);
    }

    private ArrayList<BufferedImage> loadGif(String tiedostonNimi) {
        try {
            ArrayList<BufferedImage> loadedImages = new ArrayList<>();
            String[] imageatt = new String[] {
                "imageLeftPosition",
                "imageTopPosition",
                "imageWidth",
                "imageHeight"
            };    

            ImageReader reader = (ImageReader)ImageIO.getImageReadersByFormatName("gif").next();
            ImageInputStream ciis = ImageIO.createImageInputStream(new File(tiedostonNimi));
            reader.setInput(ciis, false);

            if (tiedostonNimi.endsWith(".gif")) {
                tiedostonNimi = tiedostonNimi.substring(0, tiedostonNimi.length()-4);
            }

            int noi = reader.getNumImages(true);
            BufferedImage master = null;

            for (int i = 0; i < noi; i++) { 
                BufferedImage image = reader.read(i);
                IIOMetadata metadata = reader.getImageMetadata(i);

                Node tree = metadata.getAsTree("javax_imageio_gif_image_1.0");
                NodeList children = tree.getChildNodes();

                for (int j = 0; j < children.getLength(); j++) {
                    Node nodeItem = children.item(j);

                    if(nodeItem.getNodeName().equals("ImageDescriptor")){
                        Map<String, Integer> imageAttr = new HashMap<String, Integer>();

                        for (int k = 0; k < imageatt.length; k++) {
                            NamedNodeMap attr = nodeItem.getAttributes();
                            Node attnode = attr.getNamedItem(imageatt[k]);
                            imageAttr.put(imageatt[k], Integer.valueOf(attnode.getNodeValue()));
                        }
                        if(i==0){
                            master = new BufferedImage(imageAttr.get("imageWidth"), imageAttr.get("imageHeight"), BufferedImage.TYPE_INT_ARGB);
                        }
                        int left = imageAttr.get("imageLeftPosition");
                        int top = imageAttr.get("imageTopPosition");
                        int width = imageAttr.get("imageWidth");
                        int height = imageAttr.get("imageHeight");
                        Graphics2D g2d = master.createGraphics();
                        g2d.setBackground(new Color(0, 0, 0, 0));
                        g2d.clearRect(left, top, width, height);
                        g2d.drawImage(image, imageAttr.get("imageLeftPosition"), imageAttr.get("imageTopPosition"), null);
                    }
                }
                loadedImages.add(image);
            }
            //extractFrames(loadedImages, tiedostonNimi);
            return loadedImages;
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
        catch (IllegalStateException ise) {
            System.out.println("ei voitu ladata: " + tiedostonNimi);
            ise.printStackTrace();
            return null;
        }
    }

    private void extractFrames(ArrayList<BufferedImage> loadedGif, String fileName) {
        for (int i = 0; i < loadedGif.size(); i++) {
            try {
                System.out.println(fileName);
                ImageIO.write(loadedGif.get(i), "PNG", new File(fileName + "/" + i + ".png"));
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
