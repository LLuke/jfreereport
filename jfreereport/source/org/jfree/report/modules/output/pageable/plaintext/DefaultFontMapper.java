package org.jfree.report.modules.output.pageable.plaintext;

import java.util.HashMap;

import org.jfree.report.style.FontDefinition;

public class DefaultFontMapper implements FontMapper
{
  private HashMap fontMapping;
  private byte defaultFont;

  public DefaultFontMapper ()
  {
    fontMapping = new HashMap();
    defaultFont = PrinterDriver.SELECT_FONT_ROMAN;
  }

  public void addFontMapping (final String fontName, final byte printerCode)
  {
    if (fontName == null)
    {
      throw new NullPointerException();
    }
    fontMapping.put(fontName, new Byte(printerCode));
  }

  public void removeFontMapping (final String fontName)
  {
    fontMapping.remove(fontName);
  }

  public byte getPrinterFont (final FontDefinition fontDefinition)
  {
    final Byte b = (Byte) fontMapping.get(fontDefinition.getFontName());
    if (b != null)
    {
      return b.byteValue();
    }
    return handleDefault(fontDefinition);
  }

  protected byte handleDefault (final FontDefinition fd)
  {
    if (fd.isCourier())
    {
      return PrinterDriver.SELECT_FONT_COURIER;
    }
    if (fd.isSerif())
    {
      return PrinterDriver.SELECT_FONT_ROMAN;
    }
    if (fd.isSansSerif())
    {
      return PrinterDriver.SELECT_FONT_OCR_A;
    }
    return defaultFont;
  }

  public byte getDefaultFont ()
  {
    return defaultFont;
  }

  public void setDefaultFont (final byte defaultFont)
  {
    this.defaultFont = defaultFont;
  }
}
