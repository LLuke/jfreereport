/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * ------------------------------
 * JFreeReportBoot.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: JFreeReportBoot.java,v 1.6 2005/09/06 11:40:19 taqua Exp $
 *
 * Changes
 * -------------------------
 * 20-Jan-2005 : Initial version
 */
package org.jfree.report;

import java.util.Enumeration;

import org.jfree.base.AbstractBoot;
import org.jfree.base.BootableProjectInfo;
import org.jfree.base.config.ModifiableConfiguration;
import org.jfree.base.config.HierarchicalConfiguration;
import org.jfree.base.config.SystemPropertyConfiguration;
import org.jfree.base.config.PropertyFileConfiguration;
import org.jfree.base.log.DefaultLog;
import org.jfree.base.modules.PackageManager;
import org.jfree.report.util.CSVTokenizer;
import org.jfree.util.Configuration;
import org.jfree.util.Log;

/**
 * An utility class to safely boot and initialize the JFreeReport library. This class
 * should be called before using the JFreeReport classes, to make sure that all subsystems
 * are initialized correctly and in the correct order.
 * <p/>
 * Application developers should make sure, that the booting is done, before JFreeReport
 * objects are used. Although the boot process will be started automaticly if needed, this
 * automated start may no longer guarantee the module initialization order.
 * <p/>
 * Additional modules can be specified by defining the system property
 * <code>"org.jfree.report.boot.Modules"</code>. The property expects a comma-separated
 * list of {@link org.jfree.base.modules.Module} implementations.
 * <p/>
 * Booting should be done by aquirering a new boot instance using {@link
 * JFreeReportBoot#getInstance()} and then starting the boot process with {@link
 * JFreeReportBoot#start()}.
 *
 * @author Thomas Morgner
 */
public class JFreeReportBoot extends AbstractBoot
{
  /**
   * A wrappper around the user supplied global configuration.
   */
  private static class UserConfigWrapper extends HierarchicalConfiguration
          implements Configuration
  {
    /** The wrapped configuration. */
    private Configuration wrappedConfiguration;

    /**
     * Default constructor.
     */
    public UserConfigWrapper ()
    {
    }

    /**
     * Sets a new configuration. This configuration will be inserted into the
     * report configuration hierarchy. Set this property to null to disable
     * the user defined configuration.
     *
     * @param wrappedConfiguration the wrapped configuration.
     */
    public void setWrappedConfiguration (final Configuration wrappedConfiguration)
    {
      this.wrappedConfiguration = wrappedConfiguration;
    }

    /**
     * Returns the user supplied global configuration, if exists.
     *
     * @return the user configuration.
     */
    public Configuration getWrappedConfiguration ()
    {
      return wrappedConfiguration;
    }

    /**
     * Returns the configuration property with the specified key.
     *
     * @param key the property key.
     * @return the property value.
     */
    public String getConfigProperty (final String key)
    {
      if (wrappedConfiguration == null)
      {
        return getParentConfig().getConfigProperty(key);
      }

      final String retval = wrappedConfiguration.getConfigProperty(key);
      if (retval != null)
      {
        return retval;
      }
      return getParentConfig().getConfigProperty(key);
    }

    /**
     * Returns the configuration property with the specified key
     * (or the specified default value if there is no such property).
     * <p/>
     * If the property is not defined in this configuration, the code
     * will lookup the property in the parent configuration.
     *
     * @param key          the property key.
     * @param defaultValue the default value.
     * @return the property value.
     */
    public String getConfigProperty (final String key, final String defaultValue)
    {
      if (wrappedConfiguration == null)
      {
        return getParentConfig().getConfigProperty(key, defaultValue);
      }

      final String retval = wrappedConfiguration.getConfigProperty(key, null);
      if (retval != null)
      {
        return retval;
      }
      return getParentConfig().getConfigProperty(key, defaultValue);
    }

    /**
     * Sets a configuration property.
     *
     * @param key   the property key.
     * @param value the property value.
     */
    public void setConfigProperty (final String key, final String value)
    {
      if (wrappedConfiguration instanceof ModifiableConfiguration)
      {
        final ModifiableConfiguration modConfiguration =
                (ModifiableConfiguration) wrappedConfiguration;
        modConfiguration.setConfigProperty(key, value);
      }
    }

    /**
     * Returns all defined configuration properties for the report. The enumeration
     * contains all keys of the changed properties, properties set from files or
     * the system properties are not included.
     *
     * @return all defined configuration properties for the report.
     */
    public Enumeration getConfigProperties ()
    {
      if (wrappedConfiguration instanceof ModifiableConfiguration)
      {
        final ModifiableConfiguration modConfiguration =
                (ModifiableConfiguration) wrappedConfiguration;
        return modConfiguration.getConfigProperties();
      }
      return super.getConfigProperties();
    }
  }

  /**
   * The singleton instance of the Boot class.
   */
  private static JFreeReportBoot instance;
  /**
   * The project info contains all meta data about the project.
   */
  private BootableProjectInfo projectInfo;

