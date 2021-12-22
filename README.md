# RAPPORT DU PROJET FRACTALY


## 1. (Identifiants)

> Membres du groupe:
> -  FOURMONT Baptiste: 21953237 Groupe 3
> -  AL AZAWI RAYAN: 21959238 Groupe 3


## 2. (Fonctionnalités)
Nous avons ajoutées toutes les fonctions demandées.
Voici, Les extensions implémentées:

1. Séparation de nos fichiers en MVC

2. Sauvegarde de l'image ainsi qu'un petit texte contenant son descriptif

3. ZOOM | Déplacements dans l'image 

4. Changement de couleurs en temps réel

5. Utilisation de ForkJoinPool (Multithreading)

6. Ensemble de MandelBrot / Julia

7. Classe Immuable

8. Utilisation du Patron Builder


## 3. (Compilation et exécution)
Pour lancer le programme:
    ./run.sh [-g Show GUI, -t Use only terminal] [-m Use MandelBrot function, -j Use Julia function]
A l'exécution, on pourra choisir dans l'interface graphique si on veut afficher une fonction Julia ou bien un Mandelbrot.
## 4. (Découpage Modulaire)
    Nous avons séparés  nos fichiers, pour décomposer notre application en plusieurs modules.
    1. Model

        ComputeFractal -> Permet de Calculer les fractales à l'aide du Multithreading
    2. Views

        Fractal -> Classe content l'objet Fractal celle-ci étend WritableImage. On calcule une fractale à l'aide de ComputeFractal ( MultiThreading).
        Et on affiche celle-ci.

        JuliaDialog -> Permet de créer une fonction Julia grâce à une boîte de dialogue celle ci prends une Paire(Réel,Image) (Complexe) et la renvoie. 

    3. Utils 
        Complex -> Classe permettant de créer un Complexe

        FractalColors -> Contient les fonctions de couleurs pour les Fractales. Nous disposons de 4 fonctions de couleurs.
        On utilisera cette classe pour pouvoir ajouter une couleur à notre fractale.

        Julia -> Classe permetant de créer une fonction de Julia

        MandelBrot -> Classe permettant de créer un ensemble de Mandelbrot

    4. App.java

        Cette classe contient le main et c'est aussi le contrôleur.
        Il parse les arguments qu'on lui donne voir (3) à l'aide de apache-commons-cli.
        On pourra ainsi choisir entre GUI ou Terminal.
        Il contient tout ce qui est lié au terminal
            -> Scanner pour pouvoir récupèrer X et Y
            -> Option pour save les images
            -> Fichier de description
            -> Créer un Mandelbrot/Julia
        GUI -> Fonctionnalités en temps réel
                -> ZOOM / Déplacement 
                -> Couleurs en temps réel
                -> Génrérer une fractal selon le Réel et l'imaginaire donné
            -> Save et fichier de description
    5. 

## 5. (Organisation du travail)
    Nous nous sommes répartis les tâches en fonctions de notre emploi du temps et de notre intérêt particulier à certaines fonctions.
    Mais surtout, nous avons travaillé ensemble afin d'établir les meilleurs prototype de chaque fonction et nous avons compris ensemble l'intérêt principal de diviser chaque fonctionnalité en sous problème afin de mieux séparé les tâches et de pouvoir éviter tout bug.

## 6. (Miscs)
    Nous aurions voulu avoir:
        - une meilleur modularité câd que le Contrôleur (App) aurait pu avoir des fonctions dans d'autre fichiers
        - ajouter d'autre informations dans le fichier texte comme:
            - ZOOM utilisé
            - Couleur utilisé (Fonction pointant vers une adresse lambda qlc ...)
            - Dernier déplacements
        - Trouver le threeshold parfait 
        

