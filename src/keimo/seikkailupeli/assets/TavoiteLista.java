package keimo.seikkailupeli.assets;

import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.gui.hud.TavoitePopup;
import keimo.seikkailupeli.objektit.kenttäkohteet.*;
import keimo.seikkailupeli.objektit.kenttäkohteet.esine.Esine;
import keimo.seikkailupeli.objektit.kenttäkohteet.esine.Pesäpallomaila;
import keimo.seikkailupeli.objektit.kenttäkohteet.kiintopiste.Kiintopiste;
import keimo.seikkailupeli.objektit.kenttäkohteet.kiintopiste.Nuotio;
import keimo.seikkailupeli.objektit.kenttäkohteet.warp.Salaovi;
import keimo.seikkailupeli.äänet.Äänet;

import java.util.HashMap;

public class TavoiteLista {
    
    public static HashMap<String, Boolean> tavoiteLista = new HashMap<String, Boolean>();
    public static HashMap<Integer, String> pääTavoitteet = new HashMap<Integer, String>();
    public static String nykyinenTavoite = "";

    public static void suoritaTavoite(String tavoitteenTunniste) {
        if (tavoiteLista.keySet().contains(tavoitteenTunniste)) {
            tavoiteLista.put(tavoitteenTunniste, true);
            TavoitePopup.suoritettuTavoite = tavoitteenTunniste;
            TavoitePopup.popupAjastin = 240;
            Äänet.toistaSFX("Tavoite_suoritettu");
            suoritaTavoitteenLisätoimet(tavoitteenTunniste);
        }
    }

    public static void suoritaPääTavoite(int tavoitteenNro) {
        suoritaTavoite(pääTavoitteet.get(tavoitteenNro));
        if (tavoitteenNro +1 >= pääTavoitteet.size()) {
            nykyinenTavoite = "Ei määritelty";
        }
        else {
            nykyinenTavoite = pääTavoitteet.get(tavoitteenNro +1);
        }
    }

    private static void suoritaTavoitteenLisätoimet(String tavoitteenTunniste) {
        switch (tavoitteenTunniste) {
            case "Löydä salahuone" -> {
                if (Peli.huone.annaNimi().equals("Keimo-baari")) {
                    for (KenttäKohde[] kk : Peli.huone.annaHuoneenKenttäSisältö()) {
                        for (KenttäKohde k : kk) {
                            if (k instanceof Salaovi) {
                                Salaovi so = (Salaovi)k;
                                so.asetaNäkyväksi();
                            }
                        }
                    }
                }
            }
        }
    }

    
    public static void luoPääTavoiteLista() {
        pääTavoitteet.put(0, "Löydä takaisin kotiin");
        pääTavoitteet.put(1, "Etsi pesäpallomaila");
        pääTavoitteet.put(2, "Etsi nuotiopaikka");
        pääTavoitteet.put(3, "Sytytä nuotio");
        pääTavoitteet.put(4, "Hae Pasi nuotiolle");
        pääTavoitteet.put(5, "Etsi Keimo-Baari");
        pääTavoitteet.put(6, "Etsi Pasi");
        pääTavoitteet.put(7, "Etsi Velhometsä");
        pääTavoitteet.put(8, "Etsi Temppeli");
        pääTavoitteet.put(9, "Etsi pääjehu");
        pääTavoitteet.put(10, "Voita pomo");
        nykyinenTavoite = pääTavoitteet.get(0);
    }

    public static void luoTavoiteLista() {
        tavoiteLista.put(pääTavoitteet.get(0), false);
        tavoiteLista.put(pääTavoitteet.get(1), false);
        tavoiteLista.put(pääTavoitteet.get(2), false);
        tavoiteLista.put(pääTavoitteet.get(3), false);
        tavoiteLista.put(pääTavoitteet.get(4), false);
        tavoiteLista.put(pääTavoitteet.get(5), false);
        tavoiteLista.put(pääTavoitteet.get(6), false);
        tavoiteLista.put(pääTavoitteet.get(7), false);
        tavoiteLista.put(pääTavoitteet.get(8), false);
        tavoiteLista.put(pääTavoitteet.get(9), false);
        tavoiteLista.put(pääTavoitteet.get(10), false);
        tavoiteLista.put("Löydä Jumal Yoda", false);
        tavoiteLista.put("Avaa takahuone", false);
        tavoiteLista.put("Keitä booli", false);
        tavoiteLista.put("Löydä salahuone", false);
    }

    public static int tarkistaSuoritetutPääTavoitteet() {
        int suoritetutPääTavoitteet = 0;
        for (int i = 0; i < pääTavoitteet.size(); i++) {
            if (tavoiteLista.get(pääTavoitteet.get(i))) {
                if (i < pääTavoitteet.size()-1) {
                    suoritetutPääTavoitteet++;
                    nykyinenTavoite = pääTavoitteet.get(i+1);
                }
                else {
                    nykyinenTavoite = "Ei määritelty";
                }
            }
        }
        return suoritetutPääTavoitteet;
    }

    public static void tarkistaTavoiteEsine(Esine e) {
        if (nykyinenTavoite == pääTavoitteet.get(1) && e instanceof Pesäpallomaila) {
            suoritaPääTavoite(1);
        }
    }

    public static void tarkistaTavoiteKiintopiste(KenttäKohde kk) {
        if (kk instanceof Kiintopiste) {
            Kiintopiste kp = (Kiintopiste)kk;
            if (nykyinenTavoite == pääTavoitteet.get(2) && kp instanceof Nuotio) {
                suoritaPääTavoite(2);
            }
            if (nykyinenTavoite == pääTavoitteet.get(3) && kp instanceof Nuotio) {
                Nuotio nuotio = (Nuotio)kp;
                if (nuotio.onSytytetty()) {
                    suoritaPääTavoite(3);
                }
            }
        }
    }

    public static void nollaaTavoiteLista() {
        pääTavoitteet.clear();
        tavoiteLista.clear();
        luoPääTavoiteLista();
        luoTavoiteLista();
    }
}
