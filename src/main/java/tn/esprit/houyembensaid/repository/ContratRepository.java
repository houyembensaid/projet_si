package tn.esprit.houyembensaid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.houyembensaid.domaine.Contrat;

public interface ContratRepository extends JpaRepository<Contrat, Long> {
}