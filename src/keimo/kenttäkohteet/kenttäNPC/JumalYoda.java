package keimo.kenttäkohteet.kenttäNPC;

import keimo.PääIkkuna;
import keimo.HuoneEditori.TavoiteEditori.TavoiteLista;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.kenttäkohteet.esine.Esine;

import javax.swing.ImageIcon;

public final class JumalYoda extends NPC_KenttäKohde {

    private Tekstuuri pimeäTekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/goblin.png");
    private Tekstuuri normaaliTekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/yoda.png");

    @Override
    public String kokeileEsinettä(Esine e) {
        this.näytäDialogi(e);
        return katsomisTeksti;
    }

    @Override
    public String katso() {
        return katsomisTeksti;
    }

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

    @Override
    public void näytäDialogi(Esine e) {
        if (super.onkoCustomDialogi()) {
            super.näytäDialogi(e);
        }
        else {
            if (TavoiteLista.tavoiteLista.get("Löydä Jumal Yoda")) {
                super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/yoda.png");
                super.dialogiKuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/yoda_dialogi.png");
                if (TavoiteLista.tavoiteLista.get("Avaa takahuone")) {
                    PääIkkuna.avaaDialogi(super.dialogiKuvake, "Hmm...mm...", super.nimi);
                }
                else {
                    PääIkkuna.avaaPitkäDialogiRuutu("goblin_alku");
                }
            }
            else {
                PääIkkuna.avaaDialogi(super.dialogiKuvake, "Hrmm...", "Goblin");
            }
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
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.tekstuuri = pimeäTekstuuri;
        super.dialogiKuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/goblin_dialogi.png");
        super.dialogiTekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/dialogi/goblin_dialogi.png");
        super.katsomisTeksti = "Polku pimeälle puolelle?";
        super.asetaTiedot();
    }
}
