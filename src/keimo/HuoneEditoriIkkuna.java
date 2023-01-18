package keimo;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.Dimension;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

import keimo.Kenttäkohteet.Warp.Suunta;
import keimo.Kenttäkohteet.*;
import keimo.Maastot.*;

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

    static JPanel editointiKenttäPaneli, editointiKenttäAlue;
    static JPanel peliAluePaneli;
    static JPanel huoneInfoPaneli, huoneenVaihtoPaneli, huoneenNimiPaneli;
    static JLabel huoneenNimiLabel;
    static JButton huoneenVaihtoNappiVasen, huoneenVaihtoNappiOikea;
    static JLabel huoneInfoLabel;
    static String[] esineLista = {"Avain", "Hiili", "Kaasupullo", "Kaasusytytin", "Kilpi", "Kirstu", "Makkara", "Nuotio", "Pahavihu", "Paperi", "Pikkuvihu", "Oviruutu", "Suklaalevy", "Vesiämpäri", "Ämpärikone"};
    static JComboBox<String> esineValikko;
    static HashMap<Integer, Huone> huoneKartta = new HashMap<Integer, Huone>();
    static int muokattavaHuone = 0;
    static String huoneenNimi = "";
    static String huoneenAlue = "";
    static ImageIcon huoneenTaustakuva;
    static String huoneenAlkuDialogiTeksti = "";

    static JButton[][] kenttäKohteenKuvake, maastoKohteenKuvake;
    static KenttäKohde[][] objektiKenttä;
    static Maasto[][] maastoKenttä;
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
        
        ikkunanLeveys = esineenKokoPx * Main.kentänKoko + 35;
        ikkunanKorkeus = esineenKokoPx * Main.kentänKoko + 310;
        kenttäKohteenKuvake = new JButton[Main.kentänKoko][Main.kentänKoko];
        maastoKohteenKuvake = new JButton[Main.kentänKoko][Main.kentänKoko];
        
        ikkuna = new JFrame("Huone-editori v0.2");
        ikkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja.png").getImage());
        ikkuna.setBounds(600, 100, ikkunanLeveys, ikkunanKorkeus);
        //ikkuna.setLayout(new FlowLayout(FlowLayout.TRAILING));
        ikkuna.setLayout(new BorderLayout());
        ikkuna.setBackground(Color.black);
        ikkuna.setVisible(true);
        ikkuna.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ikkuna.revalidate();
        ikkuna.repaint();

        uusi = new JMenuItem("Uusi", new ImageIcon("./kuvat/pelaaja32.png"));
        uusi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int tyhjennysValinta = CustomViestiIkkunat.EditorinTyhjennys.showDialog("Haluatko varmasti tyhjentää kaikki huoneet?", "Uusi pohja");
                if (tyhjennysValinta == JOptionPane.YES_OPTION) {
                    for (int i = 0; i < Main.kentänKoko; i++) {
                        for (int j = 0; j < Main.kentänKoko; j++) {
                            objektiKenttä[j][i] = null;
                            
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
                        Scanner sc = new Scanner(tiedosto);
                        if (sc.hasNextLine()) {
                            String tarkastettavaRivi = sc.nextLine();
                            if (!tarkastettavaRivi.startsWith("<KEIMO>")) {
                                System.out.println(tarkastettavaRivi);
                                throw new FileNotFoundException();
                            }
                        }
                        while (sc.hasNextLine()) {
                            String tarkastettavaRivi = sc.nextLine();
                            if (tarkastettavaRivi.startsWith("Huone ")) {
                                huoneidenMääräTiedostossa++;
                            }
                        }
                        sc.close();
                        huoneetMerkkijonoina = new String[huoneidenMääräTiedostossa];
                        huoneidenMääräTiedostossa = 0;
                        sc = new Scanner(tiedosto);
                        while (sc.hasNextLine()) {
                            String tarkastettavaRivi = sc.nextLine();
                            if (tarkastettavaRivi.startsWith("Huone ")) {
                                huoneenIdTiedostossa = Integer.parseInt(tarkastettavaRivi.substring(6, tarkastettavaRivi.length()-1));
                                huoneidenMääräTiedostossa++;
                                huoneetMerkkijonoina[huoneidenMääräTiedostossa-1] = "";
                                while (sc.hasNextLine()) {
                                    huoneetMerkkijonoina[huoneidenMääräTiedostossa-1] += tarkastettavaRivi + "\n";
                                    if (tarkastettavaRivi.startsWith("/Huone")) {
                                        break;
                                    }
                                    tarkastettavaRivi = sc.nextLine();
                                }
                            }
                            else if (tarkastettavaRivi.startsWith("</KEIMO>")) {
                                break;
                            }
                        }
                        for (String s : huoneetMerkkijonoina) {
                            System.out.println("huone: " + s);
                        }
                        huoneKartta = luoHuoneKarttaMerkkijonosta(huoneetMerkkijonoina);
                        lataaHuoneKartasta(muokattavaHuone);
                    }
                }
                catch (FileNotFoundException fnfe) {
                    JOptionPane.showMessageDialog(null, "Tiedostovirhe", "Tiedosto puuttuu tai on virheellinen.\n\nKeimon seikkailupeliin tarkoitetut tiedostot alkavat prefiksillä <KEIMO>.", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        tallenna = new JMenuItem("Tallenna");
        tallenna.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tallennaTiedostoon(luoMerkkijonotHuonekartasta(huoneKartta));
            }
        });

        tiedosto = new JMenu("Tiedosto");
        tiedosto.add(uusi);
        tiedosto.add(avaa);
        tiedosto.add(tallenna);

        kokeilePelissä = new JMenuItem("Kokeile pelissä");
        kokeilePelissä.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ikkuna.dispose();
                Main.huoneKartta = huoneKartta;
                Main.uusiHuone = 0;
                Main.huoneVaihdettava = true;
            }
        });

        peli = new JMenu("Peli");
        peli.add(kokeilePelissä);

        yläPalkki = new JMenuBar();
        yläPalkki.setPreferredSize(new Dimension(ikkunanLeveys -20,20));
        yläPalkki.add(tiedosto);
        yläPalkki.add(peli);

        esineValikko = new JComboBox<String>(esineLista);

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
        
        editointiKenttäPaneli = new JPanel();
        editointiKenttäPaneli.setLayout(null);
        editointiKenttäPaneli.setPreferredSize(new Dimension(ikkunanLeveys-30, ikkunanLeveys -30));
        editointiKenttäPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        editointiKenttäPaneli.revalidate();
        editointiKenttäPaneli.repaint();

        editointiKenttäAlue = new JPanel();
        editointiKenttäAlue.setLayout(new BorderLayout());
        editointiKenttäAlue.setPreferredSize(new Dimension(ikkunanLeveys-30, ikkunanLeveys -30));
        editointiKenttäAlue.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        editointiKenttäAlue.add(huoneInfoPaneli, BorderLayout.NORTH);
        editointiKenttäAlue.add(editointiKenttäPaneli, BorderLayout.CENTER);
        editointiKenttäAlue.revalidate();
        editointiKenttäAlue.repaint();

        int kohteenSijX = 10;
        int kohteensijY = 10;
        for (int i = 0; i < Main.kentänKoko; i++) {
            for (int j = 0; j < Main.kentänKoko; j++) {
                JLabel peliTestiLabel = new JLabel("PeliTestiLabel");
                peliTestiLabel.setBounds(kohteenSijX, kohteensijY, esineenKokoPx, esineenKokoPx);
                editointiKenttäPaneli.add(peliTestiLabel);
                kohteenSijX += esineenKokoPx;
            }
            kohteenSijX = 10;
            kohteensijY += esineenKokoPx;
        }

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

        peliAluePaneli = new JPanel();
        peliAluePaneli.setLayout(new BorderLayout());
        peliAluePaneli.add(yläPaneeli, BorderLayout.NORTH);
        peliAluePaneli.add(editointiKenttäAlue, BorderLayout.CENTER);
        peliAluePaneli.add(hud, BorderLayout.SOUTH);

        crd = new CardLayout();
        kortit = new JPanel(crd);
        kortit.add(peliAluePaneli);
        

        ikkuna.add(yläPalkki, BorderLayout.NORTH);
        //ikkuna.add(yläPaneeli);
        //ikkuna.add(peliKenttä);
        //ikkuna.add(hud);
        ikkuna.add(kortit, BorderLayout.CENTER);
        ikkuna.revalidate();
        ikkuna.repaint();

        huoneKartta = Main.huoneKartta;
        luoAlkuIkkuna(0, 0, null);
        lataaHuoneKartasta(muokattavaHuone);
    }

    static void lataaHuoneKartasta(int uusiHuone) {
        objektiKenttä = huoneKartta.get(uusiHuone).annaHuoneenKenttäSisältö();
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
        
        huoneKartta.put(nykyinenHuone, new Huone(nykyinenHuone, 10,  huoneenNimi,null, huoneenAlue, objektiKenttäLista, maastoKenttäLista, false, "alkudialogi"));
        for (int i = 0; i < Main.kentänKoko; i++) {
            for (int j = 0; j < Main.kentänKoko; j++) {
                objektiKenttä[j][i] = null;
                maastoKenttä[j][i] = null;
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

    static KenttäKohde luoObjektiTiedoilla(String objektinNimi, boolean määritettySijainti, int sijX, int sijY) {

        KenttäKohde luotavaObjekti;

        if (määritettySijainti) {
            switch (objektinNimi) {
                case "Avain":
                luotavaObjekti = new Avain(sijX, sijY);
                break;

            case "Hiili":
                luotavaObjekti = new Hiili(sijX, sijY);
                break;

            case "Kaasupullo":
                luotavaObjekti = new Kaasupullo(sijX, sijY);
                break;

            case "Kaasusytytin":
                luotavaObjekti = new Kaasusytytin(sijX, sijY, "tyhjä");
                break;

            case "Kilpi":
                luotavaObjekti = new Kilpi(sijX, sijY);
                break;

            case "Kirstu":
                luotavaObjekti = new Kirstu(sijX, sijY);
                break;

            case "Makkara":
                luotavaObjekti = new Makkara(sijX, sijY);
                break;

            case "Nuotio":
                luotavaObjekti = new Nuotio(sijX, sijY);
                break;

            case "Pahavihu":
                luotavaObjekti = new PahaVihu(sijX, sijY);
                break;

            case "Paperi":
                luotavaObjekti = new Paperi(sijX, sijY);
                break;

            case "Pikkuvihu":
                luotavaObjekti = new PikkuVihu(sijX, sijY);
                break;

            case "Oviruutu":
                luotavaObjekti = new ReunaWarppi(sijX, sijY, 0, 0, 0, Suunta.VASEN);
                break;

            case "Suklaalevy":
                luotavaObjekti = new Suklaalevy(sijX, sijY);
                break;

            case "Vesiämpäri":
                luotavaObjekti = new Vesiämpäri(sijX, sijY);
                break;

            case "Ämpärikone":
                luotavaObjekti = new Ämpärikone(sijX, sijY);
                break;

            default:
                luotavaObjekti = null;
                break;
            }
        }
        else {
            switch (objektinNimi) {
                case "Avain":
                luotavaObjekti = new Avain();
                break;

            case "Hiili":
                luotavaObjekti = new Hiili();
                break;

            case "Kaasupullo":
                luotavaObjekti = new Kaasupullo();
                break;

            case "Kaasusytytin":
                luotavaObjekti = new Kaasusytytin("tyhjä");
                break;

            case "Kilpi":
                luotavaObjekti = new Kilpi();
                break;

            case "Kirstu":
                luotavaObjekti = new Kirstu();
                break;

            case "Makkara":
                luotavaObjekti = new Makkara();
                break;

            case "Nuotio":
                luotavaObjekti = new Nuotio();
                break;

            case "Pahavihu":
                luotavaObjekti = new PahaVihu();
                break;

            case "Paperi":
                luotavaObjekti = new Paperi();
                break;

            case "Pikkuvihu":
                luotavaObjekti = new PikkuVihu();
                break;

            case "Suklaalevy":
                luotavaObjekti = new Suklaalevy();
                break;

            case "Vesiämpäri":
                luotavaObjekti = new Vesiämpäri();
                break;

            case "Ämpärikone":
                luotavaObjekti = new Ämpärikone();
                break;

            default:
                luotavaObjekti = null;
                break;
            }
        }
        return luotavaObjekti;
    }

    static HashMap<Integer, Huone> luoHuoneKarttaMerkkijonosta(String[] merkkijonot) {
        HashMap<Integer, Huone> uusiHuoneKartta = new HashMap<Integer, Huone>();
        int uusiHuoneenId = 0;
        String uusiHuoneenNimi = "";
        String uusiHuoneenAlue = "";
        Image uusiHuoneenTausta = null;

        String luotavaObjekti = "";
        int luotavanObjektinX = 0;
        int luotavanObjektinY = 0;
        ArrayList<KenttäKohde> uusiObjektiLista = new ArrayList<KenttäKohde>();
        ArrayList<Maasto> uusiMaastoLista = new ArrayList<Maasto>();

        try {
            for (String s : merkkijonot) {
                Scanner sc = new Scanner(s);
                while (sc.hasNextLine()) {
                    String tarkastettavaRivi = sc.nextLine();
                    if (tarkastettavaRivi.startsWith("Huone")) {
                        uusiHuoneenId = Integer.parseInt(tarkastettavaRivi.substring(6, tarkastettavaRivi.length() -1));
                    }
                    else if (tarkastettavaRivi.contains("#nimi:")) {
                        uusiHuoneenNimi = tarkastettavaRivi.substring(11, tarkastettavaRivi.length() -1);
                    }
                    else if (tarkastettavaRivi.contains("#alue:")) {
                        uusiHuoneenAlue = tarkastettavaRivi.substring(11, tarkastettavaRivi.length() -1);
                    }
                    else if (tarkastettavaRivi.contains("#tausta:")) {
                        uusiHuoneenTausta = Toolkit.getDefaultToolkit().createImage(tarkastettavaRivi.substring(11, tarkastettavaRivi.length() -1));
                    }
                    else if (tarkastettavaRivi.contains("#kenttä:")) {
                        if (tarkastettavaRivi.contains("{")) {
                            tarkastettavaRivi = sc.nextLine();
                            while (!tarkastettavaRivi.startsWith("}")) {
                                luotavaObjekti = "";
                                luotavanObjektinX = 0;
                                luotavanObjektinY = 0;
                                if (tarkastettavaRivi.contains("_")) {
                                    luotavaObjekti = tarkastettavaRivi.substring(8, tarkastettavaRivi.indexOf("_"));
                                    //System.out.println("sij x: " + tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("_") +1, tarkastettavaRivi.indexOf("_") +2));
                                    //System.out.println("sij y: " + tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("_") +3, tarkastettavaRivi.indexOf("_") +4));
                                    luotavanObjektinX = Integer.parseInt(tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("_") +1, tarkastettavaRivi.indexOf("_") +2));
                                    luotavanObjektinY = Integer.parseInt(tarkastettavaRivi.substring(tarkastettavaRivi.indexOf("_") +3, tarkastettavaRivi.indexOf("_") +4));
                                    uusiObjektiLista.add(luoObjektiTiedoilla(luotavaObjekti, true, luotavanObjektinX, luotavanObjektinY));
                                }
                                else {
                                    uusiObjektiLista.add(luoObjektiTiedoilla(luotavaObjekti, false, luotavanObjektinX, luotavanObjektinY));
                                }
                                if (sc.hasNextLine()) {
                                    tarkastettavaRivi = sc.nextLine();
                                }
                                else {
                                    break;
                                }
                            }
                        }
                    }
                }
                Huone huone = new Huone(uusiHuoneenId, 10, uusiHuoneenNimi, uusiHuoneenTausta, uusiHuoneenAlue, uusiObjektiLista, uusiMaastoLista, false, "");
                uusiHuoneKartta.put(uusiHuoneenId, huone);
                uusiObjektiLista.clear();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            String virheViesti = "";
            virheViesti += ("huone: " + uusiHuoneenId + "\n");
            virheViesti += ("nimi: " + uusiHuoneenNimi + "\n");
            virheViesti += ("alue: " + uusiHuoneenAlue + "\n");
            virheViesti += ("virheellinen objekti: " + luotavaObjekti + "\n");
            virheViesti += ("objektin X: " + luotavanObjektinX + "\n");
            virheViesti += ("objektin Y: " + luotavanObjektinY + "\n");
            System.out.println(virheViesti);
            JOptionPane.showMessageDialog(null, "Tiedostossa on virheellinen asettelu eikä kaikkia elementtejä voitu ladata.\n\nTämä johtuu todennäköisesti siitä, että tiedostoa on muokattu muuten kuin pelinsisäisellä editorilla.\n\n" + virheViesti, "Virhe ladatessa tiedostoa.", JOptionPane.ERROR_MESSAGE);
        }
        return uusiHuoneKartta;
    }

    static String luoMerkkijonotHuonekartasta(HashMap<Integer, Huone> huoneKartta) {
        String kokoTiedostoMerkkijonona = "";
        kokoTiedostoMerkkijonona += "<KEIMO>\n\n";
        String[] huoneetMerkkijonoina = new String[huoneKartta.size()];
        for (Integer i : huoneKartta.keySet()) {
            huoneetMerkkijonoina[i] = "";
            huoneetMerkkijonoina[i] += "Huone " + huoneKartta.get(i).annaId() + ":" + "\n    ";
            huoneetMerkkijonoina[i] += "#nimi: " + huoneKartta.get(i).annaNimi() + ";" + "\n    ";
            huoneetMerkkijonoina[i] += "#alue: " + huoneKartta.get(i).annaAlue() + ";" + "\n    ";
            huoneetMerkkijonoina[i] += "#tausta: " + huoneKartta.get(i).annaTausta() + ";" + "\n    ";
            huoneetMerkkijonoina[i] += "#kenttä: " + "{\n";
            for (KenttäKohde[] kk : huoneKartta.get(i).annaHuoneenKenttäSisältö()) {
                for (KenttäKohde k : kk) {
                    if (k != null) {
                        if (k.onkoMääritettySijainti()) {
                            huoneetMerkkijonoina[i] += "        " + k.annaNimi() + "_" + k.annaSijX() + "_" + k.annaSijY();
                        }
                        else {
                            huoneetMerkkijonoina[i] += "        " + k.annaNimi();
                        }
                        if (k.onkolisäOminaisuuksia()) {
                            huoneetMerkkijonoina[i] += "_ominaisuudet:[";
                            for (String s : k.annalisäOminaisuudet()) {
                                huoneetMerkkijonoina[i] += s + ",";
                            }
                            huoneetMerkkijonoina[i] += "]";
                        }
                        huoneetMerkkijonoina[i] += ",\n";
                    }
                }
            }
            huoneetMerkkijonoina[i] += "    }\n";
            kokoTiedostoMerkkijonona += huoneetMerkkijonoina[i];
            kokoTiedostoMerkkijonona += "/Huone" + "\n";
        }
        kokoTiedostoMerkkijonona += "\n</KEIMO>";
        return kokoTiedostoMerkkijonona;
    }

    static void tallennaTiedostoon(String kokoTiedostoMerkkijonona) {
        JFileChooser tiedostoSelain = new JFileChooser(".\\");
        FileNameExtensionFilter tiedostoSuodatin = new FileNameExtensionFilter("Keimon Seikkailupelin Tiedosto (.kst)", "kst");
        tiedostoSelain.setFileFilter(tiedostoSuodatin);
        int valinta = tiedostoSelain.showOpenDialog(ikkuna);
        if (valinta == JFileChooser.APPROVE_OPTION) {
            try {
                File tiedosto = tiedostoSelain.getSelectedFile();
                if (tiedosto.isFile()) {
                    int korvaaTiedosto = CustomViestiIkkunat.TiedostonKorvaus.showDialog("Tiedosto " + tiedosto.getName() + " on jo olemassa. Haluatko korvata sen?", "Tiedosto on jo olemassa.");
                    if (korvaaTiedosto == JOptionPane.YES_OPTION) {
                        FileWriter tiedostoTallentaja = new FileWriter(tiedosto.getName());
                        tiedostoTallentaja.write(kokoTiedostoMerkkijonona);
                        tiedostoTallentaja.close();
                    }
                }
                else {
                    FileWriter tiedostoTallentaja = new FileWriter(tiedosto.getName() + ".kst");
                    tiedostoTallentaja.write(kokoTiedostoMerkkijonona);
                    tiedostoTallentaja.close();
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

            case "Kaasupullo":
                objektiKenttä[sijX][sijY] = new Kaasupullo(sijX, sijY);
                break;

            case "Kaasusytytin":
                objektiKenttä[sijX][sijY] = new Kaasusytytin(sijX, sijY, "tyhjä");
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

            case "Pikkuvihu":
                objektiKenttä[sijX][sijY] = new PikkuVihu(sijX, sijY);
                break;

            case "Oviruutu":
                objektiKenttä[sijX][sijY] = new ReunaWarppi(sijX, sijY, 0, 0, 0, Suunta.VASEN);
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

    static ArrayList<JMenuItem> luoOikeaClickOminaisuusLista(KenttäKohde k) {

        ArrayList<JMenuItem> menuItemit = new ArrayList<JMenuItem>();

        switch (k.annaNimi()) {

            default:
                break;

            case "Avain":
                break;

            case "Kaasusytytin", "Tyhjä kaasusytytin", "Toimiva kaasusytytin":
                JCheckBoxMenuItem toimiva = new JCheckBoxMenuItem("Toimiva");
                menuItemit.add(toimiva);
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
        static final int ikkunanKorkeus = 165;
        static JFrame muokkausIkkuna;
        static String[] tekstit;
        static int valintojenMäärä;
        static JPanel paneli;
        static JLabel[] tekstiLabelit;
        static JTextField[] tekstiKentät;

        static void hyväksyMuutokset(int sijX, int sijY, String muokattavanKohteenNimi) {
            try {
                switch (muokattavanKohteenNimi) {
                    case "Oviruutu":
                        ReunaWarppi oviruutu = (ReunaWarppi)objektiKenttä[sijX][sijY];
                        oviruutu.asetaKohdeHuone(Integer.parseInt(tekstiKentät[0].getText()));
                        oviruutu.asetaKohdeRuudut(Integer.parseInt(tekstiKentät[1].getText()), Integer.parseInt(tekstiKentät[2].getText()));
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
                    valintojenMäärä = 3;
                    tekstit = new String[valintojenMäärä];
                    tekstit[0] = "Kohdehuone (ID)";
                    tekstit[1] = "Kohderuudun X-sijainti";
                    tekstit[2] = "Kohderuudun Y-sijainti";
                    break;
                default:
                    break;
            }
            
            paneli = new JPanel(new SpringLayout());
            tekstiLabelit = new JLabel[valintojenMäärä];
            tekstiKentät = new JTextField[valintojenMäärä];

            for (int i = 0; i < valintojenMäärä; i++) {
                tekstiLabelit[i] = new JLabel(tekstit[i]);
                tekstiKentät[i] = new JTextField();
                paneli.add(tekstiLabelit[i]);
                paneli.add(tekstiKentät[i]);
            }
            switch (muokattavaKohde.annaNimi()) {
                case "Oviruutu":
                    Warp oviruutu = (Warp)muokattavaKohde;
                    tekstiKentät[0].setText("" + oviruutu.annaKohdeHuone());
                    tekstiKentät[1].setText("" + oviruutu.annaKohdeRuutuX());
                    tekstiKentät[2].setText("" + oviruutu.annaKohdeRuutuY());
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

        editointiKenttäPaneli.removeAll();
        editointiKenttäAlue.setBackground(new Color(0, 0, 0));
        
        int kohteenSijX = 10;
        int kohteensijY = 10;
        objektiKenttä = new KenttäKohde[Main.kentänKoko][Main.kentänKoko];
        maastoKenttä = new Maasto[Main.kentänKoko][Main.kentänKoko];
        
        for (int i = 0; i < Main.kentänKoko; i++) {
            for (int j = 0; j < Main.kentänKoko; j++) {
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
            }
        }
        pelaajaLabel = new JLabel(pelaajaKuvake);
        pelaajaLabel.setBounds(Pelaaja.sijX * pelaajanKokoPx + 10, Pelaaja.sijY * pelaajanKokoPx + 10, pelaajanKokoPx, pelaajanKokoPx);
        //pelaajaLabel.setIcon(valitsePelaajanKuvake());
        taustaLabel = new JLabel(new ImageIcon());
        taustaLabel.setBounds(0, 0, Main.kentänKoko * esineenKokoPx + 20, Main.kentänKoko * esineenKokoPx + 20);
        
        try {
            editointiKenttäPaneli.add(pelaajaLabel);
            editointiKenttäPaneli.add(taustaLabel);
            for (int i = 0; i < Main.kentänKoko; i++) {
                for (int j = 0; j < Main.kentänKoko; j++) {
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
                                    else if (Peli.pelikenttä[j][i] instanceof NPC) {
                                        if (Peli.pelikenttä[j][i] instanceof Vihollinen) {
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
                        editointiKenttäPaneli.add(kenttäKohteenKuvake[j][i]);
                        editointiKenttäPaneli.setComponentZOrder(kenttäKohteenKuvake[j][i], 1);
                        editointiKenttäPaneli.add(maastoKohteenKuvake[j][i]);
                        editointiKenttäPaneli.setComponentZOrder(maastoKohteenKuvake[j][i], 2);
                        kohteenSijX += esineenKokoPx;
                    }
                }
                kohteenSijX = 10;
                kohteensijY += esineenKokoPx;
            }
            
            editointiKenttäPaneli.setComponentZOrder(pelaajaLabel, 0);
            //peliKenttä.setComponentZOrder(taustaLabel, 2);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "Jokin meni pieleen", "Array index out of Bounds", JOptionPane.ERROR_MESSAGE);
        }
        catch (IllegalArgumentException e) {

        }

        editointiKenttäPaneli.revalidate();
        editointiKenttäPaneli.repaint();
    }

    static JPanel päivitäIkkuna() {
        
        //peliKenttä.removeAll();
        //int kohteenSijX = 10;
        //int kohteenSijY = 10;
        
        if (vaatiiPäivityksen && editointiKenttäPaneli != null && objektiKenttä != null && maastoKenttä != null) {
            try {

                huoneInfoLabel.setText("Huone " + muokattavaHuone);

                for (int i = 0; i < Main.kentänKoko; i++) {
                    for (int j = 0; j < Main.kentänKoko; j++) {
                        if (objektiKenttä[j][i] instanceof KenttäKohde) {
                            kenttäKohteenKuvake[j][i].setIcon(objektiKenttä[j][i].annaKuvake());
                        }
                        else if (kenttäKohteenKuvake[j][i] != null) {
                            kenttäKohteenKuvake[j][i].setIcon(null);
                        }
                        if (maastoKenttä[j][i] instanceof Maasto) {
                            maastoKohteenKuvake[j][i].setIcon(maastoKenttä[j][i].annaKuvake());
                        }
                        else if (maastoKohteenKuvake[j][i] != null) {
                            maastoKohteenKuvake[j][i].setIcon(null);
                        }

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
                                    else if (objektiKenttä[j][i] instanceof NPC) {
                                        if (objektiKenttä[j][i] instanceof Vihollinen) {
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
            }

            editointiKenttäPaneli.revalidate();
            editointiKenttäPaneli.repaint();
            //vaatiiPäivityksen = false;
            vaatiiKentänPäivityksen = false;
        }
        return editointiKenttäPaneli;
    }
}
