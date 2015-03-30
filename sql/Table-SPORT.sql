-- Table: "SPORT"

-- DROP TABLE "SPORT";

CREATE TABLE "SPORT"
(
  id integer NOT NULL,
  label character varying(25) NOT NULL,
  label_fr character varying(25) NOT NULL,
  website character varying(50) DEFAULT NULL::character varying,
  "type" smallint,
  id_member integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  url_wiki character varying(200),
  url_olyref character varying(200),
  wiki_pattern character varying(30),
  ref smallint,
  CONSTRAINT "SPORT_pkey" PRIMARY KEY (id),
  CONSTRAINT "SPORT_id_member_fkey" FOREIGN KEY (id_member)
      REFERENCES "~MEMBER" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "SPORT_label_key" UNIQUE (label)
)
WITH (
  OIDS=FALSE
);
