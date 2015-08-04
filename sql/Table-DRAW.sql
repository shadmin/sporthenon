-- Table: "DRAW"

-- DROP TABLE "DRAW";

CREATE TABLE "DRAW"
(
  id integer NOT NULL,
  id_result integer NOT NULL,
  id1_qf1 integer,
  id2_qf1 integer,
  id1_qf2 integer,
  id2_qf2 integer,
  id1_qf3 integer,
  id2_qf3 integer,
  id1_qf4 integer,
  id2_qf4 integer,
  id1_sf1 integer,
  id2_sf1 integer,
  id1_sf2 integer,
  id2_sf2 integer,
  result_qf1 character varying(40),
  result_qf2 character varying(40),
  result_qf3 character varying(40),
  result_qf4 character varying(40),
  result_sf1 character varying(40),
  result_sf2 character varying(40),
  id_contributor integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  id1_thd integer,
  id2_thd integer,
  result_thd character varying(40),
  CONSTRAINT "DRAW_pkey" PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);

-- Trigger: trigger_dr on "DRAW"

-- DROP TRIGGER trigger_dr ON "DRAW";

CREATE TRIGGER trigger_dr
  AFTER INSERT OR UPDATE OR DELETE
  ON "DRAW"
  FOR EACH ROW
  EXECUTE PROCEDURE "UPDATE_REF"('DR');

