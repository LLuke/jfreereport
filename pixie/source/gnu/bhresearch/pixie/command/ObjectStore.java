package gnu.bhresearch.pixie.command;

import gnu.bhresearch.pixie.image.PixieDataInput;
import java.util.Vector;
import java.io.IOException;

public class ObjectStore
{
  private Vector objects;

  public ObjectStore ()
  {
    objects = new Vector ();
  }

  public ObjectStore (int[] offsets, PixieDataInput in) throws IOException
  {
    this ();
    loadObjects (offsets, in);
  }
  
  public PixieObject getObject (int number)
  {
    return (PixieObject) objects.elementAt (number);
  }
  
  public void addObject (PixieObject o)
  {
    objects.add (o);
  }
  
  public void loadObjects (int[] offsets, PixieDataInput in) throws IOException
  {
    for (int i = 0; i < offsets.length; i++)
    {
      in.seek (offsets[i]);
      addObject (new PixieObject (in, this, false));
    }
  }
}
