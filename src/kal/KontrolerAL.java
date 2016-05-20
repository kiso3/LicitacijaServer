/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kal;

import domen.OpstiDomenskiObjekat;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import so.dodaj.SoAddPrijava;
import so.dodaj.SoAddUcesnika;
import so.izmeni.SoUpdatePrijavu;
import so.izmeni.SoUpdateUcesnika;
import so.obrisi.SoDeleteOdo;
import so.vrati.SoGetUcesnik;
import so.vrati.list.SOGetListPrijavaZaNadmetanje;
import so.vrati.list.SOGetListUcesnikaZaNadmetanje;
import so.vrati.list.SoGetListNadmetajeZaOglas;
import so.vrati.list.SoGetListOglas;
import util.Konstante;
import util.TransferObjekat;

/**
 *
 * @author Mladen
 */
public class KontrolerAL {
    
    static ServerSocket ss;
    static Klijent kl[];
    
    public static void main(String[] args) throws IOException {
        kl = new Klijent[10];
        ss = new ServerSocket(1500);
        
        System.out.println("Podignut je serverski program:");
        for (int brojKlijenta = 0; brojKlijenta < 10; brojKlijenta++) {
            Socket socketS = ss.accept();
            System.out.println("Klijent "+ (brojKlijenta+1));
            kl[brojKlijenta] = new Klijent(socketS, brojKlijenta+1);
        }
    }
    
}

class Klijent extends Thread{

    private Socket soketS;
    int brojKlijenta;
    ObjectOutputStream out;
    ObjectInputStream in;
    
