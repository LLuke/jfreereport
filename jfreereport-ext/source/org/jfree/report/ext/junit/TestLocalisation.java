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
 * TestLocalisation.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: TestLocalisation.java,v 1.2 2003/09/20 13:00:38 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 28.08.2003 : Initial version
 *  
 */

package org.jfree.report.ext.junit;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class TestLocalisation
{
  public static void main(String[] args)
  {
    Locale locale = new Locale("es", "ES");
    DecimalFormatSymbols syms = new DecimalFormatSymbols(locale);
    //System.out.println(syms.getCurrency());
    System.out.println(syms.getCurrencySymbol());
    System.out.println(syms.getDecimalSeparator());
    System.out.println(syms.getDigit());
    System.out.println(syms.getGroupingSeparator());
    System.out.println(syms.getInfinity());
    System.out.println(syms.getInternationalCurrencySymbol());
    System.out.println(syms.getMinusSign());
    System.out.println(syms.getMonetaryDecimalSeparator());
    System.out.println(syms.getNaN());
    System.out.println(syms.getPatternSeparator());
    System.out.println(syms.getPercent());
    System.out.println(syms.getPercent());
    System.out.println(syms.getPerMill());
    System.out.println(syms.getZeroDigit());

    DecimalFormat format = new DecimalFormat("#,##0.00");
    System.out.println(format.format(10));
  }
}
