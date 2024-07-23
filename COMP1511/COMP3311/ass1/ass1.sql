-- COMP3311 23T1 Assignment 1

-- Q1: amount of alcohol in the best beers

-- put any Q1 helper views/functions here
create or replace function
	alcohol_vol(v milliliters, abv abvvalue) returns text
as $$
declare 
	vol numeric(18,1);
begin 
	vol = v*abv/100 :: numeric(18,1);
	return vol::text || 'ml';
end;
$$ language plpgsql;

create or replace function
	sold(v milliliters, s containertype) returns text
as $$
begin 	
	return v::text || 'ml ' || s::text;
end;
$$ language plpgsql;

create or replace view Q1(beer, "sold in", alcohol)
as
select name, sold(volume, sold_in), alcohol_vol(volume, ABV)
from Beers
where rating > 9
;

-- Q2: beers that don't fit the ABV style guidelines

-- put any Q2 helper views/functions here
create or replace function
	reason(b_abv abvvalue, s_min abvvalue, s_max abvvalue) returns text
as $$
begin 
	if (b_abv > s_max) then
		return 'too strong by ' || (b_abv - s_max)::numeric(4,1) || '%';
	else
		return 'too weak by ' || (s_min - b_abv)::numeric(4,1) || '%';
	end if;
end; 
$$ language plpgsql;
	
create or replace view Q2(beer, style, abv, reason)
as
select b.name, s.name, b.abv::ABVvalue, reason(b.abv, s.min_abv, s.max_abv) 
from Beers b join Styles s on (b.style = s.id)
where b.abv > s.max_abv 
or b.abv < s.min_abv
;

-- Q3: Number of beers brewed in each country
-- put any Q3 helper views/functions here
create or replace function
	nbeersincountry(_country integer) returns integer
as $$
declare 
	beers record;
	nbeers integer := 0;
begin 
	for beers in 
		(select b.* 
		from beers b join brewed_by w on (w.beer = b.id)
		join breweries br on (br.id = w.brewery)
		join locations l on (l.id = br.located_in)
		where l.within = _country)
	loop 
		nbeers := nbeers + 1;
	end loop;
	return nbeers;
end;
$$ language plpgsql;
		

	
create or replace view Q3(country, "#beers")
as
select c.name, nbeersincountry(c.id)::bigint
from countries c 
;

-- Q4: Countries where the worst beers are brewed
	
-- put any Q4 helper views/functions here

create or replace view Q4(beer, brewery, country)
as
select b.name, br.name, c.name 
from beers b join brewed_by w on (w.beer = b.id)
join breweries br on (br.id = w.brewery)
join locations l on (l.id = br.located_in)
join countries c on (l.within = c.id)
where b.rating < 3
;

-- Q5: Beers that use ingredients from the Czech Republic

-- put any Q5 helper views/functions here

create or replace view Q5(beer, ingredient, "type")
as
select b.name, i.name, i.itype::IngredientType 
from beers b join contains co on (co.beer = b.id)
join ingredients i on (i.id = co.ingredient)
join countries c on (c.id = i.origin)
where c.name = 'Czech Republic'
;

-- Q6: Beers containing the most used hop and the most used grain

-- put any Q6 helper views/functions here

create or replace view hops(beer, ingredient, type)
as 
select b.id, i.name, i.itype
from beers b join contains c on (c.beer = b.id)
join ingredients i on (i.id = c.ingredient)
where i.itype = 'hop' 
;

create or replace view hpopularity(ingredient, ningredient, type)
as 
select ingredient, count(ingredient), type
from hops h 
group by ingredient, type
;

create or replace view grains(beer, ingredient, type)
as 
select b.id, i.name, i.itype
from beers b join contains c on (c.beer = b.id)
join ingredients i on (i.id = c.ingredient)
where i.itype = 'grain' 
;

create or replace view gpopularity(ingredient, ningredient, type)
as 
select ingredient, count(ingredient), type
from grains g
group by ingredient, type
;

create or replace view maxhops(ingredient, type)
as 
select ingredient, type 
from hpopularity
where ningredient = (select max(ningredient) from hpopularity)
;

create or replace view maxgrains(ingredient, type)
as
select ingredient, type 
from gpopularity 
where ningredient = (select max(ningredient) from gpopularity)
;

create or replace function 
	beerinclusion(_beer integer) returns integer
as $$
declare 
	hbeer integer;
	gbeer integer;
