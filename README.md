# tp1-2026c1-pushmaindefense
# TP1 - Tower Defense

## Información academica
- Universidad: Universidad de Buenos Aires
- Facultad: FIUBA
- Materia: Paradigmas de Programación
- Curso: TB025
- Cuatrimestre: 1C 2026

## Docente y Corrector
- Diego Essaya
- Leonel Rolon

## Integrantes
- Jean Luka Arena
- padron: 112757
- Sebastian Velarde
- padron: 113749

## Nombre del proyecto
- Tower Defense

## Descripción breve del proyecto
Juego Tower Defense implementado en Java con JavaFX. El jugador defiende su base instalando torretas en el mapa para destruir las oleadas de enemigos antes de que lleguen. Tiene 3 niveles, 3 tipos de torretas y 3 tipos de enemigos. Los niveles se cargan desde archivos XML.

## Instrucciones de ejecución
Cómo ejecutar: 

Requiere Java 21 y Maven.

-git clone `<url-del-repositorio>`

-cd tp1-2026c1-pushmaindefense/tower-defense

-mvn javafx:run

## instrucciones de juego
El objetivo es destruir todos los enemigos antes de que lleguen a tu base. La base empieza con 10 de vida.
Instalar una torreta: hacé click en el ícono de la torreta en el panel derecho y luego click en un slot del mapa. Si el slot ya tiene una torreta distinta, la reemplaza. El costo se descuenta automáticamente del dinero disponible.
Las torretas disparan solas al enemigo más cercano dentro de su rango.
Torretas disponibles:

Simple ($50) — 1 de daño, 1 disparo por segundo

Rápida ($75) — 1 de daño, 2 disparos por segundo

Poderosa ($100) — 2 de daño, 1 disparo por segundo

Enemigos:

Débil — 1 de vida, lento, da $10

Rápido — 2 de vida, rápido, da $20

Tanque — 6 de vida, muy lento, da $30

Si ganás un nivel, el dinero, puntaje, vida de la base y torretas pasan al siguiente. Si perdés, volvés al menú y empezás desde el nivel 1.

## Formato .xml utilizado para la carga de niveles
Los niveles están en `src/main/resources/nivelN.xml` y se validan con un XSD al cargar.

Cada archivo define el dinero inicial, la ruta que siguen los enemigos (una lista de puntos donde el primero es el spawn y el último es la base), los slots disponibles para torretas, torretas preinstaladas si las hay, y la lista de enemigos con su tipo y el delay en milisegundos desde el inicio del nivel.

Los tipos de enemigos válidos son `WEAK`, `FAST` y `TANK`.

## links videos

https://youtu.be/vPXINI7Xvok?si=mdL1XQWhAbvfT--h

https://youtu.be/liWerDg73sc?si=bC2eauzbZyX9sh8D

## derechos de autor de sprites

Copyright/Attribution Notice: 
Riley Gombart

Copyright/Attribution Notice: 
Credit "Kenney.nl" or "www.kenney.nl", this is not mandatory.
