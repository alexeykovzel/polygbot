#!/bin/bash
set -eo pipefail
gradle -q packageLibs
mv build/distributions/polygbot-1.0-SNAPSHOT.zip build/polygbot-lib.zip