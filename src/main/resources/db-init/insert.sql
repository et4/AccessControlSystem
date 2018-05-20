INSERT INTO "group" VALUES (default, true);
INSERT INTO "group" VALUES (default, false);
INSERT INTO "group" VALUES (default, true);
INSERT INTO "group" VALUES (default, false);

-- cards without groups
INSERT INTO card VALUES (1, true);
INSERT INTO card VALUES (2, false);

-- cards with one group and no exceptions
INSERT INTO card VALUES (3, true);
INSERT INTO card VALUES (4, false);
INSERT INTO card VALUES (5, true);
INSERT INTO card VALUES (6, false);

-- cards with two groups and no exceptions
INSERT INTO card VALUES (7, true);
INSERT INTO card VALUES (8, false);
INSERT INTO card VALUES (9, true);
INSERT INTO card VALUES (10, false);
INSERT INTO card VALUES (11, true);
INSERT INTO card VALUES (12, false);

-- cards with one group and exceptions
INSERT INTO card VALUES (13, true);
INSERT INTO card VALUES (14, false);
INSERT INTO card VALUES (15, true);
INSERT INTO card VALUES (16, false);
INSERT INTO card VALUES (17, true);
INSERT INTO card VALUES (18, false);
INSERT INTO card VALUES (19, true);
INSERT INTO card VALUES (20, false);

-- cards with two groups and exceptions
INSERT INTO card VALUES (21, true);
INSERT INTO card VALUES (22, false);
INSERT INTO card VALUES (23, true);
INSERT INTO card VALUES (24, false);

-- cards with defaults and exceptions
INSERT INTO card VALUES (25, true);
INSERT INTO card VALUES (26, false);
INSERT INTO card VALUES (27, true);

INSERT INTO groupaccess VALUES (3, 1, 'DEFAULT');
INSERT INTO groupaccess VALUES (4, 1, 'DEFAULT');
INSERT INTO groupaccess VALUES (5, 2, 'DEFAULT');
INSERT INTO groupaccess VALUES (6, 2, 'DEFAULT');

INSERT INTO groupaccess VALUES (7, 1, 'DEFAULT');
INSERT INTO groupaccess VALUES (7, 2, 'DEFAULT');
INSERT INTO groupaccess VALUES (8, 1, 'DEFAULT');
INSERT INTO groupaccess VALUES (8, 2, 'DEFAULT');
INSERT INTO groupaccess VALUES (9, 1, 'DEFAULT');
INSERT INTO groupaccess VALUES (9, 3, 'DEFAULT');
INSERT INTO groupaccess VALUES (10,2, 'DEFAULT');
INSERT INTO groupaccess VALUES (10,4, 'DEFAULT');
INSERT INTO groupaccess VALUES (11,2, 'DEFAULT');
INSERT INTO groupaccess VALUES (11,4, 'DEFAULT');
INSERT INTO groupaccess VALUES (12,1, 'DEFAULT');
INSERT INTO groupaccess VALUES (12,3, 'DEFAULT');

INSERT INTO groupaccess VALUES (13,1, 'GRANTED');
INSERT INTO groupaccess VALUES (14,1, 'FORBIDDEN');
INSERT INTO groupaccess VALUES (15,1, 'FORBIDDEN');
INSERT INTO groupaccess VALUES (16,1, 'GRANTED');
INSERT INTO groupaccess VALUES (17,2, 'GRANTED');
INSERT INTO groupaccess VALUES (18,2, 'FORBIDDEN');
INSERT INTO groupaccess VALUES (19,2, 'FORBIDDEN');
INSERT INTO groupaccess VALUES (20,2, 'GRANTED');

INSERT INTO groupaccess VALUES (21,1, 'GRANTED');
INSERT INTO groupaccess VALUES (21,2, 'FORBIDDEN');
INSERT INTO groupaccess VALUES (22,1, 'GRANTED');
INSERT INTO groupaccess VALUES (22,2, 'FORBIDDEN');
INSERT INTO groupaccess VALUES (23,1, 'FORBIDDEN');
INSERT INTO groupaccess VALUES (23,2, 'GRANTED');
INSERT INTO groupaccess VALUES (24,1, 'FORBIDDEN');
INSERT INTO groupaccess VALUES (24,2, 'GRANTED');

INSERT INTO groupaccess VALUES (25,1, 'FORBIDDEN');
INSERT INTO groupaccess VALUES (25,2, 'FORBIDDEN');
INSERT INTO groupaccess VALUES (25,3, 'DEFAULT');

INSERT INTO groupaccess VALUES (26,1, 'FORBIDDEN');
INSERT INTO groupaccess VALUES (26,2, 'DEFAULT');
INSERT INTO groupaccess VALUES (26,3, 'FORBIDDEN');

INSERT INTO groupaccess VALUES (27,1, 'FORBIDDEN');
INSERT INTO groupaccess VALUES (27,2, 'DEFAULT');
INSERT INTO groupaccess VALUES (27,3, 'FORBIDDEN');
INSERT INTO groupaccess VALUES (27,4, 'GRANTED');
