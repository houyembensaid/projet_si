package tn.esprit.houyembensaid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.houyembensaid.domaine.Equipement;

public interface IEquipementRepository extends JpaRepository<Equipement, Long> {
}