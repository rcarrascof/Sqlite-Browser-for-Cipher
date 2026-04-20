# SQLite Browser for Cipher

Explorador de bases de datos SQLite con soporte completo para **SQLCipher**.

## 🏗️ Arquitectura

El proyecto sigue los principios de **Clean Architecture** y patrones de diseño modernos de Android:

*   **UI Declarativa**: Implementado íntegramente con Jetpack Compose y Material Design 3.
*   **MVVM (Model-View-ViewModel)**: Gestión de estado reactiva para separar la lógica de negocio de la interfaz.
*   **Inyección de Dependencias**: Uso de Dagger Hilt para un desacoplamiento robusto entre componentes.
*   **Data Flow**: Comunicación asíncrona mediante Coroutines y StateFlow.
*   **Repository Pattern**: Capa de abstracción para el acceso a datos (SQLCipher).

## 🛠️ Características Principales

*   **Gestión de Archivos**: Acceso total al almacenamiento (MANAGE_EXTERNAL_STORAGE) para navegación libre por el sistema de archivos.
*   **Visualización Dinámica**: Grid de datos con soporte para desplazamiento horizontal/vertical y renderizado de valores NULL.
*   **Consola SQL Interactiva**: Terminal para ejecución de comandos DDL y DML manuales.
*   **Edición en Vivo**: Interacción mediante pulsación larga para modificar registros directamente en la base de datos.
*   **Seguridad**: Diálogo de autenticación integrado para bases de datos cifradas con SQLCipher.

## 📦 Tecnologías y Librerías

*   **Compose**: Material 3, Navigation, Hilt Navigation.
*   **Persistencia**: net.zetetic:android-database-sqlcipher.
*   **Inyección**: Google Hilt.
*   **Async**: Kotlin Coroutines & Flow.

## ⚙️ Especificaciones Técnicas

*   **SDK Mínimo**: API 24 (Android 7.0)
*   **SDK de Compilación**: API 34 (Android 14)
*   **Build System**: Gradle 8.5 con Kotlin DSL y Catalog de versiones.

## 📄 Licencia

MIT License
