# Sistema de Gestión de Trabajos de Grado - FIET

# 📋 Descripción del Proyecto
Sistema de escritorio desarrollado en Java para automatizar y gestionar el proceso de trabajos de grado en la Facultad de Ingeniería Electrónica y Telecomunicaciones (FIET) de la Universidad del Cauca. Este proyecto reemplaza el proceso manual basado en formatos PDF y correos electrónicos, proporcionando una plataforma centralizada para docentes, estudiantes y coordinadores.
# 
Iteración Actual: Primer Corte - Implementación Monolítica en Capas (MVC).
# 
# 🎯 Objetivo del Primer Corte
Implementar un conjunto de historias de usuario de alto valor para el cliente utilizando una arquitectura monolítica en tres capas (Presentación, Lógica de Negocio, Persistencia) con el patrón MVC, principios de diseño SOLID y patrones GoF para garantizar la modificabilidad.

# ⚙️ Funcionalidades Implementadas (Primer Corte)
Registro de Docentes: Los docentes pueden registrarse en el sistema con sus datos personales e institucionales.

Presentación del Formato A: Los docentes autenticados pueden diligenciar y subir el Formato A de un proyecto de grado.

Evaluación del Formato A: El coordinador del programa puede revisar, aprobar, rechazar (con observaciones) los Formatos A enviados.

Re-subida del Formato A: Los docentes pueden cargar nuevas versiones del Formato A si este es rechazado (máximo 3 intentos).

Seguimiento del Estado: Los estudiantes pueden visualizar el estado actual de su proyecto de grado (e.g., "En primera evaluación", "Aceptado").

# 🏗️ Arquitectura y Diseño
Tipo de Aplicación: Escritorio (Java Swing/JavaFX).

Estilo Arquitectónico: Monolítico en 3 capas + Micro-patrón MVC.

Principios de Diseño: SOLID.

Patrones de Diseño GoF: Se aplican patrones como Factory, Singleton, Observer, entre otros, para promover la baja cohesión y el alto acoplamiento.

Modelo C4: La documentación de la arquitectura se encuentra detallada en el documento PDF del proyecto.

# 🛠️ Tecnologías Utilizadas
Lenguaje: Java

Base de Datos: MySQL

Control de Versiones: Git / GitHub

Gestión de Proyecto: Jira

# 📦 Instalación y Ejecución
Prerrequisitos
Java JDK 11 o superior

Maven 

Motor de base de datos (e.g., MySQL) configurado con el script SQL proporcionado.

Pasos para la Ejecución
Clonar el repositorio:

bash
git clone [https://github.com/Jfernando014/ProyectoDeGrado.git]
cd [ProyectoDeGrado]
Importar el proyecto en su IDE (IntelliJ, NetBeans).

O realizarlo desde la aplicación de GitHub Dekstop para mejor usabilidad.

# 👥 Autores:
 * @author Juan Fernando Portilla
 * @author Edier Fabian Dorado
 * @author David Santiago Arias

# 📄 Documentación Adicional
Documento PDF del Proyecto: GestióndelProcesodeTrabajodeGrado.pdf - Contiene la especificación completa de requisitos, prototipos, diseños UML y justificaciones técnicas.

Tablero de Tareas: [https://unicauca-team-dag79f44.atlassian.net/jira/software/projects/KAN/boards/1?atlOrigin=eyJpIjoiODQ2N2I3YWUzNmRjNDFiNzljOWFiNzg5ZTEyZjUwNGUiLCJwIjoiaiJ9] - Para seguimiento del sprint y las tareas del equipo.

# 

Universidad del Cauca - Facultad de Ingeniería Electrónica y Telecomunicaciones (FIET)
*Ingeniería de Software II - 2025.2*
