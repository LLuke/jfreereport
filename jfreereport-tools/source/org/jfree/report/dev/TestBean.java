package org.jfree.report.dev;

import java.awt.event.ActionListener;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeListener;
import javax.swing.event.ChangeListener;


/**
 * If not specified, the beans description is taken from here.
 *
 * @generatedoc true
 * @name TestBean
 * @displayname TestBean (First Test)
 * @shortDescription A nice descriptive text here, please
 * @expert false
 * @hidden false
 * @preferred true
 * @stopClass java.lang.Object
 * @defaultevent propertyChange
 */
public class TestBean
{
  private String aProperty;

  public TestBean() throws IntrospectionException
  {
    BeanInfo bi =
        Introspector.getBeanInfo(TestBean.class.getSuperclass(),
                                 Object.class);
    bi.getMethodDescriptors();
  }

  /**
   *
   * @return
   */
  public String getaProperty()
  {
    return aProperty;
  }

  public void setaProperty(String aProperty)
  {
    this.aProperty = aProperty;
  }


  public String getDisabledProperty()
  {
    return aProperty;
  }

  public void setDisabledProperty(String aProperty)
  {
    this.aProperty = aProperty;
  }

  /**
   *
   * @param p
   * @property write property
   */
  public void SETProperty (int p)
  {

  }

  /**
   *
   * @property read property
   */
  public int GETPROPERTY ()
  {
    return 0;
  }

  /**
   * A property set, that would be included by the default introspector.
   *
   * @param l
   */
  public void addPropertyChangeListener (PropertyChangeListener l)
  {
  }

  public void removePropertyChangeListener (PropertyChangeListener l)
  {
  }

  /**
   * A custom event set. The add and remove methods are
   * named in an invalid way and would not be recognized by the
   * default introspector.
   *
   * @param al
   * @event add action default
   */
  public void IAddActionListener (ActionListener al)
  {
  }

  /**
   * A custom event set. The add and remove methods are
   * named in an invalid way and would not be recognized by the
   * default introspector.
   *
   * @param al
   * @event remove action default
   */
  public void IRemoveActionListener (ActionListener al)
  {
  }

  /**
   * A disabled event set.
   *
   * @param al
   * @event disable change
   */
  public void addChangeListener (ChangeListener al)
  {
  }

  public void removeChangeListener (ChangeListener al)
  {
  }
}
