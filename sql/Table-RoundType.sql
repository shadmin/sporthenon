-- Table: "RoundType"

-- DROP TABLE "RoundType";

CREATE TABLE "RoundType"
(
  id integer NOT NULL,
  label character varying(25),
  label_fr character varying(25),
  index integer,
  id_contributor integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  first_update timestamp without time zone NOT NULL DEFAULT now()
)
WITH (
  OIDS=FALSE
);
