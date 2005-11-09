package org.jfree.fonts.truetype;

import java.io.UnsupportedEncodingException;
import java.util.TreeSet;

import org.jfree.fonts.ByteAccessUtilities;
import org.jfree.fonts.LanguageCode;
import org.jfree.fonts.truetype.mappings.PlatformIdentifier;

/**
 * Creation-Date: 06.11.2005, 20:24:42
 *
 * @author Thomas Morgner
 */
public class NameTable implements FontTable
{
  public static class NameRecord
  {
    private PlatformIdentifier platformId;
    private int platformEncodingId;
    private int languageId;
    private int nameId;
    private String name;

    public NameRecord(final byte[] data,
                      final int recordOffset,
                      final int stringOffset)
            throws UnsupportedEncodingException
    {
      platformId = PlatformIdentifier.getIdentifier(
              ByteAccessUtilities.readUShort(data, recordOffset));
      platformEncodingId = ByteAccessUtilities.readUShort(data,
              recordOffset + 2);
      languageId = ByteAccessUtilities.readUShort(data, recordOffset + 4);
      nameId = ByteAccessUtilities.readUShort(data, recordOffset + 6);
      final int length = ByteAccessUtilities.readUShort(data, recordOffset + 8);
      final int offset = ByteAccessUtilities.readUShort(data,
              recordOffset + 10);
      name = ByteAccessUtilities.readString
              (data, stringOffset + offset, length,
                      platformId.getEncoding(platformEncodingId, languageId));
    }

    public PlatformIdentifier getPlatformId()
    {
      return platformId;
    }

    public int getPlatformEncodingId()
    {
      return platformEncodingId;
    }

    public int getLanguageId()
    {
      return languageId;
    }

    public int getNameId()
    {
      return nameId;
    }

    public String getName()
    {
      return name;
    }
  }

  public static final int NAME_COPYRIGHT = 0;
  public static final int NAME_FAMILY = 1;
  public static final int NAME_SUBFAMILY = 2;
  public static final int NAME_UNIQUE_SUBFAMILY = 3;
  public static final int NAME_FULLNAME = 4;
  public static final int NAME_VERSION = 5;
  public static final int NAME_POSTSCRIPT = 6;
  public static final int NAME_TRADEMARK = 7;
  public static final int NAME_MANUFACTURER = 8;
  public static final int NAME_DESIGNER = 9;

  public static final int NAME_DESCRIPTION = 10;
  public static final int NAME_VENDOR_URL = 11;
  public static final int NAME_DESIGNER_URL = 12;
  public static final int NAME_LICENCE_DESCRIPTION = 13;
  public static final int NAME_LICENCE_URL = 14;

  public static final int NAME_RESERVED = 15;
  public static final int NAME_PREFERRED_FAMILY = 16;
  public static final int NAME_PREFERRED_SUBFAMILY = 17;
  public static final int NAME_COMPATIBLE_FULL = 18;
  public static final int NAME_SAMPLE_TEXT = 19;

  public static final long TABLE_ID =
          ('n' << 24 | 'a' << 16 | 'm' << 8 | 'e');

  // deprecated: Format selector.
  private int format;
  private int recordCount;
  private int stringOffset;
  private NameRecord[] names;

  public NameTable(final byte[] buffer) throws UnsupportedEncodingException
  {
    format = 0; // const ...
    recordCount = ByteAccessUtilities.readUShort(buffer, 2);
    stringOffset = ByteAccessUtilities.readUShort(buffer, 4);
    names = new NameRecord[recordCount];
    for (int i = 0; i < recordCount; i++)
    {
      names[i] = new NameRecord(buffer, 6 + i * 12, stringOffset);
    }
  }

  public String getName(final int type,
                        final PlatformIdentifier platformId,
                        final int platformEncoding,
                        final int rawLanguage)
  {
    for (int i = 0; i < names.length; i++)
    {
      NameRecord name = names[i];
      if (name.getPlatformId().equals(platformId) &&
              name.getPlatformEncodingId() == platformEncoding &&
              name.getLanguageId() == rawLanguage &&
              name.getNameId() == type)
      {
        return name.getName();
      }
    }
    return null;
  }

  public String getName(final int type, final LanguageCode language)
  {
    for (int i = 0; i < names.length; i++)
    {
      NameRecord name = names[i];
      if (name.getNameId() != type)
      {
        continue;
      }
      if (name.getLanguageId() == language.getCode())
      {
        return name.getName();
      }
    }
    return null;
  }

  public int getFormat()
  {
    return format;
  }

  public int getRecordCount()
  {
    return recordCount;
  }

  public int getStringOffset()
  {
    return stringOffset;
  }

  public NameRecord[] getNameRecords()
  {
    return (NameRecord[]) names.clone();
  }

  public NameRecord getNameRecord(int pos)
  {
    return names[pos];
  }

  public String getPrimaryName(int type)
  {
    String unicodeFallback = null;

    for (int i = 0; i < names.length; i++)
    {
      NameRecord name = names[i];
      if (name.getNameId() != type)
      {
        continue;
      }

      if (name.getPlatformId().equals(PlatformIdentifier.MACINTOSH))
      {
        if (name.getLanguageId() ==
                LanguageCode.MacLanguageCode.ENGLISH.getCode())
        {
          return name.getName();
        }
      }

      if (name.getPlatformId().equals(PlatformIdentifier.MICROSOFT))
      {
        if (name.getLanguageId() ==
                LanguageCode.MicrosoftLanguageCode.ENGLISH_US.getCode())
        {
          return name.getName();
        }
      }

      if (name.getPlatformId().equals(PlatformIdentifier.UNICODE))
      {
        unicodeFallback = name.getName();
      }
      if (unicodeFallback != null)
      {
        // use any name ...
        unicodeFallback = name.getName();
      }
    }
    return unicodeFallback;
  }

  public String[] getAllNames(int type)
  {
    final TreeSet retvalCollector = new TreeSet();

    for (int i = 0; i < names.length; i++)
    {
      NameRecord name = names[i];
      if (name.getNameId() != type)
      {
        continue;
      }

      retvalCollector.add(name.getName());
    }
    return (String[]) retvalCollector.toArray(new String[retvalCollector.size()]);
  }

  public long getName()
  {
    return TABLE_ID;
  }
}
