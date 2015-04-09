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
import model.IPermission;
import model.SimplePermission;

/**
 * Este classe simula um banco de dados usando estruturas simples, mas usando
 * toda a infra-estrutura do EJB concorrência
 * @author foliveira
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@Startup
public class SimpleDAO implements SimpleDAOLocal {

    private final Map<String, String> users = new HashMap<>();
    private final Map<String, String> sessions = new HashMap<>();
    private final Map<String, IPermission> permissions = new HashMap<>();
    
    
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
    
    /**
     * Cria as sessões dos usuários do sistema
     * @param user o login do usuário do sistema
     * @return um identificador único da sessão
     */
    @Override
    @Lock(LockType.WRITE)
    public String startSession(String user) {
        String id = new UID().toString();
        sessions.put(id, user);
        return id;
    }

    /**
     * Cria uma conta de usuário no sistema
     * @param name o nome do usuário
     * @param passwd a senha do usuário
     * @return true em caso de sucesso e false caso contrário
     * @throws Exception 
     */
    @Override
    @Lock(LockType.WRITE)
    public Boolean createUser(String name, String passwd) {
        boolean successfull;
        if (users.containsKey(name)) {
            //throw new RuntimeException("Usuário já cadastrado.");
        } else if (name.isEmpty()) {
            //throw new RuntimeException("Nome de usuario inválido.");
        } else if (passwd.isEmpty()) {
            //throw new RuntimeException("Senha inválida.");
        }

        users.put(name, passwd);

        // criando as permissões
        setPermissionToUser(name, new SimplePermission());
        successfull = true;
        return successfull;
    }
    
    /**
     * Configura as permissões de um usuário
     *
     * @param user o nome de um usuário valido do sistema
     * @param permission as permissões do usuário     
     * @throws java.lang.Exception     
     */
    @Override
    @Lock(LockType.WRITE)
    public void setPermissionToUser(String user, IPermission permission)  {
        // se o usuário não for válido lança o erro
        if (!users.containsKey(user)) {
            //throw new RuntimeException(String.format("Usuário %s inválido.", user));
        }
        // atualizar a permissão do usuário
        permissions.put(user, permission);
    }

    
    
    
    
}
