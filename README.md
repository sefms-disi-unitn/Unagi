Unagi CASE Tool for Zanshin
===========================

This is an RCP application I started developing in the context of [my research on the design of adaptive systems](http://www.inf.ufes.br/~vitorsouza/academia/phd-thesis/), which started when I was a PhD Student at the [University of Trento, Italy](http://ict.unitn.it/). It's supposed to be a CASE (Computer-aided Software Engineering) Tool for the _Zanshin_ approach. I've also developed the _Zanshin_ Framework to experiment with the approach ([see its repository here on github](https://github.com/sefms-disi-unitn/Zanshin)).

_Zanshin_ refers to a "term used in the Japanese martial arts. It refers to a state of awareness" ([Wikipedia](http://en.wikipedia.org/wiki/Zanshin)), which relates to the first part of the _Zanshin_ approach (the elicitation of _Awareness Requirements_, for more information on the _Zanshin_ approach, see some of my [publications](http://disi.unitn.it/~vitorsouza/academia/)). In episode 17 of the 6th season of the American television series [Friends](http://en.wikipedia.org/wiki/Friends), Ross uses "Unagi" instead of "Zanshin" to refer to this concept, hence the name of the CASE Tool.

The development of a CASE Tool for _Zanshin_ has been halted at its early stages, but some of _Unagi's_ code can be used by developers building Eclipse RCP applications as example code in order to get started in their project. This is why this repository is still here.



How to run _Unagi_ in Eclipse Kepler
------------------------------------

Follow [Lars Vogel's instructions on tool installation for e4 development](http://www.vogella.com/articles/EclipseRCP/article.html#tutorial_installation):

1. Visit [The Eclipse Project Downloads page](http://download.eclipse.org/eclipse/downloads/), download version 4.3.1 under _Latest Releases_ for your operating system, unzip it and run Eclipse;

2. Click _Help_ > _Install New Software_, add http://download.vogella.com/kepler/e4tools into the _Work with_ field, press Enter and wait for the packages to load. Select _CSS spy for Eclipse 4_ and _Eclipse 4 core tools_ and install them;

3. After restarting Eclipse, again under _Help_ > _Install New Software_, select _Kepler_ from the _Work with_ combo box, wait for the packages to load and install _Eclipse Git Team Provider_ (under _Collaboration_), _EMF - Eclipse Modeling Framework SDK_ and _OCL End User SDK_ (under _Modeling_);


Then obtain _Unagi_:

1. Click on _File_ > _Import..._, select _Projects from Git_ (under _Git_) and click _Next_;

2. Select _Clone URI_ as a repository source, click _Next_;

3. Fill in the _URI_ field with one of the clone URIs that can be found at this page and click _Next_. If one of them doesn't work, try another one;

4. Select the branches you want to clone (you can leave both _master_ and _develop_ selected). Click _Next_;

5. Select the directory where you'd like to clone the repository (I usually put it under my Eclipse workspace) and click _Next_;

6. Select the option _Import existing projects_ and click _Next_;

7. Finally, click _Finish_.


Finally, run _Unagi_:

1. Open the `unagi.product` file under project `it.unitn.disi.unagi.rcpapp`, go to its "Overview" tab and click the "Launch and Eclipse application" link. The first time you run, you will probably get an error: `java.lang.RuntimeException: No application id has been found.` This is due to Eclipse not adding the required bundles to the run configuration. Let's fix that next;

3. Click on the menu "Run" > "Run Configurations..." and locate the "unagi.product" configuration under the category "Eclipse Application" at the left-hand side of the Run Configurations screen. Click on it to open;

4. Open the "Plug-ins" tab and click on the "Add Required Plug-ins" button many times, untile the number of plug-ins selected (which is reported above the "Validate Plug-ins" button) doesn't change anymore;

5. Click "Apply", then "Run". From now on, do not use the `unagi.product` file to run the application anymore. Instead, use the "Run Configurations" screen or select the "unagi.product" run from your run history. The run history can be accessed via the run button at the toolbar (the green play button) or via the menu "Run" > "Run History".



Contact
-------

Questions, comments, contributions, please contact [me](http://www.inf.ufes.br/~vitorsouza/).
