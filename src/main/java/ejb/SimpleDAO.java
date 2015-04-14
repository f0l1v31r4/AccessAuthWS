package ejb;

import common.AbstractShape;
import common.Circle;
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
import common.IPermission;
import common.SimplePermission;
import common.Square;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Este classe simula um banco de dados usando estruturas simples, mas usando
 * toda a infra-estrutura do EJB concorrência
 *
 * @author foliveira
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@Startup
public class SimpleDAO implements SimpleDAOLocal
{

  private final Map<String, String> users = new HashMap<>();
  private final Map<String, String> sessions = new HashMap<>();
  private final Map<String, IPermission> permissions = new HashMap<>();
  private final Map<String, AbstractShape> objects = new HashMap<>();
  private static final Logger LOGGER = Logger.getLogger(SimpleDAO.class.getName());
  private String message;

  /**
   *
   */
  @PostConstruct
  private void starComponent()
  {
    LOGGER.info("Iniciando o componente EJB.");
    initUsers();
    initPermissions();
    initObjects();
  }

  /**
   * Este método será chamado apos a construção do bean
   */
  @Lock(LockType.WRITE)
  public void initUsers()
  {
    users.put("admin", "123");
    users.put("caio", "456");
    users.put("andre", "789");
    users.put("antonio", "789");
    message = String.format("Criando os usuários iniciais da aplicação: %s.", users);
    LOGGER.info(message);
  }

  /**
   *
   */
  @Lock(LockType.WRITE)
  public void initPermissions()
  {
    permissions.put("admin", new SimplePermission(true, true));
    permissions.put("caio", new SimplePermission(false, false));
    permissions.put("andre", new SimplePermission(true, false));
    permissions.put("antonio", new SimplePermission());
    message = String.format("Atribuindo as permisões iniciais dos usuários: %s.", permissions);
    LOGGER.info(message);
  }

  /**
   *
   */
  @Lock(LockType.WRITE)
  public void initObjects()
  {
    objects.put("Obj1", new Square("Obj1", "admin"));
    objects.put("Obj2", new Circle("Obj2", "admin"));
    objects.put("Obj3", new Square("Obj3", "caio"));
    objects.put("Obj4", new Circle("Obj3", "antonio"));
    message = String.format("Criando objetos de exemplo %s.", objects);
    LOGGER.info(message);
  }

  /**
   * Cria as sessões dos usuários do sistema
   *
   * @param user o login do usuário do sistema
   * @return um identificador único da sessão
   */
  @Override
  @Lock(LockType.WRITE)
  public String startSession(String user)
  {
    
    if (sessions.containsKey(user))
    {
      message = String.format("Usuário %s já tem uma sessão aberta.", user);
      LOGGER.info(message);
      return sessions.get(user);
    } else
    {
      String id = new UID().toString();
      sessions.put(id, user);
      message = String.format("Criando a sessão %s para o usuários %s.", id, user);
      LOGGER.info(message);
      return id;
    }
  }

