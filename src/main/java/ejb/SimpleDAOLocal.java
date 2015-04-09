package ejb;

import javax.ejb.Local;

/**
 * 
 * @author foliveira
 */
@Local
public interface SimpleDAOLocal {

    String startSession(String login);
    void initUsers();
    
}
