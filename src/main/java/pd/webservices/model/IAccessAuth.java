package pd.webservices.model;

import java.rmi.Remote;
import java.rmi.RemoteException;
//import java.util.List;

/**
 *
 * @author foliveira
 */
public interface IAccessAuth extends Remote
{
  public String login(String name, String passwd) throws RemoteException;
//  public boolean createUser(String name, String passwd) throws RemoteException;
//  public List<String> getListObject(String session) throws RemoteException;
//  public AbstractShape getObject(String session, String id) throws RemoteException;
//  public boolean writeObject(String session,AbstractShape shape) throws RemoteException;
//  public boolean removeObject(String session,String id) throws RemoteException;
  
}
