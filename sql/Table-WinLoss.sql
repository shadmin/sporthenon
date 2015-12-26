-- Table: "WinLoss"

-- DROP TABLE "WinLoss";

CREATE TABLE "WinLoss"
(
  id integer NOT NULL,
  id_league integer NOT NULL,
  id_team integer NOT NULL,
  type character varying(35) NOT NULL,
  count_win smallint NOT NULL,
  count_loss smallint NOT NULL,
  count_tie smallint,
  count_otloss smallint,
  id_contributor integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  CONSTRAINT "WIN_LOSS_pkey" PRIMARY KEY (id),
  CONSTRAINT "WIN_LOSS_id_league_fkey" FOREIGN KEY (id_league)
      REFERENCES "League" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "WIN_LOSS_id_member_fkey" FOREIGN KEY (id_contributor)
      REFERENCES "~Contributor" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "WIN_LOSS_id_team_fkey" FOREIGN KEY (id_team)
      REFERENCES "Team" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL
)
WITH (
  OIDS=FALSE
);

-- Trigger: TriggerWL on "WinLoss"

-- DROP TRIGGER "TriggerWL" ON "WinLoss";

CREATE TRIGGER "TriggerWL"
  AFTER INSERT OR UPDATE OR DELETE
  ON "WinLoss"
  FOR EACH ROW
  EXECUTE PROCEDURE "UpdateRef"('WL');
