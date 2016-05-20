/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbkomunikacija;

import domen.OpstiDomenskiObjekat;
import domen.*;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static so.dodaj.SoAddUcesnika.odo;
import so.vrati.SoGetUcesnik;

/**
 *
 * @author Mladen
 */
public class DBBroker {
    private Connection connection;
    private Statement st;
    Util util;
    private static volatile DBBroker instance;
    private String porukaMetode;
    private String UrlBaze;
    
    private DBBroker(){
        try {
            util = new Util();
        } catch (IOException eio) {
            
        }
        
    }
    
    public static DBBroker getIstance(){
        if(instance == null){
            synchronized(DBBroker.class){
                instance = new DBBroker();
            }
        }
        return instance;
    }
    
    public void ucitajDriver() {
        try {
            Class.forName(util.getDriver());
            konstruisiUrlBaze();
        } catch (ClassNotFoundException ex) {
            porukaMetode = porukaMetode + "\nDriver nije pronađen: " + ex;                   
        }
    }
    
    private void konstruisiUrlBaze(){           
            //UrlBaze += util.getDriver();
            UrlBaze = new String();
            UrlBaze += util.getUrl();
            UrlBaze += util.getServerName();
            UrlBaze += util.getInstanceName();
            UrlBaze += util.getPortName();            
            UrlBaze += util.getDatabaseName();
            UrlBaze += util.getUser();
            UrlBaze += util.getPosswoard();
    }
           
    public boolean otvoriKonekciju() {
        try {
            //conection = DriverManager.getConnection(UrlBaze, UrlBaze, UrlBaze);
            connection = DriverManager.getConnection(UrlBaze);
            connection.setAutoCommit(false);
        } catch (SQLException ex) {
            porukaMetode = porukaMetode + "\nGreska kod konekcije: " + ex;                   
        } catch (SecurityException ex){
            porukaMetode = porukaMetode + "\nGreska zastite: " + ex;                   
            return false;
        }
        return true;
    
    }
    
    public boolean commitTransakcije(){
        try {
            connection.commit();
        } catch (SQLException esql) {
            porukaMetode = porukaMetode + "\nNije uspesno uradjen commit transakcije " + esql;
            return false;
        }
        porukaMetode = porukaMetode + "\nUspesno uradnjen commit transakcije ";
        return true;
    }
    
    public boolean rollbackTransakcije(){
        try {
            connection.commit();
        } catch (SQLException esql) {
            porukaMetode = porukaMetode + "\nNije uspesno uradjne rollack transakcije " + esql;
            return false;
        }
        porukaMetode = porukaMetode + "\nUspesno uradjen rollback transakcije";
        return true;
    }    
    
    public boolean nadjiSlogVratiGa(OpstiDomenskiObjekat odo){
        ResultSet rs;        
        int brojStavki;
        String upit;
        Statement st;
        try {
            st = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            /*
            upit = "SELECT *"+
                    " FROM " + odo.vratiImeKlase() +
                    " WHERE " + odo.vratiUslovZaNadjiSlog();
            */
                        
            GenerisiSqlUpit generisiSqlUpit = new GenerisiSqlUpit(odo ,"", null);
            upit = generisiSqlUpit.getUpit() + "\nAND "+ odo.vratiUslovZaNadjiSlog();
            //upit = odo.vratiVrednostZaSelect();
            System.out.println("Upit-nadji: " +upit);
            rs = st.executeQuery(upit);
            
            boolean signal = rs.next();
             if(signal ==false){
                 porukaMetode = porukaMetode + "\nNe postoji slog u bazi podataka.";
                 return false;
             }
  
        porukaMetode = porukaMetode + "\nUspesno je procitan slog iz bazepodataka.";
        
        if(odo.Napuni(rs, generisiSqlUpit.getMap(), null)){
            for (int i = 0; i < odo.vratiBrojVezanihObjekata(); i++) {
                OpstiDomenskiObjekat vezo = odo.vratiVezaniObjekat(i);
                if(vezo==null){
                    porukaMetode = porukaMetode + "\nNe postoji vezani objekat a navedeno je da postoji.";
                    return false;
                }
                else{
                    upit = "SELECT COUNT(*) as brojStavki" +
                            " FROM " + vezo.vratiImeKlase() +
                            " WHERE " + vezo.vratiUslovZaNadjiSlogove();
                    
                    rs = st.executeQuery(upit);
                    
                    if(rs.next() == false){
                        porukaMetode = porukaMetode + "\nNe postoje slogovi veynog objekata.";
                        return true;
                    }
                    brojStavki = rs.getInt("brojStavki");
                    odo.kreirajVezaniObjekat(brojStavki, i);
                    
                    upit = "SELECT * " +
                            " FROM " + vezo.vratiImeKlase() +
                            " WHERE " + vezo.vratiUslovZaNadjiSlogove();
                    rs = st.executeQuery(upit);
                    
                    int brojSloga = 0;
                    while(rs.next()){
                        odo.Napuni(rs, brojSloga, i);
                        brojSloga++;
                        porukaMetode = porukaMetode + "\n Uspesno su procitani slogovi veynog objekta";
                    }
                }
            }
        }        
        rs.close();
        st.close();
        } catch (Exception e) {
            porukaMetode = porukaMetode + "\nGreska kod citanja sloga iz baze podataka." +e;
            return false;
        } 
        return true;
    }
    
