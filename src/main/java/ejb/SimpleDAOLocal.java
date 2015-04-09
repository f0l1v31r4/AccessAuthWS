package ejb;

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
    Boolean createUser(String name, String passwd);
    void setPermissionToUser(String user, IPermission permission);
    
}
