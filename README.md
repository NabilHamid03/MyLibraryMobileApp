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

## Configuration

Le projet a besoin de clés API (Supabase + Google Books). Elles sont chargées depuis un fichier `MyLibrary/secret.properties` qui n'est **pas** versionné.

1. Copier le template :
   ```
   cp MyLibrary/secret.properties.example MyLibrary/secret.properties
   ```
2. Remplir les valeurs dans `MyLibrary/secret.properties` :
   - `SUPABASE_URL` et `SUPABASE_KEY` : depuis le dashboard Supabase de ton projet.
   - `GOOGLE_KEY` : clé API Google Books (Google Cloud Console).
3. Ouvrir le dossier `MyLibrary/` dans Android Studio et synchroniser Gradle.

## CI / Distribution de l'APK

Deux workflows GitHub Actions sont fournis dans [.github/workflows/](.github/workflows/) :

- **`build-apk.yml`** — Build l'APK debug et l'attache à une release. Se déclenche sur `git push` d'un tag `v*` (ex. `v1.0.0`) ou manuellement via *Actions → Build & release APK → Run workflow*.
- **`keep-supabase-alive.yml`** — Pingue Supabase tous les jours pour éviter la mise en pause après 7 jours d'inactivité.

### Secrets GitHub à configurer

Dans *Settings → Secrets and variables → Actions → New repository secret*, ajouter :

| Nom | Valeur |
|---|---|
| `SUPABASE_URL` | L'URL de ton projet Supabase (ex. `https://xxx.supabase.co`) |
| `SUPABASE_KEY` | La clé **anon / publishable** (jamais la `service_role`) |
| `GOOGLE_KEY` | Ta clé API Google Books |

### Publier une nouvelle version

```bash
git tag v1.0.0
git push origin v1.0.0
```

L'APK sera buildé et publié sur la page *Releases* du repo.

## Améliorations futures

- Gestion de la déconnexion utilisateur.
- Ajout d’une page dédiée aux auteurs avec plus d’informations.
- Fonctionnalité de partage et consultation des avis entre utilisateurs.
- Remplacement de Google Books API par une alternative plus complète (ex. : [ISBNdb](https://isbndb.com), payant).
