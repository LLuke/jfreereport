package gnu.bhresearch.pixie.command;

import gnu.bhresearch.pixie.image.PixieDataInput;

import java.awt.Graphics;
import java.io.IOException;

public class Comment extends PixieImageCommand
{
  public Comment (PixieDataInput nl)
          throws IOException
  {
    nl.readUnsignedVInt ();		// Ignore type.
    int len = nl.readUnsignedVInt ();
    nl.flushVInt ();
    nl.skipBytes (len);	// Ignore everything.
  }

  public Comment (String text)
  {
  }

  public Comment (int type, byte[] data)
  {
  }

  public Comment (int type, long data)
  {
  }

  public Comment (int type, String data)
  {
  }

  public void paint (Graphics g)
  {
  }

  public int getWidth ()
  {
    return 0;
  }

  public int getHeight ()
  {
    return 0;
  }


}

