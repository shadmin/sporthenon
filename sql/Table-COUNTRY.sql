-- Table: "COUNTRY"

-- DROP TABLE "COUNTRY";

CREATE TABLE "COUNTRY"
(
  id integer NOT NULL,
  code character varying(3) NOT NULL,
  label character varying(35) NOT NULL,
  label_fr character varying(35) NOT NULL,
  id_member integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  url_wiki character varying(200),
  url_olyref character varying(200),
  ref smallint,
  CONSTRAINT "COUNTRY_pkey" PRIMARY KEY (id),
  CONSTRAINT "COUNTRY_id_member_fkey" FOREIGN KEY (id_member)
      REFERENCES "~MEMBER" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL
)
WITH (
  OIDS=FALSE
);
