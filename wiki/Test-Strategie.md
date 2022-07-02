## Inhaltsverzeichnis 

- [Test Bibliothek](#test-bibliothek)
- [Test Methoden Konventionen](#test-methoden-konventionen)
- [Beispiel](#beispiel)

## Test Bibliothek

Es wird die Test Bibliothek JUnit 5 verwendet. Genauere Informationen können [hier](https://junit.org/junit5/docs/current/user-guide/#overview) nachgelesen werden.

In der übergeordneten Test Klasse müssen folgende Annotationen eingefügt werden:
```
@ActiveProfiles({ "test", "datagen" })
@SpringBootTest
```
Falls außerdem Mock Tests durchgeführt werden sollen (Beispielsweise für REST Tests) müssen außerdem Folgende Annotationen und  Zeilen eingefügt werden:
```
@EnableWebMvc
@WebAppConfiguration

```
```
@Autowired
private WebApplicationContext webAppContext;
private MockMvc mockMvc;

@BeforeEach
public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
}
```

## Test Methoden Konventionen

### Reihenfolge

Die Reihenfolge der Testmethoden darf nicht angenommen oder fixiert werden. Jeder Test soll unabhängig voneinander und eigenständig ablaufen können. Wenn es wirklich nicht anders geht, können passende Annotationen verwendet werden, um beispielsweise eine Methode vor jedem Test auszuführen.

### Annotationen

### `@DisplayName`

Test Methoden müssen mit einer `@DisplayName` Annotation versehen werden. Der Displayname beschreibt den Test genauer und kann Sonderzeichen wie beispielsweise Abstände beinhalten. [Dokumentation](https://junit.org/junit5/docs/current/user-guide/#writing-tests-display-names)
```
✅
@Test
@DisplayName("getById() returns the correct object")
public void getById() throws Exception {

@Test
@DisplayName("Getting the object by its id")
public void getById() throws Exception {

@Test
@DisplayName("Custom test name containing spaces")
void testWithDisplayNameContainingSpaces() {
}
```
```
❌
@Test
@DisplayName("getById()")
public void getById() throws Exception {
```

### `@Transactional`

Test Methoden welche die Datenbank verändern müssen mit der Annotation `@Transactional` versehen werden.

### `@Nested`

Falls Testmethoden gruppiert werden sollen, kann das mit einer inneren, mit der Annotation `@Nested` versehenen, Klasse getan werden. [Dokumentation](https://junit.org/junit5/docs/current/user-guide/#writing-tests-nested)

### `@Disabled`

Kurzzeitig nicht benötigte Test Methoden sollen durch die Annotatione `@Disabled` ausgeschalten werden.

### `@ParameterizedTest`

Parameterisierte Tests sollten im Namen den Index sowie getesteten Wert angegeben haben, sofern dies möglich ist bzw. Sinn macht. Bsp:
```
✅
@ParameterizedTest(name = "[{index}] value = {0}")
```

Genaueres über JUnit 5 Annotationen kann [hier](https://junit.org/junit5/docs/current/user-guide/#writing-tests-annotations) nachgeschlagen werden.

### Beispiel

Die [Dokumentation für eine verschachtelte Test-Struktur](https://junit.org/junit5/docs/current/user-guide/#writing-tests-nested) ist ein gutes Beispiel für den Einsatz von der Annotation `@DisplayName` sowie für übersichtliche Tests.

