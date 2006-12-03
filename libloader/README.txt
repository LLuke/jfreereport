************************
* LIBLOADER 0.2.0      *
************************

03 Dec 2006

1. INTRODUCTION
---------------
LibLoader is a general purpose resource loading framework. It has been
designed to allow to load resources from any physical location and to
allow the processing of that content data in a generic way, totally
transparent to the user of that library.

For the latest news and information about LibLoader, please refer to:

    http://jfreereport.pentaho.org/libloader/


2. Requirements
---------------
LibLoader needs at least JDK 1.2.2 for its core functionality.

The EHCache support requires at least JDK 1.4. This module is therefore not
included by default. It can be built manually using the supplied ANT-script.


3. CHANGES
----------

0.2.0:  (03-Dec-2006)
        This is the first real release version of LibLoader. At this
        point this library has some primitive caching and does a good
        job at loading resources.

0.1.5:  (26-Sep-2006)
0.1.4:  (31-Aug-2006)
0.1.3:  (30-Jul-2006)
0.1.2:  (30-May-2006)
        BugFixes and more active development of that library.

0.1.1:  (30-Apr-2006)
        BugFixes for derived keys, resource factories are fully
        initialized from the global config.

0.1.0:  (17-Apr-2006)
        Initial version.

