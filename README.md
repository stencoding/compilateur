Utilisation de notre compilateur avec Eclipse :

Dans la configuration du main (clique droit sur Main.java, run as, run configurations), onglet arguments : deux arguments.
Le premier est le fichier à traiter, le deuxième est oui ou non si on veut afficher l'arbre syntaxique.

Exemple : -./files/in/general.txt non
-> Analyse du fichier general.txt, sans affichage de l'arbre syntaxique

Les fichiers dans le dossier in :
- test_exp.txt
- test_for.txt
- test_function.txt
- test_if-else.txt
- test_multi_var.txt
- test_var.txt
- test_while.txt

Ils contiennent des bouts de code qui fonctionne et qui permettent de tester chaque composant du compilateur.

Le fichier general.txt, contient plusieurs fonctions qui regroupent tous les composants du compilateur.

Une fois le fichier main compilé, le code est généré dans le fichier out/code_generated.txt.

La commande : ./MSM/msm -d -d ./files/out/code_generated.txt > ./files/out/out.txt
permet d'utiliser de MSM avec le fichier code_generated.txt.
La sortie est redirigée dans le fichier out/out.txt. C'est dedans que l'on pourra voir ce que MSM a généré : seulement les out.i ou la pile si les paramètres -d -d ont été utilisés

			
