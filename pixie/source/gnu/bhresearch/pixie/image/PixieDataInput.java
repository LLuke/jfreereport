package gnu.bhresearch.pixie.image;

import java.io.DataInput;
import java.io.InputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class PixieDataInput implements DataInput
{
  public static final int STEPS = 32;
  public static final float STEP = 1.0f/STEPS;
 
  public static final boolean READ_X = true;
  public static final boolean READ_Y = false;
  
  private DataInput in;
  private RandomAccessStream rasin;
  private RandomAccessFile rafin;
  
  public PixieDataInput (RandomAccessStream in)
  {
    this.in = in;
    this.rasin = in;
  }

  public PixieDataInput (RandomAccessFile in)
  {
    this.in = in;
    this.rafin = in;
  }

  public long getFilePointer () throws IOException
  {
    if (rasin == null)
      return rafin.getFilePointer();
      
    return rasin.getFilePointer ();  
  }

  public void seek (long pos) throws IOException
  {
    if (rasin == null)
      rafin.seek(pos);
    else  
      rasin.seek (pos);  
  }

  /** 
  * Load variable length integers from a nibble format -
  * 4 bits to a nibble. 
  */

  /**	
  * Second half of nibble from a byte. 
  */
  private int nibBuf = 0;

  /**
  * is the buffer filled?
  */
  private boolean nibBufFull = false;

  private int prevX;
  private int prevY;
  
//  private float scaleX;
//  private float scaleY;
//
  private int pixieWidth;
  private int pixieHeight;
  
  public void resetPrevXY ()
  {
    prevX = 0;
    prevY = 0;
    flushVInt ();
  }
  
  public void setImageSize (int w, int h)
  {
    System.out.println ("Reader: Size: " + w  + " " + h);
    this.pixieWidth = w;
    this.pixieHeight = h;
  }

//  public void setScale (float scaleX, float scaleY)
//  {
//    System.out.println ("Reader: Scale: " + scaleX  + " " + scaleY);
//    this.scaleX = scaleX;
//    this.scaleY = scaleY;
//  }
//
  public int readWidth () throws IOException
  {
    int length = readUnsignedVInt();
    if (length == 0)
    {
      length = pixieWidth;
    }
    prevX  += length;
    return (length == 0) ? 1 : length;
  }
  
  public int readHeight () throws IOException
  {
    int length = readUnsignedVInt();
    if (length == 0)
    {
      length = pixieHeight;
    }
    prevY += length;
    return (length == 0) ? 1 : length;
  }

  /** 
   * Return integer scaled to output units. 
   */
  public int readVIntY( ) throws IOException
  {
    prevY += readVInt();
    return prevY;
  }

  /** 
   * Return integer scaled to output units. 
   */
  public int readVIntX () throws IOException
  {
    prevX += readVInt();
    return prevX;
  }

  /** Return signed integer. */
  public int readVInt() throws IOException
  {
    int result = readUnsignedVInt();
    // Sign is stored in lowest bit.
    if ((result & 0x01) == 0)
    {
      return (result >> 1);
    }
    else
    {
      return (0 - result - 1) >> 1;
    }
  }

  /** 
   * Return unsigned integer. The core variable length int routine. 
   */
  public int readUnsignedVInt() throws IOException
  {
    int result = 0;
    while (true)
    {
      // Read the next 4-bit nibble.
      int nibble;
      if (nibBufFull)
      {
        nibBufFull = false;
        // Use previously read nibble.
        nibble = nibBuf;
      }
      else
      {
        nibBufFull = true;
        // Use the low bits of a new byte, and save
        // the high bits for next time.
        nibble = in.readByte();
        nibBuf = nibble >> 4;
      }

      // Extract low 3 bits from nibble.
      result = (result << 3) | (nibble & 0x07);

      // Last nibble?
      if ((nibble & 0x08) == 0)
      {
        return result;
      }
    }
  }

  public void flushVInt()
  {
    nibBufFull = false;
  }


  public int[] readPointsX( int outCnt, boolean[] ht)
    throws IOException
  {
    int prev = prevX;
    int dest[] = new int[ outCnt ];
    for (int h = 0, dOffset = 0; dOffset < outCnt; h++)
    {
      if (ht[h])
      {
        // Bezier consumes 3 handles.
        float s0 = prev;
        float s1 = prev += readVInt();
        float s2 = prev += readVInt();
        float s3 = prev += readVInt();

        float k1 = -3.0f * (s0-s1);
        float k2 = 3.0f * ((s0+s2) - 2.0f*s1);
        float k3 = -s0 + 3.0f * (s1-s2) + s3;

        for (float t = STEP; t <= 1.0f; t += STEP)
        {
          dest[dOffset++] = (int)(s0 + t*(k1 + t*(k2 + t*k3)) + 0.5f);
        }
      }
      else
      {
        // Simple straight line segment.
        
        prev += readVInt();
        dest[dOffset++] = (int)(prev);
      }
    }
    prevX = prev;
    return dest;
  }

  public int[] readPointsY( int outCnt, boolean[] ht)
    throws IOException
  {
    int prev = prevY;
//    float scale = scaleY;
    int dest[] = new int[ outCnt ];
    for (int h = 0, dOffset = 0; dOffset < outCnt; h++)
    {
      if (ht[h])
      {
        // Bezier consumes 3 handles.
        float s0 = prev;
        float s1 = prev += readVInt();
        float s2 = prev += readVInt();
        float s3 = prev += readVInt();

        float k1 = -3.0f * (s0-s1);
        float k2 = 3.0f * ((s0+s2) - 2.0f*s1);
        float k3 = -s0 + 3.0f * (s1-s2) + s3;

        for (float t = STEP; t <= 1.0f; t += STEP)
        {
          dest[dOffset++] = (int)(s0 + t*(k1 + t*(k2 + t*k3)) + 0.5f);
        }
      }
      else
      {
        // Simple straight line segment.
        
        prev += readVInt();
        dest[dOffset++] = (int)(prev);
      }
    }
    prevY = prev;
    return dest;
  }

  public boolean readBoolean()  throws IOException
  {
    boolean b = in.readBoolean ();
    return b;
  }
  
  public byte readByte()  throws IOException
  {
    byte b = in.readByte ();
    return b;
  }
   
  public char readChar()  throws IOException
  {
    char c = in.readChar();
    return c;
  }
          
  public double readDouble()  throws IOException
  {
    double d = in.readDouble ();
    return d;
  }
          
  public float readFloat()  throws IOException
  {
    float f  =in.readFloat ();
    return f;
  }
          
  public void readFully(byte[] b)  throws IOException
  {
    in.readFully (b);
  }
          
  public void readFully(byte[] b, int off, int len)  throws IOException
  {
    in.readFully (b, off, len);
  }
          
  public int readInt()  throws IOException
  {
    int i =  in.readInt ();
    return i;
  }
          
  public String readLine()  throws IOException
  {
    return in.readLine ();
  }
          
  public long readLong()  throws IOException
  {
    return in.readLong ();
  }
          
  public short readShort()  throws IOException
  {
    return in.readShort ();
  }
          
  public int readUnsignedByte()  throws IOException
  {
    return in.readUnsignedByte();
  }

  public int readUnsignedShort()  throws IOException
  {
    return in.readUnsignedShort();
  }
   
  public String readUTF() throws IOException
  {
    return in.readUTF();
  }
  
  public int skipBytes(int n)  throws IOException
  {
    return in.skipBytes (n);
  }
  
  public void close () throws IOException
  {
    if (rasin == null)
      rafin.close ();
    else  
      rasin.close ();  
  
  }
}
