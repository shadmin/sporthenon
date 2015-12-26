-- Table: "~Config"

-- DROP TABLE "~Config";

CREATE TABLE "~Config"
(
  key character varying(50) NOT NULL,
  value character varying(100),
  CONSTRAINT "~Config_pkey" PRIMARY KEY (key)
)
WITH (
  OIDS=FALSE
);
