package org.jfree.report.dev.printerspecs;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;

import org.jfree.report.modules.output.pageable.plaintext.AbstractPrinterSpecificationLoader;
import org.jfree.report.modules.output.pageable.plaintext.PrinterEncoding;
import org.jfree.report.modules.output.pageable.plaintext.PrinterSpecification;
import org.jfree.report.modules.output.pageable.plaintext.DefaultPrinterSpecification;
import org.jfree.util.DefaultConfiguration;
import org.jfree.util.ObjectUtilities;

public class EditablePrinterSpecificationLoader
        extends AbstractPrinterSpecificationLoader
{
  private static class EncodingSorter implements Comparator
  {
    public EncodingSorter ()
    {
    }

    public int compare (final Object o1, final Object o2)
    {
      final PrinterEncoding e1 = (PrinterEncoding) o1;
      final PrinterEncoding e2 = (PrinterEncoding) o2;

      final byte[] c1 = e1.getCode();
      final byte[] c2 = e2.getCode();

      final int length = Math.min (c1.length, c2.length);
      for (int i = 0; i < length; i++)
      {
        if (c1[i] < c2[i])
        {
          return -1;
        }
        else if (c1[i] > c2[i])
        {
          return 1;
        }

      }

      if (c1.length < c2.length)
      {
        return -1;
      }
      else if (c1.length > c2.length)
      {
        return 1;
      }
      else
      {
        return 0;
      }
    }
  }

  public EditablePrinterSpecificationLoader ()
  {
  }

  public void load (final PrinterEncodingTableModel tableModel,
                                         final File file)
          throws IOException
  {
    final BufferedInputStream in = new BufferedInputStream (new FileInputStream(file));
    load (tableModel, in);
    in.close();
  }

  public void load (final PrinterEncodingTableModel tableModel,
                    final String fileName)
          throws IOException
  {
    load (tableModel, new File (fileName));
  }

  public synchronized void load (final PrinterEncodingTableModel tableModel,
                                 final InputStream in)
          throws IOException
  {
    if (in == null)
    {
      throw new NullPointerException("InputStream must not be null.");
    }
    if (tableModel == null)
    {
      throw new NullPointerException("TableModel must not be null.");
    }

    tableModel.clear();

    final DefaultConfiguration encodingConfig = new DefaultConfiguration();
    encodingConfig.load(in);

    final PrinterEncoding[] encodings = loadEncodings(encodingConfig);
    Arrays.sort(encodings, new EncodingSorter());
    for (int i = 0; i < encodings.length; i++)
    {
      tableModel.addEncoding(encodings[i]);
    }

    final PrinterSpecification[] printers = loadPrinters (encodingConfig, encodings);
    for (int i = 0; i < printers.length; i++)
    {
      tableModel.addPrinter((ModifiablePrinterSpecification) printers[i]);
    }
  }

  public void loadEpson9PinSpecs (final PrinterEncodingTableModel tableModel)
          throws IOException
  {
    final InputStream in = ObjectUtilities.getResourceAsStream
            ("org/jfree/report/modules/output/pageable/" +
             "plaintext/epson-9pin-printer-specifications.properties",
                    EditablePrinterSpecificationLoader.class);
    load(tableModel, in);
    in.close();
  }

  public void loadEpson24PinSpecs (final PrinterEncodingTableModel tableModel)
          throws IOException
  {
    final InputStream in = ObjectUtilities.getResourceAsStream
            ("org/jfree/report/modules/output/pageable/" +
             "plaintext/epson-24pin-printer-specifications.properties",
                    EditablePrinterSpecificationLoader.class);
    load(tableModel, in);
    in.close();
  }

  protected DefaultPrinterSpecification createPrinterSpecification
          (final String name, final String displayName)
  {
    return new ModifiablePrinterSpecification(name, displayName);
  }
}
