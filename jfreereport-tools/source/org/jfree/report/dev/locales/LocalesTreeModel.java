package org.jfree.report.dev.locales;

import java.io.File;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Enumeration;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.jfree.util.ObjectUtilities;
import org.jfree.util.Log;

public class LocalesTreeModel extends DefaultTreeModel
{
  private static class LocalesRootNode extends DefaultMutableTreeNode
  {
    /**
     * Creates a tree node that has no parent and no children, but which allows children.
     */
    public LocalesRootNode ()
    {
    }

    /**
     * Returns the result of sending <code>toString()</code> to this node's user object, or
     * null if this node has no user object.
     *
     * @see	#getUserObject
     */
    public String toString ()
    {
      return "Locales";
    }

  }

  private class ResourceLocationNodeImpl extends DefaultMutableTreeNode
          implements ResourceLocationNode
  {
    private File resourceLocation;
    private File rootResource;
    private String resourceName;
    public static final String DEFAULT_LANG = "<default>";
    private String displayName;

    /**
     *
     */
    public ResourceLocationNodeImpl (final File rootResource, final String displayName)
    {
      if (rootResource == null)
      {
        throw new NullPointerException("File given is null");
      }
      final String name = rootResource.getName();
      if (name.endsWith(".properties") == false)
      {
        throw new IllegalArgumentException("The file name is not valid");
      }
      final int langSeparator = name.indexOf("_");
      final int endIndex = name.length() - ".properties".length();
      if (langSeparator == -1)
      {
        this.resourceName = name.substring(0, endIndex);
        this.rootResource = rootResource;
      }
      else
      {
        this.resourceName = name.substring(0, langSeparator);
        this.rootResource = new File
                (rootResource.getParentFile(), this.resourceName + ".properties");
      }
      if (this.rootResource.isFile() == false || this.rootResource.canRead() == false)
      {
        throw new IllegalArgumentException("The file is no ordinary file or not readable:" + rootResource);
      }
      this.resourceLocation = this.rootResource.getParentFile();
      this.displayName = displayName;
      addAllAvailable();
    }

    public String getDisplayName ()
    {
      return displayName;
    }

    public void addAllAvailable ()
    {
      removeAllChildren();

      final TreeSet locales = new TreeSet();
      // addAll
      final File[] files = this.resourceLocation.listFiles (new LocalesFileFilter(resourceName));
      for (int i = 0; i < files.length; i++)
      {
        final String name = files[i].getName();
        final int langSeparator = name.indexOf("_");
        final int endIndex = name.length() - ".properties".length();
        if (langSeparator == -1)
        {
          locales.add(DEFAULT_LANG);
        }
        else
        {
          final String language = name.substring(langSeparator + 1, endIndex);
          locales.add(language);
        }
      }

      final Iterator it = locales.iterator();
      while (it.hasNext())
      {
        final String locale = (String) it.next();
        if (locale.equals(DEFAULT_LANG))
        {
          addLocalization(null);
        }
        else
        {
          addLocalization(locale);
        }
      }
    }

    public void addLocalization (final String locale)
    {
      Log.debug ("Adding new localization...");
      add(new LanguagePackNodeImpl(locale));
      nodeStructureChanged(this);
    }

    public void removeLocalization (final String locale)
    {
      for (int i = 0; i < children.size(); i++)
      {
        final LanguagePackNode lpn = (LanguagePackNode) children.get(i);
        if (ObjectUtilities.equal(lpn.getLanguage(), locale))
        {
          remove(i);
          nodeStructureChanged(this);
          return;
        }
      }
    }

    /**
     * Returns the result of sending <code>toString()</code> to this node's user object, or
     * null if this node has no user object.
     *
     * @see	#getUserObject
     */
    public String toString ()
    {
      if (displayName != null)
      {
        return displayName;
      }
      return resourceLocation.getPath();
    }

    public File getRootResource ()
    {
      return rootResource;
    }

    public File getResourceLocation ()
    {
      return resourceLocation;
    }

    public String getResourceName ()
    {
      return resourceName;
    }

    public String[] getDefinedLocales ()
    {
      final TreeSet locales = new TreeSet();
      final Enumeration childs = children();
      while (childs.hasMoreElements())
      {
        final LanguagePackNode lpn = (LanguagePackNode) childs.nextElement();
        if (lpn.getLanguage() != null)
        {
          locales.add(lpn.getLanguage());
        }
      }
      return (String[]) locales.toArray(new String[locales.size()]);
    }
  }

