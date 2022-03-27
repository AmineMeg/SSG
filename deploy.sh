#!/bin/bash
curl --header 'Content-Type: application/json' --header "PRIVATE-TOKEN: 3AZgFN33XZQpQahwmzo4" \
     --data '{ "name": "SSG Déploiement v1.0.1", "tag_name": "v1.0.1", "description": "Hotfix: Suppression de dossiers généré par les tests", "ref": "master" }' \
     --request POST https://gaufre.informatique.univ-paris-diderot.fr/api/v4/projects/4208/releases
