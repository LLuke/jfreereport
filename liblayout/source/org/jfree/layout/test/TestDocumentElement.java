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
 * 14-May-2004 : Initial version
 *
 */
package org.jfree.layout.test;

import org.jfree.layout.doc.AbstractDocumentElement;
import org.jfree.layout.style.DefaultStyleKeys;
import org.jfree.layout.style.DisplayStyle;

public class TestDocumentElement extends AbstractDocumentElement
{
  protected TestDocumentElement (final String name)
  {
    super(name);
  }

  public static TestDocumentElement createBlockLayoutElement (final String name)
  {
    final TestDocumentElement element = new TestDocumentElement(name);
    element.getStyle().setStyleProperty(DefaultStyleKeys.DISPLAY_STYLE, DisplayStyle.BLOCK);
    element.getStyle().setStyleProperty(DefaultStyleKeys.PADDING_TOP, new Float(5));
    element.getStyle().setStyleProperty(DefaultStyleKeys.PADDING_LEFT, new Float(5));
    return element;
  }

  public static TestDocumentElement createInlineLayoutElement (final String name)
  {
    final TestDocumentElement element = new TestDocumentElement(name);
    element.getStyle().setStyleProperty(DefaultStyleKeys.DISPLAY_STYLE, DisplayStyle.INLINE);
    element.getStyle().setStyleProperty(DefaultStyleKeys.PADDING_TOP, new Float(5));
    element.getStyle().setStyleProperty(DefaultStyleKeys.PADDING_LEFT, new Float(5));
    return element;
  }

  public static TestDocumentElement createTableLayoutElement (final String name)
  {
    final TestDocumentElement element = new TestDocumentElement(name);
    element.getStyle().setStyleProperty(DefaultStyleKeys.DISPLAY_STYLE, DisplayStyle.TABLE);
    element.getStyle().setStyleProperty(DefaultStyleKeys.PADDING_TOP, new Float(5));
    element.getStyle().setStyleProperty(DefaultStyleKeys.PADDING_LEFT, new Float(5));
    return element;
  }

}