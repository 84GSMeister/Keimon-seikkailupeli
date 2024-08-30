package keimo.HuoneEditori;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.Kenttäkohteet.*;
import keimo.Maastot.*;
import keimo.NPCt.*;
import keimo.NPCt.Vihollinen.LiikeTapa;
import keimo.Utility.KäännettäväKuvake;
import keimo.Utility.Käännettävä.Suunta;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.swing.*;

public class HuoneenLatausSäie { //implements Runnable {

    static int uusiHuoneLatausSäie;
    static boolean käynnissä;
    static int esineenKokoPx = 64;
    public static Object editorinHuoneenLatausLukko = new Object();

    private static JFrame ikkuna;
    private static JProgressBar edistymisPalkki;
    private static JLabel edistymisTeksti;
    protected static double edistyminen = 0;
    
    // @Override
    // public void run() {
    //     käynnissä = true;
    //     HuoneEditoriIkkuna.huoneenVaihtoNappiVasen.setEnabled(false);
    //     HuoneEditoriIkkuna.huoneenVaihtoNappiOikea.setEnabled(false);
    //     HuoneEditoriIkkuna.huoneInfoLabel.setEnabled(false);
    //     HuoneEditoriIkkuna.huoneenNimiLabel.setEnabled(false);
    //     HuoneEditoriIkkuna.huoneenNimiLabel.setText("Ladataan...");
    //     HuoneEditoriIkkuna.huoneInfoLabel.setText("Ladataan...");
    //     HuoneEditoriIkkuna.päivitäEditorinKoko(HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaKoko());
    //     HuoneEditoriIkkuna.uudelleenAlustaEditoriKenttä(HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaKoko());

    //     HuoneEditoriIkkuna.objektiKenttä = HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaHuoneenKenttäSisältö();
    //     HuoneEditoriIkkuna.maastoKenttä = HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaHuoneenMaastoSisältö();
    //     HuoneEditoriIkkuna.npcKenttä = HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaHuoneenNPCSisältö();
    //     HuoneEditoriIkkuna.huoneenNimi = HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaNimi();
    //     HuoneEditoriIkkuna.huoneenAlue = HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaAlue();
    //     HuoneEditoriIkkuna.huoneenAlkuDialoginTunniste = HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaTarinaRuudunTunniste();
    //     HuoneEditoriIkkuna.huoneenVaaditunTavoitteenTunniste = HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaVaaditunTavoitteenTunniste();
    //     HuoneEditoriIkkuna.huoneInfoLabel.setText("Huone " + uusiHuoneLatausSäie);
    //     HuoneEditoriIkkuna.huoneenNimiLabel.setText(HuoneEditoriIkkuna.huoneenNimi + " (" + HuoneEditoriIkkuna.huoneenAlue + ")");
    //     HuoneEditoriIkkuna.huoneenTaustakuvaPolku = HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaTaustanPolku();
    //     HuoneEditoriIkkuna.warpVasen = HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaReunaWarppiTiedot(Suunta.VASEN);
    //     HuoneEditoriIkkuna.warpVasenHuoneId = HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaReunaWarpinKohdeId(Suunta.VASEN);
    //     HuoneEditoriIkkuna.warpOikea = HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaReunaWarppiTiedot(Suunta.OIKEA);
    //     HuoneEditoriIkkuna.warpOikeaHuoneId = HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaReunaWarpinKohdeId(Suunta.OIKEA);
    //     HuoneEditoriIkkuna.warpAlas = HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaReunaWarppiTiedot(Suunta.ALAS);
    //     HuoneEditoriIkkuna.warpAlasHuoneId = HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaReunaWarpinKohdeId(Suunta.ALAS);
    //     HuoneEditoriIkkuna.warpYlös = HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaReunaWarppiTiedot(Suunta.YLÖS);
    //     HuoneEditoriIkkuna.warpYlösHuoneId = HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaReunaWarpinKohdeId(Suunta.YLÖS);

    //     HuoneEditoriIkkuna.objektiEditointiKenttäPaneli.setPreferredSize(new Dimension(HuoneEditoriIkkuna.huoneenKoko * HuoneEditoriIkkuna.esineenKokoPx, HuoneEditoriIkkuna.huoneenKoko * HuoneEditoriIkkuna.esineenKokoPx));
    //     HuoneEditoriIkkuna.maastoEditointiKenttäPaneli.setPreferredSize(new Dimension(HuoneEditoriIkkuna.huoneenKoko * HuoneEditoriIkkuna.esineenKokoPx, HuoneEditoriIkkuna.huoneenKoko * HuoneEditoriIkkuna.esineenKokoPx));
    //     HuoneEditoriIkkuna.npcEditointiKenttäPaneli.setPreferredSize(new Dimension(HuoneEditoriIkkuna.huoneenKoko * HuoneEditoriIkkuna.esineenKokoPx, HuoneEditoriIkkuna.huoneenKoko * HuoneEditoriIkkuna.esineenKokoPx));
        
