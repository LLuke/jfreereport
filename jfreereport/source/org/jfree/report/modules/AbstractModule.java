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
 * AbstractModule.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: AbstractModule.java,v 1.3 2003/07/11 18:33:20 taqua Exp $
 *
 * Changes
 * -------------------------
 * 05.07.2003 : Initial version
 *
 */

package org.jfree.report.modules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public abstract class AbstractModule extends DefaultModuleInfo implements Module
{
  private class ReaderHelper
  {
    private String buffer;
    private BufferedReader reader;

    public ReaderHelper(BufferedReader reader) throws IOException
    {
      this.reader = reader;
    }

    public boolean hasNext () throws IOException
    {
      if (buffer == null)
      {
        buffer = readLine();
      }
      return buffer != null;
    }

    public String next () throws IOException
    {
      String line = buffer;
      buffer = null;
      return line;
    }

    public void pushBack (String line)
    {
      buffer = line;
    }

    protected String readLine () throws IOException
    {
      String line = reader.readLine();
      while (line != null && (line.length() == 0 || line.startsWith("#")))
      {
        // empty line or comment is ignored
        line = reader.readLine();
      }
      return line;
    }

    public void close() throws IOException
    {
      reader.close();
    }
  }

  private String name;
  private String description;
  private String producer;

  public AbstractModule()
  {
    setModuleClass(this.getClass().getName());
  }
  protected void loadModuleInfo () throws ModuleInitializeException
  {
    InputStream in = getClass().getResourceAsStream("module.properties");
    if (in == null)
    {
      throw new ModuleInitializeException
          ("File 'module.properties' not found in module package.");
    }
    loadModuleInfo(in);
  }

  protected void loadModuleInfo (InputStream in) throws ModuleInitializeException
  {
    try
    {
      if (in == null)
      {
        throw new NullPointerException
            ("Given InputStream is null.");
      }
      ReaderHelper rh = new ReaderHelper(new BufferedReader
          (new InputStreamReader(in, "ISO-8859-1")));

      ArrayList optionalModules = new ArrayList();
      ArrayList dependendModules = new ArrayList();

      while (rh.hasNext())
      {
        String lastLineRead = rh.next();
        if (lastLineRead.startsWith("module-info:"))
        {
          readModuleInfo(rh);
        }
        else if (lastLineRead.startsWith("depends:"))
        {
          dependendModules.add (readExternalModule (rh));
        }
        else if (lastLineRead.startsWith("optional:"))
        {
          optionalModules.add (readExternalModule(rh));
        }
        else
        {
          // we dont understand the current line, so we skip it ...
          // should we throw a parse exception instead?
        }
      }
      rh.close();

      this.optionalModules = (ModuleInfo[])
          optionalModules.toArray(new ModuleInfo[optionalModules.size()]);

      this.requiredModules = (ModuleInfo[])
          dependendModules.toArray(new ModuleInfo[dependendModules.size()]);
    }
    catch (IOException ioe)
    {
      throw new ModuleInitializeException("Failed to load properties", ioe);
    }
  }

  private String readValue (ReaderHelper reader, String firstLine)
    throws IOException
  {
    StringBuffer b = new StringBuffer();
    while (firstLine != null && parseKey(firstLine) == null)
    {
      if (b.length() != 0)
      {
        b.append("\n");
      }
      b.append(parseValue(firstLine.trim()));
      firstLine = reader.next();
    }
    return b.toString();
  }

  private void readModuleInfo (ReaderHelper reader) throws IOException
  {
    while (reader.hasNext())
    {
      String lastLineRead = reader.next();

      if (Character.isWhitespace(lastLineRead.charAt(0)) == false)
      {
        // break if the current character is no whitespace ...
        reader.pushBack(lastLineRead);
        return;
      }

      String line = lastLineRead.trim();
      String key = parseKey(line);
      if (key != null)
      {
        // parse error: Non data line does not contain a colon
        String b = readValue(reader, parseValue(line.trim()));

        if (key.equals("name"))
        {
          setName(b);
        }
        else if (key.equals("producer"))
        {
          setProducer(b);
        }
        else if (key.equals("description"))
        {
          setDescription(b);
        }
        else if (key.equals("version.major"))
        {
          setMajorVersion(b);
        }
        else if (key.equals("version.minor"))
        {
          setMinorVersion(b);
        }
        else if (key.equals("version.patchlevel"))
        {
          setPatchLevel(b);
        }
      }
    }
  }

  private String parseKey (String line)
  {
    int idx = line.indexOf(':');
    if (idx == -1)
    {
      return null;
    }
    return line.substring(0, idx);
  }

  private String parseValue (String line)
  {
    int idx = line.indexOf(':');
    if (idx == -1)
    {
      return line;
    }
    return line.substring(idx + 1);
  }

  private DefaultModuleInfo readExternalModule (ReaderHelper reader)
      throws IOException
  {
    DefaultModuleInfo mi = new DefaultModuleInfo();

    while (reader.hasNext())
    {
      String lastLineRead = reader.next();

      if (Character.isWhitespace(lastLineRead.charAt(0)) == false)
      {
        // break if the current character is no whitespace ...
        reader.pushBack(lastLineRead);
        return mi;
      }

      String line = lastLineRead.trim();
      String key = parseKey(line);
      if (key != null)
      {
        String b = readValue(reader, parseValue(line));
        if (key.equals("module"))
        {
          mi.setModuleClass(b);
        }
        else if (key.equals("version.major"))
        {
          mi.setMajorVersion(b);
        }
        else if (key.equals("version.minor"))
        {
          mi.setMinorVersion(b);
        }
        else if (key.equals("version.patchlevel"))
        {
          mi.setPatchLevel(b);
        }
      }
    }
    return mi;
  }


  public String getName()
  {
    return name;
  }

  protected void setName(String name)
  {
    this.name = name;
  }

  public String getDescription()
  {
    return description;
  }

  protected void setDescription(String description)
  {
    this.description = description;
  }

  public String getProducer()
  {
    return producer;
  }

  protected void setProducer(String producer)
  {
    this.producer = producer;
  }

  private ModuleInfo[] requiredModules;
  private ModuleInfo[] optionalModules;

  public ModuleInfo[] getRequiredModules()
  {
    ModuleInfo[] retval = new ModuleInfo[requiredModules.length];
    System.arraycopy(requiredModules, 0, retval, 0, requiredModules.length);
    return retval;
  }

  public ModuleInfo[] getOptionalModules()
  {
    ModuleInfo[] retval = new ModuleInfo[optionalModules.length];
    System.arraycopy(optionalModules, 0, retval, 0, optionalModules.length);
    return retval;
  }

  protected void setRequiredModules(ModuleInfo[] requiredModules)
  {
    this.requiredModules = new ModuleInfo[requiredModules.length];
    System.arraycopy(requiredModules, 0, this.requiredModules, 0, requiredModules.length);
  }

  protected void setOptionalModules(ModuleInfo[] optionalModules)
  {
    this.optionalModules = new ModuleInfo[optionalModules.length];
    System.arraycopy(optionalModules, 0, this.optionalModules, 0, optionalModules.length);
  }

  public String toString ()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("Module : ");
    buffer.append(getName());
    buffer.append("\n");
    buffer.append("ModuleClass : ");
    buffer.append(getModuleClass());
    buffer.append("\n");
    buffer.append("Version: ");
    buffer.append(getMajorVersion());
    buffer.append(".");
    buffer.append(getMinorVersion());
    buffer.append(".");
    buffer.append(getPatchLevel());
    buffer.append("\n");
    buffer.append("Producer: ");
    buffer.append(getProducer());
    buffer.append("\n");
    buffer.append("Description: ");
    buffer.append(getDescription());
    buffer.append("\n");
    return buffer.toString();
  }

  /**
   * tries to load a class to indirectly check for the existence
   * of a certain library.
   *
   * @param name the name of the library class.
   * @return true, if the class could be loaded, false otherwise.
   */
  public boolean isClassLoadable (String name)
  {
    try
    {
      Thread.currentThread().getContextClassLoader().loadClass(name);
      return true;
    }
    catch (Exception e)
    {
      return false;
    }
  }

  /**
   * Configures the module by loading the configuration properties and
   * adding them to the package configuration.
   */
  public void configure()
  {
    InputStream in = getClass().getResourceAsStream("configuration.properties");
    if (in == null)
    {
      return;
    }
    PackageManager.getInstance().getPackageConfiguration().load(in);
  }

  public void performExternalInitialize (String classname)
    throws ModuleInitializeException
  {
    ModuleInitializer mi = null;
    try
    {
      Class c = Thread.currentThread().getContextClassLoader().loadClass(classname);
      mi = (ModuleInitializer) c.newInstance();
    }
    catch (Exception e)
    {
      throw new ModuleInitializeException("Failed to load specified initializer class.", e);
    }
    mi.performInit();

  }
}
