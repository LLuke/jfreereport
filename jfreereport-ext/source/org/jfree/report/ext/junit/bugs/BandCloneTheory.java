/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2002, by Simba Management Limited and Contributors.
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
 * ----------------
 * BandCloneTheory.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BandCloneTheory.java,v 1.1 2003/07/08 14:21:47 taqua Exp $
 *
 * Changes
 * -------
 * 04.04.2003 : Initial version
 */
package org.jfree.report.ext.junit.bugs;

import java.awt.geom.Dimension2D;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.TextElement;
import org.jfree.report.layout.LayoutCacheKey;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.util.Log;
import org.jfree.ui.FloatDimension;

public class BandCloneTheory
{
  public static void main(final String[] args) throws Exception
  {
    final ElementStyleSheet es = new ElementStyleSheet("Ahar");

    final TextElement te = new TextElement();
    te.getStyle().setAllowCaching(true);
    te.getStyle().addParent(es);
    final Band b = new Band();
    b.addElement(te);
    Log.debug(b.getStyle().getParents());
    Log.debug(b.getStyle().getDefaultParents());
    Log.debug(te.getStyle().getParents());
    Log.debug(te.getStyle().getDefaultParents());

    final Band b2 = (Band) b.clone();
    final Element e2 = b2.getElement(0);
    Log.debug(b2.getStyle().getParents());
    Log.debug(b2.getStyle().getDefaultParents());

    Log.debug(e2.getStyle().getParents());
    Log.debug(e2.getStyle().getDefaultParents());

    final Dimension2D pDim = new FloatDimension(123, 123);
    final LayoutCacheKey key = new LayoutCacheKey(te, pDim);
    final LayoutCacheKey key2 = new LayoutCacheKey(e2, pDim);
    Log.debug("Equal: " + key.equals(key2) + " -> " + key.hashCode() + " -> " + key2.hashCode());
  }
}
