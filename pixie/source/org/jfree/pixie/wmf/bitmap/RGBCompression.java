package org.jfree.pixie.wmf.bitmap;

import java.io.IOException;
import java.io.InputStream;

public class RGBCompression extends BitmapCompression
{
  public int[] decompress (InputStream in, GDIPalette palette)
  throws IOException
  {
    int[] target = new int[getWidth () * getHeight ()];
    
    switch (getBpp ())
    {
      case 1:  fillMono  (target, in, palette); break;
      case 4:  fill4Bit  (target, in, palette); break;
      case 8:  fill8Bit  (target, in, palette); break;
      case 16: fill16Bit (target, in, palette); break;
      case 24: fill24Bit (target, in, palette); break;
      case 32: fill32Bit (target, in, palette); break;
    }
    return target;
  }
  
  /**
   * Cut or padd the string to the given size
   *
   * @param size the wanted length
   * @param padChar char to use for padding (must be of length()==1!)
   * @return the string with correct lenght, padded with pad if necessary
   */
  public static String forceToSizeLeft(String str, int size, char padChar)
  {
    if (str != null && str.length() == size)
      return str;

    StringBuffer tmp;
    if (str == null)
    {
      tmp = new StringBuffer(size);
    }
    else
    {
      tmp = new StringBuffer(str);
    }

    if (tmp.length() > size) 
    {
      tmp.setLength (size);
      return tmp.toString ();  // do cutting
    }
    else 
    {
      StringBuffer t2 = new StringBuffer (size);
      
      int arsize = size - tmp.length();
      char[] ar = new char[arsize];
      for (int i = 0; i < arsize; i++)
      {
        ar[i] = padChar;
      }
      t2.append (ar);
      t2.append (tmp);
      return t2.toString ();
    }
  }

  public void fillMono (int[] target, InputStream in, GDIPalette pal)
    throws IOException
  {
    int noOfBytes = (int) Math.ceil (target.length / 8);
    if (isTopDown () == false)
    {
      for (int i = noOfBytes - 1; i >= 0; i--)
      {
        int iByte = readInt (in);
        if (iByte == -1)
           return;
           
           
        int[] data = expandMonocrome (iByte, pal);
        int left = (target.length - i*8); 
        int size = Math.min (8, left);
      
        for (int ij = size - 1; ij >= 0; ij--)
        {
          target[7 - ij + i*8] = data[ij];
        }
      }
    }
    else
    {
      for (int i = 0; i < noOfBytes; i++)
      {
        int iByte = readInt (in);
        if (iByte == -1)
           return;
           
           
        int[] data = expandMonocrome (iByte, pal);
        int left = (target.length - i*8); 
        int size = Math.min (8, left);
    
        for (int ij = 0; ij < 8; ij++)
        {
          target[ij + i*8] = data[ij];
        }
      }
    }
  }

  public void fill4Bit (int[] target, InputStream in, GDIPalette pal)
  throws IOException
  {
    int noOfBytes = (int) Math.ceil (target.length / 2);

    if (isTopDown () == false)
    {
      for (int i = noOfBytes - 1; i >= 0; i--)
      {
        int iByte = in.read ();
        if (iByte == -1)
           return;
      
        int[] data = expand4BitTuple(iByte, pal);
        target[i * 2] = data[1];
        target[i * 2 + 1] = data[0];
      }
    }
    else
    {
      for (int i = 0; i < noOfBytes; i++)
      {
        int iByte = in.read ();
        if (iByte == -1)
           return;
    
        int[] data = expand4BitTuple(iByte, pal);
        target[i * 2] = data[0];
        target[i * 2 + 1] = data[1];
      }
    }
  }

  public void fill8Bit (int[] target, InputStream in, GDIPalette pal)
  throws IOException
  {
    int noOfBytes = target.length;
    if (isTopDown () == false)
    {
      for (int i = noOfBytes - 1; i >= 0; i--)
      {
        int iByte = in.read ();
        if (iByte == -1)
           return;
           
        target[i] = pal.lookupColor (iByte);
      }
    }
    else
    {
      for (int i = 0; i < noOfBytes; i++)
      {
        int iByte = in.read ();
        if (iByte == -1)
           return;
           
        target[i] = pal.lookupColor (iByte);
      }
    }
  }

  public void fill16Bit (int[] target, InputStream in, GDIPalette pal)
  throws IOException
  {
    int noOfBytes = target.length * 2;
    if (isTopDown () == false)
    {
      for (int i = noOfBytes - 1; i >= 0; i--)
      {
        int iByte = in.read ();
        if (iByte == -1) return;
        int iByte2 = in.read ();
        if (iByte2 == -1) return;
      
        target[i] = pal.lookupColor ((iByte2 << 8) + iByte);
      }
    }
    else
    {
      for (int i = 0; i < noOfBytes; i++)
      {
        int iByte = in.read ();
        if (iByte == -1) return;
        int iByte2 = in.read ();
        if (iByte2 == -1) return;
      
        target[i] = pal.lookupColor ((iByte2 << 8) + iByte);
      }
    }
  }

  public void fill24Bit (int[] target, InputStream in, GDIPalette pal)
  throws IOException
  {
    int noOfBytes = target.length * 4;
    if (isTopDown () == false)
    {
      for (int i = noOfBytes - 1; i >= 0; i--)
      {
        target[i] = pal.lookupColor (readInt (in));
      }
    }
    else
    {
      for (int i = 0; i < noOfBytes; i++)
      {
        target[i] = pal.lookupColor (readInt (in));
      }
    }
  }

  public void fill32Bit (int[] target, InputStream in, GDIPalette pal)
  throws IOException
  {
    int noOfBytes = target.length * 4;
    if (isTopDown () == false)
    {
      for (int i = noOfBytes - 1; i >= 0; i--)
      {
        target[i] = pal.lookupColor (readInt (in));
      }
    }
    else
    {
      for (int i = 0; i < noOfBytes; i++)
      {
        target[i] = pal.lookupColor (readInt (in));
      }
    }
  }
  
  protected int readInt (InputStream in) throws IOException
  {
    int iByte = in.read ();
    if (iByte == -1) return -1;
    int iByte2 = in.read ();
    if (iByte2 == -1) return - 1;
    int iByte3 = in.read ();
    if (iByte3 == -1) return - 1;
    int iByte4 = in.read ();
    if (iByte4 == -1) return -1;

      
    int retval = ((iByte4 << 24) + (iByte3 << 16) + (iByte2 << 8) + (iByte));
    return retval;
  }
}