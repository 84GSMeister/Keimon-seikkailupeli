package keimo.HuoneEditori;

import keimo.*;
import keimo.Kenttäkohteet.*;
import keimo.Kenttäkohteet.Warp.Suunta;
import keimo.Maastot.*;
import keimo.Utility.KäännettäväKuvake.KääntöValinta;
import keimo.Utility.KäännettäväKuvake.Peilaus;
import keimo.Utility.KäännettäväKuvake.PeilausValinta;
import keimo.NPCt.*;
import keimo.NPCt.Vihollinen.LiikeTapa;
import keimo.Säikeet.*;
import keimo.Utility.*;
import keimo.Ikkunat.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.event.*;

import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.*;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import static java.awt.GraphicsDevice.WindowTranslucency.*;

import java.awt.AlphaComposite;
import java.io.*;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.charset.*;
import java.nio.file.*;

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
    static JPanel yläPaneeli, yläPaneelinYläosa, YläPaneelinAlaosa, yläVasenPaneeli, yläOikeaPaneeli;
    static JButton hyväksyNappi;
    static JLabel huoneenNimiTekstiKenttäLabel, huoneenAlueTekstiKenttäLabel, huoneenKuvaTekstiKenttäLabel, huoneenDialogiTekstiKenttäLabel;
    static JTextField huoneenNimiTekstiKenttä, huoneenAlueTekstiKenttä;
    static JButton huoneenNimiTekstiKenttäNappi, huoneenAlueTekstiKenttäNappi, huoneenKuvaTekstiKenttäNappi, huoneenDialogiTekstiKenttäNappi;
    static JButton huoneenKuvaValintaNappi, huoneenDialogiValintaNappi;
    static boolean ctrlPainettu = false;
    static String[] kopioituOminaisuusLista;
    static String kopioituObjektinNimi;

    static JTabbedPane välilehdet;
    static JPanel objektiEditointiKenttäPaneli, maastoEditointiKenttäPaneli, npcEditointiKenttäPaneli, editointiKenttäAlue;
    static JPanel peliAluePaneli;
    static JPanel huoneInfoPaneli, huoneenVaihtoPaneli, huoneenNimiPaneli;
    static JLabel huoneenNimiLabel;
    static JButton huoneenVaihtoNappiVasen, huoneenVaihtoNappiOikea;
    static JButton huoneInfoLabel;
    static String[] kenttäkohdeLista = {"Avain", "Hiili", "Huume", "Juhani", "Kaasupullo", "Kaasusytytin", "Kauppa", "Kilpi", "Kirstu", "Koriste-esine", "Kuparilager", "Makkara", "Nuotio", "Paperi", "Pesäpallomaila", "Oviruutu", "Seteli", "Suklaalevy", "Vesiämpäri", "Ämpärikone"};
    static String[] esineLista = {"Avain", "Hiili", "Huume", "Kaasupullo", "Kaasusytytin", "Kilpi", "Kuparilager", "Makkara", "Paperi", "Pesäpallomaila", "Seteli", "Suklaalevy", "Vesiämpäri"};
    static String[] npcNimiLista = {"Pikkuvihu", "Pahavihu"};
    static JComboBox<String> esineValikko;
    static JComboBox<Object> maastoValikko;
    static JComboBox<String> npcValikko;
    static JComboBox<Object> koristeEsineenKuvaValikko;
    static HashMap<Integer, Huone> huoneKartta = new HashMap<Integer, Huone>();
    static int muokattavaHuone = 0;
    static String huoneenNimi = "";
    static String huoneenAlue = "";
    static ImageIcon huoneenTaustakuva;
    static String huoneenTaustakuvaPolku = "";
    static String huoneenAlkuDialogiTeksti = "";
    static boolean warpVasen = false;
    static boolean warpOikea = false;
    static boolean warpAlas = false;
    static boolean warpYlös = false;
    static int warpVasenHuoneId = 0;
    static int warpOikeaHuoneId = 0;
    static int warpAlasHuoneId = 0;
    static int warpYlösHuoneId = 0;
    static JButton[] warpVasenNappi, warpOikeaNappi, warpAlasNappi, warpYlösNappi;

    static JButton[][] kenttäKohteenKuvake, maastoKohteenKuvake, npcKohteenKuvake;
    static JLabel[][] maastoKohteenKuvakeObjektiPanelissa, maastoKohteenKuvakeNpcPanelissa;
    static KenttäKohde[][] objektiKenttä;
    static Maasto[][] maastoKenttä;
    static NPC[][] npcKenttä;
    static JLabel pelaajaLabel, taustaLabel;
    static JPanel hud;
    static CardLayout crd;
    static JPanel kortit;
    static boolean reunatNäkyvissä = true;
    public static boolean vaatiiPäivityksen = true, vaatiiKentänPäivityksen = false;
    static boolean näytäTallennusVaroitus = true;
    static ArrayList<String> editorinMuutosHistoria = new ArrayList<String>();

    static JPanel tekstiPaneli, infoPaneli;
    static JLabel hudTeksti;
    static JLabel[] kontrolliInfoLabel = new JLabel[9];
    static boolean käytäKopioitujaOminaisuuksia = false;
    static EditorinNäppäinkomennot editorinNäppäinkomennot = new EditorinNäppäinkomennot();
    static boolean muutoksiaTehty = false;
    
    public static void luoEditoriIkkuna() {
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        ikkunanLeveys = esineenKokoPx * Peli.kentänKoko + 110;
        ikkunanKorkeus = esineenKokoPx * Peli.kentänKoko + 450;
        kenttäKohteenKuvake = new JButton[Peli.kentänKoko][Peli.kentänKoko];
        maastoKohteenKuvake = new JButton[Peli.kentänKoko][Peli.kentänKoko];
        npcKohteenKuvake = new JButton[Peli.kentänKoko][Peli.kentänKoko];

        maastoKohteenKuvakeObjektiPanelissa = new JLabel[Peli.kentänKoko][Peli.kentänKoko];
        maastoKohteenKuvakeNpcPanelissa = new JLabel[Peli.kentänKoko][Peli.kentänKoko];
        
        ikkuna = new JFrame("Huone-editori v0.6");
        ikkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja_og.png").getImage());
        ikkuna.setBounds(600, 100, ikkunanLeveys, ikkunanKorkeus);
        ikkuna.setLayout(new BorderLayout());
        ikkuna.setBackground(Color.black);
        ikkuna.setLocationRelativeTo(null);
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
                            npcKenttä[j][i] = null;
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
                int korvaaMuutokset = JOptionPane.OK_OPTION;
                if (muutoksiaTehty) {
                    korvaaMuutokset = CustomViestiIkkunat.MuutoksetEditorissa.showDialog();
                }
                if (korvaaMuutokset != JOptionPane.NO_OPTION) {
                    try {
                        JFileChooser tiedostoSelain = new JFileChooser(".\\");
                        FileNameExtensionFilter tiedostoSuodatin = new FileNameExtensionFilter("Keimon Seikkailupelin Tiedosto (.kst)", "kst");
                        tiedostoSelain.setFileFilter(tiedostoSuodatin);
                        int valinta = tiedostoSelain.showOpenDialog(ikkuna);
                        if (valinta == JFileChooser.APPROVE_OPTION) {
                            File tiedosto = tiedostoSelain.getSelectedFile();
                            String[] huoneetMerkkijonoina;
                            int huoneidenMääräTiedostossa = 0;
                            Path path = FileSystems.getDefault().getPath(tiedosto.getPath());
                            Charset charset = Charset.forName("UTF-8");
                            //Scanner sc = new Scanner(tiedosto);
                            BufferedReader read = Files.newBufferedReader(path, charset);
                            String tarkastettavaRivi = null;
                            if ((tarkastettavaRivi = read.readLine()) != null) {
                                //tarkastettavaRivi = read.readLine();
                                if (!tarkastettavaRivi.startsWith("<KEIMO>")) {
                                    //System.out.println(tarkastettavaRivi);
                                    System.out.println(tarkastettavaRivi);
                                    throw new FileNotFoundException();
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
                            warpVasen = huoneKartta.get(muokattavaHuone).annaReunaWarppiTiedot(Suunta.VASEN);
                            warpOikea = huoneKartta.get(muokattavaHuone).annaReunaWarppiTiedot(Suunta.OIKEA);
                            warpAlas = huoneKartta.get(muokattavaHuone).annaReunaWarppiTiedot(Suunta.ALAS);
                            warpYlös = huoneKartta.get(muokattavaHuone).annaReunaWarppiTiedot(Suunta.YLÖS);
                            warpVasenHuoneId = huoneKartta.get(muokattavaHuone).annaReunaWarpinKohdeId(Suunta.VASEN);
                            warpOikeaHuoneId = huoneKartta.get(muokattavaHuone).annaReunaWarpinKohdeId(Suunta.OIKEA);
                            warpAlasHuoneId = huoneKartta.get(muokattavaHuone).annaReunaWarpinKohdeId(Suunta.ALAS);
                            warpYlösHuoneId = huoneKartta.get(muokattavaHuone).annaReunaWarpinKohdeId(Suunta.YLÖS);
                            for (Maasto[] mm : maastoKenttä) {
                                for (Maasto m : mm) {
                                    m.päivitäKuvanAsento();
                                }
                            }
                        }
                    }
                    catch (FileNotFoundException fnfe) {
                        JOptionPane.showMessageDialog(null, "Tiedosto puuttuu tai on virheellinen.\n\nKeimon seikkailupeliin tarkoitetut tiedostot alkavat prefiksillä <KEIMO>.\n\nTämä virhe voi tulla, jos tiedostoa on muokattu muuten kuin pelinsisäisellä editorilla.", "Tiedostovirhe", JOptionPane.ERROR_MESSAGE);
                    }
                    catch (IOException ioe) {
    
                    }
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
                
                if (näytäTallennusVaroitus) {
                    int valinta = CustomViestiIkkunat.TiedostonTallennusVaroitus.showDialog();
                    if (valinta == JOptionPane.NO_OPTION) {
                        näytäTallennusVaroitus = false;
                    }
                    if (valinta == JOptionPane.YES_OPTION || valinta == JOptionPane.NO_OPTION) {
                        asetaUusiHuoneKarttaan(muokattavaHuone, true);
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
                }
                else {
                    asetaUusiHuoneKarttaan(muokattavaHuone, true);
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
            }
        });

        peli = new JMenu("Peli");
        peli.add(kokeilePelissä);

        yläPalkki = new JMenuBar();
        yläPalkki.setPreferredSize(new Dimension(ikkunanLeveys -20,20));
        yläPalkki.add(tiedosto);
        yläPalkki.add(peli);

        huoneenNimiTekstiKenttäLabel = new JLabel("Huoneen nimi: ");
        huoneenNimiTekstiKenttäLabel.setToolTipText("Nimi ei ole pakollinen, mutta selkeyttävä seikka");
        huoneenNimiTekstiKenttä = new JTextField();
        huoneenNimiTekstiKenttä.setPreferredSize(new Dimension(200, 20));
        huoneenNimiTekstiKenttä.setToolTipText("Nimi ei ole pakollinen, mutta selkeyttävä seikka");
        huoneenAlueTekstiKenttäLabel = new JLabel("Huoneen alue: ");
        huoneenAlueTekstiKenttäLabel.setToolTipText("Tämä näkyy ruudun yläreunassa");
        huoneenAlueTekstiKenttä = new JTextField();
        huoneenAlueTekstiKenttä.setPreferredSize(new Dimension(200, 20));
        huoneenAlueTekstiKenttä.setToolTipText("Tämä näkyy ruudun yläreunassa");
        huoneenKuvaTekstiKenttäLabel = new JLabel("Huoneen tausta: ");
        huoneenKuvaTekstiKenttäLabel.setToolTipText("Huoneen taustaksi suositellaan 660x660 pikselin kuvia. Muunkokoiset kuvat voivat näyttää väärältä tai eivät näy kokonaan.");
        huoneenKuvaValintaNappi = new JButton("Valitse kuva");
        huoneenKuvaValintaNappi.setToolTipText("Huoneen taustaksi suositellaan 660x660 pikselin kuvia. Muunkokoiset kuvat voivat näyttää väärältä tai eivät näy kokonaan.");
        huoneenKuvaValintaNappi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser tiedostoSelain = new JFileChooser(".\\");
                FileNameExtensionFilter tiedostoSuodatin = new FileNameExtensionFilter("Kuvatiedosto (.png .jpg .gif)", "png", "jpg", "gif");
                tiedostoSelain.setFileFilter(tiedostoSuodatin);
                int valinta = tiedostoSelain.showOpenDialog(ikkuna);
                if (valinta == JFileChooser.APPROVE_OPTION) {
                    File tiedosto = tiedostoSelain.getSelectedFile();
                    huoneenTaustakuvaPolku = tiedosto.getName();
                    huoneenTaustakuva = new ImageIcon("tiedostot/kuvat/taustat/" + huoneenTaustakuvaPolku);
                    huoneKartta.get(muokattavaHuone).päivitäTausta(huoneenTaustakuvaPolku);
                }
            }
        });
        huoneenDialogiTekstiKenttäLabel = new JLabel("Huoneen alkudialogi: ");
        huoneenDialogiTekstiKenttäLabel.setToolTipText("Alkudialogi/tarinaeditori mahdollisesti tulossa myöhemmin!");
        huoneenDialogiValintaNappi = new JButton("Valitse teksti");
        huoneenDialogiValintaNappi.setEnabled(false);
        huoneenDialogiValintaNappi.setToolTipText("Alkudialogi/tarinaeditori mahdollisesti tulossa myöhemmin!");
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
                huoneKartta.get(muokattavaHuone).päivitäHuoneenKenttäSisältö(objektiKenttä);
                huoneKartta.get(muokattavaHuone).päivitäHuoneenMaastoSisältö(maastoKenttä);
                huoneKartta.get(muokattavaHuone).päivitäHuoneenNPCSisältö(npcKenttä);
                huoneenNimiLabel.setText(huoneenNimi + " (" + huoneenAlue + ")");
            }
        });

        yläOikeaPaneeli = new JPanel();
        yläOikeaPaneeli.setLayout(new BorderLayout());
        yläOikeaPaneeli.add(hyväksyNappi, BorderLayout.CENTER);

        yläPaneelinYläosa = new JPanel();
        yläPaneelinYläosa.setLayout(new BorderLayout());
        yläPaneelinYläosa.setMaximumSize(new Dimension(500, 60));
        yläPaneelinYläosa.add(yläVasenPaneeli, BorderLayout.WEST);
        yläPaneelinYläosa.add(yläOikeaPaneeli, BorderLayout.EAST);

        JButton perumisNappi = new JButton(new ImageIcon("tiedostot/kuvat/menu/gui/peru.png"));
        perumisNappi.setBounds(0, 0, 20, 20);
        perumisNappi.setToolTipText("Peru viimeisin muutos");
        perumisNappi.addKeyListener(editorinNäppäinkomennot);
        perumisNappi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                peruMuutos();
            }
        });

        YläPaneelinAlaosa = new JPanel();
        YläPaneelinAlaosa.setLayout(new FlowLayout(FlowLayout.LEFT));
        YläPaneelinAlaosa.setMaximumSize(new Dimension(500, 20));
        YläPaneelinAlaosa.setBounds(0, 0, 500, 30);
        YläPaneelinAlaosa.add(perumisNappi);

        yläPaneeli = new JPanel();
        yläPaneeli.setLayout(new BorderLayout());
        yläPaneeli.add(yläPaneelinYläosa, BorderLayout.NORTH);
        yläPaneeli.add(YläPaneelinAlaosa, BorderLayout.SOUTH);
        yläPaneeli.setPreferredSize(new Dimension(ikkunanLeveys -30, 90));
        yläPaneeli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        yläPaneeli.revalidate();
        yläPaneeli.repaint();

        esineValikko = new JComboBox<String>(kenttäkohdeLista);
        esineValikko.addKeyListener(editorinNäppäinkomennot);
        esineValikko.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                käytäKopioitujaOminaisuuksia = false;
                if (esineValikko.getSelectedItem() == "Koriste-esine") {
                    koristeEsineenKuvaValikko.setEnabled(true);
                    koristeEsineenKuvaValikko.setToolTipText("Valitse koriste-esineen kuva. Kuvat, joiden nimi päättyy '_e' luovat esteen.");
                }
                else {
                    koristeEsineenKuvaValikko.setEnabled(false);
                    koristeEsineenKuvaValikko.setToolTipText("Käytössä vain koriste-esineille.");
                }
            }
        });
        maastoValikko = new JComboBox<Object>(listaaKuvat("tiedostot/kuvat/maasto").toArray());
        maastoValikko.addKeyListener(editorinNäppäinkomennot);
        maastoValikko.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                käytäKopioitujaOminaisuuksia = false;
            }
        });
        npcValikko = new JComboBox<String>(npcNimiLista);
        npcValikko.addKeyListener(editorinNäppäinkomennot);
        npcValikko.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                käytäKopioitujaOminaisuuksia = false;
            }
        });

        koristeEsineenKuvaValikko = new JComboBox<Object>(listaaKuvat("tiedostot/kuvat/kenttäkohteet/visuaaliset_objektit").toArray());
        koristeEsineenKuvaValikko.addKeyListener(editorinNäppäinkomennot);
        koristeEsineenKuvaValikko.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                käytäKopioitujaOminaisuuksia = false;
            }
        });

        huoneenNimiLabel = new JLabel("Huone (alue)");
        huoneenNimiLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
        //huoneenNimiLabel.setBounds(15, 15, 200, 45);
        huoneenNimiPaneli = new JPanel(new GridBagLayout());
        huoneenNimiPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        huoneenNimiPaneli.setPreferredSize(new Dimension(380, 60));
        huoneenNimiPaneli.add(huoneenNimiLabel);

        huoneInfoLabel = new JButton("Huone");
        huoneInfoLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
        huoneInfoLabel.setHorizontalAlignment(JLabel.CENTER);
        huoneInfoLabel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                HuoneenVaihtoIkkuna.luoHuoneenVaihtoikkuna();
            }
        });

        huoneenVaihtoNappiVasen = new JButton(new KäännettäväKuvake(new ImageIcon("tiedostot/kuvat/osoitin_pieni.png"), 90));
        huoneenVaihtoNappiVasen.setPreferredSize(new Dimension(60, 60));
        huoneenVaihtoNappiVasen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (muokattavaHuone <= 0) {
                    
                }
                else {
                    int uusiHuone = muokattavaHuone -1;
                    vaihdaHuonetta(muokattavaHuone, uusiHuone, true);
                    muokattavaHuone--;
                }
            }
        });
        huoneenVaihtoNappiOikea = new JButton(new KäännettäväKuvake(new ImageIcon("tiedostot/kuvat/osoitin_pieni.png"), 270));
        huoneenVaihtoNappiOikea.setPreferredSize(new Dimension(60, 60));
        huoneenVaihtoNappiOikea.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int uusiHuone = muokattavaHuone +1;
                vaihdaHuonetta(muokattavaHuone, uusiHuone, true);
                muokattavaHuone++;
            }
        });

        huoneenVaihtoPaneli = new JPanel(new SpringLayout());
        huoneenVaihtoPaneli.add(huoneenVaihtoNappiVasen);
        huoneenVaihtoPaneli.add(huoneInfoLabel);
        huoneenVaihtoPaneli.add(huoneenVaihtoNappiOikea);
        huoneenVaihtoPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        huoneenVaihtoPaneli.setPreferredSize(new Dimension(180, 60));
        SpringUtilities.makeCompactGrid(huoneenVaihtoPaneli, 1, 3, 0, 0, 6, 2);

        JPanel esineValikkoPaneli = new JPanel(new BorderLayout());
        esineValikkoPaneli.setPreferredSize(new Dimension(180, 60));
        esineValikkoPaneli.add(esineValikko, BorderLayout.CENTER);
        esineValikkoPaneli.add(koristeEsineenKuvaValikko, BorderLayout.SOUTH);

        huoneInfoPaneli = new JPanel();
        huoneInfoPaneli.setLayout(new BorderLayout());
        huoneInfoPaneli.setPreferredSize(new Dimension(300, 60));
        huoneInfoPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        huoneInfoPaneli.add(esineValikkoPaneli, BorderLayout.WEST);
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
                JLabel peliTestiLabel = new JLabel("");
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
                JLabel peliTestiLabel = new JLabel("");
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
                JLabel peliTestiLabel = new JLabel("");
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
                    if (esineValikkoPaneli.isAncestorOf(esineValikko)) {
                        esineValikkoPaneli.remove(esineValikko);
                    }
                    if (esineValikkoPaneli.isAncestorOf(maastoValikko)) {
                        esineValikkoPaneli.remove(maastoValikko);
                    }
                    if (esineValikkoPaneli.isAncestorOf(npcValikko)) {
                        esineValikkoPaneli.remove(npcValikko);
                    }

                    switch (välilehdet.getTitleAt(välilehdet.getSelectedIndex())) {
                        
                        case "Objektit":
                            esineValikkoPaneli.add(esineValikko, BorderLayout.WEST);
                            if (esineValikko.getSelectedItem() == "Koriste-esine") {
                                koristeEsineenKuvaValikko.setEnabled(true);
                            }
                            break;
                        case "Maasto":
                            esineValikkoPaneli.add(maastoValikko, BorderLayout.WEST);
                            koristeEsineenKuvaValikko.setEnabled(false);
                            break;
                        case "NPC:t":
                            esineValikkoPaneli.add(npcValikko, BorderLayout.WEST);
                            koristeEsineenKuvaValikko.setEnabled(false);
                            break;
                        default: break;
                    }
                    huoneInfoPaneli.revalidate();
                    huoneInfoPaneli.repaint();
                    huoneInfoPaneli.repaint();
                    huoneInfoPaneli.repaint();
                    huoneInfoPaneli.repaint();
                    huoneInfoPaneli.repaint();
                    ikkuna.revalidate();
                    ikkuna.repaint();
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
        kontrolliInfoLabel[4] = new JLabel("CTRL + hiiren vasen: kopioi tile");
        kontrolliInfoLabel[5] = new JLabel("CTRL + Z: peru viimeisin muutos");
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
        kortit.add(peliAluePaneli);

        ikkuna.add(yläPalkki, BorderLayout.NORTH);
        ikkuna.add(kortit, BorderLayout.CENTER);
        ikkuna.addKeyListener(editorinNäppäinkomennot);
        ikkuna.requestFocus();
        ikkuna.revalidate();
        ikkuna.repaint();

        huoneKartta = Peli.huoneKartta;
        luoAlkuIkkuna(0, 0, null);
        lataaHuoneKartasta(muokattavaHuone);
    }

    public static boolean editoriAuki() {
        if (ikkuna == null) {
            return false;
        }
        else {
            if (ikkuna.isVisible()) {
                return true;
            }
            else {
                return false;
            }
        }
    }

    public static void asetaPäällimmäiseksi() {
        ikkuna.requestFocus();
    }

    // public static Set<String> listaaMaastot(String dir) {
    //     return Stream.of(new File(dir).listFiles())
    //       .filter(file -> !file.isDirectory() && (file.getName().endsWith(".png") || file.getName().endsWith(".jpg") || file.getName().endsWith(".gif")))
    //       .map(File::getName).sorted()
    //       .collect(Collectors.toSet());
    // }

    public static List<String> listaaKuvat(String dir) {
        List<String> kuvaLista = Stream.of(new File(dir).listFiles())
            .filter(file -> !file.isDirectory() && (file.getName().endsWith(".png") || file.getName().endsWith(".jpg") || file.getName().endsWith(".gif")))
            .map(File::getName).sorted()
            .collect(Collectors.toList());
        Collections.sort(kuvaLista, new Comparator<String>() {
            public int compare(String e1, String e2) {
                return e1.compareTo(e2);
            }
        });
        return kuvaLista;
    }

    static void lataaHuoneKartasta(int uusiHuone) {
        objektiKenttä = huoneKartta.get(uusiHuone).annaHuoneenKenttäSisältö();
        maastoKenttä = huoneKartta.get(uusiHuone).annaHuoneenMaastoSisältö();
        npcKenttä = huoneKartta.get(uusiHuone).annaHuoneenNPCSisältö();
        huoneenNimi = huoneKartta.get(uusiHuone).annaNimi();
        huoneenAlue = huoneKartta.get(uusiHuone).annaAlue();
        huoneenNimiLabel.setText(huoneenNimi + " (" + huoneenAlue + ")");
        huoneenNimiTekstiKenttä.setText(huoneenNimi);
        huoneenAlueTekstiKenttä.setText(huoneenAlue);
        huoneenTaustakuvaPolku = huoneKartta.get(uusiHuone).annaTaustanPolku();
        warpVasen = huoneKartta.get(uusiHuone).annaReunaWarppiTiedot(Suunta.VASEN);
        warpVasenHuoneId = huoneKartta.get(uusiHuone).annaReunaWarpinKohdeId(Suunta.VASEN);
        warpOikea = huoneKartta.get(uusiHuone).annaReunaWarppiTiedot(Suunta.OIKEA);
        warpOikeaHuoneId = huoneKartta.get(uusiHuone).annaReunaWarpinKohdeId(Suunta.OIKEA);
        warpAlas = huoneKartta.get(uusiHuone).annaReunaWarppiTiedot(Suunta.ALAS);
        warpAlasHuoneId = huoneKartta.get(uusiHuone).annaReunaWarpinKohdeId(Suunta.ALAS);
        warpYlös = huoneKartta.get(uusiHuone).annaReunaWarppiTiedot(Suunta.YLÖS);
        warpYlösHuoneId = huoneKartta.get(uusiHuone).annaReunaWarpinKohdeId(Suunta.YLÖS);
    }

    static void asetaUusiHuoneKarttaan(int nykyinenHuone, boolean tyhjennäHuone) {
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
        huoneKartta.put(nykyinenHuone, new Huone(nykyinenHuone, 10,  huoneenNimi, huoneenTaustakuvaPolku, huoneenAlue, objektiKenttäLista, maastoKenttäLista, npcKenttäLista, false, "alkudialogi"));
        if (tyhjennäHuone) {
            huoneKartta.get(nykyinenHuone).päivitäReunawarppienTiedot(warpVasen, warpVasenHuoneId, warpOikea, warpOikeaHuoneId, warpAlas, warpAlasHuoneId, warpYlös, warpYlösHuoneId);
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
    }

    static void vaihdaHuonetta(int nykyinenHuone, int seuraavaHuone, boolean tyhjennäHuone) {
        
        if (huoneKartta.containsKey(seuraavaHuone)) {
            asetaUusiHuoneKarttaan(nykyinenHuone, tyhjennäHuone);
            lataaHuoneKartasta(seuraavaHuone);
        }
        else {
            asetaUusiHuoneKarttaan(nykyinenHuone, tyhjennäHuone);
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
                muutoksiaTehty = false;
            }
            catch (IOException e) {
    
            } 
        }
    }

    static void tallennaMuutos(String muutos) {
        System.out.println(muutos);
        editorinMuutosHistoria.add(muutos);
    }

    static void peruMuutos() {
        if (editorinMuutosHistoria != null) {
            if (editorinMuutosHistoria.size() > 0) {
                String peruttavaMuutos = editorinMuutosHistoria.get(editorinMuutosHistoria.size()-1);
                int peruttavanKohteenX = 0, peruttavanKohteenY = 0;
                try {
                    peruttavanKohteenX = Integer.parseInt(peruttavaMuutos.substring(peruttavaMuutos.indexOf("x=")+2, peruttavaMuutos.indexOf("x=")+3));
                    peruttavanKohteenY = Integer.parseInt(peruttavaMuutos.substring(peruttavaMuutos.indexOf("y=")+2, peruttavaMuutos.indexOf("y=")+3));
                }
                catch (NumberFormatException e) {
                    e.printStackTrace();
                    System.out.println(peruttavaMuutos.substring(peruttavaMuutos.indexOf("x=")+2, peruttavaMuutos.indexOf("x=")+3));
                }
                if (peruttavaMuutos.contains("objekti")) {
                    if (peruttavaMuutos.contains("aseta")) {
                        String objektinNimi = peruttavaMuutos.substring(peruttavaMuutos.indexOf("aseta_")+6, peruttavaMuutos.indexOf("x=")-1);
                        System.out.println("palautetaan: " + objektiKenttä[peruttavanKohteenX][peruttavanKohteenY] + " tilalle " + objektinNimi);
                        String ominaisuudet = peruttavaMuutos.substring(peruttavaMuutos.indexOf("+ominaisuudet")+15, peruttavaMuutos.length()-1);
                        if (ominaisuudet != "") {
                            asetaEsineRuutuun(peruttavanKohteenX, peruttavanKohteenY, objektinNimi, ominaisuudet.split(","));
                        }
                        else {
                            asetaEsineRuutuun(peruttavanKohteenX, peruttavanKohteenY, objektinNimi, null);
                        }
                    }
                    else if (peruttavaMuutos.contains("poista")) {
                        String objektinNimi = peruttavaMuutos.substring(peruttavaMuutos.indexOf("poista_")+7, peruttavaMuutos.indexOf("x=")-1);
                        String ominaisuudet = peruttavaMuutos.substring(peruttavaMuutos.indexOf("+ominaisuudet")+15, peruttavaMuutos.length()-1);
                        if (ominaisuudet != "") {
                            asetaEsineRuutuun(peruttavanKohteenX, peruttavanKohteenY, objektinNimi, ominaisuudet.split(","));
                        }
                        else {
                            asetaEsineRuutuun(peruttavanKohteenX, peruttavanKohteenY, objektinNimi, null);
                        }
                    }
                }
                else if (peruttavaMuutos.contains("maasto")) {
                    if (peruttavaMuutos.contains("aseta")) {
                        String maastonNimi = peruttavaMuutos.substring(peruttavaMuutos.indexOf("aseta_")+6, peruttavaMuutos.indexOf("x=")-1);
                        System.out.println("palautetaan: " + maastoKenttä[peruttavanKohteenX][peruttavanKohteenY] + " tilalle " + maastonNimi);
                        String ominaisuudet = peruttavaMuutos.substring(peruttavaMuutos.indexOf("+ominaisuudet")+15, peruttavaMuutos.length()-1);
                        asetaMaastoRuutuun(peruttavanKohteenX, peruttavanKohteenY, maastonNimi, ominaisuudet.split(","));
                    }
                    else if (peruttavaMuutos.contains("poista")) {
                        String maastonNimi = peruttavaMuutos.substring(peruttavaMuutos.indexOf("poista_")+7, peruttavaMuutos.indexOf("x=")-1);
                        String ominaisuudet = peruttavaMuutos.substring(peruttavaMuutos.indexOf("+ominaisuudet")+15, peruttavaMuutos.length()-1);
                        asetaMaastoRuutuun(peruttavanKohteenX, peruttavanKohteenY, maastonNimi, ominaisuudet.split(","));
                    }
                }
                else if (peruttavaMuutos.contains("npc")) {
                    if (peruttavaMuutos.contains("aseta")) {
                        String npcnNimi = peruttavaMuutos.substring(peruttavaMuutos.indexOf("aseta_")+6, peruttavaMuutos.indexOf("x=")-1);
                        System.out.println("palautetaan: " + npcKenttä[peruttavanKohteenX][peruttavanKohteenY] + " tilalle " + npcnNimi);
                        String ominaisuudet = peruttavaMuutos.substring(peruttavaMuutos.indexOf("+ominaisuudet")+15, peruttavaMuutos.length()-1);
                        asetaNPCRuutuun(peruttavanKohteenX, peruttavanKohteenY, npcnNimi, ominaisuudet.split(","));
                    }
                    else if (peruttavaMuutos.contains("poista")) {
                        String npcnNimi = peruttavaMuutos.substring(peruttavaMuutos.indexOf("poista_")+7, peruttavaMuutos.indexOf("x=")-1);
                        String ominaisuudet = peruttavaMuutos.substring(peruttavaMuutos.indexOf("+ominaisuudet")+15, peruttavaMuutos.length()-1);
                        asetaNPCRuutuun(peruttavanKohteenX, peruttavanKohteenY, npcnNimi, ominaisuudet.split(","));
                    }
                }
                editorinMuutosHistoria.remove(editorinMuutosHistoria.size()-1);
            }
            else {
                System.out.println("Muutoshistoria on tyhjä.");
            }
        }
    }

    static void asetaEsineRuutuun(int sijX, int sijY, String esineenNimi, String[] ominaisuusLista) {
        if (ominaisuusLista != null) {
            switch (esineenNimi) {

                case "Kaasusytytin":
                    objektiKenttä[sijX][sijY] = new Kaasusytytin(true, sijX, sijY, ominaisuusLista);
                    break;

                case "Kirstu":
                    objektiKenttä[sijX][sijY] = new Kirstu(true, sijX, sijY, ominaisuusLista);
                    break;

                case "Oviruutu":
                    objektiKenttä[sijX][sijY] = new Oviruutu(sijX, sijY, ominaisuusLista);
                    break;

                case "Koriste-esine":
                    objektiKenttä[sijX][sijY] = new VisuaalinenObjekti(true, sijX, sijY, ominaisuusLista);
                    break;

                default:
                    objektiKenttä[sijX][sijY] = null;
                    break;
            }
        }
        else {
            switch (esineenNimi) {

                case "Avain":
                    objektiKenttä[sijX][sijY] = new Avain(true, sijX, sijY);
                    break;

                case "Hiili":
                    objektiKenttä[sijX][sijY] = new Hiili(true, sijX, sijY);
                    break;

                case "Huume":
                    objektiKenttä[sijX][sijY] = new Huume(true, sijX, sijY);
                    break;

                case "Juhani":
                    objektiKenttä[sijX][sijY] = new Juhani(true, sijX, sijY);
                    break;

                case "Kaasupullo":
                    objektiKenttä[sijX][sijY] = new Kaasupullo(true, sijX, sijY);
                    break;

                case "Kaasusytytin":
                    objektiKenttä[sijX][sijY] = new Kaasusytytin(true, sijX, sijY, null);
                    break;

                case "Kauppa":
                    objektiKenttä[sijX][sijY] = new Kauppa(true, sijX, sijY);
                    break;

                case "Kilpi":
                    objektiKenttä[sijX][sijY] = new Kilpi(true, sijX, sijY);
                    break;

                case "Kirstu":
                    objektiKenttä[sijX][sijY] = new Kirstu(true, sijX, sijY, null);
                    break;
                
                case "Koriste-esine":
                    objektiKenttä[sijX][sijY] = new VisuaalinenObjekti(true, sijX, sijY, null);
                    break;

                case "Kuparilager":
                    objektiKenttä[sijX][sijY] = new Kuparilager(true, sijX, sijY);
                    break;

                case "Makkara":
                    objektiKenttä[sijX][sijY] = new Makkara(true, sijX, sijY);
                    break;

                case "Nuotio":
                    objektiKenttä[sijX][sijY] = new Nuotio(true, sijX, sijY);
                    break;

                case "Paperi":
                    objektiKenttä[sijX][sijY] = new Paperi(true, sijX, sijY);
                    break;

                case "Pesäpallomaila":
                    objektiKenttä[sijX][sijY] = new Pesäpallomaila(true, sijX, sijY);
                    break;

                case "Oviruutu":
                    objektiKenttä[sijX][sijY] = new Oviruutu(sijX, sijY, null);
                    break;

                case "Seteli":
                    objektiKenttä[sijX][sijY] = new Seteli(true, sijX, sijY);
                    break;

                case "Suklaalevy":
                    objektiKenttä[sijX][sijY] = new Suklaalevy(true, sijX, sijY);
                    break;

                case "Vesiämpäri":
                    objektiKenttä[sijX][sijY] = new Vesiämpäri(true, sijX, sijY);
                    break;

                case "Ämpärikone":
                    objektiKenttä[sijX][sijY] = new Ämpärikone(true, sijX, sijY);
                    break;

                default:
                    objektiKenttä[sijX][sijY] = null;
                    break;
            }
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

    static void asetaNPCRuutuun(int sijX, int sijY, String npcnNimi, String[] ominaisuusLista) {
        
        switch (npcnNimi) {

            case "Pikkuvihu":
                npcKenttä[sijX][sijY] = new Pikkuvihu(sijX, sijY, ominaisuusLista);
                break;

            case "Pahavihu":
                npcKenttä[sijX][sijY] = new Pahavihu(sijX, sijY, ominaisuusLista);
                break;

            default:
                npcKenttä[sijX][sijY] = null;
                break;
        }
    }

    static ArrayList<JMenuItem> luoOikeaClickOminaisuusLista(KenttäKohde k) {

        ArrayList<JMenuItem> menuItemit = new ArrayList<JMenuItem>();

        JMenuItem tiedot = new JMenuItem("Tiedot");
        tiedot.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, k.annaTiedot(), "Objektin tiedot", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        tiedot.setText("Tiedot: " + k.annaNimi());
        menuItemit.add(tiedot);

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
                Oviruutu r = (Oviruutu)k;
                JMenuItem warppiKohde = new JMenuItem("Muokkaa warpin kohdetta");
                warppiKohde.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        ObjektinMuokkausIkkuna.luoObjektinMuokkausIkkuna(k.annaSijX(), k.annaSijY(), k);
                    }
                });
                menuItemit.add(warppiKohde);

                JMenuItem lataaWarpinKohdehuone = new JMenuItem("Lataa kohdehuone");
                lataaWarpinKohdehuone.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        int uusiHuone = r.annaKohdeHuone();
                        vaihdaHuonetta(muokattavaHuone, uusiHuone, true);
                        muokattavaHuone = uusiHuone;
                    }
                });
                menuItemit.add(lataaWarpinKohdehuone);
            break;

            case "Kirstu":
                Kirstu kirstu = (Kirstu)k;
                JMenuItem kirstunSisältö = new JMenuItem("Sisältö: " + kirstu.annaSisältö());
                kirstunSisältö.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        ObjektinMuokkausIkkuna.luoObjektinMuokkausIkkuna(k.annaSijX(), k.annaSijY(), k);
                    }
                });
                menuItemit.add(kirstunSisältö);
            break;

            case "Koriste-esine":
                VisuaalinenObjekti ke = (VisuaalinenObjekti)k;
                JMenu kääntöValikko = new JMenu("Kierrä kuvaa");
                JMenuItem kääntöMyötäpäivään = new JMenuItem("Kierrä myötäpäivään", new ImageIcon("tiedostot/kuvat/menu/gui/kierrä_myötäpäivään.png"));
                kääntöMyötäpäivään.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        ke.käännäKuvaa(KääntöValinta.MYÖTÄPÄIVÄÄN);
                    }
                });
                JMenuItem kääntöVastapäivään = new JMenuItem("Kierrä vastapäivään", new ImageIcon("tiedostot/kuvat/menu/gui/kierrä_vastapäivään.png"));
                kääntöVastapäivään.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        ke.käännäKuvaa(KääntöValinta.VASTAPÄIVÄÄN);
                    }
                });
                kääntöValikko.add(kääntöMyötäpäivään);
                kääntöValikko.add(kääntöVastapäivään);
                menuItemit.add(kääntöValikko);
                
                JMenu peilausValikko = new JMenu("Peilaa kuva");
                JMenuItem peilausVaaka = new JMenuItem("Peilaa vaakasuunnassa", new ImageIcon("tiedostot/kuvat/menu/gui/peilaa_vaaka.png"));
                peilausVaaka.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        ke.peilaaKuva(PeilausValinta.PEILAA_VAAKA);
                    }
                });
                JMenuItem peilausPysty = new JMenuItem("Peilaa pystysuunnassa", new ImageIcon("tiedostot/kuvat/menu/gui/peilaa_pysty.png"));
                peilausPysty.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        ke.peilaaKuva(PeilausValinta.PEILAA_PYSTY);
                    }
                });
                peilausValikko.add(peilausVaaka);
                peilausValikko.add(peilausPysty);
                menuItemit.add(peilausValikko);
            break;
        }
        return menuItemit;
    }

    static ArrayList<JMenuItem> luoOikeaClickOminaisuusLista(Maasto m) {

        ArrayList<JMenuItem> menuItemit = new ArrayList<JMenuItem>();

        JMenuItem tiedot = new JMenuItem("Tiedot");
        tiedot.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, m.annaTiedot(), "Maaston tiedot", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        tiedot.setText("Tiedot: " + m.annaNimi());
        menuItemit.add(tiedot);

        switch (m.annaNimi()) {

            default:
            break;

            case "Tile", "EsteTile":
                JMenu kääntöValikko = new JMenu("Kierrä kuvaa");
                JMenuItem kääntöMyötäpäivään = new JMenuItem("Kierrä myötäpäivään", new ImageIcon("tiedostot/kuvat/menu/gui/kierrä_myötäpäivään.png"));
                kääntöMyötäpäivään.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        m.käännäKuvaa(KääntöValinta.MYÖTÄPÄIVÄÄN);
                    }
                });
                JMenuItem kääntöVastapäivään = new JMenuItem("Kierrä vastapäivään", new ImageIcon("tiedostot/kuvat/menu/gui/kierrä_vastapäivään.png"));
                kääntöVastapäivään.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        m.käännäKuvaa(KääntöValinta.VASTAPÄIVÄÄN);
                    }
                });
                kääntöValikko.add(kääntöMyötäpäivään);
                kääntöValikko.add(kääntöVastapäivään);
                menuItemit.add(kääntöValikko);
                
                JMenu peilausValikko = new JMenu("Peilaa kuva");
                JMenuItem peilausVaaka = new JMenuItem("Peilaa vaakasuunnassa", new ImageIcon("tiedostot/kuvat/menu/gui/peilaa_vaaka.png"));
                peilausVaaka.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        m.peilaaKuva(PeilausValinta.PEILAA_VAAKA);
                    }
                });
                JMenuItem peilausPysty = new JMenuItem("Peilaa pystysuunnassa", new ImageIcon("tiedostot/kuvat/menu/gui/peilaa_pysty.png"));
                peilausPysty.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        m.peilaaKuva(PeilausValinta.PEILAA_PYSTY);
                    }
                });
                peilausValikko.add(peilausVaaka);
                peilausValikko.add(peilausPysty);
                menuItemit.add(peilausValikko);
            break;

        }
        return menuItemit;
    }

    static ArrayList<JMenuItem> luoOikeaClickOminaisuusLista(NPC n) {

        ArrayList<JMenuItem> menuItemit = new ArrayList<JMenuItem>();

        JMenuItem tiedot = new JMenuItem("Tiedot");
        tiedot.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, n.annaTiedot(), "NPC:n tiedot", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        tiedot.setText("Tiedot: " + n.annaNimi());
        menuItemit.add(tiedot);

        switch (n.annaNimi()) {

            default:
            break;

            case "Pikkuvihu", "Pahavihu":
                Vihollinen v = (Vihollinen)n;
                JMenu liikeTapa = new JMenu("Liiketapa");
                for (LiikeTapa lt : LiikeTapa.values()) {
                    JCheckBoxMenuItem liikeTapaValinta = new JCheckBoxMenuItem(lt.name());
                    liikeTapaValinta.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            v.liikeTapa = Vihollinen.LiikeTapa.valueOf(lt.name());
                            v.päivitäLisäOminaisuudet(lt);
                        }
                    });
                    if (v.liikeTapa == Vihollinen.LiikeTapa.valueOf(lt.name())) {
                        liikeTapaValinta.setSelected(true);
                    }
                    else {
                        liikeTapaValinta.setSelected(false);
                    }
                    liikeTapa.add(liikeTapaValinta);
                }
                menuItemit.add(liikeTapa);
            break;

        }
        return menuItemit;
    }
    
    static void luoAlkuIkkuna(int sijX, int sijY, Icon pelaajaKuvake) {

        objektiEditointiKenttäPaneli.removeAll();

        warpVasenNappi = new JButton[3];
        for (int i = 0; i < warpVasenNappi.length; i++){
            JButton jButton = new JButton(new KäännettäväKuvake(new ImageIcon("tiedostot/kuvat/osoitin.png"), 90, false));
            jButton.setBounds(0, 40, 40, Peli.kentänKoko*esineenKokoPx);
            jButton.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        if (huoneKartta.get(muokattavaHuone).annaReunaWarppiTiedot(Suunta.VASEN)) {
                            int uusiHuone = huoneKartta.get(muokattavaHuone).annaReunaWarpinKohdeId(Suunta.VASEN);
                            vaihdaHuonetta(muokattavaHuone, uusiHuone, true);
                            muokattavaHuone = uusiHuone;
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Tässä huoneessa ei ole warppia vasemmalle.\n\nAseta warppi klikkaamalla hiiren oikealla.", "Ei warppia", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                    else if (SwingUtilities.isRightMouseButton(e)) {
                        JPopupMenu ominaisuusMenu = new JPopupMenu();
                        for (JMenuItem mi : luoOikeaClickWarpMenu(Suunta.VASEN)) {
                            ominaisuusMenu.add(mi);
                        }
                        ominaisuusMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            });
            jButton.addKeyListener(editorinNäppäinkomennot);
            warpVasenNappi[i] = jButton;
        }

        warpOikeaNappi = new JButton[3];
        for (int i = 0; i < warpOikeaNappi.length; i++){
            JButton jButton = new JButton(new KäännettäväKuvake(new ImageIcon("tiedostot/kuvat/osoitin.png"), 270, false));
            jButton.setBounds(Peli.kentänKoko*esineenKokoPx + 40, 40, 40, Peli.kentänKoko*esineenKokoPx);
            jButton.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        if (huoneKartta.get(muokattavaHuone).annaReunaWarppiTiedot(Suunta.OIKEA)) {
                            int uusiHuone = huoneKartta.get(muokattavaHuone).annaReunaWarpinKohdeId(Suunta.OIKEA);
                            vaihdaHuonetta(muokattavaHuone, uusiHuone, true);
                            muokattavaHuone = uusiHuone;
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Tässä huoneessa ei ole warppia oikealle.\n\nAseta warppi klikkaamalla hiiren oikealla.", "Ei warppia", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                    else if (SwingUtilities.isRightMouseButton(e)) {
                        JPopupMenu ominaisuusMenu = new JPopupMenu();
                        for (JMenuItem mi : luoOikeaClickWarpMenu(Suunta.OIKEA)) {
                            ominaisuusMenu.add(mi);
                        }
                        ominaisuusMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            });
            jButton.addKeyListener(editorinNäppäinkomennot);
            warpOikeaNappi[i] = jButton;
        }

        warpYlösNappi = new JButton[3];
        for (int i = 0; i < warpYlösNappi.length; i++){
            JButton jButton = new JButton(new KäännettäväKuvake(new ImageIcon("tiedostot/kuvat/osoitin.png"), 180, false));
            jButton.setBounds(40, 0, Peli.kentänKoko*esineenKokoPx, 40);
            jButton.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        if (huoneKartta.get(muokattavaHuone).annaReunaWarppiTiedot(Suunta.YLÖS)) {
                            int uusiHuone = huoneKartta.get(muokattavaHuone).annaReunaWarpinKohdeId(Suunta.YLÖS);
                            vaihdaHuonetta(muokattavaHuone, uusiHuone, true);
                            muokattavaHuone = uusiHuone;
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Tässä huoneessa ei ole warppia ylös.\n\nAseta warppi klikkaamalla hiiren oikealla.", "Ei warppia", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                    else if (SwingUtilities.isRightMouseButton(e)) {
                        JPopupMenu ominaisuusMenu = new JPopupMenu();
                        for (JMenuItem mi : luoOikeaClickWarpMenu(Suunta.YLÖS)) {
                            ominaisuusMenu.add(mi);
                        }
                        ominaisuusMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            });
            jButton.addKeyListener(editorinNäppäinkomennot);
            warpYlösNappi[i] = jButton;
        }

        warpAlasNappi = new JButton[3];
        for (int i = 0; i < warpAlasNappi.length; i++){
            JButton jButton = new JButton(new KäännettäväKuvake(new ImageIcon("tiedostot/kuvat/osoitin.png"), 0, false));
            jButton.setBounds(40, Peli.kentänKoko*esineenKokoPx + 40, Peli.kentänKoko*esineenKokoPx, 40);
            jButton.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        if (huoneKartta.get(muokattavaHuone).annaReunaWarppiTiedot(Suunta.ALAS)) {
                            int uusiHuone = huoneKartta.get(muokattavaHuone).annaReunaWarpinKohdeId(Suunta.ALAS);
                            vaihdaHuonetta(muokattavaHuone, uusiHuone, true);
                            muokattavaHuone = uusiHuone;
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Tässä huoneessa ei ole warppia alas.\n\nAseta warppi klikkaamalla hiiren oikealla.", "Ei warppia", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                    else if (SwingUtilities.isRightMouseButton(e)) {
                        JPopupMenu ominaisuusMenu = new JPopupMenu();
                        for (JMenuItem mi : luoOikeaClickWarpMenu(Suunta.ALAS)) {
                            ominaisuusMenu.add(mi);
                        }
                        ominaisuusMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            });
            jButton.addKeyListener(editorinNäppäinkomennot);
            warpAlasNappi[i] = jButton;
        }
        
        int kohteenSijX = 40;
        int kohteensijY = 40;
        objektiKenttä = new KenttäKohde[Peli.kentänKoko][Peli.kentänKoko];
        maastoKenttä = new Maasto[Peli.kentänKoko][Peli.kentänKoko];
        npcKenttä = new NPC[Peli.kentänKoko][Peli.kentänKoko];
        
        for (int i = 0; i < Peli.kentänKoko; i++) {
            for (int j = 0; j < Peli.kentänKoko; j++) {
                int x = j;
                int y = i;
                kenttäKohteenKuvake[x][y] = new JButton(new ImageIcon());
                maastoKohteenKuvakeObjektiPanelissa[x][y] = new JLabel(new ImageIcon());
                maastoKohteenKuvakeNpcPanelissa[x][y] = new JLabel(new ImageIcon());
                kenttäKohteenKuvake[x][y].addKeyListener(editorinNäppäinkomennot);
                kenttäKohteenKuvake[x][y].addMouseListener(new MouseAdapter() {
                    public void mousePressed (MouseEvent e) {
                        try {
                            muutoksiaTehty = true;
                            if (ctrlPainettu && SwingUtilities.isLeftMouseButton(e)) {
                                if (objektiKenttä[x][y] != null) {
                                    kopioituOminaisuusLista = objektiKenttä[x][y].annalisäOminaisuudet();
                                    kopioituObjektinNimi = objektiKenttä[x][y].annaNimi();
                                }
                                else {
                                    kopioituObjektinNimi = "tyhjä";
                                }
                                käytäKopioitujaOminaisuuksia = true;
                            }
                            else if (SwingUtilities.isLeftMouseButton(e)) {
                                if (käytäKopioitujaOminaisuuksia) {
                                    if (objektiKenttä[x][y] != null) {
                                        tallennaMuutos("objekti_aseta_" + kopioituObjektinNimi + "_x=" + x + "_y=" + y + "+ominaisuudet:[" + objektiKenttä[x][y].annaLisäOminaisuudetYhtenäMjonona() + "]");
                                    }
                                    else {
                                        tallennaMuutos("objekti_aseta_" + "tyhjä" + "_x=" + x + "_y=" + y + "+ominaisuudet:[" + "]");
                                    }
                                    asetaEsineRuutuun(x, y, kopioituObjektinNimi, kopioituOminaisuusLista);
                                }
                                else {
                                    if (kenttäkohdeLista[esineValikko.getSelectedIndex()] == "Koriste-esine") {
                                        String[] koristeEsineenOminaisuusLista = new String[1];
                                        koristeEsineenOminaisuusLista[0] = "kuva=" + koristeEsineenKuvaValikko.getSelectedItem();
                                        if (objektiKenttä[x][y] != null) {
                                            tallennaMuutos("objekti_aseta_" + objektiKenttä[x][y].annaNimi() + "_x=" + x + "_y=" + y + "+ominaisuudet:[" + objektiKenttä[x][y].annaLisäOminaisuudetYhtenäMjonona() + "]");
                                            asetaEsineRuutuun(x, y, kenttäkohdeLista[esineValikko.getSelectedIndex()], koristeEsineenOminaisuusLista);
                                        }
                                        else {
                                            tallennaMuutos("objekti_aseta_" + "tyhjä" + "_x=" + x + "_y=" + y + "+ominaisuudet:[" + "]");
                                            asetaEsineRuutuun(x, y, kenttäkohdeLista[esineValikko.getSelectedIndex()], koristeEsineenOminaisuusLista);
                                        }
                                    }
                                    else if (objektiKenttä[x][y] != null) {
                                        tallennaMuutos("objekti_aseta_" + objektiKenttä[x][y].annaNimi() + "_x=" + x + "_y=" + y + "+ominaisuudet:[" + "]");
                                        asetaEsineRuutuun(x, y, kenttäkohdeLista[esineValikko.getSelectedIndex()], null);
                                    }
                                    else {
                                        tallennaMuutos("objekti_aseta_" + "tyhjä" + "_x=" + x + "_y=" + y + "+ominaisuudet:[" + "]");
                                        asetaEsineRuutuun(x, y, kenttäkohdeLista[esineValikko.getSelectedIndex()], null);
                                    }
                                    hudTeksti.setText(kenttäkohdeLista[esineValikko.getSelectedIndex()] + ", " + x + ", " + y);
                                }
                            }
                            else if (SwingUtilities.isRightMouseButton(e)) {
                                JPopupMenu ominaisuusMenu = new JPopupMenu();
                                for (JMenuItem mi : luoOikeaClickOminaisuusLista(objektiKenttä[x][y])) {
                                    ominaisuusMenu.add(mi);
                                }
                                ominaisuusMenu.show(e.getComponent(), e.getX(), e.getY());
                            }
                            else if (SwingUtilities.isMiddleMouseButton(e)) {
                                tallennaMuutos("objekti_poista_" + kenttäkohdeLista[esineValikko.getSelectedIndex()] + "_x=" + x + "_y=" + y + "+ominaisuudet:[" + objektiKenttä[x][y].annaLisäOminaisuudetYhtenäMjonona() + "]");
                                asetaEsineRuutuun(x, y, "", null);
                                hudTeksti.setText("tyhjä" + ", " + x + ", " + y);
                            }
                        }
                        catch (NullPointerException npe) {
                            System.out.println("Ei ominaisuuksia (tyhjä ruutu)");
                            npe.printStackTrace();
                        }
                    }
                });

                maastoKohteenKuvake[j][i] = new JButton();
                maastoKohteenKuvake[x][y].addKeyListener(editorinNäppäinkomennot);
                maastoKohteenKuvake[x][y].addMouseListener(new MouseAdapter() {
                    public void mousePressed (MouseEvent e) {
                        try {
                            muutoksiaTehty = true;
                            if (ctrlPainettu && SwingUtilities.isLeftMouseButton(e)) {
                                kopioituOminaisuusLista = maastoKenttä[x][y].annalisäOminaisuudet();
                                käytäKopioitujaOminaisuuksia = true;
                            }
                            else if (SwingUtilities.isLeftMouseButton(e)) {
                                String[] ominaisuusLista = new String[1];
                                if (maastoKenttä[x][y] == null) {
                                    tallennaMuutos("maasto_aseta_" + "Tile" + "_x=" + x + "_y=" + y + "+ominaisuudet:[" + "]");
                                }
                                else {
                                    tallennaMuutos("maasto_aseta_" + "Tile" + "_x=" + x + "_y=" + y + "+ominaisuudet:[" + maastoKenttä[x][y].annaLisäOminaisuudetYhtenäMjonona() + "]");
                                }
                                if (kopioituOminaisuusLista != null && käytäKopioitujaOminaisuuksia) {
                                    ominaisuusLista = kopioituOminaisuusLista;
                                }
                                else {
                                    ominaisuusLista = new String[1];
                                    ominaisuusLista[0] = "kuva=" + maastoValikko.getSelectedItem();
                                }
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
                                for (JMenuItem mi : luoOikeaClickOminaisuusLista(maastoKenttä[x][y])) {
                                    ominaisuusMenu.add(mi);
                                }
                                ominaisuusMenu.show(e.getComponent(), e.getX(), e.getY());
                            }
                            else if (SwingUtilities.isMiddleMouseButton(e)) {
                                String[] ominaisuusLista = new String[1];
                                ominaisuusLista[0] = "kuva=" + maastoValikko.getSelectedItem();
                                tallennaMuutos("maasto_poista_" + "Tile" + "_x=" + x + "_y=" + y + "+ominaisuudet:[" + maastoKenttä[x][y].annaLisäOminaisuudetYhtenäMjonona() + "]");
                                asetaMaastoRuutuun(x, y, "", ominaisuusLista);
                                hudTeksti.setText("tyhjä" + ", " + x + ", " + y);
                            }
                            else {
            
                            }
                        }
                        catch (NullPointerException npe) {
                            System.out.println("Ei ominaisuuksia (tyhjä ruutu)");
                            npe.printStackTrace();
                        }
                    }
                });

                npcKohteenKuvake[x][y] = new JButton(new ImageIcon());
                npcKohteenKuvake[x][y].addKeyListener(editorinNäppäinkomennot);
                npcKohteenKuvake[x][y].addMouseListener(new MouseAdapter() {
                    public void mousePressed (MouseEvent e) {
                        try {
                            muutoksiaTehty = true;
                            if (SwingUtilities.isLeftMouseButton(e)) {
                                String[] ominaisuusLista = new String[1];
                                ominaisuusLista[0] = "liiketapa=" + LiikeTapa.LOOP_NELIÖ_MYÖTÄPÄIVÄÄN;
                                asetaNPCRuutuun(x, y, npcNimiLista[npcValikko.getSelectedIndex()], ominaisuusLista);
                                tallennaMuutos("npc_aseta_" + npcNimiLista[npcValikko.getSelectedIndex()] + "_x=" + x + "_y=" + y + "+ominaisuudet:[" + npcKenttä[x][y].annaLisäOminaisuudetYhtenäMjonona() + "]");
                                hudTeksti.setText(npcNimiLista[npcValikko.getSelectedIndex()] + ", " + x + ", " + y);
                            }
                            else if (SwingUtilities.isRightMouseButton(e)) {
                                JPopupMenu ominaisuusMenu = new JPopupMenu();
                                for (JMenuItem mi : luoOikeaClickOminaisuusLista(npcKenttä[x][y])) {
                                    ominaisuusMenu.add(mi);
                                }
                                ominaisuusMenu.show(e.getComponent(), e.getX(), e.getY());
                            }
                            else if (SwingUtilities.isMiddleMouseButton(e)) {
                                String[] ominaisuusLista = new String[1];
                                ominaisuusLista[0] = "liiketapa=" + LiikeTapa.LOOP_NELIÖ_MYÖTÄPÄIVÄÄN;
                                tallennaMuutos("npc_poista_" + npcNimiLista[npcValikko.getSelectedIndex()] + "_x=" + x + "_y=" + y + "+ominaisuudet:[" + npcKenttä[x][y].annaLisäOminaisuudetYhtenäMjonona() + "]");
                                asetaNPCRuutuun(x, y, "", ominaisuusLista);
                                hudTeksti.setText("tyhjä" + ", " + x + ", " + y);
                            }
                            else {
            
                            }
                        }
                        catch (NullPointerException npe) {
                            System.out.println("Ei ominaisuuksia (tyhjä ruutu)");
                            npe.printStackTrace();
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
                            if (Peli.annaObjektiKenttä()[j][i] instanceof KenttäKohde) {
                                kenttäKohteenKuvake[j][i].setIcon(Peli.annaObjektiKenttä()[j][i].annaKuvake());
                            }
                            if (Peli.annaMaastoKenttä()[j][i] instanceof Maasto) {
                                maastoKohteenKuvake[j][i].setIcon(Peli.annaMaastoKenttä()[j][i].annaKuvake());
                            }
                        }
                        if (reunatNäkyvissä) {
                            if (Peli.annaObjektiKenttä()[j][i] instanceof KenttäKohde) {
                                if (Pelaaja.sijX == j && Pelaaja.sijY == i) {
                                    kenttäKohteenKuvake[j][i].setBorder(null);
                                }
                                else if (Peli.annaObjektiKenttä()[j][i] instanceof KenttäKohde) {
                                    kenttäKohteenKuvake[j][i].setIcon(Peli.annaObjektiKenttä()[j][i].annaKuvake());
                                    if (Peli.annaObjektiKenttä()[j][i] instanceof Kiintopiste) {
                                        kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(0,255,0), 1, true));
                                    }
                                    else if (Peli.annaObjektiKenttä()[j][i] instanceof Esine) {
                                        kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(0,0,255), 1, true));
                                    }
                                    else if (Peli.annaObjektiKenttä()[j][i] instanceof NPC_KenttäKohde) {
                                        kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(200,200,0), 1, true));
                                        
                                    }
                                    else if (Peli.annaObjektiKenttä()[j][i] instanceof Warp) {
                                        kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(80,200,0), 1, true));
                                    }
                                }
                            }
                            else if (Peli.annaObjektiKenttä()[j][i] == null) {
                                kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
                            }
                        }
                        else {
                            kenttäKohteenKuvake[j][i].setBorder(null);
                        }
                        if (Peli.annaObjektiKenttä()[j][i] instanceof KenttäKohde) {
                            kenttäKohteenKuvake[j][i].setIcon(Peli.annaObjektiKenttä()[j][i].annaKuvake());
                        }
                        if (Peli.annaMaastoKenttä()[j][i] instanceof Maasto) {
                            maastoKohteenKuvake[j][i].setIcon(Peli.annaMaastoKenttä()[j][i].annaKuvake());
                            maastoKohteenKuvakeObjektiPanelissa[j][i].setIcon(new LäpinäkyväKuvake(Peli.annaMaastoKenttä()[j][i].annaKuvanTiedostoNimi(),0.5f));
                            maastoKohteenKuvakeNpcPanelissa[j][i].setIcon(Peli.annaMaastoKenttä()[j][i].annaKuvake());
                        }
                    
                        kenttäKohteenKuvake[j][i].setBounds(kohteenSijX, kohteensijY, esineenKokoPx, esineenKokoPx);
                        kenttäKohteenKuvake[j][i].setOpaque(false);
                        kenttäKohteenKuvake[j][i].setContentAreaFilled(false);
                        kenttäKohteenKuvake[j][i].setBorderPainted(false);
                        maastoKohteenKuvake[j][i].setBounds(kohteenSijX, kohteensijY, esineenKokoPx, esineenKokoPx);
                        npcKohteenKuvake[j][i].setBounds(kohteenSijX, kohteensijY, esineenKokoPx, esineenKokoPx);
                        npcKohteenKuvake[j][i].setOpaque(false);
                        npcKohteenKuvake[j][i].setContentAreaFilled(false);
                        npcKohteenKuvake[j][i].setBorderPainted(false);
                        maastoKohteenKuvakeObjektiPanelissa[j][i].setBounds(kohteenSijX, kohteensijY, esineenKokoPx, esineenKokoPx);
                        maastoKohteenKuvakeNpcPanelissa[j][i].setBounds(kohteenSijX, kohteensijY, esineenKokoPx, esineenKokoPx);

                        objektiEditointiKenttäPaneli.add(kenttäKohteenKuvake[j][i]);
                        objektiEditointiKenttäPaneli.add(maastoKohteenKuvakeObjektiPanelissa[j][i]);
                        objektiEditointiKenttäPaneli.setComponentZOrder(kenttäKohteenKuvake[j][i], 1);
                        objektiEditointiKenttäPaneli.setComponentZOrder(maastoKohteenKuvakeObjektiPanelissa[j][i], 2);
                        maastoEditointiKenttäPaneli.add(maastoKohteenKuvake[j][i]);
                        maastoEditointiKenttäPaneli.setComponentZOrder(maastoKohteenKuvake[j][i], 1);
                        npcEditointiKenttäPaneli.add(npcKohteenKuvake[j][i]);
                        npcEditointiKenttäPaneli.add(maastoKohteenKuvakeNpcPanelissa[j][i]);
                        npcEditointiKenttäPaneli.setComponentZOrder(npcKohteenKuvake[j][i], 1);
                        npcEditointiKenttäPaneli.setComponentZOrder(maastoKohteenKuvakeNpcPanelissa[j][i], 2);
                        kohteenSijX += esineenKokoPx;
                    }
                }
                kohteenSijX = 40;
                kohteensijY += esineenKokoPx;
            }

            objektiEditointiKenttäPaneli.add(warpVasenNappi[0]);
            maastoEditointiKenttäPaneli.add(warpVasenNappi[1]);
            npcEditointiKenttäPaneli.add(warpVasenNappi[2]);
            objektiEditointiKenttäPaneli.add(warpOikeaNappi[0]);
            maastoEditointiKenttäPaneli.add(warpOikeaNappi[1]);
            npcEditointiKenttäPaneli.add(warpOikeaNappi[2]);
            objektiEditointiKenttäPaneli.add(warpYlösNappi[0]);
            maastoEditointiKenttäPaneli.add(warpYlösNappi[1]);
            npcEditointiKenttäPaneli.add(warpYlösNappi[2]);
            objektiEditointiKenttäPaneli.add(warpAlasNappi[0]);
            maastoEditointiKenttäPaneli.add(warpAlasNappi[1]);
            npcEditointiKenttäPaneli.add(warpAlasNappi[2]);
            
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

    static ArrayList<JMenuItem> luoOikeaClickWarpMenu(Suunta suunta) {

        ArrayList<JMenuItem> menuItemit = new ArrayList<JMenuItem>();

        JMenuItem asetaWarp = new JMenuItem("Valitse warpin kohde");
        asetaWarp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ReunaWarppiIkkuna.luoReunaWarppiIkkuna(suunta, false, 0);
            }
        });
        JMenuItem luoUusiHuone = new JMenuItem("Luo uusi huone");
        luoUusiHuone.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ikkuna.setFocusable(false);
                HuoneenLuontiIkkuna.luoHuoneenLuontiIkkuna(suunta);
            }
        });
        menuItemit.add(asetaWarp);
        menuItemit.add(luoUusiHuone);

        return menuItemit;
    }

    public static void päivitäEditoriIkkuna() {
        päivitäObjektiKenttä();
        päivitäMaastoKenttä();
        päivitäNPCKenttä();
        päivitäTooltipit();
    }

    static void päivitäTooltipit() {
        if (huoneKartta.get(muokattavaHuone) != null) {
            if (warpVasenNappi != null) {
                if (huoneKartta.get(muokattavaHuone).annaReunaWarppiTiedot(Suunta.VASEN)) {
                    for (int i = 0; i < warpVasenNappi.length; i++) {
                        if (warpVasenNappi[i] != null) {
                            warpVasenNappi[i].setToolTipText("lataa huone " + huoneKartta.get(muokattavaHuone).annaReunaWarpinKohdeId(Suunta.VASEN) + ", " + huoneKartta.get(huoneKartta.get(muokattavaHuone).annaReunaWarpinKohdeId(Suunta.VASEN)).annaNimi());
                        }
                    }
                }
                else {
                    for (int i = 0; i < warpVasenNappi.length; i++) {
                        if (warpVasenNappi[i] != null) {
                            warpVasenNappi[i].setToolTipText("ei warppia");
                        }
                    }
                }
            }
            if (warpOikeaNappi != null) {
                if (huoneKartta.get(muokattavaHuone).annaReunaWarppiTiedot(Suunta.OIKEA)) {
                    for (int i = 0; i < warpOikeaNappi.length; i++) {
                        if (warpOikeaNappi[i] != null) {
                            warpOikeaNappi[i].setToolTipText("lataa huone " + huoneKartta.get(muokattavaHuone).annaReunaWarpinKohdeId(Suunta.OIKEA) + ", " + huoneKartta.get(huoneKartta.get(muokattavaHuone).annaReunaWarpinKohdeId(Suunta.OIKEA)).annaNimi());
                        }
                    }
                }
                else {
                    for (int i = 0; i < warpOikeaNappi.length; i++) {
                        if (warpOikeaNappi[i] != null) {
                            warpOikeaNappi[i].setToolTipText("ei warppia");
                        }
                    }
                }
            }
            if (warpAlasNappi != null) {
                if (huoneKartta.get(muokattavaHuone).annaReunaWarppiTiedot(Suunta.ALAS)) {
                    for (int i = 0; i < warpAlasNappi.length; i++) {
                        if (warpAlasNappi[i] != null) {
                            warpAlasNappi[i].setToolTipText("lataa huone " + huoneKartta.get(muokattavaHuone).annaReunaWarpinKohdeId(Suunta.ALAS) + ", " + huoneKartta.get(huoneKartta.get(muokattavaHuone).annaReunaWarpinKohdeId(Suunta.ALAS)).annaNimi());
                        }
                    }
                }
                else {
                    for (int i = 0; i < warpAlasNappi.length; i++) {
                        if (warpAlasNappi[i] != null) {
                            warpAlasNappi[i].setToolTipText("ei warppia");
                        }
                    }
                }
            }
            if (warpYlösNappi != null) {
                if (huoneKartta.get(muokattavaHuone).annaReunaWarppiTiedot(Suunta.YLÖS)) {
                    for (int i = 0; i < warpYlösNappi.length; i++) {
                        if (warpYlösNappi[i] != null) {
                            warpYlösNappi[i].setToolTipText("lataa huone " + huoneKartta.get(muokattavaHuone).annaReunaWarpinKohdeId(Suunta.YLÖS) + ", " + huoneKartta.get(huoneKartta.get(muokattavaHuone).annaReunaWarpinKohdeId(Suunta.YLÖS)).annaNimi());
                        }
                    }
                }
                else {
                    for (int i = 0; i < warpYlösNappi.length; i++) {
                        if (warpYlösNappi[i] != null) {
                            warpYlösNappi[i].setToolTipText("ei warppia");
                        }
                    }
                }
            }
        }
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
                        if (objektiKenttä[j][i] instanceof KenttäKohde && kenttäKohteenKuvake[j][i] != null) {
                            kenttäKohteenKuvake[j][i].setIcon(objektiKenttä[j][i].annaKuvake());
                        }
                        else if (kenttäKohteenKuvake[j][i] != null) {
                            kenttäKohteenKuvake[j][i].setIcon(null);
                        }
                        if (maastoKenttä[j][i] instanceof Maasto && maastoKohteenKuvakeObjektiPanelissa[j][i] != null) {
                            maastoKohteenKuvakeObjektiPanelissa[j][i].setIcon(new LäpinäkyväKuvake("tiedostot/kuvat/maasto/" + maastoKenttä[j][i].annaKuvanTiedostoNimi(),0.5f));
                        }
                        else if (maastoKohteenKuvakeObjektiPanelissa[j][i] != null) {
                            maastoKohteenKuvakeObjektiPanelissa[j][i].setIcon(null);
                        }

                        if (maastoKenttä[j][i] instanceof Maasto && maastoKohteenKuvakeNpcPanelissa[j][i] != null) {
                            maastoKohteenKuvakeNpcPanelissa[j][i].setIcon(new LäpinäkyväKuvake("tiedostot/kuvat/maasto/" + maastoKenttä[j][i].annaKuvanTiedostoNimi(),0.5f));
                        }
                        else if (maastoKohteenKuvakeNpcPanelissa[j][i] != null) {
                            maastoKohteenKuvakeNpcPanelissa[j][i].setIcon(null);
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
                                    else if (objektiKenttä[j][i] instanceof NPC_KenttäKohde) {
                                        kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(200,200,0), 1, true));
                                        
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
                        if (maastoKenttä[j][i] instanceof Maasto && maastoKohteenKuvake[j][i] != null) {
                            maastoKohteenKuvake[j][i].setIcon(maastoKenttä[j][i].annaKuvake());
                            maastoKenttä[j][i].päivitäKuvanAsento();
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
                        if (npcKenttä[j][i] instanceof NPC && npcKohteenKuvake[j][i] != null) {
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

    static class EditorinNäppäinkomennot implements KeyListener {

        /*
         * Määritellään mitä eri näppäinkomennot tekee ja mitä metodeja kutsutaan
         */

        @Override
            public void keyTyped(KeyEvent e) {
                
            }
    
        @Override
            public void keyPressed(KeyEvent e) {

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_CONTROL:
                        ctrlPainettu = true;
                    break;
                    case KeyEvent.VK_Z:
                        if (ctrlPainettu) {
                            peruMuutos();
                        }
                    break;
                    default:
                        System.out.println("Näppäimellä "+ e.getKeyCode() + " ei ole toimintoa.");
                    break;
                }
                System.out.println(ctrlPainettu);
            }
    
        @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_CONTROL:
                        ctrlPainettu = false;
                    break;
                }
                System.out.println(ctrlPainettu);
            }
    }
}
