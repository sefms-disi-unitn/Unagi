Unagi CASE Tool for Zanshin
===========================

This is an RCP application I'm slowly developing in the context of my research on the design of adaptive systems, which started when I was a PhD Student at the [University of Trento, Italy](http://ict.unitn.it/). It's supposed to be a CASE (Computer-aided Software Engineering) Tool for the _Zanshin_ approach. I've also developed the _Zanshin_ Framework to experiment with the approach ([see its repository here on github](https://github.com/sefms-disi-unitn/Zanshin)).

_Zanshin_ refers to a "term used in the Japanese martial arts. It refers to a state of awareness" ([Wikipedia](http://en.wikipedia.org/wiki/Zanshin)), which relates to the first part of the _Zanshin_ approach (the elicitation of _Awareness Requirements_, for more information on the _Zanshin_ approach, see some of my [publications](http://disi.unitn.it/~vitorsouza/academia/)). In episode 17 of the 6th season of the American television series [Friends](http://en.wikipedia.org/wiki/Friends), Ross uses "Unagi" instead of "Zanshin" to refer to this concept, hence the name of the CASE Tool.



Pre-requisites for running Unagi
--------------------------------

This is an Eclipse RCP application. Each folder in the root of the repository is a different Eclipse project. If you want to contribute or just run it, you should install Eclipse Juno for RCP developers and use its Git support (EGit) to clone the repository.

Unagi is being developed using E4 (the new set of technologies for plug-in development introduced in Eclipse 4). After you install Eclipse Juno you should, therefore, follow the instructions for Eclipse E4 development from (Lars Vogel's tutorial)[http://www.vogella.com/articles/EclipseRCP/article.html]:

1. Add the following Update Site to the list of Eclipse repositories: `http://download.eclipse.org/e4/updates/0.12`;

2. Install two items from this repository: _E4 CSS Spy_ and _Eclipse e4 Tools_.


Finally, you will also need the following plug-in from the Eclipse Juno repository (if not already installed):

- EMF - Eclipse Modeling Framework SDK


Running the Tool
----------------

To run Unagi, follow the steps below:

1. Open the `unagi.product` file under project `it.unitn.disi.unagi.rcpapp`, go to its "Overview" tab and click the "Launch and Eclipse application" link;

2. The first time you run, you will probably get an error: `java.lang.RuntimeException: No application id has been found.` This is due to Eclipse not adding the required bundles to the run configuration. Let's fix that next;

3. Click on the menu "Run" > "Run Configurations..." and locate the "unagi.product" configuration under the category "Eclipse Application" at the left-hand side of the Run Configurations screen. Click on it to open;

4. Open the "Plug-ins" tab and click on the "Add Required Plug-ins" button many times, untile the number of plug-ins selected (which is reported above the "Validate Plug-ins" button) doesn't change anymore;

5. Click "Apply", then "Run". From now on, do not use the `unagi.product` file to run the application anymore. Instead, use the "Run Configurations" screen or select the "unagi.product" run from your run history. The run history can be accessed via the run button at the toolbar (the green play button) or via the menu "Run" > "Run History".



Note for Ubuntu (and Unity) users
---------------------------------

I'm not sure if this is still applicable (I haven't developed this project in Ubuntu for a while now), but if you use Ubuntu 11.04 with Unity, set environment variable UBUNTU_MENUPROXY=0 before running Eclipse, otherwise the application menu will now show (might also happen with other distros that use Unity. If you changed to gnome-shell/Gnome 3 or classic/Gnome 2, you're probably safe).



Contact
-------

Questions, comments, contributions, please contact me.
