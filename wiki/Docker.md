# Getting started

- Um mit Docker arbeiten zu können muss Docker heruntergeladen und installiert werden. Den Download kann man auf der [offiziellen Seite von Docker](https://www.docker.com/products/docker-desktop/) oder in der [Docker-Dokumentation](https://docs.docker.com/engine/install/) finden.
- Nach der Installation im BIOS überprüfen ob Virtualization enabled ist.
- Docker starten

# Flow

## Datenbank-Start

**Datenbank mithilfe von Docker starten**
```plaintext
\22ss-sepm-pr-qse-14\backend>docker-compose up
```

**Datenbank Container wieder schließen**
```plaintext
\22ss-sepm-pr-qse-14\backend>docker-compose down
```

## Prod-Start [DEPRECATED]

**Backend und Datenbank mithilfe von Docker starten**
```plaintext
\22ss-sepm-pr-qse-14\backend> mvn clean package -DskipTests
\22ss-sepm-pr-qse-14\backend> cp target/backend-0.0.1-SNAPSHOT.jar .
\22ss-sepm-pr-qse-14\backend> docker-compose -f docker-compose.yml -f docker-compose.prod.yml up
```

> `mvn clean package` kann auch ohne `-DskipTests` ausgeführt werden, allerdings wird keine `.jar` erstellt, wenn die Tests fehlschlagen.

**Backend und Datenbank Container wieder schließen**
```plaintext
CTRL-C
\22ss-sepm-pr-qse-14\backend> docker-compose -f docker-compose.yml -f docker-compose.prod.yml down
```

**Backend-Container löschen** _(wenn bereits ein Docker-Container existiert)_

Wenn man eine neue `.jar` fürs Backend ausprobieren möchte, muss zuerst der alte Docker-Container gelöscht werden.
```plaintext
\22ss-sepm-pr-qse-14\backend> docker rmi backend
```
> Die restlichen Schritte sind wie oben beschrieben. Hier geht es nur darum den existierenden Container von Docker wieder zu löschen, damit er neu initialisiert werden kann mit der neuen `.jar`. Ohne dies würde Docker sonst einfach immer den alten Container mit der alten `.jar` starten.

**Datenbank-Container löschen**
```plaintext
\22ss-sepm-pr-qse-14\backend> docker rmi postgres
```
> Ist im Normalfall nicht notwendig, falls man aber wirklich alles neustarten möchte, kann auch der Datenbank-Container komplett neu initialisiert werden.

# Beispiel

Man möchte das Backend und die Datenbank zum ersten Mal starten.
```plaintext
\22ss-sepm-pr-qse-14\backend> mvn clean package -DskipTests
\22ss-sepm-pr-qse-14\backend> cp target/backend-0.0.1-SNAPSHOT.jar .
\22ss-sepm-pr-qse-14\backend> docker-compose -f docker-compose.yml -f docker-compose.prod.yml up
```
Nun möchte man das Backend mit einer neuen `.jar` starten.
```plaintext
\22ss-sepm-pr-qse-14\backend> docker-compose down
\22ss-sepm-pr-qse-14\backend> docker rmi backend
\22ss-sepm-pr-qse-14\backend> mvn clean package -DskipTests
\22ss-sepm-pr-qse-14\backend> cp target/backend-0.0.1-SNAPSHOT.jar .
\22ss-sepm-pr-qse-14\backend> docker-compose -f docker-compose.yml -f docker-compose.prod.yml up
```