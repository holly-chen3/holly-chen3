-- COMP3311 22T3 Final Exam Q5
-- Helper views and functions to support q5
-- You must submit this, even if you don't change it
create or replace function prize(totalprize integer, finished integer) returns integer
as $$
begin
	if (finished = 1) then
		return 0.7*totalprize;
	elsif (finished = 2) then
		return 0.2*totalprize;
	elsif (finished = 3) then
		return 0.1*totalprize;
	else 
		return 0;
	end if;
end;
$$ language plpgsql;
