package keimo.seikkailupeli.objektit.kenttäkohteet.kenttäNPC;

import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.assets.TavoiteLista;
import keimo.seikkailupeli.objektit.Pelaaja;
import keimo.seikkailupeli.objektit.kenttäkohteet.esine.Esine;
import keimo.seikkailupeli.objektit.kenttäkohteet.esine.Jallupullo;
import keimo.seikkailupeli.objektit.kenttäkohteet.esine.Paskanmarjat;
import keimo.seikkailupeli.objektit.kenttäkohteet.esine.Ponuainekset;
import keimo.seikkailupeli.toiminnot.Dialogit;

import java.util.ArrayList;

public final class JumalVelho extends NPC_KenttäKohde {

    public JumalVelho(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.nimi = "Jumal Velho";
        super.tiedostonNimi = "velho.png";
        super.tekstuuri = Assets.annaTekstuuri("jumalvelho");
        super.dialogiTekstuuri = Assets.annaTekstuuri("jumalvelho_dialogi");
        super.katsomisTeksti = "No se on se Jumal Velho!";
        super.dialogit.add("metsä");
        super.dialogit.add("kuu");
        if (ominaisuusLista == null) super.valitseVakioDialogi();
        super.asetaTiedot();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Jumal Velho";
            case "genetiivi":    return "Jumal Velhon";
            case "essiivi":      return "Jumal Velhona";
            case "partitiivi":   return "Jumal Velhoa";
            case "translatiivi": return "Jumal Velhoksi";
            case "inessiivi":    return "Jumal Velhossa";
            case "elatiivi":     return "Jumal Velhosta";
            case "illatiivi":    return "Jumal Velhoon";
            case "adessiivi":    return "Jumal Velholla";
            case "ablatiivi":    return "Jumal Velholta";
            case "allatiivi":    return "Jumal Velholle";
            default:             return "Jumal Velho";
        }
    }

    @Override
    public void juttele() {
        switch (this.annaDialogi()) {
            case "metsä" -> {
                if (!this.löydetty()) {
                    this.löydäJumalVelho();
                    Dialogit.avaaDialogi(this.annaDialogiTekstuuri(), this.haeDialogiTeksti("löydä"), this.annaNimi());
                }
                else {
                    boolean ponuLöytyy = false;
                    boolean jalluLöytyy = false;
                    boolean paskanmarjatLöytyy = false;
                    for (Esine pelaajanEsine : Pelaaja.esineet) {
                        if (pelaajanEsine instanceof Ponuainekset) ponuLöytyy = true;
                        else if (pelaajanEsine instanceof Jallupullo) jalluLöytyy = true;
                        else if (pelaajanEsine instanceof Paskanmarjat) paskanmarjatLöytyy = true;
                    }
                    if (ponuLöytyy && jalluLöytyy) {
                        for (int i = 0; i < Pelaaja.esineet.length; i++) {
                            if (Pelaaja.esineet[i] instanceof Ponuainekset) {
                                Pelaaja.esineet[i] = new Paskanmarjat(0, 0);
                                break;
                            }
                        }
                        Dialogit.avaaDialogi(this.annaDialogiTekstuuri(), this.haeDialogiTeksti("anna_paskanmarjat"), this.annaNimi());
                    }
                    else if (jalluLöytyy && paskanmarjatLöytyy) {
                        Dialogit.avaaDialogi(this.annaDialogiTekstuuri(), this.haeDialogiTeksti("yhdistä_vinkki"), this.annaNimi());
                    }
                    else {
                        Dialogit.avaaDialogi(this.annaDialogiTekstuuri(), this.haeDialogiTeksti("booli_vinkki"), this.annaNimi());
                    }
                }
            }
            case "kuu" -> {
                Dialogit.avaaPitkäDialogiRuutu("velho_kuu");
            }
            case null, default -> {
                Dialogit.avaaDialogi("", "Objektille " + this.annaNimi() + " ei ole määritetty dialogia " + "\"" + this.annaDialogi() + "\".", "Virheellinen dialogi");
            }
        }
    }

    @Override
    public String haeDialogiTeksti(String teksti) {
        switch (teksti) {
            case "löydä": return "Tervetuloa kaikki mun bordello dello dello dello dello dello dello delloon";
            case "booli_vinkki": return "Jumal velhon booli: tarvitaan vain oikeat ainekset (pontikkaa ja jallua)";
            case "anna_paskanmarjat": return "Näin se booli keittyy, paskanmarjat jaloviinaan peittyy.";
            case "yhdistä_vinkki": return "Yhdistä ne paskanmarjat siihen jaloviinaan. Jos oot vammanen kääpiö ja unohdit miten yhdistetään, se on Z-näppäimellä.";
            case "kuu": return "Jumal Velhon boolilla pääsee kuuhun!";
            case null, default: return katso();
        }
    }

    public void löydäJumalVelho() {
        TavoiteLista.suoritaTavoite("Löydä Jumal Yoda");
    }

    public boolean löydetty() {
        return TavoiteLista.tavoiteLista.get("Löydä Jumal Yoda");
    }
}
