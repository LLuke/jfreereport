/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2006, by Object Refinery Limited, Pentaho Corporation and Contributors.
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
 * Element.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: Element.java,v 1.2 2006/04/21 17:31:23 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */

package org.jfree.report.structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Locale;

import org.jfree.layouting.input.style.CSSStyleRule;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.box.BoxStyleKeys;
import org.jfree.layouting.input.style.keys.box.Visibility;
import org.jfree.layouting.namespace.Namespaces;
import org.jfree.layouting.util.LocaleUtility;
import org.jfree.report.JFreeReportInfo;
import org.jfree.report.function.Expression;
import org.jfree.report.util.AttributeMap;

/**
 * An element is a node that can have attributes. The 'id' and the 'name'
 * attribute is defined for all elements.
 * <p/>
 * Both the name and the id attribute may be null.
 * <p/>
 * Properties in the 'http://jfreereport.sourceforge.net/namespaces/engine/flow'
 * namespace and in the
 * 'http://jfreereport.sourceforge.net/namespaces/engine/compatibility'
 * namespace are considered internal. You should only touch them, if you really
 * know what you are doing. 
 *
 * @author Thomas Morgner
 */
public abstract class Element extends Node
{
  private static final String[] EMPTY_VARIABLES = new String[0];
  private static final Expression[] EMPTY_EXPRESSIONS = new Expression[0];

  public static final String NAME_ATTRIBUTE = "name";
  public static final String ID_ATTRIBUTE = "id";
  /** The type corresponds (somewhat) to the tagname of HTML. */
  public static final String TYPE_ATTRIBUTE = "type";
  /** See XML-Namespaces for the idea of that one ... */
  public static final String NAMESPACE_ATTRIBUTE = "namespace";

  /** The name of the element. */
  private AttributeMap attributes;
  private CSSStyleRule style;
  private ArrayList expressions;
  private HashSet variables;
  private AttributeMap attributeExpressions;
  private HashMap styleExpressions;
  private boolean enabled;
  private Expression displayCondition;

  /**
   * Constructs an element.
   * <p/>
   * The element inherits the element's defined default ElementStyleSheet to
   * provide reasonable default values for common stylekeys. When the element is
   * added to the band, the bands stylesheet is set as parent to the element's
   * stylesheet.
   * <p/>
   * A datasource is assigned with this element is set to a default source,
   * which always returns null.
   */
  protected Element()
  {
    this.attributes = new AttributeMap();
    this.style = new CSSStyleRule(null, null);
    this.expressions = new ArrayList();
    this.variables = new HashSet();
    this.attributeExpressions = new AttributeMap();
    this.styleExpressions = new HashMap();
    this.enabled = true;
    setNamespace(JFreeReportInfo.REPORT_NAMESPACE);
  }

  public String getNamespace()
  {
    return (String) getAttribute(JFreeReportInfo.REPORT_NAMESPACE, NAMESPACE_ATTRIBUTE);
  }

  public void setNamespace(final String id)
  {
    setAttribute(JFreeReportInfo.REPORT_NAMESPACE, NAMESPACE_ATTRIBUTE, id);
  }

  public String getId()
  {
    return (String) getAttribute("", ID_ATTRIBUTE);
  }

  public void setId(final String id)
  {
    setAttribute("", ID_ATTRIBUTE, id);
  }

  public String getType()
  {
    return (String) getAttribute(JFreeReportInfo.REPORT_NAMESPACE, TYPE_ATTRIBUTE);
  }

  public void setType(final String type)
  {
    setAttribute(JFreeReportInfo.REPORT_NAMESPACE, TYPE_ATTRIBUTE, type);
  }

  /**
   * Defines the name for this Element. The name must not be empty, or a
   * NullPointerException is thrown.
   * <p/>
   * Names can be used to lookup an element within a band. There is no
   * requirement for element names to be unique.
   *
   * @param name the name of this self (null not permitted)
   * @throws NullPointerException if the given name is null.
   */
  public void setName(final String name)
  {
    if (name == null)
    {
      throw new NullPointerException("Element.setName(...): name is null.");
    }
    setAttribute("", NAME_ATTRIBUTE, name);
  }


  /**
   * Returns the name of the Element. The name of the Element is never null.
   *
   * @return the name.
   */
  public String getName()
  {
    return (String) getAttribute("", NAME_ATTRIBUTE);
  }

  public void setAttribute(String name, Object value)
  {
    setAttribute(getNamespace(), name, value);
  }

  public void setAttribute(final String namespace, String name, Object value)
  {
    if (name == null)
    {
      throw new NullPointerException();
    }
    this.attributes.setAttribute(namespace, name, value);
  }

  public Object getAttribute(String name)
  {
    return getAttribute(getNamespace(), name);
  }

  public Object getAttribute(String namespace, String name)
  {
    return this.attributes.getAttribute(namespace, name);
  }

  public Map getAttributes(String namespace)
  {
    return this.attributes.getAttributes(namespace);
  }

  public AttributeMap getAttributeMap()
  {
    return new AttributeMap(this.attributes);
  }

  public String[] getAttributeNameSpaces()
  {
    return this.attributes.getNameSpaces();
  }

  /**
   * Returns this elements private stylesheet. This sheet can be used to
   * override the default values set in one of the parent-stylesheets.
   *
   * @return the Element's stylesheet
   */
  public CSSStyleRule getStyle()
  {
    return style;
  }

