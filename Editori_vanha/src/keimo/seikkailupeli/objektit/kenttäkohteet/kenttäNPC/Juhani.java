package keimo.seikkailupeli.objektit.kenttäkohteet.kenttäNPC;

import keimo.TarkistettavatArvot;
import keimo.seikkailupeli.objektit.Pelaaja;
import keimo.seikkailupeli.objektit.Pelaaja.KeimonState;
import keimo.seikkailupeli.objektit.kenttäkohteet.esine.Huume;

import java.util.ArrayList;

public final class Juhani extends NPC_KenttäKohde {

    public Juhani(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.nimi = "Juhani";
        super.tiedostonNimi = "juhani.gif";
        super.katsomisTeksti = "Hämärän näköinen tyyppi. Mitähän se aikoo?";
        super.dialogit.add("vakio");
        if (ominaisuusLista == null) super.valitseVakioDialogi();
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

    public void annaHuume() {
        Pelaaja.annaEsine(new Huume(0, 0));
        if (!Pelaaja.loputonRaha) Pelaaja.raha -= 20;
    }

    public void kuolemaJuhani() {
        TarkistettavatArvot.pelinLoppuSyy = TarkistettavatArvot.PelinLopetukset.KUOLEMA_JUHANI;
        Pelaaja.hp = 0;
        Pelaaja.keimonState = KeimonState.KUOLLUT;
    }
}
