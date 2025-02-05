package keimo.keimoEngine.menu;

import keimo.HuoneEditori.Keimo3D.Keimo3D;
import keimo.keimoEngine.assets.Assets;
import keimo.keimoEngine.grafiikat.Shader;
import keimo.keimoEngine.grafiikat.Teksti;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.keimoEngine.grafiikat.objekti2d.Model;
import keimo.keimoEngine.grafiikat.objekti3d.Transform3D;
import keimo.keimoEngine.ikkuna.Kamera;
import keimo.keimoEngine.ikkuna.Window;

import java.awt.Color;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

public class TestiRuutu {
    private static Shader shader = new Shader("shader3dtest");
    private static Shader shader1 = new Shader("shader");
    private static Tekstuuri testiTekstuuri = new Tekstuuri("tiedostot/kuvat/muut/84gs.jpg");
    private static float posX = 0, posY = 0, posZ = 0;

    static float testX, testY, testZ;
    static float liikeNopeus = 4f;
    static float liikkeenSuuruus = 50f;
    static float pyörimisNopeus = 0.5f;
    private static int uniformMatProjection, uniformMatTransformWorld, uniformMatTransformObject;

    public static void render(Window window, Kamera kamera, Transform3D transform) {
        shader.bind();
        testX += liikeNopeus;
        testY += liikeNopeus;
        testZ += liikeNopeus;
        //transform.setPosition(new Vector3f((float)Math.sin(Math.toRadians(testX)), 0, 0));
        transform.getRotation().rotateAxis((float)Math.toRadians(pyörimisNopeus), 0, 1, 0);

        //kamera.setPosition(new Vector3f(0 * (float)Math.sin(Math.toRadians(testX)), 0 * (float)Math.sin(Math.toRadians(testY)), liikkeenSuuruus * (float)Math.sin(Math.toRadians(testZ))));
        kamera.setPosition(new Vector3f(0, 0, 0));
        kamera.setPerspective((float)Math.toRadians(70), 1, 0.001f, 1000f);
		//kamera.setRotation(new Quaternionf(new AxisAngle4f((float)Math.toRadians(testX), new Vector3f(1, 1, 1))));
        //kamera.setRotation(new Quaternionf(new AxisAngle4f(0.5f * (float)Math.toRadians(testX), new Vector3f(0, 0, 0.125f))));
        //kamera.setLookAt(0, 0, 0, (float)Math.sin(Math.toRadians(testX)), 0, (float)Math.cos(Math.toRadians(testX)), 0, 1, 0);
        //kamera.setProjection(200, 200);

        shader.setCamera(kamera);
        shader.setTransform(transform);

        Matrix4f matOtsikko = new Matrix4f();
        shader.setUniform("projection", matOtsikko);
        //testiTekstuuri.bind(0);
        //Assets.getModel().render();

        //glLoadIdentity();
        //Assets.getModel().render();
        //Assets.getMeshQuad().draw(GL_QUADS);
        //Assets.getMeshTriangle().draw(GL_TRIANGLES);
        //Assets.getMeshCube().draw(GL_QUADS);
        //Assets.getMeshObj("Wooden_Crate_2").draw(GL_TRIANGLES);
        //Assets.getMeshObj("cube").draw(GL_TRIANGLES);
        Assets.getModel3D("asunto_yokyla").draw();
        Assets.getModel3D("yokyla_40-54").draw();
        Assets.getModel3D("Sieni").draw();
    }

