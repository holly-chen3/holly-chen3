-- COMP3311 20T3 Final Exam
-- Q3: team(s) with most players who have never scored a goal

create or replace view playersandgoals(team, player, ngoals)
as
select t.country, p.name, count(g.id)
from players p 
join teams t on (p.memberof = t.id)
left outer join goals g on (g.scoredby = p.id)
group by t.country, p.name
;

create or replace view nogoals(team, nplayers)
as
select team, count(player)
from playersandgoals
where ngoals = 0
group by team
;

create or replace view Q3(team,nplayers)
as
select team, nplayers from nogoals
where nplayers = (select max(nplayers) from nogoals);
;

