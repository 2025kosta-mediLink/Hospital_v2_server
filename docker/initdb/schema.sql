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
CREATE TABLE `member` (
  `member_id` bigint NOT NULL AUTO_INCREMENT,
  `uuid` varchar(50) DEFAULT NULL,
  `login_id` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `name` varchar(50) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `gender` char(1) NOT NULL COMMENT 'M/F',
  `address` varchar(100) NOT NULL,
  `rrn` char(14) NOT NULL COMMENT '주민번호 (하이픈 포함)',
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `delete_at` datetime DEFAULT NULL,
  PRIMARY KEY (`member_id`),
  UNIQUE KEY `login_id` (`login_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 2) 진료과
-- ============================================
CREATE TABLE `department` (
  `department_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(60) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`department_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 3) 의사
-- ============================================
CREATE TABLE `doctor` (
  `doctor_id` bigint NOT NULL AUTO_INCREMENT,
  `department_id` bigint NOT NULL,
  `name` varchar(50) NOT NULL,
  `profile_image_url` varchar(255) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`doctor_id`),
  KEY `fk_doctor_department` (`department_id`),
  CONSTRAINT `fk_doctor_department` FOREIGN KEY (`department_id`) REFERENCES `department` (`department_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 4) 예약
-- ============================================
CREATE TABLE `reservation` (
  `reservation_id` bigint NOT NULL AUTO_INCREMENT,
  `member_id` bigint NOT NULL,
  `doctor_id` bigint NOT NULL,
  `reservation_no` varchar(20) NOT NULL,
  `appointment_at` datetime NOT NULL,
  `status` varchar(20) NOT NULL COMMENT '(''RESERVED'',''CANCELLED'',''DONE'')',
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`reservation_id`),
  KEY `fk_reservation_member` (`member_id`),
  KEY `fk_reservation_doctor` (`doctor_id`),
  CONSTRAINT `fk_reservation_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`doctor_id`),
  CONSTRAINT `fk_reservation_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 5) 접수 (예약 or 직접접수)
-- ============================================
CREATE TABLE `reception` (
  `reception_id` bigint NOT NULL AUTO_INCREMENT,
  `reservation_id` bigint DEFAULT NULL,
  `member_id` bigint NOT NULL,
  `doctor_id` bigint NOT NULL,
  `reception_no` varchar(20) NOT NULL,
  `type` varchar(20) NOT NULL COMMENT '(''RESERVATION'', ''DIRECT'')',
  `status` varchar(20) NOT NULL COMMENT '(''WAITING'',''IN_SERVICE'',''DONE'',''CANCELLED'')',
  `consent_notice` tinyint(1) NOT NULL,
  `consent_at` datetime NOT NULL,
  `note_to_doctor` varchar(500) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`reception_id`),
  UNIQUE KEY `reception_no` (`reception_no`),
  KEY `fk_reception_reservation` (`reservation_id`),
  KEY `fk_reception_member` (`member_id`),
  KEY `fk_reception_doctor` (`doctor_id`),
  CONSTRAINT `fk_reception_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`doctor_id`),
  CONSTRAINT `fk_reception_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`),
  CONSTRAINT `fk_reception_reservation` FOREIGN KEY (`reservation_id`) REFERENCES `reservation` (`reservation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 6) 처방전
-- ============================================
CREATE TABLE `prescription` (
  `prescription_id` bigint NOT NULL AUTO_INCREMENT,
  `reception_id` bigint NOT NULL,
  `doctor_id` bigint NOT NULL,
  `issued_at` datetime NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `pharmacy_name` varchar(100) DEFAULT NULL,
  `completed_date` datetime DEFAULT NULL,
  `completed` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`prescription_id`),
  KEY `fk_prescription_reception` (`reception_id`),
  KEY `fk_prescription_doctor` (`doctor_id`),
  CONSTRAINT `fk_prescription_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`doctor_id`),
  CONSTRAINT `fk_prescription_reception` FOREIGN KEY (`reception_id`) REFERENCES `reception` (`reception_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 7) 약국별 처방전
