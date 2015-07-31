ALTER TABLE "RESULT" DISABLE TRIGGER trigger_rs;
update "RESULT" set comment = replace(comment, ' kg', 'kg') where comment like '% kg%';
ALTER TABLE "RESULT" ENABLE TRIGGER trigger_rs;

