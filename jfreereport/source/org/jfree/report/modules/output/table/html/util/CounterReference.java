/**
 * Created on 07.11.2003
 *
 */
package org.jfree.report.modules.output.table.html.util;

/**
 * A simple counter carrier.
 * 
 * @author Thomas Morgner
 */
public final class CounterReference
{
  /** a counter. */
  private int count;
  
  /**
   * DefaultConstructor.
   */
  public CounterReference ()
  {
  }
  
  /**
   * Returns the current count.
   * @return the current count.
   */
  public int getCount()
  {
    return count;
  }
  
  /**
   * increases the current count by one.
   */
  public void increase()
  {
    count++;
  }
}
