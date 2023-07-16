/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package org.foi.nwtis.ilucic.aplikacija_4.zrna;

import java.util.List;
import org.foi.nwtis.ilucic.aplikacija_4.jpa.AerodromiLetovi;
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
import jakarta.persistence.criteria.Subquery;

/**
 *
 * @author ilucic
 */
@Stateless
public class AerodromiLetoviFacade {
  @PersistenceContext(unitName = "nwtis_dz3_pu")
  private EntityManager em;
  private CriteriaBuilder cb;

  @PostConstruct
  private void init() {
    System.out.println("LetoviPolasciFacade- init");
    cb = em.getCriteriaBuilder();
  }

  public void create(AerodromiLetovi let) {
    em.persist(let);
  }

  public void edit(AerodromiLetovi let) {
    em.merge(let);
  }

  public void remove(AerodromiLetovi Let) {
    em.remove(em.merge(Let));
  }

  public LetoviPolasci find(Object id) {
    return em.find(LetoviPolasci.class, id);
  }

  public List<AerodromiLetovi> findAll() {
    cb = em.getCriteriaBuilder();
    CriteriaQuery<AerodromiLetovi> cq = cb.createQuery(AerodromiLetovi.class);
    cq.select(cq.from(AerodromiLetovi.class));
    return em.createQuery(cq).getResultList();
  }

  public List<Airports> dajAerodromeZaLetove() {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Airports> cq = cb.createQuery(Airports.class);
    Root<Airports> rt = cq.from(Airports.class);
    Subquery<String> subquery = cq.subquery(String.class);
    Root<AerodromiLetovi> aerodromiRoot = subquery.from(AerodromiLetovi.class);
    subquery.select(aerodromiRoot.get("airport").get("icao"));

    cq.select(rt);
    cq.where(rt.get("icao").in(subquery));

    TypedQuery<Airports> query = em.createQuery(cq);

    return query.getResultList();
  }

  public AerodromiLetovi dajAerodromLetPoIcao(String icao) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<AerodromiLetovi> cq = cb.createQuery(AerodromiLetovi.class);
    Root<AerodromiLetovi> rt = cq.from(AerodromiLetovi.class);

    Predicate icaoPredicate = cb.equal(rt.get("airport").get("icao"), icao);
    cq.where(icaoPredicate);

    return em.createQuery(cq).getSingleResult();
  }

  public int count() {
    cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    Root<AerodromiLetovi> rt = cq.from(AerodromiLetovi.class);
    cq.select(cb.count(rt));
    Query q = em.createQuery(cq);
    return ((Long) q.getSingleResult()).intValue();
  }
}
