-- Table: "Event"

-- DROP TABLE "Event";

CREATE TABLE "Event"
(
  id integer NOT NULL,
  label character varying(50) NOT NULL,
  label_fr character varying(50) NOT NULL,
  id_type integer NOT NULL,
  id_contributor integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  index integer,
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  ref smallint,
  no_pic boolean,
  CONSTRAINT "EVENT_pkey" PRIMARY KEY (id),
  CONSTRAINT "EVENT_id_member_fkey" FOREIGN KEY (id_contributor)
      REFERENCES "~Contributor" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "EVENT_id_type_fkey" FOREIGN KEY (id_type)
      REFERENCES "Type" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL
)
WITH (
  OIDS=FALSE
);
