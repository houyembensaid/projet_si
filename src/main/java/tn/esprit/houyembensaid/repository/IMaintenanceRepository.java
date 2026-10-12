package tn.esprit.houyembensaid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.houyembensaid.domaine.Maintenance;

public interface IMaintenanceRepository extends JpaRepository<Maintenance, Long> {
}