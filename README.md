# APLICACIÓN "SPYRO THE DRAGON" ##

## Introducción ##

La aplicaciónm consiste en una guía con información sobre el universo de "Spyro". Mi trabajo en esta app ha consistido en introducir la guía de uso inicial, así como las animaciones y dos EasterEggs.

## Caracterísitcas principales ##

La aplicación cuenta con 3 apartados: lista de personajes, lista de mundos y lista de coleccionables. Cada apartado se separa en su propio fragmento. Las transicciones entre fragmentos han sido modificadas para que sean más dinámicas y llamativas.

Al iniciar la app por primera vez, aparecerá una guía de uso que cuenta con:
- *Pantalla de bienvenida*:
  
<img src="https://github.com/user-attachments/assets/7b21d4ee-99ac-4274-9da6-953a054bf074" width="33%" height="33%">

- *4 Pasos con animaciones*: cada paso cuenta con un botón para pasar al siguiente paso y un botón para omitir la guía.
  
<img src="https://github.com/user-attachments/assets/122d54e0-f0b3-481d-ae9c-321181de7a3b" width="33%" height="33%">
<img src="https://github.com/user-attachments/assets/b6bd7381-818d-44d9-b275-914eeb2326f8" width="33%" height="33%">

- *Pantalla de finalización*:

<img src="https://github.com/user-attachments/assets/c903824c-55cc-4e4a-ab44-ef764eaff0b3" width="33%" height="33%">

La guía solo debe aparecer la primera vez que abres las aplicación y finalices u omitas la guía. Sin embargo, para la realización de pruebas introduje la opción para poder reiniciar la necesidad de la guía: solo debes pulsar sobre la última opción de la lista de Coleccionables.

También cuenta con 2 EasterEgg:
- Al realizar una pulsación prolongada sobre Spyro en la lista de personajes, saldrá fuego de su boca (o algo similar).
- Al pulsar 4 veces sobre las gemas en la pantalla de coleccionables, se reproducirá un video sobre el mundo de Spyro.

La aplicación cuenta con sonidos ambientados en la temática.

## Video demostración ##

https://github.com/user-attachments/assets/2facb9a2-4482-4c03-877e-0aa5698d7a33

## Tecnologías utilizadas ##
La tecnología utilizada para mi parte del desarrollo han sido las siguientes:
- *ObjectAnimator* y *AnimatorSet* para las animaciones de la guía.
- *Canvas* para la animaación del fuego.
- *SharedPreferences* para guardar la necesidad de volver a aparecer la guía.
- *SoundPool* para la reproducción de sonidos.

### CÓMO CLONAR MI PROYECTO ###
- 1º Pulsa el botón "<>CODE" y copia el URL en la modalidad que te interese
- 2º Abre Git Bash y posicionate en el directorio donde te interese clonar el poyecto
- 3º Escribe el comando ''git clone'' seguido de la URL que has copiado en el primer paso. Pulsar "Enter" y se creará el clon del proyecto.

## MIS CONCLUSIONES ##
Este trabajo me ha resultado entretenido, pero muy laborioso. Sobre todo control de las animaaciones (las posiciones, el movimiento, la duración...) y la creación del fuego, el cual no ha quedado muy realista. 

El control de sonidos y la guía me ha resultaado sencillo.

En conclusióm, la tarea eraa corta, pero ha precisado mucho tiempo para poder controlar el uso de las nuevas herramientas.
