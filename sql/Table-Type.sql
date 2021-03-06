-- Table: "Type"

-- DROP TABLE "Type";

CREATE TABLE "Type"
(
  id integer NOT NULL,
  label character varying(25) NOT NULL,
  label_fr character varying(25) NOT NULL,
  "number" smallint NOT NULL,
  id_contributor integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  CONSTRAINT "TYPE_pkey" PRIMARY KEY (id),
  CONSTRAINT "TYPE_id_member_fkey" FOREIGN KEY (id_contributor)
      REFERENCES "~Contributor" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL
)
WITH (
  OIDS=FALSE
);
