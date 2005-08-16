package org.jfree.report.dev.beans.dochandler;

import com.sun.javadoc.ClassDoc;
import org.jfree.report.dev.beans.GeneratorBeanInfo;
import org.jfree.report.dev.beans.GeneratorBeanDescriptor;
import org.jfree.report.dev.beans.BeanInfoDoclet;

public class BeanClassDocletHandler
{
  private GeneratorBeanInfo beanInfo;
  private ClassDoc beanClassDoc;

  public BeanClassDocletHandler()
  {
    beanInfo = new GeneratorBeanInfo();
  }

  public void setClassDoc(final ClassDoc cd)
  {
    beanClassDoc = cd;
    beanInfo.setBeanDescriptor(createBeanDescriptor());
    final EventSetHandler eventSetHandler = new EventSetHandler(cd);
    beanInfo.setEventSetDescriptors(eventSetHandler.getEventSetDescriptors());
    beanInfo.setDefaultEventIndex(eventSetHandler.getDefaultIndex());

    final PropertiesHandler propertiesHandler = new PropertiesHandler(cd);
    beanInfo.setPropertyDescriptors(propertiesHandler.getPropertyDescriptors());
  }

  public ClassDoc getBeanClassDoc()
  {
    return beanClassDoc;
  }

  private GeneratorBeanDescriptor createBeanDescriptor()
  {
    final GeneratorBeanDescriptor bd = new GeneratorBeanDescriptor();
    bd.setBeanClass(BeanInfoDoclet.getName(beanClassDoc.name()));
    if (beanClassDoc.containingPackage() != null)
    {
      bd.setPackageName(beanClassDoc.containingPackage().name());
    }

    final String shortDesc = BeanInfoDoclet.buildFirstSentence(beanClassDoc.firstSentenceTags());
    bd.setShortDescription(shortDesc);

    final String customizerClass =
        BeanInfoDoclet.getTagContent(beanClassDoc, "customizer", null);
    if (customizerClass != null)
    {
      bd.setCustomizerClass(customizerClass);
    }
    BeanInfoDoclet.fillFeatureDescriptor(bd, beanClassDoc);
    return bd;
  }

  public GeneratorBeanInfo getBeanInfo()
  {
    return beanInfo;
  }
}
