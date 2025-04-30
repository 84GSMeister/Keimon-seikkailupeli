package keimo.kenttäkohteet.kenttäNPC;

import keimo.keimoEngine.grafiikat.Tekstuuri;

public final class JumalYoda extends NPC_KenttäKohde {

    private Tekstuuri pimeäTekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/goblin.png");
    private Tekstuuri normaaliTekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/yoda.png");

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        //Vaikee kuvitella, että näitäkään ikinä tarvis, mutta laitetaan ihan vaan perfektionismin tähden.
        switch (sijamuoto) {
            case "nominatiivi": return "Jumal Yoda";
            case "genetiivi": return "Jumal Yodan";
            case "esiivi": return "Jumal Yodana";
            case "partitiivi": return "Jumal Yodaa";
            case "translatiivi": return "Jumal Yodaksi";
            case "inessiivi": return "Jumal Yodassa";
            case "elatiivi": return "Jumal Yodasta";
            case "illatiivi": return "Jumal Yodaan";
            case "adessiivi": return "Jumal Yodalla";
            case "ablatiivi": return "Jumal Yodalta";
            case "allatiivi": return "Jumal Yodalle";
            default: return "Jumal Velho";
        }
    }

    public void löydä(boolean löydetty) {
        if (löydetty) this.tekstuuri = normaaliTekstuuri;
        else this.tekstuuri = pimeäTekstuuri;
    }

    public JumalYoda(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Jumal Yoda";
        super.tiedostonNimi = "goblin.png";
        super.tekstuuri = pimeäTekstuuri;
        super.dialogiTekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/dialogi/goblin_dialogi.png");
        super.katsomisTeksti = "Polku pimeälle puolelle?";
        super.asetaTiedot();
    }
}
