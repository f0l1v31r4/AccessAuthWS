package services;

import ejb.SimpleDAOLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import model.AbstractShape;
import model.IAccessAuth;

/**
 * O webservice usa o nosso ejb SimpleDAO
 * @author foliveira
 */
@WebService(serviceName = "AccessAuthWS")
public class AccessAuthWS implements IAccessAuth {

    @EJB
    private SimpleDAOLocal simpleDAO;

    @Override
    @WebMethod(operationName = "login")
    public String login(@WebParam(name = "name") String name, @WebParam(name = "passwd") String passwd) throws Exception {
        return simpleDAO.startSession(name);
    }

    @Override
    @WebMethod(operationName = "createUser")
    public boolean createUser(@WebParam(name = "name") String name, @WebParam(name = "passwd") String passwd) throws Exception {
        return simpleDAO.createUser(name, passwd);
    }

    @Override
    @WebMethod(operationName = "getListObject")
    public List<String> getListObject(@WebParam(name = "session") String session) throws Exception {
        return simpleDAO.getListObject(session);
    }


    @Override
    @WebMethod(operationName = "getObject")
    public AbstractShape getObject(@WebParam(name = "session") String session, @WebParam(name = "id") String id) throws Exception {
        return simpleDAO.getObject(session, id);
    }
//
//    @Override
//    @WebMethod(operationName = "writeObject")
//    public boolean writeObject(@WebParam(name = "session") String session, @WebParam(name = "shape") AbstractShape shape) throws Exception {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    @WebMethod(operationName = "removeObject")
//    public boolean removeObject(@WebParam(name = "session") String session, @WebParam(name = "id") String id) throws Exception {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }

}
