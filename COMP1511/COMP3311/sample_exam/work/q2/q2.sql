-- COMP3311 20T3 Final Exam
-- Q2: view of amazing goal scorers

-- ... helpers go here ...
create or replace view Q2(player,ngoals)
as
select p.name, count(g.id) from players p 
join goals g on (g.scoredby = p.id)
where g.rating = 'amazing'
group by p.name
having count(g.id) > 1
;

