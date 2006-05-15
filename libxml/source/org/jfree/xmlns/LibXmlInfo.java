package org.jfree.xmlns;

import java.util.Arrays;

import org.jfree.ui.about.ProjectInfo;
import org.jfree.ui.about.Contributor;
import org.jfree.JCommon;
import org.jfree.resourceloader.LibLoaderInfo;

public class LibXmlInfo extends ProjectInfo
{
  private static LibXmlInfo info;
  
  /**
   * Constructs an empty project info object.
   */
  public LibXmlInfo ()
  {
    setName("LibXML");
    setVersion("0.1.1");
    setInfo("http://www.jfree.org/jfreereport/index.html");
    setCopyright ("(C)opyright 2006, by Thomas Morgner, " +
            "Object Refinery Limited and Contributors");

    setContributors(Arrays.asList(
        new Contributor[]
        {
          new Contributor("Thomas Morgner", "taqua@users.sourceforge.net"),
        }
    ));

    addLibrary(JCommon.INFO);
    addLibrary(LibLoaderInfo.getInstance());

    setBootClass(LibXmlBoot.class.getName());
    setAutoBoot(true);
  }


  public static synchronized ProjectInfo getInstance()
  {
    if (info == null)
    {
      info = new LibXmlInfo();
    }
    return info;
  }
}
