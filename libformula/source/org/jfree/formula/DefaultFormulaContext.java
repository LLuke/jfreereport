/**
 * =========================================
 * LibLayout : a free Java layouting library
 * =========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2006, by Pentaho Corperation and Contributors.
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
 * DefaultFormulaContext.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: DefaultFormulaContext.java,v 1.1 2006/11/04 15:40:56 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.formula;

import java.util.HashMap;

import org.jfree.formula.function.DefaultFunctionRegistry;
import org.jfree.formula.function.FunctionRegistry;
import org.jfree.formula.operators.DefaultOperatorFactory;
import org.jfree.formula.operators.OperatorFactory;
import org.jfree.formula.typing.DefaultTypeRegistry;
import org.jfree.formula.typing.Type;
import org.jfree.formula.typing.TypeRegistry;
import org.jfree.formula.typing.coretypes.AnyType;
import org.jfree.util.Configuration;

/**
 * Creation-Date: 31.10.2006, 16:32:32
 *
 * @author Thomas Morgner
 */
public class DefaultFormulaContext implements FormulaContext
{
  private DefaultTypeRegistry typeRegistry;
  private DefaultFunctionRegistry functionRegistry;
  private OperatorFactory operatorFactory;
  private LocalizationContext localizationContext;
  private Configuration config;
  private HashMap references;

  public DefaultFormulaContext()
  {
    this(LibFormulaBoot.getInstance().getGlobalConfig());
  }

  public DefaultFormulaContext(Configuration config)
  {
    this.config = config;
    localizationContext = new DefaultLocalizationContext();
    typeRegistry = new DefaultTypeRegistry();
    typeRegistry.initialize(config, localizationContext);
    functionRegistry = new DefaultFunctionRegistry();
    functionRegistry.initialize(config);
    operatorFactory = new DefaultOperatorFactory();
    operatorFactory.initalize(config);
  }

  public OperatorFactory getOperatorFactory()
  {
    return operatorFactory;
  }

  public void defineReference(String name, Object value)
  {
    if (references == null)
    {
      references = new HashMap();
    }
    references.put(name, value);
  }

  public Object resolveReference(String name)
  {
    if (references == null)
    {
      return null;
    }
    return references.get(name);
  }

  public Configuration getConfiguration()
  {
    return config;
  }

  public FunctionRegistry getFunctionRegistry()
  {
    return functionRegistry;
  }

  public Type resolveReferenceType(String name)
  {
    return AnyType.TYPE;
  }

  public TypeRegistry getTypeRegistry()
  {
    return typeRegistry;
  }

  public LocalizationContext getLocalizationContext()
  {
    return localizationContext;
  }

  public boolean isReferenceDirty(String name)
  {
    return true;
  }
}