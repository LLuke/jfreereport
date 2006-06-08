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
 * FontMetrics.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: FontMetrics.java,v 1.5 2006/04/30 09:31:14 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.fonts.registry;

/**
 * Font-metrics are computed for a certain font-size and style. There are
 * no general metrics, which are assumed to be globally available.
 *
 * The use of these font metrics is application dependent. As building these
 * metrics usually is memory and CPU intensive, this must be done in an
 * application specific context. You certainly want to cache the font data. 
 *
 * @author Thomas Morgner
 */
public interface FontMetrics
{
  /**
   * From the baseline to the
   * @return
   */
  public double getAscent();
  public double getDescent();
  public double getLeading();

  /**
   * The height of the lowercase 'x'. This is used as hint, which size the
   * lowercase characters will have.
   * 
   * @return
   */
  public double getXHeight();
  public double getOverlinePosition();
  public double getUnderlinePosition();
  public double getStrikeThroughPosition();

  public double getMaxAscent();
  public double getMaxDescent();
  public double getMaxLeading();
  public double getMaxHeight();

  public double getMaxCharAdvance();

  public double getCharWidth(int codePoint);
  public double getKerning(int previous, int codePoint);


}
