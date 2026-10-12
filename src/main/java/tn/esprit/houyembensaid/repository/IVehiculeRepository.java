package tn.esprit.houyembensaid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.houyembensaid.domaine.Vehicule;

public interface IVehiculeRepository extends JpaRepository<Vehicule, Long> {
}