package org.jfree.layouting;

import java.util.Arrays;

import org.jfree.JCommon;
import org.jfree.loader.LibLoaderInfo;
import org.jfree.ui.about.Contributor;
import org.jfree.ui.about.ProjectInfo;

public class LibLayoutInfo extends ProjectInfo
{
  private static LibLayoutInfo info;

  /**
   * Constructs an empty project info object.
   */
  private LibLayoutInfo ()
  {
    setName("LibLayout");
    setVersion("pre-alpha-01");
    setInfo("http://www.jfree.org/jfreereport/index.html");
    setCopyright ("(C)opyright 2000-2004, by Thomas Morgner, " +
            "Object Refinery Limited and Contributors");

    setContributors(Arrays.asList(
        new Contributor[]
        {
          new Contributor("Thomas Morgner", "taqua@users.sourceforge.net"),
        }
    ));

    addLibrary(JCommon.INFO);
    addDependency(JCommon.INFO);
    addLibrary(LibLoaderInfo.getInstance());
    addDependency(LibLoaderInfo.getInstance());
    setBootClass(LibLayoutBoot.class.getName());
    setAutoBoot(true);
  }

  public static ProjectInfo getInstance()
  {
    if (info == null)
    {
      info = new LibLayoutInfo();
    }
    return info;
  }
}
