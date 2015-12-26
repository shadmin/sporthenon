-- Table: "~FolderHistory"

-- DROP TABLE "~FolderHistory";

CREATE TABLE "~FolderHistory"
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
