# Mi Salud Wear 🏋️‍♀️

Una aplicación completa de seguimiento de salud y bienestar diseñada específicamente para Wear OS, que te ayuda a mantener un estilo de vida saludable directamente desde tu muñeca.

## 📱 Características Principales

### 🚶 Seguimiento de Pasos
- **Conteo automático de pasos** utilizando sensores del dispositivo
- **Metas personalizables** (5,000 - 20,000 pasos)
- **Progreso visual** con indicadores circulares animados
- **Estadísticas detalladas**: calorías estimadas, distancia recorrida
- **Celebraciones automáticas** al alcanzar metas

### 💧 Control de Hidratación
- **Registro fácil** de vasos de agua consumidos
- **Metas personalizables** (4-15 vasos por día)
- **Seguimiento de tiempo** del último vaso consumido
- **Conversión automática** a litros
- **Recordatorios inteligentes** basados en el tiempo

### 🔔 Recordatorios de Actividad
- **Recordatorios personalizables** para mantenerte activo
- **Detección de inactividad** prolongada
- **Vibración suave** para notificaciones discretas
- **Control de activación/desactivación** desde configuración

### 📊 Dashboard Intuitivo
- **Resumen diario** de todas las métricas
- **Acciones rápidas** para registro instantáneo
- **Navegación fluida** entre pantallas
- **Interfaz optimizada** para pantallas circulares y cuadradas

## 🏗️ Arquitectura Técnica

### 📂 Estructura del Proyecto

```
app/
├── src/main/java/com/example/miappwear/
│   ├── data/
│   │   ├── HealthData.kt           # Modelos de datos de salud
│   │   └── HealthRepository.kt     # Repositorio con DataStore
│   ├── presentation/
│   │   ├── components/             # Componentes UI reutilizables
│   │   ├── screens/               # Pantallas de la aplicación
│   │   └── theme/                 # Tema personalizado
│   ├── services/
│   │   └── StepCounterService.kt  # Servicio de conteo de pasos
│   ├── viewmodel/
│   │   └── HealthViewModel.kt     # ViewModel principal
│   ├── complication/              # Complicaciones para watchface
│   └── tile/                      # Tiles del sistema
└── src/main/res/                  # Recursos de la aplicación
```

### 🛠️ Tecnologías Utilizadas

- **Kotlin** - Lenguaje de programación principal
- **Jetpack Compose for Wear OS** - UI moderna y declarativa
- **Coroutines & Flow** - Programación asíncrona reactiva
- **DataStore Preferences** - Almacenamiento persistente de datos
- **Health Services** - Integración con sensores de salud
- **Navigation Component** - Navegación entre pantallas
- **Wear OS Complications** - Integración con watchfaces
- **Wear OS Tiles** - Información rápida en el sistema

### 📊 Gestión de Datos

#### Persistencia Local
- **DataStore Preferences** para configuración y métricas diarias
- **Reseteo automático** de datos diarios a medianoche
- **Almacenamiento eficiente** sin bases de datos complejas

#### Sensores Utilizados
- **TYPE_STEP_COUNTER** - Conteo acumulativo desde el arranque
- **TYPE_STEP_DETECTOR** - Detección individual de pasos (fallback)
- **Vibrator** - Feedback háptico para interacciones

## 🚀 Instalación y Configuración

### Prerrequisitos
- Android Studio Arctic Fox o superior
- SDK de Android 30 o superior (Wear OS)
- Dispositivo Wear OS o emulador configurado

### Pasos de Instalación

1. **Clonar el repositorio**
```bash
git clone [URL_DEL_REPOSITORIO]
cd MiAppWear
```

2. **Abrir en Android Studio**
```bash
# Abrir el proyecto en Android Studio
File > Open > Seleccionar carpeta del proyecto
```

3. **Sincronizar dependencias**
```bash
# Android Studio sincronizará automáticamente
# O manualmente: Tools > Sync Project with Gradle Files
```

4. **Configurar dispositivo**
- Conectar dispositivo Wear OS via ADB
- O configurar emulador de Wear OS

5. **Ejecutar aplicación**
```bash
# Seleccionar dispositivo de destino
# Presionar Run (Ctrl+R / Cmd+R)
```

### Permisos Requeridos
La aplicación solicitará automáticamente los siguientes permisos:

