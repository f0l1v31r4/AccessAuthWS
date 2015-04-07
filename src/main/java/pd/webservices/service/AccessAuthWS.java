package pd.webservices.service;

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
    public String login(@WebParam(name = "name") String name,@WebParam(name = "passwd") String passwd) {
        return String.format("login of %s %s",name,passwd);
    }
}
