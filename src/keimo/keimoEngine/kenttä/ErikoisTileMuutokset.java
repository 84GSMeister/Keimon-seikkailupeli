package keimo.keimoEngine.kenttä;

import keimo.Peli;
import keimo.Peli.SyötteenTila;
import keimo.keimoEngine.grafiikat.Kuva;
import keimo.keimoEngine.grafiikat.Tekstuuri;

public class ErikoisTileMuutokset {
    private static Tekstuuri kassaVihkoauki = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/visuaaliset_objektit/kassa_vihkoauki.png");
    private static Tekstuuri kassaVihkokiinni = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/visuaaliset_objektit/kassa_vihkokiinni.png");

    public static Kuva annaSpesiaaliTekstuuri(Kuva tekstuuri, String alkuperäinen) {
        switch (alkuperäinen) {
            case "kassa_vihkoauki.png":
                if (Peli.huone.annaNimi().startsWith("Kauppa") && Peli.syötteenTila == SyötteenTila.DIALOGI) return kassaVihkoauki;
                else return kassaVihkokiinni;
            case null, default:
                return tekstuuri;
        }
    }
}
