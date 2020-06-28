package model;

import java.util.ArrayList;

public class AtributIVrednost {
    private String atribut;
    private String vrednost;
    private ArrayList<String> slike;


    public AtributIVrednost(String atribut, String vrednost, ArrayList<String> slike) {
        this.atribut = atribut;
        this.vrednost = vrednost;
        this.slike = slike;
    }

    public AtributIVrednost() {
        this.atribut = "";
        this.vrednost = "";
        slike = new ArrayList<>();

    }

    public ArrayList<String> getSlike() {
        return slike;
    }

    public void setSlike(ArrayList<String> slike) {
        this.slike = slike;
    }

    public String getAtribut() {
        return atribut;
    }

    public void setAtribut(String atribut) {
        this.atribut = atribut;
    }

    public String getVrednost() {
        return vrednost;
    }

    public void setVrednost(String vrednost) {
        this.vrednost = vrednost;
    }

    @Override
    public String toString() {
        return "AtributIVrednost{" +
                "atribut='" + atribut + '\'' +
                ", vrednost='" + vrednost + '\'' +
                '}';
    }
}
