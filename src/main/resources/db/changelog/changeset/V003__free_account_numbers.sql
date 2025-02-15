CREATE TABLE free_account_numbers
(
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    account_type smallint NOT NULL,
    account_number VARCHAR(20) NOT NULL UNIQUE CHECK (length(account_number) >= 12 AND account_number SIMILAR TO '[0-9]+'),
    CONSTRAINT uc_account_type_account_number UNIQUE (account_type)
);

CREATE UNIQUE INDEX account_type_number_idx ON free_account_numbers (account_type);

CREATE TABLE account_numbers_sequence
(
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    account_type smallint NOT NULL,
    current_count BIGINT NOT NULL DEFAULT 0,
    version BIGINT NOT NULL DEFAULT 0
);
CREATE UNIQUE INDEX account_numbers_sequence_ind ON account_numbers_sequence (account_type);

INSERT INTO account_numbers_sequence (account_type, current_count)
VALUES (0, 0),
       (1, 0),
       (2, 0),
       (3, 0);