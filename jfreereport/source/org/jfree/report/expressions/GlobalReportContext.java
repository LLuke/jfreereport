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
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.report.expressions;

import java.io.Serializable;

/**
 * A global context that is shared among all derived instances of all report
 * elements in all report runs. (That's why it is called 'global'.)
 *
 * The report context is bound to a report job and stores (among others) the
 * precomputation registry and
 *
 * @author Thomas Morgner
 */
public interface GlobalReportContext extends Serializable
{
  public static final String PRECOMPUTE_REGISTRY = "system::precompute-registry";
  public static final String FORMULA_CONTEXT = "system::formula-context";
  
  public void setAttribute (Object key, Object value);
  public Object getAttribute (Object key);
  public boolean isSystemAttribute (Object key);

}
