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
 * IncludeParser.java
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
 * 14.07.2003 : Initial version
 *  
 */

package org.jfree.report.modules.parser.base;

import org.jfree.xml.Parser;

public class IncludeParser extends Parser
{
  public static final String INCLUDE_PARSING_KEY = "include-parsing";

  private Parser backend;

  public IncludeParser(Parser backend)
  {
    this.backend = backend;
    setConfigProperty(IncludeParser.INCLUDE_PARSING_KEY, "true");
  }

  public Parser getInstance()
  {
    return new IncludeParser(backend);
  }

  public Object getResult()
  {
    return backend.getResult();
  }

  public String getConfigProperty(String key)
  {
    return super.getConfigProperty(key, backend.getConfigProperty(key));
  }

  public String getConfigProperty(String key, String defaultValue)
  {
    return super.getConfigProperty(key,  backend.getConfigProperty(key, defaultValue));
  }

  public void setHelperObject(String key, Object o)
  {
    super.setHelperObject(key, o);
  }

  public Object getHelperObject(String key)
  {
    Object o = super.getHelperObject(key);
    if (o == null)
    {
      return backend.getHelperObject(key);
    }
    return o;
  }
}
