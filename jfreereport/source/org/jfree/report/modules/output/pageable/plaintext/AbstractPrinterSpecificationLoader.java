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
 * AbstractPrinterSpecificationLoader.java
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.jfree.util.DefaultConfiguration;

public abstract class AbstractPrinterSpecificationLoader
{
  public static final String ENCODING_PREFIX = "encoding.";
  public static final String ENCODING_NAME = ".name";
  public static final String ENCODING_CHARSET = ".charset";
  public static final String ENCODING_BYTES = ".bytes";

  public static final String PRINTER_PREFIX = "printer.";
  public static final String PRINTER_NAME = ".name";
  public static final String PRINTER_ENCODINGS = ".encodings";
  public static final String PRINTER_OPERATIONS = ".operations";

  public AbstractPrinterSpecificationLoader ()
  {
  }


  protected PrinterSpecification[] loadPrinters
          (final DefaultConfiguration printerConfig,
           final PrinterEncoding[] encodings)
  {
    final HashMap encodingsByKey = new HashMap();
    for (int i = 0; i < encodings.length; i++)
    {
      encodingsByKey.put(encodings[i].getInternalName(), encodings[i]);
    }

    // collect all available printer model names ...
    final TreeSet availablePrinterNames = new TreeSet();
    final Iterator it = printerConfig.findPropertyKeys
            (PRINTER_PREFIX);
    while (it.hasNext())
    {
      final String name = (String) it.next();
      final int beginIndex = name.indexOf('.');
      if (beginIndex == -1)
      {
        continue;
      }
      final int endIndex = name.indexOf('.', beginIndex + 1);
      if (endIndex == -1)
      {
        continue;
      }
      availablePrinterNames.add(name.substring(beginIndex + 1, endIndex));
    }

    final PrinterSpecification[] retval =
            new PrinterSpecification[availablePrinterNames.size()];
    int index = 0;
    // and load them
    final Iterator printerIt = availablePrinterNames.iterator();
    while (printerIt.hasNext())
    {
      final String printerKey = (String) printerIt.next();
      final String printerName = printerConfig.getProperty
              (PRINTER_PREFIX + printerKey +
              PRINTER_NAME);
      final String printerCharsets = printerConfig.getProperty
              (PRINTER_PREFIX + printerKey +
              PRINTER_ENCODINGS);
      //final String printerOperations = printerConfig.getProperty
      //        (PRINTER_PREFIX + printerKey + ".operations");
      final String[] supportedCharsets = parseCSVString(printerCharsets);
      //final String[] supportedOperations = parseCSVString(printerOperations);
      final DefaultPrinterSpecification specification =
              createPrinterSpecification(printerKey, printerName);

      for (int i = 0; i < supportedCharsets.length; i++)
      {
        final PrinterEncoding encoding = (PrinterEncoding)
                encodingsByKey.get(supportedCharsets[i]);
        if (encoding == null)
        {
          throw new NullPointerException
                  ("PrinterEncoding '" + supportedCharsets[i] + "' is not defined.");
        }
        specification.addEncoding(encoding);
      }
      retval[index] = specification;
      index += 1;
    }
    return retval;
  }

  protected DefaultPrinterSpecification createPrinterSpecification
          (final String name, final String displayName)
  {
    return new DefaultPrinterSpecification(name, displayName);
  }

  protected PrinterEncoding[] loadEncodings (final DefaultConfiguration encodingConfig)
  {
    // collect all available encoding names ...
    final TreeSet availableEncodingNames = new TreeSet();
    final Iterator it = encodingConfig.findPropertyKeys
            (ENCODING_PREFIX);
    while (it.hasNext())
    {
      final String name = (String) it.next();
      final int beginIndex = name.indexOf('.');
      if (beginIndex == -1)
      {
        continue;
      }
      final int endIndex = name.indexOf('.', beginIndex + 1);
      if (endIndex == -1)
      {
        continue;
      }
      availableEncodingNames.add(name.substring(beginIndex + 1, endIndex));
    }

    // and load them
    final Iterator encIt = availableEncodingNames.iterator();
    final ArrayList encodings = new ArrayList();
    while (encIt.hasNext())
    {
      final String encodingKey = (String) encIt.next();
      final String encodingName = encodingConfig.getProperty
              (ENCODING_PREFIX + encodingKey +
              ENCODING_NAME);
      final String encodingCharset = encodingConfig.getProperty
              (ENCODING_PREFIX + encodingKey +
              ENCODING_CHARSET);
      final String encodingBytes = encodingConfig.getProperty
              (ENCODING_PREFIX + encodingKey +
              ENCODING_BYTES);
      final byte[] encodingCode = parseBytes(encodingBytes);
      final PrinterEncoding encoding =
              new PrinterEncoding(encodingKey, encodingName,
                      encodingCharset, encodingCode);
      encodings.add(encoding);
    }

    return (PrinterEncoding[])
            encodings.toArray(new PrinterEncoding[encodings.size()]);
  }

  private byte[] parseBytes (final String encString)
  {
    final StringTokenizer strtok = new StringTokenizer(encString, ",", false);
    final ArrayList tokens = new ArrayList();
    while (strtok.hasMoreTokens())
    {
      final String token = strtok.nextToken();
      tokens.add(token);
    }

    final byte[] retval = new byte[tokens.size()];
    for (int i = 0; i < tokens.size(); i++)
    {
      retval[i] = Byte.parseByte((String) tokens.get(i));
    }
    return retval;
  }

  private String[] parseCSVString (final String encString)
  {
    final StringTokenizer strtok = new StringTokenizer(encString, ",", false);
    final ArrayList tokens = new ArrayList();
    while (strtok.hasMoreTokens())
    {
      final String token = strtok.nextToken();
      tokens.add(token);
    }

    final String[] retval = new String[tokens.size()];
    return (String[]) tokens.toArray(retval);
  }

}
