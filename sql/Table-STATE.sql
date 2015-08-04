-- Table: "STATE"

-- DROP TABLE "STATE";

CREATE TABLE "STATE"
(
  id integer NOT NULL,
  code character varying(2) NOT NULL,
  label character varying(25) NOT NULL,
  label_fr character varying(25) NOT NULL,
  capital character varying(20) NOT NULL,
  id_contributor integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  ref smallint,
  CONSTRAINT "STATE_pkey" PRIMARY KEY (id),
  CONSTRAINT "STATE_id_member_fkey" FOREIGN KEY (id_contributor)
      REFERENCES "~Contributor" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "STATE_label_key" UNIQUE (label)
)
WITH (
  OIDS=FALSE
);