    static float angle = 0;
    public static void render1() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f); /* tyhjennysväri (taustaväri) */
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0.0, 1.0, 0.0, 1.0, -1.0, 1.0);

        shader1.bind();
        Matrix4f matOtsikko = new Matrix4f();
        angle += 0.01;
        matOtsikko.setLookAt(0, 0, 0, (float)Math.sin(angle), 0, (float)Math.cos(angle), 0, 1, 0);
        shader1.setUniform("projection", matOtsikko);

        glClear(GL_COLOR_BUFFER_BIT);
        glColor3f(1.0f, 1.0f, 1.0f);
        glBegin(GL_POLYGON);
            glVertex3f(0.25f, 0.25f, 0.0f);
            glVertex3f(0.75f, 0.25f, 0.0f);
            glVertex3f(0.75f, 0.75f, 0.0f);
            glVertex3f(0.25f, 0.75f, 0.0f);
        glEnd();
        glFlush();
    }

    public static void render() {
        päivitäSijainti();
        glEnable(GL11.GL_DEPTH_TEST);
        glLoadIdentity();
        glLoadMatrixf(new Matrix4f().scale(4f).setLookAt(xSij, ySij, zSij, xKohde, yKohde, zKohde, upX, upY, upZ).get(new float[16]));

        shader1.bind();
        Matrix4f matrix = new Matrix4f();
        matrix.setLookAt(xSij, ySij, zSij, xKohde, yKohde, zKohde, upX, upY, upZ);
        shader1.setUniform("projection", matrix);
        testiTekstuuri.bind(0);

        float posX = 0, posY = 0, posZ = 0;
        glBegin(GL_QUADS);
            glColor3f(1f, 0f, 0f);

            glTexCoord2f(0.0f, 0.0f); glVertex3f(posX - 0.5f, posY, posZ + 0.5f);
            glTexCoord2f(1.0f, 0.0f); glVertex3f(posX + 0.5f, posY, posZ + 0.5f);
            glTexCoord2f(1.0f, 1.0f); glVertex3f(posX + 0.5f, posY + 1, posZ + 0.5f);
            glTexCoord2f(0.0f, 1.0f); glVertex3f(posX - 0.5f, posY + 1, posZ + 0.5f);

            //Back Face
            glTexCoord2f(1.0f, 0.0f); glVertex3f(posX - 0.5f, posY, posZ - 0.5f);
            glTexCoord2f(1.0f, 1.0f); glVertex3f(posX - 0.5f, posY + 1, posZ - 0.5f);
            glTexCoord2f(0.0f, 1.0f); glVertex3f(posX + 0.5f, posY + 1, posZ - 0.5f);
            glTexCoord2f(0.0f, 0.0f); glVertex3f(posX + 0.5f, posY, posZ - 0.5f);

            //Top Face
            glTexCoord2f(0.0f, 1.0f); glVertex3f(posX - 0.5f, posY + 1, posZ - 0.5f);
            glTexCoord2f(0.0f, 0.0f); glVertex3f(posX - 0.5f, posY + 1, posZ + 0.5f);
            glTexCoord2f(1.0f, 0.0f); glVertex3f(posX + 0.5f, posY + 1, posZ + 0.5f);
            glTexCoord2f(1.0f, 1.0f); glVertex3f(posX + 0.5f, posY + 1, posZ - 0.5f);

            //Bottom Face
            glTexCoord2f(1.0f, 1.0f); glVertex3f(posX - 0.5f, posY, posZ - 0.5f);
            glTexCoord2f(0.0f, 1.0f); glVertex3f(posX + 0.5f, posY, posZ - 0.5f);
            glTexCoord2f(0.0f, 0.0f); glVertex3f(posX + 0.5f, posY, posZ + 0.5f);
            glTexCoord2f(1.0f, 0.0f); glVertex3f(posX - 0.5f, posY, posZ + 0.5f);

            //Right face
            glTexCoord2f(1.0f, 0.0f); glVertex3f(posX + 0.5f, posY, posZ - 0.5f);
            glTexCoord2f(1.0f, 1.0f); glVertex3f(posX + 0.5f, posY + 1, posZ - 0.5f);
            glTexCoord2f(0.0f, 1.0f); glVertex3f(posX + 0.5f, posY + 1, posZ + 0.5f);
            glTexCoord2f(0.0f, 0.0f); glVertex3f(posX + 0.5f, posY, posZ + 0.5f);

            //Left Face
            glTexCoord2f(0.0f, 0.0f); glVertex3f(posX - 0.5f, posY, posZ - 0.5f);
            glTexCoord2f(1.0f, 0.0f); glVertex3f(posX - 0.5f, posY, posZ + 0.5f);
            glTexCoord2f(1.0f, 1.0f); glVertex3f(posX - 0.5f, posY + 1, posZ + 0.5f);
            glTexCoord2f(0.0f, 1.0f); glVertex3f(posX - 0.5f, posY + 1, posZ - 0.5f);
        
        glEnd();
        for (Model model : Assets.lattiaTilet) {
            testiTekstuuri.bind(0);
            model.render();
        }
    }

    public enum Liike {
        ETEENPÄIN,
        TAAKSEPÄIN,
        VASEN,
        OIKEA,
        YLÖS,
        ALAS;
    }

    public static double posStep = 0.01;
    public static float xSij = 0;
    public static float ySij = 1f;
    public static float zSij = 0;
    public static float kameranYSij = ySij + 0.5f;
    public static float xKohde = xSij;
    public static float yKohde = ySij;
    public static float zKohde = zSij -2;
    public static float upX = 0;
    public static float upY = 1f;
    public static float upZ = 0;
    public static float hSpeed = 0.05f;
    public static float vSpeed = 0.05f;
    public static double kääntöNopeus = 1;
    public static float yaw = 0;
    public static float pitch = 0;
    public static float roll = 0;

    public static void liiku(Liike liike) {
        switch (liike) {
            case ETEENPÄIN:
                xSij -= hSpeed * (float)Math.cos(Math.toRadians(yaw));
                zSij -= hSpeed * (float)Math.sin(Math.toRadians(yaw));
            break;
            case TAAKSEPÄIN:
                xSij += hSpeed * (float)Math.cos(Math.toRadians(yaw));
                zSij += hSpeed * (float)Math.sin(Math.toRadians(yaw));
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
                ySij -= vSpeed;
            break;
            case YLÖS:
                ySij += vSpeed;
            break;
            case null, default:
            break;
        }
    }

    private static void päivitäSijainti() {
        //kameranYSij = ySij + 0.5f;
        //yKohde = kameranYSij + (float)Math.sin(Math.toRadians(pitch));
        //xKohde = xSij + (float)Math.cos(Math.toRadians(yaw)) * (float)Math.cos(Math.toRadians(pitch));
        //zKohde = zSij + (float)Math.sin(Math.toRadians(yaw)) * (float)Math.cos(Math.toRadians(pitch));
        //upY = (float)Math.cos(Math.toRadians(roll)) * (float)Math.cos(Math.toRadians(pitch));
        //upZ = (float)Math.sin(Math.toRadians(roll)) * (float)Math.cos(Math.toRadians(yaw)) * (float)Math.cos(Math.toRadians(pitch));
        //upX = (float)Math.sin(Math.toRadians(roll)) * (float)-Math.sin(Math.toRadians(yaw)) * (float)Math.cos(Math.toRadians(pitch));
        yKohde = ySij + 0.5f;
        xKohde = xSij + (float)Math.cos(Math.toRadians(yaw));
        zKohde = zSij + (float)Math.sin(Math.toRadians(yaw));
        upY = 1;
        upZ = 0;
        upX = 0;
    }
}
