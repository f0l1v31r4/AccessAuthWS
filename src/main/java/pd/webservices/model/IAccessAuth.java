package pd.webservices.model;

import java.util.List;

/**
 *
 * @author foliveira
 */
public interface IAccessAuth
{
  public String login(String name, String passwd) throws Exception;
  public boolean createUser(String name, String passwd) throws Exception;
  public List<String> getListObject(String session) throws Exception;
  public AbstractShape getObject(String session, String id) throws Exception;
  public boolean writeObject(String session,AbstractShape shape) throws Exception;
//  public boolean removeObject(String session,String id) throws RemoteException;
  
}
