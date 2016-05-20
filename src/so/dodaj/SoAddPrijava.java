/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package so.dodaj;

import domen.OpstiDomenskiObjekat;
import so.OpstaSO;

/**
 *
 * @author Mladen
 */
public class SoAddPrijava extends OpstaSO{
    
    public static OpstiDomenskiObjekat odo;    
     
    public static String SoDodajPrijava(OpstiDomenskiObjekat odo){
        SoAddPrijava soDodaj = new SoAddPrijava();
        soDodaj.transakcija = true;        
        return soDodaj.opsteIzvrsavanjeSo(odo, soDodaj);
    }
     
    @Override
    public boolean izvrsavanjeSo(OpstiDomenskiObjekat odo) {
        if(!proveriPreduslovSo(odo)){
            DBB.dodajPorukuMetode("Sisitem ne moze da zapamti " + odo.vratiNazivObjkta() + ".");
            return false;
        }
        
        signal = DBB.kreirajSlog(odo);
        if(!signal){
            DBB.dodajPorukuMetode("Sisitem ne moze da zapamti " + odo.vratiNazivObjkta() + ".");
            return false;
        }
       DBB.dodajPorukuMetode(" Sistem je zapamtio " + odo.vratiNazivObjkta() + ".");
       return true;
    }

    @Override
    public boolean proveriPreduslovSo(OpstiDomenskiObjekat odo) {               
        return true;
    }
    
}
