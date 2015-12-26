-- Table: "Calendar"

-- DROP TABLE "Calendar";

CREATE TABLE "Calendar"
(
  id integer NOT NULL,
  id_sport integer NOT NULL,
  id_championship integer NOT NULL,
  id_event integer,
  id_subevent integer,
  id_subevent2 integer,
  id_complex integer,
  id_city integer,
  date1 character varying(10) DEFAULT NULL::character varying,
  date2 character varying(10) DEFAULT NULL::character varying,
  id_contributor integer NOT NULL,
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  CONSTRAINT "Calendar_pkey" PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