    public boolean nadjiSlogVrati(OpstiDomenskiObjekat odo, List<OpstiDomenskiObjekat> listOdo, String include, OpstiDomenskiObjekat filterOdo){
        ResultSet rs;
        ResultSet rsInner;        
        int brojStavki;
        int brojSlogova;
        String upit;
        Statement st;
        try {
            st = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                       
            GenerisiSqlUpit generisiSqlUpit = new GenerisiSqlUpit(odo ,include, filterOdo);
            upit = generisiSqlUpit.getUpit();
            
            rs = st.executeQuery(upit);
            
            System.out.println("Upit-nadji: " +upit);

            Class<?> c = Class.forName(odo.getClass().getName());
            Constructor<?> cons = c.getConstructor();
            
            
            while(rs.next()){    
                OpstiDomenskiObjekat object =  (OpstiDomenskiObjekat) cons.newInstance();
                if(object.Napuni(rs, generisiSqlUpit.getMap(), null)){

                    listOdo.add(object);
      
                } 
            }
             
  
             if(listOdo.size() == 0){
                 porukaMetode = porukaMetode + "\nNe postoji slog u bazi podataka.";
                 return false;
             }    
            
        rs.close();
        st.close();
        
        } catch (Exception e) {
            porukaMetode = porukaMetode + "\nGreska kod citanja sloga iz baze podataka." +e;
            return false;
        } 
        return true;
    }
    
    public boolean kreirajSlog(OpstiDomenskiObjekat odo){
    
        String upit;
        ResultSet rs;
        try {
            st = connection.createStatement();          
            upit = odo.vratiVrednostZaInsert();
            System.out.println(upit);
            st.executeUpdate(upit);
            
            upit = "SELECT Max("+odo.vratiAtributPretrazivanja()+") As Max" +
                    " FROM " + odo.vratiImeKlase();
            
            System.out.println(upit);
            rs = st.executeQuery(upit);
            if(rs.next()){
                odo.posaviVrednostZaAtributPretrazivanja(rs);
                //nadjiSlogVratiGa(odo);
            }
            
            st.close();
        } catch (Exception e) {
            porukaMetode = porukaMetode + "\nNe moze da se kreira novi slog: " + e;
        }
        porukaMetode = porukaMetode + "\nKreiran je novi slog: ";
        return true;
    }
    
    public boolean obrisiSlog(OpstiDomenskiObjekat odo){    
        String upit;
        int broj = 0;
        upit = "DELETE FROM " + odo.vratiImeKlase()+
                "\n WHERE " + odo.vratiUslovZaNadjiSlog();
        try {
            st = connection.createStatement();
            broj = st.executeUpdate(upit);            
        } catch (Exception e) {
            porukaMetode = porukaMetode + "\nNeuspelo brinaje sloga:" + e;
        }        
        if(broj == 0){
            return false;
        }
        return true;
    }
    
    public boolean promeniSlog(OpstiDomenskiObjekat odo){   
        String upit;
        int broj = 0;
        upit = odo.vratiVrednostZaUpdate();
        System.out.println(upit);
        try {
            st = connection.createStatement();
            broj = st.executeUpdate(upit);            
        } catch (Exception e) {
            porukaMetode = porukaMetode + "\nNeuspelo azuriran sloga:" + e;
        }        
        if(broj == 0){
            return false;
        }
        return true;        
    }
    
    public String vratiPorukuMetoe() {return porukaMetode;}
    
    public void isprazniPoruku(){porukaMetode = "";}
    
    public void dodajPorukuMetode(String poruka){
        porukaMetode = "\n" + poruka + porukaMetode;
    }
}
