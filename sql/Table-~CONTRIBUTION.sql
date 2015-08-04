-- Table: "~CONTRIBUTION"

-- DROP TABLE "~CONTRIBUTION";

CREATE TABLE "~CONTRIBUTION"
(
  id integer NOT NULL,
  id_item integer NOT NULL,
  id_contributor integer NOT NULL,
  "type" character(1),
  date timestamp without time zone,
  CONSTRAINT "~CONTRIBUTION_pkey" PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);