# Boulder-Dash-3-en-1

Boulder Dash est un jeu dans lequel le personnage principal, appelé Rockford, doit creuser dans des grottes pour collecter des diamants et atteindre la sortie dans un temps limité.
Il doit aussi éviter différentes créatures et obstacles, en particulier des chutes de rochers et doit faire très attention à ne pas provoquer d’avalanche qui risqueraient de le tuer ou l’enfermer dans la grotte.
Chaque «niveau» est représenté par une grille en 2 dimensions de cases contenant différents types d’éléments.

Cette version du Boulder Dash peut être jouée en mode console ou en mode graphique, au tour par tour ou en temps réel.
En mode graphique, il y a 2 nouvelles actions qui n'existait pas dans le jeu de base : Rockford peut poser des bombes qui explosent après un certain temps, il en dispose de 5 par niveau (touche maj droite pour les utiliser), ainsi que la possibilité de se camoufler en rocher afin de passer inaperçu à côté des ennemies (rester appuyer sur la touche entrée). Les IA n'utilisent pas ces 2 nouvelles compétences.

On peut également visualiser des IA avec différentes stratégies (notamment une ia qui prend les chemins les plus courts vers les prochains diamants, ou encore une ia "génétique" qui va jouer plein de parties différente et prendre les essais avec le meilleur score pour créer une nouvelle génération d'essais en les modifiants légèrement, et ainsi de suite).

Le programme java s'exécute dans un terminal avec les options suivantes :

java -jar boulder_dash.jar -name (affiche noms et prénoms)
java -jar boulder_dash.jar -h (rappelle la liste des options du programme)
java -jar boulder_dash.jar -lis ficher.bdcff (lis et affiche les paramètres d’un fichier BDCFF(de description de niveaux boulder dash).
java -jar boulder_dash.jar -joue fichier.bdcff [-niveau N] (permet de jouer les niveaux décrits dans le fichier BDCFF. Le chemin utilisé par le joueur est sauvegardé dans un fichier au format .dash. Si le fichier BDCFF contient plusieurs niveaux, si l’option -niveau N est passée au programme alors il ne jouera que le niveau N, sinon il jouera tous les niveaux décrits dans le fichier BDCFF, l’un après l’autre).
java -jar boulder_dash.jar -cal strategieIA fichier.bdcff -niveau N (calcule un chemin suivant une stratégie et renvoie ce chemin dans un fichier au format.dash. Stratégies possibles : -simplet, -evolue N (avec N qui définit le nombre d'étapes d'évolutions max), -directif, et -direvol N).
java -jar boulder_dash.jar -rejoue fichier.dash fichier.bdcff -niveau N (rejoue une partie de boulder dash en appliquant les déplacements fournis dans le fichier au format .dash fourni).
java -jar boulder_dash.jar -simul N strategieIA1 strategieIA2 fichier.bdcff -niveau N (évalue les deux stratégies en paramètre par simulation en lançant N parties et renvoie le score moyen, la longueur moyenne du chemin obtenu, et le temps moyen mis pour l’obtenir avec chacune des deux stratégies).

Quelques commanndes pour tester rapidemment :

java -jar boulder_dash.jar -joue ArnoDash01.bd
java -jar boulder_dash.jar -cal -direvol 10 ArnoDash01.bd -niveau 3 (puis répondre oui au moment de visualiser le chemin, sinon faire java -jar boulder_dash.jar -rejoue Essais_IA/DATE ET HEURE/Niveau_3.dash ArnoDash01.bd -niveau 3)
java -jar boulder_dash.jar -simul 5 -evolue -direvol ArnoDash01.bd -niveau 3 (puis répondre 5 pour le nombre de génération).