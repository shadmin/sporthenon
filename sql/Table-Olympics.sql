-- Table: "Olympics"

-- DROP TABLE "Olympics";

CREATE TABLE "Olympics"
(
  id integer NOT NULL,
  id_year integer NOT NULL,
  id_city integer NOT NULL,
  count_country smallint NOT NULL,
  count_person smallint NOT NULL,
  count_sport smallint NOT NULL,
  count_event smallint NOT NULL,
  date1 character varying(10) NOT NULL,
  date2 character varying(10) NOT NULL,
  type smallint NOT NULL,
  id_contributor integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  ref smallint,
  CONSTRAINT "OLYMPICS_pkey" PRIMARY KEY (id),
  CONSTRAINT "OLYMPICS_id_city_fkey" FOREIGN KEY (id_city)
      REFERENCES "City" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "OLYMPICS_id_member_fkey" FOREIGN KEY (id_contributor)
      REFERENCES "~Contributor" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "OLYMPICS_id_year_fkey" FOREIGN KEY (id_year)
      REFERENCES "Year" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "OLYMPICS_id_year_key" UNIQUE (id_year, id_city)
)
WITH (
  OIDS=FALSE
);

-- Trigger: TriggerOL on "Olympics"

-- DROP TRIGGER "TriggerOL" ON "Olympics";

CREATE TRIGGER "TriggerOL"
  AFTER INSERT OR UPDATE OR DELETE
  ON "Olympics"
  FOR EACH ROW
  EXECUTE PROCEDURE "UpdateRef"('OL');
