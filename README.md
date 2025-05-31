# MyLibrary

**Auteur** : Nabil Hamid (60505)  
**Cours** : MOBG6

## Résumé

**MyLibrary** est une application Android qui permet de gérer une collection personnelle de livres physiques. Elle permet d’ajouter des livres à votre bibliothèque grâce au scan de leur code ISBN. L'application fournit des fonctionnalités de tri, de recherche, d’évaluation et de catégorisation des livres.

## Fonctionnalités

- Création de compte et authentification via Supabase (email et mot de passe).
- Ajout rapide d’un livre par scan de son ISBN.
- Recherche dans la collection par titre ou auteur.
- Ajout de tags personnalisés (par exemple : *Lu*, *Non lu*, etc.).
- Tri des livres par :
  - Titre (ordre alphabétique)
  - Auteur (ordre alphabétique)
  - Note (décroissante)
  - Tags
- Consultation des détails d’un livre (si disponible) :
  - Titre
  - Auteur(s)
  - Éditeur
  - Nombre de pages
  - Description
- Attribution d’une note et d’un avis à chaque livre.

## Outils et technologies

- **Android Studio** avec **Kotlin**.
- **Supabase** : authentification et base de données SQL.
- **Google Books API** : récupération des informations des livres.
- **OpenLibrary** : fallback si la couverture n’est pas disponible via Google Books API.

## Améliorations futures

- Gestion de la déconnexion utilisateur.
- Ajout d’une page dédiée aux auteurs avec plus d’informations.
- Fonctionnalité de partage et consultation des avis entre utilisateurs.
- Remplacement de Google Books API par une alternative plus complète (ex. : [ISBNdb](https://isbndb.com), payant).
