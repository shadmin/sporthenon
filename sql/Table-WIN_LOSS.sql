-- Table: "WIN_LOSS"

-- DROP TABLE "WIN_LOSS";

CREATE TABLE "WIN_LOSS"
(
  id integer NOT NULL,
  id_league integer NOT NULL,
  id_team integer NOT NULL,
  "type" character varying(35) NOT NULL,
  count_win smallint NOT NULL,
  count_loss smallint NOT NULL,
  count_tie smallint,
  count_otloss smallint,
  average character varying(5) DEFAULT NULL::character varying,
  id_member integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  CONSTRAINT "WIN_LOSS_pkey" PRIMARY KEY (id),
  CONSTRAINT "WIN_LOSS_id_league_fkey" FOREIGN KEY (id_league)
      REFERENCES "LEAGUE" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "WIN_LOSS_id_member_fkey" FOREIGN KEY (id_member)
      REFERENCES "~MEMBER" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "WIN_LOSS_id_team_fkey" FOREIGN KEY (id_team)
      REFERENCES "TEAM" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL
)
WITH (
  OIDS=FALSE
);

-- Trigger: trigger_wl on "WIN_LOSS"

-- DROP TRIGGER trigger_wl ON "WIN_LOSS";

CREATE TRIGGER trigger_wl
  AFTER INSERT OR UPDATE OR DELETE
  ON "WIN_LOSS"
  FOR EACH ROW
  EXECUTE PROCEDURE "UPDATE_REF"('WL');

