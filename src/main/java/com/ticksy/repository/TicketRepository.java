package com.ticksy.repository;

import com.ticksy.config.HibernateUtil;
import com.ticksy.model.Ticket;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class TicketRepository extends GenericRepository<Ticket> {

    public TicketRepository() {
        super(Ticket.class);
    }

    public List<Ticket> searchByKeyword(String keyword) {
        return findByQuery(
                "SELECT t FROM Ticket t WHERE LOWER(t.title) LIKE LOWER(?1) OR LOWER(t.ticketNumber) LIKE LOWER(?1)",
                "%" + keyword + "%");
    }

    public List<Ticket> findByStatusName(String statusName) {
        return findByQuery("SELECT t FROM Ticket t WHERE t.status.name = ?1", statusName);
    }

    public List<Ticket> findByAssignedTo(Long userId) {
        return findByQuery("SELECT t FROM Ticket t WHERE t.assignedTo.id = ?1", userId);
    }

    public List<Ticket> findUnassigned() {
        return executeInTransaction(em -> {
            TypedQuery<Ticket> query = em.createQuery(
                    "SELECT t FROM Ticket t WHERE t.assignedTo IS NULL AND t.status.name = 'OPEN'",
                    Ticket.class);
            return query.getResultList();
        });
    }

    public String generateTicketNumber() {
        return executeInTransaction(em -> {
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(t) FROM Ticket t", Long.class);
            long count = query.getSingleResult() + 1;
            return String.format("TKT-%05d", count);
        });
    }

    // --- Report Queries ---

    public List<Object[]> getTicketCountByStatus() {
        return executeInTransaction(em ->
                em.createQuery("SELECT t.status.name, COUNT(t) FROM Ticket t GROUP BY t.status.name",
                        Object[].class).getResultList());
    }

    public List<Object[]> getTicketCountByPriority() {
        return executeInTransaction(em ->
                em.createQuery("SELECT t.priority.name, COUNT(t) FROM Ticket t GROUP BY t.priority.name",
                        Object[].class).getResultList());
    }

    public List<Object[]> getTicketCountByCategory() {
        return executeInTransaction(em ->
                em.createQuery("SELECT t.category.name, COUNT(t) FROM Ticket t GROUP BY t.category.name",
                        Object[].class).getResultList());
    }

    public List<Object[]> getAgentPerformance() {
        return executeInTransaction(em ->
                em.createQuery(
                        "SELECT t.assignedTo.fullName, t.status.name, COUNT(t) " +
                                "FROM Ticket t WHERE t.assignedTo IS NOT NULL " +
                                "GROUP BY t.assignedTo.fullName, t.status.name " +
                                "ORDER BY t.assignedTo.fullName",
                        Object[].class).getResultList());
    }

    public List<Object[]> getAverageResolutionTime() {
        return executeInTransaction(em ->
                em.createQuery(
                        "SELECT t.assignedTo.fullName, " +
                                "AVG(EXTRACT(EPOCH FROM (t.closedAt - t.createdAt)) / 3600) " +
                                "FROM Ticket t WHERE t.closedAt IS NOT NULL AND t.assignedTo IS NOT NULL " +
                                "GROUP BY t.assignedTo.fullName",
                        Object[].class).getResultList());
    }

    public List<Object[]> getTicketsCreatedOverTime() {
        return executeInTransaction(em ->
                em.createQuery(
                        "SELECT CAST(t.createdAt AS DATE), COUNT(t) " +
                                "FROM Ticket t GROUP BY CAST(t.createdAt AS DATE) " +
                                "ORDER BY CAST(t.createdAt AS DATE)",
                        Object[].class).getResultList());
    }

    public List<Object[]> getUserActivity() {
        return executeInTransaction(em ->
                em.createQuery(
                        "SELECT t.createdBy.fullName, COUNT(t) " +
                                "FROM Ticket t GROUP BY t.createdBy.fullName " +
                                "ORDER BY COUNT(t) DESC",
                        Object[].class).getResultList());
    }
}
