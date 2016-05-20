/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package so.vrati.list;


import domen.OpstiDomenskiObjekat;
import java.util.List;
import so.OpstaSO;
import static so.OpstaSO.signal;
import static so.vrati.SoGetUcesnik.listOdo;

/**
 *
 * @author Mladen
 */
public class SoGetListaUcesnik extends OpstaSO {

    public static List<OpstiDomenskiObjekat> listodo;
    
    public static String SoGetListaUcesnik(OpstiDomenskiObjekat odo, List<OpstiDomenskiObjekat> listOdo){
        SoGetListaUcesnik soGetListaUcesnik = new SoGetListaUcesnik();
        soGetListaUcesnik.transakcija = false;
        soGetListaUcesnik.listodo = listOdo;
        return soGetListaUcesnik.opsteIzvrsavanjeSo(odo, soGetListaUcesnik);
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
