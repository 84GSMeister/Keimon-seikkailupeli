package keimo.HuoneEditori.DialogiEditori;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import javax.swing.tree.TreePath;

import keimo.HuoneEditori.HuoneEditoriIkkuna;
import keimo.HuoneEditori.TavoiteEditori.TavoiteLista;
import keimo.Ikkunat.CustomViestiIkkunat;
import keimo.Utility.Downloaded.SpringUtilities;
import keimo.keimoEngine.toiminnot.Dialogit;

public class DialogiEditoriIkkuna {

    static JFrame ikkuna;

    static JTextArea tekstiMuokkausKenttä;
    static String käytettävänDialoginTunniste;
    static JTree dialogipuu;
    static HashMap<String, VuoropuheDialogiPätkä> editorinDialogiKartta = new HashMap<>();

    static JTextField puhujanNimiTekstikenttä;
    static JButton puhujanKuvaNappi;
    static JLabel esikatseluPuhujanKuvaLabel, esikatseluPuhujanTekstiLabel, esikatseluPuhujanNimiLabel;
    static JPanel keskiPaneeli, dialogiMuokkausPaneeli, valintaMuokkausPaneeli, esikatseluPaneeli;

    static JTextField valinnanTunnisteTekstikenttä, valinnanOtsikkoTekstikenttä, valinnanVaihtoehtoTekstikenttä, valinnanVaihtoehdonKohdeTekstikenttä, valinnanTriggeriTekstikenttä;
    static JPanel valinnanTunnistePaneeli, valinnanOtsikkoPaneeli, valinnanVaihtoehtoPaneeli, valinnanVaihtoehdonKohdePaneeli, valinnanTriggeriPaneeli;
    static JComboBox<Object> valinnanVaihtoehdonKohdeValinta, valinnanTriggeriValinta;
    static JPanel esikatseluValinnanVaihtoehtoPaneli;
    static JLabel esikatseluValinnanOtsikkoLabel;
    static JPanel esikatseluDialogiPaneeli, esikatseluValintaPaneeli;

