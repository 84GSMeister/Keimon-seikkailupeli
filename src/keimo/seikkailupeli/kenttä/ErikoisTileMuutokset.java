package keimo.seikkailupeli.kenttä;

import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.Peli.SyötteenTila;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.objektit.Pelaaja;

public class ErikoisTileMuutokset {
    private static Renderöitävä kassaVihkoauki = Assets.annaTekstuuri("kassa_vihkoauki");
    private static Renderöitävä kassaVihkokiinni = Assets.annaTekstuuri("kassa_vihkokiinni");
    private static Renderöitävä virheTekstuuri = Assets.annaTekstuuri("virhe");

    public static Renderöitävä annaSpesiaaliTekstuuri(Renderöitävä tekstuuri, String alkuperäinen, int sijX, int sijy) {
        // Erittäin hardkoodattua settiä
        switch (alkuperäinen) {
            case "kassa_vihkokiinni_e.png":
                if (Peli.huone.annaNimi().startsWith("Kauppa") &&
                    Peli.syötteenTila == SyötteenTila.DIALOGI &&
                    (sijX == Pelaaja.sijX + 1 || sijX == Pelaaja.sijX - 1)
                ) return kassaVihkoauki;
                else return kassaVihkokiinni;
            case null:
                return virheTekstuuri;
            default:
                return tekstuuri;
        }
    }
}
