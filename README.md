<div align="center">

![The Logo](https://raw.githubusercontent.com/justinwoodring/DFAify/main/src/main/resources/com/justinwoodring/dfaify/dfaify.png)

<h1>DFAify</h1>
</div>

## About

:wave: Hi there! I'm Justin Woodring! 

I built DFAify as a hobby project to allow users to model and inspect the behaviour of basic DFAs.
DFAify is a diagramming and graphical analysis tool supporting playback and stepping logic for DFAs.
  
![image](https://github.com/JustinWoodring/DFAify/assets/41842051/147d750d-b49f-4b29-b46f-af53240d35e1)

### Consider Supporting Me
If you really love DFAify consider supporting me!  [:dollar: paypal.me/jwoodrg ](https://paypal.me/jwoodrg)

## Getting Started
If you just want to use DFAify, I recommend taking a look at the releases. 

But if you're curious here's how you build from the source:
1. Install JDK 17, I recommend Adoptium's builds.
2. Clone this repo.
3. Run `./gradlew run` on Linux and Mac or `.\gradlew.bat run` on Windows
4. Done! :partying_face: 

## Writing your own DFAs
DFAs are expressed in a very simple xml format. Here's a sample:
```xml
<dfa>
  <state name="EVEN A" ref="even" entry="true" final="true">
    <conn to="odd" takes="a"/>
    <conn to="even" takes="b"/>
  </state>
  <state name="ODD A" ref="odd" entry="false" final="false">
    <conn to="even" takes="a"/>
    <conn to="odd" takes="b"/>
  </state>
</dfa>
```

Rules:
* A DFA XML documents begins with a `<dfa>` and ends with a `</dfa>`
* A DFA must have at least one `<state>`.
* Each `<state>` must be an child of the dfa root element.
* Each `<state>` has a `name` attribute which will be displayed when they are rendered.
* Each `<state>` has a `ref` attribute which is how you will reference other states in their connections or `<conn>` tags.
  * `ref` attributes must hold unique values.
* Each `<state>` has a `entry` attribute which is boolean flag indicating whether that state is the DFA's entry point.
  * There must one and only one `entry` attribute flagged as `true`
* Each `<state>` has a `final` attribute declaring whether that state is a final state or not.
  * You can any number of final states including zero.
* Each `<conn>` tag must be a child of a state element.
* Each `<conn>` has a `to` attribute which takes the value of a `ref` attribute on a `<state>`
* Each `<conn>` has a `takes` attribute takes a character which represents the transition character to another `<state>`
  * Each `<conn>` cannot have another sibling `<conn>` which has the same `takes` attribute.
* All attributes are not optional.

Save this file with an `.xml` extension. And open it up in DFAify.

