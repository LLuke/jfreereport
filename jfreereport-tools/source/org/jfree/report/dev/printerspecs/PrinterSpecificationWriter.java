package org.jfree.report.dev.printerspecs;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.jfree.report.modules.output.pageable.plaintext.PrinterEncoding;
import org.jfree.report.modules.output.pageable.plaintext.AbstractPrinterSpecificationLoader;
import org.jfree.util.DefaultConfiguration;
import org.jfree.util.SortedConfigurationWriter;

public class PrinterSpecificationWriter
{
  public PrinterSpecificationWriter()
  {
  }

  public void save (final String filename, final PrinterEncodingTableModel model)
          throws IOException
  {
    save (new File(filename), model);
  }

  public void save (final File file, final PrinterEncodingTableModel model)
          throws IOException
  {
    final BufferedOutputStream out =
            new BufferedOutputStream(new FileOutputStream(file));
    save (out, model);
    out.close();
  }

  public void save (final OutputStream out, final PrinterEncodingTableModel model)
          throws IOException
  {
    final DefaultConfiguration p = new DefaultConfiguration();

    for (int i = 0; i < model.getEncodingCount(); i++)
    {
      final PrinterEncoding enc = model.getEncoding(i);
      final String key = enc.getInternalName();
      p.setProperty(AbstractPrinterSpecificationLoader.ENCODING_PREFIX + key +
                    AbstractPrinterSpecificationLoader.ENCODING_NAME, enc.getDisplayName());
      p.setProperty(AbstractPrinterSpecificationLoader.ENCODING_PREFIX + key +
                    AbstractPrinterSpecificationLoader.ENCODING_CHARSET, enc.getEncoding());
      p.setProperty(AbstractPrinterSpecificationLoader.ENCODING_PREFIX + key +
                    AbstractPrinterSpecificationLoader.ENCODING_BYTES, createCodeString(enc.getCode()));
    }

    for (int i = 0; i < model.getPrinterCount(); i++)
    {
      final ModifiablePrinterSpecification spec = model.getPrinter(i);
      final String key = spec.getName();
      p.setProperty(AbstractPrinterSpecificationLoader.PRINTER_PREFIX + key +
              AbstractPrinterSpecificationLoader.PRINTER_NAME, spec.getDisplayName());
      p.setProperty(AbstractPrinterSpecificationLoader.PRINTER_PREFIX + key +
              AbstractPrinterSpecificationLoader.PRINTER_ENCODINGS,
              createEncodingsString(spec.getSupportedEncodings()));
    }

    final SortedConfigurationWriter writer = new SortedConfigurationWriter();
    writer.save(out, p);
  }

  private String createEncodingsString (final PrinterEncoding[] encodings)
  {
    final StringBuffer b = new StringBuffer();
    for (int i = 0; i < encodings.length; i++)
    {
      if (i > 0)
      {
        b.append(",");
      }
      b.append(encodings[i].getInternalName());
    }
    return b.toString();
  }

  private String createCodeString (final byte[] code)
  {
    final StringBuffer b = new StringBuffer();
    for (int i = 0; i < code.length; i++)
    {
      if (i > 0)
      {
        b.append(",");
      }
      b.append(Integer.toString(code[i]));
    }
    return b.toString();
  }

}
