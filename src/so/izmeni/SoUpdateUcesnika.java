/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package so.izmeni;

import domen.OpstiDomenskiObjekat;
import domen.Ucesnik;
import domen.UcesnikFizicko;
import domen.UcesnikPravno;
import java.util.logging.Level;
import java.util.logging.Logger;
import so.OpstaSO;
import so.dodaj.SoAddUcesnika;
import validator.ucensik.ValidatorAdresa;
import validator.ucensik.ValidatorEmail;
import validator.ucensik.ValidatorIme;
import validator.ucensik.ValidatorImeRoditelja;
import validator.ucensik.ValidatorMb;
import validator.ucensik.ValidatorNaziv;
import validator.ucensik.ValidatorPib;
import validator.ucensik.ValidatorPrezime;
import validator.ucensik.ValidatorTelefon;

/**
 *
 * @author Mladen
 */
public class SoUpdateUcesnika  extends OpstaSO{

     public static String SoIzmeniUcesnika(OpstiDomenskiObjekat odo){
        SoUpdateUcesnika soIzmeni = new SoUpdateUcesnika();
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
        Ucesnik u = (Ucesnik)odo;
        
        ValidatorAdresa validatorAdresa = new ValidatorAdresa();
        ValidatorEmail validatorEmail = new ValidatorEmail();
        ValidatorTelefon validatorTelefon = new ValidatorTelefon();
        
        try {
            validatorAdresa.validate(u.getAdresa());
            validatorEmail.validate(u.getEmail());
            validatorTelefon.validate(u.getTelefon());
        } catch (Exception ex) {
            Logger.getLogger(SoAddUcesnika.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        if(u instanceof UcesnikFizicko){
            UcesnikFizicko uf = (UcesnikFizicko) u;
            
            ValidatorIme validatorIme = new ValidatorIme();
            ValidatorImeRoditelja validatorImeRoditelja = new ValidatorImeRoditelja();
            ValidatorPrezime validatorPrezime = new ValidatorPrezime();
            try {
                validatorIme.validate(uf.getIme());
                validatorImeRoditelja.validate(uf.getImeRoditelja());
                validatorPrezime.validate(uf.getPrezime());
            } catch (Exception ex) {
                Logger.getLogger(SoAddUcesnika.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }else{
            UcesnikPravno up = (UcesnikPravno) u;
            
            ValidatorMb validatorMb = new ValidatorMb();
            ValidatorNaziv validatorNaziv = new ValidatorNaziv();
            ValidatorPib validatorPib = new ValidatorPib();
            
            try {
                validatorMb.validate(up.getMb());
                validatorNaziv.validate(up.getNaziv());
                validatorPib.validate(up.getPib());
            } catch (Exception ex) {
                Logger.getLogger(SoAddUcesnika.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        
        }
        
        return true;
    }
    
    
}
