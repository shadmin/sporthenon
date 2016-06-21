-- Table: "Country"

-- DROP TABLE "Country";

CREATE TABLE "Country"
(
  id integer NOT NULL,
  code character varying(3) NOT NULL,
  label character varying(35) NOT NULL,
  label_fr character varying(35) NOT NULL,
  id_contributor integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  ref smallint,
  no_pic boolean,
  CONSTRAINT "COUNTRY_pkey" PRIMARY KEY (id),
  CONSTRAINT "COUNTRY_id_member_fkey" FOREIGN KEY (id_contributor)
      REFERENCES "~Contributor" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL
)
WITH (
  OIDS=FALSE
);
