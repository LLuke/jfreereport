package org.jfree.report.dev.locales;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import org.jfree.base.BaseBoot;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.action.AbstractFileSelectionAction;
import org.jfree.util.Log;
import org.jfree.io.IOUtils;

public class LocalesEditor extends JFrame
{
  private class LoadLocalesConfigurationAction extends AbstractFileSelectionAction
  {
    private File selectedFile;

    /**
     * Defines an <code>Action</code> object with a default description string and default
     * icon.
     */
    public LoadLocalesConfigurationAction ()
    {
      super(LocalesEditor.this);
      putValue(NAME, "Load ..");
    }

    /**
     * Returns a descriptive text describing the file extension.
     *
     * @return the file description.
     */
    protected String getFileDescription ()
    {
      return "Locales configuration files";
    }

    /**
     * Returns the file extension that should be used for the operation.
     *
     * @return the file extension.
     */
    protected String getFileExtension ()
    {
      return ".properties";
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed (final ActionEvent e)
    {
      final File newSelectedFile = performSelectFile
              (selectedFile, JFileChooser.OPEN_DIALOG, true);
      if (newSelectedFile == null)
      {
        return;
      }
      // load ...
      try
      {
        final Properties p = new Properties();
        final FileInputStream fin = new FileInputStream(newSelectedFile);
        p.load(fin);
        fin.close();
        selectedFile = newSelectedFile;

        final Iterator it = p.entrySet().iterator();
        final File parentFile = newSelectedFile.getParentFile();
        while (it.hasNext())
        {
          final Map.Entry me = (Map.Entry) it.next();
          final File f = new File (parentFile, (String) me.getValue());
          try
          {
            addLocation(f, (String) me.getKey());
          }
          catch(Exception ex)
          {
            Log.error ("Unable to add location " + f, ex);
          }
        }
      }
      catch (IOException e1)
      {
        return;
      }
    }
  }

  private class SaveLocalesConfigurationAction extends AbstractFileSelectionAction
  {
    private File selectedFile;

    /**
     * Defines an <code>Action</code> object with a default description string and default
     * icon.
     */
    public SaveLocalesConfigurationAction ()
    {
      super(LocalesEditor.this);
      putValue(NAME, "Save ..");
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed (final ActionEvent e)
    {
      final File newSelectedFile = performSelectFile
              (selectedFile, JFileChooser.OPEN_DIALOG, true);
      if (newSelectedFile == null)
      {
        return;
      }
      // save ...
      try
      {
        final Properties p = new Properties();
        final int c = treeModel.getResourceLocationCount();

        final File parentFile = newSelectedFile.getParentFile();
        final URL targetURL = parentFile.toURL();

        for (int i = 0; i < c; i++)
        {
          final File location = treeModel.getResourceLocation(c);
          final String displayName = treeModel.getLocationDisplayName(location);
          final URL locationURL = location.toURL();

          if (displayName != null)
          {
            p.setProperty(displayName, IOUtils.getInstance().createRelativeURL
                    (locationURL, targetURL));
          }
          else
          {
            p.setProperty(location.getPath(), IOUtils.getInstance().createRelativeURL
                    (locationURL, targetURL));
          }
        }
        selectedFile = newSelectedFile;

        final FileOutputStream fout = new FileOutputStream(newSelectedFile);
        p.store(fout, "");
        fout.close();
      }
      catch (IOException e1)
      {
        return;
      }

    }


    /**
     * Returns a descriptive text describing the file extension.
     *
     * @return the file description.
     */
    protected String getFileDescription ()
    {
      return "Locales configuration files";
    }

    /**
     * Returns the file extension that should be used for the operation.
     *
     * @return the file extension.
     */
    protected String getFileExtension ()
    {
      return ".properties";
    }

  }

  private class AddResourceLocationAction extends AbstractFileSelectionAction
  {
    private File selectedFile;

    /**
     * Defines an <code>Action</code> object with a default description string and default
     * icon.
     */
    public AddResourceLocationAction ()
    {
      super(LocalesEditor.this);
      putValue(NAME, "Add new resource location ..");
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed (final ActionEvent e)
    {
      final File newSelection = performSelectFile(selectedFile, JFileChooser.OPEN_DIALOG, true);
      if (newSelection == null)
      {
        return;
      }
      try
      {
        addLocation(newSelection, null);
        selectedFile = newSelection;
      }
      catch(Exception ex)
      {
        Log.error ("Unable to add that locale");
      }
    }

    /**
     * Returns a descriptive text describing the file extension.
     *
     * @return the file description.
     */
    protected String getFileDescription ()
    {
      return "ocales (*.properties files)";
    }

    /**
     * Returns the file extension that should be used for the operation.
     *
     * @return the file extension.
     */
    protected String getFileExtension ()
    {
      return ".properties";
    }
  }