```xml
<!-- Reconocimiento de actividad -->
<uses-permission android:name="android.permission.ACTIVITY_RECOGNITION" />

<!-- Sensores corporales -->
<uses-permission android:name="android.permission.BODY_SENSORS" />

<!-- Vibraciones para feedback -->
<uses-permission android:name="android.permission.VIBRATE" />

<!-- Notificaciones (Android 13+) -->
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

## 🎯 Guía de Uso

### Configuración Inicial
1. **Primera ejecución**: La app solicitará permisos necesarios
2. **Configurar metas**: Establecer objetivos diarios personalizados
3. **Activar recordatorios**: Habilitar notificaciones de actividad

### Navegación Principal
- **Dashboard**: Pantalla principal con resumen del día
- **Pasos**: Detalles completos del seguimiento de pasos
- **Hidratación**: Control y registro de consumo de agua
- **Configuración**: Personalización de metas y preferencias

### Acciones Rápidas
- **Agregar agua**: Un toque para registrar un vaso
- **Registrar actividad**: Actualizar tiempo de última actividad
- **Cambiar metas**: Acceso rápido desde cada pantalla específica

## 🔧 Personalización

### Modificar Metas por Defecto
Ubicación: `app/src/main/java/com/example/miappwear/data/HealthData.kt`

```kotlin
data class StepData(
    val steps: Int = 0,
    val goal: Int = 10000,  // Cambiar meta por defecto de pasos
    // ...
)

data class HydrationData(
    val glassesConsumed: Int = 0,
    val dailyGoal: Int = 8,  // Cambiar meta por defecto de hidratación
    // ...
)
```

### Ajustar Intervalos de Recordatorios
Ubicación: `app/src/main/java/com/example/miappwear/data/HealthData.kt`

```kotlin
data class ActivityReminderData(
    val lastActivityTime: Long = System.currentTimeMillis(),
    val reminderInterval: Long = 60 * 60 * 1000, // Cambiar intervalo (1 hora)
    // ...
)
```

### Personalizar Colores del Tema
Ubicación: `app/src/main/java/com/example/miappwear/presentation/components/WearComponents.kt`

```kotlin
// Cambiar colores de progreso
progressColor = Color(0xFF4CAF50)  // Verde para pasos
progressColor = Color(0xFF2196F3)  // Azul para hidratación
```

## 📱 Integración con Wear OS

### Complicaciones
La aplicación incluye una complicación que muestra el conteo de pasos actual:
- **Ubicación**: `MainComplicationService.kt`
- **Tipo**: `SHORT_TEXT`
- **Actualización**: Automática con cambios de datos

### Tiles
Tile del sistema que muestra resumen de salud:
- **Ubicación**: `MainTileService.kt`
- **Contenido**: Pasos e hidratación del día
- **Acceso**: Deslizar desde la pantalla principal

## 🐛 Resolución de Problemas

### Problemas Comunes

**1. Los pasos no se registran**
- Verificar permisos de `ACTIVITY_RECOGNITION` y `BODY_SENSORS`
- Comprobar que el servicio `StepCounterService` esté ejecutándose
- Reiniciar la aplicación si es necesario

**2. Datos se resetean inesperadamente**
- Verificar la fecha del sistema
- El reseteo automático ocurre a medianoche según la configuración local

**3. Recordatorios no funcionan**
- Verificar permisos de notificaciones
- Comprobar configuración de recordatorios en Settings
- Verificar que la app no esté en modo de ahorro de batería

### Logs de Depuración
```bash
# Ver logs de la aplicación
adb logcat | grep "miappwear"

# Ver servicios en ejecución
adb shell dumpsys activity services | grep StepCounterService
```

## 🚀 Roadmap Futuro

### Características Planificadas
- [ ] **Seguimiento de frecuencia cardíaca**
- [ ] **Análisis de patrones de sueño**
- [ ] **Sincronización con Google Fit**
- [ ] **Exportación de datos**
- [ ] **Logros y badges**
- [ ] **Competencias con amigos**
- [ ] **Modo entrenamientos**

### Mejoras Técnicas
- [ ] **Optimización de batería**
- [ ] **Soporte para múltiples idiomas**
- [ ] **Themes adicionales**
- [ ] **Mejoras en accesibilidad**

## 🤝 Contribuciones

Las contribuciones son bienvenidas! Por favor:

1. **Fork** el proyecto
2. **Crear** una rama para tu característica (`git checkout -b feature/AmazingFeature`)
3. **Commit** tus cambios (`git commit -m 'Add: Amazing Feature'`)
4. **Push** a la rama (`git push origin feature/AmazingFeature`)
5. **Abrir** un Pull Request

## 👥 Autores

- **Giovanni** - *Desarrollo inicial* - [@GioSENPAII](https://github.com/GioSENPAII)

## 🙏 Agradecimientos

- **Equipo de Wear OS** por la excelente documentación
- **Comunidad de Android** por las bibliotecas y herramientas
- **Jetpack Compose** por hacer el desarrollo UI más intuitivo

---

## 📞 Soporte

¿Tienes preguntas o problemas? 

- 📧 **Email**: glongoria.3a.is@gmail.com
- 🐛 **Issues**: [GitHub Issues](https://github.com/GioSENPAII/mi-salud-wear/issues)
---

**¡Mantente saludable con Mi Salud Wear! 💪⌚**
