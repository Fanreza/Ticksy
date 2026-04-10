# Ticksy — IT Helpdesk Ticketing System

Aplikasi desktop helpdesk modern berbasis JavaFX untuk mengelola tiket dukungan IT, manajemen pengguna, dan pelaporan. Dibangun dengan arsitektur berlapis (MVC + Repository + Service), tema dark mode, dan kontrol akses berbasis peran (RBAC).

---

## Fitur

### Manajemen Tiket
- Buat, lihat, edit, dan hapus tiket
- Penomoran tiket otomatis format `TKT-00001`
- Assign tiket ke agen
- Pelacakan status dengan alur kerja: `OPEN → ASSIGNED → IN_PROGRESS → RESOLVED → CLOSED`
- Level prioritas dengan kode warna (LOW, MEDIUM, HIGH, CRITICAL)
- Klasifikasi berdasarkan kategori
- Komentar penutupan beserta timestamp

### Manajemen Pengguna
- Autentikasi multi-peran (ADMIN, AGENT, USER)
- CRUD pengguna lengkap
- Penugasan departemen
- Aktivasi / nonaktivasi akun

### Data Master *(khusus Admin)*
- Kategori, Departemen, Prioritas, Peran, Status
- CRUD penuh untuk setiap entitas master

### Laporan *(khusus Admin)*
- **Ringkasan Tiket** — jumlah per status, prioritas, dan kategori
- **Performa Agen** — metrik kinerja agen
- **Waktu Resolusi** — analisis durasi penyelesaian tiket
- **Tiket dari Waktu ke Waktu** — tren volume tiket
- **Aktivitas Pengguna** — rekam jejak aktivitas pengguna

### UI/UX
- Tema dark mode SaaS modern (Imperial Blue `#001D51` + Peach Yellow `#FFE3A5`)
- Animasi transisi halus saat berpindah tampilan
- Ikon Material Design (Ikonli)
- Dukungan dark title bar Windows native
- Menu dinamis berdasarkan peran pengguna
- USER hanya melihat tiket miliknya sendiri; ADMIN/AGENT mendapat akses penuh

---

## Prasyarat

| Kebutuhan | Versi Minimum |
|-----------|---------------|
| Java (JDK) | 21 |
| Apache Maven | 3.8+ |
| PostgreSQL | 13+ |

> Pastikan `JAVA_HOME` sudah mengarah ke JDK 21 dan `mvn` tersedia di PATH.

---

## Instalasi & Menjalankan Aplikasi

### 1. Clone repositori

```bash
git clone <url-repositori>
cd Tinksty
```

### 2. Buat database PostgreSQL

```sql
CREATE DATABASE ticksy;
```

### 3. Jalankan schema database

```bash
psql -U postgres -d ticksy -f schema.sql
```

Script ini akan membuat semua tabel sekaligus mengisi data awal (roles, status, prioritas, kategori, departemen, dan akun admin default).

### 4. Konfigurasi koneksi database

Buka file [src/main/resources/META-INF/persistence.xml](src/main/resources/META-INF/persistence.xml) dan sesuaikan properti berikut:

```xml
<property name="jakarta.persistence.jdbc.url"      value="jdbc:postgresql://localhost:5432/ticksy"/>
<property name="jakarta.persistence.jdbc.user"     value="postgres"/>
<property name="jakarta.persistence.jdbc.password" value="123qweasd"/>
```

### 5. Jalankan aplikasi

```bash
mvn javafx:run
```

### Login default

| Username | Password | Peran |
|----------|----------|-------|
| `admin`  | `admin123` | ADMIN |

> Segera ganti password admin setelah login pertama kali.

---

## Struktur Proyek

```
Tinksty/
├── pom.xml                        # Konfigurasi Maven
├── schema.sql                     # Schema database + seed data
└── src/main/
    ├── java/com/ticksy/
    │   ├── TicksyApplication.java # Entry point utama
    │   ├── config/                # Konfigurasi Hibernate & seeder
    │   ├── controller/            # Controller MVC (26 kelas)
    │   │   ├── dashboard/
    │   │   ├── master/
    │   │   ├── transaction/
    │   │   └── report/
    │   ├── model/                 # JPA Entity (8 kelas)
    │   ├── service/               # Business logic (7 kelas)
    │   ├── repository/            # Data access layer (8 kelas)
    │   └── util/                  # Helper: Session, Navigator, Alert
    └── resources/
        ├── fxml/                  # Tampilan UI (24 file FXML)
        ├── css/styles.css         # Tema dark mode
        └── META-INF/
            └── persistence.xml   # Konfigurasi JPA
```

---

## Tech Stack

| Layer | Teknologi |
|-------|-----------|
| UI Framework | JavaFX 21.0.2 |
| Tema UI | AtlantaFX 2.0.1 (CupertinoDark) |
| Komponen Tambahan | ControlsFX 11.2.1 |
| Ikon | Ikonli 12.3.1 (Material Design) |
| ORM | Hibernate 6.4.4 + Jakarta JPA 3.1 |
| Database | PostgreSQL |
| Connection Pool | HikariCP (via Hibernate) |
| Logging | SLF4J + Logback 1.4.14 |
| Windows Native | JNA 5.14.0 |
| Build Tool | Apache Maven |

---

## Peran & Hak Akses

| Fitur | ADMIN | AGENT | USER |
|-------|:-----:|:-----:|:----:|
| Dashboard | ✓ | ✓ | |
| Semua tiket | ✓ | ✓ | |
| Tiket sendiri | ✓ | ✓ | ✓ |
| Buat tiket | ✓ | ✓ | ✓ |
| Assign tiket | ✓ | ✓ | |
| Data master | ✓ | | |
| Manajemen user | ✓ | | |
| Laporan | ✓ | | |
