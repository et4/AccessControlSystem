CREATE TABLE `Card` (
  `id`        INT AUTO_INCREMENT NOT NULL,
  `hasAccess` BOOLEAN            NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `Group` (
  `id`        INT AUTO_INCREMENT NOT NULL,
  `hasAccess` BOOLEAN            NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `GroupAccess` (
  `cardId`            INT     NOT NULL,
  `groupId`           INT     NOT NULL,
  `exceptionalAccess` VARCHAR NOT NULL
);

CREATE TABLE `Log` (
  `id`        INT AUTO_INCREMENT NOT NULL,
  `cardId`    INT                NOT NULL,
  `datetime`  TIMESTAMP          NOT NULL,
  `eventType` VARCHAR            NOT NULL,
  `success`   BOOLEAN            NOT NULL,
  PRIMARY KEY (`id`)
);

ALTER TABLE `GroupAccess`
  ADD CONSTRAINT `fk_GroupAccess_groupId`
FOREIGN KEY (`groupId`) REFERENCES `Group` (`id`);

ALTER TABLE `GroupAccess`
  ADD CONSTRAINT `fk_GroupAccess_cardId`
FOREIGN KEY (`cardId`) REFERENCES `Card` (`id`);

ALTER TABLE `Log`
  ADD CONSTRAINT `fk_Log_cardId`
FOREIGN KEY (`cardId`) REFERENCES `Card` (`id`);
