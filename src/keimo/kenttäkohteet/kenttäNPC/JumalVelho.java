package keimo.kenttäkohteet.kenttäNPC;

import keimo.PääIkkuna;
import keimo.HuoneEditori.TavoiteEditori.TavoiteLista;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.kenttäkohteet.esine.Esine;

import javax.swing.ImageIcon;

public final class JumalVelho extends NPC_KenttäKohde {
    
    @Override
    public String kokeileEsinettä(Esine e) {
        this.näytäDialogi(e);
        return katsomisTeksti;
    }

    @Override
    public void näytäDialogi(Esine e) {
        if (super.onkoCustomDialogi()) {
            super.näytäDialogi(e);
        }
        else {
            TavoiteLista.suoritaTavoite("Löydä Jumal Yoda");
            if (TavoiteLista.tavoiteLista.get("Keitä booli")) {
                PääIkkuna.avaaDialogi(super.dialogiKuvake, "...", super.nimi);
            }
            else {
                PääIkkuna.avaaDialogi(super.dialogiKuvake, "Tervetuloa kaikki mun bordello dello dello dello dello dello dello delloon", super.nimi);
            }
        }
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi": return "Jumal Velho";
            case "genetiivi": return "Jumal Velhon";
            case "esiivi": return "Jumal Velhona";
            case "partitiivi": return "Jumal Velhoa";
            case "translatiivi": return "Jumal Velhoksi";
            case "inessiivi": return "Jumal Velhossa";
            case "elatiivi": return "Jumal Velhosta";
            case "illatiivi": return "Jumal Velhoon";
            case "adessiivi": return "Jumal Velholla";
            case "ablatiivi": return "Jumal Velholta";
            case "allatiivi": return "Jumal Velholle";
            default: return "Jumal Velho";
        }
    }

    @Override
    public String haeDialogiTeksti(String teksti) {
        switch (teksti) {
            case "löydä": return "Tervetuloa kaikki mun bordello dello dello dello dello dello dello delloon";
            case "anna_paskanmarjat": return "Näin se booli keittyy, paskanmarjat jaloviinaan peittyy.";
            case "booli_vinkki": return "Jumal velhon booli: tarvitaan vain oikeat ainekset (pontikkaa ja jallua)";
            case null, default: return katso();
        }
    }

    public void löydäJumalVelho() {
        TavoiteLista.suoritaTavoite("Löydä Jumal Yoda");
    }

    public boolean löydetty() {
        return TavoiteLista.tavoiteLista.get("Löydä Jumal Yoda");
    }

    public JumalVelho(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Jumal Velho";
        super.tiedostonNimi = "velho.png";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.dialogiKuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/velho_dialogi.png");
        super.dialogiTekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/dialogi/velho_dialogi.png");
        super.katsomisTeksti = "No se on se Jumal Velho!";
        super.asetaTiedot();
    }
}