    //     if (HuoneEditoriIkkuna.huoneenKoko == 10) {
    //         HuoneEditoriIkkuna.scrollattavaObjektiKenttäPaneli.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    //         HuoneEditoriIkkuna.scrollattavaObjektiKenttäPaneli.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    //         HuoneEditoriIkkuna.scrollattavaMaastoKenttäPaneli.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    //         HuoneEditoriIkkuna.scrollattavaMaastoKenttäPaneli.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    //         HuoneEditoriIkkuna.scrollattavaNPCKenttäPaneli.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    //         HuoneEditoriIkkuna.scrollattavaNPCKenttäPaneli.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    //     }
    //     else {
    //         HuoneEditoriIkkuna.scrollattavaObjektiKenttäPaneli.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    //         HuoneEditoriIkkuna.scrollattavaObjektiKenttäPaneli.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    //         HuoneEditoriIkkuna.scrollattavaMaastoKenttäPaneli.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    //         HuoneEditoriIkkuna.scrollattavaMaastoKenttäPaneli.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    //         HuoneEditoriIkkuna.scrollattavaNPCKenttäPaneli.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    //         HuoneEditoriIkkuna.scrollattavaNPCKenttäPaneli.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    //     }
    //     HuoneEditoriIkkuna.huoneenVaihtoNappiVasen.setEnabled(true);
    //     HuoneEditoriIkkuna.huoneenVaihtoNappiOikea.setEnabled(true);
    //     HuoneEditoriIkkuna.huoneInfoLabel.setEnabled(true);
    //     HuoneEditoriIkkuna.huoneenNimiLabel.setEnabled(true);
    //     käynnissä = false;
    // }


    private static JFrame createGUI(){
        ikkuna = new JFrame("Ladataan huonetta");
   
        edistymisPalkki = new JProgressBar();
        edistymisPalkki.setMinimum(0);
        edistymisPalkki.setMaximum(100);
   
        edistymisTeksti = new JLabel("Ladataan huonetta...");
   
        ikkuna.getContentPane().add(edistymisPalkki, BorderLayout.CENTER);
        ikkuna.getContentPane().add(edistymisTeksti, BorderLayout.SOUTH);
        ikkuna.setIconImage(new ImageIcon("tiedostot/kuvat/pelaaja_og.png").getImage());
        ikkuna.setPreferredSize(new Dimension(300, 80));
        ikkuna.setAlwaysOnTop(true);
        ikkuna.setLocationRelativeTo(null);
   
        return ikkuna;
    }
   
    public static void launch() throws InvocationTargetException, InterruptedException {
        JFrame frame = createGUI();
        frame.pack();
        frame.setVisible(true);
        HuoneenLataajaSäie worker = new HuoneenLataajaSäie();
        worker.execute();
    }

    static int huoneListanSuurin = 0;

    private static class HuoneenLataajaSäie extends SwingWorker<String, Double> {

        private void lataaHuone() {
            synchronized(editorinHuoneenLatausLukko) {
                //System.out.println("Lataussäie ottaa lukon");
                käynnissä = true;
                HuoneEditoriIkkuna.huoneenVaihtoNappiVasen.setEnabled(false);
                HuoneEditoriIkkuna.huoneenVaihtoNappiOikea.setEnabled(false);
                HuoneEditoriIkkuna.huoneInfoLabel.setEnabled(false);
                HuoneEditoriIkkuna.huoneenNimiLabel.setEnabled(false);
                HuoneEditoriIkkuna.huoneenNimiLabel.setText("Ladataan...");
                HuoneEditoriIkkuna.huoneInfoLabel.setText("Ladataan...");
                edistyminen = 10; publish((double)edistyminen);

                päivitäEditorinKoko(HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaKoko());
                edistyminen = 20; publish((double)edistyminen);

                uudelleenAlustaEditoriKenttä(HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaKoko());
                edistyminen = 90; publish((double)edistyminen);
        
                HuoneEditoriIkkuna.objektiKenttä = HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaHuoneenKenttäSisältö();
                HuoneEditoriIkkuna.maastoKenttä = HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaHuoneenMaastoSisältö();
                HuoneEditoriIkkuna.npcKenttä = HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaHuoneenNPCSisältö();
                HuoneEditoriIkkuna.huoneenNimi = HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaNimi();
                HuoneEditoriIkkuna.huoneenAlue = HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaAlue();
                HuoneEditoriIkkuna.huoneenAlkuDialoginTunniste = HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaTarinaRuudunTunniste();
                HuoneEditoriIkkuna.huoneenVaaditunTavoitteenTunniste = HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaVaaditunTavoitteenTunniste();
                HuoneEditoriIkkuna.huoneInfoLabel.setText("Huone " + uusiHuoneLatausSäie);
                HuoneEditoriIkkuna.huoneenNimiLabel.setText(HuoneEditoriIkkuna.huoneenNimi + " (" + HuoneEditoriIkkuna.huoneenAlue + ")");
                HuoneEditoriIkkuna.huoneenTaustakuvaPolku = HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaTaustanPolku();
                HuoneEditoriIkkuna.huoneenMusa = HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaHuoneenMusa();
                HuoneEditoriIkkuna.warpVasen = HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaReunaWarppiTiedot(Suunta.VASEN);
                HuoneEditoriIkkuna.warpVasenHuoneId = HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaReunaWarpinKohdeId(Suunta.VASEN);
                HuoneEditoriIkkuna.warpOikea = HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaReunaWarppiTiedot(Suunta.OIKEA);
                HuoneEditoriIkkuna.warpOikeaHuoneId = HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaReunaWarpinKohdeId(Suunta.OIKEA);
                HuoneEditoriIkkuna.warpAlas = HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaReunaWarppiTiedot(Suunta.ALAS);
                HuoneEditoriIkkuna.warpAlasHuoneId = HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaReunaWarpinKohdeId(Suunta.ALAS);
                HuoneEditoriIkkuna.warpYlös = HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaReunaWarppiTiedot(Suunta.YLÖS);
                HuoneEditoriIkkuna.warpYlösHuoneId = HuoneEditoriIkkuna.huoneKartta.get(uusiHuoneLatausSäie).annaReunaWarpinKohdeId(Suunta.YLÖS);
        
                HuoneEditoriIkkuna.objektiEditointiKenttäPaneli.setPreferredSize(new Dimension(HuoneEditoriIkkuna.huoneenKoko * HuoneEditoriIkkuna.esineenKokoPx, HuoneEditoriIkkuna.huoneenKoko * HuoneEditoriIkkuna.esineenKokoPx));
                HuoneEditoriIkkuna.maastoEditointiKenttäPaneli.setPreferredSize(new Dimension(HuoneEditoriIkkuna.huoneenKoko * HuoneEditoriIkkuna.esineenKokoPx, HuoneEditoriIkkuna.huoneenKoko * HuoneEditoriIkkuna.esineenKokoPx));
                HuoneEditoriIkkuna.npcEditointiKenttäPaneli.setPreferredSize(new Dimension(HuoneEditoriIkkuna.huoneenKoko * HuoneEditoriIkkuna.esineenKokoPx, HuoneEditoriIkkuna.huoneenKoko * HuoneEditoriIkkuna.esineenKokoPx));
                if (HuoneEditoriIkkuna.huoneenKoko == 10) {
                    HuoneEditoriIkkuna.scrollattavaObjektiKenttäPaneli.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                    HuoneEditoriIkkuna.scrollattavaObjektiKenttäPaneli.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
                    HuoneEditoriIkkuna.scrollattavaMaastoKenttäPaneli.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                    HuoneEditoriIkkuna.scrollattavaMaastoKenttäPaneli.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
                    HuoneEditoriIkkuna.scrollattavaNPCKenttäPaneli.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                    HuoneEditoriIkkuna.scrollattavaNPCKenttäPaneli.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
                }
                else {
                    HuoneEditoriIkkuna.scrollattavaObjektiKenttäPaneli.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                    HuoneEditoriIkkuna.scrollattavaObjektiKenttäPaneli.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                    HuoneEditoriIkkuna.scrollattavaMaastoKenttäPaneli.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                    HuoneEditoriIkkuna.scrollattavaMaastoKenttäPaneli.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                    HuoneEditoriIkkuna.scrollattavaNPCKenttäPaneli.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                    HuoneEditoriIkkuna.scrollattavaNPCKenttäPaneli.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                }
                HuoneEditoriIkkuna.huoneenVaihtoNappiVasen.setEnabled(true);
                HuoneEditoriIkkuna.huoneenVaihtoNappiOikea.setEnabled(true);
                HuoneEditoriIkkuna.huoneInfoLabel.setEnabled(true);
                HuoneEditoriIkkuna.huoneenNimiLabel.setEnabled(true);
                käynnissä = false;
            }
            //System.out.println("Lataussäie vapauttaa lukon");
        }

