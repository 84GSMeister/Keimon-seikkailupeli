package keimo.seikkailupeli.objektit.kenttäkohteet.kiintopiste;

import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.assets.TavoiteLista;

import java.util.ArrayList;

public final class Kirstu extends Säiliö {

    boolean avattu;

    public Kirstu(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.nimi = "Kirstu";
        super.tiedostonNimi = "kirstu.png";
        super.tekstuuri = Assets.annaTekstuuri("kirstu");
        super.katsomisTeksti = "Kirstu on lukittu. Minneköhän sen avain on unohtunut?";
        super.asetaTiedot();
    }

    @Override
    public String katso(){
        if (super.tavoiteSuoritettu) {    
            return "Avattu kirstu";
        }
        else {
            return "Kirstu on lukittu. Minneköhän sen avain on unohtunut?";
        }
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Kirstu";
            case "genetiivi":    return "Kirstun";
            case "essiivi":      return "Kirstuna";
            case "partitiivi":   return "Kirstua";
            case "translatiivi": return "Kirstuksi";
            case "inessiivi":    return "Kirstussa";
            case "elatiivi":     return "Kirstusta";
            case "illatiivi":    return "Kirstuun";
            case "adessiivi":    return "Kirstulla";
            case "ablatiivi":    return "Kirstulta";
            case "allatiivi":    return "Kirstulle";
            default:             return "Kirstu";
        }
    }

    @Override
    public String haeDialogiTeksti(String teksti) {
        switch (teksti) {
            case "avaa": return "Kirstu avattiin. Sait " + sisältö.annaNimiSijamuodossa("genetiivi");
            case null, default: return katso();
        }
    }

    public void avaa() {
        super.tavoiteSuoritettu = true;
        super.tekstuuri = Assets.annaTekstuuri("kirstu_avattu");
        this.avattu = true;
        TavoiteLista.tarkistaTavoiteEsine(this.luoSisältö(this.annaSisältö(), null));
    }

    public boolean onkoAvattu() {
        return avattu;
    }
}
