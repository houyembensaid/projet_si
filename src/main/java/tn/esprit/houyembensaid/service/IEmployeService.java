package tn.esprit.houyembensaid.service;

import java.util.List;
import tn.esprit.houyembensaid.domaine.Employe;

public interface IEmployeService {

    Employe create(Employe employe);

    Employe findById(Long id);

    List<Employe> findAll();

    Employe update(Long id, Employe employe);

    void deleteById(Long id);
}