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
 * UtilTestSuite.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: UtilTestSuite.java,v 1.1 2003/06/20 12:05:13 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 16.06.2003 : Initial version
 *  
 */

package com.jrefinery.report.ext.junit.base.basic.util;

import junit.framework.Test;
import junit.framework.TestSuite;

public class UtilTestSuite extends TestSuite
{
  public UtilTestSuite(final String s)
  {
    super(s);
    addTestSuite(LineBreakIteratorTest.class);
    addTestSuite(CharacterEntityParserTest.class);
    addTestSuite(ReportConfigurationTest.class);
  }

  public static Test suite()
  {
    return new UtilTestSuite(UtilTestSuite.class.getName());
  }

}
