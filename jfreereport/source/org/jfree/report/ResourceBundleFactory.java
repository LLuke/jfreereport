package org.jfree.report;

import java.io.Serializable;
import java.util.ResourceBundle;

public interface ResourceBundleFactory extends Serializable
{
  public ResourceBundle getResourceBundle (String key);
}
