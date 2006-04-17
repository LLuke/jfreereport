/**
 * ===========================================
 * LibFonts : a free Java font reading library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/libfonts/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * PlatformIdentifier.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.fonts.truetype.mappings;

/**
 * Creation-Date: 06.11.2005, 21:33:16
 *
 * @author Thomas Morgner
 */
public abstract class PlatformIdentifier
{
  public static final PlatformIdentifier UNICODE =
          new UnicodePlatformIdentifier();
  public static final PlatformIdentifier MACINTOSH =
          new MacintoshPlatformIdentifier();
  public static final PlatformIdentifier ISO =
          new IsoPlatformIdentifier();
  public static final PlatformIdentifier MICROSOFT =
          new MicrosoftPlatformIdentifier();

  private final int type; // for debug only

  protected PlatformIdentifier(int type)
  {
    this.type = type;
  }

  public int getType()
  {
    return type;
  }

  public String toString()
  {
    switch (type)
    {
      case 0:
        return "Unicode";
      case 1:
        return "Macintosh";
      case 2:
        return "ISO (deprecated)";
      case 3:
        return "Microsoft";
      default:
        return "Custom";
    }
  }

  public static PlatformIdentifier getIdentifier(int param)
  {

    switch (param)
    {
      case 0:
        return UNICODE;
      case 1:
        return MACINTOSH;
      case 2:
        return ISO;
      case 3:
        return MICROSOFT;
      default:
        return new CustomPlatformIdentifier(param);
    }
  }

  /**
   * Quoted from the OpenTypeSpecs:
   * <p/>
   * Note that OS/2 and Windows both require that all name strings be defined in
   * Unicode. Thus all 'name' table strings for platform ID = 3 (Microsoft) will
   * require two bytes per character. Macintosh fonts require single byte
   * strings.
   *
   * @param encodingId
   * @param language
   * @return
   */
  public abstract String getEncoding(int encodingId, int language);
}