        private void päivitäEditorinKoko(int uusiKoko) {
            if (uusiKoko != HuoneEditoriIkkuna.huoneenKoko) {
                HuoneEditoriIkkuna.huoneenKoko = uusiKoko;
                HuoneEditoriIkkuna.objektiKenttä = new KenttäKohde[uusiKoko][uusiKoko];
                HuoneEditoriIkkuna.maastoKenttä = new Maasto[uusiKoko][uusiKoko];
                HuoneEditoriIkkuna.npcKenttä = new Entity[uusiKoko][uusiKoko];
                HuoneEditoriIkkuna.kenttäKohteenKuvake = new JButton[uusiKoko][uusiKoko];
                HuoneEditoriIkkuna.maastoKohteenKuvakeObjektiPanelissa = new JLabel[uusiKoko][uusiKoko];
                HuoneEditoriIkkuna.maastoKohteenKuvakeNpcPanelissa = new JLabel[uusiKoko][uusiKoko];
                HuoneEditoriIkkuna.maastoKohteenKuvake = new JButton[uusiKoko][uusiKoko];
                HuoneEditoriIkkuna.npcKohteenKuvake = new JButton[uusiKoko][uusiKoko];
            }
        }

        private void uudelleenAlustaEditoriKenttä(int uusiKoko) {
            for (Component c : HuoneEditoriIkkuna.objektiEditointiKenttäPaneli.getComponents()) {
                HuoneEditoriIkkuna.objektiEditointiKenttäPaneli.remove(c);
            }
            HuoneEditoriIkkuna.objektiEditointiKenttäPaneli.add(new JLabel("ASD"));
            for (Component c : HuoneEditoriIkkuna.maastoEditointiKenttäPaneli.getComponents()) {
                HuoneEditoriIkkuna.maastoEditointiKenttäPaneli.remove(c);
            }
            HuoneEditoriIkkuna.maastoEditointiKenttäPaneli.add(new JLabel("ASD"));
            for (Component c : HuoneEditoriIkkuna.npcEditointiKenttäPaneli.getComponents()) {
                HuoneEditoriIkkuna.npcEditointiKenttäPaneli.remove(c);
            }
            HuoneEditoriIkkuna.npcEditointiKenttäPaneli.add(new JLabel("ASD"));
            
            int kohteenSijX = 0;
            int kohteensijY = 0;
            HuoneEditoriIkkuna.objektiKenttä = new KenttäKohde[uusiKoko][uusiKoko];
            HuoneEditoriIkkuna.maastoKenttä = new Maasto[uusiKoko][uusiKoko];
            HuoneEditoriIkkuna.npcKenttä = new Entity[uusiKoko][uusiKoko];
            HuoneEditoriIkkuna.kenttäKohteenKuvake = new JButton[uusiKoko][uusiKoko];
            HuoneEditoriIkkuna.maastoKohteenKuvake = new JButton[uusiKoko][uusiKoko];
            HuoneEditoriIkkuna.npcKohteenKuvake = new JButton[uusiKoko][uusiKoko];
            HuoneEditoriIkkuna.maastoKohteenKuvakeObjektiPanelissa = new JLabel[uusiKoko][uusiKoko];
            HuoneEditoriIkkuna.maastoKohteenKuvakeNpcPanelissa = new JLabel[uusiKoko][uusiKoko];
            edistyminen = 30; publish((double)edistyminen);
            
            for (int i = 0; i < uusiKoko; i++) {
                for (int j = 0; j < uusiKoko; j++) {
                    int x = j;
                    int y = i;
                    HuoneEditoriIkkuna.kenttäKohteenKuvake[x][y] = new JButton(new ImageIcon());
                    HuoneEditoriIkkuna.maastoKohteenKuvakeObjektiPanelissa[x][y] = new JLabel(new ImageIcon());
                    HuoneEditoriIkkuna.maastoKohteenKuvakeObjektiPanelissa[x][y].setName("maastokohteen_kuvake_objektipanelissa_" + x + "_" + y);
                    HuoneEditoriIkkuna.maastoKohteenKuvakeNpcPanelissa[x][y] = new JLabel(new ImageIcon());
                    HuoneEditoriIkkuna.maastoKohteenKuvakeNpcPanelissa[x][y].setName("maastokohteen_kuvake_npcpanelissa_" + x + "_" + y);
                    HuoneEditoriIkkuna.kenttäKohteenKuvake[x][y].addKeyListener(HuoneEditoriIkkuna.editorinNäppäinkomennot);
                    HuoneEditoriIkkuna.kenttäKohteenKuvake[x][y].setName("kenttäkohteen_kuvake_" + x + "_" + y);
                    HuoneEditoriIkkuna.kenttäKohteenKuvake[x][y].addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed (MouseEvent e) {
                            try {
                                HuoneEditoriIkkuna.muutoksiaTehty = true;
                                if (HuoneEditoriIkkuna.ctrlPainettu && SwingUtilities.isLeftMouseButton(e)) {
                                    if (HuoneEditoriIkkuna.objektiKenttä[x][y] != null) {
                                        HuoneEditoriIkkuna.kopioituOminaisuusLista = HuoneEditoriIkkuna.objektiKenttä[x][y].annalisäOminaisuudet();
                                        HuoneEditoriIkkuna.kopioituObjektinNimi = HuoneEditoriIkkuna.objektiKenttä[x][y].annaNimi();
                                    }
                                    else {
                                        HuoneEditoriIkkuna.kopioituObjektinNimi = "tyhjä";
                                    }
                                    HuoneEditoriIkkuna.käytäKopioitujaOminaisuuksia = true;
                                }
                                else if (SwingUtilities.isLeftMouseButton(e)) {
                                    if (HuoneEditoriIkkuna.käytäKopioitujaOminaisuuksia) {
                                        if (HuoneEditoriIkkuna.objektiKenttä[x][y] != null) {
                                            HuoneEditoriIkkuna.tallennaMuutos("objekti_aseta_" + HuoneEditoriIkkuna.kopioituObjektinNimi + "_x=" + x + "_y=" + y + "+ominaisuudet:[" + HuoneEditoriIkkuna.objektiKenttä[x][y].annaLisäOminaisuudetYhtenäMjonona() + "]");
                                        }
                                        else {
                                            HuoneEditoriIkkuna.tallennaMuutos("objekti_aseta_" + "tyhjä" + "_x=" + x + "_y=" + y + "+ominaisuudet:[" + "]");
                                        }
                                        HuoneEditoriIkkuna.asetaEsineRuutuun(x, y, HuoneEditoriIkkuna.kopioituObjektinNimi, HuoneEditoriIkkuna.kopioituOminaisuusLista);
                                    }
                                    else {
                                        System.out.println(HuoneEditoriIkkuna.kenttäkohdeLista[HuoneEditoriIkkuna.esineValikko.getSelectedIndex()]);
                                        if (HuoneEditoriIkkuna.kenttäkohdeLista[HuoneEditoriIkkuna.esineValikko.getSelectedIndex()].startsWith("Koriste-esine")) {
                                            String[] koristeEsineenOminaisuusLista = new String[1];
                                            koristeEsineenOminaisuusLista[0] = "kuva=" + HuoneEditoriIkkuna.koristeEsineenKuvaValikko.getSelectedItem();
                                            if (HuoneEditoriIkkuna.objektiKenttä[x][y] != null) {
                                                HuoneEditoriIkkuna.tallennaMuutos("objekti_aseta_" + HuoneEditoriIkkuna.objektiKenttä[x][y].annaNimi() + "_x=" + x + "_y=" + y + "+ominaisuudet:[" + HuoneEditoriIkkuna.objektiKenttä[x][y].annaLisäOminaisuudetYhtenäMjonona() + "]");
                                                HuoneEditoriIkkuna.asetaEsineRuutuun(x, y, HuoneEditoriIkkuna.kenttäkohdeLista[HuoneEditoriIkkuna.esineValikko.getSelectedIndex()], koristeEsineenOminaisuusLista);
                                            }
                                            else {
                                                HuoneEditoriIkkuna.tallennaMuutos("objekti_aseta_" + "tyhjä" + "_x=" + x + "_y=" + y + "+ominaisuudet:[" + "]");
                                                HuoneEditoriIkkuna.asetaEsineRuutuun(x, y, HuoneEditoriIkkuna.kenttäkohdeLista[HuoneEditoriIkkuna.esineValikko.getSelectedIndex()], koristeEsineenOminaisuusLista);
                                            }
                                        }
                                        else if (HuoneEditoriIkkuna.objektiKenttä[x][y] != null) {
                                            HuoneEditoriIkkuna.tallennaMuutos("objekti_aseta_" + HuoneEditoriIkkuna.objektiKenttä[x][y].annaNimi() + "_x=" + x + "_y=" + y + "+ominaisuudet:[" + "]");
                                            HuoneEditoriIkkuna.asetaEsineRuutuun(x, y, HuoneEditoriIkkuna.kenttäkohdeLista[HuoneEditoriIkkuna.esineValikko.getSelectedIndex()], null);
                                        }
                                        else {
                                            HuoneEditoriIkkuna.tallennaMuutos("objekti_aseta_" + "tyhjä" + "_x=" + x + "_y=" + y + "+ominaisuudet:[" + "]");
                                            HuoneEditoriIkkuna.asetaEsineRuutuun(x, y, HuoneEditoriIkkuna.kenttäkohdeLista[HuoneEditoriIkkuna.esineValikko.getSelectedIndex()], null);
                                        }
                                    }
                                }
                                else if (SwingUtilities.isRightMouseButton(e)) {
                                    HuoneEditoriIkkuna.hiirenSijainti = e.getPoint();
                                    HuoneEditoriIkkuna.hiiriLiikutettiin = false;
                                }
                                else if (SwingUtilities.isMiddleMouseButton(e)) {
                                    HuoneEditoriIkkuna.tallennaMuutos("objekti_poista_" + HuoneEditoriIkkuna.kenttäkohdeLista[HuoneEditoriIkkuna.esineValikko.getSelectedIndex()] + "_x=" + x + "_y=" + y + "+ominaisuudet:[" + HuoneEditoriIkkuna.objektiKenttä[x][y].annaLisäOminaisuudetYhtenäMjonona() + "]");
                                    HuoneEditoriIkkuna.asetaEsineRuutuun(x, y, "", null);
                                }
                            }
                            catch (NullPointerException npe) {
                                System.out.println("Ei ominaisuuksia (tyhjä ruutu)");
                                npe.printStackTrace();
                            }
                        }
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            if (!HuoneEditoriIkkuna.hiiriLiikutettiin) {
                                if (SwingUtilities.isRightMouseButton(e)) {
                                    JPopupMenu ominaisuusMenu = new JPopupMenu();
                                    for (JMenuItem mi : HuoneEditoriIkkuna.luoOikeaClickOminaisuusLista(HuoneEditoriIkkuna.objektiKenttä[x][y])) {
                                        ominaisuusMenu.add(mi);
                                    }
                                    ominaisuusMenu.show(e.getComponent(), e.getX(), e.getY());
                                }
                            }
                        }
                    });
                    HuoneEditoriIkkuna.kenttäKohteenKuvake[x][y].addMouseMotionListener(new MouseAdapter() {
                        public void mouseDragged(MouseEvent e) {
                            if (SwingUtilities.isRightMouseButton(e)) {
                                HuoneEditoriIkkuna.hiiriLiikutettiin = true;
                                if (HuoneEditoriIkkuna.hiirenSijainti != null) {
                                    JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, HuoneEditoriIkkuna.objektiEditointiKenttäPaneli);
                                    if (viewPort != null) {
                                        int deltaX = HuoneEditoriIkkuna.hiirenSijainti.x - e.getX();
                                        int deltaY = HuoneEditoriIkkuna.hiirenSijainti.y - e.getY();

                                        Rectangle view = viewPort.getViewRect();
                                        view.x += deltaX;
                                        view.y += deltaY;

                                        HuoneEditoriIkkuna.objektiEditointiKenttäPaneli.scrollRectToVisible(view);
                                        HuoneEditoriIkkuna.maastoEditointiKenttäPaneli.scrollRectToVisible(view);
                                        HuoneEditoriIkkuna.npcEditointiKenttäPaneli.scrollRectToVisible(view);
                                    }
                                }
                            }
                        }
                    });
                    HuoneEditoriIkkuna.maastoKohteenKuvake[j][i] = new JButton();
                    HuoneEditoriIkkuna.maastoKohteenKuvake[x][y].addKeyListener(HuoneEditoriIkkuna.editorinNäppäinkomennot);
                    HuoneEditoriIkkuna.maastoKohteenKuvake[x][y].setName("maastokohteen_kuvake_" + x + "_" + y);
                    HuoneEditoriIkkuna.maastoKohteenKuvake[x][y].addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed (MouseEvent e) {
                            try {
                                HuoneEditoriIkkuna.muutoksiaTehty = true;
                                if (HuoneEditoriIkkuna.ctrlPainettu && SwingUtilities.isLeftMouseButton(e)) {
                                    HuoneEditoriIkkuna.kopioituOminaisuusLista = HuoneEditoriIkkuna.maastoKenttä[x][y].annaLisäOminaisuudet();
                                    HuoneEditoriIkkuna.käytäKopioitujaOminaisuuksia = true;
                                }
                                else if (SwingUtilities.isLeftMouseButton(e)) {
                                    String[] ominaisuusLista = new String[1];
                                    if (HuoneEditoriIkkuna.maastoKenttä[x][y] == null) {
                                        HuoneEditoriIkkuna.tallennaMuutos("maasto_aseta_" + "Tile" + "_x=" + x + "_y=" + y + "+ominaisuudet:[" + "]");
                                    }
                                    else {
                                        HuoneEditoriIkkuna.tallennaMuutos("maasto_aseta_" + "Tile" + "_x=" + x + "_y=" + y + "+ominaisuudet:[" + HuoneEditoriIkkuna.maastoKenttä[x][y].annaLisäOminaisuudetYhtenäMjonona() + "]");
                                    }
                                    if (HuoneEditoriIkkuna.kopioituOminaisuusLista != null && HuoneEditoriIkkuna.käytäKopioitujaOminaisuuksia) {
                                        ominaisuusLista = HuoneEditoriIkkuna.kopioituOminaisuusLista;
                                    }
                                    else {
                                        ominaisuusLista = new String[1];
                                        ominaisuusLista[0] = "kuva=" + HuoneEditoriIkkuna.maastoValikko.getSelectedItem();
                                    }
                                    if (ominaisuusLista[0].endsWith("_e.png")) {
                                        HuoneEditoriIkkuna.asetaMaastoRuutuun(x, y, "EsteTile", ominaisuusLista);
                                    }
                                    else if (ominaisuusLista[0].endsWith("_y.png")) {
                                        HuoneEditoriIkkuna.asetaMaastoRuutuun(x, y, "Yksisuuntainen Tile", ominaisuusLista);
                                    }
                                    else{
                                        HuoneEditoriIkkuna.asetaMaastoRuutuun(x, y, "Tile", ominaisuusLista);
                                    }
                                }
                                else if (SwingUtilities.isRightMouseButton(e)) {
                                    HuoneEditoriIkkuna.hiirenSijainti = e.getPoint();
                                    HuoneEditoriIkkuna.hiiriLiikutettiin = false;
                                }
                                else if (SwingUtilities.isMiddleMouseButton(e)) {
                                    String[] ominaisuusLista = new String[1];
                                    ominaisuusLista[0] = "kuva=" + HuoneEditoriIkkuna.maastoValikko.getSelectedItem();
                                    HuoneEditoriIkkuna.tallennaMuutos("maasto_poista_" + "Tile" + "_x=" + x + "_y=" + y + "+ominaisuudet:[" + HuoneEditoriIkkuna.maastoKenttä[x][y].annaLisäOminaisuudetYhtenäMjonona() + "]");
                                    HuoneEditoriIkkuna.asetaMaastoRuutuun(x, y, "", ominaisuusLista);
                                }
                            }
                            catch (NullPointerException npe) {
                                System.out.println("Ei ominaisuuksia (tyhjä ruutu)");
                                npe.printStackTrace();
                            }
                        }

