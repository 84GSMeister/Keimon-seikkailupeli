package keimo;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import keimo.Kenttäkohteet.*;
import keimo.Maastot.*;
import keimo.NPCt.*;
import keimo.Kenttäkohteet.Warp.Suunta;

public class Huone {
    
    private int id;
    private String nimi;
    private int huoneenKoko;
    private KenttäKohde[][] huoneenKenttäSisältö;
    private Maasto[][] huoneenMaastoSisältö;
    private NPC[][] huoneenNPCSisältö;
    private ImageIcon tausta;
    private String taustanPolku;
    private String alue;
    public int npcidenMäärä;
    int esineitäKentällä = 0;
    int maastoaKentällä = 0;
    int npcitäKentällä = 0;
    public boolean lataaTarinaRuutu = false;
    public String tarinaRuudunTunniste = "";
    boolean warpVasen = false;
    boolean warpOikea = false;
    boolean warpAlas = false;
    boolean warpYlös = false;
    int warpVasenHuoneId = 0;
    int warpOikeaHuoneId = 0;
    int warpAlasHuoneId = 0;
    int warpYlösHuoneId = 0;

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

    public ImageIcon annaTausta() {
        return tausta;
    }

    public String annaTaustanPolku() {
        return taustanPolku;
    }

    public String annaAlue() {
        return alue;
    }
    
    public KenttäKohde[][] annaHuoneenKenttäSisältö() {
        return huoneenKenttäSisältö;
    }

    public Maasto[][] annaHuoneenMaastoSisältö() {
        return huoneenMaastoSisältö;
    }

    public NPC[][] annaHuoneenNPCSisältö() {
        return huoneenNPCSisältö;
    }

    public void päivitäNimiJaAlue(String nimi, String alue) {
        this.nimi = nimi;
        this.alue = alue;
    }

    public void päivitäTausta(String taustaString){
        this.taustanPolku = taustaString;
        this.tausta = new ImageIcon(taustaString);
    }

    public void päivitäHuoneenKenttäSisältö(KenttäKohde[][] k) {
        this.huoneenKenttäSisältö = k;
    }

    public void päivitäHuoneenMaastoSisältö(Maasto[][] m) {
        this.huoneenMaastoSisältö = m;
    }

    public void päivitäHuoneenNPCSisältö(NPC[][] n) {
        this.huoneenNPCSisältö = n;
    }

    public void päivitäReunawarppienTiedot(boolean warpVasen, int warpVasenHuoneId, boolean warpOikea, int warpOikeaHuoneId, boolean warpAlas, int warpAlasHuoneId, boolean warpYlös, int warpYlösHuoneId) {
        this.warpVasen = warpVasen;
        this.warpVasenHuoneId = warpVasenHuoneId;
        this.warpOikea = warpOikea;
        this.warpOikeaHuoneId = warpOikeaHuoneId;
        this.warpAlas = warpAlas;
        this.warpAlasHuoneId = warpAlasHuoneId;
        this.warpYlös = warpYlös;
        this.warpYlösHuoneId = warpYlösHuoneId;
    }

    public boolean annaReunaWarppiTiedot(Suunta suunta) {
        switch (suunta) {
            case VASEN:
                return warpVasen;
            case OIKEA:
                return warpOikea;
            case YLÖS:
                return warpYlös;
            case ALAS:
                return warpAlas;
            default:
                return false;
        }
    }

    public int annaReunaWarpinKohdeId(Suunta suunta) {
        switch (suunta) {
            case VASEN:
                return warpVasenHuoneId;
            case OIKEA:
                return warpOikeaHuoneId;
            case YLÖS:
                return warpYlösHuoneId;
            case ALAS:
                return warpAlasHuoneId;
            default:
                return 0;
        }
    }

    /**
     * Arpoo satunnaisesti pelikentän x- ja y-koordinaatit.
     * Lisää arvottuun kohtaan syötteenä saadun KenttäKohde-tyyppisen olion
     * eli jonkin Esine-luokan tai Kiintopiste-luokan alaluokan olioista.
     * @param k KenttäKohde-objekti, jonka sijainti arvotaan
     * @.pre {k instanceof KenttäKohde}
     * @.post pelikenttä[randX][randY] != null
     */

