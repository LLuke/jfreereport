/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BandCloneTheory.java,v 1.2 2003/04/23 17:32:36 taqua Exp $
 *
 * Changes
 * -------
 * 04.04.2003 : Initial version
 */
package com.jrefinery.report.ext.junit.bugs;

import java.awt.geom.Dimension2D;

import com.jrefinery.report.Band;
import com.jrefinery.report.Element;
import com.jrefinery.report.TextElement;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.targets.base.bandlayout.LayoutCacheKey;
import org.jfree.ui.FloatDimension;
import com.jrefinery.report.util.Log;

public class BandCloneTheory
{
  public static void main (String [] args) throws Exception
  {
    ElementStyleSheet es = new ElementStyleSheet("Ahar");

    TextElement te = new TextElement();
    te.getStyle().setAllowCaching(true);
    te.getStyle().addParent(es);
    Band b = new Band ();
    b.addElement(te);
    Log.debug (b.getStyle().getParents());
    Log.debug (b.getStyle().getDefaultParents());
    Log.debug (te.getStyle().getParents());
    Log.debug (te.getStyle().getDefaultParents());

    Band b2 = (Band) b.clone();
    Element e2 = b2.getElement(0);
    Log.debug (b2.getStyle().getParents());
    Log.debug (b2.getStyle().getDefaultParents());

    Log.debug (e2.getStyle().getParents());
    Log.debug (e2.getStyle().getDefaultParents());

    Dimension2D pDim = new FloatDimension(123,123);
    LayoutCacheKey key = new LayoutCacheKey(te, pDim);
    LayoutCacheKey key2 = new LayoutCacheKey(e2, pDim);
    Log.debug ("Equal: " + key.equals(key2) + " -> " + key.hashCode() + " -> " + key2.hashCode());
  }
}
