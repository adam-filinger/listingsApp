-- Uzivatel Table
CREATE TABLE IF NOT EXISTS Uzivatel (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    username VARCHAR(255) UNIQUE,
    email VARCHAR(255) UNIQUE,
    passwd VARCHAR(255)
);

-- PO Table
CREATE TABLE IF NOT EXISTS Pravnicka_Osoba (
    ICO VARCHAR(255) PRIMARY KEY,
    uzivatel_id INT,
    name VARCHAR(255),
    email VARCHAR(255),
    tel VARCHAR(255),
    FOREIGN KEY (uzivatel_id) REFERENCES Uzivatel(ID) ON DELETE CASCADE
);

-- Poptavka Table
CREATE TABLE IF NOT EXISTS Poptavka (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    ICO VARCHAR(255),
    name VARCHAR(255),
    description TEXT,
    price DOUBLE,
    created_date DATE,
    version INT,
    category VARCHAR(255),
    FOREIGN KEY (ICO) REFERENCES PRAVNICKA_OSOBA(ICO) ON DELETE CASCADE
);


-- Nabidka Table
CREATE TABLE IF NOT EXISTS Nabidka (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    poptavka_id INT,
    uzivatel_id INT,
    text TEXT,
    proposed_price DOUBLE,
    status VARCHAR(255),
    FOREIGN KEY (poptavka_id) REFERENCES Poptavka(ID) ON DELETE CASCADE,
    FOREIGN KEY (uzivatel_id) REFERENCES Uzivatel(ID) ON DELETE CASCADE
);
