package org.jfree.pixie;

import java.util.Arrays;

import org.jfree.JCommon;
import org.jfree.ui.about.Contributor;
import org.jfree.ui.about.Licences;
import org.jfree.ui.about.ProjectInfo;

public class PixieInfo extends ProjectInfo
{
  private static PixieInfo singleton;

  /**
   * Returns the single instance of this class.
   *
   * @return The single instance of information about the JCommon library.
   */
  public static PixieInfo getInstance ()
  {
    if (singleton == null)
    {
      singleton = new PixieInfo();
    }
    return singleton;
  }

  /**
   * Creates a new instance. (Must be public so that we can instantiate
   * the library-info using Class.newInstance(..)).
   */
  public PixieInfo ()
  {
    setName("Pixie");
    setVersion("0.8.4");
    setInfo("Pixie is a viewer library for WindowsMetaFiles (WMF)");
    setCopyright
            ("(C)opyright 2000-2004, by Thomas Morgner, Object Refinery Limited and Contributors");

    setLicenceName("LGPL");
    setLicenceText(Licences.getInstance().getLGPL());

    setContributors(Arrays.asList(new Contributor[]
    {
      new Contributor("David Gilbert", "david.gilbert@object-refinery.com"),
      new Contributor("Thomas Morgner", "taqua@users.sourceforge.net"),
    }));

    addLibrary(JCommon.INFO);
    addDependency(JCommon.INFO);
  }
}
