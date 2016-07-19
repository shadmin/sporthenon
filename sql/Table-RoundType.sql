-- Table: "RoundType"

-- DROP TABLE "RoundType";

CREATE TABLE "RoundType"
(
  id integer NOT NULL,
  label character varying(50),
  label_fr character varying(50),
  index double precision,
  id_contributor integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  CONSTRAINT "RoundType_pkey" PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
