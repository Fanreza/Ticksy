# Ticksy — IT Helpdesk Ticketing System

A modern JavaFX desktop app for managing IT support tickets, users, and reports. Built with MVC + Repository + Service architecture, dark mode UI, and role-based access control.

---

## Features

### Ticket Management
- Create, view, edit, and delete tickets
- Auto ticket numbering (`TKT-00001`)
- Assign tickets to agents
- Status workflow: `OPEN → ASSIGNED → IN_PROGRESS → RESOLVED → CLOSED`
- Priority levels with color coding (LOW, MEDIUM, HIGH, CRITICAL)
- Category classification and closing comments

### User Management
- Multi-role authentication (ADMIN, AGENT, USER)
- Full user CRUD with department assignment
- Account activation / deactivation

### Master Data *(Admin only)*
- Full CRUD for Categories, Departments, Priorities, Roles, and Statuses

### Reports *(Admin only)*
- Ticket Summary, Agent Performance, Resolution Time, Tickets Over Time, User Activity

### UI/UX
- Dark mode SaaS theme (Imperial Blue `#001D51` + Peach Yellow `#FFE3A5`)
- Smooth view transition animations, Material Design Icons
- Windows native dark title bar
- Dynamic menu and filtered views per role

---

## Prerequisites

| Requirement | Version |
|-------------|---------|
| Java (JDK) | 21+ |
| Apache Maven | 3.8+ |
| PostgreSQL | 13+ |

---

## Installation

```bash
# 1. Clone the repo
git clone <repository-url>
cd Tinksty

# 2. Create the database
psql -U postgres -c "CREATE DATABASE ticksy;"

# 3. Run the schema
psql -U postgres -d ticksy -f schema.sql
```

Update DB credentials in [src/main/resources/META-INF/persistence.xml](src/main/resources/META-INF/persistence.xml):

```xml
<property name="jakarta.persistence.jdbc.url"      value="jdbc:postgresql://localhost:5432/ticksy"/>
<property name="jakarta.persistence.jdbc.user"     value="postgres"/>
<property name="jakarta.persistence.jdbc.password" value="your_password"/>
```

```bash
# 4. Run
mvn javafx:run
```

**Default credentials:** `admin` / `admin123`

---

## Build (Portable)

```bash
mvn clean package
mvn jpackage:jpackage
```

Output: `target/dist/Ticksy/Ticksy.exe`

---

## Tech Stack

| Layer | Technology |
|-------|------------|
| UI | JavaFX 21 + AtlantaFX + ControlsFX + Ikonli |
| ORM | Hibernate 6.4 + Jakarta JPA 3.1 |
| Database | PostgreSQL + HikariCP |
| Logging | SLF4J + Logback |
| Build | Apache Maven |

---

## Roles & Permissions

| Feature | ADMIN | AGENT | USER |
|---------|:-----:|:-----:|:----:|
| Dashboard | ✓ | ✓ | |
| All tickets | ✓ | ✓ | |
| Own tickets | ✓ | ✓ | ✓ |
| Create ticket | ✓ | ✓ | ✓ |
| Assign ticket | ✓ | ✓ | |
| Master data | ✓ | | |
| User management | ✓ | | |
| Reports | ✓ | | |
