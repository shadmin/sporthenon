-- Table: "CITY"

-- DROP TABLE "CITY";

CREATE TABLE "CITY"
(
  id integer NOT NULL,
  label character varying(25) NOT NULL,
  label_fr character varying(25) NOT NULL,
  id_country integer NOT NULL,
  id_state integer,
  id_member integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  ref smallint,
  CONSTRAINT "CITY_pkey" PRIMARY KEY (id),
  CONSTRAINT "CITY_id_member_fkey" FOREIGN KEY (id_member)
      REFERENCES "~MEMBER" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "CITY_label_key" UNIQUE (label, id_state, id_country)
)
WITH (
  OIDS=FALSE
);

-- Trigger: trigger_ct on "CITY"

-- DROP TRIGGER trigger_ct ON "CITY";

CREATE TRIGGER trigger_ct
  AFTER INSERT OR UPDATE OR DELETE
  ON "CITY"
  FOR EACH ROW
  EXECUTE PROCEDURE "UPDATE_REF"('CT');

