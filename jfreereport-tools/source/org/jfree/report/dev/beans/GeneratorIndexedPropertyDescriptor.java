package org.jfree.report.dev.beans;

public class GeneratorIndexedPropertyDescriptor extends GeneratorPropertyDescriptor
{
  private String indexedReadMethod;
  private String indexedWriteMethod;

  public GeneratorIndexedPropertyDescriptor()
  {
  }

  public String getIndexedReadMethod()
  {
    return indexedReadMethod;
  }

  public void setIndexedReadMethod(String indexedReadMethod)
  {
    this.indexedReadMethod = indexedReadMethod;
  }

  public String getIndexedWriteMethod()
  {
    return indexedWriteMethod;
  }

  public void setIndexedWriteMethod(String indexedWriteMethod)
  {
    this.indexedWriteMethod = indexedWriteMethod;
  }

  public boolean isIndexed ()
  {
    return true;
  }

}