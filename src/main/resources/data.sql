-- Insert permissions
INSERT INTO permissions (name, api_path, method, module, created_by) VALUES
('Xem danh sách người dùng', '/api/users', 'GET', 'USER', 'system'),
('Thêm người dùng', '/api/users', 'POST', 'USER', 'system'),
('Sửa người dùng', '/api/users/{id}', 'PUT', 'USER', 'system'),
('Xóa người dùng', '/api/users/{id}', 'DELETE', 'USER', 'system'),
('Xem danh sách bác sĩ', '/api/doctors', 'GET', 'DOCTOR', 'system'),
('Thêm bác sĩ', '/api/doctors', 'POST', 'DOCTOR', 'system'),
('Sửa bác sĩ', '/api/doctors/{id}', 'PUT', 'DOCTOR', 'system'),
('Xóa bác sĩ', '/api/doctors/{id}', 'DELETE', 'DOCTOR', 'system'),
('Xem danh sách lịch hẹn', '/api/appointments', 'GET', 'APPOINTMENT', 'system'),
('Thêm lịch hẹn', '/api/appointments', 'POST', 'APPOINTMENT', 'system'),
('Sửa lịch hẹn', '/api/appointments/{id}', 'PUT', 'APPOINTMENT', 'system'),
('Xóa lịch hẹn', '/api/appointments/{id}', 'DELETE', 'APPOINTMENT', 'system');

-- Insert roles
INSERT INTO roles (name, description, active, created_by) VALUES
('ADMIN', 'Quản trị viên hệ thống', true, 'system'),
('DOCTOR', 'Bác sĩ', true, 'system'),
('PATIENT', 'Bệnh nhân', true, 'system'),
('NURSE', 'Y tá', true, 'system'),
('RECEPTIONIST', 'Lễ tân', true, 'system'),
('PHARMACIST', 'Dược sĩ', true, 'system'),
('LAB_TECHNICIAN', 'Kỹ thuật viên xét nghiệm', true, 'system'),
('RADIOLOGIST', 'Bác sĩ chẩn đoán hình ảnh', true, 'system'),
('PHYSIOTHERAPIST', 'Bác sĩ vật lý trị liệu', true, 'system'),
('NUTRITIONIST', 'Chuyên gia dinh dưỡng', true, 'system');

-- Insert role_permissions
INSERT INTO permission_role (role_id, permission_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9), (1, 10), (1, 11), (1, 12),
(2, 5), (2, 9), (2, 10), (2, 11),
(3, 5), (3, 9), (3, 10),
(4, 5), (4, 9),
(5, 5), (5, 9), (5, 10),
(6, 5), (6, 9),
(7, 5), (7, 9),
(8, 5), (8, 9),
(9, 5), (9, 9),
(10, 5), (10, 9);

