package org.jfree.report.demo.helper;

/**
 * Creation-Date: 27.08.2005, 12:35:48
 *
 * @author: Thomas Morgner
 */
public interface DemoSelector
{
  public String getName();
  public DemoSelector[] getChilds();
  public int getChildCount();
  public DemoHandler[] getDemos();
  public int getDemoCount();
}
