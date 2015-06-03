-- Table: "~EXTERNAL_LINK"

-- DROP TABLE "~EXTERNAL_LINK";

CREATE TABLE "~EXTERNAL_LINK"
(
  id integer NOT NULL,
  entity character varying(2),
  id_item integer,
  "type" character varying(10),
  url character varying(200),
  CONSTRAINT "~EXTERNAL_LINK_pkey" PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
