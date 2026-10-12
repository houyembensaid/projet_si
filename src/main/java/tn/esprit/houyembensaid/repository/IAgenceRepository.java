package tn.esprit.houyembensaid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.houyembensaid.domaine.Agence;

public interface IAgenceRepository extends JpaRepository<Agence, Long> {
}