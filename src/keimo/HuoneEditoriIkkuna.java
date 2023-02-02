package keimo;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.event.*;
import javax.swing.UIManager;

import java.awt.*;
import java.awt.event.*;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.*;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.Dimension;
import java.io.*;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.charset.*;
import java.nio.file.*;

import keimo.Kenttäkohteet.Warp.Suunta;
import keimo.Kenttäkohteet.*;
import keimo.Maastot.*;
import keimo.NPCt.*;
import keimo.NPCt.Vihollinen.LiikeTapa;

public class HuoneEditoriIkkuna {

    static final int esineenKokoPx = 64;
    static final int pelaajanKokoPx = 64;
    static int ikkunanLeveys;
    static int ikkunanKorkeus;
    static JFrame ikkuna;
    static JMenuBar yläPalkki;
    static JMenu tiedosto, peli;
    static JMenuItem uusi, avaa, tallenna;
    static JMenuItem kokeilePelissä;
    static JPanel yläPaneeli, yläVasenPaneeli, yläOikeaPaneeli;
    static JButton hyväksyNappi;
    static JLabel huoneenNimiTekstiKenttäLabel, huoneenAlueTekstiKenttäLabel, huoneenKuvaTekstiKenttäLabel, huoneenDialogiTekstiKenttäLabel;
    static JTextField huoneenNimiTekstiKenttä, huoneenAlueTekstiKenttä;
    static JButton huoneenNimiTekstiKenttäNappi, huoneenAlueTekstiKenttäNappi, huoneenKuvaTekstiKenttäNappi, huoneenDialogiTekstiKenttäNappi;
    static JButton huoneenKuvaValintaNappi, huoneenDialogiValintaNappi;

    static JTabbedPane välilehdet;
    static JPanel objektiEditointiKenttäPaneli, maastoEditointiKenttäPaneli, npcEditointiKenttäPaneli, editointiKenttäAlue;
    static JPanel peliAluePaneli;
    static JPanel huoneInfoPaneli, huoneenVaihtoPaneli, huoneenNimiPaneli;
    static JLabel huoneenNimiLabel;
    static JButton huoneenVaihtoNappiVasen, huoneenVaihtoNappiOikea;
    static JLabel huoneInfoLabel;
    static String[] esineLista = {"Avain", "Hiili", "Huume", "Juhani", "Kaasupullo", "Kaasusytytin", "Kilpi", "Kirstu", "Makkara", "Nuotio", "Pahavihu", "Paperi", "Pesäpallomaila", "Pikkuvihu", "Oviruutu", "Seteli", "Suklaalevy", "Vesiämpäri", "Ämpärikone"};
    static String[] maastoLista = {"Hiekka", "Seinä", "Vesi"};
    static String[] npcNimiLista = {"Pikkuvihu", "Pahavihu"};
    static JComboBox<String> esineValikko;
    static JComboBox<Object> maastoValikko;
    static JComboBox<String> npcValikko;
    static HashMap<Integer, Huone> huoneKartta = new HashMap<Integer, Huone>();
    static int muokattavaHuone = 0;
    static String huoneenNimi = "";
    static String huoneenAlue = "";
    static ImageIcon huoneenTaustakuva;
    static String huoneenAlkuDialogiTeksti = "";

    static JButton[][] kenttäKohteenKuvake, maastoKohteenKuvake, npcKohteenKuvake;
    static KenttäKohde[][] objektiKenttä;
    static Maasto[][] maastoKenttä;
    static NPC[][] npcKenttä;
    static JLabel pelaajaLabel, taustaLabel;
    static JPanel hud;
    static CardLayout crd;
    static JPanel kortit;
    static boolean reunatNäkyvissä = true;
    static boolean vaatiiPäivityksen = true, vaatiiKentänPäivityksen = false;

    static JPanel tekstiPaneli, infoPaneli;
    static JLabel hudTeksti;
    static JLabel[] kontrolliInfoLabel = new JLabel[9];
    
