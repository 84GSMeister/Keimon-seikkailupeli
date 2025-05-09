package keimo.keimoEngine.io;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.PelinAsetukset;
import keimo.Pelaaja.*;
import keimo.Peli.SyötteenTila;
import keimo.keimoEngine.toiminnot.Dialogit;
import keimo.Utility.Käännettävä.Suunta;
import keimo.Utility.Käännettävä.SuuntaVasenOikea;
import keimo.keimoEngine.KeimoEngine;
import keimo.keimoEngine.grafiikat.Teksti;
import keimo.keimoEngine.gui.toimintoIkkunat.*;
import keimo.keimoEngine.ikkuna.*;
import keimo.keimoEngine.kenttä.*;
import keimo.keimoEngine.menu.*;
import keimo.keimoEngine.menu.asetusRuudut.*;
import keimo.kenttäkohteet.esine.Esine;

import static org.lwjgl.glfw.GLFW.*;

public class NäppäinKomennot {

    static int huoneenId = 0;
    static float rotZ = 0;
    static boolean setCam = true;

    public static boolean vasenPainettu = false;
    public static boolean oikeaPainettu = false;
    public static boolean ylösPainettu = false;
    public static boolean alasPainettu = false;

    public static void tarkistaSyöte(Window window, Kamera camera, Maailma world) {
        tarkistaNäppäinKomennot(window, camera, world);
        tarkistaPainetutNäppäimet();
        tarkistaPelaajanTila(window);
    }
    
