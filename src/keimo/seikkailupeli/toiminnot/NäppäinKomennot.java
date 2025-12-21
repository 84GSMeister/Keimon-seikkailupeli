package keimo.seikkailupeli.toiminnot;

import keimo.keimoengine.KeimoEngine;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.ikkuna.*;
import keimo.keimoengine.io.Input;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.PelinAsetukset;
import keimo.seikkailupeli.Peli.SyötteenTila;
import keimo.seikkailupeli.gui.toimintoIkkunat.*;
import keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat.MinipeliIkkuna3D;
import keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat.MinipeliIkkunaOverflow;
import keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat.MinipeliIkkunaPokeri;
import keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat.MinipeliIkkunaPong;
import keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat.MinipeliIkkunaTetris;
import keimo.seikkailupeli.kenttä.*;
import keimo.seikkailupeli.menu.*;
import keimo.seikkailupeli.menu.asetusRuudut.*;
import keimo.seikkailupeli.menu.editori.EditoriRuutu;
import keimo.seikkailupeli.menu.editori.EditoriRuutuVarmistus;
import keimo.seikkailupeli.menu.editori.gui.EditorinValikko;
import keimo.seikkailupeli.objektit.Pelaaja;
import keimo.seikkailupeli.objektit.Käännettävä.Suunta;
import keimo.seikkailupeli.objektit.Käännettävä.SuuntaVasenOikea;
import keimo.seikkailupeli.objektit.Pelaaja.*;
import keimo.seikkailupeli.objektit.kenttäkohteet.esine.Esine;

import java.util.List;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;

import static org.lwjgl.glfw.GLFW.*;

public class NäppäinKomennot extends Input {

    //static int huoneenId = 0;
    //static float rotZ = 0;
    static boolean setCam = true;

    public static boolean vasenPainettu = false;
    public static boolean oikeaPainettu = false;
    public static boolean ylösPainettu = false;
    public static boolean alasPainettu = false;

    public NäppäinKomennot(long window) {
        super(window);
        avaaMidiLaitteet();
    }

    @Override
    public void tarkistaSyöte() {
        tarkistaNäppäinKomennot();
        tarkistaHiiriKomennot();
        tarkistaPelaajanTila();
    }
    