-- Insert users
INSERT INTO users (name, email, password, phone, age, gender, address, role_id, created_by) VALUES
('Admin', 'admin@mediclinic.com', '$2a$10$rDkPvvAFV6GgJkKqGXZxUOQZQZQZQZQZQZQZQZQZQZQZQZQZQZQZ', '0123456789', 30, 'MALE', 'Hà Nội', 1, 'system'),
('Bác sĩ Nguyễn Văn A', 'doctor1@mediclinic.com', '$2a$10$rDkPvvAFV6GgJkKqGXZxUOQZQZQZQZQZQZQZQZQZQZQZQZQZQZQZ', '0123456788', 35, 'MALE', 'Hà Nội', 2, 'system'),
('Bác sĩ Trần Thị B', 'doctor2@mediclinic.com', '$2a$10$rDkPvvAFV6GgJkKqGXZxUOQZQZQZQZQZQZQZQZQZQZQZQZQZQZQZ', '0123456787', 32, 'FEMALE', 'Hà Nội', 2, 'system'),
('Bệnh nhân Lê Văn C', 'patient1@mediclinic.com', '$2a$10$rDkPvvAFV6GgJkKqGXZxUOQZQZQZQZQZQZQZQZQZQZQZQZQZQZQZ', '0123456786', 25, 'MALE', 'Hà Nội', 3, 'system'),
('Bệnh nhân Phạm Thị D', 'patient2@mediclinic.com', '$2a$10$rDkPvvAFV6GgJkKqGXZxUOQZQZQZQZQZQZQZQZQZQZQZQZQZQZQZ', '0123456785', 28, 'FEMALE', 'Hà Nội', 3, 'system'),
('Y tá Nguyễn Thị E', 'nurse1@mediclinic.com', '$2a$10$rDkPvvAFV6GgJkKqGXZxUOQZQZQZQZQZQZQZQZQZQZQZQZQZQZQZ', '0123456784', 27, 'FEMALE', 'Hà Nội', 4, 'system'),
('Lễ tân Trần Văn F', 'receptionist1@mediclinic.com', '$2a$10$rDkPvvAFV6GgJkKqGXZxUOQZQZQZQZQZQZQZQZQZQZQZQZQZQZQZ', '0123456783', 24, 'MALE', 'Hà Nội', 5, 'system'),
('Dược sĩ Lê Thị G', 'pharmacist1@mediclinic.com', '$2a$10$rDkPvvAFV6GgJkKqGXZxUOQZQZQZQZQZQZQZQZQZQZQZQZQZQZQZ', '0123456782', 29, 'FEMALE', 'Hà Nội', 6, 'system'),
('Kỹ thuật viên Phạm Văn H', 'labtech1@mediclinic.com', '$2a$10$rDkPvvAFV6GgJkKqGXZxUOQZQZQZQZQZQZQZQZQZQZQZQZQZQZQZ', '0123456781', 26, 'MALE', 'Hà Nội', 7, 'system'),
('Bác sĩ chẩn đoán Nguyễn Văn I', 'radiologist1@mediclinic.com', '$2a$10$rDkPvvAFV6GgJkKqGXZxUOQZQZQZQZQZQZQZQZQZQZQZQZQZQZQZ', '0123456780', 33, 'MALE', 'Hà Nội', 8, 'system');

-- Insert specialties
INSERT INTO specialties (name, description, image_url, created_by) VALUES
('Nội khoa', 'Chuyên khoa nội tổng quát', 'https://example.com/internal.jpg', 'system'),
('Ngoại khoa', 'Chuyên khoa ngoại tổng quát', 'https://example.com/surgery.jpg', 'system'),
('Nhi khoa', 'Chuyên khoa nhi', 'https://example.com/pediatrics.jpg', 'system'),
('Sản phụ khoa', 'Chuyên khoa sản phụ khoa', 'https://example.com/obstetrics.jpg', 'system'),
('Da liễu', 'Chuyên khoa da liễu', 'https://example.com/dermatology.jpg', 'system'),
('Răng hàm mặt', 'Chuyên khoa răng hàm mặt', 'https://example.com/dental.jpg', 'system'),
('Mắt', 'Chuyên khoa mắt', 'https://example.com/eye.jpg', 'system'),
('Tai mũi họng', 'Chuyên khoa tai mũi họng', 'https://example.com/ent.jpg', 'system'),
('Thần kinh', 'Chuyên khoa thần kinh', 'https://example.com/neurology.jpg', 'system'),
('Tim mạch', 'Chuyên khoa tim mạch', 'https://example.com/cardiology.jpg', 'system');

