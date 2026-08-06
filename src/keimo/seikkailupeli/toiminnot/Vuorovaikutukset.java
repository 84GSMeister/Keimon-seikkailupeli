package keimo.seikkailupeli.toiminnot;

import keimo.TarkistettavatArvot;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.assets.TavoiteLista;
import keimo.seikkailupeli.gui.toimintoIkkunat.*;
import keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat.*;
import keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat.minipeli3d.MinipeliIkkuna3D;
import keimo.seikkailupeli.objektit.Pelaaja;
import keimo.seikkailupeli.objektit.kenttäkohteet.*;
import keimo.seikkailupeli.objektit.kenttäkohteet.avattavaEste.*;
import keimo.seikkailupeli.objektit.kenttäkohteet.esine.*;
import keimo.seikkailupeli.objektit.kenttäkohteet.kenttäNPC.*;
import keimo.seikkailupeli.objektit.kenttäkohteet.kiintopiste.*;
import keimo.seikkailupeli.objektit.kenttäkohteet.triggeri.*;
import keimo.seikkailupeli.objektit.kenttäkohteet.warp.Warp;
import keimo.seikkailupeli.äänet.Äänet;

import java.util.ArrayList;

public class Vuorovaikutukset {
    
    public static void vuorovaikuta(KenttäKohde k, Esine e) {

        if (k instanceof Kiintopiste) {
            Kiintopiste kp = (Kiintopiste)k;
            if (kp instanceof Nuotio) {
                Nuotio n = (Nuotio)kp;
                if (TavoiteLista.nykyinenTavoite.startsWith("Löydä takaisin kotiin")) {
                    Dialogit.avaaDialogi(n.annaDialogiTekstuuri(), "Sinun on löydettävä kotiin ensin. Voit palata myöhemmin nuotion äärelle.", n.annaNimi());
                }
                else if (TavoiteLista.nykyinenTavoite.startsWith("Etsi pesäpallomaila")) {
                    Dialogit.avaaDialogi(n.annaDialogiTekstuuri(), "Sinun kannattaisi etsiä jokin ase. Voit palata myöhemmin nuotion äärelle.", n.annaNimi());
                }
                else {
                    if (TavoiteLista.nykyinenTavoite.startsWith("Etsi nuotiopaikka")) TavoiteLista.suoritaPääTavoite(2);
                    if (!n.onSytytetty()) {
                        if (e instanceof Paperi) {
                            n.kokeileEsinettä(e);
                            e = null;
                            Peli.valittuEsine = e;
                            Pelaaja.esineet[Peli.esineValInt] = e;
                            Dialogit.avaaDialogi(n.annaDialogiTekstuuri(), n.haeDialogiTeksti("lisää sytyke"), n.annaNimi());
                        }
                        else if (e instanceof Hiili) {
                            n.kokeileEsinettä(e);
                            e = null;
                            Peli.valittuEsine = e;
                            Pelaaja.esineet[Peli.esineValInt] = e;
                            Dialogit.avaaDialogi(n.annaDialogiTekstuuri(), n.haeDialogiTeksti("lisää polttoaine"), n.annaNimi());
                        }
                        else if (e instanceof Kaasusytytin) {
                            Kaasusytytin ks = (Kaasusytytin)e;
                            if (!ks.toimiva) {
                                Dialogit.avaaDialogi(n.annaDialogiTekstuuri(), n.haeDialogiTeksti("ei_toimiva"), n.annaNimi());
                            }
                            else if (ks.toimiva && n.onkoPolttoaine() && n.onkoSytyke()) {
                                n.sytytä(true);
                                TavoiteLista.suoritaPääTavoite(3);
                                Dialogit.avaaDialogi(n.annaDialogiTekstuuri(), n.haeDialogiTeksti("sytytä"), n.annaNimi());
                            }
                            else Dialogit.avaaDialogi(n.annaDialogiTekstuuri(), n.katso(), n.annaNimi());
                        }
                        else if (e instanceof Makkara) {
                            Dialogit.avaaDialogi(n.annaDialogiTekstuuri(), n.haeDialogiTeksti("makkara_ei_sytytetty"), n.annaNimi());
                        }
                        else if (e != null) {
                            Dialogit.avaaDialogi(n.annaDialogiTekstuuri(), e.annaNimiSijamuodossa("partitiivi") + " ei voi käyttää " + n.annaNimiSijamuodossa("illatiivi"), n.annaNimi());
                        }
                        else Dialogit.avaaDialogi(n.annaDialogiTekstuuri(), n.katso(), n.annaNimi());
                    }
                    else {
                        if (e instanceof Makkara) {
                            Makkara m = (Makkara)e;
                            String paistoTeksti = m.annaPaistoTeksti();
                            m.paista();
                            Dialogit.avaaDialogi(m.annaDialogiTekstuuri(), paistoTeksti, m.annaNimi());
                        }
                        else if (e instanceof Vesiämpäri) {
                            n.sytytä(false);
                            Dialogit.avaaDialogi(n.annaDialogiTekstuuri(), n.haeDialogiTeksti("sammuta"), n.annaNimi());
                        }
                        else if (e instanceof Kaasusytytin) {
                            Dialogit.avaaDialogi(n.annaDialogiTekstuuri(), "Sytytintä ei enää tarvi.", n.annaNimi());
                        }
                        else if (e instanceof Hiili || e instanceof Paperi) {
                            Dialogit.avaaDialogi(n.annaDialogiTekstuuri(), n.haeDialogiTeksti("sytyke_eitarvi"), n.annaNimi());
                        }
                        else if (e != null) {
                            Dialogit.avaaDialogi(n.annaDialogiTekstuuri(), e.annaNimiSijamuodossa("partitiivi") + " ei voi käyttää " + n.annaNimiSijamuodossa("illatiivi"), n.annaNimi());
                        }
                        else {
                            Dialogit.avaaDialogi(n.annaDialogiTekstuuri(), n.katso(), n.annaNimi());
                        }
                    }
                }
            }
            else if (kp instanceof Kirstu) {
                Kirstu kirstu = (Kirstu)kp;
                if (e instanceof Avain) {
                    if (!kirstu.onkoAvattu()) {
                        kirstu.avaa();
                        e = null;
                        Peli.valittuEsine = e;
                        Pelaaja.esineet[Peli.esineValInt] = e;
                        Pelaaja.annaEsine(kirstu.annaSisältöEsine());
                        Dialogit.avaaDialogi(kirstu.annaTekstuuri(), kirstu.haeDialogiTeksti("avaa"), kirstu.annaNimi());
                    }
                }
                else if (e != null) {
                    Dialogit.avaaDialogi(kirstu.annaTekstuuri(), e.annaNimiSijamuodossa("partitiivi") + " ei voi käyttää " + kirstu.annaNimiSijamuodossa("illatiivi"), kirstu.annaNimi());
                }
                else Dialogit.avaaDialogi(kirstu.annaTekstuuri(), kirstu.katso(), kirstu.annaNimi());
            }
            else if (kp instanceof Lepopaikka) {
                Lepopaikka l = (Lepopaikka)kp;
                Dialogit.avaaDialogi(l.annaTekstuuri(), l.vuorovaikuta(e), l.annaNimi());
            }
            else if (kp instanceof KauppaHylly) {
                KauppaHylly hylly = (KauppaHylly)kp;
                int tyhjätPaikat = 0;
                for (Esine esine : Pelaaja.esineet) {
                    if (esine == null) {
                        tyhjätPaikat++;
                    }
                }
                if (Pelaaja.ostosKori.size() >= tyhjätPaikat) {
                    Dialogit.avaaDialogi(hylly.annaDialogiTekstuuri(), "Ostoskoriin ei voi lisätä enempää tavaraa kuin tavaraluettelossa on tyhjiä paikkoja!", hylly.annaNimi());
                }
                else {
                    if (hylly.annaSisältöEsine() instanceof Kuparilager) Äänet.toistaSFX("Kalja_kilinä", true);
                    else Äänet.toistaSFX("Kerää", true);
                    Dialogit.avaaDialogi(hylly.annaDialogiTekstuuri(), "Ostoskoriin lisättiin " + hylly.annaSisältö(), hylly.annaNimi());
                }
                Pelaaja.lisääOstoskoriin(hylly.annaSisältöEsine());
            }
            else if (kp instanceof KauppaRuutu) {
                KauppaRuutu ruutu = (KauppaRuutu)kp;
                boolean ponuAineksetOstettu = false;
                boolean jalluOstettu = false;
                for (Esine pelaajanEsine : Pelaaja.ostosKori) {
                    if (pelaajanEsine instanceof Ponuainekset) {
                        ponuAineksetOstettu = true;
                        break;
                    }
                    if (pelaajanEsine instanceof Jallupullo) {
                        jalluOstettu = true;
                        break;
                    }
                }
                if (ponuAineksetOstettu) {
                    Dialogit.avaaPitkäDialogiRuutu("kyläkauppa");
                    if (Pelaaja.raha > Pelaaja.ostostenHintaYhteensä || Pelaaja.loputonRaha) {
                        int tyhjätPaikat = 0;
                        for (Esine esine : Pelaaja.esineet) {
                            if (esine == null) {
                                tyhjätPaikat++;
                            }
                        }
                        if (Pelaaja.ostosKori.size() - tyhjätPaikat > 0) {
                            Dialogit.avaaDialogi("Ostokset ei mahdu tavaraluetteloon.", "");
                        }
                        else {
                            if (!Pelaaja.loputonRaha) Pelaaja.raha -= Pelaaja.ostostenHintaYhteensä;
                            Pelaaja.ostostenHintaYhteensä = 0;
                            for (Esine ostos : Pelaaja.ostosKori) {
                                Pelaaja.annaEsine((Esine)KenttäKohde.luoObjektiTiedoilla(ostos.annaNimi(), 0, 0, null));
                            }
                            Pelaaja.tyhjennäOstoskori();
                        }
                    }
                    else {
                        Pelaaja.tyhjennäOstoskori();
                    }
                }
                else if (jalluOstettu) {
                    Dialogit.avaaPitkäDialogiRuutu("kyläkauppa2");
                    if (Pelaaja.raha > Pelaaja.ostostenHintaYhteensä || Pelaaja.loputonRaha) {
                        int tyhjätPaikat = 0;
                        for (Esine esine : Pelaaja.esineet) {
                            if (esine == null) {
                                tyhjätPaikat++;
                            }
                        }
                        if (Pelaaja.ostosKori.size() - tyhjätPaikat > 0) {
                            Dialogit.avaaDialogi("Ostokset ei mahdu tavaraluetteloon.", "");
                        }
                        else {
                            if (!Pelaaja.loputonRaha) Pelaaja.raha -= Pelaaja.ostostenHintaYhteensä;
                            Pelaaja.ostostenHintaYhteensä = 0;
                            for (Esine ostos : Pelaaja.ostosKori) {
                                Pelaaja.annaEsine((Esine)KenttäKohde.luoObjektiTiedoilla(ostos.annaNimi(), 0, 0, null));
                            }
                            Pelaaja.tyhjennäOstoskori();
                        }
                    }
                    else {
                        Pelaaja.tyhjennäOstoskori();
                    }
                }
                else if (Pelaaja.ostostenHintaYhteensä <= 0) {
                    Dialogit.avaaDialogi(ruutu.annaDialogiTekstuuri(), "Meinasitko ostaa jotain?", "ASS-Market kassa");
                }
                else if (Pelaaja.raha >= Pelaaja.ostostenHintaYhteensä || Pelaaja.loputonRaha) {
                    int tyhjätPaikat = 0;
                    for (Esine esine : Pelaaja.esineet) {
                        if (esine == null) {
                            tyhjätPaikat++;
                        }
                    }
                    if (Pelaaja.ostosKori.size() - tyhjätPaikat > 0) {
                        Dialogit.avaaDialogi(ruutu.annaDialogiTekstuuri(), "Ostokset ei mahdu tavaraluetteloon.", "");
                    }
                    else {
                        Dialogit.avaaPitkäDialogiRuutu("kauppa_normaali");
                        if (!Pelaaja.loputonRaha) Pelaaja.raha -= Pelaaja.ostostenHintaYhteensä;
                        Pelaaja.ostostenHintaYhteensä = 0;
                        for (Esine ostos : Pelaaja.ostosKori) {
                            Pelaaja.annaEsine((Esine)KenttäKohde.luoObjektiTiedoilla(ostos.annaNimi(), 0, 0, null));
                        }
                        Pelaaja.tyhjennäOstoskori();
                    }
                }
                else {
                    Dialogit.avaaPitkäDialogiRuutu("kauppa_eivaraa");
                    Pelaaja.tyhjennäOstoskori();
                }
            }
            else if (kp instanceof BaariRuutu) {
                BaariRuutu ruutu = (BaariRuutu)kp;
                switch (ruutu.annaTyyppi()) {
                    case "normaali" -> {
                        ArrayList<String> ominaisuusLista = new ArrayList<>();
                        ominaisuusLista.add("juoma=OLUT");
                        Juomalasi juomalasi = new Juomalasi(0, 0, ominaisuusLista);
                        if (Pelaaja.raha >= juomalasi.annaHinta() || Pelaaja.loputonRaha) {
                            int tyhjätPaikat = 0;
                            for (int i = 0; i < Pelaaja.esineet.length; i++) {
                                Esine esine = Pelaaja.esineet[i];
                                if (esine instanceof Juomalasi) {
                                    Juomalasi lasi = (Juomalasi)esine;
                                    if (lasi.annaJuoma().equals("TYHJÄ")) {
                                        Pelaaja.esineet[i] = null;
                                    }
                                }
                                if (Pelaaja.esineet[i] == null) {
                                    tyhjätPaikat++;
                                }
                            }
                            if (tyhjätPaikat <= 0) {
                                Dialogit.avaaDialogi(ruutu.annaTekstuuri(), "Tavaraluettelo on täynnä. Voit asioida baarissa vain, jos sinulla on tilaa tavaraluettelossa (tyhjät lasit voi palauttaa tiskille).", ruutu.annaNimi());
                            }
                            else {
                                Dialogit.avaaPitkäDialogiRuutu("baari_normaali");
                            }
                        }
                        else Dialogit.avaaDialogi(ruutu.annaTekstuuri(), "Sinulla ei ole varaa asioida baarissa. \n(Pst. tiedätkö, mitä tapahtuu\nF5-näppäimestä?)", ruutu.annaNimi());
                    }
                    case "kuu" -> {
                        ArrayList<String> ominaisuusLista = new ArrayList<>();
                        ominaisuusLista.add("juoma=KUUOLUT");
                        Juomalasi juomalasi = new Juomalasi(0, 0, ominaisuusLista);
                        int tyhjätPaikat = 0;
                        for (int i = 0; i < Pelaaja.esineet.length; i++) {
                            Esine esine = Pelaaja.esineet[i];
                            if (esine instanceof Juomalasi) {
                                Juomalasi lasi = (Juomalasi)esine;
                                if (lasi.annaJuoma().equals("TYHJÄ")) {
                                    Pelaaja.esineet[i] = null;
                                }
                            }
                            if (Pelaaja.esineet[i] == null) {
                                tyhjätPaikat++;
                            }
                        }
                        if (tyhjätPaikat <= 0) {
                            Dialogit.avaaDialogi(ruutu.annaTekstuuri(), "Tavaraluettelo on täynnä. Voit asioida baarissa vain, jos sinulla on tilaa tavaraluettelossa (tyhjät lasit voi palauttaa tiskille).", ruutu.annaNimi());
                        }
                        else {
                            if (TarkistettavatArvot.kuubaariLöydetty) {
                                Dialogit.avaaPitkäDialogiRuutu("kuu_baari_2");
                            }
                            else {
                                TarkistettavatArvot.kuubaariLöydetty = true;
                                Dialogit.avaaPitkäDialogiRuutu("kuu_baari");
                            }
                            Pelaaja.annaEsine(juomalasi);
                        }
                    }
                    case null, default -> {
                        Dialogit.avaaDialogi(ruutu.annaNimiSijamuodossa("allatiivi") + " ei ole määritetty tyyppiä " + ruutu.annaTyyppi(), "Virheellinen tyyppi");
                    }
                }
            }
            else if (kp instanceof Ämpärikone) {
                ÄmpäriJonoIkkuna.avaaToimintoIkkuna();
            }
            else if (kp instanceof Pelikone) {
                Pelikone pk = (Pelikone)kp;
                switch (pk.annaTyyppi()) {
                    case 0: MinipeliIkkuna3D.avaaToimintoIkkuna(); break;
                    case 1: MinipeliIkkunaPong.avaaToimintoIkkuna(); break;
                    case 2: MinipeliIkkunaPokeri.avaaToimintoIkkuna(); break;
                    case 3: MinipeliIkkunaTetris.avaaToimintoIkkuna(); break;
                    case 4: MinipeliIkkunaOverflow.avaaToimintoIkkuna(); break;
                    case 5: MinipeliIkkunaKeimoäly.avaaToimintoIkkuna(); break;
                    default: Dialogit.avaaDialogi("Minipeliä " + pk.annaTyyppi() + " ei löytynyt.", pk.annaNimi()); break;
                }
            }
            else if (kp instanceof Pulloautomaatti) {
                PullonPalautusIkkuna.avaaToimintoIkkuna();
            }
            else if (kp instanceof Silta) {
                Dialogit.avaaPitkäDialogiRuutu("silta");
            }
            else if (kp instanceof KoristeOvi) {
                if (TavoiteLista.tavoiteLista.get(TavoiteLista.pääTavoitteet.get(3))) {
                    if (!TavoiteLista.tavoiteLista.get(TavoiteLista.pääTavoitteet.get(4))) {
                        TavoiteLista.suoritaPääTavoite(4);
                    }
                    Dialogit.avaaPitkäDialogiRuutu("pasi_eikotona");
                }
                else {
                    Dialogit.haeTavoiteVinkkiTeksti("Sytytä nuotio");
                }
            }
        }
        else if (k instanceof NPC_KenttäKohde) {
            ((NPC_KenttäKohde)k).juttele();
        }
        else if (k instanceof Triggeri) {
            Triggeri trg = (Triggeri)k;
            if (trg.annaVaadittuVihollinen() != null) {
                Dialogit.avaaDialogi(trg.annaDialogiTekstuuri(), "Tähän tarvitaan jokin vihollinen.", trg.annaNimi());
            }
            else if (trg.annaVaadittuEsine() != null) {
                Dialogit.avaaDialogi(trg.annaDialogiTekstuuri(), "Tähän tarvitaan jokin esine.", trg.annaNimi());
            }
            else trg.triggeröi();
        }
        else if (k instanceof VisuaalinenObjekti) {
            VisuaalinenObjekti vo = (VisuaalinenObjekti)k;
            if (vo.onkoKatsottava()) {
                Dialogit.avaaPitkäDialogiRuutu(vo.annaKatsomisDialogi());
            }
        }
        else if (k instanceof AvattavaEste || k instanceof Warp) {

        }
        else if (k != null) {
            Dialogit.avaaDialogi(k.annaDialogiTekstuuri(), "Objektin alatyypin määritys puuttuu", k.annaNimi());
        }
    }

