-- ============================================
-- 🏥 HOSPITAL DATABASE - FULL INIT DATA (FINAL, 50명의 의사를 20개 진료과에 재배치)
-- ============================================

USE hospital;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================
-- 1) 회원 (30명) - 비밀번호: test1234 (BCrypt)
-- ============================================
INSERT INTO member (uuid, login_id, password, name, phone, gender, address, rrn, created_at, updated_at) VALUES
('UUID-001', 'minji88', '$2b$10$WpWykhYAbASkIKqVUmP39eX53sEBEqBKLqo0g0lBZrbqgG6.DKbje', '이민지', '010-2345-7811', 'F', '서울시 송파구 잠실동', '880512-2234567', NOW(), NOW()),
('UUID-002', 'kangjh', '$2b$10$WpWykhYAbASkIKqVUmP39eX53sEBEqBKLqo0g0lBZrbqgG6.DKbje', '강지훈', '010-3421-9900', 'M', '서울시 강남구 역삼동', '900318-1234567', NOW(), NOW()),
('UUID-003', 'hanjy', '$2b$10$WpWykhYAbASkIKqVUmP39eX53sEBEqBKLqo0g0lBZrbqgG6.DKbje', '한지영', '010-8822-7733', 'F', '경기도 성남시 분당구', '950120-2345678', NOW(), NOW()),
('UUID-004', 'parkdh', '$2b$10$WpWykhYAbASkIKqVUmP39eX53sEBEqBKLqo0g0lBZrbqgG6.DKbje', '박동현', '010-6677-5599', 'M', '서울시 영등포구 여의도동', '870915-1239876', NOW(), NOW()),
('UUID-005', 'choiyr', '$2b$10$WpWykhYAbASkIKqVUmP39eX53sEBEqBKLqo0g0lBZrbqgG6.DKbje', '최유라', '010-8787-2344', 'F', '서울시 마포구 서교동', '970621-2348765', NOW(), NOW()),
('UUID-006', 'songhm', '$2b$10$WpWykhYAbASkIKqVUmP39eX53sEBEqBKLqo0g0lBZrbqgG6.DKbje', '송현민', '010-5522-3311', 'M', '서울시 노원구 공릉동', '920225-1233211', NOW(), NOW()),
('UUID-007', 'juhk',  '$2b$10$WpWykhYAbASkIKqVUmP39eX53sEBEqBKLqo0g0lBZrbqgG6.DKbje', '주하경', '010-7711-6622', 'F', '경기도 고양시 일산동구', '980904-2234455', NOW(), NOW()),
('UUID-008', 'baekh', '$2b$10$WpWykhYAbASkIKqVUmP39eX53sEBEqBKLqo0g0lBZrbqgG6.DKbje', '백현우', '010-9955-8855', 'M', '서울시 강서구 화곡동', '940701-1345790', NOW(), NOW()),
('UUID-009', 'yoonse','$2b$10$WpWykhYAbASkIKqVUmP39eX53sEBEqBKLqo0g0lBZrbqgG6.DKbje', '윤서은', '010-4444-1111', 'F', '인천시 부평구 부개동', '010101-4234567', NOW(), NOW()),
('UUID-010', 'jangms','$2b$10$WpWykhYAbASkIKqVUmP39eX53sEBEqBKLqo0g0lBZrbqgG6.DKbje', '장민석', '010-6666-3434', 'M', '서울시 성북구 길음동', '890210-1334567', NOW(), NOW()),
('UUID-011', 'kimhj', '$2b$10$WpWykhYAbASkIKqVUmP39eX53sEBEqBKLqo0g0lBZrbqgG6.DKbje', '김현지', '010-2222-9999', 'F', '서울시 도봉구 창동', '970215-2234567', NOW(), NOW()),
('UUID-012', 'leehj', '$2b$10$WpWykhYAbASkIKqVUmP39eX53sEBEqBKLqo0g0lBZrbqgG6.DKbje', '이하준', '010-8888-2222', 'M', '서울시 강북구 수유동', '960215-1234567', NOW(), NOW()),
('UUID-013', 'moonhj','$2b$10$WpWykhYAbASkIKqVUmP39eX53sEBEqBKLqo0g0lBZrbqgG6.DKbje', '문하정', '010-6543-7654', 'F', '서울시 관악구 봉천동', '990615-2234567', NOW(), NOW()),
('UUID-014', 'jooyj', '$2b$10$WpWykhYAbASkIKqVUmP39eX53sEBEqBKLqo0g0lBZrbqgG6.DKbje', '주유진', '010-5432-9999', 'F', '경기도 용인시 죽전동', '980915-2234567', NOW(), NOW()),
('UUID-015', 'parkjr','$2b$10$WpWykhYAbASkIKqVUmP39eX53sEBEqBKLqo0g0lBZrbqgG6.DKbje', '박진우', '010-9988-2233', 'M', '서울시 서초구 방배동', '940112-1334567', NOW(), NOW()),
('UUID-016', 'choyh', '$2b$10$WpWykhYAbASkIKqVUmP39eX53sEBEqBKLqo0g0lBZrbqgG6.DKbje', '최윤호', '010-3333-1212', 'M', '서울시 은평구 응암동', '910320-1334567', NOW(), NOW()),
('UUID-017', 'songy', '$2b$10$WpWykhYAbASkIKqVUmP39eX53sEBEqBKLqo0g0lBZrbqgG6.DKbje', '송유진', '010-9999-5555', 'F', '서울시 강동구 천호동', '990522-2234567', NOW(), NOW()),
('UUID-018', 'anjs',  '$2b$10$WpWykhYAbASkIKqVUmP39eX53sEBEqBKLqo0g0lBZrbqgG6.DKbje', '안지수', '010-8787-2222', 'F', '서울시 강서구 화곡동', '010310-4234567', NOW(), NOW()),
('UUID-019', 'ryuhj', '$2b$10$WpWykhYAbASkIKqVUmP39eX53sEBEqBKLqo0g0lBZrbqgG6.DKbje', '류하진', '010-8888-9999', 'M', '서울시 구로구 고척동', '950122-1334567', NOW(), NOW()),
('UUID-020', 'kimms', '$2b$10$WpWykhYAbASkIKqVUmP39eX53sEBEqBKLqo0g0lBZrbqgG6.DKbje', '김민성', '010-2222-3333', 'M', '서울시 성동구 행당동', '980111-1334567', NOW(), NOW()),
('UUID-021', 'hanmj', '$2b$10$WpWykhYAbASkIKqVUmP39eX53sEBEqBKLqo0g0lBZrbqgG6.DKbje', '한민정', '010-2233-8899', 'F', '서울시 구로구 신도림동', '960821-2434567', NOW(), NOW()),
('UUID-022', 'leejy', '$2b$10$WpWykhYAbASkIKqVUmP39eX53sEBEqBKLqo0g0lBZrbqgG6.DKbje', '이재연', '010-6655-4433', 'F', '경기도 성남시 수정구', '000430-4234567', NOW(), NOW()),
('UUID-023', 'parkys','$2b$10$WpWykhYAbASkIKqVUmP39eX53sEBEqBKLqo0g0lBZrbqgG6.DKbje', '박예슬', '010-8888-9999', 'F', '경기도 김포시 운양동', '970319-2234567', NOW(), NOW()),
('UUID-024', 'choigs','$2b$10$WpWykhYAbASkIKqVUmP39eX53sEBEqBKLqo0g0lBZrbqgG6.DKbje', '최건수', '010-5554-3321', 'M', '서울시 중랑구 면목동', '910310-1334567', NOW(), NOW()),
('UUID-025', 'jeonjw','$2b$10$WpWykhYAbASkIKqVUmP39eX53sEBEqBKLqo0g0lBZrbqgG6.DKbje', '전지원', '010-8889-1111', 'F', '서울시 서초구 반포동', '020411-4234567', NOW(), NOW()),
('UUID-026', 'baeksm','$2b$10$WpWykhYAbASkIKqVUmP39eX53sEBEqBKLqo0g0lBZrbqgG6.DKbje', '백수민', '010-9998-1234', 'F', '서울시 용산구 한남동', '970912-2234567', NOW(), NOW()),
('UUID-027', 'kanghr','$2b$10$WpWykhYAbASkIKqVUmP39eX53sEBEqBKLqo0g0lBZrbqgG6.DKbje', '강하람', '010-2222-6666', 'M', '서울시 강서구 등촌동', '900312-1334567', NOW(), NOW()),
('UUID-028', 'ohsj',  '$2b$10$WpWykhYAbASkIKqVUmP39eX53sEBEqBKLqo0g0lBZrbqgG6.DKbje', '오서준', '010-3333-2222', 'M', '서울시 마포구 망원동', '950201-1334567', NOW(), NOW()),
('UUID-029', 'jungy', '$2b$10$WpWykhYAbASkIKqVUmP39eX53sEBEqBKLqo0g0lBZrbqgG6.DKbje', '정예림', '010-9876-1234', 'F', '서울시 은평구 불광동', '000101-4234567', NOW(), NOW()),
('UUID-030', 'jangsw','$2b$10$WpWykhYAbASkIKqVUmP39eX53sEBEqBKLqo0g0lBZrbqgG6.DKbje', '장서우', '010-6655-7766', 'M', '서울시 동작구 사당동', '980512-1334567', NOW(), NOW());

