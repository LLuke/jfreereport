package gnu.bhresearch.pixie.image;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RandomAccessStream extends InputStream implements DataInput
{
  private ByteArrayInputStream bytein;
  private DataInputStream datain;
  private byte[] data;
  private long pos = 0;
  private long markpos = 0;

  public static class InputCarrier
  {
    public byte[] data;
  }

  public RandomAccessStream (InputStream in)
          throws IOException
  {
    ByteArrayOutputStream byteout = new ByteArrayOutputStream ();
    byte[] buffer = new byte[4096];

    int readResult = in.read (buffer);
    while (readResult > -1)
    {
      byteout.write (buffer, 0, readResult);
      readResult = in.read (buffer);
    }

    data = byteout.toByteArray ();
    bytein = new ByteArrayInputStream (data);
    datain = new DataInputStream (this);
  }

  public int available ()
          throws IOException
  {
    return bytein.available ();
  }

  public void close ()
          throws IOException
  {
    bytein.close ();
  }

  public void mark (int readlimit)
  {
    bytein.mark (readlimit);
    markpos = pos;
  }

  public boolean markSupported ()
  {
    return bytein.markSupported ();
  }

  public int read ()
          throws IOException
  {
    int retval = bytein.read ();
    pos++;
    return retval;
  }

  public int read (byte[] b)
          throws IOException
  {
    int retval = bytein.read (b);
    pos += retval;
    return retval;
  }

  public int read (byte[] b, int off, int len)
          throws IOException
  {
    int retval = bytein.read (b, off, len);
    pos += retval;
    return retval;
  }

  public void reset ()
          throws IOException
  {
    pos = markpos;
    bytein.reset ();
  }

  public long skip (long n)
          throws IOException
  {
    long retval = bytein.skip (n);
    pos += retval;
    return retval;
  }

  public void seek (long pos)
  {
    bytein = new ByteArrayInputStream (data);
    bytein.skip (pos);
    this.pos = pos;
  }

  public long getFilePointer ()
  {
    return pos;
  }

  public boolean readBoolean () throws IOException
  {
    return datain.readBoolean ();
  }

  public byte readByte () throws IOException
  {
    return datain.readByte ();
  }

  public char readChar () throws IOException
  {
    return datain.readChar ();
  }

  public double readDouble () throws IOException
  {
    return datain.readDouble ();
  }

  public float readFloat () throws IOException
  {
    return datain.readFloat ();
  }

  public void readFully (byte[] b) throws IOException
  {
    datain.readFully (b);
  }

  public void readFully (byte[] b, int off, int len) throws IOException
  {
    datain.readFully (b, off, len);
  }

  public int readInt () throws IOException
  {
    return datain.readInt ();
  }

  public String readLine () throws IOException
  {
    return datain.readLine ();
  }

  public long readLong () throws IOException
  {
    return datain.readLong ();
  }

  public short readShort () throws IOException
  {
    return datain.readShort ();
  }

  public int readUnsignedByte () throws IOException
  {
    return datain.readUnsignedByte ();
  }

  public int readUnsignedShort () throws IOException
  {
    return datain.readUnsignedShort ();
  }

  public String readUTF () throws IOException
  {
    return datain.readUTF ();
  }

  public int skipBytes (int n) throws IOException
  {
    return datain.skipBytes (n);
  }

}
