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
 * UnicodeEncodeTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id$
 *
 * Changes 
 * -------------------------
 * 12.06.2003 : Initial version
 *  
 */

package com.jrefinery.report.ext.junit.ext;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;

public class UnicodeEncodeTest
{
  public static void main(String[] args) throws Exception
  {
    String test = "  ";
    writeBytes(test.getBytes("UTF-16"));
    ByteArrayOutputStream bo = new ByteArrayOutputStream();
    OutputStreamWriter wr = new OutputStreamWriter(bo, "UTF-16");
    wr.write("  ");
    wr.write("  ");
    wr.flush();
    writeBytes(bo.toByteArray());
  }

  public static void writeBytes (byte[] bytes)
  {
    for (int i = 0; i < bytes.length; i++)
    {
      if (i != 0)
      {
        System.out.print (";");
      }
      System.out.print (Integer.toHexString(bytes[i]));

    }
  }


}