-- ============================================
-- 2) 진료과 (20개)
-- ============================================
INSERT INTO department (name, created_at, updated_at) VALUES
('내과', NOW(), NOW()), ('정형외과', NOW(), NOW()), ('피부과', NOW(), NOW()),
('이비인후과', NOW(), NOW()), ('안과', NOW(), NOW()), ('치과', NOW(), NOW()),
('소아청소년과', NOW(), NOW()), ('신경외과', NOW(), NOW()), ('산부인과', NOW(), NOW()),
('비뇨기과', NOW(), NOW()), ('신경과', NOW(), NOW()), ('정신건강의학과', NOW(), NOW()),
('재활의학과', NOW(), NOW()), ('흉부외과', NOW(), NOW()), ('응급의학과', NOW(), NOW()),
('마취통증의학과', NOW(), NOW()), ('영상의학과', NOW(), NOW()), ('진단검사의학과', NOW(), NOW()),
('가정의학과', NOW(), NOW()), ('소화기내과', NOW(), NOW());

-- ============================================
-- 3) 의사 (1~50) - 진료과 재배치 버전
--  공지/예외일에 쓰인 의사는 기존 진료과 유지
--  사용되지 않던 33~40, 49~50만 14~20번 진료과로 이동
-- ============================================
INSERT INTO doctor (department_id, name, profile_image_url, created_at, updated_at) VALUES
(1,  '김성훈', '/images/doctor/1_김성훈.png', NOW(), NOW()),
(1,  '박지은', '/images/doctor/2_박지은.png', NOW(), NOW()),
(1,  '이승현', '/images/doctor/3_이승현.png', NOW(), NOW()),
(1,  '정민호', '/images/doctor/4_정민호.png', NOW(), NOW()),
(1,  '윤하늘', '/images/doctor/5_윤하늘.png', NOW(), NOW()),
(2,  '이재혁', '/images/doctor/6_이재혁.png', NOW(), NOW()),
(2,  '최우석', '/images/doctor/7_최우석.png', NOW(), NOW()),
(2,  '박지환', '/images/doctor/8_박지환.png', NOW(), NOW()),
(2,  '김도현', '/images/doctor/9_김도현.png', NOW(), NOW()),
(3,  '박수현', '/images/doctor/10_박수현.png', NOW(), NOW()),
(3,  '정유진', '/images/doctor/11_정유진.png', NOW(), NOW()),
(3,  '이하늘', '/images/doctor/12_이하늘.png', NOW(), NOW()),
(3,  '문예린', '/images/doctor/13_문예린.png', NOW(), NOW()),
(4,  '최민아', '/images/doctor/14_최민아.png', NOW(), NOW()),
(4,  '김정환', '/images/doctor/15_김정환.png', NOW(), NOW()),
(4,  '류민재', '/images/doctor/16_류민재.png', NOW(), NOW()),
(4,  '서지훈', '/images/doctor/17_서지훈.png', NOW(), NOW()),
(5,  '오지훈', '/images/doctor/18_오지훈.png', NOW(), NOW()),
(5,  '강소연', '/images/doctor/19_강소연.png', NOW(), NOW()),
(5,  '배민아', '/images/doctor/20_배민아.png', NOW(), NOW()),
(6,  '정다혜', '/images/doctor/21_정다혜.png', NOW(), NOW()),
(6,  '이도윤', '/images/doctor/22_이도윤.png', NOW(), NOW()),
(6,  '김준영', '/images/doctor/23_김준영.png', NOW(), NOW()),
(6,  '박서진', '/images/doctor/24_박서진.png', NOW(), NOW()),
(7,  '유지호', '/images/doctor/25_유지호.png', NOW(), NOW()),
(7,  '홍은주', '/images/doctor/26_홍은주.png', NOW(), NOW()),
(7,  '남수현', '/images/doctor/27_남수현.png', NOW(), NOW()),
(7,  '임도현', '/images/doctor/28_임도현.png', NOW(), NOW()),
(8,  '강서연', '/images/doctor/29_강서연.png', NOW(), NOW()),
(8,  '이한결', '/images/doctor/30_이한결.png', NOW(), NOW()),
(8,  '박재훈', '/images/doctor/31_박재훈.png', NOW(), NOW()),
(8,  '최정윤', '/images/doctor/32_최정윤.png', NOW(), NOW()),
-- 33~36: 산부인과(9) → 일부는 그대로 유지, 일부는 아래에서 이동
(9,  '홍지은', '/images/doctor/33_홍지은.png', NOW(), NOW()),  -- 산부인과
(9,  '서유림', '/images/doctor/34_서유림.png', NOW(), NOW()),  -- 산부인과
(9,  '윤소라', '/images/doctor/35_윤소라.png', NOW(), NOW()),  -- 산부인과
(9,  '김선우', '/images/doctor/36_김선우.png', NOW(), NOW()),  -- 산부인과
-- 37~40: 비뇨기과 대신 흉부/응급/마취/영상으로 재배치
(14, '문정우', '/images/doctor/37_문정우.png', NOW(), NOW()),  -- 흉부외과
(15, '박도현', '/images/doctor/38_박도현.png', NOW(), NOW()),  -- 응급의학과
(16, '이태경', '/images/doctor/39_이태경.png', NOW(), NOW()),  -- 마취통증의학과
(17, '한채원', '/images/doctor/40_한채원.png', NOW(), NOW()),  -- 영상의학과
(11, '정민수', '/images/doctor/41_정민수.png', NOW(), NOW()),
(11, '오예진', '/images/doctor/42_오예진.png', NOW(), NOW()),
(11, '김세현', '/images/doctor/43_김세현.png', NOW(), NOW()),
(11, '박도하', '/images/doctor/44_박도하.png', NOW(), NOW()),
(12, '김유정', '/images/doctor/45_김유정.png', NOW(), NOW()),
(12, '이수빈', '/images/doctor/46_이수빈.png', NOW(), NOW()),
(12, '정서윤', '/images/doctor/47_정서윤.png', NOW(), NOW()),
(12, '한지후', '/images/doctor/48_한지후.png', NOW(), NOW()),
-- 49~50: 재활의학과 → 가정의학과/소화기내과로 이동
(19, '박한솔', '/images/doctor/49_박한솔.png', NOW(), NOW()),  -- 가정의학과
(20, '이도하', '/images/doctor/50_이도하.png', NOW(), NOW());  -- 소화기내과

