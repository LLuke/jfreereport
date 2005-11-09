package org.jfree.fonts;

import org.jfree.base.AbstractBoot;
import org.jfree.base.BootableProjectInfo;
import org.jfree.util.Configuration;

/**
 * Creation-Date: 06.11.2005, 18:25:11
 *
 * @author Thomas Morgner
 */
public class LibFontBoot extends AbstractBoot
{
  private static LibFontBoot instance;

  public static synchronized LibFontBoot getInstance()
  {
    if (instance == null)
    {
      instance = new LibFontBoot();
    }
    return instance;
  }

  private LibFontBoot()
  {
  }

  protected Configuration loadConfiguration()
  {
    return null;
  }

  protected void performBoot()
  {

  }

  protected BootableProjectInfo getProjectInfo()
  {
    return null;
  }
}
