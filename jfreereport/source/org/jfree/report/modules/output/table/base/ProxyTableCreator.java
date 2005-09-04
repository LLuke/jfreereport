package org.jfree.report.modules.output.table.base;

import org.jfree.report.ReportDefinition;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.modules.output.meta.MetaBand;

/**
 * Creation-Date: 04.09.2005, 19:52:02
 *
 * @author: Thomas Morgner
 */
public class ProxyTableCreator implements LayoutCreator
{
  private TableCreator parent;
  private boolean open;
  private boolean ignoreOpenState;

  public ProxyTableCreator(final TableCreator parent)
  {
    this.parent = parent;
    this.parent.setIgnoreOpenState(true);
  }

  public TableCreator getParent()
  {
    return parent;
  }

  public boolean isIgnoreOpenState()
  {
    return ignoreOpenState;
  }

  public void setIgnoreOpenState(final boolean ignoreOpenState)
  {
    this.ignoreOpenState = ignoreOpenState;
  }

  public void open(ReportDefinition report) throws ReportProcessingException
  {
    // no, we do not do open anything.
    open = true;
  }

  public void beginTable(ReportDefinition report)
          throws ReportProcessingException
  {
    parent.beginTable(report);
  }

  public void processBand(MetaBand band)
  {
    parent.processBand(band);
  }

  public void endTable() throws ReportProcessingException
  {
    parent.endTable();
  }

  public void close() throws ReportProcessingException
  {
    open = false;
  }

  public boolean isOpen()
  {
    return open;
  }

  public boolean isEmpty()
  {
    return parent.isEmpty();
  }

  public boolean flush() throws ReportProcessingException
  {
    return parent.flush();
  }

  public SheetLayoutCollection getSheetLayoutCollection()
  {
    if (parent instanceof LayoutCreator)
    {
      LayoutCreator layoutParent = (LayoutCreator) parent;
      return layoutParent.getSheetLayoutCollection();
    }
    return null;
  }
}
