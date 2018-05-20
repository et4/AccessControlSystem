INSERT INTO "group" VALUES (1, true);
INSERT INTO "group" VALUES (2, false);

INSERT INTO card VALUES (1, true);
INSERT INTO card VALUES (2, false);

INSERT INTO card VALUES (3, true);
INSERT INTO card VALUES (4, false);
INSERT INTO card VALUES (5, true);
INSERT INTO card VALUES (6, false);

INSERT INTO card VALUES (7, true);
INSERT INTO card VALUES (8, false);

INSERT INTO groupaccess VALUES (3, 1, 'DEFAULT');
INSERT INTO groupaccess VALUES (4, 1, 'DEFAULT');
INSERT INTO groupaccess VALUES (5, 2, 'DEFAULT');
INSERT INTO groupaccess VALUES (6, 2, 'DEFAULT');

INSERT INTO groupaccess VALUES (7, 2, 'FORBIDDEN');
INSERT INTO groupaccess VALUES (8, 2, 'GRANTED');
