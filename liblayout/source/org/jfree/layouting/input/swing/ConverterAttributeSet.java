package org.jfree.layouting.input.swing;

/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * ConverterAttributeSet.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   Cedric Pronzato;
 *
 * $Id: Converter.java,v 1.1 2006/11/07 22:38:11 mimil Exp $
 *
 * Changes
 * -------
 *
 *
 */
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.AttributeSet;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: mimil
 * Date: Nov 12, 2006
 * Time: 9:56:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConverterAttributeSet extends SimpleAttributeSet
{
  // todo : use a classe to have a better typing instead of a simple String
  public static String NOT_TYPED = "not_typed";

  private Map typeMap;

  public ConverterAttributeSet() {
    super();
    typeMap = new HashMap();
  }

  public void addAttribute(Object name, Object value)
  {
    addAttribute(NOT_TYPED, name, value);
  }

  public synchronized void addAttribute(Object type, Object name, Object value) {
    if(type == null)
    {
      type = NOT_TYPED;
    }
    
    typeMap.put(name, type);
    super.addAttribute(name, value);
  }

  public void addAttributes(ConverterAttributeSet attributes)
  {
    super.addAttributes(attributes);
    typeMap.putAll(attributes.getTypeMap());
  }


  public void removeAttribute(Object name)
  {
    super.removeAttribute(name);
    typeMap.remove(name);
  }

  public Map getTypeMap()
  {
    return typeMap;
  }

  public void setTypeMap(Map typeMap)
  {
    this.typeMap = typeMap;
  }

  public AttributeSet getAttributesByType(Object type) {
    final SimpleAttributeSet attr = new SimpleAttributeSet();

    final Iterator it = typeMap.keySet().iterator();
    while(it.hasNext())
    {
      final Object name = typeMap.get(it.next());
      if (name != null || name.equals(type))
      {
        attr.addAttribute(name, getAttribute(name));
      }
    }

    return attr;
  }
}
