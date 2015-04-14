package ejb;

import common.AbstractShape;
import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Local;
import common.IPermission;
import java.util.Map;

/**
 * 
 * @author foliveira
 */
@Local
public interface SimpleDAOLocal {

    String startSession(String name);    
    Boolean createUser(String name, String passwd) throws EJBException;
    void setPermissionToUser(String user, IPermission permission) throws EJBException;
    List<String> getListObject(String session) throws EJBException;
    AbstractShape getObject(String session, String id) throws EJBException;
    Boolean writeObject(String session,AbstractShape shape) throws EJBException;
    Boolean removeObject(String session, String id) throws EJBException;
    Map<String, String> getUsers();
    Map<String, String> getSessions();
    Map<String, IPermission> getPermissions();
    Map<String, AbstractShape> getObjects();
  
    
}