    private void tarkistaNäppäinKomennot() {

        if (isKeyPressed(GLFW_KEY_F1)) {
            Peli.nollaaPeli();
            KeimoEngine.kaatoTeksti = new Teksti("", 0, 0);
            KeimoEngine.lataaTarinaRuutu("alku");
        }
        else if (isKeyDown(GLFW_KEY_LEFT_ALT) && isKeyPressed(GLFW_KEY_F4)) {
            glfwSetWindowShouldClose(KeimoEngine.window.getWindow(), true);
        }
        else if (isKeyPressed(GLFW_KEY_F11)) {
            KeimoEngine.window.setFullscreen(!KeimoEngine.window.isFullscreen(), false);
        }
        
        switch (Peli.aktiivinenRuutu) {
            case PELIRUUTU:
                switch (Peli.syötteenTila) {
                    case PELI -> {
                        if (setCam) {
                            Kamera.zoomX = KeimoEngine.window.getWidth()/2;
                            Kamera.zoomY = KeimoEngine.window.getHeight()/2;
                            setCam = false;
                        }
                        
                        if (isKeyPressed(GLFW_KEY_ESCAPE)) {
                            Peli.pausetaPeli(true);
                            DialogiValintaIkkuna.avaaToimintoIkkuna("pause");
                        }
                        else if (isKeyPressed(GLFW_KEY_F2)) {
                            KeimoEngine.kaatoTeksti = null;
                        }
                        else if (isKeyPressed(GLFW_KEY_F3)) {
                            PelinAsetukset.debugTiedot = !PelinAsetukset.debugTiedot;
                        }
                        else if (isKeyPressed(GLFW_KEY_F5)) {
                            HuijauskoodiValikko.avaaValikko();
                        }
                
                        if (isKeyDown(GLFW_KEY_A) || isKeyDown(GLFW_KEY_LEFT)) {
                            vasenPainettu = true;
                        }
                        else {
                            vasenPainettu = false;
                        }
                        if (isKeyDown(GLFW_KEY_D) || isKeyDown(GLFW_KEY_RIGHT)) {
                            oikeaPainettu = true;
                        }
                        else {
                            oikeaPainettu = false;
                        }
                        if (isKeyDown(GLFW_KEY_W) || isKeyDown(GLFW_KEY_UP)) {
                            ylösPainettu = true;
                        }
                        else {
                            ylösPainettu = false;
                        }
                        if (isKeyDown(GLFW_KEY_S) || isKeyDown(GLFW_KEY_DOWN)) {
                            alasPainettu = true;
                        }
                        else {
                            alasPainettu = false;
                        }
                        if (vasenPainettu && ylösPainettu) Pelaaja.kokeileLiikkumista(Suunta.YLÄVASEN);
                        else if (vasenPainettu && alasPainettu) Pelaaja.kokeileLiikkumista(Suunta.ALAVASEN);
                        else if (oikeaPainettu && ylösPainettu) Pelaaja.kokeileLiikkumista(Suunta.YLÄOIKEA);
                        else if (oikeaPainettu && alasPainettu) Pelaaja.kokeileLiikkumista(Suunta.ALAOIKEA);
                        else if (vasenPainettu) Pelaaja.kokeileLiikkumista(Suunta.VASEN);
                        else if (oikeaPainettu) Pelaaja.kokeileLiikkumista(Suunta.OIKEA);
                        else if (ylösPainettu) Pelaaja.kokeileLiikkumista(Suunta.YLÖS);
                        else if (alasPainettu) Pelaaja.kokeileLiikkumista(Suunta.ALAS);

                        if (isKeyPressed(GLFW_KEY_E)) {
                            Peli.painaE(Pelaaja.sijX, Pelaaja.sijY);
                        }
                        if (isKeyPressed(GLFW_KEY_Q)) {
                            Peli.painaQ(Pelaaja.sijX, Pelaaja.sijY);
                        }
                        if (isKeyPressed(GLFW_KEY_1)) {
                            tavarapaikanVaihto(0);
                        }
                        else if (isKeyPressed(GLFW_KEY_2)) {
                            tavarapaikanVaihto(1);
                        }
                        else if (isKeyPressed(GLFW_KEY_3)) {
                            tavarapaikanVaihto(2);
                        }
                        else if (isKeyPressed(GLFW_KEY_4)) {
                            tavarapaikanVaihto(3);
                        }
                        else if (isKeyPressed(GLFW_KEY_5)) {
                            tavarapaikanVaihto(4);
                        }
                        else if (isKeyPressed(GLFW_KEY_6)) {
                            tavarapaikanVaihto(5);
                        }
                        if (isKeyDown(GLFW_KEY_SPACE)) {
                            Peli.käyttö(Peli.esineValInt);
                        }
                        if (isKeyPressed(GLFW_KEY_X)) {
                            Peli.katsoEsinettä();
                        }
                        if (isKeyPressed(GLFW_KEY_C)) {
                            Peli.katsoKenttää();
                        }
                        if (isKeyPressed(GLFW_KEY_Z)) {
                            if (Peli.yhdistäminenKäynnissä) {
                                if (!(Peli.yhdistettäväTavarapaikka < 0)) {
                                    if (Peli.kokeileYhdistämistä(Peli.yhdistettäväTavarapaikka, Peli.esineValInt)) {
                                        Pelaaja.esineet[Peli.esineValInt] = Esine.yhdistä2Esinettä(Pelaaja.esineet[Peli.esineValInt], Pelaaja.esineet[Peli.yhdistettäväTavarapaikka]);
                                        Pelaaja.esineet[Peli.yhdistettäväTavarapaikka] = null;
                                        Dialogit.avaaDialogi(Pelaaja.esineet[Peli.esineValInt].annaDialogiTekstuuri(), "Yhdistäminen onnistui! " + "Sait uuden esineen: " + Pelaaja.esineet[Peli.esineValInt].annaNimiSijamuodossa("nominatiivi"), "Yhdistäminen");
                                    }
                                    else {
                                        Dialogit.avaaDialogi("", "Yhdistäminen ei onnistunut.", "Yhdistäminen");
                                    }
                                }
                                Peli.yhdistäminenKäynnissä = false;
                                Peli.yhdistettäväTavarapaikka = -1;
                            }    
                            else if (Peli.tarkistaYhdistettävyys(Peli.esineValInt)) {
                                Peli.yhdistettäväTavarapaikka = Peli.esineValInt;
                                Peli.yhdistäminenKäynnissä = true;
                            }
                            else {
                                if (Pelaaja.esineet[Peli.esineValInt] == null) {
                                    Dialogit.avaaDialogi("", "Ei valittua esinettä", "Yhdistäminen");
                                }
                                else {
                                    Dialogit.avaaDialogi(Pelaaja.esineet[Peli.esineValInt].annaDialogiTekstuuri(), Pelaaja.esineet[Peli.esineValInt].annaNimiSijamuodossa("partitiivi") + " ei voi yhdistää.", "Yhdistäminen");
                                }
                            }
                        }
                    }
                    case DIALOGI -> {
                        if (isKeyPressed(GLFW_KEY_E) || isKeyPressed(GLFW_KEY_X) || isKeyPressed(GLFW_KEY_C)) {
                            Dialogit.kelaaDialogi();
                        }
                        vasenPainettu = false;
                        oikeaPainettu = false;
                        ylösPainettu = false;
                        alasPainettu = false;
                        Pelaaja.keimonState = KeimonState.IDLE;
                    }
                    case TOIMINTO -> {
                        switch (Peli.toimintoIkkuna) {
                            case PULLONPALAUTUS -> {
                                if (PullonPalautusIkkuna.virheenTyyppi == PullonPalautusIkkuna.VirheenTyyppi.PAKKAUS && isKeyPressed(GLFW_KEY_SPACE)) {
                                    PullonPalautusIkkuna.jatkoSyöteAnnettu = true;
                                }
                                else if (PullonPalautusIkkuna.virheenTyyppi == PullonPalautusIkkuna.VirheenTyyppi.MUOTO && isKeyPressed(GLFW_KEY_X)) {
                                    PullonPalautusIkkuna.jatkoSyöteAnnettu = true;
                                }
                                else if (PullonPalautusIkkuna.virheenTyyppi == PullonPalautusIkkuna.VirheenTyyppi.KÄSI && isKeyPressed(GLFW_KEY_C)) {
                                    PullonPalautusIkkuna.jatkoSyöteAnnettu = true;
                                }
                                else if (PullonPalautusIkkuna.virheenTyyppi == PullonPalautusIkkuna.VirheenTyyppi.MERKKI && isKeyPressed(GLFW_KEY_Z)) {
                                    PullonPalautusIkkuna.jatkoSyöteAnnettu = true;
                                }
                                else if (isKeyPressed(GLFW_KEY_ESCAPE)) {
                                    Peli.syötteenTila = SyötteenTila.PELI;
                                }
                            }
                            case VALINTADIALOGI -> {
                                if (isKeyPressed(GLFW_KEY_SPACE) || isKeyPressed(GLFW_KEY_ENTER)) {
                                    DialogiValintaIkkuna.hyväksyValinta();
                                }
                                else if (isKeyPressed(GLFW_KEY_ESCAPE)) {
                                    DialogiValintaIkkuna.peruValinta();
                                }
                                else if (isKeyPressed(GLFW_KEY_W) || isKeyPressed(GLFW_KEY_UP)) {
                                    DialogiValintaIkkuna.pienennäValintaa();
                                }
                                else if (isKeyPressed(GLFW_KEY_S) || isKeyPressed(GLFW_KEY_DOWN)) {
                                    DialogiValintaIkkuna.kasvataValintaa();
                                }
                            }
                            case ÄMPÄRIJONO -> {
                                if (isKeyPressed(GLFW_KEY_SPACE) || isKeyPressed(GLFW_KEY_ENTER)) {
                                    ÄmpäriJonoIkkuna.keskeytetty = true;
                                }
                            }
                            case MINIPELI_0 -> {
                                if (isKeyPressed(GLFW_KEY_ESCAPE) || isKeyPressed(GLFW_KEY_ENTER)) {
                                    MinipeliIkkuna3D.suljeToimintoIkkuna();
                                }

                                if (isKeyDown(GLFW_KEY_W)) {
                                    Maailma3D.liiku(Maailma3D.Liike.ETEENPÄIN);
                                    System.out.println("z-sij: " + Maailma3D.zSij);
                                }
                                if (isKeyDown(GLFW_KEY_S)) {
                                    Maailma3D.liiku(Maailma3D.Liike.TAAKSEPÄIN);
                                    System.out.println("z-sij: " + Maailma3D.zSij);
                                }
                                if (isKeyDown(GLFW_KEY_A)) {
                                    Maailma3D.liiku(Maailma3D.Liike.VASEN);
                                    System.out.println("x-sij: " + Maailma3D.xSij);
                                }
                                if (isKeyDown(GLFW_KEY_D)) {
                                    Maailma3D.liiku(Maailma3D.Liike.OIKEA);
                                    System.out.println("x-sij: " + Maailma3D.xSij);
                                }
                                if (isKeyDown(GLFW_KEY_Q)) {
                                    Maailma3D.liiku(Maailma3D.Liike.ALAS);
                                    System.out.println("y-sij: " + Maailma3D.ySij);
                                }
                                if (isKeyDown(GLFW_KEY_E)) {
                                    Maailma3D.liiku(Maailma3D.Liike.YLÖS);
                                    System.out.println("y-sij: " + Maailma3D.ySij);
                                }
                
                                if (isKeyDown(GLFW_KEY_KP_4)) {
                                    Maailma3D.yaw -= Maailma3D.kääntöNopeus;
                                    Maailma3D.yaw %= 360;
                                    System.out.println("yaw: " + Maailma3D.yaw);
                                }
                                if (isKeyDown(GLFW_KEY_KP_6)) {
                                    Maailma3D.yaw += Maailma3D.kääntöNopeus;
                                    Maailma3D.yaw %= 360;
                                    System.out.println("yaw: " + Maailma3D.yaw);
                                }
                            }
                            case MINIPELI_1 -> {
                                if (isKeyPressed(GLFW_KEY_ESCAPE) || isKeyPressed(GLFW_KEY_ENTER)) {
                                    MinipeliIkkunaPong.suljeToimintoIkkuna();
                                }

                                if (isKeyHeld(GLFW_KEY_SPACE)) {
                                    MinipeliIkkunaPong.ohitaValikko();
                                }
                                if (isKeyDown(GLFW_KEY_W) || isKeyDown(GLFW_KEY_UP)) {
                                    if (MinipeliIkkunaPong.pelaajanSijY < MinipeliIkkunaPong.maxY) MinipeliIkkunaPong.pelaajanSijY++;
                                }
                                if (isKeyDown(GLFW_KEY_S) || isKeyDown(GLFW_KEY_DOWN)) {
                                    if (MinipeliIkkunaPong.pelaajanSijY > MinipeliIkkunaPong.minY) MinipeliIkkunaPong.pelaajanSijY--;
                                }
                            }
                            case MINIPELI_2 -> {
                                if (isKeyPressed(GLFW_KEY_ESCAPE) || isKeyPressed(GLFW_KEY_ENTER)) {
                                    MinipeliIkkunaPokeri.suljeToimintoIkkuna();
                                }

                                if (isKeyPressed(GLFW_KEY_1)) {
                                    MinipeliIkkunaPokeri.valitseKortti(0);
                                }
                                if (isKeyPressed(GLFW_KEY_2)) {
                                    MinipeliIkkunaPokeri.valitseKortti(1);
                                }
                                if (isKeyPressed(GLFW_KEY_3)) {
                                    MinipeliIkkunaPokeri.valitseKortti(2);
                                }
                                if (isKeyPressed(GLFW_KEY_4)) {
                                    MinipeliIkkunaPokeri.valitseKortti(3);
                                }
                                if (isKeyPressed(GLFW_KEY_5)) {
                                    MinipeliIkkunaPokeri.valitseKortti(4);
                                }
                                if (isKeyPressed(GLFW_KEY_SPACE)) {
                                    MinipeliIkkunaPokeri.pelaaValitut();
                                }
                            }
                            case MINIPELI_3 -> {
                                if (isKeyPressed(GLFW_KEY_ESCAPE) || isKeyPressed(GLFW_KEY_ENTER)) {
                                    MinipeliIkkunaTetris.suljeToimintoIkkuna();
                                }

                                if (isKeyPressed(GLFW_KEY_SPACE)) {
                                    MinipeliIkkunaTetris.ohitaValikko();
                                }
                                if (isKeyHeld(GLFW_KEY_A) || isKeyHeld(GLFW_KEY_LEFT)) {
                                    MinipeliIkkunaTetris.siirräPalikkaa(false);
                                }
                                if (isKeyHeld(GLFW_KEY_D) || isKeyHeld(GLFW_KEY_RIGHT)) {
                                    MinipeliIkkunaTetris.siirräPalikkaa(true);
                                }
                                if (isKeyPressed(GLFW_KEY_SPACE) | isKeyPressed(GLFW_KEY_W) || isKeyPressed(GLFW_KEY_UP)) {
                                    MinipeliIkkunaTetris.käännäPalikkaa();
                                }
                                if (isKeyPressed(GLFW_KEY_S) || isKeyPressed(GLFW_KEY_DOWN)) {
                                    MinipeliIkkunaTetris.pudotaPalikka();
                                }
                            }
                            case MINIPELI_4 -> {
                                if (isKeyPressed(GLFW_KEY_ESCAPE) || isKeyPressed(GLFW_KEY_ENTER) || isKeyPressed(GLFW_KEY_ESCAPE)) {
                                    MinipeliIkkunaOverflow.suljeToimintoIkkuna();
                                }
                            }
                            case KARTTA -> {
                                if (isKeyPressed(GLFW_KEY_SPACE) || isKeyPressed(GLFW_KEY_ENTER) || isKeyPressed(GLFW_KEY_ESCAPE)) {
                                    KarttaIkkuna.suljeToimintoIkkuna();
                                }
                            }
                            case OHJEET -> {
                                if (isKeyPressed(GLFW_KEY_SPACE) || isKeyPressed(GLFW_KEY_ENTER) || isKeyPressed(GLFW_KEY_ESCAPE)) {
                                    OhjeIkkuna.suljeToimintoIkkuna();
                                }
                            }
                            case HUIJAUSKOODIT -> {
                                if (isKeyPressed(GLFW_KEY_ESCAPE) || isKeyPressed(GLFW_KEY_ENTER) || isKeyHeld(GLFW_KEY_SPACE) || isKeyPressed(GLFW_KEY_F5)) {
                                    HuijauskoodiValikko.suljeValikko();
                                }
                                if (isKeyHeld(GLFW_KEY_A) || isKeyHeld(GLFW_KEY_LEFT) || isKeyHeld(GLFW_KEY_D) || isKeyHeld(GLFW_KEY_RIGHT)) {
                                    HuijauskoodiValikko.muutaValintaa();
                                }
                                if (isKeyHeld(GLFW_KEY_W) || isKeyHeld(GLFW_KEY_UP)) {
                                    HuijauskoodiValikko.pienennäValintaa();
                                }
                                if (isKeyHeld(GLFW_KEY_S) || isKeyHeld(GLFW_KEY_DOWN)) {
                                    HuijauskoodiValikko.kasvataValintaa();
                                }
                            }
                        }
                    }
                }
            break;
            case TARINARUUTU:
                if (isKeyPressed(GLFW_KEY_SPACE)) {
                    TarinaRuutu.jatka();
                }
            break;
            case VALIKKORUUTU:
                if (isKeyHeld(GLFW_KEY_S) || isKeyHeld(GLFW_KEY_DOWN)) {
                    ValikkoRuutu.painaNäppäintä("alas");
                }
                if (isKeyHeld(GLFW_KEY_W) || isKeyHeld(GLFW_KEY_UP)) {
                    ValikkoRuutu.painaNäppäintä("ylös");
                }
                if (isKeyPressed(GLFW_KEY_SPACE) || isKeyPressed(GLFW_KEY_ENTER)) {
                    ValikkoRuutu.painaNäppäintä("enter");
                }
            break;
            case ASETUSRUUTU:
                if (isKeyPressed(GLFW_KEY_S) || isKeyPressed(GLFW_KEY_DOWN)) {
                    AsetusRuutu.painaNäppäintä("alas");
                }
                if (isKeyPressed(GLFW_KEY_W) || isKeyPressed(GLFW_KEY_UP)) {
                    AsetusRuutu.painaNäppäintä("ylös");
                }
                if (isKeyPressed(GLFW_KEY_SPACE) || isKeyPressed(GLFW_KEY_ENTER)) {
                    AsetusRuutu.painaNäppäintä("enter");
                }
            break;
            case ASETUSRUUTU_GRAFIIKKA:
                if (isKeyPressed(GLFW_KEY_S) || isKeyPressed(GLFW_KEY_DOWN)) {
                    GrafiikkaAsetusRuutu.painaNäppäintä("alas");
                }
                if (isKeyPressed(GLFW_KEY_W) || isKeyPressed(GLFW_KEY_UP)) {
                    GrafiikkaAsetusRuutu.painaNäppäintä("ylös");
                }
                if (isKeyHeld(GLFW_KEY_D) || isKeyHeld(GLFW_KEY_RIGHT)) {
                    GrafiikkaAsetusRuutu.painaNäppäintä("oikea");
                }
                if (isKeyHeld(GLFW_KEY_A) || isKeyHeld(GLFW_KEY_LEFT)) {
                    GrafiikkaAsetusRuutu.painaNäppäintä("vasen");
                }
                if (isKeyPressed(GLFW_KEY_SPACE) || isKeyPressed(GLFW_KEY_ENTER)) {
                    GrafiikkaAsetusRuutu.painaNäppäintä("enter");
                }
            break;
            case ASETUSRUUTU_ÄÄNI:
                if (isKeyPressed(GLFW_KEY_S) || isKeyPressed(GLFW_KEY_DOWN)) {
                    ÄäniAsetusRuutu.painaNäppäintä("alas");
                }
                if (isKeyPressed(GLFW_KEY_W) || isKeyPressed(GLFW_KEY_UP)) {
                    ÄäniAsetusRuutu.painaNäppäintä("ylös");
                }
                if (isKeyHeld(GLFW_KEY_D) || isKeyHeld(GLFW_KEY_RIGHT)) {
                    ÄäniAsetusRuutu.painaNäppäintä("oikea");
                }
                if (isKeyHeld(GLFW_KEY_A) || isKeyHeld(GLFW_KEY_LEFT)) {
                    ÄäniAsetusRuutu.painaNäppäintä("vasen");
                }
                if (isKeyPressed(GLFW_KEY_SPACE) || isKeyPressed(GLFW_KEY_ENTER)) {
                    ÄäniAsetusRuutu.painaNäppäintä("enter");
                }
            break;
            case ASETUSRUUTU_PELI:
                if (isKeyPressed(GLFW_KEY_S) || isKeyPressed(GLFW_KEY_DOWN)) {
                    PeliAsetusRuutu.painaNäppäintä("alas");
                }
                if (isKeyPressed(GLFW_KEY_W) || isKeyPressed(GLFW_KEY_UP)) {
                    PeliAsetusRuutu.painaNäppäintä("ylös");
                }
                if (isKeyHeld(GLFW_KEY_D) || isKeyHeld(GLFW_KEY_RIGHT)) {
                    PeliAsetusRuutu.painaNäppäintä("oikea");
                }
                if (isKeyHeld(GLFW_KEY_A) || isKeyHeld(GLFW_KEY_LEFT)) {
                    PeliAsetusRuutu.painaNäppäintä("vasen");
                }
                if (isKeyPressed(GLFW_KEY_SPACE) || isKeyPressed(GLFW_KEY_ENTER)) {
                    PeliAsetusRuutu.painaNäppäintä("enter");
                }
            break;
            case ASETUSRUUTU_ÄÄNITESTI:
                if (isKeyPressed(GLFW_KEY_S) || isKeyPressed(GLFW_KEY_DOWN)) {
                    ÄäniTestiRuutu.painaNäppäintä("alas");
                }
                if (isKeyPressed(GLFW_KEY_W) || isKeyPressed(GLFW_KEY_UP)) {
                    ÄäniTestiRuutu.painaNäppäintä("ylös");
                }
                if (isKeyHeld(GLFW_KEY_D) || isKeyHeld(GLFW_KEY_RIGHT)) {
                    ÄäniTestiRuutu.painaNäppäintä("oikea");
                }
                if (isKeyHeld(GLFW_KEY_A) || isKeyHeld(GLFW_KEY_LEFT)) {
                    ÄäniTestiRuutu.painaNäppäintä("vasen");
                }
                if (isKeyPressed(GLFW_KEY_SPACE) || isKeyPressed(GLFW_KEY_ENTER)) {
                    ÄäniTestiRuutu.painaNäppäintä("enter");
                }
                if (isKeyPressed(GLFW_KEY_ESCAPE)) {
                    ÄäniTestiRuutu.painaNäppäintä("esc");
                }
            break;
            case KEHITTÄJÄRUUTU:
                if (isKeyPressed(GLFW_KEY_SPACE) || isKeyPressed(GLFW_KEY_ENTER)) {
                    KehittäjäRuutu.takaisin();
                }
            break;
            case LOPPURUUTU:
                if (isKeyHeld(GLFW_KEY_S) || isKeyHeld(GLFW_KEY_DOWN)) {
                    LoppuRuutu.painaNäppäintä("alas");
                }
                if (isKeyHeld(GLFW_KEY_W) || isKeyHeld(GLFW_KEY_UP)) {
                    LoppuRuutu.painaNäppäintä("ylös");
                }
                if (isKeyPressed(GLFW_KEY_SPACE) || isKeyPressed(GLFW_KEY_ENTER)) {
                    LoppuRuutu.painaNäppäintä("enter");
                }
            break;
            case MINIPELIRUUTU:
                if (isKeyPressed(GLFW_KEY_ESCAPE) || isKeyPressed(GLFW_KEY_ENTER)) {
                    MinipeliIkkuna3D.suljeToimintoIkkuna();
                }
                if (isKeyPressed(GLFW_KEY_F3)) {
                    Maailma3D.debugTiedotNäkyvissä = !Maailma3D.debugTiedotNäkyvissä;
                }
                if (isKeyPressed(GLFW_KEY_F5)) {
                    Maailma3D.moonJump = !Maailma3D.moonJump;
                }
                if (isKeyDown(GLFW_KEY_W)) {
                    Maailma3D.liiku(Maailma3D.Liike.ETEENPÄIN);
                }
                if (isKeyDown(GLFW_KEY_S)) {
                    Maailma3D.liiku(Maailma3D.Liike.TAAKSEPÄIN);
                }
                if (isKeyDown(GLFW_KEY_A)) {
                    Maailma3D.liiku(Maailma3D.Liike.VASEN);
                }
                if (isKeyDown(GLFW_KEY_D)) {
                    Maailma3D.liiku(Maailma3D.Liike.OIKEA);
                }
                if (isKeyDown(GLFW_KEY_SPACE)) {
                    Maailma3D.liiku(Maailma3D.Liike.HYPPY);
                }
                if (isKeyDown(GLFW_KEY_KP_4) || isKeyDown(GLFW_KEY_LEFT)) {
                    Maailma3D.yaw -= Maailma3D.kääntöNopeus;
                    if (Maailma3D.yaw < 0) Maailma3D.yaw += 360;
                    Maailma3D.yaw %= 360;
                }
                if (isKeyDown(GLFW_KEY_KP_6) || isKeyDown(GLFW_KEY_RIGHT)) {
                    Maailma3D.yaw += Maailma3D.kääntöNopeus;
                    Maailma3D.yaw %= 360;
                }
                if (isKeyDown(GLFW_KEY_KP_2) || isKeyDown(GLFW_KEY_DOWN)) {
                    Maailma3D.pitch -= Maailma3D.kääntöNopeus;
                    // if (Maailma3D.pitch < -89.999) Maailma3D.pitch = -89.999f;
                    // else Maailma3D.pitch = Math.round(Maailma3D.pitch);
                    if (Maailma3D.pitch < -90) Maailma3D.pitch = -90;
                }
                if (isKeyDown(GLFW_KEY_KP_8) || isKeyDown(GLFW_KEY_UP)) {
                    Maailma3D.pitch += Maailma3D.kääntöNopeus;
                    // if (Maailma3D.pitch > 89.999) Maailma3D.pitch = 89.999f;
                    // else Maailma3D.pitch = Math.round(Maailma3D.pitch);
                    if (Maailma3D.pitch < -90) Maailma3D.pitch = -90;
                }
                if (isKeyDown(GLFW_KEY_KP_7)) {
                    Maailma3D.roll -= Maailma3D.kääntöNopeus;
                    if (Maailma3D.roll < 0) Maailma3D.roll += 360;
                    Maailma3D.roll %= 360;
                }
                if (isKeyDown(GLFW_KEY_KP_9)) {
                    Maailma3D.roll += Maailma3D.kääntöNopeus;
                    Maailma3D.roll %= 360;
                }
                if (isKeyPressed(GLFW_KEY_MINUS) || isKeyPressed(GLFW_KEY_KP_SUBTRACT)) {
                    Maailma3D.vaihdaHuonetta(Maailma3D.annaHuoneenId()-1);
                }
                if (isKeyPressed(59) || isKeyPressed(GLFW_KEY_KP_ADD)) {
                    Maailma3D.vaihdaHuonetta(Maailma3D.annaHuoneenId()+1);
                }
            break;
            case EDITORIRUUTU:
                switch (Peli.syötteenTila) {
                    case PELI -> {
                        if (isKeyPressed(GLFW_KEY_ESCAPE)) {
                            EditoriRuutu.painaEsc();
                        }
                        if (isKeyPressed(GLFW_KEY_F3)) {
                            EditoriRuutu.debugTiedotNäkyvissä = !EditoriRuutu.debugTiedotNäkyvissä;
                        }
                        if (isKeyDown(GLFW_KEY_W) || isKeyHeld(GLFW_KEY_UP)) {
                            EditoriRuutu.kameranSijY -= 1;
                            if (EditoriRuutu.aktiivinenKomponentti == EditoriRuutu.EditorinTilat.POPUP) EditoriRuutu.avaaPopup(false);
                        }
                        if (isKeyDown(GLFW_KEY_S) || isKeyHeld(GLFW_KEY_DOWN)) {
                            EditoriRuutu.kameranSijY += 1;
                            if (EditoriRuutu.aktiivinenKomponentti == EditoriRuutu.EditorinTilat.POPUP) EditoriRuutu.avaaPopup(false);
                        }
                        if (isKeyDown(GLFW_KEY_A) || isKeyHeld(GLFW_KEY_LEFT)) {
                            EditoriRuutu.kameranSijX += 1;
                            if (EditoriRuutu.aktiivinenKomponentti == EditoriRuutu.EditorinTilat.POPUP) EditoriRuutu.avaaPopup(false);
                        }
                        if (isKeyDown(GLFW_KEY_D) || isKeyHeld(GLFW_KEY_RIGHT)) {
                            EditoriRuutu.kameranSijX -= 1;
                            if (EditoriRuutu.aktiivinenKomponentti == EditoriRuutu.EditorinTilat.POPUP) EditoriRuutu.avaaPopup(false);
                        }
                        if (isKeyDown(GLFW_KEY_LEFT_CONTROL)) {
                            EditoriRuutu.kopiointi = true;
                        }
                        else EditoriRuutu.kopiointi = false;
                        if (isKeyPressed(47) || isKeyPressed(GLFW_KEY_KP_SUBTRACT)) {
                            if (EditoriRuutu.zoom < 5) EditoriRuutu.zoom += 0.1;
                        }
                        if (isKeyPressed(GLFW_KEY_MINUS) || isKeyPressed(GLFW_KEY_KP_ADD)) {
                            if (EditoriRuutu.zoom > 0.5) EditoriRuutu.zoom -= 0.1;
                        }
                    }
                    case TOIMINTO -> {
                        if (isKeyPressed(GLFW_KEY_ESCAPE)) {
                            EditorinValikko.suljeValikko();
                        }
                        if (isKeyHeld(GLFW_KEY_W) || isKeyHeld(GLFW_KEY_UP)) {
                            EditorinValikko.pienennäValintaa();
                        }
                        if (isKeyHeld(GLFW_KEY_S) || isKeyHeld(GLFW_KEY_DOWN)) {
                            EditorinValikko.kasvataValintaa();
                        }
                        if (isKeyPressed(GLFW_KEY_ENTER) || isKeyPressed(GLFW_KEY_SPACE)) {
                            EditorinValikko.hyväksyValinta();
                        }
                    }
                    default -> {}
                }
                
            break;
            case EDITORIRUUTU_VARMISTUS:
                if (isKeyPressed(GLFW_KEY_A) || isKeyPressed(GLFW_KEY_LEFT)) {
                    EditoriRuutuVarmistus.painaNäppäintä("ylös");
                }
                if (isKeyPressed(GLFW_KEY_D) || isKeyPressed(GLFW_KEY_RIGHT)) {
                    EditoriRuutuVarmistus.painaNäppäintä("alas");
                }
                if (isKeyPressed(GLFW_KEY_SPACE) || isKeyPressed(GLFW_KEY_ENTER)) {
                    EditoriRuutuVarmistus.painaNäppäintä("enter");
                }
            break;
            case VIRHERUUTU:
                if (isKeyPressed(GLFW_KEY_ESCAPE)) {
                    System.exit(1);
                }
            break;
            case null, default:
            break;
        }
    }

