------------------------------------------------------------------------------------------------------------------------
-- Data in this file is most likely used in tests. So be careful when you change/delete something in it!
-- To be sure, run all tests afterwards.
------------------------------------------------------------------------------------------------------------------------

-- SUBJECTs
INSERT INTO PUBLIC.SUBJECT (MODIFIED, NAME, ALIAS, COLOR) VALUES (sysdate, 'Subject1', NULL, NULL);
INSERT INTO PUBLIC.SUBJECT (MODIFIED, NAME, ALIAS, COLOR) VALUES (sysdate, 'Subject2', NULL, 'FF0000FF');
INSERT INTO PUBLIC.SUBJECT (MODIFIED, NAME, ALIAS, COLOR) VALUES (sysdate, 'Subject3', 'Sub3', NULL);
INSERT INTO PUBLIC.SUBJECT (MODIFIED, NAME, ALIAS, COLOR) VALUES (sysdate, 'Subject4', 'Sub4', '00FFFFFF');

-- Observations
INSERT INTO PUBLIC.OBSERVATION (MODIFIED, NAME, VIDEO, DATE) VALUES (sysdate, 'Observation1', NULL, NULL);
INSERT INTO PUBLIC.OBSERVATION (MODIFIED, NAME, VIDEO, DATE) VALUES (sysdate, 'Observation2', NULL, sysdate);

-- Modifier(Factories)
-- -- DECIMAL_RANGE_MODIFIER
INSERT INTO PUBLIC.MODIFIERFACTORY (MODIFIED, TYPE, NAME, RANGEFROM, RANGETO)
VALUES (sysdate, 'DECIMAL_RANGE_MODIFIER_FACTORY', 'One To Five', 1, 5);
INSERT INTO PUBLIC.MODIFIER (MODIFIED, TYPE, MODIFIERFACTORY, NUMBER) VALUES (sysdate, 'DECIMAL_MODIFIER', (SELECT ID
                                                                                                            FROM
                                                                                                              MODIFIERFACTORY
                                                                                                            WHERE NAME =
                                                                                                                  'One To Five'),
                                                                              2);

-- -- SUBJECT_MODIFIER
INSERT INTO PUBLIC.MODIFIERFACTORY (MODIFIED, TYPE, NAME, ALIAS, RANGEFROM, RANGETO)
VALUES (sysdate, 'SUBJECT_MODIFIER_FACTORY', 'Subject One Or Two', NULL, NULL, NULL);
INSERT INTO PUBLIC.VALIDSUBJECT (MODIFIED, SUBJECT, MODIFIERFACTORY) VALUES (sysdate, (SELECT ID
                                                                                       FROM PUBLIC.SUBJECT
                                                                                       WHERE NAME = 'Subject1'),
                                                                             (SELECT ID
                                                                              FROM PUBLIC.MODIFIERFACTORY
                                                                              WHERE NAME = 'Subject One Or Two'));
INSERT INTO PUBLIC.VALIDSUBJECT (MODIFIED, SUBJECT, MODIFIERFACTORY) VALUES (sysdate, (SELECT ID
                                                                                       FROM PUBLIC.SUBJECT
                                                                                       WHERE NAME = 'Subject2'),
                                                                             (SELECT ID
                                                                              FROM PUBLIC.MODIFIERFACTORY
                                                                              WHERE NAME = 'Subject One Or Two'));
INSERT INTO PUBLIC.MODIFIER (MODIFIED, TYPE, MODIFIERFACTORY, SUBJECT) VALUES (sysdate, 'SUBJECT_MODIFIER', (SELECT ID
                                                                                                             FROM
                                                                                                               MODIFIERFACTORY
                                                                                                             WHERE
                                                                                                               NAME =
                                                                                                               'Subject One Or Two'),
                                                                               (SELECT ID
                                                                                FROM SUBJECT
                                                                                WHERE NAME = 'Subject1'));

-- -- ENUMERATION_MODIFIER
INSERT INTO PUBLIC.MODIFIERFACTORY (MODIFIED, TYPE, NAME, RANGEFROM, RANGETO)
VALUES (sysdate, 'ENUMERATION_MODIFIER_FACTORY', 'Slow Or Fast', NULL, NULL);
INSERT INTO PUBLIC.EnumerationItem (MODIFIED, value, MODIFIERFACTORY) VALUES (sysdate, 'Slow', (SELECT ID
                                                                                                FROM
                                                                                                  PUBLIC.MODIFIERFACTORY
                                                                                                WHERE NAME =
                                                                                                      'Slow Or Fast'));
