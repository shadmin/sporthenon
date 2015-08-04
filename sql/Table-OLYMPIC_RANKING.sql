-- Table: "OLYMPIC_RANKING"

-- DROP TABLE "OLYMPIC_RANKING";

CREATE TABLE "OLYMPIC_RANKING"
(
  id integer NOT NULL,
  id_olympics integer NOT NULL,
  id_country integer NOT NULL,
  count_gold smallint NOT NULL,
  count_silver smallint NOT NULL,
  count_bronze smallint NOT NULL,
  id_contributor integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  CONSTRAINT "OLYMPIC_RANKING_pkey" PRIMARY KEY (id),
  CONSTRAINT "OLYMPIC_RANKING_id_country_fkey" FOREIGN KEY (id_country)
      REFERENCES "COUNTRY" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "OLYMPIC_RANKING_id_member_fkey" FOREIGN KEY (id_contributor)
      REFERENCES "~Contributor" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "OLYMPIC_RANKING_id_olympics_fkey" FOREIGN KEY (id_olympics)
      REFERENCES "OLYMPICS" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL
)
WITH (
  OIDS=FALSE
);

-- Trigger: trigger_or on "OLYMPIC_RANKING"

-- DROP TRIGGER trigger_or ON "OLYMPIC_RANKING";

CREATE TRIGGER trigger_or
  AFTER INSERT OR UPDATE OR DELETE
  ON "OLYMPIC_RANKING"
  FOR EACH ROW
  EXECUTE PROCEDURE "UPDATE_REF"('OR');

