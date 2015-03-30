-- Table: "~INACTIVE_ITEM"

-- DROP TABLE "~INACTIVE_ITEM";

CREATE TABLE "~INACTIVE_ITEM"
(
  id integer NOT NULL,
  id_sport integer NOT NULL,
  id_championship integer NOT NULL,
  id_event integer,
  id_subevent integer,
  id_subevent2 integer
)
WITH (
  OIDS=FALSE
);
