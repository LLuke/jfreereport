/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/index.html
 * Project Lead:  Thomas Morgner (taquera@sherito.org);
 *
 * (C) Copyright 2000-2004, by Simba Management Limited and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * ------------------------------
 * Treatment.java
 * ------------------------------
 * (C)opyright 2004, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: Treatment.java,v 1.1.2.1 2004/04/05 16:48:50 taqua Exp $
 *
 * Changes 
 * -------------------------
 * 05.04.2004 : Initial version
 *  
 */

package org.jfree.report.demo.form;

import java.util.Date;

public class Treatment
{
  private Date date;
  private String description;
  private String medication;
  private String success;

  public Treatment ()
  {
  }

  public Treatment (final Date date, final String description,
                    final String medication, final String success)
  {
    this.date = date;
    this.description = description;
    this.medication = medication;
    this.success = success;
  }

  public Date getDate ()
  {
    return date;
  }

  public void setDate (final Date date)
  {
    this.date = date;
  }

  public String getDescription ()
  {
    return description;
  }

  public void setDescription (final String description)
  {
    this.description = description;
  }

  public String getMedication ()
  {
    return medication;
  }

  public void setMedication (final String medication)
  {
    this.medication = medication;
  }

  public String getSuccess ()
  {
    return success;
  }

  public void setSuccess (final String success)
  {
    this.success = success;
  }
}