                        @Override
                        public void mouseReleased(MouseEvent e) {
                            if (!HuoneEditoriIkkuna.hiiriLiikutettiin) {
                                if (SwingUtilities.isRightMouseButton(e)) {
                                    JPopupMenu ominaisuusMenu = new JPopupMenu();
                                    for (JMenuItem mi : HuoneEditoriIkkuna.luoOikeaClickOminaisuusLista(HuoneEditoriIkkuna.maastoKenttä[x][y])) {
                                        ominaisuusMenu.add(mi);
                                    }
                                    ominaisuusMenu.show(e.getComponent(), e.getX(), e.getY());
                                }
                            }
                        }
                    });
                    HuoneEditoriIkkuna.maastoKohteenKuvake[x][y].addMouseMotionListener(new MouseAdapter() {
                        public void mouseDragged(MouseEvent e) {
                            if (SwingUtilities.isRightMouseButton(e)) {
                                HuoneEditoriIkkuna.hiiriLiikutettiin = true;
                                if (HuoneEditoriIkkuna.hiirenSijainti != null) {
                                    JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, HuoneEditoriIkkuna.maastoEditointiKenttäPaneli);
                                    if (viewPort != null) {
                                        int deltaX = HuoneEditoriIkkuna.hiirenSijainti.x - e.getX();
                                        int deltaY = HuoneEditoriIkkuna.hiirenSijainti.y - e.getY();

                                        Rectangle view = viewPort.getViewRect();
                                        view.x += deltaX;
                                        view.y += deltaY;

                                        HuoneEditoriIkkuna.objektiEditointiKenttäPaneli.scrollRectToVisible(view);
                                        HuoneEditoriIkkuna.maastoEditointiKenttäPaneli.scrollRectToVisible(view);
                                        HuoneEditoriIkkuna.npcEditointiKenttäPaneli.scrollRectToVisible(view);
                                    }
                                }
                            }
                        }
                    });

                    HuoneEditoriIkkuna.npcKohteenKuvake[x][y] = new JButton(new ImageIcon());
                    HuoneEditoriIkkuna.npcKohteenKuvake[x][y].addKeyListener(HuoneEditoriIkkuna.editorinNäppäinkomennot);
                    HuoneEditoriIkkuna.npcKohteenKuvake[x][y].setName("npckohteen_kuvake_" + x + "_" + y);
                    HuoneEditoriIkkuna.npcKohteenKuvake[x][y].addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed (MouseEvent e) {
                            HuoneEditoriIkkuna.hiirenSijainti = e.getPoint();
                            try {
                                HuoneEditoriIkkuna.muutoksiaTehty = true;
                                if (SwingUtilities.isLeftMouseButton(e)) {
                                    String[] ominaisuusLista = new String[1];
                                    ominaisuusLista[0] = "liiketapa=" + LiikeTapa.LOOP_NELIÖ_MYÖTÄPÄIVÄÄN;
                                    HuoneEditoriIkkuna.asetaNPCRuutuun(x, y, HuoneEditoriIkkuna.entityLista[HuoneEditoriIkkuna.npcValikko.getSelectedIndex()], ominaisuusLista);
                                    HuoneEditoriIkkuna.tallennaMuutos("npc_aseta_" + HuoneEditoriIkkuna.entityLista[HuoneEditoriIkkuna.npcValikko.getSelectedIndex()] + "_x=" + x + "_y=" + y + "+ominaisuudet:[" + HuoneEditoriIkkuna.npcKenttä[x][y].annaLisäOminaisuudetYhtenäMjonona() + "]");
                                }
                                else if (SwingUtilities.isRightMouseButton(e)) {
                                    HuoneEditoriIkkuna.hiirenSijainti = e.getPoint();
                                    HuoneEditoriIkkuna.hiiriLiikutettiin = false;
                                }
                                else if (SwingUtilities.isMiddleMouseButton(e)) {
                                    String[] ominaisuusLista = new String[1];
                                    ominaisuusLista[0] = "liiketapa=" + LiikeTapa.LOOP_NELIÖ_MYÖTÄPÄIVÄÄN;
                                    HuoneEditoriIkkuna.tallennaMuutos("npc_poista_" + HuoneEditoriIkkuna.entityLista[HuoneEditoriIkkuna.npcValikko.getSelectedIndex()] + "_x=" + x + "_y=" + y + "+ominaisuudet:[" + HuoneEditoriIkkuna.npcKenttä[x][y].annaLisäOminaisuudetYhtenäMjonona() + "]");
                                    HuoneEditoriIkkuna.asetaNPCRuutuun(x, y, "", ominaisuusLista);
                                }
                            }
                            catch (NullPointerException npe) {
                                System.out.println("Ei ominaisuuksia (tyhjä ruutu)");
                                npe.printStackTrace();
                            }
                        }
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            if (!HuoneEditoriIkkuna.hiiriLiikutettiin) {
                                if (SwingUtilities.isRightMouseButton(e)) {
                                    JPopupMenu ominaisuusMenu = new JPopupMenu();
                                    for (JMenuItem mi : HuoneEditoriIkkuna.luoOikeaClickOminaisuusLista(HuoneEditoriIkkuna.npcKenttä[x][y])) {
                                        ominaisuusMenu.add(mi);
                                    }
                                    ominaisuusMenu.show(e.getComponent(), e.getX(), e.getY());
                                }
                            }
                        }
                    });
                    HuoneEditoriIkkuna.npcKohteenKuvake[x][y].addMouseMotionListener(new MouseAdapter() {
                        public void mouseDragged(MouseEvent e) {
                            if (SwingUtilities.isRightMouseButton(e)) {
                                HuoneEditoriIkkuna.hiiriLiikutettiin = true;
                                if (HuoneEditoriIkkuna.hiirenSijainti != null) {
                                    JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, HuoneEditoriIkkuna.npcEditointiKenttäPaneli);
                                    if (viewPort != null) {
                                        int deltaX = HuoneEditoriIkkuna.hiirenSijainti.x - e.getX();
                                        int deltaY = HuoneEditoriIkkuna.hiirenSijainti.y - e.getY();

                                        Rectangle view = viewPort.getViewRect();
                                        view.x += deltaX;
                                        view.y += deltaY;

                                        HuoneEditoriIkkuna.objektiEditointiKenttäPaneli.scrollRectToVisible(view);
                                        HuoneEditoriIkkuna.maastoEditointiKenttäPaneli.scrollRectToVisible(view);
                                        HuoneEditoriIkkuna.npcEditointiKenttäPaneli.scrollRectToVisible(view);
                                    }
                                }
                            }
                        }
                    });
                }
            }
            edistyminen = 40; publish((double)edistyminen);
            double edistymisLisä = 50d/uusiKoko;
            try {
                for (int i = 0; i < uusiKoko; i++) {
                    edistyminen += edistymisLisä;
                    publish((double)edistyminen);
                    for (int j = 0; j < uusiKoko; j++) {
                        if (HuoneEditoriIkkuna.kenttäKohteenKuvake[j][i] == null) {

                        }
                        else {
                            if (HuoneEditoriIkkuna.reunatNäkyvissä) {
                                if (HuoneEditoriIkkuna.objektiKenttä[j][i] instanceof KenttäKohde) {
                                    if (Pelaaja.sijX == j && Pelaaja.sijY == i) {
                                        HuoneEditoriIkkuna.kenttäKohteenKuvake[j][i].setBorder(null);
                                    }
                                    else if (HuoneEditoriIkkuna.objektiKenttä[j][i] instanceof KenttäKohde) {
                                        HuoneEditoriIkkuna.kenttäKohteenKuvake[j][i].setIcon(Peli.annaObjektiKenttä()[j][i].annaKuvake());
                                        if (HuoneEditoriIkkuna.objektiKenttä[j][i] instanceof Kiintopiste) {
                                            HuoneEditoriIkkuna.kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(0,255,0), 1, true));
                                        }
                                        else if (HuoneEditoriIkkuna.objektiKenttä[j][i] instanceof Esine) {
                                            HuoneEditoriIkkuna.kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(0,0,255), 1, true));
                                        }
                                        else if (HuoneEditoriIkkuna.objektiKenttä[j][i] instanceof NPC_KenttäKohde) {
                                            HuoneEditoriIkkuna.kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(200,200,0), 1, true));
                                            
                                        }
                                        else if (HuoneEditoriIkkuna.objektiKenttä[j][i] instanceof Warp) {
                                            HuoneEditoriIkkuna.kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(new Color(80,200,0), 1, true));
                                        }
                                    }
                                }
                                else if (HuoneEditoriIkkuna.objektiKenttä[j][i] == null) {
                                    HuoneEditoriIkkuna.kenttäKohteenKuvake[j][i].setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
                                }
                            }
                            else {
                                HuoneEditoriIkkuna.kenttäKohteenKuvake[j][i].setBorder(null);
                            }
                            if (HuoneEditoriIkkuna.objektiKenttä[j][i] instanceof KenttäKohde) {
                                HuoneEditoriIkkuna.kenttäKohteenKuvake[j][i].setIcon(HuoneEditoriIkkuna.objektiKenttä[j][i].annaKuvake());
                            }
                            if (HuoneEditoriIkkuna.maastoKenttä[j][i] instanceof Maasto) {
                                Maasto m = HuoneEditoriIkkuna.maastoKenttä[j][i];
                                HuoneEditoriIkkuna.maastoKohteenKuvake[j][i].setIcon(HuoneEditoriIkkuna.maastoKenttä[j][i].annaKuvake());
                                HuoneEditoriIkkuna.maastoKohteenKuvakeObjektiPanelissa[j][i].setIcon(new KäännettäväKuvake(m.annaKuvake(), 0, false, false, 64, 0.5f));
                                HuoneEditoriIkkuna.maastoKohteenKuvakeNpcPanelissa[j][i].setIcon(new KäännettäväKuvake(m.annaKuvake(), 0, false, false, 64, 0.5f));
                            }
                        
                            HuoneEditoriIkkuna.kenttäKohteenKuvake[j][i].setBounds(kohteenSijX, kohteensijY, esineenKokoPx, esineenKokoPx);
                            HuoneEditoriIkkuna.kenttäKohteenKuvake[j][i].setOpaque(false);
                            HuoneEditoriIkkuna.kenttäKohteenKuvake[j][i].setContentAreaFilled(false);
                            HuoneEditoriIkkuna.kenttäKohteenKuvake[j][i].setBorderPainted(false);
                            HuoneEditoriIkkuna.maastoKohteenKuvake[j][i].setBounds(kohteenSijX, kohteensijY, esineenKokoPx, esineenKokoPx);
                            HuoneEditoriIkkuna.npcKohteenKuvake[j][i].setBounds(kohteenSijX, kohteensijY, esineenKokoPx, esineenKokoPx);
                            HuoneEditoriIkkuna.npcKohteenKuvake[j][i].setOpaque(false);
                            HuoneEditoriIkkuna.npcKohteenKuvake[j][i].setContentAreaFilled(false);
                            HuoneEditoriIkkuna.npcKohteenKuvake[j][i].setBorderPainted(false);
                            HuoneEditoriIkkuna.maastoKohteenKuvakeObjektiPanelissa[j][i].setBounds(kohteenSijX, kohteensijY, esineenKokoPx, esineenKokoPx);
                            HuoneEditoriIkkuna.maastoKohteenKuvakeNpcPanelissa[j][i].setBounds(kohteenSijX, kohteensijY, esineenKokoPx, esineenKokoPx);

                            HuoneEditoriIkkuna.objektiEditointiKenttäPaneli.add(HuoneEditoriIkkuna.kenttäKohteenKuvake[j][i]);
                            HuoneEditoriIkkuna.objektiEditointiKenttäPaneli.add(HuoneEditoriIkkuna.maastoKohteenKuvakeObjektiPanelissa[j][i]);
                            HuoneEditoriIkkuna.maastoEditointiKenttäPaneli.add(HuoneEditoriIkkuna.maastoKohteenKuvake[j][i]);
                            HuoneEditoriIkkuna.npcEditointiKenttäPaneli.add(HuoneEditoriIkkuna.npcKohteenKuvake[j][i]);
                            HuoneEditoriIkkuna.npcEditointiKenttäPaneli.add(HuoneEditoriIkkuna.maastoKohteenKuvakeNpcPanelissa[j][i]);
                            kohteenSijX += esineenKokoPx;
                        }
                    }
                    kohteenSijX = 0;
                    kohteensijY += esineenKokoPx;
                }
            }
            catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Virheellinen kentän koko.");
                e.printStackTrace();
            }
            catch (IllegalArgumentException e) {

            }

            HuoneEditoriIkkuna.objektiEditointiKenttäPaneli.revalidate();
            HuoneEditoriIkkuna.objektiEditointiKenttäPaneli.repaint();
        }
      
        @Override
        protected String doInBackground() throws Exception {
            try {
                publish((double)edistyminen);
                lataaHuone();
                edistyminen = 100;
                publish((double)edistyminen);
                Thread.sleep(50);
            }
            catch (ArrayIndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(null, "Virheellinen huonelista.\n\nHäire sovelluksessa.", "Array Index out of Bounds", JOptionPane.ERROR_MESSAGE);
            }
            catch (NullPointerException e) {
                System.out.println("Ohitetaan tyhjät indeksit");
            }
            catch (InterruptedException ie) {
                System.out.println("Lataus keskeytetty.");
                ie.printStackTrace();
            }
            return "Ladattu";
        }
      
        @Override
        protected void process(List<Double> aDoubles) {
            int edistymisProsentti = (int)(double)aDoubles.get( aDoubles.size() - 1 );
            edistymisTeksti.setText("Ladataan huonetta..." + edistymisProsentti + "%");
            edistymisPalkki.setValue(edistymisProsentti);
        }
      
        @Override
        protected void done() {
            ikkuna.dispose();
            HuoneEditoriIkkuna.ikkuna.setVisible(true);
        }
    }
}
