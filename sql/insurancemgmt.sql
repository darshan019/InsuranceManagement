CREATE DATABASE IF NOT EXISTS `InsuranceManagement_DB`;
USE `InsuranceManagement_DB`;


DROP TABLE IF EXISTS Claim;
DROP TABLE IF EXISTS Payment;
DROP TABLE IF EXISTS Policy;
DROP TABLE IF EXISTS Category;
DROP TABLE IF EXISTS InsuranceType;
DROP TABLE IF EXISTS Insurance_type;
DROP TABLE IF EXISTS Customer;
DROP TABLE IF EXISTS Admin;
DROP TABLE IF EXISTS Agent_To_Policy;


CREATE TABLE Customer (
  customer_id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(255) NOT NULL UNIQUE,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  address VARCHAR(255),
  date_of_birth DATE
);

CREATE TABLE Admin (
  admin_id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL
);

CREATE TABLE Agent (
  agent_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  phone VARCHAR(50)
);

CREATE TABLE Insurance_type (
  type_id INT AUTO_INCREMENT PRIMARY KEY,
  type_name VARCHAR(255) NOT NULL,
  description VARCHAR(255)
);

CREATE TABLE Category (
  category_id INT AUTO_INCREMENT PRIMARY KEY,
  type_id INT NOT NULL,
  category_name VARCHAR(100) NOT NULL,
  premium_amount DECIMAL(10,2) NOT NULL,
  coverage_amount DECIMAL(15,2) NOT NULL,
  FOREIGN KEY (type_id) REFERENCES Insurance_type(type_id)
);

CREATE TABLE Policy (
  policy_id INT AUTO_INCREMENT PRIMARY KEY,
  customer_id INT NOT NULL,
  agent_id INT NOT NULL,
  type_id INT NOT NULL,
  category_id INT NOT NULL,
  policy_number VARCHAR(255) NOT NULL,
  start_date TIMESTAMP NOT NULL,
  end_date TIMESTAMP,
  status VARCHAR(50) NOT NULL,
  FOREIGN KEY (customer_id) REFERENCES Customer(customer_id),
  FOREIGN KEY (agent_id) REFERENCES Agent(agent_id),
  FOREIGN KEY (type_id) REFERENCES Insurance_type(type_id),
  FOREIGN KEY (category_id) REFERENCES Category(category_id)
);

CREATE TABLE Claim (
  claim_id INT AUTO_INCREMENT PRIMARY KEY,
  policy_id INT NOT NULL,
  claim_date TIMESTAMP NOT NULL,
  description TEXT,
  claim_amount DECIMAL(15,2) NOT NULL,
  status VARCHAR(50) NOT NULL,
  approved_by INT,
  approved_at TIMESTAMP,
  FOREIGN KEY (policy_id) REFERENCES Policy(policy_id),
  FOREIGN KEY (approved_by) REFERENCES Admin(admin_id)
);

CREATE TABLE Payment (
  payment_id INT AUTO_INCREMENT PRIMARY KEY,
  policy_id INT NOT NULL,
  payment_date TIMESTAMP NOT NULL,
  amount DECIMAL(10,2) NOT NULL,
  payment_method VARCHAR(50),
  status VARCHAR(50) NOT NULL,
  FOREIGN KEY (policy_id) REFERENCES Policy(policy_id)
);

-- Insurance Types
INSERT INTO Insurance_type (type_name, description) VALUES
('Motor', 'Motor insurance for vehicles'),
('Property', 'Property insurance for houses and buildings'),
('Life', 'Life insurance plans');

-- Categories
INSERT INTO Category (type_id, category_name, premium_amount, coverage_amount) VALUES
(1, '2 Wheeler', 1000, 500000),
(1, '3 Wheeler', 1500, 750000),
(1, '4 Wheeler', 2500, 1200000),
(2, 'Small Property', 10000, 5000000),
(2, 'Medium Property', 20000, 15000000),
(2, 'Large Property', 40000, 22000000),
(3, 'Basic Life', 4800, 10000000),
(3, 'Premium Life', 9600, 20000000);

-- Customers
INSERT INTO Customer (username, email, password, address, date_of_birth) VALUES
('arun_k', 'arun@example.com', 'pass123', 'Chennai, TN', '1995-06-15'),
('meera_s', 'meera@example.com', 'secure456', 'Bangalore, KA', '1990-09-20'),
('rahul_p', 'rahul@example.com', 'rahul789', 'Hyderabad, TS', '1988-12-05');

-- Agents
INSERT INTO Agent (name, email, phone) VALUES
('Ravi Kumar', 'ravi.agent@example.com', '9876543210'),
('Priya Sharma', 'priya.agent@example.com', '9123456789');

-- Policies (linked to agents + customers)
INSERT INTO Policy (customer_id, agent_id, type_id, category_id, policy_number, start_date, end_date, status) VALUES
(1, 1, 1, 1, 'POL-MOT-0001', NOW(), DATE_ADD(NOW(), INTERVAL 1 YEAR), 'Active'),
(2, 2, 2, 5, 'POL-PROP-0002', NOW(), DATE_ADD(NOW(), INTERVAL 1 YEAR), 'Active'),
(3, 1, 3, 8, 'POL-LIFE-0003', NOW(), DATE_ADD(NOW(), INTERVAL 1 YEAR), 'Active');

-- Claims
INSERT INTO Claim (policy_id, claim_date, description, claim_amount, status) VALUES
(1, NOW(), 'Accident damage to bike', 20000, 'Pending'),
(2, NOW(), 'Fire damage to kitchen', 150000, 'Approved'),
(3, NOW(), 'Life insurance payout request', 20000000, 'Pending');

-- Payments
INSERT INTO Payment (policy_id, payment_date, amount, payment_method, status) VALUES
(1, NOW(), 1500.00, 'Credit Card', 'Completed');

INSERT INTO Admin (username, email, password)
VALUES ('admin1', 'admin1@example.com', 'adminpass');

UPDATE Claim
SET approved_by = 1, approved_at = NOW(), status = 'Approved'
WHERE claim_id = 2;

