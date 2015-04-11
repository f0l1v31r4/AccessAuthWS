package common;

/**
 * Uma implementação simples de sistema de autorização e acesso a objetos
 * @author foliveira
 */
public class SimplePermission implements IPermission
{
  private boolean read;
  private boolean write;

  /**
   * Construtor padrão onde sera defindo as permissões iniciais
   * @param read 
   * @param write 
   */
  public SimplePermission(boolean read, boolean write)
  {
    this.read = read;
    this.write = write;
  }
 
  public SimplePermission()
  {
    read=false;
    write=false;
  }
  
  @Override
  public boolean canRead()
  {
    return read;
  }

  @Override
  public boolean canWrite()
  {
    return write;
  }
  
   public void setRead(boolean read)
  {
    this.read = read;
  }

  public void setWrite(boolean write)
  {
    this.write = write;
  }

}