  private static class LanguagePackNodeImpl extends DefaultMutableTreeNode
          implements LanguagePackNode
  {
    private String language;

    public LanguagePackNodeImpl (final String language)
    {
      this.language = language;
    }

    public String toString ()
    {
      if (language == null)
      {
        return "<default>";
      }
      return language;
    }

    public String getLanguage ()
    {
      return language;
    }

    public File getFile ()
    {
      if (getParent() == null)
      {
        return null;
      }
      final ResourceLocationNodeImpl locationNodeImpl = (ResourceLocationNodeImpl) getParent();
      final File directory = locationNodeImpl.getResourceLocation();
      final String name = locationNodeImpl.getResourceName();
      final String resourceName;
      if (language == null)
      {
         resourceName = name + ".properties";
      }
      else
      {
        resourceName = name + "_" + language + ".properties";
      }
      return new File(directory, resourceName);
    }

    public boolean isLeaf ()
    {
      return true;
    }

    /**
     * Returns true if this node is allowed to have children.
     *
     * @return	true if this node allows children, else false
     */
    public boolean getAllowsChildren ()
    {
      return false;
    }
  }

  private LocalesRootNode rootNode;
  /**
   * Creates a tree.
   */
  public LocalesTreeModel ()
  {
    super(new LocalesRootNode(), true);
    this.rootNode = (LocalesRootNode) root;
  }

  public synchronized boolean addResource (final File resource, final String displayName)
  {
    if (findResourceLocation(resource.getParentFile()) == null)
    {
      rootNode.add(new ResourceLocationNodeImpl(resource, displayName));
      nodeStructureChanged(rootNode);
      return true;
    }
    return false;
  }

  public File getResourceLocation (final int pos)
  {
    final ResourceLocationNodeImpl rln = (ResourceLocationNodeImpl) rootNode.getChildAt(pos);
    return rln.getResourceLocation();
  }

  public File getRootResource (final int pos)
  {
    final ResourceLocationNodeImpl rln = (ResourceLocationNodeImpl) rootNode.getChildAt(pos);
    return rln.getRootResource();
  }

  public int getResourceLocationCount ()
  {
    return rootNode.getChildCount();
  }

  private ResourceLocationNodeImpl findResourceLocation (final File resource)
  {
    for (int i = 0; i < rootNode.getChildCount(); i++)
    {
      final ResourceLocationNodeImpl rln =
              (ResourceLocationNodeImpl) rootNode.getChildAt(i);
      if (rln.getResourceLocation().equals(resource))
      {
        return rln;
      }
    }
    return null;
  }

  public boolean addLanguage (final File location, final String displayName, final String language)
  {
    ResourceLocationNodeImpl rln = findResourceLocation(location);
    if (rln == null)
    {
      rln = new ResourceLocationNodeImpl(location, displayName);
      rootNode.add (rln);
      rln.add(new LanguagePackNodeImpl(language));
    }
    else
    {
      for (int i = 0; i < rln.getChildCount(); i++)
      {
        final LanguagePackNodeImpl lpn = (LanguagePackNodeImpl) rln.getChildAt(i);
        if (ObjectUtilities.equal(language, lpn.getLanguage()))
        {
          return false;
        }
      }
      rln.add(new LanguagePackNodeImpl(language));
    }
    nodeStructureChanged(rootNode);
    return true;
  }

  public int getLanguageCount (final File resourceLocation)
  {
    final ResourceLocationNodeImpl rln = findResourceLocation(resourceLocation);
    if (rln == null)
    {
      return 0;
    }
    return rln.getChildCount();
  }

  public File getLocalizationFile (final File location, final int pos)
  {
    final ResourceLocationNodeImpl rln = findResourceLocation(location);
    if (rln == null)
    {
      return null;
    }
    final LanguagePackNodeImpl lpn = (LanguagePackNodeImpl) rln.getChildAt(pos);
    return lpn.getFile();
  }

  public String getLanguage (final File location, final int pos)
  {
    final ResourceLocationNodeImpl rln = findResourceLocation(location);
    if (rln == null)
    {
      return null;
    }
    final LanguagePackNodeImpl lpn = (LanguagePackNodeImpl) rln.getChildAt(pos);
    return lpn.getLanguage();
  }

  public String getLocationDisplayName (final File location)
  {
    final ResourceLocationNodeImpl rln = findResourceLocation(location);
    if (rln == null)
    {
      return null;
    }
    return rln.getDisplayName();
  }

  public void removeLocation (final File location)
  {
    final ResourceLocationNodeImpl rln = findResourceLocation(location);
    if (rln == null)
    {
      return;
    }
    rootNode.remove(rln);
    nodeStructureChanged(rootNode);
  }

  public void removeLocalization (final File location, final int pos)
  {
    final ResourceLocationNodeImpl rln = findResourceLocation(location);
    if (rln == null)
    {
      return;
    }
    rln.remove(pos);
    nodeStructureChanged(rln);
  }

  public void removeLocalization (final File location, final String lang)
  {
    final ResourceLocationNodeImpl rln = findResourceLocation(location);
    if (rln == null)
    {
      return;
    }
    rln.removeLocalization(lang);
  }
}
