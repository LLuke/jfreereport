package org.jfree.xmlns;

import org.jfree.base.AbstractBoot;
import org.jfree.base.BootableProjectInfo;
import org.jfree.util.Configuration;

public class LibXmlBoot extends AbstractBoot
{
  private static LibXmlBoot singleton;

  public static synchronized LibXmlBoot getInstance()
  {
    if (singleton == null)
    {
      singleton = new LibXmlBoot();
    }
    return singleton;
  }

  private LibXmlBoot ()
  {
  }

  /**
   * Returns the project info.
   *
   * @return The project info.
   */
  protected BootableProjectInfo getProjectInfo ()
  {
    return LibXmlInfo.getInstance();
  }

  /**
   * Loads the configuration.
   *
   * @return The configuration.
   */
  protected Configuration loadConfiguration ()
  {
    return createDefaultHierarchicalConfiguration
            ("/org/jfree/xmlns/libxml.properties",
             "/libxml.properties", true);
  }

  /**
   * Performs the boot.
   */
  protected void performBoot ()
  {
  }
}
