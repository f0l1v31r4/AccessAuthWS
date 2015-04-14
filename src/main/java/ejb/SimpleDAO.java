package ejb;

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
import common.AbstractShape;
import common.Circle;
import common.IPermission;
import common.SimplePermission;
import common.Square;
import java.util.logging.Logger;

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
    private final Map<String, Square> objects = new HashMap<>();
    private static final Logger LOGGER = Logger.getLogger(SimpleDAO.class.getName());
    
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
    
    /**
     * 
     */
    @Lock(LockType.WRITE)
    public void initPermissions() {
        permissions.put("admin", new SimplePermission(true,true));
        permissions.put("caio", new SimplePermission(false,true));
        permissions.put("andre", new SimplePermission(true,false));
        permissions.put("antonio", new SimplePermission());
    }
    
    /**
     * 
     */
    @Lock(LockType.WRITE)
    public void initObjects() {
        objects.put("Obj1",new Square("Obj1","admin"));
        objects.put("Obj2",new Square("Obj2","admin"));
        objects.put("Obj3",new Square("Obj3","caio"));
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
            throw new EJBException(String.format("Usuario '%s' ja cadastrado.",name));
        } else if (name.isEmpty()) {
            throw new EJBException("Nome de usuario invalido.");
        } else if (passwd.isEmpty()) {
            throw new EJBException("Senha invalida.");
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
            throw new EJBException(String.format("Usuario '%s' invalido.", user));
        }
        // atualizar a permissão do usuário
        permissions.put(user, permission);
    }

    /**
     * 
     * @param session
     * @return
     * @throws EJBException 
     */
    @Override
    @Lock(LockType.READ)
    public List<String> getListObject(String session) throws EJBException {
        List<String> listObject = new ArrayList<>();

        if (!sessions.containsKey(session)  ) {
            throw new EJBException(String.format("Sessao '%s' invalida",session));
        }

        for (Square shape : objects.values()) {
            String info = shape.toString();
            listObject.add(info);
        }

        return listObject;        
    }

    /**
     * 
     * @param session
     * @param id
     * @return
     * @throws EJBException 
     */
    @Override
    public Square getObject(String session, String id) throws EJBException {
        Square shape;
        if (!sessions.containsKey(session) ) {
            throw new EJBException(String.format("Sessao '%s' invalida.",session));
        }
                
        String user = sessionToUser(session);
                
        if (!getPermissionsFromUser(user)[0]) {
            throw new EJBException(String.format("Usuario '%s' nao tem permissao de ler o objeto '%s'.", user, id));
        }

        if (!objects.containsKey(id)) {
            throw new EJBException(String.format("O objeto '%s' nao existe.", id));
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
            throw new EJBException(String.format("Usuario %s invalido.", user));
        }

        IPermission authorization = this.permissions.get(user);
        permission[0] = authorization.canRead();
        permission[1] = authorization.canWrite();

        return permission;
    }

  @Override
  public Boolean writeObject(String session, Square shape) throws EJBException
  {
        boolean successfull;

        if (!sessions.containsKey(session)) {
            throw new EJBException(String.format("Sessao '%s' invalida.", session));
        }

        String user = sessionToUser(session);
        if (!getPermissionsFromUser(user)[1]) {
            throw new EJBException(String.format("Usuario '%s' nao tem permissao de criar o objeto '%s'.", user, shape.getId()));
        }

        LOGGER.info(String.format("Solicitacao de escrita de objeto %s do usuario da sessao %s",shape.getId(), session));
        objects.put(user, shape);
        successfull = true;

        return successfull;
  }

  @Override
  public Boolean removeObject(String session, String id) throws EJBException
  {
        boolean successfull = true;
        if (!sessions.containsKey(session)) {
            throw new EJBException(String.format("Sessao invalida '%s'.",session));
        }

        String user = sessionToUser(session);
        if (!getPermissionsFromUser(user)[1]) {
            throw new EJBException(String.format("Usuario '%s' nao tem permissao de remover o objecto '%s'.", user, id));
        }

        if (!objects.containsKey(id)) {
            throw new EJBException(String.format("O objeto '%s' nao existe.", id));
        }
        LOGGER.info(String.format("Solicitacao de remocao do objeto '%s', sessao '%s' ", id,session));
        objects.remove(id);
        return successfull;
  }
  
  
}
