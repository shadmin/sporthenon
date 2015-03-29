-- Table: "COMPLEX"

-- DROP TABLE "COMPLEX";

CREATE TABLE "COMPLEX"
(
  id integer NOT NULL,
  label character varying(80) NOT NULL,
  label_fr character varying(80) NOT NULL,
  id_city integer NOT NULL,
  id_member integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  url_wiki character varying(200),
  ref smallint,
  CONSTRAINT "COMPLEX_pkey" PRIMARY KEY (id),
  CONSTRAINT "COMPLEX_id_city_fkey" FOREIGN KEY (id_city)
      REFERENCES "CITY" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL,
  CONSTRAINT "COMPLEX_id_member_fkey" FOREIGN KEY (id_member)
      REFERENCES "~MEMBER" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL
)
WITH (
  OIDS=FALSE
);

-- Trigger: trigger_cx on "COMPLEX"

-- DROP TRIGGER trigger_cx ON "COMPLEX";

CREATE TRIGGER trigger_cx
  AFTER INSERT OR UPDATE OR DELETE
  ON "COMPLEX"
  FOR EACH ROW
  EXECUTE PROCEDURE "UPDATE_REF"('CX');
