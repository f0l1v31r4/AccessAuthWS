package ejb;

import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Local;
import model.AbstractShape;
import model.IPermission;

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
    
}
