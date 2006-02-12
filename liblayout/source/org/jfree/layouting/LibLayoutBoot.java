package org.jfree.layouting;

import org.jfree.base.AbstractBoot;
import org.jfree.base.BootableProjectInfo;
import org.jfree.util.Configuration;

public class LibLayoutBoot extends AbstractBoot
{
  private static LibLayoutBoot singleton;

  public static LibLayoutBoot getInstance()
  {
    if (singleton == null)
    {
      singleton = new LibLayoutBoot();
    }
    return singleton;
  }

  private LibLayoutBoot ()
  {
  }

  /**
   * Returns the project info.
   *
   * @return The project info.
   */
  protected BootableProjectInfo getProjectInfo ()
  {
    return LibLayoutInfo.getInstance();
  }

  /**
   * Loads the configuration.
   *
   * @return The configuration.
   */
  protected Configuration loadConfiguration ()
  {
    return createDefaultHierarchicalConfiguration
            ("/org/jfree/layouting/layout.properties",
             "/layout.properties", true);
  }

  /**
   * Performs the boot.
   */
  protected void performBoot ()
  {
    
  }
}
