package org.jfree.report.dev.locales;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.TreeMap;

import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

public class LanguageCollection
{
  private TreeMap languages;

  public LanguageCollection ()
  {
    languages = new TreeMap();
  }

  public void load () throws IOException
  {
    final InputStream in = ObjectUtilities.getResourceRelativeAsStream
            ("iso639-2-language-code.csv", LanguageCollection.class);
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
      final String cc = trimmedLine.substring(0, 2);
      final String name = trimmedLine.substring(3);
      languages.put (cc, name);
    }

    reader.close();
  }

  public String[][] getLanguages ()
  {
    final String[][] retval = new String[languages.size()][];
    final String[] codes = getCodes();
    for (int i = 0; i < codes.length; i++)
    {
      retval[i] = new String[2];
      retval[i][0] = codes[i];
      retval[i][1] = (String) languages.get(codes[i]);
    }
    return retval;
  }

  public String[] getCodes ()
  {
    return (String[]) languages.keySet().toArray(new String[languages.size()]);
  }

  public boolean isValidCode (final String c)
  {
    return languages.containsKey(c);
  }

  public boolean isValidLanguage (final String c)
  {
    return languages.containsValue(c);
  }
}
