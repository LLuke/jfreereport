package org.jfree.report.dev.beans;

import java.beans.FeatureDescriptor;

public class GeneratorEventSetDescriptor extends FeatureDescriptor
{
  private String addMethod;
  private String removeMethod;
  private String[] listenerMethods;
  private String listenerType;

  public GeneratorEventSetDescriptor()
  {
  }

  public String getAddMethod()
  {
    return addMethod;
  }

  public void setAddMethod(String addMethod)
  {
    this.addMethod = addMethod;
  }

  public String getRemoveMethod()
  {
    return removeMethod;
  }

  public void setRemoveMethod(String removeMethod)
  {
    this.removeMethod = removeMethod;
  }

  public String[] getListenerMethods()
  {
    return listenerMethods;
  }

  public void setListenerMethods(String[] listenerMethods)
  {
    this.listenerMethods = listenerMethods;
  }

  public String getListenerType()
  {
    return listenerType;
  }

  public void setListenerType(String listenerType)
  {
    this.listenerType = listenerType;
  }

}