  private static transient UserConfigWrapper configWrapper = new UserConfigWrapper();

  /**
   * Creates a new instance.
   */
  private JFreeReportBoot ()
  {
    projectInfo = new JFreeReportInfo();
  }

  /**
   * Returns the singleton instance of the boot utility class.
   *
   * @return the boot instance.
   */
  public static synchronized JFreeReportBoot getInstance ()
  {
    if (instance == null)
    {
      // make sure that I am able to debug the package manager ..
      DefaultLog.getDefaultLog();
      instance = new JFreeReportBoot();
    }
    return instance;
  }

  public ModifiableConfiguration getEditableConfig()
  {
    return (ModifiableConfiguration) getGlobalConfig();
  }

  /**
   * Returns the project info.
   *
   * @return The project info.
   */
  protected BootableProjectInfo getProjectInfo ()
  {
    return projectInfo;
  }

  /**
   * Loads the configuration. This will be called exactly once.
   *
   * @return The configuration.
   */
  protected Configuration loadConfiguration ()
  {
    HierarchicalConfiguration globalConfig = new HierarchicalConfiguration();

    final PropertyFileConfiguration rootProperty = new PropertyFileConfiguration();
    rootProperty.load("/org/jfree/report/jfreereport.properties");
    rootProperty.load("/org/jfree/report/ext/jfreereport-ext.properties");
    globalConfig.insertConfiguration(rootProperty);
    globalConfig.insertConfiguration(JFreeReportBoot.getInstance().getPackageManager()
            .getPackageConfiguration());

    final PropertyFileConfiguration baseProperty = new PropertyFileConfiguration();
    baseProperty.load("/jfreereport.properties");
    globalConfig.insertConfiguration(baseProperty);

    globalConfig.insertConfiguration(configWrapper);

    final SystemPropertyConfiguration systemConfig = new SystemPropertyConfiguration();
    globalConfig.insertConfiguration(systemConfig);
    return globalConfig;
  }

  /**
   * Performs the actual boot process.
   */
  protected void performBoot ()
  {
    if (isStrictFP() == false)
    {
      Log.warn("The used VM seems to use a non-strict floating point arithmetics");
      Log.warn("Layouts computed with this Java Virtual Maschine may be invalid.");
      Log.warn("JFreeReport and the library 'iText' depend on the strict floating point rules");
      Log.warn("of Java1.1 as implemented by the Sun Virtual Maschines.");
      Log.warn("If you are using the BEA JRockit VM, start the Java VM with the option");
      Log.warn("'-Xstrictfp' to restore the default behaviour.");
    }

    final PackageManager mgr = getPackageManager();

    mgr.addModule(JFreeReportCoreModule.class.getName());
    mgr.addModule(DefaultLogModule.class.getName());
    mgr.load("org.jfree.report.modules.");
    mgr.load("org.jfree.report.ext.modules.");

    bootAdditionalModules();
    mgr.initializeModules();
  }

  /**
   * Boots modules, which have been spcified in the "org.jfree.report.boot.Modules"
   * configuration parameter.
   */
  private void bootAdditionalModules ()
  {
    try
    {
      final String bootModules =
              getGlobalConfig().getConfigProperty
              ("org.jfree.report.boot.Modules");
      if (bootModules != null)
      {
        final CSVTokenizer csvToken = new CSVTokenizer(bootModules, ",");
        while (csvToken.hasMoreTokens())
        {
          final String token = csvToken.nextToken();
          getPackageManager().load(token);
        }
      }
    }
    catch (SecurityException se)
    {
      Log.info("Security settings forbid to check the system properties for extension modules.");
    }
    catch (Exception se)
    {
      Log.error
              ("An error occured while checking the system properties for extension modules.", se);
    }
  }


  /**
   * This method returns true on non-strict floating point systems.
   * <p/>
   * Since Java 1.2 Virtual Maschines may implement the floating point arithmetics in a
   * more performant way, which does not put the old strict constraints on the floating
   * point types <code>float</code> and <code>double</code>.
   * <p/>
   * As iText and this library requires strict (in the sense of Java1.1) floating point
   * operations, we have to test for that feature here.
   * <p/>
   * The only known VM that seems to implement that feature is the JRockit VM. The strict
   * mode can be restored on that VM by adding the "-Xstrictfp" VM parameter.
   *
   * @return true, if the VM uses strict floating points by default, false otherwise.
   */
  private static boolean isStrictFP ()
  {
    final double d = 8e+307;
    final double result1 = 4.0 * d * 0.5;
    final double result2 = 2.0 * d;
    return (result1 != result2 && (result1 == Double.POSITIVE_INFINITY));
  }


  /**
   * Returns the user supplied global configuration.
   *
   * @return the user configuration, if any.
   */
  public static Configuration getUserConfig ()
  {
    return configWrapper.getWrappedConfiguration();
  }

  /**
   * Defines the global user configuration.
   *
   * @param config the user configuration.
   */
  public static void setUserConfig (final Configuration config)
  {
    configWrapper.setWrappedConfiguration(config);
  }

}
