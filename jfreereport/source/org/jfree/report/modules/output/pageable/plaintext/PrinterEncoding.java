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
    final byte[] retval  = new byte[code.length];
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
