package org.jfree.report.dev.locales;

import java.io.File;
import javax.swing.tree.TreeNode;

public interface ResourceLocationNode extends TreeNode
{
  public File getRootResource ();
  public File getResourceLocation ();
  public String getResourceName ();

  public String[] getDefinedLocales ();

  public void addLocalization (String locale);
  public void removeLocalization (final String locale);

}
