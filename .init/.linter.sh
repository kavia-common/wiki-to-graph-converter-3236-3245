#!/bin/bash
cd /home/kavia/workspace/code-generation/wiki-to-graph-converter-3236-3245/wiki_to_neo4j_backend
./gradlew checkstyleMain
LINT_EXIT_CODE=$?
if [ $LINT_EXIT_CODE -ne 0 ]; then
   exit 1
fi

