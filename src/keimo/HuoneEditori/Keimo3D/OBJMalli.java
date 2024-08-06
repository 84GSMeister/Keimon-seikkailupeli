package keimo.HuoneEditori.Keimo3D;

import keimo.HuoneEditori.Keimo3D.objLoader.builder.Build;
import keimo.HuoneEditori.Keimo3D.objLoader.builder.Face;
import keimo.HuoneEditori.Keimo3D.objLoader.builder.FaceVertex;
import keimo.HuoneEditori.Keimo3D.objLoader.builder.Material;
import keimo.HuoneEditori.Keimo3D.objLoader.parser.Parse;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class OBJMalli implements GLEventListener {

    Build builder;
    Random random = new Random();

    String modelName;
    String objPath;

    int defaultTextureInt;
    String defaultTextureFileName = "tiedostot/kuvat/muut/tiili.png";
    ArrayList<Integer> textureInts = new ArrayList<>();
    ArrayList<String> textureFileNames = new ArrayList<>();

    float originX = 0;
    float originY = 0;
    float originZ = 0;
    float scale = 1;
    boolean invertYZ = false;

    public static int randU1 = 0;
    public static int randV1 = 0;
    public static int randU2 = 0;
    public static int randV2 = 0;
    public static int randU3 = 0;
    public static int randV3 = 0;

    @Override
    public void display(GLAutoDrawable glautodrawable) {
        GL2 gl = glautodrawable.getGL().getGL2();
        gl.glColor3f(1f, 1f, 1f);
        if (builder != null) {
            for (Face f : builder.faces) {
                if (f.material != null) {
                    int textureFileNameId = textureFileNames.indexOf(f.material.mapKdFilename);
                    if (textureFileNameId != -1) {
                        int textureId = textureInts.get(textureFileNameId);
                        gl.glBindTexture(GL2.GL_TEXTURE_2D, textureId);
                    }
                }
                gl.glBegin(GL2.GL_TRIANGLES);
                // for (FaceVertex fv : f.vertices) {
                //     if (fv.t != null) {
                //         //gl.glTexCoord2f(fv.t.u, fv.t.v);
                //         //float testTexU = random.nextFloat(0.1f, 10f);
                //         //gl.glTexCoord2f(fv.t.u*testTexU, fv.t.v);
                //         gl.glTexCoord2f(fv.t.u, fv.t.v);
                //         System.out.println(f.material.name + ": " + fv.t.u + ", " + fv.t.v);
                //     }
                //     if (invertYZ) {
                //         gl.glVertex3f(fv.v.x * scale + originX, fv.v.z * scale + originZ, fv.v.y * scale + originY);
                //     }
                //     else {
                //         gl.glVertex3f(fv.v.x * scale + originX, fv.v.y * scale + originY, fv.v.z * scale + originZ);
                //     }
                // }
                try {
                    for (int i = 0; i < f.vertices.size(); i++) {
                        FaceVertex fv1 = f.vertices.get(0);
                        FaceVertex fv2 = f.vertices.get(1);
                        FaceVertex fv3 = f.vertices.get(2);
                        if (fv1.t != null) {
                            int u = 0, v = 0;
                            //gl.glTexCoord2f(u, v);
                            gl.glTexCoord2f(randU1, randV1);
                            //System.out.println("Vertex1: UV in file: " + fv1.t.u + ", " + fv1.t.v + "; UV in code: " + u + ", " + v + "; Normal: " + fv1.n.x + " " + fv1.n.y + " " + fv1.n.z);
                        }
                        if (invertYZ) {
                            gl.glVertex3f(fv1.v.x * scale + originX, fv1.v.z * scale + originZ, fv1.v.y * scale + originY);
                        }
                        else {
                            gl.glVertex3f(fv1.v.x * scale + originX, fv1.v.y * scale + originY, fv1.v.z * scale + originZ);
                        }
                        if (fv2.t != null) {
                            int u = 1, v = 1;
                            //gl.glTexCoord2f(u, v);
                            gl.glTexCoord2f(randU2, randV2);
                            //System.out.println("Vertex2: UV in file: " + fv2.t.u + ", " + fv2.t.v + "; UV in code: " + u + ", " + v + "; Normal: " + fv2.n.x + " " + fv2.n.y + " " + fv2.n.z);
                        }
                        if (invertYZ) {
                            gl.glVertex3f(fv2.v.x * scale + originX, fv2.v.z * scale + originZ, fv2.v.y * scale + originY);
                        }
                        else {
                            gl.glVertex3f(fv2.v.x * scale + originX, fv2.v.y * scale + originY, fv2.v.z * scale + originZ);
                        }
                        if (fv3.t != null) {
                            int u = 0, v = 1;
                            //gl.glTexCoord2f(u, v);
                            gl.glTexCoord2f(randU3, randV3);
                            //System.out.println("Vertex3: UV in file: " + fv3.t.u + ", " + fv3.t.v + "; UV in code: " + u + ", " + v + "; Normal: " + fv3.n.x + " " + fv3.n.y + " " + fv3.n.z);
                        }
                        if (invertYZ) {
                            gl.glVertex3f(fv3.v.x * scale + originX, fv3.v.z * scale + originZ, fv3.v.y * scale + originY);
                        }
                        else {
                            gl.glVertex3f(fv3.v.x * scale + originX, fv3.v.y * scale + originY, fv3.v.z * scale + originZ);
                        }
                    }
                }
                catch (IndexOutOfBoundsException ioobe) {
                    System.out.println("Only triangles supported.");
                }
                // try {
                //     int faces = 0, vertices = 0, normals = 0, texcoords = 0;
                //     int[][][] indexes = new int[faces][3][3]; // currently your indices [face][vertex][attrib_index]
                //     float[][] vertex = new float[vertices][3]; // your vertex positions
                //     float[][] normal = new float[normals][3]; // your normals
                //     float[][] texcoord = new float[texcoords][3]; // your texcoords

                //     int vertices2 = 3*faces;
                //     float[][] vertex2 = new float[vertices2][3]; // your vertex positions
                //     float[][] normal2 = new float[vertices2][3]; // your normals
                //     float[][] texcoord2 = new float[vertices2][2]; // your texcoords

                //     int v = 0;
                //     for (int face = 0; face < faces; face++) {

                //         for (int fv = 0; fv < 3; fv++, v++)
                //         {
                //             vertex2[v][0] = vertex[ indexes[face][fv][0] ][0];
                //             vertex2[v][1] = vertex[ indexes[face][fv][0] ][1];
                //             vertex2[v][2] = vertex[ indexes[face][fv][0] ][2];

                //             normal2[v][0] = normal[ indexes[face][fv][1] ][0];
                //             normal2[v][1] = normal[ indexes[face][fv][1] ][1];
                //             normal2[v][2] = normal[ indexes[face][fv][1] ][2];

                //             texcoord2[v][0] = texcoord[ indexes[face][fv][2] ][0];
                //             texcoord2[v][1] = texcoord[ indexes[face][fv][2] ][1];
                //         }
                //     }
                // }
                // catch (IndexOutOfBoundsException ioobe) {
                //     System.out.println("Only triangles supported.");
                // }
                gl.glEnd();
            }
        }
    }

    @Override
    public void dispose(GLAutoDrawable glautodrawable) {

    }

    @Override
    public void init(GLAutoDrawable glautodrawable) {
        final GL2 gl = glautodrawable.getGL().getGL2();
        try {
            String textureFileName = defaultTextureFileName;
            File im = new File(textureFileName);
            Texture t = TextureIO.newTexture(im, true);
            defaultTextureInt = t.getTextureObject(gl);
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        };
        for (Material m : builder.materialLib.values()) {
            System.out.println(m.mapKdFilename);
            if (m.mapKdFilename != null) {
                try {
                    String textureFileName = "tiedostot/3d-objektit/" + modelName + "/" + m.mapKdFilename;
                    File im = new File(textureFileName);
                    Texture t = TextureIO.newTexture(im, true);
                    textureInts.add(t.getTextureObject(gl));
                    textureFileNames.add(m.mapKdFilename);
                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                };
            }
        }
    }

    @Override
    public void reshape(GLAutoDrawable glautodrawable, int x, int y, int width, int height) {

    }

    public OBJMalli(String objNimi, float skaala, Point3D origin, boolean käännäYZ) {
        setFileName(objNimi);
        testParse();
        if (origin != null) {
            setOrigin(origin.x, origin.y, origin.z);
        }
        setScale(skaala, käännäYZ);
    }

    private void testParse() {
        try {
            builder = new Build();
            Parse obj = new Parse(builder, objPath);
            System.out.println("faces: " + builder.faceTriCount);
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private void setFileName(String modelFileName) {
        modelName = modelFileName;
        objPath = "tiedostot/3d-objektit/" + modelName + "/" + modelName + ".obj";
    }

    private void setOrigin(float orgX, float orgY, float orgZ) {
        originX = orgX;
        originY = orgY;
        originZ = orgZ;
    }

    private void setScale(float scaleFloat, boolean käännäYZ) {
        scale = scaleFloat;
        invertYZ = käännäYZ;
    }
}
