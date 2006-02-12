package org.jfree.layouting.layouter.i18n;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Locale;

public interface LocalizationContext
{
  public DateFormat getDateFormat (Locale language);
  public DateFormat getDateFormat (String format, Locale language);
  public DateFormat getTimeFormat (Locale language);

  public NumberFormat getIntegerFormat (Locale language);
  public NumberFormat getNumberFormat (Locale language);
}
