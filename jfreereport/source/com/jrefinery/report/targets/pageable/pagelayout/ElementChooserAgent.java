/**
 * Date: Dec 1, 2002
 * Time: 7:02:14 PM
 *
 * $Id$
 */
package com.jrefinery.report.targets.pageable.pagelayout;

public class ElementChooserAgent extends LayoutAgent
{
  private static final String CHOOSING_DONE = ElementChooserAgent.class.getName();

  public ElementChooserAgent()
  {
  }

  public LayoutAgentProgress processTask(LayoutTask task)
  {
    EventType type = task.getEventType();
    if (type == EventType.PAGE_START)
    {
    }
    else if (type == EventType.PAGE_END)
    {
    }
    else if (type == EventType.REPORT_START)
    {
    }
    else if (type == EventType.REPORT_END)
    {
    }
    else if (type == EventType.GROUP_START)
    {
    }
    else if (type == EventType.GROUP_END)
    {
    }
    else if (type == EventType.ITEM_ADVANCE)
    {
    }
    return LayoutAgentProgress.WONT_PROCESS;
  }
}
