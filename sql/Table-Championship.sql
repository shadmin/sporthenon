-- Table: "Championship"

-- DROP TABLE "Championship";

CREATE TABLE "Championship"
(
  id integer NOT NULL,
  label character varying(40) NOT NULL,
  label_fr character varying(40) NOT NULL,
  id_contributor integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  index double precision,
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  ref smallint,
  no_pic boolean,
  CONSTRAINT "CHAMPIONSHIP_pkey" PRIMARY KEY (id),
  CONSTRAINT "CHAMPIONSHIP_id_member_fkey" FOREIGN KEY (id_contributor)
      REFERENCES "~Contributor" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "CHAMPIONSHIP_label_key" UNIQUE (label)
)
WITH (
  OIDS=FALSE
);
