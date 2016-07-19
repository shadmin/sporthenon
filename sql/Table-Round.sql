-- Table: "Round"

-- DROP TABLE "Round";

CREATE TABLE "Round"
(
  id integer NOT NULL,
  id_result integer NOT NULL,
  id_result_type integer NOT NULL,
  id_round_type integer NOT NULL,
  id_rank1 integer,
  result1 character varying(40),
  id_rank2 integer,
  result2 character varying(20),
  id_rank3 integer,
  result3 character varying(20),
  id_city integer,
  id_complex integer,
  date character varying(10),
  exa character varying(15),
  comment character varying(500),
  id_contributor integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  id_city1 integer,
  id_complex1 integer,
  CONSTRAINT "Round_pkey" PRIMARY KEY (id),
  CONSTRAINT "Round_id_city_fkey" FOREIGN KEY (id_city)
      REFERENCES "City" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

-- Trigger: TriggerRD on "Round"

-- DROP TRIGGER "TriggerRD" ON "Round";

CREATE TRIGGER "TriggerRD"
  AFTER INSERT OR UPDATE OR DELETE
  ON "Round"
  FOR EACH ROW
  EXECUTE PROCEDURE "UpdateRef"('RD');
