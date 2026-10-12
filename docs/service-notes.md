# Notes – Atelier 4 : IoC, injection de dépendances et couche Service

## A. Modes d'injection comparés

| Critère | Constructeur | Attribut |
|---|---|---|
| Champ `final` possible | Oui | Non |
| Dépendance visible dans la signature de la classe | Oui | Non |
| Utilisable avec `new` hors conteneur sans dépendance null | Oui (le constructeur impose le repository) | Non (le champ reste null) |
| Signalé par SonarQube for IDE | Non | Oui (règle java:S6813) |

## B. Services

| Service | Dépendances injectées | Mode | Justification |
|---|---|---|---|
| ContratServiceImpl | IContratRepository | Constructeur | Dépendance obligatoire, champ final, typée par interface ; refuse un id non null à la création et un montant négatif. |
| AgenceServiceImpl | IAgenceRepository | Constructeur | Même convention ; refuse une agence sans nom. |
| EmployeServiceImpl | IEmployeRepository | Constructeur | Même convention ; refuse un employé sans nom, prénom ou rôle. |
| VehiculeServiceImpl | IVehiculeRepository | Constructeur | Même convention ; refuse un tarif journalier négatif. |
| EquipementServiceImpl | IEquipementRepository | Constructeur | Même convention ; refuse un équipement sans libellé. |
| ClientServiceImpl | IClientRepository | Constructeur | Même convention ; refuse un client sans nom ou e-mail. |
| ReservationServiceImpl | IReservationRepository | Constructeur | Même convention ; refuse une date de fin antérieure à la date de début. |
| PaiementServiceImpl | IPaiementRepository | Constructeur | Lecture seule : un paiement est une composition du Contrat et passe par lui (cascade et orphanRemoval). |
| MaintenanceServiceImpl | IMaintenanceRepository | Constructeur | Même convention ; refuse une date de fin antérieure à la date de début. |

## C. Messages d'erreur

**Message 1 (NoSuchBeanDefinitionException)** : aucun bean de type IContratService n'a été trouvé pour ContratController. Causes probables : @Service oublié sur ContratServiceImpl, ou classe placée hors du package de scan tn.esprit.houyembensaid. Correction : ajouter @Service et vérifier le package.

**Message 2 (NoUniqueBeanDefinitionException)** : deux beans du même type (emailNotificateur et smsNotificateur). Correction 1 : @Primary placé sur la classe du bean choisi par défaut. Correction 2 : @Qualifier("emailNotificateur") placé sur le paramètre du constructeur du contrôleur.

**Message 3 (dépendance circulaire)** : avec l'injection par constructeur, chaque bean a besoin de l'autre pour être créé, donc aucun ne peut l'être en premier. Piste de conception : extraire la logique commune dans un troisième service dont les deux dépendent, ou faire dépendre l'un des services d'un repository plutôt que de l'autre service.

## D. Anomalies SonarQube for IDE

| Règle | Explication | Correction |
|---|---|---|
| java:S6813 | Injection par attribut avec @Autowired : champ non final, dépendance cachée. | Injection par constructeur avec @RequiredArgsConstructor et champ final. |
| java:S112 | Levée d'une exception générique (RuntimeException). | Utiliser ResourceNotFoundException, exception métier explicite. |
| java:S1128 | Import inutile (lombok.RequiredArgsConstructor non utilisé après le retrait de l'annotation). | Supprimer l'import ou remettre l'annotation. |

## E. Questions de compréhension

1. **Où est le new de ContratServiceImpl et qui l'exécute ?** Le conteneur Spring l'exécute au démarrage : le scan détecte @Service, instancie la classe et lui fournit le proxy du repository par son constructeur. Aucun new n'apparaît dans notre code.
2. **Pourquoi un contrôleur dépendra-t-il de IContratService et non de ContratServiceImpl ?** Il dépend d'une abstraction : on peut changer ou remplacer l'implémentation sans modifier le contrôleur.
3. **Pourquoi un service singleton doit-il rester sans état ?** Il est partagé par tous les threads. Exemple de bug : un attribut « contratCourant » modifié par une requête est écrasé par une requête simultanée d'un autre utilisateur, qui travaille alors sur le contrat d'un autre.
4. **Pourquoi update charge-t-il l'entité existante avant save ?** Passer directement l'objet reçu écraserait toutes les colonnes et, avec orphanRemoval, pourrait vider la collection de paiements. On charge donc l'existant puis on recopie seulement les attributs modifiables.
5. **Différences.** @Component se pose sur une classe détectée par le scan ; @Bean se pose sur une méthode d'une classe @Configuration, pour une classe qu'on ne peut pas annoter. @Primary (sur la classe) désigne le candidat par défaut ; @Qualifier (à l'injection) choisit explicitement un bean. IPaiementService n'expose ni création, ni modification, ni suppression car un paiement n'existe que rattaché à son contrat : ces opérations passent par le Contrat.