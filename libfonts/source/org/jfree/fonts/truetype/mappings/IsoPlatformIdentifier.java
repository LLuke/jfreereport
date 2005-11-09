package org.jfree.fonts.truetype.mappings;

import org.jfree.fonts.truetype.mappings.PlatformIdentifier;

/**
 * Creation-Date: 07.11.2005, 15:27:14
 *
 * @author Thomas Morgner
 */
public class IsoPlatformIdentifier extends PlatformIdentifier
{
  public IsoPlatformIdentifier()
  {
    super(2);
  }

  public String getEncoding(int encodingId, int language)
  {
    if (encodingId == 0)
    {
      return "US_ASCII";
    }
    else if (encodingId == 1)
    {
      return "UTF-16";
    }
    return "ISO-8859-1";
  }
}
