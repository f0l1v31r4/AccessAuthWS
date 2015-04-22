package common;

import java.io.Serializable;

/**
 *
 * @author foliveira
 */
public abstract class AbstractShape implements  Serializable
{
  protected String id = "";
  protected String owner;
 
  public AbstractShape(){
      
  }
  
  public AbstractShape(String id, String owner)
  {
    this.id = id;
    this.owner = owner;
  }

  public String getId()
  {
    return id;
  }

  public String getOwner()
  {
    return owner;
  }

  @Override
  public String toString()
  {
    return String.format("%s id: %s owner: %s",AbstractShape.class.getName(), id ,owner );
  }
  
}
