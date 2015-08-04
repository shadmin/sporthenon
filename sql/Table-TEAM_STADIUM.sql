-- Table: "TEAM_STADIUM"

-- DROP TABLE "TEAM_STADIUM";

CREATE TABLE "TEAM_STADIUM"
(
  id integer NOT NULL,
  id_league integer NOT NULL,
  id_team integer NOT NULL,
  id_complex integer NOT NULL,
  date1 smallint NOT NULL,
  date2 smallint NOT NULL,
  "comment" character varying(500) DEFAULT NULL::character varying,
  id_contributor integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  renamed boolean,
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  CONSTRAINT "TEAM_STADIUM_pkey" PRIMARY KEY (id),
  CONSTRAINT "TEAM_STADIUM_id_complex_fkey" FOREIGN KEY (id_complex)
      REFERENCES "COMPLEX" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "TEAM_STADIUM_id_league_fkey" FOREIGN KEY (id_league)
      REFERENCES "LEAGUE" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "TEAM_STADIUM_id_member_fkey" FOREIGN KEY (id_contributor)
      REFERENCES "~Contributor" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "TEAM_STADIUM_id_team_fkey" FOREIGN KEY (id_team)
      REFERENCES "TEAM" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL
)
WITH (
  OIDS=FALSE
);

-- Trigger: trigger_ts on "TEAM_STADIUM"

-- DROP TRIGGER trigger_ts ON "TEAM_STADIUM";

CREATE TRIGGER trigger_ts
  AFTER INSERT OR UPDATE OR DELETE
  ON "TEAM_STADIUM"
  FOR EACH ROW
  EXECUTE PROCEDURE "UPDATE_REF"('TS');