  private class RemoveResourceLocationAction extends AbstractAction
          implements TreeSelectionListener
  {
    private ResourceLocationNode location;

    /**
     * Defines an <code>Action</code> object with a default description string and default
     * icon.
     */
    public RemoveResourceLocationAction ()
    {
      putValue(NAME, "Remove resource location ..");
      setEnabled(false);
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed (final ActionEvent e)
    {
      if (location == null)
      {
        return;
      }
      treeModel.removeLocation(location.getResourceLocation());
    }

    /**
     * Called whenever the value of the selection changes.
     *
     * @param e the event that characterizes the change.
     */
    public void valueChanged (final TreeSelectionEvent e)
    {
      final TreePath path = e.getNewLeadSelectionPath();
      if (path == null)
      {
        setEnabled(false);
        return;
      }
      final Object[] objects = path.getPath();
      if (objects.length < 2)
      {
        setEnabled(false);
        return;
      }

      this.location = (ResourceLocationNode) objects[1];
      setEnabled(true);
    }
  }

  private class AddLocaleAction extends AbstractAction
          implements TreeSelectionListener
  {
    private ResourceLocationNode location;

    /**
     * Defines an <code>Action</code> object with a default description string and default
     * icon.
     */
    public AddLocaleAction ()
    {
      putValue(NAME, "Add new Locale ..");
      setEnabled(false);
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed (final ActionEvent e)
    {
      if (location == null)
      {
        return;
      }

      localesDialog.setInvalidLocales(location.getDefinedLocales());
      final String locale = localesDialog.selectLocale();
      if (locale == null)
      {
        return;
      }

      Log.debug ("Adding .." + locale);
      location.addLocalization (locale);
    }

    /**
     * Called whenever the value of the selection changes.
     *
     * @param e the event that characterizes the change.
     */
    public void valueChanged (final TreeSelectionEvent e)
    {
      final TreePath path = e.getNewLeadSelectionPath();
      if (path == null)
      {
        setEnabled(false);
        return;
      }
      final Object[] objects = path.getPath();
      if (objects.length < 2)
      {
        setEnabled(false);
        return;
      }

      this.location = (ResourceLocationNode) objects[1];
      setEnabled(true);
    }
  }

  private class RemoveLocaleAction extends AbstractAction
          implements TreeSelectionListener
  {
    private ResourceLocationNode location;
    private LanguagePackNode locale;

    /**
     * Defines an <code>Action</code> object with a default description string and default
     * icon.
     */
    public RemoveLocaleAction ()
    {
      putValue(NAME, "Remove locale ..");
      setEnabled(false);
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed (final ActionEvent e)
    {
      if (location == null)
      {
        return;
      }
      if (locale == null)
      {
        return;
      }
      location.removeLocalization(locale.getLanguage());
    }

    /**
     * Called whenever the value of the selection changes.
     *
     * @param e the event that characterizes the change.
     */
    public void valueChanged (final TreeSelectionEvent e)
    {
      this.locale = null;
      this.location = null;

      final TreePath path = e.getNewLeadSelectionPath();
      if (path == null)
      {
        setEnabled(false);
        return;
      }
      final Object[] objects = path.getPath();
      if (objects.length < 3)
      {
        setEnabled(false);
        return;
      }

      this.location = (ResourceLocationNode) objects[1];
      this.locale = (LanguagePackNode) objects[2];
      setEnabled(true);
    }
  }

  private class ExitAction extends AbstractAction
          implements WindowListener
  {
    /**
     * Defines an <code>Action</code> object with a default description string and default
     * icon.
     */
    public ExitAction ()
    {
      putValue(NAME, "Exit");
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed (final ActionEvent e)
    {
      System.exit(0);
    }

    /**
     * Invoked when the Window is set to be the active Window. Only a Frame or a Dialog can
     * be the active Window. The native windowing system may denote the active Window or its
     * children with special decorations, such as a highlighted title bar. The active Window
     * is always either the focused Window, or the first Frame or Dialog that is an owner of
     * the focused Window.
     */
    public void windowActivated (final WindowEvent e)
    {
    }

    /**
     * Invoked when a window has been closed as the result of calling dispose on the
     * window.
     */
    public void windowClosed (final WindowEvent e)
    {
    }

    /**
     * Invoked when the user attempts to close the window from the window's system menu.  If
     * the program does not explicitly hide or dispose the window while processing this
     * event, the window close operation will be cancelled.
     */
    public void windowClosing (final WindowEvent e)
    {
      System.exit(0);
    }

    /**
     * Invoked when a Window is no longer the active Window. Only a Frame or a Dialog can be
     * the active Window. The native windowing system may denote the active Window or its
     * children with special decorations, such as a highlighted title bar. The active Window
     * is always either the focused Window, or the first Frame or Dialog that is an owner of
     * the focused Window.
     */
    public void windowDeactivated (final WindowEvent e)
    {
    }

    /**
     * Invoked when a window is changed from a minimized to a normal state.
     */
    public void windowDeiconified (final WindowEvent e)
    {
    }

    /**
     * Invoked when a window is changed from a normal to a minimized state. For many
     * platforms, a minimized window is displayed as the icon specified in the window's
     * iconImage property.
     *
     * @see java.awt.Frame#setIconImage
     */
    public void windowIconified (final WindowEvent e)
    {
    }

    /**
     * Invoked the first time a window is made visible.
     */
    public void windowOpened (final WindowEvent e)
    {
    }
  }

  private class SaveAction extends AbstractAction
  {
    /**
     * Defines an <code>Action</code> object with a default description string and default
     * icon.
     */
    public SaveAction ()
    {
      putValue(NAME, "Save all locales");
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed (final ActionEvent e)
    {
      final Iterator it = loadedLocales.entrySet().iterator();
      while (it.hasNext())
      {
        final Map.Entry me = (Map.Entry) it.next();
        final File key = (File) me.getKey();
        final EditableProperties properties = (EditableProperties) me.getValue();

        try
        {
          final FileOutputStream fout = new FileOutputStream(key);
          properties.write(fout);
          fout.close();
        }
        catch(Exception ex)
        {
          Log.debug ("Unable to save properties for " + key);
        }
      }
    }
  }

  private class ReloadAction extends AbstractAction
  {
    /**
     * Defines an <code>Action</code> object with a default description string and default
     * icon.
     */
    public ReloadAction ()
    {
      putValue(NAME, "(Re)load locales");
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed (final ActionEvent e)
    {
      final int c = treeModel.getResourceLocationCount();
      final ArrayList files = new ArrayList();
      for (int i = 0; i < c; i++)
      {
        files.add (treeModel.getRootResource(i));
      }
      loadedLocales.clear();
      for (int i = 0; i < files.size(); i++)
      {
        final File resource = (File) files.get(i);
        final String name = treeModel.getLocationDisplayName(resource);
        treeModel.removeLocation(resource);
        treeModel.addResource(resource, name);
      }
    }
  }

  private class LocalesSelectionHandler implements TreeSelectionListener
  {
    public LocalesSelectionHandler ()
    {
    }

    /**
     * Called whenever the value of the selection changes.
     *
     * @param e the event that characterizes the change.
     */
    public void valueChanged (final TreeSelectionEvent e)
    {
      final TreePath path = e.getNewLeadSelectionPath();
      if (path == null)
      {
        localesEditor.setModel(createEmptyModel());
        return;
      }
      final Object[] objects = path.getPath();
      if (objects.length < 3)
      {
        localesEditor.setModel(createEmptyModel());
        return;
      }

      final ResourceLocationNode location = (ResourceLocationNode) objects[1];
      final LanguagePackNode locale = (LanguagePackNode) objects[2];
      try
      {
        localesEditor.setModel(createModel(location.getRootResource(), locale.getFile()));
      }
      catch (IOException e1)
      {
        localesEditor.setModel(createEmptyModel());
      }
    }
  }

  private class TreeContextMenuHandler extends MouseAdapter
  {
    public TreeContextMenuHandler ()
    {
    }

    private void handleContextMenuOpen (final MouseEvent e)
    {
      final TreePath path = localesTree.getPathForLocation(e.getX(), e.getY());
      if (path == null)
      {
        return;
      }
      localesTree.setSelectionPath(path);
      treeContextMenu.pack();
      treeContextMenu.show(e.getComponent(), e.getX(), e.getY());
      treeContextMenu.setVisible(true);

    }

    /**
     * Invoked when the mouse has been clicked on a component.
     */
    public void mouseClicked (final MouseEvent e)
    {
      if (e.isPopupTrigger())
      {
        handleContextMenuOpen(e);
      }
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     */
    public void mousePressed (final MouseEvent e)
    {
      if (e.isPopupTrigger())
      {
        handleContextMenuOpen(e);
      }
    }

    /**
     * Invoked when a mouse button has been released on a component.
     */
    public void mouseReleased (final MouseEvent e)
    {
      if (e.isPopupTrigger())
      {
        handleContextMenuOpen(e);
      }
    }
  }

  private LocalesTreeModel treeModel;
  private JTree localesTree;
  private JTable localesEditor;
  private AddLocaleAction addLocaleAction;
  private AddResourceLocationAction addResourceLocationAction;
  private RemoveLocaleAction removeLocaleAction;
  private RemoveResourceLocationAction removeResourceLocationAction;
  private ExitAction exitAction;
  private Action loadAction;
  private Action saveAction;
  private Action loadConfigAction;
  private Action saveConfigAction;

  private HashMap loadedLocales;
  private JPopupMenu treeContextMenu;
  private SelectLocalesDialog localesDialog;


  public LocalesEditor ()
  {
    setTitle("Locales Editor");

    loadedLocales = new HashMap();
    localesDialog = new SelectLocalesDialog(this);
    localesDialog.pack();

    addLocaleAction = new AddLocaleAction();
    addResourceLocationAction = new AddResourceLocationAction();
    removeLocaleAction = new RemoveLocaleAction();
    removeResourceLocationAction = new RemoveResourceLocationAction();

    exitAction = new ExitAction();
    addWindowListener(exitAction);
    loadAction = new ReloadAction();
    saveAction = new SaveAction();
    loadConfigAction = new LoadLocalesConfigurationAction();
    saveConfigAction = new SaveLocalesConfigurationAction();

    treeModel = new LocalesTreeModel();

    treeContextMenu = new JPopupMenu();
    treeContextMenu.add(new JMenuItem (addResourceLocationAction));
    treeContextMenu.add(new JMenuItem (addLocaleAction));
    treeContextMenu.addSeparator();
    treeContextMenu.add(new JMenuItem (removeResourceLocationAction));
    treeContextMenu.add(new JMenuItem (removeLocaleAction));

    localesTree = new JTree (treeModel);
    localesTree.addTreeSelectionListener(new LocalesSelectionHandler());
    localesTree.addTreeSelectionListener(addLocaleAction);
    localesTree.addTreeSelectionListener(removeResourceLocationAction);
    localesTree.addTreeSelectionListener(removeLocaleAction);
    localesTree.addMouseListener(new TreeContextMenuHandler());
    localesEditor = new JTable(createEmptyModel());
    localesEditor.setDefaultRenderer(Object.class, new LocalesTableCellRenderer());

    final JPanel contentPane = new JPanel();
    contentPane.setLayout(new BorderLayout());
    contentPane.add(new JSplitPane (JSplitPane.HORIZONTAL_SPLIT,
                    new JScrollPane(localesTree), new JScrollPane(localesEditor)));

    setContentPane(contentPane);

    final JMenu localesMenu = new JMenu("Locales");
    localesMenu.add(new JMenuItem (addResourceLocationAction));
    localesMenu.add(new JMenuItem (addLocaleAction));
    localesMenu.addSeparator();
    localesMenu.add(new JMenuItem (removeResourceLocationAction));
    localesMenu.add(new JMenuItem (removeLocaleAction));
    localesMenu.addSeparator();
    localesMenu.add(new JMenuItem (loadAction));
    localesMenu.add(new JMenuItem (saveAction));
    localesMenu.addSeparator();
    localesMenu.add(new JMenuItem (loadConfigAction));
    localesMenu.add(new JMenuItem (saveConfigAction));
    localesMenu.addSeparator();
    localesMenu.add(new JMenuItem (exitAction));

    final JMenuBar menuBar = new JMenuBar();
    menuBar.add(localesMenu);
    setJMenuBar(menuBar);
  }

  private LocalesTableModel createEmptyModel()
  {
    final EditableProperties epDefault = new EditableProperties();
    final LocalesTableModel ltm = new LocalesTableModel(epDefault, epDefault);
    return ltm;
  }

  private LocalesTableModel createModel (final File rootLocation, final File localeFile)
          throws IOException
  {
    final EditableProperties epDefault = getProperties(rootLocation);
    final EditableProperties epLocale = getProperties(localeFile);
    final LocalesTableModel ltm = new LocalesTableModel(epLocale, epDefault);
    return ltm;
  }

  private synchronized EditableProperties getProperties (final File file) throws IOException
  {
    final EditableProperties cached = (EditableProperties) loadedLocales.get(file);
    if (cached != null)
    {
      Log.debug ("Cached " + file);
      return cached;
    }


    final EditableProperties epLocale = new EditableProperties();
    if (file.exists())
    {
      Log.debug ("Loading " + file);
      final FileInputStream localeStream = new FileInputStream (file);
      epLocale.load(localeStream);
      localeStream.close();
    }
    else
    {
      Log.debug ("Creating " + file);
    }

    loadedLocales.put (file, epLocale);
    return epLocale;
  }

  public static void main (final String[] args)
  {
    BaseBoot.getInstance().start();
    final LocalesEditor localesEditor = new LocalesEditor();
    localesEditor.setSize(500, 400);
    RefineryUtilities.centerFrameOnScreen(localesEditor);
    localesEditor.setVisible(true);
  }

  private void addLocation (final File location, final String displayName)
  {
    treeModel.addResource(location, displayName);
  }
}
