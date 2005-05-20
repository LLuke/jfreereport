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
 * -------------------
 * SwingIconsDemo.java
 * -------------------
 * (C)opyright 2002, 2003, by Simba Management Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Simba Management Limited);
 * Contributor(s):   Thomas Morgner;
 *
 * $Id: SwingIconsDemo.java,v 1.9 2005/05/18 18:38:27 taqua Exp $
 *
 * Changes
 * -------
 * 15-Jul-2002 : Version 1 (DG);
 * 20-Nov-2002 : Corrected possible read error if the icon is not read completely from the zip file;
 * 27-Feb-2003 : Renamed First.java --> SwingIconsDemo.java (DG);
 *
 */

package org.jfree.report.demo;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Properties;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.jfree.io.FileUtilities;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.util.ImageUtils;
import org.jfree.report.demo.helper.AbstractDemoFrame;
import org.jfree.report.demo.helper.ImageCellRenderer;
import org.jfree.report.modules.gui.base.PreviewFrame;
import org.jfree.report.modules.misc.configstore.base.ConfigFactory;
import org.jfree.report.modules.misc.configstore.base.ConfigStorage;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.action.AbstractFileSelectionAction;
import org.jfree.ui.action.ActionMenuItem;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;
import org.jfree.xml.ElementDefinitionException;

/**
 * A demonstration application. <P> This demo is written up in the JFreeReport PDF
 * Documentation.  Please notify David Gilbert (david.gilbert@object-refinery.com) if you
 * need to make changes to this file. <P> To run this demo, you need to have the Java Look
 * and Feel Icons jar file on your classpath.
 *
 * @author David Gilbert
 */
public class SwingIconsDemo extends AbstractDemoFrame
{
  private class SelectRepositoryFileAction extends AbstractFileSelectionAction
  {
    private File selectedFile;

    public SelectRepositoryFileAction ()
    {
      super(SwingIconsDemo.this);
      putValue(Action.NAME, "Select graphics archive ..");
      this.putValue(Action.SMALL_ICON, ImageUtils.createTransparentIcon(16, 16));
      this.putValue("ICON24", ImageUtils.createTransparentIcon(24, 24));
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed (final ActionEvent e)
    {
      selectedFile = performSelectFile(selectedFile, JFileChooser.OPEN_DIALOG, true);
      if (selectedFile != null)
      {
        if (selectedFile.exists() && selectedFile.canRead() && selectedFile.isFile())
        {
          try
          {
            loadData(selectedFile.toURL());
          }
          catch (MalformedURLException ex)
          {
            Log.warn ("Unable to form local file URL. Is there no local filesystem?");
          }
        }
      }
    }

    /**
     * Returns a descriptive text describing the file extension.
     *
     * @return the file description.
     */
    protected String getFileDescription ()
    {
      return "Java Look and Feel Graphics Repository";
    }

    /**
     * Returns the file extension that should be used for the operation.
     *
     * @return the file extension.
     */
    protected String getFileExtension ()
    {
      return ".jar";
    }
  }

  /**
   * The data for the report.
   */
  private SwingIconsDemoTableModel data;

  /**
   * Constructs the demo application.
   *
   * @param title the frame title.
   */
  public SwingIconsDemo (final String title)
  {
    data = new SwingIconsDemoTableModel();

    setTitle(title);
    setJMenuBar(createMenuBar());
    setContentPane(createContent());

    loadData(findDataFile());
  }

  protected void loadData(final URL sourceURL)
  {
    if (sourceURL != null)
    {
      // on success update the config path, else clear the path.
      if (this.data.readData(sourceURL))
      {
        storeToConfiguration(sourceURL);
        return;
      }
      else
      {
        final String title = "Unable to load the icons.";
        final String message =
                ("There was a problem while loading 'jlfgr-1_0.jar'.\n"
                + "A URL was given, but the contents seems to be invalid.\n\n"
                + "You may download this jar-file from: \n"
                + "http://java.sun.com/developer/techDocs/hi/repository/");
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
      }
    }
    this.data.clear();
    storeToConfiguration(null);
  }

  /**
   * Loads the URL of the Graphics Repository from the local configuration.
   *
   * @return the loaded URL or null, if the configuration did not hold an entry.
   */
  protected URL loadFromConfiguration()
  {
    final String configPath = ConfigFactory.encodePath("SwingIconsDemo-TableModel");
    final ConfigStorage cs = ConfigFactory.getInstance().getUserStorage();
    if (cs.existsProperties(configPath) == false)
    {
      return null;
    }
    try
    {
      final Properties p = cs.loadProperties(configPath, null);
      final String property = p.getProperty("repository-path");
      if (property == null)
      {
        return null;
      }
      return new URL (property);
    }
    catch (Exception e)
    {
      return null;
    }
  }

  protected void storeToConfiguration(final URL url)
  {
    final String configPath = ConfigFactory.encodePath("SwingIconsDemo-TableModel");
    final ConfigStorage cs = ConfigFactory.getInstance().getUserStorage();
    try
    {
      final Properties p = new Properties();
      if (url != null)
      {
        p.setProperty("repository-path", url.toExternalForm());
      }
      cs.storeProperties(configPath, p);
    }
    catch (Exception e)
    {
      // ignored ..
      Log.debug ("Unable to store the configuration.", e);
    }
  }