  public void setVisibility(Visibility v)
  {
    getStyle().setPropertyValue(BoxStyleKeys.VISIBILITY, v);
  }


  public Visibility getVisibility()
  {
    return (Visibility) getStyle().getPropertyCSSValue(BoxStyleKeys.VISIBILITY);
  }

  public void setAttributeExpression(final String attr,
                                     final Expression function)
  {
    setAttribute(getNamespace(), attr, function);
  }

  /**
   * Adds a function to the report's collection of expressions.
   *
   * @param function the function.
   */
  public void setAttributeExpression(final String namespace,
                                     final String attr,
                                     final Expression function)
  {
    attributeExpressions.setAttribute(namespace, attr, function);
  }

  /**
   * Returns the expressions for the report.
   *
   * @return the expressions.
   */
  public Expression getAttributeExpression(final String attr)
  {
    return getAttributeExpression(getNamespace(), attr);
  }

  public Expression getAttributeExpression(final String namespace,
                                           final String attr)
  {
    return (Expression) attributeExpressions.getAttribute(namespace, attr);
  }

  public Map getAttributeExpressions(String namespace)
  {
    return attributeExpressions.getAttributes(namespace);
  }

  public AttributeMap getAttributeExpressionMap()
  {
    return new AttributeMap(this.attributeExpressions);
  }


  /**
   * Adds a function to the report's collection of expressions.
   *
   * @param function the function.
   */
  public void setStyleExpression(final StyleKey stylename,
                                 final Expression function)
  {
    if (function == null)
    {
      styleExpressions.remove(stylename);
    }
    else
    {
      styleExpressions.put(stylename, function);
    }
  }

  /**
   * Returns the expressions for the report.
   *
   * @return the expressions.
   */
  public Expression getStyleExpression(final StyleKey stylename)
  {
    return (Expression) styleExpressions.get(stylename);
  }

  public Map getStyleExpressions()
  {
    return styleExpressions;
  }

  /**
   * Adds a function to the report's collection of expressions.
   *
   * @param function the function.
   */
  public void addExpression(final Expression function)
  {
    expressions.add(function);
  }

  /**
   * Returns the expressions for the report.
   *
   * @return the expressions.
   */
  public Expression[] getExpressions()
  {
    if (expressions.size() == 0)
    {
      return EMPTY_EXPRESSIONS;
    }
    return (Expression[]) expressions.toArray(
            new Expression[expressions.size()]);
  }

  /**
   * Sets the expressions for the report.
   *
   * @param expressions the expressions (<code>null</code> not permitted).
   */
  public void setExpressions(final Expression[] expressions)
  {
    if (expressions == null)
    {
      throw new NullPointerException(
              "JFreeReport.setExpressions(...) : null not permitted.");
    }
    this.expressions.clear();
    this.expressions.addAll(Arrays.asList(expressions));
  }

  /**
   * Returns true, if the element is enabled.
   *
   * @return true or false
   */
  public boolean isEnabled()
  {
    return enabled;
  }

  /**
   * Defines whether the element is enabled. Disabled elements will be fully
   * ignored by the report processor. This is a design time property to exclude
   * elements from the processing without actually having to deal with the other
   * complex properties.
   *
   * @param enabled
   */
  public void setEnabled(final boolean enabled)
  {
    this.enabled = enabled;
  }

  public void addVariable(String name)
  {
    this.variables.add(name);
  }

  public void removeVariable(String name)
  {
    this.variables.remove(name);
  }

  public String[] getVariables()
  {
    if (variables.isEmpty())
    {
      return EMPTY_VARIABLES;
    }
    return (String[]) this.variables.toArray(new String[this.variables.size()]);
  }

  public Expression getDisplayCondition()
  {
    return displayCondition;
  }

  public void setDisplayCondition(final Expression displayCondition)
  {
    this.displayCondition = displayCondition;
  }

  public Locale getLocale()
  {
    final Locale locale = getLocaleFromAttributes();
    if (locale != null)
    {
      return locale;
    }
    return super.getLocale();
  }

  protected Locale getLocaleFromAttributes ()
  {
    final Object mayBeXmlLang = getAttribute(Namespaces.XML_NAMESPACE, "lang");
    if (mayBeXmlLang instanceof String)
    {
      return LocaleUtility.createLocale((String) mayBeXmlLang);
    }
    else if (mayBeXmlLang instanceof Locale)
    {
      return (Locale) mayBeXmlLang;
    }

    final Object mayBeXhtmlLang = getAttribute(Namespaces.XHTML_NAMESPACE, "lang");
    if (mayBeXhtmlLang instanceof String)
    {
      return LocaleUtility.createLocale((String) mayBeXhtmlLang);
    }
    else if (mayBeXhtmlLang instanceof Locale)
    {
      return (Locale) mayBeXhtmlLang;
    }

    final Object mayBeHtmlLang = getAttribute(Namespaces.XHTML_NAMESPACE, "lang");
    if (mayBeHtmlLang instanceof String)
    {
      return LocaleUtility.createLocale((String) mayBeHtmlLang);
    }
    else if (mayBeHtmlLang instanceof Locale)
    {
      return (Locale) mayBeHtmlLang;
    }

    return null;
  }
}
