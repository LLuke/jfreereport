package org.jfree.report.modules.gui.base;

import org.jfree.report.util.ReportConfiguration;

/**
 * Creation-Date: 05.09.2005, 19:52:42
 *
 * @author: Thomas Morgner
 */
public class DefaultPluginSelector implements ExportPluginSelector
{
  private Class pluginClass;
  private String preference;
  private String enableKey;

  public DefaultPluginSelector(final Class pluginClass,
                               final String preference,
                               final String enableKey)
  {
    this.pluginClass = pluginClass;
    this.preference = preference;
    this.enableKey = enableKey;
  }

  public boolean equals(final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }

    final DefaultPluginSelector that = (DefaultPluginSelector) o;

    if (!pluginClass.equals(that.pluginClass))
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    return pluginClass.hashCode();
  }

  public boolean isPluginValid(final PreviewProxy proxy, final ReportConfiguration config)
  {
    return config.getConfigProperty(enableKey, "false").equals("true");
  }

  public String getEnableKey ()
  {
    return enableKey;
  }

  public Class getPluginClass()
  {
    return pluginClass;
  }

  public String getPreference()
  {
    return preference;
  }
}
