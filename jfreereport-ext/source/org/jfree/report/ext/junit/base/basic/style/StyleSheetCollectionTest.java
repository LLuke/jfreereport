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
 * $Id: StyleSheetCollectionTest.java,v 1.3 2003/07/03 16:06:19 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 18.06.2003 : Initial version
 *  
 */

package org.jfree.report.ext.junit.base.basic.style;

import java.util.List;

import junit.framework.TestCase;
import org.jfree.report.style.ElementStyleSheet;
import org.jfree.report.style.StyleSheetCollection;
import org.jfree.report.style.ElementDefaultStyleSheet;
import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.util.Log;
import org.jfree.report.ext.junit.base.basic.ElementTest;


public class StyleSheetCollectionTest extends TestCase
{
  public StyleSheetCollectionTest()
  {
  }

  public StyleSheetCollectionTest(final String s)
  {
    super(s);
  }

  public void testCollectSimple ()
  {
    final ElementStyleSheet es1 = new ElementStyleSheet("es1");
    final ElementStyleSheet es2 = new ElementStyleSheet("es2");
    final ElementStyleSheet es3 = new ElementStyleSheet("es3");
    final ElementStyleSheet es2a = new ElementStyleSheet("es2a");
    final ElementStyleSheet es1a = new ElementStyleSheet("es1a");

    es2.addParent(es3);
    es1.addParent(es2);

    es2a.addParent(es3);
    es1a.addParent(es2a);

    final StyleSheetCollection sc = new StyleSheetCollection();
    es1.registerStyleSheetCollection(sc);
    es1a.registerStyleSheetCollection(sc);

    assertStylesConnected(es1, sc);
    assertStylesConnected(es1a, sc);

    es1.unregisterStyleSheetCollection(sc);
    es1a.unregisterStyleSheetCollection(sc);
    assertStylesConnected(es1, null);
    assertStylesConnected(es1a, null);
  }

  public void testCollectSimpleCrash ()
  {
    final ElementStyleSheet es1 = new ElementStyleSheet("es1");
    final ElementStyleSheet es2 = new ElementStyleSheet("es2");
    final ElementStyleSheet es3 = new ElementStyleSheet("es3");
    final ElementStyleSheet es2a = new ElementStyleSheet("es2a");
    final ElementStyleSheet es1a = new ElementStyleSheet("es1a");

    es2.addParent(es3);
    es1.addParent(es2);

    es2a.addParent(es3);
    es1a.addParent(es2a);

    final StyleSheetCollection sc = new StyleSheetCollection();
    es1.registerStyleSheetCollection(sc);

    try
    {
      final StyleSheetCollection sc2 = new StyleSheetCollection();
      es2.registerStyleSheetCollection(sc2);
      fail();
    }
    catch (Exception e)
    {
    }
  }

  public void testCollectDefaultsSimple ()
  {
    final ElementStyleSheet es1 = new ElementStyleSheet("es1");
    final ElementStyleSheet es2 = new ElementStyleSheet("es2");
    final ElementStyleSheet es3 = ElementDefaultStyleSheet.getDefaultStyle();
    final ElementStyleSheet es2a = new ElementStyleSheet("es2a");
    final ElementStyleSheet es1a = new ElementStyleSheet("es1a");

    es2.addParent(es3);
    es1.addParent(es2);

    es2a.addParent(es3);
    es1a.addParent(es2a);

    final StyleSheetCollection sc = new StyleSheetCollection();
    es1.registerStyleSheetCollection(sc);

    final StyleSheetCollection sc2 = new StyleSheetCollection();
    es1a.registerStyleSheetCollection(sc2);
  }

