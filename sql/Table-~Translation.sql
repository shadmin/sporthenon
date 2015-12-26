-- Table: "~Translation"

-- DROP TABLE "~Translation";

CREATE TABLE "~Translation"
(
  id integer NOT NULL,
  entity character varying(2),
  id_item integer,
  checked boolean
)
WITH (
  OIDS=FALSE
);
