package org.jfree.report.dev.locales;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.TreeMap;

import org.jfree.util.Log;

public class CountryCollection
{
  private TreeMap countries;

  public CountryCollection ()
  {
    countries = new TreeMap();
  }

  public void load () throws IOException
  {
    final InputStream in = getClass().getResourceAsStream("iso3166-country-codes.csv");
    if (in == null)
    {
      Log.debug ("Unable to find required resource");
      return;
    }
    final BufferedReader reader = new BufferedReader (new InputStreamReader(in, "ASCII"));

    String line;
    while ((line = reader.readLine()) != null)
    {
      if (line.startsWith("#"))
      {
        // comment ...
        continue;
      }
      final String trimmedLine = line.trim();
      if (trimmedLine.length() < 4)
      {
        // empty or invalid line
        continue;
      }
      final int index = trimmedLine.indexOf(';');
      if (index < 0)
      {
        continue;
      }
      final String name = trimmedLine.substring(0, index);
      final String cc = trimmedLine.substring(index + 1);
      countries.put (cc, name);
    }

    reader.close();
  }

  public String[][] getCountries ()
  {
    final String[][] retval = new String[countries.size()][];
    final String[] codes = getCodes();
    for (int i = 0; i < codes.length; i++)
    {
      retval[i] = new String[2];
      retval[i][0] = codes[i];
      retval[i][1] = (String) countries.get(codes[i]);
    }
    return retval;
  }

  public String[] getCodes ()
  {
    return (String[]) countries.keySet().toArray(new String[countries.size()]);
  }

  public boolean isValidCode (final String c)
  {
    return countries.containsKey(c);
  }

  public boolean isValidCountry (final String c)
  {
    return countries.containsValue(c);
  }
}
