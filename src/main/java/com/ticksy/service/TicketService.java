package com.ticksy.service;

import com.ticksy.model.Status;
import com.ticksy.model.Ticket;
import com.ticksy.model.User;
import com.ticksy.repository.StatusRepository;
import com.ticksy.repository.TicketRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class TicketService {

    private final TicketRepository ticketRepository = new TicketRepository();
    private final StatusRepository statusRepository = new StatusRepository();

    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> findById(Long id) {
        return ticketRepository.findById(id);
    }

    public Ticket createTicket(Ticket ticket) {
        ticket.setTicketNumber(ticketRepository.generateTicketNumber());
        Status openStatus = statusRepository.searchByName("OPEN").stream().findFirst()
                .orElseThrow(() -> new RuntimeException("OPEN status not found"));
        ticket.setStatus(openStatus);
        return ticketRepository.save(ticket);
    }

    public Ticket save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public void delete(Ticket ticket) {
        ticketRepository.delete(ticket);
    }

    public List<Ticket> search(String keyword) {
        if (keyword == null || keyword.isBlank()) return findAll();
        return ticketRepository.searchByKeyword(keyword);
    }

    public Ticket assignTicket(Ticket ticket, User agent) {
        ticket.setAssignedTo(agent);
        Status assignedStatus = statusRepository.searchByName("ASSIGNED").stream().findFirst()
                .orElseThrow(() -> new RuntimeException("ASSIGNED status not found"));
        ticket.setStatus(assignedStatus);
        return ticketRepository.save(ticket);
    }

    public Ticket updateStatus(Ticket ticket, String statusName) {
        Status status = statusRepository.searchByName(statusName).stream().findFirst()
                .orElseThrow(() -> new RuntimeException(statusName + " status not found"));
        ticket.setStatus(status);
        return ticketRepository.save(ticket);
    }

    public Ticket closeTicket(Ticket ticket, String closingComment) {
        ticket.setClosingComment(closingComment);
        ticket.setClosedAt(LocalDateTime.now());
        Status closedStatus = statusRepository.searchByName("CLOSED").stream().findFirst()
                .orElseThrow(() -> new RuntimeException("CLOSED status not found"));
        ticket.setStatus(closedStatus);
        return ticketRepository.save(ticket);
    }

    public List<Ticket> findUnassigned() {
        return ticketRepository.findUnassigned();
    }

    // Report methods
    public List<Object[]> getTicketCountByStatus() { return ticketRepository.getTicketCountByStatus(); }
    public List<Object[]> getTicketCountByPriority() { return ticketRepository.getTicketCountByPriority(); }
    public List<Object[]> getTicketCountByCategory() { return ticketRepository.getTicketCountByCategory(); }
    public List<Object[]> getAgentPerformance() { return ticketRepository.getAgentPerformance(); }
    public List<Object[]> getAverageResolutionTime() { return ticketRepository.getAverageResolutionTime(); }
    public List<Object[]> getTicketsCreatedOverTime() { return ticketRepository.getTicketsCreatedOverTime(); }
    public List<Object[]> getUserActivity() { return ticketRepository.getUserActivity(); }

    public long count() { return ticketRepository.count(); }
}