-- ============================================
-- 4) 예약 (50건, doctor_id 1~50)
-- ============================================
INSERT INTO reservation (member_id, doctor_id, reservation_no, appointment_at, status, created_at, updated_at)
SELECT
  FLOOR(1 + RAND() * 30),           -- 1~30 회원
  FLOOR(1 + RAND() * 50),           -- 1~50 의사
  CONCAT('RSV', LPAD(FLOOR(1000 + RAND() * 9000), 4, '0')),
  DATE_ADD(CURDATE(), INTERVAL FLOOR(RAND() * 30) DAY),
  ELT(FLOOR(1 + RAND() * 3), 'RESERVED','DONE','CANCELLED'),
  NOW(),
  NOW()
FROM (
  SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5
  UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10
  UNION SELECT 11 UNION SELECT 12 UNION SELECT 13 UNION SELECT 14 UNION SELECT 15
  UNION SELECT 16 UNION SELECT 17 UNION SELECT 18 UNION SELECT 19 UNION SELECT 20
  UNION SELECT 21 UNION SELECT 22 UNION SELECT 23 UNION SELECT 24 UNION SELECT 25
  UNION SELECT 26 UNION SELECT 27 UNION SELECT 28 UNION SELECT 29 UNION SELECT 30
  UNION SELECT 31 UNION SELECT 32 UNION SELECT 33 UNION SELECT 34 UNION SELECT 35
  UNION SELECT 36 UNION SELECT 37 UNION SELECT 38 UNION SELECT 39 UNION SELECT 40
  UNION SELECT 41 UNION SELECT 42 UNION SELECT 43 UNION SELECT 44 UNION SELECT 45
  UNION SELECT 46 UNION SELECT 47 UNION SELECT 48 UNION SELECT 49 UNION SELECT 50
) t;

