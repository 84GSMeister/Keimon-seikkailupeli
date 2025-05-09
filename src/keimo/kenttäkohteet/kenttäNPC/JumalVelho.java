package keimo.kenttäkohteet.kenttäNPC;

import keimo.Pelaaja;
import keimo.HuoneEditori.TavoiteEditori.TavoiteLista;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.keimoEngine.toiminnot.Dialogit;
import keimo.kenttäkohteet.esine.Esine;
import keimo.kenttäkohteet.esine.Jallupullo;
import keimo.kenttäkohteet.esine.Paskanmarjat;
import keimo.kenttäkohteet.esine.Ponuainekset;

import java.util.ArrayList;

public final class JumalVelho extends NPC_KenttäKohde {

    public JumalVelho(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.nimi = "Jumal Velho";
        super.tiedostonNimi = "velho.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.dialogiTekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/dialogi/velho_dialogi.png");
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
                    for (Esine pelaajanEsine : Pelaaja.esineet) {
                        if (pelaajanEsine instanceof Ponuainekset) ponuLöytyy = true;
                        else if (pelaajanEsine instanceof Jallupullo) jalluLöytyy = true; 
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
            case "anna_paskanmarjat": return "Näin se booli keittyy, paskanmarjat jaloviinaan peittyy.";
            case "booli_vinkki": return "Jumal velhon booli: tarvitaan vain oikeat ainekset (pontikkaa ja jallua)";
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
