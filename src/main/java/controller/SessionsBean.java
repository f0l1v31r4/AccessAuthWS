/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import ejb.SimpleDAOLocal;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;

/**
 *
 * @author foliveira
 */
@Named(value = "sessions")
@SessionScoped
public class SessionsBean implements Serializable
{
  @EJB
  private SimpleDAOLocal simpleDAO;
  
  
  public SessionsBean()
  {
  }
  
  public List<String> getListSessions()
  {
    
    List<String>list  = new ArrayList<>();
    
    simpleDAO.getSessions().keySet().stream().forEach((list1) ->
    {
      list.add(list1);
    });
    
    return list;
  }
}
