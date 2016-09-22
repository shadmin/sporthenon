-- Table: "~Config"

-- DROP TABLE "~Config";

CREATE TABLE "~Config"
(
  key character varying(50) NOT NULL,
  value character varying(300),
  value_html text,
  CONSTRAINT "~Config_pkey" PRIMARY KEY (key)
)
WITH (
  OIDS=FALSE
);