  public void testClone () throws Exception
  {
    final ElementStyleSheet es1 = new ElementStyleSheet("es1");
    final ElementStyleSheet es2 = new ElementStyleSheet("es2");
    final ElementStyleSheet es3 = ElementDefaultStyleSheet.getDefaultStyle();
    final ElementStyleSheet es2a = new ElementStyleSheet("es2a");
    final ElementStyleSheet es1a = new ElementStyleSheet("es1a");

    es2.addParent(es3);
    es1.addParent(es2);

    es2a.addParent(es3);
    es1a.addParent(es2a);

    final StyleSheetCollection sc = new StyleSheetCollection();
    es1.registerStyleSheetCollection(sc);
    es1a.registerStyleSheetCollection(sc);

    final StyleSheetCollection scc = (StyleSheetCollection) sc.clone();
    final ElementStyleSheet esC1 = scc.getFirst("es1");
    assertNotNull(esC1);
    assertFalse(es1 == esC1);

    final ElementStyleSheet esC2 = getParent(esC1, "es2");
    assertNotNull(esC2);
    assertFalse(es2 == esC2);
    assertTrue(scc.getFirst("es2") == esC2);

    final ElementStyleSheet esC1a = scc.getFirst("es1a");
    assertNotNull(esC1a);
    assertFalse(es1a == esC1a);
    assertTrue(scc.getFirst("es1a") == esC1a);

    final ElementStyleSheet esC2a = getParent(esC1a, "es2a");
    assertNotNull(esC2a);
    assertFalse(es2a == esC2a);
    assertTrue(scc.getFirst("es2a") == esC2a);

    final ElementStyleSheet esC3a = getParent(esC2a, "GlobalElement");
    assertNotNull(esC3a);

    final ElementStyleSheet esC3 = getParent(esC2, "GlobalElement");
    assertNotNull(esC3);
    assertTrue(esC3 == esC3a);

  }

  public void testUpdate () throws Exception
  {
    final ElementStyleSheet es1 = new ElementStyleSheet("es1");
    final ElementStyleSheet es2 = new ElementStyleSheet("es2");
    final ElementStyleSheet es3 = ElementDefaultStyleSheet.getDefaultStyle();
    final ElementStyleSheet es2a = new ElementStyleSheet("es2a");
    final ElementStyleSheet es1a = new ElementStyleSheet("es1a");

    es2.addParent(es3);
    es1.addParent(es2);

    es2a.addParent(es3);
    es1a.addParent(es2a);

    final StyleSheetCollection sc = new StyleSheetCollection();
    es1.registerStyleSheetCollection(sc);
    es1a.registerStyleSheetCollection(sc);

    final StyleSheetCollection scc = (StyleSheetCollection) sc.clone();
    final ElementStyleSheet esX1 = (ElementStyleSheet) es1.clone();
    scc.updateStyleSheet(esX1);

    final ElementStyleSheet esC1 = scc.getFirst("es1");
    assertNotNull(esC1);
    assertTrue(esX1 == esC1);

    final ElementStyleSheet esC2 = getParent(esC1, "es2");
    assertNotNull(esC2);
    assertFalse(es2 == esC2);
    assertSame(scc.getFirst("es2"), esC2);

    final ElementStyleSheet esC1a = scc.getFirst("es1a");
    assertNotNull(esC1a);
    assertFalse(es1a == esC1a);

    final ElementStyleSheet esC2a = getParent(esC1a, "es2a");
    assertNotNull(esC2a);
    assertFalse(es2a == esC2a);

    final ElementStyleSheet esC3a = getParent(esC2a, "GlobalElement");
    assertNotNull(esC3a);

    final ElementStyleSheet esC3 = getParent(esC2, "GlobalElement");
    assertNotNull(esC3);
    assertTrue(esC3 == esC3a);

  }


  private ElementStyleSheet getParent (final ElementStyleSheet child, final String parent)
  {
    List l = child.getParents();
    for (int i = 0; i < l.size(); i++)
    {
      final ElementStyleSheet p = (ElementStyleSheet) l.get(i);
      if (p.getName().equals(parent))
        return p;
    }
    l = child.getDefaultParents();
    for (int i = 0; i < l.size(); i++)
    {
      final ElementStyleSheet p = (ElementStyleSheet) l.get(i);
      if (p.getName().equals(parent))
        return p;
    }
    return null;
  }

