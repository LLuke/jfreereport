/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
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
 * ------------------------------
 * AbstractTableCreator.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: AbstractTableCreator.java,v 1.5 2005/02/23 21:05:33 taqua Exp $
 *
 * Changes 
 * -------------------------
 * Feb 26, 2004 : Initial version
 *  
 */

package org.jfree.report.modules.output.table.base;

import org.jfree.report.modules.output.meta.MetaBand;
import org.jfree.report.modules.output.meta.MetaElement;
import org.jfree.report.util.geom.StrictBounds;

public abstract class AbstractTableCreator implements TableCreator
{
  private boolean empty;
  private boolean ignoreOpenState;

  public AbstractTableCreator ()
  {
    empty = true;
  }

  protected void setEmpty (final boolean b)
  {
    empty = b;
  }

  /**
   * Checks, whether the current table contains content. Returns true, if there is no
   * current table open.
   *
   * @return true, if the table does not contain content, false otherwise.
   */
  public boolean isEmpty ()
  {
    return empty;
  }

  /**
   * Add the specified element to the logical page. Create content from the values
   * contained in the element and format the content by using the element's attributes.
   *
   * @param e the element.
   * @throws NullPointerException if the element has no valid layout (no BOUNDS defined).
   *                              Bounds are usually defined by the BandLayoutManager.
   */
  protected abstract void processElement (MetaElement e);

  /**
   * Add the specified band definition to the table sheet. By default, Band definitions
   * are not used to create content, but they might be important for the layout. it is up
   * to the implementor to decide whether to use the supplied content of the band (if
   * any).
   *
   * @param e the element.
   * @return true, if the band is fully processed and the children should be ignored,
   *         false to indicate that we need the children to complete the process.
   *
   * @throws NullPointerException if the element has no valid layout (no BOUNDS defined).
   *                              Bounds are usually defined by the BandLayoutManager.
   */
  protected abstract boolean processBandDefinition (MetaBand e);

  public boolean isIgnoreOpenState()
  {
    return ignoreOpenState;
  }

  public void setIgnoreOpenState(final boolean ignoreOpenState)
  {
    this.ignoreOpenState = ignoreOpenState;
  }

  /**
   * Processes the given metaband. The MetaBandProducer has already collected all
   * necessary data to allow the content creation. Table implementors should provide their
   * own MetaBandProducer if they need additional properties.
   *
   * @param band the metaband that is processed.
   */
  public void processBand (final MetaBand band)
  {
    if (isIgnoreOpenState() == false && isOpen() == false)
    {
      throw new IllegalStateException("Producer already closed");
    }

    final StrictBounds bounds = band.getBounds();
    // do nothing if the band has no height...
    if (bounds.getHeight() == 0)
    {
      return;
    }

    // handle the band itself, the band's bounds are already translated.
    if (processBandDefinition(band) == true)
    {
      // band indicated that no further processing should be done
      return;
    }

    // process all elements
    final MetaElement[] l = band.toArray();
    for (int i = 0; i < l.length; i++)
    {
      final MetaElement e = l[i];
      if (e instanceof MetaBand)
      {
        processBand((MetaBand) e);
      }
      else
      {
        processElement(e);
      }
    }
  }

}
