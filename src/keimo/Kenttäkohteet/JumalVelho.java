package keimo.Kenttäkohteet;

import keimo.PääIkkuna;
import keimo.TavoiteLista;
import keimo.Utility.KäännettäväKuvake;

import javax.swing.ImageIcon;

public final class JumalVelho extends NPC_KenttäKohde {
    
    @Override
    public String kokeileEsinettä(Esine e) {
        this.näytäDialogi(e);
        return katsomisTeksti;
    }

    @Override
    public void näytäDialogi(Esine e) {
        TavoiteLista.suoritaTavoite("Löydä Jumal Yoda");
        if (TavoiteLista.tavoiteLista.get("Keitä booli")) {
            PääIkkuna.avaaDialogi(super.dialogiKuvake, "...", super.nimi);
        }
        else {
            PääIkkuna.avaaDialogi(super.dialogiKuvake, "Tervetuloa kaikki mun bordello dello dello dello dello dello dello delloon", super.nimi);
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

    public JumalVelho(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Jumal Velho";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/velho.png");
        super.dialogiKuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/velho_dialogi.png");
        super.skaalattuKuvake = new KäännettäväKuvake(kuvake, 0, false, false, 96);
        super.katsomisTeksti = "No se on se Jumal Velho!";
        super.asetaTiedot();
    }
}
