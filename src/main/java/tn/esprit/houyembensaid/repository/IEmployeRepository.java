package tn.esprit.houyembensaid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.houyembensaid.domaine.Employe;

public interface IEmployeRepository extends JpaRepository<Employe, Long> {
}