-- ============================================
-- 5) 접수 (40건)
-- ============================================
INSERT INTO reception (reservation_id, member_id, doctor_id, reception_no, type, status, consent_notice, consent_at, note_to_doctor, created_at, updated_at)
SELECT
  r.reservation_id,
  r.member_id,
  r.doctor_id,
  2000 + r.reservation_id,
  ELT(FLOOR(1 + RAND()*2), 'RESERVATION','DIRECT'),
  ELT(FLOOR(1 + RAND()*4), 'WAITING','IN_SERVICE','DONE','CANCELLED'),
  1,
  NOW(),
  CONCAT('증상 메모_', r.reservation_no),
  NOW(), NOW()
FROM reservation r
ORDER BY r.reservation_id
LIMIT 40;

-- ============================================
-- 6) 처방전 (DONE 상태의 접수만)
-- ============================================
INSERT INTO prescription (reception_id, doctor_id, issued_at, content, created_at, updated_at)
SELECT
  r.reception_id,
  r.doctor_id,
  NOW(),
  CONCAT('처방 내용 - ', r.reception_id, '번 접수'),
  NOW(), NOW()
FROM reception r
WHERE r.status = 'DONE';

-- ============================================
-- 7) 약국별 처방전
-- ============================================
INSERT INTO pharmacy_prescription (pharmacy_id, prescription_id, pharmacy_name, expected_finish_time, status, assigned_pharmacist, created_at, updated_at)
SELECT
  NULL,
  p.prescription_id,
  CONCAT('약국_', FLOOR(1 + RAND()*10)),
  DATE_ADD(NOW(), INTERVAL FLOOR(RAND()*60) MINUTE),
  ELT(FLOOR(1 + RAND()*4), 'START','IN_PROGRESS','READY','DONE'),
  CONCAT('약사_', FLOOR(1 + RAND()*30)),
  NOW(), NOW()
