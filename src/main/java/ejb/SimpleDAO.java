/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.rmi.RemoteException;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJBException;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import model.AbstractShape;
import model.Circle;
import model.IPermission;
import model.SimplePermission;
import model.Square;

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
    private final Map<String, AbstractShape> objects = new HashMap<>();
    
    @PostConstruct
    private void starComponent()
    {
        initUsers();
        initPermissions();
        initObjects();
    }
    
    /**
     * Este método será chamado apos a acontrução do bean
     */
    @Lock(LockType.WRITE)
    public void initUsers() {
        users.put("admin", "123");
        users.put("caio", "456");
        users.put("andre", "789");
        users.put("antonio", "789");
    }
    
    @Lock(LockType.WRITE)
    public void initPermissions() {
        permissions.put("admin", new SimplePermission(true,true));
        permissions.put("caio", new SimplePermission(false,true));
        permissions.put("andre", new SimplePermission(true,false));
        permissions.put("antonio", new SimplePermission());
    }
    
    @Lock(LockType.WRITE)
    public void initObjects() {
        objects.put("circulo1",new Circle("circulo1","admin"));
        objects.put("circulo2",new Circle("circulo2","admin"));
        objects.put("Quadrado1",new Square("Quadrado1","caio"));
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
     * @throws EJBException 
     */
    @Override
    @Lock(LockType.WRITE)
    public Boolean createUser(String name, String passwd) throws EJBException {
        boolean successfull;
        if (users.containsKey(name)) {
            throw new EJBException("Usuário já cadastrado.");
        } else if (name.isEmpty()) {
            throw new EJBException("Nome de usuario inválido.");
        } else if (passwd.isEmpty()) {
            throw new EJBException("Senha inválida.");
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
     * @throws EJBException     
     */
    @Override
    @Lock(LockType.WRITE)
    public void setPermissionToUser(String user, IPermission permission) throws EJBException {
        // se o usuário não for válido lança o erro
        if (!users.containsKey(user)) {
            throw new EJBException(String.format("Usuário %s inválido.", user));
        }
        // atualizar a permissão do usuário
        permissions.put(user, permission);
    }

    @Override
    @Lock(LockType.READ)
    public List<String> getListObject(String session) throws EJBException {
        List<String> listObject = new ArrayList<>();

        if (!sessions.containsKey(session)  ) {
            throw new EJBException("Sessão inválida");
        }

        for (AbstractShape shape : objects.values()) {
            String info = shape.toString();
            listObject.add(info);
        }

        return listObject;        
    }

    @Override
    public AbstractShape getObject(String session, String id) throws EJBException {
        AbstractShape shape;
        if (!sessions.containsKey(session) ) {
            throw new EJBException("Invalid session.");
        }

        String user = sessionToUser(session);
        if (!getPermissionsFromUser(session)[0]) {
            throw new RuntimeException(String.format("User %s does not have permission to read object %s.", user, id));
        }

        if (!objects.containsKey(id)) {
            throw new RuntimeException(String.format("Object does not exist %s.", id));
        }

        shape = objects.get(id);
        return shape;
    }

    
    public String sessionToUser(String session) {
        if (!sessions.containsKey(session) || session.isEmpty()) {
            return null;
        }

        return sessions.get(session);
    }
    
    public boolean[] getPermissionsFromUser(String user) throws EJBException {
        // permissao padrao
        boolean[] permission = {false, false};
        // se o usuário for inválido, lança um erro
        if (!users.containsKey(user) || user.isEmpty()) {
            throw new EJBException(String.format("Usuário %s inválido.", user));
        }

        IPermission authorization = this.permissions.get(user);
        permission[0] = authorization.canRead();
        permission[1] = authorization.canWrite();

        return permission;
    }
    
}
