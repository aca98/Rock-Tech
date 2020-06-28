package model;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ServiceLoader;
import java.util.zip.DeflaterOutputStream;

public class Slusalice {
    public static long incEAN;
    private ArrayList<String> slike;
    private String ime;
    private int popust;
    private double cena;
    private String marka;
    private int ocena;
    private String sistem;
    private String frekventniOdziv;
    private String impedansa;
    private String pozadinskoOsvetljenje;
    private String frekventniOdzivMikrofona;
    private String osetljivostMikrofona;
    private String kontrole;
    private String povezivanje;
    private long ean;

    public Slusalice(ArrayList<String> slike, String ime, int popust, double cena, String marka, int ocena, String sistem, String frekventniOdziv, String impedansa, String pozadinskoOsvetljenje, String frekventniOdzivMikrofona, String osetljivostMikrofona, String kontrole, String povezivanje, long ean) {
        this.slike = slike;
        this.ime = ime;
        this.popust = popust;
        this.cena = cena;
        this.marka = marka;
        this.ocena = ocena;
        this.sistem = sistem;
        this.frekventniOdziv = frekventniOdziv;
        this.impedansa = impedansa;
        this.pozadinskoOsvetljenje = pozadinskoOsvetljenje;
        this.frekventniOdzivMikrofona = frekventniOdzivMikrofona;
        this.osetljivostMikrofona = osetljivostMikrofona;
        this.kontrole = kontrole;
        this.povezivanje = povezivanje;
        this.ean = ean;
    }

    public Slusalice() {

        this.slike = new ArrayList<String>();
        this.ime = "";
        this.popust = 0;
        this.cena = 0;
        this.marka = "";
        this.ocena = 0;
        this.sistem = "";
        this.frekventniOdziv = "";
        this.impedansa = "";
        this.pozadinskoOsvetljenje = "";
        this.frekventniOdzivMikrofona = "";
        this.osetljivostMikrofona = "";
        this.kontrole = "";
        this.povezivanje = "";
        incEAN++;
        this.ean = incEAN;
    }


    public static long MaxEAN(ArrayList<Slusalice> list)
    {
        long max = 0;
        for(Slusalice s:list)
        {
            if(s.getEan() > max)
            {
                max= s.getEan();
            }
        }
        return max;
    }
    public static Slusalice DodeliVrednosti(ArrayList<String> list) throws IllegalAccessException {
        Slusalice temp = new Slusalice();
        for (Field f : temp.getClass().getDeclaredFields()) {
            for (String s : list) {
//                System.out.println(s);
                if (f.getName().toLowerCase().equals(s.split(":")[0].toLowerCase()) && s.split(":").length>1 && !f.getName().toLowerCase().equals("slike") && !f.getName().toLowerCase().equals("ean")) {
                    System.out.println(s);
                    f.setAccessible(true);
                    try {
                        Integer.parseInt(s.split(":")[1]);
                        f.set(temp, Integer.parseInt(s.split(":")[1]));
                    } catch (Exception e) {
                        try {
                            Double.parseDouble(s.split(":")[1]);
                            f.set(temp, Double.parseDouble(s.split(":")[1]));
                        } catch (Exception ex) {
                            try {
                                Long.parseLong(s.split(":")[1]);
                                f.set(temp, Long.parseLong(s.split(":")[1]));
                            } catch (Exception exp) {
                                f.set(temp, s.split(":")[1]);
                            }
                        }
                    }
                }
            }
        }
        System.out.println(temp);;
        return temp;
    }
    public void DodeliVrednostiPostojecem(ArrayList<String> list) throws IllegalAccessException {
        Slusalice temp = this;
        for (Field f : this.getClass().getDeclaredFields()) {
            for (String s : list) {
//                System.out.println(s);
                if (f.getName().toLowerCase().equals(s.split(":")[0].toLowerCase()) && s.split(":").length>1 && !f.getName().toLowerCase().equals("slike") && !f.getName().toLowerCase().equals("ean")) {
                    System.out.println(s);
                    f.setAccessible(true);
                    try {
                        Integer.parseInt(s.split(":")[1]);
                        f.set(this, Integer.parseInt(s.split(":")[1]));
                    } catch (Exception e) {
                        try {
                            Double.parseDouble(s.split(":")[1]);
                            f.set(this, Double.parseDouble(s.split(":")[1]));
                        } catch (Exception ex) {
                            try {
                                Long.parseLong(s.split(":")[1]);
                                f.set(this, Long.parseLong(s.split(":")[1]));
                            } catch (Exception exp) {
                                f.set(this, s.split(":")[1]);
                            }
                        }
                    }
                }
            }
        }
    }
    public static ArrayList<String> attributiUString() {
        int br = 0;
        Field[] fields = Slusalice.class.getDeclaredFields();
        ArrayList<String> imena = new ArrayList<>();
        String s1 = "";
        String s2 = "";
        int t = 0;
        for (Field f : fields) {
            br++;
            if(br >2) {
                imena.add(RazdvojIKapitalizuj(f.getName()));

            }
        }
        for (int i = 0; i < imena.size(); i++) {
            imena.set(i, imena.get(i).substring(0, 1).toUpperCase() + imena.get(i).substring(1).toLowerCase());
        }
        return imena;
    }
    public static ArrayList<String> attributiUStringDodaj() {
        int br = 0;
        Field[] fields = Slusalice.class.getDeclaredFields();
        ArrayList<String> imena = new ArrayList<>();
        String s1 = "";
        String s2 = "";
        int t = 0;
        for (Field f : fields) {
            br++;
            if(br >2 && !f.getName().toLowerCase().contains("ean")) {
                imena.add(RazdvojIKapitalizuj(f.getName()));
            }
        }
        for (int i = 0; i < imena.size(); i++) {
            imena.set(i, imena.get(i).substring(0, 1).toUpperCase() + imena.get(i).substring(1).toLowerCase());
        }
        return imena;
    }
    public AtributIVrednost AttributIVrednostUJSON(Field field) {
        AtributIVrednost v = new AtributIVrednost();
        Field[] fields = Slusalice.class.getDeclaredFields();
        ArrayList<String> lst = new ArrayList<>();
        try{
            if(field.getName().equals("slike"))
            {
                String[] st = field.get(this).toString().replace("[","").replace("]","").split(",");

                for(String p:st)
                {
                    System.out.println(p);
                    lst.add(p.trim());
                }
                v= new AtributIVrednost(RazdvojIKapitalizuj(field.getName()),field.get(this).toString(),lst);
            }
            else
            {
                v= new AtributIVrednost(RazdvojIKapitalizuj(field.getName()),field.get(this).toString(), null);
            }

        }catch (Exception e)
        {
        }

        return v;
    }
    public ArrayList<AtributIVrednost> SviAttributIVrednostiUJSON()
    {
       ArrayList<AtributIVrednost>temp = new ArrayList<>();
        Field[] fields = Slusalice.class.getDeclaredFields();
        for (Field f : fields) {
            if(!f.getName().contains("inc") && !f.getName().toLowerCase().contains("ean") && !f.getName().contains("slike"))
            {
                temp.add(AttributIVrednostUJSON(f));
            }
        }
        return temp;
    }

