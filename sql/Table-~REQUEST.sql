-- Table: "~REQUEST"

-- DROP TABLE "~REQUEST";

CREATE TABLE "~REQUEST"
(
  id integer NOT NULL,
  "type" character varying(2),
  params character varying(50),
  date timestamp without time zone
)
WITH (
  OIDS=FALSE
);