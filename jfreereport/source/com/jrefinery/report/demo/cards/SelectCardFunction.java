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
 * -----------------------
 * SelectCardFunction.java
 * -----------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: SelectCardFunction.java,v 1.1 2003/04/02 21:24:01 taqua Exp $
 *
 * Changes
 * -------
 * 29.03.2003 : Initial version
 */
package com.jrefinery.report.demo.cards;

import com.jrefinery.report.Band;
import com.jrefinery.report.Element;
import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.function.AbstractFunction;
import com.jrefinery.report.function.FunctionInitializeException;

/**
 * Valid property names are:
 * <ul>
 * <li>Account
 * <li>Admin
 * <li>User
 * <li>Prepaid
 * <li>Free
 * <li>Empty
 * </ul>
 * 
 * @author Thomas Morgner.
 */
public class SelectCardFunction extends AbstractFunction
{
  /** Literal text for the field property. */
  public static final String FIELD_PROPERTY = "field";
  
  /** Literal text for the baseCard property. */
  public static final String BASECARD_PROPERTY = "baseCard";

  /**
   * Default constructor.
   */
  public SelectCardFunction()
  {
  }

  /**
   * Hides all bands, which are invalid for the current card type.
   * A band declares its validity by having the same element name as the
   * card type name.
   *
   * @param band the band that should be evaluated
   */
  private void selectBand (Band band)
  {
    CardType type = (CardType) getDataRow().get(getProperty(FIELD_PROPERTY));
    if (type == null)
    {
      type = CardType.EMPTY;
    }

    String bandName = getProperty(type.getTypeName(), "");

    // if the special type empty is active, then everything will be hidden ...
    if (type == CardType.EMPTY)
    {
      band.setVisible(false);
    }
    else
    {
      band.setVisible(true);

      Element[] elements = band.getElementArray();
      for (int i = 0; i < elements.length; i++)
      {
        if (elements[i] instanceof Band)
        {
          Element e = elements[i];
          e.setVisible(e.getName().equals(bandName));
        }
      }
    }
  }

  /**
   * Receives notification that a row of data is being processed.
   *
   * @param event  the event.
   */
  public void itemsAdvanced(ReportEvent event)
  {
    Element[] elements = event.getReport().getItemBand().getElementArray();
    String rootName = getProperty(BASECARD_PROPERTY, "");
    // the itemband contains several cards, every card is contained in a single band.
    for (int i = 0; i < elements.length; i++)
    {
      if (elements[i] instanceof Band && elements[i].getName().equals(rootName))
      {
        selectBand((Band) elements[i]);
      }
    }
  }

  /**
   * Checks that the function has been correctly initialized.
   * <p>
   * The only check performed at present is to make sure the name is not <code>null</code>.
   *
   * @throws FunctionInitializeException in case the function is not initialized properly.
   */
  public void initialize() throws FunctionInitializeException
  {
    super.initialize();
    if (getProperty(BASECARD_PROPERTY) == null)
    {
      throw new FunctionInitializeException("'baseCard' property is not defined");
    }
    if (getProperty(FIELD_PROPERTY) == null)
    {
      throw new FunctionInitializeException("'field' property is not defined");
    }
  }

  /**
   * Return the current expression value.
   * <P>
   * The value depends (obviously) on the expression implementation.
   *
   * @return the value of the function.
   */
  public Object getValue()
  {
    return null;
  }
}
