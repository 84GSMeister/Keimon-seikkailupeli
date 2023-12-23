package keimo.HuoneEditori.DialogiEditori;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import keimo.Ikkunat.CustomViestiIkkunat;

public class DialogiEditoriIkkuna {

    static JFrame ikkuna;

    static JTextArea tekstiMuokkausKenttä;
    static String käytettävänDialoginTunniste;
    static JTree dialogipuu;
    static HashMap<String, VuoropuheDialogiPätkä> editorinDialogiKartta = new HashMap<>();

    static JTextField puhujanNimiTekstikenttä;
    static JButton puhujanKuvaNappi;
    static JLabel esikatseluPuhujanKuvaLabel;
    static JLabel esikatseluPuhujanTekstiLabel;
    static JLabel esikatseluPuhujanNimiLabel;
    static JPanel keskiPaneeli, dialogiMuokkausPaneeli, valintaMuokkausPaneeli;

    static DialogiKuvakeTyyppi[][] puunKuvakeTyypit;
    
    public static void luoDialogiEditoriIkkuna() {
        List<VuoropuheDialogiPätkä> editorinVdpt = VuoropuheDialogit.vuoropuheDialogiKartta.values().stream().toList();
        editorinDialogiKartta.clear();
        for (VuoropuheDialogiPätkä vdp : editorinVdpt) {
            System.out.println(vdp.annaTunniste());
            String[] kopioidutTekstit = Arrays.copyOf(vdp.annaTekstit(), vdp.annaTekstit().length);
            String[] kopioidutPuhujat = Arrays.copyOf(vdp.annaPuhujat(), vdp.annaPuhujat().length);
            String[] kopioidutValinnanVaihtoehdot = null;
            String[] kopioidutValinnanVaihtoehtojenKohdedialogit = null;
            String[] kopioidutTriggerit = null;
            if (vdp.onkoValinta()) {
                kopioidutValinnanVaihtoehdot = Arrays.copyOf(vdp.annaValinnanVaihtoehdot(), vdp.annaValinnanVaihtoehdot().length);
                kopioidutValinnanVaihtoehtojenKohdedialogit = Arrays.copyOf(vdp.annaValinnanVaihtoehtojenKohdeDialogit(), vdp.annaValinnanVaihtoehtojenKohdeDialogit().length);
                kopioidutTriggerit = Arrays.copyOf(vdp.annaTriggerit(), vdp.annaTriggerit().length);
            }
            editorinDialogiKartta.put(vdp.annaTunniste(), new VuoropuheDialogiPätkä(vdp.annaTunniste(), vdp.annaPituus(), vdp.annaKuvat(), kopioidutTekstit, kopioidutPuhujat, vdp.onkoValinta(), vdp.annaValinnanNimi(), vdp.annaValinnanOtsikko(), kopioidutValinnanVaihtoehdot, kopioidutValinnanVaihtoehtojenKohdedialogit, kopioidutTriggerit));
        }

        ikkuna = new JFrame("Dialogieditori v0.1");
        ikkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja_og.png").getImage());
        ikkuna.setBounds(0, 0, 800, 600);
        ikkuna.setLocationRelativeTo(null);
        ikkuna.setVisible(true);

        JMenuItem tuoTekstitiedostosta = new JMenuItem("Tuo tekstitiedostosta");
        tuoTekstitiedostosta.addActionListener(e -> {
            JFileChooser tiedostoSelain = new JFileChooser("tiedostot/tekstit");
            int valinta = tiedostoSelain.showOpenDialog(ikkuna);
            if (valinta == JFileChooser.APPROVE_OPTION) {
                File tiedosto = tiedostoSelain.getSelectedFile();
                try {
                    Scanner sc = new Scanner(tiedosto);
                    String tiedostonTeksti = "";
                    while (sc.hasNextLine()) {
                        tiedostonTeksti += sc.nextLine() + "\n";
                    }
                    tekstiMuokkausKenttä.setText(tiedostonTeksti);
                    sc.close();
                }
                catch (FileNotFoundException fnfe) {
                    System.out.println("Tiedostoa ei löytynyt.");
                }
            }
        });

        JMenu tiedosto = new JMenu("Tiedosto");
        tiedosto.add(tuoTekstitiedostosta);

        JMenuItem päivitä = new JMenuItem("Päivitä (joo tähän joku parempi mekaniikka)");
        päivitä.addActionListener(e -> {
            DefaultTreeModel model = (DefaultTreeModel)dialogipuu.getModel();
            model.reload();
        });

        JMenu toiminnot = new JMenu("Toiminnot");
        toiminnot.add(päivitä);

        JMenuBar yläPalkki = new JMenuBar();
        yläPalkki.add(tiedosto);
        yläPalkki.add(toiminnot);
        
        String tarinaTekstiPuussa = "";
        DefaultMutableTreeNode dialogiPuunJuuri = new DefaultMutableTreeNode("Dialogit");
        for (VuoropuheDialogiPätkä vdp : editorinDialogiKartta.values()) {
            String dialoginNimi = vdp.vuoropuheTunniste;
            DialogiPuunAlkio dialogiPuunKansio = new DialogiPuunAlkio(dialoginNimi);
            for (String s : editorinDialogiKartta.get(dialoginNimi).annaTekstit()) {
                tarinaTekstiPuussa += s;
                tarinaTekstiPuussa += "\n\n";
                DialogiPuunAlkio dialogiPuunItemi = new DialogiPuunAlkio(tarinaTekstiPuussa);
                dialogiPuunItemi.asetaKuvakeTyyppi(DialogiKuvakeTyyppi.VUOROPUHE);
                dialogiPuunKansio.add(dialogiPuunItemi);
                tarinaTekstiPuussa = "";
            }
            if (vdp.onkoValinta()) {
                DialogiPuunAlkio dialogiPuunValintaKansio = new DialogiPuunAlkio(vdp.annaValinnanNimi());
                dialogiPuunValintaKansio.asetaKuvakeTyyppi(DialogiKuvakeTyyppi.VALINTA);
                dialogiPuunKansio.add(dialogiPuunValintaKansio);
                DialogiPuunAlkio dialogiPuunValinnanOtsikkoNode = new DialogiPuunAlkio("O: " + vdp.annaValinnanOtsikko());
                dialogiPuunValintaKansio.add(dialogiPuunValinnanOtsikkoNode);
                for (int i = 0; i < vdp.annaValinnanVaihtoehdot().length; i++) {
                    DialogiPuunAlkio dialogiPuunValintaVaihtoehtoNode = new DialogiPuunAlkio("V: " + vdp.annaValinnanVaihtoehdot()[i]);
                    dialogiPuunValintaVaihtoehtoNode.asetaKuvakeTyyppi(DialogiKuvakeTyyppi.VAIHTOEHTO);
                    dialogiPuunValintaKansio.add(dialogiPuunValintaVaihtoehtoNode);
                    if (vdp.annaTriggerit()[i] != null) {
                        DialogiPuunAlkio dialogiPuunTriggeriNode = new DialogiPuunAlkio("T: " + vdp.annaTriggerit()[i]);
                        dialogiPuunTriggeriNode.asetaKuvakeTyyppi(DialogiKuvakeTyyppi.TRIGGERI);
                        dialogiPuunValintaKansio.add(dialogiPuunTriggeriNode);
                    }
                    DialogiPuunAlkio dialogiPuunValintaVaihtoehdonKohdeNode = new DialogiPuunAlkio("D: " + vdp.annaValinnanVaihtoehtojenKohdeDialogit()[i]);
                    dialogiPuunValintaVaihtoehdonKohdeNode.asetaKuvakeTyyppi(DialogiKuvakeTyyppi.VAIHTOEHDON_KOHDE);
                    dialogiPuunValintaKansio.add(dialogiPuunValintaVaihtoehdonKohdeNode);
                }
            }
            dialogiPuunJuuri.add(dialogiPuunKansio);
        }
        dialogipuu = new JTree(dialogiPuunJuuri);
        dialogipuu.setMinimumSize(new Dimension(200, 300));
        dialogipuu.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent tse) {
                if (tse.getNewLeadSelectionPath() != null) {
                    String node = tse.getNewLeadSelectionPath().getLastPathComponent().toString();
                    käytettävänDialoginTunniste = node;
                    //DefaultMutableTreeNode löydettyNode = findNode(node);
                    DefaultMutableTreeNode löydettyNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
                    DefaultTreeModel model = (DefaultTreeModel)dialogipuu.getModel();
                    if (löydettyNode.getLevel() == 2) {
                        if (löydettyNode.getChildCount() <= 0) {
                            int dialogiIndeksi = löydettyNode.getParent().getIndex(löydettyNode);
                            String dialoginNimi = "" + löydettyNode.getParent();
                            VuoropuheDialogiPätkä vdp = editorinDialogiKartta.get(dialoginNimi);
                            if (vdp.annaPituus() > dialogiIndeksi) {
                                tekstiMuokkausKenttä.setText(vdp.annaTekstit()[dialogiIndeksi]);
                                puhujanNimiTekstikenttä.setText(vdp.annaPuhujat()[dialogiIndeksi]);
                                puhujanKuvaNappi.setIcon(vdp.annaKuvat()[dialogiIndeksi]);
                                esikatseluPuhujanKuvaLabel.setIcon(puhujanKuvaNappi.getIcon());
                                dialogiMuokkausPaneeli.setVisible(true);
                                valintaMuokkausPaneeli.setVisible(false);
                                keskiPaneeli.remove(valintaMuokkausPaneeli);
                                keskiPaneeli.add(dialogiMuokkausPaneeli);
                            }
                        }
                        else {
                            dialogiMuokkausPaneeli.setVisible(false);
                            valintaMuokkausPaneeli.setVisible(true);
                            keskiPaneeli.add(valintaMuokkausPaneeli);
                            keskiPaneeli.remove(dialogiMuokkausPaneeli);
                        }
                    }
                    else if (löydettyNode.getLevel() == 3) {
                        dialogiMuokkausPaneeli.setVisible(false);
                        valintaMuokkausPaneeli.setVisible(true);
                        keskiPaneeli.add(valintaMuokkausPaneeli);
                        keskiPaneeli.remove(dialogiMuokkausPaneeli);
                    }
                    else {
                        puhujanNimiTekstikenttä.setText("");
                        puhujanKuvaNappi.setIcon(null);
                        esikatseluPuhujanKuvaLabel.setIcon(null);
                        dialogiMuokkausPaneeli.setVisible(false);
                        valintaMuokkausPaneeli.setVisible(false);
                    }
                }
                if (puhujanKuvaNappi.getIcon() != null) {
                    puhujanKuvaNappi.repaint();
                }
                ikkuna.revalidate();
                ikkuna.repaint();
            }
        });
        dialogipuu.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    DefaultTreeModel model = (DefaultTreeModel)dialogipuu.getModel();
                }
                else if (SwingUtilities.isRightMouseButton(e)) {
                    if (dialogipuu.getSelectionPath() != null) {
                        DefaultMutableTreeNode valittuNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
                        switch (valittuNode.getLevel()) {
                            case 0:
                                JPopupMenu muokkausValikko = new JPopupMenu();
                                JMenuItem lisääDialogiPätkä = new JMenuItem("Lisää dialogipätkä");
                                lisääDialogiPätkä.addActionListener(listener -> {
                                    DefaultTreeModel model = (DefaultTreeModel)dialogipuu.getModel();
                                    DefaultMutableTreeNode löydettyNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
                                    if (löydettyNode != null) {
                                        MutableTreeNode lisättäväNode = new DefaultMutableTreeNode("dialogipätkä");
                                        model.insertNodeInto(lisättäväNode, (MutableTreeNode)löydettyNode, löydettyNode.getChildCount());
                                        editorinDialogiKartta.put("" + lisättäväNode, new VuoropuheDialogiPätkä("" + lisättäväNode, 0, new Icon[0], new String[0], new String[0], false, null, null, null, null, null));
                                    }
                                });
                                muokkausValikko.add(lisääDialogiPätkä);
                                muokkausValikko.show(dialogipuu, e.getX(), e.getY());
                            break;

                            case 1:
                                if (true) {
                                    muokkausValikko = new JPopupMenu();

                                    JMenu lisäysValikko = new JMenu("Lisää");
                                    JMenuItem lisääDialogi = new JMenuItem("Tekstipätkä");
                                    lisääDialogi.addActionListener(listener -> {
                                        DefaultTreeModel model = (DefaultTreeModel)dialogipuu.getModel();
                                        DefaultMutableTreeNode löydettyNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
                                        if (löydettyNode != null) {
                                            if (löydettyNode.getParent().getIndex(löydettyNode) < löydettyNode.getParent().getChildCount()-1 || löydettyNode.getParent().getChildAt(löydettyNode.getParent().getChildCount()-1).getChildCount() <= 0) {
                                                MutableTreeNode lisättäväNode = new DefaultMutableTreeNode("teksti");
                                                model.insertNodeInto(lisättäväNode, (MutableTreeNode)löydettyNode, löydettyNode.getChildCount());
                                                ArrayList<String> tekstiLista = new ArrayList<>();
                                                ArrayList<String> puhujaLista = new ArrayList<>();
                                                ArrayList<Icon> kuvaLista = new ArrayList<>();
                                                VuoropuheDialogiPätkä vdp = editorinDialogiKartta.get("" + löydettyNode);
                                                for (String s : vdp.annaTekstit()) {
                                                    tekstiLista.add(s);
                                                }
                                                tekstiLista.add(tekstiLista.size(), "" + lisättäväNode);
                                                String[] uusiTekstiLista = tekstiLista.toArray(new String[tekstiLista.size()]);
                                                for (String s : vdp.annaPuhujat()) {
                                                    puhujaLista.add(s);
                                                }
                                                puhujaLista.add(puhujaLista.size(), "puhuja");
                                                String[] uusiPuhujaLista = puhujaLista.toArray(new String[puhujaLista.size()]);
                                                for (Icon icon : vdp.annaKuvat()) {
                                                    kuvaLista.add(icon);
                                                }
                                                kuvaLista.add(kuvaLista.size(), null);
                                                Icon[] uusiKuvaLista = kuvaLista.toArray(new Icon[kuvaLista.size()]);
                                                vdp.lisääSivu();
                                                editorinDialogiKartta.put(vdp.annaTunniste(), new VuoropuheDialogiPätkä(vdp.annaTunniste(), vdp.annaPituus(), uusiKuvaLista, uusiTekstiLista, uusiPuhujaLista, vdp.onkoValinta(), vdp.annaValinnanNimi(), vdp.annaValinnanOtsikko(), vdp.annaValinnanVaihtoehdot(), vdp.annaValinnanVaihtoehtojenKohdeDialogit(), vdp.annaTriggerit()));
                                            }
                                            else {
                                                JOptionPane.showMessageDialog(null, "Valinnan jälkeen ei voi lisätä tekstipätkää, sillä valinta johtaa aina ulos nykyisestä dialogista.", "Dialogia ei voi lisätä", JOptionPane.INFORMATION_MESSAGE);
                                            }
                                        }
                                    });
                                    JMenuItem lisääValinta = new JMenuItem("Valinta");
                                    lisääValinta.addActionListener(listener -> {
                                        DefaultTreeModel model = (DefaultTreeModel)dialogipuu.getModel();
                                        DefaultMutableTreeNode löydettyNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
                                        if (löydettyNode != null) {
                                            if (löydettyNode.getLevel() == 1) {
                                                boolean onValinta = false;
                                                for (int i = 0; i < löydettyNode.getChildCount(); i++) {
                                                    if (löydettyNode.getChildAt(i) != null) {
                                                        if (löydettyNode.getChildAt(i).getChildCount() > 0) {
                                                            onValinta = true;
                                                            break;
                                                        }
                                                    }
                                                }
                                                if (onValinta) {
                                                    JOptionPane.showMessageDialog(null, "Dialogi päättyy jo valintaan. Yhdessä dialogipätkässä voi olla vain yksi (1) valinta.\nValinnan vaihtoehtojen täytyy johtaa aina seuraavaan dialogiin.", "Valinta on jo määritetty", JOptionPane.INFORMATION_MESSAGE);
                                                }
                                                else {
                                                    DialogiPuunAlkio lisättäväNode = new DialogiPuunAlkio("valinta");
                                                    lisättäväNode.asetaKuvakeTyyppi(DialogiKuvakeTyyppi.VALINTA);
                                                    model.insertNodeInto(lisättäväNode, (MutableTreeNode)löydettyNode, löydettyNode.getChildCount());
                                                    DialogiPuunAlkio lisättäväNodeVaihtoehto = new DialogiPuunAlkio("V: vaihtoehto");
                                                    lisättäväNodeVaihtoehto.asetaKuvakeTyyppi(DialogiKuvakeTyyppi.VAIHTOEHTO);
                                                    DialogiPuunAlkio lisättäväNodeVaihtoehtonKohde = new DialogiPuunAlkio("D: dialogi");
                                                    lisättäväNodeVaihtoehtonKohde.asetaKuvakeTyyppi(DialogiKuvakeTyyppi.VAIHTOEHDON_KOHDE);
                                                    model.insertNodeInto(lisättäväNodeVaihtoehto, lisättäväNode, 0);
                                                    model.insertNodeInto(lisättäväNodeVaihtoehtonKohde, lisättäväNode, 1);
                                                }
                                            }
                                        }
                                    });
                                    lisäysValikko.add(lisääDialogi);
                                    lisäysValikko.add(lisääValinta);

                                    JMenuItem nimeäUudelleen = new JMenuItem("Nimeä uudelleen");
                                    nimeäUudelleen.addActionListener(listener -> {
                                        NimenValintaIkkuna.luoNimenValintaIkkuna();
                                    });

                                    JMenuItem poista = new JMenuItem("Poista");
                                    poista.addActionListener(listener -> {
                                        DefaultTreeModel model = (DefaultTreeModel)dialogipuu.getModel();
                                        //DefaultMutableTreeNode foundNode = findNode(käytettävänDialoginTunniste);
                                        DefaultMutableTreeNode löydettyNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
                                        if (löydettyNode != null) {
                                            int valinta = CustomViestiIkkunat.PoistaDialogiPätkä.showDialog();
                                            if (valinta == JOptionPane.YES_OPTION) {
                                                editorinDialogiKartta.remove("" + löydettyNode);
                                                model.removeNodeFromParent(löydettyNode);
                                            }
                                        }
                                    });

                                    muokkausValikko.add(lisäysValikko);
                                    muokkausValikko.add(new JSeparator());
                                    muokkausValikko.add(nimeäUudelleen);
                                    muokkausValikko.add(poista);
                                    muokkausValikko.show(dialogipuu, e.getX(), e.getY());
                                }
                            break;

                            case 2:
                                if (valittuNode.getChildCount() <= 0) {
                                    muokkausValikko = new JPopupMenu();

                                    JMenu lisäysValikko = new JMenu("Lisää");
                                    JMenuItem lisääDialogi = new JMenuItem("Tekstipätkä");
                                    lisääDialogi.addActionListener(listener -> {
                                        DefaultTreeModel model = (DefaultTreeModel)dialogipuu.getModel();
                                        DefaultMutableTreeNode löydettyNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
                                        if (löydettyNode != null) {
                                            if (löydettyNode.getParent().getIndex(löydettyNode) < löydettyNode.getParent().getChildCount()-1 || löydettyNode.getParent().getChildAt(löydettyNode.getParent().getChildCount()-1).getChildCount() <= 0) {
                                                MutableTreeNode lisättäväNode = new DefaultMutableTreeNode("teksti");
                                                model.insertNodeInto(lisättäväNode, (MutableTreeNode)löydettyNode.getParent(), löydettyNode.getParent().getIndex(löydettyNode)+1);
                                                ArrayList<String> tekstiLista = new ArrayList<>();
                                                ArrayList<String> puhujaLista = new ArrayList<>();
                                                ArrayList<Icon> kuvaLista = new ArrayList<>();
                                                VuoropuheDialogiPätkä vdp = editorinDialogiKartta.get("" + löydettyNode.getParent());
                                                if (vdp != null) {
                                                    for (String s : vdp.annaTekstit()) {
                                                        tekstiLista.add(s);
                                                    }
                                                    tekstiLista.add(löydettyNode.getParent().getIndex(löydettyNode) +1, "" + lisättäväNode);
                                                    String[] uusiTekstiLista = tekstiLista.toArray(new String[tekstiLista.size()]);
                                                    for (String s : vdp.annaPuhujat()) {
                                                        puhujaLista.add(s);
                                                    }
                                                    puhujaLista.add(löydettyNode.getParent().getIndex(löydettyNode) +1, "puhuja");
                                                    String[] uusiPuhujaLista = puhujaLista.toArray(new String[puhujaLista.size()]);
                                                    for (Icon icon : vdp.annaKuvat()) {
                                                        kuvaLista.add(icon);
                                                    }
                                                    kuvaLista.add(löydettyNode.getParent().getIndex(löydettyNode) +1, null);
                                                    Icon[] uusiKuvaLista = kuvaLista.toArray(new Icon[kuvaLista.size()]);
                                                    vdp.lisääSivu();
                                                    editorinDialogiKartta.put(vdp.annaTunniste(), new VuoropuheDialogiPätkä(vdp.annaTunniste(), vdp.annaPituus(), uusiKuvaLista, uusiTekstiLista, uusiPuhujaLista, vdp.onkoValinta(), vdp.annaValinnanNimi(), vdp.annaValinnanOtsikko(), vdp.annaValinnanVaihtoehdot(), vdp.annaValinnanVaihtoehtojenKohdeDialogit(), vdp.annaTriggerit()));
                                                }
                                                else {
                                                    editorinDialogiKartta.put("" + löydettyNode.getParent(), new VuoropuheDialogiPätkä("" + lisättäväNode, 0, null, null, null, false, null, null, null, null, null));
                                                }
                                            }
                                            else {
                                                JOptionPane.showMessageDialog(null, "Valinnan jälkeen ei voi lisätä tekstipätkää, sillä valinta johtaa aina ulos nykyisestä dialogista.", "Dialogia ei voi lisätä", JOptionPane.INFORMATION_MESSAGE);
                                            }
                                        }
                                    });
                                    JMenuItem lisääValinta = new JMenuItem("Valinta");
                                    lisääValinta.addActionListener(listener -> {
                                        DefaultTreeModel model = (DefaultTreeModel)dialogipuu.getModel();
                                        DefaultMutableTreeNode löydettyNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
                                        if (löydettyNode != null) {
                                            if (löydettyNode.getLevel() == 2) {
                                                boolean onValinta = false;
                                                for (int i = 0; i < löydettyNode.getParent().getChildCount(); i++) {
                                                    if (löydettyNode.getParent().getChildAt(i) != null) {
                                                        if (löydettyNode.getParent().getChildAt(i).getChildCount() > 0) {
                                                            onValinta = true;
                                                            break;
                                                        }
                                                    }
                                                }
                                                if (onValinta) {
                                                    JOptionPane.showMessageDialog(null, "Dialogi päättyy jo valintaan. Yhdessä dialogipätkässä voi olla vain yksi (1) valinta.\nValinnan vaihtoehtojen täytyy johtaa aina seuraavaan dialogiin.", "Valinta on jo määritetty", JOptionPane.INFORMATION_MESSAGE);
                                                }
                                                else {
                                                    DialogiPuunAlkio lisättäväNode = new DialogiPuunAlkio("valinta");
                                                    lisättäväNode.asetaKuvakeTyyppi(DialogiKuvakeTyyppi.VALINTA);
                                                    model.insertNodeInto(lisättäväNode, (MutableTreeNode)löydettyNode.getParent(), löydettyNode.getParent().getChildCount());
                                                    DialogiPuunAlkio lisättäväNodeVaihtoehto = new DialogiPuunAlkio("V: vaihtoehto");
                                                    lisättäväNodeVaihtoehto.asetaKuvakeTyyppi(DialogiKuvakeTyyppi.VAIHTOEHTO);
                                                    DialogiPuunAlkio lisättäväNodeVaihtoehtonKohde = new DialogiPuunAlkio("D: dialogi");
                                                    lisättäväNodeVaihtoehtonKohde.asetaKuvakeTyyppi(DialogiKuvakeTyyppi.VAIHTOEHDON_KOHDE);
                                                    model.insertNodeInto(lisättäväNodeVaihtoehto, lisättäväNode, 0);
                                                    model.insertNodeInto(lisättäväNodeVaihtoehtonKohde, lisättäväNode, 1);
                                                }
                                            }
                                        }
                                    });
                                    lisäysValikko.add(lisääDialogi);
                                    lisäysValikko.add(lisääValinta);

                                    JMenuItem poista = new JMenuItem("Poista");
                                    poista.addActionListener(listener -> {
                                        DefaultTreeModel model = (DefaultTreeModel)dialogipuu.getModel();
                                        //DefaultMutableTreeNode foundNode = findNode(käytettävänDialoginTunniste);
                                        DefaultMutableTreeNode löydettyNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
                                        if (löydettyNode != null) {
                                            ArrayList<String> tekstiLista = new ArrayList<>();
                                            ArrayList<String> puhujaLista = new ArrayList<>();
                                            ArrayList<Icon> kuvaLista = new ArrayList<>();
                                            VuoropuheDialogiPätkä vdp = editorinDialogiKartta.get("" + löydettyNode.getParent());
                                            for (String s : vdp.annaTekstit()) {
                                                tekstiLista.add(s);
                                            }
                                            tekstiLista.remove(löydettyNode.getParent().getIndex(löydettyNode));
                                            String[] uusiTekstiLista = tekstiLista.toArray(new String[tekstiLista.size()]);
                                            for (String s : vdp.annaPuhujat()) {
                                                puhujaLista.add(s);
                                            }
                                            puhujaLista.remove(löydettyNode.getParent().getIndex(löydettyNode));
                                            String[] uusiPuhujaLista = puhujaLista.toArray(new String[puhujaLista.size()]);
                                            for (Icon icon : vdp.annaKuvat()) {
                                                kuvaLista.add(icon);
                                            }
                                            kuvaLista.remove(löydettyNode.getParent().getIndex(löydettyNode));
                                            Icon[] uusiKuvaLista = kuvaLista.toArray(new Icon[kuvaLista.size()]);
                                            vdp.poistaSivu();
                                            editorinDialogiKartta.put(vdp.annaTunniste(), new VuoropuheDialogiPätkä(vdp.annaTunniste(), vdp.annaPituus(), uusiKuvaLista, uusiTekstiLista, uusiPuhujaLista, vdp.onkoValinta(), vdp.annaValinnanNimi(), vdp.annaValinnanOtsikko(), vdp.annaValinnanVaihtoehdot(), vdp.annaValinnanVaihtoehtojenKohdeDialogit(), vdp.annaTriggerit()));
                                            model.removeNodeFromParent(löydettyNode);
                                        }
                                    });

                                    muokkausValikko.add(lisäysValikko);
                                    muokkausValikko.add(new JSeparator());
                                    muokkausValikko.add(poista);
                                    muokkausValikko.show(dialogipuu, e.getX(), e.getY());
                                }
                                else {
                                    muokkausValikko = new JPopupMenu();
                                    JMenuItem lisääVaihtoehto = new JMenuItem("Lisää vaihtoehto");
                                    lisääVaihtoehto.addActionListener(listener -> {
                                        DefaultTreeModel model = (DefaultTreeModel)dialogipuu.getModel();
                                        DefaultMutableTreeNode löydettyNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
                                        if (löydettyNode != null) {
                                            if (löydettyNode.getLevel() == 2) {
                                                DialogiPuunAlkio lisättäväNodeVaihtoehto = new DialogiPuunAlkio("V: vaihtoehto");
                                                lisättäväNodeVaihtoehto.asetaKuvakeTyyppi(DialogiKuvakeTyyppi.VAIHTOEHTO);
                                                DialogiPuunAlkio lisättäväNodeVaihtoehtonKohde = new DialogiPuunAlkio("D: dialogi");
                                                lisättäväNodeVaihtoehtonKohde.asetaKuvakeTyyppi(DialogiKuvakeTyyppi.VAIHTOEHDON_KOHDE);
                                                model.insertNodeInto(lisättäväNodeVaihtoehto, löydettyNode, 0);
                                                model.insertNodeInto(lisättäväNodeVaihtoehtonKohde, löydettyNode, 1);
                                            }
                                        }
                                    });
                                    muokkausValikko.add(lisääVaihtoehto);
                                    muokkausValikko.show(dialogipuu, e.getX(), e.getY());
                                }
                            break;

                            case 3:
                                muokkausValikko = new JPopupMenu();

                                if (valittuNode.toString().startsWith("V:")) {
                                    JMenu lisäysValikko = new JMenu("Lisää");
                                    JMenuItem lisääTriggeri = new JMenuItem("Triggeri");
                                    lisääTriggeri.addActionListener(listener -> {
                                        DefaultTreeModel model = (DefaultTreeModel)dialogipuu.getModel();
                                        DefaultMutableTreeNode löydettyNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
                                        if (löydettyNode != null) {
                                            MutableTreeNode lisättäväNode = new DefaultMutableTreeNode("T: triggeri");
                                            model.insertNodeInto(lisättäväNode, (MutableTreeNode)löydettyNode.getParent(), löydettyNode.getParent().getIndex(löydettyNode)+1);
                                        }
                                    });
                                    JMenuItem lisääKohde = new JMenuItem("Kohdedialogi");
                                    lisääKohde.addActionListener(listener -> {
                                        DefaultTreeModel model = (DefaultTreeModel)dialogipuu.getModel();
                                        DefaultMutableTreeNode löydettyNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
                                        if (löydettyNode != null) {
                                            if (löydettyNode.getParent().getIndex(löydettyNode) < löydettyNode.getParent().getChildCount()-1) {
                                                MutableTreeNode lisättäväNode = new DefaultMutableTreeNode("D: dialogi");
                                                model.insertNodeInto(lisättäväNode, (MutableTreeNode)löydettyNode.getParent(), löydettyNode.getParent().getIndex(löydettyNode)+1);
                                            }
                                            else {
                                                JOptionPane.showMessageDialog(null, "Valinnan jälkeen ei voi lisätä tekstipätkää, sillä valinta johtaa aina ulos nykyisestä dialogista.", "Dialogia ei voi lisätä", JOptionPane.INFORMATION_MESSAGE);
                                            }
                                        }
                                    });

                                    JMenuItem poista = new JMenuItem("Poista");
                                    poista.addActionListener(listener -> {
                                        DefaultTreeModel model = (DefaultTreeModel)dialogipuu.getModel();
                                        //DefaultMutableTreeNode foundNode = findNode(käytettävänDialoginTunniste);
                                        DefaultMutableTreeNode löydettyNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
                                        if (löydettyNode != null) {
                                            DefaultMutableTreeNode parent = (DefaultMutableTreeNode)löydettyNode.getParent();
                                            if (parent.getChildCount() <= 1) {
                                                model.removeNodeFromParent(parent);
                                            }
                                            else {
                                                int poistettavanNodenAlkuIndeksi = parent.getIndex(löydettyNode) +1;
                                                int poistettavanNodenLoppuIndeksi = parent.getChildCount();
                                                model.removeNodeFromParent(löydettyNode);
                                                System.out.println("poista " + poistettavanNodenAlkuIndeksi + " - " + poistettavanNodenLoppuIndeksi);
                                                for (int i = poistettavanNodenAlkuIndeksi; i < poistettavanNodenLoppuIndeksi; i++) {
                                                    if (parent.getChildCount() > poistettavanNodenAlkuIndeksi) {
                                                        System.out.println("" + poistettavanNodenAlkuIndeksi + "; " + parent.getChildAt(poistettavanNodenAlkuIndeksi));
                                                        if (parent.getChildAt(poistettavanNodenAlkuIndeksi).toString().startsWith("V:")) {
                                                            break;
                                                        }
                                                        else {
                                                            model.removeNodeFromParent((DefaultMutableTreeNode)parent.getChildAt(poistettavanNodenAlkuIndeksi));
                                                        }
                                                    }
                                                }
                                            }
                                            int löydetytVaihtoehdot = 0;
                                            for (int i = 0; i < parent.getChildCount(); i++) {
                                                if (parent.getChildAt(i).toString().startsWith("V:")) {
                                                    löydetytVaihtoehdot++;
                                                }
                                            }
                                            if (löydetytVaihtoehdot <= 0) {
                                                model.removeNodeFromParent(parent);
                                            }
                                        }
                                    });
                                    
                                    lisäysValikko.add(lisääTriggeri);
                                    //lisäysValikko.add(lisääKohde);
                                    muokkausValikko.add(lisäysValikko);
                                    muokkausValikko.add(new JSeparator());
                                    muokkausValikko.add(poista);
                                }
                                else if (valittuNode.toString().startsWith("T:")) {
                                    JMenuItem poista = new JMenuItem("Poista");
                                    poista.addActionListener(listener -> {
                                        DefaultTreeModel model = (DefaultTreeModel)dialogipuu.getModel();
                                        //DefaultMutableTreeNode foundNode = findNode(käytettävänDialoginTunniste);
                                        DefaultMutableTreeNode löydettyNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
                                        if (löydettyNode != null) {
                                            DefaultMutableTreeNode parent = (DefaultMutableTreeNode)löydettyNode.getParent();
                                            if (parent.getChildCount() <= 1) {
                                                model.removeNodeFromParent(parent);
                                            }
                                            else {
                                                model.removeNodeFromParent(löydettyNode);
                                            }
                                            int löydetytVaihtoehdot = 0;
                                            for (int i = 0; i < parent.getChildCount(); i++) {
                                                if (parent.getChildAt(i).toString().startsWith("V:")) {
                                                    löydetytVaihtoehdot++;
                                                }
                                            }
                                            if (löydetytVaihtoehdot <= 0) {
                                                model.removeNodeFromParent(parent);
                                            }
                                        }
                                    });
                                    muokkausValikko.add(poista);
                                }

                                muokkausValikko.show(dialogipuu, e.getX(), e.getY());
                            break;

                            default:
                            break;
                        }
                    }
                    
                }
            }
        });
        //DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) dialogipuu.getCellRenderer();
        //renderer.setLeafIcon(new ImageIcon("tiedostot/kuvat/menu/gui/puhedialogi.png"));
        dialogipuu.setCellRenderer(new DefaultTreeCellRenderer() {
            //private Icon loadIcon = UIManager.getIcon("OptionPane.errorIcon");
            //private Icon saveIcon = UIManager.getIcon("OptionPane.informationIcon");
            private Icon dialogiKuvake = new ImageIcon("tiedostot/kuvat/menu/gui/puhedialogi.png");
            private Icon dialogiPätkäKuvake = new ImageIcon("tiedostot/kuvat/menu/gui/kansio.png");
            private Icon dialogiValintaKuvake = new ImageIcon("tiedostot/kuvat/menu/gui/dialogieditori_valinta.png");
            private Icon dialogiValintaOtsikkoKuvake = new ImageIcon("tiedostot/kuvat/menu/gui/dialogieditori_otsikko.png");
            private Icon dialogiVaihtoehtoKuvake = new ImageIcon("tiedostot/kuvat/menu/gui/dialogieditori_vaihtoehto.png");
            private Icon dialogiVaihtoehdonKohdeKuvake = new ImageIcon("tiedostot/kuvat/menu/gui/dialogieditori_vaihtoehdonkohde.png");
            private Icon dialogiTriggeriKuvake = new ImageIcon("tiedostot/kuvat/menu/gui/dialogieditori_triggeri.png");
      
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean isLeaf, int row, boolean focused) {
                Component c = super.getTreeCellRendererComponent(tree, value, selected, expanded, isLeaf, row, focused);
                // DefaultMutableTreeNode node1 = (DefaultMutableTreeNode) value;
                // DialogiPuunAlkio node = (DialogiPuunAlkio) node1;
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                int nodeIndex = -1;
                int nodeLevel = node.getLevel();
                if (node.getParent() != null) {
                    nodeIndex = node.getParent().getIndex(node);
                }
                VuoropuheDialogiPätkä vdp = editorinDialogiKartta.get("" + node);
                // if (node.kuvakeTyyppi != null) {
                //     switch (node.kuvakeTyyppi) {
                //         case VAIHTOEHDON_KOHDE:
                //             setIcon(dialogiVaihtoehdonKohdeKuvake);
                //         break;
                //         case VAIHTOEHTO:
                //             setIcon(dialogiVaihtoehtoKuvake);
                //         break;
                //         case VALINTA:
                //             setIcon(dialogiValintaKuvake);
                //         break;
                //         case VUOROPUHE:
                //             setIcon(dialogiKuvake);
                //         break;
                //         default: break;
                //     }
                // }
                if (tree.getModel().getRoot().equals(node)) {
                    setIcon(dialogiPätkäKuvake);
                }
                else if (nodeLevel == 1) {
                    if (node.getChildCount() > 0) {
                        setIcon(dialogiPätkäKuvake);
                    }
                }
                else if (nodeLevel == 2) {
                    if (node.getChildCount() > 0) {
                        setIcon(dialogiValintaKuvake);
                    }
                    else {
                        setIcon(dialogiKuvake);
                    }
                }
                
                else if (nodeLevel == 3) {
                    if (node.toString().startsWith("V:")) {
                        setIcon(dialogiVaihtoehtoKuvake);
                    }
                    else if (node.toString().startsWith("D:")) {
                        setIcon(dialogiVaihtoehdonKohdeKuvake);
                    }
                    else if (node.toString().startsWith("T:")) {
                        setIcon(dialogiTriggeriKuvake);
                    }
                    else if (node.toString().startsWith("O:")) {
                        setIcon(dialogiValintaOtsikkoKuvake);
                    }
                    else {
                        setIcon(null);
                    }
                }
                return c;
            }
        });

        

        JPanel vasenPaneeli = new JPanel(new BorderLayout());
        vasenPaneeli.setName("vasenpaneeli");
        vasenPaneeli.setBounds(0, 0, 200, 500);
        vasenPaneeli.add(dialogipuu, BorderLayout.WEST);
        JScrollPane vasenScrollPaneeli = new JScrollPane(vasenPaneeli);
        vasenScrollPaneeli.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        vasenScrollPaneeli.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //vasenScrollPaneeli.setMaximumSize(new Dimension(150, 300));
        //vasenScrollPaneeli.setMinimumSize(new Dimension(150, 300));
        vasenScrollPaneeli.setPreferredSize(new Dimension(200, 300));
        //vasenScrollPaneeli.setBounds(0, 0, 200, 500);

        JPanel oikeaPaneeli = new JPanel(new BorderLayout());
        oikeaPaneeli.setName("oikeapaneeli");
        oikeaPaneeli.add(new JButton(""));

        JLabel puhujanNimiLabel = new JLabel("Puhujan nimi");
        puhujanNimiTekstikenttä = new JTextField();
        puhujanNimiTekstikenttä.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                esikatseluPuhujanNimiLabel.setText("<html><p>" + puhujanNimiTekstikenttä.getText() + "</p></html>");
                DefaultMutableTreeNode valittuNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
                if (valittuNode.getLevel() == 2 && valittuNode.getChildCount() <= 0) {
                    String dialogiPätkänNimi = "" + valittuNode.getParent();
                    VuoropuheDialogiPätkä vdp = editorinDialogiKartta.get("" + dialogiPätkänNimi);
                    String[] puhujanNimet = vdp.annaPuhujat();
                    puhujanNimet[valittuNode.getParent().getIndex(valittuNode)] = puhujanNimiTekstikenttä.getText();
                    editorinDialogiKartta.put(dialogiPätkänNimi, new VuoropuheDialogiPätkä(dialogiPätkänNimi, vdp.annaPituus(), vdp.annaKuvat(), vdp.annaTekstit(), puhujanNimet));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                esikatseluPuhujanNimiLabel.setText("<html><p>" + puhujanNimiTekstikenttä.getText() + "</p></html>");
                DefaultMutableTreeNode valittuNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
                if (valittuNode.getLevel() == 2 && valittuNode.getChildCount() <= 0) {
                    String dialogiPätkänNimi = "" + valittuNode.getParent();
                    VuoropuheDialogiPätkä vdp = editorinDialogiKartta.get("" + dialogiPätkänNimi);
                    String[] puhujanNimet = vdp.annaPuhujat();
                    puhujanNimet[valittuNode.getParent().getIndex(valittuNode)] = puhujanNimiTekstikenttä.getText();
                    editorinDialogiKartta.put(dialogiPätkänNimi, new VuoropuheDialogiPätkä(dialogiPätkänNimi, vdp.annaPituus(), vdp.annaKuvat(), vdp.annaTekstit(), puhujanNimet));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                esikatseluPuhujanNimiLabel.setText("<html><p>" + puhujanNimiTekstikenttä.getText() + "</p></html>");
                DefaultMutableTreeNode valittuNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
                if (valittuNode.getLevel() == 2 && valittuNode.getChildCount() <= 0) {
                    String dialogiPätkänNimi = "" + valittuNode.getParent();
                    VuoropuheDialogiPätkä vdp = editorinDialogiKartta.get("" + dialogiPätkänNimi);
                    String[] puhujanNimet = vdp.annaPuhujat();
                    puhujanNimet[valittuNode.getParent().getIndex(valittuNode)] = puhujanNimiTekstikenttä.getText();
                    editorinDialogiKartta.put(dialogiPätkänNimi, new VuoropuheDialogiPätkä(dialogiPätkänNimi, vdp.annaPituus(), vdp.annaKuvat(), vdp.annaTekstit(), puhujanNimet));
                }
            }
        });
        JLabel puhujanKuvaLabel = new JLabel("Puhujan kuva");
        puhujanKuvaNappi = new JButton("Valitse kuva");
        puhujanKuvaNappi.setMinimumSize(new Dimension(400, 100));
        puhujanKuvaNappi.setMaximumSize(new Dimension(400, 100));
        puhujanKuvaNappi.addActionListener(e -> {
            FileNameExtensionFilter suodatin = new FileNameExtensionFilter("Kuvatiedostot (*.jpg *.png *.gif)", "jpg", "png", "gif");
            JFileChooser tiedostoValitsin = new JFileChooser("tiedostot/kuvat");
            tiedostoValitsin.setFileFilter(suodatin);
            int valinta = tiedostoValitsin.showOpenDialog(ikkuna);
            if (valinta == JFileChooser.APPROVE_OPTION) {
                File valittuTiedosto = tiedostoValitsin.getSelectedFile();
                try {
                    BufferedImage img = ImageIO.read(valittuTiedosto);
                    if (img.getWidth() != 140 || img.getHeight() != 110) {
                        int valitseKuvaVirheestäHuolimatta = JOptionPane.showConfirmDialog(null, "Valitun kuvan koko on " + img.getWidth() + " x " + img.getHeight() + " pikseliä.\nDialogikuvat toimivat parhaiten, kun niiden koko on 140 x 110 pikseliä.\nVääränkokoisten kuvien (varsinkin liian suurten) käyttäminen voi saada aikaan ei-toivottuja tuloksia.\n\nHaluatko valita kuvan silti?", "Epäoptimaalinen kuvan koko", JOptionPane.WARNING_MESSAGE);
                        if (valitseKuvaVirheestäHuolimatta == JOptionPane.YES_OPTION) {
                            puhujanKuvaNappi.setIcon(new ImageIcon(img));
                            esikatseluPuhujanKuvaLabel.setIcon(puhujanKuvaNappi.getIcon());
                        }
                    }
                    else {
                        puhujanKuvaNappi.setIcon(new ImageIcon(img));
                        esikatseluPuhujanKuvaLabel.setIcon(puhujanKuvaNappi.getIcon());
                    }
                }
                catch (IOException ioe) {
                    System.out.println("virhe tarkastellessa kuvatiedostoa!");
                    ioe.printStackTrace();
                }
            }
        });

        JLabel puhujanTekstiLabel = new JLabel("Puheteksti");
        tekstiMuokkausKenttä = new JTextArea();
        tekstiMuokkausKenttä.setBorder(BorderFactory.createLineBorder(Color.black, 1, false));
        tekstiMuokkausKenttä.setBounds(0, 0, 300, 100);
        //tekstiMuokkausKenttä.setMaximumSize(new Dimension(300, 100));
        tekstiMuokkausKenttä.getDocument().addDocumentListener(new DocumentListener() {
            
            @Override
            public void insertUpdate(DocumentEvent e) {
                esikatseluPuhujanTekstiLabel.setText("<html><p>" + tekstiMuokkausKenttä.getText() + "</p></html>");
                DefaultMutableTreeNode valittuNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
                if (valittuNode.getLevel() == 2 && valittuNode.getChildCount() <= 0) {
                    String dialogiPätkänNimi = "" + valittuNode.getParent();
                    VuoropuheDialogiPätkä vdp = editorinDialogiKartta.get("" + dialogiPätkänNimi);
                    String[] puhujanTekstit = vdp.annaTekstit();
                    puhujanTekstit[valittuNode.getParent().getIndex(valittuNode)] = tekstiMuokkausKenttä.getText();
                    editorinDialogiKartta.put(dialogiPätkänNimi, new VuoropuheDialogiPätkä(dialogiPätkänNimi, vdp.annaPituus(), vdp.annaKuvat(), puhujanTekstit, vdp.annaPuhujat()));
                    valittuNode.setUserObject(tekstiMuokkausKenttä.getText());
                    ((DefaultTreeModel) dialogipuu.getModel()).nodeChanged(valittuNode);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                esikatseluPuhujanTekstiLabel.setText("<html><p>" + tekstiMuokkausKenttä.getText() + "</p></html>");
                DefaultMutableTreeNode valittuNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
                if (valittuNode.getLevel() == 2 && valittuNode.getChildCount() <= 0) {
                    String dialogiPätkänNimi = "" + valittuNode.getParent();
                    VuoropuheDialogiPätkä vdp = editorinDialogiKartta.get("" + dialogiPätkänNimi);
                    String[] puhujanTekstit = vdp.annaTekstit();
                    puhujanTekstit[valittuNode.getParent().getIndex(valittuNode)] = tekstiMuokkausKenttä.getText();
                    editorinDialogiKartta.put(dialogiPätkänNimi, new VuoropuheDialogiPätkä(dialogiPätkänNimi, vdp.annaPituus(), vdp.annaKuvat(), puhujanTekstit, vdp.annaPuhujat()));
                    valittuNode.setUserObject(tekstiMuokkausKenttä.getText());
                    ((DefaultTreeModel) dialogipuu.getModel()).nodeChanged(valittuNode);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                esikatseluPuhujanTekstiLabel.setText("<html><p>" + tekstiMuokkausKenttä.getText() + "</p></html>");
                DefaultMutableTreeNode valittuNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
                if (valittuNode.getLevel() == 2 && valittuNode.getChildCount() <= 0) {
                    String dialogiPätkänNimi = "" + valittuNode.getParent();
                    VuoropuheDialogiPätkä vdp = editorinDialogiKartta.get("" + dialogiPätkänNimi);
                    String[] puhujanTekstit = vdp.annaTekstit();
                    puhujanTekstit[valittuNode.getParent().getIndex(valittuNode)] = tekstiMuokkausKenttä.getText();
                    editorinDialogiKartta.put(dialogiPätkänNimi, new VuoropuheDialogiPätkä(dialogiPätkänNimi, vdp.annaPituus(), vdp.annaKuvat(), puhujanTekstit, vdp.annaPuhujat()));
                    valittuNode.setUserObject(tekstiMuokkausKenttä.getText());
                    ((DefaultTreeModel) dialogipuu.getModel()).nodeChanged(valittuNode);
                }
            }
        });

        // JPanel muokkausPaneeliYlempi = new JPanel(new SpringLayout());
        // muokkausPaneeliYlempi.add(puhujanNimiLabel);
        // muokkausPaneeliYlempi.add(puhujanNimiTekstikenttä);
        // muokkausPaneeliYlempi.add(puhujanKuvaLabel);
        // muokkausPaneeliYlempi.add(puhujanKuvaNappi);
        // muokkausPaneeliYlempi.add(puhujanTekstiLabel, BorderLayout.NORTH);
        // muokkausPaneeliYlempi.add(tekstiMuokkausKenttä, BorderLayout.CENTER);
        // SpringUtilities.makeCompactGrid(muokkausPaneeliYlempi, 3, 2, 6, 6, 6, 6);
        // muokkausPaneeliYlempi.setLayout(new BoxLayout(muokkausPaneeliYlempi, BoxLayout.PAGE_AXIS));
        // muokkausPaneeliYlempi.revalidate();
        // muokkausPaneeliYlempi.repaint();

        JPanel muokkausPaneeliYlempi = new JPanel();
        GroupLayout layout = new GroupLayout(muokkausPaneeliYlempi);
        muokkausPaneeliYlempi.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(puhujanNimiLabel)
                    .addComponent(puhujanKuvaLabel)
                    .addComponent(puhujanTekstiLabel)
                    )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(puhujanNimiTekstikenttä)
                    .addComponent(puhujanKuvaNappi)
                    .addComponent(tekstiMuokkausKenttä)
                    )
        );
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(puhujanNimiLabel)
                    .addComponent(puhujanNimiTekstikenttä)
                    )  
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(puhujanKuvaLabel)
                    .addComponent(puhujanKuvaNappi)
                    )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(puhujanTekstiLabel)
                    .addComponent(tekstiMuokkausKenttä)
                    )
        );

        JPanel muokkausPaneeliAlempi = new JPanel(new BorderLayout());
        muokkausPaneeliAlempi.setBounds(0, 0, 300, 100);

        dialogiMuokkausPaneeli = new JPanel(new BorderLayout());
        dialogiMuokkausPaneeli.setName("tekstiPaneeli");
        dialogiMuokkausPaneeli.add(muokkausPaneeliYlempi, BorderLayout.NORTH);
        dialogiMuokkausPaneeli.add(muokkausPaneeliAlempi, BorderLayout.SOUTH);
        dialogiMuokkausPaneeli.setVisible(false);

        JLabel todoLabel = new JLabel("TODO: Valinnoille joku muokkauskäyttöliittymä");
        valintaMuokkausPaneeli = new JPanel(new GridBagLayout());
        valintaMuokkausPaneeli.add(todoLabel);
        valintaMuokkausPaneeli.setVisible(false);

        esikatseluPuhujanKuvaLabel = new JLabel();
        JPanel puhujanKuvaPaneeli = new JPanel(new BorderLayout());
        puhujanKuvaPaneeli.setPreferredSize(new Dimension(140, 110));
        puhujanKuvaPaneeli.setBorder(BorderFactory.createLineBorder(Color.black, 1, false));
        puhujanKuvaPaneeli.add(esikatseluPuhujanKuvaLabel);

        esikatseluPuhujanNimiLabel = new JLabel();
        JPanel puhujanNimiPaneeli = new JPanel(new BorderLayout());
        puhujanNimiPaneeli.setPreferredSize(new Dimension(400, 20));
        puhujanNimiPaneeli.setBorder(BorderFactory.createLineBorder(Color.black, 1, false));
        puhujanNimiPaneeli.add(esikatseluPuhujanNimiLabel);

        esikatseluPuhujanTekstiLabel = new JLabel();
        esikatseluPuhujanTekstiLabel.setAlignmentY(JLabel.TOP_ALIGNMENT);
        esikatseluPuhujanTekstiLabel.setVerticalAlignment(SwingConstants.TOP);
        JPanel puhujanTekstiPaneeli = new JPanel(new BorderLayout());
        puhujanTekstiPaneeli.setBounds(0, 0, 400, 80);
        puhujanTekstiPaneeli.setBorder(BorderFactory.createLineBorder(Color.black, 1, false));
        puhujanTekstiPaneeli.add(esikatseluPuhujanTekstiLabel, BorderLayout.NORTH);

        JPanel esikatseluPaneeli = new JPanel(new BorderLayout());
        esikatseluPaneeli.add(puhujanKuvaPaneeli, BorderLayout.WEST);
        esikatseluPaneeli.add(puhujanNimiPaneeli, BorderLayout.NORTH);
        esikatseluPaneeli.add(puhujanTekstiPaneeli, BorderLayout.CENTER);

        keskiPaneeli = new JPanel(new BorderLayout());
        keskiPaneeli.setMinimumSize(new Dimension(500, 500));
        keskiPaneeli.setMaximumSize(new Dimension(500, 500));
        keskiPaneeli.setBounds(0, 0, 300, 300);
        keskiPaneeli.add(dialogiMuokkausPaneeli, BorderLayout.NORTH);
        keskiPaneeli.add(esikatseluPaneeli, BorderLayout.SOUTH);


        JButton okNappi = new JButton("OK");
        okNappi.setBounds(0, 0, 20, 20);
        okNappi.addActionListener(e -> {
            VuoropuheDialogit.vuoropuheDialogiKartta = editorinDialogiKartta;
            ikkuna.dispose();
        });

        JButton cancelNappi = new JButton("Peruuta");
        cancelNappi.setBounds(60, 0, 20, 20);
        cancelNappi.addActionListener(e -> {
            ikkuna.dispose();
        });

        JPanel alaPaneeli = new JPanel();
        alaPaneeli.setName("alaPaneeli");
        alaPaneeli.add(okNappi);
        alaPaneeli.add(cancelNappi);

        JPanel pääPaneeli = new JPanel(new BorderLayout());
        pääPaneeli.add(vasenScrollPaneeli, BorderLayout.WEST);
        pääPaneeli.add(oikeaPaneeli, BorderLayout.EAST);
        pääPaneeli.add(keskiPaneeli, BorderLayout.CENTER);
        pääPaneeli.add(alaPaneeli, BorderLayout.SOUTH);

        ikkuna.add(yläPalkki, BorderLayout.NORTH);
        ikkuna.add(pääPaneeli, BorderLayout.CENTER);
    }

    static void nimeäUudelleenDialogiPätkä(String uusiNimi) {
        DefaultTreeModel model = (DefaultTreeModel)dialogipuu.getModel();
        DefaultMutableTreeNode löydettyNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
        if (löydettyNode != null) {
            VuoropuheDialogiPätkä vdp = editorinDialogiKartta.get("" + löydettyNode);
            editorinDialogiKartta.remove("" + löydettyNode);
            editorinDialogiKartta.put(uusiNimi, new VuoropuheDialogiPätkä(uusiNimi, vdp.annaPituus(), vdp.annaKuvat(), vdp.annaTekstit(), vdp.annaPuhujat(), vdp.onkoValinta(), vdp.annaValinnanNimi(), vdp.annaValinnanOtsikko(), vdp.annaValinnanVaihtoehdot(), vdp.annaValinnanVaihtoehtojenKohdeDialogit(), vdp.annaTriggerit()));
            editorinDialogiKartta.remove("" + löydettyNode);
            löydettyNode.setUserObject(uusiNimi);
            model.nodeChanged(löydettyNode);
        }
    }

    enum DialogiKuvakeTyyppi {
        VUOROPUHE,
        VALINTA,
        VAIHTOEHTO,
        TRIGGERI,
        VAIHTOEHDON_KOHDE;
    }

    private static final DefaultMutableTreeNode findNode(String searchString) {

        List<DefaultMutableTreeNode> searchNodes = getSearchNodes((DefaultMutableTreeNode)dialogipuu.getModel().getRoot());
        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)dialogipuu.getLastSelectedPathComponent();

        DefaultMutableTreeNode foundNode = null;
        int bookmark = -1;

        if (currentNode != null) {
            for (int index = 0; index < searchNodes.size(); index++) {
                if (searchNodes.get(index) == currentNode) {
                    bookmark = index;
                    break;
                }
            }
        }

        for (int index = bookmark + 1; index < searchNodes.size(); index++) {    
            if (searchNodes.get(index).toString().toLowerCase().contains(searchString.toLowerCase())) {
                foundNode = searchNodes.get(index);
                break;
            }
        }

        if (foundNode == null) {
            for (int index = 0; index <= bookmark; index++) {    
                if (searchNodes.get(index).toString().toLowerCase().contains(searchString.toLowerCase())) {
                    foundNode = searchNodes.get(index);
                    break;
                }
            }
        }
        return foundNode;
    }

    private static final List<DefaultMutableTreeNode> getSearchNodes(DefaultMutableTreeNode root) {
        List<DefaultMutableTreeNode> searchNodes = new ArrayList<DefaultMutableTreeNode>();

        Enumeration<?> e = root.preorderEnumeration();
        while (e.hasMoreElements()) {
            searchNodes.add((DefaultMutableTreeNode)e.nextElement());
        }
        return searchNodes;
    }

    private static class DialogiPuunAlkio extends DefaultMutableTreeNode {
        DialogiKuvakeTyyppi kuvakeTyyppi;

        public DialogiPuunAlkio(Object o) {
            super(o);
        }

        public void asetaKuvakeTyyppi(DialogiKuvakeTyyppi kuvakeTyyppi) {
            this.kuvakeTyyppi = kuvakeTyyppi;
        }
    }
}
