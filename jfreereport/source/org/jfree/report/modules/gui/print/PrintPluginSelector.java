package org.jfree.report.modules.gui.print;

import org.jfree.report.modules.gui.base.DefaultPluginSelector;
import org.jfree.report.modules.gui.base.PreviewProxy;
import org.jfree.util.Configuration;

/**
 * Creation-Date: 05.09.2005, 20:04:37
 *
 * @author: Thomas Morgner
 */
public class PrintPluginSelector extends DefaultPluginSelector
{
  private static final String PRINT_SERVICE_KEY =
          "org.jfree.report.modules.gui.print.PrintService";
  private boolean defaultService;

  public PrintPluginSelector(final Class pluginClass,
                             final String preference,
                             final String enableKey,
                             final boolean defaultService)
  {
    super(pluginClass, preference, enableKey);
    this.defaultService = defaultService;
  }

  public boolean isPluginValid(final PreviewProxy proxy,
                               final Configuration config)
  {
    final String activeService = config.getConfigProperty(PRINT_SERVICE_KEY);

    // if no service is defined and this is the default, activate ...
    if (activeService == null && defaultService)
    {
      return super.isPluginValid(proxy, config);
    }

    final String serviceName = getPluginClass().getName();
    // check, whether this service is the same as the defined one
    if (serviceName.equals(activeService))
    {
      return super.isPluginValid(proxy, config);
    }

    return false;
  }
}
