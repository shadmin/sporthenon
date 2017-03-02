-- Table: "~Import"

-- DROP TABLE "~Import";

CREATE TABLE "~Import"
(
  id integer NOT NULL,
  date timestamp without time zone,
  csv_content text,
  CONSTRAINT "~Import_pkey" PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
