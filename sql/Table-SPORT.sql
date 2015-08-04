-- Table: "SPORT"

-- DROP TABLE "SPORT";

CREATE TABLE "SPORT"
(
  id integer NOT NULL,
  label character varying(25) NOT NULL,
  label_fr character varying(25) NOT NULL,
  "type" smallint,
  id_contributor integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  ref smallint,
  "index" double precision,
  img_url character varying(255),
  CONSTRAINT "SPORT_pkey" PRIMARY KEY (id),
  CONSTRAINT "SPORT_id_member_fkey" FOREIGN KEY (id_contributor)
      REFERENCES "~Contributor" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "SPORT_label_key" UNIQUE (label)
)
WITH (
  OIDS=FALSE
);