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
 * ConvertRun.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ConvertRun.java,v 1.1 2003/09/12 17:51:06 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 22.07.2003 : Initial version
 *  
 */

package org.jfree.report.ext.junit.ext;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;

import org.jfree.report.modules.parser.extwriter.ReportConverter;
import org.jfree.report.util.Log;
import org.jfree.util.ObjectUtilities;

public class ConvertRun
{
  private static final String REPORTNAME = "/org/jfree/report/demo/swing-icons.xml";

  public static void main(String[] args) throws IOException
  {
    final ReportConverter rc = new ReportConverter();
    final URL url = ObjectUtilities.getResourceRelative(REPORTNAME, ConvertRun.class);
    final File file =  new File("/tmp/test.xml");
    final FileOutputStream bo = new FileOutputStream(file);
    try
    {
      Writer w = new OutputStreamWriter (new BufferedOutputStream (bo), "UTF-8");
      rc.convertReport(url, file.toURL(), w, "UTF-8");
      w.close();
    }
    catch (Exception e)
    {
      Log.debug("Failed to write or parse " + url, e);
    }

  }
}
