package org.jfree.report.demo.helper;

import java.awt.BorderLayout;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.modules.gui.base.PreviewFrame;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.report.util.Log;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.action.ActionMenuItem;
import org.jfree.ui.action.ActionButton;
import org.jfree.xml.ElementDefinitionException;

public abstract class SimpleDemoFrame extends AbstractDemoFrame
{
  public SimpleDemoFrame ()
  {
  }

  protected void init()
  {
    setTitle(getResources().getString(getResourcePrefix() + ".Title"));
    setJMenuBar(createMenuBar());
    setContentPane(createContent());
  }

  protected abstract String getResourcePrefix();
  protected abstract TableModel getData();

  /**
   * Creates a menu bar.
   *
   * @return the menu bar.
   */
  protected JMenuBar createMenuBar()
  {
    final JMenuBar mb = new JMenuBar();
    final JMenu fileMenu = createJMenu("menu.file");

    final JMenuItem previewItem = new ActionMenuItem(getPreviewAction());
    final JMenuItem exitItem = new ActionMenuItem(getCloseAction());

    fileMenu.add(previewItem);
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
  public JPanel createContent()
  {
    final String d = getResources().getString(getResourcePrefix() + ".Description");
    final JTextArea textArea = new JTextArea(d);
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    textArea.setEditable(false);

    final JScrollPane scroll = new JScrollPane(textArea);
    scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    final JScrollPane scrollPane = new JScrollPane(new JTable(getData()));
    final JPanel innerContent = new JPanel();
    innerContent.setLayout(new BorderLayout());
    innerContent.add(scroll, BorderLayout.NORTH);
    innerContent.add(scrollPane, BorderLayout.CENTER);

    final JPanel content = new JPanel(new BorderLayout());
    content.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    content.add(innerContent);
    content.add(new ActionButton(getPreviewAction()), BorderLayout.SOUTH);
    return content;
  }

  /**
   * Handler method called by the preview action. This method should perform all
   * operations to preview the report.
   */
  protected void attemptPreview ()
  {
    Log.debug ("Attempt Preview");
    try
    {
      final JFreeReport report = createReport();
      report.setData(getData());
      Log.debug ("Got the report");
      final PreviewFrame frame = new PreviewFrame(report);
      frame.getBase().setToolbarFloatable(true);
      frame.pack();
      RefineryUtilities.positionFrameRandomly(frame);
      frame.setVisible(true);
      frame.requestFocus();
    }
    catch (ElementDefinitionException e)
    {
      Log.error ("Unable to parse the report; report definition contained errors.",e);
      showExceptionDialog("report.definitionfailure", e);
    }
    catch (FileNotFoundException e)
    {
      Log.error ("The report definition file was not found.");
      JOptionPane.showMessageDialog(this,
          MessageFormat.format(getResources().getString("report.definitionnotfound"),
              new Object[]{e.getMessage()}),
          getResources().getString("error"), JOptionPane.ERROR_MESSAGE);
    }
    catch (IOException e)
    {
      Log.error ("Unable to parse the report; IO failure while reading the report.", e);
      showExceptionDialog("report.definitionfailure", e);
    }
    catch (ReportProcessingException rpe)
    {
      Log.error ("Unable to procress the report");
      showExceptionDialog("report.previewfailure", rpe);
    }
  }

  protected abstract JFreeReport createReport()
          throws ElementDefinitionException, IOException;


  /**
   * Reads the report from the specified template file.
   *
   * @param resourceName the template location.
   *
   * @return a report.
   * @throws IOException if an error occured while readin the stream
   * @throws ElementDefinitionException if the XML contained syntax
   * errors.
   */
  protected JFreeReport loadReport (final String resourceName)
          throws IOException, ElementDefinitionException
  {
    final URL in = getClass().getResource(resourceName);
    if (in == null)
    {
      throw new FileNotFoundException(resourceName);
    }

    final ReportGenerator generator = ReportGenerator.getInstance();
    final JFreeReport report = generator.parseReport(in);
    return report;
  }

}
