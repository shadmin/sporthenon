-- Table: "LEAGUE"

-- DROP TABLE "LEAGUE";

CREATE TABLE "LEAGUE"
(
  id integer NOT NULL,
  label character varying(5) NOT NULL,
  id_member integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  CONSTRAINT "LEAGUE_pkey" PRIMARY KEY (id),
  CONSTRAINT "LEAGUE_id_member_fkey" FOREIGN KEY (id_member)
      REFERENCES "~MEMBER" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "LEAGUE_label_key" UNIQUE (label)
)
WITH (
  OIDS=FALSE
);