    public static String RazdvojIKapitalizuj(String string)
    { String s2 = "";
        if(!string.equals("")) {

//            System.out.println("test:"+s2);


            int t = 0;
            for (int i = 0; i < string.length(); i++) {
                if (Character.isUpperCase(string.charAt(i))) {
                    s2 += string.substring(t, i) + " ";
                    t = i;
                }
            }
            s2 += string.substring(t, string.length()) + " ";
            t = 0;
//            System.out.println("test2:"+s2);
            s2 = s2.substring(0, 1).toUpperCase() + s2.substring(1).toLowerCase();
        }
        return s2;
    }

    public static ArrayList<String> izvuciMarke(ArrayList<Slusalice> sl)
    {
        ArrayList<String> list = new ArrayList<>();
        for (Slusalice t:sl)
        {
            if(!list.contains(t.getMarka()))
            {
                list.add(t.getMarka());
            }
        }
        return list;
    }
    public static ArrayList<String> izvuciSisteme(ArrayList<Slusalice> sl)
    {
        ArrayList<String> list = new ArrayList<>();
        for (Slusalice t:sl)
        {
            if(!list.contains(t.getSistem()))
            {
                list.add(t.getSistem());
            }
        }
        return list;
    }
    public static double izvuciCenu(ArrayList<Slusalice> sl)
    {
        double max = 0;
        ArrayList<String> list = new ArrayList<>();
        for (Slusalice t:sl)
        {
            if(t.getCena() > max)
            {
                max = t.getCena();
            }
        }
        return max;
    }


