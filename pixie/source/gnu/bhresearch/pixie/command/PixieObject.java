package gnu.bhresearch.pixie.command;

import gnu.bhresearch.pixie.image.PixieDataInput;
import gnu.bhresearch.pixie.Constants;
import java.util.Vector;
import java.io.IOException;
import java.awt.Graphics;

public class PixieObject extends PixieImageCommand
{
  private Vector commands;
  private boolean isBlank;
  
  public PixieObject (PixieDataInput nl, ObjectStore store, boolean isBlank)
    throws IOException
  {
    Vector v = new Vector ();
    this.isBlank = isBlank;
    
    nl.resetPrevXY ();

    while (true)
    {
      int cmd = nl.readUnsignedVInt();
      switch (cmd)
      {
        case Constants.CMD_SET_COLOR:
        {
          v.add (new SetColor (nl));
          break;
        }
        case Constants.CMD_STROKE_CURVE:
        {
          v.add (new StrokeCurve (nl));
          break;
        }
        case Constants.CMD_FILL_CURVE:
        {
          v.add (new FilledCurve (nl));
          break;
        }
        case Constants.CMD_STROKE_POLYLINE:
        {
          v.add (new StrokePolygon (nl));
          break;
        }
        case Constants.CMD_FILL_POLYGON:
        {
          v.add (new FilledPolygon (nl));
          break;
        }
        case Constants.CMD_USE_OBJECT:
        {
          v.add (new PixieObjectRef (nl, store));
          break;         
        }
        case Constants.CMD_COMMENT:
        {
          v.add (new Comment (nl));
          break;
        }
        case Constants.CMD_END:
        {
          //in = oldIn;
          commands = v;
          return;
        }
        case Constants.CMD_SET_FONT:
        {
          v.add (new SetFont (nl));
          break;
        }
        case Constants.CMD_FILL_TEXT:
        {
          v.add (new FillText (nl));
          break;
        }
        case Constants.CMD_HOT_SPOT:
        {
        	v.add (new Hotspot(nl));
          break;
        }
        case Constants.CMD_STROKE_ELLIPSE:
        {
          v.add (new StrokeOval (nl));
          break;
        }
        case Constants.CMD_FILL_ELLIPSE:
        {
          v.add (new FilledOval (nl));
          break;
        }
        case Constants.CMD_STROKE_RECTANGLE:
        {
          v.add (new StrokeRectangle (nl));
          break;
        }
        case Constants.CMD_FILL_RECTANGLE:
        {
          v.add (new FilledRectangle (nl));
          break;
        }
        default:	// 0 and other numbers not used.
          throw new IOException( "Unknown record type " + cmd );
      }
    }
  }
  
  public PixieObject ()
  {
    commands = new Vector ();
  }
  
  public void addCommand (PixieImageCommand cmd)
  {
    System.out.println ("AddedCommand: " + cmd.getClass().getName());
    commands.add (cmd);
  }
  
  public void paint (Graphics g)
  {
    int l = commands.size ();
//    System.out.println ("Painting for : " + l + " commands");
    for (int i = 0; i < l; i++)
    {
      PixieImageCommand cmd = (PixieImageCommand) commands.elementAt (i);
      cmd.paint (g);
    }
  }

  public int getCommandCount ()
  {
    return commands.size ();
  }

  public void setScale (float scaleX, float scaleY)
  {
    System.out.println ("Scale to : " + scaleX + ";" + scaleY);
    int l = commands.size ();
    for (int i = 0; i < l; i++)
    {
      PixieImageCommand cmd = (PixieImageCommand) commands.elementAt (i);
      cmd.setScale (scaleX, scaleY);
    }
  }
  
  public int getWidth ()
  {
    int max = 1;
    int l = commands.size ();
    for (int i = 0; i < l; i++)
    {
      PixieImageCommand cmd = (PixieImageCommand) commands.elementAt (i);
      int cmdW = cmd.getWidth ();
      if (cmdW > max)
        max = cmdW;
    }
    return max;
  }
  
  public int getHeight ()
  {
    int max = 1;
    int l = commands.size ();
    for (int i = 0; i < l; i++)
    {
      PixieImageCommand cmd = (PixieImageCommand) commands.elementAt (i);
      int cmdH = cmd.getHeight ();
      if (cmdH > max)
        max = cmdH;
    }
    return max;
  }
}
