-- Table: "~ExternalLink"

-- DROP TABLE "~ExternalLink";

CREATE TABLE "~ExternalLink"
(
  id integer NOT NULL,
  entity character varying(2),
  id_item integer,
  "type" character varying(10),
  url character varying(200),
  checked boolean,
  CONSTRAINT "~EXTERNAL_LINK_pkey" PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);