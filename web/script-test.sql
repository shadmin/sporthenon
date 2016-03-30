ALTER TABLE "Result" DISABLE TRIGGER "TriggerRS";

update "Result" set date2=substring(date2, 4, 2) || '/' || substring(date2, 1, 2) || '/' || substring(date2, 7, 4)
where id_sport=50 and id_championship=1;

ALTER TABLE "Result" ENABLE TRIGGER "TriggerRS";