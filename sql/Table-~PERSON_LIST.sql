-- Table: "~PERSON_LIST"

-- DROP TABLE "~PERSON_LIST";

CREATE TABLE "~PERSON_LIST"
(
  id integer NOT NULL,
  id_result integer NOT NULL,
  rank integer NOT NULL,
  id_person integer NOT NULL
)
WITH (
  OIDS=FALSE
);

-- Trigger: trigger_pl on "~PERSON_LIST"

-- DROP TRIGGER trigger_pl ON "~PERSON_LIST";

CREATE TRIGGER trigger_pl
  AFTER INSERT OR UPDATE OR DELETE
  ON "~PERSON_LIST"
  FOR EACH ROW
  EXECUTE PROCEDURE "UPDATE_REF"('PL');