  /**
   * Cria uma conta de usuário no sistema
   *
   * @param name o nome do usuário
   * @param passwd a senha do usuário
   * @return true em caso de sucesso e false caso contrário
   * @throws EJBException
   */
  @Override
  @Lock(LockType.WRITE)
  public Boolean createUser(String name, String passwd) throws EJBException
  {
    boolean successfull;
    if (users.containsKey(name))
    {
      message = String.format("Usuário %s já cadastrado.", name);
      LOGGER.severe(message);

      throw new EJBException(message);
    } else if (name.isEmpty())
    {
      message = "Nome de usuário inválido.";
      LOGGER.severe(message);
      throw new EJBException(message);
    } else if (passwd.isEmpty())
    {
      message = "Senha inválida.";
      LOGGER.severe(message);
      throw new EJBException(message);
    }

    users.put(name, passwd);

    // criando as permissões
    IPermission permission = new SimplePermission();
    setPermissionToUser(name, permission);
    message = String.format("Criando o usuário %s: senha %s e as permissões %s.", name,passwd, permission);
    LOGGER.info(message);
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
  public void setPermissionToUser(String user, IPermission permission) throws EJBException
  {
    // se o usuário não for válido lança o erro
    if (!users.containsKey(user))
    {
      message = String.format("Usuario %s inválido.", user);
      LOGGER.severe(message);
      throw new EJBException(message);
    }
    // atualizar a permissão do usuário
    message = String.format("Atribuíndo as permissões %s ao usuário %s.", permission, user);
    LOGGER.info(message);
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
  public List<String> getListObject(String session) throws EJBException
  {
    List<String> listObject = new ArrayList<>();

    if (!sessions.containsKey(session))
    {
      message = String.format("Sessão %s inválida.", session);
      LOGGER.severe(message);
      throw new EJBException(message);
    }

    objects.values().stream().map((shape) -> shape.toString()).forEach((info) ->
    {
      listObject.add(info);
    });

    message = String.format("Sessão %s, solicitando os objetos %s.", session, listObject);
    LOGGER.info(message);
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
  @Lock(LockType.READ)
  public AbstractShape getObject(String session, String id) throws EJBException
  {
    AbstractShape shape;
    if (!sessions.containsKey(session))
    {
      message = String.format("Sessão %s inválida.", session);
      LOGGER.severe(message);
      throw new EJBException(message);
    }

    String user = sessionToUser(session);

    if (!getPermissionsFromUser(user)[0])
    {
      message = String.format("Usuário %s, não tem permissão de ler o objeto o %s.", user, id);
      LOGGER.severe(message);
      throw new EJBException(message);
    }

    if (!objects.containsKey(id))
    {
      message = String.format("O objeto %s não existe.", id);
      LOGGER.severe(message);
      throw new EJBException(message);
    }

    message = String.format("Sessão %s, obtendo o objeto %s.",session,id);
    LOGGER.info(message);
    shape = objects.get(id);

    return shape;
  }

  @Lock(LockType.READ)
  public String sessionToUser(String session)
  {
    if (!sessions.containsKey(session) || session.isEmpty())
    {
      return null;
    }
    String user  = sessions.get(session);
    message = String.format("O usuário %s está vinculado a sessão %s.", user,session);
    LOGGER.info(message);
    return user;
  }

  @Lock(LockType.READ)
  public boolean[] getPermissionsFromUser(String user) throws EJBException
  {
    // permissao padrao
    boolean[] permission = { false, false  };
    // se o usuário for inválido, lança um erro
    if (!users.containsKey(user) || user.isEmpty())
    {
      message = String.format("Usuário %s inválido.", user);
      LOGGER.severe(message);
      throw new EJBException(message);
    }

    IPermission authorization = this.permissions.get(user);
    permission[0] = authorization.canRead();
    permission[1] = authorization.canWrite();

    message = String.format("Verificando as permissões do usuário %s: %s",user,Arrays.toString(permission));
    LOGGER.info(message);
            
    return permission;
  }

  @Override
  @Lock(LockType.WRITE)
  public Boolean writeObject(String session, AbstractShape shape) throws EJBException
  {
    boolean successfull;

    if (!sessions.containsKey(session))
    {
      message = String.format("Sessão %s inválida.", session);
      LOGGER.severe(message);
      throw new EJBException(message);
    }

    String user = sessionToUser(session);
    if (!getPermissionsFromUser(user)[1])
    {
      message = String.format("Usuário %s não tem permissão de criar o objeto %s.", user, shape);
      LOGGER.severe(message);
      throw new EJBException(message);
    }

    LOGGER.info(String.format("Solicitacao de escrita de objeto %s do usuario da sessao %s", shape.getId(), session));
    objects.put(user, shape);
    successfull = true;

    return successfull;
  }

  @Override
  @Lock(LockType.WRITE)
  public Boolean removeObject(String session, String id) throws EJBException
  {
    boolean successfull = true;
    if (!sessions.containsKey(session))
    {
      message = String.format("Sessao invalida '%s'.", session);
      LOGGER.severe(message);
      throw new EJBException(message);
    }

    String user = sessionToUser(session);
    if (!getPermissionsFromUser(user)[1])
    {
      message = String.format("Usuário %s não tem permissão de remover o objecto %s.", user, id);
      LOGGER.severe(message);
      throw new EJBException(message);
    }

    if (!objects.containsKey(id))
    {
      message = String.format("O objeto '%s' nao existe.", id);
      LOGGER.severe(message);
      throw new EJBException(message);
    }
    LOGGER.info(String.format("Sessão %s, removendo o objeto %s.", session,id));
    objects.remove(id);
    return successfull;
  }

  @Override
  @Lock(LockType.READ)
  public Map<String, String> getUsers()
  {
    message = String.format("Lendo a lista de usuários %s.",users);
    LOGGER.info(message);
    return users;
  }

  @Override
  @Lock(LockType.READ)
  public Map<String, String> getSessions()
  {
    message = String.format("Lendo a lista de sessões %s.",sessions);
    LOGGER.info(message);
    return sessions;
  }

  @Override
  @Lock(LockType.READ)
  public Map<String, IPermission> getPermissions()
  {
    message = String.format("Lendo as permissões %s.",permissions);
    LOGGER.info(message);
    return permissions;
  }

  @Override
  @Lock(LockType.READ)
  public Map<String, AbstractShape> getObjects()
  {
    message = String.format("Lendo a lista de objetos %s.",objects);
    LOGGER.info(message);
    return objects;
  }

}
