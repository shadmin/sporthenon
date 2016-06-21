-- Table: "Complex"

-- DROP TABLE "Complex";

CREATE TABLE "Complex"
(
  id integer NOT NULL,
  label character varying(80) NOT NULL,
  id_city integer NOT NULL,
  id_contributor integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  ref smallint,
  link integer,
  photo_source character varying(100),
  CONSTRAINT "COMPLEX_pkey" PRIMARY KEY (id),
  CONSTRAINT "COMPLEX_id_city_fkey" FOREIGN KEY (id_city)
      REFERENCES "City" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "COMPLEX_id_member_fkey" FOREIGN KEY (id_contributor)
      REFERENCES "~Contributor" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL
)
WITH (
  OIDS=FALSE
);

-- Trigger: TriggerCX on "Complex"

-- DROP TRIGGER "TriggerCX" ON "Complex";

CREATE TRIGGER "TriggerCX"
  AFTER INSERT OR UPDATE OR DELETE
  ON "Complex"
  FOR EACH ROW
  EXECUTE PROCEDURE "UpdateRef"('CX');
