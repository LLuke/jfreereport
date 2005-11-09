package org.jfree.fonts.truetype.mappings;

/**
 * Creation-Date: 06.11.2005, 21:44:21
 *
 * @author Thomas Morgner
 */
public class UnicodePlatformIdentifier extends PlatformIdentifier
{
  public UnicodePlatformIdentifier()
  {
    super (0);
  }

  /**
   * According to the Apple OpenType specifications, all Unicode characters
   * must be encoded using UTF-16. Depending on the encodingId, some blocks
   * may be interpreted differently. LibFont ignores that and uses the
   * Java-Default UTF-16 mapping.
   *
   * @param encodingId
   * @param language
   * @return the encoding, always "UTF-16"
   * @see http://developer.apple.com/fonts/TTRefMan/RM06/Chap6name.html#ID
   */
  public String getEncoding(int encodingId, int language)
  {
    return "UTF-16";
  }
}
