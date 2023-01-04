import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.io.*;

public class MukautusIkkuna {
    
    static final int ikkunanLeveys = 250;
    static final int ikkunanKorkeus = 260;
    static JFrame ikkuna;
    static String[] tekstit = {"Huoneen ID", "Huoneen nimi", "Kentän koko", "Suklaiden määrä", "Makkaroiden määrä", "Vihollisten määrä", "Tehtäväitemit"};
    static int valintojenMäärä = tekstit.length;
    static JTextField[] tekstiKentät = new JTextField[valintojenMäärä];
    static KenttäKohde[][] huoneenSisältö = new KenttäKohde[Main.kentänKoko][Main.kentänKoko];
    static ArrayList<KenttäKohde> huoneenSisältöLista = new ArrayList<KenttäKohde>();
    static ArrayList<Maasto> huoneenMaastoLista = new ArrayList<Maasto>();

    static class MaastoGeneraattoriIkkuna {

        static final int ikkunanLeveys = 420;
        static final int ikkunanKorkeus = 165;
        static JFrame maastoIkkuna;
        static String[] tekstit = {"Luo objektikenttä kuvan avulla", "Luo maasto kuvan avulla", "Tarkastele maastoa"};
        static int valintojenMäärä = tekstit.length;
        static JButton valitseKuva, valitseKuva2;
        static JLabel maastonTarkastelu;

        static int[][] objektiKuvanVäriArvot;
        static int[][] objektiKuvanVäriArvotPunainen;
        static int[][] objektiKuvanVäriArvotVihreä;
        static int[][] objektiKuvanVäriArvotSininen;

        static int[][] maastoKuvanVäriArvot;
        static int[][] maastoKuvanVäriArvotPunainen;
        static int[][] maastoKuvanVäriArvotVihreä;
        static int[][] maastoKuvanVäriArvotSininen;

        static Maasto[][] kuvaMaasto;
        static KenttäKohde[][] kuvaKenttä;

        static boolean objektitValittu = false;
        static boolean maastoValittu = false;
    
