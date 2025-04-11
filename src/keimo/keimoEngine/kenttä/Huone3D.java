package keimo.keimoEngine.kenttä;

import keimo.keimoEngine.grafiikat.objekti3d.Model3D;

import java.util.ArrayList;

public class Huone3D {
    
    private int id;
    private String nimi;
    private Model3D huoneenModel;
    private ArrayList<Model3D> huoneenObjektit;

    public Huone3D(int luontiId, String luontiNimi, Model3D luontiModel, ArrayList<Model3D> luontiObjektit) {
        this.id = luontiId;
        this.nimi = luontiNimi;
        this.huoneenModel = luontiModel;
        this.huoneenObjektit = luontiObjektit;
    }

    public int annaId() {
        return id;
    }

    public String annaNimi() {
        return nimi;
    }

    public Model3D annaHuoneenModel() {
        return huoneenModel;
    }

    public ArrayList<Model3D> annaHuoneenObjektit() {
        return huoneenObjektit;
    }
}
