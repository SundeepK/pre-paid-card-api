#!/bin/bash

cd "$( dirname "$0" )"

mkdir -p build/test-results
bundle install --path "${BUNDLE_PATH:-.gems}"
exec bundle exec cucumber --color --expand --strict --format json -o build/test-results/cucumber.json --format pretty --tags ~@wip "$@"
