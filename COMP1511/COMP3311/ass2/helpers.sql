-- COMP3311 23T1 Ass2 ... SQL helper Views/Functions
-- Add any views or functions you need into this file
-- Note: it must load without error into a freshly created Movies database
-- Note: you must submit this file even if you add nothing to it

-- The `dbpop()` function is provided for you in the dump file
-- This is provided in case you accidentally delete it

DROP TYPE IF EXISTS Population_Record CASCADE;
CREATE TYPE Population_Record AS (
	Tablename Text,
	Ntuples   Integer
);

CREATE OR REPLACE FUNCTION DBpop() RETURNS SETOF Population_Record
AS $$
DECLARE
    rec Record;
    qry Text;
    res Population_Record;
    num Integer;
BEGIN
    FOR rec IN SELECT tablename FROM pg_tables WHERE schemaname='public' ORDER BY tablename LOOP
        qry := 'SELECT count(*) FROM ' || quote_ident(rec.tablename);

        execute qry INTO num;

        res.tablename := rec.tablename;
        res.ntuples   := num;

        RETURN NEXT res;
    END LOOP;
END;
$$ LANGUAGE plpgsql;

--
-- Example Views/Functions
-- These Views/Functions may or may not be useful to you.
-- You may modify or delete them as you see fit.
--

-- `Move_Learning_Info`
-- The `Learnable_Moves` table is a relation between Pokemon, Moves, Games and Requirements.
-- As it just consists of foreign keys, it is not very easy to read.
-- This view makes it easier to read by displaying the names of the Pokemon, Moves and Games instead of their IDs.
CREATE OR REPLACE VIEW Move_Learning_Info(Pokemon, Move, Game, Requirement) AS
SELECT
    P.Name,
    M.Name,
    G.Name,
    R.Assertion
FROM
    Learnable_Moves AS L
    JOIN
    Pokemon         AS P ON Learnt_By   = P.ID
    JOIN
    Games           AS G ON Learnt_In   = G.ID
    JOIN
    Moves           AS M ON Learns      = M.ID
    JOIN
    Requirements    AS R ON Learnt_When = R.ID
;

-- `Super_Effective`
-- This function takes a type name and
-- returns a set of all types that it is super effective against (multiplier > 100)
-- eg Water is super effective against Fire, so `Super_Effective('Water')` will return `Fire` (amongst others)
CREATE OR REPLACE FUNCTION Super_Effective(_Type Text) RETURNS SETOF Text
AS $$
SELECT
    B.Name
FROM
    Types              AS A
    JOIN
    Type_Effectiveness AS E ON A.ID = E.Attacking
    JOIN
    Types              AS B ON B.ID = E.Defending
WHERE
    A.Name = _Type
    AND
    E.Multiplier > 100
$$ LANGUAGE SQL;

--
-- Your Views/Functions Below Here
-- Remember This file must load into a clean Pokemon database in one pass without any error
-- NOTICEs are fine, but ERRORs are not
-- Views/Functions must be defined in the correct order (dependencies first)
-- eg if my_supper_clever_function() depends on my_other_function() then my_other_function() must be defined first
-- Your Views/Functions Below Here
--

CREATE OR REPLACE VIEW Density(density, game, location, region)
AS 
SELECT sum(((p.average_weight::float*1000::float)/((4::float/3::float) * pi() * 
((p.average_height::float*0.5*100::float)^3))) * (0.01*e.rarity))::numeric(100,4)
AS density, g.name, l.name, g.region
FROM pokemon p 
JOIN encounters e ON (e.occurs_with = p.id)
JOIN locations l ON (e.occurs_at = l.id)
JOIN games g ON (l.appears_in = g.id)
GROUP BY l.name, g.name, g.region
ORDER BY l.name
;

CREATE OR REPLACE VIEW Location_Density(density, location, region)
AS 
SELECT avg(d.density)::numeric(100,4), l.name, g.region 
FROM density d 
JOIN games g ON (g.name = d.game) 
JOIN locations l ON (l.name = d.location)
GROUP BY l.name, g.region
ORDER BY l.name
;

