-- Table: "TeamStadium"

-- DROP TABLE "TeamStadium";

CREATE TABLE "TeamStadium"
(
  id integer NOT NULL,
  id_league integer NOT NULL,
  id_team integer NOT NULL,
  id_complex integer NOT NULL,
  date1 smallint NOT NULL,
  date2 smallint NOT NULL,
  comment character varying(500) DEFAULT NULL::character varying,
  id_contributor integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  renamed boolean,
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  CONSTRAINT "TEAM_STADIUM_pkey" PRIMARY KEY (id),
  CONSTRAINT "TEAM_STADIUM_id_complex_fkey" FOREIGN KEY (id_complex)
      REFERENCES "Complex" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "TEAM_STADIUM_id_league_fkey" FOREIGN KEY (id_league)
      REFERENCES "League" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "TEAM_STADIUM_id_member_fkey" FOREIGN KEY (id_contributor)
      REFERENCES "~Contributor" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "TEAM_STADIUM_id_team_fkey" FOREIGN KEY (id_team)
      REFERENCES "Team" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL
)
WITH (
  OIDS=FALSE
);

-- Trigger: TriggerTS on "TeamStadium"

-- DROP TRIGGER "TriggerTS" ON "TeamStadium";

CREATE TRIGGER "TriggerTS"
  AFTER INSERT OR UPDATE OR DELETE
  ON "TeamStadium"
  FOR EACH ROW
  EXECUTE PROCEDURE "UpdateRef"('TS');
