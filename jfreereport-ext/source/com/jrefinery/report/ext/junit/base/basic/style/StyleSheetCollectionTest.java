/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * $Id$
 *
 * Changes 
 * -------------------------
 * 18.06.2003 : Initial version
 *  
 */

package com.jrefinery.report.ext.junit.base.basic.style;

import java.util.List;

import junit.framework.TestCase;
import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.targets.style.StyleSheetCollection;
import com.jrefinery.report.targets.style.ElementDefaultStyleSheet;
// todo write me!
public class StyleSheetCollectionTest extends TestCase
{
  public StyleSheetCollectionTest()
  {
  }

  public StyleSheetCollectionTest(String s)
  {
    super(s);
  }

  public void testCollectSimple ()
  {
    ElementStyleSheet es1 = new ElementStyleSheet("es1");
    ElementStyleSheet es2 = new ElementStyleSheet("es2");
    ElementStyleSheet es3 = new ElementStyleSheet("es3");
    ElementStyleSheet es2a = new ElementStyleSheet("es2a");
    ElementStyleSheet es1a = new ElementStyleSheet("es1a");

    es2.addParent(es3);
    es1.addParent(es2);

    es2a.addParent(es3);
    es1a.addParent(es2a);

    StyleSheetCollection sc = new StyleSheetCollection();
    es1.setStyleSheetCollection(sc);
    es1a.setStyleSheetCollection(sc);
  }

  public void testCollectSimpleCrash ()
  {
    ElementStyleSheet es1 = new ElementStyleSheet("es1");
    ElementStyleSheet es2 = new ElementStyleSheet("es2");
    ElementStyleSheet es3 = new ElementStyleSheet("es3");
    ElementStyleSheet es2a = new ElementStyleSheet("es2a");
    ElementStyleSheet es1a = new ElementStyleSheet("es1a");

    es2.addParent(es3);
    es1.addParent(es2);

    es2a.addParent(es3);
    es1a.addParent(es2a);

    StyleSheetCollection sc = new StyleSheetCollection();
    es1.setStyleSheetCollection(sc);

    try
    {
      StyleSheetCollection sc2 = new StyleSheetCollection();
      es2.setStyleSheetCollection(sc2);
      fail();
    }
    catch (Exception e)
    {
    }
  }

  public void testCollectDefaultsSimple ()
  {
    ElementStyleSheet es1 = new ElementStyleSheet("es1");
    ElementStyleSheet es2 = new ElementStyleSheet("es2");
    ElementStyleSheet es3 = ElementDefaultStyleSheet.getDefaultStyle();
    ElementStyleSheet es2a = new ElementStyleSheet("es2a");
    ElementStyleSheet es1a = new ElementStyleSheet("es1a");

    es2.addParent(es3);
    es1.addParent(es2);

    es2a.addParent(es3);
    es1a.addParent(es2a);

    StyleSheetCollection sc = new StyleSheetCollection();
    es1.setStyleSheetCollection(sc);

    StyleSheetCollection sc2 = new StyleSheetCollection();
    es1a.setStyleSheetCollection(sc2);
  }

  public void testClone () throws Exception
  {
    ElementStyleSheet es1 = new ElementStyleSheet("es1");
    ElementStyleSheet es2 = new ElementStyleSheet("es2");
    ElementStyleSheet es3 = ElementDefaultStyleSheet.getDefaultStyle();
    ElementStyleSheet es2a = new ElementStyleSheet("es2a");
    ElementStyleSheet es1a = new ElementStyleSheet("es1a");

    es2.addParent(es3);
    es1.addParent(es2);

    es2a.addParent(es3);
    es1a.addParent(es2a);

    StyleSheetCollection sc = new StyleSheetCollection();
    es1.setStyleSheetCollection(sc);
    es1a.setStyleSheetCollection(sc);

    StyleSheetCollection scc = (StyleSheetCollection) sc.clone();
    ElementStyleSheet esC1 = scc.getFirst("es1");
    assertNotNull(esC1);
    assertFalse(es1 == esC1);

    ElementStyleSheet esC2 = getParent(esC1, "es2");
    assertNotNull(esC2);
    assertFalse(es2 == esC2);
    assertTrue(scc.getFirst("es2") == esC2);

    ElementStyleSheet esC1a = scc.getFirst("es1a");
    assertNotNull(esC1a);
    assertFalse(es1a == esC1a);
    assertTrue(scc.getFirst("es1a") == esC1a);

    ElementStyleSheet esC2a = getParent(esC1a, "es2a");
    assertNotNull(esC2a);
    assertFalse(es2a == esC2a);
    assertTrue(scc.getFirst("es2a") == esC2a);

    ElementStyleSheet esC3a = getParent(esC2a, "GlobalElement");
    assertNotNull(esC3a);

    ElementStyleSheet esC3 = getParent(esC2, "GlobalElement");
    assertNotNull(esC3);
    assertTrue(esC3 == esC3a);

  }

  public void testUpdate () throws Exception
  {
    ElementStyleSheet es1 = new ElementStyleSheet("es1");
    ElementStyleSheet es2 = new ElementStyleSheet("es2");
    ElementStyleSheet es3 = ElementDefaultStyleSheet.getDefaultStyle();
    ElementStyleSheet es2a = new ElementStyleSheet("es2a");
    ElementStyleSheet es1a = new ElementStyleSheet("es1a");

    es2.addParent(es3);
    es1.addParent(es2);

    es2a.addParent(es3);
    es1a.addParent(es2a);

    StyleSheetCollection sc = new StyleSheetCollection();
    es1.setStyleSheetCollection(sc);
    es1a.setStyleSheetCollection(sc);

    StyleSheetCollection scc = (StyleSheetCollection) sc.clone();
    ElementStyleSheet esX1 = (ElementStyleSheet) es1.clone();
    scc.updateStyleSheet(esX1);

    ElementStyleSheet esC1 = scc.getFirst("es1");
    assertNotNull(esC1);
    assertTrue(esX1 == esC1);

    ElementStyleSheet esC2 = getParent(esC1, "es2");
    assertNotNull(esC2);
    assertFalse(es2 == esC2);
    assertSame(scc.getFirst("es2"), esC2);

    ElementStyleSheet esC1a = scc.getFirst("es1a");
    assertNotNull(esC1a);
    assertFalse(es1a == esC1a);

    ElementStyleSheet esC2a = getParent(esC1a, "es2a");
    assertNotNull(esC2a);
    assertFalse(es2a == esC2a);

    ElementStyleSheet esC3a = getParent(esC2a, "GlobalElement");
    assertNotNull(esC3a);

    ElementStyleSheet esC3 = getParent(esC2, "GlobalElement");
    assertNotNull(esC3);
    assertTrue(esC3 == esC3a);

  }


  private ElementStyleSheet getParent (ElementStyleSheet child, String parent)
  {
    List l = child.getParents();
    for (int i = 0; i < l.size(); i++)
    {
      ElementStyleSheet p = (ElementStyleSheet) l.get(i);
      if (p.getName().equals(parent))
        return p;
    }
    l = child.getDefaultParents();
    for (int i = 0; i < l.size(); i++)
    {
      ElementStyleSheet p = (ElementStyleSheet) l.get(i);
      if (p.getName().equals(parent))
        return p;
    }
    return null;
  }


}
