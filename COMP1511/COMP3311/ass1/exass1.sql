create or replace view q1 (beer, "sold in",alcohol)
as select 
b.name, 
b.volume::text ||'ml '|| b.sold_in::text,
(b.volume*b.abv/100)::numeric(18,1)::text ||'ml'
from beers b 
where b.rating > 9
;

create or replace function q2a(abv abvvalue, minabv abvvalue, maxabv abvvalue) returns text 
as $$
begin
	if (abv < minabv) then
		return 'too weak by ' || (minabv-abv)::numeric(18,1)::text || '%';
	elsif (abv > maxabv) then
		return 'too strong by ' || (abv-maxabv)::numeric(18,1)::text || '%';
	end if;
	return '';
end;
$$ language plpgsql;

create or replace view q2 (beer,style,abv,reason)
as select
b.name,
s.name,
b.abv,
q2a(b.abv, s.min_abv, s.max_abv)
from beers b join styles s on (s.id = b.style)
where b.abv < s.min_abv 
or b.abv > s.max_abv
;