CREATE OR REPLACE FUNCTION Attack_Defend(category text, normal smallint, special smallint) RETURNS smallint
AS $$
BEGIN
	IF (category = 'Special') THEN
		RETURN special;
	ELSE 
		RETURN normal;
	END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION Stab(m_type integer, first_type integer, second_type integer) RETURNS float
AS $$
BEGIN 
	IF ((m_type = first_type) OR (m_type = second_type)) THEN
		RETURN 1.5;
	ELSE
		RETURN 1.0;
	END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION Type_Effectiveness(m_type integer, f_type integer, s_type integer) RETURNS float
AS $$ 
DECLARE 
	f Record;
	s Record;
BEGIN 
	SELECT tye.multiplier AS multiplier INTO f
	FROM type_effectiveness tye 
	JOIN moves m ON (m.of_type = tye.attacking)
	WHERE (f_type = tye.defending)
	AND (m_type = tye.attacking);
	
	SELECT tye.multiplier AS multiplier INTO s
	FROM type_effectiveness tye 
	JOIN moves m ON (m.of_type = tye.attacking)
	WHERE (s_type = tye.defending)
	AND (m_type = tye.attacking);
	
	IF (f IS NOT null AND s IS null) THEN
		RETURN 0.01*f.multiplier;
	END IF;
	
	IF (s IS NOT null AND f IS null) THEN
		RETURN 0.01*s.multiplier;
	END IF;
	
	IF (s IS NOT null AND f IS NOT null) THEN
		RETURN 0.01*f.multiplier*0.01*s.multiplier;
	END IF;
	
	RETURN 1;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION Damage(attacker_level int, pow statistic, attack smallint, 
defense smallint, random_factor numeric, stab float, t_effectiveness float) RETURNS integer
AS $$
BEGIN
	RETURN TRUNC(ROUND((((((((2::float * attacker_level::float) / 5) + 2) * pow::float * 
	(attack::float / defense::float)) / 50.0) + 2) * random_factor::float * stab::float * t_effectiveness)::Numeric, 1));
END; 
$$ LANGUAGE plpgsql;

CREATE OR REPLACE VIEW Attack_Moves(pow, attack, stab, game, category, m_type, moves, attacker)
AS
SELECT DISTINCT m.power, Attack_Defend(m.category::text, (p.base_stats).attack::smallint, (p.base_stats).special_attack::smallint), 
	Stab(m.of_type, p.first_type, p.second_type), 
	g.name, m.category, m.of_type, m.name, p.name
FROM pokemon p 
JOIN learnable_moves l ON (l.learnt_by = p.id)
JOIN moves m ON (m.id = l.learns)
JOIN games g ON (l.learnt_in = g.id) 
WHERE m.power IS NOT null
ORDER BY m.category
;

CREATE OR REPLACE VIEW Defend_Moves(defense, special_defense, first_type, second_type, game, defender)
AS
SELECT (p.base_stats).defense, 
(p.base_stats).special_defense,
p.first_type, p.second_type, g.name, p.name
FROM pokemon p 
JOIN pokedex po ON (po.national_id = p.id)
JOIN games g ON (g.id = po.game) 
;

CREATE OR REPLACE VIEW AD_Moves(min_damage, max_damage, move, attacker, defender, game)
AS SELECT DISTINCT Damage(1, a.pow, a.attack, Attack_Defend(a.category::text, d.defense::smallint, d.special_defense::smallint), 
0.85, a.stab::float, Type_Effectiveness(a.m_type, d.first_type, d.second_type)::float),
Damage(100, a.pow, a.attack, Attack_Defend(a.category::text, d.defense::smallint, d.special_defense::smallint), 1.00, a.stab::float, 
Type_Effectiveness(a.m_type, d.first_type, d.second_type)::float), a.moves, a.attacker, d.defender, a.game
FROM attack_moves a
JOIN defend_moves d ON (a.game = d.game)
WHERE (Type_Effectiveness(a.m_type, d.first_type, d.second_type)::float <> 0)
;
