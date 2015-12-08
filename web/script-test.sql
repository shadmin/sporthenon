CREATE TABLE "RoundType" (
id integer NOT NULL,
label character varying(25),
  label_fr character varying(25),
  "index" integer
);

CREATE SEQUENCE "SeqRoundType";
CREATE SEQUENCE "SeqRound";

CREATE TABLE "Round"
(
  id integer NOT NULL,
  id_result integer NOT NULL,
  id_result_type integer NOT NULL,
  id_round_type integer NOT NULL,
  id_rank1 integer,
  result1 character varying(40),
  id_rank2 integer,
  result2 character varying(20),
  id_rank3 integer,
  result3 character varying(20),
  id_city integer,
  id_complex integer,
  date character varying(10),
  exa character varying(15),
  "comment" character varying(500),
  id_contributor integer NOT NULL,
  last_update timestamp without time zone NOT NULL DEFAULT now(),
  first_update timestamp without time zone NOT NULL DEFAULT now(),
  CONSTRAINT "Round_pkey" PRIMARY KEY (id),
  CONSTRAINT "Round_id_city_fkey" FOREIGN KEY (id_city)
      REFERENCES "City" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);



insert into "Round" select nextval('"SeqRound"'), T.id_result, 1, 1, id1_qf1, result_qf1, id2_qf1, null, null, null, null, null, null, null, null, 1, T.last_update, T.first_update from "Draw" T;
insert into "Round" select nextval('"SeqRound"'), T.id_result, 1, 2, id1_qf2, result_qf2, id2_qf2, null, null, null, null, null, null, null, null, 1, T.last_update, T.first_update from "Draw" T;
insert into "Round" select nextval('"SeqRound"'), T.id_result, 1, 3, id1_qf3, result_qf3, id2_qf3, null, null, null, null, null, null, null, null, 1, T.last_update, T.first_update from "Draw" T;
insert into "Round" select nextval('"SeqRound"'), T.id_result, 1, 4, id1_qf4, result_qf4, id2_qf4, null, null, null, null, null, null, null, null, 1, T.last_update, T.first_update from "Draw" T;
insert into "Round" select nextval('"SeqRound"'), T.id_result, 1, 5, id1_sf1, result_sf1, id2_sf1, null, null, null, null, null, null, null, null, 1, T.last_update, T.first_update from "Draw" T;
insert into "Round" select nextval('"SeqRound"'), T.id_result, 1, 6, id1_sf2, result_sf2, id2_sf2, null, null, null, null, null, null, null, null, 1, T.last_update, T.first_update from "Draw" T;
insert into "Round" select nextval('"SeqRound"'), T.id_result, 99, 7, id1_thd, result_thd, id2_thd, null, null, null, null, null, null, null, null, 1, T.last_update, T.first_update from "Draw" T where t.id1_thd is not null;




ALTER TABLE "RoundType" add  id_contributor integer NOT NULL;
  ALTER TABLE "RoundType" add  last_update timestamp without time zone NOT NULL DEFAULT now();
  ALTER TABLE "RoundType" add  first_update timestamp without time zone NOT NULL DEFAULT now();
  
  
  
  insert into "RoundType" values (nextval('"SeqRoundType"'), 'Quarterfinal #1', 'Quart de finale #1', 1, 1, current_date, current_date);
insert into "RoundType" values (nextval('"SeqRoundType"'), 'Quarterfinal #2', 'Quart de finale #2', 2, 1, current_date, current_date);
insert into "RoundType" values (nextval('"SeqRoundType"'), 'Quarterfinal #3', 'Quart de finale #3', 3, 1, current_date, current_date);
insert into "RoundType" values (nextval('"SeqRoundType"'), 'Quarterfinal #4', 'Quart de finale #4', 4, 1, current_date, current_date);
insert into "RoundType" values (nextval('"SeqRoundType"'), 'Semifinal #1', 'Demi-finale #1', 5, 1, current_date, current_date);
insert into "RoundType" values (nextval('"SeqRoundType"'), 'Semifinal #2', 'Demi-finale #2', 6, 1, current_date, current_date);
insert into "RoundType" values (nextval('"SeqRoundType"'), 'Third place game', 'Match pour la 3ème place', 7, 1, current_date, current_date);





  