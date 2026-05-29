-- Uzivatel Table
CREATE TABLE IF NOT EXISTS Uzivatel (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    username VARCHAR(255) UNIQUE,
    email VARCHAR(255) UNIQUE,
    passwd VARCHAR(255)
);

-- PO Table
CREATE TABLE IF NOT EXISTS PO (
    ICO VARCHAR(255) PRIMARY KEY,
    uzivatel_id INT,
    name VARCHAR(255),
    email VARCHAR(255),
    tel VARCHAR(255),
    FOREIGN KEY (uzivatel_id) REFERENCES Uzivatel(ID)
);

-- Poptavka Table
CREATE TABLE IF NOT EXISTS Poptavka (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    ICO VARCHAR(255),
    name VARCHAR(255),
    description TEXT,
    price DOUBLE,
    created_date DATE,
    FOREIGN KEY (ICO) REFERENCES PO(ICO)
);

-- Tag Table
CREATE TABLE IF NOT EXISTS Tag (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) UNIQUE
);

-- Poptavka_Tag Table
CREATE TABLE IF NOT EXISTS Poptavka_Tag (
    poptavka_id INT,
    tag_id INT,
    PRIMARY KEY (poptavka_id, tag_id),
    FOREIGN KEY (poptavka_id) REFERENCES Poptavka(ID),
    FOREIGN KEY (tag_id) REFERENCES Tag(ID)
);

-- Nabidka Table
CREATE TABLE IF NOT EXISTS Nabidka (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    poptavka_id INT,
    uzivatel_id INT,
    text TEXT,
    proposed_price DOUBLE,
    status VARCHAR(255),
    FOREIGN KEY (poptavka_id) REFERENCES Poptavka(ID),
    FOREIGN KEY (uzivatel_id) REFERENCES Uzivatel(ID)
);
