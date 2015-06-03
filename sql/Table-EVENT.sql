-- Table: "EVENT"

-- DROP TABLE "EVENT";

CREATE TABLE "EVENT"
(
  id integer NOT NULL,
  label character varying(45) NOT NULL,
  label_fr character varying(45) NOT NULL,
  id_type integer NOT NULL,
  id_member integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  "index" integer,
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  ref smallint,
  CONSTRAINT "EVENT_pkey" PRIMARY KEY (id),
  CONSTRAINT "EVENT_id_member_fkey" FOREIGN KEY (id_member)
      REFERENCES "~MEMBER" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "EVENT_id_type_fkey" FOREIGN KEY (id_type)
      REFERENCES "TYPE" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL
)
WITH (
  OIDS=FALSE
);
