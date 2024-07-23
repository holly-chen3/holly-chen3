-- COMP3311 22T3 Final Exam Q4
-- Function to return average winnings for horses matching a partial name

drop type if exists horse_winnings cascade;
create type horse_winnings as (horse text, average integer);

-- put helper views (if any) here
-- if the horse finished first in any race, grab the prize of that race
create or replace view horseprize(horse, prize) 
as 
select h.name, sum(c.prize) from horses h
join runners r on (r.horse = h.id)
join races c on (r.race = c.id)
where r.finished = 1 
group by h.name
;

-- number of races the horse has run in
create or replace view nraces(horse, nrace)
as 
select h.name, count(c.name)
from horses h 
join runners r on (r.horse = h.id)
join races c on (r.race = c.id)
group by h.name
;

create or replace view horsewins(horse, average)
as
select h.horse, h.prize/n.nrace from horseprize h
join nraces n on (h.horse = n.horse)
;
-- answer: Q4(part_name text) -> setof horse_winnings

create or replace function
    Q4(part_name text) returns setof horse_winnings
as $$
declare 
	rec horse_winnings;
begin 
	for rec in (select * from horsewins)
	loop
		if (rec.horse ~* part_name) then
			return next rec;
		end if;
	end loop;
end;
$$ language plpgsql;
