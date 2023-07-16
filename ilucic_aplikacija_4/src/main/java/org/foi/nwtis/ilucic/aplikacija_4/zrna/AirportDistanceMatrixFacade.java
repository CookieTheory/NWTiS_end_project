/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package org.foi.nwtis.ilucic.aplikacija_4.zrna;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.foi.nwtis.ilucic.aplikacija_4.jpa.AirportsDistanceMatrix;
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
 * @author Ivan Lucić
 */
@Stateless
public class AirportDistanceMatrixFacade {
  @PersistenceContext(unitName = "nwtis_dz3_pu")
  private EntityManager em;
  private CriteriaBuilder cb;

  @PostConstruct
  private void init() {
    System.out.println("AirportDistanceMatrixFacade- init");
    cb = em.getCriteriaBuilder();
  }

  public void create(AirportsDistanceMatrix distance) {
    em.persist(distance);
  }

  public void edit(AirportsDistanceMatrix distance) {
    em.merge(distance);
  }

  public void remove(AirportsDistanceMatrix distance) {
    em.remove(em.merge(distance));
  }

  public AirportsDistanceMatrix find(Object id) {
    return em.find(AirportsDistanceMatrix.class, id);
  }

  public List<AirportsDistanceMatrix> pronadiUdaljenosti(String icaoOd) {
    cb = em.getCriteriaBuilder();
    CriteriaQuery<AirportsDistanceMatrix> cq = cb.createQuery(AirportsDistanceMatrix.class);
    Root<AirportsDistanceMatrix> rt = cq.from(AirportsDistanceMatrix.class);
    cq.select(rt);
    Predicate prvi = cb.like(rt.get("airport1").get("icao"), "%" + icaoOd + "%");
    cq.where(prvi);

    TypedQuery<AirportsDistanceMatrix> q = em.createQuery(cq);
    List<AirportsDistanceMatrix> listaRezultata = q.getResultList();

    Map<String, AirportsDistanceMatrix> rucnoGrupiranje = new HashMap<>();
    // Mucio se 5 sati za ovo jer sam pretpostavio da ne smijemo koristiti Object[] kao za partial
    // class, distinct nije radio bez multiselecta koji je zahtjevao partial class (inace ukljuci
    // sve vrijednosti), groupBy imao isti problem s ostalim vrijednostima, mislim da ovo
    // zadovoljava uvjete zadace dok jos uvijek koristi criteria builder
    for (AirportsDistanceMatrix rezultat : listaRezultata) {
      String key = rezultat.getAirport2() + "_" + rezultat.getDistTot();
      if (rucnoGrupiranje.containsKey(key)) {
      } else {
        rucnoGrupiranje.put(key, rezultat);
      }
    }

    return new ArrayList<>(rucnoGrupiranje.values());
  }

  public List<AirportsDistanceMatrix> pronadiUdaljenost2Aerodroma(String icaoOd, String icaoDo) {
    cb = em.getCriteriaBuilder();
    CriteriaQuery<AirportsDistanceMatrix> cq = cb.createQuery(AirportsDistanceMatrix.class);
    Root<AirportsDistanceMatrix> rt = cq.from(AirportsDistanceMatrix.class);
    Predicate prvi = cb.like(rt.get("airport1").get("icao"), "%" + icaoOd + "%");
    Predicate drugi = cb.like(rt.get("airport2").get("icao"), "%" + icaoDo + "%");
    Predicate prviIDrugi = cb.and(prvi, drugi);
    cq.where(prviIDrugi);
    TypedQuery<AirportsDistanceMatrix> q = em.createQuery(cq);
    return q.getResultList();
  }

  public AirportsDistanceMatrix pronadiNajvecuUdaljenostAerodroma(String icaoOd) {
    cb = em.getCriteriaBuilder();
    CriteriaQuery<AirportsDistanceMatrix> cq = cb.createQuery(AirportsDistanceMatrix.class);
    Root<AirportsDistanceMatrix> rt = cq.from(AirportsDistanceMatrix.class);

    Subquery<Float> subquery = cq.subquery(Float.class);
    Root<AirportsDistanceMatrix> subqueryRoot = subquery.from(AirportsDistanceMatrix.class);
    subquery.select(cb.max(subqueryRoot.get("distCtry")))
        .where(cb.equal(subqueryRoot.get("airport1").get("icao"), icaoOd));

    cq.where(cb.and(cb.equal(rt.get("airport1").get("icao"), icaoOd),
        cb.equal(rt.get("distCtry"), subquery)));

    TypedQuery<AirportsDistanceMatrix> q = em.createQuery(cq).setMaxResults(1);
    return q.getSingleResult();
  }

  public int count() {
    cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    Root<AirportsDistanceMatrix> rt = cq.from(AirportsDistanceMatrix.class);
    cq.select(cb.count(rt));
    Query q = em.createQuery(cq);
    return ((Long) q.getSingleResult()).intValue();
  }
}