    void sijoitaSatunnaiseenRuutuun(KenttäKohde k){
        int randX = r.nextInt(Peli.kentänKoko);
        int randY = r.nextInt(Peli.kentänKoko);
        if (huoneenKenttäSisältö[randX][randY] == null) {
            huoneenKenttäSisältö[randX][randY] = k;
            esineitäKentällä++;
        }
        else {
            if (esineitäKentällä < Peli.kentänKoko * Peli.kentänKoko) {
                sijoitaSatunnaiseenRuutuun(k);
            }
            else {
                //JOptionPane.showMessageDialog(null, "Esineiden määrä yli kentän koon.\n\nViimeisimpänä spawnattu esine hylätään.", "Kenttä täynnä esineitä", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    void sijoitaSatunnaiseenRuutuun(Maasto m){
        int randX = r.nextInt(Peli.kentänKoko);
        int randY = r.nextInt(Peli.kentänKoko);
        if (huoneenMaastoSisältö[randX][randY] == null) {
            huoneenMaastoSisältö[randX][randY] = m;
            maastoaKentällä++;
        }
        else {
            if (esineitäKentällä < Peli.kentänKoko * Peli.kentänKoko) {
                sijoitaSatunnaiseenRuutuun(m);
            }
            else {
                //JOptionPane.showMessageDialog(null, "Esineiden määrä yli kentän koon.\n\nViimeisimpänä spawnattu esine hylätään.", "Kenttä täynnä esineitä", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    void sijoitaSatunnaiseenRuutuun(NPC n){
        int randX = r.nextInt(Peli.kentänKoko);
        int randY = r.nextInt(Peli.kentänKoko);
        if (huoneenNPCSisältö[randX][randY] == null) {
            huoneenNPCSisältö[randX][randY] = n;
            npcitäKentällä++;
        }
        else {
            if (npcitäKentällä < Peli.kentänKoko * Peli.kentänKoko) {
                sijoitaSatunnaiseenRuutuun(n);
            }
            else {
                //JOptionPane.showMessageDialog(null, "Esineiden määrä yli kentän koon.\n\nViimeisimpänä spawnattu esine hylätään.", "Kenttä täynnä esineitä", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * Asettaa objektin pelikentälle x- ja y-koordinaatteihin.
     * Lisää valittuun kohtaan syötteenä saadun KenttäKohde-tyyppisen olion
     * eli jonkin Esine-luokan tai Kiintopiste-luokan alaluokan olioista.
     * @param k KenttäKohde-objekti, joka asetetaan kohteeseen
     * @param sijX x-koordinaatti
     * @param sijY y-koordinaatti
     * @.pre {k instanceof KenttäKohde && sijX >= 0 && sijX <= 9 && sijY >= 0 && sijY <= 9}
     * @.post pelikenttä[sijX][sijY] != null
     */

    void sijoitaMäärättyynRuutuun(int sijX, int sijY, KenttäKohde k){
        if (huoneenKenttäSisältö[sijX][sijY] == null) {
            huoneenKenttäSisältö[sijX][sijY] = k;
            esineitäKentällä++;
        }
        else {
            if (maastoaKentällä < Peli.kentänKoko * Peli.kentänKoko) {
                //sijoitaMäärättyynRuutuun(sijX, sijY, t);
            }
            else {
                //JOptionPane.showMessageDialog(null, "Esineiden määrä yli kentän koon.\n\nViimeisimpänä spawnattu esine hylätään.", "Kenttä täynnä esineitä", JOptionPane.WARNING_MESSAGE);
            }
            JOptionPane.showMessageDialog(null, "Ei voi sijoittaa ruutuun, jossa on jo jotakin.", "Virheellinen sijainti.", JOptionPane.ERROR_MESSAGE);
        }
    }

    void sijoitaMäärättyynRuutuun(int sijX, int sijY, Maasto m){
        if (huoneenMaastoSisältö[sijX][sijY] == null) {
            huoneenMaastoSisältö[sijX][sijY] = m;
            maastoaKentällä++;
        }
        else {
            if (maastoaKentällä < Peli.kentänKoko * Peli.kentänKoko) {
                //sijoitaMäärättyynRuutuun(sijX, sijY, t);
            }
            else {
                //JOptionPane.showMessageDialog(null, "Esineiden määrä yli kentän koon.\n\nViimeisimpänä spawnattu esine hylätään.", "Kenttä täynnä esineitä", JOptionPane.WARNING_MESSAGE);
            }
            JOptionPane.showMessageDialog(null, "Ei voi sijoittaa ruutuun, jossa on jo jotakin.", "Virheellinen sijainti.", JOptionPane.ERROR_MESSAGE);
        }
    }

    void sijoitaMäärättyynRuutuun(int sijX, int sijY, NPC n){
        if (huoneenNPCSisältö[sijX][sijY] == null) {
            huoneenNPCSisältö[sijX][sijY] = n;
            npcitäKentällä++;
        }
        else {
            if (npcitäKentällä < Peli.kentänKoko * Peli.kentänKoko) {
                //sijoitaMäärättyynRuutuun(sijX, sijY, t);
            }
            else {
                //JOptionPane.showMessageDialog(null, "Esineiden määrä yli kentän koon.\n\nViimeisimpänä spawnattu esine hylätään.", "Kenttä täynnä esineitä", JOptionPane.WARNING_MESSAGE);
            }
            JOptionPane.showMessageDialog(null, "Ei voi sijoittaa ruutuun, jossa on jo jotakin.", "Virheellinen sijainti.", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Huone(int luontiId, int luontiKoko, String luontiNimi, String luontiTaustanPolku, String luontiAlue, ArrayList<KenttäKohde> luontiKenttäSisältö, ArrayList<Maasto> luontiMaastoSisältö, ArrayList<NPC> luontiNPCSisältö, boolean lataaTarinaRuutu, String tarinaRuudunTunniste) {
        this.id = luontiId;
        this.nimi = luontiNimi;
        this.huoneenKoko = luontiKoko;
        this.huoneenKenttäSisältö = new KenttäKohde[Peli.kentänKoko][Peli.kentänKoko];
        this.huoneenMaastoSisältö = new Maasto[Peli.kentänKoko][Peli.kentänKoko];
        this.huoneenNPCSisältö = new NPC[Peli.kentänKoko][Peli.kentänKoko];
        this.tausta = new ImageIcon("tiedostot/kuvat/taustat/" + luontiTaustanPolku);
        this.taustanPolku = luontiTaustanPolku;
        this.lataaTarinaRuutu = lataaTarinaRuutu;
        this.tarinaRuudunTunniste = tarinaRuudunTunniste;
        this.alue = luontiAlue;

        try {

            for (int i = 0; i < huoneenKenttäSisältö.length; i++) {
                for (int j = 0; j < huoneenKenttäSisältö.length; j++) {
                    this.huoneenKenttäSisältö[j][i] = null;
                    this.huoneenMaastoSisältö[j][i] = null;
                }
            }
            for (KenttäKohde k : luontiKenttäSisältö) {
                if (k != null) {
                    if (k.onkoMääritettySijainti()) {
                        sijoitaMäärättyynRuutuun(k.annaSijX(), k.annaSijY(), k);
                    }
                    else {
                        sijoitaSatunnaiseenRuutuun(k);
                    }
                }
            }
            for (Maasto m : luontiMaastoSisältö) {
                if (m != null) {
                    if (m.onkoMääritettySijainti()) {
                        sijoitaMäärättyynRuutuun(m.annaSijX(), m.annaSijY(), m);
                    }
                    else {
                        sijoitaSatunnaiseenRuutuun(m);
                    }
                }
            }
            for (NPC n : luontiNPCSisältö) {
                if (n != null) {
                    if (n.onkoMääritettySijainti()) {
                        sijoitaMäärättyynRuutuun(n.annaAlkuSijX(), n.annaAlkuSijY(), n);
                    }
                    else {
                        sijoitaSatunnaiseenRuutuun(n);
                    }
                }
            }
        }
        catch (NullPointerException e) {
            if (luontiKenttäSisältö == null) {
                System.out.println("Kenttäkohteita ei voitu ladata tiedostosta huoneeseen " + id + ".");
            }
            else if (luontiMaastoSisältö == null) {
                System.out.println("Maastoa ei voitu ladata tiedostosta huoneeseen " + id + ".");
            }
            else if (luontiNPCSisältö == null) {
                System.out.println("NPC:itä ei voitu ladata tiedostosta huoneeseen " + id + ".");
            }
            else {
                System.out.println("Joitain elementtejä ei voitu ladata tiedostosta huoneeseen " + id + ".");
                e.printStackTrace();
            }
        }
    }

}
