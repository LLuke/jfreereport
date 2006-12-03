/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
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
 * SimpleDemoFrame.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */
package org.jfree.report.demo.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.jfree.ui.action.ActionButton;
import org.jfree.util.Log;

public class SimpleDemoFrame extends AbstractDemoFrame
{
  private InternalDemoHandler demoHandler;

  public SimpleDemoFrame (final InternalDemoHandler demoHandler)
  {
    this.demoHandler = demoHandler;
  }

  protected InternalDemoHandler getDemoHandler()
  {
    return demoHandler;
  }

  public void init ()
  {
    final InternalDemoHandler demoHandler = getDemoHandler();
    setTitle(demoHandler.getDemoName());
    setJMenuBar(createMenuBar());
    setContentPane(createDefaultContentPane());
  }

  protected JComponent createDefaultContentPane ()
  {
    final JPanel content = new JPanel(new BorderLayout());
    content.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

    final InternalDemoHandler demoHandler = getDemoHandler();
    final JEditorPane editorPane = new JEditorPane();
    final URL url = demoHandler.getDemoDescriptionSource();
    editorPane.setEditable(false);
    editorPane.setPreferredSize(new Dimension (400, 200));
    if (url != null)
    {
      try
      {
        editorPane.setPage(url);
      }
      catch (IOException e)
      {
        Log.error("Failed to load demo description", e);
        editorPane.setText("Unable to load the demo description. Error: " + e.getMessage());
      }
    }
    else
    {
      editorPane.setText("Unable to load the demo description. No such resource.");
    }

    final JScrollPane scroll = new JScrollPane(editorPane,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    final JButton previewButton = new ActionButton(getPreviewAction());

    final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    splitPane.setTopComponent(scroll);
    splitPane.setBottomComponent(demoHandler.getPresentationComponent());
    content.add(splitPane, BorderLayout.CENTER);
    content.add(previewButton, BorderLayout.SOUTH);
    return content;
  }

  /**
   * Handler method called by the preview action. This method should perform all
   * operations to preview the report.
   */
  protected void attemptPreview ()
  {
    final InternalDemoHandler demoHandler = getDemoHandler();
    final PreviewHandler previewHandler = demoHandler.getPreviewHandler();
    previewHandler.attemptPreview();
  }
}
