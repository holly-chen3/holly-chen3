-- COMP3311 22T3 Final Exam Q1
-- Horse(s) that have won the most Group 1 races

-- put helper views (if any) here

-- answer: Q1(horse)

create or replace view group1winners(horse, race)
as
select h.name, ra.id from horses h 
join runners r on (h.id = r.horse)
join races ra on (r.race = ra.id)
where ra.level = 1
and r.finished = 1
;

create or replace view countwinners(horse, cnt)
as
select horse, count(race)
from group1winners
group by horse
;

create or replace view Q1(horse) 
as
select horse from countwinners
where cnt = (select max(cnt) from countwinners)
;
