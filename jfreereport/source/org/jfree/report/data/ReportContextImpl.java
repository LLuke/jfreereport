/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * $Id: ReportContextImpl.java,v 1.2 2006/11/24 17:12:12 taqua Exp $
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.report.data;

import java.util.HashMap;

import org.jfree.formula.FormulaContext;
import org.jfree.report.flow.ReportContext;
import org.jfree.report.flow.ReportJob;
import org.jfree.report.flow.layoutprocessor.LayoutControllerFactory;
import org.jfree.report.i18n.ResourceBundleFactory;

/**
 * Creation-Date: 20.11.2006, 12:19:34
 *
 * @author Thomas Morgner
 */
public class ReportContextImpl implements ReportContext
{
  private static class DataCarrier
  {
    private boolean locked;
    private Object value;

    public DataCarrier(final Object value)
    {
      this.value = value;
    }

    public void lock ()
    {
      locked = true;
    }

    public boolean isLocked()
    {
      return locked;
    }

    public Object getValue()
    {
      return value;
    }

    public void setValue(final Object value)
    {
      this.value = value;
    }
  }

  private HashMap backend;
  private String exportDescriptor;
  private FormulaContext formulaContext;
  private LayoutControllerFactory layoutControllerFactory;
  private ResourceBundleFactory resourceBundleFactory;

  public ReportContextImpl()
  {
    backend = new HashMap();
  }

  public String getExportDescriptor()
  {
    return exportDescriptor;
  }

  public void setExportDescriptor(final String exportDescriptor)
  {
    this.exportDescriptor = exportDescriptor;
  }

  public FormulaContext getFormulaContext()
  {
    return formulaContext;
  }

  public void setFormulaContext(final FormulaContext formulaContext)
  {
    this.formulaContext = formulaContext;
  }

  public LayoutControllerFactory getLayoutControllerFactory()
  {
    return layoutControllerFactory;
  }

  public void setLayoutControllerFactory(final LayoutControllerFactory layoutControllerFactory)
  {
    this.layoutControllerFactory = layoutControllerFactory;
  }

  public void setAttribute(Object key, Object value)
  {
    final DataCarrier dc = (DataCarrier) backend.get(key);
    if (dc == null)
    {
      if (value == null)
      {
        return;
      }

      final DataCarrier ndc = new DataCarrier(value);
      backend.put (key, ndc);
      return;
    }

    if (dc.isLocked())
    {
      throw new IllegalStateException("Context-Entry is locked.");
    }
    dc.setValue (value);
  }

  public void setSystemAttribute(Object key, Object value)
  {
    final DataCarrier dc = (DataCarrier) backend.get(key);
    if (dc == null)
    {
      if (value == null)
      {
        return;
      }

      final DataCarrier ndc = new DataCarrier(value);
      ndc.lock();
      backend.put (key, ndc);
      return;
    }

    if (dc.isLocked())
    {
      throw new IllegalStateException("Context-Entry is locked.");
    }
    dc.setValue (value);
  }

  public Object getAttribute(Object key)
  {
    final DataCarrier dc = (DataCarrier) backend.get(key);
    if (dc == null)
    {
      return null;
    }
    return dc.getValue();
  }

  public boolean isSystemAttribute(Object key)
  {
    final DataCarrier dc = (DataCarrier) backend.get(key);
    if (dc == null)
    {
      return false;
    }
    return dc.isLocked();
  }

  public ResourceBundleFactory getResourceBundleFactory()
  {
    return resourceBundleFactory;
  }

  public void setResourceBundleFactory(final ResourceBundleFactory resourceBundleFactory)
  {
    this.resourceBundleFactory = resourceBundleFactory;
  }
}