FROM prescription p;

-- ============================================
-- 8) 수령 이력
-- ============================================
INSERT INTO pickup_history (pharmacy_prescription_id, member_id, pickup_at, status, verified_by, created_at)
SELECT
  pp.pharmacy_prescription_id,
  r.member_id,
  NOW(),
  ELT(FLOOR(1 + RAND()*2), 'PICKED_UP','COMPLETED'),
  CONCAT('약사_', FLOOR(1 + RAND()*30)),
  NOW()
FROM pharmacy_prescription pp
JOIN prescription p ON pp.prescription_id = p.prescription_id
JOIN reception r ON p.reception_id = r.reception_id
WHERE pp.status = 'DONE'
LIMIT 5;

-- ============================================
-- 9) 증상 (10개)
-- ============================================
INSERT INTO symptom (name, created_at, updated_at) VALUES
('기침', NOW(), NOW()),
('두통', NOW(), NOW()),
('피부 트러블', NOW(), NOW()),
('눈 충혈', NOW(), NOW()),
('콧물', NOW(), NOW()),
('목통증', NOW(), NOW()),
('소화불량', NOW(), NOW()),
('생리불순', NOW(), NOW()),
('치통', NOW(), NOW()),
('허리통증', NOW(), NOW());

-- ============================================
-- 10) 접수-증상 관계 (15건 예시)
-- ============================================
INSERT INTO reception_symptom (reception_id, symptom_id, created_at) VALUES
(1, 3, NOW()),
(2, 1, NOW()),
(3, 10, NOW()),
(4, 2, NOW()),
(5, 4, NOW()),
(6, 6, NOW()),
(7, 7, NOW()),
(8, 5, NOW()),
(9, 9, NOW()),
(10, 8, NOW()),
(11, 2, NOW()),
(12, 1, NOW()),
(13, 3, NOW()),
(14, 4, NOW()),
(15, 6, NOW());

-- ============================================
-- 11) 의사 예외일 (오늘~다음주, 과별 2~3개씩)
--   type ENUM('CONFERENCE','EMERGENCY','OTHER','SICK_LEAVE','VACATION')
-- ============================================
INSERT INTO doctor_exception_day (doctor_id, exception_date, type, created_at, updated_at) VALUES
-- 2025-11-21 (금) - 내과, 정형외과
(1,  '2025-11-21', 'SICK_LEAVE',  NOW(), NOW()),
(2,  '2025-11-21', 'OTHER',       NOW(), NOW()),
(3,  '2025-11-21', 'VACATION',    NOW(), NOW()),
(6,  '2025-11-21', 'CONFERENCE',  NOW(), NOW()),
(7,  '2025-11-21', 'OTHER',       NOW(), NOW()),

-- 2025-11-22 (토) - 소아청소년과, 정형외과
(25, '2025-11-22', 'CONFERENCE',  NOW(), NOW()),
(26, '2025-11-22', 'OTHER',       NOW(), NOW()),
(8,  '2025-11-22', 'EMERGENCY',   NOW(), NOW()),
(9,  '2025-11-22', 'VACATION',    NOW(), NOW()),

-- 2025-11-23 (일) - 안과, 소아청소년과
(18, '2025-11-23', 'VACATION',    NOW(), NOW()),
(19, '2025-11-23', 'OTHER',       NOW(), NOW()),
(27, '2025-11-23', 'EMERGENCY',   NOW(), NOW()),
(28, '2025-11-23', 'VACATION',    NOW(), NOW()),

