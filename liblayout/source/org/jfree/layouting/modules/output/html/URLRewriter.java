package org.jfree.layouting.modules.output.html;

import org.jfree.repository.ContentEntity;

/**
 * This service takes a generated content location and rewrites it into an
 * absolute or relative URL.
 *
 * @author Thomas Morgner
 */
public interface URLRewriter
{
  public String rewrite (ContentEntity content, ContentEntity dataEntity);
}
