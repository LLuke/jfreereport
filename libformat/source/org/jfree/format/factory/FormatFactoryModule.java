package org.jfree.format.factory;

import java.text.Format;
import java.util.Properties;

public interface FormatFactoryModule
{
  public Format createFormat (Properties properties);
  public Format createFormat (String formatString);

  public String createFormatString (Format format);
  public Properties createProperties (Format format);
}
