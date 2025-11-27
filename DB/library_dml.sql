--測試資料
INSERT INTO book (isbn, name, author, introduction) VALUES 
('978-0001', 'Spring Boot 實戰', 'Craig Walls', '深入淺出 Spring Boot'),
('978-0002', 'Vue.js 權威指南', 'Evan You', '前端開發必備'),
('978-0003', 'PostgreSQL 高效能', 'Gregory Smith', '資料庫優化技巧');

INSERT INTO inventory (isbn, status) VALUES 
('978-0001', '1'),
('978-0001', '1'),
('978-0001', '4'),
('978-0002', '1'),
('978-0002', '1'),
('978-0003', '1');
