/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package so.vrati.list;

import domen.OpstiDomenskiObjekat;
import domen.PrijavaFizicko;
import domen.PrijavaPravno;
import domen.UcesnikFizicko;
import domen.UcesnikPravno;
import java.util.ArrayList;
import java.util.List;
import so.OpstaSO;
import static so.OpstaSO.signal;


/**
 *
 * @author Mladen
 */
public class SOGetListUcesnikaZaNadmetanje extends OpstaSO {
    public static List<OpstiDomenskiObjekat> listodo;
    
    public static String SOGetListUcesnikaZaNadmetanje(OpstiDomenskiObjekat odo, List<OpstiDomenskiObjekat> listOdo) {
        SOGetListUcesnikaZaNadmetanje soGetListUcesnikaZaNadmetanje = new SOGetListUcesnikaZaNadmetanje();
        soGetListUcesnikaZaNadmetanje.transakcija = false;
        soGetListUcesnikaZaNadmetanje.listodo = listOdo;        
        return soGetListUcesnikaZaNadmetanje.opsteIzvrsavanjeSo(odo, soGetListUcesnikaZaNadmetanje);
    }

        
    @Override
    public boolean izvrsavanjeSo(OpstiDomenskiObjekat odo) {
        List<OpstiDomenskiObjekat> listPf = new ArrayList<OpstiDomenskiObjekat>();
        List<OpstiDomenskiObjekat> listPp = new ArrayList<OpstiDomenskiObjekat>();        
        
        PrijavaFizicko pf = new PrijavaFizicko();
        PrijavaPravno pp = new PrijavaPravno();
        signal = DBB.nadjiSlogVrati((OpstiDomenskiObjekat)pf, listPf, "Mesto.Nadmetanje.UcesnikFizicko",odo);
        signal = DBB.nadjiSlogVrati((OpstiDomenskiObjekat)pp, listPp, "Mesto.Nadmetanje.UcesnikPravno", odo);
        
        UcesnikFizicko uf = new UcesnikFizicko();
        UcesnikPravno up = new UcesnikPravno();
        signal = DBB.nadjiSlogVrati((OpstiDomenskiObjekat)uf, listodo, "Mesto.Nadmetanje.UcesnikPravno", null);        
        signal = DBB.nadjiSlogVrati((OpstiDomenskiObjekat)up, listodo, "Mesto.Nadmetanje.UcesnikPravno", null);
       
       List<OpstiDomenskiObjekat> removeElement = new ArrayList<OpstiDomenskiObjekat>();
        
        listodo.forEach(x -> {if(x instanceof UcesnikFizicko){
                                   listPf.forEach(f -> {
                                       if(((PrijavaFizicko)f).getPonudjacFizicko().getUcesnikId()==((UcesnikFizicko) x).getUcesnikId()){
                                        removeElement.add(x);
                                      return;
                                   }                                   
                                   });
                                }if(x instanceof UcesnikPravno){
                                   listPp.forEach(p -> {
                                       if(((PrijavaPravno)p).getUcesnikPravno().getUcesnikId()==((UcesnikPravno) x).getUcesnikId()){
                                        removeElement.add(x);
                                      return;
                                   }                                   
                                }); 
                            }
       
        });
        removeElement.forEach(x -> {
            listodo.remove(x);
        });

        if(!signal){
            DBB.dodajPorukuMetode("Sisitem ne moze da nadje " + odo.vratiNazivObjkta() + " po zadatoj vrednosti.");
            return false;
        }
       DBB.dodajPorukuMetode(" Sistem je nasao " + odo.vratiNazivObjkta() + " po zadatoj vrednosi");
       return true;
    }

    @Override
    public boolean proveriPreduslovSo(OpstiDomenskiObjekat odo) {
       return true;
    }
}
