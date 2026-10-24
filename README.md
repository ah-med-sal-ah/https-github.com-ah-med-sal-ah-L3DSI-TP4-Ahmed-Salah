# TP4 — ListView de contacts modifiable (Android Kotlin)

**Auteur :** Ahmed Salah
**Formation :** Licence en Technologies de l'Information (L3 DSI) — ISET Djerba
**Technologies :** Kotlin, Android Studio, ListView, ArrayAdapter, ViewModel

## Description

Application Android qui affiche une liste de contacts dans une `ListView` et permet de les modifier, ajouter et supprimer.

## Fonctionnalités

- Affichage des contacts dans une `ListView` grâce à un `ArrayAdapter`
- Clic sur un contact : son nom est copié dans l'`EditText`
- Bouton **Modifier** avec contrôles (aucune sélection, nom vide)
- Bouton **Ajouter**
- Suppression par appui long avec confirmation (`AlertDialog`)
- Refus des doublons (sans tenir compte de la casse)
- Tri alphabétique de la liste
- Conservation des données à la rotation de l'écran (`ViewModel`)

## Captures d'écran

### 1. Création du projet

![Création du projet](screenshots/01-creation-projet.png)

### 2. Suppression d'un contact avec confirmation

![Suppression avec dialogue de confirmation](screenshots/02-suppression-dialogue.png)

### 3. Ajout d'un contact et liste triée

![Ajout et tri de la liste](screenshots/03-ajout-tri-liste.png)
