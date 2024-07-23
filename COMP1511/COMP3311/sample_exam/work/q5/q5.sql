-- COMP3311 20T3 Final Exam
-- Q5: show "cards" awarded against a given team

-- ... helper views and/or functions go here ...

drop function if exists q5(text);
drop type if exists RedYellow;

create type RedYellow as (nreds integer, nyellows integer);

create or replace function
	Q5(_team text) returns RedYellow
as $$
declare
	ncards redyellow;
begin
	perform * from teams t 
	where t.country = _team;
	if (not found) then
		return (null,null);
	end if;

	select count(c.id) into ncards.nreds from players p 
	join teams t on (p.memberof = t.id)
	join cards c on (c.givento = p.id)
	where t.country = 'Australia'
	and c.cardtype = 'red'
	group by t.country;
	select count(c.id) into ncards.nyellows from players p 
	join teams t on (p.memberof = t.id)
	join cards c on (c.givento = p.id)
	where t.country = _team
	and c.cardtype = 'yellow'
	group by t.country;

	return ncards;
end;
$$ language plpgsql
;
