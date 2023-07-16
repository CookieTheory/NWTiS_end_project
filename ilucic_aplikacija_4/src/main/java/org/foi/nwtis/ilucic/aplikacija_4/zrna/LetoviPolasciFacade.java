/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package org.foi.nwtis.ilucic.aplikacija_4.zrna;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.foi.nwtis.ilucic.aplikacija_4.jpa.Airports;
import org.foi.nwtis.ilucic.aplikacija_4.jpa.LetoviPolasci;
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
public class LetoviPolasciFacade {
  @PersistenceContext(unitName = "nwtis_dz3_pu")
  private EntityManager em;
  private CriteriaBuilder cb;

  @PostConstruct
  private void init() {
    System.out.println("LetoviPolasciFacade- init");
    cb = em.getCriteriaBuilder();
  }

  public void create(LetoviPolasci let) {
    em.persist(let);
  }

  public void edit(LetoviPolasci let) {
    em.merge(let);
  }

  public void remove(LetoviPolasci Let) {
    em.remove(em.merge(Let));
  }

  public LetoviPolasci find(Object id) {
    return em.find(LetoviPolasci.class, id);
  }

  public List<LetoviPolasci> findAll() {
    cb = em.getCriteriaBuilder();
    CriteriaQuery<LetoviPolasci> cq = cb.createQuery(LetoviPolasci.class);
    cq.select(cq.from(LetoviPolasci.class));
    return em.createQuery(cq).getResultList();
  }

  public List<LetoviPolasci> getLetoviIcaoIVrijeme(Airports aerodrom, String vrijemeOd,
      String vrijemeDo, int odBroja, int broj) {
    try {
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
      Date vrijemeOdFormatirano = dateFormat.parse(vrijemeOd);
      Date vrijemeDoFormatirano = dateFormat.parse(vrijemeDo);
      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaQuery<LetoviPolasci> cq = cb.createQuery(LetoviPolasci.class);
      Root<LetoviPolasci> root = cq.from(LetoviPolasci.class);

      Predicate icaoPredicate = cb.equal(root.get("airport").get("icao"), aerodrom.getIcao());
      Predicate vrijemePredicate =
          cb.between(root.get("stored"), vrijemeOdFormatirano, vrijemeDoFormatirano);

      cq.where(icaoPredicate, vrijemePredicate);

      TypedQuery<LetoviPolasci> query =
          em.createQuery(cq).setFirstResult(odBroja).setMaxResults(broj);

      return query.getResultList();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public List<LetoviPolasci> getLetoviVrijeme(Airports aerodrom, String vrijeme, int odBroja,
      int broj) {
    try {
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
      Date vrijemeOdFormatirano = dateFormat.parse(vrijeme);
      Calendar cal = Calendar.getInstance();
      cal.setTime(vrijemeOdFormatirano);
      cal.add(Calendar.DAY_OF_MONTH, 1);
      Date vrijemeDoFormatirano = cal.getTime();
      // System.out.println("" + vrijemeOdFormatirano + vrijemeDoFormatirano);
      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaQuery<LetoviPolasci> cq = cb.createQuery(LetoviPolasci.class);
      Root<LetoviPolasci> root = cq.from(LetoviPolasci.class);

      Predicate icaoPredicate = cb.equal(root.get("airport").get("icao"), aerodrom.getIcao());
      Predicate vrijemePredicate =
          cb.between(root.get("stored"), vrijemeOdFormatirano, vrijemeDoFormatirano);

      cq.where(icaoPredicate, vrijemePredicate);

      TypedQuery<LetoviPolasci> query =
          em.createQuery(cq).setFirstResult(odBroja).setMaxResults(broj);

      return query.getResultList();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public int count() {
    cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    Root<LetoviPolasci> rt = cq.from(LetoviPolasci.class);
    cq.select(cb.count(rt));
    Query q = em.createQuery(cq);
    return ((Long) q.getSingleResult()).intValue();
  }
}
