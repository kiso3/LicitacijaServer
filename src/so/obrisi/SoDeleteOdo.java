/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package so.obrisi;

import domen.OpstiDomenskiObjekat;
import so.OpstaSO;

/**
 *
 * @author Mladen
 */
public class SoDeleteOdo extends OpstaSO{
    
    public static String SoObrisiUcesnika(OpstiDomenskiObjekat odo){
        SoDeleteOdo soObrisi = new SoDeleteOdo();
        soObrisi.transakcija = true;        
        return soObrisi.opsteIzvrsavanjeSo(odo, soObrisi);
    }
    
    
    @Override
    public boolean izvrsavanjeSo(OpstiDomenskiObjekat odo) {
        signal = DBB.obrisiSlog(odo);
        if(!signal){
            DBB.dodajPorukuMetode("Sisitem ne moze da obrise " + odo.vratiNazivObjkta() + ".");
            return false;
        }
       DBB.dodajPorukuMetode(" Sistem je obrisao " + odo.vratiNazivObjkta() + ".");
       return true;
    }

    @Override
    public boolean proveriPreduslovSo(OpstiDomenskiObjekat odo) {
        return true;
    }
    
}
