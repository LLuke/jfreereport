/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
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
 * BarcodeException.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 * Contributor(s):   Cedric Pronzato;
 *
 * $Id: BarcodeException.java,v 1.2 2005/05/19 00:24:08 mimil Exp $
 *
 * Changes (from 2005-05-17) (PC)
 * -------------------------
 *
 */

package org.jfree.report.dev.barcode;

public class BarcodeException extends RuntimeException
{

  public BarcodeException ()
  {
  }

  public BarcodeException (String message)
  {
    super(message);
  }

  public BarcodeException (Throwable cause)
  {
    super(cause);
  }

  public BarcodeException (String message, Throwable cause)
  {
    super(message, cause);
  }

  public BarcodeException (final String barcodeType, final char illegalCharacter)
  {
    super("The charAt '" + illegalCharacter + "' is illegal in the " + barcodeType + " specification.");
  }
}
