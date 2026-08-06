package keimo.seikkailupeli.assets.huone;

import keimo.keimoengine.KeimoEngine;
import keimo.keimoengine.ikkuna.DialogiIkkunat;
import keimo.keimoengine.ruudut.LatausRuutu;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.assets.PeliTiedosto;
import keimo.seikkailupeli.assets.dialogi.VuoropuheDialogiPätkä;
import keimo.seikkailupeli.assets.tarina.TarinaPätkä;
import keimo.seikkailupeli.objektit.Pelaaja;
import keimo.seikkailupeli.objektit.Suunnallinen.Suunta;
import keimo.seikkailupeli.objektit.entityt.*;
import keimo.seikkailupeli.objektit.entityt.npc.Pikkuvihu;
import keimo.seikkailupeli.objektit.kenttäkohteet.KenttäKohde;
import keimo.seikkailupeli.objektit.kenttäkohteet.esine.*;
import keimo.seikkailupeli.objektit.kenttäkohteet.kiintopiste.*;
import keimo.seikkailupeli.objektit.kenttäkohteet.warp.*;
import keimo.seikkailupeli.objektit.maastot.Maasto;
import keimo.seikkailupeli.objektit.maastot.Tile;
import keimo.utility.KSTLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class HuoneLista {

    public static HashMap<Integer, Huone> ladattuHuoneKartta = new HashMap<>();
    public static HashMap<String, TarinaPätkä> ladattuTarinaKartta = new HashMap<>();
    public static HashMap<String, VuoropuheDialogiPätkä> ladattuDialogiKartta = new HashMap<>();
    public static boolean huoneKarttaLadattu = false;

        public static void lataaPelitiedosto() {
            try {
                lataaHuoneetJaDialogitKST();
                lataaReferenssiHuonekartta();
                //luoTestiHuone();
                //debugSpawn();
            }
            catch (Exception e) {
                e.printStackTrace();
                String viesti = "Ei voitu ladata tiedostoa default.kst\nLadataanko failsafe-kenttä?";
                boolean vastaus = DialogiIkkunat.viestiIkkuna("Virhe kentän luonnissa", viesti, "yesno", "error", true);
                if (vastaus) lataaFailsafe();
                else System.exit(1);
            }
        }
        
        /**
         * Legacy alkaa
         * Vakiokentän luonti koodissa (ei lataamalla default.kst)
         */
        private static HashMap<Integer, Huone> lataaHuonelistaLegacy() {
            HashMap<Integer, Huone> huoneKartta = new HashMap<>();

            ArrayList<KenttäKohde> huone0Kenttä = luoVakioKenttäLegacy(0);
            ArrayList<Maasto> huone0Maasto = luoVakioMaastoLegacy(0);
            ArrayList<Entity> huone0Entityt = luoVakioNPCListaLegacy(0);
            huoneKartta.put(0, new Huone(0, 10, "Puisto_keski", "tausta_puisto.png", null, huone0Kenttä, huone0Maasto, huone0Entityt, "puisto", null, null));
            ArrayList<KenttäKohde> huone1Kenttä = luoVakioKenttäLegacy(1);
            ArrayList<Maasto> huone1Maasto = luoVakioMaastoLegacy(1);
            ArrayList<Entity> huone1Entityt = luoVakioNPCListaLegacy(1);
            huoneKartta.put(1, new Huone(1, 10, "Puisto_ylä", "tausta_puisto.png", null, huone1Kenttä, huone1Maasto, huone1Entityt, "puisto", null, null));
            ArrayList<KenttäKohde> huone2Kenttä = luoVakioKenttäLegacy(2);
            ArrayList<Maasto> huone2Maasto = luoVakioMaastoLegacy(2);
            ArrayList<Entity> huone2Entityt = luoVakioNPCListaLegacy(2);
            huoneKartta.put(2, new Huone(2, 10, "Puisto_ala", "tausta_puisto.png", null, huone2Kenttä, huone2Maasto, huone2Entityt, "puisto", null, null));
            ArrayList<KenttäKohde> huone3Kenttä = luoVakioKenttäLegacy(3);
            ArrayList<Maasto> huone3Maasto = luoVakioMaastoLegacy(3);
            huoneKartta.put(3, new Huone(3, 10, "Puisto_oikea", "tausta_puisto.png", null, huone3Kenttä, huone3Maasto, null, "puisto", null, null));
            ArrayList<KenttäKohde> huone4Kenttä = luoVakioKenttäLegacy(4);
            ArrayList<Maasto> huone4Maasto = luoVakioMaastoLegacy(4);
            huoneKartta.put(4, new Huone(4, 10, "Puisto_vasen", "tausta_puisto.png", null, huone4Kenttä, huone4Maasto, null, "puisto", null, null));
            ArrayList<KenttäKohde> huone5Kenttä = luoVakioKenttäLegacy(5);
            ArrayList<Maasto> huone5Maasto = luoVakioMaastoLegacy(5);
            huoneKartta.put(5, new Huone(5, 10, "Asuintalot", "tausta_jatkuu.png", null, huone5Kenttä, huone5Maasto, null, "overworld", null, null));
            Pelaaja.alkuHuone = 0;
            Pelaaja.alkuSijX = 5;
            Pelaaja.alkuSijY = 5;

            return huoneKartta;
        }
    
        private static ArrayList<KenttäKohde> luoVakioKenttäLegacy(int huoneenId) {

            ArrayList<KenttäKohde> kenttäkohteet = new ArrayList<>();
    
            switch (huoneenId) {
                default:
                    kenttäkohteet.removeAll(kenttäkohteet);
                    String[] ominaisuudet = {"kohdehuone=1", "kohderuutuX=3", "kohderuutuY=9", "suunta=Ylös"};
                    kenttäkohteet.add(new Oviruutu(3, 0, new ArrayList<String>(List.of(ominaisuudet))));
                    String[] ominaisuudet2 = {"kohdehuone=2", "kohderuutuX=5", "kohderuutuY=0", "suunta=Alas"};
                    kenttäkohteet.add(new Oviruutu(5, 9, new ArrayList<String>(List.of(ominaisuudet2))));
                    String[] ominaisuudet3 = {"kohdehuone=3", "kohderuutuX=0", "kohderuutuY=3", "suunta=Oikea"};
                    kenttäkohteet.add(new Oviruutu(9, 3, new ArrayList<String>(List.of(ominaisuudet3))));
                    String[] ominaisuudet4 = {"kohdehuone=4", "kohderuutuX=9", "kohderuutuY=7", "suunta=Vasen"};
                    kenttäkohteet.add(new Oviruutu(0, 7, new ArrayList<String>(List.of(ominaisuudet4))));
                    kenttäkohteet.add(new Suklaalevy(0, 6));
                    kenttäkohteet.add(new Vesiämpäri(2, 3));
                    break;
                case 1:
                    kenttäkohteet.removeAll(kenttäkohteet);
                    String[] ominaisuudet5 = {"kohdehuone=0", "kohderuutuX=3", "kohderuutuY=0", "suunta=Alas"};
                    kenttäkohteet.add(new Oviruutu(3, 9, new ArrayList<String>(List.of(ominaisuudet5))));
                    kenttäkohteet.add(new Hiili(1, 0));
                    kenttäkohteet.add(new Kaasusytytin(2, 0, new ArrayList<String>(List.of(new String[]{"toimivuus=tyhjä"}))));
                    kenttäkohteet.add(new Makkara(3, 0));
                    kenttäkohteet.add(new Suklaalevy(4, 0));
                    kenttäkohteet.add(new Kilpi(5, 0));
                    kenttäkohteet.add(new Makkara(6, 0));
                    kenttäkohteet.add(new Makkara(7, 0));
                    kenttäkohteet.add(new Makkara(0, 0));
                    break;
                case 2:
                    kenttäkohteet.removeAll(kenttäkohteet);
                    String[] ominaisuudet6 = {"kohdehuone=0", "kohderuutuX=5", "kohderuutuY=9", "suunta=Ylös"};
                    kenttäkohteet.add(new Oviruutu(5, 0, new ArrayList<String>(List.of(ominaisuudet6))));
                    kenttäkohteet.add(new Suklaalevy(1, 0));
                    kenttäkohteet.add(new Kaasupullo(0, 2));
                    kenttäkohteet.add(new Makkara(3, 0));
                    kenttäkohteet.add(new Makkara(0, 0));
                    break;
                case 3:
                    kenttäkohteet.removeAll(kenttäkohteet);
                    String[] ominaisuudet7 = {"kohdehuone=0", "kohderuutuX=9", "kohderuutuY=3", "suunta=Vasen"};
                    kenttäkohteet.add(new Oviruutu(0, 3, new ArrayList<String>(List.of(ominaisuudet7))));
                    kenttäkohteet.add(new Vesiämpäri(0, 0));
                    kenttäkohteet.add(new Avain(2, 0));
                    kenttäkohteet.add(new Paperi(0, 1));
                    kenttäkohteet.add(new Makkara(3, 0));
                    kenttäkohteet.add(new Makkara(0, 4));
                    kenttäkohteet.add(new Makkara(5, 0));
                    break;
                case 4:
                    kenttäkohteet.removeAll(kenttäkohteet);
                    String[] ominaisuudet8 = {"kohdehuone=0", "kohderuutuX=0", "kohderuutuY=7", "suunta=Oikea"};
                    kenttäkohteet.add(new Oviruutu(9, 7, new ArrayList<String>(List.of(ominaisuudet8))));
                    String[] ominaisuudet9 = {"kohdehuone=5", "kohderuutuX=9", "kohderuutuY=2", "suunta=Vasen"};
                    kenttäkohteet.add(new Oviruutu(0, 2, new ArrayList<String>(List.of(ominaisuudet9))));
                    kenttäkohteet.add(new Vesiämpäri(0, 0));
                    break;
                case 5:
                    kenttäkohteet.removeAll(kenttäkohteet);
                    String[] ominaisuudet10 = {"kohdehuone=4", "kohderuutuX=0", "kohderuutuY=2", "suunta=Oikea"};
                    kenttäkohteet.add(new Oviruutu(9, 2, new ArrayList<String>(List.of(ominaisuudet10))));
                    kenttäkohteet.add(new Nuotio(2, 2, null));
                    kenttäkohteet.add(new Kirstu(4, 2, null));
                    kenttäkohteet.add(new Ämpärikone(1, 0, null));
                    break;
            }
            return kenttäkohteet;
        }
    
        private static ArrayList<Maasto> luoVakioMaastoLegacy(int huoneenId) {

            ArrayList<Maasto> maastot = new ArrayList<>();

            switch (huoneenId) {
                default:
                    maastot.removeAll(maastot);
                    maastot.add(new Tile(1, 3, new ArrayList<String>(List.of(new String[]{"kuva=hiekka.png"}))));
                    maastot.add(new Tile(8, 8, new ArrayList<String>(List.of(new String[]{"kuva=kauppa_seinä_e.png"}))));
                    maastot.add(new Tile(8, 9, new ArrayList<String>(List.of(new String[]{"kuva=kauppa_seinä_e.png"}))));
                    maastot.add(new Tile(9, 8, new ArrayList<String>(List.of(new String[]{"kuva=kauppa_seinä_e.png"}))));
                    maastot.add(new Tile(9, 9, new ArrayList<String>(List.of(new String[]{"kuva=kauppa_seinä_e.png"}))));
                    maastot.add(new Tile(9, 3, new ArrayList<String>(List.of(new String[]{"kuva=hiekka.png"}))));
                    maastot.add(new Tile(9, 4, new ArrayList<String>(List.of(new String[]{"kuva=hiekka.png"}))));
                    maastot.add(new Tile(9, 5, new ArrayList<String>(List.of(new String[]{"kuva=hiekka.png"}))));
                    break;
                case 1:
                    maastot.removeAll(maastot);
                    maastot.add(new Tile(2, 1, new ArrayList<String>(List.of(new String[]{"kuva=vesi_e.png"}))));
                    maastot.add(new Tile(1, 3, new ArrayList<String>(List.of(new String[]{"kuva=hiekka.png"}))));
                    maastot.add(new Tile(9, 3, new ArrayList<String>(List.of(new String[]{"kuva=hiekka.png"}))));
                    maastot.add(new Tile(9, 4, new ArrayList<String>(List.of(new String[]{"kuva=hiekka.png"}))));
                    maastot.add(new Tile(9, 5, new ArrayList<String>(List.of(new String[]{"kuva=hiekka.png"}))));
                    break;
                case 2:
                    maastot.removeAll(maastot);
                    maastot.add(new Tile(1, 3, new ArrayList<String>(List.of(new String[]{"kuva=hiekka.png"}))));
                    maastot.add(new Tile(2, 1, new ArrayList<String>(List.of(new String[]{"kuva=vesi_e.png"}))));
                    maastot.add(new Tile(9, 3, new ArrayList<String>(List.of(new String[]{"kuva=hiekka.png"}))));
                    maastot.add(new Tile(9, 4, new ArrayList<String>(List.of(new String[]{"kuva=hiekka.png"}))));
                    maastot.add(new Tile(9, 5, new ArrayList<String>(List.of(new String[]{"kuva=hiekka.png"}))));
                    maastot.add(new Tile(8, 3, new ArrayList<String>(List.of(new String[]{"kuva=vesi_e.png"}))));
                    maastot.add(new Tile(8, 4, new ArrayList<String>(List.of(new String[]{"kuva=vesi_e.png"}))));
                    maastot.add(new Tile(8, 5, new ArrayList<String>(List.of(new String[]{"kuva=vesi_e.png"}))));
                    break;
                case 3:
                    maastot.removeAll(maastot);
                    maastot.add(new Tile(7, 1, new ArrayList<String>(List.of(new String[]{"kuva=vesi_e.png"}))));
                    maastot.add(new Tile(7, 2, new ArrayList<String>(List.of(new String[]{"kuva=vesi_e.png"}))));
                    maastot.add(new Tile(7, 3, new ArrayList<String>(List.of(new String[]{"kuva=vesi_e.png"}))));
                    maastot.add(new Tile(7, 4, new ArrayList<String>(List.of(new String[]{"kuva=vesi_e.png"}))));
                    maastot.add(new Tile(8, 1, new ArrayList<String>(List.of(new String[]{"kuva=vesi_e.png"}))));
                    maastot.add(new Tile(8, 2, new ArrayList<String>(List.of(new String[]{"kuva=vesi_e.png"}))));
                    maastot.add(new Tile(8, 3, new ArrayList<String>(List.of(new String[]{"kuva=vesi_e.png"}))));
                    maastot.add(new Tile(8, 4, new ArrayList<String>(List.of(new String[]{"kuva=vesi_e.png"}))));
                    maastot.add(new Tile(8, 5, new ArrayList<String>(List.of(new String[]{"kuva=vesi_e.png"}))));
                    maastot.add(new Tile(9, 3, new ArrayList<String>(List.of(new String[]{"kuva=hiekka.png"}))));
                    maastot.add(new Tile(9, 4, new ArrayList<String>(List.of(new String[]{"kuva=hiekka.png"}))));
                    maastot.add(new Tile(9, 5, new ArrayList<String>(List.of(new String[]{"kuva=hiekka.png"}))));
                    break;
            }
            return maastot;
        }
    
        private static ArrayList<Entity> luoVakioNPCListaLegacy(int huoneenId) {
            ArrayList<Entity> npcLista = new ArrayList<Entity>();
            npcLista.clear();
            switch (huoneenId) {
                default:
                    npcLista.add(new Pikkuvihu(5, 6, new ArrayList<String>(List.of(new String[]{"liiketapa=LOOP_NELIÖ_VASTAPÄIVÄÄN"}))));
                    npcLista.add(new Pikkuvihu(2, 2, new ArrayList<String>(List.of(new String[]{"liiketapa=LOOP_NELIÖ_MYÖTÄPÄIVÄÄN"}))));
                    break;
                case 1:
                    npcLista.add(new Pikkuvihu(3, 3, new ArrayList<String>(List.of(new String[]{"liiketapa=LOOP_NELIÖ_VASTAPÄIVÄÄN"}))));
                    npcLista.add(new Pikkuvihu(4, 2, new ArrayList<String>(List.of(new String[]{"liiketapa=LOOP_NELIÖ_MYÖTÄPÄIVÄÄN"}))));
                    npcLista.add(new Pikkuvihu(1, 4, new ArrayList<String>(List.of(new String[]{"liiketapa=LOOP_NELIÖ_MYÖTÄPÄIVÄÄN"}))));
                    break;
                case 2:
                    npcLista.add(new Pikkuvihu(1, 5, new ArrayList<String>(List.of(new String[]{"liiketapa=LOOP_NELIÖ_MYÖTÄPÄIVÄÄN"}))));
                    npcLista.add(new Pikkuvihu(5, 3, new ArrayList<String>(List.of(new String[]{"liiketapa=LOOP_NELIÖ_MYÖTÄPÄIVÄÄN"}))));
                    npcLista.add(new Pikkuvihu(2, 4, new ArrayList<String>(List.of(new String[]{"liiketapa=LOOP_NELIÖ_VASTAPÄIVÄÄN"}))));
                    break;
            }
            return npcLista;
        }
        /**
         * Legacy päättyy
         */
    
    
    /**
     * Lataa pelin alussa luotava kenttä default.kst -tiedostosta
    */

    private static void lataaHuoneetJaDialogitKST() {
        try {
            String tiedostoPolku = "tiedostot/pelitiedostot/default.kst";
            KSTLoader.lataaAsetuksetKST(tiedostoPolku);

            renderöiLatausRuutu("Ladataan kenttiä", 40);
            ladattuHuoneKartta = KSTLoader.lataaKentätKST(tiedostoPolku);

            renderöiLatausRuutu("Ladataan tarinaa", 50);
            ladattuTarinaKartta = KSTLoader.lataaTarinatKST(tiedostoPolku);

            renderöiLatausRuutu("Ladataan dialogeja", 60);
            ladattuDialogiKartta = KSTLoader.lataaDialogitKST(tiedostoPolku);

            Peli.peliTiedosto = new PeliTiedosto(ladattuHuoneKartta, ladattuTarinaKartta, ladattuDialogiKartta);
        }
        catch (Exception e) {
            e.printStackTrace();
            String viesti = "Ei voitu ladata tiedostoa default.kst Ladataanko failsafe-kenttä?";
            boolean vastaus = DialogiIkkunat.viestiIkkuna("Virhe kentän luonnissa", viesti, "yesno", "error", false);
            if (vastaus) HuoneLista.lataaFailsafe();
            else System.exit(1);
        }
    }

    private static void renderöiLatausRuutu(String latausTeksti, int latausProsentti) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		LatausRuutu.päivitäLatausTeksti(latausTeksti, latausProsentti);
		LatausRuutu.renderöiLatausRuutu(KeimoEngine.window, latausProsentti);
		KeimoEngine.window.swapBuffers();
	}

    public static void lataaFailsafe() {
        Peli.peliTiedosto = new PeliTiedosto(lataaHuonelistaLegacy(), ladattuTarinaKartta, ladattuDialogiKartta);
        luoTestiHuone();
        debugSpawn();
        lataaReferenssiHuonekartta();
    }

    /**
     * Testihuone
     */
    private static void luoTestiHuone() {
        int testiHuoneenId = Peli.peliTiedosto.annaHuoneKartta().size();
        System.out.println("Testihuoneen id: " + testiHuoneenId);
        int testihuoneenKoko = 30;
        ArrayList<KenttäKohde> kenttäKohdeLista = new ArrayList<>();
        for (int i = 0; i < testihuoneenKoko; i++) {
            for (int j = 0; j < testihuoneenKoko; j++) {
                kenttäKohdeLista.add(KenttäKohde.luoRandomKenttäKohde(j, i));
            }
        }
        ArrayList<Maasto> maastoLista = new ArrayList<>();
        for (int i = 0; i < testihuoneenKoko; i++) {
            for (int j = 0; j < testihuoneenKoko; j++) {
                //maastoLista.add(Maasto.luoRandomMaasto(j, i));
            }
        }
        Huone testiHuone = new Huone(testiHuoneenId, testihuoneenKoko, "testihuone", null, "testialue", kenttäKohdeLista, maastoLista, null, null, null, null);
        Peli.peliTiedosto.annaHuoneKartta().put(testiHuoneenId, testiHuone);
    }

    /**
     * Debug spawn
     */
    private static void debugSpawn() {
        Peli.entityLista.add(new TyönnettäväLaatikko(12, 12));
    }

    public static void lataaReferenssiHuonekartta() {
        huoneKarttaLadattu = false;
        Peli.annaHuoneKartta().clear();
        for (int i = 0; i < Peli.peliTiedosto.annaHuoneKartta().size(); i++) {
            Huone h = Peli.peliTiedosto.annaHuoneKartta().get(i);
            if (h != null) {
                ArrayList<KenttäKohde> kenttäKohteet = new ArrayList<>();
                for (KenttäKohde[] kk : h.annaHuoneenKenttäSisältö()) {
                    for (KenttäKohde k : kk) {
                        if (k != null) kenttäKohteet.add(KenttäKohde.luoObjektiTiedoilla(k.annaNimi(), k.annaSijX(), k.annaSijY(), k.annaLisäOminaisuudet()));
                    }
                }
                ArrayList<Maasto> maastot = new ArrayList<>();
                for (Maasto[] mm : h.annaHuoneenMaastoSisältö()) {
                    for (Maasto m : mm) {
                        maastot.add(m);
                    }
                }
                ArrayList<Entity> entityt = new ArrayList<>();
                for (Entity[] ee : h.annaHuoneenNPCSisältö()) {
                    for (Entity e : ee) {
                        if (e != null) entityt.add(Entity.luoEntityTiedoilla(e.annaNimi(), e.annaSijX(), e.annaAlkuSijY(), e.annaLisäOminaisuudet()));
                    }
                }
                Huone uusiHuone = new Huone(h.annaId(),
                                            h.annaKoko(),
                                            h.annaNimi(),
                                            h.annaTaustanPolku(),
                                            h.annaAlue(),
                                            kenttäKohteet,
                                            maastot,
                                            entityt,
                                            h.annaHuoneenMusa(),
                                            h.annaTarinaRuudunTunniste(),
                                            h.annaVaaditunTavoitteenTunniste());
                uusiHuone.päivitäReunawarppienTiedot(h.annaReunaWarppiTiedot(Suunta.VASEN),
                                                    h.annaReunaWarpinKohdeId(Suunta.VASEN),
                                                    h.annaReunaWarppiTiedot(Suunta.OIKEA),
                                                    h.annaReunaWarpinKohdeId(Suunta.OIKEA),
                                                    h.annaReunaWarppiTiedot(Suunta.ALAS),
                                                    h.annaReunaWarpinKohdeId(Suunta.ALAS),
                                                    h.annaReunaWarppiTiedot(Suunta.YLÖS),
                                                    h.annaReunaWarpinKohdeId(Suunta.YLÖS));
                Peli.annaHuoneKartta().put(i, uusiHuone);
            }
        }
        huoneKarttaLadattu = true;
    }
}
