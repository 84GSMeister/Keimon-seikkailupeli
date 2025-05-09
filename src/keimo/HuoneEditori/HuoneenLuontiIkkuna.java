package keimo.HuoneEditori;

import keimo.*;
import keimo.Utility.Downloaded.SpringUtilities;
import keimo.Utility.Käännettävä.Suunta;
//import keimo.kenttäkohteet.*;

import javax.swing.*;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class HuoneenLuontiIkkuna {

    static final int ikkunanLeveys = 260;
    static final int ikkunanKorkeus = 190;
    static JFrame ikkuna;
    static String[] tekstit = {"Huoneen ID", "Huoneen nimi", "Kentän koko", "Tuo huonetiedosto"};
    static int valintojenMäärä = tekstit.length;
    static JTextField[] tekstiKentät = new JTextField[valintojenMäärä];
    static JButton huoneTiedostoValintaNappi;
    static Huone huoneKSH;

    private static boolean asetettavaWarpVasen = false;
    private static int asetettavaWarpVasenHuoneId = 0;
    private static boolean asetettavaWarpOikea = false;
    private static int asetettavaWarpOikeaHuoneId = 0;
    private static boolean asetettavaWarpAlas = false;
    private static int asetettavaWarpAlasHuoneId = 0;
    private static boolean asetettavaWarpYlös = false;
    private static int asetettavaWarpYlösHuoneId = 0;

    private static int huoneenId;

    static boolean tarkistaArvot() {
        try {
            huoneenId = Integer.parseInt(tekstiKentät[0].getText());
            String huoneenNimi = tekstiKentät[1].getText();
            int kentänKoko = Integer.parseInt(tekstiKentät[2].getText());
            if (huoneenId < 0) {
                JOptionPane.showMessageDialog(ikkuna, "Negatiivinen ID ei kelpaa.", "Virheellinen ID!", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            else if (HuoneEditoriIkkuna.huoneKartta.containsKey(huoneenId)) {
                JOptionPane.showMessageDialog(ikkuna, "Huone ID:llä " + huoneenId + " löytyy jo.", "Virheellinen ID!", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            else if (huoneenNimi.contains("(") || huoneenNimi.contains(")")) {
                CustomViestiIkkunat.SulkumerkkiVaroitus.näytäDialogi();
                return false;
            }
            else if (kentänKoko < 1) {
                if (kentänKoko < 0) JOptionPane.showMessageDialog(ikkuna, "Huoneen koko ei voi olla negatiivinen, älypää!", "Virheellinen koko!", JOptionPane.ERROR_MESSAGE);
                else JOptionPane.showMessageDialog(ikkuna, "Huoneen koon täytyy olla positiivinen.", "Virheellinen koko!", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            else {
                asetaArvot(huoneenId, huoneenNimi, kentänKoko);
                return true;
            }
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(ikkuna, "Virheellinen syöte!", "Virheellinen syöte!", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    static void asetaArvot(int huoneenId, String huoneenNimi, int asetettuKentänKoko) {
        TarkistettavatArvot.uusiKentänKoko = asetettuKentänKoko;

        if (huoneKSH == null) {
            HuoneEditoriIkkuna.vaihdaHuonetta(HuoneEditoriIkkuna.muokattavaHuone, huoneenId, false);
            Peli.luoHuone(huoneenId, asetettuKentänKoko, huoneenNimi, null, "Oma alue", null, null, null, null, null, null);
        }
        else {
            HuoneEditoriIkkuna.huoneKartta.put(huoneenId, huoneKSH);
        }
        ikkuna.dispose();
    }

    public static void luoHuoneenLuontiIkkuna(Suunta suunta) {
        
        huoneKSH = null;
        Huone h = HuoneEditoriIkkuna.huoneKartta.get(HuoneEditoriIkkuna.muokattavaHuone);
        JPanel paneli = new JPanel(new SpringLayout());
        for (int i = 0; i < valintojenMäärä-1; i++) {
            JLabel teksti = new JLabel(tekstit[i], JLabel.TRAILING);
            paneli.add(teksti);
            tekstiKentät[i] = new JTextField("" + Peli.kentänKoko,10);
            teksti.setLabelFor(tekstiKentät[i]);
            paneli.add(tekstiKentät[i]);
        }
        if (HuoneEditoriIkkuna.huoneKartta != null) {
            huoneenId = HuoneEditoriIkkuna.huoneKartta.size();
            tekstiKentät[0].setText("" + huoneenId);
        }
        else {
            tekstiKentät[0].setText("" + 0);
        }
        tekstiKentät[0].setToolTipText("ID:tä käytetään warppaamiseen huoneiden välillä");
        tekstiKentät[1].setText("");
        tekstiKentät[1].setToolTipText("Ei pakollinen");
        tekstiKentät[2].setText("" + h.annaKoko());
        tekstiKentät[2].setToolTipText("Huoneen leveys ja korkeus tileinä: esim. 10 tarkoittaa 10x10-huonetta.");
        paneli.add(new JLabel(tekstit[3]));
        huoneTiedostoValintaNappi = new JButton("Valitse huonetiedosto");
        huoneTiedostoValintaNappi.addActionListener(e -> {
            TiedostoSelain.launchAvaaTiedosto();
        });
        paneli.add(huoneTiedostoValintaNappi);

        JButton okNappi = new JButton("OK");
        okNappi.addMouseListener(new MouseAdapter() {
            public void mousePressed (MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) {
                    if (HuoneEditoriIkkuna.huoneKartta != null) {
                        if (HuoneEditoriIkkuna.huoneKartta.get(HuoneEditoriIkkuna.muokattavaHuone) != null) {
                            HuoneEditoriIkkuna.huoneKartta.get(HuoneEditoriIkkuna.muokattavaHuone).päivitäReunawarppienTiedot(asetettavaWarpVasen, asetettavaWarpVasenHuoneId, asetettavaWarpOikea, asetettavaWarpOikeaHuoneId, asetettavaWarpAlas, asetettavaWarpAlasHuoneId, asetettavaWarpYlös, asetettavaWarpYlösHuoneId);
                            if (tarkistaArvot()) {
                                HuoneEditoriIkkuna.ikkuna.setFocusable(true);
                                if (suunta != null) {
                                    ReunaWarppiIkkuna.luoReunaWarppiIkkuna(true, suunta, true, huoneenId);
                                }
                                else {
                                    HuoneEditoriIkkuna.muokattavaHuone = huoneenId;
                                    HuoneEditoriIkkuna.lataaHuoneKartasta(huoneenId, false);
                                }
                            }
                        }
                    }
                }
            }
        });

        JButton cancelNappi = new JButton("Peruuta");
        cancelNappi.addMouseListener(new MouseAdapter() {
            public void mousePressed (MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) {
                    ikkuna.dispose();
                    HuoneEditoriIkkuna.ikkuna.setFocusable(true);
                }
            }
        });

        paneli.add(okNappi);
        paneli.add(cancelNappi);

        SpringUtilities.makeCompactGrid(paneli, valintojenMäärä+1, 2, 6, 6, 6, 6);

        ikkuna = new JFrame("Luo huone " + suunta + " huoneesta " + HuoneEditoriIkkuna.muokattavaHuone);
        ikkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja_og.png").getImage());
        ikkuna.setBounds(100, 50, ikkunanLeveys, ikkunanKorkeus);
        ikkuna.setLayout(new BorderLayout());
        ikkuna.setVisible(true);
        ikkuna.setLocationRelativeTo(null);
        ikkuna.add(paneli, BorderLayout.CENTER);
        ikkuna.revalidate();
        ikkuna.repaint();
    }

    private class TiedostoSelain {

        public static void launchAvaaTiedosto() {
            // This method is invoked on the EDT thread
            JFXPanel fxPanel = new JFXPanel();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    fxPanel.setScene(luoTiedostonAvausIkkuna());
                }
            });
        }
    
        private static Scene luoTiedostonAvausIkkuna() {
            // This method is invoked on the JavaFX thread
            Group root = new Group();
            Scene scene = new Scene(root, 640, 360, javafx.scene.paint.Color.ALICEBLUE);
            Text text = new Text();
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialDirectory(new File("tiedostot/pelitiedostot/huoneet"));
                ExtensionFilter tiedostoPäätteet = new ExtensionFilter("Keimon seikkailupelin huone", "*.ksh");
                fileChooser.getExtensionFilters().add(tiedostoPäätteet);
                File avattuTiedosto = fileChooser.showOpenDialog(null);
                String huoneMerkkijonoina = "";
                Path path = FileSystems.getDefault().getPath(avattuTiedosto.getPath());
                Charset charset = Charset.forName("UTF-8");
                BufferedReader read = Files.newBufferedReader(path, charset);
                String tarkastettavaRivi = null;
                tarkastettavaRivi = read.readLine();
                while ((tarkastettavaRivi != null)) {
                    if (tarkastettavaRivi.startsWith("Huone ")) {
                        huoneMerkkijonoina = "";
                        while (tarkastettavaRivi != null) {
                            huoneMerkkijonoina += tarkastettavaRivi + "\n";
                            if (tarkastettavaRivi.startsWith("/Huone")) {
                                break;
                            }
                            tarkastettavaRivi = read.readLine();
                        }
                    }
                    else if (tarkastettavaRivi.startsWith("</KEIMO>")) {
                        break;
                    }
                    else {
                        tarkastettavaRivi = read.readLine();
                    }
                }
                huoneKSH = HuoneEditorinMetodit.luoHuoneMerkkijonosta(huoneMerkkijonoina, huoneenId);
                huoneTiedostoValintaNappi.setText(avattuTiedosto.getName());
                tekstiKentät[1].setText(huoneKSH.annaNimi());
                tekstiKentät[2].setText("" + huoneKSH.annaKoko());
                read.close();
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
            }
    
            root.getChildren().add(text);
    
            return scene;
        }
    }
}
