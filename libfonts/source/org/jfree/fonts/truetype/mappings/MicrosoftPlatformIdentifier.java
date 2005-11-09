package org.jfree.fonts.truetype.mappings;

/**
 * Creation-Date: 07.11.2005, 15:29:42
 *
 * @author Thomas Morgner
 */
public class MicrosoftPlatformIdentifier extends PlatformIdentifier
{
  public MicrosoftPlatformIdentifier()
  {
    super(3);
  }

  /**
   * For now, copy the assumption of iText: Always assume UFT-16.
   *
   * @param encodingId
   * @param language
   * @return
   */
  public String getEncoding(int encodingId, int language)
  {
    return "UTF-16";
  }
}