    public static ArrayList<Slusalice> Filtriraj(ArrayList<Slusalice> lista,String marke,String sistemi,int ocena,double cena)
    {
        ArrayList<Slusalice> temp = new ArrayList<Slusalice>();
        boolean flagMarke = false;
        boolean flagSistemi = false;
        boolean flagOcena = false;
        boolean flagCena = false;
        for (Slusalice sl:lista)
        {
            if (!marke.equals(""))
            {
                if(marke.contains(sl.getMarka()))
                {
                    flagMarke = true;
                }
            }
            else
            {
                flagMarke = true;
            }
            if(!sistemi.equals(""))
            {
                if(sistemi.contains(sl.getSistem()))
                {
                    flagSistemi = true;
                }
            }
            else
            {
                flagSistemi = true;
            }
            if(ocena > -1)
            {
                if(sl.getOcena() >= ocena)
                {
                    flagOcena = true;
                }
            }
            else
            {
                flagOcena = true;
            }
            if(cena >-1)
            {
                if(sl.getCena() >= cena)
                {
                    flagCena = true;
                }
            }
            else
            {
                flagCena = true;
            }

            if(flagMarke && flagSistemi && flagOcena && flagCena)
            {
                temp.add(sl);
            }
            flagMarke = false;
            flagSistemi = false;
            flagOcena = false;
            flagCena = false;
        }
        return temp;
    }

    public static ArrayList<Slusalice> Pretraga(ArrayList<Slusalice> lista, String trazena)
    {
        ArrayList<Slusalice> temp = new ArrayList<>();
        for (Slusalice sl:lista)
        {
            if(sl.toString().toLowerCase().contains(trazena.toLowerCase()))
            {
                temp.add(sl);
            }
        }
        return temp;
    }
    public ArrayList<String> getSlike() {
        return slike;
    }

    public void setSlike(ArrayList<String> slike) {
        this.slike = slike;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public int getPopust() {
        return popust;
    }

    public void setPopust(int popust) {
        this.popust = popust;
    }

    public double getCena() {
        return cena;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }

    public String getMarka() {
        return marka;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public int getOcena() {
        return ocena;
    }

    public void setOcena(int ocena) {
        this.ocena = ocena;
    }

    public String getSistem() {
        return sistem;
    }

    public void setSistem(String sistem) {
        this.sistem = sistem;
    }

    public String getFrekventniodziv() {
        return frekventniOdziv;
    }

    public void setFrekventniodziv(String frekventniodziv) {
        this.frekventniOdziv = frekventniodziv;
    }

    public String getImpedansa() {
        return impedansa;
    }

    public void setImpedansa(String impedansa) {
        this.impedansa = impedansa;
    }

    public String getPozadinskoOsvetljenje() {
        return pozadinskoOsvetljenje;
    }

    public void setPozadinskoOsvetljenje(String pozadinskoOsvetljenje) {
        this.pozadinskoOsvetljenje = pozadinskoOsvetljenje;
    }


    public String getFrekventniOdzivMikrofona() {
        return frekventniOdzivMikrofona;
    }

    public void setFrekventniOdzivMikrofona(String frekventniOdzivMikrofona) {
        this.frekventniOdzivMikrofona = frekventniOdzivMikrofona;
    }

    public String getOsetljivostMikrofona() {
        return osetljivostMikrofona;
    }

    public void setOsetljivostMikrofona(String osetljivostMikrofona) {
        this.osetljivostMikrofona = osetljivostMikrofona;
    }

    public String getKontrole() {
        return kontrole;
    }

    public void setKontrole(String kontrole) {
        this.kontrole = kontrole;
    }

    public String getPovezivanje() {
        return povezivanje;
    }

    public void setPovezivanje(String povezivanje) {
        this.povezivanje = povezivanje;
    }

    public long getEan() {
        return ean;
    }

    public void setEan(long ean) {
        this.ean = ean;
    }
    public String toStringNoJson() {
        return ""+ime +" "+ popust+" "+cena+" "+ marka +" "+ocena +" "+sistem +" "+frekventniOdziv +" "+impedansa +" "+pozadinskoOsvetljenje +" "+frekventniOdzivMikrofona +" "+osetljivostMikrofona +" "+kontrole +" "+povezivanje +" "+ean;
    }
    @Override
    public String toString() {
        return "Slusalice{" +
                "slike=" + slike +
                ", ime='" + ime + '\'' +
                ", popust=" + popust +
                ", cena=" + cena +
                ", marka='" + marka + '\'' +
                ", ocena=" + ocena +
                ", sistem='" + sistem + '\'' +
                ", frekventniOdziv='" + frekventniOdziv + '\'' +
                ", impedansa='" + impedansa + '\'' +
                ", pozadinskoOsvetljenje='" + pozadinskoOsvetljenje + '\'' +
                ", frekventniOdzivMikrofona='" + frekventniOdzivMikrofona + '\'' +
                ", osetljivostMikrofona='" + osetljivostMikrofona + '\'' +
                ", kontrole='" + kontrole + '\'' +
                ", povezivanje='" + povezivanje + '\'' +
                ", ean=" + ean +
                '}';
    }
}
