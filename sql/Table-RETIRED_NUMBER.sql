-- Table: "RETIRED_NUMBER"

-- DROP TABLE "RETIRED_NUMBER";

CREATE TABLE "RETIRED_NUMBER"
(
  id integer NOT NULL,
  id_league integer NOT NULL,
  id_team integer NOT NULL,
  id_person integer NOT NULL,
  "number" smallint NOT NULL,
  id_member integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  id_year integer,
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  CONSTRAINT "RETIRED_NUMBER_pkey" PRIMARY KEY (id),
  CONSTRAINT "RETIRED_NUMBER_id_league_fkey" FOREIGN KEY (id_league)
      REFERENCES "LEAGUE" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "RETIRED_NUMBER_id_member_fkey" FOREIGN KEY (id_member)
      REFERENCES "~MEMBER" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "RETIRED_NUMBER_id_person_fkey" FOREIGN KEY (id_person)
      REFERENCES "PERSON" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "RETIRED_NUMBER_id_team_fkey" FOREIGN KEY (id_team)
      REFERENCES "TEAM" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL
)
WITH (
  OIDS=FALSE
);

-- Trigger: trigger_rn on "RETIRED_NUMBER"

-- DROP TRIGGER trigger_rn ON "RETIRED_NUMBER";

CREATE TRIGGER trigger_rn
  AFTER INSERT OR UPDATE OR DELETE
  ON "RETIRED_NUMBER"
  FOR EACH ROW
  EXECUTE PROCEDURE "UPDATE_REF"('RN');
