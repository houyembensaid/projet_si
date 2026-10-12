# Stratégie de fetch et de cascade – AutoLoc

## 1. Tableau récapitulatif

| Association | Fetch | Cascade | Justification |
|---|---|---|---|
| Contrat → Paiement | LAZY | ALL + orphanRemoval | Un paiement n'existe que rattaché à son contrat (composition) ; supprimer le contrat supprime ses paiements, et retirer un paiement de la liste le supprime en base. |
| Agence → Vehicule | LAZY | Aucune | Un véhicule survit à la suppression de son agence ; la liste des véhicules n'est chargée que si on en a besoin. |
| Agence → Employe | LAZY | Aucune | Les employés ont une existence propre ; on évite de charger toute l'équipe à chaque lecture d'une agence. |
| Vehicule ↔ Equipement | LAZY | Aucune | Les équipements sont partagés entre plusieurs véhicules : supprimer un véhicule ne doit pas supprimer un équipement encore utilisé ailleurs. Utilisation d'un Set pour éviter les doublons. |
| Client → Reservation | LAZY | PERSIST | Enregistrer un client avec de nouvelles réservations les sauvegarde ensemble ; pas de REMOVE pour conserver l'historique des réservations. |
| Reservation → Vehicule | LAZY | Aucune | Un véhicule existe indépendamment de ses réservations ; un @ManyToOne est EAGER par défaut, on le force en LAZY pour ne pas charger toute la chaîne d'objets liés. |
| Reservation ↔ Contrat | LAZY | ALL (côté Reservation) | Un contrat dépend de sa réservation : il est créé, mis à jour et supprimé avec elle. Limite : le côté inverse d'un @OneToOne n'est pas réellement paresseux sans bytecode enhancement. |
| Vehicule → Maintenance | LAZY | PERSIST | Les maintenances sont enregistrées avec le véhicule ; pas de REMOVE pour garder l'historique d'entretien. |

## 2. Choix généraux

- **@ManyToOne et @OneToOne** : EAGER par défaut dans JPA, forcés en LAZY pour éviter de charger toute la chaîne d'objets liés.
- **@OneToMany et @ManyToMany** : LAZY par défaut, conservé : une collection ne se charge que si on l'utilise.
- **Cascade** : appliquée uniquement quand l'enfant ne peut pas vivre sans son parent (composition : Paiement, Contrat) ou quand il est créé avec lui (PERSIST). Jamais de REMOVE sur des entités qui ont une existence propre (Vehicule, Employe, Equipement).
- **Côté propriétaire** : toujours le côté qui porte la clé étrangère (@ManyToOne, ou Contrat pour le @OneToOne avec Reservation, ou Vehicule pour la table de jointure `vehicule_equipement`).
- **Lombok** : @Getter/@Setter ciblés, jamais @Data, pour éviter un StackOverflowError dû au toString/hashCode qui parcourt les deux côtés d'une association bidirectionnelle.

## 3. Observation LAZY vs EAGER (Contrat → Paiement)

- **LAZY (retenu)** : le chargement d'un contrat exécute une seule requête sur `contrat`. Les paiements sont chargés par une seconde requête uniquement à l'appel de `getPaiements()`.
- **EAGER** : la requête sur `contrat` contient déjà un `left join paiement`, même quand les paiements ne sont pas utilisés, ce qui alourdit chaque lecture.
- **Limite du LAZY** : un accès à la collection hors transaction lève une `LazyInitializationException`, à traiter par une requête dédiée (Atelier 7).
- 