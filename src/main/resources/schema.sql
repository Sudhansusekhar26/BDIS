-- ===================================================================
-- Blood Donation Management and Inventory Control System (BDIS)
-- MySQL 8.x Normalized DDL Schema Definition
-- ===================================================================

DROP TABLE IF EXISTS audit_logs;
DROP TABLE IF EXISTS donor_pledges;
DROP TABLE IF EXISTS blood_deliveries;
DROP TABLE IF EXISTS blood_requests;
DROP TABLE IF EXISTS blood_inventory;
DROP TABLE IF EXISTS hospital_profiles;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS location_districts;
DROP TABLE IF EXISTS location_states;

-- 1. Location Hierarchy Tables
CREATE TABLE location_states (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    code VARCHAR(10) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE location_districts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    state_id BIGINT NOT NULL,
    CONSTRAINT fk_districts_state FOREIGN KEY (state_id) REFERENCES location_states(id) ON DELETE CASCADE,
    CONSTRAINT uq_state_district UNIQUE (state_id, name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Core Users Table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    role VARCHAR(30) NOT NULL,
    blood_group VARCHAR(20),
    state_id BIGINT,
    district_id BIGINT,
    address VARCHAR(300),
    is_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_users_state FOREIGN KEY (state_id) REFERENCES location_states(id) ON DELETE SET NULL,
    CONSTRAINT fk_users_district FOREIGN KEY (district_id) REFERENCES location_districts(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Hospital Profiles Table
CREATE TABLE hospital_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    hospital_name VARCHAR(200) NOT NULL,
    license_number VARCHAR(100) NOT NULL UNIQUE,
    is_verified BOOLEAN NOT NULL DEFAULT FALSE,
    contact_person VARCHAR(150) NOT NULL,
    emergency_helpline VARCHAR(50) NOT NULL,
    state_id BIGINT NOT NULL,
    district_id BIGINT NOT NULL,
    address VARCHAR(300) NOT NULL,
    total_beds INT DEFAULT 0,
    CONSTRAINT fk_hospital_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_hospital_state FOREIGN KEY (state_id) REFERENCES location_states(id),
    CONSTRAINT fk_hospital_district FOREIGN KEY (district_id) REFERENCES location_districts(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Blood Inventory Table
CREATE TABLE blood_inventory (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    hospital_id BIGINT NOT NULL,
    blood_group VARCHAR(20) NOT NULL,
    component_type VARCHAR(30) NOT NULL,
    units_available INT NOT NULL DEFAULT 0,
    expiry_date DATE,
    batch_number VARCHAR(100) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_inventory_hospital FOREIGN KEY (hospital_id) REFERENCES hospital_profiles(id) ON DELETE CASCADE,
    CONSTRAINT uq_hospital_stock_batch UNIQUE (hospital_id, blood_group, component_type, batch_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Blood Requests Table
CREATE TABLE blood_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    requester_id BIGINT NOT NULL,
    hospital_id BIGINT NOT NULL,
    patient_name VARCHAR(150) NOT NULL,
    admitted_hospital_name VARCHAR(200) NOT NULL,
    blood_group VARCHAR(20) NOT NULL,
    component_type VARCHAR(30) NOT NULL,
    units_requested INT NOT NULL,
    urgency_level VARCHAR(30) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    delivery_address VARCHAR(300) NOT NULL,
    prescription_notes TEXT,
    rejection_reason VARCHAR(300),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_requests_user FOREIGN KEY (requester_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_requests_hospital FOREIGN KEY (hospital_id) REFERENCES hospital_profiles(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. Blood Delivery Table
CREATE TABLE blood_deliveries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    request_id BIGINT NOT NULL UNIQUE,
    delivery_partner_name VARCHAR(150) NOT NULL,
    delivery_contact VARCHAR(50) NOT NULL,
    vehicle_number VARCHAR(50),
    tracking_notes TEXT,
    dispatched_at TIMESTAMP NULL DEFAULT NULL,
    delivered_at TIMESTAMP NULL DEFAULT NULL,
    current_status VARCHAR(30) NOT NULL DEFAULT 'ASSIGNED',
    CONSTRAINT fk_deliveries_request FOREIGN KEY (request_id) REFERENCES blood_requests(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. Donor Pledges Table
CREATE TABLE donor_pledges (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    donor_id BIGINT NOT NULL,
    hospital_id BIGINT,
    pledged_date DATE NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PLEDGED',
    units_donated INT DEFAULT 1,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_pledges_donor FOREIGN KEY (donor_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_pledges_hospital FOREIGN KEY (hospital_id) REFERENCES hospital_profiles(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 8. Audit Logs Table
CREATE TABLE audit_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    action VARCHAR(100) NOT NULL,
    performed_by VARCHAR(150) NOT NULL,
    entity_name VARCHAR(100) NOT NULL,
    entity_id VARCHAR(100),
    details TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Indexes for high performance searches
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_inventory_lookup ON blood_inventory(blood_group, component_type, units_available);
CREATE INDEX idx_requests_status ON blood_requests(status);
CREATE INDEX idx_requests_hospital ON blood_requests(hospital_id, status);
CREATE INDEX idx_hospital_verification ON hospital_profiles(is_verified);
