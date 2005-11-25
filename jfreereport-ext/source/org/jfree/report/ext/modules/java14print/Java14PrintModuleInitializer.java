package org.jfree.report.ext.modules.java14print;

import javax.print.attribute.HashAttributeSet;

import org.jfree.base.modules.ModuleInitializeException;
import org.jfree.base.modules.ModuleInitializer;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.modules.gui.base.ExportPluginFactory;
import org.jfree.report.modules.gui.print.AWTPrintingGUIModule;
import org.jfree.report.modules.gui.print.PrintPluginSelector;
import org.jfree.util.ObjectUtilities;


/**
 * An initializer for the Java 1.4 Image-I/O API.
 * @author Thomas Morgner
 */
public class Java14PrintModuleInitializer implements ModuleInitializer
{
  /**
   * Default Constructor.
   */
  public Java14PrintModuleInitializer()
  {
  }

  /**
   * Initializes the module and defines the storage implementation.
   *
   * @throws ModuleInitializeException if an error ocurred.
   */
  public void performInit() throws ModuleInitializeException
  {
    if (ObjectUtilities.loadAndInstantiate
          (HashAttributeSet.class.getName(), Java14PrintModule.class) == null)
    {
      return;
    }

    String printOrder = JFreeReportBoot.getInstance().getGlobalConfig().getConfigProperty
        (AWTPrintingGUIModule.PRINT_ORDER_KEY, "0");

    ExportPluginFactory.getInstance().registerPlugin(new PrintPluginSelector
        (Java14PrintingPlugin.class, printOrder, AWTPrintingGUIModule.PRINT_ENABLE_KEY,
                false));

    final JFreeReportBoot subSystem = JFreeReportBoot.getInstance();
    if (subSystem.getGlobalConfig().getConfigProperty
            (AWTPrintingGUIModule.PRINT_SERVICE_KEY) == null)
    {
      subSystem.getPackageManager().getPackageConfiguration().setConfigProperty
            ("org.jfree.report.modules.gui.print.PrintService", Java14PrintingPlugin.class.getName());
    }
  }
}
