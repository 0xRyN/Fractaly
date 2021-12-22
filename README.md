# RAPPORT DU PROJET FRACTALY


## 1. (Identifiants)

> Membres du groupe:
> -  FOURMONT Baptiste: 21953237 Groupe 3
> -  AL AZAWI RAYAN: 21959238 Groupe 3


## 2. (Fonctionnalités)

Les fonctionnalitées implémentées sont:
1. Nous avons séparé tout nos fichiers en MVC

2. Sauvegarde de l'image avec un petit fichier texte contenant son descriptif

3. ZOOM / Déplacement de la fractale

4. Changement de couleurs 

5. Utilisation de ForkJoinPool (Multithreading)

6. Ensemble de MandelBrot / Julia


## 3. (Compilation et exécution)
Pour lancer le programme:
    ./run.sh [-g Show GUI, -t Use only terminal] [-m Use MandelBrot function, -j Use Julia function]
A l'exécution, on pourra choisir dans l'interface graphique si on veut afficher une fonction Julia ou bien un Mandelbrot.
## 4. (Découpage Modulaire)
    Nous avons séparés  nos fichiers de manière intelligente afin de rendre un contenu plus propre et plus pratique.
    Il est séparé en trois dossiers:
    1. Model
        ComputeFractal -> Permet de Calculer les fractals
    2. Views
        Fractal -> Contient permettant de créer un Fractal
        JuliaDialog -> Permet de créer une fonction Julia grâce à une boîte de dialogue
    3. Utils 
        Complex -> Classe permettant de créer un Complexe
        FractalColors -> Contient les fonctions de couleurs pour les Fractales
        Julia -> Classe permetant de créer une fonction de Julia
        MandelBrot -> Classe permettant de créer un ensemble de Mandelbrot
    4. App.java
        Cette classe contient le main est c'est le contrôleur.
        Il parse les arguments qu'on lui donne voir (3) à l'aide de apache-commons-cli
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
        

