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
 * -------------------
 * DefaultLayoutSupport.java
 * -------------------
 * (C)opyright 2000-2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: DefaultLayoutSupport.java,v 1.5 2003/04/06 18:11:30 taqua Exp $
 *
 * Changes
 * -------
 * 29-Jan-2003 : Initial version
 * 05-Feb-2003 : Moved from package com.jrefinery.report.targets.pageable
 *
 */
package com.jrefinery.report.targets.base.layout;

import com.jrefinery.report.targets.FontDefinition;
import com.jrefinery.report.targets.base.content.ContentFactory;
import com.jrefinery.report.targets.base.content.DefaultContentFactory;
import com.jrefinery.report.targets.base.content.DrawableContentFactoryModule;
import com.jrefinery.report.targets.base.content.ImageContentFactoryModule;
import com.jrefinery.report.targets.base.content.ShapeContentFactoryModule;
import com.jrefinery.report.targets.base.content.TextContentFactoryModule;
import com.jrefinery.report.targets.pageable.OutputTargetException;

/**
 * The DefaultLayoutSupport uses the AWT to estaminate the content sizes.
 * A LayoutSupport contains all methods required to estaminate sizes for the
 * content-creation.
 *
 * @see com.jrefinery.report.targets.base.content.Content
 * @see com.jrefinery.report.targets.pageable.operations.OperationModule
 * 
 * @author Thomas Morgner
 */
public class DefaultLayoutSupport implements LayoutSupport
{
  /** The content factory. */
  private DefaultContentFactory contentFactory;

  /** A singleton instance of the DefaultLayoutSupport. */
  private static DefaultLayoutSupport singleton;

  /**
   * Returns the single instance of this class.
   * 
   * @return The single instance of this class.
   */
  public static DefaultLayoutSupport getDefaultInstance ()
  {
    if (singleton == null)
    {
      singleton = new DefaultLayoutSupport();
    }
    return singleton;
  }

  /**
   * Default-Constructor.
   */
  public DefaultLayoutSupport()
  {
    contentFactory = new DefaultContentFactory();
    contentFactory.addModule(new TextContentFactoryModule());
    contentFactory.addModule(new ImageContentFactoryModule());
    contentFactory.addModule(new ShapeContentFactoryModule());
    contentFactory.addModule(new DrawableContentFactoryModule());
  }

  /**
   * Creates a size calculator for the current state of the output target.  The calculator
   * is used to calculate the string width and line height and later maybe more...
   *
   * @param font  the font.
   *
   * @return the size calculator.
   *
   * @throws OutputTargetException if there is a problem with the output target.
   */
  public SizeCalculator createTextSizeCalculator(FontDefinition font) throws OutputTargetException
  {
    return DefaultSizeCalculator.getDefaultSizeCalculator(font);
  }

  /**
   * Returns the element alignment. Elements will be layouted aligned to this
   * border, so that <code>mod(X, horizontalAlignment) == 0</code> and
   * <code>mod(Y, verticalAlignment) == 0</code>. Returning 0 will disable
   * the alignment.
   *
   * @return the vertical alignment grid boundry
   */
  public float getVerticalAlignmentBorder()
  {
    return 0;
  }

  /**
   * Returns the element alignment. Elements will be layouted aligned to this
   * border, so that <code>mod(X, horizontalAlignment) == 0</code> and
   * <code>mod(Y, verticalAlignment) == 0</code>. Returning 0 will disable
   * the alignment.
   *
   * @return the vertical alignment grid boundry
   */
  public float getHorizontalAlignmentBorder()
  {
    return 0;
  }

  /**
   * Returns the assigned content factory for the target.
   *
   * @return the content factory.
   */
  public ContentFactory getContentFactory()
  {
    return contentFactory;
  }
}
