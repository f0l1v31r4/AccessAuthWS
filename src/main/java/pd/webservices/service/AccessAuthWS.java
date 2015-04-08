package pd.webservices.service;

import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import pd.webservices.model.IAccessAuth;

/**
 *
 * @author foliveira
 */
@WebService(serviceName = "AccessAuthWS")
public class AccessAuthWS implements IAccessAuth{

    
    @Override
    @WebMethod(operationName = "login")
    public String login(@WebParam(name = "name") String name,@WebParam(name = "passwd") String passwd) throws Exception {
        return String.format("login of %s %s",name,passwd);
    }

  @Override
  @WebMethod(operationName = "getListObject")
  public List<String> getListObject(@WebParam(name = "session")  String session) throws Exception
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }
    
}
