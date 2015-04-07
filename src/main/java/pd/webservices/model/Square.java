package pd.webservices.model;

/**
 *
 * @author foliveira
 */
public class Square extends AbstractShape
{

  public Square(String id, String owner)
  {
    super(id, owner);
  }

  @Override
  public String toString()
  {
    return String.format("%s id: %s owner: %s",Square.class.getName(), id ,owner );
  }
  
}
