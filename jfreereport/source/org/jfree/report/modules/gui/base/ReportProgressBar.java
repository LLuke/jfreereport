/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2003, by Object Refinery Limited and Contributors.
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
 * ReportProgressBar.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: ReportProgressBar.java,v 1.3 2004/03/27 17:23:19 taqua Exp $
 *
 * Changes
 * -------------------------
 * 31.01.2004 : Initial version
 *
 */

package org.jfree.report.modules.gui.base;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import org.jfree.report.event.RepaginationListener;
import org.jfree.report.event.RepaginationState;

public class ReportProgressBar extends JProgressBar implements RepaginationListener
{
  private class ScreenUpdateRunnable implements Runnable
  {
    private int pass;
    private int page;
    private boolean prepare;
    private int maxRow;
    private int currentRow;

    public ScreenUpdateRunnable(final int currentRow, final int maxRow,
                                final int page, final int pass, final boolean prepare)
    {
      this.currentRow = currentRow;
      this.maxRow = maxRow;
      this.page = page;
      this.pass = pass;
      this.prepare = prepare;
    }

    public void run()
    {
      updatePassMessage(page, pass, prepare);
      final boolean maxRowChanged = lastMaxRow != maxRow;
      if (maxRowChanged)
      {
        setMaximum(maxRow);
      }
      setValue(currentRow);
    }
  }

  /** The last page received. */
  private int lastPage;
  /** The last pass values received. */
  private int lastPass;
  /** The last max-row received. */
  private int lastMaxRow;

  /** The reuseable message format for the page label. */
  private MessageFormat pageMessageFormatter;
  /** The reuseable message format for the pass label. */
  private MessageFormat passMessageFormatter;
  /** a text which describes the layouting process. */
  private String layoutText;
  /** a text that describes the export phase of the report processing. */
  private String outputText;


  /** Localised resources. */
  private final ResourceBundle resources;

  /** The base resource class. */
  public static final String BASE_RESOURCE_CLASS =
          "org.jfree.report.modules.gui.base.resources.jfreereport-resources";

  /**
   * Creates a horizontal progress bar
   * that displays a border but no progress string.
   * The initial and minimum values are 0,
   * and the maximum is 100.
   *
   * @see #setOrientation
   * @see #setBorderPainted
   * @see #setStringPainted
   * @see #setString
   * @see #setIndeterminate
   */
  public ReportProgressBar()
  {
    resources = ResourceBundle.getBundle(BASE_RESOURCE_CLASS);
    pageMessageFormatter = new MessageFormat(resources.getString("progress-dialog.page-label"));
    passMessageFormatter = new MessageFormat(resources.getString("progress-dialog.pass-label"));
    setOutputText(resources.getString("progress-dialog.perform-output"));
    setLayoutText(resources.getString("progress-dialog.prepare-layout"));

    lastPass = -1;
    lastMaxRow = -1;
    lastPage = -1;
  }

  /**
   * Receives notification of a repagination update.
   *
   * @param state  the state.
   */
  public void repaginationUpdate(final RepaginationState state)
  {
    ScreenUpdateRunnable runnable = new ScreenUpdateRunnable
        (state.getCurrentRow(), state.getMaxRow(), state.getPage(),
            state.getPass(), state.isPrepare());
    if (SwingUtilities.isEventDispatchThread())
    {
      runnable.run();
    }
    else
    {
      SwingUtilities.invokeLater(runnable);
    }
  }

  private void updatePassMessage(final int page, final int pass, final boolean prepare)
  {

    if (lastPage != page || lastPass != pass)
    {
      StringBuffer message = new StringBuffer();
      final Object[] pageparameters = new Object[]{new Integer(page)};
      message.append(pageMessageFormatter.format(pageparameters));

      lastPage = page;
      lastPass = pass;

      if (pass >= 0)
      {
        final Object[] passparameters = new Object[]{new Integer(pass)};
        message.append(passMessageFormatter.format(passparameters));
      }
      else
      {
        if (prepare)
        {
          message.append(getLayoutText());
        }
        else
        {
          message.append(getOutputText());
        }
      }

      setString(message.toString());
    }

  }

  /**
   * Returns the output text message. This text describes the export phases of
   * the report processing.
   *
   * @return the output phase description.
   */
  public String getOutputText()
  {
    return outputText;
  }

  /**
   * Defines the output text message. This text describes the export phases of
   * the report processing.
   *
   * @param outputText the output message.
   */
  public void setOutputText(final String outputText)
  {
    if (outputText == null)
    {
      throw new NullPointerException("OutputText must not be null.");
    }
    this.outputText = outputText;
  }

  /**
   * Returns the layout text. This text describes the prepare phases of
   * the report processing.
   *
   * @return the layout text.
   */
  public String getLayoutText()
  {
    return layoutText;
  }

  /**
   * Defines the layout text message. This text describes the prepare phases of
   * the report processing.
   *
   * @param layoutText the layout message.
   */
  public void setLayoutText(final String layoutText)
  {
    if (layoutText == null)
    {
      throw new NullPointerException("LayoutText must not be null.");
    }
    this.layoutText = layoutText;
  }

}
