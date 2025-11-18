-- ============================================
-- 🏥 HOSPITAL DATABASE (REALISTIC DUMMY VERSION)
-- ============================================

-- ⚙️ 0. 초기화
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP DATABASE IF EXISTS hospital;
CREATE DATABASE hospital
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;
USE hospital;

-- ============================================
-- 1) 회원 테이블 (기본 인적 정보)
-- ============================================
CREATE TABLE member (
    member_id   BIGINT NOT NULL AUTO_INCREMENT,
    uuid        VARCHAR(50),
    login_id    VARCHAR(50) NOT NULL UNIQUE,
    password    VARCHAR(100) NOT NULL,
    name        VARCHAR(50) NOT NULL,
    phone       VARCHAR(20) NOT NULL,
    gender      CHAR(1) NOT NULL COMMENT 'M/F',
    address     VARCHAR(100) NOT NULL,
    rrn         CHAR(14) NOT NULL COMMENT '주민번호 (하이픈 포함)',
    created_at  DATETIME NOT NULL,
    updated_at  DATETIME NOT NULL,
    delete_at   DATETIME NULL,
    PRIMARY KEY (member_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 2) 진료과
-- ============================================
CREATE TABLE department (
    department_id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(60) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    PRIMARY KEY (department_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 3) 의사
-- ============================================
CREATE TABLE doctor (
    doctor_id BIGINT NOT NULL AUTO_INCREMENT,
    department_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL,
    profile_image_url VARCHAR(255) DEFAULT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    PRIMARY KEY (doctor_id),
    CONSTRAINT fk_doctor_department FOREIGN KEY (department_id) REFERENCES department(department_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 4) 예약
-- ============================================
CREATE TABLE reservation (
    reservation_id BIGINT NOT NULL AUTO_INCREMENT,
    member_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    reservation_no VARCHAR(20) NOT NULL,
    appointment_at DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL COMMENT "('RESERVED','CANCELLED','DONE','NO_SHOW')",
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL, 
    PRIMARY KEY (reservation_id),
    CONSTRAINT fk_reservation_member FOREIGN KEY (member_id) REFERENCES member(member_id),
    CONSTRAINT fk_reservation_doctor FOREIGN KEY (doctor_id) REFERENCES doctor(doctor_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 5) 접수 (예약 or 직접접수)
-- ============================================
CREATE TABLE reception (
    reception_id BIGINT NOT NULL AUTO_INCREMENT,
    reservation_id BIGINT NULL,
    member_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    reception_no INT NOT NULL,
    type VARCHAR(20) NOT NULL COMMENT "('RESERVATION', 'DIRECT')",
    status VARCHAR(20) NOT NULL COMMENT "('WAITING','IN_SERVICE','DONE','CANCELLED')",
    consent_notice TINYINT(1) NOT NULL,
    consent_at DATETIME NOT NULL,
    note_to_doctor VARCHAR(500),
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    PRIMARY KEY (reception_id),
    CONSTRAINT fk_reception_reservation FOREIGN KEY (reservation_id) REFERENCES reservation(reservation_id),
    CONSTRAINT fk_reception_member FOREIGN KEY (member_id) REFERENCES member(member_id),
    CONSTRAINT fk_reception_doctor FOREIGN KEY (doctor_id) REFERENCES doctor(doctor_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 6) 처방전
-- ============================================
CREATE TABLE prescription (
    prescription_id BIGINT NOT NULL AUTO_INCREMENT,
    reception_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    issued_at DATETIME NOT NULL,
    content VARCHAR(255),
    pharmacy_name VARCHAR(100) DEFAULT NULL,
    completed_date DATETIME DEFAULT NULL,
    completed TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    PRIMARY KEY (prescription_id),
    CONSTRAINT fk_prescription_reception FOREIGN KEY (reception_id) REFERENCES reception(reception_id),
    CONSTRAINT fk_prescription_doctor FOREIGN KEY (doctor_id) REFERENCES doctor(doctor_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 7) 약국별 처방전 
-- ============================================
CREATE TABLE pharmacy_prescription (
    pharmacy_prescription_id BIGINT NOT NULL AUTO_INCREMENT,
    pharmacy_id BIGINT NULL,  -- NULL 허용 (API에서 가져올 예정)
    prescription_id BIGINT NOT NULL,
    pharmacy_name VARCHAR(100),  -- API에서 선택한 약국이름
    expected_finish_time DATETIME,
    status VARCHAR(20) NOT NULL COMMENT "('START','IN_PROGRESS','READY','DONE')",
    assigned_pharmacist VARCHAR(100),
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    PRIMARY KEY (pharmacy_prescription_id),
    CONSTRAINT fk_pp_prescription FOREIGN KEY (prescription_id) REFERENCES prescription(prescription_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 8) 수령 이력
-- ============================================
CREATE TABLE pickup_history (
    pickup_id BIGINT NOT NULL AUTO_INCREMENT,
    pharmacy_prescription_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,
    pickup_at DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL COMMENT "('PICKED_UP','COMPLETED')",
    verified_by VARCHAR(100),
    created_at DATETIME NOT NULL,
    PRIMARY KEY (pickup_id),
    CONSTRAINT fk_pickup_pharmacy_prescription FOREIGN KEY (pharmacy_prescription_id) REFERENCES pharmacy_prescription(pharmacy_prescription_id),
    CONSTRAINT fk_pickup_member FOREIGN KEY (member_id) REFERENCES member(member_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 9) 대기 티켓
-- ============================================
CREATE TABLE waiting_ticket (
    ticket_id BIGINT NOT NULL AUTO_INCREMENT,
    reception_id BIGINT NOT NULL,
    queue_no INT NOT NULL,
    status VARCHAR(20) NOT NULL COMMENT "('WAITING','CALLED','IN_SERVICE','DONE','CANCELLED','SKIPPED')",
    estimated_wait_minutes INT,
    estimated_call_time DATETIME,
    called_at DATETIME,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    PRIMARY KEY (ticket_id),
    CONSTRAINT fk_waiting_reception FOREIGN KEY (reception_id) REFERENCES reception(reception_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 10) 증상
-- ============================================
CREATE TABLE symptom (
    symptom_id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    PRIMARY KEY (symptom_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 11) 접수-증상 관계
-- ============================================
CREATE TABLE reception_symptom (
    reception_symptom_id BIGINT NOT NULL AUTO_INCREMENT,
    reception_id BIGINT NOT NULL,
    symptom_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL,
    PRIMARY KEY (reception_symptom_id),
    CONSTRAINT fk_rs_reception FOREIGN KEY (reception_id) REFERENCES reception(reception_id),
    CONSTRAINT fk_rs_symptom FOREIGN KEY (symptom_id) REFERENCES symptom(symptom_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 12) 의사 예외일 / 공지
-- ============================================
CREATE TABLE doctor_exception_day (
    exception_id BIGINT NOT NULL AUTO_INCREMENT,
    doctor_id BIGINT NOT NULL,
    exception_date DATE NOT NULL,
    type VARCHAR(20) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    PRIMARY KEY (exception_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE doctor_notice (
    notice_id BIGINT NOT NULL AUTO_INCREMENT,
    doctor_id BIGINT NOT NULL,
    content VARCHAR(600) NOT NULL,
    starts_at DATETIME NOT NULL,
    ends_at DATETIME NOT NULL,
    priority INT,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    PRIMARY KEY (notice_id),
    CONSTRAINT fk_notice_doctor FOREIGN KEY (doctor_id) REFERENCES doctor(doctor_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 13) 의사 스케줄
-- ============================================
CREATE TABLE doctor_weekly_schedule (
    schedule_id BIGINT NOT NULL AUTO_INCREMENT,
    doctor_id BIGINT NOT NULL,
    day_of_week TINYINT NOT NULL,
    am_flag TINYINT(1) NOT NULL,
    pm_flag TINYINT(1) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    PRIMARY KEY (schedule_id),
    CONSTRAINT fk_doctor_weekly_schedule_doctor FOREIGN KEY (doctor_id) REFERENCES doctor(doctor_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;