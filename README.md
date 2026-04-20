# SQLite Browser for Cipher (Modernized)

Una aplicación Android potente y moderna para explorar bases de datos SQLite, con soporte completo para **SQLCipher**.

Este proyecto ha sido totalmente modernizado desde una base legacy de Java a una arquitectura contemporánea de producción.

## 🚀 Características Modernas

*   **UI 100% Jetpack Compose**: Interfaz declarativa, fluida y basada en Material Design 3.
*   **Kotlin First**: Código 100% escrito en Kotlin siguiendo las mejores prácticas.
*   **Clean Architecture**: Separación clara de capas (Data, Domain, UI) para máxima mantenibilidad.
*   **MVVM + Flow**: Gestión de estado reactiva y asíncrona mediante Coroutines y StateFlow.
*   **Hilt Inyection**: Inyección de dependencias robusta para desacoplamiento total.
*   **Acceso Total a Archivos**: Integración con `MANAGE_EXTERNAL_STORAGE` para navegar por el dispositivo sin las restricciones de Scoped Storage.
*   **SQLCipher 4**: Soporte nativo para bases de datos cifradas con cifrado de grado militar.

## 🛠️ Funcionalidades

*   **Explorador de Archivos**: Abre cualquier base de datos `.db` o `.sqlite` en tu dispositivo.
*   **Visor de Tablas**: Lista todas las tablas y esquemas de la base de datos abierta.
*   **Data Grid Dinámico**: Visualiza los datos en una tabla desplazable con soporte para valores NULL y BLOB.
*   **Consola SQL (Interactive)**: Ejecuta cualquier comando SQL manual (`SELECT`, `UPDATE`, `INSERT`, `DELETE`) directamente desde la app.
*   **Edición Manual**: Mantén presionado cualquier registro en el grid para editar sus valores de forma instantánea.
*   **Soporte Cipher**: Abre bases de datos cifradas proporcionando la contraseña mediante un diálogo seguro.

## 🏗️ Requisitos de Compilación

*   **Android Studio Jellyfish** (o superior)
*   **JDK 21**
*   **Gradle 8.5**
*   **SDK Mínimo**: API 24 (Android 7.0)
*   **SDK de Compilación**: API 34 (Android 14)

## 📦 Tecnologías Utilizadas

*   Jetpack Compose (Material 3)
*   Dagger Hilt
*   Kotlin Coroutines & Flow
*   SQLCipher for Android (net.zetetic)
*   Android Navigation Compose

## 📄 Licencia

Este proyecto se distribuye bajo los términos del desarrollador original. Modernizado en 2026.
