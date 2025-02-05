package keimo.HuoneEditori;

import keimo.*;
import keimo.HuoneEditori.DialogiEditori.DialogiEditoriIkkuna;
import keimo.HuoneEditori.JFXTiedostoIkkuna.TiedostoTyyppi;
import keimo.HuoneEditori.Keimo3D.Keimo3D;
import keimo.HuoneEditori.TarinaEditori.TarinaDialogiLista;
import keimo.HuoneEditori.TarinaEditori.TarinaEditoriIkkuna;
import keimo.HuoneEditori.TavoiteEditori.TavoiteEditoriIkkuna;
import keimo.HuoneEditori.muokkausIkkunat.KenttäNPCMuokkaus;
import keimo.HuoneEditori.muokkausIkkunat.PorttiMuokkaus;
import keimo.HuoneEditori.muokkausIkkunat.SpawnpointMuokkaus;
import keimo.HuoneEditori.muokkausIkkunat.SäiliöMuokkaus;
import keimo.HuoneEditori.muokkausIkkunat.VartijaMuokkaus;
import keimo.HuoneEditori.muokkausIkkunat.VisuaalinenObjektiMuokkaus;
import keimo.HuoneEditori.muokkausIkkunat.WarpMuokkaus;
import keimo.Maastot.*;
import keimo.Utility.KäännettäväKuvake.KääntöValinta;
import keimo.Utility.KäännettäväKuvake.PeilausValinta;
import keimo.entityt.*;
import keimo.entityt.Entity.SuuntaVasenOikea;
import keimo.entityt.npc.Vartija;
import keimo.entityt.npc.Vihollinen;
import keimo.entityt.npc.Vihollinen.LiikeTapa;
import keimo.keimoEngine.toiminnot.Dialogit;
import keimo.kenttäkohteet.*;
import keimo.kenttäkohteet.avattavaEste.AvattavaEste;
import keimo.kenttäkohteet.esine.Esine;
import keimo.kenttäkohteet.esine.Kaasusytytin;
import keimo.kenttäkohteet.kenttäNPC.NPC_KenttäKohde;
import keimo.kenttäkohteet.kiintopiste.Kiintopiste;
import keimo.kenttäkohteet.kiintopiste.Säiliö;
import keimo.kenttäkohteet.triggeri.Triggeri;
import keimo.kenttäkohteet.warp.Warp;
import keimo.Utility.*;
import keimo.Utility.Downloaded.SpringUtilities;
import keimo.Utility.Käännettävä.Suunta;
import keimo.Ikkunat.*;

