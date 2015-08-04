-- Sequence: "~SeqFolderHistory"

-- DROP SEQUENCE "~SeqFolderHistory";

CREATE SEQUENCE "~SeqFolderHistory"
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE "~SeqFolderHistory" OWNER TO inachos;
