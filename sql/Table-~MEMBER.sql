-- Table: "~MEMBER"

-- DROP TABLE "~MEMBER";

CREATE TABLE "~MEMBER"
(
  id integer NOT NULL,
  "login" character varying(15) NOT NULL,
  "password" character varying(35),
  email character varying(50),
  active boolean,
  public_name character varying(100),
  CONSTRAINT "~MEMBER_pkey" PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
