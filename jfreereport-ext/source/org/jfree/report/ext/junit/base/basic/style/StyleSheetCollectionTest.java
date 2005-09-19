/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * ------------------------------
 * StyleSheetCollectionTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: StyleSheetCollectionTest.java,v 1.6 2005/01/31 17:16:34 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 18.06.2003 : Initial version
 *  
 */

package org.jfree.report.ext.junit.base.basic.style;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.StyleSheetCollection;


public class StyleSheetCollectionTest extends TestCase
{
  public StyleSheetCollectionTest()
  {
  }

  public StyleSheetCollectionTest(final String s)
  {
    super(s);
  }

  public void testSimpleClone ()
          throws CloneNotSupportedException
  {
    final StyleSheetCollection sc = new StyleSheetCollection();
    final ElementStyleSheet es1 = sc.createStyleSheet("one");
    final ElementStyleSheet es2 = sc.createStyleSheet("two");
    final ElementStyleSheet es3 = sc.createStyleSheet("three");
    final ElementStyleSheet es4 = sc.createStyleSheet("four");

    es1.addParent(es2);
    es1.addParent(es4);

    es2.addParent(es3);
    es3.addParent(es4);

    try
    {
      es4.addParent(es1);
      fail("Loop not detected");
    }
    catch(Exception e)
    {

    }

    final StyleSheetCollection scc = (StyleSheetCollection) sc.clone();
    final ElementStyleSheet esc1 = scc.getStyleSheet("one");
    final ElementStyleSheet esc2 = scc.getStyleSheet("two");
    final ElementStyleSheet esc3 = scc.getStyleSheet("three");
    final ElementStyleSheet esc4 = scc.getStyleSheet("four");

    assertEquals(es1.getId(), esc1.getId());
    assertEquals(es2.getId(), esc2.getId());
    assertEquals(es3.getId(), esc3.getId());
    assertEquals(es4.getId(), esc4.getId());

    final List parents = Arrays.asList(esc1.getParents());
    assertTrue(parents.contains(esc2));
    assertTrue(parents.contains(esc4));
    assertFalse(parents.contains(es2));
    assertFalse(parents.contains(es4));

    final List parentsOriginal = Arrays.asList(es1.getParents());
    assertTrue(parentsOriginal.contains(es2));
    assertTrue(parentsOriginal.contains(es4));
    assertFalse(parentsOriginal.contains(esc2));
    assertFalse(parentsOriginal.contains(esc4));
  }

  public void testForeignStyles ()
          throws CloneNotSupportedException
  {
    final StyleSheetCollection sc = new StyleSheetCollection();
    final ElementStyleSheet es1 = sc.createStyleSheet("one");
    final ElementStyleSheet es2 = sc.createStyleSheet("two");

    es1.addParent(es2);

    final StyleSheetCollection scc = (StyleSheetCollection) sc.clone();
    final ElementStyleSheet esc1 = scc.getStyleSheet("one");
    final ElementStyleSheet esc2 = scc.getStyleSheet("two");
    final List parents = Arrays.asList(esc1.getParents());
    assertTrue(parents.contains(esc2));
  }

}
