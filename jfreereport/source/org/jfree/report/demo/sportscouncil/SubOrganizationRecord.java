package org.jfree.report.demo.sportscouncil;

public class SubOrganizationRecord extends Record
{
  private String name;
  private String email;
  private int maleGenderCount;
  private int femaleGenderCount;

  public SubOrganizationRecord (final String orgID,
                                final String name, final String email,
                                final int maleGenderCount,
                                final int femaleGenderCount)
  {
    super("org", orgID);
    this.name = name;
    this.email = email;
    this.maleGenderCount = maleGenderCount;
    this.femaleGenderCount = femaleGenderCount;
  }

  public String getEmail ()
  {
    return email;
  }

  public int getFemaleGenderCount ()
  {
    return femaleGenderCount;
  }

  public int getMaleGenderCount ()
  {
    return maleGenderCount;
  }

  public String getName ()
  {
    return name;
  }
}
