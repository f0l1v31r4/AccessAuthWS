package ejb;

import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Local;
import common.IPermission;
import common.Square;

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
    Square getObject(String session, String id) throws EJBException;
    Boolean writeObject(String session,Square shape) throws EJBException;
    Boolean removeObject(String session, String id) throws EJBException;
    
}
