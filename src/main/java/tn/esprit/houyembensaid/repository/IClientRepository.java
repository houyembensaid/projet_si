package tn.esprit.houyembensaid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.houyembensaid.domaine.Client;

public interface IClientRepository extends JpaRepository<Client, Long> {
}