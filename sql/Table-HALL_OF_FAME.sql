-- Table: "HALL_OF_FAME"

-- DROP TABLE "HALL_OF_FAME";

CREATE TABLE "HALL_OF_FAME"
(
  id integer NOT NULL,
  id_league integer NOT NULL,
  id_year integer NOT NULL,
  id_person integer NOT NULL,
  id_member integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  "position" character varying(30),
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  CONSTRAINT "HALL_OF_FAME_pkey" PRIMARY KEY (id),
  CONSTRAINT "HALL_OF_FAME_id_league_fkey" FOREIGN KEY (id_league)
      REFERENCES "LEAGUE" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "HALL_OF_FAME_id_member_fkey" FOREIGN KEY (id_member)
      REFERENCES "~MEMBER" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "HALL_OF_FAME_id_person_fkey" FOREIGN KEY (id_person)
      REFERENCES "PERSON" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "HALL_OF_FAME_id_year_fkey" FOREIGN KEY (id_year)
      REFERENCES "YEAR" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL
)
WITH (
  OIDS=FALSE
);

-- Trigger: trigger_hf on "HALL_OF_FAME"

-- DROP TRIGGER trigger_hf ON "HALL_OF_FAME";

CREATE TRIGGER trigger_hf
  AFTER INSERT OR UPDATE OR DELETE
  ON "HALL_OF_FAME"
  FOR EACH ROW
  EXECUTE PROCEDURE "UPDATE_REF"('HF');

