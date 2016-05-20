/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbkomunikacija;

import domen.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Properties;

/**
 *
 * @author Mladen
 */
public class Util {
    private Properties properties;
    private  Util instance;
    private String currentdb;
    
    public  Util() throws IOException {
        properties = new Properties();
        properties.load(new FileInputStream("D:\\User\\Documents\\Fakultet\\FON\\Softverski Procesi\\Seminsrski rad\\LicitacijaAplikacija\\LicitacijaServer\\src\\dbkomunikacija\\db.properties"));
        currentdb = properties.getProperty("current_db");
    }

    public String getUrl(){
        return properties.getProperty(currentdb + "_url");
    }
    
    public String getUser(){
        return properties.getProperty(currentdb + "_user");
    }
    
    public String getPosswoard(){
        return properties.getProperty(currentdb + "_passwoard");
    }
    
    public String getDriver(){
        return properties.getProperty(currentdb + "_driver");
    }
    
    public String getServerName(){
        return properties.getProperty(currentdb + "_serverName");
    }
    
    public String getInstanceName(){
        return properties.getProperty(currentdb + "_instanceName");
    }
    
    public String getPortName(){
        return properties.getProperty(currentdb + "_portName");
    }
    
    public String getDatabaseName(){
        return properties.getProperty(currentdb + "_databaseName");
    }

        
}
