package org.jfree.report.dev.beans;

import java.beans.FeatureDescriptor;

public class GeneratorPropertyDescriptor extends FeatureDescriptor
{
  private String propertyEditor;
  private String propertyType;
  private String readMethod;
  private String writeMethod;
  private boolean bound;
  private boolean constrained;

  public GeneratorPropertyDescriptor()
  {
  }

  public String getPropertyEditor()
  {
    return propertyEditor;
  }

  public void setPropertyEditor(final String propertyEditor)
  {
    this.propertyEditor = propertyEditor;
  }

  public String getPropertyType()
  {
    return propertyType;
  }

  public void setPropertyType(final String propertyType)
  {
    this.propertyType = propertyType;
  }

  public String getReadMethod()
  {
    return readMethod;
  }

  public void setReadMethod(final String readMethod)
  {
    this.readMethod = readMethod;
  }

  public String getWriteMethod()
  {
    return writeMethod;
  }

  public void setWriteMethod(final String writeMethod)
  {
    this.writeMethod = writeMethod;
  }

  public boolean isBound()
  {
    return bound;
  }

  public void setBound(final boolean bound)
  {
    this.bound = bound;
  }

  public boolean isConstrained()
  {
    return constrained;
  }

  public void setConstrained(final boolean constrained)
  {
    this.constrained = constrained;
  }

  public boolean isIndexed ()
  {
    return false;
  }
  
}
