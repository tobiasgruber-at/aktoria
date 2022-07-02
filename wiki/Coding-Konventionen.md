## Inhaltsverzeichnis

- [Allgemein](#allgemein)
- [IDE Einstellungen](#ide%20einstellungen)
- [Commit Format](#commit%20format)
- [Tests](#tests)

## Allgemein

- Wir orientieren uns bei dem akzeptierten Coding Format an dem Standard Schema von IntelliJ.
- Frontendseitig beziehen wir uns auf den [Angular Style Guide](https://angular.io/guide/styleguide).

## IDE Einstellungen

### Backend (IntelliJ)

1. Unter `Settings > Tools > Actions on Save` sollen folgende Einstellungen aktiviert werden:
   - Reformat code
   - Optimize imports
   - Rearrange code
   - Run code cleanup
2. Unter `Settings > Editor > Code Style > Java` soll unter `Scheme:` das `Default IDE` code style Schema ausgewählt werden.
3. Die Operationen `Reformat Code` oder `Reformat File` sollten regelmäßig, aber vor allem vor einem Commit ausgeführt werden.
4. `Reformat File` kann unter dem Menüpunkt `Code > Reformat File` aufgefunden werden.
5. Standardgemäß sind `Reformat Code` oder `Reformat File` jeweils mit `Strg-Alt-L` und `Strg-Alt-Umschalt-L` aufrufbar.
6. Unter `Settings > Editor > General > On Save` soll `Remove trailing spaces on:` ausgeschaltet sein.
7. `Check Style IDEA` plugin installieren
   - `Settings > Editor > Code Style > Zahnrad neben Scheme > Import > Checkstyle configuration > Checkstyle.xml im Backend Ordern auswählen`
   - `Settings > Tools > Checkstyle` Soll das `Checkstyle.xml` File als Configuration file hinzugefügt werden
   - `Settings > Tools > Checkstyle > Scan Scope:` auf "All files in project" setzen
8. Unter `Settings > Build, Execution, Deployment > Compiler` die Einstellung `Build project automatically` aktivieren. 

### Frontend (IntelliJ oder WebStorm)

1. Unter `Plugins > Marketplace` soll `Prettier` installiert werden (nicht vergessen `Apply` zu klicken)
2. Unter `Settings > Languages & Frameworks > JavaScript > Prettier` soll:
   - Für `Run for files` folgender Wert eingegeben werden: `{**/*,*}.{js,ts,jsx,tsx,html,scss}`
   - `On save` aktiviert werden
3. Unter `Settings > Tools > Actions on Save` sollen folgende Einstellungen aktiviert werden:
   - Reformat code
   - Optimize imports
   - Rearrange code
   - Run code cleanup
   - Run `eslint --fix`

## Commit Format

Für Commits wird folgendes, einheitliches Format genutzt:

`#<issue-id>: <message>`

Durch die Prefix (`#<issue-id>`) erkennt GitLab automatisch die entsprechenden Issues und verlinkt diese.

Commit Messages sind in Englisch, Präsents und einheitlich in Kleinbuchstaben zu schreiben.

## Tests

Coding Konventionen im Rahmen von Tests können [hier](Test-Strategie) nachgelesen werden.