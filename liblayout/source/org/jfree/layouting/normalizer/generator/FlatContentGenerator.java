package org.jfree.layouting.normalizer.generator;

/**
 * Breaks down the content into a flat structure. Block content is no longer
 * contained in other block content, but tables and lines can still be nested.
 * <p/>
 * This transformation is needed for plain document output, like the StarWriter
 * export. It creates its own ambugities, as divs with multiple borders cannot
 * be expressed with this system.
 * <p/>
 * So a smart engine would interfere here and would generated nested tables
 * instead.
 * <p/>
 * This implementation seems to be driven by madness - it needs a redesign...
 */
public class FlatContentGenerator
{
  // you see, this class needs some work - its empty!
}