  /**
   * Creates a menu bar.
   *
   * @return the menu bar.
   */
  public JMenuBar createMenuBar ()
  {
    final JMenuBar mb = new JMenuBar();
    final JMenu fileMenu = createJMenu("menu.file");

    final JMenuItem previewItem = new ActionMenuItem(getPreviewAction());
    final JMenuItem exitItem = new ActionMenuItem(getCloseAction());

    fileMenu.add(previewItem);
    fileMenu.add(new ActionMenuItem(new SelectRepositoryFileAction()));
    fileMenu.addSeparator();
    fileMenu.add(exitItem);
    mb.add(fileMenu);
    return mb;
  }

  /**
   * Creates the content for the application frame.
   *
   * @return a panel containing the basic user interface.
   */
  public JPanel createContent ()
  {
    final JPanel content = new JPanel(new BorderLayout());
    content.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    final JTable table = new JTable(data);
    table.setDefaultRenderer(Image.class, new ImageCellRenderer());
    table.setRowHeight(26);
    final JScrollPane scrollPane = new JScrollPane(table);
    content.add(scrollPane);
    return content;
  }

  /**
   * Searches for the 'jlfgr_1_0.jar' file on the classpath, in the
   * classpath directories and the working directory. If that fails,
   * the user is asked to choose the correct file.
   *
   * @return the URL to the graphics repository.
   */
  private URL findDataFile ()
  {
    final URL url = ObjectUtilities.getResource("jlfgr-1_0.jar", SwingIconsDemo.class);
    if (url != null)
    {
      return url;
    }
    final URL urlFromConfig = loadFromConfiguration();
    if (urlFromConfig != null)
    {
      return urlFromConfig;
    }

    final File localFile = new File ("jlfgr-1_0.jar");
    if (localFile.exists() && localFile.canRead() && localFile.isFile())
    {
      try
      {
        return localFile.toURL();
      }
      catch (MalformedURLException e)
      {
        Log.warn ("Unable to form local file URL. Is there no local filesystem?");
      }
    }

    final File classpathFile = FileUtilities.findFileOnClassPath("jlfgr-1_0.jar");
    if (classpathFile != null)
    {
      if (classpathFile.exists() && classpathFile.canRead() && classpathFile.isFile())
      {
        try
        {
          return classpathFile.toURL();
        }
        catch (MalformedURLException e)
        {
          Log.warn ("Unable to form local file URL. Is there no local filesystem?");
        }
      }
    }

    final String title = "Unable to load the icons.";
    final String message = ("Unable to find 'jlfgr-1_0.jar'\n"
            + "Please make sure you have the Java Look and Feel Graphics Repository in "
            + "in your classpath, the same directory as the JFreeReport-jar files or in "
            + "the current working directory.\n\n"
            + "You may download this jar-file from: \n"
            + "http://java.sun.com/developer/techDocs/hi/repository/");
    JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    return null;
  }

  /**
   * Displays a print preview screen for the sample report.
   */
  protected void attemptPreview ()
  {
    final URL in = ObjectUtilities.getResource
            ("org/jfree/report/demo/swing-icons.xml", SwingIconsDemo.class);
    if (in == null)
    {
      JOptionPane.showMessageDialog(this,
              MessageFormat.format(getResources().getString("report.definitionnotfound"),
                      new Object[]{in}),
              getResources().getString("error"), JOptionPane.ERROR_MESSAGE);
    }

    final JFreeReport report;
    try
    {
      report = parseReport(in);
      report.setData(this.data);
    }
    catch (Exception ex)
    {
      showExceptionDialog("report.definitionfailure", ex);
      return;
    }

    try
    {
      final PreviewFrame frame = new PreviewFrame(report);
      frame.getBase().setLargeIconsEnabled(true);
      frame.getBase().setToolbarFloatable(false);
      frame.pack();
      RefineryUtilities.positionFrameRandomly(frame);
      frame.setVisible(true);
      frame.requestFocus();
    }
    catch (ReportProcessingException rpe)
    {
      showExceptionDialog("report.previewfailure", rpe);
    }

  }

  /**
   * Reads the report from the swing-icons.xml report template.
   *
   * @param templateURL The template location.
   * @return A report.
   *
   * @throws ElementDefinitionException if the report generator encountered an error.
   * @throws IOException                if there was an IO error while reading from the
   *                                    URL.
   */
  private JFreeReport parseReport (final URL templateURL)
          throws IOException, ElementDefinitionException
  {

    final ReportGenerator generator = ReportGenerator.getInstance();
    return generator.parseReport(templateURL);

  }

  /**
   * Entry point for running the demo application...
   *
   * @param args ignored.
   */
  public static void main (final String[] args)
  {
    // initialize JFreeReport
    JFreeReportBoot.getInstance().start();

    final SwingIconsDemo frame = new SwingIconsDemo("Swing Icons Report");
    frame.pack();
    RefineryUtilities.centerFrameOnScreen(frame);
    frame.setVisible(true);
  }

}