-- 2025-11-24 (월) - 피부과, 신경과
(10, '2025-11-24', 'CONFERENCE',  NOW(), NOW()),
(11, '2025-11-24', 'OTHER',       NOW(), NOW()),
(12, '2025-11-24', 'SICK_LEAVE',  NOW(), NOW()),
(41, '2025-11-24', 'OTHER',       NOW(), NOW()),
(42, '2025-11-24', 'VACATION',    NOW(), NOW()),

-- 2025-11-25 (화) - 내과, 신경외과
(4,  '2025-11-25', 'SICK_LEAVE',  NOW(), NOW()),
(5,  '2025-11-25', 'VACATION',    NOW(), NOW()),
(29, '2025-11-25', 'EMERGENCY',   NOW(), NOW()),
(30, '2025-11-25', 'OTHER',       NOW(), NOW()),

-- 2025-11-26 (수) - 안과, 정신건강의학과
(20, '2025-11-26', 'CONFERENCE',  NOW(), NOW()),
(45, '2025-11-26', 'OTHER',       NOW(), NOW()),
(46, '2025-11-26', 'CONFERENCE',  NOW(), NOW()),
(47, '2025-11-26', 'VACATION',    NOW(), NOW()),

-- 2025-11-27 (목) - 정형외과, 신경외과
(6,  '2025-11-27', 'VACATION',    NOW(), NOW()),
(7,  '2025-11-27', 'OTHER',       NOW(), NOW()),
(31, '2025-11-27', 'CONFERENCE',  NOW(), NOW()),
(32, '2025-11-27', 'OTHER',       NOW(), NOW()),

-- 2025-11-28 (금) - 신경과, 정신건강의학과
(43, '2025-11-28', 'SICK_LEAVE',  NOW(), NOW()),
(44, '2025-11-28', 'OTHER',       NOW(), NOW()),
(48, '2025-11-28', 'VACATION',    NOW(), NOW()),
(45, '2025-11-28', 'OTHER',       NOW(), NOW());

-- ============================================
-- 12) 의사 공지사항 (예외일과 매칭)
-- ============================================
INSERT INTO doctor_notice (doctor_id, content, starts_at, ends_at, priority, created_at, updated_at) VALUES
-- 2025-11-21 (금) - 내과, 정형외과
(1,
 '11월 21일(금) 내과 김성훈 교수님 감기 몸살로 외래 진료가 전면 취소됩니다.',
 '2025-11-21 00:00:00', '2025-11-21 23:59:59', 1, NOW(), NOW()),
(2,
 '11월 21일(금) 내과 박지은 교수님 병동 교육 참여로 오후 진료 시간이 단축 운영됩니다.',
 '2025-11-20 00:00:00', '2025-11-21 23:59:59', 2, NOW(), NOW()),
(3,
 '11월 21일(금) 내과 이승현 교수님 개인 일정으로 오후 반차 휴무입니다.',
 '2025-11-20 00:00:00', '2025-11-21 23:59:59', 3, NOW(), NOW()),
(6,
 '11월 21일(금) 정형외과 이재혁 교수님 정형외과 학회 참석으로 오전 외래만 운영됩니다.',
 '2025-11-20 00:00:00', '2025-11-21 23:59:59', 2, NOW(), NOW()),
(7,
 '11월 21일(금) 정형외과 최우석 교수님 병원 내 워크숍으로 일부 예약이 조정될 수 있습니다.',
 '2025-11-21 00:00:00', '2025-11-21 23:59:59', 3, NOW(), NOW()),

-- 2025-11-22 (토) - 소아청소년과, 정형외과
(25,
 '11월 22일(토) 소아청소년과 유지호 교수님 소아학회 참석으로 오전 진료만 진행됩니다.',
 '2025-11-21 00:00:00', '2025-11-22 23:59:59', 2, NOW(), NOW()),
(26,
 '11월 22일(토) 소아청소년과 홍은주 교수님 예방접종 캠페인 참여로 외래 진료가 축소 운영됩니다.',
 '2025-11-21 00:00:00', '2025-11-22 23:59:59', 3, NOW(), NOW()),
(8,
 '11월 22일(토) 정형외과 박지환 교수님 응급수술 대기로 진료 대기 시간이 길어질 수 있습니다.',
 '2025-11-22 00:00:00', '2025-11-22 23:59:59', 1, NOW(), NOW()),
(9,
 '11월 22일(토) 정형외과 김도현 교수님 토요 연차로 외래 진료가 중단됩니다.',
 '2025-11-21 00:00:00', '2025-11-22 23:59:59', 2, NOW(), NOW()),

