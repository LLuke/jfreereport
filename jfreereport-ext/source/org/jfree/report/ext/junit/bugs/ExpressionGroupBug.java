/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * ExpressionGroupBug.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ExpressionGroupBug.java,v 1.4 2005/05/31 18:28:01 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 21-Jul-2003 : Initial version
 *  
 */

package org.jfree.report.ext.junit.bugs;

import javax.swing.table.TableModel;

import junit.framework.TestCase;
import org.jfree.report.JFreeReport;
import org.jfree.report.ext.junit.TestSystem;
import org.jfree.util.Log;

public class ExpressionGroupBug extends TestCase
{
  public String evaluateExpression (Number val)
  {
    double value = val.doubleValue();
    if (value > 0)
    {          
      return ("Over than 0");
    }
    if (value == 0)
    {         
      return ("Equal to 0");
    }
    if (value > - 25000)
    {    
      return ("From           0 to     -25 000");
    }
    if (value > - 50000)
    {    
      return ("From     -25 000 to     -50 000");
    }
    if (value > - 100000)
    {   
      return ("From     -50 000 to    -100 000");
    }
    if (value > - 200000)
    {   
      return ("From    -100 000 to    -200 000");
    }
    if (value > - 300000)
    {   
      return ("From    -200 000 to    -300 000");
    }
    if (value > - 400000)
    {   
      return ("From    -300 000 to    -400 000");
    }
    if (value > - 500000)
    {   
      return ("From    -400 000 to    -500 000");
    }
    if (value > - 750000)
    {   
      return ("From    -500 000 to    -750 000");
    }
    if (value > - 1000000)
    {  
      return ("From    -750 000 to  -1 000 000");
    }
    if (value > - 1250000)  
    {  
    return ("From  -1 000 000 to  -1 250 000");
    }
    if (value > - 1500000)  
    {  
    return ("From  -1 250 000 to  -1 500 000");
    }
    if (value > - 1750000)  
    {  
    return ("From  -1 500 000 to  -1 750 000");
    }
    if (value > - 2000000)  
    {  
    return ("From  -1 750 000 to  -2 000 000");
    }
    if (value > - 2500000)  
    {  
    return ("From  -2 000 000 to  -2 500 000");
    }
    if (value > - 3000000)  
    {  
    return ("From  -2 500 000 to  -3 000 000");
    }
    if (value > - 3500000)  
    {  
    return ("From  -3 000 000 to  -3 500 000");
    }
    if (value > - 4000000)  
    {  
    return ("From  -3 500 000 to  -4 000 000");
    }
    if (value > - 4500000)  
    {  
    return ("From  -4 000 000 to  -4 500 000");
    }
    if (value > - 5000000)  
    {  
    return ("From  -4 500 000 to  -5 000 000");
    }
    if (value > - 6000000)  
    {  
    return ("From  -5 000 000 to  -6 000 000");
    }
    if (value > - 7000000)  
    {  
    return ("From  -6 000 000 to  -7 000 000");
    }
    if (value > - 8000000)  
    {  
    return ("From  -7 000 000 to  -8 000 000");
    }
    if (value > - 9000000)  
    {  
    return ("From  -8 000 000 to  -9 000 000");
    }
    if (value > - 10000000) 
    {  
    return ("From  -9 000 000 to -10 000 000");
    }
    if (value > - 15000000) 
    {  
    return ("From -10 000 000 to -15 000 000");
    }
    if (value > - 20000000) 
    {  
    return ("From -15 000 000 to -20 000 000");
    }
    if (value <= - 20000000)
    {  
    return ("Less than - 20 000 000");
    }

    return ("NULL");
  }

  public boolean test (JFreeReport report)
  {
    TableModel mod = report.getData();
    for (int i = 0; i < mod.getRowCount(); i++)
    {
      Log.debug (evaluateExpression((Number) mod.getValueAt(i, 0)));
    }
    return true;
  }

  public static void main (String [] args) throws Exception
  {
    final TableModel mdataModel = new ExpressionGroupBugDataSet();
    ExpressionGroupBug bug = new ExpressionGroupBug();
    JFreeReport report = TestSystem.loadReport
        ("org/jfree/report/ext/junit/bugs/resource/ExpressionGroupBug.xml", mdataModel);
    if (report == null)
    {
      Log.error ("Failed after parse.");
      return;
    }
    TestSystem.showPreview(report);
    if (bug.test(report) == false)
    {
      Log.error ("Failed after test.");
      return;
    }
  }
}
