/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import ejb.SimpleDAO;
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
public class SessionsMB implements Serializable
{
  @EJB
  private SimpleDAO simpleDAO;
  
  
  public SessionsMB()
  {
  }
  
  public List<String> getListSessions()
  {
    
    List<String>list  = new ArrayList<>();
    
    simpleDAO.getSessions();
    for (String list1 : simpleDAO.getSessions().keySet())
    {
      list.add(list1);
    }
    return list;
  }
}
