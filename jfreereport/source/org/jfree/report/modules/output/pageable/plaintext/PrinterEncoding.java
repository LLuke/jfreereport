/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * PrinterEncoding.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: JCommon.java,v 1.1 2004/07/15 14:49:46 mungady Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.report.modules.output.pageable.plaintext;

public final class PrinterEncoding
{
  private String displayName;
  private String encoding;
  private byte[] code;
  private String internalName;

  public PrinterEncoding (final String internalName,
                          final String displayName,
                          final String encoding,
                          final byte[] code)
  {
    if (internalName == null)
    {
      throw new NullPointerException();
    }
    this.internalName = internalName;
    this.displayName = displayName;
    this.encoding = encoding;
    this.code = new byte[code.length];
    System.arraycopy(code, 0, this.code, 0, code.length);
  }

  public byte[] getCode ()
  {
    final byte[] retval = new byte[code.length];
    System.arraycopy(code, 0, retval, 0, code.length);
    return retval;
  }

  public String getDisplayName ()
  {
    return displayName;
  }

  public String getEncoding ()
  {
    return encoding;
  }

  public String getInternalName ()
  {
    return internalName;
  }

  public boolean equals (final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof PrinterEncoding))
    {
      return false;
    }

    final PrinterEncoding printerEncoding = (PrinterEncoding) o;

    if (!internalName.equals(printerEncoding.internalName))
    {
      return false;
    }

    return true;
  }

  public int hashCode ()
  {
    return internalName.hashCode();
  }


  public String toString ()
  {
    return "org.jfree.report.modules.output.pageable.plaintext.PrinterEncoding{" +
            "internalName='" + internalName + "'" +
            ", displayName='" + displayName + "'" +
            ", encoding='" + encoding + "'" +
            "}";
  }
}
