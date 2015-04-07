package pd.webservices.model;

/**
 *
 * @author foliveira
 */
public class Circle extends AbstractShape
{

  public Circle(String id, String owner)
  {
    super(id, owner);
  }

  @Override
  public String toString()
  {
    return String.format("%s id: %s owner: %s",Circle.class.getName(), id ,owner );
  }
  
}
