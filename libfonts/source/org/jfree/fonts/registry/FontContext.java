/**
 * ===========================================
 * LibFonts : a free Java font reading library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libfonts/
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
 * $Id: FontContext.java,v 1.4 2006/12/03 18:11:59 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.fonts.registry;

/**
 * The font context decribes how a certain font will be used. The context
 * influences the font metrics and therefore a certain metrics object is
 * only valid for a given font context.
 *
 * @author Thomas Morgner
 */
public interface FontContext
{
  public String getEncoding(); 

  public boolean isEmbedded();

  /**
   * This is controlled by the output target and the stylesheet. If the
   * output target does not support aliasing, it makes no sense to enable
   * it and all such requests are ignored.
   *
   * @return
   */
  public boolean isAntiAliased();

  /**
   * This is defined by the output target. This is not controlled by the
   * stylesheet.
   *
   * @return
   */
  public boolean isFractionalMetrics();

  /**
   * The requested font size. A font may have a fractional font size
   * (ie. 8.5 point). The font size may be influenced by the output target.
   *
   * @return the font size.
   */
  public double getFontSize();

}
