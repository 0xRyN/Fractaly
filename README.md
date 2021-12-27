# RAPPORT DU PROJET FRACTALY

## 1. Identifiants

> Membres du groupe:
>
> -   FOURMONT Baptiste: 21953237 Groupe 3
> -   AL AZAWI RAYAN: 21959238 Groupe 3

## 2. Fonctionnalités

Nous avons ajoutées toutes les fonctions demandées.
Voici, Les extensions implémentées:

1. Séparation de nos fichiers en MVC (App.java étant le controlleur)

2. Sauvegarde de l'image ainsi qu'un petit texte contenant son descriptif

3. ZOOM | Déplacements dans l'image

4. Changement de couleurs en temps réel

5. Utilisation de ForkJoinPool (Multithreading) et benchmark dans le terminal à chaque génération de nouvelle fractale

6. Ensemble de MandelBrot / Julia

7. Classe Immuable

8. Utilisation du Patron Builder

9. Fonctions de première classe (UnaryOperator, BiFunction)

10. API complète de Fractales, retournant une WritableImage, pouvant être utilisée dans d'autres programmes.

## 3. Compilation et exécution

Pour lancer le programme:
./run.sh [-g Show GUI, -t Use only terminal] [-m Use MandelBrot function, -j Use Julia function]

Équivalent windows:
run-win.batch - Il ne prendra pas d'argument, et est équivalent à ./run.sh -g -m

A l'exécution, on pourra choisir dans l'interface graphique si on veut afficher une fonction Julia ou bien un Mandelbrot.

## 4. Découpage Modulaire

Nous avons séparés nos fichiers, pour décomposer notre application en plusieurs modules.

### 1. Model

ComputeFractal -> Permet de Calculer les fractales à l'aide du Multithreading ForkJoinPool.

### 2. Views

Fractal -> Classe content l'objet Fractal celle-ci étend WritableImage. On calcule une fractale à l'aide de ComputeFractal ( MultiThreading).
Et on affiche celle-ci.

JuliaDialog -> Permet de créer une fonction Julia grâce à une boîte de dialogue celle ci prends une Paire(Réel,Image) (Complexe) et la renvoie.

### 3. Utils

#### Complex

Classe permettant de créer un Complexe

#### FractalColors

Contient les fonctions de couleurs pour les Fractales. Nous disposons de 4 fonctions de couleurs.
On utilisera cette classe pour pouvoir ajouter une couleur à notre fractale.

#### Julia

Classe permetant de créer une fonction de Julia

#### MandelBrot

Classe permettant de créer un ensemble de Mandelbrot

### 4. App.java

Cette classe contient le main et c'est aussi le contrôleur.
Il parse les arguments qu'on lui donne voir (3) à l'aide de apache-commons-cli.
On pourra ainsi choisir entre l'interface graphique ou
bien l'utilisation de ligne de commande (alias terminal).

#### TERMINAL :

-   Scanner pour pouvoir récupèrer X et Y
-   Option pour save les images
-   Fichier de description
-   Créer un Mandelbrot/Julia

#### GUI : Fonctionnalités en temps réel

-   ZOOM / Déplacement / Déplacement exact avec la souris
-   Couleurs en temps réel
-   Génrérer une fractal selon le réel et l'imaginaire donné
-   Save et fichier de description

**NOTE : Même si le controlleur ne permet pas de tout modifier, l'API des fractales est complète.**

Il est donc très facile d'ajouter des fonctionnalités GUI au controlleur.

Il suffit d'appeller le Builder avec les paramètres qu'on souhaite (cf zoom ou changeJulia par ex dans App.java).

La fonction de clonage est déjà faite : Fractal.Builder(fract).zoom(2) permet de dupliquer fract et de spécifier son zoom a 2.

Tous les paramètres ne sont pas requis, des valeurs par défaut sont spécifiées.

Tous ces paramètres sont modifiables :

-   int w (Largeur)
-   int h (Hauteur)
    (La hauteur et la largeur permettent de déterminer le pas et le repère complexe.)
-   int maxIter (Max itérations)
-   double zoom (Zoom)
-   double offsetX (Deplacement horizontal)
-   double offsetY (Deplacement vertical)
-   UnaryOperator<Complex> juliaFunction (La fonction Julia si applicable)
-   BiFunction<Integer, Integer, Color> colorFunction (La fonction couleur)

## 5. Organisation du travail

Nous nous sommes répartis les tâches en fonctions de notre emploi du temps et de notre intérêt particulier à certaines fonctions.

Mais surtout, nous avons travaillé ensemble afin d'établir les meilleurs prototype de chaque fonction et nous avons compris ensemble l'intérêt principal de diviser chaque fonctionnalité en sous problème afin de mieux séparé les tâches et de pouvoir éviter tout bug.

## 6. Miscs

Nous aurions voulu avoir:

-   Une meilleur modularité câd que le Contrôleur (App) aurait pu avoir des fonctions dans d'autre fichiers

-   Ajouter d'autre informations dans le fichier texte comme:

    -   la taille du ZOOM utilisé
    -   Couleur utilisé (Fonction pointant vers une adresse lambda qlc ...)
    -   Dernier déplacements

-   Trouver le threeshold parfait : en effet, créer des Task de ForkJoinPool prend du temps. Il suffit de trouver
    l'équilibre parfait du nombre de pixels à donner à chaque Task. (ForkJoinPool est légèrement moins rapide que du
    threading classique, car il crée beaucoup de taches, et serait plus adapté sur des images de très grande résolution)

-   Un smart contract Solidity pour générer des Fractales NFT
