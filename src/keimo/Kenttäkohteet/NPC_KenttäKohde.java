package keimo.Kenttäkohteet;

import keimo.PääIkkuna;

public abstract class NPC_KenttäKohde extends KenttäKohde{
    
    protected int hp = 10;
    
    public String kokeileEsinettä(Esine e) {
        System.out.println("Mitään ei tapahtunut!");
        return "Mitään ei tapahtunut!";
    }

    public Esine suoritaMuutoksetEsineelle(Esine e) {
        PääIkkuna.hudTeksti.setText(e.annaNimiSijamuodossa("partitiivi") + " ei voi käyttää " + this.annaNimiSijamuodossa("illatiivi"));
        return e;
    }

    public String annaNimi(){
        return nimi;
    }

    public NPC_KenttäKohde(boolean määritettySijainti, int sijX, int sijY) {
        super(määritettySijainti, sijX, sijY);
    }
}