        static void tarkistaArvot() {
            kuvaKenttä = new KenttäKohde[10][10];
            kuvaMaasto = new Maasto[10][10];
            try {
                if (objektitValittu) {
                    for (int i = 0; i < objektiKuvanVäriArvot.length; i++) {
                        for (int j = 0; j < objektiKuvanVäriArvot.length; j++) {
                            System.out.println("" + j + ", " + i + " - Väri: " + objektiKuvanVäriArvot[j][i]);
                            if (objektiKuvanVäriArvot[j][i] == 0x00D0C030) {
                                kuvaKenttä[j][i] = new Avain(j, i);
                            }
                            else if (objektiKuvanVäriArvot[j][i] == 0x001E1107) {
                                kuvaKenttä[j][i] = new Hiili(j, i);
                            }
                            else if (objektiKuvanVäriArvot[j][i] == 0x001B2644) {
                                kuvaKenttä[j][i] = new Kaasupullo(j, i);
                            }
                            else if (objektiKuvanVäriArvot[j][i] == 0x0032416B) {
                                kuvaKenttä[j][i] = new Kaasusytytin(j, i, "tyhjä");
                            }
                            else if (objektiKuvanVäriArvot[j][i] == 0x00666666) {
                                kuvaKenttä[j][i] = new Kilpi(j, i);
                            }
                            else if (objektiKuvanVäriArvot[j][i] == 0x005B3318) {
                                kuvaKenttä[j][i] = new Kirstu(j, i);
                            }
                            else if (objektiKuvanVäriArvot[j][i] == 0x00BA5E6B) {
                                kuvaKenttä[j][i] = new Makkara(j, i);
                            }
                            else if (objektiKuvanVäriArvot[j][i] == 0x00232330) {
                                kuvaKenttä[j][i] = new Nuotio(j, i);
                            }
                            else if (objektiKuvanVäriArvot[j][i] == 0x00CE631C) {
                                kuvaKenttä[j][i] = new ReunaWarppi(j, i, 0, 0, 0, Warp.Suunta.OIKEA);
                            }
                            else if (objektiKuvanVäriArvot[j][i] == 0x00AD1616) {
                                kuvaKenttä[j][i] = new PahaVihu(j, i);
                            }
                            else if (objektiKuvanVäriArvot[j][i] == 0x00E5DD72) {
                                kuvaKenttä[j][i] = new Paperi(j, i);
                            }
                            else if (objektiKuvanVäriArvot[j][i] == 0x0000FF00) {
                                kuvaKenttä[j][i] = null;
                            }
                            else if (objektiKuvanVäriArvot[j][i] == 0x00AA4949) {
                                kuvaKenttä[j][i] = new PikkuVihu(j, i);
                            }
                            else if (objektiKuvanVäriArvot[j][i] == 0x007F4A00) {
                                kuvaKenttä[j][i] = new Suklaalevy(j, i);
                            }
                            else if (objektiKuvanVäriArvot[j][i] == 0x0029722E) {
                                kuvaKenttä[j][i] = new Vesiämpäri(j, i);
                            }
                            else if (objektiKuvanVäriArvot[j][i] == 0x00006806) {
                                kuvaKenttä[j][i] = new Ämpärikone(j, i);
                            }
                            else {
                                kuvaKenttä[j][i] = null;
                            }
                        }
                    }
                }
                if (maastoValittu) {
                    for (int i = 0; i < maastoKuvanVäriArvot.length; i++) {
                        for (int j = 0; j < maastoKuvanVäriArvot.length; j++) {
                            if (maastoKuvanVäriArvotPunainen[j][i] < 20 && maastoKuvanVäriArvotVihreä[j][i] < 150 && maastoKuvanVäriArvotSininen[j][i] > 200) {
                                kuvaMaasto[j][i] = new Vesi();
                            }
                            else if (maastoKuvanVäriArvotPunainen[j][i] > 200 && maastoKuvanVäriArvotVihreä[j][i] > 200 && maastoKuvanVäriArvotSininen[j][i] < 20) {
                                kuvaMaasto[j][i] = new Hiekka();
                            }
                            else {
                                kuvaMaasto[j][i] = null;
                            }
                        }
                    }
                }
                asetaArvot();
            }
            catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Virheellinen syöte!", "Virheellinen syöte!", JOptionPane.ERROR_MESSAGE);
            }
            catch (ArrayIndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(null, "Nyt näyttää siltä, että jotain on mennyt särki!\n\nIlmoitathan kehittäjille, mitä teit, että sait tämän tapahtumaan.", "Array Index out of Bounds", JOptionPane.ERROR_MESSAGE);
            }
            catch (NullPointerException e) {
                JOptionPane.showMessageDialog(null, "Ei valittua tiedostoa.", "Ei voi ladata!", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    
        static void asetaArvot() {
            Main.maastokenttä = kuvaMaasto;
            Main.pelikenttä = kuvaKenttä;
            Main.korjaaObjektiDesync = true;
        }
    
        static void luoMaastoGeneraattoriIkkuna() {
            
            JPanel paneli = new JPanel(new SpringLayout());
    
            valitseKuva = new JButton("Valitse kuva objekteja varten");
            valitseKuva.addMouseListener(new MouseAdapter() {
                public void mousePressed (MouseEvent e) {
                    if (!SwingUtilities.isRightMouseButton(e)) {
                        JOptionPane.showMessageDialog(null, "Tämän työkalun avulla voit luoda maaston png-kuvan värien perusteella.\nKuvan täytyy olla 10 x 10 -kokoinen.", "Luo maasto kuvan perusteella", JOptionPane.INFORMATION_MESSAGE);
                        JFileChooser tiedostoSelain = new JFileChooser(".\\");
                        FileNameExtensionFilter tiedostoSuodatin = new FileNameExtensionFilter("Kuvatiedosto (.png)", "png");
                        tiedostoSelain.setFileFilter(tiedostoSuodatin);
                        int valinta = tiedostoSelain.showOpenDialog(maastoIkkuna);
                        if (valinta == JFileChooser.APPROVE_OPTION) {
                            String väriArvot = "";
                            File tiedosto = tiedostoSelain.getSelectedFile();
                            try {
                                BufferedImage kuva = ImageIO.read(tiedosto);
                                objektiKuvanVäriArvot = new int[kuva.getWidth()][kuva.getHeight()];
                                objektiKuvanVäriArvotPunainen = new int[kuva.getWidth()][kuva.getHeight()];
                                objektiKuvanVäriArvotVihreä = new int[kuva.getWidth()][kuva.getHeight()];
                                objektiKuvanVäriArvotSininen = new int[kuva.getWidth()][kuva.getHeight()];
                                if (kuva.getWidth() == 10 && kuva.getHeight() == 10) {
                                    for (int i = 0; i < kuva.getHeight(); i++) {
                                        for (int j = 0; j < kuva.getWidth(); j++) {
                                            objektiKuvanVäriArvot[j][i] = kuva.getRGB(j, i);
                                            objektiKuvanVäriArvotPunainen[j][i] = (objektiKuvanVäriArvot[j][i] & 0x00ff0000) >> 16;
                                            objektiKuvanVäriArvotVihreä[j][i] = (objektiKuvanVäriArvot[j][i] & 0x0000ff00) >> 8;
                                            objektiKuvanVäriArvotSininen[j][i] = (objektiKuvanVäriArvot[j][i] & 0x000000ff);
                                            objektiKuvanVäriArvot[j][i] = objektiKuvanVäriArvotSininen[j][i] + 256 * objektiKuvanVäriArvotVihreä[j][i] + 65536 * objektiKuvanVäriArvotPunainen[j][i];
                                            väriArvot += "Pikseli " + j + "_" + i + " - Värit: " + objektiKuvanVäriArvot[j][i] + " - Punainen: " + objektiKuvanVäriArvotPunainen[j][i] + ", Vihreä: " + objektiKuvanVäriArvotVihreä[j][i] + ", Sininen: " + objektiKuvanVäriArvotSininen[j][i] + ";\n";
                                        }
                                    }
                                    objektitValittu = true;
                                    System.out.println(väriArvot);
                                    JOptionPane.showMessageDialog(null, "<html><p>" + väriArvot + "</p></html>", "Väriarvot", JOptionPane.INFORMATION_MESSAGE);
                                }
                                else {
                                    JOptionPane.showMessageDialog(null, "Vain 10 x 10 -kuvat kelpaavat.", "Virheellinen kuvatiedosto", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                            catch (IOException ioe) {
                                JOptionPane.showMessageDialog(null, "Virhe tiedoston käsittelyssä.", "Virhe", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            });
            valitseKuva2 = new JButton("Valitse kuva maastoa varten");
            valitseKuva2.addMouseListener(new MouseAdapter() {
                public void mousePressed (MouseEvent e) {
                    if (!SwingUtilities.isRightMouseButton(e)) {
                        JOptionPane.showMessageDialog(null, "Tämän työkalun avulla voit luoda maaston png-kuvan värien perusteella.\nKuvan täytyy olla 10 x 10 -kokoinen.", "Luo maasto kuvan perusteella", JOptionPane.INFORMATION_MESSAGE);
                        JFileChooser tiedostoSelain = new JFileChooser(".\\");
                        FileNameExtensionFilter tiedostoSuodatin = new FileNameExtensionFilter("Kuvatiedosto (.png)", "png");
                        tiedostoSelain.setFileFilter(tiedostoSuodatin);
                        int valinta = tiedostoSelain.showOpenDialog(maastoIkkuna);
                        if (valinta == JFileChooser.APPROVE_OPTION) {
                            String väriArvot = "";
                            File tiedosto = tiedostoSelain.getSelectedFile();
                            try {
                                BufferedImage kuva = ImageIO.read(tiedosto);
                                maastoKuvanVäriArvot = new int[kuva.getWidth()][kuva.getHeight()];
                                maastoKuvanVäriArvotPunainen = new int[kuva.getWidth()][kuva.getHeight()];
                                maastoKuvanVäriArvotVihreä = new int[kuva.getWidth()][kuva.getHeight()];
                                maastoKuvanVäriArvotSininen = new int[kuva.getWidth()][kuva.getHeight()];
                                if (kuva.getWidth() == 10 && kuva.getHeight() == 10) {
                                    for (int i = 0; i < kuva.getHeight(); i++) {
                                        for (int j = 0; j < kuva.getWidth(); j++) {
                                            maastoKuvanVäriArvot[j][i] = kuva.getRGB(j, i);
                                            maastoKuvanVäriArvotPunainen[j][i] = (maastoKuvanVäriArvot[j][i] & 0x00ff0000) >> 16;
                                            maastoKuvanVäriArvotVihreä[j][i] = (maastoKuvanVäriArvot[j][i] & 0x0000ff00) >> 8;
                                            maastoKuvanVäriArvotSininen[j][i] = (maastoKuvanVäriArvot[j][i] & 0x000000ff);
                                            väriArvot += "Pikseli " + j + "_" + i + " - Punainen: " + maastoKuvanVäriArvotPunainen[j][i] + ", Vihreä: " + maastoKuvanVäriArvotVihreä[j][i] + ", Sininen: " + maastoKuvanVäriArvotSininen[j][i] + ";\n";
                                        }
                                    }
                                    maastoValittu = true;
                                    System.out.println(väriArvot);
                                    JOptionPane.showMessageDialog(null, "<html><p>" + väriArvot + "</p></html>", "Väriarvot", JOptionPane.INFORMATION_MESSAGE);
                                }
                                else {
                                    JOptionPane.showMessageDialog(null, "Vain 10 x 10 -kuvat kelpaavat.", "Virheellinen kuvatiedosto", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                            catch (IOException ioe) {
                                JOptionPane.showMessageDialog(null, "Virhe tiedoston käsittelyssä.", "Virhe", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            });
            JLabel teksti0 = new JLabel(tekstit[0], JLabel.TRAILING);
            teksti0.setLabelFor(valitseKuva);
            paneli.add(teksti0);
            paneli.add(valitseKuva);

            JLabel teksti1 = new JLabel(tekstit[1], JLabel.TRAILING);
            teksti1.setLabelFor(valitseKuva2);
            paneli.add(teksti1);
            paneli.add(valitseKuva2);
    
            maastonTarkastelu = new JLabel();
            JLabel teksti2 = new JLabel(tekstit[2], JLabel.TRAILING);
            teksti2.setLabelFor(maastonTarkastelu);
            paneli.add(teksti2);
            paneli.add(maastonTarkastelu);
            
    
            JButton okNappi = new JButton("Aseta nykyiseksi kentäksi");
            okNappi.addMouseListener(new MouseAdapter() {
                public void mousePressed (MouseEvent e) {
                    if (!SwingUtilities.isRightMouseButton(e)) {
                        tarkistaArvot();
                    }
                }
            });
    
            JButton cancelNappi = new JButton("Peruuta");
            cancelNappi.addMouseListener(new MouseAdapter() {
                public void mousePressed (MouseEvent e) {
                    if (!SwingUtilities.isRightMouseButton(e)) {
                        maastoIkkuna.dispose();
                    }
                }
            });
    
            paneli.add(okNappi);
            paneli.add(cancelNappi);
    
            SpringUtilities.makeCompactGrid(paneli, 4, 2, 6, 6, 6, 6);
    
            maastoIkkuna = new JFrame();
            maastoIkkuna.setTitle("Maastogeneraattori");
            maastoIkkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja.png").getImage());
            maastoIkkuna.setBounds(PääIkkuna.ikkuna.getBounds().x + 100, PääIkkuna.ikkuna.getBounds().y + 50, ikkunanLeveys, ikkunanKorkeus);
            maastoIkkuna.setLayout(new BorderLayout());
            maastoIkkuna.setVisible(true);
            maastoIkkuna.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            maastoIkkuna.add(paneli, BorderLayout.CENTER);
            maastoIkkuna.revalidate();
            maastoIkkuna.repaint();
        }    

    }

    static void tarkistaArvot() {
        try {
            int huoneenId = Integer.parseInt(tekstiKentät[0].getText());
            String huoneenNimi = tekstiKentät[1].getText();
            int kentänKoko = Integer.parseInt(tekstiKentät[2].getText());
            int suklaidenMäärä = Integer.parseInt(tekstiKentät[3].getText());
            int makkaroidenMäärä = Integer.parseInt(tekstiKentät[4].getText());
            int vihujenMäärä = Integer.parseInt(tekstiKentät[5].getText());
            boolean tehtäväItemit = true;
            if (huoneenId < 0) {
                JOptionPane.showMessageDialog(null, "Negatiivinen ID ei kelpaa.", "Virheellinen ID!", JOptionPane.ERROR_MESSAGE);
            }
            else if (huoneenNimi.contains("(") || huoneenNimi.contains(")")) {
                CustomViestiIkkunat.SulkumerkkiVaroitus.showDialog();
            }
            else if (Main.huoneKartta.containsKey(huoneenId)) {
                JOptionPane.showMessageDialog(null, "Huone ID:llä " + huoneenId + " löytyy jo.", "Virheellinen ID!", JOptionPane.ERROR_MESSAGE);
            }
            else {
                if (kentänKoko > 15) {
                    int kentänKokoVaroitus = CustomViestiIkkunat.IsoKenttäVaroitus.showDialog();
                    if (kentänKokoVaroitus == JOptionPane.OK_OPTION) {
                        asetaArvot(huoneenId, huoneenNimi, kentänKoko, suklaidenMäärä, makkaroidenMäärä, vihujenMäärä, tehtäväItemit);
                    }
                }
                else {
                    asetaArvot(huoneenId, huoneenNimi, kentänKoko, suklaidenMäärä, makkaroidenMäärä, vihujenMäärä, tehtäväItemit);
                }
            }
            
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Virheellinen syöte!", "Virheellinen syöte!", JOptionPane.ERROR_MESSAGE);
        }
    }

    static void asetaArvot(int huoneenId, String huoneenNimi, int asetettuKentänKoko, int suklaidenMäärä, int makkaroidenMäärä, int vihujenMäärä, boolean tehtäväItemit) {
        //PääIkkuna.uusiIkkuna = true;
        //ikkuna.dispose();
        Main.uusiKentänKoko = asetettuKentänKoko;
        PelinAsetukset.suklaidenMäärä = suklaidenMäärä;
        PelinAsetukset.makkaroidenMäärä = makkaroidenMäärä;
        PelinAsetukset.vihujenMäärä = vihujenMäärä;

        if (tehtäväItemit) {
            huoneenSisältöLista.add(new Avain());
            huoneenSisältöLista.add(new Kaasupullo());
            huoneenSisältöLista.add(new Kaasusytytin("tyhjä"));
            huoneenSisältöLista.add(new Hiili());
            huoneenSisältöLista.add(new Paperi());
            huoneenSisältöLista.add(new Vesiämpäri());
        }
        for (int i = 0; i < suklaidenMäärä; i++) {
            huoneenSisältöLista.add(new Suklaalevy());
        }
        for (int i = 0; i < makkaroidenMäärä; i++) {
            huoneenSisältöLista.add(new Makkara());
        }
        for (int i = 0; i < vihujenMäärä; i++) {
            huoneenSisältöLista.add(new PikkuVihu());
        }
        
        //for (KenttäKohde[] k : huoneenSisältö){
        //    for (KenttäKohde kk : k){
        //        huoneenSisältöLista.add(kk);
        //    }
        //}
        ikkuna.dispose();
        String huoneenSisältöString = "";
        for (KenttäKohde k : huoneenSisältöLista) {
            huoneenSisältöString += k.annaNimi() + ", ";
        }
        System.out.println("Huoneeseen asetetaan " + huoneenSisältöString);
        Peli.luoHuone(huoneenId, huoneenNimi, new ImageIcon().getImage(), "Oma alue", huoneenSisältöLista, huoneenMaastoLista, false, "");
        Main.huoneVaihdettava = true;
        Main.uusiHuone = huoneenId;
        huoneenSisältöLista.removeAll(huoneenSisältöLista);
        huoneenMaastoLista.removeAll(huoneenMaastoLista);
    }

    static void luoMukautusikkuna() {
        
        JPanel paneli = new JPanel(new SpringLayout());
        for (int i = 0; i < valintojenMäärä; i++) {
            JLabel teksti = new JLabel(tekstit[i], JLabel.TRAILING);
            paneli.add(teksti);
            tekstiKentät[i] = new JTextField("" + Main.kentänKoko,10);
            teksti.setLabelFor(tekstiKentät[i]);
            paneli.add(tekstiKentät[i]);
        }
        tekstiKentät[0].setText("" + Main.huoneidenMäärä);
        tekstiKentät[0].setToolTipText("ID:tä käytetään warppaamiseen huoneiden välillä");
        tekstiKentät[1].setText("");
        tekstiKentät[1].setToolTipText("Ei pakollinen");
        tekstiKentät[2].setText("" + Main.kentänKoko);
        tekstiKentät[2].setToolTipText("Suurin sallittu koko: 10");
        tekstiKentät[3].setText("" + PelinAsetukset.suklaidenMäärä);
        tekstiKentät[3].setToolTipText("Näin monta suklaata spawnataan satunnaiseen ruutuun.");
        tekstiKentät[4].setText("" + PelinAsetukset.makkaroidenMäärä);
        tekstiKentät[4].setToolTipText("Näin monta makkaraa spawnataan satunnaiseen ruutuun.");
        tekstiKentät[5].setText("" + PelinAsetukset.vihujenMäärä);
        tekstiKentät[5].setToolTipText("Näin monta pikkuvihua spawnataan satunnaiseen ruutuun.");
        tekstiKentät[6].setText("Kyllä");
        tekstiKentät[6].setEditable(false);
        tekstiKentät[6].setToolTipText("Avain, Hiili, Paperi, Kaasusytytin, Kaasupullo");

        JButton okNappi = new JButton("OK");
        okNappi.addMouseListener(new MouseAdapter() {
            public void mousePressed (MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) {
                    tarkistaArvot();
                }
            }
        });

        JButton cancelNappi = new JButton("Peruuta");
        cancelNappi.addMouseListener(new MouseAdapter() {
            public void mousePressed (MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) {
                    ikkuna.dispose();
                }
            }
        });

        paneli.add(okNappi);
        paneli.add(cancelNappi);

        SpringUtilities.makeCompactGrid(paneli, 8, 2, 6, 6, 6, 6);

        ikkuna = new JFrame("Luo huone");
        ikkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja.png").getImage());
        ikkuna.setBounds(PääIkkuna.ikkuna.getBounds().x + 100, PääIkkuna.ikkuna.getBounds().y + 50, ikkunanLeveys, ikkunanKorkeus);
        ikkuna.setLayout(new BorderLayout());
        ikkuna.setVisible(true);
        ikkuna.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ikkuna.add(paneli, BorderLayout.CENTER);
        ikkuna.revalidate();
        ikkuna.repaint();

        
    }
}
