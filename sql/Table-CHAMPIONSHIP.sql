-- Table: "CHAMPIONSHIP"

-- DROP TABLE "CHAMPIONSHIP";

CREATE TABLE "CHAMPIONSHIP"
(
  id integer NOT NULL,
  label character varying(40) NOT NULL,
  label_fr character varying(40) NOT NULL,
  id_member integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  "index" integer,
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  ref smallint,
  CONSTRAINT "CHAMPIONSHIP_pkey" PRIMARY KEY (id),
  CONSTRAINT "CHAMPIONSHIP_id_member_fkey" FOREIGN KEY (id_member)
      REFERENCES "~MEMBER" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "CHAMPIONSHIP_label_key" UNIQUE (label)
)
WITH (
  OIDS=FALSE
);
