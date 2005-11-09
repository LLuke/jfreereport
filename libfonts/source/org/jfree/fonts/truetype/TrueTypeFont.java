/**
 * ========================================
 * libLayout : a free Java font reading library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * ---------
 * TrueTypeFont.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 2005-11-07 : Initial version
 */
package org.jfree.fonts.truetype;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import org.jfree.fonts.ByteAccessUtilities;

/**
 * Creation-Date: 06.11.2005, 18:27:21
 *
 * @author Thomas Morgner
 */
public class TrueTypeFont
{
  private static class TrueTypeFontHeader
  {
    public static final int ENTRY_LENGTH = 12;

    private long version;
    private int numTables;
    private int searchRange;
    private int entrySelector;
    private int rangeShift;

    public TrueTypeFontHeader(final byte[] data)
    {
      this.version = ByteAccessUtilities.readULong(data, 0);
      this.numTables = ByteAccessUtilities.readUShort(data, 4);
      this.searchRange = ByteAccessUtilities.readUShort(data, 6);
      this.entrySelector = ByteAccessUtilities.readUShort(data, 8);
      this.rangeShift = ByteAccessUtilities.readUShort(data, 10);
    }

    public long getVersion()
    {
      return version;
    }

    public int getNumTables()
    {
      return numTables;
    }

    public int getSearchRange()
    {
      return searchRange;
    }

    public int getEntrySelector()
    {
      return entrySelector;
    }

    public int getRangeShift()
    {
      return rangeShift;
    }
  }

  private static class TableDirectoryEntry
  {
    public static final int ENTRY_LENGTH = 16;

    private long tag;
    private long checkSum;
    private int offset;
    private int length;
    private FontTable table;

    public TableDirectoryEntry(final byte[] data, final int offset)
    {
      this.tag = ByteAccessUtilities.readULong(data, offset);
      this.checkSum = ByteAccessUtilities.readULong(data, offset + 4);
      this.offset = (int) ByteAccessUtilities.readULong(data, offset + 8);
      this.length = (int) ByteAccessUtilities.readULong(data, offset + 12);
    }

    public long getTag()
    {
      return tag;
    }

    public long getCheckSum()
    {
      return checkSum;
    }

    public int getOffset()
    {
      return offset;
    }

    public int getLength()
    {
      return length;
    }

    public FontTable getTable()
    {
      return table;
    }

    public void setTable(final FontTable table)
    {
      this.table = table;
    }
  }

  private static class TemporaryRandomAccessFile extends RandomAccessFile
  {
    private boolean closed;

    public TemporaryRandomAccessFile(String name)
            throws FileNotFoundException
    {
      super(name, "r");
    }

    public TemporaryRandomAccessFile(File file)
            throws IOException
    {
      super(file, "r");
    }

    public boolean isClosed()
    {
      return closed;
    }

    public void close() throws IOException
    {
      closed = true;
      super.close();
    }
  }

  private long offset;
  private File filename;
  private transient TemporaryRandomAccessFile input;
  private transient byte[] readBuffer;
  private TableDirectoryEntry[] directory;
  private TrueTypeFontHeader header;
  private int collectionIndex;

  public TrueTypeFont(final File filename)
          throws IOException
  {
    this (filename, 0, -1);
  }

  public TrueTypeFont(final File filename, final long offset)
          throws IOException
  {
    this (filename,  offset, -1);
  }

  public TrueTypeFont(final File filename, final long offset, final int collectionIndex)
          throws IOException
  {
    if (offset < 0)
    {
      throw new IndexOutOfBoundsException();
    }
    this.collectionIndex = collectionIndex;
    this.offset = offset;
    this.filename = filename;

    this.header = new TrueTypeFontHeader
            (readFully(offset, TrueTypeFontHeader.ENTRY_LENGTH));
    this.directory = readTableDirectory();
  }

  public int getCollectionIndex()
  {
    return collectionIndex;
  }

  private TableDirectoryEntry[] readTableDirectory () throws IOException
  {
    final int numTables = header.getNumTables();
    final int directorySize =
            numTables * TableDirectoryEntry.ENTRY_LENGTH;
    final byte[] directoryData =
            readFully(offset + TrueTypeFontHeader.ENTRY_LENGTH, directorySize);
    final TableDirectoryEntry[] directory = new TableDirectoryEntry[numTables];
    for (int i = 0; i < header.getNumTables(); i += 1)
    {
      final int dirOffset = TableDirectoryEntry.ENTRY_LENGTH * i;
      directory[i] = new TableDirectoryEntry(directoryData, dirOffset);
    }
    return directory;
  }

  protected final synchronized RandomAccessFile getFile()
          throws IOException
  {
    if (input == null || input.isClosed())
    {
      input = new TemporaryRandomAccessFile(filename);
    }
    return input;
  }

  protected synchronized byte[] readFully(final long offset, final int length)
          throws IOException
  {
    if (readBuffer == null)
    {
      readBuffer = new byte[Math.max(8192, length)];
    }
    else if (readBuffer.length < length)
    {
      readBuffer = new byte[length];
    }

    final RandomAccessFile input = getFile();
    input.seek(offset);
    input.readFully(readBuffer, 0, length);
    if ((readBuffer.length - length) > 0)
    {
      Arrays.fill(readBuffer, length, readBuffer.length, (byte) 0);
    }
    return readBuffer;
  }

  public long getOffset()
  {
    return offset;
  }

  public File getFilename()
  {
    return filename;
  }

  public FontTable getTable (long key) throws IOException
  {
    for (int i = 0; i < directory.length; i++)
    {
      final TableDirectoryEntry entry = directory[i];
      if (entry.getTag() == key)
      {
        final FontTable table = entry.getTable();
        if (table != null)
        {
          return table;
        }
        final FontTable readTable = readTable(entry);
        entry.setTable(readTable);
        return readTable;
      }
    }
    // no such table in the font ..
    return null;
  }

  protected FontTable readTable (TableDirectoryEntry table) throws IOException
  {
    final byte[] buffer =
            readFully(table.getOffset(), table.getLength());
    if (table.getTag() == NameTable.TABLE_ID)
    {
      return new NameTable(buffer);
    }
    if (table.getTag() == FontHeaderTable.TABLE_ID)
    {
      return new FontHeaderTable(buffer);
    }
    return null;
  }

  public void dispose ()
  {
    if (input != null)
    {
      try
      {
        input.close();
      }
      catch (IOException e)
      {
        // ignore for now ..
      }
      input = null;
    }
  }

  protected void finalize() throws Throwable
  {
    super.finalize();
    dispose();
  }
}
