/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2002, by Object Refinery Limited and Contributors.
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
 * -------------------
 * DefaultLayoutSupport.java
 * -------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: DefaultLayoutSupport.java,v 1.8 2005/03/30 17:25:19 taqua Exp $
 *
 * Changes
 * -------
 * 29-Jan-2003 : Initial version
 * 05-Feb-2003 : Moved from package com.jrefinery.report.targets.pageable
 *
 */
package org.jfree.report.layout;

import org.jfree.report.content.AnchorContentFactoryModule;
import org.jfree.report.content.ContentFactory;
import org.jfree.report.content.DefaultContentFactory;
import org.jfree.report.content.DrawableContentFactoryModule;
import org.jfree.report.content.ImageContentFactoryModule;
import org.jfree.report.content.ShapeContentFactoryModule;
import org.jfree.report.content.TextContentFactoryModule;
import org.jfree.report.style.FontDefinition;

/**
 * The DefaultLayoutSupport uses the AWT to estaminate the content sizes. A LayoutSupport
 * contains all methods required to estaminate sizes for the content-creation.
 *
 * @author Thomas Morgner
 * @see org.jfree.report.content.Content
 */
public class DefaultLayoutSupport implements LayoutSupport
{
  /**
   * The content factory.
   */
  private ContentFactory contentFactory;

  private LayoutManagerCache cache;

  private boolean useMaxLineHeight;

  /**
   * Default-Constructor.
   */
  public DefaultLayoutSupport (final boolean useMaxLineHeight)
  {
    final DefaultContentFactory contentFactory = new DefaultContentFactory();
    contentFactory.addModule(new TextContentFactoryModule());
    contentFactory.addModule(new ImageContentFactoryModule());
    contentFactory.addModule(new ShapeContentFactoryModule());
    contentFactory.addModule(new DrawableContentFactoryModule());
    contentFactory.addModule(new AnchorContentFactoryModule());
    this.contentFactory = contentFactory;
    this.useMaxLineHeight = useMaxLineHeight;

    cache = new LayoutManagerCache();
  }

  public DefaultLayoutSupport (final ContentFactory cf,
                               final boolean useMaxLineHeight)
  {
    if (cf == null)
    {
      throw new NullPointerException();
    }
    this.contentFactory = cf;
    this.useMaxLineHeight = useMaxLineHeight;

    cache = new LayoutManagerCache();
  }
  /**
   * Creates a size calculator for the current state of the output target.  The calculator
   * is used to calculate the string width and line height and later maybe more...
   *
   * @param font the font.
   * @return the size calculator.
   *
   * @throws SizeCalculatorException if there is a problem with the output target.
   */
  public SizeCalculator createTextSizeCalculator (final FontDefinition font)
          throws SizeCalculatorException
  {
    return DefaultSizeCalculator.getDefaultSizeCalculator(font, useMaxLineHeight);
  }

  /**
   * Returns the element alignment. Elements will be layouted aligned to this border, so
   * that <code>mod(X, horizontalAlignment) == 0</code> and <code>mod(Y,
   * verticalAlignment) == 0</code>. Returning 0 will disable the alignment.
   *
   * @return the vertical alignment grid boundry
   */
  public float getVerticalAlignmentBorder ()
  {
    return 0;
  }

  /**
   * Returns the element alignment. Elements will be layouted aligned to this border, so
   * that <code>mod(X, horizontalAlignment) == 0</code> and <code>mod(Y,
   * verticalAlignment) == 0</code>. Returning 0 will disable the alignment.
   *
   * @return the vertical alignment grid boundry
   */
  public float getHorizontalAlignmentBorder ()
  {
    return 0;
  }

  /**
   * Returns the element alignment. Elements will be layouted aligned to this border, so
   * that <code>mod(X, horizontalAlignment) == 0</code> and <code>mod(Y,
   * verticalAlignment) == 0</code>. Returning 0 will disable the alignment.
   * <p/>
   * Q&D Hack: Save some cycles of processor time by computing that thing only once.
   *
   * @return the vertical alignment grid boundry
   */
  public long getInternalHorizontalAlignmentBorder ()
  {
    return 0;
  }

  /**
   * Returns the element alignment. Elements will be layouted aligned to this border, so
   * that <code>mod(X, horizontalAlignment) == 0</code> and <code>mod(Y,
   * verticalAlignment) == 0</code>. Returning 0 will disable the alignment.
   * <p/>
   * Q&D Hack: Save some cycles of processor time by computing that thing only once.
   *
   * @return the vertical alignment grid boundry
   */
  public long getInternalVerticalAlignmentBorder ()
  {
    return 0;
  }

  /**
   * Returns the assigned content factory for the target.
   *
   * @return the content factory.
   */
  public ContentFactory getContentFactory ()
  {
    return contentFactory;
  }

  public LayoutManagerCache getCache ()
  {
    return cache;
  }
}
