package org.jfree.report.dev.beans;

import java.beans.FeatureDescriptor;
import java.beans.Introspector;
import java.beans.IntrospectionException;

public class GeneratorBeanDescriptor extends FeatureDescriptor
{
  private String beanClass;
  private String packageName;
  private String customizerClass;
  private String stopClass;

  public GeneratorBeanDescriptor()
  {
    try
    {
      Introspector.getBeanInfo(Object.class);
    }
    catch (IntrospectionException e)
    {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
  }

  public String getCustomizerClass()
  {
    return customizerClass;
  }

  public void setCustomizerClass(String customizerClass)
  {
    this.customizerClass = customizerClass;
  }

  public String getBeanClass()
  {
    return beanClass;
  }

  public void setBeanClass(String beanClass)
  {
    this.beanClass = beanClass;
  }

  public String getPackageName()
  {
    return packageName;
  }

  public void setPackageName(String packageName)
  {
    this.packageName = packageName;
  }

  public String getStopClass()
  {
    return stopClass;
  }

  public void setStopClass(String stopClass)
  {
    this.stopClass = stopClass;
  }
}
