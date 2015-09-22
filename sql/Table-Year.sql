-- Table: "Year"

-- DROP TABLE "Year";

CREATE TABLE "Year"
(
  id integer NOT NULL,
  label character varying(4) NOT NULL,
  id_contributor integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  ref smallint,
  CONSTRAINT "YEAR_pkey" PRIMARY KEY (id),
  CONSTRAINT "YEAR_id_member_fkey" FOREIGN KEY (id_contributor)
      REFERENCES "~Contributor" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "YEAR_label_key" UNIQUE (label)
)
WITH (
  OIDS=FALSE
);