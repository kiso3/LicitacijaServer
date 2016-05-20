/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package so;

import dbkomunikacija.DBBroker;
import domen.OpstiDomenskiObjekat;

/**
 *
 * @author Mladen
 */
public abstract class OpstaSO {
    public DBBroker DBB;
    public static boolean signal;
    public boolean bazaOtvorena = false;
    public static boolean transakcija = false;
    
    public synchronized static String opsteIzvrsavanjeSo(OpstiDomenskiObjekat odo, OpstaSO oso){
        if(!oso.otvoriBazu()) return oso.vratiPorukuMetode();
        
        if(!oso.proveriPreduslovSo(odo) ) return oso.vratiPorukuMetode();
        
        if(!oso.izvrsavanjeSo(odo) && transakcija){
            signal = oso.rollbackTransakcije();
            return oso.vratiPorukuMetode();
        }
        
        if(transakcija){
            signal = oso.commitTransakcije();
        }        
        return oso.vratiPorukuMetode();
    }
            
    boolean otvoriBazu(){
    if(bazaOtvorena == false){
        DBB = DBBroker.getIstance();
        DBB.isprazniPoruku();
        DBB.ucitajDriver();
        signal = DBB.otvoriKonekciju();
        if(!signal) return false;
        }
    DBB.isprazniPoruku();
    bazaOtvorena = true;
    return true;
    }
    
    String vratiPorukuMetode(){
        System.out.println(DBB.vratiPorukuMetoe());
        return DBB.vratiPorukuMetoe();
    }
    
    boolean rollbackTransakcije(){
        return DBB.rollbackTransakcije();
    }
    
    boolean commitTransakcije(){
        return DBB.commitTransakcije();
    }
    
    public abstract boolean izvrsavanjeSo(OpstiDomenskiObjekat odo);
    
    public abstract boolean proveriPreduslovSo(OpstiDomenskiObjekat odo);
}
