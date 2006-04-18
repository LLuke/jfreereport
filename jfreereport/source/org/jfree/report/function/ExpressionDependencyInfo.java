/**
 * ========================================
 * <libname> : a free Java <foobar> library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2005, by Object Refinery Limited and Contributors.
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
 * ---------
 * ExpressionDependencyInfo.java
 * ---------
 *
 * Original Author:  Thomas Morgner;
 * Contributors: -;
 *
 * $Id: Anchor.java,v 1.3 2005/02/23 21:04:29 taqua Exp $
 *
 * Changes
 * -------------------------
 * 20.02.2006 : Initial version
 */
package org.jfree.report.function;

/**
 * Creation-Date: 20.02.2006, 16:33:33
 *
 * @author Thomas Morgner
 */
public class ExpressionDependencyInfo implements Cloneable
{
  private String[] dependendFields;
  private boolean precompute;
  private boolean deepTraversal;
  private boolean exported;
  private boolean globalView;

  public ExpressionDependencyInfo()
  {
  }

  public String[] getDependendFields()
  {
    return dependendFields;
  }

  public void setDependendFields(final String[] dependendFields)
  {
    this.dependendFields = (String[]) dependendFields.clone();
  }

  public boolean isPrecompute()
  {
    return precompute;
  }

  public void setPrecompute(final boolean precompute)
  {
    this.precompute = precompute;
  }

  public boolean isDeepTraversal()
  {
    return deepTraversal;
  }

  public void setDeepTraversal(final boolean deepTraversal)
  {
    this.deepTraversal = deepTraversal;
  }

  public boolean isExported()
  {
    return exported;
  }

  public void setExported(final boolean exported)
  {
    this.exported = exported;
  }

  public boolean isGlobalView()
  {
    return globalView;
  }

  public void setGlobalView(final boolean globalView)
  {
    this.globalView = globalView;
  }

  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }
}
