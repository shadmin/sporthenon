update result set exa = null where exa = '>';

ALTER TABLE result ALTER COLUMN draft SET DEFAULT FALSE;
ALTER TABLE result ALTER COLUMN no_place SET DEFAULT FALSE;
ALTER TABLE result ALTER COLUMN no_date SET DEFAULT FALSE;

UPDATE result set draft = FALSE where draft is null;
ALTER TABLE result ALTER COLUMN draft SET NOT NULL;


CREATE SEQUENCE _s_config;

ALTER TABLE _config
    ADD COLUMN id integer;
    
UPDATE _config SET id = NEXTVAL('_s_config') WHERE key = 'cp_medal_pattern';
UPDATE _config SET id = NEXTVAL('_s_config') WHERE key = 'default_lastupdates_limit';
UPDATE _config SET id = NEXTVAL('_s_config') WHERE key = 'default_ref_limit';
UPDATE _config SET id = NEXTVAL('_s_config') WHERE key = 'extlink_bbref';
UPDATE _config SET id = NEXTVAL('_s_config') WHERE key = 'extlink_bktref';
UPDATE _config SET id = NEXTVAL('_s_config') WHERE key = 'extlink_boxrec';
UPDATE _config SET id = NEXTVAL('_s_config') WHERE key = 'extlink_ftref';
UPDATE _config SET id = NEXTVAL('_s_config') WHERE key = 'extlink_hkref';
UPDATE _config SET id = NEXTVAL('_s_config') WHERE key = 'extlink_lequipe';
UPDATE _config SET id = NEXTVAL('_s_config') WHERE key = 'extlink_olyref';
UPDATE _config SET id = NEXTVAL('_s_config') WHERE key = 'html_contribution1_en';
UPDATE _config SET id = NEXTVAL('_s_config') WHERE key = 'html_contribution1_fr';
UPDATE _config SET id = NEXTVAL('_s_config') WHERE key = 'html_contribution2_en';
UPDATE _config SET id = NEXTVAL('_s_config') WHERE key = 'html_contribution2_fr';
UPDATE _config SET id = NEXTVAL('_s_config') WHERE key = 'html_helpdates_en';
UPDATE _config SET id = NEXTVAL('_s_config') WHERE key = 'html_helpdates_fr';
UPDATE _config SET id = NEXTVAL('_s_config') WHERE key = 'html_helpevent_en';
UPDATE _config SET id = NEXTVAL('_s_config') WHERE key = 'html_helpevent_fr';
UPDATE _config SET id = NEXTVAL('_s_config') WHERE key = 'html_helpother_en';
UPDATE _config SET id = NEXTVAL('_s_config') WHERE key = 'html_helpother_fr';
UPDATE _config SET id = NEXTVAL('_s_config') WHERE key = 'html_helpphoto_en';
UPDATE _config SET id = NEXTVAL('_s_config') WHERE key = 'html_helpphoto_fr';
UPDATE _config SET id = NEXTVAL('_s_config') WHERE key = 'html_helpplaces_en';
UPDATE _config SET id = NEXTVAL('_s_config') WHERE key = 'html_helpplaces_fr';
UPDATE _config SET id = NEXTVAL('_s_config') WHERE key = 'html_helprankings_en';
UPDATE _config SET id = NEXTVAL('_s_config') WHERE key = 'html_helprankings_fr';
UPDATE _config SET id = NEXTVAL('_s_config') WHERE key = 'html_helprounds_en';
UPDATE _config SET id = NEXTVAL('_s_config') WHERE key = 'html_helprounds_fr';
UPDATE _config SET id = NEXTVAL('_s_config') WHERE key = 'max_contributor_sports';
UPDATE _config SET id = NEXTVAL('_s_config') WHERE key = 'max_photo_height';
UPDATE _config SET id = NEXTVAL('_s_config') WHERE key = 'max_photo_width';
UPDATE _config SET id = NEXTVAL('_s_config') WHERE key = 'robot_pattern';
UPDATE _config SET id = NEXTVAL('_s_config') WHERE key = 'USL_CHAMP_EVENTS';
UPDATE _config SET id = NEXTVAL('_s_config') WHERE key = 'USL_CHAMP_SUBEVENTS';
UPDATE _config SET id = NEXTVAL('_s_config') WHERE key = 'USL_STATS_EVENT_LABEL';

ALTER TABLE _config DROP CONSTRAINT "~Config_pkey";

ALTER TABLE _config ADD CONSTRAINT "~Config_pkey" PRIMARY KEY (id);

