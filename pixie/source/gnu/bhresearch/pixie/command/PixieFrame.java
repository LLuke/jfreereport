package gnu.bhresearch.pixie.command;

import gnu.bhresearch.pixie.Constants;
import gnu.bhresearch.pixie.image.PixieDataInput;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Color;
import java.io.IOException;
import java.util.Vector;

public class PixieFrame extends PixieObject
{
  private Vector commands;

  public PixieFrame (PixieDataInput nl, ObjectStore store)
    throws IOException
  {
    super (nl, store,true);
  }
  
  public PixieFrame ()
  {
  }
  
  public static final PixieFrame loadFrame (PixieDataInput nl, ObjectStore store) 
    throws IOException
  {
    return new PixieFrame (nl, store);
  }
  
}


