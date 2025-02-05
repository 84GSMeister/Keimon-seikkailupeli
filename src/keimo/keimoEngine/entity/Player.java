package keimo.keimoEngine.entity;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.Pelaaja.SuuntaVasenOikea;
import keimo.keimoEngine.assets.Assets;
import keimo.keimoEngine.collision.AABB;
import keimo.keimoEngine.collision.Collision;
import keimo.keimoEngine.grafiikat.*;
import keimo.keimoEngine.grafiikat.objekti2d.Transform;
import keimo.keimoEngine.gui.hud.NäppäinVinkkiTekstit;
import keimo.keimoEngine.ikkuna.Kamera;
import keimo.keimoEngine.ikkuna.Window;
import keimo.keimoEngine.kenttä.Maailma;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.HashMap;
import java.util.Random;

public class Player {
    
    private Transform transform;
    private AABB boundingBox;

    private HashMap<String, Animaatio> animaatiot = new HashMap<>();
    private Tekstuuri hyökkäysTekstuuri = new Tekstuuri("tiedostot/kuvat/entity/hyökkäys_sprite.png");
    private Kuva esineTekstuuri = new Tekstuuri("tiedostot/kuvat/tyhjä.png");
    private Shader pelaajaShader = new Shader("shader");
    private Shader pelaajaShaderStaattinen = new Shader("staattinen");
    private boolean peilaaX = false;
    private boolean peilaaY = false;
    private int scale = 32;
    private Random random = new Random();

