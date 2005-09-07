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
 * @author: Thomas Morgner
 */
public interface InternalDemoHandler extends DemoHandler
{
  public String getDemoName();
  public void setControler (final DemoControler controler);
  public DemoControler getControler();
  
  public JFreeReport createReport() throws ReportDefinitionException;
  public URL getDemoDescriptionSource();
  public JComponent getPresentationComponent();
}
