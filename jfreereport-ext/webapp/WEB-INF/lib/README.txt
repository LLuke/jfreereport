This directory remains empty and is later filled by 
the Ant script when building the WAR file.

The Java-Look and feel guide icons are copied as unzipped
image-jar due to the way, they are read in the servlets.
In the base demos, the JLFGR file must be a Jar file contained
in a ZIP file. When using getServletContext().getResource()
this approach is not suitable, in contrast to the 
ClassLoader.getResource() this does no read any content from
a Jar-File, the Jarfile itself is returned instead.


