package org.jfree.report.modules.output.pageable.plaintext;

import org.jfree.report.style.FontDefinition;

public interface FontMapper
{
  public byte getPrinterFont (FontDefinition fontDefinition);
  public byte getDefaultFont ();
}