    public Klijent(Socket soketS1, int brojKlijenta) {
        soketS = soketS1;
        this.brojKlijenta = brojKlijenta;
        System.out.println("Konstruktor");
        start();
    }

    
    @Override
    public void run() {        
        try {
            String poruka = "";
            out = new ObjectOutputStream(soketS.getOutputStream());
            in = new ObjectInputStream(soketS.getInputStream());
            OpstiDomenskiObjekat odo;
            System.out.println("run");
            
            while(true){

                TransferObjekat DTO = (TransferObjekat) in.readObject();
                List<OpstiDomenskiObjekat> listOdo;
                switch (DTO.getOperacija()){
                    case Konstante.GET_LIST_MESTA:
                        listOdo = new ArrayList<OpstiDomenskiObjekat>();
                        poruka = SoGetUcesnik.SoVratiUcesnik((OpstiDomenskiObjekat) DTO.getParametar(), listOdo);
                        DTO.setPoruka(poruka);
                        
                        if(SoGetUcesnik.signal){
                            DTO.setOdgovor(listOdo);
                            DTO.setSignal(Konstante.GET_LIST_MESTA_OK);
                        }else{                        
                            DTO.setSignal(Konstante.GET_LIST_MESTA_ERROR);
                        }                                                
                    break;
                    case Konstante.ADD_UCESNIK:
                        odo = (OpstiDomenskiObjekat) DTO.getParametar();
                        poruka= SoAddUcesnika.SoDodajUcesnika(odo);   
                        DTO.setSignal(poruka);
                        
                        if(SoGetUcesnik.signal){
                            DTO.setOdgovor(odo);                        
                            DTO.setSignal(Konstante.ADD_UCESNIK_OK);
                        }else{                        
                            DTO.setSignal(Konstante.ADD_UCESNIK_ERROR);
                        }                                                
                    break;
                    case Konstante.GET_LISTA_UCESNIKA:
                        listOdo = new ArrayList<OpstiDomenskiObjekat>();
                        poruka = SoGetUcesnik.SoVratiUcesnik((OpstiDomenskiObjekat) DTO.getParametar(),listOdo);
                        DTO.setPoruka(poruka);
                        
                        if(SoGetUcesnik.signal){
                            DTO.setOdgovor(listOdo);                        
                            DTO.setSignal(Konstante.GET_LISTA_UCESNIKA_OK);
                        }else{                        
                            DTO.setSignal(Konstante.GET_LISTA_UCESNIKA_ERROR);
                        }                                                                        
                    break;
                    case Konstante.DELETE_UCESNIK:
                        poruka = SoDeleteOdo.SoObrisiUcesnika((OpstiDomenskiObjekat) DTO.getParametar());                        
                        DTO.setPoruka(poruka);
                        
                        if(SoGetUcesnik.signal){                                
                            DTO.setSignal(Konstante.DELETE_UCESNIK_OK); 
                        }else{                        
                            DTO.setSignal(Konstante.DELETE_UCESNIK_ERROR);
                        }                        
                    break;
                    case Konstante.EDIT_UCESNIK:        
                         odo = (OpstiDomenskiObjekat) DTO.getParametar();
                        poruka = SoUpdateUcesnika.SoIzmeniUcesnika(odo);   
                        DTO.setPoruka(poruka);
                        
                        if(SoGetUcesnik.signal){
                            DTO.setOdgovor(odo);
                            DTO.setSignal(Konstante.EDIT_UCESNIK_OK); 
                        }else{                        
                            DTO.setSignal(Konstante.EDIT_UCESNIK_ERROR);
                        }
                    break;
                    
                    case Konstante.GET_LIST_OGLAS:                        
                        listOdo = new ArrayList<OpstiDomenskiObjekat>();
                        poruka = SoGetListOglas.SoGetListOglas((OpstiDomenskiObjekat) DTO.getParametar(), listOdo);
                        DTO.setPoruka(poruka);
                        
                        if(SoGetUcesnik.signal){
                            DTO.setOdgovor(listOdo);
                            DTO.setSignal(Konstante.GET_LIST_OGLAS_OK); 
                        }else{                        
                            DTO.setSignal(Konstante.GET_LIST_OGLAS_ERROR);
                        }
                    break;                    
                    case Konstante.GET_LIST_NADMETANJA:                                                 
                        poruka = SoGetListNadmetajeZaOglas.SoGetListNadmetajeZaOglas((OpstiDomenskiObjekat) DTO.getParametar());
                        DTO.setPoruka(poruka);
                        
                        if(SoGetUcesnik.signal){
                            DTO.setOdgovor(DTO.getParametar());
                            DTO.setSignal(Konstante.GET_LIST_NADMETANJA_OK); 
                        }else{                        
                            DTO.setSignal(Konstante.GET_LIST_NADMETANJA_ERROR);
                        }
                    break;
                    case Konstante.GET_LIST_PRIJAVA:     
                        listOdo = new ArrayList<OpstiDomenskiObjekat>();
                        poruka = SOGetListPrijavaZaNadmetanje.SOGetListPrijafaZaNadmetanje((OpstiDomenskiObjekat) DTO.getParametar(), listOdo);
                        DTO.setPoruka(poruka);
                        
                        if(SoGetUcesnik.signal){
                            DTO.setOdgovor(listOdo);
                            DTO.setSignal(Konstante.GET_LIST_PRIJAVA_OK); 
                        }else{                        
                            DTO.setSignal(Konstante.GET_LIST_PRIJAVA_ERROR);
                        }
                    break;                                        
                    case Konstante.GET_LISTA_UCESNIKA_ZA_PRIJAVU:     
                        listOdo = new ArrayList<OpstiDomenskiObjekat>();
                        poruka = SOGetListUcesnikaZaNadmetanje.SOGetListUcesnikaZaNadmetanje((OpstiDomenskiObjekat) DTO.getParametar(), listOdo);
                        DTO.setPoruka(poruka);
                        
                        if(SoGetUcesnik.signal){
                            DTO.setOdgovor(listOdo);
                            DTO.setSignal(Konstante.GET_LISTA_UCESNIKA_ZA_PRIJAVU_OK); 
                        }else{                        
                            DTO.setSignal(Konstante.GET_LISTA_UCESNIKA_ZA_PRIJAVU_ERROR);
                        }
                    break;
                    case Konstante.ADD_PRIJAVA:                             
                        poruka = SoAddPrijava.SoDodajPrijava((OpstiDomenskiObjekat) DTO.getParametar());
                        DTO.setPoruka(poruka);
                        
                        if(SoGetUcesnik.signal){
                            DTO.setOdgovor(DTO.getParametar());                               
                            DTO.setSignal(Konstante.ADD_PRIJAVA_OK); 
                        }else{                        
                            DTO.setSignal(Konstante.ADD_PRIJAVA_ERROR);
                        }
                    break;
                     case Konstante.DELETE_PRIJAVA:
                        poruka = SoDeleteOdo.SoObrisiUcesnika((OpstiDomenskiObjekat) DTO.getParametar());                        
                        DTO.setPoruka(poruka);
                        
                        if(SoGetUcesnik.signal){                                
                            DTO.setSignal(Konstante.DELETE_PRIJAVA_OK); 
                        }else{                        
                            DTO.setSignal(Konstante.DELETE_PRIJAVA_ERROR);
                        }                        
                    break;
                    case Konstante.EDIT_PRIJAVA:                        
                        poruka = SoUpdatePrijavu.SoIzmeniPrijavu((OpstiDomenskiObjekat) DTO.getParametar());   
                        DTO.setPoruka(poruka);
                        
                        if(SoGetUcesnik.signal){
                            DTO.setOdgovor(DTO.getParametar());
                            DTO.setSignal(Konstante.EDIT_PRIJAVA_OK); 
                        }else{                        
                            DTO.setSignal(Konstante.EDIT_PRIJAVA_ERROR);
                        }
                    break;
                }
                out.writeObject(DTO);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }

}