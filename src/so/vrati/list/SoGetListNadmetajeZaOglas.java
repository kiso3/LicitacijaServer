/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package so.vrati.list;

import domen.OpstiDomenskiObjekat;
import so.OpstaSO;
import static so.OpstaSO.signal;

/**
 *
 * @author Mladen
 */
public class SoGetListNadmetajeZaOglas extends OpstaSO{
                        
    public static String SoGetListNadmetajeZaOglas(OpstiDomenskiObjekat odo) {
        SoGetListNadmetajeZaOglas soGetListNadmetajeZaOglas = new SoGetListNadmetajeZaOglas();
        soGetListNadmetajeZaOglas.transakcija = false;        
        return soGetListNadmetajeZaOglas.opsteIzvrsavanjeSo(odo, soGetListNadmetajeZaOglas);
    }

        
    @Override
    public boolean izvrsavanjeSo(OpstiDomenskiObjekat odo) {
        
        signal = DBB.nadjiSlogVratiGa(odo);
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
