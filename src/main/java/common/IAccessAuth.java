package common;

import java.util.List;

/**
 * Definição da interface do serviço de autenticação e autorização 
 * @author foliveira
 */
public interface IAccessAuth
{
  public String login(String name, String passwd) throws Exception;
  public boolean createUser(String name, String passwd) throws Exception;
  public String getListObject(String session) throws Exception;
  public AbstractShape getObject(String session, String id) throws Exception;
  public boolean writeObject(String session,String shape) throws Exception;
  public boolean removeObject(String session,String id) throws Exception;
  
}
