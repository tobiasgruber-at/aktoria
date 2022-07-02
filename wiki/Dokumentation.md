## Inhaltsverzeichnis

- [Javadoc](#javadoc)
- [Branching](#branching)
- [Commits](#commits)

## Javadoc

Für die Quellcode Dokumentation wird [Javadoc](https://www.oracle.com/technical-resources/articles/java/javadoc-tool.html) verwendet.

### Schreibweise

Die Javadoc Dokumentation ist in der Dritten Person zu schreiben.
```
✅ Gets the data.
```
```
❌ Get the data.
```

Wenn auf eine Methode verwiesen wird, dann wird nur der Name der Methode angeführt, ohne Klammern.
```
✅ foo
```
```
❌ foo()
```
Wenn die Methode überladen ist, und eine **bestimmte** überladene Methode referenziert wird, so werden zusätzlich noch die Parametertypen angegeben.
```
✅ foo(int, int, String)
```
```
❌ foo()
❌ foo
```

Nachdem Methoden meistens eine bestimmte Operation bearbeiten, starten Methodenbeschreibungen mit einem Verb.
```
✅ Gets the data needed for ...
```
```
❌ This method gets the data needed for ...
❌ In this method ...
```

Wenn eine Instanz von der Klasse, in der die Dokumentation steht, referenziert wird, verwende _this_.
```
✅ Gets the data for this component ...
```
```
❌ Gets the data for the component ...
```

Methodenbeschreibungen sollten nicht nur dieselbe Information wie der Methodenname an sich haben. Dokumentation soll dem Lesenden mehr Informationen bieten.
```
✅
/**
* Registers the text to display in a tool tip.   The text 
* displays when the cursor hovers over the component.
*
* @param text  the string to display.  If the text is null, 
*              the tool tip is turned off for this component.
*/
public void setToolTipText(String text) {
```
```
❌
/**
* Sets the tool tip text.
*
* @param text  the text of the tool tip
*/
public void setToolTipText(String text) {
```

Wer sich hier noch weiter einlesen möchte: [Styleguide](https://www.oracle.com/technical-resources/articles/java/javadoc-tool.html#styleguide)

### Reihenfolge und verpflichtende Javadoc Tags

- `@author` (nur bei Klassen und Interfaces)
- `@param` (nur bei Methoden und Konstruktoren)
- `@return` (nur bei Methoden)
- `@throws` (nur bei Methoden)
- `@see` (wenn auf eine andere Klasse, Interface oder Methode verwiesen wird)

### Formatierung

Eine Zeile sollte höchstens in etwa 80 Zeichen lang sein.

Um Absätze in der Dokumentation zu generieren verwende `<p>`
```
✅
/**
* Lorem ipsum dolor sit amet, consetetur sadipscing elitr,
* sed diam nonumy eirmod tempor invidunt ut labore et dolore 
* magna aliquyam erat, sed diam voluptua. At vero eos et accusam et.
* 
* <p>Lorem ipsum dolor sit amet, consetetur sadipscing elitr,
* sed diam nonumy eirmod tempor invidunt ut labore et dolore 
* magna aliquyam erat, sed diam voluptua. At vero eos et accusam et.
*
```
```
❌
/**
* Lorem ipsum dolor sit amet, consetetur sadipscing elitr,
* sed diam nonumy eirmod tempor invidunt ut labore et dolore 
* magna aliquyam erat, sed diam voluptua. At vero eos et accusam et.
* 
* Lorem ipsum dolor sit amet, consetetur sadipscing elitr,
* sed diam nonumy eirmod tempor invidunt ut labore et dolore 
* magna aliquyam erat, sed diam voluptua. At vero eos et accusam et.
*

/**
* Lorem ipsum dolor sit amet, consetetur sadipscing elitr,
* sed diam nonumy eirmod tempor invidunt ut labore et dolore 
* magna aliquyam erat, sed diam voluptua. At vero eos et accusam et.
* <p>
* Lorem ipsum dolor sit amet, consetetur sadipscing elitr,
* sed diam nonumy eirmod tempor invidunt ut labore et dolore 
* magna aliquyam erat, sed diam voluptua. At vero eos et accusam et.
*

/**
* <p>
* Lorem ipsum dolor sit amet, consetetur sadipscing elitr,
* sed diam nonumy eirmod tempor invidunt ut labore et dolore 
* magna aliquyam erat, sed diam voluptua. At vero eos et accusam et.
* </p>
* <p>
* Lorem ipsum dolor sit amet, consetetur sadipscing elitr,
* sed diam nonumy eirmod tempor invidunt ut labore et dolore 
* magna aliquyam erat, sed diam voluptua. At vero eos et accusam et.
* </p>
*
```

Aufzählungen werden mit `<ul>` und `<li>` gemacht.
```
✅
/**
* Lorem ipsum dolor sit amet, consetetur sadipscing elitr.
* <ul>
* <li> Lorem
* <li> ipsum 
* <li> dolor
* </ul>
*
```
```
❌
/**
* Lorem ipsum dolor sit amet, consetetur sadipscing elitr.
* - Lorem
* - ipsum 
* - dolor
*
```

## Branching

Ein neuer Feature Branch wird in folgender Form benannt:
```
feature/#<issue-id>-<issue name> 
```


## Commits

Ein Commit wird in folgender Form benannt:
```
#<issue id>: <changes>
```
