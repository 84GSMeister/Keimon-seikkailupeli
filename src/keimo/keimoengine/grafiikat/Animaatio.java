package keimo.keimoengine.grafiikat;

import keimo.keimoengine.Kello;
import keimo.keimoengine.ikkuna.DialogiIkkunat;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Animaatio extends Renderöitävä {
    private int pointer;
    private ArrayList<Tekstuuri> frames = new ArrayList<>();
    private double elapsedTime;
    private double currentTime;
    private double lastTime;
    private double fps;
    private int toistot;
    private int toistettu;

    public Animaatio(String tiedostonNimi, int fps, int toistot) {
        this.pointer = 0;
        this.elapsedTime = 0;
        this.currentTime = 0;
        this.toistot = toistot;
        this.toistettu = 0;
        this.lastTime = Kello.annaAika();
        this.fps = 1.0/(double)fps;

        ArrayList<ImageFrame> images = loadGif(tiedostonNimi);
        if (images != null) {
            for (ImageFrame image : images) {
                frames.add(new Tekstuuri(image.getImage()));
                if (fps == 0) this.fps = image.getDelay()/100d;
            }
        }
    }

    public Animaatio(String tiedostonNimi, int fps) {
        this(tiedostonNimi, fps, 0);
    }

    public Animaatio(String tiedostonNimi) {
        this(tiedostonNimi, 0, 0);
        // ArrayList<ImageFrame> images = loadGif(tiedostonNimi);
        // try {
        //     for (ImageFrame image : images) {
        //         frames.add(new Tekstuuri(image.getImage()));
        //         this.fps = image.getDelay()/100d;
        //     }
        // }
        // catch (Exception e) {
        //     e.printStackTrace();
        // }
    }

    public ArrayList<Tekstuuri> annaFramet() {
        return frames;
    }

    public double annaFps() {
        return fps;
    }

    public int annaToistot() {
        return toistot;
    }

    public void bind() {
        bind(0);
    }

    @Override
    public void bind(int sampler) {
        this.currentTime = Kello.annaAika();
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
        if (pointer < frames.size()) frames.get(pointer).bind(sampler);
    }

    /**
     * Jos haluat käyttää tätä funktiota:
     * Luo samanniminen tyhjä kansio alkuperäisen GIF-tiedoston hakemistoon (ilman .gif-päätettä).
     * GIF-kuvan framet tallennetaan PNG-formaatissa kansioon.
     * @param tiedostonNimi lähdetiedosto
     */
    public static void exportGifFramesAsPng(String tiedostonNimi) {
        ArrayList<ImageFrame> loadedGif = loadGif(tiedostonNimi);
        System.out.println("puretaan gif: " + tiedostonNimi);
        try {
            if (tiedostonNimi.endsWith(".gif")) {
                tiedostonNimi = tiedostonNimi.substring(0, tiedostonNimi.length()-4);
                for (int i = 0; i < loadedGif.size(); i++) {
                    ImageIO.write(loadedGif.get(i).getImage(), "PNG", new File(tiedostonNimi + "/" + i + ".png"));
                }
            }
            else {
                throw new UnsupportedOperationException();
            }
        }
        catch (IOException | UnsupportedOperationException e) {
            System.out.println("Virheellinen tiedosto: " + tiedostonNimi);
            e.printStackTrace();
        }
    }

    private static ArrayList<ImageFrame> loadGif(String tiedostonNimi) {
        try {
            File kuvaTiedosto = new File(tiedostonNimi);
            InputStream stream = new FileInputStream(kuvaTiedosto);
            ImageReader reader = (ImageReader) ImageIO.getImageReadersByFormatName("gif").next();
            reader.setInput(ImageIO.createImageInputStream(stream));
            
            ArrayList<ImageFrame> kuvat = new ArrayList<>();
            for (ImageFrame frame : readGIF(reader)) {
                kuvat.add(frame);
            }
            return kuvat;
        }
        catch (IOException e) {
            e.printStackTrace();
            DialogiIkkunat.viestiIkkuna("Virhe ladatessa kuvatiedostoa", "Tiedostoa " + tiedostonNimi + " ei voitu ladata", "ok", "error", false);
            return null;
        }
    }

    private static ArrayList<ImageFrame> readGIF(ImageReader reader) throws IOException {
        ArrayList<ImageFrame> frames = new ArrayList<ImageFrame>(2);
    
        int width = -1;
        int height = -1;
    
        IIOMetadata metadata = reader.getStreamMetadata();
        if (metadata != null) {
            IIOMetadataNode globalRoot = (IIOMetadataNode) metadata.getAsTree(metadata.getNativeMetadataFormatName());
    
            NodeList globalScreenDescriptor = globalRoot.getElementsByTagName("LogicalScreenDescriptor");
    
            if (globalScreenDescriptor != null && globalScreenDescriptor.getLength() > 0) {
                IIOMetadataNode screenDescriptor = (IIOMetadataNode) globalScreenDescriptor.item(0);
    
                if (screenDescriptor != null) {
                    width = Integer.parseInt(screenDescriptor.getAttribute("logicalScreenWidth"));
                    height = Integer.parseInt(screenDescriptor.getAttribute("logicalScreenHeight"));
                }
            }
        }
    
        BufferedImage master = null;
        Graphics2D masterGraphics = null;
    
        for (int frameIndex = 0;; frameIndex++) {
            BufferedImage image;
            try {
                image = reader.read(frameIndex);
            } catch (IndexOutOfBoundsException io) {
                break;
            }
    
            if (width == -1 || height == -1) {
                width = image.getWidth();
                height = image.getHeight();
            }
    
            IIOMetadataNode root = (IIOMetadataNode) reader.getImageMetadata(frameIndex).getAsTree("javax_imageio_gif_image_1.0");
            IIOMetadataNode gce = (IIOMetadataNode) root.getElementsByTagName("GraphicControlExtension").item(0);
            int delay = Integer.valueOf(gce.getAttribute("delayTime"));
            String disposal = gce.getAttribute("disposalMethod");
    
            int x = 0;
            int y = 0;
    
            if (master == null) {
                master = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                masterGraphics = master.createGraphics();
                masterGraphics.setBackground(new Color(0, 0, 0, 0));
            } else {
                NodeList children = root.getChildNodes();
                for (int nodeIndex = 0; nodeIndex < children.getLength(); nodeIndex++) {
                    Node nodeItem = children.item(nodeIndex);
                    if (nodeItem.getNodeName().equals("ImageDescriptor")) {
                        NamedNodeMap map = nodeItem.getAttributes();
                        x = Integer.valueOf(map.getNamedItem("imageLeftPosition").getNodeValue());
                        y = Integer.valueOf(map.getNamedItem("imageTopPosition").getNodeValue());
                    }
                }
            }
            masterGraphics.drawImage(image, x, y, null);
    
            BufferedImage copy = new BufferedImage(master.getColorModel(), master.copyData(null), master.isAlphaPremultiplied(), null);
            frames.add(new ImageFrame(copy, delay, disposal));
    
            if (disposal.equals("restoreToPrevious")) {
                BufferedImage from = null;
                for (int i = frameIndex - 1; i >= 0; i--) {
                    if (!frames.get(i).getDisposal().equals("restoreToPrevious") || frameIndex == 0) {
                        from = frames.get(i).getImage();
                        break;
                    }
                }
    
                master = new BufferedImage(from.getColorModel(), from.copyData(null), from.isAlphaPremultiplied(), null);
                masterGraphics = master.createGraphics();
                masterGraphics.setBackground(new Color(0, 0, 0, 0));
            } else if (disposal.equals("restoreToBackgroundColor")) {
                masterGraphics.clearRect(x, y, image.getWidth(), image.getHeight());
            }
        }
        reader.dispose();
    
        return frames;
    }

    private static class ImageFrame {
        private final int delay;
        private final BufferedImage image;
        private final String disposal;
    
        public ImageFrame(BufferedImage image, int delay, String disposal) {
            this.image = image;
            this.delay = delay;
            this.disposal = disposal;
        }
    
        public BufferedImage getImage() {
            return image;
        }
    
        public int getDelay() {
            return delay;
        }
    
        public String getDisposal() {
            return disposal;
        }
    }
}
      
