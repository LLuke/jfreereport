package org.jfree.report.dev.printerspecs;

public class PrinterEditorData
{
  private static PrinterEditorData data;
  private PrinterEncodingTableModel ninePinPrinters;
  private PrinterEncodingTableModel twentyfourPinPrinters;
  private String lruNinePinPrinterFile;
  private String lruTwentyfourPinPrinterFile;

  private PrinterEditorData ()
  {
    ninePinPrinters = new PrinterEncodingTableModel();
    twentyfourPinPrinters = new PrinterEncodingTableModel();
  }

  public static synchronized PrinterEditorData getInstance()
  {
    if (data == null)
    {
      data = new PrinterEditorData();
    }
    return data;
  }

  public PrinterEncodingTableModel getNinePinPrinters ()
  {
    return ninePinPrinters;
  }

  public PrinterEncodingTableModel getTwentyfourPinPrinters ()
  {
    return twentyfourPinPrinters;
  }

  public String getLruNinePinPrinterFile ()
  {
    return lruNinePinPrinterFile;
  }

  public void setLruNinePinPrinterFile (final String lruNinePinPrinterFile)
  {
    this.lruNinePinPrinterFile = lruNinePinPrinterFile;
  }

  public String getLruTwentyfourPinPrinterFile ()
  {
    return lruTwentyfourPinPrinterFile;
  }

  public void setLruTwentyfourPinPrinterFile (final String lruTwentyfourPinPrinterFile)
  {
    this.lruTwentyfourPinPrinterFile = lruTwentyfourPinPrinterFile;
  }
}
