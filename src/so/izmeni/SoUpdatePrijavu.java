/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package so.izmeni;

import domen.OpstiDomenskiObjekat;
import so.OpstaSO;

/**
 *
 * @author Mladen
 */
public class SoUpdatePrijavu extends OpstaSO{

    public static String SoIzmeniPrijavu(OpstiDomenskiObjekat odo){
        SoUpdatePrijavu soIzmeni = new SoUpdatePrijavu();
        soIzmeni.transakcija = true;        
        return soIzmeni.opsteIzvrsavanjeSo(odo, soIzmeni);
    }
     
    @Override
    public boolean izvrsavanjeSo(OpstiDomenskiObjekat odo) {
        
        if(!proveriPreduslovSo(odo)){
            DBB.dodajPorukuMetode("Sisitem ne moze da zapamti " + odo.vratiNazivObjkta() + ".");
            return false;
        }
                
        signal = DBB.promeniSlog(odo);
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
