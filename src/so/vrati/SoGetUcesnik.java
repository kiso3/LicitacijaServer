/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package so.vrati;

import domen.OpstiDomenskiObjekat;
import java.util.List;
import so.OpstaSO;

/**
 *
 * @author Mladen
 */
public class SoGetUcesnik extends OpstaSO{

    public static List<OpstiDomenskiObjekat> listOdo;
    
    public static String SoVratiUcesnik(OpstiDomenskiObjekat odo, List<OpstiDomenskiObjekat> listOdo) {
        SoGetUcesnik soVrati = new SoGetUcesnik();
        soVrati.transakcija = false;
        SoGetUcesnik.listOdo = listOdo;
        return soVrati.opsteIzvrsavanjeSo(odo, soVrati);
    }

    
    
    @Override
    public boolean izvrsavanjeSo(OpstiDomenskiObjekat odo) {
        signal = DBB.nadjiSlogVrati(odo, listOdo, "Mesto.Drzava.Status.TipUcesnika", null);
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
