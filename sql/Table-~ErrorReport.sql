-- Table: "~ErrorReport"

-- DROP TABLE "~ErrorReport";

CREATE TABLE "~ErrorReport"
(
  id integer NOT NULL,
  url character varying(255),
  text_ text,
  date timestamp without time zone
)
WITH (
  OIDS=FALSE
);