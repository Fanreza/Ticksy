-- Ticksy Database Schema for PostgreSQL

CREATE TABLE roles (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE departments (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE categories (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE priorities (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE,
    level       INT NOT NULL DEFAULT 0,
    color       VARCHAR(7),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE statuses (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE users (
    id            BIGSERIAL PRIMARY KEY,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name     VARCHAR(100) NOT NULL,
    email         VARCHAR(100) NOT NULL UNIQUE,
    phone         VARCHAR(20),
    role_id       BIGINT NOT NULL REFERENCES roles(id),
    department_id BIGINT REFERENCES departments(id),
    active        BOOLEAN DEFAULT TRUE,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tickets (
    id              BIGSERIAL PRIMARY KEY,
    ticket_number   VARCHAR(20)  NOT NULL UNIQUE,
    title           VARCHAR(200) NOT NULL,
    description     TEXT,
    category_id     BIGINT NOT NULL REFERENCES categories(id),
    priority_id     BIGINT NOT NULL REFERENCES priorities(id),
    status_id       BIGINT NOT NULL REFERENCES statuses(id),
    created_by      BIGINT NOT NULL REFERENCES users(id),
    assigned_to     BIGINT REFERENCES users(id),
    closing_comment TEXT,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    closed_at       TIMESTAMP
);

-- Indexes
CREATE INDEX idx_tickets_status     ON tickets(status_id);
CREATE INDEX idx_tickets_assigned   ON tickets(assigned_to);
CREATE INDEX idx_tickets_created_by ON tickets(created_by);
CREATE INDEX idx_tickets_category   ON tickets(category_id);
CREATE INDEX idx_tickets_priority   ON tickets(priority_id);
CREATE INDEX idx_users_role         ON users(role_id);
CREATE INDEX idx_users_department   ON users(department_id);

-- Seed data
INSERT INTO roles (name, description) VALUES
    ('ADMIN', 'System Administrator'),
    ('AGENT', 'Support Agent'),
    ('USER',  'Regular User');

INSERT INTO statuses (name, description) VALUES
    ('OPEN',        'Ticket is open'),
    ('ASSIGNED',    'Ticket has been assigned to an agent'),
    ('IN_PROGRESS', 'Agent is working on the ticket'),
    ('RESOLVED',    'Ticket has been resolved'),
    ('CLOSED',      'Ticket is closed');

INSERT INTO priorities (name, level, color) VALUES
    ('LOW',      1, '#22c55e'),
    ('MEDIUM',   2, '#f59e0b'),
    ('HIGH',     3, '#f97316'),
    ('CRITICAL', 4, '#ef4444');

INSERT INTO categories (name, description) VALUES
    ('Hardware',    'Hardware related issues'),
    ('Software',    'Software related issues'),
    ('Network',     'Network and connectivity issues'),
    ('Account',     'Account and access issues'),
    ('Other',       'Other issues');

INSERT INTO departments (name, description) VALUES
    ('IT',          'Information Technology'),
    ('HR',          'Human Resources'),
    ('Finance',     'Finance Department'),
    ('Operations',  'Operations Department');
