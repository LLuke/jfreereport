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
 * -------------------------
 * AbstractOutputTarget.java
 * -------------------------
 * (C)opyright 2000-2003, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: AbstractOutputTarget.java,v 1.2 2003/07/20 19:31:15 taqua Exp $
 *
 * Changes
 * -------
 * 21-May-2002 : Initial version
 * 22-May-2002 : TextAlignment fixed
 * 23-May-2002 : Replaced System.out logging with Log-class
 * 30-May-2002 : Performance upgrade: One-line texts are not processed by linebreak-method
 * 08-Jun-2002 : Documentation
 * 17-Jul-2002 : Added NullPointer handling for drawText(). Whitespaces are now replaced by
 *               space (0x20) if the text to be printed fits on a single line
 * 20-Jul-2002 : created this changelog
 * 23-Aug-2002 : breakLines was broken, fixed and removed useless code ..
 * 23-Aug-2002 : removed the strictmode, the reserved literal is now always added
 * 26-Aug-2002 : Corrected Fontheight calculations.
 * 02-Oct-2002 : Bug: breakLines() got a corrected word breaking (Aleksandr Gekht)
 * 06-Nov-2002 : Bug: LineBreaking again: Handled multiple linebreaks and empty lines
 */
package org.jfree.report.modules.output.pageable.base.output;

import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.util.Iterator;
import java.util.Properties;

import org.jfree.report.content.ContentFactory;
import org.jfree.report.content.DefaultContentFactory;
import org.jfree.report.content.DrawableContentFactoryModule;
import org.jfree.report.content.ImageContentFactoryModule;
import org.jfree.report.content.ShapeContentFactoryModule;
import org.jfree.report.content.TextContentFactoryModule;
import org.jfree.report.modules.output.pageable.base.AlignedLogicalPageWrapper;
import org.jfree.report.modules.output.pageable.base.LogicalPage;
import org.jfree.report.modules.output.pageable.base.OutputTarget;
import org.jfree.report.modules.output.pageable.base.physicals.LogicalPageImpl;

/**
 * The abstract OutputTarget implements base code for all OutputTargets. It contains
 * functions to manage the cursor, the pageformat and the line breaking of strings.
 *
 * @author David Gilbert
 * @author Thomas Morgner
 */
public abstract class AbstractOutputTarget implements OutputTarget
{
  /** Storage for the output target properties. */
  private Properties properties;

  /** The logical page. */
  private LogicalPage logicalPage;

  /** The operation bounds. */
  private Rectangle2D operationBounds;

  /** The content factory used to create content for this output-target. */
  private ContentFactory contentFactory;

  /**
   * Creates a new output target.  Both the logical page size and the physical page size will be
   * the same.
   *
   * @param format  the page format.
   */
  protected AbstractOutputTarget(final PageFormat format)
  {
    this(format, format);
  }

  /**
   * Creates a new output target with the specified logical and physical page sizes.
   *
   * @param logical  the page format used by this target for layouting.
   * @param physical  the page format used by this target for printing.
   */
  protected AbstractOutputTarget(final PageFormat logical, final PageFormat physical)
  {
    this(new LogicalPageImpl(logical, physical));
  }

  /**
   * Creates a new output target.
   *
   * @param logicalPage  the logical page.
   */
  protected AbstractOutputTarget(final LogicalPage logicalPage)
  {
    properties = new Properties();
    this.logicalPage = new AlignedLogicalPageWrapper(logicalPage.newInstance(), this);
    this.logicalPage.setOutputTarget(this);
    operationBounds = new Rectangle2D.Float();

    contentFactory = createContentFactory();
  }

  /**
   * Defines a property for this output target. Properties are the standard way of configuring
   * an output target.
   *
   * @param property  the name of the property to set (<code>null</code> not permitted).
   * @param value  the value of the property.  If the value is <code>null</code>, the property is
   * removed from the output target.
   */
  public void setProperty(final String property, final String value)
  {
    if (property == null)
    {
      throw new NullPointerException();
    }

    if (value == null)
    {
      properties.remove(property);
    }
    else
    {
      properties.setProperty(property, value);
    }
  }

  /**
   * Queries the property named with <code>property</code>. If the property is not found, <code>
   * null</code> is returned.
   *
   * @param property the name of the property to be queried
   *
   * @return the value stored under the given property name
   *
   * @throws java.lang.NullPointerException if <code>property</code> is null
   */
  public String getProperty(final String property)
  {
    return getProperty(property, null);
  }

  /**
   * Queries the property named with <code>property</code>. If the property is not found, the
   * default value is returned.
   *
   * @param property the name of the property to be queried
   * @param defaultValue the defaultvalue returned if there is no such property
   *
   * @return the value stored under the given property name
   *
   * @throws java.lang.NullPointerException if <code>property</code> is null
   */
  public String getProperty(final String property, final String defaultValue)
  {
    if (property == null)
    {
      throw new NullPointerException();
    }

    final String retval = properties.getProperty(property);
    if (retval == null)
    {
      return defaultValue;
    }
    return retval;
  }

  /**
   * Returns an enumeration of the property names.
   *
   * @return the enumeration.
   */
  protected Iterator getPropertyNames()
  {
    return properties.keySet().iterator();
  }

  /**
   * Returns the logical page.
   *
   * @return the logical page.
   */
  public LogicalPage getLogicalPage()
  {
    return logicalPage;
  }

  /**
   * Sets the operation bounds.
   *
   * @param bounds  the bounds.
   */
  public void setOperationBounds(final Rectangle2D bounds)
  {
    operationBounds.setRect(bounds);
  }

  /**
   * Returns the operation bounds.
   *
   * @return the operation bounds.
   */
  public Rectangle2D getOperationBounds()
  {
    return operationBounds.getBounds2D();
  }

  /**
   * Returns the element alignment. Elements will be layouted aligned to this
   * border, so that <code>mod(X, horizontalAlignment) == 0</code> and
   * <code>mod(Y, verticalAlignment) == 0</code>
   *
   * @return the vertical alignment grid boundry
   */
  public float getHorizontalAlignmentBorder()
  {
    return 0;
  }

  /**
   * Returns the element alignment. Elements will be layouted aligned to this
   * border, so that <code>mod(X, horizontalAlignment) == 0</code> and
   * <code>mod(Y, verticalAlignment) == 0</code>
   *
   * @return the vertical alignment grid boundry
   */
  public float getVerticalAlignmentBorder()
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

  /**
   * Creates a default content factory, which supports all known content types.
   * Override this method to supply an own implementation of the ContentFactory.
   *
   * @return a default content factory.
   */
  protected ContentFactory createContentFactory()
  {
    final DefaultContentFactory contentFactory = new DefaultContentFactory();
    contentFactory.addModule(new TextContentFactoryModule());
    contentFactory.addModule(new ImageContentFactoryModule());
    contentFactory.addModule(new ShapeContentFactoryModule());
    contentFactory.addModule(new DrawableContentFactoryModule());
    return contentFactory;
  }

}