-- Insert doctors
INSERT INTO doctors (full_name, email, phone, description, image_url, active, specialty_id, created_by) VALUES
('Bác sĩ Nguyễn Văn A', 'doctor1@mediclinic.com', '0123456788', 'Bác sĩ nội khoa có 10 năm kinh nghiệm', 'https://example.com/doctor1.jpg', true, 1, 'system'),
('Bác sĩ Trần Thị B', 'doctor2@mediclinic.com', '0123456787', 'Bác sĩ ngoại khoa có 8 năm kinh nghiệm', 'https://example.com/doctor2.jpg', true, 2, 'system'),
('Bác sĩ Lê Văn C', 'doctor3@mediclinic.com', '0123456786', 'Bác sĩ nhi khoa có 7 năm kinh nghiệm', 'https://example.com/doctor3.jpg', true, 3, 'system'),
('Bác sĩ Phạm Thị D', 'doctor4@mediclinic.com', '0123456785', 'Bác sĩ sản phụ khoa có 9 năm kinh nghiệm', 'https://example.com/doctor4.jpg', true, 4, 'system'),
('Bác sĩ Nguyễn Văn E', 'doctor5@mediclinic.com', '0123456784', 'Bác sĩ da liễu có 6 năm kinh nghiệm', 'https://example.com/doctor5.jpg', true, 5, 'system'),
('Bác sĩ Trần Thị F', 'doctor6@mediclinic.com', '0123456783', 'Bác sĩ răng hàm mặt có 8 năm kinh nghiệm', 'https://example.com/doctor6.jpg', true, 6, 'system'),
('Bác sĩ Lê Văn G', 'doctor7@mediclinic.com', '0123456782', 'Bác sĩ mắt có 7 năm kinh nghiệm', 'https://example.com/doctor7.jpg', true, 7, 'system'),
('Bác sĩ Phạm Thị H', 'doctor8@mediclinic.com', '0123456781', 'Bác sĩ tai mũi họng có 9 năm kinh nghiệm', 'https://example.com/doctor8.jpg', true, 8, 'system'),
('Bác sĩ Nguyễn Văn I', 'doctor9@mediclinic.com', '0123456780', 'Bác sĩ thần kinh có 10 năm kinh nghiệm', 'https://example.com/doctor9.jpg', true, 9, 'system'),
('Bác sĩ Trần Thị K', 'doctor10@mediclinic.com', '0123456779', 'Bác sĩ tim mạch có 8 năm kinh nghiệm', 'https://example.com/doctor10.jpg', true, 10, 'system');

-- Insert schedules
INSERT INTO schedules (doctor_id, work_date, time_slot, status, created_by) VALUES
(1, '2024-03-20', '08:00-09:00', 'AVAILABLE', 'system'),
(1, '2024-03-20', '09:00-10:00', 'AVAILABLE', 'system'),
(1, '2024-03-20', '10:00-11:00', 'AVAILABLE', 'system'),
(2, '2024-03-20', '08:00-09:00', 'AVAILABLE', 'system'),
(2, '2024-03-20', '09:00-10:00', 'AVAILABLE', 'system'),
(2, '2024-03-20', '10:00-11:00', 'AVAILABLE', 'system'),
(3, '2024-03-20', '08:00-09:00', 'AVAILABLE', 'system'),
(3, '2024-03-20', '09:00-10:00', 'AVAILABLE', 'system'),
(3, '2024-03-20', '10:00-11:00', 'AVAILABLE', 'system'),
(4, '2024-03-20', '08:00-09:00', 'AVAILABLE', 'system');

-- Insert appointments
INSERT INTO appointments (created_at, created_by, reason, status, updated_at, updated_by, doctor_id, schedule_id, user_id) VALUES
('2024-03-20 08:00:00', 'system', 'Khám bệnh định kỳ', 'PENDING', NULL, NULL, 1, 1, 4),
('2024-03-20 08:00:00', 'system', 'Tư vấn sức khỏe', 'PENDING', NULL, NULL, 2, 2, 5),
('2024-03-20 08:00:00', 'system', 'Khám bệnh thường xuyên', 'PENDING', NULL, NULL, 3, 3, 4),
('2024-03-20 08:00:00', 'system', 'Tư vấn sức khỏe', 'PENDING', NULL, NULL, 4, 4, 5),
('2024-03-20 08:00:00', 'system', 'Khám bệnh định kỳ', 'PENDING', NULL, NULL, 5, 5, 4),
('2024-03-20 08:00:00', 'system', 'Tư vấn sức khỏe', 'PENDING', NULL, NULL, 6, 6, 5),
('2024-03-20 08:00:00', 'system', 'Khám bệnh thường xuyên', 'PENDING', NULL, NULL, 7, 7, 4),
('2024-03-20 08:00:00', 'system', 'Tư vấn sức khỏe', 'PENDING', NULL, NULL, 8, 8, 5),
('2024-03-20 08:00:00', 'system', 'Khám bệnh định kỳ', 'PENDING', NULL, NULL, 9, 9, 4),
('2024-03-20 08:00:00', 'system', 'Tư vấn sức khỏe', 'PENDING', NULL, NULL, 10, 10, 5); 