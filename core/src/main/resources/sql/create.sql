-- sql statements to initialize the data structure of the in-memory test database

CREATE TABLE PUBLIC.Subject
(
  id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  modified TIMESTAMP NOT NULL,
  name VARCHAR2(255) NOT NULL,
  alias VARCHAR2(255),
  color VARCHAR2(8)
);

CREATE TABLE PUBLIC.Observation
(
  id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  modified TIMESTAMP NOT NULL,
  name VARCHAR2(255) NOT NULL,
  video VARCHAR(255),
  date DATETIME,
  focalSubject INT,
  FOREIGN KEY (focalSubject) REFERENCES Public.Subject(id)
);

CREATE TABLE PUBLIC.SubjectInObservation
(
  id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  modified TIMESTAMP NOT NULL,
  observation INT NOT NULL,
  subject INT NOT NULL,
  FOREIGN KEY (observation) REFERENCES Public.Observation(id),
  FOREIGN KEY (subject) REFERENCES Public.Subject(id)
);

CREATE TABLE PUBLIC.ModifierFactory
(
  id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  modified TIMESTAMP NOT NULL,
  type VARCHAR2(100) NOT NULL,
  name VARCHAR2(255),
  alias VARCHAR2(255),
  rangeFrom INT,
  rangeTo INT
);

CREATE TABLE PUBLIC.Action
(
  id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  modified TIMESTAMP NOT NULL,
  name VARCHAR2(255) NOT NULL,
  alias VARCHAR2(255),
  recurring INT,
  modifierFactory INT,
  type VARCHAR2(255),
  FOREIGN KEY (modifierFactory) REFERENCES Public.ModifierFactory(id)
);

CREATE TABLE PUBLIC.ValidSubject
(
  id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  modified TIMESTAMP NOT NULL,
  subject INT NOT NULL,
  modifierFactory INT NOT NULL,
  FOREIGN KEY (subject) REFERENCES Public.Subject(id),
  FOREIGN KEY (modifierFactory) REFERENCES Public.ModifierFactory(id)
);

CREATE TABLE PUBLIC.EnumerationItem
(
  id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  modified TIMESTAMP NOT NULL,
  value VARCHAR2(255) NOT NULL,
  modifierFactory INT NOT NULL,
  FOREIGN KEY (modifierFactory) REFERENCES Public.ModifierFactory(id)
);

CREATE TABLE PUBLIC.Modifier
(
  id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  modified TIMESTAMP NOT NULL,
  modifierFactory INT NOT NULL,
  type VARCHAR2(255) NOT NULL,
  subject INT,
  enumerationValue VARCHAR2(255),
  number INT,
  buildString VARCHAR(255),
  FOREIGN KEY (subject) REFERENCES Public.Subject(id),
  FOREIGN KEY (modifierFactory) REFERENCES Public.ModifierFactory(id)
);

CREATE TABLE PUBLIC.Coding
(
  id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  modified TIMESTAMP NOT NULL,
  subject INT,
  action INT,
  modifier INT,
  observation INT,
  start INT NOT NULL,
  end INT,
  FOREIGN KEY (subject) REFERENCES Public.Subject(id),
  FOREIGN KEY (action) REFERENCES Public.Action(id),
  FOREIGN KEY (modifier) REFERENCES Public.Modifier(id),
  FOREIGN KEY (observation) REFERENCES Public.Observation(id)
);

CREATE TABLE PUBLIC.Node
(
  id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  modified TIMESTAMP NOT NULL,
  parent INT,
  title VARCHAR2(255),
  type VARCHAR2(100) NOT NULL,
  subject INT,
  action INT,
  actionType VARCHAR2(100),
  initialAction INT,
  modifierFactory INT,
  observation INT,
  FOREIGN KEY (parent) REFERENCES (id),
  FOREIGN KEY (subject) REFERENCES Public.Subject(id),
  FOREIGN KEY (action) REFERENCES Public.Action(id),
  FOREIGN KEY (initialAction) REFERENCES Public.Action(id),
  FOREIGN KEY (modifierFactory) REFERENCES Public.ModifierFactory(id),
  FOREIGN KEY (observation) REFERENCES Public.Observation(id)
);

CREATE TABLE PUBLIC.Property
(
  id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  modified TIMESTAMP NOT NULL,
  key VARCHAR2(255) NOT NULL,
  value VARCHAR2(255) NOT NULL
);

-- Nodes
--- root for subjects
INSERT INTO PUBLIC.NODE (MODIFIED, PARENT, TITLE, TYPE, SUBJECT, ACTION, ACTIONTYPE, MODIFIERFACTORY, OBSERVATION)
VALUES (sysdate, null, 'Subjects', 'org.obehave.model.Subject', null, null, null, null, null);
--- root for actions
INSERT INTO PUBLIC.NODE (MODIFIED, PARENT, TITLE, TYPE, SUBJECT, ACTION, ACTIONTYPE, MODIFIERFACTORY, OBSERVATION)
VALUES (sysdate, null, 'Actions', 'org.obehave.model.Action', null, null, null, null, null);
--- root for modifierfactories
INSERT INTO PUBLIC.NODE (MODIFIED, PARENT, TITLE, TYPE, SUBJECT, ACTION, ACTIONTYPE, MODIFIERFACTORY, OBSERVATION)
VALUES (sysdate, null, 'Modifiers', 'org.obehave.model.modifier.ModifierFactory', null, null, null, null, null);
--- root for observations
INSERT INTO PUBLIC.NODE (MODIFIED, PARENT, TITLE, TYPE, SUBJECT, ACTION, ACTIONTYPE, MODIFIERFACTORY, OBSERVATION)
VALUES (sysdate, null, 'Observations', 'org.obehave.model.Observation', null, null, null, null, null);


COMMIT;