CALL apoc.load.jsonParams("http://swapi.dev/api/planets",{Authorization:"Bearer "+$token, Accept: "application/json"},null) YIELD value
UNWIND value.results AS results
WITH results.name as name, results.population as population
create (planet:Planet {name:name, population:population})
