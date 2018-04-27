CREATE TABLE `Card` (
  `id`        int AUTO_INCREMENT NOT NULL ,
  `hasAccess` boolean  NOT NULL ,
  PRIMARY KEY (`id`)
);

CREATE TABLE `Group` (
  `id`        int AUTO_INCREMENT NOT NULL ,
  `hasAccess` boolean  NOT NULL ,
  PRIMARY KEY (`id`)
);

CREATE TABLE `GroupAccess` (
  `cardId`            int  NOT NULL ,
  `groupId`           int  NOT NULL ,
  `exceptionalAccess` varchar  NOT NULL
);

CREATE TABLE `Log` (
  `id`        int AUTO_INCREMENT NOT NULL ,
  `cardId`    int  NOT NULL ,
  `datetime`  timestamp  NOT NULL ,
  `eventType` varchar  NOT NULL ,
  `success`   boolean  NOT NULL ,
  PRIMARY KEY (`id`)
);

ALTER TABLE `GroupAccess` ADD CONSTRAINT `fk_GroupAccess_groupId`
FOREIGN KEY(`groupId`) REFERENCES `Group` (`id`);

ALTER TABLE `GroupAccess` ADD CONSTRAINT `fk_GroupAccess_cardId`
FOREIGN KEY(`cardId`) REFERENCES `Card` (`id`);

ALTER TABLE `Log` ADD CONSTRAINT `fk_Log_cardId`
FOREIGN KEY(`cardId`) REFERENCES `Card` (`id`);
