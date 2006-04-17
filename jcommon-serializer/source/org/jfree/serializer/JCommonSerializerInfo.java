package org.jfree.serializer;

import java.util.Arrays;

import org.jfree.ui.about.ProjectInfo;
import org.jfree.ui.about.Contributor;
import org.jfree.JCommonInfo;

/**
 * Creation-Date: 23.10.2005, 18:49:39
 *
 * @author Thomas Morgner
 */
public class JCommonSerializerInfo extends ProjectInfo
{
  /**
   * The info singleton.
   */
  private static JCommonSerializerInfo singleton;

  /**
   * Returns the single instance of this class.
   *
   * @return The single instance of information about the JCommon library.
   */
  public static synchronized JCommonSerializerInfo getInstance() {
      if (singleton == null) {
          singleton = new JCommonSerializerInfo();
      }
      return singleton;
  }


  /**
   * Constructs an empty project info object.
   */
  private JCommonSerializerInfo() {
      final JCommonInfo info = JCommonInfo.getInstance();

      setName("JCommon-Serializer");
      setVersion(info.getVersion());
      setInfo(info.getInfo());
      setCopyright(info.getCopyright());

      setLicenceName(info.getLicenceName());
      setLicenceText(info.getLicenceText());

      setContributors(Arrays.asList(new Contributor[]{
          new Contributor("David Gilbert", "david.gilbert@object-refinery.com"),
          new Contributor("Thomas Morgner", "tmorgner@pentaho.org"),
      }));
  }

}