-- ============================================
CREATE TABLE `pharmacy_prescription` (
  `pharmacy_prescription_id` bigint NOT NULL AUTO_INCREMENT,
  `pharmacy_id` bigint DEFAULT NULL,
  `prescription_id` bigint NOT NULL,
  `pharmacy_name` varchar(100) DEFAULT NULL,
  `expected_finish_time` datetime DEFAULT NULL,
  `status` varchar(20) NOT NULL COMMENT '(''START'',''IN_PROGRESS'',''READY'',''DONE'')',
  `assigned_pharmacist` varchar(100) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`pharmacy_prescription_id`),
  KEY `fk_pp_prescription` (`prescription_id`),
  CONSTRAINT `fk_pp_prescription` FOREIGN KEY (`prescription_id`) REFERENCES `prescription` (`prescription_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 8) 수령 이력
-- ============================================
CREATE TABLE `pickup_history` (
  `pickup_id` bigint NOT NULL AUTO_INCREMENT,
  `pharmacy_prescription_id` bigint NOT NULL,
  `member_id` bigint NOT NULL,
  `pickup_at` datetime NOT NULL,
  `status` varchar(20) NOT NULL COMMENT '(''PICKED_UP'',''COMPLETED'')',
  `verified_by` varchar(100) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  PRIMARY KEY (`pickup_id`),
  KEY `fk_pickup_pharmacy_prescription` (`pharmacy_prescription_id`),
  KEY `fk_pickup_member` (`member_id`),
  CONSTRAINT `fk_pickup_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`),
  CONSTRAINT `fk_pickup_pharmacy_prescription` FOREIGN KEY (`pharmacy_prescription_id`) REFERENCES `pharmacy_prescription` (`pharmacy_prescription_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 9) 대기 티켓
-- ============================================
CREATE TABLE `waiting_ticket` (
  `ticket_id` bigint NOT NULL AUTO_INCREMENT,
  `reception_id` bigint NOT NULL,
  `queue_no` int NOT NULL,
  `status` varchar(20) NOT NULL COMMENT '(''WAITING'',''CALLED'',''IN_SERVICE'',''DONE'',''CANCELLED'',''SKIPPED'')',
  `estimated_wait_minutes` int DEFAULT NULL,
  `estimated_call_time` datetime DEFAULT NULL,
  `called_at` datetime DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`ticket_id`),
  KEY `fk_waiting_reception` (`reception_id`),
  CONSTRAINT `fk_waiting_reception` FOREIGN KEY (`reception_id`) REFERENCES `reception` (`reception_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 10) 증상
-- ============================================
CREATE TABLE `symptom` (
  `symptom_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`symptom_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 11) 접수-증상 관계
-- ============================================
CREATE TABLE `reception_symptom` (
  `reception_symptom_id` bigint NOT NULL AUTO_INCREMENT,
  `reception_id` bigint NOT NULL,
  `symptom_id` bigint NOT NULL,
  `created_at` datetime NOT NULL,
  PRIMARY KEY (`reception_symptom_id`),
  KEY `fk_rs_reception` (`reception_id`),
  KEY `fk_rs_symptom` (`symptom_id`),
  CONSTRAINT `fk_rs_reception` FOREIGN KEY (`reception_id`) REFERENCES `reception` (`reception_id`),
  CONSTRAINT `fk_rs_symptom` FOREIGN KEY (`symptom_id`) REFERENCES `symptom` (`symptom_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- 12) 의사 예외일 / 공지
-- ============================================
CREATE TABLE `doctor_exception_day` (
  `exception_id` bigint NOT NULL AUTO_INCREMENT,
  `doctor_id` bigint NOT NULL,
  `exception_date` date NOT NULL,
  `type` enum('CONFERENCE','EMERGENCY','OTHER','SICK_LEAVE','VACATION') NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`exception_id`),
  KEY `FK4u5kwwdudgwumyr6m8w4jxnqm` (`doctor_id`),
  CONSTRAINT `FK4u5kwwdudgwumyr6m8w4jxnqm` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`doctor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `doctor_notice` (
  `notice_id` bigint NOT NULL AUTO_INCREMENT,
  `doctor_id` bigint NOT NULL,
  `content` varchar(600) NOT NULL,
  `starts_at` datetime NOT NULL,
  `ends_at` datetime NOT NULL,
  `priority` int DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`notice_id`),
  KEY `fk_notice_doctor` (`doctor_id`),
  CONSTRAINT `fk_notice_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`doctor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ============================================
-- 13) 의사 스케줄
-- ============================================
CREATE TABLE `doctor_weekly_schedule` (
  `schedule_id` bigint NOT NULL AUTO_INCREMENT,
  `doctor_id` bigint NOT NULL,
  `day_of_week` tinyint NOT NULL,
  `am_flag` tinyint(1) NOT NULL,
  `pm_flag` tinyint(1) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`schedule_id`),
  KEY `fk_doctor_weekly_schedule_doctor` (`doctor_id`),
  CONSTRAINT `fk_doctor_weekly_schedule_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`doctor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
