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
 * UnicodeEncodeTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: UnicodeEncodeTest.java,v 1.3 2003/07/23 16:06:25 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 12.06.2003 : Initial version
 *  
 */

package org.jfree.report.ext.junit.ext;



public class UnicodeEncodeTest
{
  public static void main(final String[] args) throws Exception
  {
    /*
    final String test = " ";
    writeBytes(test.getBytes("UTF-16"));
    Log.debug ("------------------");
    final ByteArrayOutputStream bo = new ByteArrayOutputStream();
    final OutputStreamWriter wr = new OutputStreamWriter(bo, "UTF-16");
    wr.write("  ");
    wr.write("  ");
    wr.flush();
    writeBytes(bo.toByteArray());
    */
    "Test".getBytes("Cp1250");
  }

  public static void writeBytes (final byte[] bytes)
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
