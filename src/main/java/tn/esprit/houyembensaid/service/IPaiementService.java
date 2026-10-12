package tn.esprit.houyembensaid.service;

import java.util.List;
import tn.esprit.houyembensaid.domaine.Paiement;

public interface IPaiementService {

    Paiement findById(Long id);

    List<Paiement> findAll();
}