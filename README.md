# LoMorph

**LoMorph** is a lightweight annotation processor for Java that automatically generates getter and/or setter interfaces based on your data classes.

<p align="center">
<a href="https://github.com/web-morph/lomorph?tab=LGPL-3.0-1-ov-file"><img alt="License" src="https://img.shields.io/github/license/web-morph/lomorph"></a>
<a href="https://docs.gradle.org/8.14/release-notes.html"><img src="https://img.shields.io/badge/Gradle-8.14-brightgreen.svg?colorB=469C00&logo=gradle"></a>
<a href="https://repo.jyraf.com/service/rest/v1/search/assets/download?sort=version&repository=maven-releases&maven.groupId=com.github.webmorph&maven.artifactId=lomorph&maven.extension=jar&maven.classifier=" target="_blank"><img alt="Download" src="https://img.shields.io/nexus/r/com.github.webmorph/lomorph?server=https%3A%2F%2Frepo.jyraf.com"></a>
</p>

---

## ⚙️ Requirements

* Java 17 or above

---

## ✨ Features

- Generates `XGetter` / `XSetter` / `XApi` interfaces based on your `X` class
- Skips `final`, `static`, `transient` and explicitly ignored (`@LoIgnore`) fields
- Supports generating getters, setters, or both
- Runs at compile-time (Java Annotation Processing API)

---

## 🔧 Example

```java
@LoApi
@LoGetter
@LoSetter
public class User {
    private final int id;
    private String name;

    @LoIgnore
    private String secret;
}
```

### 👉 Generates:
```java
public interface UserApi {
    int getId();
    String getName();
    void setName(String name);
}
public interface UserGetter {
    int getId();
    String getName();
}

public interface UserSetter {
    void setName(String name);
}
```



## 📦 Installation

⚙️ Gradle (Kotlin DSL – build.gradle.kts)

```kts
repositories {
    maven("https://repo.jyraf.com/repository/maven-public/")
}

dependencies {
    compileOnly("com.github.webmorph:lomorph:<version>")
    annotationProcessor("com.github.webmorph:lomorph:<version>")
}
```

⚙️ Gradle (Groovy DSL – build.gradle)

```groovy
repositories {
    maven {
        url 'https://repo.jyraf.com/repository/maven-public/'
    }
}

dependencies {
    compileOnly "com.github.webmorph:lomorph:<version>"
    annotationProcessor "com.github.webmorph:lomorph:<version>"
}
```

# 🛠️ Contributing

Contributions are welcome! Feel free to open an issue or submit a pull request.

## 🧍 Author

### [CKATEPTb](https://github.com/CKATEPTb)
