package org.jfree.report.demo.helper;

import java.awt.BorderLayout;
import java.awt.Container;
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
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.jfree.ui.action.ActionButton;
import org.jfree.util.Log;

/**
 * Creation-Date: 27.08.2005, 13:26:17
 *
 * @author: Thomas Morgner
 */
public class CompoundDemoFrame extends AbstractDemoFrame
{
  private class TreeSelectionHandler implements TreeSelectionListener
  {
    public TreeSelectionHandler()
    {
    }

    public void valueChanged(TreeSelectionEvent e)
    {
      final TreePath treePath = e.getNewLeadSelectionPath();
      if (treePath == null)
      {
        setSelectedHandler(null);
      }
      else
      {
        final Object o = treePath.getLastPathComponent();
        if (o instanceof DemoHandlerTreeNode)
        {
          DemoHandlerTreeNode handlerNode = (DemoHandlerTreeNode) o;
          DemoHandler handler = handlerNode.getHandler();
          setSelectedHandler(handler);
        }
        else
        {
          setSelectedHandler(null);
        }
      }
    }

  }

  private DemoHandler selectedHandler;
  private DemoSelector demoSelector;
  private JPanel demoContent;

  public CompoundDemoFrame(final DemoSelector demoSelector)
  {
    this.demoSelector = demoSelector;
  }

  public DemoSelector getDemoSelector()
  {
    return demoSelector;
  }

  protected void init()
  {
    setTitle(demoSelector.getName());
    setJMenuBar(createMenuBar());
    setContentPane(createDefaultContentPane());
  }

  protected void setSelectedHandler(final DemoHandler handler)
  {
    selectedHandler = handler;
    demoContent.removeAll();
    if (handler instanceof InternalDemoHandler)
    {
      demoContent.add(createDefaultDemoPane((InternalDemoHandler) handler));
      getPreviewAction().setEnabled(true);
    }
    else
    {
      getPreviewAction().setEnabled(handler != null);
    }
    demoContent.revalidate();
  }

  public DemoHandler getSelectedHandler()
  {
    return selectedHandler;
  }

  protected Container createDefaultContentPane()
  {
    demoContent = new JPanel();
    demoContent.setLayout(new BorderLayout());

    final DemoSelectorTreeNode root = new DemoSelectorTreeNode(null,
            demoSelector);
    final DefaultTreeModel model = new DefaultTreeModel(root);
    final JTree demoTree = new JTree(model);
    demoTree.addTreeSelectionListener(new TreeSelectionHandler());

    JSplitPane rootSplitPane =
            new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, demoTree, demoContent);
    rootSplitPane.setContinuousLayout(true);
    rootSplitPane.setDividerLocation(200);
    rootSplitPane.setOneTouchExpandable(true);
    return rootSplitPane;
  }

  protected JComponent createDefaultDemoPane(final InternalDemoHandler demoHandler)
  {
    final JPanel content = new JPanel(new BorderLayout());
    content.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

    final JEditorPane editorPane = new JEditorPane();
    final URL url = demoHandler.getDemoDescriptionSource();
    editorPane.setEditable(false);
    editorPane.setPreferredSize(new Dimension(400, 200));
    if (url != null)
    {
      try
      {
        editorPane.setPage(url);
      }
      catch (IOException e)
      {
        Log.error("Failed to load demo description", e);
        editorPane.setText("Unable to load the demo description. Error: " + e
                .getMessage());
      }
    }
    else
    {
      editorPane.setText(
              "Unable to load the demo description. No such resource.");
    }

    final JScrollPane scroll = new JScrollPane(editorPane,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    final JButton previewButton = new ActionButton(getPreviewAction());

    final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    splitPane.setTopComponent(scroll);
    splitPane.setBottomComponent(demoHandler.getPresentationComponent());
    content.add(splitPane, BorderLayout.CENTER);
    content.add(previewButton, BorderLayout.SOUTH);
    return content;
  }

  protected void attemptPreview()
  {
    final DemoHandler selectedHandler = getSelectedHandler();
    if (selectedHandler == null)
    {
      return;
    }

    final PreviewHandler previewHandler = selectedHandler.getPreviewHandler();
    previewHandler.attemptPreview();
  }
}
