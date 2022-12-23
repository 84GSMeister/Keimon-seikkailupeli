import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

import java.awt.Image;

public class Huone {
    
    private int id;
    private String nimi;
    private int huoneenKoko;
    private KenttäKohde[][] huoneenSisältö;
    private Image tausta;
    int esineitäKentällä = 0;

    static Random r = new Random();

    public int annaId() {
        return id;
    }

    public String annaNimi() {
        return nimi;
    }

    public int annaKoko() {
        return huoneenKoko;
    }

    public Image annaTausta() {
        return tausta;
    }
    
    public KenttäKohde[][] annaHuoneenSisältö() {
        return huoneenSisältö;
    }

    void sijoitaSatunnaiseenRuutuun(KenttäKohde t){
        int randX = r.nextInt(Main.kentänKoko);
        int randY = r.nextInt(Main.kentänKoko);
        if (huoneenSisältö[randX][randY] == null) {
            huoneenSisältö[randX][randY] = t;
            esineitäKentällä++;
        }
        else {
            if (esineitäKentällä < Main.kentänKoko * Main.kentänKoko) {
                sijoitaSatunnaiseenRuutuun(t);
            }
            else {
                //JOptionPane.showMessageDialog(null, "Esineiden määrä yli kentän koon.\n\nViimeisimpänä spawnattu esine hylätään.", "Kenttä täynnä esineitä", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    void sijoitaMäärättyynRuutuun(int sijX, int sijY, KenttäKohde t){
        if (huoneenSisältö[sijX][sijY] == null) {
            huoneenSisältö[sijX][sijY] = t;
            esineitäKentällä++;
        }
        else {
            if (esineitäKentällä < Main.kentänKoko * Main.kentänKoko) {
                //sijoitaMäärättyynRuutuun(sijX, sijY, t);
            }
            else {
                //JOptionPane.showMessageDialog(null, "Esineiden määrä yli kentän koon.\n\nViimeisimpänä spawnattu esine hylätään.", "Kenttä täynnä esineitä", JOptionPane.WARNING_MESSAGE);
            }
            JOptionPane.showMessageDialog(null, "Ei voi sijoittaa ruutuun, jossa on jo jotakin.", "Virheellinen sijainti.", JOptionPane.ERROR_MESSAGE);
        }
    }

    Huone(int luontiId, int luontiKoko, String luontiNimi, Image luontiTausta, ArrayList<KenttäKohde> luontiSisältö) {
        this.id = luontiId;
        this.nimi = luontiNimi;
        this.huoneenKoko = luontiKoko;
        this.huoneenSisältö = HuoneLista.huoneet(luontiId);
        this.tausta = luontiTausta;

        for (int i = 0; i < huoneenSisältö.length; i++) {
            for (int j = 0; j < huoneenSisältö.length; j++) {
                this.huoneenSisältö[j][i] = null;
            }
        }
        for (KenttäKohde k : luontiSisältö) {
            if (k.määritettySijainti) {
                sijoitaMäärättyynRuutuun(k.sijX, k.sijY, k);
            }
            else {
                sijoitaSatunnaiseenRuutuun(k);
            }
        }
        Main.huoneidenMäärä++;
    }

}
