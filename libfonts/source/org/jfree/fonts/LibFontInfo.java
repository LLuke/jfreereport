package org.jfree.fonts;

import org.jfree.ui.about.ProjectInfo;
import org.jfree.JCommon;

/**
 * Creation-Date: 06.11.2005, 18:24:57
 *
 * @author Thomas Morgner
 */
public class LibFontInfo extends ProjectInfo
{
  private static LibFontInfo instance;

  public static synchronized LibFontInfo getInstance()
  {
    if (instance == null)
    {
      instance = new LibFontInfo();
    }
    return instance;
  }

  private LibFontInfo()
  {
    setName("LibFont");
    setVersion("0.0.1");
    addLibrary(JCommon.INFO);
    addDependency(JCommon.INFO);
  }
}
