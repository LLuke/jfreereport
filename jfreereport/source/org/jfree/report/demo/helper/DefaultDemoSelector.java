package org.jfree.report.demo.helper;

import java.util.ArrayList;

/**
 * Creation-Date: 27.08.2005, 13:28:38
 *
 * @author: Thomas Morgner
 */
public class DefaultDemoSelector implements DemoSelector
{
  private ArrayList demos;
  private ArrayList childs;
  private String name;

  public DefaultDemoSelector(final String name)
  {
    if (name == null)
    {
      throw new NullPointerException();
    }
    this.name = name;
    this.demos = new ArrayList();
    this.childs = new ArrayList();
  }

  public String getName()
  {
    return name;
  }

  public void addChild (DemoSelector selector)
  {
    if (selector == null)
    {
      throw new NullPointerException();
    }
    childs.add(selector);
  }

  public DemoSelector[] getChilds()
  {
    return (DemoSelector[]) childs.toArray(new DemoSelector[childs.size()]);
  }

  public void addDemo (DemoHandler handler)
  {
    if (handler == null)
    {
      throw new NullPointerException();
    }
    demos.add(handler);
  }

  public DemoHandler[] getDemos()
  {
    return (DemoHandler[]) demos.toArray(new DemoHandler[demos.size()]);
  }

  public int getChildCount()
  {
    return childs.size();
  }

  public int getDemoCount()
  {
    return demos.size();
  }
}
