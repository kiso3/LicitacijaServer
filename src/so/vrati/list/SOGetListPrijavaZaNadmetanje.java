/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package so.vrati.list;

import domen.OpstiDomenskiObjekat;
import domen.PrijavaFizicko;
import domen.PrijavaPravno;
import java.util.List;
import so.OpstaSO;
import static so.OpstaSO.signal;


/**
 *
 * @author Mladen
 */
public class SOGetListPrijavaZaNadmetanje extends OpstaSO{

    public static List<OpstiDomenskiObjekat> listodo;
    
    public static String SOGetListPrijafaZaNadmetanje(OpstiDomenskiObjekat odo, List<OpstiDomenskiObjekat> listOdo) {
        SOGetListPrijavaZaNadmetanje soGetListPrijafaZaNadmetanje = new SOGetListPrijavaZaNadmetanje();
        soGetListPrijafaZaNadmetanje.transakcija = false;
        soGetListPrijafaZaNadmetanje.listodo = listOdo;        
        return soGetListPrijafaZaNadmetanje.opsteIzvrsavanjeSo(odo, soGetListPrijafaZaNadmetanje);
    }

        
    @Override
    public boolean izvrsavanjeSo(OpstiDomenskiObjekat odo) {
        PrijavaFizicko pf = new PrijavaFizicko();
        PrijavaPravno pp = new PrijavaPravno();
        signal = DBB.nadjiSlogVrati((OpstiDomenskiObjekat)pf, listodo, "Mesto.Nadmetanje.UcesnikFizicko",odo);
        signal = DBB.nadjiSlogVrati((OpstiDomenskiObjekat)pp, listodo, "Mesto.Nadmetanje.UcesnikPravno", odo);
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
