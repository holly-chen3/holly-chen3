-- COMP3311 22T3 Final Exam Q2
-- List of races with only Mares

-- put helper views (if any) here

-- answer: Q2(name,course,date)

create or replace view racewithgender(race, gender)
as
select c.id, h.gender from races c
join runners r on (r.race = c.id)
join horses h on (r.horse = h.id)
group by c.id, h.gender
;

create or replace view onegender(race)
as 
select race from racewithgender 
group by race
having count(*) = 1
;

create or replace view racemare(race)
as
select r.race from racewithgender r
join onegender o on (o.race = r.race)
where r.gender = 'M' 
;

create or replace view Q2(name,course,date)
as
select r.name, rc.name, m.run_on
from racemare rm
join races r on(r.id = rm.race)
join meetings m on (r.part_of = m.id)
join racecourses rc on (rc.id = m.run_at)
order by r.name, rc.name, m.run_on
;
