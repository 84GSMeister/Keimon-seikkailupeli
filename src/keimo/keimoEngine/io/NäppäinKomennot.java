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
import keimo.kenttäkohteet.KenttäKohde;
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
                            //Pelaaja.kokeileLiikkumista(Suunta.VASEN);
                            vasenPainettu = true;
                        }
                        else {
                            vasenPainettu = false;
                        }
                        if (window.getInput().isKeyDown(GLFW_KEY_D) || window.getInput().isKeyDown(GLFW_KEY_RIGHT)) {
                            //Pelaaja.kokeileLiikkumista(Suunta.OIKEA);
                            oikeaPainettu = true;
                        }
                        else {
                            oikeaPainettu = false;
                        }
                        if (window.getInput().isKeyDown(GLFW_KEY_W) || window.getInput().isKeyDown(GLFW_KEY_UP)) {
                            //Pelaaja.kokeileLiikkumista(Suunta.YLÖS);
                            ylösPainettu = true;
                        }
                        else {
                            ylösPainettu = false;
                        }
                        if (window.getInput().isKeyDown(GLFW_KEY_S) || window.getInput().isKeyDown(GLFW_KEY_DOWN)) {
                            //Pelaaja.kokeileLiikkumista(Suunta.ALAS);
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
                            if (Peli.yhdistäminenKäynnissä) {
                                Peli.yhdistäminenKäynnissä = false;
                                Peli.yhdistettäväTavarapaikka = -1;
                            }
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
                            tulostaEsineenKatsomisDialogi();
                        }
                        if (window.getInput().isKeyPressed(GLFW_KEY_C)) {
                            tulostaKentänKatsomisDialogi();
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

                        // if (window.getInput().isKeyDown(GLFW_KEY_KP_7)) {
                        //     rotZ -= 0.05;
                        //     rotZ %= 2 * Math.PI;
                        //     camera.setProjection(new Matrix4f().setOrtho2D(-Kamera.zoomX, Kamera.zoomX, -Kamera.zoomY, Kamera.zoomY));
                        //     camera.setProjection(camera.getProjection().rotateZ(rotZ));
                        //     camera.setProjection(camera.getProjection().translate(Pelaaja.sijX*128, -Pelaaja.sijY*128, 0));
                        //     System.out.println(rotZ);
                        // }
                        // if (window.getInput().isKeyDown(GLFW_KEY_KP_9)) {
                        //     rotZ += 0.05;
                        //     rotZ %= 2 * Math.PI;
                        //     camera.setProjection(new Matrix4f().setOrtho2D(-Kamera.zoomX, Kamera.zoomX, -Kamera.zoomY, Kamera.zoomY).translate(-Pelaaja.sijX, -Pelaaja.sijY, 0));
                        //     camera.setProjection(camera.getProjection().rotateZ(rotZ));
                        //     System.out.println(rotZ);
                        // }
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
                    if (Maailma3D.pitch < -90) Maailma3D.pitch = -90;
                }
                if (window.getInput().isKeyDown(GLFW_KEY_KP_8) || window.getInput().isKeyDown(GLFW_KEY_UP)) {
                    Maailma3D.pitch += Maailma3D.kääntöNopeus;
                    if (Maailma3D.pitch > 90) Maailma3D.pitch = 90;
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

    private static void tulostaEsineenKatsomisDialogi() {
        if (Peli.valittuEsine != null) {
            Esine e = Peli.valittuEsine;
            Dialogit.avaaDialogi(e.annaTekstuuri(), e.katso(), e.annaNimi());
        }
    }
    private static void tulostaKentänKatsomisDialogi() {
        if (Peli.pelikenttä[Pelaaja.sijX][Pelaaja.sijY] != null) {
            KenttäKohde k = Peli.pelikenttä[Pelaaja.sijX][Pelaaja.sijY];
            Dialogit.avaaDialogi(k.annaTekstuuri(), k.katso(), k.annaNimi());
        }
    }

    // private static void tarkistaMidiSyöte(Window window) {
    //     MidiDevice midiLaite;


    //     try {
    //         Sequencer sequencer = window.getInput().getMidiSequencer();
    //         Sequence sequence = new Sequence(Sequence.PPQ, 24, 1);
    //         Track track1 = sequence.createTrack();

    //         int midiNuotti = random.nextInt(36, 84);
    //         int voimakkuus = random.nextInt(50, 100);

    //         ShortMessage noteOn = new ShortMessage();
    //         noteOn.setMessage(ShortMessage.NOTE_ON, 0, midiNuotti, voimakkuus);
    //         MidiEvent noteOnEvent = new MidiEvent(noteOn, 0);
    //         ShortMessage noteOff = new ShortMessage();
    //         noteOff.setMessage(ShortMessage.NOTE_OFF, 0, midiNuotti, 0);
    //         MidiEvent noteOffEvent = new MidiEvent(noteOff, 24);

    //         track1.add(noteOnEvent);
    //         track1.add(noteOffEvent);
    //         sequencer.setSequence(sequence);
    //         sequencer.start();

    //     }
    //     catch (InvalidMidiDataException e) {
    //         e.printStackTrace();
    //     }
    // }
}
