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
 * ------------------------------
 * ReportConfigurationTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportConfigurationTest.java,v 1.1 2003/07/08 14:21:47 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 26.06.2003 : Initial version
 *  
 */

package org.jfree.report.ext.junit.base.basic.util;

import junit.framework.TestCase;
import org.jfree.report.util.ReportConfiguration;

public class ReportConfigurationTest extends TestCase
{
  private static final String key = ReportConfiguration.LOGTARGET;;

  public ReportConfigurationTest()
  {
  }

  public ReportConfigurationTest(final String s)
  {
    super(s);
  }

  public void testBasicFunctionality ()
  {
    final String value = ReportConfiguration.getGlobalConfig().getConfigProperty(key);
    assertNotNull(value);

    System.setProperty(key, value + "-sysprop");
    final String value2 = ReportConfiguration.getGlobalConfig().getConfigProperty(key);
    assertNotNull(value2);
    assertNotSame(value, value2);

    ReportConfiguration.getGlobalConfig().setConfigProperty(key, value + "-repconf");
    final String value3 = ReportConfiguration.getGlobalConfig().getConfigProperty(key);
    assertNotNull(value3);
    assertNotSame(value, value3);
    assertNotSame(value2, value3);

    ReportConfiguration.getGlobalConfig().setConfigProperty(key, null);
    System.getProperties().remove(key);

  }
}
