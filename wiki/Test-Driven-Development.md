## Inhaltsverzeichnis

- [Ablauf des TDD](#ablauf)
- [Automatische Generierung von Tests](#automatische-generierung-von-tests)

## Ablauf des TDD

Grundsätzlich sollten Tests vor der Implementierung der Komponenten definiert und geschrieben werden. Außerdem wird das parallele Schreiben von Testfällen akzeptiert, ist allerdings nicht Best-Practice. Falls dies aufgrund Zeitmangels nicht möglich ist können Tests auch verspätet geschrieben werden.

Der Ablauf des TDD ist in vier Schritten gegliedert:

### Schritt 1

Im ersten Schritt sollte sich überlegt werden, was welche Komponente zu tun hat. Das bedeutet auch, sich konkrete überprüfbare Testfälle zu überlegen und diese zu dokumentieren. Siehe Testfalldokumentation (WIP).

Danach wird zunächst die grobe Struktur der Komponente deklariert aber noch leer gelassen. Danach werden die dazugehörigen Tests implementiert.

### Schritt 2

In der zweiten Phase sollten alle Tests aufgrund der leeren Methoden fehlschlagen.

### Schritt 3

Im dritten Schritt werden die Komponenten ausgefüllt, bzw. überarbeitet wenn bereits Funktionalität vorhandenist, und regelmäßig durch die implementierten Tests getestet. Der dritte Schritt ist erst beendet, wenn alle Tests positiv durchlaufen werden.

### Schritt 4

Im letzten Schritt wird der geschriebene Code optimiert. Dabei müssen auch regelmäßig die Tests durchlaufen werden. Wenn ein Test fehlschlägt wird zum Schritt 3 zurück gesprungen.

## Automatische Generierung von Tests

Um für eine _leere_ Komponente automatisch Tests zu definieren muss der Cursor auf den Namen der Komponente bewegt werden. Durch `Alt-Eingabe` bzw. einem Rechtsklick wird ein Menü aufgerufen, auf dem `Create Tests` ausgewählt werden kann.

Als `Testing Library` soll `JUnit 5` ausgewählt werden. Unter `Generate test methods for:` sollen alle Methoden ausgewählt werden.

Details über die Implementierung sowie Konventionen sind [hier](Test-Strategie) zu finden.