update result set comment = replace(comment, 'Rank: ', '')
where comment like '%Rank:%';
