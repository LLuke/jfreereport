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
 * $Id$
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
  private String name;
  private String description;
  private String producer;

  private String lastLineRead;

  public AbstractModule()
  {
    setModuleClass(this.getClass().getName());
  }

  protected String readLine (BufferedReader reader) throws IOException
  {
    String line = reader.readLine();
    while (line != null && line.length() == 0 || line.startsWith("#"))
    {
      // empty line or comment is ignored
      line = reader.readLine();
    }
    return line;
  }

  protected void loadModuleInfo () throws ModuleInitializeException
  {
    try
    {
      InputStream in = getClass().getResourceAsStream("module.properties");
      if (in == null)
      {
        throw new ModuleInitializeException
            ("File 'module.properties' not found in module package.");
      }
      BufferedReader reader = new BufferedReader(new InputStreamReader(in, "ISO-8859-1"));
      lastLineRead = readLine(reader);
      ArrayList optionalModules = new ArrayList();
      ArrayList dependendModules = new ArrayList();

      while (lastLineRead != null)
      {
        if (lastLineRead.startsWith("module-info:"))
        {
          readModuleInfo(reader);
        }
        else if (lastLineRead.startsWith("depends:"))
        {
          dependendModules.add (readExternalModule (reader));
        }
        else if (lastLineRead.startsWith("optional:"))
        {
          optionalModules.add (readExternalModule(reader));
        }
        else
        {
          // we dont understand the current line, so we skip it ...
          // should we throw a parse exception instead?
          lastLineRead = readLine(reader);
        }
      }
      reader.close();

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

  private void readModuleInfo (BufferedReader reader) throws IOException
  {

    lastLineRead = readLine(reader);
    while (lastLineRead != null)
    {
      if (Character.isWhitespace(lastLineRead.charAt(0)) == false)
      {
        // break if the current character is no whitespace ...
        return;
      }

      String line = lastLineRead.trim();
      String key = parseKey(line);
      if (key == null)
      {
        // parse error: Non data line does not contain a colon
        continue;
      }

      StringBuffer b = new StringBuffer();
      while (parseKey(line) == null)
      {
        if (b.length() != 0)
        {
          b.append("\n");
        }
        b.append(parseValue(line.trim()));
        lastLineRead = readLine(reader);
      }

      if (key.equals("name"))
      {
        setName(b.toString());
      }
      else if (key.equals("producer"))
      {
        setProducer(b.toString());
      }
      else if (key.equals("description"))
      {
        setDescription(b.toString());
      }
      else if (key.equals("version.major"))
      {
        setMajorVersion(b.toString());
      }
      else if (key.equals("version.minor"))
      {
        setMinorVersion(b.toString());
      }
      else if (key.equals("version.patchlevel"))
      {
        setPatchLevel(b.toString());
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
    return line.substring(idx);
  }

  private DefaultModuleInfo readExternalModule (BufferedReader reader) throws IOException
  {
    DefaultModuleInfo mi = new DefaultModuleInfo();

    lastLineRead = readLine(reader);
    while (lastLineRead != null)
    {

      if (Character.isWhitespace(lastLineRead.charAt(0)) == false)
      {
        // break if the current character is no whitespace ...
        return mi;
      }

      String line = lastLineRead.trim();
      String key = parseKey(line);
      if (key == null)
      {
        // parse error: Non data line does not contain a colon
        continue;
      }

      StringBuffer b = new StringBuffer();
      while (parseKey(line) == null)
      {
        if (b.length() != 0)
        {
          b.append("\n");
        }
        b.append(parseValue(line.trim()));
        lastLineRead = readLine(reader);
      }

      if (key.equals("module"))
      {
        mi.setModuleClass(b.toString());
      }
      else if (key.equals("version.major"))
      {
        mi.setMajorVersion(b.toString());
      }
      else if (key.equals("version.minor"))
      {
        mi.setMinorVersion(b.toString());
      }
      else if (key.equals("version.patchlevel"))
      {
        mi.setPatchLevel(b.toString());
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

  public void activate()
  {
    // todo load the report configuration for the module... 
  }
}
