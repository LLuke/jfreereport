/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * FunctionUtilsTest.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: JCommon.java,v 1.1 2004/07/15 14:49:46 mungady Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.ext.junit.base.basic.function;

import junit.framework.TestCase;
import org.jfree.report.ItemBand;
import org.jfree.report.Band;
import org.jfree.report.function.FunctionUtilities;

public class FunctionUtilsTest extends TestCase
{
  public FunctionUtilsTest (final String s)
  {
    super(s);
  }

  public void testFindElement ()
  {
    /*
    >band  name="landscape" x="0" y="0" vertical-alignment="middle" fontname="Arial Unicode MS" fontstyle="bold" fontsize="18">

<imageref     x="0"   y="0"  width="60"   height="25" src="logoiwprs200.gif" keepAspectRatio="true" scale="true"/>


<band name="noLate">
<string-field x="282" y="140" width="120" height="20" fontsize="14" alignment="left" fieldname="strLate"/>
</band>

<string-field x="620" y="160" width="75"  height="20" fontsize="10" alignment="center" fieldname="strStudentID"/>
</band>
*/

    final Band noLate = new Band();
    noLate.setName("noLate");

    final Band landScape = new Band();
    landScape.setName("landscape");
    landScape.addElement(noLate);

    final ItemBand band = new ItemBand();
    band.addElement(landScape);

    assertEquals(noLate, FunctionUtilities.findElement(band, "noLate"));
  }
}
