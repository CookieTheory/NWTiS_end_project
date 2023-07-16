/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package org.foi.nwtis.ilucic.aplikacija_4.zrna;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import org.foi.nwtis.ilucic.aplikacija_4.jpa.Korisnici;
import org.foi.nwtis.podaci.PogresnaAutentikacija;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 *
 * @author ilucic
 */
@Stateless
public class KorisniciFacade {
  @PersistenceContext(unitName = "nwtis_dz3_pu")
  private EntityManager em;
  private CriteriaBuilder cb;

  @PostConstruct
  private void init() {
    System.out.println("KorisniciFacade- init");
    cb = em.getCriteriaBuilder();
  }

  public void create(Korisnici korisnik) {
    em.persist(korisnik);
  }

  public void edit(Korisnici korisnik) {
    em.merge(korisnik);
  }

  public void remove(Korisnici Korisnik) {
    em.remove(em.merge(Korisnik));
  }

  public Korisnici find(Object id) {
    return em.find(Korisnici.class, id);
  }

  public List<Korisnici> findAll() {
    cb = em.getCriteriaBuilder();
    CriteriaQuery<Korisnici> cq = cb.createQuery(Korisnici.class);
    cq.select(cq.from(Korisnici.class));
    return em.createQuery(cq).getResultList();
  }

  public boolean kreirajKorisnika(Korisnici korisnik) {
    try {
      Korisnici noviKorisnik = new Korisnici();
      noviKorisnik.setIme(korisnik.getIme());
      noviKorisnik.setPrezime(korisnik.getPrezime());
      noviKorisnik.setKorisnickoIme(korisnik.getKorisnickoIme());
      noviKorisnik.setSifra(korisnik.getSifra());
      LocalDateTime sada = LocalDateTime.now();
      Timestamp timestamp = Timestamp.valueOf(sada);
      noviKorisnik.setDatumRegistracije(timestamp);

      em.persist(noviKorisnik);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public Korisnici pronadiKorisnikaZaLoginIliProvjeru(String korisnickoIme, String sifra)
      throws PogresnaAutentikacija {
    CriteriaQuery<Korisnici> cq = cb.createQuery(Korisnici.class);
    Root<Korisnici> rt = cq.from(Korisnici.class);
    Predicate prvi = cb.like(rt.get("korisnickoIme"), korisnickoIme);
    Predicate drugi = cb.like(rt.get("sifra"), sifra);
    Predicate prviIDrugi = cb.and(prvi, drugi);
    cq.where(prviIDrugi);
    TypedQuery<Korisnici> q = em.createQuery(cq);

    if (!q.getResultList().isEmpty()) {
      return q.getResultList().get(0);
    } else {
      throw new PogresnaAutentikacija("Pogrešna autentikacija!");
    }
  }

  public Korisnici dajKorisnika(String traziKorisnika) {
    try {
      CriteriaQuery<Korisnici> cq = cb.createQuery(Korisnici.class);
      Root<Korisnici> rt = cq.from(Korisnici.class);
      Predicate prvi = cb.like(rt.get("korisnickoIme"), traziKorisnika);
      cq.where(prvi);
      TypedQuery<Korisnici> q = em.createQuery(cq);

      if (!q.getResultList().isEmpty()) {
        return q.getResultList().get(0);
      } else {
        return null;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public List<Korisnici> dajKorisnike(String traziImeKorisnika, String traziPrezimeKorisnika) {
    try {
      CriteriaQuery<Korisnici> cq = cb.createQuery(Korisnici.class);
      Root<Korisnici> rt = cq.from(Korisnici.class);
      if (!traziImeKorisnika.isEmpty()) {
        Predicate prvi = cb.like(rt.get("ime"), "%" + traziImeKorisnika + "%");
        if (!traziPrezimeKorisnika.isEmpty()) {
          Predicate drugi = cb.like(rt.get("prezime"), "%" + traziPrezimeKorisnika + "%");
          Predicate prviIDrugi = cb.and(prvi, drugi);
          cq.where(prviIDrugi);
        } else {
          cq.where(prvi);
        }
      } else if (!traziPrezimeKorisnika.isEmpty()) {
        Predicate drugi = cb.like(rt.get("prezime"), "%" + traziPrezimeKorisnika + "%");
        cq.where(drugi);
      }
      TypedQuery<Korisnici> q = em.createQuery(cq);

      if (!q.getResultList().isEmpty()) {
        return q.getResultList();
      } else {
        return null;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public int count() {
    cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    Root<Korisnici> rt = cq.from(Korisnici.class);
    cq.select(cb.count(rt));
    Query q = em.createQuery(cq);
    return ((Long) q.getSingleResult()).intValue();
  }
}
