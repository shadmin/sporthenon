ALTER TABLE "Athlete" rename photo_copyright to photo_source;
ALTER TABLE "City" rename photo_copyright to photo_source;
ALTER TABLE "Complex" rename photo_copyright to photo_source;
ALTER TABLE "Result" rename photo_copyright to photo_source;


CREATE TABLE "~Translation"
(
  id integer NOT NULL,
  entity character varying(2),
  id_item integer,
  checked boolean
);

CREATE SEQUENCE "~SeqTranslation";



