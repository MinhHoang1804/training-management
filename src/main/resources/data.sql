/*INSERT INTO roles (role_name) VALUES ('ROLE_ADMIN');
INSERT INTO roles (role_name) VALUES ('ROLE_COORDINATOR');
INSERT INTO roles (role_name) VALUES ('ROLE_CLASS_ADMIN');
INSERT INTO roles (role_name) VALUES ('ROLE_TRAINER');
INSERT INTO roles (role_name) VALUES ('ROLE_TRAINEE');

INSERT INTO users (full_name, email, img_ava, phone, emergency_phone, address, account, password, date_of_birth, status)
VALUES
    ('Phương Diệu', 'phuongdieu@example.com', 'path/to/image_phuongdieu.jpg', '0123456784', '0987654326', 'Hà Nội', 'phuongdieu', '$2b$12$9gDUVpWVXRgQaWx7mkYMyujFyU1V4W7lRtIiYBPt/2dz7dUwBj5Ee', '1995-06-15', TRUE),
    ('Nguyễn Văn A', 'nguyenvana@example.com', 'path/to/imageA.jpg', '0123456789', '0987654321', 'Hà Nội', 'nguyenvana', '$2b$12$A6hJc3z4X1gVZCqZs4nLBe/ER2OL6X5IbZfQ6CP3noCgg3d1Qp3Fq', '1990-01-01', TRUE),
    ('Trần Thị B', 'tranthib@example.com', 'path/to/imageB.jpg', '0123456780', '0987654322', 'TP. Hồ Chí Minh', 'tranthib', '$2b$12$5vUyIfA0L7Z2m8Rg2TyQxeJbqQZTQz3uZz5rI5g5pOQ3G2Ae7OsVK', '1992-02-02', TRUE),
    ('Lê Văn C', 'levanc@example.com', 'path/to/imageC.jpg', '0123456781', '0987654323', 'Đà Nẵng', 'levanc', '$2b$12$Y2i5H4H8c/zN9.h4TnFW3OQOKXxH.3e8i6PHyB/6AP4ePHm4hXDOO', '1993-03-03', FALSE);

INSERT INTO curriculums (curriculum_name, descriptions, created_date, status)
VALUES
    ('Curriculum A', 'Description for Curriculum A', NOW(), TRUE),
    ('Curriculum B', 'Description for Curriculum B', NOW(), TRUE),
    ('Curriculum C', 'Description for Curriculum C', NOW(), FALSE);*/

/*INSERT INTO user_roles (user_id, role_id) VALUES
                                              (4, 16),  -- User Nguyễn Văn A, Role ROLE_ADMIN
                                              (5, 17),  -- User Trần Thị B, Role ROLE_COORDINATOR
                                              (6, 18),  -- User Lê Văn C, Role ROLE_CLASS_ADMIN
                                              (7, 19);
*/
/*INSERT INTO users (full_name, email, img_ava, phone, emergency_phone, address, account, password, date_of_birth, status)
VALUES
    ('Nguyễn Văn', 'nguyenvan@example.com', 'path/to/imageA.jpg', '0123856789', '0987654321', 'Hà Nội', 'nguyenvan', '$2b$12$D9POmuc2B37KlY5eDp02O.Jg18U1gg7JDk4mZGuBWG83m90EJ/Q6y', '1990-01-01', TRUE);



*//*
INSERT INTO roles (role_name) VALUES ('ROLE_ADMIN');
INSERT INTO roles (role_name) VALUES ('ROLE_COORDINATOR');
INSERT INTO roles (role_name) VALUES ('ROLE_CLASS_ADMIN');
INSERT INTO roles (role_name) VALUES ('ROLE_TRAINER');
INSERT INTO roles (role_name) VALUES ('ROLE_TRAINEE');*/
/*
INSERT
INTO user_roles (user_id, role_id) VALUES
                                              (4, 26);  -- User Nguyễn Văn A, Role ROLE_ADMIN
*/

/*INSERT INTO subject (subject_code, subject_name, document_link, weight_percentage, status, descriptions)
VALUES
    ('SUB001', 'Math Fundamentals', 'https://example.com/math-fundamentals', 20.00, TRUE, 'This is a basic math course.'),
    ('SUB002', 'Physics Basics', 'https://example.com/physics-basics', 25.50, TRUE, 'Introduction to physics.'),
    ('SUB003', 'Chemistry 101', 'https://example.com/chemistry-101', 15.75, TRUE, 'Basic chemistry course for beginners.'),
    ('SUB004', 'Computer Science', 'https://example.com/computer-science', 30.00, TRUE, 'Fundamentals of computer science.'),
    ('SUB005', 'History Overview', 'https://example.com/history-overview', 10.50, TRUE, 'A brief history overview course.');
*/

/*INSERT INTO curriculum_subject (curriculum_id, subject_id)
VALUES
    (1, 1),  -- Curriculum 1 liên kết với Subject 1
    (1, 2),  -- Curriculum 1 liên kết với Subject 2
    (1, 3),  -- Curriculum 2 liên kết với Subject 1
    (2, 3),  -- Curriculum 2 liên kết với Subject 3
    (2, 4);  -- Curriculum 3 liên kết với Subject 4
*/