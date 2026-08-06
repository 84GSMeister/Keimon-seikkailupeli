package keimo.seikkailupeli.objektit.kenttäkohteet.kenttäNPC;

import keimo.TarkistettavatArvot;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.objektit.Pelaaja;
import keimo.seikkailupeli.objektit.Pelaaja.KeimonState;
import keimo.seikkailupeli.objektit.kenttäkohteet.esine.Huume;
import keimo.seikkailupeli.toiminnot.Dialogit;

import java.util.ArrayList;

public final class Juhani extends NPC_KenttäKohde {

    public Juhani(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.nimi = "Juhani";
        super.tiedostonNimi = "juhani.gif";
        super.tekstuuri = Assets.annaTekstuuri("juhani");
        super.dialogiTekstuuri = Assets.annaDialogiTekstuuri("dialogi_juhani");
        super.katsomisTeksti = "Hämärän näköinen tyyppi. Mitähän se aikoo?";
        super.dialogit.add("vakio");
        super.asetaTiedot();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        //Tätä ei tehdä siksi, että kaikkia näitä tarvitsisi pelissä. Tätä tehdään taiteen itsensä vuoksi.
        switch (sijamuoto) {
            case "nominatiivi":  return "Juhani";
            case "genetiivi":    return "Juhanin";
            case "essiivi":      return "Juhanina";
            case "partitiivi":   return "Juhania";
            case "translatiivi": return "Juhaniksi";
            case "inessiivi":    return "Juhanissa";
            case "elatiivi":     return "Juhanista";
            case "illatiivi":    return "Juhaniin";
            case "adessiivi":    return "Juhanilla";
            case "ablatiivi":    return "Juhanilta";
            case "allatiivi":    return "Juhanille";
            default:             return "Juhani";
        }
    }

    @Override
    public void juttele() {
        switch (this.annaDialogi()) {
            case "vakio" -> {
                if (Pelaaja.raha >= 20 || Pelaaja.loputonRaha) {
                    if (Pelaaja.annaEsineidenMäärä() < Pelaaja.annaTavaraluettelonKoko()) {
                        Dialogit.avaaPitkäDialogiRuutu("juhani_huume");
                    }
                    else Dialogit.avaaDialogi(this.annaTekstuuri(), this.haeDialogiTeksti("invatäynnä"), "");
                }
                else Dialogit.avaaDialogi(this.annaDialogiTekstuuri(), this.haeDialogiTeksti("osta_huume"), this.annaNimi());
            }
            case null, default -> {
                Dialogit.avaaDialogi(this.annaNimiSijamuodossa("allatiivi") + " ei ole määritetty dialogia " + "\"" + this.annaDialogi() + "\".", "Virheellinen dialogi");
            }
        }
    }

    @Override
    public String haeDialogiTeksti(String teksti) {
        switch (teksti) {
            case "osta_huume": return "Osta Juhanilta kahel kybäl yksi huume pois.";
            case "huume_ostettu": return "-Njuu. Kyllä tää fiitti nyt siltä näyttää, että sinä annat minulle yhden massin, ja minä annan sinulle yhden huumeen.";
            case "invatäynnä": return "(Tavaraluettelo on täynnä!)";
            case null, default: return katso();
        }
    }

    public static void annaHuume() {
        Pelaaja.annaEsine(new Huume(0, 0));
        if (!Pelaaja.loputonRaha) Pelaaja.raha -= 20;
    }

    public void kuolemaJuhani() {
        TarkistettavatArvot.pelinLoppuSyy = TarkistettavatArvot.PelinLopetukset.KUOLEMA_JUHANI;
        Pelaaja.hp = 0;
        Pelaaja.keimonState = KeimonState.KUOLLUT;
    }
}