import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.*;
import java.nio.file.*;
import static java.awt.GraphicsDevice.WindowTranslucency.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class HuoneEditoriIkkuna {

    static final int esineenKokoPx = 64;
    static final int pelaajanKokoPx = 64;
    static int ikkunanLeveys;
    static int ikkunanKorkeus;
    public static JFrame ikkuna;
    //static JPanel yläPaneeli, yläPaneelinYläosa, YläPaneelinAlaosa, yläVasenPaneeli, yläOikeaPaneeli;
    //static JButton hyväksyNappi;
    //static JLabel huoneenNimiTekstiKenttäLabel, huoneenAlueTekstiKenttäLabel, huoneenKuvaTekstiKenttäLabel, huoneenDialogiTekstiKenttäLabel;
    //static JTextField huoneenNimiTekstiKenttä, huoneenAlueTekstiKenttä, huoneenDialogiValintaTekstiKenttä;
    //static JButton huoneenNimiTekstiKenttäNappi, huoneenAlueTekstiKenttäNappi, huoneenKuvaTekstiKenttäNappi, huoneenDialogiTekstiKenttäNappi;
    static JButton huoneenKuvaValintaNappi;
    static boolean ctrlPainettu = false;
    static String[] kopioituOminaisuusLista;
    static String kopioituObjektinNimi;

    static JPanel työkaluPaneli, työkaluValintaPaneli;
    static JLabel työkaluOtsikko;
    static JButton maalausTyökalu, valintaTyökalu;
    static boolean maalausKäytössä = false;

    static JPanel huoneInfoPaneli, huoneenVaihtoPaneli, huoneenNimiPaneli;
    static JButton huoneenNimiLabel;
    static JButton huoneenVaihtoNappiVasen, huoneenVaihtoNappiOikea;
    static JButton huoneInfoLabel;
    static String[] kenttäkohdeLista;// = {"Avain", "Hiili", "Huume", "Juhani", "Jumal Velho", "Jumal Yoda", "Kaasupullo", "Kaasusytytin", "Kauppahylly", "Kauppaovi", "Kaupparuutu", "Kauppias", "Kilpi", "Kirstu", "Koriste-esine", "Kuparilager", "Makkara", "Nappi", "Nuotio", "Painelaatta (pahavihu)", "Painelaatta (pikkuvihu)", "Paperi", "Pesäpallomaila", "Pontikka-ainekset", "Portti", "Pulloautomaatti", "Puuovi", "Oviruutu", "Seteli", "Silta", "Suklaalevy", "Sänky", "Vesiämpäri", "Ämpärikone"};
    static String[] entityLista;// = {"Asevihu", "Pikkuvihu", "Pahavihu", "Pomo", "Vartija"};
    static Map<Object, Icon> esineValikkoKuvakkeet = new TreeMap<Object, Icon>();
    static Map<Object, Icon> maastoValikkoKuvakkeet = new TreeMap<Object, Icon>();
    static Map<Object, Icon> entityValikkoKuvakkeet = new TreeMap<Object, Icon>();
    public static String[] esineLista = {"Avain", "Hiili", "Huume", "Jallupullo", "Kaasupullo", "Kaasusytytin", "Kilpi", "Kuparilager", "Makkara", "Paperi", "Pesäpallomaila", "Pontikka-ainekset", "Seteli", "Suklaalevy", "Vesiämpäri"};
    static JPanel esineValikkoPaneli, maastoValikkoPaneli, entityValikkoPaneli;
    static JComboBox<Object> esineValikko;
    static JComboBox<Object> maastoValikko;
    static JComboBox<Object> entityValikko;
    static JComboBox<Object> koristeEsineenKuvaValikko;

    static JPanel editointiKenttäAlue;
    static JTabbedPane välilehdet;
    static ZoomPanel2 scrollattavaObjektiKenttäPaneli, scrollattavaMaastoKenttäPaneli, scrollattavaNPCKenttäPaneli;
    static JPanel objektiEditointiKenttäPaneliUlompi, maastoEditointiKenttäPaneliUlompi, npcEditointiKenttäPaneliUlompi;
    static JPanel objektiEditointiKenttäPaneli, maastoEditointiKenttäPaneli, npcEditointiKenttäPaneli;
    static JLabel hiirenSijaintiLabel;
    static JPanel alapalkki;

    static JPanel peliAluePaneli;
    
    public static HashMap<Integer, Huone> huoneKartta = new HashMap<Integer, Huone>();
    public static int muokattavaHuone = 0;
    static String huoneenNimi = "";
    static String huoneenAlue = "";
    static ImageIcon huoneenTaustakuva;
    static String huoneenTaustakuvaPolku = "";
    static String huoneenAlkuDialoginTunniste = "";
    static String huoneenVaaditunTavoitteenTunniste = "";
    static String huoneenMusa = "";
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
    static int huoneenKoko = 10;
    public static KenttäKohde[][] objektiKenttä;
    static Maasto[][] maastoKenttä;
    static Entity[][] npcKenttä;
    static JLabel pelaajaLabel, taustaLabel;
    static JPanel hud;
    static CardLayout crd;
    static JPanel kortit;
    static boolean reunatNäkyvissä = true;
    public static boolean vaatiiPäivityksen = true, vaatiiKentänPäivityksen = false;
    static boolean näytäTallennusVaroitus = true;
    static ArrayList<String> editorinMuutosHistoria = new ArrayList<String>();

    static boolean käytäKopioitujaOminaisuuksia = false;
    static EditorinNäppäinkomennot editorinNäppäinkomennot = new EditorinNäppäinkomennot();
    static boolean muutoksiaTehty = false;
    static int zoom = 64;

    public static File jfxAvattuTiedosto;
    public static String jfxKokoTiedostoMerkkijonona;

    static class IconListRenderer extends DefaultListCellRenderer {
        private static final long serialVersionUID = 1L;
        private Map<Object, Icon> icons; 

        public IconListRenderer(Map<Object, Icon> icons) { 
            this.icons = icons;
        } 

        @SuppressWarnings("rawtypes")
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index,boolean isSelected, boolean cellHasFocus) { 
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            Icon icon = icons.get(value);
            label.setIcon(icon); 
            return label; 
        } 
    }

    public static void käynnistäEditori() {
        try {
            EditorinLatausIkkuna.launch();
        }
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(ikkuna, "Editoria ei voitu ladata.", "Virhe editorin latauksessa", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Aseta kaikki editori-ikkunan GUI-elementit paikoilleen
     */
    protected static void luoEditoriIkkuna() {
        
        ikkunanLeveys = 750;
        ikkunanKorkeus = 870;
        kenttäKohteenKuvake = new JButton[Peli.kentänKoko][Peli.kentänKoko];
        maastoKohteenKuvake = new JButton[Peli.kentänKoko][Peli.kentänKoko];
        npcKohteenKuvake = new JButton[Peli.kentänKoko][Peli.kentänKoko];

        maastoKohteenKuvakeObjektiPanelissa = new JLabel[Peli.kentänKoko][Peli.kentänKoko];
        maastoKohteenKuvakeNpcPanelissa = new JLabel[Peli.kentänKoko][Peli.kentänKoko];
        
        ikkuna = new JFrame("Huone-editori v0.8");
        ikkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja_og.png").getImage());
        ikkuna.setBounds(600, 100, ikkunanLeveys, ikkunanKorkeus);
        ikkuna.setLayout(new BorderLayout());
        ikkuna.setBackground(Color.black);
        ikkuna.setLocationRelativeTo(PääIkkuna.ikkuna);
        if (!Peli.legacy) ikkuna.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ikkuna.setVisible(false);
        ikkuna.revalidate();
        ikkuna.repaint();

        JMenuItem uusi = new JMenuItem("Uusi", new ImageIcon("tiedostot/kuvat/menu/gui/uusi_tiedosto.png"));
        uusi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int tyhjennysValinta = CustomViestiIkkunat.EditorinTyhjennys.näytäDialogi("Haluatko varmasti tyhjentää kaikki huoneet?", "Uusi pohja");
                if (tyhjennysValinta == JOptionPane.YES_OPTION) {
                    for (int i = 0; i < Peli.kentänKoko; i++) {
                        for (int j = 0; j < Peli.kentänKoko; j++) {
                            objektiKenttä[j][i] = null;
                            maastoKenttä[j][i] = null;
                            npcKenttä[j][i] = null;
                        }
                    }
                    if (huoneKartta != null) {
                        huoneKartta.clear();
                        huoneKartta.put(0, new Huone(0, 10, null, "Uusi huone 0", "", null, null, null, null, "", ""));
                        Peli.huoneKartta = huoneKartta;
                    }
                    else {
                        huoneKartta = new HashMap<Integer, Huone>();
                        huoneKartta.put(0, new Huone(0, 10, null, "Uusi huone 0", "", null, null, null, null, "", ""));
                        Peli.huoneKartta = huoneKartta;
                    }
                    huoneenNimi = "";
                    huoneenAlue = "";
                    huoneenNimiLabel.setText(huoneenNimi + " (" + huoneenAlue + ")");
                }
            }
        });

        JMenuItem avaa = new JMenuItem("Avaa", new ImageIcon("tiedostot/kuvat/menu/gui/kansio.png"));
        avaa.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int korvaaMuutokset = JOptionPane.OK_OPTION;
                if (muutoksiaTehty) {
                    korvaaMuutokset = CustomViestiIkkunat.MuutoksetEditorissa.näytäDialogi();
                }
                if (korvaaMuutokset != JOptionPane.NO_OPTION) {
                    try {
                        JFXTiedostoIkkuna.launchAvaaTiedosto(TiedostoTyyppi.KOKO_TIEDOSTO);
                    }
                    catch (Exception ex) {
                        System.out.println("Ei voitu ladata JavaFX:n kirjastoja. Käytetään Swingin tiedostovalintaikkunaa.");
                        ex.printStackTrace();
                        korvaaMuutokset = JOptionPane.OK_OPTION;
                        if (muutoksiaTehty) {
                            korvaaMuutokset = CustomViestiIkkunat.MuutoksetEditorissa.näytäDialogi();
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
                                    String[] tarinaDialogitMerkkijonoina;
                                    int tarinaDialogienMääräTiedostossa = 0;
                                    Path path = FileSystems.getDefault().getPath(tiedosto.getPath());
                                    Charset charset = Charset.forName("UTF-8");
                                    BufferedReader read = Files.newBufferedReader(path, charset);
                                    String tarkastettavaRivi = null;
                                    if ((tarkastettavaRivi = read.readLine()) != null) {
                                        if (!tarkastettavaRivi.startsWith("<KEIMO>")) {
                                            System.out.println(tarkastettavaRivi);
                                            throw new FileNotFoundException();
                                        }
                                    }
                                    while ((tarkastettavaRivi = read.readLine()) != null) {
                                        if (tarkastettavaRivi.startsWith("Huone ")) {
                                            huoneidenMääräTiedostossa++;
                                        }
                                        else if (tarkastettavaRivi.startsWith("Tarina ")) {
                                            tarinaDialogienMääräTiedostossa++;
                                        }
                                    }
                                    huoneetMerkkijonoina = new String[huoneidenMääräTiedostossa];
                                    huoneidenMääräTiedostossa = 0;
                                    tarinaDialogitMerkkijonoina = new String[tarinaDialogienMääräTiedostossa];
                                    tarinaDialogienMääräTiedostossa = 0;
                                    read.close();
                                    read = Files.newBufferedReader(path, charset);
                                    tarkastettavaRivi = read.readLine();
                                    while ((tarkastettavaRivi != null)) {
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
                                        else if (tarkastettavaRivi.startsWith("Tarina ")) {
                                            tarinaDialogienMääräTiedostossa++;
                                            tarinaDialogitMerkkijonoina[tarinaDialogienMääräTiedostossa-1] = "";
                                            while (tarkastettavaRivi != null) {
                                                tarinaDialogitMerkkijonoina[tarinaDialogienMääräTiedostossa-1] += tarkastettavaRivi + "\n";
                                                if (tarkastettavaRivi.startsWith("/Tarina")) {
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
                                    TarinaDialogiLista.tarinaKartta = HuoneEditorinMetodit.luoTarinaKarttaMerkkijonosta(tarinaDialogitMerkkijonoina);
                                    muokattavaHuone = 0;
                                    lataaHuoneKartasta(muokattavaHuone, false);
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
                                    read.close();
                                }
                            }
                            catch (FileNotFoundException fnfe) {
                                JOptionPane.showMessageDialog(null, "Tiedosto puuttuu tai on virheellinen.\n\nKeimon seikkailupeliin tarkoitetut tiedostot alkavat prefiksillä <KEIMO>.\n\nTämä virhe voi tulla, jos tiedostoa on muokattu muuten kuin pelinsisäisellä editorilla.", "Tiedostovirhe", JOptionPane.ERROR_MESSAGE);
                            }
                            catch (IOException ioe) {
            
                            }
                        }
                    }
                }
            }
        });
        
        JMenuItem tallenna = new JMenuItem("Tallenna", new ImageIcon("tiedostot/kuvat/menu/gui/korppu.png"));
        tallenna.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jfxKokoTiedostoMerkkijonona = HuoneEditorinMetodit.luoMerkkijonotHuonekartasta(huoneKartta, TarinaDialogiLista.tarinaKartta, Dialogit.PitkätDialogit.vuoropuheDialogiKartta);
                try {
                    JFXTiedostoIkkuna.launchTallennaTiedosto(TiedostoTyyppi.KOKO_TIEDOSTO);
                }
                catch (Exception ex) {
                    tallennaTiedostoonVanha(jfxKokoTiedostoMerkkijonona);
                }
            }
        });

        JMenuItem tuoHuone = new JMenuItem("Tuo huone", new ImageIcon("tiedostot/kuvat/menu/gui/tuo_huone.png"));
        tuoHuone.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jfxKokoTiedostoMerkkijonona = HuoneEditorinMetodit.luoMerkkijonotHuonekartasta(huoneKartta, TarinaDialogiLista.tarinaKartta, Dialogit.PitkätDialogit.vuoropuheDialogiKartta);
                try {
                    JFXTiedostoIkkuna.launchAvaaTiedosto(TiedostoTyyppi.VAIN_HUONE);
                }
                catch (Exception ex) {
                    tallennaTiedostoonVanha(jfxKokoTiedostoMerkkijonona);
                }
            }
        });

        JMenuItem vieHuone = new JMenuItem("Vie huone", new ImageIcon("tiedostot/kuvat/menu/gui/vie_huone.png"));
        vieHuone.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jfxKokoTiedostoMerkkijonona = HuoneEditorinMetodit.luoMerkkijonoHuoneesta(huoneKartta, HuoneEditoriIkkuna.muokattavaHuone);
                try {
                    JFXTiedostoIkkuna.launchTallennaTiedosto(TiedostoTyyppi.VAIN_HUONE);
                }
                catch (Exception ex) {
                    tallennaTiedostoonVanha(jfxKokoTiedostoMerkkijonona);
                }
            }
        });

        JMenu huone = new JMenu("Huone");
        huone.setIcon(new ImageIcon("tiedostot/kuvat/menu/gui/kartta.png"));
        huone.add(tuoHuone);
        huone.add(vieHuone);

        JMenu tiedosto = new JMenu("Tiedosto");
        tiedosto.add(uusi);
        tiedosto.add(avaa);
        tiedosto.add(tallenna);
        tiedosto.add(new JSeparator());
        tiedosto.add(huone);

        JMenuItem muokkaaSpawnia = new JMenuItem("Muokkaa pelaajan spawn-pistettä", new ImageIcon("tiedostot/kuvat/menu/gui/spawnpoint_työkalu.png"));
        muokkaaSpawnia.addActionListener(e -> SpawnpointMuokkaus.luoSpawnpointMuokkausIkkuna());

        JMenuItem kokeilePelissä = new JMenuItem("Kokeile pelissä", new ImageIcon("tiedostot/kuvat/menu/gui/uusipeli.png"));
        kokeilePelissä.addActionListener(e -> {
            if (Peli.legacy) {
                if (näytäTallennusVaroitus) {
                    int valinta = CustomViestiIkkunat.TiedostonTallennusVaroitus.näytäDialogi();
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
                            //UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
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
                        //UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    }
                    catch (Exception ex) {

                    }
                }
            }
            else JOptionPane.showMessageDialog(ikkuna, "Editori on käynnistetty uuden pelimoottorin kautta.\n\n Editorin integraatiota uuteen pelimoottoriin ei ole vielä toteutettu.\nJos haluat kokeilla kenttää pelissä, tallenna se default.kst-tiedostoon, sulje editori ja käynnistä peli uudelleen.", "Integraatio toteuttamatta", JOptionPane.INFORMATION_MESSAGE);
        });

        JMenuItem keimo3D = new JMenuItem("Keimo3D", new ImageIcon("tiedostot/kuvat/menu/gui/keimo3d.png"));
        keimo3D.addActionListener(e -> Keimo3D.käynnistäKeimo3D());

        JMenu peli = new JMenu("Peli");
        peli.add(muokkaaSpawnia);
        peli.add(kokeilePelissä);
        peli.add(keimo3D);

        JMenuItem ohjeet = new JMenuItem("Ohjeet", new ImageIcon("tiedostot/kuvat/menu/gui/ohjeet.png"));
        ohjeet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String ohjeTeksti = "";
                ohjeTeksti += "Hiiren vasen: aseta\n";
                ohjeTeksti += "Hiiren oikea: lisävalinnat\n";
                ohjeTeksti += "Hiiren keski: poista\n";
                ohjeTeksti += "\n";
                ohjeTeksti += "CTRL + hiiren vasen: kopioi tile\n";
                ohjeTeksti += "CTRL + Z: peru viimeisin muutos\n";
                JOptionPane.showMessageDialog(null, ohjeTeksti, "Editorin ohjeet", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JMenu tietoja = new JMenu("Tietoja");
        tietoja.add(ohjeet);

        JMenuItem dialogiEditori = new JMenuItem("Dialogieditori", new ImageIcon("tiedostot/kuvat/menu/gui/puhedialogi.png"));
        dialogiEditori.addActionListener(e ->DialogiEditoriIkkuna.luoDialogiEditoriIkkuna());

        JMenuItem tarinaEditori = new JMenuItem("Tarinaeditori", new ImageIcon("tiedostot/kuvat/menu/gui/dialogieditori_vaihtoehdonkohde.png"));
        tarinaEditori.addActionListener(e -> TarinaEditoriIkkuna.luoTarinaEditoriIkkuna());

        JMenuItem tavoiteEditori = new JMenuItem("Tavoite-editori", new ImageIcon("tiedostot/kuvat/menu/gui/tavoite-editori.png"));
        tavoiteEditori.addActionListener(e -> TavoiteEditoriIkkuna.luoTavoiteEditoriIkkuna());

        JMenu lisäosat = new JMenu("Lisäosat");
        lisäosat.add(dialogiEditori);
        lisäosat.add(tarinaEditori);
        lisäosat.add(tavoiteEditori);

        JMenuBar yläPalkki = new JMenuBar();
        yläPalkki.setPreferredSize(new Dimension(ikkunanLeveys -20,20));
        yläPalkki.add(tiedosto);
        yläPalkki.add(peli);
        yläPalkki.add(tietoja);
        yläPalkki.add(lisäosat);

        huoneenNimiLabel = new JButton("Huone (alue)");
        huoneenNimiLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
        huoneenNimiLabel.setToolTipText("Klikkaa muokataksesi huoneen metatietoja");
        huoneenNimiLabel.setBorderPainted(false);
        huoneenNimiLabel.setContentAreaFilled(false);
        huoneenNimiLabel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    HuoneenMetatietoIkkuna.luoIkkuna();
                }
                else if (SwingUtilities.isRightMouseButton(e)) {
                    JPopupMenu huoneInfoMenu = new JPopupMenu();
                    JMenuItem avaaHuoneenMetatietoIkkuna = new JMenuItem("Muokkaa huoneen metatietoja");
                    avaaHuoneenMetatietoIkkuna.addActionListener(event -> HuoneenMetatietoIkkuna.luoIkkuna());
                    huoneInfoMenu.add(avaaHuoneenMetatietoIkkuna);
                    huoneInfoMenu.show(e.getComponent(), e.getX(), e.getY());
                    System.out.println("muokkausmenu");
                }
            }
        });
        huoneenNimiPaneli = new JPanel(new GridBagLayout());
        huoneenNimiPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        huoneenNimiPaneli.setPreferredSize(new Dimension(380, 60));
        huoneenNimiPaneli.add(huoneenNimiLabel);

        huoneInfoLabel = new JButton("Huone");
        huoneInfoLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
        huoneInfoLabel.setHorizontalAlignment(JLabel.CENTER);
        huoneInfoLabel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    HuoneenVaihtoIkkuna.luoHuoneenVaihtoikkuna();
                }
                else if (SwingUtilities.isRightMouseButton(e)) {
                    JPopupMenu huoneenLuontiPopupMenu = new JPopupMenu();
                    JMenuItem luoHuone = new JMenuItem("Luo uusi huone");
                    luoHuone.addActionListener(listener -> {
                        HuoneenLuontiIkkuna.luoHuoneenLuontiIkkuna(null);
                    });
                    huoneenLuontiPopupMenu.add(luoHuone);
                    huoneenLuontiPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        huoneenVaihtoNappiVasen = new JButton(new KäännettäväKuvake(new ImageIcon("tiedostot/kuvat/osoitin_pieni.png"), 90, false));
        huoneenVaihtoNappiVasen.setPreferredSize(new Dimension(20, 60));
        huoneenVaihtoNappiVasen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                synchronized (HuoneenLatausSäie.editorinHuoneenLatausLukko) {
                    if (muokattavaHuone <= 0) {
                        
                    }
                    else {
                        if (!HuoneenLatausSäie.käynnissä) {
                            int uusiHuone = muokattavaHuone -1;
                            vaihdaHuonetta(muokattavaHuone, uusiHuone, true);
                            muokattavaHuone--;
                        }
                        else {
                            System.out.println("lataus kesken");
                        }
                    }
                }
            }
        });
        huoneenVaihtoNappiOikea = new JButton(new KäännettäväKuvake(new ImageIcon("tiedostot/kuvat/osoitin_pieni.png"), 270, false));
        huoneenVaihtoNappiOikea.setPreferredSize(new Dimension(20, 60));
        huoneenVaihtoNappiOikea.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                synchronized (HuoneenLatausSäie.editorinHuoneenLatausLukko) {
                    //System.out.println("EDT ottaa lukon");
                    if (!HuoneenLatausSäie.käynnissä) {
                        if (huoneKartta.get(muokattavaHuone +1) != null) {
                            int uusiHuone = muokattavaHuone +1;
                            vaihdaHuonetta(muokattavaHuone, uusiHuone, true);
                            muokattavaHuone++;
                        }
                        else {
                            int luoUusiHuoneValinta = CustomViestiIkkunat.HuonettaEiLöytynytHuomautus.näytäDialogi("Huonetta " + (muokattavaHuone +1) + " ei löytynyt. Haluatko luoda uuden huoneen?");
                            if (luoUusiHuoneValinta == JOptionPane.YES_OPTION) {
                                HuoneenLuontiIkkuna.luoHuoneenLuontiIkkuna(null);
                                // int uusiHuone = muokattavaHuone +1;
                                // vaihdaHuonetta(muokattavaHuone, uusiHuone, true);
                                // muokattavaHuone++;
                            }
                        }
                    }
                    else {
                        System.out.println("lataus kesken");
                    }
                }
                //System.out.println("EDT vapauttaa lukon");
            }
        });

        huoneenVaihtoPaneli = new JPanel(new SpringLayout());
        huoneenVaihtoPaneli.add(huoneenVaihtoNappiVasen);
        huoneenVaihtoPaneli.add(huoneInfoLabel);
        huoneenVaihtoPaneli.add(huoneenVaihtoNappiOikea);
        huoneenVaihtoPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
        huoneenVaihtoPaneli.setPreferredSize(new Dimension(180, 60));
        SpringUtilities.makeCompactGrid(huoneenVaihtoPaneli, 1, 3, 0, 0, 6, 2);

        //JPanel esineValikkoPaneli = new JPanel(new BorderLayout());
        //esineValikkoPaneli.setPreferredSize(new Dimension(180, 60));
        //esineValikkoPaneli.add(esineValikko, BorderLayout.CENTER);
        //esineValikkoPaneli.add(koristeEsineenKuvaValikko, BorderLayout.SOUTH);

        työkaluOtsikko = new JLabel("Työkalut");
        työkaluOtsikko.setHorizontalAlignment(JLabel.CENTER);
        maalausTyökalu = new JButton(new ImageIcon("tiedostot/kuvat/menu/gui/maalaustyökalu.png"));
        maalausTyökalu.setPreferredSize(new Dimension(32, 32));
        maalausTyökalu.setToolTipText("Maalaustyökalu: pidä hiiren vasenta pohjassa maalataksesi tilejä");
        maalausTyökalu.addActionListener(e -> {
            maalausKäytössä = !maalausKäytössä;
            maalausTyökalu.setIcon(maalausKäytössä ? new ImageIcon("tiedostot/kuvat/menu/gui/maalaustyökalu_valittu.png") : new ImageIcon("tiedostot/kuvat/menu/gui/maalaustyökalu.png"));
            maalausTyökalu.setSelected(maalausKäytössä);
        });
        valintaTyökalu = new JButton(new ImageIcon("tiedostot/kuvat/menu/gui/valintatyökalu.png"));
        valintaTyökalu.setPreferredSize(new Dimension(32, 32));
        valintaTyökalu.setToolTipText("Valintatyökalu");
        työkaluValintaPaneli = new JPanel(new FlowLayout(FlowLayout.LEFT));
        työkaluValintaPaneli.add(maalausTyökalu);
        //työkaluValintaPaneli.add(valintaTyökalu);

        työkaluPaneli = new JPanel(new BorderLayout());
        työkaluPaneli.setPreferredSize(new Dimension(180, 60));
        työkaluPaneli.add(työkaluOtsikko, BorderLayout.NORTH);
        työkaluPaneli.add(työkaluValintaPaneli, BorderLayout.CENTER);

        huoneInfoPaneli = new JPanel();
        huoneInfoPaneli.setLayout(new BorderLayout());
        huoneInfoPaneli.setPreferredSize(new Dimension(300, 60));
        huoneInfoPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        huoneInfoPaneli.add(työkaluPaneli, BorderLayout.WEST);
        huoneInfoPaneli.add(huoneenNimiPaneli, BorderLayout.CENTER);
        huoneInfoPaneli.add(huoneenVaihtoPaneli, BorderLayout.EAST);

        
        esineValikkoKuvakkeet.put("Avain", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/avain.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Baariovi", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/baariovi.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Baariruutu", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/baariruutu.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Hiili", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/hiili.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Huume", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/huume.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Jallupullo", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/jallupullo.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Juhani", KäännettäväKuvake.luoSkaalattuGif(new ImageIcon("tiedostot/kuvat/kenttäkohteet/juhani.gif"), 32));
        esineValikkoKuvakkeet.put("Jumal Velho", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/velho.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Jumal Yoda", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/yoda.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Kaasupullo", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/kaasupullo.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Kaasusytytin", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/tyhjäkaasusytytin.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Kalja-automaatti", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/kalja-automaatti.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Kartta", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/kartta.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Kauppahylly", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/kauppahylly.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Kauppaovi", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/kauppaovi.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Kaupparuutu", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/kaupparuutu.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Kauppias", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/kauppias.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Kilpi", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/kilpi.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Kirstu", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/kirstu.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Kolikko", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/kolikko.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Koriste-esine", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/koriste-esine_eikuvaa.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Kuparilager", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/kuparilager.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Makkara", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/makkarat.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Nappi", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/nappi.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Nuotio", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/nuotio.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Olutlasi", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/olutlasi.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Painelaatta (pahavihu)", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/painelaatta_pahavihu.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Painelaatta (pikkuvihu)", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/painelaatta_pikkuvihu.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Paperi", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/paperi.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Pasi", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/pasi.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Pelikone", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/pelikone.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Penkki", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/puistonpenkki.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Pesäpallomaila", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/pesäpallomaila.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Pontikka-ainekset", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/ponuainekset.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Portti", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/portti.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Pulloautomaatti", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/pullonpalautus_idle.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Puuovi", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/puuovi.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Oviruutu", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/reunawarppi.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Seteli", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/seteli.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Sieni", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/sieni.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Silta", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/asfaltti_silta.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Suklaalevy", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/suklaalevy.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Sänky", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/sänky.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Tynnyri", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/tynnyri.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Vesiämpäri", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/vesiämpäri.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        esineValikkoKuvakkeet.put("Ämpärikone", new ImageIcon(new ImageIcon("tiedostot/kuvat/kenttäkohteet/ämpärikone.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));

        int kenttäkohdeListanKoko = esineValikkoKuvakkeet.keySet().size();
        TreeSet<Object> kenttäkohdeTreeSet = new TreeSet<>(esineValikkoKuvakkeet.keySet());
        kenttäkohdeLista = new String[kenttäkohdeListanKoko];
        for (int i = 0; i < kenttäkohdeListanKoko; i++) {
            kenttäkohdeLista[i] = "" + kenttäkohdeTreeSet.getFirst();
            kenttäkohdeTreeSet.removeFirst();
        }

        esineValikkoPaneli = new JPanel(new FlowLayout(FlowLayout.LEFT));
        esineValikkoPaneli.setBounds(0, 0, 600, 50);
        esineValikko = new JComboBox<Object>(new TreeSet<Object>(esineValikkoKuvakkeet.keySet()).toArray());
        esineValikko.setRenderer(new IconListRenderer(esineValikkoKuvakkeet));
        esineValikko.addKeyListener(editorinNäppäinkomennot);
        esineValikko.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                käytäKopioitujaOminaisuuksia = false;
                if (esineValikko.getSelectedItem() == "Koriste-esine") {
                    koristeEsineenKuvaValikko.setVisible(true);
                    koristeEsineenKuvaValikko.setToolTipText("Valitse koriste-esineen kuva. Kuvat, joiden nimi päättyy " + "\"_e\"" + " luovat esteen.");
                }
                else {
                    koristeEsineenKuvaValikko.setVisible(false);
                    //koristeEsineenKuvaValikko.setToolTipText("Käytössä vain koriste-esineille.");
                }
            }
        });
        koristeEsineenKuvaValikko = new JComboBox<Object>(listaaKuvat("tiedostot/kuvat/kenttäkohteet/visuaaliset_objektit").toArray());
        koristeEsineenKuvaValikko.setRenderer(new IconListRenderer(listaaKuvatKartaksi("tiedostot/kuvat/kenttäkohteet/visuaaliset_objektit", 32)));
        koristeEsineenKuvaValikko.addKeyListener(editorinNäppäinkomennot);
        koristeEsineenKuvaValikko.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                käytäKopioitujaOminaisuuksia = false;
            }
        });
        esineValikkoPaneli.add(esineValikko);
        esineValikkoPaneli.add(koristeEsineenKuvaValikko);

        maastoValikkoPaneli = new JPanel(new FlowLayout(FlowLayout.LEFT));
        maastoValikkoPaneli.setBounds(0, 0, 600, 50);
        maastoValikko = new JComboBox<Object>(listaaKuvat("tiedostot/kuvat/maasto").toArray());
        maastoValikko.setRenderer(new IconListRenderer(listaaKuvatKartaksi("tiedostot/kuvat/maasto", 32)));
        maastoValikko.addKeyListener(editorinNäppäinkomennot);
        maastoValikko.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                käytäKopioitujaOminaisuuksia = false;
            }
        });
        maastoValikkoPaneli.add(maastoValikko);

        entityValikkoKuvakkeet.put("Asevihu", new ImageIcon(new ImageIcon("tiedostot/kuvat/npc/asevihu.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        entityValikkoKuvakkeet.put("IsoLaatikko", new ImageIcon(new ImageIcon("tiedostot/kuvat/entity/iso_laatikko.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        entityValikkoKuvakkeet.put("Laatikko", new ImageIcon(new ImageIcon("tiedostot/kuvat/entity/työnnettävä_laatikko.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        entityValikkoKuvakkeet.put("Pahavihu", new ImageIcon(new ImageIcon("tiedostot/kuvat/npc/pahavihu.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        entityValikkoKuvakkeet.put("Pikkuvihu", new ImageIcon(new ImageIcon("tiedostot/kuvat/npc/pikkuvihu.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        entityValikkoKuvakkeet.put("Pomo", new ImageIcon(new ImageIcon("tiedostot/kuvat/npc/boss.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        entityValikkoKuvakkeet.put("TestiEntity", new ImageIcon(new ImageIcon("tiedostot/kuvat/entity/apu_pesukone.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        entityValikkoKuvakkeet.put("Vartija", new ImageIcon(new ImageIcon("tiedostot/kuvat/npc/vartija_off.png").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)));

        int entityListanKoko = entityValikkoKuvakkeet.keySet().size();
        TreeSet<Object> entityTreeSet = new TreeSet<>(entityValikkoKuvakkeet.keySet());
        entityLista = new String[entityListanKoko];
        for (int i = 0; i < entityListanKoko; i++) {
            entityLista[i] = "" + entityTreeSet.getFirst();
            entityTreeSet.removeFirst();
        }

        entityValikkoPaneli = new JPanel(new FlowLayout(FlowLayout.LEFT));
        entityValikkoPaneli.setBounds(0, 0, 600, 50);
        entityValikko = new JComboBox<Object>(new TreeSet<Object>(entityValikkoKuvakkeet.keySet()).toArray());
        entityValikko.setRenderer(new IconListRenderer(entityValikkoKuvakkeet));
        entityValikko.addKeyListener(editorinNäppäinkomennot);
        entityValikko.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                käytäKopioitujaOminaisuuksia = false;
            }
        });
        entityValikkoPaneli.add(entityValikko);



        objektiEditointiKenttäPaneli = new JPanel();
        objektiEditointiKenttäPaneli.setLayout(null);
        //objektiEditointiKenttäPaneli.setPreferredSize(new Dimension(ikkunanLeveys-30, ikkunanLeveys -30));
        objektiEditointiKenttäPaneli.setPreferredSize(new Dimension(640, 640));
        //objektiEditointiKenttäPaneli.setBounds(0, 0, 640, 640);
        objektiEditointiKenttäPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        objektiEditointiKenttäPaneli.revalidate();
        objektiEditointiKenttäPaneli.repaint();

        scrollattavaObjektiKenttäPaneli = new ZoomPanel2(objektiEditointiKenttäPaneli);
        scrollattavaObjektiKenttäPaneli.setBounds(40, 80, 640, 640);
        scrollattavaObjektiKenttäPaneli.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollattavaObjektiKenttäPaneli.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollattavaObjektiKenttäPaneli.getVerticalScrollBar().setUnitIncrement(0);
        scrollattavaObjektiKenttäPaneli.addMouseWheelListener(e -> {
            // double adjustedZoomFactor = e.getWheelRotation() < 0 ? ZOOM_MULTIPLIER : 1 / ZOOM_MULTIPLIER;
                    // double newZoomFactor = zoomFactor * adjustedZoomFactor;

                    // // Check if the new zoom factor is within the valid range
                    // if (newZoomFactor >= MIN_ZOOM && newZoomFactor <= MAX_ZOOM) {
                    //     zoomFactor = newZoomFactor;
                    //     zoomCenter = e.getPoint();

                    //     AffineTransform at = new AffineTransform();
                    //     at.translate(zoomCenter.getX(), zoomCenter.getY());
                    //     at.scale(adjustedZoomFactor, adjustedZoomFactor);
                    //     at.translate(-zoomCenter.getX(), -zoomCenter.getY());

                    //     currentTransform.preConcatenate(at);
                    //     updatePreferredSize(); // Add this line
                    //     repaint();
                    // }
                    // int zoomKerroin = e.getWheelRotation() < 0 ? 1 : -1;
                    // zoom += zoomKerroin;
                    // System.out.println("zoom: " + zoom);
                    // for (int y = 0; y < kenttäKohteenKuvake.length; y++) {
                    //     for (int x = 0; x < kenttäKohteenKuvake.length; x++) {
                    //         int uusiX = kenttäKohteenKuvake[x][y].getX() +x*zoomKerroin;
                    //         int uusiY = kenttäKohteenKuvake[x][y].getY() +y*zoomKerroin;
                    //         kenttäKohteenKuvake[x][y].setBounds(uusiX, uusiY, zoom, zoom);
                    //         //maastoKohteenKuvakeObjektiPanelissa[x][y].setBounds(uusiX, uusiY, zoom, zoom);
                    //         //maastoKohteenKuvake[x][y].setBounds(uusiX, uusiY, zoom, zoom);
                    //         //npcKohteenKuvake[x][y].setBounds(uusiX, uusiY, zoom, zoom);
                    //         //maastoKohteenKuvakeNpcPanelissa[x][y].setBounds(uusiX, uusiY, zoom, zoom);
                    //     }
                    // }
                    // objektiEditointiKenttäPaneli.revalidate();
                    // objektiEditointiKenttäPaneli.repaint();            
        });
        JPanel scrollattavanObjektiKentänReunaPaneli = new JPanel();
        scrollattavanObjektiKentänReunaPaneli.setPreferredSize(new Dimension(640, 640));
        //scrollattavanObjektiKentänReunaPaneli.setBounds(60, 10, 600, 600);
        //scrollattavanObjektiKentänReunaPaneli.add(scrollattavaObjektiKenttäPaneli);

        objektiEditointiKenttäPaneliUlompi = new JPanel(null);
        objektiEditointiKenttäPaneliUlompi.setPreferredSize(new Dimension(640, 640));
        objektiEditointiKenttäPaneliUlompi.add(esineValikkoPaneli);
        objektiEditointiKenttäPaneliUlompi.add(scrollattavaObjektiKenttäPaneli);

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

        scrollattavaMaastoKenttäPaneli = new ZoomPanel2(maastoEditointiKenttäPaneli);
        scrollattavaMaastoKenttäPaneli.setBounds(40, 40, 640, 640);
        scrollattavaMaastoKenttäPaneli.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollattavaMaastoKenttäPaneli.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollattavaMaastoKenttäPaneli.getVerticalScrollBar().setUnitIncrement(0);
        JPanel scrollattavanMaastoKentänReunaPaneli = new JPanel();
        scrollattavanMaastoKentänReunaPaneli.setPreferredSize(new Dimension(640, 640));

        maastoEditointiKenttäPaneliUlompi = new JPanel(null);
        maastoEditointiKenttäPaneliUlompi.setPreferredSize(new Dimension(640, 640));
        maastoEditointiKenttäPaneliUlompi.add(maastoValikkoPaneli);
        maastoEditointiKenttäPaneliUlompi.add(scrollattavaMaastoKenttäPaneli);

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

        scrollattavaNPCKenttäPaneli = new ZoomPanel2(npcEditointiKenttäPaneli);
        scrollattavaNPCKenttäPaneli.setBounds(40, 40, 640, 640);
        scrollattavaNPCKenttäPaneli.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollattavaNPCKenttäPaneli.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollattavaNPCKenttäPaneli.getVerticalScrollBar().setUnitIncrement(0);
        JPanel scrollattavanNPCKentänReunaPaneli = new JPanel();
        scrollattavanNPCKentänReunaPaneli.setPreferredSize(new Dimension(640, 640));

        npcEditointiKenttäPaneliUlompi = new JPanel(null);
        npcEditointiKenttäPaneliUlompi.setPreferredSize(new Dimension(640, 640));
        npcEditointiKenttäPaneliUlompi.add(entityValikkoPaneli);
        npcEditointiKenttäPaneliUlompi.add(scrollattavaNPCKenttäPaneli);

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
        välilehdet.add("Objektit", objektiEditointiKenttäPaneliUlompi);
        välilehdet.add("Maasto", maastoEditointiKenttäPaneliUlompi);
        välilehdet.add("Entityt", npcEditointiKenttäPaneliUlompi);
        välilehdet.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                // try {
                //     if (esineValikkoPaneli.isAncestorOf(esineValikko)) {
                //         esineValikkoPaneli.remove(esineValikko);
                //     }
                //     if (esineValikkoPaneli.isAncestorOf(maastoValikko)) {
                //         esineValikkoPaneli.remove(maastoValikko);
                //     }
                //     if (esineValikkoPaneli.isAncestorOf(npcValikko)) {
                //         esineValikkoPaneli.remove(npcValikko);
                //     }

                //     switch (välilehdet.getTitleAt(välilehdet.getSelectedIndex())) {
                        
                //         case "Objektit":
                //             esineValikkoPaneli.add(esineValikko, BorderLayout.WEST);
                //             if (esineValikko.getSelectedItem() == "Koriste-esine") {
                //                 koristeEsineenKuvaValikko.setEnabled(true);
                //             }
                //             break;
                //         case "Maasto":
                //             esineValikkoPaneli.add(maastoValikko, BorderLayout.WEST);
                //             koristeEsineenKuvaValikko.setEnabled(false);
                //             break;
                //         case "Entityt":
                //             esineValikkoPaneli.add(npcValikko, BorderLayout.WEST);
                //             koristeEsineenKuvaValikko.setEnabled(false);
                //             break;
                //         default: break;
                //     }
                //     huoneInfoPaneli.revalidate();
                //     huoneInfoPaneli.repaint();
                //     huoneInfoPaneli.repaint();
                //     huoneInfoPaneli.repaint();
                //     huoneInfoPaneli.repaint();
                //     huoneInfoPaneli.repaint();
                //     ikkuna.revalidate();
                //     ikkuna.repaint();
                // }
                // catch (NullPointerException npe) {
                //     npe.printStackTrace();
                // }
            }
        });

        hiirenSijaintiLabel = new JLabel("Hiiren sijainti");
        alapalkki = new JPanel(new FlowLayout(FlowLayout.LEFT));
        alapalkki.setPreferredSize(new Dimension(600, 30));
        alapalkki.add(hiirenSijaintiLabel);

        editointiKenttäAlue = new JPanel();
        editointiKenttäAlue.setLayout(new BorderLayout());
        editointiKenttäAlue.setPreferredSize(new Dimension(ikkunanLeveys-30, ikkunanLeveys -30));
        editointiKenttäAlue.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        editointiKenttäAlue.add(huoneInfoPaneli, BorderLayout.NORTH);
        editointiKenttäAlue.add(välilehdet, BorderLayout.CENTER);
        editointiKenttäAlue.add(alapalkki, BorderLayout.SOUTH);
        editointiKenttäAlue.revalidate();
        editointiKenttäAlue.repaint();


        peliAluePaneli = new JPanel();
        peliAluePaneli.setLayout(new BorderLayout());
        //peliAluePaneli.add(yläPaneeli, BorderLayout.NORTH);
        peliAluePaneli.add(editointiKenttäAlue, BorderLayout.CENTER);
        
        crd = new CardLayout();
        kortit = new JPanel(crd);
        kortit.add(peliAluePaneli);

        ikkuna.add(yläPalkki, BorderLayout.NORTH);
        ikkuna.add(kortit, BorderLayout.CENTER);
        ikkuna.addKeyListener(editorinNäppäinkomennot);
        ikkuna.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                skaalaaEditointiKenttä();
            }
        });
        ikkuna.requestFocus();
        ikkuna.revalidate();
        ikkuna.repaint();

        huoneKartta = Peli.huoneKartta;
        luoAlkuIkkuna(0, 0, null);
        lataaHuoneKartasta(muokattavaHuone, true);
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
        if (editoriAuki()) {
            ikkuna.requestFocus();
        }
    }

    /**
     * Lataa kuvatiedostot ja luo niistä lista.
     * Käytä tätä funktiota maastolle ja koriste-esineille.
     * @param dir tiedostopolku
     * @return kuvalista merkkijonona
     */
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

    public static Map<Object, Icon> listaaKuvatKartaksi(String dir, int kuvanKoko) {
        switch (kuvanKoko) {
            case 64:
                Map<Object, Icon> kuvaKartta = Stream.of(new File(dir).listFiles())
                    .filter(file -> !file.isDirectory() && (file.getName().endsWith(".png") || file.getName().endsWith(".jpg") || file.getName().endsWith(".gif")))
                    .collect(Collectors.toMap(File::getName, file -> new ImageIcon(file.getPath())));
            return kuvaKartta;
            default:
                kuvaKartta = Stream.of(new File(dir).listFiles())
                    .filter(file -> !file.isDirectory() && (file.getName().endsWith(".png") || file.getName().endsWith(".jpg") || file.getName().endsWith(".gif")))
                    .collect(Collectors.toMap(File::getName, file -> new ImageIcon(new ImageIcon(file.getPath()).getImage().getScaledInstance(kuvanKoko, kuvanKoko, Image.SCALE_DEFAULT))));
            return kuvaKartta;
        }
    }

    static Point hiirenSijainti;
    static boolean hiiriLiikutettiin;

    /**
     * Lataa huonelistasta (huonekartasta) valitulla ID:llä huone editoriin.
     * Tätä käytetään kun halutaan vaihtaa editorissa auki olevaa huonetta.
     * @param uusiHuone ladattavan huoneen ID
     */
    static void lataaHuoneKartasta(int uusiHuone, boolean ensimmäinenLataus) {
        try {
            if (!ensimmäinenLataus) {
                if (!HuoneenLatausSäie.käynnissä) {
                    HuoneenLatausSäie.uusiHuoneLatausSäie = uusiHuone;
                    //new Thread(huoneenLatausSäie).start();
                    HuoneenLatausSäie.launch();
                }
                else {
                    System.out.println("lataus kesken");
                }
            }
            else {
                //new Thread(huoneenLatausSäie).run();
                HuoneenLatausSäie.launch();
            }
        }
        catch (InvocationTargetException ite) {
            ite.printStackTrace();
        }
        catch (InterruptedException ie) {
            System.out.println("Lataaminen keskeytettiin.");
            ie.printStackTrace();
        }
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

        ArrayList<Entity> npcKenttäLista = new ArrayList<Entity>();
        for (Entity[] nn : npcKenttä) {
            for (Entity n : nn) {
                npcKenttäLista.add(n);
            }
        }
        if (huoneenAlkuDialoginTunniste == null || huoneenAlkuDialoginTunniste == "") {
            huoneenAlkuDialoginTunniste = null;
        }
        if (huoneenVaaditunTavoitteenTunniste == null || huoneenVaaditunTavoitteenTunniste == "") {
            huoneenVaaditunTavoitteenTunniste = null;
        }
        huoneKartta.put(nykyinenHuone, new Huone(nykyinenHuone, huoneenKoko, huoneenNimi, huoneenTaustakuvaPolku, huoneenAlue, objektiKenttäLista, maastoKenttäLista, npcKenttäLista, huoneenMusa, huoneenAlkuDialoginTunniste, huoneenVaaditunTavoitteenTunniste));
        if (tyhjennäHuone) {
            huoneKartta.get(nykyinenHuone).päivitäReunawarppienTiedot(warpVasen, warpVasenHuoneId, warpOikea, warpOikeaHuoneId, warpAlas, warpAlasHuoneId, warpYlös, warpYlösHuoneId);
            objektiKenttä = new KenttäKohde[Peli.kentänKoko][Peli.kentänKoko];
            maastoKenttä= new Maasto[Peli.kentänKoko][Peli.kentänKoko];
            npcKenttä = new Entity[Peli.kentänKoko][Peli.kentänKoko];
            for (int i = 0; i < Peli.kentänKoko; i++) {
                for (int j = 0; j < Peli.kentänKoko; j++) {
                    if (i < 10 && j < 10) {
                        objektiKenttä[j][i] = null;
                        maastoKenttä[j][i] = null;
                        npcKenttä[j][i] = null;
                    }
                }
            }
            kenttäKohteenKuvake = new JButton[Peli.kentänKoko][Peli.kentänKoko];
            maastoKohteenKuvakeObjektiPanelissa = new JLabel[Peli.kentänKoko][Peli.kentänKoko];
            maastoKohteenKuvakeNpcPanelissa = new JLabel[Peli.kentänKoko][Peli.kentänKoko];
            maastoKohteenKuvake = new JButton[Peli.kentänKoko][Peli.kentänKoko];
            npcKohteenKuvake = new JButton[Peli.kentänKoko][Peli.kentänKoko];
            huoneenNimi = "";
            huoneenAlue = "";
            huoneenAlkuDialoginTunniste = "";
            huoneenVaaditunTavoitteenTunniste = "";
            //huoneenNimiTekstiKenttä.setText(huoneenNimi);
            //huoneenAlueTekstiKenttä.setText(huoneenAlue);
            //huoneenDialogiValintaTekstiKenttä.setText(huoneenAlkuDialoginTunniste);
            huoneenNimiLabel.setText(huoneenNimi + " (" + huoneenAlue + ")");
        }
    }

    /**
     * Vaihda editorissa auki oleva huone.
     * Korvaa huonekartassa editorissa auki olevan huoneen ID:tä vastaava huone.
     * Tarkista onko seuraavan huoneen valitulla ID:llä huone jo olemassa.
     * Jos on, lataa se editoriin. Jos ei, avaa tyhjä huone editoriin.
     * @param nykyinenHuone tallenna huonekarttaan tämä ja tyhjennä editori
     * @param seuraavaHuone lataa editoriin tämä
     * @param tyhjennäHuone aseta false vain uutta huonetta luodessa (nollaa mm. reunawarppitiedot)
     */
    static void vaihdaHuonetta(int nykyinenHuone, int seuraavaHuone, boolean tyhjennäHuone) {
        if (huoneKartta != null) {
            if (huoneKartta.containsKey(seuraavaHuone)) {
                asetaUusiHuoneKarttaan(nykyinenHuone, tyhjennäHuone);
                lataaHuoneKartasta(seuraavaHuone, false);
            }
            else {
                asetaUusiHuoneKarttaan(nykyinenHuone, tyhjennäHuone);
            }
            editorinMuutosHistoria.clear();
        }
        else {
            JOptionPane.showMessageDialog(null, "Huonekartta on tyhjä. Kokeile luoda uusi huonekartta.", "Huonekartta null", JOptionPane.ERROR_MESSAGE);
        }
    }

    static void tallennaTiedostoonVanha(String kokoTiedostoMerkkijonona) {
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
                    int korvaaTiedosto = CustomViestiIkkunat.TiedostonKorvaus.näytäDialogi("Tiedosto " + tiedosto.getName() + " on jo olemassa. Haluatko korvata sen?", "Tiedosto on jo olemassa.");
                    if (korvaaTiedosto == JOptionPane.YES_OPTION) {
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
                    // peruttavanKohteenX = Integer.parseInt(peruttavaMuutos.substring(peruttavaMuutos.indexOf("x=")+2, peruttavaMuutos.indexOf("x=")+3));
                    // peruttavanKohteenY = Integer.parseInt(peruttavaMuutos.substring(peruttavaMuutos.indexOf("y=")+2, peruttavaMuutos.indexOf("y=")+3));

                    int xAlkuIndeksi = peruttavaMuutos.indexOf("x=") +2;
                    int xLoppuIndeksi = peruttavaMuutos.indexOf("_", xAlkuIndeksi);
                    peruttavanKohteenX = Integer.parseInt(peruttavaMuutos.substring(xAlkuIndeksi, xLoppuIndeksi));
                    int yAlkuIndeksi = peruttavaMuutos.indexOf("y=") +2;
                    int yLoppuIndeksi = peruttavaMuutos.indexOf("+", yAlkuIndeksi);
                    peruttavanKohteenY = Integer.parseInt(peruttavaMuutos.substring(yAlkuIndeksi, yLoppuIndeksi));
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

    
    static void klikkaaEsineRuutuun(int x, int y, MouseEvent e) {
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
            }
        }
        catch (NullPointerException npe) {
            System.out.println("Ei ominaisuuksia (tyhjä ruutu)");
            npe.printStackTrace();
        }
    }

    static void klikkaaMaastoRuutuun(int x, int y, MouseEvent e) {
        try {
            muutoksiaTehty = true;
            if (ctrlPainettu && SwingUtilities.isLeftMouseButton(e)) {
                kopioituOminaisuusLista = maastoKenttä[x][y].annaLisäOminaisuudet();
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
                else if (ominaisuusLista[0].endsWith("_y.png")) {
                    asetaMaastoRuutuun(x, y, "Yksisuuntainen Tile", ominaisuusLista);
                }
                else{
                    asetaMaastoRuutuun(x, y, "Tile", ominaisuusLista);
                }
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
            }
            else {

            }
        }
        catch (NullPointerException npe) {
            System.out.println("Ei ominaisuuksia (tyhjä ruutu)");
            npe.printStackTrace();
        }
    }

    static void klikkaaEntityRuutuun(int x, int y, MouseEvent e) {
        try {
            muutoksiaTehty = true;
            if (SwingUtilities.isLeftMouseButton(e)) {
                String[] ominaisuusLista = new String[1];
                ominaisuusLista[0] = "liiketapa=" + LiikeTapa.LOOP_NELIÖ_MYÖTÄPÄIVÄÄN;
                asetaNPCRuutuun(x, y, entityLista[entityValikko.getSelectedIndex()], ominaisuusLista);
                tallennaMuutos("npc_aseta_" + entityLista[entityValikko.getSelectedIndex()] + "_x=" + x + "_y=" + y + "+ominaisuudet:[" + npcKenttä[x][y].annaLisäOminaisuudetYhtenäMjonona() + "]");
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
                tallennaMuutos("npc_poista_" + entityLista[entityValikko.getSelectedIndex()] + "_x=" + x + "_y=" + y + "+ominaisuudet:[" + npcKenttä[x][y].annaLisäOminaisuudetYhtenäMjonona() + "]");
                asetaNPCRuutuun(x, y, "", ominaisuusLista);
            }
            else {

            }
        }
        catch (NullPointerException npe) {
            System.out.println("Ei ominaisuuksia (tyhjä ruutu)");
            npe.printStackTrace();
        }
    }

    /**
     * Tätä funktiota käytetään kun editorin napeista (ruudut) klikataan
     * @param sijX klikatun napin x-koordinaatti
     * @param sijY klikatun napin y-koordinaatti
     * @param esineenNimi esine, joka asetetaan valittuun ruutuun
     * @param ominaisuusLista joillakin objekteilla on lisäominaisuuksia, oletuksena tyhjä (null)
     */
    static void asetaEsineRuutuun(int sijX, int sijY, String esineenNimi, String[] ominaisuusLista) {
        objektiKenttä[sijX][sijY] = KenttäKohde.luoObjektiTiedoilla(esineenNimi, true, sijX, sijY, ominaisuusLista);
    }

    static void asetaMaastoRuutuun(int sijX, int sijY, String maastonNimi, String[] ominaisuusLista) {
        maastoKenttä[sijX][sijY] = Maasto.luoMaastoTiedoilla(maastonNimi, true, sijX, sijY, ominaisuusLista);
    }

    static void asetaNPCRuutuun(int sijX, int sijY, String npcnNimi, String[] ominaisuusLista) {
        npcKenttä[sijX][sijY] = Entity.luoEntityTiedoilla(npcnNimi, true, sijX, sijY, ominaisuusLista);
    }

    /**
     * Määritetään, mitä vaihtoehtoja tulee näkyviin, kun objektia klikkaa hiiren oikealla.
     * Jokaisella objektilla on tiedot-vaihtoehto.
     * @param k objekti, jolle vaihtoehdot tarkistetaan
     * @return lista valikkopainikkeita (JMenuItem)
     */
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

        if (k instanceof Esine) {
            Esine esine = (Esine)k;
            if (k instanceof Kaasusytytin) {
                Kaasusytytin ks = (Kaasusytytin)esine;
                JCheckBoxMenuItem valitseToimivuus = new JCheckBoxMenuItem("Toimiva");
                valitseToimivuus.setState(ks.toimiva);
                valitseToimivuus.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        ks.asetaToimivuus(valitseToimivuus.getState() ? "toimiva" : "tyhjä");
                    }
                });
                menuItemit.add(valitseToimivuus);
            }
        }
        else if (k instanceof Kiintopiste) {
            Kiintopiste kp = (Kiintopiste)k;
            JMenu kääntöValikko = new JMenu("Kierrä kuvaa");
            JMenuItem kääntöMyötäpäivään = new JMenuItem("Kierrä myötäpäivään", new ImageIcon("tiedostot/kuvat/menu/gui/kierrä_myötäpäivään.png"));
            kääntöMyötäpäivään.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    kp.käännäKuvaa(KääntöValinta.MYÖTÄPÄIVÄÄN);
                }
            });
            JMenuItem kääntöVastapäivään = new JMenuItem("Kierrä vastapäivään", new ImageIcon("tiedostot/kuvat/menu/gui/kierrä_vastapäivään.png"));
            kääntöVastapäivään.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    kp.käännäKuvaa(KääntöValinta.VASTAPÄIVÄÄN);
                }
            });
            kääntöValikko.add(kääntöMyötäpäivään);
            kääntöValikko.add(kääntöVastapäivään);
            menuItemit.add(kääntöValikko);
            
            JMenu peilausValikko = new JMenu("Peilaa kuva");
            JMenuItem peilausVaaka = new JMenuItem("Peilaa vaakasuunnassa", new ImageIcon("tiedostot/kuvat/menu/gui/peilaa_vaaka.png"));
            peilausVaaka.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    kp.peilaaKuva(PeilausValinta.PEILAA_VAAKA);
                }
            });
            JMenuItem peilausPysty = new JMenuItem("Peilaa pystysuunnassa", new ImageIcon("tiedostot/kuvat/menu/gui/peilaa_pysty.png"));
            peilausPysty.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    kp.peilaaKuva(PeilausValinta.PEILAA_PYSTY);
                }
            });
            peilausValikko.add(peilausVaaka);
            peilausValikko.add(peilausPysty);
            menuItemit.add(peilausValikko);
            if (kp instanceof Säiliö) {
                Säiliö säiliö = (Säiliö)k;
                JMenuItem säiliönSisältö = new JMenuItem("Sisältö: " + säiliö.annaSisältö());
                säiliönSisältö.addActionListener(e -> SäiliöMuokkaus.luoSäiliönMuokkausIkkuna(k.annaSijX(), k.annaSijY(), k));
                menuItemit.add(säiliönSisältö);
            }
        }
        else if (k instanceof Warp) {
            Warp r = (Warp)k;
            JMenuItem warppiKohde = new JMenuItem("Muokkaa warpin kohdetta");
            warppiKohde.addActionListener(e -> WarpMuokkaus.luoWarpMuokkausIkkuna(k.annaSijX(), k.annaSijY(), k));
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
        }
        else if (k instanceof NPC_KenttäKohde) {
            NPC_KenttäKohde kenttäNPC = (NPC_KenttäKohde)k;
            JMenuItem dialogi = new JMenuItem("Dialogi: " + kenttäNPC.annaDialogi());
            dialogi.addActionListener(e -> KenttäNPCMuokkaus.luoKenttäNPCMuokkausIkkuna(k.annaSijX(), k.annaSijY(), k));
            menuItemit.add(dialogi);
        }
        else if (k instanceof AvattavaEste) {
            JMenuItem valitseTriggerit = new JMenuItem("Valitse triggerit");
            valitseTriggerit.addActionListener(e -> PorttiMuokkaus.luoPorttiMuokkausIkkuna(k.annaSijX(), k.annaSijY(), k));
            menuItemit.add(valitseTriggerit);
        }
        else if (k instanceof Triggeri) {

        }
        else if (k instanceof VisuaalinenObjekti) {
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

            JMenuItem dialogi = new JMenuItem();
            if (ke.onkoKatsottava()) {
                dialogi = new JMenuItem("Dialogi: " + ke.annaKatsomisDialogi());
            }
            else {
                dialogi = new JMenuItem("Dialogi: " + "<pois käytöstä>");
            }
            dialogi.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (ke.onkoEste()) {
                        JOptionPane.showMessageDialog(ikkuna, "Tämä objekti on este, eli se on luotu kuvasta, jonka nimi päättyy \"_e\".\n\nEsteobjekteilla ei voi olla dialogia, sillä niihin ei voi kävellä, eikä niitä näin ollen voi katsoa.", "Objekti on este", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else {
                        VisuaalinenObjektiMuokkaus.luoVisuaalisenObjektinMuokkausIkkuna(k.annaSijX(), k.annaSijY(), k);
                    }
                }
            });
            menuItemit.add(dialogi);
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

            case "Tile", "EsteTile", "Yksisuuntainen Tile":
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

    static ArrayList<JMenuItem> luoOikeaClickOminaisuusLista(Entity n) {

        ArrayList<JMenuItem> menuItemit = new ArrayList<JMenuItem>();

        JMenuItem tiedot = new JMenuItem("Tiedot");
        tiedot.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, n.annaTiedot(), "Entityn tiedot", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        tiedot.setText("Tiedot: " + n.annaNimi());
        menuItemit.add(tiedot);

        switch (n.annaNimi()) {

            default:
            break;

            case "Asevihu", "Pikkuvihu", "Pahavihu", "Pomo":
                Vihollinen v = (Vihollinen)n;
                JMenu liikeTapa = new JMenu("Liiketapa");
                liikeTapa.setToolTipText("Näille vois ehkä joskus tulla joku previewaus-ominaisuus.");
                for (LiikeTapa lt : LiikeTapa.values()) {
                    JCheckBoxMenuItem liikeTapaValinta = new JCheckBoxMenuItem(lt.name());
                    liikeTapaValinta.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            v.liikeTapa = Vihollinen.LiikeTapa.valueOf(lt.name());
                            v.päivitäLisäOminaisuudet(lt, v.suuntaVasenOikea);
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

                JMenu suunta = new JMenu("Suunta");
                suunta.setToolTipText("Vaikuttaa vihollisen katsomissuuntaan sekä asevihun ampumissuuntaan silloin, kun vihollinen on asetettu liikkumaan vain pystysuunnassa.");
                for (SuuntaVasenOikea suuntaVasenOikea : SuuntaVasenOikea.values()) {
                    JCheckBoxMenuItem suuntaValinta = new JCheckBoxMenuItem(suuntaVasenOikea.name());
                    suuntaValinta.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            v.suuntaVasenOikea = Vihollinen.SuuntaVasenOikea.valueOf(suuntaVasenOikea.name());
                            v.päivitäLisäOminaisuudet(v.liikeTapa, suuntaVasenOikea);
                        }
                    });
                    if (v.suuntaVasenOikea == Vihollinen.SuuntaVasenOikea.valueOf(suuntaVasenOikea.name())) {
                        suuntaValinta.setSelected(true);
                    }
                    else {
                        suuntaValinta.setSelected(false);
                    }
                    suunta.add(suuntaValinta);
                }
                menuItemit.add(suunta);

            break;
            case "Vartija":
                Vartija vartija = (Vartija)n;
                JMenuItem aktivointiAlue = new JMenuItem("Muokkaa aktivointialuetta");
                aktivointiAlue.addActionListener(e -> VartijaMuokkaus.luoVartijaMuokkausIkkuna(vartija));
                menuItemit.add(aktivointiAlue);
            break;

        }
        return menuItemit;
    }
    
    static void luoAlkuIkkuna(int sijX, int sijY, Icon pelaajaKuvake) {

        //objektiEditointiKenttäPaneli.removeAll();

        warpVasenNappi = new JButton[3];
        for (int i = 0; i < warpVasenNappi.length; i++){
            JButton jButton = new JButton(new KäännettäväKuvake(new ImageIcon("tiedostot/kuvat/osoitin.png"), 90, false));
            jButton.setBounds(0, 40, 40, Peli.kentänKoko*esineenKokoPx);
            jButton.setName("warp_vasen_nappi_" + i);
            jButton.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        if (huoneKartta != null) {
                            if (huoneKartta.get(muokattavaHuone).annaReunaWarppiTiedot(Suunta.VASEN)) {
                                int uusiHuone = huoneKartta.get(muokattavaHuone).annaReunaWarpinKohdeId(Suunta.VASEN);
                                vaihdaHuonetta(muokattavaHuone, uusiHuone, true);
                                muokattavaHuone = uusiHuone;
                            }
                            else {
                                String viesti = "Tässä huoneessa ei ole warppia vasemmalle.\n\nHaluatko asettaa warpin olemassaolevaan huoneeseen tai luoda uuden huoneen?";
                                int valinta = CustomViestiIkkunat.EiWarppia.näytäDialogi(viesti);
                                switch (valinta) {
                                    case JOptionPane.YES_OPTION:
                                        ReunaWarppiIkkuna.luoReunaWarppiIkkuna(true, Suunta.VASEN, false, 0);
                                    break;
                                    case JOptionPane.NO_OPTION:
                                        HuoneenLuontiIkkuna.luoHuoneenLuontiIkkuna(Suunta.VASEN);
                                    break;
                                    case JOptionPane.CANCEL_OPTION: break;
                                }
                            }
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Huonekartta on tyhjä. Kokeile luoda uusi huonekartta.", "Huonekartta null", JOptionPane.ERROR_MESSAGE);
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
            jButton.setName("warp_oikea_nappi_" + i);
            jButton.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        if (huoneKartta != null) {
                            if (huoneKartta.get(muokattavaHuone).annaReunaWarppiTiedot(Suunta.OIKEA)) {
                                int uusiHuone = huoneKartta.get(muokattavaHuone).annaReunaWarpinKohdeId(Suunta.OIKEA);
                                vaihdaHuonetta(muokattavaHuone, uusiHuone, true);
                                muokattavaHuone = uusiHuone;
                            }
                            else {
                                String viesti = "Tässä huoneessa ei ole warppia oikealle.\n\nHaluatko asettaa warpin olemassaolevaan huoneeseen tai luoda uuden huoneen?";
                                int valinta = CustomViestiIkkunat.EiWarppia.näytäDialogi(viesti);
                                switch (valinta) {
                                    case JOptionPane.YES_OPTION:
                                        ReunaWarppiIkkuna.luoReunaWarppiIkkuna(true, Suunta.OIKEA, false, 0);
                                    break;
                                    case JOptionPane.NO_OPTION:
                                        HuoneenLuontiIkkuna.luoHuoneenLuontiIkkuna(Suunta.OIKEA);
                                    break;
                                    case JOptionPane.CANCEL_OPTION: break;
                                }
                            }
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Huonekartta on tyhjä. Kokeile luoda uusi huonekartta.", "Huonekartta null", JOptionPane.ERROR_MESSAGE);
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
            jButton.setName("warp_ylös_nappi_" + i);
            jButton.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        if (huoneKartta != null) {
                            if (huoneKartta.get(muokattavaHuone).annaReunaWarppiTiedot(Suunta.YLÖS)) {
                                int uusiHuone = huoneKartta.get(muokattavaHuone).annaReunaWarpinKohdeId(Suunta.YLÖS);
                                vaihdaHuonetta(muokattavaHuone, uusiHuone, true);
                                muokattavaHuone = uusiHuone;
                            }
                            else {
                                String viesti = "Tässä huoneessa ei ole warppia ylös.\n\nHaluatko asettaa warpin olemassaolevaan huoneeseen tai luoda uuden huoneen?";
                                int valinta = CustomViestiIkkunat.EiWarppia.näytäDialogi(viesti);
                                switch (valinta) {
                                    case JOptionPane.YES_OPTION:
                                        ReunaWarppiIkkuna.luoReunaWarppiIkkuna(true, Suunta.YLÖS, false, 0);
                                    break;
                                    case JOptionPane.NO_OPTION:
                                        HuoneenLuontiIkkuna.luoHuoneenLuontiIkkuna(Suunta.YLÖS);
                                    break;
                                    case JOptionPane.CANCEL_OPTION: break;
                                }
                            }
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Huonekartta on tyhjä. Kokeile luoda uusi huonekartta.", "Huonekartta null", JOptionPane.ERROR_MESSAGE);
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
            jButton.setName("warp_alas_nappi_" + i);
            jButton.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        System.out.println(muokattavaHuone);
                        if (huoneKartta != null) {
                            if (huoneKartta.get(muokattavaHuone).annaReunaWarppiTiedot(Suunta.ALAS)) {
                                int uusiHuone = huoneKartta.get(muokattavaHuone).annaReunaWarpinKohdeId(Suunta.ALAS);
                                vaihdaHuonetta(muokattavaHuone, uusiHuone, true);
                                muokattavaHuone = uusiHuone;
                            }
                            else {
                                String viesti = "Tässä huoneessa ei ole warppia alas.\n\nHaluatko asettaa warpin olemassaolevaan huoneeseen tai luoda uuden huoneen?";
                                int valinta = CustomViestiIkkunat.EiWarppia.näytäDialogi(viesti);
                                switch (valinta) {
                                    case JOptionPane.YES_OPTION:
                                        ReunaWarppiIkkuna.luoReunaWarppiIkkuna(true, Suunta.ALAS, false, 0);
                                    break;
                                    case JOptionPane.NO_OPTION:
                                        HuoneenLuontiIkkuna.luoHuoneenLuontiIkkuna(Suunta.ALAS);
                                    break;
                                    case JOptionPane.CANCEL_OPTION: break;
                                }
                            }
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Huonekartta on tyhjä. Kokeile luoda uusi huonekartta.", "Huonekartta null", JOptionPane.ERROR_MESSAGE);
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
        
        int kohteenSijX = 0;
        int kohteensijY = 0;
        objektiKenttä = new KenttäKohde[Peli.kentänKoko][Peli.kentänKoko];
        maastoKenttä = new Maasto[Peli.kentänKoko][Peli.kentänKoko];
        npcKenttä = new Entity[Peli.kentänKoko][Peli.kentänKoko];
        kenttäKohteenKuvake = new JButton[Peli.kentänKoko][Peli.kentänKoko];
        maastoKohteenKuvake = new JButton[Peli.kentänKoko][Peli.kentänKoko];
        npcKohteenKuvake = new JButton[Peli.kentänKoko][Peli.kentänKoko];
        maastoKohteenKuvakeObjektiPanelissa = new JLabel[Peli.kentänKoko][Peli.kentänKoko];
        maastoKohteenKuvakeNpcPanelissa = new JLabel[Peli.kentänKoko][Peli.kentänKoko];
        
        for (int i = 0; i < Peli.kentänKoko; i++) {
            for (int j = 0; j < Peli.kentänKoko; j++) {
                int x = j;
                int y = i;
                kenttäKohteenKuvake[x][y] = new JButton(new ImageIcon());
                maastoKohteenKuvakeObjektiPanelissa[x][y] = new JLabel(new ImageIcon());
                maastoKohteenKuvakeObjektiPanelissa[x][y].setName("maastokohteen_kuvake_objektipanelissa_" + x + "_" + y);
                maastoKohteenKuvakeNpcPanelissa[x][y] = new JLabel(new ImageIcon());
                maastoKohteenKuvakeNpcPanelissa[x][y].setName("maastokohteen_kuvake_npcpanelissa_" + x + "_" + y);
                kenttäKohteenKuvake[x][y].addKeyListener(editorinNäppäinkomennot);
                kenttäKohteenKuvake[x][y].setName("kenttäkohteen_kuvake_" + x + "_" + y);
                kenttäKohteenKuvake[x][y].addMouseListener(new MouseAdapter() {
                    public void mousePressed (MouseEvent e) {
                        klikkaaEsineRuutuun(x, y, e);
                    }
                });

                maastoKohteenKuvake[j][i] = new JButton();
                maastoKohteenKuvake[x][y].addKeyListener(editorinNäppäinkomennot);
                maastoKohteenKuvake[x][y].setName("maastokohteen_kuvake_" + x + "_" + y);
                maastoKohteenKuvake[x][y].addMouseListener(new MouseAdapter() {
                    public void mousePressed (MouseEvent e) {
                        klikkaaMaastoRuutuun(x, y, e);
                    }
                });

                npcKohteenKuvake[x][y] = new JButton(new ImageIcon());
                npcKohteenKuvake[x][y].addKeyListener(editorinNäppäinkomennot);
                npcKohteenKuvake[x][y].setName("npckohteen_kuvake_" + x + "_" + y);
                npcKohteenKuvake[x][y].addMouseListener(new MouseAdapter() {
                    public void mousePressed (MouseEvent e) {
                        klikkaaEntityRuutuun(x, y, e);
                    }
                });
            }
        }
        
        try {
            for (int i = 0; i < Peli.kentänKoko; i++) {
                for (int j = 0; j < Peli.kentänKoko; j++) {
                    if (kenttäKohteenKuvake[j][i] != null) {
                        if (Peli.annaObjektiKenttä().length > i && Peli.annaObjektiKenttä().length > j) {
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
                                Maasto m = Peli.annaMaastoKenttä()[j][i];
                                maastoKohteenKuvake[j][i].setIcon(Peli.annaMaastoKenttä()[j][i].annaKuvake());
                                maastoKohteenKuvakeObjektiPanelissa[j][i].setIcon(new KäännettäväKuvake(m.annaKuvake(), 0, false, false, 64, 0.5f));
                                maastoKohteenKuvakeNpcPanelissa[j][i].setIcon(new KäännettäväKuvake(m.annaKuvake(), 0, false, false, 64, 0.5f));
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
                        }
                        kohteenSijX += esineenKokoPx;
                    }
                }
                kohteenSijX = 0;
                kohteensijY += esineenKokoPx;
            }

            objektiEditointiKenttäPaneliUlompi.add(warpVasenNappi[0]);
            maastoEditointiKenttäPaneliUlompi.add(warpVasenNappi[1]);
            npcEditointiKenttäPaneliUlompi.add(warpVasenNappi[2]);
            objektiEditointiKenttäPaneliUlompi.add(warpOikeaNappi[0]);
            maastoEditointiKenttäPaneliUlompi.add(warpOikeaNappi[1]);
            npcEditointiKenttäPaneliUlompi.add(warpOikeaNappi[2]);
            objektiEditointiKenttäPaneliUlompi.add(warpYlösNappi[0]);
            maastoEditointiKenttäPaneliUlompi.add(warpYlösNappi[1]);
            npcEditointiKenttäPaneliUlompi.add(warpYlösNappi[2]);
            objektiEditointiKenttäPaneliUlompi.add(warpAlasNappi[0]);
            maastoEditointiKenttäPaneliUlompi.add(warpAlasNappi[1]);
            npcEditointiKenttäPaneliUlompi.add(warpAlasNappi[2]);
            
            //objektiEditointiKenttäPaneli.setComponentZOrder(pelaajaLabel, 0);
            //peliKenttä.setComponentZOrder(taustaLabel, 2);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Jokin meni pieleen", "Array index out of Bounds", JOptionPane.ERROR_MESSAGE);
        }
        catch (IllegalArgumentException e) {

        }

        objektiEditointiKenttäPaneli.revalidate();
        objektiEditointiKenttäPaneli.repaint();
    }

    /**
     * Luo valikko, joka tulee näkyviin, kun huoneenvaihtonuolta klikataan oikealla.
     * @param suunta minkä suuntaista nuolta klikattiin
     * @return lista valikkopainikkeita (JMenuItem)
     */
    static ArrayList<JMenuItem> luoOikeaClickWarpMenu(Suunta suunta) {

        ArrayList<JMenuItem> menuItemit = new ArrayList<JMenuItem>();

        JMenuItem asetaWarp = new JMenuItem("Valitse warpin kohde");
        asetaWarp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    ReunaWarppiIkkuna.luoReunaWarppiIkkuna(true, suunta, false, 0);
                }
                catch (IndexOutOfBoundsException ioobe) {
                    ReunaWarppiIkkuna.luoReunaWarppiIkkuna(false, suunta, false, 0);
                }
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

    /**
     * Kutsu tätä funktiota joka "framessa", jos editori on auki.
     */
    public static void päivitäEditoriIkkuna() {
        if (editoriAuki()) {
            if (!HuoneenLatausSäie.käynnissä) {
                try {
                    päivitäObjektiKenttä();
                    päivitäMaastoKenttä();
                    päivitäNPCKenttä();
                    päivitäTooltipit();
                    Thread.sleep(100);
                }
                catch (InterruptedException ie) {
                    System.out.println("Kuka kehtaa herättää minut uniltani!? T. Vihainen grafiikkasäie");
                }
            }
            // if (huoneInfoLabel != null) {
            //     huoneInfoLabel.setText("Huone " + muokattavaHuone);
            // }
        }
    }

    /**
     * Päivitä komponenttien tooltip-tekstit, jotka näkyy, kun pitää hiirtä komponentin yllä.
     */
    static void päivitäTooltipit() {
        try {
            if (huoneKartta != null) {
                if (huoneKartta.get(muokattavaHuone) != null) {
                    if (warpVasenNappi != null) {
                        if (huoneKartta.get(muokattavaHuone).annaReunaWarppiTiedot(Suunta.VASEN) && huoneKartta.get(huoneKartta.get(muokattavaHuone).annaReunaWarpinKohdeId(Suunta.VASEN)) != null) {
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
                        if (huoneKartta.get(muokattavaHuone).annaReunaWarppiTiedot(Suunta.OIKEA) && huoneKartta.get(huoneKartta.get(muokattavaHuone).annaReunaWarpinKohdeId(Suunta.OIKEA)) != null) {
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
                        if (huoneKartta.get(muokattavaHuone).annaReunaWarppiTiedot(Suunta.ALAS) && huoneKartta.get(huoneKartta.get(muokattavaHuone).annaReunaWarpinKohdeId(Suunta.ALAS)) != null) {
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
                        if (huoneKartta.get(muokattavaHuone).annaReunaWarppiTiedot(Suunta.YLÖS) && huoneKartta.get(huoneKartta.get(muokattavaHuone).annaReunaWarpinKohdeId(Suunta.YLÖS)) != null) {
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
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Virhe päivittäessä editorin elementtejä", "häire", JOptionPane.ERROR_MESSAGE);
        }
    }
    static boolean näytäEdistyneetVirheviestit = true;
    static JPanel päivitäObjektiKenttä() {
        
        if (vaatiiPäivityksen && objektiEditointiKenttäPaneli != null && objektiKenttä != null && maastoKenttä != null && npcKenttä != null) {
            try {
                for (int i = 0; i < objektiKenttä.length; i++) {
                    for (int j = 0; j < objektiKenttä.length; j++) {
                        if (true) {
                            if (objektiKenttä[j][i] instanceof KenttäKohde && kenttäKohteenKuvake[j][i] != null) {
                                kenttäKohteenKuvake[j][i].setIcon(objektiKenttä[j][i].annaKuvake());
                            }
                            else if (kenttäKohteenKuvake[j][i] != null) {
                                kenttäKohteenKuvake[j][i].setIcon(null);
                            }
                            if (maastoKenttä[j][i] instanceof Maasto && maastoKohteenKuvakeObjektiPanelissa[j][i] != null) {
                                Maasto m = (Maasto)maastoKenttä[j][i];
                                maastoKohteenKuvakeObjektiPanelissa[j][i].setIcon(new KäännettäväKuvake(m.annaKuvake(), 0, false, false, 64, 0.5f));
                            }
                            else if (maastoKohteenKuvakeObjektiPanelissa[j][i] != null) {
                                maastoKohteenKuvakeObjektiPanelissa[j][i].setIcon(null);
                            }

                            if (maastoKenttä[j][i] instanceof Maasto && maastoKohteenKuvakeNpcPanelissa[j][i] != null) {
                                Maasto m = (Maasto)maastoKenttä[j][i];
                                maastoKohteenKuvakeNpcPanelissa[j][i].setIcon(new KäännettäväKuvake(m.annaKuvake(), 0, false, false, 64, 0.5f));
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
                }

                if (vaatiiKentänPäivityksen) {
                    pelaajaLabel.setBounds(Pelaaja.sijX * pelaajanKokoPx + 10, Pelaaja.sijY * pelaajanKokoPx + 10, pelaajanKokoPx, pelaajanKokoPx);
                }
            }
            catch (ArrayIndexOutOfBoundsException e) {
                //JOptionPane.showMessageDialog(null, "Jokin meni pieleen.", "Array index out of Bounds", JOptionPane.ERROR_MESSAGE);
                //GrafiikanPäivitysSäie.ongelmaGrafiikassa = true;
                System.out.println("Ohitetaan objektikentän grafiikan päivitysyritys");
                e.printStackTrace();
            }
            catch (NullPointerException e) {
                System.out.println("Ongelma ruudunpäivityksessä");
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                if (näytäEdistyneetVirheviestit) {
                    String sStackTrace = sw.toString();
                    System.out.println(sStackTrace);
                    String viesti = "Yritettiin ladata kuvaa objektille, jota ei ole vielä luotu.\n\nHäire sovelluksessa. Ilmoitathan kehittäjille.\n\n" + sStackTrace;
                    String otsikko = "Virhe ruudunpäivityksessä";
                    int virheenJälkeenValinta = CustomViestiIkkunat.FataaliVirhe.näytäDialogi(viesti, otsikko);
                    switch (virheenJälkeenValinta) {
                        case JOptionPane.YES_OPTION: break;
                        case JOptionPane.NO_OPTION: System.exit(0); break;
                        case JOptionPane.CANCEL_OPTION: ikkuna.dispose(); break;
                    }
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
        
        if (vaatiiPäivityksen && maastoEditointiKenttäPaneli != null && objektiKenttä != null && maastoKenttä != null && npcKenttä != null) {
            try {
                for (int i = 0; i < maastoKenttä.length; i++) {
                    for (int j = 0; j < maastoKenttä.length; j++) {
                        if (true) {
                            if (maastoKenttä[j][i] instanceof Maasto && maastoKohteenKuvake[j][i] != null) {
                                if (zoom == 64) {
                                    maastoKohteenKuvake[j][i].setIcon(maastoKenttä[j][i].annaKuvake());
                                    maastoKenttä[j][i].päivitäKuvanAsento();
                                }
                                else {
                                    if (maastoKenttä[j][i].annaKuvanTiedostoNimi() != null) {
                                        //maastoKohteenKuvake[j][i].setIcon(new ImageIcon(((ImageIcon)maastoKohteenKuvake[j][i].getIcon()).getImage().getScaledInstance(zoom, zoom, Image.SCALE_DEFAULT)));
                                        //maastoKohteenKuvake[j][i].setIcon(new ImageIcon((new ImageIcon(maastoKenttä[j][i].annaKuvanTiedostoNimi())).getImage().getScaledInstance(zoom, zoom, Image.SCALE_DEFAULT)));
                                        maastoKohteenKuvake[j][i].setIcon(maastoKenttä[j][i].annaKuvake());
                                    }
                                    else {
                                        maastoKohteenKuvake[j][i].setIcon(maastoKenttä[j][i].annaKuvake());
                                        maastoKenttä[j][i].päivitäKuvanAsento();
                                    }
                                }
                            }
                            else if (maastoKohteenKuvake[j][i] != null) {
                                maastoKohteenKuvake[j][i].setIcon(null);
                            }
                        }
                    }
                }

                if (vaatiiKentänPäivityksen) {
                    pelaajaLabel.setBounds(Pelaaja.sijX * pelaajanKokoPx + 10, Pelaaja.sijY * pelaajanKokoPx + 10, pelaajanKokoPx, pelaajanKokoPx);
                }
            }
            catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Ohitetaan maastokentän grafiikan päivitysyritys");
                e.printStackTrace();
            }
            catch (NullPointerException e) {
                System.out.println("Ongelma ruudunpäivityksessä");
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                if (näytäEdistyneetVirheviestit) {
                    String sStackTrace = sw.toString();
                    System.out.println(sStackTrace);
                    String viesti = "Yritettiin ladata kuvaa maastolle, jota ei ole vielä luotu.\n\nHäire sovelluksessa. Jos tämä viesti tulee toistuvasti " + "\"jatka\"" + " -painamisen jälkeen, ilmoitathan kehittäjille.\n\n" + sStackTrace;
                    String otsikko = "Virhe ruudunpäivityksessä";
                    int virheenJälkeenValinta = CustomViestiIkkunat.FataaliVirhe.näytäDialogi(viesti, otsikko);
                    switch (virheenJälkeenValinta) {
                        case JOptionPane.YES_OPTION: break;
                        case JOptionPane.NO_OPTION: System.exit(0); break;
                        case JOptionPane.CANCEL_OPTION: ikkuna.dispose(); break;
                    }
                }
            }
            catch (IllegalArgumentException e) {
                System.out.println("Ongelma ruudunpäivityksessä");
                e.printStackTrace();
            }

            maastoEditointiKenttäPaneli.revalidate();
            maastoEditointiKenttäPaneli.repaint();
        }
        return maastoEditointiKenttäPaneli;
    }

    static JPanel päivitäNPCKenttä() {
        
        if (vaatiiPäivityksen && npcEditointiKenttäPaneli != null && objektiKenttä != null && maastoKenttä != null && npcKenttä != null) {
            try {
                for (int i = 0; i < npcKenttä.length; i++) {
                    for (int j = 0; j < npcKenttä.length; j++) {
                        if (true) {
                            if (npcKenttä[j][i] instanceof Entity && npcKohteenKuvake[j][i] != null) {
                                npcKohteenKuvake[j][i].setIcon(npcKenttä[j][i].annaKuvake());
                            }
                            else if (npcKohteenKuvake[j][i] != null) {
                                npcKohteenKuvake[j][i].setIcon(null);
                            }
                        }
                    }
                }

                if (vaatiiKentänPäivityksen) {
                    pelaajaLabel.setBounds(Pelaaja.sijX * pelaajanKokoPx + 10, Pelaaja.sijY * pelaajanKokoPx + 10, pelaajanKokoPx, pelaajanKokoPx);
                }
            }
            catch (ArrayIndexOutOfBoundsException e) {
                //JOptionPane.showMessageDialog(null, "Jokin meni pieleen.", "Array index out of Bounds", JOptionPane.ERROR_MESSAGE);
                //GrafiikanPäivitysSäie.ongelmaGrafiikassa = true;
                System.out.println("Ohitetaan npc-kentän grafiikan päivitysyritys");
                e.printStackTrace();
            }
            catch (NullPointerException e) {
                System.out.println("Ongelma ruudunpäivityksessä");
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                if (näytäEdistyneetVirheviestit) {
                    String sStackTrace = sw.toString();
                    System.out.println(sStackTrace);
                    String viesti = "Yritettiin ladata kuvaa npc:lle, jota ei ole vielä luotu.\n\nHäire sovelluksessa. Ilmoitathan kehittäjille.\n\n" + sStackTrace;
                    String otsikko = "Virhe ruudunpäivityksessä";
                    int virheenJälkeenValinta = CustomViestiIkkunat.FataaliVirhe.näytäDialogi(viesti, otsikko);
                    switch (virheenJälkeenValinta) {
                        case JOptionPane.YES_OPTION: break;
                        case JOptionPane.NO_OPTION: System.exit(0); break;
                        case JOptionPane.CANCEL_OPTION: ikkuna.dispose(); break;
                    }
                }
            }
            catch (IllegalArgumentException e) {
                System.out.println("Ongelma ruudunpäivityksessä");
                e.printStackTrace();
            }

            npcEditointiKenttäPaneli.revalidate();
            npcEditointiKenttäPaneli.repaint();
        }
        return npcEditointiKenttäPaneli;
    }

    private static void skaalaaEditointiKenttä() {
        int uusiLeveys = ikkuna.getWidth() - 100;
        int uusiKorkeus = ikkuna.getHeight() - 320;
        scrollattavaObjektiKenttäPaneli.setBounds(40, 100, uusiLeveys, uusiKorkeus);
        scrollattavaMaastoKenttäPaneli.setBounds(40, 100, uusiLeveys, uusiKorkeus);
        scrollattavaNPCKenttäPaneli.setBounds(40, 100, uusiLeveys, uusiKorkeus);
        for (JButton jb : warpVasenNappi) {
            jb.setBounds(0, 100, 40, uusiKorkeus);
        }
        for (JButton jb : warpOikeaNappi) {
            jb.setBounds(uusiLeveys + 40, 100, 40, uusiKorkeus);
        }
        for (JButton jb : warpYlösNappi) {
            jb.setBounds(40, 60, uusiLeveys, 40);
        }
        for (JButton jb : warpAlasNappi) {
            jb.setBounds(40, uusiKorkeus + 100, uusiLeveys, 40);
        }
    }

    static class ZoomPanel2 extends JScrollPane {

        private static final long serialVersionUID = 1L;
        private double zoomFactor = 1.0;
        private static final double ZOOM_MULTIPLIER = 1.1;
        private static final double MIN_ZOOM = 0.1; // Minimum zoom level
        private static final double MAX_ZOOM = 4.0; // Maximum zoom level

        private Point zoomCenter;
        private AffineTransform currentTransform;
        private Point lastMousePosition;
        private Rectangle2D.Double square;
        private Point dragOffset;
        private Rectangle2D.Double redRectangle;

        public ZoomPanel2(JPanel panel) {
            super(panel);
            currentTransform = new AffineTransform();
            square = new Rectangle2D.Double(100, 100, 200, 200);
            redRectangle = new Rectangle2D.Double(0, 0, 1000, 1000);

            addMouseWheelListener(new MouseAdapter() {
                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    int zoomKerroin = e.getWheelRotation() < 0 ? 1 : -1;
                    zoom += zoomKerroin;
                    System.out.println("zoom: " + zoom);
                    for (int y = 0; y < kenttäKohteenKuvake.length; y++) {
                        for (int x = 0; x < kenttäKohteenKuvake.length; x++) {
                            int uusiX = kenttäKohteenKuvake[x][y].getX() +x*zoomKerroin;
                            int uusiY = kenttäKohteenKuvake[x][y].getY() +y*zoomKerroin;
                            kenttäKohteenKuvake[x][y].setBounds(uusiX, uusiY, zoom, zoom);
                            maastoKohteenKuvakeObjektiPanelissa[x][y].setBounds(uusiX, uusiY, zoom, zoom);
                            maastoKohteenKuvake[x][y].setBounds(uusiX, uusiY, zoom, zoom);
                            npcKohteenKuvake[x][y].setBounds(uusiX, uusiY, zoom, zoom);
                            maastoKohteenKuvakeNpcPanelissa[x][y].setBounds(uusiX, uusiY, zoom, zoom); 
                        }
                    }
                    JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, objektiEditointiKenttäPaneli);
                    if (viewPort != null) {
                        Rectangle view = viewPort.getViewRect();
                        int deltaX = e.getX() - (int)viewPort.getSize().getWidth()/2;
                        int deltaY = e.getY() - (int)viewPort.getSize().getHeight()/2;
                        
                        view.x += deltaX/((int)viewPort.getSize().getWidth()/8);
                        view.y += deltaY/((int)viewPort.getSize().getHeight()/8);
                        view.x += 8*zoomKerroin;
                        view.y += 8*zoomKerroin;

                        objektiEditointiKenttäPaneli.scrollRectToVisible(view);
                        maastoEditointiKenttäPaneli.scrollRectToVisible(view);
                        npcEditointiKenttäPaneli.scrollRectToVisible(view);
                    }
                    objektiEditointiKenttäPaneli.revalidate();
                    objektiEditointiKenttäPaneli.repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Graphics2D g2d = (Graphics2D) g;
            // g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // // Apply zoom factor
            // if (zoomCenter != null) {
            //     g2d.transform(currentTransform);
            // }

            // // Draw red rectangle
            // g2d.setColor(Color.red);
            // g2d.fill(redRectangle);

            // // Draw blue square
            // g2d.setColor(Color.BLUE);
            // g2d.fill(square);
        }

        private void updatePreferredSize() {
            int width = (int) (redRectangle.width * zoomFactor);
            int height = (int) (redRectangle.height * zoomFactor);
            setPreferredSize(new Dimension(width, height));
            revalidate();
        }    
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
                case KeyEvent.VK_SUBTRACT:
                    zoom--;    
                    for (int y = 0; y < kenttäKohteenKuvake.length; y++) {
                        for (int x = 0; x < kenttäKohteenKuvake.length; x++) {
                            int uusiX = kenttäKohteenKuvake[x][y].getX() -x;
                            int uusiY = kenttäKohteenKuvake[x][y].getY() -y;
                            kenttäKohteenKuvake[x][y].setBounds(uusiX, uusiY, zoom, zoom);
                            maastoKohteenKuvakeObjektiPanelissa[x][y].setBounds(uusiX, uusiY, zoom, zoom);
                            maastoKohteenKuvake[x][y].setBounds(uusiX, uusiY, zoom, zoom);
                            npcKohteenKuvake[x][y].setBounds(uusiX, uusiY, zoom, zoom);
                            maastoKohteenKuvakeNpcPanelissa[x][y].setBounds(uusiX, uusiY, zoom, zoom);
                        }
                    }
                    objektiEditointiKenttäPaneli.revalidate();
                    objektiEditointiKenttäPaneli.repaint();
                break;
                case KeyEvent.VK_ADD:
                    zoom++;
                    for (int y = 0; y < kenttäKohteenKuvake.length; y++) {
                        for (int x = 0; x < kenttäKohteenKuvake.length; x++) {
                            int uusiX = kenttäKohteenKuvake[x][y].getX() +x;
                            int uusiY = kenttäKohteenKuvake[x][y].getY() +y;
                            kenttäKohteenKuvake[x][y].setBounds(uusiX, uusiY, zoom, zoom);
                            maastoKohteenKuvakeObjektiPanelissa[x][y].setBounds(uusiX, uusiY, zoom, zoom);
                            maastoKohteenKuvake[x][y].setBounds(uusiX, uusiY, zoom, zoom);
                            npcKohteenKuvake[x][y].setBounds(uusiX, uusiY, zoom, zoom);
                            maastoKohteenKuvakeNpcPanelissa[x][y].setBounds(uusiX, uusiY, zoom, zoom);
                        }
                    }
                    objektiEditointiKenttäPaneli.revalidate();
                    objektiEditointiKenttäPaneli.repaint();
                break;
                default:
                    System.out.println("Näppäimellä "+ e.getKeyCode() + " ei ole toimintoa.");
                break;
            }
        }
    
        @Override
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_CONTROL:
                    ctrlPainettu = false;
                break;
            }
        }
    }
}
