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
 * DefaultOperatorFactory.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corperation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.formula.operators;

import java.util.HashMap;
import java.util.Iterator;

import org.jfree.util.Configuration;
import org.jfree.util.ObjectUtilities;

/**
 * Creation-Date: 02.11.2006, 12:29:27
 *
 * @author Thomas Morgner
 */
public class DefaultOperatorFactory implements OperatorFactory
{
  private static final String INFIX_PREFIX = "org.jfree.formula.operators.infix.";
  private static final String PREFIX_PREFIX = "org.jfree.formula.operators.prefix.";
  private static final String POSTFIX_PREFIX = "org.jfree.formula.operators.postfix.";

  private HashMap infixOperators;
  private HashMap prefixOperators;
  private HashMap postfixOperators;

  public DefaultOperatorFactory()
  {
    infixOperators = new HashMap();
    prefixOperators = new HashMap();
    postfixOperators = new HashMap();
  }

  public void initalize(Configuration configuration)
  {
    loadInfixOperators(configuration);
    loadPrefixOperators(configuration);
    loadPostfixOperators(configuration);
  }

  private void loadInfixOperators(final Configuration configuration)
  {
    final Iterator infixKeys = configuration.findPropertyKeys(INFIX_PREFIX);
    while (infixKeys.hasNext())
    {
      final String configKey = (String) infixKeys.next();
      if (configKey.endsWith(".class") == false)
      {
        continue;
      }
      final String operatorClass = configuration.getConfigProperty(configKey);
      if (operatorClass == null)
      {
        continue;
      }
      if (operatorClass.length() == 0)
      {
        continue;
      }
      final String tokenKey = configKey.substring
          (0, configKey.length() - ".class".length()) + ".token";
      final String token = configuration.getConfigProperty(tokenKey);
      if (token == null)
      {
        continue;
      }
      final String tokenTrimmed = token.trim();
      if (tokenTrimmed.length() != 1)
      {
        continue;
      }

      final Object operator = ObjectUtilities.loadAndInstantiate
          (operatorClass, DefaultOperatorFactory.class);
      if (operator instanceof InfixOperator)
      {
        infixOperators.put (tokenTrimmed, operator);
      }
    }
  }

  private void loadPrefixOperators(final Configuration configuration)
  {
    final Iterator infixKeys = configuration.findPropertyKeys(PREFIX_PREFIX);
    final int infixLength = PREFIX_PREFIX.length();
    while (infixKeys.hasNext())
    {
      final String configKey = (String) infixKeys.next();
      if (configKey.endsWith(".class") == false)
      {
        continue;
      }
      final String operatorClass = configuration.getConfigProperty(configKey);
      if (operatorClass == null)
      {
        continue;
      }
      if (operatorClass.length() == 0)
      {
        continue;
      }
      final String tokenKey = configKey.substring
          (0, configKey.length() - ".class".length()) + ".token";
      final String token = configuration.getConfigProperty(tokenKey);
      if (token == null)
      {
        continue;
      }
      final String tokenTrimmed = token.trim();
      if (tokenTrimmed.length() != 1)
      {
        continue;
      }

      final Object operator = ObjectUtilities.loadAndInstantiate
          (operatorClass, DefaultOperatorFactory.class);
      if (operator instanceof PrefixOperator)
      {
        prefixOperators.put (tokenTrimmed, operator);
      }
    }
  }

  private void loadPostfixOperators(final Configuration configuration)
  {
    final Iterator infixKeys = configuration.findPropertyKeys(POSTFIX_PREFIX);
    final int infixLength = POSTFIX_PREFIX.length();
    while (infixKeys.hasNext())
    {
      final String configKey = (String) infixKeys.next();
      if (configKey.endsWith(".class") == false)
      {
        continue;
      }
      final String operatorClass = configuration.getConfigProperty(configKey);
      if (operatorClass == null)
      {
        continue;
      }
      if (operatorClass.length() == 0)
      {
        continue;
      }
      final String tokenKey = configKey.substring
          (0, configKey.length() - ".class".length()) + ".token";
      final String token = configuration.getConfigProperty(tokenKey);
      if (token == null)
      {
        continue;
      }
      final String tokenTrimmed = token.trim();
      if (tokenTrimmed.length() != 1)
      {
        continue;
      }

      final Object operator = ObjectUtilities.loadAndInstantiate
          (operatorClass, DefaultOperatorFactory.class);
      if (operator instanceof PostfixOperator)
      {
        postfixOperators.put (tokenTrimmed, operator);
      }
    }
  }

  public InfixOperator createInfixOperator(String operator)
  {
    return (InfixOperator) infixOperators.get(operator);
  }

  public PostfixOperator createPostfixOperator(String operator)
  {
    return (PostfixOperator) postfixOperators.get(operator);
  }

  public PrefixOperator createPrefixOperator(String operator)
  {
    return (PrefixOperator) prefixOperators.get(operator);
  }
}