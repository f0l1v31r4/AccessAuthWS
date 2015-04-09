package ejb;

import javax.ejb.EJBException;
import javax.ejb.Local;
import model.IPermission;

/**
 * 
 * @author foliveira
 */
@Local
public interface SimpleDAOLocal {

    String startSession(String name);
    void initUsers();
    Boolean createUser(String name, String passwd) throws EJBException;
    void setPermissionToUser(String user, IPermission permission) throws EJBException;
    
}