    public static void käytäEsinettä(KenttäKohde k, Esine e) {
        if (e != null) {
            if (e.onkoKäyttö()) {
                if (Pelaaja.käyttöViive <= 0) {
                    if (e instanceof Ruoka) {
                        Ruoka ruoka = (Ruoka)e;
                        Dialogit.avaaDialogi(ruoka.annaTekstuuri(), ruoka.käytä(), ruoka.annaNimi());
                        Pelaaja.syöRuoka(ruoka.annaParannusMäärä());
                    }
                    else if (e instanceof Juoma ) {
                        if (e instanceof Paskanmarjabooli) {
                            Pelaaja.käyttöViive = 100;
                            if (Peli.huone.annaNimi().equals("Kuu")) {
                                Dialogit.avaaDialogi(e.annaDialogiTekstuuri(), "Sehän toimi", e.annaNimi());
                            }
                            else if (
                                Peli.huone.annaNimi().equals("Metsä") &&
                                Pelaaja.sijX > 24 && Pelaaja.sijX < 28 &&
                                Pelaaja.sijY > 9 && Pelaaja.sijY < 14
                            ) {
                                Peli.uusiHuone = 12;
                                Peli.huoneVaihdettava = true;
                                Pelaaja.teleport(29, 6);
                            }
                            else {
                                Dialogit.avaaDialogi(e.annaDialogiTekstuuri(), "Pitäisiköhän vetää tämä Jumal Velhon luona...", e.annaNimi());
                            }
                        }
                        else e.käytä();
                    }
                    else if (e instanceof Kartta) {
                        e.käytä();
                    }
                    if (e.poistoon()){
                        Pelaaja.esineet[Peli.esineValInt] = null;
                        e = null;
                    }
                }
            }
            else if (e.onkoKenttäkäyttöön()) {
                if (e instanceof Ase) {
                    Ase ase = (Ase)e;
                    if (Pelaaja.käyttöViive <= 0) {
                        Pelaaja.käytettyAse = ase;
                        Pelaaja.hyökkäysAika += ase.annaHyökkäysAika();
                        Pelaaja.käyttöViive += ase.annaHyökkäysViive();
                        Äänet.toistaSFX("Hyökkäys");
                        if (k instanceof Juhani) {
                            Juhani juhani = (Juhani)k;
                            juhani.kuolemaJuhani();
                        }
                    }
                }
            }
        }
    }

    public static void katsoEsinettä(Esine e) {
        if (e != null) {
            Dialogit.avaaDialogi(e.annaTekstuuri(), e.katso(), e.annaNimi());
        }
    }

    public static void katsoKenttää(KenttäKohde k) {
        if (k instanceof NPC_KenttäKohde) {
            Dialogit.avaaDialogi(k.annaTekstuuri(), k.katso(), "???");
        }
        else if (k != null) {
            Dialogit.avaaDialogi(k.annaTekstuuri(), k.katso(), k.annaNimi());
        }
    }
}
