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
 * ReportProgressDialog.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: ReportProgressDialog.java,v 1.1 2003/08/24 15:08:18 taqua Exp $
 *
 * Changes
 * -------------------------
 * 24.08.2003 : Initial version
 *
 */

package org.jfree.report.modules.gui.base;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

import org.jfree.report.event.RepaginationListener;
import org.jfree.report.event.RepaginationState;
import org.jfree.report.modules.gui.base.resources.JFreeReportResources;

public class ReportProgressDialog extends JDialog
    implements RepaginationListener
{
  private JLabel messageCarrier;
  private JLabel passCountMessage;
  private JLabel pageCountMessage;
  private JLabel rowCountMessage;
  private JProgressBar progressBar;

  private MessageFormat pageMessageFormatter;
  private MessageFormat rowsMessageFormatter;
  private MessageFormat passMessageFormatter;

  private int lastPage;
  private int lastPass;
  private int lastMaxRow;
  private Integer lastMaxRowInteger;  // this values doesnt change much, so reduce GC work

  private String layoutText;
  private String outputText;


  /** Localised resources. */
  private final ResourceBundle resources;

  /** The base resource class. */
  public static final String BASE_RESOURCE_CLASS =
      JFreeReportResources.class.getName();

  /**
   * Creates a non-modal dialog without a title and without
   * a specified Frame owner.  A shared, hidden frame will be
   * set as the owner of the Dialog.
   */
  public ReportProgressDialog()
  {
    resources = ResourceBundle.getBundle(BASE_RESOURCE_CLASS);
    initialize();
    addWindowListener(new WindowAdapter()
    {
      /**
       * Invoked when a window has been opened.
       */
      public void windowOpened(final WindowEvent e)
      {
        ReportProgressDialog.this.toFront();
      }
    });
    setOutputText(resources.getString("progress-dialog.perform-output"));
    setLayoutText(resources.getString("progress-dialog.prepare-layout"));

    lastPass = -1;
    lastMaxRow = -1;
    lastPage = -1;
  }

  private void initialize()
  {
    final JPanel contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    contentPane.setLayout(new GridBagLayout());

    pageMessageFormatter = new MessageFormat(resources.getString("progress-dialog.page-label"));
    rowsMessageFormatter = new MessageFormat(resources.getString("progress-dialog.rows-label"));
    passMessageFormatter = new MessageFormat(resources.getString("progress-dialog.pass-label"));

    messageCarrier = new JLabel(" ");
    passCountMessage = new JLabel(" ");
    rowCountMessage = new JLabel(" ");
    pageCountMessage = new JLabel(" ");
    progressBar = new JProgressBar();

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(3, 1, 5, 1);
    gbc.ipadx = 200;
    contentPane.add(messageCarrier, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.SOUTHWEST;
    gbc.insets = new Insets(3, 1, 1, 1);
    contentPane.add(passCountMessage, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.insets = new Insets(3, 1, 1, 1);
    contentPane.add(progressBar, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 1;
    gbc.weighty = 1;
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.insets = new Insets(3, 1, 1, 1);
    contentPane.add(pageCountMessage, gbc);

    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.insets = new Insets(3, 10, 1, 1);
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    contentPane.add(rowCountMessage, gbc);

    setContentPane(contentPane);
  }

  public String getMessage()
  {
    return messageCarrier.getText();
  }

  public void setMessage(final String message)
  {
    messageCarrier.setText(message);
  }

  /**
   * Receives notification of a repagination update.
   *
   * @param state  the state.
   */
  public void repaginationUpdate(final RepaginationState state)
  {
    final boolean maxRowChanged = lastMaxRow != state.getMaxRow();
    updatePageMessage(state.getPage());
    updatePassMessage(state.getPass(), state.isPrepare());
    updateRowsMessage(state.getCurrentRow(), state.getMaxRow());
    if (maxRowChanged)
    {
      progressBar.setMaximum(state.getMaxRow());
    }
    progressBar.setValue(state.getCurrentRow());
  }

  protected void updatePageMessage(final int page)
  {
    if (lastPage != page)
    {
      final Object[] parameters = new Object[]{new Integer(page)};
      pageCountMessage.setText(pageMessageFormatter.format(parameters));
      lastPage = page;
    }
  }

  protected void updateRowsMessage(final int rows, final int maxRows)
  {
    if (maxRows != lastMaxRow)
    {
      lastMaxRowInteger = new Integer(maxRows);
      lastMaxRow = maxRows;
    }
    final Object[] parameters = new Object[]{
      new Integer(rows),
      lastMaxRowInteger
    };
    rowCountMessage.setText(rowsMessageFormatter.format(parameters));
  }

  protected void updatePassMessage(final int pass, final boolean prepare)
  {
    if (lastPass != pass)
    {
      lastPass = pass;
      if (pass >= 0)
      {
        final Object[] parameters = new Object[]{new Integer(pass)};
        passCountMessage.setText(passMessageFormatter.format(parameters));
      }
      else
      {
        String message;
        if (prepare)
        {
          message = getLayoutText();
        }
        else
        {
          message = getOutputText();
        }
        passCountMessage.setText(message);
        lastPass = pass;

      }
    }
  }

  protected final JLabel getPassCountMessage()
  {
    return passCountMessage;
  }

  protected final JLabel getPageCountMessage()
  {
    return pageCountMessage;
  }

  protected final JLabel getRowCountMessage()
  {
    return rowCountMessage;
  }

  protected final MessageFormat getPageMessageFormatter()
  {
    return pageMessageFormatter;
  }

  protected final MessageFormat getRowsMessageFormatter()
  {
    return rowsMessageFormatter;
  }

  protected final MessageFormat getPassMessageFormatter()
  {
    return passMessageFormatter;
  }

  public String getOutputText()
  {
    return outputText;
  }

  public void setOutputText(final String outputText)
  {
    this.outputText = outputText;
  }

  public String getLayoutText()
  {
    return layoutText;
  }

  public void setLayoutText(final String layoutText)
  {
    this.layoutText = layoutText;
  }

//  public static void main(String[] args)
//  {
//    RepaginationState state = new RepaginationState("", 0, 0, 0, 0, false);
//
//    ReportProgressDialog dialog = new ReportProgressDialog();
//    dialog.setModal(true);
//    dialog.setSize(300, 100);
//    dialog.repaginationUpdate(state);
//    dialog.setTitle("Printing...");
//    dialog.setMessage("Printing report ... please wait.");
//    dialog.pack();
//    dialog.setVisible(true);
//    System.exit(0);
//  }
}
