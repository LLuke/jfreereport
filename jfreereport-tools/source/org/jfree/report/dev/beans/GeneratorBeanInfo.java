package org.jfree.report.dev.beans;

import java.beans.BeanInfo;
import java.beans.MethodDescriptor;

public class GeneratorBeanInfo
{
  private int defaultEventIndex;
  private int defaultPropertyIndex;
  private String smallMonochromeIcon;
  private String smallColorIcon;
  private String largeMonochromeIcon;
  private String largeColorIcon;
  private GeneratorBeanDescriptor beanDescriptor;
  private BeanInfo[] additionalBeanInfo;
  private GeneratorEventSetDescriptor[] eventSetDescriptors;
  private MethodDescriptor[] methodDescriptors;
  private GeneratorPropertyDescriptor[] propertyDescriptors;
  private String filename;

  public GeneratorBeanInfo()
  {
    defaultEventIndex = -1;
    defaultPropertyIndex = -1;
  }

  public int getDefaultEventIndex()
  {
    return defaultEventIndex;
  }

  public void setDefaultEventIndex(int defaultEventIndex)
  {
    this.defaultEventIndex = defaultEventIndex;
  }

  public int getDefaultPropertyIndex()
  {
    return defaultPropertyIndex;
  }

  public void setDefaultPropertyIndex(int defaultPropertyIndex)
  {
    this.defaultPropertyIndex = defaultPropertyIndex;
  }

  public String getSmallMonochromeIcon()
  {
    return smallMonochromeIcon;
  }

  public void setSmallMonochromeIcon(String smallMonochromeIcon)
  {
    this.smallMonochromeIcon = smallMonochromeIcon;
  }

  public String getSmallColorIcon()
  {
    return smallColorIcon;
  }

  public void setSmallColorIcon(String smallColorIcon)
  {
    this.smallColorIcon = smallColorIcon;
  }

  public String getLargeMonochromeIcon()
  {
    return largeMonochromeIcon;
  }

  public void setLargeMonochromeIcon(String largeMonochromeIcon)
  {
    this.largeMonochromeIcon = largeMonochromeIcon;
  }

  public String getLargeColorIcon()
  {
    return largeColorIcon;
  }

  public void setLargeColorIcon(String largeColorIcon)
  {
    this.largeColorIcon = largeColorIcon;
  }

  public GeneratorBeanDescriptor getBeanDescriptor()
  {
    return beanDescriptor;
  }

  public void setBeanDescriptor(GeneratorBeanDescriptor beanDescriptor)
  {
    this.beanDescriptor = beanDescriptor;
  }

  public BeanInfo[] getAdditionalBeanInfo()
  {
    return additionalBeanInfo;
  }

  public void setAdditionalBeanInfo(BeanInfo[] additionalBeanInfo)
  {
    this.additionalBeanInfo = additionalBeanInfo;
  }

  public GeneratorEventSetDescriptor[] getEventSetDescriptors()
  {
    return eventSetDescriptors;
  }

  public void setEventSetDescriptors(GeneratorEventSetDescriptor[] eventSetDescriptors)
  {
    this.eventSetDescriptors = eventSetDescriptors;
  }

  public int getEventSetDescriptorsCount ()
  {
    return this.eventSetDescriptors.length;
  }

  public MethodDescriptor[] getMethodDescriptors()
  {
    return methodDescriptors;
  }

  public void setMethodDescriptors(MethodDescriptor[] methodDescriptors)
  {
    this.methodDescriptors = methodDescriptors;
  }

  public GeneratorPropertyDescriptor[] getPropertyDescriptors()
  {
    return propertyDescriptors;
  }

  public void setPropertyDescriptors(GeneratorPropertyDescriptor[] propertyDescriptors)
  {
    this.propertyDescriptors = propertyDescriptors;
  }

  public int getPropertyDescriptorsCount ()
  {
    return this.propertyDescriptors.length;
  }

  public String getIcon(int iconKind)
  {
    if (iconKind == BeanInfo.ICON_MONO_16x16)
    {
      return getSmallMonochromeIcon();
    }
    if (iconKind == BeanInfo.ICON_MONO_32x32)
    {
      return getLargeMonochromeIcon();
    }
    if (iconKind == BeanInfo.ICON_COLOR_16x16)
    {
      return getSmallColorIcon();
    }
    if (iconKind == BeanInfo.ICON_COLOR_32x32)
    {
      return getLargeColorIcon();
    }
    return null;
  }

  public String getFilename()
  {
    return filename;
  }

  public void setFilename(String filename)
  {
    this.filename = filename;
  }

  public String getBeanInfoName ()
  {
    return beanDescriptor.getBeanClass() + "BeanInfo";
  }
}
