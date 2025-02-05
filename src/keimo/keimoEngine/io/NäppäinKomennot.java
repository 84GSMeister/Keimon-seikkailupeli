package keimo.keimoEngine.io;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.Pelaaja.*;
import keimo.Peli.SyötteenTila;
import keimo.keimoEngine.toiminnot.Dialogit;
import keimo.Utility.Käännettävä.Suunta;
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
import org.joml.Matrix4f;

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
            KeimoEngine.debugTiedot = !KeimoEngine.debugTiedot;
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
                            Pelaaja.kokeileLiikkumista(Suunta.VASEN);
                            vasenPainettu = true;
                        }
                        else {
                            vasenPainettu = false;
                        }
                        if (window.getInput().isKeyDown(GLFW_KEY_D) || window.getInput().isKeyDown(GLFW_KEY_RIGHT)) {
                            Pelaaja.kokeileLiikkumista(Suunta.OIKEA);
                            oikeaPainettu = true;
                        }
                        else {
                            oikeaPainettu = false;
                        }
                        if (window.getInput().isKeyDown(GLFW_KEY_W) || window.getInput().isKeyDown(GLFW_KEY_UP)) {
                            Pelaaja.kokeileLiikkumista(Suunta.YLÖS);
                            ylösPainettu = true;
                        }
                        else {
                            ylösPainettu = false;
                        }
                        if (window.getInput().isKeyDown(GLFW_KEY_S) || window.getInput().isKeyDown(GLFW_KEY_DOWN)) {
                            Pelaaja.kokeileLiikkumista(Suunta.ALAS);
                            alasPainettu = true;
                        }
                        else {
                            alasPainettu = false;
                        }
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
                
                        if (window.getInput().isKeyDown(GLFW_KEY_KP_4)) {
                            if (Maailma.fade > 0f) {
                                Maailma.fade -= 0.01f;
                            }
                        }
                        if (window.getInput().isKeyDown(GLFW_KEY_KP_6)) {
                            if (Maailma.fade < 1f) {
                                Maailma.fade += 0.01f;
                            }
                        }
                        if (window.getInput().isKeyDown(GLFW_KEY_KP_7)) {
                            rotZ -= 0.05;
                            rotZ %= 2 * Math.PI;
                            camera.setProjection(new Matrix4f().setOrtho2D(-Kamera.zoomX, Kamera.zoomX, -Kamera.zoomY, Kamera.zoomY));
                            camera.setProjection(camera.getProjection().rotateZ(rotZ));
                            camera.setProjection(camera.getProjection().translate(Pelaaja.sijX*128, -Pelaaja.sijY*128, 0));
                            System.out.println(rotZ);
                        }
                        if (window.getInput().isKeyDown(GLFW_KEY_KP_9)) {
                            rotZ += 0.05;
                            rotZ %= 2 * Math.PI;
                            camera.setProjection(new Matrix4f().setOrtho2D(-Kamera.zoomX, Kamera.zoomX, -Kamera.zoomY, Kamera.zoomY).translate(-Pelaaja.sijX, -Pelaaja.sijY, 0));
                            camera.setProjection(camera.getProjection().rotateZ(rotZ));
                            System.out.println(rotZ);
                        }
                        if (window.getInput().isKeyDown(GLFW_KEY_KP_ADD)) {
                            if (Kamera.zoomKerroin > 0.5f) Kamera.zoomKerroin -= 0.05f;
                            System.out.println("zoom:" + Kamera.zoomKerroin);
                            camera.setProjection(new Matrix4f().setOrtho2D(-Kamera.zoomX * Kamera.zoomKerroin, Kamera.zoomX * Kamera.zoomKerroin, -Kamera.zoomY * Kamera.zoomKerroin, Kamera.zoomY * Kamera.zoomKerroin));
                            world.calculateView(window, camera);
                        }
                        if (window.getInput().isKeyDown(GLFW_KEY_KP_SUBTRACT)) {
                            Kamera.zoomKerroin += 0.05f;
                            System.out.println("zoom:" + Kamera.zoomKerroin);
                            camera.setProjection(new Matrix4f().setOrtho2D(-Kamera.zoomX * Kamera.zoomKerroin, Kamera.zoomX * Kamera.zoomKerroin, -Kamera.zoomY * Kamera.zoomKerroin, Kamera.zoomY * Kamera.zoomKerroin));
                            world.calculateView(window, camera);
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
                                if (window.getInput().isKeyPressed(GLFW_KEY_SPACE) || window.getInput().isKeyPressed(GLFW_KEY_ENTER)) {
                                    MinipeliIkkuna.suljeToimintoIkkuna();
                                }
                                else if (window.getInput().isKeyDown(GLFW_KEY_KP_4)) {
                                    Maailma3D.camRotX -= 0.01;
                                }
                                else if (window.getInput().isKeyDown(GLFW_KEY_KP_6)) {
                                    Maailma3D.camRotX += 0.01;
                                }
                                else if (window.getInput().isKeyDown(GLFW_KEY_KP_2)) {
                                    Maailma3D.camRotY -= 0.01;
                                }
                                else if (window.getInput().isKeyDown(GLFW_KEY_KP_8)) {
                                    Maailma3D.camRotY += 0.01;
                                }
                                else if (window.getInput().isKeyDown(GLFW_KEY_KP_7)) {
                                    Maailma3D.camRotZ -= 0.01;
                                }
                                else if (window.getInput().isKeyDown(GLFW_KEY_KP_9)) {
                                    Maailma3D.camRotZ += 0.01;
                                }

                                if (window.getInput().isKeyDown(GLFW_KEY_W)) {
                                    TestiRuutu.liiku(TestiRuutu.Liike.ETEENPÄIN);
                                    System.out.println("z-sij: " + TestiRuutu.zSij);
                                }
                                if (window.getInput().isKeyDown(GLFW_KEY_S)) {
                                    TestiRuutu.liiku(TestiRuutu.Liike.TAAKSEPÄIN);
                                    System.out.println("z-sij: " + TestiRuutu.zSij);
                                }
                                if (window.getInput().isKeyDown(GLFW_KEY_A)) {
                                    TestiRuutu.liiku(TestiRuutu.Liike.VASEN);
                                    System.out.println("x-sij: " + TestiRuutu.xSij);
                                }
                                if (window.getInput().isKeyDown(GLFW_KEY_D)) {
                                    TestiRuutu.liiku(TestiRuutu.Liike.OIKEA);
                                    System.out.println("x-sij: " + TestiRuutu.xSij);
                                }
                                if (window.getInput().isKeyDown(GLFW_KEY_Q)) {
                                    TestiRuutu.liiku(TestiRuutu.Liike.ALAS);
                                    System.out.println("y-sij: " + TestiRuutu.ySij);
                                }
                                if (window.getInput().isKeyDown(GLFW_KEY_E)) {
                                    TestiRuutu.liiku(TestiRuutu.Liike.YLÖS);
                                    System.out.println("y-sij: " + TestiRuutu.ySij);
                                }
                
                                if (window.getInput().isKeyDown(GLFW_KEY_KP_4)) {
                                    TestiRuutu.yaw += TestiRuutu.kääntöNopeus;
                                    TestiRuutu.yaw %= 360;
                                    System.out.println("yaw: " + TestiRuutu.yaw);
                                }
                                if (window.getInput().isKeyDown(GLFW_KEY_KP_6)) {
                                    TestiRuutu.yaw -= TestiRuutu.kääntöNopeus;
                                    TestiRuutu.yaw %= 360;
                                    System.out.println("yaw: " + TestiRuutu.yaw);
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

            if (window.getInput().isKeyDown(GLFW_KEY_A) || window.getInput().isKeyDown(GLFW_KEY_LEFT)) {
                Pelaaja.keimonSuunta = Suunta.VASEN;
                Pelaaja.keimonSuuntaVasenOikea = SuuntaVasenOikea.VASEN;
            }
            if (window.getInput().isKeyDown(GLFW_KEY_D) || window.getInput().isKeyDown(GLFW_KEY_RIGHT)) {
                Pelaaja.keimonSuunta = Suunta.OIKEA;
                Pelaaja.keimonSuuntaVasenOikea = SuuntaVasenOikea.OIKEA;
            }
            if (window.getInput().isKeyDown(GLFW_KEY_W) || window.getInput().isKeyDown(GLFW_KEY_UP)) {
                Pelaaja.keimonSuunta = Suunta.YLÖS;
            }
            if (window.getInput().isKeyDown(GLFW_KEY_S) || window.getInput().isKeyDown(GLFW_KEY_DOWN)) {
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
}
