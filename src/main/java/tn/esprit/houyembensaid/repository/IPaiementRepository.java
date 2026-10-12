package tn.esprit.houyembensaid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.houyembensaid.domaine.Paiement;

public interface IPaiementRepository extends JpaRepository<Paiement, Long> {
}