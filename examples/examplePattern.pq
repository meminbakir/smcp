/*
The following pattern queries are provided for demonstration purpose. They do not represent any model species.
You can change replace the 'yourModelSpecies' placeholder with the species you want to verify. 
*/

with probability>0.5 eventually yourModelSpecies > 50 
with probability>0.5 always yourModelSpecies >=50 
with probability>0.5 next yourModelSpecies > 50 
with probability>=0.9 never yourModelSpecies >50
with probability >=1 INFINITELY-OFTEN yourModelSpecies >50
with probability>=0.9 steady-state yourModelSpecies >50
with probability >=1 yourModelSpecies >=50 until yourModelSpecies >50
with probability >=1 yourModelSpecies >=50 WEAK-UNTIL yourModelSpecies >=0
with probability >=1 yourModelSpecies >=50 release yourModelSpecies >50
with probability >=1 yourModelSpecies >=50 follows yourModelSpecies >50
with probability >=1 yourModelSpecies >=50 precedes yourModelSpecies >50