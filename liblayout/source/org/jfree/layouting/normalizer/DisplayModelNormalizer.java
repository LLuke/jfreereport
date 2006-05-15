package org.jfree.layouting.normalizer;

import java.util.ArrayList;
import java.util.Stack;

import org.jfree.layouting.input.style.keys.box.DisplayModel;
import org.jfree.layouting.input.style.keys.box.DisplayRole;
import org.jfree.layouting.input.style.keys.box.Floating;
import org.jfree.layouting.model.LayoutElement;
import org.jfree.layouting.model.LayoutTextNode;
import org.jfree.layouting.model.box.BoxSpecification;
import org.jfree.layouting.normalizer.common.display.BlockCompositionStrategy;
import org.jfree.layouting.normalizer.common.display.CompositionStrategy;
import org.jfree.layouting.normalizer.common.display.ContentBox;
import org.jfree.layouting.normalizer.common.display.ContentFlow;
import org.jfree.layouting.normalizer.common.display.ContentNode;
import org.jfree.layouting.normalizer.common.display.ContentRoot;
import org.jfree.layouting.normalizer.common.display.ContentText;
import org.jfree.layouting.normalizer.common.display.ExternalContent;
import org.jfree.layouting.normalizer.common.display.InlineCompositionStrategy;

public class DisplayModelNormalizer implements Normalizer
{
  private Stack stack;
  private ContentGenerator contentGenerator;

  public DisplayModelNormalizer (final ContentGenerator contentGenerator)
  {
    if (contentGenerator == null)
    {
      throw new NullPointerException();
    }
    this.contentGenerator = contentGenerator;
    this.stack = new Stack();
  }

  public ContentGenerator getContentGenerator ()
  {
    return contentGenerator;
  }

  protected ContentBox getCurrentContext ()
//          throws CloneNotSupportedException
  {
    ContentBox currentBox = (ContentBox) stack.peek();
    if (currentBox.isFinished() == false)
    {
      return currentBox;
    }

//    throw new IllegalStateException("The current context is already finished");

    // recurse down the stack, until we find a non-finished element
    // this is our block context. We 'pop' all elements from the stack
    // into a separate list.
    try
    {
      final ArrayList contexts = new ArrayList();

      while (currentBox.isFinished())
      {
        // remove the known finished element from the stack ...
        stack.pop();
        // but add it to our list of elements that should be reopened
        contexts.add(currentBox);
        // now look at the next one...
        currentBox = (ContentBox) stack.peek();
      }

      // From there we go up, re-adding new elements.
      for (int i = contexts.size() - 1; i >= 0; i -= 1)
      {
        final ContentBox contentBox = (ContentBox) contexts.get(i);
        final ContentBox derived = contentBox.derive();
        currentBox.add(derived);
        stack.push(derived);
        //contentGenerator.nodeStarted(derived);
        currentBox = derived;
      }

      // hey, incidentally, this is a similiar structure to what the
      // openoffice guys do. So I cant be totally wrong here .
      return currentBox;
    }
    catch (Exception e)
    {
      throw new NullPointerException();
    }
  }

  public void startElement (LayoutElement element)
  {
    ContentBox currentBox = getCurrentContext();

    final BoxSpecification boxSpecification = element.getLayoutContext()
            .getBoxSpecification();
    if (boxSpecification.getFloating() == Floating.NONE)
    {
      CompositionStrategy cs;
      final DisplayModel displayModel =
              boxSpecification.getDisplayModel();
      if (displayModel == DisplayModel.INLINE_INSIDE)
      {
        cs = new InlineCompositionStrategy();
      }
      else
      {
        cs = new BlockCompositionStrategy();
      }
      ContentBox newBox = new ContentBox(element, cs);
      currentBox.add(newBox);

      stack.push(newBox);
    }
    else
    {
      // display model is always block here. This is fixed and defined.
      ContentFlow newBox = new ContentFlow(element);
      newBox.setParent(currentBox);

      getContentGenerator().flowStarted(newBox);
      getContentGenerator().nodeStarted(newBox);
      stack.push(newBox);
    }
  }

  public void addText (LayoutTextNode text)
  {
    ContentBox currentBox = getCurrentContext();
    ContentText node = new ContentText(text);
    currentBox.add(node);
  }

  public void endElement (final LayoutElement element)
  {
    ContentBox cb = (ContentBox) stack.pop();
    cb.markFinished();

    if (cb instanceof ContentFlow)
    {
      getContentGenerator().flowFinished(cb);
    }
  }

  public void startDocument ()
  {
    ContentRoot rootBox = new ContentRoot();
    rootBox.setGenerator(contentGenerator);
    stack.push(rootBox);
    contentGenerator.documentStarted();
  }

  public void endDocument ()
  {
    //print();
    stack.pop();
    if (stack.isEmpty() == false)
    {
      throw new IllegalStateException();
    }
    contentGenerator.documentFinished();
  }

  public void addReplacedElement (LayoutElement element)
  {
    ContentBox currentBox = getCurrentContext();

    final DisplayRole displayRole =
            element.getLayoutContext().getBoxSpecification().getDisplayRole();
    ExternalContent newBox = new ExternalContent(element, displayRole);
    currentBox.add(newBox);
  }

  protected ContentBox getRoot ()
  {
    if (stack.isEmpty())
    {
      return null;
    }
    return (ContentBox) stack.get(0);
  }


  public void print ()
  {
    ContentBox root = getRoot();
    print(root);
  }

  private void print (ContentBox box)
  {
    System.out.println("Start: " + box + " (Finished=" + box.isFinished());
    ContentNode[] cns = box.getElements();
    for (int i = 0; i < cns.length; i++)
    {
      ContentNode cn = cns[i];
      if (cn instanceof ContentText)
      {
        ContentText text = (ContentText) cn;
        System.out.println("Text: " + text.getLayoutElement());
      }
      else if (cn instanceof ContentBox)
      {
        print((ContentBox) cn);
      }
      else
      {
        System.out.println("AWRASDASKLDJLK");
      }
    }
    System.out.println("End  : " + box);
  }

}
