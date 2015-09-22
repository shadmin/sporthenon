-- Table: "~Request"

-- DROP TABLE "~Request";

CREATE TABLE "~Request"
(
  id integer NOT NULL,
  "type" character varying(2),
  params character varying(50),
  date timestamp without time zone
)
WITH (
  OIDS=FALSE
);