    static void luoEditoriIkkuna() {
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        ikkunanLeveys = esineenKokoPx * Peli.kentänKoko + 35;
        ikkunanKorkeus = esineenKokoPx * Peli.kentänKoko + 310;
        kenttäKohteenKuvake = new JButton[Peli.kentänKoko][Peli.kentänKoko];
        maastoKohteenKuvake = new JButton[Peli.kentänKoko][Peli.kentänKoko];
        npcKohteenKuvake = new JButton[Peli.kentänKoko][Peli.kentänKoko];
        
        ikkuna = new JFrame("Huone-editori v0.3");
        ikkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja_og.png").getImage());
        ikkuna.setBounds(600, 100, ikkunanLeveys, ikkunanKorkeus);
        //ikkuna.setLayout(new FlowLayout(FlowLayout.TRAILING));
        ikkuna.setLayout(new BorderLayout());
        ikkuna.setBackground(Color.black);
        ikkuna.setVisible(true);
        ikkuna.revalidate();
        ikkuna.repaint();

        uusi = new JMenuItem("Uusi", new ImageIcon("./kuvat/pelaaja32.png"));
        uusi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int tyhjennysValinta = CustomViestiIkkunat.EditorinTyhjennys.showDialog("Haluatko varmasti tyhjentää kaikki huoneet?", "Uusi pohja");
                if (tyhjennysValinta == JOptionPane.YES_OPTION) {
                    for (int i = 0; i < Peli.kentänKoko; i++) {
                        for (int j = 0; j < Peli.kentänKoko; j++) {
                            objektiKenttä[j][i] = null;
                            maastoKenttä[j][i] = null;
                        }
                    }
                    huoneKartta.clear();
                    huoneenNimi = "";
                    huoneenAlue = "";
                    huoneenNimiLabel.setText(huoneenNimi + " (" + huoneenAlue + ")");
                }
            }
        });

        avaa = new JMenuItem("Avaa");
        avaa.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser tiedostoSelain = new JFileChooser(".\\");
                    FileNameExtensionFilter tiedostoSuodatin = new FileNameExtensionFilter("Keimon Seikkailupelin Tiedosto (.kst)", "kst");
                    tiedostoSelain.setFileFilter(tiedostoSuodatin);
                    int valinta = tiedostoSelain.showOpenDialog(ikkuna);
                    if (valinta == JFileChooser.APPROVE_OPTION) {
                        File tiedosto = tiedostoSelain.getSelectedFile();
                        String[] huoneetMerkkijonoina;
                        int huoneidenMääräTiedostossa = 0;
                        int huoneenIdTiedostossa;
                        Path path = FileSystems.getDefault().getPath(tiedosto.getPath());
                        Charset charset = Charset.forName("UTF-8");
                        //Scanner sc = new Scanner(tiedosto);
                        BufferedReader read = Files.newBufferedReader(path, charset);
                        String tarkastettavaRivi = null;
                        if ((tarkastettavaRivi = read.readLine()) != null) {
                            tarkastettavaRivi = read.readLine();
                            if (!tarkastettavaRivi.startsWith("<KEIMO>")) {
                                //System.out.println(tarkastettavaRivi);
                                System.out.println(tarkastettavaRivi);
                                //throw new FileNotFoundException();
                            }
                        }
                        while ((tarkastettavaRivi = read.readLine()) != null) {
                            //tarkastettavaRivi = read.readLine();
                            if (tarkastettavaRivi.startsWith("Huone ")) {
                                huoneidenMääräTiedostossa++;
                            }
                        }
                        //sc.close();
                        huoneetMerkkijonoina = new String[huoneidenMääräTiedostossa];
                        huoneidenMääräTiedostossa = 0;
                        //sc = new Scanner(tiedosto);
                        read = Files.newBufferedReader(path, charset);
                        tarkastettavaRivi = read.readLine();
                        while ((tarkastettavaRivi != null)) {
                            //tarkastettavaRivi = read.readLine();
                            if (tarkastettavaRivi.startsWith("Huone ")) {
                                huoneenIdTiedostossa = Integer.parseInt(tarkastettavaRivi.substring(6, tarkastettavaRivi.length()-1));
                                huoneidenMääräTiedostossa++;
                                huoneetMerkkijonoina[huoneidenMääräTiedostossa-1] = "";
                                while (tarkastettavaRivi != null) {
                                    huoneetMerkkijonoina[huoneidenMääräTiedostossa-1] += tarkastettavaRivi + "\n";
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
                            System.out.println(tarkastettavaRivi);
                        }
                        for (String s : huoneetMerkkijonoina) {
                            System.out.println("huone: " + s);
                        }
                        huoneKartta = HuoneEditorinMetodit.luoHuoneKarttaMerkkijonosta(huoneetMerkkijonoina);
                        lataaHuoneKartasta(muokattavaHuone);
                    }
                }
                catch (FileNotFoundException fnfe) {
                    JOptionPane.showMessageDialog(null, "Tiedosto puuttuu tai on virheellinen.\n\nKeimon seikkailupeliin tarkoitetut tiedostot alkavat prefiksillä <KEIMO>.", "Tiedostovirhe", JOptionPane.ERROR_MESSAGE);
                }
                catch (IOException ioe) {

                }
            }
        });

        tallenna = new JMenuItem("Tallenna");
        tallenna.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //asetaUusiHuoneKarttaan(muokattavaHuone);
                tallennaTiedostoon(HuoneEditorinMetodit.luoMerkkijonotHuonekartasta(huoneKartta));
            }
        });

        tiedosto = new JMenu("Tiedosto");
        tiedosto.add(uusi);
        tiedosto.add(avaa);
        tiedosto.add(tallenna);

        kokeilePelissä = new JMenuItem("Kokeile pelissä");
        kokeilePelissä.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                asetaUusiHuoneKarttaan(muokattavaHuone);
                ikkuna.dispose();
                Peli.huoneKartta = huoneKartta;
                Peli.uusiHuone = 0;
                Peli.huoneVaihdettava = true;
                try {
                    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                }
                catch (Exception ex) {

                }
            }
        });

        peli = new JMenu("Peli");
        peli.add(kokeilePelissä);

        yläPalkki = new JMenuBar();
        yläPalkki.setPreferredSize(new Dimension(ikkunanLeveys -20,20));
        yläPalkki.add(tiedosto);
        yläPalkki.add(peli);

        huoneenNimiTekstiKenttäLabel = new JLabel("Huoneen nimi: ");
        huoneenNimiTekstiKenttä = new JTextField();
        huoneenNimiTekstiKenttä.setPreferredSize(new Dimension(60, 20));
        huoneenAlueTekstiKenttäLabel = new JLabel("Huoneen alue: ");
        huoneenAlueTekstiKenttä = new JTextField();
        huoneenAlueTekstiKenttä.setPreferredSize(new Dimension(60, 20));
        huoneenKuvaTekstiKenttäLabel = new JLabel("Huoneen tausta: ");
        huoneenKuvaValintaNappi = new JButton("Valitse kuva");
        huoneenKuvaValintaNappi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser tiedostoSelain = new JFileChooser(".\\");
                FileNameExtensionFilter tiedostoSuodatin = new FileNameExtensionFilter("Kuvatiedosto (.png .jpg .gif)", "png", "jpg", "gif");
                tiedostoSelain.setFileFilter(tiedostoSuodatin);
                int valinta = tiedostoSelain.showOpenDialog(ikkuna);
                if (valinta == JFileChooser.APPROVE_OPTION) {
                    File tiedosto = tiedostoSelain.getSelectedFile();
                    huoneenTaustakuva = new ImageIcon(tiedosto.getPath());
                }
            }
        });
        huoneenDialogiTekstiKenttäLabel = new JLabel("Huoneen alkudialogi: ");
        huoneenDialogiValintaNappi = new JButton("Valitse teksti");
        huoneenDialogiValintaNappi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser tiedostoSelain = new JFileChooser(".\\");
                    FileNameExtensionFilter tiedostoSuodatin = new FileNameExtensionFilter("Tekstitiedosto (.txt)", "txt");
                    tiedostoSelain.setFileFilter(tiedostoSuodatin);
                    int valinta = tiedostoSelain.showOpenDialog(ikkuna);
                    if (valinta == JFileChooser.APPROVE_OPTION) {
                        File tiedosto = tiedostoSelain.getSelectedFile();
                        Scanner sc = new Scanner(tiedosto);
                        while (sc.hasNext()) {
                            huoneenAlkuDialogiTeksti += sc.next();
                        }
                        sc.close();
                    }
                }
                catch (IOException ioe) {

                }
            }
        });
        //huoneenDialogiTekstiKenttäNappi = new JButton("Aseta");

        yläVasenPaneeli = new JPanel();
        yläVasenPaneeli.setLayout(new SpringLayout());
        yläVasenPaneeli.setMaximumSize(new Dimension(300, 60));
        yläVasenPaneeli.add(huoneenNimiTekstiKenttäLabel);
        yläVasenPaneeli.add(huoneenNimiTekstiKenttä);
        yläVasenPaneeli.add(huoneenKuvaTekstiKenttäLabel);
        yläVasenPaneeli.add(huoneenKuvaValintaNappi);
        yläVasenPaneeli.add(huoneenAlueTekstiKenttäLabel);
        yläVasenPaneeli.add(huoneenAlueTekstiKenttä);
        yläVasenPaneeli.add(huoneenDialogiTekstiKenttäLabel);
        yläVasenPaneeli.add(huoneenDialogiValintaNappi);
        SpringUtilities.makeCompactGrid(yläVasenPaneeli, 2, 4, 0, 0, 6, 6);

        hyväksyNappi = new JButton("Hyväksy muutokset");
        hyväksyNappi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                huoneenNimi = huoneenNimiTekstiKenttä.getText();
                huoneenAlue = huoneenAlueTekstiKenttä.getText();
                huoneKartta.get(muokattavaHuone).päivitäNimiJaAlue(huoneenNimi, huoneenAlue);
                huoneenNimiLabel.setText(huoneenNimi + " (" + huoneenAlue + ")");
            }
        });

        yläOikeaPaneeli = new JPanel();
        yläOikeaPaneeli.setLayout(new BorderLayout());
        yläOikeaPaneeli.add(hyväksyNappi, BorderLayout.CENTER);

        yläPaneeli = new JPanel();
        yläPaneeli.setLayout(new BorderLayout());
        yläPaneeli.add(yläVasenPaneeli, BorderLayout.WEST);
        yläPaneeli.add(yläOikeaPaneeli, BorderLayout.EAST);
        yläPaneeli.setPreferredSize(new Dimension(ikkunanLeveys -30, 60));
        yläPaneeli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        yläPaneeli.revalidate();
        yläPaneeli.repaint();

        esineValikko = new JComboBox<String>(esineLista);
        maastoValikko = new JComboBox<Object>(listaaMaastot("tiedostot/kuvat/maasto").toArray());
        npcValikko = new JComboBox<String>(npcNimiLista);

        huoneenNimiLabel = new JLabel("Huone (alue)");
        huoneenNimiPaneli = new JPanel();
        huoneenNimiPaneli.add(huoneenNimiLabel);

        huoneInfoLabel = new JLabel("Huone");
        huoneInfoLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
        huoneInfoLabel.setHorizontalAlignment(JLabel.CENTER);

        huoneenVaihtoNappiVasen = new JButton("<-");
        huoneenVaihtoNappiVasen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (muokattavaHuone <= 0) {
                    
                }
                else {
                    int uusiHuone = muokattavaHuone -1;
                    vaihdaHuonetta(muokattavaHuone, uusiHuone);
                    muokattavaHuone--;
                }
            }
        });
        huoneenVaihtoNappiOikea = new JButton("->");;
        huoneenVaihtoNappiOikea.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int uusiHuone = muokattavaHuone +1;
                vaihdaHuonetta(muokattavaHuone, uusiHuone);
                muokattavaHuone++;
            }
        });

        huoneenVaihtoPaneli = new JPanel(new SpringLayout());
        huoneenVaihtoPaneli.add(huoneenVaihtoNappiVasen);
        huoneenVaihtoPaneli.add(huoneInfoLabel);
        huoneenVaihtoPaneli.add(huoneenVaihtoNappiOikea);
        huoneenVaihtoPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        SpringUtilities.makeCompactGrid(huoneenVaihtoPaneli, 1, 3, 0, 0, 6, 6);

        huoneInfoPaneli = new JPanel();
        huoneInfoPaneli.setLayout(new BorderLayout());
        huoneInfoPaneli.setPreferredSize(new Dimension(ikkunanLeveys-30, 30));
        huoneInfoPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        huoneInfoPaneli.add(esineValikko, BorderLayout.WEST);
        huoneInfoPaneli.add(huoneenNimiPaneli, BorderLayout.CENTER);
        huoneInfoPaneli.add(huoneenVaihtoPaneli, BorderLayout.EAST);
        
        objektiEditointiKenttäPaneli = new JPanel();
        objektiEditointiKenttäPaneli.setLayout(null);
        objektiEditointiKenttäPaneli.setPreferredSize(new Dimension(ikkunanLeveys-30, ikkunanLeveys -30));
        objektiEditointiKenttäPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        objektiEditointiKenttäPaneli.revalidate();
        objektiEditointiKenttäPaneli.repaint();

        int kohteenSijX = 10;
        int kohteensijY = 10;
        for (int i = 0; i < Peli.kentänKoko; i++) {
            for (int j = 0; j < Peli.kentänKoko; j++) {
                JLabel peliTestiLabel = new JLabel("PeliTestiLabel");
                peliTestiLabel.setBounds(kohteenSijX, kohteensijY, esineenKokoPx, esineenKokoPx);
                objektiEditointiKenttäPaneli.add(peliTestiLabel);
                kohteenSijX += esineenKokoPx;
            }
            kohteenSijX = 10;
            kohteensijY += esineenKokoPx;
        }

        maastoEditointiKenttäPaneli = new JPanel();
        maastoEditointiKenttäPaneli.setLayout(null);

        int maastoKohteenSijX = 10;
        int maastoKohteensijY = 10;
        for (int i = 0; i < Peli.kentänKoko; i++) {
            for (int j = 0; j < Peli.kentänKoko; j++) {
                JLabel peliTestiLabel = new JLabel("PeliTestiLabel");
                peliTestiLabel.setBounds(maastoKohteenSijX, maastoKohteensijY, esineenKokoPx, esineenKokoPx);
                maastoEditointiKenttäPaneli.add(peliTestiLabel);
                maastoKohteenSijX += esineenKokoPx;
            }
            maastoKohteenSijX = 10;
            maastoKohteensijY += esineenKokoPx;
        }

        npcEditointiKenttäPaneli = new JPanel();
        npcEditointiKenttäPaneli.setLayout(null);

        int npcKohteenSijX = 10;
        int npcKohteenSijY = 10;
        for (int i = 0; i < Peli.kentänKoko; i++) {
            for (int j = 0; j < Peli.kentänKoko; j++) {
                JLabel peliTestiLabel = new JLabel("PeliTestiLabel");
                peliTestiLabel.setBounds(npcKohteenSijX, npcKohteenSijY, esineenKokoPx, esineenKokoPx);
                npcEditointiKenttäPaneli.add(peliTestiLabel);
                npcKohteenSijX += esineenKokoPx;
            }
            npcKohteenSijX = 10;
            npcKohteenSijY += esineenKokoPx;
        }

        välilehdet = new JTabbedPane();
        välilehdet.setVisible(true);
        välilehdet.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        välilehdet.add("Objektit", objektiEditointiKenttäPaneli);
        välilehdet.add("Maasto", maastoEditointiKenttäPaneli);
        välilehdet.add("NPC:t", npcEditointiKenttäPaneli);
        välilehdet.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                try {
                    if (huoneInfoPaneli.isAncestorOf(esineValikko)) {
                        huoneInfoPaneli.remove(esineValikko);
                    }
                    if (huoneInfoPaneli.isAncestorOf(maastoValikko)) {
                        huoneInfoPaneli.remove(maastoValikko);
                    }
                    if (huoneInfoPaneli.isAncestorOf(npcValikko)) {
                        huoneInfoPaneli.remove(npcValikko);
                    }

                    switch (välilehdet.getTitleAt(välilehdet.getSelectedIndex())) {
                        
                        case "Objektit":
                            huoneInfoPaneli.add(esineValikko, BorderLayout.WEST);
                            break;
                        case "Maasto":
                            huoneInfoPaneli.add(maastoValikko, BorderLayout.WEST);
                            break;
                        case "NPC:t":
                            huoneInfoPaneli.add(npcValikko, BorderLayout.WEST);
                            break;
                        default: break;
                    }
                    huoneInfoPaneli.revalidate();
                    huoneInfoPaneli.repaint();
                }
                catch (NullPointerException npe) {
                    npe.printStackTrace();
                }
            }
        });

        editointiKenttäAlue = new JPanel();
        editointiKenttäAlue.setLayout(new BorderLayout());
        editointiKenttäAlue.setPreferredSize(new Dimension(ikkunanLeveys-30, ikkunanLeveys -30));
        editointiKenttäAlue.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        editointiKenttäAlue.add(huoneInfoPaneli, BorderLayout.NORTH);
        editointiKenttäAlue.add(välilehdet);
        editointiKenttäAlue.revalidate();
        editointiKenttäAlue.repaint();


        hudTeksti = new JLabel("Kokeile asettaa esineitä kentälle!");
        
        tekstiPaneli = new JPanel();
        tekstiPaneli.setLayout(new BorderLayout());
        tekstiPaneli.setPreferredSize(new Dimension(ikkunanLeveys -30, 20));
        tekstiPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        tekstiPaneli.add(hudTeksti, BorderLayout.CENTER);
        tekstiPaneli.revalidate();
        tekstiPaneli.repaint();

        kontrolliInfoLabel[0] = new JLabel("Hiiren vasen: aseta");
        kontrolliInfoLabel[1] = new JLabel("Hiiren oikea: lisävalinnat");
        kontrolliInfoLabel[2] = new JLabel("Hiiren keski: poista");
        kontrolliInfoLabel[3] = new JLabel("");
        kontrolliInfoLabel[4] = new JLabel("");
        kontrolliInfoLabel[5] = new JLabel("");
        kontrolliInfoLabel[6] = new JLabel("");
        kontrolliInfoLabel[7] = new JLabel("");
        kontrolliInfoLabel[8] = new JLabel("");
        for (int i = 0; i < kontrolliInfoLabel.length; i++) {
            //kontrolliInfoLabel[i].setBorder(BorderFactory.createLineBorder(Color.black, 1));
        }
        
        infoPaneli = new JPanel();
        infoPaneli.setLayout(new GridLayout(9, 1));
        infoPaneli.setPreferredSize(new Dimension(ikkunanLeveys-480, 120));
        infoPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        for (int i = 0; i < kontrolliInfoLabel.length; i++) {
            infoPaneli.add(kontrolliInfoLabel[i]);
        }
        infoPaneli.revalidate();
        infoPaneli.repaint();

        hud = new JPanel();
        hud.setLayout(new BorderLayout());
        hud.setPreferredSize(new Dimension(ikkunanLeveys -30, 140));
        hud.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        hud.add(tekstiPaneli, BorderLayout.NORTH);
        hud.add(infoPaneli, BorderLayout.WEST);
        hud.revalidate();
        hud.repaint();

        peliAluePaneli = new JPanel();
        peliAluePaneli.setLayout(new BorderLayout());
        peliAluePaneli.add(yläPaneeli, BorderLayout.NORTH);
        peliAluePaneli.add(editointiKenttäAlue, BorderLayout.CENTER);
        peliAluePaneli.add(hud, BorderLayout.SOUTH);
        

        crd = new CardLayout();
        kortit = new JPanel(crd);
        //kortit.add(peliAluePaneli);
        kortit.add(peliAluePaneli);
        

        ikkuna.add(yläPalkki, BorderLayout.NORTH);
        //ikkuna.add(yläPaneeli);
        //ikkuna.add(peliKenttä);
        //ikkuna.add(hud);
        ikkuna.add(kortit, BorderLayout.CENTER);
        ikkuna.revalidate();
        ikkuna.repaint();

        huoneKartta = Peli.huoneKartta;
        luoAlkuIkkuna(0, 0, null);
        lataaHuoneKartasta(muokattavaHuone);
    }

    public static Set<String> listaaMaastot(String dir) {
        return Stream.of(new File(dir).listFiles())
          .filter(file -> !file.isDirectory() && (file.getName().endsWith(".png") || file.getName().endsWith(".jpg") || file.getName().endsWith(".gif")))
          .map(File::getName)
          .collect(Collectors.toSet());
    }

    static void lataaHuoneKartasta(int uusiHuone) {
        objektiKenttä = huoneKartta.get(uusiHuone).annaHuoneenKenttäSisältö();
        maastoKenttä = huoneKartta.get(uusiHuone).annaHuoneenMaastoSisältö();
        npcKenttä = huoneKartta.get(uusiHuone).annaHuoneenNPCSisältö();
        huoneenNimi = huoneKartta.get(uusiHuone).annaNimi();
        huoneenAlue = huoneKartta.get(uusiHuone).annaAlue();
        huoneenNimiLabel.setText(huoneenNimi + " (" + huoneenAlue + ")");
    }

    static void asetaUusiHuoneKarttaan(int nykyinenHuone) {
        ArrayList<KenttäKohde> objektiKenttäLista = new ArrayList<KenttäKohde>();
        for (KenttäKohde[] kk : objektiKenttä) {
            for (KenttäKohde k : kk) {
                objektiKenttäLista.add(k);
            }
        }
        ArrayList<Maasto> maastoKenttäLista = new ArrayList<Maasto>();
        for (Maasto[] mm : maastoKenttä) {
            for (Maasto m : mm) {
                maastoKenttäLista.add(m);
            }
        }

        ArrayList<NPC> npcKenttäLista = new ArrayList<NPC>();
        for (NPC[] nn : npcKenttä) {
            for (NPC n : nn) {
                npcKenttäLista.add(n);
            }
        }
        
        huoneKartta.put(nykyinenHuone, new Huone(nykyinenHuone, 10,  huoneenNimi,null, huoneenAlue, objektiKenttäLista, maastoKenttäLista, npcKenttäLista, false, "alkudialogi"));
        for (int i = 0; i < Peli.kentänKoko; i++) {
            for (int j = 0; j < Peli.kentänKoko; j++) {
                objektiKenttä[j][i] = null;
                maastoKenttä[j][i] = null;
                npcKenttä[j][i] = null;
            }
        }
        huoneenNimi = "";
        huoneenAlue = "";
        huoneenNimiTekstiKenttä.setText(huoneenNimi);
        huoneenAlueTekstiKenttä.setText(huoneenAlue);
        huoneenNimiLabel.setText(huoneenNimi + " (" + huoneenAlue + ")");
    }

    static void vaihdaHuonetta(int nykyinenHuone, int seuraavaHuone) {
        
        if (huoneKartta.containsKey(seuraavaHuone)) {
            asetaUusiHuoneKarttaan(nykyinenHuone);
            lataaHuoneKartasta(seuraavaHuone);
        }
        else {
            asetaUusiHuoneKarttaan(nykyinenHuone);
        }
        
    }

    static void tallennaTiedostoon(String kokoTiedostoMerkkijonona) {
        JFileChooser tiedostoSelain = new JFileChooser(".\\");
        tiedostoSelain.setDialogTitle("Tallenna tiedostoon");
        tiedostoSelain.setLocale(new Locale("fi", "FI"));
        FileNameExtensionFilter tiedostoSuodatin = new FileNameExtensionFilter("Keimon Seikkailupelin Tiedosto (.kst)", "kst");
        tiedostoSelain.setFileFilter(tiedostoSuodatin);
        int valinta = tiedostoSelain.showOpenDialog(ikkuna);
        if (valinta == JFileChooser.APPROVE_OPTION) {
            try {
                File tiedosto = tiedostoSelain.getSelectedFile();
                if (tiedosto.isFile()) {
                    int korvaaTiedosto = CustomViestiIkkunat.TiedostonKorvaus.showDialog("Tiedosto " + tiedosto.getName() + " on jo olemassa. Haluatko korvata sen?", "Tiedosto on jo olemassa.");
                    if (korvaaTiedosto == JOptionPane.YES_OPTION) {
                        //FileWriter tiedostoTallentaja = new FileWriter(tiedosto.getName());
                        //tiedostoTallentaja.write(kokoTiedostoMerkkijonona);
                        //tiedostoTallentaja.close();
                        Writer fstream = new OutputStreamWriter(new FileOutputStream(tiedosto.getName()), StandardCharsets.UTF_8);
                        fstream.write(kokoTiedostoMerkkijonona);
                        fstream.close();
                    }
                }
                else {
                    String tiedostonNimi = tiedosto.getName();
                    if (!tiedostonNimi.endsWith(".kst")) {
                        tiedostonNimi += ".kst";
                    }
                    //FileWriter tiedostoTallentaja = new FileWriter(tiedostonNimi);
                    //tiedostoTallentaja.write(kokoTiedostoMerkkijonona);
                    //tiedostoTallentaja.close();
                    Writer fstream = new OutputStreamWriter(new FileOutputStream(tiedostonNimi), StandardCharsets.UTF_8);
                    fstream.write(kokoTiedostoMerkkijonona);
                    fstream.close();
                }
            }
            catch (IOException e) {
    
            } 
        }
    }

    static void asetaEsineRuutuun(int sijX, int sijY, String esineenNimi) {
        
        switch (esineenNimi) {

            case "Avain":
                objektiKenttä[sijX][sijY] = new Avain(sijX, sijY);
                break;

            case "Hiili":
                objektiKenttä[sijX][sijY] = new Hiili(sijX, sijY);
                break;

            case "Huume":
                objektiKenttä[sijX][sijY] = new Huume(sijX, sijY);
                break;

            case "Juhani":
                objektiKenttä[sijX][sijY] = new Juhani(sijX, sijY);
                break;

            case "Kaasupullo":
                objektiKenttä[sijX][sijY] = new Kaasupullo(sijX, sijY);
                break;

            case "Kaasusytytin":
                objektiKenttä[sijX][sijY] = new Kaasusytytin(sijX, sijY);
                break;

            case "Kilpi":
                objektiKenttä[sijX][sijY] = new Kilpi(sijX, sijY);
                break;

            case "Kirstu":
                objektiKenttä[sijX][sijY] = new Kirstu(sijX, sijY);
                break;

            case "Makkara":
                objektiKenttä[sijX][sijY] = new Makkara(sijX, sijY);
                break;

            case "Nuotio":
                objektiKenttä[sijX][sijY] = new Nuotio(sijX, sijY);
                break;

            case "Pahavihu":
                objektiKenttä[sijX][sijY] = new PahaVihu(sijX, sijY);
                break;

            case "Paperi":
                objektiKenttä[sijX][sijY] = new Paperi(sijX, sijY);
                break;

            case "Pesäpallomaila":
                objektiKenttä[sijX][sijY] = new Pesäpallomaila(sijX, sijY);
                break;

            case "Pikkuvihu":
                objektiKenttä[sijX][sijY] = new PikkuVihu_KenttäKohde(sijX, sijY);
                break;

            case "Oviruutu":
                objektiKenttä[sijX][sijY] = new ReunaWarppi(sijX, sijY, 0, 0, 0, Suunta.VASEN);
                break;

            case "Seteli":
                objektiKenttä[sijX][sijY] = new Seteli(sijX, sijY);
                break;

            case "Suklaalevy":
                objektiKenttä[sijX][sijY] = new Suklaalevy(sijX, sijY);
                break;

            case "Vesiämpäri":
                objektiKenttä[sijX][sijY] = new Vesiämpäri(sijX, sijY);
                break;

            case "Ämpärikone":
                objektiKenttä[sijX][sijY] = new Ämpärikone(sijX, sijY);
                break;

            default:
                objektiKenttä[sijX][sijY] = null;
                break;
        }
    }

    static void asetaMaastoRuutuun(int sijX, int sijY, String maastonNimi, String[] ominaisuusLista) {
        
        switch (maastonNimi) {

            case "Tile":
                maastoKenttä[sijX][sijY] = new Tile(sijX, sijY, ominaisuusLista);
                break;

            case "EsteTile":
                maastoKenttä[sijX][sijY] = new EsteTile(sijX, sijY, ominaisuusLista);
                break;

            default:
                maastoKenttä[sijX][sijY] = null;
                break;
        }
    }

    static void asetaNPCRuutuun(int sijX, int sijY, String npcnNimi) {
        
        switch (npcnNimi) {

            case "Pikkuvihu":
                npcKenttä[sijX][sijY] = new Pikkuvihu(sijX, sijY, LiikeTapa.LOOP_NELIÖ_MYÖTÄPÄIVÄÄN);
                break;

            case "Pahavihu":
                //npcKenttä[sijX][sijY] = new Pahavihu(sijX, sijY, ominaisuusLista);
                npcKenttä[sijX][sijY] = null;
                break;

            default:
                npcKenttä[sijX][sijY] = null;
                break;
        }
    }

    static ArrayList<JMenuItem> luoOikeaClickOminaisuusLista(KenttäKohde k) {

        ArrayList<JMenuItem> menuItemit = new ArrayList<JMenuItem>();

        switch (k.annaNimi()) {

            default:
                break;

            case "Avain":
                break;

            case "Kaasusytytin", "Tyhjä kaasusytytin", "Toimiva kaasusytytin":
                Kaasusytytin ks = (Kaasusytytin)k;
                JCheckBoxMenuItem valitseToimivuus = new JCheckBoxMenuItem("Toimiva");
                valitseToimivuus.setState(ks.toimiva);
                valitseToimivuus.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        ks.asetaToimivuus(valitseToimivuus.getState() ? "toimiva" : "tyhjä");
                        System.out.println(ks.toimiva);
                    }
                });
                menuItemit.add(valitseToimivuus);
                break;

            case "Oviruutu":
                JMenuItem warppiKohde = new JMenuItem("Muokkaa warpin kohdetta");
                warppiKohde.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        ObjektinMuokkausIkkuna.luoObjektinMuokkausIkkuna(k.annaSijX(), k.annaSijY(), k);
                    }
                });
                menuItemit.add(warppiKohde);
                break;
        }
        return menuItemit;
    }

    static class ObjektinMuokkausIkkuna {

        static final int ikkunanLeveys = 420;
        static final int ikkunanKorkeus = 180;
        static JFrame muokkausIkkuna;
        static String[] tekstit;
        static int valintojenMäärä;
        static JPanel paneli;
        static JLabel[] tekstiLabelit;
        static JTextField[] tekstiKentät;
        static JComboBox<Suunta> suuntaValinta;

        static void hyväksyMuutokset(int sijX, int sijY, String muokattavanKohteenNimi) {
            try {
                switch (muokattavanKohteenNimi) {
                    case "Oviruutu":
                        ReunaWarppi oviruutu = (ReunaWarppi)objektiKenttä[sijX][sijY];
                        oviruutu.asetaKohdeHuone(Integer.parseInt(tekstiKentät[0].getText()));
                        oviruutu.asetaKohdeRuudut(Integer.parseInt(tekstiKentät[1].getText()), Integer.parseInt(tekstiKentät[2].getText()));
                        oviruutu.asetaSuunta((Warp.Suunta)suuntaValinta.getSelectedItem());
                        oviruutu.päivitäLisäOminaisuudet();
                        objektiKenttä[sijX][sijY] = oviruutu;
                        muokkausIkkuna.dispose();
                        break;
                    default:
                        break;
                }
            }
            catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "", "häire", JOptionPane.ERROR_MESSAGE);
            }
            catch (NullPointerException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "", "häire", JOptionPane.ERROR_MESSAGE);
            }
            catch (NumberFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Virheellinen syöte", "Virheellinen syöte. Vain kokonaisluvut kelpaavat.", JOptionPane.ERROR_MESSAGE);
            }
        }
    
        static void luoObjektinMuokkausIkkuna(int sijX, int sijY, KenttäKohde muokattavaKohde) {
            
            switch (muokattavaKohde.annaNimi()) {
                case "Oviruutu":
                    valintojenMäärä = 4;
                    tekstit = new String[valintojenMäärä];
                    tekstit[0] = "Kohdehuone (ID)";
                    tekstit[1] = "Kohderuudun X-sijainti";
                    tekstit[2] = "Kohderuudun Y-sijainti";
                    tekstit[3] = "Suunta";
                    break;
                default:
                    break;
            }

            paneli = new JPanel(new SpringLayout());
            tekstiLabelit = new JLabel[valintojenMäärä];
            tekstiKentät = new JTextField[valintojenMäärä];

            // for (int i = 0; i < valintojenMäärä; i++) {
            //     tekstiLabelit[i] = new JLabel(tekstit[i]);
            //     paneli.add(tekstiLabelit[i]);
            // }

            
            switch (muokattavaKohde.annaNimi()) {
                case "Oviruutu":
                    for (int i = 0; i < 3; i++) {
                        tekstiLabelit[i] = new JLabel(tekstit[i]);
                        paneli.add(tekstiLabelit[i]);
                        tekstiKentät[i] = new JTextField();
                        paneli.add(tekstiKentät[i]);
                    }
                    Warp oviruutu = (Warp)muokattavaKohde;
                    tekstiKentät[0].setText("" + oviruutu.annaKohdeHuone());
                    tekstiKentät[1].setText("" + oviruutu.annaKohdeRuutuX());
                    tekstiKentät[2].setText("" + oviruutu.annaKohdeRuutuY());
                    Suunta[] suuntaLista = {Suunta.VASEN, Suunta.OIKEA, Suunta.YLÖS, Suunta.ALAS};
                    suuntaValinta = new JComboBox<Suunta>(suuntaLista);
                    suuntaValinta.setSelectedItem(oviruutu.annaSuunta());

                    tekstiLabelit[3] = new JLabel(tekstit[3]);
                    paneli.add(tekstiLabelit[3]);
                    paneli.add(suuntaValinta);
                    break;
                default:
                    break;
            }
            JButton okNappi = new JButton("Aseta");
            okNappi.addMouseListener(new MouseAdapter() {
                public void mousePressed (MouseEvent e) {
                    if (!SwingUtilities.isRightMouseButton(e)) {
                        hyväksyMuutokset(sijX, sijY, muokattavaKohde.annaNimi());
                    }
                }
            });
    
            JButton cancelNappi = new JButton("Peruuta");
            cancelNappi.addMouseListener(new MouseAdapter() {
                public void mousePressed (MouseEvent e) {
                    if (!SwingUtilities.isRightMouseButton(e)) {
                        muokkausIkkuna.dispose();
                    }
                }
            });
    
            paneli.add(okNappi);
            paneli.add(cancelNappi);
    
            SpringUtilities.makeCompactGrid(paneli, valintojenMäärä + 1, 2, 6, 6, 6, 6);
    
            muokkausIkkuna = new JFrame();
            muokkausIkkuna.setTitle("Muokkaa objektia: " + muokattavaKohde.annaNimi());
            muokkausIkkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja.png").getImage());
            muokkausIkkuna.setBounds(PääIkkuna.ikkuna.getBounds().x + 100, PääIkkuna.ikkuna.getBounds().y + 50, ikkunanLeveys, ikkunanKorkeus);
            muokkausIkkuna.setLayout(new BorderLayout());
            muokkausIkkuna.setVisible(true);
            muokkausIkkuna.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            muokkausIkkuna.add(paneli, BorderLayout.CENTER);
            muokkausIkkuna.revalidate();
            muokkausIkkuna.repaint();
        }    
    }   

    static void luoAlkuIkkuna(int sijX, int sijY, Icon pelaajaKuvake) {

        objektiEditointiKenttäPaneli.removeAll();
        
        int kohteenSijX = 10;
        int kohteensijY = 10;
        objektiKenttä = new KenttäKohde[Peli.kentänKoko][Peli.kentänKoko];
        maastoKenttä = new Maasto[Peli.kentänKoko][Peli.kentänKoko];
        npcKenttä = new NPC[Peli.kentänKoko][Peli.kentänKoko];
        
        for (int i = 0; i < Peli.kentänKoko; i++) {
            for (int j = 0; j < Peli.kentänKoko; j++) {
                int x = j;
                int y = i;
                kenttäKohteenKuvake[x][y] = new JButton(new ImageIcon());
                kenttäKohteenKuvake[x][y].addMouseListener(new MouseAdapter() {
                    public void mousePressed (MouseEvent e) {
                        try {
                            JMenuItem ominaisuudet = new JMenuItem("Ominaisuudet");
                            ominaisuudet.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    JOptionPane.showMessageDialog(null, objektiKenttä[x][y].annaTiedot(), "Ominaisuudet", JOptionPane.INFORMATION_MESSAGE);
                                }
                            });
                            
                            if (SwingUtilities.isLeftMouseButton(e)) {
                                asetaEsineRuutuun(x, y, esineLista[esineValikko.getSelectedIndex()]);;
                                hudTeksti.setText(esineLista[esineValikko.getSelectedIndex()] + ", " + x + ", " + y);
                            }
                            else if (SwingUtilities.isRightMouseButton(e)) {
                                JPopupMenu ominaisuusMenu = new JPopupMenu();
                                ominaisuudet.setText("Ominaisuudet: " + objektiKenttä[x][y].annaNimi());
                                ominaisuusMenu.add(ominaisuudet);
                                for (JMenuItem mi : luoOikeaClickOminaisuusLista(objektiKenttä[x][y])) {
                                    ominaisuusMenu.add(mi);
                                }
                                ominaisuusMenu.show(e.getComponent(), e.getX(), e.getY());
                            }
                            else if (SwingUtilities.isMiddleMouseButton(e)) {
                                asetaEsineRuutuun(x, y, "");
                                hudTeksti.setText("tyhjä" + ", " + x + ", " + y);
                            }
                            else {
            
                            }
                        }
                        catch (NullPointerException npe) {
                            System.out.println("Ei ominaisuuksia (tyhjä ruutu)");
                        }
                        finally {
                            //asetaUusiHuoneKarttaan(muokattavaHuone);
                        }
                    }
                });

                maastoKohteenKuvake[j][i] = new JButton();
                maastoKohteenKuvake[x][y].addMouseListener(new MouseAdapter() {
                    public void mousePressed (MouseEvent e) {
                        try {
                            JMenuItem ominaisuudet = new JMenuItem("Ominaisuudet");
                            ominaisuudet.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    JOptionPane.showMessageDialog(null, maastoKenttä[x][y].annaTiedot(), "Ominaisuudet", JOptionPane.INFORMATION_MESSAGE);
                                }
                            });
                            
                            if (SwingUtilities.isLeftMouseButton(e)) {
                                String[] ominaisuusLista = new String[1];
                                ominaisuusLista[0] = "kuva=" + maastoValikko.getSelectedItem();
                                //ominaisuusLista[1] = "este=" + "ei";
                                if (ominaisuusLista[0].endsWith("_e.png")) {
                                    asetaMaastoRuutuun(x, y, "EsteTile", ominaisuusLista);
                                }
                                else{
                                    asetaMaastoRuutuun(x, y, "Tile", ominaisuusLista);
                                }
                                hudTeksti.setText(maastoValikko.getSelectedItem() + ", " + x + ", " + y);
                            }
                            else if (SwingUtilities.isRightMouseButton(e)) {
                                JPopupMenu ominaisuusMenu = new JPopupMenu();
                                ominaisuudet.setText("Ominaisuudet: " + maastoKenttä[x][y].annaNimi());
                                ominaisuusMenu.add(ominaisuudet);
                                //for (JMenuItem mi : luoOikeaClickOminaisuusLista(maastoKenttä[x][y])) {
                                //    ominaisuusMenu.add(mi);
                                //}
                                ominaisuusMenu.show(e.getComponent(), e.getX(), e.getY());
                            }
                            else if (SwingUtilities.isMiddleMouseButton(e)) {
                                String[] ominaisuusLista = new String[1];
                                ominaisuusLista[0] = "kuva=" + maastoValikko.getSelectedItem();
                                //ominaisuusLista[1] = "este=" + "ei";
                                asetaMaastoRuutuun(x, y, "", ominaisuusLista);
                                hudTeksti.setText("tyhjä" + ", " + x + ", " + y);
                            }
                            else {
            
                            }
                        }
                        catch (NullPointerException npe) {
                            System.out.println("Ei ominaisuuksia (tyhjä ruutu)");
                        }
                        finally {
                            //asetaUusiHuoneKarttaan(muokattavaHuone);
                        }
                    }
                });

                npcKohteenKuvake[x][y] = new JButton(new ImageIcon());
                npcKohteenKuvake[x][y].addMouseListener(new MouseAdapter() {
                    public void mousePressed (MouseEvent e) {
                        try {
                            JMenuItem ominaisuudet = new JMenuItem("Ominaisuudet");
                            ominaisuudet.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    JOptionPane.showMessageDialog(null, npcKenttä[x][y].annaTiedot(), "Ominaisuudet", JOptionPane.INFORMATION_MESSAGE);
                                }
                            });
                            
                            if (SwingUtilities.isLeftMouseButton(e)) {
                                asetaNPCRuutuun(x, y, npcNimiLista[npcValikko.getSelectedIndex()]);
                                hudTeksti.setText(npcNimiLista[npcValikko.getSelectedIndex()] + ", " + x + ", " + y);
                            }
                            else if (SwingUtilities.isRightMouseButton(e)) {
                                JPopupMenu ominaisuusMenu = new JPopupMenu();
                                ominaisuudet.setText("Ominaisuudet: " + npcKenttä[x][y].annaNimi());
                                ominaisuusMenu.add(ominaisuudet);
                                //for (JMenuItem mi : luoOikeaClickOminaisuusLista(npcKenttä[x][y])) {
                                //    ominaisuusMenu.add(mi);
                                //}
                                ominaisuusMenu.show(e.getComponent(), e.getX(), e.getY());
                            }
                            else if (SwingUtilities.isMiddleMouseButton(e)) {
                                asetaNPCRuutuun(x, y, "");
                                hudTeksti.setText("tyhjä" + ", " + x + ", " + y);
                            }
                            else {
            
                            }
                        }
                        catch (NullPointerException npe) {
                            System.out.println("Ei ominaisuuksia (tyhjä ruutu)");
                            npe.printStackTrace();
                        }
                        finally {
                            //asetaUusiHuoneKarttaan(muokattavaHuone);
                        }
                    }
                });
            }
        }
        pelaajaLabel = new JLabel(pelaajaKuvake);
        pelaajaLabel.setBounds(Pelaaja.sijX * pelaajanKokoPx + 10, Pelaaja.sijY * pelaajanKokoPx + 10, pelaajanKokoPx, pelaajanKokoPx);
        //pelaajaLabel.setIcon(valitsePelaajanKuvake());
        taustaLabel = new JLabel(new ImageIcon());
        taustaLabel.setBounds(0, 0, Peli.kentänKoko * esineenKokoPx + 20, Peli.kentänKoko * esineenKokoPx + 20);
        
        try {
            objektiEditointiKenttäPaneli.add(pelaajaLabel);
            objektiEditointiKenttäPaneli.add(taustaLabel);
            for (int i = 0; i < Peli.kentänKoko; i++) {
                for (int j = 0; j < Peli.kentänKoko; j++) {
                    if (kenttäKohteenKuvake[j][i] == null) {

                    }
                    else {
                        if (j == sijX && i == sijY) {
                            if (Peli.pelikenttä[j][i] instanceof KenttäKohde) {
                                kenttäKohteenKuvake[j][i].setIcon(Peli.pelikenttä[j][i].annaKuvake());
                            }
                            if (Peli.maastokenttä[j][i] instanceof Maasto) {
                                maastoKohteenKuvake[j][i].setIcon(Peli.maastokenttä[j][i].annaKuvake());
                            }
                        }
                        if (reunatNäkyvissä) {
                            if (Peli.pelikenttä[j][i] instanceof KenttäKohde) {
                                if (Pelaaja.sijX == j && Pelaaja.sijY == i) {
                                    kenttäKohteenKuvake[j][i].setBorder(null);
                                }
                                else if (Peli.pelikenttä[j][i] instanceof KenttäKohde) {
                                    kenttäKohteenKuvake[j][i].setIcon(Peli.pelikenttä[j][i].annaKuvake());
                                    if (Peli.pelikenttä[j][i] instanceof Kiintopiste) {
                                        kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(0,255,0), 1, true));
                                    }
                                    else if (Peli.pelikenttä[j][i] instanceof Esine) {
                                        kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(0,0,255), 1, true));
                                    }
                                    else if (Peli.pelikenttä[j][i] instanceof NPC_KenttäKohde) {
                                        if (Peli.pelikenttä[j][i] instanceof Vihollinen_KenttöKohde) {
                                            kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(255,0,0), 1, true));
                                        }
                                        else {
                                            kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(200,200,0), 1, true));
                                        }
                                        
                                    }
                                    else if (Peli.pelikenttä[j][i] instanceof Warp) {
                                        kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(80,200,0), 1, true));
                                    }
                                }
                            }
                            else if (Peli.pelikenttä[j][i] == null) {
                                kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
                            }
                        }
                        else {
                            kenttäKohteenKuvake[j][i].setBorder(null);
                        }
                        if (Peli.pelikenttä[j][i] instanceof KenttäKohde) {
                            kenttäKohteenKuvake[j][i].setIcon(Peli.pelikenttä[j][i].annaKuvake());
                        }
                        if (Peli.maastokenttä[j][i] instanceof Maasto) {
                            maastoKohteenKuvake[j][i].setIcon(Peli.maastokenttä[j][i].annaKuvake());
                        }
                    
                        kenttäKohteenKuvake[j][i].setBounds(kohteenSijX, kohteensijY, esineenKokoPx, esineenKokoPx);
                        maastoKohteenKuvake[j][i].setBounds(kohteenSijX, kohteensijY, esineenKokoPx, esineenKokoPx);
                        npcKohteenKuvake[j][i].setBounds(kohteenSijX, kohteensijY, esineenKokoPx, esineenKokoPx);

                        objektiEditointiKenttäPaneli.add(kenttäKohteenKuvake[j][i]);
                        objektiEditointiKenttäPaneli.setComponentZOrder(kenttäKohteenKuvake[j][i], 1);
                        maastoEditointiKenttäPaneli.add(maastoKohteenKuvake[j][i]);
                        maastoEditointiKenttäPaneli.setComponentZOrder(maastoKohteenKuvake[j][i], 1);
                        npcEditointiKenttäPaneli.add(npcKohteenKuvake[j][i]);
                        npcEditointiKenttäPaneli.setComponentZOrder(npcKohteenKuvake[j][i], 1);
                        kohteenSijX += esineenKokoPx;
                    }
                }
                kohteenSijX = 10;
                kohteensijY += esineenKokoPx;
            }
            
            objektiEditointiKenttäPaneli.setComponentZOrder(pelaajaLabel, 0);
            //peliKenttä.setComponentZOrder(taustaLabel, 2);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "Jokin meni pieleen", "Array index out of Bounds", JOptionPane.ERROR_MESSAGE);
        }
        catch (IllegalArgumentException e) {

        }

        objektiEditointiKenttäPaneli.revalidate();
        objektiEditointiKenttäPaneli.repaint();
    }

    static void päivitäEditoriIkkuna() {
        päivitäObjektiKenttä();
        päivitäMaastoKenttä();
        päivitäNPCKenttä();
    }

    static JPanel päivitäObjektiKenttä() {
        
        //peliKenttä.removeAll();
        //int kohteenSijX = 10;
        //int kohteenSijY = 10;
        
        if (vaatiiPäivityksen && objektiEditointiKenttäPaneli != null && objektiKenttä != null && maastoKenttä != null && npcKenttä != null) {
            try {

                huoneInfoLabel.setText("Huone " + muokattavaHuone);

                for (int i = 0; i < Peli.kentänKoko; i++) {
                    for (int j = 0; j < Peli.kentänKoko; j++) {
                        if (objektiKenttä[j][i] instanceof KenttäKohde) {
                            kenttäKohteenKuvake[j][i].setIcon(objektiKenttä[j][i].annaKuvake());
                        }
                        else if (kenttäKohteenKuvake[j][i] != null) {
                            kenttäKohteenKuvake[j][i].setIcon(null);
                        }
                        //if (maastoKenttä[j][i] instanceof Maasto) {
                        //    maastoKohteenKuvake[j][i].setIcon(maastoKenttä[j][i].annaKuvake());
                        //}
                        //else if (maastoKohteenKuvake[j][i] != null) {
                        //    maastoKohteenKuvake[j][i].setIcon(null);
                        //}

                        if (vaatiiKentänPäivityksen) {
                            if (reunatNäkyvissä) {
                                if (Pelaaja.sijX == j && Pelaaja.sijY == i) {
                                    kenttäKohteenKuvake[j][i].setBorder(null);
                                }
                                else if (objektiKenttä[j][i] instanceof KenttäKohde) {
                                    kenttäKohteenKuvake[j][i].setIcon(objektiKenttä[j][i].annaKuvake());
                                    if (objektiKenttä[j][i] instanceof Kiintopiste) {
                                        kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(0,255,0), 1, true));
                                    }
                                    else if (objektiKenttä[j][i] instanceof Esine) {
                                        kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(0,0,255), 1, true));
                                    }
                                    else if (objektiKenttä[j][i] instanceof NPC_KenttäKohde) {
                                        if (objektiKenttä[j][i] instanceof Vihollinen_KenttöKohde) {
                                            kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(255,0,0), 1, true));
                                        }
                                        else {
                                            kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(200,200,0), 1, true));
                                        }
                                        
                                    }
                                    else if (objektiKenttä[j][i] instanceof Warp) {
                                        kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(80,200,0), 1, true));
                                    }
                                }
                                else if (objektiKenttä[j][i] == null) {
                                    kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
                                }
                            }
                            else {
                                kenttäKohteenKuvake[j][i].setBorder(null);
                            }
                        }
                    }
                }

                if (vaatiiKentänPäivityksen) {
                    pelaajaLabel.setBounds(Pelaaja.sijX * pelaajanKokoPx + 10, Pelaaja.sijY * pelaajanKokoPx + 10, pelaajanKokoPx, pelaajanKokoPx);
                }
            }
            catch (ArrayIndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(null, "Jokin meni pieleen.", "Array index out of Bounds", JOptionPane.ERROR_MESSAGE);
                GrafiikanPäivitysSäie.ongelmaGrafiikassa = true;
            }
            catch (NullPointerException e) {
                System.out.println("Ongelma ruudunpäivityksessä");
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                String sStackTrace = sw.toString();
                System.out.println(sStackTrace);
                String viesti = "Yritettiin ladata kuvaa objektille, jota ei ole vielä luotu.\n\nHäire sovelluksessa. Ilmoitathan kehittäjille.\n\n" + sStackTrace;
                String otsikko = "Virhe ruudunpäivityksessä";
                int virheenJälkeenValinta = CustomViestiIkkunat.FataaliVirhe.showDialog(viesti, otsikko);
                switch (virheenJälkeenValinta) {
                    case JOptionPane.YES_OPTION: break;
                    case JOptionPane.NO_OPTION: System.exit(0); break;
                    case JOptionPane.CANCEL_OPTION: ikkuna.dispose(); break;
                }
            }
            catch (IllegalArgumentException e) {
                System.out.println("Ongelma ruudunpäivityksessä");
                e.printStackTrace();
            }

            objektiEditointiKenttäPaneli.revalidate();
            objektiEditointiKenttäPaneli.repaint();
            //vaatiiPäivityksen = false;
            vaatiiKentänPäivityksen = false;
        }
        return objektiEditointiKenttäPaneli;
    }

    static JPanel päivitäMaastoKenttä() {
        
        //peliKenttä.removeAll();
        //int kohteenSijX = 10;
        //int kohteenSijY = 10;
        
        if (vaatiiPäivityksen && maastoEditointiKenttäPaneli != null && objektiKenttä != null && maastoKenttä != null && npcKenttä != null) {
            try {

                huoneInfoLabel.setText("Huone " + muokattavaHuone);

                for (int i = 0; i < Peli.kentänKoko; i++) {
                    for (int j = 0; j < Peli.kentänKoko; j++) {
                        //if (objektiKenttä[j][i] instanceof KenttäKohde) {
                        //    kenttäKohteenKuvake[j][i].setIcon(objektiKenttä[j][i].annaKuvake());
                        //}
                        //else if (kenttäKohteenKuvake[j][i] != null) {
                        //    kenttäKohteenKuvake[j][i].setIcon(null);
                        //}
                        if (maastoKenttä[j][i] instanceof Maasto) {
                            maastoKohteenKuvake[j][i].setIcon(maastoKenttä[j][i].annaKuvake());
                        }
                        else if (maastoKohteenKuvake[j][i] != null) {
                            maastoKohteenKuvake[j][i].setIcon(null);
                        }

                        // if (vaatiiKentänPäivityksen) {
                        //     if (reunatNäkyvissä) {
                        //         if (Pelaaja.sijX == j && Pelaaja.sijY == i) {
                        //             kenttäKohteenKuvake[j][i].setBorder(null);
                        //         }
                        //         else if (objektiKenttä[j][i] instanceof KenttäKohde) {
                        //             kenttäKohteenKuvake[j][i].setIcon(objektiKenttä[j][i].annaKuvake());
                        //             if (objektiKenttä[j][i] instanceof Kiintopiste) {
                        //                 kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(0,255,0), 1, true));
                        //             }
                        //             else if (objektiKenttä[j][i] instanceof Esine) {
                        //                 kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(0,0,255), 1, true));
                        //             }
                        //             else if (objektiKenttä[j][i] instanceof NPC_KenttäKohde) {
                        //                 if (objektiKenttä[j][i] instanceof Vihollinen_KenttöKohde) {
                        //                     kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(255,0,0), 1, true));
                        //                 }
                        //                 else {
                        //                     kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(200,200,0), 1, true));
                        //                 }
                                        
                        //             }
                        //             else if (objektiKenttä[j][i] instanceof Warp) {
                        //                 kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(80,200,0), 1, true));
                        //             }
                        //         }
                        //         else if (objektiKenttä[j][i] == null) {
                        //             kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
                        //         }
                        //     }
                        //     else {
                        //         kenttäKohteenKuvake[j][i].setBorder(null);
                        //     }
                        // }
                    }
                }

                if (vaatiiKentänPäivityksen) {
                    pelaajaLabel.setBounds(Pelaaja.sijX * pelaajanKokoPx + 10, Pelaaja.sijY * pelaajanKokoPx + 10, pelaajanKokoPx, pelaajanKokoPx);
                }
            }
            catch (ArrayIndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(null, "Jokin meni pieleen.", "Array index out of Bounds", JOptionPane.ERROR_MESSAGE);
                GrafiikanPäivitysSäie.ongelmaGrafiikassa = true;
            }
            catch (NullPointerException e) {
                System.out.println("Ongelma ruudunpäivityksessä");
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                String sStackTrace = sw.toString();
                System.out.println(sStackTrace);
                String viesti = "Yritettiin ladata kuvaa objektille, jota ei ole vielä luotu.\n\nHäire sovelluksessa. Ilmoitathan kehittäjille.\n\n" + sStackTrace;
                String otsikko = "Virhe ruudunpäivityksessä";
                int virheenJälkeenValinta = CustomViestiIkkunat.FataaliVirhe.showDialog(viesti, otsikko);
                switch (virheenJälkeenValinta) {
                    case JOptionPane.YES_OPTION: break;
                    case JOptionPane.NO_OPTION: System.exit(0); break;
                    case JOptionPane.CANCEL_OPTION: ikkuna.dispose(); break;
                }
            }
            catch (IllegalArgumentException e) {
                System.out.println("Ongelma ruudunpäivityksessä");
                e.printStackTrace();
            }

            maastoEditointiKenttäPaneli.revalidate();
            maastoEditointiKenttäPaneli.repaint();
            //vaatiiPäivityksen = false;
            //vaatiiKentänPäivityksen = false;
        }
        return maastoEditointiKenttäPaneli;
    }

    static JPanel päivitäNPCKenttä() {
        
        //peliKenttä.removeAll();
        //int kohteenSijX = 10;
        //int kohteenSijY = 10;
        
        if (vaatiiPäivityksen && npcEditointiKenttäPaneli != null && objektiKenttä != null && maastoKenttä != null && npcKenttä != null) {
            try {

                huoneInfoLabel.setText("Huone " + muokattavaHuone);

                for (int i = 0; i < Peli.kentänKoko; i++) {
                    for (int j = 0; j < Peli.kentänKoko; j++) {
                        if (npcKenttä[j][i] instanceof NPC) {
                            npcKohteenKuvake[j][i].setIcon(npcKenttä[j][i].annaKuvake());
                        }
                        else if (npcKohteenKuvake[j][i] != null) {
                            npcKohteenKuvake[j][i].setIcon(null);
                        }

                        // if (vaatiiKentänPäivityksen) {
                        //     if (reunatNäkyvissä) {
                        //         if (Pelaaja.sijX == j && Pelaaja.sijY == i) {
                        //             kenttäKohteenKuvake[j][i].setBorder(null);
                        //         }
                        //         else if (objektiKenttä[j][i] instanceof KenttäKohde) {
                        //             kenttäKohteenKuvake[j][i].setIcon(objektiKenttä[j][i].annaKuvake());
                        //             if (objektiKenttä[j][i] instanceof Kiintopiste) {
                        //                 kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(0,255,0), 1, true));
                        //             }
                        //             else if (objektiKenttä[j][i] instanceof Esine) {
                        //                 kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(0,0,255), 1, true));
                        //             }
                        //             else if (objektiKenttä[j][i] instanceof NPC_KenttäKohde) {
                        //                 if (objektiKenttä[j][i] instanceof Vihollinen_KenttöKohde) {
                        //                     kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(255,0,0), 1, true));
                        //                 }
                        //                 else {
                        //                     kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(200,200,0), 1, true));
                        //                 }
                                        
                        //             }
                        //             else if (objektiKenttä[j][i] instanceof Warp) {
                        //                 kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(80,200,0), 1, true));
                        //             }
                        //         }
                        //         else if (objektiKenttä[j][i] == null) {
                        //             kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
                        //         }
                        //     }
                        //     else {
                        //         kenttäKohteenKuvake[j][i].setBorder(null);
                        //     }
                        // }
                    }
                }

                if (vaatiiKentänPäivityksen) {
                    pelaajaLabel.setBounds(Pelaaja.sijX * pelaajanKokoPx + 10, Pelaaja.sijY * pelaajanKokoPx + 10, pelaajanKokoPx, pelaajanKokoPx);
                }
            }
            catch (ArrayIndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(null, "Jokin meni pieleen.", "Array index out of Bounds", JOptionPane.ERROR_MESSAGE);
                GrafiikanPäivitysSäie.ongelmaGrafiikassa = true;
            }
            catch (NullPointerException e) {
                System.out.println("Ongelma ruudunpäivityksessä");
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                String sStackTrace = sw.toString();
                System.out.println(sStackTrace);
                String viesti = "Yritettiin ladata kuvaa objektille, jota ei ole vielä luotu.\n\nHäire sovelluksessa. Ilmoitathan kehittäjille.\n\n" + sStackTrace;
                String otsikko = "Virhe ruudunpäivityksessä";
                int virheenJälkeenValinta = CustomViestiIkkunat.FataaliVirhe.showDialog(viesti, otsikko);
                switch (virheenJälkeenValinta) {
                    case JOptionPane.YES_OPTION: break;
                    case JOptionPane.NO_OPTION: System.exit(0); break;
                    case JOptionPane.CANCEL_OPTION: ikkuna.dispose(); break;
                }
            }
            catch (IllegalArgumentException e) {
                System.out.println("Ongelma ruudunpäivityksessä");
                e.printStackTrace();
            }

            npcEditointiKenttäPaneli.revalidate();
            npcEditointiKenttäPaneli.repaint();
            //vaatiiPäivityksen = false;
            //vaatiiKentänPäivityksen = false;
        }
        return npcEditointiKenttäPaneli;
    }
}
