package org.jfree.report.modules.output.pageable.plaintext;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.jfree.util.DefaultConfiguration;
import org.jfree.util.Log;

public class PrinterSpecificationManager
        extends AbstractPrinterSpecificationLoader

{
  private class GenericPrinterSpecification
          implements PrinterSpecification
  {
    private PrinterEncoding genericEncoding;

    public GenericPrinterSpecification ()
    {
      genericEncoding = new PrinterEncoding
              ("ASCII", "ASCII", "ASCII", new byte[]{0,0});
    }

    public String getDisplayName ()
    {
      return "Generic";
    }

    /**
     * Returns the encoding definition for the given java encoding.
     *
     * @param encoding the java encoding that should be mapped into a printer specific
     *                 encoding.
     * @return the printer specific encoding.
     */
    public PrinterEncoding getEncoding (final String encoding)
    {
      return genericEncoding;
    }

    /**
     * Returns the name of the encoding mapping. This is usually the same as the printer
     * model name.
     *
     * @return the printer model.
     */
    public String getName ()
    {
      return "Generic";
    }

    /**
     * Checks, whether the given Java-encoding is supported.
     *
     * @param encoding the java encoding that should be mapped into a printer specific
     *                 encoding.
     * @return true, if there is a mapping, false otherwise.
     */
    public boolean isEncodingSupported (final String encoding)
    {
      return true;
    }

    /**
     * Returns true, if a given operation is supported, false otherwise.
     *
     * @param operationName the operation, that should be performed
     * @return true, if the printer will be able to perform that operation, false
     *         otherwise.
     */
    public boolean isFeatureAvailable (final String operationName)
    {
      return true;
    }
  }

  private HashMap printerModels;
  private PrinterSpecification generic;

  protected PrinterSpecificationManager ()
  {
    // Maps all encodings to ASCII
    generic = new GenericPrinterSpecification();
    printerModels = new HashMap();
    printerModels.put(generic.getName(), generic);
  }

  public void load (final String resourceName)
  {
    final InputStream in = getClass().getResourceAsStream(resourceName);
    if (in == null)
    {
      Log.error ("Printer definition is missing: " + resourceName);
      return;
    }
    try
    {
      load (in);
    }
    catch (IOException e)
    {
      Log.error ("Unable to load printer definition file " + resourceName, e);
    }
  }

  public synchronized void load (final InputStream in)
          throws IOException
  {
    final DefaultConfiguration encodingConfig = new DefaultConfiguration();
    encodingConfig.load(in);

    final PrinterEncoding[] encodings = loadEncodings(encodingConfig);
    final PrinterSpecification[] printers = loadPrinters (encodingConfig, encodings);

    for (int i = 0; i < printers.length; i++)
    {
      addPrinter(printers[i]);
    }
  }

  private void addPrinter (final PrinterSpecification printer)
  {
    printerModels.put(printer.getName(), printer);
  }

  public String[] getPrinterNames ()
  {
    return (String[]) printerModels.keySet().toArray(new String[printerModels.size()]);
  }

  public PrinterSpecification getPrinter (final String name)
  {
    return (PrinterSpecification) printerModels.get (name);
  }

  public PrinterSpecification getGenericPrinter ()
  {
    return generic;
  }
}
