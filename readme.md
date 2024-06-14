# todo

- create dev/prod with aero
- Dockerize
- CI/CD with github actions
- add tests

## notes

- standard
  - run with:
  - ``clojure -M:dev -m usermanager.web.core``

- standalone:
  - build with:
  - ``cd projects/usermanager && clojure -T:build uber``
  - run with:
  - ``java -jar projects/usermanager/target/usermanager-standalone.jar``