INSERT INTO PUBLIC.EnumerationItem (MODIFIED, value, MODIFIERFACTORY) VALUES (sysdate, 'Fast', (SELECT ID
                                                                                                FROM
                                                                                                  PUBLIC.MODIFIERFACTORY
                                                                                                WHERE NAME =
                                                                                                      'Slow Or Fast'));
INSERT INTO PUBLIC.MODIFIER (MODIFIED, TYPE, MODIFIERFACTORY, ENUMERATIONVALUE)
VALUES (sysdate, 'ENUMERATION_MODIFIER', (SELECT ID
                                          FROM MODIFIERFACTORY
                                          WHERE NAME = 'Slow Or Fast'), 'Slow');

-- ACTIONs
INSERT INTO PUBLIC.ACTION (MODIFIED, NAME, ALIAS, RECURRING, MODIFIERFACTORY, TYPE)
VALUES (sysdate, 'Howling', NULL, NULL, NULL, 'POINT');
INSERT INTO PUBLIC.ACTION (MODIFIED, NAME, ALIAS, RECURRING, MODIFIERFACTORY, TYPE)
VALUES (sysdate, 'Fighting', NULL, 0, NULL, 'STATE');
INSERT INTO PUBLIC.ACTION (MODIFIED, NAME, ALIAS, RECURRING, MODIFIERFACTORY, TYPE)
VALUES (sysdate, 'Scratching', 'Scr', 5, NULL, 'STATE');
INSERT INTO PUBLIC.ACTION (MODIFIED, NAME, ALIAS, RECURRING, MODIFIERFACTORY, TYPE)
VALUES (sysdate, 'Running', 'Ru', 5, (SELECT ID
                                      FROM
                                        PUBLIC.MODIFIERFACTORY
                                      WHERE NAME =
                                            'Slow Or Fast'), 'STATE');
INSERT INTO PUBLIC.ACTION (MODIFIED, NAME, ALIAS, RECURRING, MODIFIERFACTORY, TYPE)
VALUES (sysdate, 'Crouching', 'Cro', 0, (SELECT ID
                                      FROM
                                        PUBLIC.MODIFIERFACTORY
                                      WHERE NAME =
                                            'One To Five'), 'STATE');
INSERT INTO PUBLIC.ACTION (MODIFIED, NAME, ALIAS, RECURRING, MODIFIERFACTORY, TYPE)
VALUES (sysdate, 'Looking', 'Look', 0, (SELECT ID
                                        FROM
                                          PUBLIC.MODIFIERFACTORY
                                        WHERE NAME =
                                              'Subject One Or Two'), 'POINT');

-- Coding
INSERT INTO PUBLIC.Coding (MODIFIED, SUBJECT, ACTION, MODIFIER, OBSERVATION, START, END) VALUES (sysdate, (SELECT ID
                                                                                                           FROM
                                                                                                             PUBLIC.SUBJECT
                                                                                                           WHERE NAME =
                                                                                                                 'Subject1'),
                                                                                                 (SELECT ID
                                                                                                  FROM PUBLIC.ACTION
                                                                                                  WHERE
                                                                                                    NAME = 'Howling'),
                                                                                                 NULL, (SELECT ID
                                                                                                        FROM
                                                                                                          PUBLIC.OBSERVATION
                                                                                                        WHERE NAME =
                                                                                                              'Observation1'),
                                                                                                 300, NULL);
INSERT INTO PUBLIC.Coding (MODIFIED, SUBJECT, ACTION, MODIFIER, OBSERVATION, START, END) VALUES (sysdate, (SELECT ID
                                                                                                           FROM
                                                                                                             PUBLIC.SUBJECT
                                                                                                           WHERE NAME =
                                                                                                                 'Subject1'),
                                                                                                 (SELECT ID
                                                                                                  FROM PUBLIC.ACTION
                                                                                                  WHERE
                                                                                                    NAME = 'Running'),
                                                                                                 (SELECT ID
                                                                                                  FROM PUBLIC.MODIFIER
                                                                                                  WHERE
                                                                                                    ENUMERATIONVALUE =
                                                                                                    'Slow'), (SELECT ID
                                                                                                              FROM
                                                                                                                PUBLIC.OBSERVATION
                                                                                                              WHERE
                                                                                                                NAME =
                                                                                                                'Observation1'),
                                                                                                 500, 700);

COMMIT;