-- COMP3311 22T3 Final Exam Q3
-- List Gender/Age for all horses for all races of a specified type

drop type if exists race_horses cascade;
create type race_horses as (race text, horses text);

-- put helper views (if any) here
create or replace view racehorses(race, horse)
as
select r.name, h.gender::text || h.age::text 
from races r 
join runners n on (n.race = r.id)
join horses h on (n.horse = h.id)
order by r.name, h.gender, h.age
;

create or replace view racesh(race, horses)
as
select race, string_agg(horse, ',')
from racehorses 
group by race
;

-- answer: Q3(text) -> setof race_horses

create or replace function Q3(text) returns setof race_horses
as
$$
	select r.race, r.horses from racesh r where r.race like '%'||$1;
$$
language sql;