-- 2025-11-23 (일) - 안과, 소아청소년과
(18,
 '11월 23일(일) 안과 오지훈 교수님 휴무로 외래 진료가 없습니다.',
 '2025-11-22 00:00:00', '2025-11-23 23:59:59', 2, NOW(), NOW()),
(19,
 '11월 23일(일) 안과 강소연 교수님 검사 장비 점검으로 진료가 제한적으로 진행됩니다.',
 '2025-11-22 00:00:00', '2025-11-23 23:59:59', 3, NOW(), NOW()),
(27,
 '11월 23일(일) 소아청소년과 남수현 교수님 응급 소아환자 대응으로 대기 시간이 길어질 수 있습니다.',
 '2025-11-23 00:00:00', '2025-11-23 23:59:59', 1, NOW(), NOW()),
(28,
 '11월 23일(일) 소아청소년과 임도현 교수님 휴무로 일부 예약이 다른 의료진으로 변경됩니다.',
 '2025-11-22 00:00:00', '2025-11-23 23:59:59', 2, NOW(), NOW()),

-- 2025-11-24 (월) - 피부과, 신경과
(10,
 '11월 24일(월) 피부과 박수현 교수님 피부염 심포지엄 참석으로 오후 진료만 운영됩니다.',
 '2025-11-23 00:00:00', '2025-11-24 23:59:59', 2, NOW(), NOW()),
(11,
 '11월 24일(월) 피부과 정유진 교수님 교육 참여로 일부 진료 시간이 변경됩니다.',
 '2025-11-23 00:00:00', '2025-11-24 23:59:59', 3, NOW(), NOW()),
(12,
 '11월 24일(월) 피부과 이하늘 교수님 컨디션 난조로 진료가 조기 종료될 수 있습니다.',
 '2025-11-24 00:00:00', '2025-11-24 23:59:59', 1, NOW(), NOW()),
(41,
 '11월 24일(월) 신경과 정민수 교수님 연구 미팅으로 오전 진료가 축소 운영됩니다.',
 '2025-11-23 00:00:00', '2025-11-24 23:59:59', 2, NOW(), NOW()),
(42,
 '11월 24일(월) 신경과 오예진 교수님 연차로 외래 진료가 중단됩니다.',
 '2025-11-23 00:00:00', '2025-11-24 23:59:59', 3, NOW(), NOW()),

-- 2025-11-25 (화) - 내과, 신경외과
(4,
 '11월 25일(화) 내과 정민호 교수님 독감으로 진료가 전면 취소되었습니다.',
 '2025-11-24 00:00:00', '2025-11-25 23:59:59', 1, NOW(), NOW()),
(5,
 '11월 25일(화) 내과 윤하늘 교수님 연차 사용으로 오후 진료가 없습니다.',
 '2025-11-24 00:00:00', '2025-11-25 23:59:59', 2, NOW(), NOW()),
(29,
 '11월 25일(화) 신경외과 강서연 교수님 응급 수술 일정으로 외래 대기 시간이 길어질 수 있습니다.',
 '2025-11-25 00:00:00', '2025-11-25 23:59:59', 1, NOW(), NOW()),
(30,
 '11월 25일(화) 신경외과 이한결 교수님 병동 회진 강화로 오후 일부 진료가 취소됩니다.',
 '2025-11-24 00:00:00', '2025-11-25 23:59:59', 2, NOW(), NOW()),

-- 2025-11-26 (수) - 안과, 정신건강의학과
(20,
 '11월 26일(수) 안과 배민아 교수님 시력교정 수술 세미나 참석으로 오전 진료만 가능합니다.',
 '2025-11-25 00:00:00', '2025-11-26 23:59:59', 2, NOW(), NOW()),
(45,
 '11월 26일(수) 정신건강의학과 김유정 교수님 케이스 컨퍼런스로 일부 진료 시간이 조정됩니다.',
 '2025-11-25 00:00:00', '2025-11-26 23:59:59', 3, NOW(), NOW()),
(46,
 '11월 26일(수) 정신건강의학과 이수빈 교수님 학회 참석으로 오후 진료만 진행됩니다.',
 '2025-11-25 00:00:00', '2025-11-26 23:59:59', 2, NOW(), NOW()),
(47,
 '11월 26일(수) 정신건강의학과 정서윤 교수님 휴가로 외래 진료가 중단됩니다.',
 '2025-11-25 00:00:00', '2025-11-26 23:59:59', 1, NOW(), NOW()),

-- 2025-11-27 (목) - 정형외과, 신경외과
(6,
 '11월 27일(목) 정형외과 이재혁 교수님 연차로 외래 진료가 없습니다.',
 '2025-11-26 00:00:00', '2025-11-27 23:59:59', 2, NOW(), NOW()),
