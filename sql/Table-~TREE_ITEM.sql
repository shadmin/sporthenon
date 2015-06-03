-- Table: "~TREE_ITEM"

-- DROP TABLE "~TREE_ITEM";

CREATE TABLE "~TREE_ITEM"
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