    private static void uudelleenAlustaDialogiKartta() {
        //List<VuoropuheDialogiPätkä> editorinVdpt = VuoropuheDialogit.vuoropuheDialogiKartta.values().stream().toList();
        List<VuoropuheDialogiPätkä> editorinVdpt = Dialogit.PitkätDialogit.vuoropuheDialogiKartta.values().stream().toList();
        editorinDialogiKartta.clear();
        for (VuoropuheDialogiPätkä vdp : editorinVdpt) {
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
    }
    
    public static void luoDialogiEditoriIkkuna() {
        
        uudelleenAlustaDialogiKartta();

        ikkuna = new JFrame("Dialogieditori v0.2");
        ikkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja_og.png").getImage());
        ikkuna.setBounds(0, 0, 800, 600);
        ikkuna.setLocationRelativeTo(HuoneEditoriIkkuna.ikkuna);
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
            DefaultMutableTreeNode dialogiPuunKansio = new DefaultMutableTreeNode(dialoginNimi);
            for (String s : editorinDialogiKartta.get(dialoginNimi).annaTekstit()) {
                tarinaTekstiPuussa += s;
                tarinaTekstiPuussa += "\n\n";
                DefaultMutableTreeNode dialogiPuunItemi = new DefaultMutableTreeNode(tarinaTekstiPuussa);
                dialogiPuunKansio.add(dialogiPuunItemi);
                tarinaTekstiPuussa = "";
            }
            if (vdp.onkoValinta()) {
                DefaultMutableTreeNode dialogiPuunValintaKansio = new DefaultMutableTreeNode(vdp.annaValinnanNimi());
                dialogiPuunKansio.add(dialogiPuunValintaKansio);
                MutableTreeNode dialogiPuunValinnanOtsikkoNode = new DefaultMutableTreeNode("O: " + vdp.annaValinnanOtsikko());
                dialogiPuunValintaKansio.add(dialogiPuunValinnanOtsikkoNode);
                for (int i = 0; i < vdp.annaValinnanVaihtoehdot().length; i++) {
                    DefaultMutableTreeNode dialogiPuunValintaVaihtoehtoNode = new DefaultMutableTreeNode("V: " + vdp.annaValinnanVaihtoehdot()[i]);
                    dialogiPuunValintaKansio.add(dialogiPuunValintaVaihtoehtoNode);
                    if (vdp.annaTriggerit()[i] != null) {
                        MutableTreeNode dialogiPuunTriggeriNode = new DefaultMutableTreeNode("T: " + vdp.annaTriggerit()[i]);
                        dialogiPuunValintaVaihtoehtoNode.add(dialogiPuunTriggeriNode);
                    }
                    MutableTreeNode dialogiPuunValintaVaihtoehdonKohdeNode = new DefaultMutableTreeNode("D: " + vdp.annaValinnanVaihtoehtojenKohdeDialogit()[i]);
                    dialogiPuunValintaVaihtoehtoNode.add(dialogiPuunValintaVaihtoehdonKohdeNode);
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
                    DefaultMutableTreeNode löydettyNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
                    if (löydettyNode.getLevel() == 2) {
                        if (löydettyNode.getChildCount() <= 0) {
                            int dialogiIndeksi = löydettyNode.getParent().getIndex(löydettyNode);
                            String dialoginNimi = "" + löydettyNode.getParent();
                            VuoropuheDialogiPätkä vdp = editorinDialogiKartta.get(dialoginNimi);
                            if (vdp.annaPituus() > dialogiIndeksi) {
                                tekstiMuokkausKenttä.setText(vdp.annaTekstit()[dialogiIndeksi]);
                                puhujanNimiTekstikenttä.setText(vdp.annaPuhujat()[dialogiIndeksi]);
                                if (vdp.annaKuvat() != null) puhujanKuvaNappi.setIcon(vdp.annaKuvat()[dialogiIndeksi]);
                                esikatseluPuhujanKuvaLabel.setIcon(puhujanKuvaNappi.getIcon());
                                dialogiMuokkausPaneeli.setVisible(true);
                                valintaMuokkausPaneeli.setVisible(false);
                                keskiPaneeli.remove(valintaMuokkausPaneeli);
                                keskiPaneeli.add(dialogiMuokkausPaneeli);
                                esikatseluPaneeli.removeAll();
                                esikatseluPaneeli.add(esikatseluDialogiPaneeli);
                                esikatseluPaneeli.setVisible(true);
                            }
                        }
                        else {
                            päivitäValintaEsikatselu();
                            valinnanTunnisteTekstikenttä.setText("" + löydettyNode);
                            dialogiMuokkausPaneeli.setVisible(false);
                            valintaMuokkausPaneeli.setVisible(true);
                            keskiPaneeli.add(valintaMuokkausPaneeli);
                            keskiPaneeli.remove(dialogiMuokkausPaneeli);
                            valintaMuokkausPaneeli.removeAll();
                            valintaMuokkausPaneeli.add(valinnanTunnistePaneeli);
                            esikatseluPaneeli.removeAll();
                            esikatseluPaneeli.add(esikatseluValintaPaneeli);
                            esikatseluPaneeli.setVisible(true);
                        }
                    }
                    else if (löydettyNode.getLevel() == 3) {
                        päivitäValintaEsikatselu();
                        dialogiMuokkausPaneeli.setVisible(false);
                        valintaMuokkausPaneeli.setVisible(true);
                        keskiPaneeli.add(valintaMuokkausPaneeli);
                        keskiPaneeli.remove(dialogiMuokkausPaneeli);
                        valintaMuokkausPaneeli.removeAll();
                        if (löydettyNode.toString().startsWith("O:")) {
                            valinnanOtsikkoTekstikenttä.setText(("" + löydettyNode).substring(3, ("" + löydettyNode).length()));
                            valintaMuokkausPaneeli.add(valinnanOtsikkoPaneeli);
                        }
                        else if (löydettyNode.toString().startsWith("V:")) {
                            valinnanVaihtoehtoTekstikenttä.setText(("" + löydettyNode).substring(3, ("" + löydettyNode).length()));
                            valintaMuokkausPaneeli.add(valinnanVaihtoehtoPaneeli);
                        }
                        esikatseluPaneeli.removeAll();
                        esikatseluPaneeli.add(esikatseluValintaPaneeli);
                        esikatseluPaneeli.setVisible(true);
                    }
                    else if (löydettyNode.getLevel() == 4) {
                        if (löydettyNode.toString().startsWith("T:")) {
                            valintaMuokkausPaneeli.add(valinnanTriggeriPaneeli);
                            valinnanTriggeriValinta.setSelectedItem(("" + löydettyNode).substring(3, ("" + löydettyNode).length()));
                        }
                        else if (löydettyNode.toString().startsWith("D:")) {
                            valinnanVaihtoehdonKohdeTekstikenttä.setText(("" + löydettyNode).substring(3, ("" + löydettyNode).length()));
                            valinnanVaihtoehdonKohdeValinta.setSelectedItem(("" + löydettyNode).substring(3, ("" + löydettyNode).length()));
                            valintaMuokkausPaneeli.add(valinnanVaihtoehdonKohdePaneeli);
                        }
                        esikatseluPaneeli.removeAll();
                        esikatseluPaneeli.add(esikatseluValintaPaneeli);
                        esikatseluPaneeli.setVisible(true);
                    }
                    else {
                        puhujanNimiTekstikenttä.setText("");
                        puhujanKuvaNappi.setIcon(null);
                        esikatseluPuhujanKuvaLabel.setIcon(null);
                        dialogiMuokkausPaneeli.setVisible(false);
                        valintaMuokkausPaneeli.setVisible(false);
                        esikatseluPaneeli.setVisible(false);
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
                    
                }
                else if (SwingUtilities.isRightMouseButton(e)) {
                    if (dialogipuu.getSelectionPath() != null) {
                        //DefaultMutableTreeNode valittuNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
                        TreePath polku = dialogipuu.getPathForLocation(e.getX(), e.getY());
                        dialogipuu.setSelectionPath(polku);
                        DefaultMutableTreeNode valittuNode = (DefaultMutableTreeNode)polku.getLastPathComponent();
                        switch (valittuNode.getLevel()) {
                            case 0:
                                JPopupMenu muokkausValikko = new JPopupMenu();
                                JMenuItem lisääDialogiPätkä = new JMenuItem("Lisää dialogipätkä");
                                lisääDialogiPätkä.addActionListener(listener -> {
                                    DefaultTreeModel model = (DefaultTreeModel)dialogipuu.getModel();
                                    DefaultMutableTreeNode löydettyNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
                                    if (löydettyNode != null) {
                                        DefaultMutableTreeNode lisättäväNode = new DefaultMutableTreeNode("dialogipätkä");
                                        model.insertNodeInto(lisättäväNode, (MutableTreeNode)löydettyNode, löydettyNode.getChildCount());
                                        editorinDialogiKartta.put("" + lisättäväNode, new VuoropuheDialogiPätkä("" + lisättäväNode, 0, new Icon[0], new String[0], new String[0], false, null, null, null, null, null));
                                        dialogipuu.setSelectionPath(new TreePath(lisättäväNode.getPath()));
                                        NimenValintaIkkuna.luoNimenValintaIkkuna();
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
                                            if (löydettyNode.getChildCount() <= 0) {
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
                                            else if ((löydettyNode.getChildAt(löydettyNode.getChildCount()-1)).getChildCount() <= 0) {
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
                                                    MutableTreeNode lisättäväNode = new DefaultMutableTreeNode("valinta");
                                                    model.insertNodeInto(lisättäväNode, (MutableTreeNode)löydettyNode, löydettyNode.getChildCount());
                                                    MutableTreeNode lisättäväNodeOtsikko = new DefaultMutableTreeNode("O: Otsikko");
                                                    MutableTreeNode lisättäväNodeVaihtoehto = new DefaultMutableTreeNode("V: vaihtoehto");
                                                    String kohdeDialoginNimi = "" + editorinDialogiKartta.keySet().toArray()[0];
                                                    MutableTreeNode lisättäväNodeVaihtoehtonKohde = new DefaultMutableTreeNode("D: " + kohdeDialoginNimi);
                                                    model.insertNodeInto(lisättäväNodeOtsikko, lisättäväNode, 0);
                                                    model.insertNodeInto(lisättäväNodeVaihtoehto, lisättäväNode, 1);
                                                    model.insertNodeInto(lisättäväNodeVaihtoehtonKohde, lisättäväNode, 2);
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
                                        DefaultMutableTreeNode löydettyNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
                                        if (löydettyNode != null) {
                                            int valinta = CustomViestiIkkunat.PoistaDialogiPätkä.näytäDialogi();
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
                                                    editorinDialogiKartta.put("" + löydettyNode.getParent(), new VuoropuheDialogiPätkä("" + lisättäväNode, 0, new Icon[1], null, null, false, null, null, null, null, null));
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
                                                    MutableTreeNode lisättäväNode = new DefaultMutableTreeNode("valinta");
                                                    model.insertNodeInto(lisättäväNode, (MutableTreeNode)löydettyNode.getParent(), löydettyNode.getParent().getChildCount());
                                                    MutableTreeNode lisättäväNodeOtsikko = new DefaultMutableTreeNode("O: Otsikko");
                                                    MutableTreeNode lisättäväNodeVaihtoehto = new DefaultMutableTreeNode("V: vaihtoehto");
                                                    String kohdeDialoginNimi = "" + editorinDialogiKartta.keySet().toArray()[0];
                                                    MutableTreeNode lisättäväNodeVaihtoehtonKohde = new DefaultMutableTreeNode("D: " + kohdeDialoginNimi);
                                                    model.insertNodeInto(lisättäväNodeOtsikko, lisättäväNode, 0);
                                                    model.insertNodeInto(lisättäväNodeVaihtoehto, lisättäväNode, 1);
                                                    model.insertNodeInto(lisättäväNodeVaihtoehtonKohde, lisättäväNode, 2);
                                                }
                                            }
                                        }
                                    });
                                    lisäysValikko.add(lisääDialogi);
                                    lisäysValikko.add(lisääValinta);

                                    JMenuItem poista = new JMenuItem("Poista");
                                    poista.addActionListener(listener -> {
                                        DefaultTreeModel model = (DefaultTreeModel)dialogipuu.getModel();
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
                                                MutableTreeNode lisättäväNodeVaihtoehto = new DefaultMutableTreeNode("V: vaihtoehto");
                                                String kohdeDialoginNimi = "" + editorinDialogiKartta.keySet().toArray()[0];
                                                MutableTreeNode lisättäväNodeVaihtoehtonKohde = new DefaultMutableTreeNode("D: " + kohdeDialoginNimi);
                                                model.insertNodeInto(lisättäväNodeVaihtoehto, löydettyNode, löydettyNode.getChildCount());
                                                model.insertNodeInto(lisättäväNodeVaihtoehtonKohde, lisättäväNodeVaihtoehto, lisättäväNodeVaihtoehto.getChildCount());
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

                                    JMenuItem poista = new JMenuItem("Poista");
                                    poista.addActionListener(listener -> {
                                        DefaultTreeModel model = (DefaultTreeModel)dialogipuu.getModel();
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
        dialogipuu.setCellRenderer(new DefaultTreeCellRenderer() {
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
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                int nodeLevel = node.getLevel();
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
                    else if (node.toString().startsWith("O:")) {
                        setIcon(dialogiValintaOtsikkoKuvake);
                    }
                    else {
                        setIcon(null);
                    }
                }
                else if (nodeLevel == 4) {
                    if (node.toString().startsWith("D:")) {
                        setIcon(dialogiVaihtoehdonKohdeKuvake);
                    }
                    else if (node.toString().startsWith("T:")) {
                        setIcon(dialogiTriggeriKuvake);
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
        vasenScrollPaneeli.setPreferredSize(new Dimension(200, 300));

        // JPanel oikeaPaneeli = new JPanel(new BorderLayout());
        // oikeaPaneeli.setName("oikeapaneeli");
        // oikeaPaneeli.add(new JButton(""));

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
                        int valitseKuvaVirheestäHuolimatta = CustomViestiIkkunat.DialogiKuvanKokoHuomautus.näytäDialogi("Valitun kuvan koko on " + img.getWidth() + " x " + img.getHeight() + " pikseliä.\nDialogikuvat toimivat parhaiten, kun niiden koko on 140 x 110 pikseliä.\nVääränkokoisten kuvien (varsinkin liian suurten) käyttäminen voi saada aikaan ei-toivottuja tuloksia.\n\nHaluatko valita kuvan silti?");
                        if (valitseKuvaVirheestäHuolimatta == JOptionPane.YES_OPTION) {
                            puhujanKuvaNappi.setIcon(new ImageIcon(img));
                            esikatseluPuhujanKuvaLabel.setIcon(puhujanKuvaNappi.getIcon());
                            DefaultMutableTreeNode valittuNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
                            String dialogiPätkänNimi = "" + valittuNode.getParent();
                            VuoropuheDialogiPätkä vdp = editorinDialogiKartta.get("" + dialogiPätkänNimi);
                            Icon[] puhujanKuvat = vdp.annaKuvat();
                            puhujanKuvat[valittuNode.getParent().getIndex(valittuNode)] = puhujanKuvaNappi.getIcon();
                            editorinDialogiKartta.put(dialogiPätkänNimi, new VuoropuheDialogiPätkä(dialogiPätkänNimi, vdp.annaPituus(), puhujanKuvat, vdp.annaTekstit(), vdp.annaPuhujat()));
                        }
                    }
                    else {
                        puhujanKuvaNappi.setIcon(new ImageIcon(img));
                        esikatseluPuhujanKuvaLabel.setIcon(puhujanKuvaNappi.getIcon());
                        DefaultMutableTreeNode valittuNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
                        String dialogiPätkänNimi = "" + valittuNode.getParent();
                        VuoropuheDialogiPätkä vdp = editorinDialogiKartta.get("" + dialogiPätkänNimi);
                        Icon[] puhujanKuvat = vdp.annaKuvat();
                        puhujanKuvat[valittuNode.getParent().getIndex(valittuNode)] = puhujanKuvaNappi.getIcon();
                        editorinDialogiKartta.put(dialogiPätkänNimi, new VuoropuheDialogiPätkä(dialogiPätkänNimi, vdp.annaPituus(), puhujanKuvat, vdp.annaTekstit(), vdp.annaPuhujat()));
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

        esikatseluDialogiPaneeli = new JPanel(new BorderLayout());
        esikatseluDialogiPaneeli.add(puhujanKuvaPaneeli, BorderLayout.WEST);
        esikatseluDialogiPaneeli.add(puhujanNimiPaneeli, BorderLayout.NORTH);
        esikatseluDialogiPaneeli.add(puhujanTekstiPaneeli, BorderLayout.CENTER);

        esikatseluPaneeli = new JPanel();


        JLabel valinnanTunnisteLabel = new JLabel("Valinnan tunniste");
        valinnanTunnisteTekstikenttä = new JTextField();
        valinnanTunnisteTekstikenttä.setMinimumSize(new Dimension(400, 20));
        valinnanTunnisteTekstikenttä.setMaximumSize(new Dimension(400, 20));
        valinnanTunnisteTekstikenttä.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {

            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        JPanel valinnanTunnisteMuokkausPaneeli = new JPanel();
        GroupLayout valinnanTunnistePaneelinLayout = new GroupLayout(valinnanTunnisteMuokkausPaneeli);
        valinnanTunnisteMuokkausPaneeli.setLayout(valinnanTunnistePaneelinLayout);
        valinnanTunnistePaneelinLayout.setAutoCreateGaps(true);
        valinnanTunnistePaneelinLayout.setAutoCreateContainerGaps(true);
        valinnanTunnistePaneelinLayout.setHorizontalGroup(
            valinnanTunnistePaneelinLayout.createSequentialGroup()
                .addGroup(valinnanTunnistePaneelinLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(valinnanTunnisteLabel)
                    )
                .addGroup(valinnanTunnistePaneelinLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(valinnanTunnisteTekstikenttä)
                    )
        );
        valinnanTunnistePaneelinLayout.setVerticalGroup(
            valinnanTunnistePaneelinLayout.createSequentialGroup()
                .addGroup(valinnanTunnistePaneelinLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(valinnanTunnisteLabel)
                    .addComponent(valinnanTunnisteTekstikenttä)
                    )
        );

        valinnanTunnistePaneeli = new JPanel(new GridBagLayout());
        valinnanTunnistePaneeli.add(valinnanTunnisteMuokkausPaneeli);
        valinnanTunnistePaneeli.setVisible(true);

        JLabel valinnanOtsikkoLabel = new JLabel("Valinnan otsikko");
        valinnanOtsikkoTekstikenttä = new JTextField();
        valinnanOtsikkoTekstikenttä.setMinimumSize(new Dimension(400, 20));
        valinnanOtsikkoTekstikenttä.setMaximumSize(new Dimension(400, 20));
        valinnanOtsikkoTekstikenttä.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                esikatseluValinnanOtsikkoLabel.setText("<html><p>" + valinnanOtsikkoTekstikenttä.getText() + "</p></html>");
                DefaultMutableTreeNode valittuNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
                if (valittuNode.getLevel() == 3 && valittuNode.getChildCount() <= 0) {
                    String dialogiPätkänNimi = "" + valittuNode.getParent().getParent();
                    VuoropuheDialogiPätkä vdp = editorinDialogiKartta.get("" + dialogiPätkänNimi);
                    String valinnanOtsikko = vdp.annaValinnanOtsikko();
                    valinnanOtsikko = valinnanOtsikkoTekstikenttä.getText();
                    editorinDialogiKartta.put(dialogiPätkänNimi, new VuoropuheDialogiPätkä(dialogiPätkänNimi, vdp.annaPituus(), vdp.annaKuvat(), vdp.annaTekstit(), vdp.annaPuhujat(), true, vdp.annaValinnanNimi(), valinnanOtsikko, vdp.annaValinnanVaihtoehdot(), vdp.annaValinnanVaihtoehtojenKohdeDialogit(), vdp.annaTriggerit()));
                    valittuNode.setUserObject("O: " + valinnanOtsikkoTekstikenttä.getText());
                    ((DefaultTreeModel) dialogipuu.getModel()).nodeChanged(valittuNode);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                esikatseluValinnanOtsikkoLabel.setText("<html><p>" + valinnanOtsikkoTekstikenttä.getText() + "</p></html>");
                DefaultMutableTreeNode valittuNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
                if (valittuNode.getLevel() == 3 && valittuNode.getChildCount() <= 0) {
                    String dialogiPätkänNimi = "" + valittuNode.getParent().getParent();
                    VuoropuheDialogiPätkä vdp = editorinDialogiKartta.get("" + dialogiPätkänNimi);
                    String valinnanOtsikko = vdp.annaValinnanOtsikko();
                    valinnanOtsikko = valinnanOtsikkoTekstikenttä.getText();
                    editorinDialogiKartta.put(dialogiPätkänNimi, new VuoropuheDialogiPätkä(dialogiPätkänNimi, vdp.annaPituus(), vdp.annaKuvat(), vdp.annaTekstit(), vdp.annaPuhujat(), true, vdp.annaValinnanNimi(), valinnanOtsikko, vdp.annaValinnanVaihtoehdot(), vdp.annaValinnanVaihtoehtojenKohdeDialogit(), vdp.annaTriggerit()));
                    valittuNode.setUserObject("O: " + valinnanOtsikkoTekstikenttä.getText());
                    ((DefaultTreeModel) dialogipuu.getModel()).nodeChanged(valittuNode);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                esikatseluValinnanOtsikkoLabel.setText("<html><p>" + valinnanOtsikkoTekstikenttä.getText() + "</p></html>");
                DefaultMutableTreeNode valittuNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
                if (valittuNode.getLevel() == 3 && valittuNode.getChildCount() <= 0) {
                    String dialogiPätkänNimi = "" + valittuNode.getParent().getParent();
                    VuoropuheDialogiPätkä vdp = editorinDialogiKartta.get("" + dialogiPätkänNimi);
                    String valinnanOtsikko = vdp.annaValinnanOtsikko();
                    valinnanOtsikko = valinnanOtsikkoTekstikenttä.getText();
                    editorinDialogiKartta.put(dialogiPätkänNimi, new VuoropuheDialogiPätkä(dialogiPätkänNimi, vdp.annaPituus(), vdp.annaKuvat(), vdp.annaTekstit(), vdp.annaPuhujat(), true, vdp.annaValinnanNimi(), valinnanOtsikko, vdp.annaValinnanVaihtoehdot(), vdp.annaValinnanVaihtoehtojenKohdeDialogit(), vdp.annaTriggerit()));
                    valittuNode.setUserObject("O: " + valinnanOtsikkoTekstikenttä.getText());
                    ((DefaultTreeModel) dialogipuu.getModel()).nodeChanged(valittuNode);
                }
            }
        });

        valintaMuokkausPaneeli = new JPanel();

        JPanel valinnanOtsikkoMuokkausPaneeli = new JPanel(new SpringLayout());
        GroupLayout valinnanOtsikkoPaneelinLayout = new GroupLayout(valinnanOtsikkoMuokkausPaneeli);
        valinnanOtsikkoMuokkausPaneeli.setLayout(valinnanOtsikkoPaneelinLayout);
        valinnanOtsikkoPaneelinLayout.setAutoCreateGaps(true);
        valinnanOtsikkoPaneelinLayout.setAutoCreateContainerGaps(true);
        valinnanOtsikkoPaneelinLayout.setHorizontalGroup(
            valinnanOtsikkoPaneelinLayout.createSequentialGroup()
                .addGroup(valinnanOtsikkoPaneelinLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(valinnanOtsikkoLabel)
                    )
                .addGroup(valinnanOtsikkoPaneelinLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(valinnanOtsikkoTekstikenttä)
                    )
        );
        valinnanOtsikkoPaneelinLayout.setVerticalGroup(
            valinnanOtsikkoPaneelinLayout.createSequentialGroup()
                .addGroup(valinnanOtsikkoPaneelinLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(valinnanOtsikkoLabel)
                    .addComponent(valinnanOtsikkoTekstikenttä)
                    )
        );

        valinnanOtsikkoPaneeli = new JPanel(new GridBagLayout());
        valinnanOtsikkoPaneeli.add(valinnanOtsikkoMuokkausPaneeli);
        valinnanOtsikkoPaneeli.setVisible(true);

        JLabel valinnanVaihtoehtoLabel = new JLabel("Vaihtoehto");
        valinnanVaihtoehtoTekstikenttä = new JTextField();
        valinnanVaihtoehtoTekstikenttä.setMinimumSize(new Dimension(400, 20));
        valinnanVaihtoehtoTekstikenttä.setMaximumSize(new Dimension(400, 20));
        valinnanVaihtoehtoTekstikenttä.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                DefaultMutableTreeNode valittuNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
                if (valittuNode.getLevel() == 3 && valittuNode.getChildCount() <= 0) {
                    String dialogiPätkänNimi = "" + valittuNode.getParent().getParent();
                    VuoropuheDialogiPätkä vdp = editorinDialogiKartta.get("" + dialogiPätkänNimi);
                    String[] valinnanVaihtoehdot = vdp.annaValinnanVaihtoehdot();
                    int valintaIndeksi = 0;
                    for (int i = 0; i < valittuNode.getParent().getChildCount(); i++) {
                        if (valittuNode.getParent().getChildAt(i).toString().startsWith("V:")) {
                            valintaIndeksi++;
                            if (valittuNode.getParent().getChildAt(i).toString().substring(3) == valinnanVaihtoehtoTekstikenttä.getText()) {
                                valinnanVaihtoehdot[valintaIndeksi] = valinnanVaihtoehtoTekstikenttä.getText();
                                break;
                            }
                        }
                    }
                    editorinDialogiKartta.put(dialogiPätkänNimi, new VuoropuheDialogiPätkä(dialogiPätkänNimi, vdp.annaPituus(), vdp.annaKuvat(), vdp.annaTekstit(), vdp.annaPuhujat(), true, vdp.annaValinnanNimi(), vdp.annaValinnanOtsikko(), vdp.annaValinnanVaihtoehdot(), vdp.annaValinnanVaihtoehtojenKohdeDialogit(), vdp.annaTriggerit()));
                    valittuNode.setUserObject("V: " + valinnanVaihtoehtoTekstikenttä.getText());
                    ((DefaultTreeModel) dialogipuu.getModel()).nodeChanged(valittuNode);
                    päivitäValintaEsikatselu();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                DefaultMutableTreeNode valittuNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
                if (valittuNode.getLevel() == 3 && valittuNode.getChildCount() <= 0) {
                    String dialogiPätkänNimi = "" + valittuNode.getParent().getParent();
                    VuoropuheDialogiPätkä vdp = editorinDialogiKartta.get("" + dialogiPätkänNimi);
                    String[] valinnanVaihtoehdot = vdp.annaValinnanVaihtoehdot();
                    int valintaIndeksi = 0;
                    for (int i = 0; i < valittuNode.getParent().getChildCount(); i++) {
                        if (valittuNode.getParent().getChildAt(i).toString().startsWith("V:")) {
                            valintaIndeksi++;
                            if (valittuNode.getParent().getChildAt(i).toString().substring(3) == valinnanVaihtoehtoTekstikenttä.getText()) {
                                valinnanVaihtoehdot[valintaIndeksi] = valinnanVaihtoehtoTekstikenttä.getText();
                                break;
                            }
                        }
                    }
                    editorinDialogiKartta.put(dialogiPätkänNimi, new VuoropuheDialogiPätkä(dialogiPätkänNimi, vdp.annaPituus(), vdp.annaKuvat(), vdp.annaTekstit(), vdp.annaPuhujat(), true, vdp.annaValinnanNimi(), vdp.annaValinnanOtsikko(), vdp.annaValinnanVaihtoehdot(), vdp.annaValinnanVaihtoehtojenKohdeDialogit(), vdp.annaTriggerit()));
                    valittuNode.setUserObject("V: " + valinnanVaihtoehtoTekstikenttä.getText());
                    ((DefaultTreeModel) dialogipuu.getModel()).nodeChanged(valittuNode);
                    päivitäValintaEsikatselu();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                DefaultMutableTreeNode valittuNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
                if (valittuNode.getLevel() == 3 && valittuNode.getChildCount() <= 0) {
                    String dialogiPätkänNimi = "" + valittuNode.getParent().getParent();
                    VuoropuheDialogiPätkä vdp = editorinDialogiKartta.get("" + dialogiPätkänNimi);
                    String[] valinnanVaihtoehdot = vdp.annaValinnanVaihtoehdot();
                    int valintaIndeksi = 0;
                    for (int i = 0; i < valittuNode.getParent().getChildCount(); i++) {
                        if (valittuNode.getParent().getChildAt(i).toString().startsWith("V:")) {
                            valintaIndeksi++;
                            if (valittuNode.getParent().getChildAt(i).toString().substring(3) == valinnanVaihtoehtoTekstikenttä.getText()) {
                                valinnanVaihtoehdot[valintaIndeksi] = valinnanVaihtoehtoTekstikenttä.getText();
                                break;
                            }
                        }
                    }
                    editorinDialogiKartta.put(dialogiPätkänNimi, new VuoropuheDialogiPätkä(dialogiPätkänNimi, vdp.annaPituus(), vdp.annaKuvat(), vdp.annaTekstit(), vdp.annaPuhujat(), true, vdp.annaValinnanNimi(), vdp.annaValinnanOtsikko(), vdp.annaValinnanVaihtoehdot(), vdp.annaValinnanVaihtoehtojenKohdeDialogit(), vdp.annaTriggerit()));
                    valittuNode.setUserObject("V: " + valinnanVaihtoehtoTekstikenttä.getText());
                    ((DefaultTreeModel) dialogipuu.getModel()).nodeChanged(valittuNode);
                    päivitäValintaEsikatselu();
                }
            }
        });

        JPanel valinnanVaihtoehtoMuokkausPaneeli = new JPanel();
        GroupLayout valinnanVaihtoehtoPaneelinLayout = new GroupLayout(valinnanVaihtoehtoMuokkausPaneeli);
        valinnanVaihtoehtoMuokkausPaneeli.setLayout(valinnanVaihtoehtoPaneelinLayout);
        valinnanVaihtoehtoPaneelinLayout.setAutoCreateGaps(true);
        valinnanVaihtoehtoPaneelinLayout.setAutoCreateContainerGaps(true);
        valinnanVaihtoehtoPaneelinLayout.setHorizontalGroup(
            valinnanVaihtoehtoPaneelinLayout.createSequentialGroup()
                .addGroup(valinnanVaihtoehtoPaneelinLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(valinnanVaihtoehtoLabel)
                    )
                .addGroup(valinnanVaihtoehtoPaneelinLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(valinnanVaihtoehtoTekstikenttä)
                    )
        );
        valinnanVaihtoehtoPaneelinLayout.setVerticalGroup(
            valinnanVaihtoehtoPaneelinLayout.createSequentialGroup()
                .addGroup(valinnanVaihtoehtoPaneelinLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(valinnanVaihtoehtoLabel)
                    .addComponent(valinnanVaihtoehtoTekstikenttä)
                    )
        );

        valinnanVaihtoehtoPaneeli = new JPanel(new GridBagLayout());
        valinnanVaihtoehtoPaneeli.add(valinnanVaihtoehtoMuokkausPaneeli);
        valinnanVaihtoehtoPaneeli.setVisible(true);

        JLabel valinnanVaihtoehdonKohdeLabel = new JLabel("Vaihtoehdon kohdedialogi");
        JLabel valinnanVaihtoehdonKohdeVaroitus = new JLabel("<html><p>Huom! Valittu kohdedialogi on sama kuin nykyinen dialogi. Tällainen valinta johtaa ikuiseen silmukkaan dialogissa. Käytä tällaista rakennetta vain, jos vaihtoehtoja on useita ja jonkin niistä halutaan tarkoituksella johtavan takaisin dialogin alkuun.</p></html>");
        valinnanVaihtoehdonKohdeVaroitus.setForeground(Color.red);
        valinnanVaihtoehdonKohdeVaroitus.setPreferredSize(new Dimension(400, 200));
        valinnanVaihtoehdonKohdeVaroitus.setVisible(false);
        valinnanVaihtoehdonKohdeTekstikenttä = new JTextField();
        valinnanVaihtoehdonKohdeValinta = new JComboBox<>(editorinDialogiKartta.keySet().toArray());
        valinnanVaihtoehdonKohdeValinta.setMinimumSize(new Dimension(400, 20));
        valinnanVaihtoehdonKohdeValinta.setMaximumSize(new Dimension(400, 20));
        valinnanVaihtoehdonKohdeValinta.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().startsWith("COMBOBOX.CP_READONLY")) {
                    DefaultMutableTreeNode valittuNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
                    if (valittuNode.getLevel() == 3 && valittuNode.getChildCount() <= 0 && valittuNode.toString().startsWith("D:")) {
                        valittuNode.setUserObject("D: " + valinnanVaihtoehdonKohdeValinta.getSelectedItem());
                        ((DefaultTreeModel) dialogipuu.getModel()).nodeChanged(valittuNode);
                        if (valinnanVaihtoehdonKohdeValinta.getSelectedItem().toString() == valittuNode.getParent().getParent().toString()) {
                            valinnanVaihtoehdonKohdeVaroitus.setVisible(true);
                        }
                        else {
                            valinnanVaihtoehdonKohdeVaroitus.setVisible(false);
                        }
                        päivitäValintaEsikatselu();
                    }
                }
            }
        });

        JPanel valinnanVaihtoehdonKohdeMuokkausPaneeli = new JPanel();
        GroupLayout valinnanVaihtoehdonKohdePaneelinLayout = new GroupLayout(valinnanVaihtoehdonKohdeMuokkausPaneeli);
        valinnanVaihtoehdonKohdeMuokkausPaneeli.setLayout(valinnanVaihtoehdonKohdePaneelinLayout);
        valinnanVaihtoehdonKohdePaneelinLayout.setAutoCreateGaps(true);
        valinnanVaihtoehdonKohdePaneelinLayout.setAutoCreateContainerGaps(true);
        valinnanVaihtoehdonKohdePaneelinLayout.setHorizontalGroup(
            valinnanVaihtoehdonKohdePaneelinLayout.createSequentialGroup()
                .addGroup(valinnanVaihtoehdonKohdePaneelinLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(valinnanVaihtoehdonKohdeLabel)
                    )
                .addGroup(valinnanVaihtoehdonKohdePaneelinLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(valinnanVaihtoehdonKohdeValinta)
                    .addComponent(valinnanVaihtoehdonKohdeVaroitus)
                    )
        );
        valinnanVaihtoehdonKohdePaneelinLayout.setVerticalGroup(
            valinnanVaihtoehdonKohdePaneelinLayout.createSequentialGroup()
                .addGroup(valinnanVaihtoehdonKohdePaneelinLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(valinnanVaihtoehdonKohdeLabel)
                    .addComponent(valinnanVaihtoehdonKohdeValinta)
                    )
                .addGroup(valinnanVaihtoehdonKohdePaneelinLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(valinnanVaihtoehdonKohdeVaroitus)
                    .addComponent(valinnanVaihtoehdonKohdeVaroitus)
                    )
        );

        valinnanVaihtoehdonKohdePaneeli = new JPanel(new GridBagLayout());
        valinnanVaihtoehdonKohdePaneeli.add(valinnanVaihtoehdonKohdeMuokkausPaneeli);
        valinnanVaihtoehdonKohdePaneeli.setVisible(true);

        JLabel valinnanTriggeriLabel = new JLabel("Valitse triggeri");
        valinnanTriggeriValinta = new JComboBox<>(TavoiteLista.tavoiteLista.keySet().toArray());
        valinnanTriggeriValinta.setMinimumSize(new Dimension(400, 20));
        valinnanTriggeriValinta.setMaximumSize(new Dimension(400, 20));
        valinnanTriggeriValinta.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().startsWith("COMBOBOX.CP_READONLY")) {
                    DefaultMutableTreeNode valittuNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
                    if (valittuNode.getLevel() == 3 && valittuNode.getChildCount() <= 0 && valittuNode.toString().startsWith("T:")) {
                        valittuNode.setUserObject("T: " + valinnanTriggeriValinta.getSelectedItem());
                        ((DefaultTreeModel) dialogipuu.getModel()).nodeChanged(valittuNode);
                    }
                }
            }
        });

        JPanel valinnanTriggeriMuokkausPaneeli = new JPanel();
        GroupLayout valinnanTriggeriPaneelinLayout = new GroupLayout(valinnanTriggeriMuokkausPaneeli);
        valinnanTriggeriMuokkausPaneeli.setLayout(valinnanTriggeriPaneelinLayout);
        valinnanTriggeriPaneelinLayout.setAutoCreateGaps(true);
        valinnanTriggeriPaneelinLayout.setAutoCreateContainerGaps(true);
        valinnanTriggeriPaneelinLayout.setHorizontalGroup(
            valinnanTriggeriPaneelinLayout.createSequentialGroup()
                .addGroup(valinnanTriggeriPaneelinLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(valinnanTriggeriLabel)
                    )
                .addGroup(valinnanTriggeriPaneelinLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(valinnanTriggeriValinta)
                    )
        );
        valinnanTriggeriPaneelinLayout.setVerticalGroup(
            valinnanTriggeriPaneelinLayout.createSequentialGroup()
                .addGroup(valinnanTriggeriPaneelinLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(valinnanTriggeriLabel)
                    .addComponent(valinnanTriggeriValinta)
                    )
        );

        valinnanTriggeriPaneeli = new JPanel(new GridBagLayout());
        valinnanTriggeriPaneeli.add(valinnanTriggeriMuokkausPaneeli);
        valinnanTriggeriPaneeli.setVisible(true);

        esikatseluValinnanOtsikkoLabel = new JLabel("Valinnan otsikko");
        JPanel esikatseluValinnanOtsikkoPaneli = new JPanel(new BorderLayout());
        esikatseluValinnanOtsikkoPaneli.setPreferredSize(new Dimension(400, 30));
        esikatseluValinnanOtsikkoPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, false));
        esikatseluValinnanOtsikkoPaneli.add(esikatseluValinnanOtsikkoLabel);

        esikatseluValinnanVaihtoehtoPaneli = new JPanel(new SpringLayout());
        esikatseluValinnanVaihtoehtoPaneli.setPreferredSize(new Dimension(400, 100));
        esikatseluValinnanVaihtoehtoPaneli.setBorder(BorderFactory.createLineBorder(Color.black, 1, false));
        päivitäValintaEsikatselu();

        esikatseluValintaPaneeli = new JPanel(new BorderLayout());
        esikatseluValintaPaneeli.add(esikatseluValinnanOtsikkoPaneli, BorderLayout.NORTH);
        esikatseluValintaPaneeli.add(esikatseluValinnanVaihtoehtoPaneli, BorderLayout.CENTER);


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
            Dialogit.PitkätDialogit.vuoropuheDialogiKartta = editorinDialogiKartta;
            editorinDialogiKartta = new HashMap<>();
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
        //pääPaneeli.add(oikeaPaneeli, BorderLayout.EAST);
        pääPaneeli.add(keskiPaneeli, BorderLayout.CENTER);
        pääPaneeli.add(alaPaneeli, BorderLayout.SOUTH);

        ikkuna.add(yläPalkki, BorderLayout.NORTH);
        ikkuna.add(pääPaneeli, BorderLayout.CENTER);
    }

    static void päivitäValintaEsikatselu() {
        if (dialogipuu.getSelectionPath() != null) {
            DefaultMutableTreeNode valittuNode = (DefaultMutableTreeNode)dialogipuu.getSelectionPath().getLastPathComponent();
            if (valittuNode.getLevel() == 3 || (valittuNode.getLevel() == 2 && valittuNode.getChildCount() > 0)) {
                int vaihtoehdot = 0;
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode)valittuNode.getParent();
                for (int i = 0; i < parent.getChildCount(); i++) {
                    if (("" + parent.getChildAt(i)).startsWith("V:")) {
                        vaihtoehdot++;
                    }
                }
                ArrayList<String> vaihtoehtojenNimet = new ArrayList<>();
                for (int i = 0; i < parent.getChildCount(); i++) {
                    if (("" + parent.getChildAt(i)).startsWith("V:")) {
                        vaihtoehtojenNimet.add(("" + parent.getChildAt(i)));
                    }
                }
                ArrayList<String> triggereidenNimet = new ArrayList<>();
                for (int i = 0; i < parent.getChildCount(); i++) {
                    if (("" + parent.getChildAt(i)).startsWith("V:") && (parent.getChildAt(i)).getChildCount() > 1) {
                        for (int j = 0; j < parent.getChildAt(i).getChildCount(); j++) {
                            if (("" + parent.getChildAt(i).getChildAt(j)).startsWith("T: ")) {
                                triggereidenNimet.add(("" + parent.getChildAt(i).getChildAt(j)));
                            }
                        }
                    }
                }
                ArrayList<String> dialogienNimet = new ArrayList<>();
                for (int i = 0; i < parent.getChildCount(); i++) {
                    DefaultMutableTreeNode alaNode = (DefaultMutableTreeNode)parent.getChildAt(i);
                    if (alaNode.getChildCount() > 0) {
                        DefaultMutableTreeNode dialogiNode = (DefaultMutableTreeNode)alaNode.getChildAt(alaNode.getChildCount()-1);
                        if (("" + dialogiNode).startsWith("D:")) {
                            dialogienNimet.add(("" + dialogiNode));
                        }
                    }
                }
                if (esikatseluValinnanVaihtoehtoPaneli != null) {
                    
                    esikatseluValinnanVaihtoehtoPaneli.removeAll();
                    for (int i = 0; i < vaihtoehdot; i++) {
                        JPanel valinnanVaihtoehtoYksittäinenPaneeli = new JPanel(new BorderLayout());
                        JLabel osoitin = new JLabel(new ImageIcon("tiedostot/kuvat/menu/dialogi/osoitin32.png"));
                        osoitin.setPreferredSize(new Dimension(32, 64/vaihtoehdot));
                        valinnanVaihtoehtoYksittäinenPaneeli.add(osoitin, BorderLayout.WEST);
                        JLabel valinta = new JLabel(vaihtoehtojenNimet.get(i).substring(3));
                        valinta.setAlignmentX(JLabel.LEFT_ALIGNMENT);
                        valinta.setPreferredSize(new Dimension(32, 64/vaihtoehdot));
                        valinnanVaihtoehtoYksittäinenPaneeli.add(valinta, BorderLayout.CENTER);
                        JLabel triggeriJaDialogi;
                        if (triggereidenNimet.size() > i) {
                            triggeriJaDialogi = new JLabel("->    " + triggereidenNimet.get(i) + "    ->    " + dialogienNimet.get(i).substring(3));
                        }
                        else {
                            triggeriJaDialogi = new JLabel("->    " + "<ei triggeriä>" + "    ->    " + dialogienNimet.get(i).substring(3));
                        }
                        triggeriJaDialogi.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
                        triggeriJaDialogi.setPreferredSize(new Dimension(250, 64/vaihtoehdot));
                        valinnanVaihtoehtoYksittäinenPaneeli.add(triggeriJaDialogi, BorderLayout.EAST);
                        esikatseluValinnanVaihtoehtoPaneli.add(valinnanVaihtoehtoYksittäinenPaneeli);
                        esikatseluValinnanVaihtoehtoPaneli.revalidate();
                        esikatseluValinnanVaihtoehtoPaneli.repaint();
                    }
                    SpringUtilities.makeCompactGrid(esikatseluValinnanVaihtoehtoPaneli, vaihtoehdot, 1, 6, 6, 6, 6);
                }
            }
        }
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
}