    private static void tarkistaNäppäinKomennot(Window window, Kamera camera, Maailma world) {

        if (window.getInput().isKeyPressed(GLFW_KEY_F1)) {
            Peli.nollaaPeli();
            KeimoEngine.kaatoTeksti = new Teksti("", 0, 0);
            KeimoEngine.lataaTarinaRuutu("alku");
        }
        else if (window.getInput().isKeyPressed(GLFW_KEY_F2)) {
            KeimoEngine.kaatoTeksti = null;
        }
        else if (window.getInput().isKeyPressed(GLFW_KEY_F3)) {
            PelinAsetukset.debugTiedot = !PelinAsetukset.debugTiedot;
        }
        else if (window.getInput().isKeyPressed(GLFW_KEY_F5)) {
            Pelaaja.noclip = !Pelaaja.noclip;
        }
        else if (window.getInput().isKeyPressed(GLFW_KEY_F6)) {
            Pelaaja.ohitaTavoitteet = !Pelaaja.ohitaTavoitteet;
        }
        else if (window.getInput().isKeyPressed(GLFW_KEY_F7)) {
            Pelaaja.loputonRaha = !Pelaaja.loputonRaha;
        }
        else if (window.getInput().isKeyDown(GLFW_KEY_LEFT_ALT) && window.getInput().isKeyPressed(GLFW_KEY_F4)) {
            glfwSetWindowShouldClose(window.getWindow(), true);
        }
        else if (window.getInput().isKeyPressed(GLFW_KEY_F11)) {
            window.setFullscreen(!window.isFullscreen(), false);
        }
        
        switch (Peli.aktiivinenRuutu) {
            case PELIRUUTU:
                switch (Peli.syötteenTila) {
                    case PELI -> {
                        if (setCam) {
                            Kamera.zoomX = window.getWidth()/2;
                            Kamera.zoomY = window.getHeight()/2;
                            setCam = false;
                        }
                        
                        
                        if (window.getInput().isKeyPressed(GLFW_KEY_ESCAPE)) {
                            Peli.pausetaPeli(true);
                            DialogiValintaIkkuna.avaaToimintoIkkuna("pause");
                        }
                
                        if (window.getInput().isKeyDown(GLFW_KEY_A) || window.getInput().isKeyDown(GLFW_KEY_LEFT)) {
                            vasenPainettu = true;
                        }
                        else {
                            vasenPainettu = false;
                        }
                        if (window.getInput().isKeyDown(GLFW_KEY_D) || window.getInput().isKeyDown(GLFW_KEY_RIGHT)) {
                            oikeaPainettu = true;
                        }
                        else {
                            oikeaPainettu = false;
                        }
                        if (window.getInput().isKeyDown(GLFW_KEY_W) || window.getInput().isKeyDown(GLFW_KEY_UP)) {
                            ylösPainettu = true;
                        }
                        else {
                            ylösPainettu = false;
                        }
                        if (window.getInput().isKeyDown(GLFW_KEY_S) || window.getInput().isKeyDown(GLFW_KEY_DOWN)) {
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

                        if (window.getInput().isKeyPressed(GLFW_KEY_E)) {
                            Peli.painaE(Pelaaja.sijX, Pelaaja.sijY);
                        }
                        if (window.getInput().isKeyPressed(GLFW_KEY_Q)) {
                            Peli.pudota(Pelaaja.sijX, Pelaaja.sijY, Peli.esineValInt);
                        }
                        if (window.getInput().isKeyPressed(GLFW_KEY_1)) {
                            tavarapaikanVaihto(0);
                        }
                        else if (window.getInput().isKeyPressed(GLFW_KEY_2)) {
                            tavarapaikanVaihto(1);
                        }
                        else if (window.getInput().isKeyPressed(GLFW_KEY_3)) {
                            tavarapaikanVaihto(2);
                        }
                        else if (window.getInput().isKeyPressed(GLFW_KEY_4)) {
                            tavarapaikanVaihto(3);
                        }
                        else if (window.getInput().isKeyPressed(GLFW_KEY_5)) {
                            tavarapaikanVaihto(4);
                        }
                        else if (window.getInput().isKeyPressed(GLFW_KEY_6)) {
                            tavarapaikanVaihto(5);
                        }
                        if (window.getInput().isKeyDown(GLFW_KEY_SPACE)) {
                            Peli.käyttö(Peli.esineValInt);
                        }
                        if (window.getInput().isKeyPressed(GLFW_KEY_X)) {
                            Peli.katsoEsinettä();
                        }
                        if (window.getInput().isKeyPressed(GLFW_KEY_C)) {
                            Peli.katsoKenttää();
                        }
                        if (window.getInput().isKeyPressed(GLFW_KEY_Z)) {
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
                        if (window.getInput().isKeyPressed(GLFW_KEY_E) || window.getInput().isKeyPressed(GLFW_KEY_X) || window.getInput().isKeyPressed(GLFW_KEY_C)) {
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
                                if (PullonPalautusIkkuna.virheenTyyppi == PullonPalautusIkkuna.VirheenTyyppi.PAKKAUS && window.getInput().isKeyPressed(GLFW_KEY_SPACE)) {
                                    PullonPalautusIkkuna.jatkoSyöteAnnettu = true;
                                }
                                else if (PullonPalautusIkkuna.virheenTyyppi == PullonPalautusIkkuna.VirheenTyyppi.MUOTO && window.getInput().isKeyPressed(GLFW_KEY_X)) {
                                    PullonPalautusIkkuna.jatkoSyöteAnnettu = true;
                                }
                                else if (PullonPalautusIkkuna.virheenTyyppi == PullonPalautusIkkuna.VirheenTyyppi.KÄSI && window.getInput().isKeyPressed(GLFW_KEY_C)) {
                                    PullonPalautusIkkuna.jatkoSyöteAnnettu = true;
                                }
                                else if (PullonPalautusIkkuna.virheenTyyppi == PullonPalautusIkkuna.VirheenTyyppi.MERKKI && window.getInput().isKeyPressed(GLFW_KEY_Z)) {
                                    PullonPalautusIkkuna.jatkoSyöteAnnettu = true;
                                }
                                else if (window.getInput().isKeyPressed(GLFW_KEY_ESCAPE)) {
                                    Peli.syötteenTila = SyötteenTila.PELI;
                                }
                            }
                            case VALINTADIALOGI -> {
                                if (window.getInput().isKeyPressed(GLFW_KEY_SPACE) || window.getInput().isKeyPressed(GLFW_KEY_ENTER)) {
                                    DialogiValintaIkkuna.hyväksyValinta();
                                }
                                else if (window.getInput().isKeyPressed(GLFW_KEY_ESCAPE)) {
                                    DialogiValintaIkkuna.peruValinta();
                                }
                                else if (window.getInput().isKeyPressed(GLFW_KEY_W) || window.getInput().isKeyPressed(GLFW_KEY_UP)) {
                                    DialogiValintaIkkuna.pienennäValintaa();
                                }
                                else if (window.getInput().isKeyPressed(GLFW_KEY_S) || window.getInput().isKeyPressed(GLFW_KEY_DOWN)) {
                                    DialogiValintaIkkuna.kasvataValintaa();
                                }
                            }
                            case ÄMPÄRIJONO -> {
                                if (window.getInput().isKeyPressed(GLFW_KEY_SPACE) || window.getInput().isKeyPressed(GLFW_KEY_ENTER)) {
                                    ÄmpäriJonoIkkuna.keskeytetty = true;
                                }
                            }
                            case MINIPELI -> {
                                if (window.getInput().isKeyPressed(GLFW_KEY_ESCAPE) || window.getInput().isKeyPressed(GLFW_KEY_ENTER)) {
                                    MinipeliIkkuna.suljeToimintoIkkuna();
                                }

                                if (window.getInput().isKeyDown(GLFW_KEY_W)) {
                                    Maailma3D.liiku(Maailma3D.Liike.ETEENPÄIN);
                                    System.out.println("z-sij: " + Maailma3D.zSij);
                                }
                                if (window.getInput().isKeyDown(GLFW_KEY_S)) {
                                    Maailma3D.liiku(Maailma3D.Liike.TAAKSEPÄIN);
                                    System.out.println("z-sij: " + Maailma3D.zSij);
                                }
                                if (window.getInput().isKeyDown(GLFW_KEY_A)) {
                                    Maailma3D.liiku(Maailma3D.Liike.VASEN);
                                    System.out.println("x-sij: " + Maailma3D.xSij);
                                }
                                if (window.getInput().isKeyDown(GLFW_KEY_D)) {
                                    Maailma3D.liiku(Maailma3D.Liike.OIKEA);
                                    System.out.println("x-sij: " + Maailma3D.xSij);
                                }
                                if (window.getInput().isKeyDown(GLFW_KEY_Q)) {
                                    Maailma3D.liiku(Maailma3D.Liike.ALAS);
                                    System.out.println("y-sij: " + Maailma3D.ySij);
                                }
                                if (window.getInput().isKeyDown(GLFW_KEY_E)) {
                                    Maailma3D.liiku(Maailma3D.Liike.YLÖS);
                                    System.out.println("y-sij: " + Maailma3D.ySij);
                                }
                
                                if (window.getInput().isKeyDown(GLFW_KEY_KP_4)) {
                                    Maailma3D.yaw -= Maailma3D.kääntöNopeus;
                                    Maailma3D.yaw %= 360;
                                    System.out.println("yaw: " + Maailma3D.yaw);
                                }
                                if (window.getInput().isKeyDown(GLFW_KEY_KP_6)) {
                                    Maailma3D.yaw += Maailma3D.kääntöNopeus;
                                    Maailma3D.yaw %= 360;
                                    System.out.println("yaw: " + Maailma3D.yaw);
                                }
                            }
                            case KARTTA -> {
                                if (window.getInput().isKeyPressed(GLFW_KEY_SPACE) || window.getInput().isKeyPressed(GLFW_KEY_ENTER)) {
                                    KarttaIkkuna.suljeToimintoIkkuna();
                                }
                            }
                            case OHJEET -> {
                                if (window.getInput().isKeyPressed(GLFW_KEY_SPACE) || window.getInput().isKeyPressed(GLFW_KEY_ENTER)) {
                                    OhjeIkkuna.suljeToimintoIkkuna();
                                }
                            }
                        }
                    }
                }
            break;
            case TARINARUUTU:
                if (window.getInput().isKeyPressed(GLFW_KEY_SPACE)) {
                    TarinaRuutu.jatka();
                }
            break;
            case VALIKKORUUTU:
                if (window.getInput().isKeyHeld(GLFW_KEY_S) || window.getInput().isKeyHeld(GLFW_KEY_DOWN)) {
                    ValikkoRuutu.painaNäppäintä("alas");
                }
                if (window.getInput().isKeyHeld(GLFW_KEY_W) || window.getInput().isKeyHeld(GLFW_KEY_UP)) {
                    ValikkoRuutu.painaNäppäintä("ylös");
                }
                if (window.getInput().isKeyPressed(GLFW_KEY_SPACE) || window.getInput().isKeyPressed(GLFW_KEY_ENTER)) {
                    ValikkoRuutu.painaNäppäintä("enter");
                }
            break;
            case ASETUSRUUTU:
                if (window.getInput().isKeyPressed(GLFW_KEY_S) || window.getInput().isKeyPressed(GLFW_KEY_DOWN)) {
                    AsetusRuutu.painaNäppäintä("alas");
                }
                if (window.getInput().isKeyPressed(GLFW_KEY_W) || window.getInput().isKeyPressed(GLFW_KEY_UP)) {
                    AsetusRuutu.painaNäppäintä("ylös");
                }
                if (window.getInput().isKeyPressed(GLFW_KEY_SPACE) || window.getInput().isKeyPressed(GLFW_KEY_ENTER)) {
                    AsetusRuutu.painaNäppäintä("enter");
                }
            break;
            case ASETUSRUUTU_GRAFIIKKA:
                if (window.getInput().isKeyPressed(GLFW_KEY_S) || window.getInput().isKeyPressed(GLFW_KEY_DOWN)) {
                    GrafiikkaAsetusRuutu.painaNäppäintä("alas");
                }
                if (window.getInput().isKeyPressed(GLFW_KEY_W) || window.getInput().isKeyPressed(GLFW_KEY_UP)) {
                    GrafiikkaAsetusRuutu.painaNäppäintä("ylös");
                }
                if (window.getInput().isKeyHeld(GLFW_KEY_D) || window.getInput().isKeyHeld(GLFW_KEY_RIGHT)) {
                    GrafiikkaAsetusRuutu.painaNäppäintä("oikea");
                }
                if (window.getInput().isKeyHeld(GLFW_KEY_A) || window.getInput().isKeyHeld(GLFW_KEY_LEFT)) {
                    GrafiikkaAsetusRuutu.painaNäppäintä("vasen");
                }
                if (window.getInput().isKeyPressed(GLFW_KEY_SPACE) || window.getInput().isKeyPressed(GLFW_KEY_ENTER)) {
                    GrafiikkaAsetusRuutu.painaNäppäintä("enter");
                }
            break;
            case ASETUSRUUTU_ÄÄNI:
                if (window.getInput().isKeyPressed(GLFW_KEY_S) || window.getInput().isKeyPressed(GLFW_KEY_DOWN)) {
                    ÄäniAsetusRuutu.painaNäppäintä("alas");
                }
                if (window.getInput().isKeyPressed(GLFW_KEY_W) || window.getInput().isKeyPressed(GLFW_KEY_UP)) {
                    ÄäniAsetusRuutu.painaNäppäintä("ylös");
                }
                if (window.getInput().isKeyHeld(GLFW_KEY_D) || window.getInput().isKeyHeld(GLFW_KEY_RIGHT)) {
                    ÄäniAsetusRuutu.painaNäppäintä("oikea");
                }
                if (window.getInput().isKeyHeld(GLFW_KEY_A) || window.getInput().isKeyHeld(GLFW_KEY_LEFT)) {
                    ÄäniAsetusRuutu.painaNäppäintä("vasen");
                }
                if (window.getInput().isKeyPressed(GLFW_KEY_SPACE) || window.getInput().isKeyPressed(GLFW_KEY_ENTER)) {
                    ÄäniAsetusRuutu.painaNäppäintä("enter");
                }
            break;
            case ASETUSRUUTU_PELI:
                if (window.getInput().isKeyPressed(GLFW_KEY_S) || window.getInput().isKeyPressed(GLFW_KEY_DOWN)) {
                    PeliAsetusRuutu.painaNäppäintä("alas");
                }
                if (window.getInput().isKeyPressed(GLFW_KEY_W) || window.getInput().isKeyPressed(GLFW_KEY_UP)) {
                    PeliAsetusRuutu.painaNäppäintä("ylös");
                }
                if (window.getInput().isKeyHeld(GLFW_KEY_D) || window.getInput().isKeyHeld(GLFW_KEY_RIGHT)) {
                    PeliAsetusRuutu.painaNäppäintä("oikea");
                }
                if (window.getInput().isKeyHeld(GLFW_KEY_A) || window.getInput().isKeyHeld(GLFW_KEY_LEFT)) {
                    PeliAsetusRuutu.painaNäppäintä("vasen");
                }
                if (window.getInput().isKeyPressed(GLFW_KEY_SPACE) || window.getInput().isKeyPressed(GLFW_KEY_ENTER)) {
                    PeliAsetusRuutu.painaNäppäintä("enter");
                }
            break;
            case ASETUSRUUTU_ÄÄNITESTI:
                if (window.getInput().isKeyPressed(GLFW_KEY_S) || window.getInput().isKeyPressed(GLFW_KEY_DOWN)) {
                    ÄäniTestiRuutu.painaNäppäintä("alas");
                }
                if (window.getInput().isKeyPressed(GLFW_KEY_W) || window.getInput().isKeyPressed(GLFW_KEY_UP)) {
                    ÄäniTestiRuutu.painaNäppäintä("ylös");
                }
                if (window.getInput().isKeyHeld(GLFW_KEY_D) || window.getInput().isKeyHeld(GLFW_KEY_RIGHT)) {
                    ÄäniTestiRuutu.painaNäppäintä("oikea");
                }
                if (window.getInput().isKeyHeld(GLFW_KEY_A) || window.getInput().isKeyHeld(GLFW_KEY_LEFT)) {
                    ÄäniTestiRuutu.painaNäppäintä("vasen");
                }
                if (window.getInput().isKeyPressed(GLFW_KEY_SPACE) || window.getInput().isKeyPressed(GLFW_KEY_ENTER)) {
                    ÄäniTestiRuutu.painaNäppäintä("enter");
                }
            break;
            case KEHITTÄJÄRUUTU:
                if (window.getInput().isKeyPressed(GLFW_KEY_SPACE) || window.getInput().isKeyPressed(GLFW_KEY_ENTER)) {
                    KehittäjäRuutu.takaisin();
                }
            break;
            case LOPPURUUTU:
                if (window.getInput().isKeyHeld(GLFW_KEY_S) || window.getInput().isKeyHeld(GLFW_KEY_DOWN)) {
                    LoppuRuutu.painaNäppäintä("alas");
                }
                if (window.getInput().isKeyHeld(GLFW_KEY_W) || window.getInput().isKeyHeld(GLFW_KEY_UP)) {
                    LoppuRuutu.painaNäppäintä("ylös");
                }
                if (window.getInput().isKeyPressed(GLFW_KEY_SPACE) || window.getInput().isKeyPressed(GLFW_KEY_ENTER)) {
                    LoppuRuutu.painaNäppäintä("enter");
                }
            break;
            case MINIPELIRUUTU:
                if (window.getInput().isKeyPressed(GLFW_KEY_ESCAPE) || window.getInput().isKeyPressed(GLFW_KEY_ENTER)) {
                    MinipeliIkkuna.suljeToimintoIkkuna();
                }
                if (window.getInput().isKeyPressed(GLFW_KEY_F5)) {
                    Maailma3D.moonJump = !Maailma3D.moonJump;
                }
                if (window.getInput().isKeyDown(GLFW_KEY_W)) {
                    Maailma3D.liiku(Maailma3D.Liike.ETEENPÄIN);
                }
                if (window.getInput().isKeyDown(GLFW_KEY_S)) {
                    Maailma3D.liiku(Maailma3D.Liike.TAAKSEPÄIN);
                }
                if (window.getInput().isKeyDown(GLFW_KEY_A)) {
                    Maailma3D.liiku(Maailma3D.Liike.VASEN);
                }
                if (window.getInput().isKeyDown(GLFW_KEY_D)) {
                    Maailma3D.liiku(Maailma3D.Liike.OIKEA);
                }
                if (window.getInput().isKeyDown(GLFW_KEY_SPACE)) {
                    Maailma3D.liiku(Maailma3D.Liike.HYPPY);
                }
                if (window.getInput().isKeyDown(GLFW_KEY_KP_4) || window.getInput().isKeyDown(GLFW_KEY_LEFT)) {
                    Maailma3D.yaw -= Maailma3D.kääntöNopeus;
                    if (Maailma3D.yaw < 0) Maailma3D.yaw += 360;
                    Maailma3D.yaw %= 360;
                }
                if (window.getInput().isKeyDown(GLFW_KEY_KP_6) || window.getInput().isKeyDown(GLFW_KEY_RIGHT)) {
                    Maailma3D.yaw += Maailma3D.kääntöNopeus;
                    Maailma3D.yaw %= 360;
                }
                if (window.getInput().isKeyDown(GLFW_KEY_KP_2) || window.getInput().isKeyDown(GLFW_KEY_DOWN)) {
                    Maailma3D.pitch -= Maailma3D.kääntöNopeus;
                    // if (Maailma3D.pitch < -89.999) Maailma3D.pitch = -89.999f;
                    // else Maailma3D.pitch = Math.round(Maailma3D.pitch);
                    if (Maailma3D.pitch < -90) Maailma3D.pitch = -90;
                }
                if (window.getInput().isKeyDown(GLFW_KEY_KP_8) || window.getInput().isKeyDown(GLFW_KEY_UP)) {
                    Maailma3D.pitch += Maailma3D.kääntöNopeus;
                    // if (Maailma3D.pitch > 89.999) Maailma3D.pitch = 89.999f;
                    // else Maailma3D.pitch = Math.round(Maailma3D.pitch);
                    if (Maailma3D.pitch < -90) Maailma3D.pitch = -90;
                }
                if (window.getInput().isKeyDown(GLFW_KEY_KP_7)) {
                    Maailma3D.roll -= Maailma3D.kääntöNopeus;
                    if (Maailma3D.roll < 0) Maailma3D.roll += 360;
                    Maailma3D.roll %= 360;
                }
                if (window.getInput().isKeyDown(GLFW_KEY_KP_9)) {
                    Maailma3D.roll += Maailma3D.kääntöNopeus;
                    Maailma3D.roll %= 360;
                }
                if (window.getInput().isKeyPressed(GLFW_KEY_MINUS) || window.getInput().isKeyPressed(GLFW_KEY_KP_SUBTRACT)) {
                    Maailma3D.vaihdaHuonetta(Maailma3D.annaHuoneenId()-1);
                }
                if (window.getInput().isKeyPressed(59) || window.getInput().isKeyPressed(GLFW_KEY_KP_ADD)) {
                    Maailma3D.vaihdaHuonetta(Maailma3D.annaHuoneenId()+1);
                }
            break;
            case VIRHERUUTU:
                if (window.getInput().isKeyPressed(GLFW_KEY_ESCAPE)) {
                    System.exit(1);
                }
            break;
            case null, default:
            break;
        }
    }

    private static void tarkistaPelaajanTila(Window window) {
        if (Peli.syötteenTila == SyötteenTila.PELI) {
            if (window.getInput().isKeyDown(GLFW_KEY_A) ||
                window.getInput().isKeyDown(GLFW_KEY_D) ||
                window.getInput().isKeyDown(GLFW_KEY_W) ||
                window.getInput().isKeyDown(GLFW_KEY_S) ||
                window.getInput().isKeyDown(GLFW_KEY_LEFT) ||
                window.getInput().isKeyDown(GLFW_KEY_RIGHT) ||
                window.getInput().isKeyDown(GLFW_KEY_UP) ||
                window.getInput().isKeyDown(GLFW_KEY_DOWN)
            ) {
                Pelaaja.keimonState = KeimonState.JUOKSU;
            }
            else Pelaaja.keimonState = KeimonState.IDLE;

            if ((window.getInput().isKeyDown(GLFW_KEY_A) || window.getInput().isKeyDown(GLFW_KEY_LEFT)) && ((window.getInput().isKeyDown(GLFW_KEY_W) || window.getInput().isKeyDown(GLFW_KEY_UP)))) {
                Pelaaja.keimonSuunta = Suunta.YLÄVASEN;
                Pelaaja.keimonSuuntaVasenOikea = SuuntaVasenOikea.VASEN;
            }
            else if ((window.getInput().isKeyDown(GLFW_KEY_A) || window.getInput().isKeyDown(GLFW_KEY_LEFT)) && ((window.getInput().isKeyDown(GLFW_KEY_S) || window.getInput().isKeyDown(GLFW_KEY_DOWN)))) {
                Pelaaja.keimonSuunta = Suunta.ALAVASEN;
                Pelaaja.keimonSuuntaVasenOikea = SuuntaVasenOikea.VASEN;
            }
            else if ((window.getInput().isKeyDown(GLFW_KEY_D) || window.getInput().isKeyDown(GLFW_KEY_RIGHT)) && ((window.getInput().isKeyDown(GLFW_KEY_W) || window.getInput().isKeyDown(GLFW_KEY_UP)))) {
                Pelaaja.keimonSuunta = Suunta.YLÄOIKEA;
                Pelaaja.keimonSuuntaVasenOikea = SuuntaVasenOikea.OIKEA;
            }
            else if ((window.getInput().isKeyDown(GLFW_KEY_D) || window.getInput().isKeyDown(GLFW_KEY_RIGHT)) && ((window.getInput().isKeyDown(GLFW_KEY_S) || window.getInput().isKeyDown(GLFW_KEY_DOWN)))) {
                Pelaaja.keimonSuunta = Suunta.ALAOIKEA;
                Pelaaja.keimonSuuntaVasenOikea = SuuntaVasenOikea.OIKEA;
            }
            else if (window.getInput().isKeyDown(GLFW_KEY_A) || window.getInput().isKeyDown(GLFW_KEY_LEFT)) {
                Pelaaja.keimonSuunta = Suunta.VASEN;
                Pelaaja.keimonSuuntaVasenOikea = SuuntaVasenOikea.VASEN;
            }
            else if (window.getInput().isKeyDown(GLFW_KEY_D) || window.getInput().isKeyDown(GLFW_KEY_RIGHT)) {
                Pelaaja.keimonSuunta = Suunta.OIKEA;
                Pelaaja.keimonSuuntaVasenOikea = SuuntaVasenOikea.OIKEA;
            }
            else if (window.getInput().isKeyDown(GLFW_KEY_W) || window.getInput().isKeyDown(GLFW_KEY_UP)) {
                Pelaaja.keimonSuunta = Suunta.YLÖS;
            }
            else if (window.getInput().isKeyDown(GLFW_KEY_S) || window.getInput().isKeyDown(GLFW_KEY_DOWN)) {
                Pelaaja.keimonSuunta = Suunta.ALAS;
            }
        }
    }

    private static void tarkistaPainetutNäppäimet() {
        if (vasenPainettu) {
            
        }
    }

    private static void tavarapaikanVaihto(int tavarapaikka) {
        Peli.esineValInt = tavarapaikka;
        Peli.valittuEsine = Pelaaja.esineet[Peli.esineValInt];
    }
}
