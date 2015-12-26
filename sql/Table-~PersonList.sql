-- Table: "~PersonList"

-- DROP TABLE "~PersonList";

CREATE TABLE "~PersonList"
(
  id integer NOT NULL,
  id_result integer NOT NULL,
  rank integer NOT NULL,
  id_person integer NOT NULL,
  index character varying(20)
)
WITH (
  OIDS=FALSE
);

-- Trigger: TriggerPL on "~PersonList"

-- DROP TRIGGER "TriggerPL" ON "~PersonList";

CREATE TRIGGER "TriggerPL"
  AFTER INSERT OR UPDATE OR DELETE
  ON "~PersonList"
  FOR EACH ROW
  EXECUTE PROCEDURE "UpdateRef"('PL');
