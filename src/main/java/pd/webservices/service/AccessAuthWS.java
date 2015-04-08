package pd.webservices.service;

import java.util.List;
import java.util.Map;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import pd.webservices.model.AbstractShape;
import pd.webservices.model.IAccessAuth;
import pd.webservices.model.IPermission;

/**
 *
 * @author foliveira
 */
@WebService(serviceName = "AccessAuthWS")
public class AccessAuthWS implements IAccessAuth
{

  
  @Override
  @WebMethod(operationName = "login")
  public String login(@WebParam(name = "name") String name, @WebParam(name = "passwd") String passwd) throws Exception
  {
    return String.format("login of %s %s", name, passwd);
  }

  @Override
  @WebMethod(operationName = "getListObject")
  public List<String> getListObject(@WebParam(name = "session") String session) throws Exception
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  @WebMethod(operationName = "createUser")
  public boolean createUser(@WebParam(name = "name") String name, @WebParam(name = "passwd") String passwd) throws Exception
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  @WebMethod(operationName = "getObject")
  public AbstractShape getObject(@WebParam(name = "session") String session,@WebParam(name = "id") String id) throws Exception
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  @WebMethod(operationName = "writeObject")
  public boolean writeObject(@WebParam(name = "session") String session, @WebParam(name = "shape") AbstractShape shape) throws Exception
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  @WebMethod(operationName = "removeObject")
  public boolean removeObject(@WebParam(name = "session") String session,@WebParam(name = "id") String id) throws Exception
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  
}
