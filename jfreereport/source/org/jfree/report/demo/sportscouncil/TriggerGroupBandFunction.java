package org.jfree.report.demo.sportscouncil;

import org.jfree.report.Band;
import org.jfree.report.Element;
import org.jfree.report.Group;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.function.AbstractFunction;
import org.jfree.report.function.FunctionUtilities;

public class TriggerGroupBandFunction extends AbstractFunction
{
  /**
   * Creates an unnamed function. Make sure the name of the function is set using {@link
   * #setName} before the function is added to the report's function collection.
   */
  public TriggerGroupBandFunction ()
  {
  }

  /**
   * Receives notification that a group has started.
   *
   * @param event the event.
   */
  public void groupStarted (final ReportEvent event)
  {
    final Group g = FunctionUtilities.getCurrentGroup(event);
    if (g.getName().equals("detailGroup") == false)
    {
      return;
    }
    final String o = (String) event.getDataRow().get("recordID");
    // process the group header of the detail group
    final Band b = g.getHeader();
    for (int i = 0; i < b.getElementCount(); i++)
    {
      final Element e = b.getElement(i);
      e.setVisible(e.getName().equals(o));
    }

    // and the itemband
    final Band itemBand = event.getReport().getItemBand();
    for (int i = 0; i < itemBand.getElementCount(); i++)
    {
      final Element e = itemBand.getElement(i);
      e.setVisible(e.getName().equals(o));
    }
  }

  /**
   * Return the current expression value. <P> The value depends (obviously) on the
   * expression implementation.
   *
   * @return the value of the function.
   */
  public Object getValue ()
  {
    // is not used
    return null;
  }
}
