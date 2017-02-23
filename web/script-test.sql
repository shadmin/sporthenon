CREATE TABLE "~Picture"
(
  id integer NOT NULL PRimary key,
  entity character varying(2),
  id_item integer,
  value text,
  source character varying(100),
  embedded boolean
);
CREATE SEQUENCE "~SeqPicture";


alter table "Athlete" drop photo_source;
alter table "City" drop photo_source;
alter table "Complex" drop photo_source;
alter table "Result" drop photo_source;


