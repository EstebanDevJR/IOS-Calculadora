# Calculadora iOS - Android

Calculadora estilo iOS desarrollada con Kotlin y Jetpack Compose.

## Características

- Diseño similar a la calculadora de iOS
- Operaciones básicas: suma, resta, multiplicación, división
- Porcentaje y cambio de signo
- Historial de operaciones
- Pantalla con scroll horizontal para números largos
- Manejo de errores (división por cero)

## Arquitectura

- **Model**: `CalculatorModel` - operaciones matemáticas básicas
- **ViewModel**: `CalculatorViewModel` - lógica de la calculadora y estado
- **View**: `CalculatorScreen` y `HistoryScreen` - interfaz de usuario

## Tecnologías

- Kotlin
- Jetpack Compose
- Material 3
- Navigation Compose
- ViewModel

## Instalación

1. Clona el repositorio
2. Abre el proyecto en Android Studio
3. Ejecuta en un emulador o dispositivo Android
