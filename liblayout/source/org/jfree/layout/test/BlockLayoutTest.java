/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * ${name}
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Boot.java,v 1.6 2003/11/23 16:50:45 taqua Exp $
 *
 * Changes
 * -------------------------
 * ${date} : Initial version
 *
 *///package org.jfree.layout.test;
//
//import java.awt.geom.Point2D;
//
//import junit.framework.TestCase;
//import org.jfree.layout.LayoutCompiler;
//import org.jfree.layout.peer.LayoutInformation;
//import org.jfree.ui.FloatDimension;
//
//public class BlockLayoutTest extends TestCase
//{
//  public BlockLayoutTest (String s)
//  {
//    super(s);
//  }
//
//  public void testCompileLayout()
//  {
//    final TestDocumentElement root = TestDocumentElement.createBlockLayoutElement("root");
//    root.addChild(TestDocumentElement.createBlockLayoutElement("child1"));
//    root.addChild(TestDocumentElement.createBlockLayoutElement("child2"));
//    final TestDocumentElement child = TestDocumentElement.createBlockLayoutElement("child3");
//    root.addChild(child);
//    child.addChild(TestDocumentElement.createBlockLayoutElement("child3-1"));
//    child.addChild(TestDocumentElement.createBlockLayoutElement("child3-2"));
//    root.addChild(TestDocumentElement.createBlockLayoutElement("child4"));
//
//    final LayoutCompiler lc = new LayoutCompiler();
//    final CompiledLayout layout = lc.computeLayout(root, 500);
//
//    final LayoutInformation rootInfo = layout.getLayoutFor(root);
//    assertEquals(new FloatDimension(500, 35), rootInfo.getSize());
//    assertEquals(new Point2D.Float(0, 0), rootInfo.getPosition());
//
//    final LayoutInformation childInfo = layout.getLayoutFor(child);
//    assertEquals(new FloatDimension(495, 15), childInfo.getSize());
//    assertEquals(new Point2D.Float(0, 10), childInfo.getPosition());
//  }
//}
