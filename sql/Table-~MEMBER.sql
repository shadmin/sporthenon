-- Table: "~MEMBER"

-- DROP TABLE "~MEMBER";

CREATE TABLE "~MEMBER"
(
  id integer NOT NULL,
  "login" character varying(15) NOT NULL,
  last_name character varying(20) DEFAULT NULL::character varying,
  first_name character varying(20) DEFAULT NULL::character varying,
  "password" character varying(35),
  email character varying(50),
  active boolean,
  CONSTRAINT "~MEMBER_pkey" PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
