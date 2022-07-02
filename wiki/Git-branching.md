## Inhaltsverzeichnis

- [Flow](#flow)
- [Bedienung](#bedienung)
- [Beispiel](#beispiel)

## Flow

![gitflow.drawio__4\_](uploads/2c0a7106d699f35352d4516b8556e738/gitflow.drawio__4\_.png)

> Es soll nur auf `feature` branches direkt committed werden! Auf `master` oder `develop` soll nur mittels Merge Requests gemergt werden.

- Zu Beginn eines Sprints wird ein neuer `develop` branch angelegt.
- Zum Entwickeln eines neues Features wird, ausgehend vom `develop` branch, ein neuer `feature/#<issue-id>-<task>` angelegt.
- Soll ein Feature in den `develop` branch gemergt werden, so wird ein neuer `feature-to-develop/#<issue-id>-<task>` branch angelegt, welcher den aktuellen `develop` branch reinmergt, um eventuelle Konflikte lokal aufzulösen. Anschließend wird dieser neue branch im Zuge eines Merge Requests in den `develop` branch gemergt.

## Bedienung

**Feature branch erstellen**

```plaintext
$ git fetch
$ git checkout -b feature/#<issue-id>-<task> origin/develop
```
> Es müssen ggf noch packages installiert werden. Im `/frontend` Verzeichnis mittels `npm i` und im `/backend` Verzeichnis mittels `mvn install`.

**Feature-to-Develop branch erstellen**

```plaintext
$ git fetch
$ git checkout -b feature-to-develop/#<issue-id>-<task> feature/#<issue-id>-<task>
$ git merge origin/develop
```

**Branch pushen**
```plaintext
$ git push origin <branch-name>
```

## Beispiel

Es sollte für das erste Feature [Account anlegen (#19)](https://reset.inso.tuwien.ac.at/repo/2022ss-sepm-pr-group/22ss-sepm-pr-qse-14/-/issues/19 "Account anlegen") das Frontend aufgesetzt werden. Der entsprechende branch wurde [_feature/#19-frontend-setup_](https://reset.inso.tuwien.ac.at/repo/2022ss-sepm-pr-group/22ss-sepm-pr-qse-14/-/tree/feature/%2319-frontend-setup) benannt, da _#19_  `#<issue-id>` entspricht und _frontend-setup_ den (sub)task beschreibt.

Wenn der branch in `develop` gemergt werden soll, würde ein neuer `feature-to-develop/#19-frontend-setup` branch (vom `feature` branch ausgehend) ausgecheckt. Hier wird der `develop` nun reingemergt, um eventuelle Konflikte zu beseitigen. Anschließend wird der branch gepusht und ein Merge Request auf GitLab erstellt.

### Conventionen

- der Namen eines Branches ist in Kleinbuchstaben zu schreiben
- der Namen eines Branches ist in Englisch zu verfassen
- alle Leerzeichen werden durch einen Bindestrich ersetzt
- zu der issue id gehört immer das # dazu

```plaintext
✅
git checkout -b feature/#69-backend-clean-up origin/develop
```

```plaintext
❌
git checkout -b feature/25-frontend-setup origin/develop
git checkout -b feature/25 frontend setup origin/develop
git checkout -b feature/#25 frontend setup origin/develop
```