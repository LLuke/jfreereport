/**
 * Date: Mar 4, 2003
 * Time: 10:56:25 PM
 *
 * $Id$
 */
package com.jrefinery.report.ext.demo;

import javax.swing.table.TableModel;

public class StaticTableModelProvider implements TableModelProvider
{
  private TableModel model;

  public StaticTableModelProvider()
  {
  }

  public StaticTableModelProvider(TableModel model)
  {
    if (model == null) throw new NullPointerException();
    this.model = model;
  }

  public TableModel getModel()
  {
    return model;
  }

  public void setModel(TableModel model)
  {
    this.model = model;
  }
}
