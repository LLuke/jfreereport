package org.jfree.resourceloader.loader.file;

import org.jfree.resourceloader.loader.AbstractURLResourceLoader;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: 08.12.2006
 * Time: 08:58:26
 * To change this template use File | Settings | File Templates.
 */
public class FileUrlResourceLoader extends AbstractURLResourceLoader
{
  public FileUrlResourceLoader()
  {
  }

  public String getSchema()
  {
    return "file"; 
  }
}
