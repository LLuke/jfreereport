/**
 * =============================================================
 * JFreeReport : an open source reporting class library for Java
 * =============================================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  David Gilbert (david.gilbert@object-refinery.com)
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
 * -----------------------
 * FontChangeFunction.java
 * -----------------------
 * (C)opyright 2000-2002, by Simba Management Limited.
 *
 */
package com.jrefinery.report.demo;

import com.jrefinery.report.function.AbstractFunction;
import com.jrefinery.report.function.FunctionInitializeException;
import com.jrefinery.report.event.ReportEvent;
import com.jrefinery.report.Element;
import com.jrefinery.report.TextElement;

import javax.swing.table.TableModel;
import java.awt.Font;

public class FontChangeFunction extends AbstractFunction
{

  public FontChangeFunction ()
  {
  }

  /**
   * The ItemsSection is beeing processed. The next events will be itemsAdvanced events until the
   * itemsFinished event is raised.
   */
  public void itemsStarted (ReportEvent event)
  {
  }

  /**
   * Maps the pageStarted-method to the legacy function startPage (int).
   */
  public void itemsAdvanced (ReportEvent event)
  {
    if (event.getState().isPrepareRun()) return;

    TableModel data = event.getReport().getData ();
    int row = event.getState().getCurrentDataItem();

    String fontname = (String) data.getValueAt(row, 1);
    if (fontname == null)
      return;

    Element e = event.getReport().getItemBand().getElement(getElement());
    if (e != null)
    {
      if (e instanceof TextElement)
      {
        TextElement tx = (TextElement) e;
        tx.setFont(new Font (fontname, Font.PLAIN, 10));
      }
    }
  }

  /**
   *
   */
  public void initialize () throws FunctionInitializeException
  {
    super.initialize();
    if (getProperty("element") == null)
      throw new FunctionInitializeException("Element name must be specified");
  }

  public void setElement (String name)
  {
    setProperty("element", name);
  }

  public String getElement ()
  {
    return getProperty("element", "");
  }

  public Object getValue ()
  {
    return null;
  }
}
