package org.jfree.fonts.truetype.mappings;

/**
 * Creation-Date: 07.11.2005, 15:42:07
 *
 * @author Thomas Morgner
 */
public class CustomPlatformIdentifier extends PlatformIdentifier
{
  public CustomPlatformIdentifier(int type)
  {
    super(type);
  }

  public String getEncoding(int encodingId, int language)
  {
    return "ISO-8859-1";
  }
}
