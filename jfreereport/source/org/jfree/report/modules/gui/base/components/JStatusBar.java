package org.jfree.report.modules.gui.base.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.jfree.report.modules.gui.base.PreviewBaseModule;
import org.jfree.util.ResourceBundleSupport;

public class JStatusBar extends JComponent
{
  public static final int TYPE_ERROR = 3;
  public static final int TYPE_WARNING = 2;
  public static final int TYPE_INFORMATION = 1;
  public static final int TYPE_NONE = 0;

  private JLabel statusHolder;
  private JComponent otherComponents;
  private ResourceBundleSupport resources;

  public JStatusBar ()
  {
    setLayout(new BorderLayout());
    setBorder(BorderFactory.createMatteBorder
            (1, 0, 0, 0, UIManager.getDefaults().getColor("controlShadow")));
    statusHolder = new JLabel(" ");
    statusHolder.setMinimumSize(new Dimension(0, 20));
    add(statusHolder, BorderLayout.CENTER);

    otherComponents = new JPanel();
    add(otherComponents, BorderLayout.EAST);
  }

  public JComponent getOtherComponents ()
  {
    return otherComponents;
  }

  public void setStatus (final int type, final String text)
  {
//    Log.debug ("Setting status: " + type + ", " + text);
    if (type == TYPE_ERROR)
    {
      final Icon res = getResources().getIcon("statusbar.errorIcon");
      statusHolder.setIcon(res);
    }
    else if (type == TYPE_WARNING)
    {
      final Icon res = getResources().getIcon("statusbar.warningIcon");
      statusHolder.setIcon(res);
    }
    else if (type == TYPE_INFORMATION)
    {
      final Icon res = getResources().getIcon("statusbar.informationIcon");
      statusHolder.setIcon(res);
    }
    else
    {
      final Icon res = getResources().getIcon("statusbar.otherIcon");
      statusHolder.setIcon(res);
    }
    statusHolder.setText(text);
  }


  /**
   * Retrieves the resources for this dialog. If the resources are not initialized, they
   * get loaded on the first call to this method.
   *
   * @return this frames ResourceBundle.
   */
  protected ResourceBundleSupport getResources ()
  {
    if (resources == null)
    {
      resources = new ResourceBundleSupport(PreviewBaseModule.RESOURCES_BASE_NAME);
    }
    return resources;
  }


  public void clear ()
  {
    setStatus(TYPE_NONE, " ");
  }
}