    private void tarkistaHiiriKomennot() {
        switch (Peli.aktiivinenRuutu) {
            case EDITORIRUUTU -> {
                int mouseX = (int)getCursorPosX();
                int mouseY = (int)getCursorPosY();
                int scrollX = (int)getScrollX(); // En tiedä mihin ikinä tarvii vaakasuuntaista rullausta mut pidetään mukana
                int scrollY = (int)getScrollY();
                EditoriRuutu.tarkistaHoverNapit(mouseX, mouseY);
                if (isMouseButtonPressed(GLFW_MOUSE_BUTTON_LEFT)) {
                    EditoriRuutu.hiirenVasenToiminto(mouseX, mouseY);
                }
                if (isMouseButtonPressed(GLFW_MOUSE_BUTTON_RIGHT)) {
                    EditoriRuutu.hiirenOikeaToiminto(mouseX, mouseY);
                }
                if (isMouseButtonPressed(GLFW_MOUSE_BUTTON_MIDDLE)) {
                    EditoriRuutu.hiirenKeskiToiminto(mouseX, mouseY);
                }
                if (isMouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                    EditoriRuutu.hiirenVasenToimintoHold(mouseX, mouseY);
                }
                if (isMouseButtonDown(GLFW_MOUSE_BUTTON_RIGHT)) {
                    EditoriRuutu.hiirenOikeaToimintoHold(mouseX, mouseY);
                }
                if (isMouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE)) {
                    EditoriRuutu.hiirenKeskiToimintoHold(mouseX, mouseY);
                }
                if (isMouseButtonReleased(GLFW_MOUSE_BUTTON_LEFT)) {
                    EditoriRuutu.hiirenVasenToimintoRelease(mouseX, mouseY);
                }
                EditoriRuutu.päivitäFokus(mouseX, mouseY);
                if (updateScroll) {
                    EditoriRuutu.päivitäZoom(scrollY);
                    scrollX = 0;
                    scrollY = 0;
                    updateScroll = false;
                }
            }
            case null, default -> {}
        }
    }

    private void tarkistaPelaajanTila() {
        if (Peli.syötteenTila == SyötteenTila.PELI) {
            if (isKeyDown(GLFW_KEY_A) ||
                isKeyDown(GLFW_KEY_D) ||
                isKeyDown(GLFW_KEY_W) ||
                isKeyDown(GLFW_KEY_S) ||
                isKeyDown(GLFW_KEY_LEFT) ||
                isKeyDown(GLFW_KEY_RIGHT) ||
                isKeyDown(GLFW_KEY_UP) ||
                isKeyDown(GLFW_KEY_DOWN)
            ) {
                Pelaaja.keimonState = KeimonState.JUOKSU;
            }
            else Pelaaja.keimonState = KeimonState.IDLE;

            if ((isKeyDown(GLFW_KEY_A) || isKeyDown(GLFW_KEY_LEFT)) && ((isKeyDown(GLFW_KEY_W) || isKeyDown(GLFW_KEY_UP)))) {
                Pelaaja.keimonSuunta = Suunta.YLÄVASEN;
                Pelaaja.keimonSuuntaVasenOikea = SuuntaVasenOikea.VASEN;
            }
            else if ((isKeyDown(GLFW_KEY_A) || isKeyDown(GLFW_KEY_LEFT)) && ((isKeyDown(GLFW_KEY_S) || isKeyDown(GLFW_KEY_DOWN)))) {
                Pelaaja.keimonSuunta = Suunta.ALAVASEN;
                Pelaaja.keimonSuuntaVasenOikea = SuuntaVasenOikea.VASEN;
            }
            else if ((isKeyDown(GLFW_KEY_D) || isKeyDown(GLFW_KEY_RIGHT)) && ((isKeyDown(GLFW_KEY_W) || isKeyDown(GLFW_KEY_UP)))) {
                Pelaaja.keimonSuunta = Suunta.YLÄOIKEA;
                Pelaaja.keimonSuuntaVasenOikea = SuuntaVasenOikea.OIKEA;
            }
            else if ((isKeyDown(GLFW_KEY_D) || isKeyDown(GLFW_KEY_RIGHT)) && ((isKeyDown(GLFW_KEY_S) || isKeyDown(GLFW_KEY_DOWN)))) {
                Pelaaja.keimonSuunta = Suunta.ALAOIKEA;
                Pelaaja.keimonSuuntaVasenOikea = SuuntaVasenOikea.OIKEA;
            }
            else if (isKeyDown(GLFW_KEY_A) || isKeyDown(GLFW_KEY_LEFT)) {
                Pelaaja.keimonSuunta = Suunta.VASEN;
                Pelaaja.keimonSuuntaVasenOikea = SuuntaVasenOikea.VASEN;
            }
            else if (isKeyDown(GLFW_KEY_D) || isKeyDown(GLFW_KEY_RIGHT)) {
                Pelaaja.keimonSuunta = Suunta.OIKEA;
                Pelaaja.keimonSuuntaVasenOikea = SuuntaVasenOikea.OIKEA;
            }
            else if (isKeyDown(GLFW_KEY_W) || isKeyDown(GLFW_KEY_UP)) {
                Pelaaja.keimonSuunta = Suunta.YLÖS;
            }
            else if (isKeyDown(GLFW_KEY_S) || isKeyDown(GLFW_KEY_DOWN)) {
                Pelaaja.keimonSuunta = Suunta.ALAS;
            }
        }
    }

    private static void tavarapaikanVaihto(int tavarapaikka) {
        Peli.esineValInt = tavarapaikka;
        Peli.valittuEsine = Pelaaja.esineet[Peli.esineValInt];
    }

    private void avaaMidiLaitteet() {
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            MidiDevice midiLaite;
            MidiDevice.Info[] midiLaiteInfot = MidiSystem.getMidiDeviceInfo();
            for (int i = 0; i < midiLaiteInfot.length; i++) {
                try {
                    midiLaite = MidiSystem.getMidiDevice(midiLaiteInfot[i]);
                    List<Transmitter> transmitters = midiLaite.getTransmitters();
                    for(int j = 0; j < transmitters.size(); j++) {
                        transmitters.get(j).setReceiver(new MidiInputReceiver(midiLaite.getDeviceInfo().toString(), this));
                    }

                    Transmitter trans = midiLaite.getTransmitter();
                    trans.setReceiver(new MidiInputReceiver(midiLaite.getDeviceInfo().toString(), this));
                    midiLaite.open();
                }
                catch (MidiUnavailableException e) {
                    //e.printStackTrace();
                }
            }
        }
        catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }
}
