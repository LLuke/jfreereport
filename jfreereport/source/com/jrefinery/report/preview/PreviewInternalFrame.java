/**
 * Date: Jan 14, 2003
 * Time: 7:24:12 PM
 *
 * $Id$
 */
package com.jrefinery.report.preview;

import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.ReportProcessingException;
import com.jrefinery.report.action.CloseAction;

import javax.swing.Action;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

public class PreviewInternalFrame extends JInternalFrame implements PreviewProxy
{
  /**
   * Default 'close' action for the frame.
   */
  private class DefaultCloseAction extends CloseAction
  {
    /**
     * Creates a 'close' action.
     */
    public DefaultCloseAction()
    {
      super(getResources());
    }

    /**
     * Closes the preview frame if the default close operation is set to dispose
     * so this frame is reusable.
     *
     * @param e The action event.
     */
    public void actionPerformed(ActionEvent e)
    {
      if (getDefaultCloseOperation() == DISPOSE_ON_CLOSE)
      {
        dispose();
      }
      else
      {
        setVisible(false);
      }
    }
  }

  private PreviewProxyBase base;
  private ResourceBundle resources;

  /**
   * Constructs a PreviewFrame that displays the specified report.
   *
   * @param report  the report to be displayed.
   *
   * @throws com.jrefinery.report.ReportProcessingException if there is a problem processing the report.
   */
  public PreviewInternalFrame(JFreeReport report) throws ReportProcessingException
  {
    init(report);
  }

  private void init(JFreeReport report) throws ReportProcessingException
  {
    base = new PreviewProxyBase(report, this);
    registerCloseActions();
    setContentPane(base);
  }

  /**
   * Retrieves the resources for this PreviewFrame. If the resources are not initialized,
   * they get loaded on the first call to this method.
   *
   * @return this frames ResourceBundle.
   */
  public ResourceBundle getResources()
  {
    if (resources == null)
    {
      resources = ResourceBundle.getBundle(PreviewProxyBase.BASE_RESOURCE_CLASS);
    }
    return resources;
  }


  public Action createDefaultCloseAction()
  {
    return new DefaultCloseAction();
  }

  public void dispose()
  {
    base.dispose();
    super.dispose();
  }

  protected void registerCloseActions()
  {
    addInternalFrameListener(new InternalFrameAdapter()
    {
      /**
       * Invoked when an internal frame is in the process of being closed.
       * The close operation can be overridden at this point.
       */
      public void internalFrameClosing(InternalFrameEvent e)
      {
        base.getCloseAction().actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
                                                         "CloseFrame"));
      }
    }
    );
  }

  public PreviewProxyBase getBase()
  {
    return base;
  }

}
