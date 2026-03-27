# Análisis de Cobertura y Calidad de Pruebas 🧪

Este documento contiene los reportes y el análisis de cobertura de código generados por las diferentes herramientas de calidad del proyecto.

## 📊 Métricas de Cobertura

### 🖥️ IntelliJ IDEA Coverage
_Análisis de cobertura nativo del IDE para ejecuciones rápidas durante el desarrollo._



> ![Pruebas en IntelliJ](../../docs/tests/intellipas.png)

> ![Pruebas en IntelliJ 2](../../docs/tests/intecoverage.png)


Las imágenes muestran un reporte de Code Coverage en IntelliJ IDEA, 
revelando que el proyecto tiene una salud técnica sólida con un 88% de 
pruebas exitosas (88 de 88 pasadas). A nivel de métricas, destaca una 
cobertura de ramas (Branch %) del 88% global, con puntajes perfectos 
del 100% en controladores, lógica central y persistencia; esto indica 
que casi todos los caminos lógicos y decisiones condicionales (if/else) 
están siendo validados. El único punto de atención es el paquete de 
configuración, que presenta un 25% de Branch Coverage, sugiriendo que 
existen flujos de configuración excepcionales que aún no han sido 
ejercitados por la suite de pruebas.



---

### 🛡️ JaCoCo 
_Reporte detallado generado mediante Maven para la integración continua y validación de calidad._


> ![Reporte Jacoco](../../docs/tests/jacoco.png)


El reporte de JaCoCo confirma un estado saludable del proyecto 
con una cobertura global de instrucciones del 82% y de ramas del 86%. 
Los componentes críticos de lógica de negocio, 
validadores y persistencia alcanzan un impecable 100% de cobertura 
de ramas, lo que garantiza que los flujos principales están totalmente 
protegidos. Al igual que en el análisis anterior, las áreas de mejora 
se concentran en los paquetes de configuración (jwt y security), 
donde la baja cobertura de instrucciones sugiere la presencia de 
código de infraestructura que no requiere pruebas unitarias exhaustivas, 
manteniendo el foco de calidad en la lógica funcional del sistema.

---

### 🔍 SonarQube
_Análisis estático de código, detección de bugs, vulnerabilidades y deuda técnica._


> ![Reporte Jacoco](../../docs/tests/sonarcube.png)
 

> ![Reporte Jacoco](../../docs/tests/sonarcubesug.png)

El análisis de SonarQube muestra que, aunque el proyecto tiene 
métricas excelentes en seguridad, confiabilidad y cobertura (83.6%), 
el estado general es "Failed" debido a problemas de mantenibilidad. 
Se identificaron 8 problemas técnicos que suman un esfuerzo estimado 
de 46 minutos para corregirse. Entre las sugerencias clave destacan 
la necesidad de eliminar código duplicado 
(como el literal "LIBRARIAN" repetido 8 veces) mediante constantes, y 
la actualización de sintaxis a Java 16+ reemplazando Collectors.toList()
por el método más moderno .toList(). Estas observaciones indican que, 
si bien el código es funcional y seguro, requiere una refactorización 
para cumplir con los estándares de limpieza y modernización que exige 
el Quality Gate.



