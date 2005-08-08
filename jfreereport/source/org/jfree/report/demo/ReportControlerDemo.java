/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * ReportControlerDemo.java
 * ------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: ReportControlerDemo.java,v 1.1 2005/03/25 16:39:24 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */

package org.jfree.report.demo;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.jfree.report.ElementAlignment;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.elementfactory.TextFieldElementFactory;
import org.jfree.report.modules.gui.base.DefaultReportControler;
import org.jfree.report.modules.gui.base.PreviewDialog;
import org.jfree.report.modules.gui.base.PreviewProxyBase;
import org.jfree.util.Log;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.ui.FloatDimension;
import org.jfree.ui.action.ActionButton;

/**
 * A very simple JFreeReport demo.  The purpose of this demo is to illustrate the basic
 * steps required to connect a report definition with some data and display a report
 * preview on-screen.
 * <p/>
 * In this example, the report definition is created in code.  It is also possible to read
 * a report definition from an XML file...that is demonstrated elsewhere.
 *
 * @author David Gilbert
 */
public class ReportControlerDemo
{
  private class DemoReportControler extends DefaultReportControler
  {
    private class UpdateAction extends AbstractAction
    {
      /**
       * Defines an <code>Action</code> object with a default description string and default
       * icon.
       */
      public UpdateAction ()
      {
        putValue(Action.NAME, "Update");
      }

      /**
       * Invoked when an action occurs.
       */
      public void actionPerformed (final ActionEvent e)
      {
        final PreviewProxyBase base = getPreviewBase();
        if (base == null)
        {
          return;
        }
        final JFreeReport report = base.getReport();
        report.setProperty(MESSAGE_ONE_FIELDNAME, messageOneField.getText());
        report.setProperty(MESSAGE_TWO_FIELDNAME, messageTwoField.getText());
        try
        {
          base.refresh();
        }
        catch(Exception ex)
        {
          Log.error ("Unable to refresh the report.", ex);
        }
      }
    }

    private JTextField messageOneField;
    private JTextField messageTwoField;
    private Action updateAction;

    public DemoReportControler ()
    {
      setLayout(new GridBagLayout());

      final JLabel messageOneLabel = new JLabel ("One:");
      final JLabel messageTwoLabel = new JLabel ("Two:");
      messageOneField = new JTextField();
      messageTwoField = new JTextField();
      updateAction = new UpdateAction();

      GridBagConstraints gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 0;
      add (messageOneLabel, gbc);

      gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 1;
      add (messageTwoLabel, gbc);

      gbc = new GridBagConstraints();
      gbc.gridx = 1;
      gbc.gridy = 0;
      gbc.weightx = 1;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      add (messageOneField, gbc);

      gbc = new GridBagConstraints();
      gbc.gridx = 1;
      gbc.gridy = 1;
      gbc.weightx = 1;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      add (messageTwoField, gbc);

      gbc = new GridBagConstraints();
      gbc.gridx = 1;
      gbc.gridy = 2;
      gbc.anchor = GridBagConstraints.EAST;
      add(new ActionButton (updateAction));

      setEnabled(false);
      messageOneField.setEnabled(false);
      messageTwoField.setEnabled(false);
      updateAction.setEnabled(false);
    }

    /**
     * Sets whether or not this component is enabled. A component that is enabled may
     * respond to user input, while a component that is not enabled cannot respond to user
     * input.  Some components may alter their visual representation when they are disabled
     * in order to provide feedback to the user that they cannot take input. <p>Note:
     * Disabling a component does not disable it's children.
     * <p/>
     * <p>Note: Disabling a lightweight component does not prevent it from receiving
     * MouseEvents.
     *
     * @param enabled true if this component should be enabled, false otherwise
     * @see java.awt.Component#isEnabled
     * @see java.awt.Component#isLightweight
     */
    public void setEnabled (final boolean enabled)
    {
      super.setEnabled(enabled);
      messageOneField.setEnabled(enabled);
      messageTwoField.setEnabled(enabled);
      updateAction.setEnabled(enabled);
    }

  }

  /**
   * Window close handler.
   */
  protected static class CloseHandler extends WindowAdapter
  {
    public CloseHandler ()
    {
    }

    /**
     * Handles the window closing event.
     *
     * @param event the window event.
     */
    public void windowClosing (final WindowEvent event)
    {
      if (ReportConfiguration.getGlobalConfig().getConfigProperty
              ("org.jfree.report.demo.Embedded", "false").equals("false"))
      {
        System.exit(0);
      }
      else
      {
        event.getWindow().setVisible(false);
      }
    }
  }

  protected static final String MESSAGE_ONE_FIELDNAME = "MessageOne";
  protected static final String MESSAGE_TWO_FIELDNAME = "MessageTwo";

  /**
   * Creates and displays a simple report.
   */
  public ReportControlerDemo ()
  {
    final JFreeReport report = createReportDefinition();
    try
    {
      final PreviewDialog preview = new PreviewDialog(report);
      preview.addWindowListener(new CloseHandler());
      preview.getBase().setReportControler(new DemoReportControler());
      preview.pack();
      preview.setVisible(true);
    }
    catch (ReportProcessingException e)
    {
      Log.error("Failed to generate report ", e);
    }

  }

  /**
   * Creates a report definition.
   *
   * @return a report definition.
   */
  private JFreeReport createReportDefinition ()
  {

    final JFreeReport report = new JFreeReport();
    report.setName(getDescription());

    TextFieldElementFactory factory = new TextFieldElementFactory();
    factory.setName("T1");
    factory.setAbsolutePosition(new Point2D.Float(0, 0));
    factory.setMinimumSize(new FloatDimension(150, 12));
    factory.setColor(Color.black);
    factory.setHorizontalAlignment(ElementAlignment.RIGHT);
    factory.setVerticalAlignment(ElementAlignment.MIDDLE);
    factory.setNullString("-");
    factory.setFieldname(MESSAGE_ONE_FIELDNAME);
    factory.setDynamicHeight(Boolean.TRUE);
    report.getReportHeader().addElement(factory.createElement());

    factory = new TextFieldElementFactory();
    factory.setName("T2");
    factory.setAbsolutePosition(new Point2D.Float(200, 0));
    factory.setMinimumSize(new FloatDimension(150, 12));
    factory.setColor(Color.black);
    factory.setHorizontalAlignment(ElementAlignment.LEFT);
    factory.setVerticalAlignment(ElementAlignment.MIDDLE);
    factory.setNullString("-");
    factory.setDynamicHeight(Boolean.TRUE);
    factory.setFieldname(MESSAGE_TWO_FIELDNAME);
    report.getReportHeader().addElement(factory.createElement());

    report.setPropertyMarked(MESSAGE_ONE_FIELDNAME, true);
    report.setPropertyMarked(MESSAGE_TWO_FIELDNAME, true);
    report.setProperty(MESSAGE_ONE_FIELDNAME, "Hello");
    report.setProperty(MESSAGE_TWO_FIELDNAME, "World");
    return report;

  }

  /**
   * Returns a short description of the demo.
   *
   * @return
   */
  public String getDescription ()
  {
    return "A Very Simple Report";
  }

  /**
   * The starting point for the "Hello World" demo application.
   *
   * @param args ignored.
   */
  public static void main (final String[] args)
  {
    // initialize JFreeReport
    JFreeReportBoot.getInstance().start();

    //final HelloWorld app =
    new ReportControlerDemo();
  }

}