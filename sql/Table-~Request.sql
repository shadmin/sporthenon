-- Table: "~Request"

-- DROP TABLE "~Request";

CREATE TABLE "~Request"
(
  id integer NOT NULL,
  path character varying(20),
  params character varying(30),
  date timestamp without time zone,
  CONSTRAINT "~Request_pkey" PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
