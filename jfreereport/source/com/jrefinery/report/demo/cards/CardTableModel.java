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
 * ----------------
 * CardTableModel.java
 * ----------------
 * (C)opyright 2002, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner (taquera@sherito.org);
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes
 * -------
 * 29.03.2003 : Initial version
 */
package com.jrefinery.report.demo.cards;

import java.util.ArrayList;
import java.util.Date;
import javax.swing.table.AbstractTableModel;

public class CardTableModel extends AbstractTableModel
{
  private ArrayList cards;

  private static final int POS_TYPE = 0;
  private static final int POS_NAME = 1;
  private static final int POS_FIRSTNAME = 2;
  private static final int POS_CARDNR = 3;
  private static final int POS_LOGIN = 4;
  private static final int POS_PASSWORD = 5;
  private static final int POS_EXPIRES = 6;

  private static final String[] COL_NAMES =
      {
        "type", "name", "firstName", "cardNr", "login", "password", "expires"
      };

  public CardTableModel ()
  {
    cards = new ArrayList();
  }

  public void addCard (Card c)
  {
    if (c == null)
      throw new NullPointerException();
    
    cards.add(c);
  }

  /**
   * Returns the number of rows in the model. A
   * <code>JTable</code> uses this method to determine how many rows it
   * should display.  This method should be quick, as it
   * is called frequently during rendering.
   *
   * @return the number of rows in the model
   * @see #getColumnCount
   */
  public int getRowCount()
  {
    return cards.size();
  }

  /**
   * Returns the number of columns in the model. A
   * <code>JTable</code> uses this method to determine how many columns it
   * should create and display by default.
   *
   * @return the number of columns in the model
   * @see #getRowCount
   */
  public int getColumnCount()
  {
    return COL_NAMES.length;
  }

  /**
   *  Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
   *
   *  @param columnIndex  the column being queried
   *  @return the Object.class
   */
  public Class getColumnClass(int columnIndex)
  {
    if (columnIndex == POS_TYPE)
    {
      return CardType.class;
    }
    if (columnIndex == POS_EXPIRES)
    {
      return Date.class;
    }
    return String.class;
  }

  /**
   *  Returns a default name for the column using spreadsheet conventions:
   *  A, B, C, ... Z, AA, AB, etc.  If <code>column</code> cannot be found,
   *  returns an empty string.
   *
   * @param column  the column being queried
   * @return a string containing the default name of <code>column</code>
   */
  public String getColumnName(int column)
  {
    return COL_NAMES[column];
  }

  /**
   * Returns the value for the cell at <code>columnIndex</code> and
   * <code>rowIndex</code>.
   *
   * @param	rowIndex	the row whose value is to be queried
   * @param	columnIndex 	the column whose value is to be queried
   * @return	the value Object at the specified cell
   */
  public Object getValueAt(int rowIndex, int columnIndex)
  {
    Card c = (Card) cards.get(rowIndex);
    if (columnIndex == POS_TYPE)
    {
      return c.getType();
    }
    if (c.getType() == CardType.Account)
    {
      AccountCard ac = (AccountCard) c;
      if (columnIndex == POS_NAME)
      {
        return ac.getLastName();
      }
      if (columnIndex == POS_FIRSTNAME)
      {
        return ac.getFirstName();
      }
      if (columnIndex == POS_LOGIN)
      {
        return ac.getLogin();
      }
      if (columnIndex == POS_PASSWORD)
      {
        return ac.getPassword();
      }
    }
    else if ((c.getType() == CardType.Admin) ||
             (c.getType() == CardType.User))
    {
      UserCard ac = (UserCard) c;
      if (columnIndex == POS_NAME)
      {
        return ac.getLastName();
      }
      if (columnIndex == POS_FIRSTNAME)
      {
        return ac.getFirstName();
      }
      if (columnIndex == POS_LOGIN)
      {
        return ac.getLogin();
      }
      if (columnIndex == POS_PASSWORD)
      {
        return ac.getPassword();
      }
      if (columnIndex == POS_CARDNR)
      {
        return ac.getCardNr();
      }
      if (columnIndex == POS_EXPIRES)
      {
        return ac.getExpires();
      }
    }
    else if (c.getType() == CardType.Free)
    {
      FreeCard ac = (FreeCard) c;
      if (columnIndex == POS_CARDNR)
      {
        return ac.getCardNr();
      }
      if (columnIndex == POS_EXPIRES)
      {
        return ac.getExpires();
      }
    }
    else if (c.getType() == CardType.Prepaid)
    {
      PrepaidCard ac = (PrepaidCard) c;
      if (columnIndex == POS_NAME)
      {
        return ac.getLastName();
      }
      if (columnIndex == POS_FIRSTNAME)
      {
        return ac.getFirstName();
      }
      if (columnIndex == POS_CARDNR)
      {
        return ac.getCardNr();
      }
    }

    return null;
  }
}
