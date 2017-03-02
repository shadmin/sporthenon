-- Table: "City"

-- DROP TABLE "City";

CREATE TABLE "City"
(
  id integer NOT NULL,
  label character varying(30) NOT NULL,
  label_fr character varying(30) NOT NULL,
  id_country integer NOT NULL,
  id_state integer,
  id_contributor integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  ref smallint,
  link integer,
  CONSTRAINT "CITY_pkey" PRIMARY KEY (id),
  CONSTRAINT "CITY_id_member_fkey" FOREIGN KEY (id_contributor)
      REFERENCES "~Contributor" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "CITY_label_key" UNIQUE (label, id_state, id_country)
)
WITH (
  OIDS=FALSE
);

-- Index: "CT_LABEL_FR_INDEX"

-- DROP INDEX "CT_LABEL_FR_INDEX";

CREATE INDEX "CT_LABEL_FR_INDEX"
  ON "City"
  USING btree
  (lower(label_fr::text) COLLATE pg_catalog."default");

-- Index: "CT_LABEL_INDEX"

-- DROP INDEX "CT_LABEL_INDEX";

CREATE INDEX "CT_LABEL_INDEX"
  ON "City"
  USING btree
  (lower(label::text) COLLATE pg_catalog."default");


-- Trigger: TriggerCT on "City"

-- DROP TRIGGER "TriggerCT" ON "City";

CREATE TRIGGER "TriggerCT"
  AFTER INSERT OR UPDATE OR DELETE
  ON "City"
  FOR EACH ROW
  EXECUTE PROCEDURE "UpdateRef"('CT');
