-- COMP3311 20T3 Final Exam
-- Q4: function that takes two team names and
--     returns #matches they've played against each other

-- ... helper views and/or functions (if any) go here ...
create or replace function
	Q4(_team1 text, _team2 text) returns integer
as $$
declare
	matches integer;
begin
	select count(*) into matches
	from (
		(select m.id from teams t
		join involves i on (t.id = i.team)
		join matches m on (i.match = m.id)
		where t.country = _team1)
		intersect
		(select m.id from teams t
		join involves i on (t.id = i.team)
		join matches m on (i.match = m.id)
		where t.country = _team2)
	) as X;
	return matches;
end;
$$ language plpgsql
;
