/**
 * Date: Jan 13, 2003
 * Time: 6:32:21 PM
 *
 * $Id$
 */
package com.jrefinery.report.io.ext.writer;

import com.jrefinery.report.io.ext.factory.objects.ObjectDescription;

import java.io.Writer;
import java.io.IOException;

public class ObjectWriter extends AbstractXMLDefinitionWriter
{
  private Object baseObject;
  private ObjectDescription objectDescription;

  public ObjectWriter(ReportWriter reportWriter, Object baseObject, ObjectDescription objectDescription)
  {
    super(reportWriter);
    this.baseObject = baseObject;
    this.objectDescription = objectDescription;
  }

  public void write(Writer writer) throws IOException
  {
  }
}
