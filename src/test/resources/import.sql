-- Clean up (optional with create-drop, but safe)
DELETE FROM annonce;
DELETE FROM categories;
DELETE FROM users;

-- Users
INSERT INTO users (username, email, password, createdAt) VALUES ('testuser', 'test@example.com', '$2a$10$wTfBvDk6S.6Tz4Z.fS.O.eQ8B8q6F.L.G.Z.H.Y.X.W.V.U.T.S.R', CURRENT_TIMESTAMP);

-- Categories
INSERT INTO categories (label) VALUES ('Auto');
INSERT INTO categories (label) VALUES ('Immo');

-- Annonces (12 items for pagination testing)
INSERT INTO annonce (title, description, adress, mail, date, status, author_id, category_id, version) VALUES ('Peugeot 208', 'Bel etat', 'Paris', 'auto@mail.com', CURRENT_TIMESTAMP, 'PUBLISHED', 1, 1, 0);
INSERT INTO annonce (title, description, adress, mail, date, status, author_id, category_id, version) VALUES ('Renault Clio', 'Prix negociable', 'Lyon', 'auto@mail.com', CURRENT_TIMESTAMP, 'DRAFT', 1, 1, 0);
INSERT INTO annonce (title, description, adress, mail, date, status, author_id, category_id, version) VALUES ('Appartement T2', 'Proche métro', 'Lille', 'immo@mail.com', CURRENT_TIMESTAMP, 'PUBLISHED', 1, 2, 0);
INSERT INTO annonce (title, description, adress, mail, date, status, author_id, category_id, version) VALUES ('Villa avec piscine', 'Luxe', 'Nice', 'immo@mail.com', CURRENT_TIMESTAMP, 'DRAFT', 1, 2, 0);
INSERT INTO annonce (title, description, adress, mail, date, status, author_id, category_id, version) VALUES ('Velo electrique', 'Neuf', 'Bordeaux', 'velo@mail.com', CURRENT_TIMESTAMP, 'PUBLISHED', 1, 1, 0);
INSERT INTO annonce (title, description, adress, mail, date, status, author_id, category_id, version) VALUES ('Canapé cuir', 'Trés confortable', 'Marseille', 'deco@mail.com', CURRENT_TIMESTAMP, 'DRAFT', 1, 1, 0);
INSERT INTO annonce (title, description, adress, mail, date, status, author_id, category_id, version) VALUES ('Table bois', 'Artisanal', 'Toulouse', 'deco@mail.com', CURRENT_TIMESTAMP, 'PUBLISHED', 1, 1, 0);
INSERT INTO annonce (title, description, adress, mail, date, status, author_id, category_id, version) VALUES ('PC Gamer', 'RTX 4080', 'Nantes', 'tech@mail.com', CURRENT_TIMESTAMP, 'DRAFT', 1, 1, 0);
INSERT INTO annonce (title, description, adress, mail, date, status, author_id, category_id, version) VALUES ('Iphone 15', 'Comme neuf', 'Strasbourg', 'tech@mail.com', CURRENT_TIMESTAMP, 'PUBLISHED', 1, 1, 0);
INSERT INTO annonce (title, description, adress, mail, date, status, author_id, category_id, version) VALUES ('Moto Honda', 'Révision faite', 'Montpellier', 'moto@mail.com', CURRENT_TIMESTAMP, 'DRAFT', 1, 1, 0);
INSERT INTO annonce (title, description, adress, mail, date, status, author_id, category_id, version) VALUES ('Guitare Fender', 'Super son', 'Rennes', 'music@mail.com', CURRENT_TIMESTAMP, 'PUBLISHED', 1, 1, 0);
INSERT INTO annonce (title, description, adress, mail, date, status, author_id, category_id, version) VALUES ('Batterie Pearl', 'Complet', 'Reims', 'music@mail.com', CURRENT_TIMESTAMP, 'DRAFT', 1, 1, 0);
