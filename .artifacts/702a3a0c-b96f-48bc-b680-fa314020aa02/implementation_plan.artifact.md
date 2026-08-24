# Actualización de configuración para compilación

Este plan tiene como objetivo corregir los errores de compilación y modernizar la configuración del proyecto para que sea compatible con las versiones más recientes de Android SDK y Kotlin.

## Cambios Propuestos

### Configuración de Gradle y Dependencias

#### [MODIFY] [libs.versions.toml](file:///Users/tayler/Desktop/project/android/PlalValu/gradle/libs.versions.toml)
- Actualizar y agregar definiciones para Hilt (2.52).
- Asegurar que todas las dependencias de Compose usen el BOM.
- Actualizar versiones de Lifecycle y otras bibliotecas base.

#### [MODIFY] [build.gradle.kts](file:///Users/tayler/Desktop/project/android/PlalValu/build.gradle.kts) (Raíz)
- Actualizar el plugin de Hilt a la versión 2.52.

#### [MODIFY] [build.gradle.kts](file:///Users/tayler/Desktop/project/android/PlalValu/app/build.gradle.kts)
- Actualizar `sourceCompatibility`, `targetCompatibility` y `jvmTarget` a Java 17 (requerido para SDK 35).
- Limpiar el bloque de dependencias para usar `libs` de forma consistente.
- Corregir la dependencia de `material3` que estaba forzada a una versión beta antigua.

#### [MODIFY] [gradle-wrapper.properties](file:///Users/tayler/Desktop/project/android/PlalValu/gradle/wrapper/gradle-wrapper.properties)
- Actualizar Gradle a 8.12 para mejorar la compatibilidad con versiones recientes de Java.

## Verificación

### Pruebas Automatizadas
- Ejecutar `./gradlew assembleDebug` para verificar que el proyecto compile correctamente.

### Notas importantes
> [!WARNING]
> El error reportado indica que se está usando **Java 25**, el cual no es soportado oficialmente por Gradle 8.11. Se recomienda configurar el IDE para usar **Java 17 o 21** (JDK 17 es el estándar actual para Android). La actualización a Gradle 8.12 incluida en este plan intenta mitigar esto, pero el cambio de JDK en los ajustes del IDE sigue siendo la solución definitiva.
