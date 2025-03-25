package keimo.kenttäkohteet.kiintopiste;

import keimo.Pelaaja;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.kenttäkohteet.esine.Esine;
import keimo.kenttäkohteet.esine.Olutlasi;

import javax.swing.ImageIcon;

public class BaariRuutu extends Kiintopiste {
    @Override
    public String vuorovaikuta(Esine e) {
        Olutlasi olutlasi = new Olutlasi(true, 0, 0);
        if (Pelaaja.raha >= olutlasi.annaHinta()) {
            Pelaaja.annaEsine(olutlasi);
            Pelaaja.raha -= olutlasi.annaHinta();
        }
        return katso();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Baariruutu";
            case "genetiivi":    return "Baariruudun";
            case "esiivi":       return "Baariruutuna";
            case "partitiivi":   return "Baariruutua";
            case "translatiivi": return "Baariruuduksi";
            case "inessiivi":    return "Baariruudussa";
            case "elatiivi":     return "Baariruudusta";
            case "illatiivi":    return "Baariruutuun";
            case "adessiivi":    return "Baariruudulla";
            case "ablatiivi":    return "Baariruudulta";
            case "allatiivi":    return "Baariruudulle";
            default:             return "Baariruutu";
        }
    }
    
    public BaariRuutu (boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {
        super(määritettySijainti, sijX, sijY, ominaisuusLista);
        super.nimi = "Baariruutu";
        super.tiedostonNimi = "baariruutu.png";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);
        super.dialogiKuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
        super.dialogiTekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/dialogi/kauppias_dialogi.png");
        super.katsomisTeksti = "Tästä tilataan.";
        super.erillisDialogi = true;
        super.ignooraaEsineValintaDialogissa = true;
        super.ohitaDialogiTesktiboksi = true;
        super.asetaTiedot();
    }
}
