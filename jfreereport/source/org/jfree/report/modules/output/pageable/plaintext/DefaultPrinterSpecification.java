package org.jfree.report.modules.output.pageable.plaintext;

import java.util.HashMap;
import java.util.TreeSet;

public class DefaultPrinterSpecification
        implements PrinterSpecification, Cloneable
{
  private String name;
  private String displayName;
  private HashMap encodings;
  private TreeSet operations;
  private PrinterEncoding[] encodingsCached;

  public DefaultPrinterSpecification (final String name, final String displayName)
  {
    this.encodings = new HashMap();
    this.operations = new TreeSet();
    this.name = name;
    this.displayName = displayName;
  }

  public String getDisplayName ()
  {
    return displayName;
  }

  /**
   * Returns the name of the encoding mapping. This is usually the
   * same as the printer model name.
   *
   * @return the printer model.
   */
  public String getName ()
  {
    return name;
  }

  public synchronized boolean isEncodingSupported (final String encoding)
  {
    if (encoding == null)
    {
      throw new NullPointerException();
    }
    return encodings.containsKey(encoding.toLowerCase());
  }

  public synchronized boolean contains (final PrinterEncoding encoding)
  {
    if (encoding == null)
    {
      throw new NullPointerException();
    }
    return encodings.containsValue(encoding);
  }

  /**
   * Returns the encoding definition for the given java encoding.
   *
   * @param encoding the java encoding that should be mapped into a printer specific
   *                 encoding.
   * @return the printer specific encoding.
   * @throws IllegalArgumentException if the given encoding is not supported.
   */
  public synchronized PrinterEncoding getEncoding (final String encoding)
  {
    if (encoding == null)
    {
      throw new NullPointerException();
    }
    final PrinterEncoding enc = (PrinterEncoding) encodings.get(encoding.toLowerCase());
    if (enc == null)
    {
      throw new IllegalArgumentException("Encoding is not supported.");
    }
    return enc;
  }

  public synchronized void addEncoding (final PrinterEncoding encoding)
  {
    if (encoding == null)
    {
      throw new NullPointerException();
    }
    if (encodings.containsKey(encoding.getEncoding()) == false)
    {
      encodings.put (encoding.getEncoding().toLowerCase(), encoding);
      encodingsCached = null;
    }
  }

  public synchronized void removeEncoding (final PrinterEncoding encoding)
  {
    if (encoding == null)
    {
      throw new NullPointerException();
    }
    if (encodings.remove (encoding.getEncoding().toLowerCase()) != null)
    {
      encodingsCached = null;
    }
  }

  public synchronized PrinterEncoding[] getSupportedEncodings ()
  {
    if (encodingsCached == null)
    {
      final PrinterEncoding[] encodingArray = new PrinterEncoding[encodings.size()];
      encodingsCached = (PrinterEncoding[]) encodings.values().toArray(encodingArray);
    }
    return encodingsCached;
  }

  public Object clone ()
          throws CloneNotSupportedException
  {
    final DefaultPrinterSpecification spec = (DefaultPrinterSpecification) super.clone();
    spec.encodings = (HashMap) encodings.clone();
    return spec;
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
    return operations.contains(operationName);
  }
}
