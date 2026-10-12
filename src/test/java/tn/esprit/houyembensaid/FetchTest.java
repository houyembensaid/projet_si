package tn.esprit.houyembensaid;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.houyembensaid.domaine.Contrat;
import tn.esprit.houyembensaid.domaine.ModePaiement;
import tn.esprit.houyembensaid.domaine.Paiement;
import tn.esprit.houyembensaid.repository.IContratRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootTest
class FetchTest {

    @Autowired
    IContratRepository contratRepository;

    @Autowired
    EntityManager em;

    @Test
    @Transactional
    void comparerFetch() {
        // 1. Créer un contrat avec un paiement
        Contrat c = new Contrat();
        c.setDateSignature(LocalDate.now());
        c.setMontantTotal(new BigDecimal("500"));
        c.setValide(true);

        Paiement p = new Paiement();
        p.setMontant(new BigDecimal("200"));
        p.setDatePaiement(LocalDate.now());
        p.setModePaiement(ModePaiement.CARTE);
        c.addPaiement(p);

        contratRepository.saveAndFlush(c);

        // 2. Vider le cache pour forcer une vraie lecture en base
        em.clear();

        // 3. Charger le contrat
        System.out.println("========== AVANT findById ==========");
        Contrat charge = contratRepository.findById(c.getIdContrat()).get();
        System.out.println("========== APRES findById (paiements pas encore utilises) ==========");

        // 4. Utiliser enfin la liste
        System.out.println("Nombre de paiements : " + charge.getPaiements().size());
        System.out.println("========== APRES getPaiements() ==========");
    }
}