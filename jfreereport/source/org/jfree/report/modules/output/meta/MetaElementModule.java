package org.jfree.report.modules.output.meta;

import org.jfree.base.modules.AbstractModule;
import org.jfree.base.modules.SubSystem;
import org.jfree.base.modules.ModuleInitializeException;

public class MetaElementModule extends AbstractModule
{
  public MetaElementModule ()
          throws ModuleInitializeException
  {
    loadModuleInfo();
  }

  /**
   * Initializes the module. Use this method to perform all initial setup operations. This
   * method is called only once in a modules lifetime. If the initializing cannot be
   * completed, throw a ModuleInitializeException to indicate the error,. The module will
   * not be available to the system.
   *
   * @param subSystem the subSystem.
   * @throws org.jfree.base.modules.ModuleInitializeException
   *          if an error ocurred while initializing the module.
   */
  public void initialize (final SubSystem subSystem)
          throws ModuleInitializeException
  {
  }
}
