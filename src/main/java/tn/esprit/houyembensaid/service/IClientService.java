package tn.esprit.houyembensaid.service;

import java.util.List;
import tn.esprit.houyembensaid.domaine.Client;

public interface IClientService {

    Client create(Client client);

    Client findById(Long id);

    List<Client> findAll();

    Client update(Long id, Client client);

    void deleteById(Long id);
}