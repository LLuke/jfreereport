/**
 * Date: Dec 1, 2002
 * Time: 5:55:33 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.pageable.pagelayout;

/**
 * An utility class which is able to decide how somelayout tasks are done ...
 */
public abstract class LayoutAgent
{
  public LayoutAgent()
  {
  }

  public abstract LayoutAgentProgress processTask (LayoutTask task);
}
