package keimo.HuoneEditori.Keimo3D;

import keimo.HuoneEditori.TavoiteEditori.TavoitteenMuokkausIkkuna;
import keimo.Ikkunat.LatausIkkuna;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.awt.TextRenderer;

import java.awt.Frame;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;

public class Keimo3D {

    static boolean liikuEteen;
    static boolean liikuTaakse;
    static boolean LiikuVasemmalle;
    static boolean liikuOikealle;
    static float hSpeed = 0.05f;
    static float vSpeed = 0;

    static boolean translateXNeg = false;
    static boolean translateXPos = false;
    static boolean translateYNeg = false;
    static boolean translateYPos = false;
    static boolean translateZNeg = false;
    static boolean translateZPos = false;
    static boolean aloitaHyppy = false;
    static double posStep = 0.1;
    static float xSij = 0;
    static float ySij = 1f;
    static float zSij = 0;
    static float kameranYSij = ySij + 0.5f;
    static float xKohde = xSij;
    static float yKohde = ySij;
    static float zKohde = zSij -2;
    static float upX = 0;
    static float upY = 1f;
    static float upZ = 0;

    static boolean rotXPos = false;
    static boolean rotXNeg = false;
    static boolean rotYPos = false;
    static boolean rotYNeg = false;
    static boolean rotZPos = false;
    static boolean rotZNeg = false;
    static double kääntöNopeus = 1;
    static float yaw = 0;
    static float pitch = 0;
    static float roll = 0;

    static boolean testU1Neg = false;
    static boolean testU1Pos = false;
    static boolean testV1Neg = false;
    static boolean testV1Pos = false;
    static boolean testU2Neg = false;
    static boolean testU2Pos = false;
    static boolean testV2Neg = false;
    static boolean testV2Pos = false;
    static boolean testU3Neg = false;
    static boolean testU3Pos = false;
    static boolean testV3Neg = false;
    static boolean testV3Pos = false;
    static int testUV = 0;
    
    static TextRenderer renderer;
    static FPSAnimator animator;
    static Random random = new Random();
    static double targetFPS;
    static double fps;
    static boolean säieKäynnissä = false;

    protected static final Frame frame = new Frame("Keimo3D demo");

    protected static ArrayList<MukautuvaKenttäObjekti> testiObjektit = new ArrayList<>();

    public static void käynnistäKeimo3D() {
        try {
            LatausIkkuna.palautaLatausIkkuna();
            LatausIkkuna.päivitäLatausTeksti("Keimo3D engine käynnistyy...");
            testKeimo3D();
            säieKäynnissä = true;
        }
        catch (Exception e) {
            e.printStackTrace();
            LatausIkkuna.suljeLatausIkkuna();
        }
        Thread keimo3DinputThread = new Thread() {
            @Override
            public void run() {
                System.out.println("käynnissä");
                while (säieKäynnissä) {
                    mainLoop();
                    try {
                        Thread.sleep(10);
                    }
                    catch (InterruptedException ie) {
                        System.out.println("interrupted");
                        ie.printStackTrace();
                    }
                }
            }
        };
        keimo3DinputThread.start();
    }

