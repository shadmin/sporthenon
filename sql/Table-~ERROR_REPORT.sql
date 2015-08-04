-- Table: "~ERROR_REPORT"

-- DROP TABLE "~ERROR_REPORT";

CREATE TABLE "~ERROR_REPORT"
(
  id integer NOT NULL,
  url character varying(255),
  text_ text,
  date timestamp without time zone
)
WITH (
  OIDS=FALSE
);