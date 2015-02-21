-- Subjects
INSERT INTO PUBLIC.Subject (modified, name, alias, color) VALUES (sysdate, 'Subject1', NULL, NULL);
INSERT INTO PUBLIC.Subject (modified, name, alias, color) VALUES (sysdate, 'Subject2', NULL, 'FF0000FF');
INSERT INTO PUBLIC.Subject (modified, name, alias, color) VALUES (sysdate, 'Subject3', 'Sub3', NULL);
INSERT INTO PUBLIC.Subject (modified, name, alias, color) VALUES (sysdate, 'Subject4', 'Sub4', '00FFFFFF');

-- Observations
INSERT INTO PUBLIC.Observation (modified, name, video, date) VALUES (sysdate, 'Observation1', NULL, NULL);
INSERT INTO PUBLIC.Observation (modified, name, video, date) VALUES (sysdate, 'Observation2', NULL, sysdate);

-- ModifierFactories
-- -- DECIMAL_RANGE_MODIFIER_FACTORY
INSERT INTO PUBLIC.ModifierFactory (modified, type, name, rangeFrom, rangeTo)
VALUES (sysdate, 'DECIMAL_RANGE_MODIFIER_FACTORY', 'One To Five', 1, 5);

-- -- SUBJECT_MODIFIER_FACTORY
INSERT INTO PUBLIC.ModifierFactory (modified, type, name, rangeFrom, rangeTo)
VALUES (sysdate, 'SUBJECT_MODIFIER_FACTORY', 'Subject One Or Two', NULL, NULL);
INSERT INTO PUBLIC.ValidSubject (modified, subject, modifierFactory) VALUES (sysdate, (SELECT id
                                                                                       FROM PUBLIC.Subject
                                                                                       WHERE name = 'Subject1'),
                                                                             (SELECT id
                                                                              FROM PUBLIC.ModifierFactory
                                                                              WHERE name = 'Subject One Or Two'));
INSERT INTO PUBLIC.ValidSubject (modified, subject, modifierFactory) VALUES (sysdate, (SELECT id
                                                                                       FROM PUBLIC.Subject
                                                                                       WHERE name = 'Subject2'),
                                                                             (SELECT id
                                                                              FROM PUBLIC.ModifierFactory
                                                                              WHERE name = 'Subject One Or Two'));

-- -- ENUMERATION_MODIFIER_FACTORY
INSERT INTO PUBLIC.ModifierFactory (modified, type, name, rangeFrom, rangeTo)
VALUES (sysdate, 'SUBJECT_MODIFIER_FACTORY', 'Slow Or Fast', NULL, NULL);
INSERT INTO PUBLIC.EnumerationItem (modified, value, modifierFactory) VALUES (sysdate, 'Slow', (SELECT id
                                                                                                FROM
                                                                                                  PUBLIC.ModifierFactory
                                                                                                WHERE name =
                                                                                                      'Slow Or Fast'));
INSERT INTO PUBLIC.EnumerationItem (modified, value, modifierFactory) VALUES (sysdate, 'Fast', (SELECT id
                                                                                                FROM
                                                                                                  PUBLIC.ModifierFactory
                                                                                                WHERE name =
                                                                                                      'Slow Or Fast'));

-- Actions
INSERT INTO PUBLIC.Action (modified, name, alias, recurring, modifierFactory, type)
VALUES (sysdate, 'Howling', NULL, NULL, NULL, 'POINT');
INSERT INTO PUBLIC.Action (modified, name, alias, recurring, modifierFactory, type)
VALUES (sysdate, 'Fighting', NULL, 0, NULL, 'STATE');
INSERT INTO PUBLIC.Action (modified, name, alias, recurring, modifierFactory, type)
VALUES (sysdate, 'Scratching', 'Scr', 5, NULL, 'STATE');
INSERT INTO PUBLIC.Action (modified, name, alias, recurring, modifierFactory, type)
VALUES (sysdate, 'Running', 'Ru', 5, (SELECT id
                                      FROM
                                        PUBLIC.ModifierFactory
                                      WHERE name =
                                            'Slow Or Fast'), 'STATE');

-- Coding
INSERT INTO PUBLIC.Coding (modified, subject, action, modifier, observation, start, end) VALUES (sysdate, (SELECT id
                                                                                                           FROM
                                                                                                             PUBLIC.Subject
                                                                                                           WHERE name =
                                                                                                                 'Subject1'),
                                                                                                 (SELECT id
                                                                                                  FROM PUBLIC.Action
                                                                                                  WHERE
                                                                                                    name = 'Howling'),
                                                                                                 NULL, (SELECT id
                                                                                                        FROM
                                                                                                          PUBLIC.Observation
                                                                                                        WHERE name =
                                                                                                              'Observation1'),
                                                                                                 300, NULL);

COMMIT;