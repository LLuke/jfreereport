/**
 * Date: Dec 1, 2002
 * Time: 6:42:46 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.pageable.pagelayout;

public class LayoutAgentProgress
{
  public static final LayoutAgentProgress WONT_PROCESS = new LayoutAgentProgress("WONT_PROCESS");
  public static final LayoutAgentProgress PROCESSING_COMPLETE = new LayoutAgentProgress("PROCESSING_COMPLETE");
  public static final LayoutAgentProgress PROCESSING_INCOMPLETE = new LayoutAgentProgress("PROCESSING_INCOMPLETE");

  private final String myName; // for debug only

  private LayoutAgentProgress(String name)
  {
    myName = name;
  }

  public String toString()
  {
    return myName;
  }
}
