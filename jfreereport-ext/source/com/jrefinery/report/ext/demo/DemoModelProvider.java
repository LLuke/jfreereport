/**
 * Date: Mar 4, 2003
 * Time: 10:58:29 PM
 *
 * $Id$
 */
package com.jrefinery.report.ext.demo;

import com.jrefinery.report.demo.SwingIconsDemoTableModel;

import javax.swing.table.TableModel;
import java.net.URL;

public class DemoModelProvider implements TableModelProvider
{
  private URL base;
  private TableModel model;

  public DemoModelProvider(URL base)
  {
    this.base = base;
  }

  public TableModel getModel()
  {
    if (model == null)
    {
      model = new SwingIconsDemoTableModel(base);
    }
    return model;
  }
}