(7,
 '11월 27일(목) 정형외과 최우석 교수님 병원 워크숍으로 오후 진료가 축소 운영됩니다.',
 '2025-11-26 00:00:00', '2025-11-27 23:59:59', 3, NOW(), NOW()),
(31,
 '11월 27일(목) 신경외과 박재훈 교수님 학회 참석으로 오전 진료만 가능합니다.',
 '2025-11-26 00:00:00', '2025-11-27 23:59:59', 2, NOW(), NOW()),
(32,
 '11월 27일(목) 신경외과 최정윤 교수님 연구 일정으로 일부 예약이 조정될 수 있습니다.',
 '2025-11-26 00:00:00', '2025-11-27 23:59:59', 3, NOW(), NOW()),

-- 2025-11-28 (금) - 신경과, 정신건강의학과
(43,
 '11월 28일(금) 신경과 김세현 교수님 컨디션 난조로 진료 시간이 단축 운영됩니다.',
 '2025-11-28 00:00:00', '2025-11-28 23:59:59', 2, NOW(), NOW()),
(44,
 '11월 28일(금) 신경과 박도하 교수님 편두통 악화로 오후 진료가 취소되었습니다.',
 '2025-11-28 00:00:00', '2025-11-28 23:59:59', 1, NOW(), NOW()),
(48,
 '11월 28일(금) 정신건강의학과 한지후 교수님 휴가로 외래 진료가 없습니다.',
 '2025-11-27 00:00:00', '2025-11-28 23:59:59', 2, NOW(), NOW()),
(45,
 '11월 28일(금) 정신건강의학과 김유정 교수님 팀 회의로 일부 예약 시간이 변경될 수 있습니다.',
 '2025-11-27 00:00:00', '2025-11-28 23:59:59', 3, NOW(), NOW());

-- ============================================
-- 13) 대기 티켓 (모든 접수 기준)
-- ============================================
INSERT INTO waiting_ticket (reception_id, queue_no, status, estimated_wait_minutes, estimated_call_time, called_at, created_at, updated_at)
SELECT
  r.reception_id,
  3000 + r.reception_id,
  ELT(FLOOR(1 + RAND()*5), 'WAITING','IN_SERVICE','DONE','CANCELLED','SKIPPED'),
  FLOOR(RAND()*20),
  DATE_ADD(NOW(), INTERVAL FLOOR(RAND()*15) MINUTE),
  CASE WHEN RAND() > 0.5 THEN NOW() ELSE NULL END,
  NOW(), NOW()
FROM reception r;

-- ============================================
-- 14) 의사 주간 스케줄 (doctor_id 1~50)
-- ============================================
SET @seed := 20251016;

INSERT INTO doctor_weekly_schedule (doctor_id, day_of_week, am_flag, pm_flag, created_at, updated_at)
SELECT
  doctor_id,
  day_of_week,
  am_flag,
  CASE WHEN am_flag=0 AND pm_base=0 THEN 1 ELSE pm_base END AS pm_flag,
  NOW(), NOW()
FROM (
  SELECT
    d.doctor_id,
    w.day_of_week,
    CASE
      WHEN RAND(d.doctor_id*97 + w.day_of_week*13 + @seed) <
           (CASE WHEN w.day_of_week=4 THEN 0.40 ELSE 0.70 END)
      THEN 1 ELSE 0
    END AS am_flag,
    CASE
      WHEN w.day_of_week IN (3,6)
        THEN CASE WHEN RAND(d.doctor_id*131 + w.day_of_week*29 + @seed) < 0.35 THEN 1 ELSE 0 END
      WHEN w.day_of_week = 4
        THEN CASE WHEN RAND(d.doctor_id*131 + w.day_of_week*29 + @seed) < 0.25 THEN 1 ELSE 0 END
      ELSE CASE WHEN RAND(d.doctor_id*131 + w.day_of_week*29 + @seed) < 0.60 THEN 1 ELSE 0 END
    END AS pm_base
  FROM
    (
      SELECT (a.n + b.n*10 + c.n*100) AS doctor_id
      FROM
        (SELECT 0 n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
         UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) a
      CROSS JOIN
        (SELECT 0 n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
         UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) b
      CROSS JOIN
        (SELECT 0 n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
         UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) c
    ) d
  JOIN
    (SELECT 1 AS day_of_week UNION ALL SELECT 2 UNION ALL SELECT 3
     UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6) w
  WHERE d.doctor_id BETWEEN 1 AND 50
) x
ORDER BY doctor_id, day_of_week;

SET FOREIGN_KEY_CHECKS = 1;