begin
	select h.beer into hbeer from hops h 
	join maxhops m on (m.ingredient = h.ingredient)
	where h.beer = _beer;
	
	if (not found) then
		return null;
	end if;
		
	select g.beer into gbeer from grains g 
	join maxgrains m on (m.ingredient = g.ingredient)
	where g.beer = _beer;
	
	if (not found) then 
		return null;
	end if;
	return _beer;
end;
$$ language plpgsql;

create or replace view Q6(beer)
as
select distinct b.name
from beers b join contains c on (b.id = c.beer)
join ingredients i on (c.ingredient = i.id)
where (beerinclusion(b.id) = b.id)
;

-- Q7: Breweries that make no beer

-- put any Q7 helper views/functions here
create or replace view makesbeer(brname)
as 
select br.name
from beers b join brewed_by w on (b.id = w.beer)
join breweries br on (br.id = w.brewery)
;

create or replace view Q7(brewery)
as
select br.name from breweries br
where not exists (
	select * from makesbeer m 
	where m.brname = br.name
);

-- Q8: Function to give "full name" of beer

-- put any Q8 helper views/functions here
create or replace view brewerybeers(beerid, brewery)
as
select b.id, br.name
from breweries br join brewed_by w on (w.brewery = br.id)
join beers b on (w.beer = b.id)
order by b.id
;

create or replace view regex(beerid, brewery)
as 
select b.beerid, regexp_replace(b.brewery, ' (Beer|Brew).*$', '')
from brewerybeers b
;

create or replace function
	Q8(beer_id integer) returns text
as $$
declare 
	beer record;
	name text := '';
	beername text;
begin
	for beer in 
		select b.* from regex b 
		where (b.beerid = beer_id)
	loop 
		if (name = '') then 
			name := beer.brewery;
		else 
			name := name || ' + ' || beer.brewery;
		end if;
		select b.name into beername 
		from beers b where (b.id = beer.beerid);
	end loop;
	if (name = '') then
		return 'No such beer';
	end if;
	name := name || ' ' || beername;
	return name;
end;
$$ language plpgsql
;

-- Q9: Beer data based on partial match of beer name

drop type if exists BeerData cascade;
create type BeerData as (beer text, brewer text, info text);


-- put any Q9 helper views/functions here
create or replace view brewbeer(beer, brewer)
as
select b.id, br.name
from beers b join brewed_by w on (b.id = w.beer)
join breweries br on (w.brewery = br.id)
;

create or replace view ingredbeer(beer, ingredient, type, beername)
as
select b.id, i.name, i.itype, b.name
from beers b join contains c on (b.id = c.beer)
join ingredients i on (c.ingredient = i.id)
;

create or replace view brewinfo(beer, brewery)
as
select beer, array_to_string(array_agg(brewer order by brewer),  ' + ' )
from brewbeer
group by beer
; 

create or replace function
    joining(s1 text, s2 text) returns text
as $$
begin
   if (s1 = '') then
      return s2;
   else
      return s1 || ',' || s2;
   end if;
end;
$$ language plpgsql;

create or replace function 
	ingredinfo(beerid integer) returns text
as $$
declare 
	beer record;
	hops text := '';
	grain text := '';
	extra text := '';
	info text := '';
begin
	for beer in 
		(select b.* from ingredbeer b 
		where (b.beer = beerid)
		order by b.ingredient)
	loop
		if (beer.type = 'hop') then
			hops := joining(hops, beer.ingredient);
		end if;
		if (beer.type = 'grain') then
			grain := joining(grain, beer.ingredient);
		end if;
		if (beer.type = 'adjunct') then
			extra := joining(extra, beer.ingredient);
		end if;
		
	end loop;
	if (hops <> '') then 
		hops := 'Hops: ' || hops || E'\n';
	end if;
	if (grain <> '') then 
		grain := 'Grain: ' || grain || E'\n';
	end if;
	if (extra <> '') then 
		extra := 'Extras: ' || extra || E'\n';
	end if;
	info := hops || grain || extra;
	if (info <> '') then
		select substring(info, 1, length(info) - 1) into info;
	end if;
	return info;
end;
$$ language plpgsql;


create or replace function
	Q9(partial_name text) returns setof BeerData
as $$
declare
	beer record;
	brew text;
	data BeerData;
begin 
	for beer in 
		select distinct b.* 
		from beers b 
		where (b.name ~* partial_name)
	loop 
		select brewery into brew
		from brewinfo 
		where beer.id = brewinfo.beer;
		data.beer := beer.name;
		data.brewer := brew;
		data.info := ingredinfo(beer.id);
		return next data;
	end loop;
end;
$$ language plpgsql
;

