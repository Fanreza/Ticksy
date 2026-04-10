package com.ticksy.repository;

import com.ticksy.config.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class GenericRepository<T> {

    private final Class<T> entityClass;

    public GenericRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public List<T> findAll() {
        return executeInTransaction(em -> {
            TypedQuery<T> query = em.createQuery(
                    "SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass);
            return query.getResultList();
        });
    }

    public Optional<T> findById(Long id) {
        return executeInTransaction(em -> Optional.ofNullable(em.find(entityClass, id)));
    }

    public T save(T entity) {
        return executeInTransaction(em -> em.merge(entity));
    }

    public void delete(T entity) {
        executeVoidTransaction(em -> {
            T managed = em.merge(entity);
            em.remove(managed);
        });
    }

    public void deleteById(Long id) {
        executeVoidTransaction(em -> {
            T entity = em.find(entityClass, id);
            if (entity != null) {
                em.remove(entity);
            }
        });
    }

    public List<T> findByQuery(String jpql, Object... params) {
        return executeInTransaction(em -> {
            TypedQuery<T> query = em.createQuery(jpql, entityClass);
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i + 1, params[i]);
            }
            return query.getResultList();
        });
    }

    public long count() {
        return executeInTransaction(em -> {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e", Long.class);
            return query.getSingleResult();
        });
    }

    protected <R> R executeInTransaction(Function<EntityManager, R> action) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            R result = action.apply(em);
            tx.commit();
            return result;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Transaction failed: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    protected void executeVoidTransaction(Consumer<EntityManager> action) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            action.accept(em);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Transaction failed: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
}
