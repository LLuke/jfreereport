package org.jfree.fonts.truetype;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.jfree.fonts.ByteAccessUtilities;

/**
 * Creation-Date: 09.11.2005, 17:12:52
 *
 * @author Thomas Morgner
 */
public class TrueTypeCollection
{
  public static final long MAGIC_NUMBER =
          ('t' << 24 | 't' << 16 | 'c' << 8 | 'f');

  private File filename;
  private long numFonts;
  private long[] offsets;
  private TrueTypeFont[] fonts;

  public TrueTypeCollection(final File filename) throws IOException
  {
    this.filename = filename;

    final RandomAccessFile raf = new RandomAccessFile(filename, "r");
    final byte[] headerBuffer = new byte[12];
    raf.readFully(headerBuffer);
    if (ByteAccessUtilities.readULong(headerBuffer, 0) != MAGIC_NUMBER)
    {
      throw new IOException();
    }
    numFonts = ByteAccessUtilities.readLong(headerBuffer, 8);

    final byte[] offsetBuffer = new byte[(int) (4 * numFonts)];
    raf.readFully(offsetBuffer);

    final int size = (int) numFonts;
    offsets = new long[size];
    fonts = new TrueTypeFont[size];
    for (int i = 0; i < size; i++)
    {
      offsets[i] = ByteAccessUtilities.readULong(offsetBuffer, i * 4);
    }
  }

  public File getFilename()
  {
    return filename;
  }

  public long getNumFonts()
  {
    return numFonts;
  }

  public synchronized TrueTypeFont getFont(int index) throws IOException
  {
    final TrueTypeFont cachedFont = fonts[index];
    if (cachedFont != null)
    {
      return cachedFont;
    }
    final TrueTypeFont font = new TrueTypeFont(filename, offsets[index], index);
    fonts[index] = font;
    return font;
  }
}
