-- Table: "~ExternalLink"

-- DROP TABLE "~ExternalLink";

CREATE TABLE "~ExternalLink"
(
  id integer NOT NULL,
  entity character varying(2),
  id_item integer,
  url character varying(200),
  checked boolean,
  flag character(1),
  CONSTRAINT "~EXTERNAL_LINK_pkey" PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