    static void testKeimo3D() {
        GLProfile glprofile = GLProfile.getDefault();
        GLCapabilities glcapabilities = new GLCapabilities(glprofile);
        final GLCanvas glcanvas = new GLCanvas(glcapabilities);

        glcanvas.addGLEventListener(new GLEventListener() {
            
            private GLU glu = new GLU();

            @Override
            public void init(GLAutoDrawable glautodrawable) {

            }
            
            @Override
            public void dispose(GLAutoDrawable glautodrawable) {

            }
            
            @Override
            public void display(GLAutoDrawable glautodrawable) {
                double startTime = System.nanoTime();
                final GL2 gl = glautodrawable.getGL().getGL2();
                // Clear The Screen And The Depth Buffer
                gl.glClear( GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
                gl.glLoadIdentity(); // Reset The View
                glu.gluLookAt(xSij, kameranYSij, zSij, xKohde, yKohde, zKohde, upX, upY, upZ);

                gl.glFinish();
                double endTime = System.nanoTime();
                double timeMs = (endTime - startTime)/1_000_000d;
                //fps = 1000d / timeMs;
                fps = animator.getLastFPS();
                targetFPS = animator.getFPS();
            }

            @Override
            public void reshape(GLAutoDrawable glautodrawable, int x, int y, int width, int height) {

            }
        });

        glcanvas.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                //throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_A:
                        //translateXPos = true;
                        LiikuVasemmalle = true;
                    break;
                    case KeyEvent.VK_D:
                        //translateXNeg = true;
                        liikuOikealle = true;
                    break;
                    case KeyEvent.VK_W:
                        //translateZPos = true;
                        liikuEteen = true;
                    break;
                    case KeyEvent.VK_S:
                        //translateZNeg = true;
                        liikuTaakse = true;
                    break;
                    case KeyEvent.VK_Q:
                        translateYPos = true;
                    break;
                    case KeyEvent.VK_E:
                        translateYNeg = true;
                    break;
                    case KeyEvent.VK_R:
                        randUV();
                    break;
                    case KeyEvent.VK_SPACE:
                        aloitaHyppy = true;
                    break;
                    case KeyEvent.VK_NUMPAD4:
                        rotYNeg = true;
                    break;
                    case KeyEvent.VK_NUMPAD6:
                        rotYPos = true;
                    break;
                    case KeyEvent.VK_NUMPAD8:
                        rotXPos = true;
                    break;
                    case KeyEvent.VK_NUMPAD2:
                        rotXNeg = true;
                    break;
                    case KeyEvent.VK_NUMPAD7:
                        rotZPos = true;
                    break;
                    case KeyEvent.VK_NUMPAD9:
                        rotZNeg = true;
                    break;
                    // case KeyEvent.VK_LEFT:
                    //     testU1Neg = true;
                    // break;
                    // case KeyEvent.VK_RIGHT:
                    //     testU1Pos = true;
                    // break;
                    // case KeyEvent.VK_DOWN:
                    //     testV1Neg = true;
                    // break;
                    // case KeyEvent.VK_UP:
                    //     testV1Pos = true;
                    // break;
                    case KeyEvent.VK_Y:
                        testU1Neg = true;
                    break;
                    case KeyEvent.VK_U:
                        testU1Pos = true;
                    break;
                    case KeyEvent.VK_I:
                        testV1Neg = true;
                    break;
                    case KeyEvent.VK_O:
                        testV1Pos = true;
                    break;
                    case KeyEvent.VK_H:
                        testU2Neg = true;
                    break;
                    case KeyEvent.VK_J:
                        testU2Pos = true;
                    break;
                    case KeyEvent.VK_K:
                        testV2Neg = true;
                    break;
                    case KeyEvent.VK_L:
                        testV2Pos = true;
                    break;
                    case KeyEvent.VK_N:
                        testU3Neg = true;
                    break;
                    case KeyEvent.VK_M:
                        testU3Pos = true;
                    break;
                    case KeyEvent.VK_COMMA:
                        testV3Neg = true;
                    break;
                    case KeyEvent.VK_PERIOD:
                        testV3Pos = true;
                    break;
                    default:
                        System.out.println("other pressed");
                    break;
                } 
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_A:
                        //translateXPos = false;
                        LiikuVasemmalle = false;
                    break;
                    case KeyEvent.VK_D:
                        //translateXNeg = false;
                        liikuOikealle = false;
                    break;
                    case KeyEvent.VK_W:
                        //translateZPos = false;
                        liikuEteen = false;
                    break;
                    case KeyEvent.VK_S:
                        //translateZNeg = false;
                        liikuTaakse = false;
                    break;
                    case KeyEvent.VK_Q:
                        translateYPos = false;
                    break;
                    case KeyEvent.VK_E:
                        translateYNeg = false;
                    break;
                    case KeyEvent.VK_SPACE:
                        aloitaHyppy = false;
                    break;
                    case KeyEvent.VK_NUMPAD4:
                        rotYNeg = false;
                    break;
                    case KeyEvent.VK_NUMPAD6:
                        rotYPos = false;
                    break;
                    case KeyEvent.VK_NUMPAD8:
                        rotXPos = false;
                    break;
                    case KeyEvent.VK_NUMPAD2:
                        rotXNeg = false;
                    break;
                    case KeyEvent.VK_NUMPAD7:
                        rotZPos = false;
                    break;
                    case KeyEvent.VK_NUMPAD9:
                        rotZNeg = false;
                    break;
                    // case KeyEvent.VK_LEFT:
                    //     testUNeg = false;
                    // break;
                    // case KeyEvent.VK_RIGHT:
                    //     testUPos = false;
                    // break;
                    // case KeyEvent.VK_DOWN:
                    //     testVNeg = false;
                    // break;
                    // case KeyEvent.VK_UP:
                    //     testVPos = false;
                    // break;
                    case KeyEvent.VK_Y:
                        testU1Neg = false;
                    break;
                    case KeyEvent.VK_U:
                        testU1Pos = false;
                    break;
                    case KeyEvent.VK_I:
                        testV1Neg = false;
                    break;
                    case KeyEvent.VK_O:
                        testV1Pos = false;
                    break;
                    case KeyEvent.VK_H:
                        testU2Neg = false;
                    break;
                    case KeyEvent.VK_J:
                        testU2Pos = false;
                    break;
                    case KeyEvent.VK_K:
                        testV2Neg = false;
                    break;
                    case KeyEvent.VK_L:
                        testV2Pos = false;
                    break;
                    case KeyEvent.VK_N:
                        testU3Neg = false;
                    break;
                    case KeyEvent.VK_M:
                        testU3Pos = false;
                    break;
                    case KeyEvent.VK_COMMA:
                        testV3Neg = false;
                    break;
                    case KeyEvent.VK_PERIOD:
                        testV3Pos = false;
                    break;
                }
            }
        });
        glcanvas.addGLEventListener(new Objektit3D());
        glcanvas.addGLEventListener(new HUDTeksti());
        

        frame.add(glcanvas);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowevent) {
                frame.remove(glcanvas);
                frame.dispose();
                säieKäynnissä = false;
            }
        });
        frame.setIconImage(new ImageIcon("tiedostot/kuvat/menu/gui/keimo3d.png").getImage());
        frame.setLocationRelativeTo(TavoitteenMuokkausIkkuna.ikkuna);
        frame.setSize(640, 480);
        frame.setVisible(true);

        animator = new FPSAnimator(glcanvas, 1000, true);
        animator.setUpdateFPSFrames(5, null);
        animator.start();

        LatausIkkuna.suljeLatausIkkuna();
        glcanvas.requestFocus();
    }

    private static void liiku(Liike liike) {
        switch (liike) {
            case ETEENPÄIN:
                xSij += hSpeed * (float)Math.cos(Math.toRadians(yaw));
                zSij += hSpeed * (float)Math.sin(Math.toRadians(yaw));
            break;
            case TAAKSEPÄIN:
                xSij -= hSpeed * (float)Math.cos(Math.toRadians(yaw));
                zSij -= hSpeed * (float)Math.sin(Math.toRadians(yaw));
            break;
            case VASEN:
                xSij += hSpeed * (float)Math.sin(Math.toRadians(yaw));
                zSij -= hSpeed * (float)Math.cos(Math.toRadians(yaw));
            break;
            case OIKEA:
                xSij -= hSpeed * (float)Math.sin(Math.toRadians(yaw));
                zSij += hSpeed * (float)Math.cos(Math.toRadians(yaw));
            break;
            case ALAS:
                break;
            case YLÖS:
                break;
            case null, default:
                break;
        }
    }

    private static void hyppää() {
        if (ySij <= 0f) {
            vSpeed += 0.1;
        }
    }

    // private static void moveModel(Translate translate) {
    //     switch (translate) {
    //         case X_POS:
    //             xPos += posStep;
    //         break;
    //         case X_NEG:
    //             xPos -= posStep;
    //         break;
    //         case Y_POS:
    //             yPos += posStep;
    //         break;
    //         case Y_NEG:
    //             yPos -= posStep;
    //         break;
    //         case Z_POS:
    //             zPos += posStep;
    //         break;
    //         case Z_NEG:
    //             zPos -= posStep;
    //         break;
    //     }
    // }

    private static void käännäNäkymää(Rotate rotate) {
        switch (rotate) {
            case ROLL_NEG:
                roll -= kääntöNopeus;
            break;
            case ROLL_POS:
                roll += kääntöNopeus;
            break;
            case YAW_POS:
                yaw += kääntöNopeus;
            break;
            case YAW_NEG:
                yaw -= kääntöNopeus;
            break;
            case PITCH_POS:
                pitch += kääntöNopeus;
            break;
            case PITCH_NEG:
                pitch -= kääntöNopeus;
            break;
        }
        if (yaw < 0) yaw += 360;
        //if (pitch < -90) pitch = -90;
        //else if (pitch > 90) pitch = 90;
        if (roll < 0) roll += 360;
        yaw %= 360;
        pitch %= 360;
        roll %= 360;
    }

    private static void liikutaObjekteja() {
        for (OBJMalli testObj : Objektit3D.testiObjektit) {
            if (testObj instanceof MukautuvaKenttäObjekti) {
                MukautuvaKenttäObjekti obj = (MukautuvaKenttäObjekti) testObj;
                switch (obj.objektiTyyppi) {
                    case LIIKKUVA_X_EDESTAKAISIN:
                        if (obj.liikettäJäljellä > 0) {
                            if (obj.käänteinenSuunta) obj.originX -= obj.liikkeenSuuruus;
                            else obj.originX += obj.liikkeenSuuruus;
                            obj.liikettäJäljellä--;
                        }
                    break;
                    case LIIKKUVA_Y_EDESTAKAISIN:
                        if (obj.liikettäJäljellä > 0) {
                            if (obj.käänteinenSuunta) obj.originY -= obj.liikkeenSuuruus;
                            else obj.originY += obj.liikkeenSuuruus;
                            obj.liikettäJäljellä--;
                        }
                    break;
                    case LIIKKUVA_Z_EDESTAKAISIN:
                        if (obj.liikettäJäljellä > 0) {
                            if (obj.käänteinenSuunta) obj.originZ -= obj.liikkeenSuuruus;
                            else obj.originZ += obj.liikkeenSuuruus;
                            obj.liikettäJäljellä--;
                        }
                    break;
                    case PYÖRIVÄ_Y:
                        obj.rotaatioY += obj.liikkeenSuuruus;
                    break;
                    case null, default:
                    break;
                }
                if (obj.liikettäJäljellä <= 0) {
                    obj.käänteinenSuunta = !obj.käänteinenSuunta;
                    obj.liikettäJäljellä = 100;
                }
            }
        }
    }

    private enum Liike {
        ETEENPÄIN,
        TAAKSEPÄIN,
        VASEN,
        OIKEA,
        YLÖS,
        ALAS;
    }

    private enum Translate {
        X_NEG,
        X_POS,
        Y_NEG,
        Y_POS,
        Z_NEG,
        Z_POS;
    }

    private enum Rotate {
        YAW_NEG,
        YAW_POS,
        PITCH_NEG,
        PITCH_POS,
        ROLL_NEG,
        ROLL_POS;
    }

    public static void mainLoop() {
        if (liikuEteen) {
            liiku(Liike.ETEENPÄIN);
        }
        if (liikuTaakse) {
            liiku(Liike.TAAKSEPÄIN);
        }
        if (LiikuVasemmalle) {
            liiku(Liike.VASEN);
        }
        if (liikuOikealle) {
            liiku(Liike.OIKEA);
        }
        if (rotXPos) {
            käännäNäkymää(Rotate.PITCH_POS);
        }
        if (rotXNeg) {
            käännäNäkymää(Rotate.PITCH_NEG);
        }
        if (rotYPos) {
            käännäNäkymää(Rotate.YAW_POS);
        }
        if (rotYNeg) {
            käännäNäkymää(Rotate.YAW_NEG);
        }
        if (rotZPos) {
            käännäNäkymää(Rotate.ROLL_POS);
        }
        if (rotZNeg) {
            käännäNäkymää(Rotate.ROLL_NEG);
        }
        kameranYSij = ySij + 0.5f;
        yKohde = kameranYSij + (float)Math.sin(Math.toRadians(pitch));
        xKohde = xSij + (float)Math.cos(Math.toRadians(yaw)) * (float)Math.cos(Math.toRadians(pitch));
        zKohde = zSij + (float)Math.sin(Math.toRadians(yaw)) * (float)Math.cos(Math.toRadians(pitch));
        upY = (float)Math.cos(Math.toRadians(roll)) * (float)Math.cos(Math.toRadians(pitch));
        upZ = (float)Math.sin(Math.toRadians(roll)) * (float)Math.cos(Math.toRadians(yaw)) * (float)Math.cos(Math.toRadians(pitch));
        upX = (float)Math.sin(Math.toRadians(roll)) * (float)-Math.sin(Math.toRadians(yaw)) * (float)Math.cos(Math.toRadians(pitch));
        if (ySij <= 0f) {
            ySij = 0f;
            vSpeed = 0;
        }
        if (aloitaHyppy) {
            hyppää();
        }
        if (ySij >= 0f) {
            ySij += vSpeed;
            if (vSpeed > -0.1f) {
                vSpeed -= 0.002f;
            }
        }
        mitenVitunVammaisenFunktionVoiTarviaUVMäppäystenTestaamiseenKysynpäVaan();
        liikutaObjekteja();
        //System.out.println("" + doJump + " " + vSpeed);
    }

    private static void mitenVitunVammaisenFunktionVoiTarviaUVMäppäystenTestaamiseenKysynpäVaan() {
        if (testU1Neg) {
            //testUV--;
            //randUV();
            OBJMalli.randU1 -= 0.1f;
            System.out.println("randU1: " + OBJMalli.randU1);
            System.out.println("randV1: " + OBJMalli.randV1);
        }
        if (testU1Pos) {
            //testUV++;
            //randUV();
            OBJMalli.randU1 += 0.1f;
            System.out.println("randU1: " + OBJMalli.randU1);
            System.out.println("randV1: " + OBJMalli.randV1);
        }
        if (testV1Neg) {
            //testUV--;
            //randUV();
            OBJMalli.randV1 -= 0.1f;
            System.out.println("randU1: " + OBJMalli.randU1);
            System.out.println("randV1: " + OBJMalli.randV1);
        }
        if (testV1Pos) {
            //testUV++;
            //randUV();
            OBJMalli.randV1 += 0.1f;
            System.out.println("randU1: " + OBJMalli.randU1);
            System.out.println("randV1: " + OBJMalli.randV1);
        }
        if (testU2Neg) {
            //testUV--;
            //randUV();
            OBJMalli.randU2 -= 0.1f;
            System.out.println("randU2: " + OBJMalli.randU2);
            System.out.println("randV2: " + OBJMalli.randV2);
        }
        if (testU2Pos) {
            //testUV++;
            //randUV();
            OBJMalli.randU2 += 0.1f;
            System.out.println("randU2: " + OBJMalli.randU2);
            System.out.println("randV2: " + OBJMalli.randV2);
        }
        if (testV2Neg) {
            //testUV--;
            //randUV();
            OBJMalli.randV2 -= 0.1f;
            System.out.println("randU2: " + OBJMalli.randU2);
            System.out.println("randV2: " + OBJMalli.randV2);
        }
        if (testV2Pos) {
            //testUV++;
            //randUV();
            OBJMalli.randV2 += 0.1f;
            System.out.println("randU2: " + OBJMalli.randU2);
            System.out.println("randV2: " + OBJMalli.randV2);
        }
        if (testU3Neg) {
            //testUV--;
            //randUV();
            OBJMalli.randU3 -= 0.1f;
            System.out.println("randU3: " + OBJMalli.randU3);
            System.out.println("randV3: " + OBJMalli.randV3);
        }
        if (testU3Pos) {
            //testUV++;
            //randUV();
            OBJMalli.randU3 += 0.1f;
            System.out.println("randU3: " + OBJMalli.randU3);
            System.out.println("randV3: " + OBJMalli.randV3);
        }
        if (testV3Neg) {
            //testUV--;
            //randUV();
            OBJMalli.randV3 -= 0.1f;
            System.out.println("randU3: " + OBJMalli.randU3);
            System.out.println("randV3: " + OBJMalli.randV3);
        }
        if (testV3Pos) {
            //testUV++;
            //randUV();
            OBJMalli.randV3 += 0.1f;
            System.out.println("randU3: " + OBJMalli.randU3);
            System.out.println("randV3: " + OBJMalli.randV3);
        }
    }

    private static void randUV() {
        // OBJMalli.randU1 = random.nextInt(-1, 2);
        // OBJMalli.randV1 = random.nextInt(-1, 2);
        // OBJMalli.randU2 = random.nextInt(-1, 2);
        // OBJMalli.randV2 = random.nextInt(-1, 2);
        // OBJMalli.randU3 = random.nextInt(-1, 2);
        // OBJMalli.randV3 = random.nextInt(-1, 2);

        OBJMalli.randU1 = (testUV / 3);
        OBJMalli.randV1 = (OBJMalli.randU1 / 3);
        OBJMalli.randU2 = (OBJMalli.randV1 / 3);
        OBJMalli.randV2 = (OBJMalli.randU2 / 3);
        OBJMalli.randU3 = (OBJMalli.randV2 / 3);
        OBJMalli.randV3 = (OBJMalli.randU3 / 3);
        OBJMalli.randU1 %= 3;
        OBJMalli.randV1 %= 3;
        OBJMalli.randU2 %= 3;
        OBJMalli.randV2 %= 3;
        OBJMalli.randU3 %= 3;
        OBJMalli.randV3 %= 3;
        OBJMalli.randU1--;
        OBJMalli.randV1--;
        OBJMalli.randU2--;
        OBJMalli.randV2--;
        OBJMalli.randU3--;
        OBJMalli.randV3--;
        System.out.println("testUV: " + testUV);
        System.out.println("randU1: " + OBJMalli.randU1);
        System.out.println("randV1: " + OBJMalli.randV1);
        System.out.println("randU2: " + OBJMalli.randU2);
        System.out.println("randV2: " + OBJMalli.randV2);
        System.out.println("randU3: " + OBJMalli.randU3);
        System.out.println("randV3: " + OBJMalli.randV3);
    }
}
