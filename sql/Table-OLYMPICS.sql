-- Table: "OLYMPICS"

-- DROP TABLE "OLYMPICS";

CREATE TABLE "OLYMPICS"
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
  "type" smallint NOT NULL,
  id_member integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  url_wiki character varying(200),
  url_olyref character varying(200),
  ref smallint,
  CONSTRAINT "OLYMPICS_pkey" PRIMARY KEY (id),
  CONSTRAINT "OLYMPICS_id_city_fkey" FOREIGN KEY (id_city)
      REFERENCES "CITY" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "OLYMPICS_id_member_fkey" FOREIGN KEY (id_member)
      REFERENCES "~MEMBER" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "OLYMPICS_id_year_fkey" FOREIGN KEY (id_year)
      REFERENCES "YEAR" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "OLYMPICS_id_year_key" UNIQUE (id_year, id_city)
)
WITH (
  OIDS=FALSE
);

-- Trigger: trigger_ol on "OLYMPICS"

-- DROP TRIGGER trigger_ol ON "OLYMPICS";

CREATE TRIGGER trigger_ol
  AFTER INSERT OR UPDATE OR DELETE
  ON "OLYMPICS"
  FOR EACH ROW
  EXECUTE PROCEDURE "UPDATE_REF"('OL');
