/**
 * Date: Jan 14, 2003
 * Time: 1:15:13 PM
 *
 * $Id: AbstractTemplate.java,v 1.1 2003/01/14 21:04:36 taqua Exp $
 */
package com.jrefinery.report.filter.templates;

public abstract class AbstractTemplate implements Template
{
  private String name;

  public AbstractTemplate()
  {
  }

  public void setName(String name)
  {
    if (name == null) throw new NullPointerException();
    this.name = name;
  }

  public String getName()
  {
    return name;
  }

  /**
   * Clones this <code>DataSource</code>.
   *
   * @return the clone.
   *
   * @throws CloneNotSupportedException this should never happen.
   */
  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }

  public Template getInstance()
  {
    try
    {
      return (Template) clone();
    }
    catch (CloneNotSupportedException cne)
    {
      throw new IllegalStateException("Clone not supported");
    }
  }
}
