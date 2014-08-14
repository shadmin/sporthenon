update "RESULT"
set date1=substring(date1 from 4 for 2) || '/' || substring(date1 from 1 for 2) || '/' || substring(date1 from 7)
where date1 is not null and date1<>'';
update "RESULT"
set date2=substring(date2 from 4 for 2) || '/' || substring(date2 from 1 for 2) || '/' || substring(date2 from 7)
where date2 is not null and date2<>'';

update "RECORD" set date1=substring(date1 from 4 for 2) || '/' || substring(date1 from 1 for 2) || '/' || substring(date1 from 7) where date1 is not null and date1 ~* '\\d\\d/\\d\\d/\\d\\d\\d\\d';
update "RECORD" set date2=substring(date2 from 4 for 2) || '/' || substring(date2 from 1 for 2) || '/' || substring(date2 from 7) where date2 is not null and date2 ~* '\\d\\d/\\d\\d/\\d\\d\\d\\d';
update "RECORD" set date3=substring(date3 from 4 for 2) || '/' || substring(date3 from 1 for 2) || '/' || substring(date3 from 7) where date3 is not null and date3 ~* '\\d\\d/\\d\\d/\\d\\d\\d\\d';
update "RECORD" set date4=substring(date4 from 4 for 2) || '/' || substring(date4 from 1 for 2) || '/' || substring(date4 from 7) where date4 is not null and date4 ~* '\\d\\d/\\d\\d/\\d\\d\\d\\d';
update "RECORD" set date5=substring(date5 from 4 for 2) || '/' || substring(date5 from 1 for 2) || '/' || substring(date5 from 7) where date5 is not null and date5 ~* '\\d\\d/\\d\\d/\\d\\d\\d\\d';

