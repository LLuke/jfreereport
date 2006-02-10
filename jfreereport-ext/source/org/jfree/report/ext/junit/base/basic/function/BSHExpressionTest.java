/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * ------------------------------
 * BSHExpressionTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: BSHExpressionTest.java,v 1.7 2006/02/01 09:47:40 taqua Exp $
 *
 * Changes
 * -------------------------
 * 01.06.2003 : Initial version
 *
 */

package org.jfree.report.ext.junit.base.basic.function;

import javax.swing.table.DefaultTableModel;

import junit.framework.TestCase;
import org.jfree.report.DataRow;
import org.jfree.report.modules.misc.beanshell.BSHExpression;
import org.jfree.report.states.DataRowConnector;

public class BSHExpressionTest extends TestCase
{
  public BSHExpressionTest(final String s)
  {
    super(s);
  }

  public void testCreate() throws Exception
  {
    assertTrue(DataRow.class.isAssignableFrom(DataRowConnector.class));
    final BSHExpression ex = new BSHExpression();
    ex.setExpression("");
    ex.setRuntime(new TestingExpressionRuntime
            (new DefaultTableModel(), 0, "pageable/pdf"));
    ex.getValue();
  }
}
