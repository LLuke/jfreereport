/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
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
 * $Id: IncludeParser.java,v 1.2 2003/08/20 18:56:51 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 14-Jul-2003 : Initial version
 *  
 */

package org.jfree.report.modules.parser.base;

import org.jfree.xml.Parser;

/**
 * The include parser is used to support include statements in
 * the report definition. It contains a special mark to indicate
 * that this is a included report definition and uses the
 * configuration settings of an parent parser.
 * 
 * @author Thomas Morgner
 */
public class IncludeParser extends Parser
{
  /** The key that indicates that this is a included parser. */
  public static final String INCLUDE_PARSING_KEY = "include-parsing";

  /** The parser backend that supplies the configuration. */
  private Parser backend;

  /**
   * Creates a new include parser with the given parser as backend.
   * 
   * @param backend the backend parser that provides the configuration.
   */
  public IncludeParser(Parser backend)
  {
    this.backend = backend;
    setConfigProperty(IncludeParser.INCLUDE_PARSING_KEY, "true");
  }

  /**
   * Returns a new parser instance. 
   * @see org.jfree.xml.Parser#getInstance()
   * 
   * @return the new include parser instance using the same backend as
   * this instance.
   */
  public Parser getInstance()
  {
    return new IncludeParser(backend);
  }

  /**
   * Returns the parser result. Returns the backends result. 
   * @see org.jfree.xml.Parser#getResult()
   * 
   * @return the backends result.
   */
  public Object getResult()
  {
    return backend.getResult();
  }

  /**
   * Returns the configuration property stored with the given key.
   * Uses the backend as provider for the default value. 
   *  
   * @see org.jfree.util.Configuration#getConfigProperty(java.lang.String)
   * 
   * @param key the property name.
   * @return the stored value
   */
  public String getConfigProperty(String key)
  {
    return super.getConfigProperty(key, backend.getConfigProperty(key));
  }

  /**
   * Returns the configuration property stored with the given key using
   * the given default values as final fallback.
   * Uses the backend as provider for the default value. The backends
   * value will be used before the default value is returned.  
   *  
   * @see org.jfree.util.Configuration#getConfigProperty(java.lang.String)
   * 
   * @param key the property name.
   * @param defaultValue the default value that should be returned in case
   * that neither this parser or the backend contains a value for that key.
   * @return the stored value
   */
  public String getConfigProperty(String key, String defaultValue)
  {
    return super.getConfigProperty(key,  backend.getConfigProperty(key, defaultValue));
  }

  /**
   * Returns a helper object which is stored on the parser. If the helper
   * object is not defined in this object, this method will look for it
   * in the backend. 
   * @see org.jfree.xml.Parser#getHelperObject(java.lang.String)
   * 
   * @param key the helper object key
   * @return the helper object or null if there is no such object stored.
   */
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
