package org.jfree.layouting.layouter.i18n;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DefaultLocalizationContext implements LocalizationContext
{
  public DateFormat getDateFormat (Locale language)
  {
    return DateFormat.getDateInstance(DateFormat.MEDIUM, language);
  }

  public NumberFormat getIntegerFormat (Locale language)
  {
    return NumberFormat.getInstance(language);
  }

  public NumberFormat getNumberFormat (Locale language)
  {
    return NumberFormat.getNumberInstance(language);
  }

  public DateFormat getTimeFormat (Locale language)
  {
    return DateFormat.getTimeInstance(DateFormat.MEDIUM, language);
  }

  public DateFormat getDateFormat (String format, Locale language)
  {
    return new SimpleDateFormat(format, language);
  }
}
