package org.jfree.report.function;

import org.jfree.report.Anchor;
import org.jfree.report.Group;
import org.jfree.report.event.ReportEvent;

public class CreateGroupAnchorsFunction extends AbstractFunction
{
  private String group;
  private String anchorPrefix;
  private Anchor anchor;
  private int count;

  /**
   * Creates an unnamed function. Make sure the name of the function is set using {@link
   * #setName} before the function is added to the report's function collection.
   */
  public CreateGroupAnchorsFunction ()
  {
  }

  public String getAnchorPrefix ()
  {
    return anchorPrefix;
  }

  public void setAnchorPrefix (final String anchorPrefix)
  {
    this.anchorPrefix = anchorPrefix;
  }

  public String getGroup ()
  {
    return group;
  }

  public void setGroup (final String group)
  {
    this.group = group;
  }

  /**
   * Receives notification that report generation initializes the current run. <P> The
   * event carries a ReportState.Started state.  Use this to initialize the report.
   *
   * @param event The event.
   */
  public void reportInitialized (final ReportEvent event)
  {
    anchor = null;
    count = 0;
  }

  /**
   * Receives notification that a group has started.
   *
   * @param event the event.
   */
  public void groupStarted (final ReportEvent event)
  {
    if (FunctionUtilities.isDefinedGroup(getGroup(), event) == false)
    {
      return;
    }

    final StringBuffer targetBuffer = new StringBuffer();
    targetBuffer.append(getAnchorPrefix());
    final Group g = FunctionUtilities.getCurrentGroup(event);
    targetBuffer.append(g.getName());
    targetBuffer.append("%3D");
    targetBuffer.append(count);
    anchor = new Anchor(targetBuffer.toString());
  }

  /**
   * Return the current expression value. <P> The value depends (obviously) on the
   * expression implementation.
   *
   * @return the value of the function.
   */
  public Object getValue ()
  {
    return anchor;
  }
}
