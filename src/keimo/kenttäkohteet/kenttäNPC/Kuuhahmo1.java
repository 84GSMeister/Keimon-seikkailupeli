package keimo.kenttäkohteet.kenttäNPC;

import keimo.keimoEngine.grafiikat.Tekstuuri;

public class Kuuhahmo1 extends NPC_KenttäKohde{
    
    public Kuuhahmo1(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Kuuhahmo1";
        super.tiedostonNimi = "kuuhahmo_1.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = "Yee!";
        super.asetaTiedot();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        return "sijamuotojen määrittely puuttuu";
    }
}
