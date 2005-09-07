package org.jfree.report.ext.modules.java14imageio;

import org.jfree.base.modules.ModuleInitializeException;
import org.jfree.base.modules.ModuleInitializer;
import org.jfree.report.resourceloader.ImageFactory;


/**
 * An initializer for the Java 1.4 Image-I/O API.
 * @author Thomas Morgner
 */
public class Java14ImageIOModuleInitializer implements ModuleInitializer
{
  /**
   * Default Constructor.
   */
  public Java14ImageIOModuleInitializer()
  {
  }

  /**
   * Initializes the module and defines the storage implementation.
   *
   * @throws ModuleInitializeException if an error ocurred.
   */
  public void performInit() throws ModuleInitializeException
  {
    ImageFactory.getInstance().registerModule(new Java14ImageFactoryModule());
  }
}