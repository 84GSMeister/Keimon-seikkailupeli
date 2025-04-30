package keimo.kenttäkohteet.kenttäNPC;

import keimo.keimoEngine.grafiikat.Tekstuuri;

public class Kuuhahmo3 extends NPC_KenttäKohde{
    
    public Kuuhahmo3(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Kuuhahmo3";
        super.tiedostonNimi = "kuuhahmo_3.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.katsomisTeksti = "Yee!";
        super.asetaTiedot();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        return "sijamuotojen määrittely puuttuu";
    }
}
