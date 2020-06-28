package controller;

import com.google.gson.Gson;
import model.Data;
import model.Slusalice;
import spark.*;
import spark.template.handlebars.HandlebarsTemplateEngine;
import spark.utils.IOUtils;
import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import static spark.Spark.*;

public class Launcher {
    public static void main(String[] args) {
        staticFiles.location("/public");
        port(5000);
        String path = "slusalice.json";
        String pathforFiles = "src\\public\\images\\";
        File here = new File(".");
        pathforFiles = here.getAbsolutePath().substring(0,here.getAbsolutePath().length()-1) + pathforFiles;
        System.out.println(pathforFiles);
        HashMap<String, Object> polja = new HashMap<>();
        ArrayList<Slusalice> list = Data.readFromJson(path);
        Slusalice.incEAN = Slusalice.MaxEAN(list);
        System.out.println(Slusalice.incEAN);
        get("/",(request, response) -> {
            polja.put("slusalice",list);
            polja.put("filterMarke", Slusalice.izvuciMarke(list));
            polja.put("filterSistemi", Slusalice.izvuciSisteme(list));
            polja.put("filterCena", Slusalice.izvuciCenu(list));
            polja.put("filterOcena", 5);

            return new ModelAndView(polja,"index.hbs");
        }, new HandlebarsTemplateEngine());

        get("/dodaj",(request, response) -> {

            polja.put("atributi",Slusalice.attributiUStringDodaj());
            return new ModelAndView(polja,"azuriranje.hbs");
        }, new HandlebarsTemplateEngine());
        get("/slusalice/:ean",(request, response) -> {
            Slusalice temps = new Slusalice();

            for (Slusalice s: list)
            {
                String st = ""+s.getEan();
                if(st.compareTo(request.params(":ean")) == 0)
                {
                    System.out.println(st +":"+request.params(":ean"));
                    temps = s;
                }
            }
            polja.put("slikeSlusalica",temps.getSlike());
            polja.put("ean",temps.getEan());
            polja.put("atributivrednost",temps.SviAttributIVrednostiUJSON());
            return new ModelAndView(polja,"slusalica.hbs");
        }, new HandlebarsTemplateEngine());

        String finalPathforFiles = pathforFiles;
        path("/api/",()->{
            post("filtriraj", (request, response) -> {
                response.type("application/json");
                String marke = request.queryParams("marke");
                String sistemi = request.queryParams("sistemi");
                int ocena = Integer.parseInt(request.queryParams("ocena"));
                double cena = Double.parseDouble(request.queryParams("cena"));

                Gson gson = new Gson();

                return gson.toJson(Slusalice.Filtriraj(list,marke,sistemi,ocena,cena));
            });
            post("pretraga", (request, response) -> {
                response.type("application/json");
                String pretraga = request.queryParams("pretraga");
                Gson gson = new Gson();
                if (pretraga.equals("")) {
                    return gson.toJson(list);
                }
                else
                {
                    return gson.toJson(Slusalice.Pretraga(list,pretraga));
                }
            });
            post("obrisiSlusalicu",(request, response) -> {
                Long ean = Long.parseLong(request.queryParams("ean"));
                for (int i = 0 ; i< list.size();i++)
                {
                    if(list.get(i).getEan() == ean)
                    {
                        for (int j = 0;j< list.get(i).getSlike().size();j++)
                        {
                            File file = new File(finalPathforFiles+list.get(i).getSlike().get(j)+".png");
                            file.delete();
                        }
                        list.remove(i);
                        break;
                    }
                }
                Data.writeToJSON(list,path);

                return "Slusalice uspesno obrisane";
            });
            post("dodajSlusalice",(request, response) -> {
                ArrayList<String> listVrednosti = new ArrayList<>();
                MultipartConfigElement multipartConfigElement = new MultipartConfigElement("images");
                request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);


                request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("images"));
                Part uploadedFile = request.raw().getPart("file");

                for (String p: Slusalice.attributiUString())
                {
                    listVrednosti.add(p.replace(" ","")+":"+request.queryParams(p));
                }
                Slusalice temp = Slusalice.DodeliVrednosti(listVrednosti);
                try(InputStream inputStream = uploadedFile.getInputStream())
                {
                    String temp3 = temp.getEan() +"-"+temp.getSlike().size();
                    OutputStream outputStream = new FileOutputStream(finalPathforFiles + temp3+".png");
                    IOUtils.copy(inputStream,outputStream);
                    outputStream.close();
                    temp.getSlike().add(temp3);
                    System.out.println(temp3);
                }
                list.add(temp);
                Data.writeToJSON(list,"slusalice.json");

                return "Uspesno Dodato";
            });
            post("obrisiSliku",(request, response) -> {
                String s = request.queryParams("obrisi");
                for (Slusalice sl:list)
                {
                    if(sl.getEan() == Long.parseLong(s.split("-")[0]))
                    {
                        for (int i=0;i<sl.getSlike().size();i++)
                        {
                            if(sl.getSlike().get(i).equals(s))
                            {
                                sl.getSlike().remove(i);
                            }
                        }
                    }
                }
                String a = finalPathforFiles;
                File f = new File("" + finalPathforFiles + s +".png");
                System.out.println(f.getAbsolutePath());
                Data.writeToJSON(list,"slusalice.json");
                if(f.delete())
                {
                    return "Uspesno obrisano";
                }
                else
                {
                    return "Greska pri brisanju";
                }

            });
            post("azuriranjeSlika", (request, response) -> {
                MultipartConfigElement multipartConfigElement = new MultipartConfigElement("images");
                request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);

                request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("images"));
                Part uploadedFile = request.raw().getPart("slika");

                String temp = request.queryParams("eans").trim();
                Slusalice sl = new Slusalice();
                String tem = "";
                for (Slusalice s: list)
                {
                    if(s.getEan() == Long.parseLong(temp))
                    {
                        sl = s;
                    }
                }
                try(InputStream inputStream = uploadedFile.getInputStream())
                {
                    tem = ""+sl.getEan()+"-"+sl.getSlike().size();
                    OutputStream outputStream = new FileOutputStream(finalPathforFiles + tem.trim()+".png");
                    IOUtils.copy(inputStream,outputStream);
                    outputStream.close();
                    for (Slusalice s: list)
                    {
                        if(s.getEan() == sl.getEan())
                        {
                            s.getSlike().add(tem.trim());
                        }
                    }
                }
                Data.writeToJSON(list,"slusalice.json");

                return tem;
            });
            post("azuriranjeVrednosti",(request, response) -> {
                ArrayList<String> listVrednosti = new ArrayList<>();
                MultipartConfigElement multipartConfigElement = new MultipartConfigElement("images");
                request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);

                request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("images"));
                String ean = request.queryParams("ean");

                for (String p: Slusalice.attributiUString())
                {
                    listVrednosti.add(p.replace(" ","")+":"+request.queryParams(p));
                }
                for (int i = 0;i< list.size();i++)
                {
                    if (list.get(i).getEan() == Long.parseLong(ean))
                    {
                        list.get(i).DodeliVrednostiPostojecem(listVrednosti);
                    }
                }
                Data.writeToJSON(list,"slusalice.json");

                return "Uspesno azurirano";
            });


        });

    }
}
