-- Table: "~InactiveItem"

-- DROP TABLE "~InactiveItem";

CREATE TABLE "~InactiveItem"
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