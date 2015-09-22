-- Table: "~TreeItem"

-- DROP TABLE "~TreeItem";

CREATE TABLE "~TreeItem"
(
  id integer NOT NULL,
  id_item integer NOT NULL,
  label character varying(50) NOT NULL,
  "level" smallint NOT NULL,
  label_en character varying(50)
)
WITH (
  OIDS=FALSE
);