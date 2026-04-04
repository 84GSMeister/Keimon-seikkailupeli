package keimo.seikkailupeli.io;

import keimo.keimoengine.KeimoEngine;
import keimo.keimoengine.ikkuna.*;
import keimo.keimoengine.io.Input;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.PelinAsetukset;
import keimo.seikkailupeli.Peli.SyöteLaitteet;
import keimo.seikkailupeli.Peli.SyötteenTila;
import keimo.seikkailupeli.gui.toimintoIkkunat.*;
import keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat.MinipeliIkkuna3D;
import keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat.MinipeliIkkunaKeimoäly;
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
import keimo.seikkailupeli.toiminnot.Dialogit;

import java.util.List;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;

import static org.lwjgl.glfw.GLFW.*;

public class SyöteYhdistetty extends Input {

    static boolean setCam = true;

    public SyöteYhdistetty(long window) {
        super(window);
        avaaMidiLaitteet();
    }

    @Override
    public void tarkistaSyöte() {
        tarkistaNäppäinJaOhjainKomennot();
        tarkistaHiiriKomennot();
        tarkistaPelaajanTila();
        tarkistaSyöttölaite();
        tarkistaJumittavatAnalogit();
    }
    
    private void tarkistaNäppäinJaOhjainKomennot() {
        if (isKeyPressed(GLFW_KEY_F1)) {
            Peli.vaatiiUudelleenkäynnistyksen = true;
        }
        else if (isKeyDown(GLFW_KEY_LEFT_ALT)) {
            if (isKeyPressed(GLFW_KEY_F4)) {
                glfwSetWindowShouldClose(KeimoEngine.window.getWindow(), true);
            }
            else if (isKeyPressed(GLFW_KEY_ENTER)) {
                KeimoEngine.window.setFullscreen(!KeimoEngine.window.isFullscreen(), false);
            }
        }
        else if (isKeyPressed(GLFW_KEY_F11)) {
            KeimoEngine.window.setFullscreen(!KeimoEngine.window.isFullscreen(), false);
        }
        
        switch (Peli.aktiivinenRuutu) {
            case PELIRUUTU -> {
                if (isKeyPressed(GLFW_KEY_F2)) {
                    KeimoEngine.kaatoTeksti = null;
                }
                else if (isKeyPressed(GLFW_KEY_F3)) {
                    PelinAsetukset.debugTiedot = !PelinAsetukset.debugTiedot;
                }
                switch (Peli.syötteenTila) {
                    case PELI -> {
                        if (setCam) {
                            Kamera.zoomX = KeimoEngine.window.getWidth()/2;
                            Kamera.zoomY = KeimoEngine.window.getHeight()/2;
                            setCam = false;
                        }
                        
                        if (isKeyPressed(GLFW_KEY_ESCAPE) || isJoystickButtonPressed(NÄPPÄIN_START)) {
                            Peli.pausetaPeli(true);
                            DialogiValintaIkkuna.avaaToimintoIkkuna("pause");
                        }
                        else if (isKeyPressed(GLFW_KEY_F5)) {
                            HuijauskoodiValikko.avaaValikko();
                        }

                        if (isKeyPressed(GLFW_KEY_A) || isKeyPressed(GLFW_KEY_LEFT) || isJoystickAnalogPressed(ANALOG_L_VASEN)) {
                            Maailma.liiku(Maailma.Liike.VASEN);
                        }
                        if (isKeyReleased(GLFW_KEY_A) || isKeyReleased(GLFW_KEY_LEFT) || isJoystickAnalogReleased(ANALOG_L_VASEN)) {
                            Maailma.lopetaLiike(Maailma.Liike.VASEN);
                        }
                        if (isKeyPressed(GLFW_KEY_D) || isKeyPressed(GLFW_KEY_RIGHT) || isJoystickAnalogPressed(ANALOG_L_OIKEA)) {
                            Maailma.liiku(Maailma.Liike.OIKEA);
                        }
                        if (isKeyReleased(GLFW_KEY_D) || isKeyReleased(GLFW_KEY_RIGHT) || isJoystickAnalogReleased(ANALOG_L_OIKEA)) {
                            Maailma.lopetaLiike(Maailma.Liike.OIKEA);
                        }
                        if (isKeyPressed(GLFW_KEY_W) || isKeyPressed(GLFW_KEY_UP) || isJoystickAnalogPressed(ANALOG_L_YLÖS)) {
                            Maailma.liiku(Maailma.Liike.YLÖS);
                        }
                        if (isKeyReleased(GLFW_KEY_W) || isKeyReleased(GLFW_KEY_UP) || isJoystickAnalogReleased(ANALOG_L_YLÖS)) {
                            Maailma.lopetaLiike(Maailma.Liike.YLÖS);
                        }
                        if (isKeyPressed(GLFW_KEY_S) || isKeyPressed(GLFW_KEY_DOWN) || isJoystickAnalogPressed(ANALOG_L_ALAS)) {
                            Maailma.liiku(Maailma.Liike.ALAS);
                        }
                        if (isKeyReleased(GLFW_KEY_S) || isKeyReleased(GLFW_KEY_DOWN) || isJoystickAnalogReleased(ANALOG_L_ALAS)) {
                            Maailma.lopetaLiike(Maailma.Liike.ALAS);
                        }

                        if (isKeyPressed(GLFW_KEY_E) || isJoystickButtonPressed(NÄPPÄIN_A)) {
                            Peli.painaE(Pelaaja.sijX, Pelaaja.sijY);
                        }
                        if (isKeyPressed(GLFW_KEY_Q) || isJoystickAnalogPressed(TRIGGERI_OIKEA)) {
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
                        if (isJoystickHatPressed(DPAD_YLÖS)) {
                            tavarapaikanVaihtoOhjain(-3);
                        }
                        else if (isJoystickHatPressed(DPAD_ALAS)) {
                            tavarapaikanVaihtoOhjain(3);
                        }
                        else if (isJoystickHatPressed(DPAD_VASEN)) {
                            tavarapaikanVaihtoOhjain(-1);
                        }
                        else if (isJoystickHatPressed(DPAD_OIKEA)) {
                            tavarapaikanVaihtoOhjain(1);
                        }

                        if (isKeyDown(GLFW_KEY_SPACE) || isJoystickButtonDown(NÄPPÄIN_B)) {
                            Peli.käyttö(Peli.esineValInt);
                        }
                        if (isKeyPressed(GLFW_KEY_X) || isJoystickButtonPressed(NÄPPÄIN_R)) {
                            Peli.katsoEsinettä();
                        }
                        if (isKeyPressed(GLFW_KEY_C) || isJoystickButtonPressed(NÄPPÄIN_X)) {
                            Peli.katsoKenttää();
                        }
                        if (isKeyPressed(GLFW_KEY_Z) || isJoystickButtonPressed(NÄPPÄIN_SELECT)) {
                            Peli.painaZ();
                        }
                    }
                    case DIALOGI -> {
                        if (isKeyPressed(GLFW_KEY_E) || isKeyPressed(GLFW_KEY_X) || isKeyPressed(GLFW_KEY_C) || isJoystickButtonPressed(NÄPPÄIN_A) || isJoystickButtonPressed(NÄPPÄIN_X) || isJoystickButtonPressed(NÄPPÄIN_R)) {
                            Dialogit.kelaaDialogi();
                        }
                        Maailma.lopetaLiike(null);
                        Pelaaja.keimonState = KeimonState.IDLE;
                    }
                    case TOIMINTO -> {
                        switch (Peli.toimintoIkkuna) {
                            case PULLONPALAUTUS -> {
                                if (PullonPalautusIkkuna.virheenTyyppi == PullonPalautusIkkuna.VirheenTyyppi.PAKKAUS) {
                                    if (isKeyPressed(GLFW_KEY_SPACE) || isJoystickButtonPressed(NÄPPÄIN_A)) {
                                        PullonPalautusIkkuna.jatkoSyöteAnnettu = true;
                                    }
                                }
                                else if (PullonPalautusIkkuna.virheenTyyppi == PullonPalautusIkkuna.VirheenTyyppi.MUOTO) {
                                    if (isKeyPressed(GLFW_KEY_X) || isJoystickButtonPressed(NÄPPÄIN_Y)) {
                                        PullonPalautusIkkuna.jatkoSyöteAnnettu = true;
                                    }
                                }
                                else if (PullonPalautusIkkuna.virheenTyyppi == PullonPalautusIkkuna.VirheenTyyppi.KÄSI) {
                                    if (isKeyPressed(GLFW_KEY_C) || isJoystickButtonPressed(NÄPPÄIN_X)) {
                                        PullonPalautusIkkuna.jatkoSyöteAnnettu = true;
                                    }
                                }
                                else if (PullonPalautusIkkuna.virheenTyyppi == PullonPalautusIkkuna.VirheenTyyppi.MERKKI) {
                                    if (isKeyPressed(GLFW_KEY_Z) || isJoystickButtonPressed(NÄPPÄIN_B)) {
                                        PullonPalautusIkkuna.jatkoSyöteAnnettu = true;
                                    }
                                }
                                else if (isKeyPressed(GLFW_KEY_ESCAPE) || isJoystickButtonPressed(NÄPPÄIN_START)) {
                                    Peli.syötteenTila = SyötteenTila.PELI;
                                }
                            }
                            case VALINTADIALOGI -> {
                                if (isKeyPressed(GLFW_KEY_SPACE) || isKeyPressed(GLFW_KEY_ENTER) || isJoystickButtonPressed(NÄPPÄIN_A) || isJoystickButtonPressed(NÄPPÄIN_START)) {
                                    DialogiValintaIkkuna.hyväksyValinta();
                                }
                                else if (isKeyPressed(GLFW_KEY_ESCAPE) || isJoystickButtonPressed(NÄPPÄIN_B)) {
                                    DialogiValintaIkkuna.peruValinta();
                                }
                                else if (isKeyHeld(GLFW_KEY_W) || isKeyPressed(GLFW_KEY_UP) || isJoystickAnalogHeld(ANALOG_L_YLÖS) || isJoystickHatPressed(DPAD_YLÖS)) {
                                    DialogiValintaIkkuna.pienennäValintaa();
                                }
                                else if (isKeyHeld(GLFW_KEY_S) || isKeyPressed(GLFW_KEY_DOWN) || isJoystickAnalogHeld(ANALOG_L_ALAS) || isJoystickHatPressed(DPAD_ALAS)) {
                                    DialogiValintaIkkuna.kasvataValintaa();
                                }
                            }
                            case ÄMPÄRIJONO -> {
                                if (isKeyPressed(GLFW_KEY_SPACE) || isKeyPressed(GLFW_KEY_ENTER) || isJoystickButtonPressed(NÄPPÄIN_A) || isJoystickButtonPressed(NÄPPÄIN_START)) {
                                    ÄmpäriJonoIkkuna.keskeytetty = true;
                                }
                            }
                            case MINIPELI_3D -> {
                                if (isKeyPressed(GLFW_KEY_ESCAPE) || isKeyPressed(GLFW_KEY_ENTER) || isJoystickButtonPressed(NÄPPÄIN_START)) {
                                    MinipeliIkkuna3D.suljeToimintoIkkuna();
                                }
                                if (isKeyPressed(GLFW_KEY_F3)) {
                                    Maailma3D.debugTiedotNäkyvissä = !Maailma3D.debugTiedotNäkyvissä;
                                }
                                if (isKeyPressed(GLFW_KEY_F5)) {
                                    Maailma3D.moonJump = !Maailma3D.moonJump;
                                }
                                if (isKeyPressed(GLFW_KEY_W) || isJoystickAnalogPressed(ANALOG_L_YLÖS)) {
                                    Maailma3D.liiku(Maailma3D.Liike.ETEENPÄIN);
                                }
                                if (isKeyPressed(GLFW_KEY_S) || isJoystickAnalogPressed(ANALOG_L_ALAS)) {
                                    Maailma3D.liiku(Maailma3D.Liike.TAAKSEPÄIN);
                                }
                                if (isKeyPressed(GLFW_KEY_A) || isJoystickAnalogPressed(ANALOG_L_VASEN)) {
                                    Maailma3D.liiku(Maailma3D.Liike.VASEN);
                                }
                                if (isKeyPressed(GLFW_KEY_D) || isJoystickAnalogPressed(ANALOG_L_OIKEA)) {
                                    Maailma3D.liiku(Maailma3D.Liike.OIKEA);
                                }
                                if (isKeyReleased(GLFW_KEY_W) || isJoystickAnalogReleased(ANALOG_L_YLÖS)) {
                                    Maailma3D.lopetaLiike(Maailma3D.Liike.ETEENPÄIN);
                                }
                                if (isKeyReleased(GLFW_KEY_S) || isJoystickAnalogReleased(ANALOG_L_ALAS)) {
                                    Maailma3D.lopetaLiike(Maailma3D.Liike.TAAKSEPÄIN);
                                }
                                if (isKeyReleased(GLFW_KEY_A) || isJoystickAnalogReleased(ANALOG_L_VASEN)) {
                                    Maailma3D.lopetaLiike(Maailma3D.Liike.VASEN);
                                }
                                if (isKeyReleased(GLFW_KEY_D) || isJoystickAnalogReleased(ANALOG_L_OIKEA)) {
                                    Maailma3D.lopetaLiike(Maailma3D.Liike.OIKEA);
                                }
                                if (isKeyDown(GLFW_KEY_SPACE) || isJoystickButtonDown(NÄPPÄIN_A)) {
                                    Maailma3D.liiku(Maailma3D.Liike.HYPPY);
                                }
                                if (isKeyDown(GLFW_KEY_KP_4) || isKeyDown(GLFW_KEY_LEFT) || isJoystickAnalogDown(ANALOG_R_VASEN)) {
                                    Maailma3D.käännä(Maailma3D.KameranLiike.VASEN);
                                }
                                if (isKeyDown(GLFW_KEY_KP_6) || isKeyDown(GLFW_KEY_RIGHT) || isJoystickAnalogDown(ANALOG_R_OIKEA)) {
                                    Maailma3D.käännä(Maailma3D.KameranLiike.OIKEA);
                                }
                                if (isKeyDown(GLFW_KEY_KP_2) || isKeyDown(GLFW_KEY_DOWN) || isJoystickAnalogDown(ANALOG_R_ALAS)) {
                                    Maailma3D.käännä(Maailma3D.KameranLiike.ALAS);
                                }
                                if (isKeyDown(GLFW_KEY_KP_8) || isKeyDown(GLFW_KEY_UP) || isJoystickAnalogDown(ANALOG_R_YLÖS)) {
                                    Maailma3D.käännä(Maailma3D.KameranLiike.YLÖS);
                                }
                                if (isKeyDown(GLFW_KEY_KP_7) || isJoystickAnalogDown(TRIGGERI_VASEN)) {
                                    Maailma3D.käännä(Maailma3D.KameranLiike.PYÖRITÄ_VASEN);
                                }
                                if (isKeyDown(GLFW_KEY_KP_9) || isJoystickAnalogDown(TRIGGERI_OIKEA)) {
                                    Maailma3D.käännä(Maailma3D.KameranLiike.PYÖRITÄ_OIKEA);
                                }
                                if (isKeyPressed(GLFW_KEY_MINUS) || isKeyPressed(GLFW_KEY_KP_SUBTRACT)) {
                                    Maailma3D.vaihdaHuonetta(Maailma3D.annaHuoneenId()-1);
                                }
                                if (isKeyPressed(59) || isKeyPressed(GLFW_KEY_KP_ADD)) {
                                    Maailma3D.vaihdaHuonetta(Maailma3D.annaHuoneenId()+1);
                                }
                            }
                            case MINIPELI_PONG -> {
                                if (isKeyPressed(GLFW_KEY_ESCAPE) || isKeyPressed(GLFW_KEY_ENTER) || isJoystickButtonPressed(NÄPPÄIN_START)) {
                                    MinipeliIkkunaPong.suljeToimintoIkkuna();
                                }

                                if (isKeyPressed(GLFW_KEY_SPACE) || isJoystickButtonPressed(NÄPPÄIN_A)) {
                                    MinipeliIkkunaPong.ohitaValikko();
                                }
                                if (isKeyDown(GLFW_KEY_W) || isKeyDown(GLFW_KEY_UP) || isJoystickAnalogDown(ANALOG_L_YLÖS) || isJoystickHatPressed(DPAD_YLÖS)) {
                                    MinipeliIkkunaPong.liikutaYlös();
                                }
                                if (isKeyDown(GLFW_KEY_S) || isKeyDown(GLFW_KEY_DOWN) || isJoystickAnalogDown(ANALOG_L_ALAS) || isJoystickHatPressed(DPAD_ALAS)) {
                                    MinipeliIkkunaPong.liikutaAlas();
                                }
                            }
                            case MINIPELI_POKERI -> {
                                if (isKeyPressed(GLFW_KEY_ESCAPE) || isKeyPressed(GLFW_KEY_ENTER) || isJoystickButtonPressed(NÄPPÄIN_START)) {
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
                                    MinipeliIkkunaPokeri.ohitaValikko();
                                }
                            }
                            case MINIPELI_TETRIS -> {
                                if (isKeyPressed(GLFW_KEY_ESCAPE) || isKeyPressed(GLFW_KEY_ENTER) || isJoystickButtonPressed(NÄPPÄIN_START)) {
                                    MinipeliIkkunaTetris.suljeToimintoIkkuna();
                                }

                                if (isKeyPressed(GLFW_KEY_SPACE) || isJoystickButtonPressed(NÄPPÄIN_A)) {
                                    MinipeliIkkunaTetris.ohitaValikko();
                                }
                                if (isKeyHeld(GLFW_KEY_A) || isKeyHeld(GLFW_KEY_LEFT) || isJoystickAnalogHeld(ANALOG_L_VASEN) || isJoystickHatPressed(DPAD_VASEN)) {
                                    MinipeliIkkunaTetris.siirräPalikkaa(false);
                                }
                                if (isKeyHeld(GLFW_KEY_D) || isKeyHeld(GLFW_KEY_RIGHT) || isJoystickAnalogHeld(ANALOG_L_OIKEA) || isJoystickHatPressed(DPAD_OIKEA)) {
                                    MinipeliIkkunaTetris.siirräPalikkaa(true);
                                }
                                if (isKeyPressed(GLFW_KEY_SPACE) | isKeyPressed(GLFW_KEY_W) || isKeyPressed(GLFW_KEY_UP) || isJoystickButtonPressed(NÄPPÄIN_A)) {
                                    MinipeliIkkunaTetris.käännäPalikkaa();
                                }
                                if (isKeyPressed(GLFW_KEY_S) || isKeyPressed(GLFW_KEY_DOWN) || isJoystickAnalogPressed(ANALOG_L_ALAS) || isJoystickHatPressed(DPAD_ALAS)) {
                                    MinipeliIkkunaTetris.pudotaPalikka();
                                }
                            }
                            case MINIPELI_4 -> {
                                if (isKeyPressed(GLFW_KEY_SPACE) || isKeyPressed(GLFW_KEY_ENTER) || isKeyPressed(GLFW_KEY_ESCAPE) || isJoystickButtonPressed(NÄPPÄIN_START)) {
                                    MinipeliIkkunaOverflow.suljeToimintoIkkuna();
                                }
                            }
                            case MINIPELI_KEIMOÄLY -> {
                                if (isKeyPressed(GLFW_KEY_ESCAPE) || isJoystickButtonPressed(NÄPPÄIN_START)) {
                                    MinipeliIkkunaKeimoäly.suljeToimintoIkkuna();
                                }
                                if (isKeyPressed(GLFW_KEY_ENTER) || isJoystickButtonPressed(NÄPPÄIN_A)) {
                                    MinipeliIkkunaKeimoäly.lähetäViesti();
                                }
                                if (isKeyHeld(GLFW_KEY_BACKSPACE)) {
                                    MinipeliIkkunaKeimoäly.pyyhiKirjain();
                                }
                                if (isKeyPressed(GLFW_KEY_SPACE)) {
                                    MinipeliIkkunaKeimoäly.lisääKirjainSyötteeseen(" ");
                                }
                                else if (isKeyPressed(GLFW_KEY_Q)) {
                                    MinipeliIkkunaKeimoäly.lisääKirjainSyötteeseen("Q");
                                }
                                else if (isKeyPressed(GLFW_KEY_W)) {
                                    MinipeliIkkunaKeimoäly.lisääKirjainSyötteeseen("W");
                                }
                                else if (isKeyPressed(GLFW_KEY_E)) {
                                    MinipeliIkkunaKeimoäly.lisääKirjainSyötteeseen("E");
                                }
                                else if (isKeyPressed(GLFW_KEY_R)) {
                                    MinipeliIkkunaKeimoäly.lisääKirjainSyötteeseen("R");
                                }
                                else if (isKeyPressed(GLFW_KEY_T)) {
                                    MinipeliIkkunaKeimoäly.lisääKirjainSyötteeseen("T");
                                }
                                else if (isKeyPressed(GLFW_KEY_Y)) {
                                    MinipeliIkkunaKeimoäly.lisääKirjainSyötteeseen("Y");
                                }
                                else if (isKeyPressed(GLFW_KEY_U)) {
                                    MinipeliIkkunaKeimoäly.lisääKirjainSyötteeseen("U");
                                }
                                else if (isKeyPressed(GLFW_KEY_I)) {
                                    MinipeliIkkunaKeimoäly.lisääKirjainSyötteeseen("I");
                                }
                                else if (isKeyPressed(GLFW_KEY_O)) {
                                    MinipeliIkkunaKeimoäly.lisääKirjainSyötteeseen("O");
                                }
                                else if (isKeyPressed(GLFW_KEY_P)) {
                                    MinipeliIkkunaKeimoäly.lisääKirjainSyötteeseen("P");
                                }
                                else if (isKeyPressed(GLFW_KEY_LEFT_BRACKET)) {
                                    MinipeliIkkunaKeimoäly.lisääKirjainSyötteeseen("Å");
                                }
                                else if (isKeyPressed(GLFW_KEY_A)) {
                                    MinipeliIkkunaKeimoäly.lisääKirjainSyötteeseen("A");
                                }
                                else if (isKeyPressed(GLFW_KEY_S)) {
                                    MinipeliIkkunaKeimoäly.lisääKirjainSyötteeseen("S");
                                }
                                else if (isKeyPressed(GLFW_KEY_D)) {
                                    MinipeliIkkunaKeimoäly.lisääKirjainSyötteeseen("D");
                                }
                                else if (isKeyPressed(GLFW_KEY_F)) {
                                    MinipeliIkkunaKeimoäly.lisääKirjainSyötteeseen("F");
                                }
                                else if (isKeyPressed(GLFW_KEY_G)) {
                                    MinipeliIkkunaKeimoäly.lisääKirjainSyötteeseen("G");
                                }
                                else if (isKeyPressed(GLFW_KEY_H)) {
                                    MinipeliIkkunaKeimoäly.lisääKirjainSyötteeseen("H");
                                }
                                else if (isKeyPressed(GLFW_KEY_J)) {
                                    MinipeliIkkunaKeimoäly.lisääKirjainSyötteeseen("J");
                                }
                                else if (isKeyPressed(GLFW_KEY_K)) {
                                    MinipeliIkkunaKeimoäly.lisääKirjainSyötteeseen("K");
                                }
                                else if (isKeyPressed(GLFW_KEY_L)) {
                                    MinipeliIkkunaKeimoäly.lisääKirjainSyötteeseen("L");
                                }
                                else if (isKeyPressed(GLFW_KEY_SEMICOLON)) {
                                    MinipeliIkkunaKeimoäly.lisääKirjainSyötteeseen("Ö");
                                }
                                else if (isKeyPressed(GLFW_KEY_APOSTROPHE)) {
                                    MinipeliIkkunaKeimoäly.lisääKirjainSyötteeseen("Ä");
                                }
                                else if (isKeyPressed(GLFW_KEY_Z)) {
                                    MinipeliIkkunaKeimoäly.lisääKirjainSyötteeseen("Z");
                                }
                                else if (isKeyPressed(GLFW_KEY_X)) {
                                    MinipeliIkkunaKeimoäly.lisääKirjainSyötteeseen("X");
                                }
                                else if (isKeyPressed(GLFW_KEY_C)) {
                                    MinipeliIkkunaKeimoäly.lisääKirjainSyötteeseen("C");
                                }
                                else if (isKeyPressed(GLFW_KEY_V)) {
                                    MinipeliIkkunaKeimoäly.lisääKirjainSyötteeseen("V");
                                }
                                else if (isKeyPressed(GLFW_KEY_B)) {
                                    MinipeliIkkunaKeimoäly.lisääKirjainSyötteeseen("B");
                                }
                                else if (isKeyPressed(GLFW_KEY_N)) {
                                    MinipeliIkkunaKeimoäly.lisääKirjainSyötteeseen("N");
                                }
                                else if (isKeyPressed(GLFW_KEY_M)) {
                                    MinipeliIkkunaKeimoäly.lisääKirjainSyötteeseen("M");
                                }
                                else if (isKeyPressed(GLFW_KEY_COMMA)) {
                                    MinipeliIkkunaKeimoäly.lisääKirjainSyötteeseen(",");
                                }
                                else if (isKeyPressed(GLFW_KEY_PERIOD)) {
                                    MinipeliIkkunaKeimoäly.lisääKirjainSyötteeseen(".");
                                }
                            }
                            case KARTTA -> {
                                if (isKeyPressed(GLFW_KEY_SPACE) || isKeyPressed(GLFW_KEY_ENTER) || isKeyPressed(GLFW_KEY_ESCAPE) || isJoystickButtonPressed(NÄPPÄIN_A) || isJoystickButtonPressed(NÄPPÄIN_START)) {
                                    KarttaIkkuna.suljeToimintoIkkuna();
                                }
                            }
                            case OHJEET -> {
                                if (isKeyPressed(GLFW_KEY_SPACE) || isKeyPressed(GLFW_KEY_ENTER) || isKeyPressed(GLFW_KEY_ESCAPE) || isJoystickButtonPressed(NÄPPÄIN_A) || isJoystickButtonPressed(NÄPPÄIN_START)) {
                                    OhjeIkkuna.suljeToimintoIkkuna();
                                }
                            }
                            case HUIJAUSKOODIT -> {
                                if (isKeyPressed(GLFW_KEY_ESCAPE) || isKeyPressed(GLFW_KEY_ENTER) || isKeyHeld(GLFW_KEY_SPACE) || isKeyPressed(GLFW_KEY_F5) || isJoystickButtonPressed(NÄPPÄIN_START)) {
                                    HuijauskoodiValikko.suljeValikko();
                                }
                                if (isKeyHeld(GLFW_KEY_A) || isKeyPressed(GLFW_KEY_LEFT) || isKeyHeld(GLFW_KEY_D) || isKeyPressed(GLFW_KEY_RIGHT) || isJoystickAnalogHeld(ANALOG_L_VASEN) || isJoystickHatPressed(DPAD_VASEN) || isJoystickAnalogHeld(ANALOG_L_OIKEA) || isJoystickHatPressed(DPAD_OIKEA)) {
                                    HuijauskoodiValikko.muutaValintaa();
                                }
                                if (isKeyHeld(GLFW_KEY_W) || isKeyPressed(GLFW_KEY_UP) || isJoystickAnalogHeld(ANALOG_L_YLÖS) || isJoystickHatPressed(DPAD_YLÖS)) {
                                    HuijauskoodiValikko.pienennäValintaa();
                                }
                                if (isKeyHeld(GLFW_KEY_S) || isKeyPressed(GLFW_KEY_DOWN) || isJoystickAnalogHeld(ANALOG_L_ALAS) || isJoystickHatPressed(DPAD_ALAS)) {
                                    HuijauskoodiValikko.kasvataValintaa();
                                }
                            }
                        }
                    }
                }
            }
            case TARINARUUTU -> {
                if (isKeyPressed(GLFW_KEY_SPACE) || isKeyPressed(GLFW_KEY_ENTER) || isJoystickButtonPressed(NÄPPÄIN_A) || isJoystickButtonPressed(NÄPPÄIN_START)) {
                    TarinaRuutu.jatka();
                }
            }
            case VALIKKORUUTU -> {
                if (isKeyHeld(GLFW_KEY_W) || isKeyPressed(GLFW_KEY_UP) || isJoystickAnalogHeld(ANALOG_L_YLÖS) || isJoystickHatPressed(DPAD_YLÖS)) {
                    ValikkoRuutu.painaNäppäintä("ylös");
                }    
                if (isKeyHeld(GLFW_KEY_S) || isKeyPressed(GLFW_KEY_DOWN) || isJoystickAnalogHeld(ANALOG_L_ALAS) || isJoystickHatPressed(DPAD_ALAS)) {
                    ValikkoRuutu.painaNäppäintä("alas");
                }
                if (isKeyPressed(GLFW_KEY_SPACE) || isKeyPressed(GLFW_KEY_ENTER) || isJoystickButtonPressed(NÄPPÄIN_A) || isJoystickButtonPressed(NÄPPÄIN_START)) {
                    ValikkoRuutu.painaNäppäintä("enter");
                }
            }
            case ASETUSRUUTU -> {
                if (isKeyHeld(GLFW_KEY_W) || isKeyPressed(GLFW_KEY_UP) || isJoystickAnalogHeld(ANALOG_L_YLÖS) || isJoystickHatPressed(DPAD_YLÖS)) {
                    AsetusRuutu.painaNäppäintä("ylös");
                }
                if (isKeyHeld(GLFW_KEY_S) || isKeyPressed(GLFW_KEY_DOWN) || isJoystickAnalogHeld(ANALOG_L_ALAS) || isJoystickHatPressed(DPAD_ALAS)) {
                    AsetusRuutu.painaNäppäintä("alas");
                }
                if (isKeyHeld(GLFW_KEY_A) || isKeyPressed(GLFW_KEY_LEFT) || isJoystickAnalogHeld(ANALOG_L_VASEN) || isJoystickHatPressed(DPAD_VASEN)) {
                    AsetusRuutu.painaNäppäintä("vasen");
                }
                if (isKeyHeld(GLFW_KEY_D) || isKeyPressed(GLFW_KEY_RIGHT) || isJoystickAnalogHeld(ANALOG_L_OIKEA) || isJoystickHatPressed(DPAD_OIKEA)) {
                    AsetusRuutu.painaNäppäintä("oikea");
                }
                if (isKeyPressed(GLFW_KEY_SPACE) || isKeyPressed(GLFW_KEY_ENTER) || isJoystickButtonPressed(NÄPPÄIN_A) || isJoystickButtonPressed(NÄPPÄIN_START)) {
                    AsetusRuutu.painaNäppäintä("enter");
                }
                if (isKeyPressed(GLFW_KEY_ESCAPE) || isJoystickButtonPressed(NÄPPÄIN_B)) {
                    AsetusRuutu.painaNäppäintä("esc");
                }
            }
            case KEHITTÄJÄRUUTU -> {
                if (isKeyPressed(GLFW_KEY_SPACE) || isKeyPressed(GLFW_KEY_ENTER) || isKeyPressed(GLFW_KEY_ESCAPE) || isJoystickButtonPressed(NÄPPÄIN_A) || isJoystickButtonPressed(NÄPPÄIN_START)) {
                    KehittäjäRuutu.takaisin();
                }
            }
            case LOPPURUUTU -> {
                if (isKeyHeld(GLFW_KEY_S) || isKeyPressed(GLFW_KEY_DOWN) || isJoystickAnalogHeld(ANALOG_L_ALAS) || isJoystickHatPressed(DPAD_ALAS)) {
                    LoppuRuutu.painaNäppäintä("alas");
                }
                if (isKeyHeld(GLFW_KEY_W) || isKeyPressed(GLFW_KEY_UP) || isJoystickAnalogHeld(ANALOG_L_YLÖS) || isJoystickHatPressed(DPAD_YLÖS)) {
                    LoppuRuutu.painaNäppäintä("ylös");
                }
                if (isKeyPressed(GLFW_KEY_SPACE) || isKeyPressed(GLFW_KEY_ENTER)) {
                    LoppuRuutu.painaNäppäintä("enter");
                }
            }
            case EDITORIRUUTU -> {
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
                        if (isKeyHeld(GLFW_KEY_W) || isKeyPressed(GLFW_KEY_UP)) {
                            EditorinValikko.pienennäValintaa();
                        }
                        if (isKeyHeld(GLFW_KEY_S) || isKeyPressed(GLFW_KEY_DOWN)) {
                            EditorinValikko.kasvataValintaa();
                        }
                        if (isKeyPressed(GLFW_KEY_ENTER) || isKeyPressed(GLFW_KEY_SPACE)) {
                            EditorinValikko.hyväksyValinta();
                        }
                    }
                    default -> {}
                }
                
            }
            case EDITORIRUUTU_VARMISTUS -> {
                if (isKeyPressed(GLFW_KEY_A) || isKeyPressed(GLFW_KEY_LEFT) || isJoystickAnalogHeld(ANALOG_L_VASEN) || isJoystickHatPressed(DPAD_VASEN)) {
                    EditoriRuutuVarmistus.painaNäppäintä("ylös");
                }
                if (isKeyPressed(GLFW_KEY_D) || isKeyPressed(GLFW_KEY_RIGHT) || isJoystickAnalogHeld(ANALOG_L_OIKEA) || isJoystickHatPressed(DPAD_OIKEA)) {
                    EditoriRuutuVarmistus.painaNäppäintä("alas");
                }
                if (isKeyPressed(GLFW_KEY_SPACE) || isKeyPressed(GLFW_KEY_ENTER) || isJoystickButtonPressed(NÄPPÄIN_A) || isJoystickButtonPressed(NÄPPÄIN_START)) {
                    EditoriRuutuVarmistus.painaNäppäintä("enter");
                }
            }
            case VIRHERUUTU -> {
                if (isKeyPressed(GLFW_KEY_ESCAPE)) {
                    System.exit(1);
                }
                else if (isJoystickButtonPressed(NÄPPÄIN_START)) {
                    Peli.vaatiiUudelleenkäynnistyksen = true;
                }
            }
            case null, default -> {}
        }
    }

    private void tarkistaHiiriKomennot() {
        switch (Peli.aktiivinenRuutu) {
            case EDITORIRUUTU -> {
                int hiiriX = (int)getCursorPosX();
                int hiiriY = (int)getCursorPosY();
                int scrollX = (int)getScrollX(); // En tiedä mihin ikinä tarvii vaakasuuntaista rullausta mut pidetään mukana
                int scrollY = (int)getScrollY();
                EditoriRuutu.tarkistaHoverNapit(hiiriX, hiiriY);
                if (isMouseButtonPressed(GLFW_MOUSE_BUTTON_LEFT)) {
                    EditoriRuutu.hiirenVasenToiminto(hiiriX, hiiriY);
                }
                if (isMouseButtonPressed(GLFW_MOUSE_BUTTON_RIGHT)) {
                    EditoriRuutu.hiirenOikeaToiminto(hiiriX, hiiriY);
                }
                if (isMouseButtonPressed(GLFW_MOUSE_BUTTON_MIDDLE)) {
                    EditoriRuutu.hiirenKeskiToiminto(hiiriX, hiiriY);
                }
                if (isMouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                    EditoriRuutu.hiirenVasenToimintoHold(hiiriX, hiiriY);
                }
                if (isMouseButtonDown(GLFW_MOUSE_BUTTON_RIGHT)) {
                    EditoriRuutu.hiirenOikeaToimintoHold(hiiriX, hiiriY);
                }
                if (isMouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE)) {
                    EditoriRuutu.hiirenKeskiToimintoHold(hiiriX, hiiriY);
                }
                if (isMouseButtonReleased(GLFW_MOUSE_BUTTON_LEFT)) {
                    EditoriRuutu.hiirenVasenToimintoRelease(hiiriX, hiiriY);
                }
                EditoriRuutu.päivitäFokus(hiiriX, hiiriY);
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
                isKeyDown(GLFW_KEY_DOWN) ||
                isJoystickAnalogDown(ANALOG_L_VASEN) ||
                isJoystickAnalogDown(ANALOG_L_OIKEA) ||
                isJoystickAnalogDown(ANALOG_L_YLÖS) ||
                isJoystickAnalogDown(ANALOG_L_ALAS)
            ) {
                Pelaaja.keimonState = KeimonState.JUOKSU;
            }
            else Pelaaja.keimonState = KeimonState.IDLE;

            if ((isKeyDown(GLFW_KEY_A) || isKeyDown(GLFW_KEY_LEFT) || isJoystickAnalogDown(ANALOG_L_VASEN)) && ((isKeyDown(GLFW_KEY_W) || isKeyDown(GLFW_KEY_UP) || isJoystickAnalogDown(ANALOG_L_YLÖS)))) {
                Pelaaja.keimonSuunta = Suunta.YLÄVASEN;
                Pelaaja.keimonSuuntaVasenOikea = SuuntaVasenOikea.VASEN;
            }
            else if ((isKeyDown(GLFW_KEY_A) || isKeyDown(GLFW_KEY_LEFT) || isJoystickAnalogDown(ANALOG_L_VASEN)) && ((isKeyDown(GLFW_KEY_S) || isKeyDown(GLFW_KEY_DOWN) || isJoystickAnalogDown(ANALOG_L_ALAS)))) {
                Pelaaja.keimonSuunta = Suunta.ALAVASEN;
                Pelaaja.keimonSuuntaVasenOikea = SuuntaVasenOikea.VASEN;
            }
            else if ((isKeyDown(GLFW_KEY_D) || isKeyDown(GLFW_KEY_RIGHT) || isJoystickAnalogDown(ANALOG_L_OIKEA)) && ((isKeyDown(GLFW_KEY_W) || isKeyDown(GLFW_KEY_UP) || isJoystickAnalogDown(ANALOG_L_YLÖS)))) {
                Pelaaja.keimonSuunta = Suunta.YLÄOIKEA;
                Pelaaja.keimonSuuntaVasenOikea = SuuntaVasenOikea.OIKEA;
            }
            else if ((isKeyDown(GLFW_KEY_D) || isKeyDown(GLFW_KEY_RIGHT) || isJoystickAnalogDown(ANALOG_L_OIKEA)) && ((isKeyDown(GLFW_KEY_S) || isKeyDown(GLFW_KEY_DOWN) || isJoystickAnalogDown(ANALOG_L_ALAS)))) {
                Pelaaja.keimonSuunta = Suunta.ALAOIKEA;
                Pelaaja.keimonSuuntaVasenOikea = SuuntaVasenOikea.OIKEA;
            }
            else if (isKeyDown(GLFW_KEY_A) || isKeyDown(GLFW_KEY_LEFT) || isJoystickAnalogDown(ANALOG_L_VASEN)) {
                Pelaaja.keimonSuunta = Suunta.VASEN;
                Pelaaja.keimonSuuntaVasenOikea = SuuntaVasenOikea.VASEN;
            }
            else if (isKeyDown(GLFW_KEY_D) || isKeyDown(GLFW_KEY_RIGHT) || isJoystickAnalogDown(ANALOG_L_OIKEA)) {
                Pelaaja.keimonSuunta = Suunta.OIKEA;
                Pelaaja.keimonSuuntaVasenOikea = SuuntaVasenOikea.OIKEA;
            }
            else if (isKeyDown(GLFW_KEY_W) || isKeyDown(GLFW_KEY_UP) || isJoystickAnalogDown(ANALOG_L_YLÖS)) {
                Pelaaja.keimonSuunta = Suunta.YLÖS;
            }
            else if (isKeyDown(GLFW_KEY_S) || isKeyDown(GLFW_KEY_DOWN) || isJoystickAnalogDown(ANALOG_L_ALAS)) {
                Pelaaja.keimonSuunta = Suunta.ALAS;
            }
        }
    }

    private void tarkistaJumittavatAnalogit() {
        if (!isKeyDown(GLFW_KEY_W) && !isKeyDown(GLFW_KEY_S) && !isKeyDown(GLFW_KEY_A) && !isKeyDown(GLFW_KEY_D) && !isKeyDown(GLFW_KEY_UP) && !isKeyDown(GLFW_KEY_DOWN) && !isKeyDown(GLFW_KEY_LEFT) && !isKeyDown(GLFW_KEY_RIGHT)) {
            if (!isJoystickAnalogDown(ANALOG_L_YLÖS) && !isJoystickAnalogDown(ANALOG_L_ALAS) && !isJoystickAnalogDown(ANALOG_L_VASEN) && !isJoystickAnalogDown(ANALOG_L_OIKEA)) {
                nollaaPainallukset();
            }
        }
    }

    private void tarkistaSyöttölaite() {
        for (int i = 32; i < GLFW_KEY_LAST; i++) {
            if (isKeyDown(i)) {
                Peli.viimeisinSyöteLaite = SyöteLaitteet.NÄPPÄIMISTÖ;
            }
        }
        for (int i = 0; i < GLFW_JOYSTICK_LAST; i++) {
            if (isJoystickButtonPressed(i)) {
                Peli.viimeisinSyöteLaite = SyöteLaitteet.PELIOHJAIN;
            }
        }
        for (int i = 0; i < 16; i++) {
            if (isJoystickHatPressed(i)) {
                Peli.viimeisinSyöteLaite = SyöteLaitteet.PELIOHJAIN;
            }
        }
        for (int i = 0; i < 10; i++) {
            if (isJoystickAnalogPressed(i)) {
                Peli.viimeisinSyöteLaite = SyöteLaitteet.PELIOHJAIN;
            }
        }
    }

    private static void tavarapaikanVaihto(int tavarapaikka) {
        Peli.esineValInt = tavarapaikka;
        Peli.valittuEsine = Pelaaja.esineet[Peli.esineValInt];
    }

    private static void tavarapaikanVaihtoOhjain(int määrä) {
        Peli.esineValInt += määrä;
        if (Peli.esineValInt < 0) Peli.esineValInt += Pelaaja.esineet.length;
        Peli.esineValInt %= Pelaaja.esineet.length;
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

    public static void nollaaPainallukset() {
        Maailma.lopetaLiike(null);
        Maailma3D.lopetaLiike(null);
    }
}