    public Player() {
        
        //this.model = Assets.getModel();
        //this.animation = new Animation(10, 15, "main_osoitin");
        //this.texture = new Texture("tiedostot/kuvat/pelaaja/default.gif");
        this.animaatiot.put("default", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/pelaaja_default.gif"));
        this.animaatiot.put("ylensyönti", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/pelaaja_ylensyönti.gif"));

        this.animaatiot.put("idle_hyvä_laiha", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/idle/pelaaja_idle_hyvä_laiha.gif"));
        this.animaatiot.put("idle_hyvä_normaali", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/idle/pelaaja_idle_hyvä_normaali.gif"));
        this.animaatiot.put("idle_hyvä_lihava", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/idle/pelaaja_idle_hyvä_lihava.gif"));
        this.animaatiot.put("idle_hyvä_erittäinlihava", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/idle/pelaaja_idle_hyvä_erittäinlihava.gif"));
        this.animaatiot.put("idle_neutraali_laiha", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/idle/pelaaja_idle_neutraali_laiha.gif"));
        this.animaatiot.put("idle_neutraali_normaali", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/idle/pelaaja_idle_neutraali_normaali.gif"));
        this.animaatiot.put("idle_neutraali_lihava", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/idle/pelaaja_idle_neutraali_lihava.gif"));
        this.animaatiot.put("idle_neutraali_erittäinlihava", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/idle/pelaaja_idle_neutraali_erittäinlihava.gif"));
        this.animaatiot.put("idle_huono_laiha", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/idle/pelaaja_idle_huono_laiha.gif"));
        this.animaatiot.put("idle_huono_normaali", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/idle/pelaaja_idle_huono_normaali.gif"));
        this.animaatiot.put("idle_huono_lihava", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/idle/pelaaja_idle_huono_lihava.gif"));
        this.animaatiot.put("idle_huono_erittäinlihava", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/idle/pelaaja_idle_huono_erittäinlihava.gif"));
        
        this.animaatiot.put("kävely_vasen_laiha", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_vasen_laiha.gif"));
        this.animaatiot.put("kävely_oikea_laiha", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_oikea_laiha.gif"));
        this.animaatiot.put("kävely_ylös_laiha", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_ylös_laiha.gif"));
        this.animaatiot.put("kävely_alas_laiha", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_alas_laiha.gif"));
        this.animaatiot.put("kävely_vasen_normaali", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_vasen_normaali.gif"));
        this.animaatiot.put("kävely_oikea_normaali", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_oikea_normaali.gif"));
        this.animaatiot.put("kävely_ylös_normaali", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_ylös_normaali.gif"));
        this.animaatiot.put("kävely_alas_normaali", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_alas_normaali.gif"));
        this.animaatiot.put("kävely_vasen_lihava", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_vasen_lihava.gif"));
        this.animaatiot.put("kävely_oikea_lihava", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_oikea_lihava.gif"));
        this.animaatiot.put("kävely_ylös_lihava", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_ylös_lihava.gif"));
        this.animaatiot.put("kävely_alas_lihava", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_alas_lihava.gif"));
        this.animaatiot.put("kävely_vasen_erittäinlihava", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_vasen_erittäinlihava.gif"));
        this.animaatiot.put("kävely_oikea_erittäinlihava", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_oikea_erittäinlihava.gif"));
        this.animaatiot.put("kävely_ylös_erittäinlihava", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_ylös_erittäinlihava.gif"));
        this.animaatiot.put("kävely_alas_erittäinlihava", new Animaatio(15, "tiedostot/kuvat/animaatiot/pelaaja/kävely/kävely_alas_erittäinlihava.gif"));
        this.transform = new Transform();
        this.transform.scale = new Vector3f(scale, scale,1);
        this.boundingBox = new AABB(new Vector2f(transform.pos.x, transform.pos.y), new Vector2f(1, 1));
    }

    public void update(float delta, Window window, Kamera camera, Maailma world) {
        transform.pos.set(new Vector3f((float)Pelaaja.hitbox.getCenterX()/scale -1, (float)-Pelaaja.hitbox.getCenterY()/scale +1, 0));
        boundingBox.getCenter().set(transform.pos.x, transform.pos.y);

        int collisionCheckRadius = 5;
        AABB[][] boxes = new AABB[5][5];
        for (int y = 0; y < collisionCheckRadius; y++) {
            for (int x = 0; x < collisionCheckRadius; x++) {
                boxes[x][y] = world.getTileBoundingBox(
                    (int)(((transform.pos.x / 2) + 0.5f) - (collisionCheckRadius / 2) + x),
                    (int)(((-transform.pos.y / 2) + 0.5f) - (collisionCheckRadius / 2) + y)
                );
            }
        }
        AABB box = null;
        for (int y = 0; y < boxes.length; y++) {
            for (int x = 0; x < boxes.length; x++) {
                if (boxes[x][y] != null) {
                    if (box == null) {
                        box = boxes[x][y];
                    }
                    Vector2f length1 = box.getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
                    Vector2f length2 = boxes[x][y].getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());

                    if (length1.lengthSquared() > length2.lengthSquared()) {
                        box = boxes[x][y];
                    }
                }
            }
        }

        if (box != null) {
            Collision data = boundingBox.getCollision(box);
            if (data.isIntersecting) {
                boundingBox.correctPosition(box, data);
                transform.pos.set(boundingBox.getCenter(), 0);
            }
        }

        //camera.setPosition(transform.pos.mul(-world.getScale()/2, new Vector3f()));
    }

    

    public void render(Kamera camera, Maailma world) {
        pelaajaShader.bind();
        pelaajaShader.setUniform("sampler", 0);

        float känniHajontaX = random.nextFloat(0.001f + Pelaaja.känninVoimakkuusFloat);
		float känniHajontaY = random.nextFloat(0.001f + Pelaaja.känninVoimakkuusFloat);
		float känniScaleX = random.nextFloat(0.001f + Pelaaja.känninVoimakkuusFloat);
		float känniScaleY = random.nextFloat(0.001f + Pelaaja.känninVoimakkuusFloat);
    
        float scaleX = 64;
        float scaleY = 64;

        pelaajaShader.setUniform("projection", transform.getProjection(camera.getProjection().translate(känniHajontaX, känniHajontaY, 0)));
        valitsePelaajanKuvake();
        if (Pelaaja.känniKuolemattomuus > 0) {
            if (Peli.globaaliTickit % 4 == 0 || Peli.globaaliTickit % 4 == 1) {
                pelaajaShader.setUniform("addcolor", new Vector4f(1, 0, 0, 0));
            }
            else {
                pelaajaShader.setUniform("addcolor", new Vector4f(0, 0, 1, 0));
            }
        }
        else pelaajaShader.setUniform("addcolor", new Vector4f(0, 0, 0, 0));
        if (Pelaaja.kuolemattomuusAika <= 0 || Peli.globaaliTickit % 2 == 0) {
            Assets.getModel(0, peilaaX, peilaaY).render();
        }

        if (Pelaaja.hyökkäysAika > 0) {
            boolean hyökkäysKuvakkeenPeilaus = Pelaaja.keimonSuuntaVasenOikea == SuuntaVasenOikea.VASEN;
            pelaajaShader.setUniform("projection", transform.getProjection(camera.getProjection().translate(känniHajontaX, känniHajontaY, 0).translate(hyökkäysKuvakkeenPeilaus ? -32 : 32, 0, 0)));
            hyökkäysTekstuuri.bind(0);
            Assets.getModel(0, !hyökkäysKuvakkeenPeilaus, false).render();
        }
        if (Peli.valittuEsine != null) {
            pelaajaShader.setUniform("projection", transform.getProjection(camera.getProjection().scale(0.5f, 0.5f, 0).translate((float)Pelaaja.hitbox.getX() + känniHajontaX, (float)-Pelaaja.hitbox.getY() + känniHajontaY, 0)));
            esineTekstuuri = Peli.valittuEsine.annaTekstuuri();
            esineTekstuuri.bind(0);
            Assets.getModel().render();
        }
        if (Peli.annaObjektiKenttä()[Pelaaja.sijX][Pelaaja.sijY] != null) {
            NäppäinVinkkiTekstit.renderöiNäppäinVinkki(pelaajaShaderStaattinen, camera, transform);
        }
    }

    private void valitsePelaajanKuvake() {
        try {
            peilaaX = false;
            peilaaY = false;
            switch (Pelaaja.keimonState) {
                case IDLE -> {
                    switch (Pelaaja.keimonTerveys) {
                        case ÜBER -> animaatiot.get("default").bind();
                        case HYVÄ -> {
                            switch (Pelaaja.keimonKylläisyys) {
                                case LAIHA -> {
                                    switch (Pelaaja.keimonSuuntaVasenOikea) {
                                        case VASEN: animaatiot.get("idle_hyvä_laiha").bind(); peilaaX = true; break;
                                        case OIKEA: animaatiot.get("idle_hyvä_laiha").bind(); break;
                                    }
                                }
                                case NORMAALI -> {
                                    switch (Pelaaja.keimonSuuntaVasenOikea) {
                                        case VASEN: animaatiot.get("idle_hyvä_normaali").bind(); peilaaX = true; break;
                                        case OIKEA: animaatiot.get("idle_hyvä_normaali").bind(); break;
                                    }
                                }
                                case LIHAVA -> {
                                    switch (Pelaaja.keimonSuuntaVasenOikea) {
                                        case VASEN: animaatiot.get("idle_hyvä_lihava").bind(); peilaaX = true; break;
                                        case OIKEA: animaatiot.get("idle_hyvä_lihava").bind(); break;
                                    }
                                }
                                case ERITTÄIN_LIHAVA -> {
                                    switch (Pelaaja.keimonSuuntaVasenOikea) {
                                        case VASEN: animaatiot.get("idle_hyvä_erittäinlihava").bind(); peilaaX = true; break;
                                        case OIKEA: animaatiot.get("idle_hyvä_erittäinlihava").bind(); break;
                                    }
                                }
                                case YLENSYÖNTI -> animaatiot.get("ylensyönti").bind();
                            }
                        }
                        case OK -> {
                            switch (Pelaaja.keimonKylläisyys) {
                                case LAIHA -> {
                                    switch (Pelaaja.keimonSuuntaVasenOikea) {
                                        case VASEN: animaatiot.get("idle_neutraali_laiha").bind(); peilaaX = true; break;
                                        case OIKEA: animaatiot.get("idle_neutraali_laiha").bind(); break;
                                    }
                                }
                                case NORMAALI -> {
                                    switch (Pelaaja.keimonSuuntaVasenOikea) {
                                        case VASEN: animaatiot.get("idle_neutraali_normaali").bind(); peilaaX = true; break;
                                        case OIKEA: animaatiot.get("idle_neutraali_normaali").bind(); break;
                                    }
                                }
                                case LIHAVA -> {
                                    switch (Pelaaja.keimonSuuntaVasenOikea) {
                                        case VASEN: animaatiot.get("idle_neutraali_lihava").bind(); peilaaX = true; break;
                                        case OIKEA: animaatiot.get("idle_neutraali_lihava").bind(); break;
                                    }
                                }
                                case ERITTÄIN_LIHAVA -> {
                                    switch (Pelaaja.keimonSuuntaVasenOikea) {
                                        case VASEN: animaatiot.get("idle_neutraali_erittäinlihava").bind(); peilaaX = true; break;
                                        case OIKEA: animaatiot.get("idle_neutraali_erittäinlihava").bind(); break;
                                    }
                                }
                                case YLENSYÖNTI -> animaatiot.get("ylensyönti").bind();
                            }
                        }
                        case HUONO -> {
                            switch (Pelaaja.keimonKylläisyys) {
                                case LAIHA -> {
                                    switch (Pelaaja.keimonSuuntaVasenOikea) {
                                        case VASEN: animaatiot.get("idle_huono_laiha").bind(); peilaaX = true; break;
                                        case OIKEA: animaatiot.get("idle_huono_laiha").bind(); break;
                                    }
                                }
                                case NORMAALI -> {
                                    switch (Pelaaja.keimonSuuntaVasenOikea) {
                                        case VASEN: animaatiot.get("idle_huono_normaali").bind(); peilaaX = true; break;
                                        case OIKEA: animaatiot.get("idle_huono_normaali").bind(); break;
                                    }
                                }
                                case LIHAVA -> {
                                    switch (Pelaaja.keimonSuuntaVasenOikea) {
                                        case VASEN: animaatiot.get("idle_huono_lihava").bind(); peilaaX = true; break;
                                        case OIKEA: animaatiot.get("idle_huono_lihava").bind(); break;
                                    }
                                }
                                case ERITTÄIN_LIHAVA -> {
                                    switch (Pelaaja.keimonSuuntaVasenOikea) {
                                        case VASEN: animaatiot.get("idle_huono_erittäinlihava").bind(); peilaaX = true; break;
                                        case OIKEA: animaatiot.get("idle_huono_erittäinlihava").bind(); break;
                                    }
                                }
                                case YLENSYÖNTI -> animaatiot.get("ylensyönti").bind();
                            }
                        }
                    }
                }
                case JUOKSU -> {
                    switch (Pelaaja.keimonSuunta) {
                        case VASEN -> {
                            switch (Pelaaja.keimonKylläisyys) {
                                case LAIHA: animaatiot.get("kävely_vasen_laiha").bind(); break;
                                case NORMAALI: animaatiot.get("kävely_vasen_normaali").bind(); break;
                                case LIHAVA: animaatiot.get("kävely_vasen_lihava").bind(); break;
                                case ERITTÄIN_LIHAVA: animaatiot.get("kävely_vasen_erittäinlihava").bind(); break;
                                case YLENSYÖNTI: animaatiot.get("ylensyönti").bind(); break;
                            }
                        }
                        case OIKEA -> {
                            switch (Pelaaja.keimonKylläisyys) {
                                case LAIHA: animaatiot.get("kävely_oikea_laiha").bind(); break;
                                case NORMAALI: animaatiot.get("kävely_oikea_normaali").bind(); break;
                                case LIHAVA: animaatiot.get("kävely_oikea_lihava").bind(); break;
                                case ERITTÄIN_LIHAVA: animaatiot.get("kävely_oikea_erittäinlihava").bind(); break;
                                case YLENSYÖNTI: animaatiot.get("ylensyönti").bind(); break;
                            }
                        }
                        case YLÖS -> {
                            switch (Pelaaja.keimonKylläisyys) {
                                case LAIHA: animaatiot.get("kävely_ylös_laiha").bind(); break;
                                case NORMAALI: animaatiot.get("kävely_ylös_normaali").bind(); break;
                                case LIHAVA: animaatiot.get("kävely_ylös_lihava").bind(); break;
                                case ERITTÄIN_LIHAVA: animaatiot.get("kävely_ylös_erittäinlihava").bind(); break;
                                case YLENSYÖNTI: animaatiot.get("ylensyönti").bind(); break;
                            }
                        }
                        case ALAS -> {
                            switch (Pelaaja.keimonKylläisyys) {
                                case LAIHA: animaatiot.get("kävely_alas_laiha").bind(); break;
                                case NORMAALI: animaatiot.get("kävely_alas_normaali").bind(); break;
                                case LIHAVA: animaatiot.get("kävely_alas_lihava").bind(); break;
                                case ERITTÄIN_LIHAVA: animaatiot.get("kävely_alas_erittäinlihava").bind(); break;
                                case YLENSYÖNTI: animaatiot.get("ylensyönti").bind(); break;
                            }
                        }
                    }
                }
                case null, default -> {

                }
            }
        }
        catch (NullPointerException npe) {
            animaatiot.get("default").bind();
        }
    }
}
