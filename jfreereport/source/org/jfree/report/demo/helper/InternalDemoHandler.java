package org.jfree.report.demo.helper;

import java.net.URL;
import javax.swing.JComponent;

import org.jfree.report.JFreeReport;

/**
 * A demo handler allows the generic use of demos in the framework.
 * <p>
 * Every demo has a name, a way to create a report and a description in
 * HTML documenting the demo. A demo also provides a presentation component
 * to either show the data or control the demo's appearance.
 *
 * @author Thomas Morgner
 */
public interface InternalDemoHandler extends DemoHandler
{
  /**
   * Returns the display name of the demo.
   *
   * @return the name.
   */
  public String getDemoName();

  /**
   * Assigns a demo controler to this demo. It is guaranteed, that a controler
   * is set, before the presentation component is queried or a report is
   * created.
   *
   * @param controler the controler.
   */
  public void setControler (final DemoControler controler);

  /**
   * Returns the demo controler for this demo. The demo controler is supplied
   * by the user of the demo handler.
   *
   * @return the demo controler for this demo handler.
   */
  public DemoControler getControler();

  /**
   * Creates the report. For XML reports, this will most likely call the
   * ReportGenerator, while API reports may use this function to build and return
   * a new, fully initialized report object.
   *
   * @return the fully initialized JFreeReport object.
   * @throws ReportDefinitionException if an error occured preventing the
   * report definition.
   */
  public JFreeReport createReport() throws ReportDefinitionException;

  /**
   * Returns the URL of the HTML document describing this demo.
   *
   * @return the demo description.
   */
  public URL getDemoDescriptionSource();

  /**
   * Returns the presentation component for this demo. This component is
   * shown before the real report generation is started. Ususally it contains
   * a JTable with the demo data and/or input components, which allow to configure
   * the report.
   *
   * @return the presentation component, never null.
   */
  public JComponent getPresentationComponent();
}
