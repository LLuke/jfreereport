package org.jfree.report.resourceloader;

public interface ResourceFactoryModule
{
  public boolean canHandleResourceByName (String name);
  public boolean canHandleResourceByContent (byte[] content);
  public int getHeaderFingerprintSize();
}