  public void testBand ()
  {
    final ElementStyleSheet esGlobal = new ElementStyleSheet("es-global");
    final ElementStyleSheet esGlobalBand = new ElementStyleSheet("es-global-band");
    final ElementStyleSheet esGlobalBandDefault = new ElementStyleSheet("es-global-banddefault");

    final Band b = new Band ();
    b.getStyle().addParent(esGlobalBand);
    b.getBandDefaults().addParent(esGlobalBandDefault);

    ElementTest.ElementImpl e = new ElementTest.ElementImpl();
    e.getStyle().addParent(esGlobal);
    b.addElement(e);

    e = new ElementTest.ElementImpl();
    e.getStyle().addParent(esGlobal);
    b.addElement(e);

    final Band b2 = new Band ();
    b.getStyle().addParent(esGlobalBand);
    b.getBandDefaults().addParent(esGlobalBandDefault);

    e = new ElementTest.ElementImpl();
    e.getStyle().addParent(esGlobal);
    b2.addElement(e);

    e = new ElementTest.ElementImpl();
    e.getStyle().addParent(esGlobal);
    b2.addElement(e);

    b.addElement(b2);

    final StyleSheetCollection sc = new StyleSheetCollection();
    b.registerStyleSheetCollection(sc);
    assertStyleCollectionConnected(b, sc);

    b.unregisterStyleSheetCollection(sc);
    assertStyleCollectionConnected(b, null);
  }

  private void assertStyleCollectionConnected(final Band band, final StyleSheetCollection sc)
  {
    assertStylesConnected(band.getStyle(), sc);
    assertStylesConnected(band.getBandDefaults(), sc);
    final Element[] elements = band.getElementArray();
    for (int i = 0; i < elements.length; i++)
    {
      assertTrue(elements[i].getStyleSheetCollection() == sc);
      if (elements[i] instanceof Band)
      {
        assertStyleCollectionConnected((Band) elements[i], sc);
      }
      else
      {
        assertStylesConnected(elements[i].getStyle(), sc);
      }
    }
  }

  private void assertStylesConnected (final ElementStyleSheet es, final StyleSheetCollection sc)
  {
    if (es.isGlobalDefault())
      return;

    assertTrue(es.getName() + " " + es.hashCode() + " - " + es.getStyleSheetCollection(), es.getStyleSheetCollection() == sc);
    List parents = es.getParents();
    for (int i = 0; i < parents.size(); i++)
    {
      assertStylesConnected((ElementStyleSheet) parents.get(i), sc);
    }
    parents = es.getDefaultParents();
    for (int i = 0; i < parents.size(); i++)
    {
      assertStylesConnected((ElementStyleSheet) parents.get(i), sc);
    }
  }

  public void testBandPreConnected ()
  {
    final ElementStyleSheet esGlobal = new ElementStyleSheet("es-global");
    final ElementStyleSheet esGlobalBand = new ElementStyleSheet("es-global-band");
    final ElementStyleSheet esGlobalBandDefault = new ElementStyleSheet("es-global-banddefault");

    final Band b = new Band ();
    final StyleSheetCollection sc = new StyleSheetCollection();
    b.registerStyleSheetCollection(sc);

    assertStyleCollectionConnected(b, sc);

    b.getStyle().addParent(esGlobalBand);
    assertStyleCollectionConnected(b, sc);

    b.getBandDefaults().addParent(esGlobalBandDefault);
    assertStyleCollectionConnected(b, sc);

    ElementTest.ElementImpl e = new ElementTest.ElementImpl();
    e.getStyle().addParent(esGlobal);
    b.addElement(e);

    e = new ElementTest.ElementImpl();
    e.getStyle().addParent(esGlobal);
    b.addElement(e);

    final Band b2 = new Band ();
    b.getStyle().addParent(esGlobalBand);
    b.getBandDefaults().addParent(esGlobalBandDefault);

    e = new ElementTest.ElementImpl();
    e.getStyle().addParent(esGlobal);
    b2.addElement(e);

    e = new ElementTest.ElementImpl();
    e.getStyle().addParent(esGlobal);
    b2.addElement(e);

    b.addElement(b2);

    assertStyleCollectionConnected(b, sc);

    b.unregisterStyleSheetCollection(sc);
    assertStyleCollectionConnected(b, null);
  }

}
