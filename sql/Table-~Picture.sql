-- Table: "~Picture"

-- DROP TABLE "~Picture";

CREATE TABLE "~Picture"
(
  id integer NOT NULL,
  entity character varying(2),
  id_item integer,
  value text,
  source character varying(100),
  embedded boolean,
  CONSTRAINT "~Picture_pkey" PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
