/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbkomunikacija;

import domen.OdoPrimaryKey;
import domen.OpstiDomenskiObjekat;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Mladen
 */
public class GenerisiSqlUpit {
    private String select;
    private String from;
    private String where;
    private int nodNumber = 0;
    private String superClass;
    private String[] includeArray;
    private Map<String,String> map = new HashMap<String,String>();
    private String upit;
    private OpstiDomenskiObjekat odo;
    private OpstiDomenskiObjekat filterOdo;
    
        
    public GenerisiSqlUpit(OpstiDomenskiObjekat odo , String include, OpstiDomenskiObjekat filterOdo ) {
        includeArray = include.split("\\.");        
        select = new String();
        from = new String();
        where = new String();
        this.odo = odo;   
        this.filterOdo = filterOdo;
        konstruisiSql();

    }
              
    private void konstruisiSql(){
         
        try {
            Class<?> cTemp = Class.forName(odo.getClass().getName());
            subclass(cTemp, 0); 

        } catch (Exception e) {
            System.out.println("Domenska klasa nije prnadjena" + e.toString() );
        }
        
    };
    
    public Class<?> subclass(Class<?> odo, int nodNuberForJoin ){
        String odoName = odo.getSimpleName();        
        Class<?> sc = (Class<?>) odo.getGenericSuperclass();  
        String scName = sc.getSimpleName(); 
        //Proverava se da li je poslati objekat Object 
        if(odoName.compareTo("Object") != 0){  
            //Proverava se da li je objekat koji nasleđuje odo Objec, 
            //ukoliko nije znači da odo objekat nasleđuje neki drugi odo objekat            
            //pa se prolaprolazi rekurzivno sve dok se ne stigne do početnog odo u ijararhiji nasleđivanja
            if(scName.compareTo("Object") != 0){                                                  
                int localNod = nodNumber++;                  
                Class<?> rootClassName =  subclass(sc, localNod);                 
                attribute(odo, localNod, nodNuberForJoin, rootClassName);                       
                return rootClassName;
            }else{
                int localNod = nodNumber++;                  
                attribute(odo, localNod, nodNuberForJoin, null);                       
                return odo;                
            }
        }
        return null;
    }        

    public void attribute(Class<?> odo, int nodNuber, int nodNuberForJoin, Class<?>  rootClassName){        
        Field [] attributes = odo.getDeclaredFields();
        for (Field attribute : attributes) {                        
            for (int i = 0; i < includeArray.length; i++) {                                    
                if(attribute.getType().getSimpleName().compareTo(includeArray[i]) == 0){
                    try {
                        Class<?> c = Class.forName(attribute.getType().getName());                                                
                        subclass(c, nodNuber);                                                
                        } catch (Exception e) {
                            System.out.println("Greska kod citanja klase");
                        }
                    //Restrikcija nad objektom koji se učitava definiše se u WHERE klauzuli
                    if(filterOdo != null){
                        if(attribute.getType().getSimpleName().compareTo(filterOdo.getClass().getSimpleName()) == 0 ){                                                                       
                            where = where + String.format(filterOdo.vratiUslovZaNadjiSlog() + " AND ", nodNuber);
                        }
                    }
                }                 
            }

            //Dodavanje kolona u SELECT klauzuli
            if(!attribute.getType().getName().startsWith("domen")
               && !attribute.getType().getName().contains("List")) {
                select = select + String.format("\n[%d].[%s] as '[%d].[%s]',", nodNuber, attribute.getName(), nodNuber, attribute.getName());
            }
        }
        
        //Definisanje tablea za spajanje u FROM klauzuli
        from = from + String.format("\n%s [%d],", odo.getSimpleName(), nodNuber);        
        
        //Definisanje spajanja relacija u WHERE klauzuli              
        try{
        Class<?> joinClass;           
        if(rootClassName != null){
            joinClass = rootClassName;
        }else{
            joinClass = odo;}               
        Annotation[] annotations = joinClass.getDeclaredAnnotations();
        for(Annotation annotation : annotations){
            if(annotation instanceof OdoPrimaryKey){
                OdoPrimaryKey odoPrimaryKey = (OdoPrimaryKey) annotation;                  
                String[] primaryKey = odoPrimaryKey.primaryKey();
                for (String pk : primaryKey) {
                 where = where + String.format("\n [%d].%s = [%d].%s AND ", nodNuber, pk, nodNuberForJoin, pk);                                       
                }

            }
        }
        }catch(Exception ex){
		ex.printStackTrace();
	}            
    }
        
    public Map<String, String> getMap() {
        String[] entries = from.split(",");
        for (String entry : entries) {          
          String[] keyValue = entry.split("\\ ");
          map.put(keyValue[0].replace("\n", ""),keyValue[1]);
        }
        return map;
    }

    public String getUpit() {
        upit = "SELECT " + select.substring(0, select.lastIndexOf(",")) + 
                "\n FROM "  + from.substring(0, from.lastIndexOf(",")) +
                "\n WHERE " + where.substring(0, where.lastIndexOf("AND")) ;
        return upit;
    }
     

      
}
