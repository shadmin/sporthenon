-- Table: "~RefItem"

-- DROP TABLE "~RefItem";

CREATE TABLE "~RefItem"
(
  id integer NOT NULL,
  id_item integer NOT NULL,
  label character varying(500) NOT NULL,
  entity character varying(2) NOT NULL,
  count_ref smallint NOT NULL,
  id_rel1 integer,
  id_rel2 integer,
  id_rel3 integer,
  label_rel1 character varying(200),
  label_rel2 character varying(200),
  label_rel3 character varying(200),
  id_rel4 integer,
  label_rel4 character varying(200),
  comment character varying(20),
  txt1 character varying(40),
  txt2 character varying(40),
  id_rel5 integer,
  label_rel5 character varying(200),
  link integer,
  id_rel6 integer,
  id_rel7 integer,
  id_rel8 integer,
  id_rel9 integer,
  id_rel10 integer,
  label_rel6 character varying(200),
  label_rel7 character varying(200),
  label_rel8 character varying(200),
  label_rel9 character varying(200),
  label_rel10 character varying(200),
  txt3 character varying(500),
  txt4 character varying(500),
  id_rel11 integer,
  label_rel11 character varying(200),
  id_rel12 integer,
  id_rel13 integer,
  id_rel14 integer,
  id_rel15 integer,
  id_rel16 integer,
  id_rel17 integer,
  count1 smallint,
  count2 smallint,
  count3 smallint,
  count4 smallint,
  count5 smallint,
  label_rel12 character varying(200),
  id_rel18 integer,
  label_rel18 character varying(200),
  date1 timestamp without time zone,
  label_en character varying(500),
  label_rel13 character varying(200),
  label_rel14 character varying(200),
  label_rel15 character varying(200),
  label_rel16 character varying(200),
  label_rel17 character varying(200),
  label_rel19 character varying(200),
  label_rel20 character varying(200),
  label_rel21 character varying(200),
  label_rel22 character varying(200),
  label_rel23 character varying(200),
  label_rel24 character varying(200),
  label_rel25 character varying(200),
  date2 timestamp without time zone,
  txt5 character varying(50),
  txt6 text,
  date3 timestamp without time zone,
  CONSTRAINT "~RefItem_pkey" PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
