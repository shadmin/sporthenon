-- Table: "~Contributor"

-- DROP TABLE "~Contributor";

CREATE TABLE "~Contributor"
(
  id integer NOT NULL,
  "login" character varying(15) NOT NULL,
  "password" character varying(35),
  email character varying(50),
  active boolean,
  public_name character varying(100),
  "admin" boolean,
  sports character varying(50),
  CONSTRAINT "~MEMBER_pkey" PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);