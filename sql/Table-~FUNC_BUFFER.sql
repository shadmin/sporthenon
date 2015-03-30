-- Table: "~FUNC_BUFFER"

-- DROP TABLE "~FUNC_BUFFER";

CREATE TABLE "~FUNC_BUFFER"
(
  id integer NOT NULL,
  int_value integer,
  char_value character varying(100),
  CONSTRAINT "~FUNC_BUFFER_pkey" PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
