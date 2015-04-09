/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.rmi.server.UID;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author foliveira
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@Startup
public class SimpleDAO implements SimpleDAOLocal {

    private final  Map<String, String> users = new HashMap<>();
    private final  Map<String, String> sessions = new HashMap<>();
    
    
    /**
     * Este método será chamado apos a acontrução do bean
     */
    @Override
    @PostConstruct
    public void initUsers() {
        users.put("admin", "123");
        users.put("caio", "456");
        users.put("andre", "789");
        users.put("antonio", "789");
    }
    
    @Override
    @Lock(LockType.WRITE)
    public String startSession(String user) {
        String id = new UID().toString();
        sessions.put(id, user);
        return id;
    }

    
    
    
    
}
