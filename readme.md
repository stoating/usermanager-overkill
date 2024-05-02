# todo

- implement base project
- replace sql with xtdb
- replace qeries with XTQL
- create schema for xtdb with malli
- create dev/prod with aero
- replace compojure with reitit
- replace component with integrant
- replace selmer with hiccup

# notes

- standard
  - run with:
  - ``clojure -M:dev -m usermanager.web.core``

- standalone:
  - build with:
  - ``cd projects/usermanager && clojure -T:build uber``
  - run with:
  - ``java -jar projects/usermanager/target/usermanager-standalone.jar``
