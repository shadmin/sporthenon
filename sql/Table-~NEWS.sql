-- Table: "~NEWS"

-- DROP TABLE "~NEWS";

CREATE TABLE "~NEWS"
(
  title character varying(100) NOT NULL,
  text_html character varying(300),
  date timestamp without time zone,
  id integer NOT NULL DEFAULT 0,
  title_fr character varying(100),
  text_html_fr character varying(300),
  CONSTRAINT "~NEWS_pkey" PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
