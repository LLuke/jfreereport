package org.jfree.report.dev.locales;

import java.io.File;
import javax.swing.tree.TreeNode;

public interface LanguagePackNode extends TreeNode
{
  public String getLanguage ();
  public File getFile ();

}
