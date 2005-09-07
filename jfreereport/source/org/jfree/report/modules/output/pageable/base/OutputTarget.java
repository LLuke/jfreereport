/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * -----------------
 * OutputTarget.java
 * -----------------
 * (C)opyright 2002, 2003, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: OutputTarget.java,v 1.6 2005/02/23 21:05:28 taqua Exp $
 *
 * Changes
 * -------
 * 21-Feb-2002 : Version 1 (DG);
 * 18-Apr-2002 : Introduced drawImage and drawMultiLine
 * 10-May-2002 : Documentation
 * 16-May-2002 : Interface of drawShape changed
 * 20-May-2002 : Moved into new package. Extended to support Strokes, cursors and saveable states.
 *               Created beginPage() state callback to property initialize new pages. FillShape
 *               added.
 * 31-Aug-2002 : Added properties to support a generic configuration interface
 * 10-Dec-2002 : Javadoc updates (DG);
 * 29-Jan-2003 : Extracted SizeCalculator super-interface.
 */

package org.jfree.report.modules.output.pageable.base;

import org.jfree.report.PageDefinition;
import org.jfree.report.layout.LayoutSupport;
import org.jfree.report.modules.output.meta.MetaPage;
import org.jfree.util.Configuration;

/**
 * An interface that defines the methods that must be supported by a report output target.
 * JFreeReport currently implements three targets:  one for Graphics2D (screen and
 * printer), one for Acrobat PDF files and an other target for PlainText output.
 *
 * @author David Gilbert
 */
public interface OutputTarget extends LayoutSupport
{
  /**
   * Literal text for the 'title' property name.
   */
  public static final String TITLE = "Title";

  /**
   * Literal text for the 'author' property name.
   */
  public static final String AUTHOR = "Author";

  /**
   * Returns the value of the specified property.  If the property is not found, the
   * <code>defaultValue</code> is returned.
   *
   * @param property     the property name (or key).
   * @param defaultValue the default value.
   * @return the property value.
   *
   * @throws java.lang.NullPointerException if <code>property</code> is null.
   */
  public String getProperty (String property, String defaultValue);

  /**
   * Returns the value of the specified property.  If the property is not found,
   * <code>null</code> is returned.
   *
   * @param property the property name (or key).
   * @return the property value.
   *
   * @throws java.lang.NullPointerException if <code>property</code> is null.
   */
  public String getProperty (String property);

  /**
   * Defines a property for this target. <P> Properties provide a mechanism for
   * configuring a target.  For example, you can add title and author information to a PDF
   * report using the 'title' and 'author' properties.
   *
   * @param property the property name (key).
   * @param value    the property value (use null to remove an existing property).
   */
  public void setProperty (String property, String value);

  /**
   * Opens the target.
   *
   * @throws OutputTargetException if there is some problem opening the target.
   */
  public void open ()
          throws OutputTargetException;

  /**
   * Returns true if the target is open, and false otherwise.
   *
   * @return true or false.
   */
  public boolean isOpen ();

  /**
   * Closes the target.
   */
  public void close ();

  public void printPage (MetaPage content, PageDefinition page, int index)
          throws OutputTargetException;

  /**
   * Configures the output target.
   *
   * @param config the configuration.
   */
  public void configure (Configuration config);
}
