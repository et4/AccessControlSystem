CREATE TABLE card (
  id        SERIAL PRIMARY KEY,
  hasAccess BOOLEAN  NOT NULL
);

CREATE TABLE "group" (
  id        SERIAL  PRIMARY KEY,
  hasAccess BOOLEAN NOT NULL
);

CREATE TABLE groupaccess (
  cardId            INT     NOT NULL,
  groupId           INT     NOT NULL,
  exceptionalAccess VARCHAR NOT NULL,
  PRIMARY KEY (cardId, groupId)
);

CREATE TABLE log (
  id        SERIAL PRIMARY KEY,
  cardId    INT                NOT NULL,
  datetime  TIMESTAMP          NOT NULL,
  eventType VARCHAR            NOT NULL,
  success   BOOLEAN            NOT NULL
);

ALTER TABLE groupaccess
  ADD CONSTRAINT fk_GroupAccess_groupId
FOREIGN KEY (groupId) REFERENCES "group" (id);

ALTER TABLE groupaccess
  ADD CONSTRAINT fk_GroupAccess_cardId
FOREIGN KEY (cardId) REFERENCES card (id);

ALTER TABLE log
  ADD CONSTRAINT fk_Log_cardId
FOREIGN KEY (cardId) REFERENCES card (id);
