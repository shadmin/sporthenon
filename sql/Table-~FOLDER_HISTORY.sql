-- Table: "~FOLDER_HISTORY"

-- DROP TABLE "~FOLDER_HISTORY";

CREATE TABLE "~FOLDER_HISTORY"
(
  id integer NOT NULL,
  previous_params character varying(30),
  current_params character varying(30),
  current_path character varying(255),
  date timestamp without time zone
)
WITH (
  OIDS=FALSE
);