CREATE TABLE usergroup (
  id          INT PRIMARY KEY AUTO_INCREMENT,
  hasAccess   BOOLEAN
);

CREATE TABLE card (
  id             INT PRIMARY KEY AUTO_INCREMENT,
  hasAccess      BOOLEAN,
  priorityAccess BOOLEAN,
  groupid        INT,
  FOREIGN KEY (groupid) REFERENCES usergroup (id)
);

CREATE TABLE log (
  cardid      INT,
  datetime   TIMESTAMP,
  eventtype  BOOLEAN,
  success    BOOLEAN,
  PRIMARY KEY (cardid, datetime),
  FOREIGN KEY (cardid) REFERENCES card (id)
);

CREATE TABLE groupExceptions (
  groupid     INT,
  cardid      INT,
  hasAccess   BOOLEAN,
  PRIMARY KEY (groupid, cardid),
  FOREIGN KEY (cardid) REFERENCES card (id),
  FOREIGN KEY (groupid) REFERENCES usergroup (id)
);

INSERT INTO usergroup (id, hasAccess)
VALUES (DEFAULT, true);