package org.jfree.report;

import java.util.ResourceBundle;
import java.io.Serializable;

public interface ResourceBundleFactory extends Serializable
{
  public ResourceBundle getResourceBundle (String key);
}
