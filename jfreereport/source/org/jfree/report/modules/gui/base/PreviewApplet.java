/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * ------------------------------
 * PreviewApplet.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: PreviewApplet.java,v 1.1 2003/07/07 22:44:05 taqua Exp $
 *
 * Changes
 * -------------------------
 * 17.06.2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.base;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.Action;
import javax.swing.JApplet;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;

/**
 * The preview applet implements the preview proxy interface for JApplets.
 * <p>
 * Due to the special lifecycle of applets, this implementation must also
 * provide methods to handle the creation of reports. Implementations of this
 * class must implement the <code>getReport()</code> method to provide their
 * fully initialized report definitions.
 * <p>
 * The <code>getReport()</code> method is called from the <code>init()</code>
 * method.
 *
 * @author Thomas Morgner
 */
public abstract class PreviewApplet extends JApplet implements PreviewProxy
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
    public void actionPerformed(final ActionEvent e)
    {
      setVisible(false);
    }
  }

  /** A preview proxy. */
  private PreviewProxyBase base;

  /** Localised resources. */
  private ResourceBundle resources;

  /**
   * DefaultConstructor.
   */
  public PreviewApplet()
  {
  }

  /**
   * Called by the browser or applet viewer to inform
   * this applet that it is being reclaimed and that it should destroy
   * any resources that it has allocated. The <code>stop</code> method
   * will always be called before <code>destroy</code>.
   * <p>
   * A subclass of <code>Applet</code> should override this method if
   * it has any operation that it wants to perform before it is
   * destroyed. For example, an applet with threads would use the
   * <code>init</code> method to create the threads and the
   * <code>destroy</code> method to kill them.
   * <p>
   * The implementation of this method provided by the
   * <code>Applet</code> class does nothing.
   *
   * @see     java.applet.Applet#init()
   * @see     java.applet.Applet#start()
   * @see     java.applet.Applet#stop()
   */
  public void destroy()
  {
    super.destroy();
    base.dispose();
  }

  /**
   * Called by the browser or applet viewer to inform
   * this applet that it has been loaded into the system. It is always
   * called before the first time that the <code>start</code> method is
   * called.
   * <p>
   * A subclass of <code>Applet</code> should override this method if
   * it has initialization to perform. For example, an applet with
   * threads would use the <code>init</code> method to create the
   * threads and the <code>destroy</code> method to kill them.
   * <p>
   * The implementation of this method provided by the
   * <code>Applet</code> class does nothing.
   *
   * @see     java.applet.Applet#destroy()
   * @see     java.applet.Applet#start()
   * @see     java.applet.Applet#stop()
   */
  public void init()
  {
    base = new PreviewProxyBase(this);
    try
    {
      base.init(getReport());
    }
    catch (ReportProcessingException pre)
    {
      pre.printStackTrace();
      this.getAppletContext().showStatus(pre.getMessage());
    }
    setContentPane(base);
  }

  /**
   * Returns the report that should be displayed in that applet.
   * Make sure, that subsequent calls to this method return the same report instance.
   * <p>
   * This method is called from PreviewApplet.init(), so make sure that all
   * initialization is done before the init() method is called.
   *
   * @return the report that should be previewed.
   */
  public abstract JFreeReport getReport();

  /**
   * Packs the preview component.
   */
  public void pack()
  {
    setSize(getPreferredSize());
    validate();
  }

  /**
   * Disposes the preview component.
   */
  public void dispose()
  {
  }

  /**
   * Creates a default close action.
   *
   * @return The close action.
   */
  public Action createDefaultCloseAction()
  {
    return new DefaultCloseAction();
  }

  /**
   * Sets the title for the preview component.
   *
   * @param title  the title.
   */
  public void setTitle(final String title)
  {
    // no need to implement it, except you want to do something
    // special ...
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

  /**
   * Returns the preview proxy.
   *
   * @return The proxy.
   */
  public PreviewProxyBase getBase()
  {
    return base;
  }
}
