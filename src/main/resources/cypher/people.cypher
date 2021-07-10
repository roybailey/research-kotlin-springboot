CALL apoc.load.jsonParams("http://swapi.dev/api/people",{Authorization:"Bearer "+$token, Accept: "application/json"},null) YIELD value
UNWIND value.results AS results
WITH results.name as name, results.gender as gender
create (person:Person {name:name, gender:gender})
